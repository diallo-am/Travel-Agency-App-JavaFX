package com.mycompany.agenceavion.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import com.mycompany.agenceavion.App;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;

public class SearchResultsController {
    HBox hbox = new HBox();
    GridPane view = new GridPane();
    private GridPane mainView = new GridPane();
    private App mainApp;

    public SearchResultsController(App mainApp, String searchResults, String monCompte, String otherResults) {
        this.mainApp = mainApp;
        createView(searchResults, monCompte, otherResults);
    }

    private void createView(String searchResults, String monCompte, String otherResults) {
        
        hbox.setAlignment(Pos.CENTER);
        mainView.setHgap(10);
        mainView.setVgap(10);
        mainView.setPadding(new Insets(25, 25, 25, 25));
        
        
        Image logo = new Image(getClass().getResource("/Images/agencelogo.png").toExternalForm());
        ImageView logoVue = new ImageView(logo);
        logoVue.setFitWidth(150);
        logoVue.setFitHeight(120);
        view.add(logoVue,0,0);
        view.add(mainView,0,1);
        hbox.getChildren().add(view);
        GridPane.setHalignment(logoVue,HPos.CENTER);

       
        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 5, 0);
        
        Label assistanceLabel = new Label("Besoin d'assistance ?");
        mainView.add(assistanceLabel, 6, 0);

        Button contactButton = new Button("Nous contacter");
        contactButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Assistance");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez nous contacter sur WhatsApp au numéro suivant : ...");
            alert.showAndWait();
        });
        mainView.add(contactButton, 6, 1);

        
       
        Label resultsLabel = new Label("Résultats de la recherche");
        mainView.add(resultsLabel, 1, 1, 2, 1);

       
        String[] results = searchResults.split("\n");
        if (results.length >= 7) {
            TextField trainNumberField = new TextField(results[0]);
            TextField departTimeField = new TextField(results[1]);
            TextField departGareField = new TextField(results[2]);
            TextField arrivalFimeField = new TextField(results[3]);
            TextField arrivalGareField = new TextField(results[4]);
            TextField firstClassPriceField = new TextField(results[5]);
            TextField secondClassPriceField = new TextField(results[6]);

            trainNumberField.setEditable(false);
            departTimeField.setEditable(false);
            departGareField.setEditable(false);
            arrivalFimeField.setEditable(false);
            arrivalGareField.setEditable(false);
            firstClassPriceField.setEditable(false);
            secondClassPriceField.setEditable(false);

            mainView.add(new Label("Numéro de train:"), 0, 2);
            mainView.add(trainNumberField, 1, 2);
            mainView.add(new Label("Heure départ:"), 0, 3);
            mainView.add(departTimeField, 1, 3);
            mainView.add(new Label("Gare départ:"), 2, 3);
            mainView.add(departGareField, 3, 3);
            mainView.add(new Label("Heure arrivée:"), 0, 4);
            mainView.add(arrivalFimeField, 1, 4);
            mainView.add(new Label("Gare arrivée:"), 2, 4);
            mainView.add(arrivalGareField, 3, 4);
            
            Label choixLabel = new Label("Choix :");
            mainView.add(choixLabel, 4, 2);

            mainView.add(new Label("1ère classe"), 4, 3);
            mainView.add(firstClassPriceField, 5, 3);
            Button reserver1Button = new Button("Réserver");
            reserver1Button.setOnAction(event -> mainApp.showPaymentView("Paiement", searchResults, monCompte, otherResults));
            mainView.add(reserver1Button, 6, 3);

            mainView.add(new Label("2ème classe"), 4, 4);
            mainView.add(secondClassPriceField, 5, 4);
            Button reserver2Button = new Button("Réserver");
            reserver2Button.setOnAction(event -> mainApp.showPaymentView("Paiement", searchResults, monCompte, otherResults));
            mainView.add(reserver2Button, 6, 4);
        }
        
        Label othersLabel = new Label("Autres trajets avec le même départ");
        mainView.add(othersLabel, 1, 7, 2, 1);
        
        String[] results2 = otherResults.split("\n");
        if (results2.length >= 7) {
            int t = 0;
            int s = 8;
            while (t < results2.length){
                TextField trainNumberField = new TextField(results2[t]); t++;
                TextField departTimeField = new TextField(results2[t]); t++;
                TextField departGareField = new TextField(results2[t]); t++;
                TextField arrivalFimeField = new TextField(results2[t]); t++;
                TextField arrivalGareField = new TextField(results2[t]); t++;
                TextField firstClassPriceField = new TextField(results2[t]); t++;
                TextField secondClassPriceField = new TextField(results2[t]); t++;

                trainNumberField.setEditable(false);
                departTimeField.setEditable(false);
                departGareField.setEditable(false);
                arrivalFimeField.setEditable(false);
                arrivalGareField.setEditable(false);
                firstClassPriceField.setEditable(false);
                secondClassPriceField.setEditable(false);

                mainView.add(new Label("Numéro de train:"), 0, s);
                mainView.add(trainNumberField, 1, s);
                mainView.add(new Label("Heure départ:"), 0, s+1);
                mainView.add(departTimeField, 1, s+1);
                mainView.add(new Label("Gare départ:"), 2, s+1);
                mainView.add(departGareField, 3, s+1);
                mainView.add(new Label("Heure arrivée:"), 0, s+2);
                mainView.add(arrivalFimeField, 1, s+2);
                mainView.add(new Label("Gare arrivée:"), 2, s+2);
                mainView.add(arrivalGareField, 3, s+2);

                Label choixLabel = new Label("Choix :");
                mainView.add(choixLabel, 4, s);

                mainView.add(new Label("1ère classe"), 4, s+1);
                mainView.add(firstClassPriceField, 5, s+1);
                Button reserver1Button = new Button("Réserver");
                reserver1Button.setOnAction(event -> mainApp.showPaymentView("Paiement", searchResults, monCompte, otherResults));
                mainView.add(reserver1Button, 6, s+1);

                mainView.add(new Label("2ème classe"), 4, s+2);
                mainView.add(secondClassPriceField, 5, s+2);
                Button reserver2Button = new Button("Réserver");
                reserver2Button.setOnAction(event -> mainApp.showPaymentView("Paiement", searchResults, monCompte, otherResults));
                mainView.add(reserver2Button, 6, s+2);
                mainView.add(new Label(""), 0, s+3);
                s+=4;
            }
        }

        
        
        
        String[] usernamE = monCompte.split("\n");    
        String username = usernamE[4];
        String[] usernamE2 = username.split(" : ");    
        String username2 = usernamE2[1];

        Button backButton = new Button("Retour");
        
        backButton.setOnAction(event -> mainApp.showWelcomeView(username2, monCompte));
        mainView.add(backButton, 0, 0);

        Button homeButton = new Button("Accueil");
        homeButton.setOnAction(event -> mainApp.showWelcomeView(username2, monCompte));
        mainView.add(homeButton, 1, 0);
        
        // Add ScrollPane for mainView
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        hbox.getChildren().clear();
        hbox.getChildren().add(scrollPane);
    }

    public HBox getView() {
        return hbox;
    }
}
