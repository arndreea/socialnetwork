package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import socialnetwork.Main;

import java.io.IOException;

public class UserController {
    protected String currentUsername;

    public UserController() {
    }

    @FXML
    protected Label currentUser;

    @FXML
    private Button manageFriends;

    @FXML
    private Button manageFriendRequests;

    @FXML
    private Button logout;

    protected void initialize1(String username) {
        this.currentUsername = username;
        currentUser.setText("Logged in as : " + username);
    }

    public void userLogout(ActionEvent event) throws IOException {
        FXMLLoader newMenu = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene newMenuScene = new Scene(newMenu.load(), 600, 400);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        LoginController loginController = newMenu.getController();
        loginController.setService();

        stage.setScene(newMenuScene);
        stage.show();
    }

    public void switchManageFriends(ActionEvent event) throws IOException {
        FXMLLoader newMenu = new FXMLLoader(Main.class.getResource("friends-view.fxml"));
        Scene newMenuScene = new Scene(newMenu.load(), 600, 400);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FriendsController friendsController = newMenu.getController();
        friendsController.initialize1(currentUsername);
        friendsController.setServices();

        stage.setScene(newMenuScene);
        stage.show();
    }

    public void switchManageFriendRequests(ActionEvent event) throws IOException {
        FXMLLoader newMenu = new FXMLLoader(Main.class.getResource("friendrequests-view.fxml"));
        Scene newMenuScene = new Scene(newMenu.load(), 600, 400);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FriendRequestsController friendRequestsController = newMenu.getController();
        friendRequestsController.initialize1(currentUsername);
        friendRequestsController.setServices();

        stage.setScene(newMenuScene);
        stage.show();
    }

    public void switchManageMessages(ActionEvent event) throws IOException{
        FXMLLoader newMenu = new FXMLLoader(Main.class.getResource("messagesmenu-view.fxml"));
        Scene newMenuScene = new Scene(newMenu.load(), 750, 500);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        MessagesController messagesController = newMenu.getController();
        messagesController.initialize1(currentUsername);
        messagesController.setServices(currentUsername);

        stage.setScene(newMenuScene);
        stage.show();
    }
}
