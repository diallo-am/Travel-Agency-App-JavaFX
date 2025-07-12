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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import com.mycompany.agenceavion.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WelcomeController {
    HBox hbox = new HBox();
    GridPane View = new GridPane();
    private GridPane mainView = new GridPane();
    private TextField departureStationField;
    private TextField arrivalStationField;
    private DatePicker departureDateField;
    private DatePicker returnDateField;
    private App mainApp;
    

    public WelcomeController(App mainApp, String username, String monCompte) {
        this.mainApp = mainApp;
        createView(username, monCompte);
    }

    private void createView(String username, String monCompte) {
        
        hbox.setAlignment(Pos.CENTER);
        mainView.setHgap(10);
        mainView.setVgap(20);
        mainView.setPadding(new Insets(25, 25, 25, 25));
        mainView.setId("mainView");
        
        
        Image logo = new Image(getClass().getResource("/Images/agencelogo.png").toExternalForm());
        ImageView logoVue = new ImageView(logo);
        logoVue.setFitWidth(150);
        logoVue.setFitHeight(120);
        View.add(logoVue,0,0);
        View.add(mainView,0,1);
        hbox.getChildren().add(View);
        GridPane.setHalignment(logoVue,HPos.CENTER);
        
        
        

        // Header section with buttons
        Button accountButton = new Button("Mon compte");
        accountButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mon compte");
            alert.setHeaderText(null);
            alert.setContentText(monCompte);
            alert.showAndWait();
        });
        mainView.add(accountButton, 5, 0);

        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 4, 0);

        

        // Welcome message
        Label welcomeLabel = new Label("Bienvenue, " + username + "!");
        mainView.add(welcomeLabel, 1, 1, 2, 1);
        
        Label researchLabel = new Label("RECHERCHER UN TRAJET");
        mainView.add(researchLabel, 0, 2, 2, 1);

        // Search journey section
        Label departureStationLabel = new Label("GARE DÉPART:");
        ComboBox<String> choixVilleDepart = new ComboBox<>();
        choixVilleDepart.getItems().addAll("Tetouan", "Tanger", "Rabat", "Casablanca","Ouagadougou");
        choixVilleDepart.setPrefWidth(10);
        departureStationField = new TextField();
        departureStationField.setPromptText("Choisissez une option");
        choixVilleDepart.setOnAction(event -> {
            String selectedValue = choixVilleDepart.getValue();
            if (selectedValue != null) {
                departureStationField.setText(selectedValue);
            }
        });
        HBox departureHBox = new HBox(10, departureStationField, choixVilleDepart);
        mainView.add(departureStationLabel, 0, 3);
        mainView.add(departureHBox, 1, 3, 2, 1);

        Label arrivalStationLabel = new Label("GARE ARRIVÉE:");
        ComboBox<String> choixVilleArrive = new ComboBox<>();
        choixVilleArrive.getItems().addAll("Tetouan", "Tanger", "Rabat", "Casablanca","Ouagadougou");
        choixVilleArrive.setPrefWidth(10);
        arrivalStationField = new TextField();
        arrivalStationField.setPromptText("Choisissez une option");
        choixVilleArrive.setOnAction(event -> {
            String selectedValue = choixVilleArrive.getValue();
            if (selectedValue != null) {
                arrivalStationField.setText(selectedValue);
            }
        });
        HBox arrivalHBox = new HBox(10, arrivalStationField, choixVilleArrive);
        mainView.add(arrivalStationLabel, 0, 4);
        mainView.add(arrivalHBox, 1, 4, 2, 1);

        Label departureDateLabel = new Label("DATE DÉPART:");
        departureDateField = new DatePicker();
        departureDateField.setPrefWidth(120);
        HBox departureDateHBox = new HBox(10, departureDateLabel, departureDateField);
        mainView.add(departureDateHBox, 3, 3);
        

        Label returnDateLabel = new Label("DATE RETOUR:");
        returnDateField = new DatePicker();
        returnDateField.setPrefWidth(120);
        HBox returnDateHBox = new HBox(10, returnDateLabel, returnDateField);
        mainView.add(returnDateHBox, 3, 4);

        // Buttons for filter, search, and contact
       
        ComboBox<String> filterField = new ComboBox<>();
        filterField.getItems().addAll("1ère Classe", "2ème Classe", "Aller-Simple", "Aller-Retour");
        filterField.setPrefWidth(100);
        filterField.setPromptText("Filtre");
        mainView.add(filterField, 3, 5);

        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(event -> handleSearch(monCompte));
        mainView.add(searchButton, 2, 5);
        
        Label assistanceLabel = new Label("Besoin d'assistance ?");
        mainView.add(assistanceLabel, 4, 5);
        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 4, 6);
        
        Button backButton = new Button("Se déconnecter");
        backButton.setOnAction(event -> mainApp.showLoginView());
        mainView.add(backButton, 0, 0);
    }

    public HBox getView() {
        return hbox;
    }

    private void handleSearch(String monCompte) {
        String departureStation = departureStationField.getText();
        String arrivalStation = arrivalStationField.getText();
        String departureDate = departureDateField.getValue() != null ? departureDateField.getValue().toString() : null;
        String returnDate = returnDateField.getValue() != null ? returnDateField.getValue().toString() : null;
        

        // Logique pour rechercher des trajets dans la base de données
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM trajets WHERE gare_depart = ? AND gare_arrivee = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, departureStation);
            statement.setString(2, arrivalStation);
            ResultSet resultSet = statement.executeQuery();

            StringBuilder searchResults = new StringBuilder();
            while (resultSet.next()) {
                searchResults.append(resultSet.getString("numero_train"))
                              .append("\n")
                              .append(resultSet.getString("heure_depart"))
                              .append("\n")
                              .append(resultSet.getString("gare_depart"))
                              .append("\n")
                              .append(resultSet.getString("heure_arrivee"))
                              .append("\n")
                              .append(resultSet.getString("gare_arrivee"))
                              .append("\n")
                              .append(resultSet.getString("prix_classe1"))
                              .append("\n")
                              .append(resultSet.getString("prix_classe2"))
                              .append("\n");
            }
            
            String query2 = "SELECT * FROM trajets WHERE gare_depart = ?";
            PreparedStatement statement2 = connection.prepareStatement(query2);
            statement2.setString(1, departureStation);
            ResultSet resultSet2 = statement2.executeQuery();
            StringBuilder searchResults2 = new StringBuilder();
            StringBuilder otherResults = new StringBuilder();
            while (resultSet2.next()) {
                searchResults2.append(resultSet2.getString("numero_train"))
                              .append("\n")
                              .append(resultSet2.getString("heure_depart"))
                              .append("\n")
                              .append(resultSet2.getString("gare_depart"))
                              .append("\n")
                              .append(resultSet2.getString("heure_arrivee"))
                              .append("\n")
                              .append(resultSet2.getString("gare_arrivee"))
                              .append("\n")
                              .append(resultSet2.getString("prix_classe1"))
                              .append("\n")
                              .append(resultSet2.getString("prix_classe2"))
                              .append("\n");
                if(!searchResults2.toString().equals(searchResults.toString())) {
                    otherResults.append(searchResults2);
                }
                searchResults2 = new StringBuilder();
            }

            mainApp.showSearchResultsView(searchResults.toString(), monCompte, otherResults.toString());
        } catch (SQLException e) {
            showError("Erreur lors de la recherche des trajets : " + e.getMessage());
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


