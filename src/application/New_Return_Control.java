package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class New_Return_Control {

    @FXML
    private TextField txtBorrowID;
    @FXML
    private DatePicker dtReturn;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnCancel;

    @FXML
    public void CreateClick(MouseEvent event) {
        // Get input data
        String borrowIDStr = txtBorrowID.getText().trim();
        LocalDate returnDate = dtReturn.getValue();

        // Validate input
        if (borrowIDStr.isEmpty() || returnDate == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        int borrowID, quantity;
        try {
            borrowID = Integer.parseInt(borrowIDStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid input. Borrow ID and Quantity must be integers.");
            return;
        }

        // Insert new return record
        String insertQuery = "INSERT INTO return_record (borrowID, staffID, returned_date) VALUES (?, ?, ?)";
        try (Connection conn = Connect.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            insertStmt.setInt(1, borrowID);
            // For demonstration purpose, staffID is set to 1
            insertStmt.setInt(2, 1); // Assuming staffID 1 is the current staff
            insertStmt.setDate(3, Date.valueOf(returnDate));

            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Return record created successfully.");
                // Close the current window
                Stage stage = (Stage) btnCreate.getScene().getWindow();
                stage.close();

    		    // Refresh the category list in the main view
    		    Return_Control controller = new Return_Control();
    		    controller.loadReturns();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create return record. Please try again.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error creating return record: " + e.getMessage());
        }
        
        
    }

    @FXML
    public void CancelClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Return.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
