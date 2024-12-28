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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Update_Borrow_Control {

    @FXML
    private TextField txtCustomerID; // fx:id should be txtCustomerID
    @FXML
    private TextField txtBookID; // fx:id should be txtBookID
    @FXML
    private TextField txtQuantity; // fx:id should be txtQuantity
    @FXML
    private DatePicker dtBorrow; // fx:id should be dtBorrow
    @FXML
    private DatePicker dtReturn; // fx:id should be dtReturn
    @FXML
    private Button btnUpdate; // fx:id should be btnUpdate
    @FXML
    private Button btnCancel; // fx:id should be btnCancel
    
    @FXML
    private int borrowID; // Store the borrow ID that is being updated

    private TableView<Borrow> tbBorrow; // Reference to tbBorrow from BorrowControl

    public void setTbBorrow(TableView<Borrow> tbBorrow) {
        this.tbBorrow = tbBorrow;
    }

    public void initData(int borrowID, int customerID, int bookID, int quantity, LocalDate borrowDate, LocalDate returnDate) {
        this.borrowID = borrowID;
        txtCustomerID.setText(String.valueOf(customerID));
        txtBookID.setText(String.valueOf(bookID));
        txtQuantity.setText(String.valueOf(quantity));
        dtBorrow.setValue(borrowDate);
        dtReturn.setValue(returnDate);
    }

    @FXML
    public void UpdateClick(MouseEvent event) {
        if (tbBorrow == null) {
            showAlert(Alert.AlertType.ERROR, "Internal Error", "Borrow table view is not properly initialized.");
            return;
        }

        Borrow selectedBorrow = tbBorrow.getSelectionModel().getSelectedItem();
        if (selectedBorrow != null) {
            int customerID = Integer.parseInt(txtCustomerID.getText());
            int bookID = Integer.parseInt(txtBookID.getText());
            int quantity = Integer.parseInt(txtQuantity.getText());
            LocalDate borrowDate = dtBorrow.getValue();
            LocalDate returnDate = dtReturn.getValue();

            if (customerID <= 0 || bookID <= 0 || quantity <= 0 || borrowDate == null || returnDate == null) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
                return;
            }

            String query = "UPDATE borrow_record SET cusID = ?, bookID = ?, quantity = ?, released_date = ?, return_date = ? WHERE borrowID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, customerID);
                pstmt.setInt(2, bookID);
                pstmt.setInt(3, quantity);
                pstmt.setDate(4, java.sql.Date.valueOf(borrowDate));
                pstmt.setDate(5, java.sql.Date.valueOf(returnDate));
                pstmt.setInt(6, selectedBorrow.getBorrowID());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Borrow record updated successfully.");

                    // Close the current stage
                    Stage stage = (Stage) btnUpdate.getScene().getWindow();
                    stage.close();

                    // Refresh the borrow list in the main view
                    if (tbBorrow != null) {
                        Borrow_Control controller = new Borrow_Control();
                        controller.loadBorrows();
                    }

                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error updating borrow record. Borrow ID might not exist.");
                }

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating borrow record: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a borrow to update.");
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
