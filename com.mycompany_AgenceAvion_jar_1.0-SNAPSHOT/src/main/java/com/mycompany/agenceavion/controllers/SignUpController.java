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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import com.mycompany.agenceavion.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SignUpController {
    HBox hbox = new HBox();
    GridPane view = new GridPane();
    private GridPane mainView = new GridPane();
    private TextField lastNameField;
    private TextField firstNameField;
    private DatePicker dobField;
    private TextField phoneField;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private App mainApp;

    public SignUpController(App mainApp) {
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
        view.add(mainView,0,1);
        hbox.getChildren().add(view);
        GridPane.setHalignment(logoVue,HPos.CENTER);
        

        // Langues et Retour à l'accueil
        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 3, 0);
        
        // Titre de l'inscription
        Label signUpLabel = new Label("Je m'inscris");
        mainView.add(signUpLabel, 1, 1, 2, 1);

        // Infos personnelles
        Label personalInfoLabel = new Label("infos personnelles");
        mainView.add(personalInfoLabel, 0, 2, 2, 1);

        Label lastNameLabel = new Label("Nom:");
        lastNameField = new TextField();
        mainView.add(lastNameLabel, 0, 3);
        mainView.add(lastNameField, 1, 3);

        Label firstNameLabel = new Label("Prénom:");
        firstNameField = new TextField();
        mainView.add(firstNameLabel, 0, 4);
        mainView.add(firstNameField, 1, 4);

        Label dobLabel = new Label("Date de naissance:");
        dobField = new DatePicker();
        mainView.add(dobLabel, 2, 3);
        mainView.add(dobField, 3, 3);

        Label phoneLabel = new Label("Téléphone:");
        phoneField = new TextField();
        mainView.add(phoneLabel, 2, 4);
        mainView.add(phoneField, 3, 4);

        // Infos de connexion
        Label loginInfoLabel = new Label("infos de connexion");
        mainView.add(loginInfoLabel, 0, 5, 2, 1);

        Label usernameLabel = new Label("Choisissez un nom d'utilisateur:");
        usernameField = new TextField();
        mainView.add(usernameLabel, 0, 6);
        mainView.add(usernameField, 1, 6);

        Label passwordLabel = new Label("Mot de passe:");
        passwordField = new PasswordField();
        mainView.add(passwordLabel, 0, 7);
        mainView.add(passwordField, 1, 7);

        Label confirmPasswordLabel = new Label("Confirmer le mot de passe:");
        confirmPasswordField = new PasswordField();
        mainView.add(confirmPasswordLabel, 2, 7);
        mainView.add(confirmPasswordField, 3, 7);

        // Bouton S'inscrire
        Button signUpButton = new Button("S'inscrire");
        signUpButton.setOnAction(event -> handleSignUp());
        mainView.add(signUpButton, 1, 8);

        // Besoin d'assistance?
        Label assistanceLabel = new Label("Besoin d'assistance?");
        mainView.add(assistanceLabel, 3, 9);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 3, 10);
        
        Button backButton = new Button("Retour");
        backButton.setOnAction(event -> mainApp.showLoginView());
        mainView.add(backButton, 0, 0);
    }

    public HBox getView() {
        return hbox;
    }

    private void handleSignUp() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String dob;
        if (dobField.getValue() != null) {
            dob = dobField.getValue().toString();
        }
        else
            dob="";
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            showError("Les mots de passe ne correspondent pas.");
            return;
        }

        // Logique pour inscrire un nouvel utilisateur dans la base de données
        if (!"".equals(lastName) && !"".equals(firstName) && !"".equals(dob) && !"".equals(phone) && !"".equals(username) && !"".equals(password) && !"".equals(confirmPassword)) { 
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
                String query = "INSERT INTO utilisateur (nom, prenom, date_naissance, telephone, nom_utilisateur, mot_de_passe) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, lastName);
                statement.setString(2, firstName);
                statement.setString(3, dob);
                statement.setString(4, phone);
                statement.setString(5, username);
                statement.setString(6, password);
                statement.executeUpdate();
                mainApp.showLoginView();
            } catch (SQLException e) {
                showError("Erreur lors de l'inscription : " + e.getMessage());
            }
        }
        else
            showError("Des champs sont vides, veuillez les remplir !");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



