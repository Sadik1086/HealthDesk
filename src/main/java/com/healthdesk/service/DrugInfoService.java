package com.healthdesk.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DrugInfoService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static class DrugInfo {
        public final String brandName;
        public final String manufacturer;
        public final String indications;
        public final String dosage;
        public final String sideEffects;
        public final String warnings;
        public final String resolvedName;
        public final String source;

        public DrugInfo(String brandName, String manufacturer,
                        String indications, String dosage,
                        String sideEffects, String warnings,
                        String resolvedName, String source) {
            this.brandName    = brandName;
            this.manufacturer = manufacturer;
            this.indications  = indications;
            this.dosage       = dosage;
            this.sideEffects  = sideEffects;
            this.warnings     = warnings;
            this.resolvedName = resolvedName;
            this.source       = source;
        }
    }

    public DrugInfo search(String userInput) {
        if (userInput == null || userInput.isBlank()) return null;

        String term = userInput.trim().toLowerCase();

        if (term.contains("napa") || term.contains("ace") || term.contains("acetaminophen") || term.contains("paracetamol")) {
            return new DrugInfo(
                    "Napa / Paracetamol (500mg)",
                    "Beximco Pharmaceuticals Ltd.",
                    "• Fever and mild to moderate pain\n• Headache, toothache, body ache & cold symptoms",
                    "• Adults: 1-2 tablets every 4-6 hours (Max: 8 tablets/day)\n• Children (6-12 yrs): 1/2 to 1 tablet 3-4 times daily",
                    "• Nausea or mild skin rash (rare)\n• Liver damage only if over-dosed (>4000mg/day)",
                    "• Do not take with other Paracetamol products\n• Consult doctor if fever lasts >3 days",
                    "Acetaminophen / Paracetamol",
                    "Clinical Verified Data"
            );
        }

        if (term.contains("seclo") || term.contains("omeprazole")) {
            return new DrugInfo(
                    "Seclo / Omeprazole (20mg)",
                    "Square Pharmaceuticals Ltd.",
                    "• Gastric ulcer, acidity, GERD, and heartburn relief",
                    "• 1 capsule (20mg) daily before meal (preferably in the morning)",
                    "• Headache, diarrhea, abdominal pain or flatulence",
                    "• Do not crush or chew the capsule; swallow whole",
                    "Omeprazole",
                    "Clinical Verified Data"
            );
        }

        return fetchFromApi(term);
    }

    private DrugInfo fetchFromApi(String term) {
        try {
            String enc = URLEncoder.encode(term, StandardCharsets.UTF_8);
            URI uri = new URI("https", "api.fda.gov", "/drug/label.json",
                    "search=openfda.generic_name:%22" + enc + "%22+AND+openfda.product_type:%22HUMAN+OTC+DRUG%22&limit=5", null);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofSeconds(8)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) return null;

            JSONObject json = new JSONObject(response.body());
            if (!json.has("results")) return null;

            JSONArray results = json.getJSONArray("results");
            JSONObject drug = null;

            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                String brand = extractOpenFda(obj, "brand_name").toLowerCase();
                if (!brand.contains("sunscreen") && !brand.contains("foundation") && !brand.contains("makeup")) {
                    drug = obj;
                    break;
                }
            }

            if (drug == null) return null;

            String brand = extractOpenFda(drug, "brand_name");
            String generic = extractOpenFda(drug, "generic_name");
            String uses = cleanText(extractField(drug, "indications_and_usage"));
            String dosage = cleanText(extractField(drug, "dosage_and_administration"));
            String sideEffects = cleanText(extractField(drug, "adverse_reactions"));

            return new DrugInfo(
                    "N/A".equals(brand) ? generic : brand,
                    extractOpenFda(drug, "manufacturer_name"),
                    uses, dosage, sideEffects, "Use as directed by physician.", term, "OpenFDA"
            );
        } catch (Exception e) {
            return null;
        }
    }

    private String cleanText(String text) {
        if ("N/A".equals(text) || text.isBlank()) return "Consult doctor/pharmacist for instructions.";
        if (text.length() > 250) return text.substring(0, 250) + "...";
        return text;
    }

    private String extractOpenFda(JSONObject drug, String field) {
        try {
            JSONObject openfda = drug.optJSONObject("openfda");
            if (openfda == null) return "N/A";
            JSONArray arr = openfda.optJSONArray(field);
            return (arr != null && !arr.isEmpty()) ? arr.getString(0) : "N/A";
        } catch (Exception e) { return "N/A"; }
    }

    private String extractField(JSONObject drug, String field) {
        try {
            JSONArray arr = drug.optJSONArray(field);
            return (arr != null && !arr.isEmpty()) ? arr.getString(0).trim() : "N/A";
        } catch (Exception e) { return "N/A"; }
    }
}