package com.healthdesk.controller;

import com.healthdesk.service.BdDrugMapper;
import com.healthdesk.service.DrugInfoService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DrugInfoController {

    @FXML private TextField         tfDrugName;
    @FXML private ProgressIndicator searchProgress;
    @FXML private Label             lblStatus;
    @FXML private Label             lblMappingNote;  // "Ace -> searched as Acetaminophen"
    @FXML private Label             lblSource;       // "OpenFDA" or "RxNav (NLM)"

    @FXML private Label    lblBrandName;
    @FXML private Label    lblManufacturer;
    @FXML private TextArea taIndications;
    @FXML private TextArea taDosage;
    @FXML private TextArea taSideEffects;
    @FXML private TextArea taWarnings;

    private final DrugInfoService service = new DrugInfoService();

    @FXML
    public void initialize() {
        tfDrugName.setOnAction(e -> handleSearch());
        hideMappingNote();
        lblSource.setText("");
        clearResults();
    }

    // -----------------------------------------------------------------------
    // Search — runs the three-step lookup on a background thread
    // -----------------------------------------------------------------------

    @FXML
    private void handleSearch() {
        String userInput = tfDrugName.getText() == null ? "" : tfDrugName.getText().trim();
        if (userInput.isEmpty()) {
            setStatus("Please enter a drug name to search.", false);
            return;
        }

        // Check BD mapping before launching thread — so we can show it immediately
        boolean willMap  = BdDrugMapper.isBdBrand(userInput);
        String  mappedTo = willMap ? BdDrugMapper.resolve(userInput) : null;

        // Prepare UI for search
        searchProgress.setVisible(true);
        searchProgress.setManaged(true);
        hideMappingNote();
        lblSource.setText("");
        clearResults();

        String statusMsg = willMap
                ? "\"" + userInput + "\" → mapped to \"" + mappedTo + "\" — contacting OpenFDA..."
                : "Searching for \"" + userInput + "\"...";
        setStatus(statusMsg, false);

        // Week 4: background Task — HTTP calls happen off the UI thread
        Task<DrugInfoService.DrugInfo> task = new Task<>() {
            @Override
            protected DrugInfoService.DrugInfo call() {
                return service.search(userInput);
            }
        };

        // Runs on UI thread after task finishes — safe to update controls here
        task.setOnSucceeded(e -> {
            searchProgress.setVisible(false);
            searchProgress.setManaged(false);

            DrugInfoService.DrugInfo info = task.getValue();

            if (info == null) {
                setStatus("No results found for \"" + userInput + "\". "
                                + "Try the international generic name (e.g. Paracetamol, Amoxicillin, Omeprazole, Ibuprofen).",
                        true);
                return;
            }

            // Show mapping note in green if a BD brand was resolved
            if (willMap && mappedTo != null) {
                lblMappingNote.setText("🔄  \"" + userInput + "\"  →  searched as  \"" + info.resolvedName + "\"");
                lblMappingNote.setVisible(true);
                lblMappingNote.setManaged(true);
            }

            lblSource.setText("Source: " + info.source);
            setStatus("Found via " + info.source + ".", false);
            populateResults(info);
        });

        // Runs on UI thread if task throws — safe to update controls here
        task.setOnFailed(e -> {
            searchProgress.setVisible(false);
            searchProgress.setManaged(false);
            setStatus("Network error — check your internet connection and try again.", true);
        });

        Thread bgThread = new Thread(task, "healthdesk-drug-search");
        bgThread.setDaemon(true);
        bgThread.start();
    }

    // -----------------------------------------------------------------------
    // Clear
    // -----------------------------------------------------------------------

    @FXML
    private void handleClear() {
        tfDrugName.clear();
        setStatus("Enter a drug name above and click Search.", false);
        hideMappingNote();
        lblSource.setText("");
        clearResults();
        tfDrugName.requestFocus();
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void populateResults(DrugInfoService.DrugInfo info) {
        lblBrandName.setText(info.brandName);
        lblManufacturer.setText(info.manufacturer);
        taIndications.setText(info.indications);
        taDosage.setText(info.dosage);
        taSideEffects.setText(info.sideEffects);
        taWarnings.setText(info.warnings);
    }

    private void clearResults() {
        lblBrandName.setText("-");
        lblManufacturer.setText("-");
        taIndications.setText("");
        taDosage.setText("");
        taSideEffects.setText("");
        taWarnings.setText("");
    }

    private void setStatus(String message, boolean isError) {
        lblStatus.setText(message);
        lblStatus.setStyle(isError
                ? "-fx-text-fill: #E74C3C; -fx-font-size: 12px;"
                : "-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");
    }

    private void hideMappingNote() {
        lblMappingNote.setVisible(false);
        lblMappingNote.setManaged(false);
    }
}
