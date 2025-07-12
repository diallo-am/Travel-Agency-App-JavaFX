package com.mycompany.agenceavion.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import com.mycompany.agenceavion.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

public class PaymentController {
    HBox hbox = new HBox();
    private GridPane view;
    private TextField cardHolderField;
    private TextField cardNumberField;
    private DatePicker expiryDateField;
    private TextField cvvField;
    private TextField nom;
    private TextField telephone;
    
    private App mainApp;

    public PaymentController(App mainApp, String searchResults, String monCompte, String otherResults) {
        this.mainApp = mainApp;
        createView(searchResults, monCompte, otherResults);
    }

    private void createView(String searchResults, String monCompte, String otherResults) {
        
        view = new GridPane();
        hbox.setAlignment(Pos.CENTER);
        view.setHgap(10);
        view.setVgap(10);
        view.setPadding(new Insets(60, 25, 0, 25));
        
        
        
        Image logo = new Image(getClass().getResource("/Images/agencelogo.png").toExternalForm());
        ImageView logoVue = new ImageView(logo);
        logoVue.setFitWidth(150);
        logoVue.setFitHeight(120);
        view.add(logoVue,3,0,2,13);
        hbox.getChildren().add(view);
        GridPane.setValignment(logoVue,VPos.CENTER);
        

        Button backButton = new Button("Retour");
        backButton.setOnAction(event -> mainApp.showSearchResultsView(searchResults, monCompte, otherResults));
        
        String[] usernamE = monCompte.split("\n");    
        String username = usernamE[4];
        String[] usernamE2 = username.split(" : ");    
        String username2 = usernamE2[1];
        Button homeButton = new Button("Accueil");
        homeButton.setOnAction(event -> mainApp.showWelcomeView(username2,monCompte));

        HBox topLeftBox = new HBox(10, backButton, homeButton);
        topLeftBox.setAlignment(Pos.TOP_LEFT);
        view.add(topLeftBox, 0, 0);
        GridPane InfoGrid = new GridPane();
        InfoGrid.setId("mainView");
        InfoGrid.setVgap(10);

        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(150);
        view.add(languagesButton, 2, 0);

        Label titleLabel = new Label("Compléter le paiement");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        view.add(titleLabel, 0, 1, 2, 1);

        Label personalInfoLabel = new Label("Informations personnelles");
        personalInfoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        InfoGrid.add(personalInfoLabel, 0, 0);

        GridPane userInfoGrid = new GridPane();
        
        userInfoGrid.setHgap(10);
        userInfoGrid.setVgap(10);
        userInfoGrid.setAlignment(Pos.CENTER_LEFT);

        Label labelPrenom = new Label("Nom complet:");
        nom = new TextField();
        Label labelTelephone = new Label("Téléphone:");
        telephone = new TextField();

        userInfoGrid.add(labelPrenom, 0, 1);
        userInfoGrid.add(nom, 1, 1);
        userInfoGrid.add(labelTelephone, 0, 2);
        userInfoGrid.add(telephone, 1, 2);
        
        InfoGrid.add(userInfoGrid, 0, 1, 2, 1);
        view.add(InfoGrid, 0, 2, 2, 1);

        Label paymentInfoLabel = new Label("Informations de paiement");
        paymentInfoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        InfoGrid.add(paymentInfoLabel, 0, 2, 2, 1);

        GridPane paymentInfoGrid = new GridPane();
        paymentInfoGrid.setHgap(10);
        paymentInfoGrid.setVgap(10);
        paymentInfoGrid.setAlignment(Pos.CENTER_LEFT);

        Label cardHolderLabel = new Label("Détenteur de la carte:");
        cardHolderField = new TextField();
        Label cardNumberLabel = new Label("Numéro de la carte:");
        cardNumberField = new TextField();
        Label expiryDateLabel = new Label("Date d’expiration:");
        expiryDateField = new DatePicker();
        Label cvvLabel = new Label("Code CVV:");
        cvvField = new TextField();

        paymentInfoGrid.add(cardHolderLabel, 0, 0);
        paymentInfoGrid.add(cardHolderField, 1, 0);
        paymentInfoGrid.add(cardNumberLabel, 0, 1);
        paymentInfoGrid.add(cardNumberField, 1, 1);
        paymentInfoGrid.add(expiryDateLabel, 0, 2);
        paymentInfoGrid.add(expiryDateField, 1, 2);
        paymentInfoGrid.add(cvvLabel, 0, 3);
        paymentInfoGrid.add(cvvField, 1, 3);

        InfoGrid.add(paymentInfoGrid, 0, 3, 2, 1);

        Button validateButton = new Button("Valider le paiement");
        validateButton.setOnAction(event -> handlePayment());
        InfoGrid.add(validateButton, 0, 4, 2, 1);

        Label acceptLabel = new Label("Nous acceptons:");
        view.add(acceptLabel, 0, 3);

        HBox logosBox = new HBox(10);
        logosBox.setAlignment(Pos.CENTER_LEFT);

        Image logoImage1 = new Image(getClass().getResource("/Images/VisaCard.png").toExternalForm());
        ImageView logoView1 = new ImageView(logoImage1);
        logoView1.setFitWidth(30);
        logoView1.setFitHeight(20);

        Image logoImage2 = new Image(getClass().getResource("/Images/MasterCard.png").toExternalForm());
        ImageView logoView2 = new ImageView(logoImage2);
        logoView2.setFitWidth(30);
        logoView2.setFitHeight(20);

        logosBox.getChildren().addAll(logoView1, logoView2);
        view.add(logosBox, 0, 4, 2, 1);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        view.add(contactButton, 8, 3);
    }

    public HBox getView() {
        return hbox;
    }

    private void handlePayment() {
    	String nomReservation = nom.getText();
    	String telephoneReservation = telephone.getText();
    	
        String cardHolder = cardHolderField.getText();
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getValue() != null ? expiryDateField.getValue().toString() : null;
        String cvv = cvvField.getText();

     
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "INSERT INTO paiements (detenteur_carte, numero_carte, date_expiration, cvv) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cardHolder);
            statement.setString(2, cardNumber);
            statement.setString(3, expiryDate);
            statement.setString(4, cvv);
            statement.executeUpdate();
            
            String query2 = "INSERT INTO reservation_confirmation (nom, telephone) VALUES (?, ?)";
            PreparedStatement statement2 = connection.prepareStatement(query2);
            statement2.setString(1, nomReservation);
            statement2.setString(2, telephoneReservation);
            statement2.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION); 
            alert.setTitle("Confirmation de Paiement"); 
            alert.setHeaderText(null); 
            alert.setContentText("Paiement effectué avec succès, vous recevrez votre ticket par email !"); 
            alert.showAndWait(); 

            mainApp.showWelcomeView("Utilisateur","");
        } catch (SQLException e) {
            showError("Erreur lors du paiement : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur !!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
