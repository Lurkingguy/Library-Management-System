package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class New_Borrow_Control {

    @FXML
    private TextField txtCustomerID;     // fx:id should be txtCustomerID
    @FXML 
    private TextField txtBookID;     // fx:id should be txtBookID
    @FXML
    private TextField txtQuantity;   // fx:id should be txtQuantity
    @FXML
    private DatePicker dtBorrow;   // fx:id should be dtBorrow
    @FXML
    private DatePicker dtReturn;   // fx:id should be dtReturn
    @FXML
    private Button btnCreate;   // fx:id should be btnCreate
    @FXML
    private Button btnCancel;   // fx:id should be btnCancel

    private int loggedInUserID;  // Add this line

    public void setLoggedInUserID(int userID) { // Add this method
        this.loggedInUserID = userID;
    }

    @FXML
    public void CreateClick(MouseEvent event) {
        String customerIDText = txtCustomerID.getText();
        String bookIDText = txtBookID.getText();
        String quantityText = txtQuantity.getText();
        LocalDate borrowDate = dtBorrow.getValue();
        LocalDate returnDate = dtReturn.getValue();

        if (customerIDText.isEmpty() || bookIDText.isEmpty() || quantityText.isEmpty() || borrowDate == null || returnDate == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        int customerID;
        int bookID;
        int quantity;

        try {
            customerID = Integer.parseInt(customerIDText);
            bookID = Integer.parseInt(bookIDText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Customer ID, Book ID, and Quantity must be valid numbers.");
            return;
        }

        // Debugging: Print loggedInUserID
        System.out.println("loggedInUserID: " + loggedInUserID);

        String query = "INSERT INTO borrow_record (cusID, bookID, quantity, released_date, return_date, staffID) VALUES (?, ?, ?, ?, ?, ?)"; // Modified query
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerID);
            pstmt.setInt(2, bookID);
            pstmt.setInt(3, quantity);
            pstmt.setDate(4, java.sql.Date.valueOf(borrowDate));
            pstmt.setDate(5, java.sql.Date.valueOf(returnDate));
            pstmt.setInt(6, loggedInUserID); // Add this line

            pstmt.executeUpdate();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Borrow record added successfully.");

            // Close the current stage
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.close();

            // Refresh the borrow list in the main view
            Borrow_Control controller = new Borrow_Control();
            controller.loadBorrows();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding borrow record: " + e.getMessage());
        }
    }

    @FXML
    public void CancelClick(MouseEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
