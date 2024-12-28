package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Update_Return_Control {

    @FXML
    private TextField txtBorrowID;
    @FXML
    private TextField txtQuantity;
    @FXML
    private DatePicker dtReturn;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;

    private TableView<Return> tbReturn; // Reference to tbReturn from ReturnControl

    public void setTbReturn(TableView<Return> tbReturn) {
        this.tbReturn = tbReturn;
    }

    public void initData(int borrowID, int quantity, String returnDate) {
        txtBorrowID.setText(String.valueOf(borrowID));
        txtQuantity.setText(String.valueOf(quantity));
        dtReturn.setValue(LocalDate.parse(returnDate));
    }

    @FXML
    public void UpdateClick(MouseEvent event) {
        Return selectedReturn = tbReturn.getSelectionModel().getSelectedItem();
        if (selectedReturn != null) {
            try {
                int borrowID = selectedReturn.getBorrowID();
                LocalDate returnDate = dtReturn.getValue();

                if (returnDate == null) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
                    return;
                }

                // Check if borrowID exists in return_record table
                if (!checkIfBorrowIDExists(borrowID)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Borrow ID does not exist in return_record.");
                    return;
                }

                // Update the return record
                String updateQuery = "UPDATE return_record SET returned_date = ? WHERE borrowID = ?";
                try (Connection conn = Connect.getConnection();
                     PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

                    updateStmt.setDate(1, Date.valueOf(returnDate));
                    updateStmt.setInt(2, borrowID);

                    int affectedRows = updateStmt.executeUpdate();

                    if (affectedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Return record updated successfully.");

                        // Close the current stage
                        Stage stage = (Stage) btnUpdate.getScene().getWindow();
                        stage.close();

                        // Refresh the return list in the main view
                        if (tbReturn != null) {
                            Return_Control controller = new Return_Control();
                            controller.loadReturns();
                        }

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Error updating return record. Please try again.");
                    }

                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating return record: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity must be a valid number.");
            }

        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a return record to update.");
        }
    }

    private boolean checkIfBorrowIDExists(int borrowID) {
        String selectQuery = "SELECT borrowID FROM return_record WHERE borrowID = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            selectStmt.setInt(1, borrowID);
            ResultSet rs = selectStmt.executeQuery();
            return rs.next(); // true if borrowID exists in return_record, false otherwise

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error checking borrow ID: " + e.getMessage());
            return false; // Return false in case of any exception
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
