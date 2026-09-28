package com.healthdesk;

import com.healthdesk.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Initialize DB + schema on first run
        DatabaseConnection.getConnection();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 660);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());

        stage.setTitle("HealthDesk - Hospital Management System");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(660);
        stage.show();

        stage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
    }

    public static void setRoot(Parent root, String title) {
        primaryStage.getScene().setRoot(root);
        if (title != null) {
            primaryStage.setTitle(title);
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
