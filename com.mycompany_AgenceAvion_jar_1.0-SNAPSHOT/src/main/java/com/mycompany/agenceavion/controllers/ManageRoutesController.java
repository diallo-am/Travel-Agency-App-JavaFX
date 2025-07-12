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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ManageRoutesController {
    private HBox hbox = new HBox();
    private GridPane mainView = new GridPane();
    private App mainApp;

    public ManageRoutesController(App mainApp) {
        this.mainApp = mainApp;
        createView();
        displayAllRoutes();
    }

    private void createView() {
        hbox.setAlignment(Pos.CENTER);
        mainView.setHgap(10);
        mainView.setVgap(10);
        mainView.setPadding(new Insets(25, 25, 25, 25));

        // Add header components, search bar, and add new route section
        ComboBox<String> languagesButton = new ComboBox<>();
        languagesButton.getItems().addAll("Français", "English", "Español", "العربية");
        languagesButton.setValue("Français");
        languagesButton.setPrefWidth(100);
        mainView.add(languagesButton, 5, 0);
        
        // Buttons "Accueil" and "Retour"
        Button homeButton = new Button("Se déconnecter");
        homeButton.setOnAction(event -> mainApp.showAdminLoginView());
        mainView.add(homeButton, 6, 0);
        Button backButton = new Button("Retour");
        backButton.setOnAction(event -> mainApp.showAdminDashboardView("Admin"));
        mainView.add(backButton, 1, 0);
        
        Image logo = new Image(getClass().getResource("/Images/agencelogo.png").toExternalForm());
        ImageView logoVue = new ImageView(logo);
        logoVue.setFitWidth(80);
        logoVue.setFitHeight(64);
        mainView.add(logoVue,0,0);
        
       

        Label searchLabel = new Label("Rechercher un trajet par numéro de train:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(event -> {
            handleSearch(searchField.getText());
            Button list = new Button("Liste complète");
            list.setOnAction(even -> {
                displayAllRoutes();
                mainView.getChildren().remove(list);
            });
            mainView.add(list, 4, 1);
                });

        mainView.add(searchLabel, 0, 1, 2, 1);
        mainView.add(searchField, 2, 1);
        mainView.add(searchButton, 3, 1);

        Label newRouteLabel = new Label("Ajouter un nouveau trajet");
        TextField newTrainNumberField = new TextField();
        TextField newDepartureStationField = new TextField();
        TextField newArrivalStationField = new TextField();
        TextField newDateField = new TextField();
        TextField newDepartureTimeField = new TextField();
        TextField newArrivalTimeField = new TextField();
        TextField firstClassPrice = new TextField();
        TextField secondClassPrice = new TextField();
        Button addButton = new Button("Ajouter");
        addButton.setOnAction(event -> handleAdd(newTrainNumberField, newDepartureStationField, newArrivalStationField, newDateField, newDepartureTimeField, newArrivalTimeField, firstClassPrice, secondClassPrice));
        
        mainView.add(newRouteLabel, 0, 2, 2, 1);
        mainView.add(new Label("Numéro de train:"), 0, 3);
        mainView.add(newTrainNumberField, 0, 4);
        mainView.add(new Label("Gare de départ:"), 1, 3);
        mainView.add(newDepartureStationField, 1, 4);
        mainView.add(new Label("Gare d'arrivée:"), 2, 3);
        mainView.add(newArrivalStationField, 2, 4);
        mainView.add(new Label("Date:"), 3, 3);
        mainView.add(newDateField, 3, 4);
        mainView.add(new Label("Heure de départ:"), 4, 3);
        mainView.add(newDepartureTimeField, 4, 4);
        mainView.add(new Label("Heure d'arrivée:"), 5, 3);
        mainView.add(newArrivalTimeField, 5, 4);
        mainView.add(new Label("1ère classe:"), 6, 3);
        mainView.add(firstClassPrice, 6, 4);
        mainView.add(new Label("2ème classe:"), 7, 3);
        mainView.add(secondClassPrice, 7, 4);
        mainView.add(addButton, 7, 5);
        
        Label routeInfoLabel = new Label("Détails des trajets");
        mainView.add(routeInfoLabel, 0, 7, 2, 1);
        
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

        // Add ScrollPane for mainView
        ScrollPane scrollPane = new ScrollPane(mainView);
        scrollPane.setFitToWidth(true);
        hbox.getChildren().clear();
        hbox.getChildren().add(scrollPane);
    }

    public HBox getView() {
        return hbox;
    }

    private void displayAllRoutes() {
        mainView.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 7);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM trajets";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int row = 8;
            while (resultSet.next()) {
                String trainNumber = resultSet.getString("numero_train");
                String departureStation = resultSet.getString("gare_depart");
                String arrivalStation = resultSet.getString("gare_arrivee");
                String date = resultSet.getString("date");
                String departureTime = resultSet.getString("heure_depart");
                String arrivalTime = resultSet.getString("heure_arrivee");
                String firstClass = resultSet.getString("prix_classe1");
                String secondClass = resultSet.getString("prix_classe2");

                addRouteToView(trainNumber, departureStation, arrivalStation, date, departureTime, arrivalTime, firstClass, secondClass, row);
                row += 3;
            }
        } catch (SQLException e) {
            showError("Erreur lors de l'affichage des trajets : " + e.getMessage());
        }
    }

    private void addRouteToView(String trainNumber, String departureStation, String arrivalStation, String date, String departureTime, String arrivalTime, String firstClass, String secondClass, int row) {
        TextField trainNumberField = new TextField(trainNumber);
        TextField departureStationField = new TextField(departureStation);
        TextField arrivalStationField = new TextField(arrivalStation);
        TextField dateField = new TextField(date);
        TextField departureTimeField = new TextField(departureTime);
        TextField arrivalTimeField = new TextField(arrivalTime);
        TextField firstClassPriceField = new TextField(firstClass);
        TextField secondClassPriceField = new TextField(secondClass);

        mainView.add(new Label("Numéro de train:"), 0, row);
        mainView.add(trainNumberField, 0, row + 1);
        mainView.add(new Label("Gare de départ:"), 1, row);
        mainView.add(departureStationField, 1, row + 1);
        mainView.add(new Label("Gare d'arrivée:"), 2, row);
        mainView.add(arrivalStationField, 2, row + 1);
        mainView.add(new Label("Date:"), 3, row);
        mainView.add(dateField, 3, row + 1);
        mainView.add(new Label("Heure de départ:"), 4, row);
        mainView.add(departureTimeField, 4, row + 1);
        mainView.add(new Label("Heure d'arrivée:"), 5, row);
        mainView.add(arrivalTimeField, 5, row + 1);
        mainView.add(new Label("1ère classe:"), 6, row);
        mainView.add(firstClassPriceField, 6, row + 1);
        mainView.add(new Label("2ème classe:"), 7, row);
        mainView.add(secondClassPriceField, 7, row + 1);

        Button editButton = new Button("Modifier");
        editButton.setOnAction(event -> handleEdit(trainNumber, departureStationField.getText(), arrivalStationField.getText(), dateField.getText(), departureTimeField.getText(), arrivalTimeField.getText(), firstClassPriceField.getText(), secondClassPriceField.getText(),row));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> {
            handleDelete(trainNumber, row);
            displayAllRoutes();
        });

        mainView.add(editButton, 6, row + 2);
        mainView.add(deleteButton, 7, row + 2);
    }

    private void handleSearch(String trainNumber) {
        mainView.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 7);
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "SELECT * FROM trajets WHERE numero_train = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, trainNumber);
            ResultSet resultSet = statement.executeQuery();

            int row = 8;
            while (resultSet.next()) {
                String departureStation = resultSet.getString("gare_depart");
                String arrivalStation = resultSet.getString("gare_arrivee");
                String date = resultSet.getString("date");
                String departureTime = resultSet.getString("heure_depart");
                String arrivalTime = resultSet.getString("heure_arrivee");
                String firstClass = resultSet.getString("prix_classe1");
                String secondClass = resultSet.getString("prix_classe2");

                addRouteToView(trainNumber, departureStation, arrivalStation, date, departureTime, arrivalTime, firstClass, secondClass, row);
                row += 3;
            }
        } catch (SQLException e) {
            showError("Erreur lors de la recherche du trajet : " + e.getMessage());
        }
        
    }
    

    private void handleAdd(TextField... fields) {
        String trainNumber = fields[0].getText();
        String departureStation = fields[1].getText();
        String arrivalStation = fields[2].getText();
        String date = fields[3].getText();
        String departureTime = fields[4].getText();
        String arrivalTime = fields[5].getText();
        String firstClass = fields[6].getText();
        String secondClass = fields[7].getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "INSERT INTO trajets (numero_train, gare_depart, gare_arrivee, date, heure_depart, heure_arrivee, prix_classe1, prix_classe2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, trainNumber);
            statement.setString(2, departureStation);
            statement.setString(3, arrivalStation);
            statement.setString(4, date);
            statement.setString(5, departureTime);
            statement.setString(6, arrivalTime);
            statement.setString(7, firstClass);
            statement.setString(8, secondClass);
            statement.executeUpdate();

            showSuccess("Le trajet a été ajouté avec succès.");
            clearFields(fields);
            displayAllRoutes();
        } catch (SQLException e) {
            showError("Erreur lors de l'ajout du trajet : " + e.getMessage());
        }
    }

    private void handleEdit(String trainNumber, String departureStation, String arrivalStation, String date, String departureTime, String arrivalTime, String firstClass, String secondClass, int row) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "UPDATE trajets SET gare_depart = ?, gare_arrivee = ?, date = ?, heure_depart = ?, heure_arrivee = ?, prix_classe1 = ?, prix_classe2 = ? WHERE numero_train = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, departureStation);
            statement.setString(2, arrivalStation);
            statement.setString(3, date);
            statement.setString(4, departureTime);
            statement.setString(5, arrivalTime);
            statement.setString(6, firstClass);
            statement.setString(7, secondClass);
            statement.setString(8, trainNumber);
            statement.executeUpdate();

            showSuccess("Le trajet a été modifié avec succès.");
            displayAllRoutes();
        } catch (SQLException e) {
            showError("Erreur lors de la modification du trajet : " + e.getMessage());
        }
    }

    private void handleDelete(String trainNumber, int row) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agenceavion", "root", "")) {
            String query = "DELETE FROM trajets WHERE numero_train = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, trainNumber);
            statement.executeUpdate();

            showSuccess("Le trajet a été supprimé avec succès.");
            mainView.getChildren().removeIf(node -> {
                Integer nodeRow = GridPane.getRowIndex(node);
                return nodeRow != null && nodeRow >= row && nodeRow < row + 3;
            });
        } catch (SQLException e) {
            showError("Erreur lors de la suppression du trajet : " + e.getMessage());
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
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

