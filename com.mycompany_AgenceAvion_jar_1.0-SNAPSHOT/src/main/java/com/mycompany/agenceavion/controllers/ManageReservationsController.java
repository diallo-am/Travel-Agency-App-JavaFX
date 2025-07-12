package com.mycompany.agenceavion.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.mycompany.agenceavion.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ManageReservationsController {
    HBox hbox = new HBox();
    GridPane view = new GridPane();
    private GridPane mainView = new GridPane();
    private App mainApp;

    public ManageReservationsController(App mainApp) {
        this.mainApp = mainApp;
        createView();
        displayAllReservations();
    }

    private void createView() {
        hbox.setAlignment(Pos.CENTER);
        mainView.setHgap(10);
        mainView.setVgap(10);
        mainView.setPadding(new Insets(25, 25, 25, 25));
        
        Image logo = new Image(getClass().getResource("/Images/agencelogo.png").toExternalForm());
        ImageView logoVue = new ImageView(logo);
        logoVue.setFitWidth(150);
        logoVue.setFitHeight(120);
        view.add(logoVue, 0, 0);
        view.add(mainView, 0, 1);
        hbox.getChildren().add(view);
        GridPane.setHalignment(logoVue, HPos.CENTER);

        // Header section with language button
        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(150);
        mainView.add(languagesButton, 6, 0);

        // Search bar
        Label searchLabel = new Label("Rechercher une réservation par nom d'utilisateur:");
        TextField searchField = new TextField();
        mainView.add(searchLabel, 0, 1, 2, 1);
        mainView.add(searchField, 2, 1);
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(event -> {
            handleSearch(searchField.getText());
            Button list = new Button("Liste complète");
            list.setOnAction(even -> {
                displayAllReservations();
                mainView.getChildren().remove(list);
            });
            mainView.add(list, 3, 0);
                });
        mainView.add(searchButton, 3, 1);

        // Reservation information section
        Label reservationInfoLabel = new Label("Détails des réservations");
        mainView.add(reservationInfoLabel, 0, 2, 2, 1);

        // Assistance section
        Label assistanceLabel = new Label("Besoin d'assistance ?");
        mainView.add(assistanceLabel, 6, 1);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 7, 1);

        // Buttons "Accueil" and "Retour"
        Button homeButton = new Button("Se déconnecter");
        homeButton.setOnAction(event -> mainApp.showAdminLoginView());
        mainView.add(homeButton, 7, 0);

        Button backButton = new Button("Retour");
        backButton.setOnAction(event -> mainApp.showAdminDashboardView("Admin"));
        mainView.add(backButton, 0, 0);

        // Add ScrollPane for mainView
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        hbox.getChildren().clear();
        hbox.getChildren().add(scrollPane);
    }

    public HBox getView() {
        return hbox;
    }

    private void displayAllReservations() {
        mainView.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 1);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM reservation_confirmation";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int row = 3;
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String name = resultSet.getString("nom");
                String phone = resultSet.getString("telephone");
                String reservationId = resultSet.getString("id");

                addReservationToView(date, name, phone, reservationId, row);
                row += 3;
            }
        } catch (SQLException e) {
            showError("Erreur lors de l'affichage des réservations : " + e.getMessage());
        }
    }

    private void addReservationToView(String date, String name, String phone, String reservationId, int row) {
        TextField dateField = new TextField(date);
        TextField nameField = new TextField(name);
        TextField phoneField = new TextField(phone);
        TextField reservationIdField = new TextField(reservationId);

        mainView.add(new Label("Date:"), 0, row);
        mainView.add(dateField, 0, row + 1);
        mainView.add(new Label("Nom complet:"), 1, row);
        mainView.add(nameField, 1, row + 1);
        mainView.add(new Label("Téléphone:"), 2, row);
        mainView.add(phoneField, 2, row + 1);
        mainView.add(new Label("ID de réservation:"), 3, row);
        mainView.add(reservationIdField, 3, row + 1);

        Button confirmButton = new Button("Confirmer");
        confirmButton.setOnAction(event -> {
            handleConfirm(dateField.getText(), nameField.getText(), phoneField.getText(), reservationIdField.getText());
            displayAllReservations();
        });

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> {
            handleDelete(reservationId, row);
            displayAllReservations();
        });

        mainView.add(confirmButton, 6, row + 1);
        mainView.add(deleteButton, 7, row + 1);
    }

    private void handleSearch(String username) {
        mainView.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 1);
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM reservation_confirmation WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            int row = 3;
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String name = resultSet.getString("nom");
                String phone = resultSet.getString("telephone");
                String reservationId = resultSet.getString("id");

                addReservationToView(date, name, phone, reservationId, row);
                row += 3;
            }
        } catch (SQLException e) {
            showError("Erreur lors de la recherche de la réservation : " + e.getMessage());
        }
    }

    private void handleConfirm(String date, String name, String phone, String reservationId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "UPDATE reservation_confirmation SET date = ?, nom = ?, telephone = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, date);
            statement.setString(2, name);
            statement.setString(3, phone);
            statement.setString(4, reservationId);
            statement.executeUpdate();

            showSuccess("La réservation a été confirmée avec succès.");
        } catch (SQLException e) {
            showError("Erreur lors de la confirmation de la réservation : " + e.getMessage());
        }
    }

    private void handleDelete(String reservationId, int row) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "DELETE FROM reservation_confirmation WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, reservationId);
            statement.executeUpdate();

            showSuccess("La réservation a été supprimée avec succès.");
            mainView.getChildren().removeIf(node -> {
                Integer nodeRow = GridPane.getRowIndex(node);
                return nodeRow != null && nodeRow >= row && nodeRow < row + 3;
            });
        } catch (SQLException e) {
            showError("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

        private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
