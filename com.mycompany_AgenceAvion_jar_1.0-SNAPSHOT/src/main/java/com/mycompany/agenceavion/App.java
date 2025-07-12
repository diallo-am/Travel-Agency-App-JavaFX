package com.mycompany.agenceavion;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.mycompany.agenceavion.controllers.*;
import javafx.scene.image.Image;

public class App extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/agencelogo.png")));
        primaryStage.setResizable(false);
        showLoginView();
    }
    
    public void showWelcomeView(String username, String searchResults) {
        WelcomeController welcomeController = new WelcomeController(this, username, searchResults);
        Scene welcomeScene = new Scene(welcomeController.getView(), 1200, 630);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome");
        welcomeScene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());
    }

    public void showLoginView() {
        LoginController loginController = new LoginController(this); 
        Scene loginScene = new Scene(loginController.getView(), 1200, 630); 
        primaryStage.setScene(loginScene); primaryStage.setTitle("Login"); 
        primaryStage.show(); 
        loginScene.getStylesheets().add(getClass().getResource("/css/style2Accueil.css").toExternalForm());
    }

    public void showSignUpView() {
        SignUpController signUpController = new SignUpController(this);
        Scene signUpScene = new Scene(signUpController.getView(), 1200, 630);
        primaryStage.setScene(signUpScene);
        primaryStage.setTitle("Sign Up");
        signUpScene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());
    }

    public void showSearchResultsView(String searchResults, String monCompte, String otherResults) { 
        SearchResultsController searchResultsController = new SearchResultsController(this, searchResults, monCompte, otherResults); 
        Scene searchResultsScene = new Scene(searchResultsController.getView(), 1200, 630); 
        primaryStage.setScene(searchResultsScene); primaryStage.setTitle("Search Results");
        searchResultsScene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());
    }

    public void showPaymentView(String string, String searchResults, String monCompte, String otherResults) { 
        PaymentController paymentController = new PaymentController(this, searchResults, monCompte, otherResults); 
        Scene paymentScene = new Scene(paymentController.getView(), 1200, 630); 
        primaryStage.setScene(paymentScene); 
        primaryStage.setTitle("Paiement");
        paymentScene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());
    }

    public void showAdminLoginView() {
        AdminLoginController adminLoginController = new AdminLoginController(this);
        Scene adminLoginScene = new Scene(adminLoginController.getView(), 1200, 630);
        primaryStage.setScene(adminLoginScene);
        primaryStage.setTitle("Admin Login");
        adminLoginScene.getStylesheets().add(getClass().getResource("/css/style2Accueil.css").toExternalForm());
    }

    public void showAdminDashboardView(String username) {
        AdminDashboardController adminDashboardController = new AdminDashboardController(this);
        Scene adminDashboardScene = new Scene(adminDashboardController.getView(), 1200, 630);
        primaryStage.setScene(adminDashboardScene);
        primaryStage.setTitle("Admin Dashboard");
        adminDashboardScene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());
    }

    public void showAdminRegistrationView() {
        AdminRegistrationController adminRegistrationController = new AdminRegistrationController(this);
        Scene adminRegistrationScene = new Scene(adminRegistrationController.getView(), 1200, 630);
        primaryStage.setScene(adminRegistrationScene);
        primaryStage.setTitle("Admin Registration");
        adminRegistrationScene.getStylesheets().add(getClass().getResource("/css/style2.css").toExternalForm());
    }

    public void showManageClientsView() {
        ManageClientsController manageClientsController = new ManageClientsController(this);
        Scene manageClientsScene = new Scene(manageClientsController.getView(), 1200, 630);
        primaryStage.setScene(manageClientsScene);
        primaryStage.setTitle("Manage Clients");
        manageClientsScene.getStylesheets().add(getClass().getResource("/css/style22.css").toExternalForm());
    }

    public void showManageReservationsView() {
        ManageReservationsController manageReservationsController = new ManageReservationsController(this);
        Scene manageReservationsScene = new Scene(manageReservationsController.getView(), 1200, 630);
        primaryStage.setScene(manageReservationsScene);
        primaryStage.setTitle("Manage Reservations");
        manageReservationsScene.getStylesheets().add(getClass().getResource("/css/style22.css").toExternalForm());
    }

    // Ajout de la méthode pour afficher la vue de gestion des trajets
    public void showManageRoutesView() {
        ManageRoutesController manageRoutesController = new ManageRoutesController(this);
        Scene manageRoutesScene = new Scene(manageRoutesController.getView(), 1200, 630);
        primaryStage.setScene(manageRoutesScene);
        primaryStage.setTitle("Manage Routes");
        manageRoutesScene.getStylesheets().add(getClass().getResource("/css/style22.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}





