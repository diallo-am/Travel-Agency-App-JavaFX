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

public class ManageClientsController {
    HBox hbox = new HBox();
    GridPane view = new GridPane();
    private GridPane mainView = new GridPane();
    private App mainApp;

    public ManageClientsController(App mainApp) {
        this.mainApp = mainApp;
        createView();
        displayAllClients();
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
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 4, 0);

        // Search bar
        Label searchLabel = new Label("Rechercher un utilisateur par nom d'utilisateur:");
        TextField searchField = new TextField();
        mainView.add(searchLabel, 0, 1, 2, 1);
        mainView.add(searchField, 2, 1);
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(event -> {
            handleSearch(searchField.getText());
            Button list = new Button("Liste complète");
            list.setOnAction(even -> {
                displayAllClients();
                mainView.getChildren().remove(list);
            });
            mainView.add(list, 3, 0);
                });
        mainView.add(searchButton, 3, 1);

        // Assistance section
        Label assistanceLabel = new Label("Besoin d'assistance ?");
        mainView.add(assistanceLabel, 4, 1);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 5, 1);

        // Buttons "Accueil" and "Retour"
        Button homeButton = new Button("Se déconnecter");
        homeButton.setOnAction(event -> mainApp.showAdminLoginView());
        mainView.add(homeButton, 5, 0);

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

    private void displayAllClients() {
        mainView.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 1);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM utilisateur";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int row = 3;
            while (resultSet.next()) {
                String lastName = resultSet.getString("nom");
                String firstName = resultSet.getString("prenom");
                String dob = resultSet.getString("date_naissance");
                String phone = resultSet.getString("telephone");
                String username = resultSet.getString("nom_utilisateur");
                String password = resultSet.getString("mot_de_passe");

                addClientToView(lastName, firstName, dob, phone, username, password, row);
                row += 3;
            }
        } catch (SQLException e) {
            showError("Erreur lors de l'affichage des clients : " + e.getMessage());
        }
    }

    private void addClientToView(String lastName, String firstName, String dob, String phone, String username, String password, int row) {
        TextField lastNameField = new TextField(lastName);
        TextField firstNameField = new TextField(firstName);
        TextField dobField = new TextField(dob);
        TextField phoneField = new TextField(phone);
        TextField usernameField = new TextField(username);
        TextField passwordField = new TextField(password);

        mainView.add(new Label("Nom:"), 0, row);
        mainView.add(lastNameField, 0, row + 1);
        mainView.add(new Label("Prénom:"), 1, row);
        mainView.add(firstNameField, 1, row + 1);
        mainView.add(new Label("Date de naissance:"), 2, row);
        mainView.add(dobField, 2, row + 1);
        mainView.add(new Label("Téléphone:"), 3, row);
        mainView.add(phoneField, 3, row + 1);
        mainView.add(new Label("Nom d'utilisateur:"), 4, row);
        mainView.add(usernameField, 4, row + 1);
        mainView.add(new Label("Mot de passe:"), 5, row);
        mainView.add(passwordField, 5, row + 1);

        Button editButton = new Button("Modifier");
        editButton.setOnAction(event -> {
            handleEdit(username, lastNameField.getText(), firstNameField.getText(), dobField.getText(), phoneField.getText(), passwordField.getText());
            displayAllClients();
        });

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> {
            handleDelete(username, row);
            displayAllClients();
        });

        mainView.add(editButton, 4, row + 2);
        mainView.add(deleteButton, 5, row + 2);
    }

    private void handleSearch(String username) {
        mainView.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 1);
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM utilisateur WHERE nom_utilisateur = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            int row = 3;
            while (resultSet.next()) {
                String lastName = resultSet.getString("nom");
                String firstName = resultSet.getString("prenom");
                String dob = resultSet.getString("date_naissance");
                String phone = resultSet.getString("telephone");
                String password = resultSet.getString("mot_de_passe");

                addClientToView(lastName, firstName, dob, phone, username, password, row);
                row += 3;
            }
        } catch (SQLException e) {
            showError("Erreur lors de la recherche du client : " + e.getMessage());
        }
    }

    private void handleEdit(String username, String lastName, String firstName, String dob, String phone, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "UPDATE utilisateur SET nom = ?, prenom = ?, date_naissance = ?, telephone = ?, mot_de_passe = ? WHERE nom_utilisateur = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lastName);
            statement.setString(2, firstName);
            statement.setString(3, dob);
            statement.setString(4, phone);
            statement.setString(5, password);
            statement.setString(6, username);
            statement.executeUpdate();

            showSuccess("Les informations de l'utilisateur ont été mises à jour avec succès.");
        } catch (SQLException e) {
            showError("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

        private void handleDelete(String username, int row) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "DELETE FROM utilisateur WHERE nom_utilisateur = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.executeUpdate();

            showSuccess("L'utilisateur a été supprimé avec succès.");
            mainView.getChildren().removeIf(node -> {
                Integer nodeRow = GridPane.getRowIndex(node);
                return nodeRow != null && nodeRow >= row && nodeRow < row + 3;
            });
        } catch (SQLException e) {
            showError("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
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
