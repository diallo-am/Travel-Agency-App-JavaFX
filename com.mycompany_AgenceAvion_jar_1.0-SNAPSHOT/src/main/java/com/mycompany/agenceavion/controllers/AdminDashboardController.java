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
import javafx.scene.layout.GridPane;
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
import javafx.scene.layout.HBox;

public class AdminDashboardController {
    HBox hbox = new HBox();
    GridPane view = new GridPane();
    private GridPane mainView = new GridPane();
    private App mainApp;

    public AdminDashboardController(App mainApp) {
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

        // Header section with logo and language button
        
        
        Button homeButton = new Button("Se déconnecter");
        homeButton.setOnAction(event -> mainApp.showAdminLoginView());
        mainView.add(homeButton, 0, 0);
        
        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 3, 0);

        // Welcome message
        Label welcomeLabel = new Label("Bienvenue dans l'espace administrateur");
        mainView.add(welcomeLabel, 1, 1, 2, 1);

        // Buttons for managing clients, routes, and reservations
        Button manageClientsButton = new Button("Gestion des comptes clients");
        manageClientsButton.setOnAction(event -> mainApp.showManageClientsView());
        mainView.add(manageClientsButton, 1, 2);

        Button manageRoutesButton = new Button("Gestion des trajets");
        manageRoutesButton.setOnAction(event -> mainApp.showManageRoutesView());
        mainView.add(manageRoutesButton, 1, 3);

        Button manageReservationsButton = new Button("Gestion des réservations");
        manageReservationsButton.setOnAction(event -> mainApp.showManageReservationsView());
        mainView.add(manageReservationsButton, 1, 4);

        // Assistance section
        Label assistanceLabel = new Label("Besoin d'assistance ?");
        mainView.add(assistanceLabel, 3, 5);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 3, 6);
    }

    public HBox getView() {
        return hbox;
    }
}


