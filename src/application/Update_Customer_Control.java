package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Update_Customer_Control {

    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtPhone;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;

    private TableView<Customer> tbCustomer; // Reference to tbCustomer from CustomerControl

    public void setTbCustomer(TableView<Customer> tbCustomer) {
        this.tbCustomer = tbCustomer;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void UpdateClick(MouseEvent event) {
    	Customer selectedCustomer = tbCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.ERROR, "Internal Error", "No customer selected for update.");
            return;
        }

        String customerName = txtCustomerName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (customerName.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        String query = "UPDATE customer SET cusName = ?, phone = ? WHERE cusID = ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerName);
            pstmt.setString(2, phone);
            pstmt.setInt(3, selectedCustomer.getCusID());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");

                // Close the current stage
                Stage stage = (Stage) btnUpdate.getScene().getWindow();
                stage.close();

                // Refresh the customer list in the main view
                Customer_Control controller = new Customer_Control();
                controller.loadCustomers();

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Error updating customer. Customer ID might not exist.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating customer: " + e.getMessage());
        }
    }

    @FXML
    public void CancelClick(MouseEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
