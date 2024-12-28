package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Login_Control {
    @FXML
    private TextField txtName;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Button btnLogIn;

    private static String loggedInUserName;
    private static int loggedInUserID;
    
    public static int getLoggedInUserID() {
        return loggedInUserID;
    }
    public static void setLoggedInUserID(int userID) {
        loggedInUserID = userID;}


    @FXML
    public void initialize() {
    	
        // pre-populate
        txtName.setText("ChickenNugget");
        txtPass.setText("password1");
    }

    @FXML
    public void LoginClick(MouseEvent event) {
        String name = txtName.getText();
        String pass = txtPass.getText();

        if (validateCredentials(name, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Dashboard.fxml"));
                Parent root = loader.load();
                Dashboard_Control dashboardController = loader.getController();
                dashboardController.setLoggedInUserName(loggedInUserName);       // logged in username and pass
                Stage stage = (Stage) btnLogIn.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error loading Dashboard: " + e.getMessage());
            }
        } 
        
        else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private boolean validateCredentials(String username, String password) {
        String query = "SELECT staffID, staffName FROM staff WHERE staffName = ? AND password = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                loggedInUserID = rs.getInt("staffID");    
                loggedInUserName = rs.getString("staffName");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error validating credentials: " + e);
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
