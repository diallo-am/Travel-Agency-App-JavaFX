/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agenceavion.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import com.mycompany.agenceavion.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class AdminLoginController {
    HBox hbox = new HBox();
    GridPane view = new GridPane();
    private GridPane mainView = new GridPane();
    private TextField usernameField;
    private PasswordField passwordField;
    private App mainApp;

    public AdminLoginController(App mainApp) {
        this.mainApp = mainApp;
        createView();
    }

    private void createView() {
        
        hbox.setAlignment(Pos.CENTER);
        mainView.setHgap(10);
        mainView.setVgap(10);
        mainView.setPadding(new Insets(25, 25, 25, 25));
        mainView.setId("mainView");
        
        Image logo = new Image(getClass().getResource("/Images/agencelogo.png").toExternalForm());
        ImageView logoVue = new ImageView(logo);
        logoVue.setFitWidth(150);
        logoVue.setFitHeight(120);
        view.add(logoVue,0,0);
        
        hbox.getChildren().add(view);
        GridPane.setHalignment(logoVue,HPos.CENTER);
        
        // Création de l'arrière-plan flou
        Region background = new Region();
        background.setStyle("-fx-background-color: rgba(135, 206, 250, 0.5);"); // Semi-transparent
        background.setEffect(new GaussianBlur(10)); // Effet de flou

        // Lier les dimensions de l'arrière-plan à celles du GridPane
        background.prefWidthProperty().bind(mainView.widthProperty());
        background.prefHeightProperty().bind(mainView.heightProperty());

        // Superposition des couches (arrière-plan + contenu)
        StackPane root = new StackPane();
        root.getChildren().addAll(background, mainView);
        view.add(root,0,1);
        

        // Header section with logo and language button
        
        
        // espace user
        Button userSpaceButton = new Button("Espace utilisateur");
        userSpaceButton.setOnAction(event -> mainApp.showLoginView());
        mainView.add(userSpaceButton, 4, 0);

        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 3, 0);

        // Admin login form
        Label adminLabel = new Label("Etes-vous un administrateur ?");
        mainView.add(adminLabel, 1, 1, 2, 1);

        Label loginLabel = new Label("Je m'identifie");
        mainView.add(loginLabel, 1, 2, 2, 1);

        Label usernameLabel = new Label("Nom d'utilisateur :");
        usernameField = new TextField();
        mainView.add(usernameLabel, 0, 3);
        mainView.add(usernameField, 1, 3, 2, 1);

        Label passwordLabel = new Label("Mot de passe :");
        passwordField = new PasswordField();
        mainView.add(passwordLabel, 0, 4);
        mainView.add(passwordField, 1, 4, 2, 1);
        
        Label forgotPasswordLabel = new Label("Mot de passe oublié ?");
        forgotPasswordLabel.setStyle("-fx-text-fill: #0078d7; -fx-underline: true; -fx-cursor: hand;");
        forgotPasswordLabel.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Réinitialisation de mot de passe");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez contacter l'administration pour réinitialiser votre mot de passe.");
            alert.showAndWait();
        });
        mainView.add(forgotPasswordLabel, 1,5,2,1);

        Button loginButton = new Button("Se connecter");
        loginButton.setOnAction(event -> handleLogin());
        mainView.add(loginButton, 1, 6, 2, 1);

        Button createAdminButton = new Button("Créer un compte admin");
        createAdminButton.setOnAction(event -> mainApp.showAdminRegistrationView());
        mainView.add(createAdminButton, 1, 7, 2, 1);

        // Assistance section
        Label assistanceLabel = new Label("Besoin d'assistance ?");
        mainView.add(assistanceLabel, 4, 8);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 4, 9);
    }

    public HBox getView() {
        return hbox;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (authenticateAdmin(username, password)) {
            mainApp.showAdminDashboardView(username);
        } else {
            showError("Login échoué. Veuillez réessayer.");
        }
    }

    private boolean authenticateAdmin(String username, String password) {
        // Logique d'authentification pour les admins
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM admin WHERE nom_utilisateur = ? AND mot_de_passe = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Retourne true si un admin est trouvé, sinon false
        } catch (SQLException e) {
            showError("Erreur lors de l'authentification : " + e.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


