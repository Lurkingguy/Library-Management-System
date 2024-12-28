package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class New_Category_Control {

    @FXML
    private TextField txtCategoryName; // fx:id should be txtCategoryName
    @FXML
    private Button btnCreate; // fx:id should be btnCreate
    @FXML
    private Button btnCancel; // fx:id should be btnCancel

    @FXML
    public void CreateClick(MouseEvent event) throws IOException {
        String categoryName = txtCategoryName.getText();

        if (categoryName.isEmpty()) {
            // Handle error when fields are empty
            return;
        }

        // Insert into database
		String query = "INSERT INTO category (cate) VALUES (?)";
		try (Connection conn = Connect.getConnection();
		     PreparedStatement pstmt = conn.prepareStatement(query)) {
		    pstmt.setString(1, categoryName);
		    pstmt.executeUpdate();

		    // Show success message
		    showAlert(AlertType.INFORMATION, "Success", "Category added successfully.");

		    // Close the current stage
		    Stage stage = (Stage) btnCreate.getScene().getWindow();
		    stage.close();

		    // Refresh the category list in the main view
		    Category_Control controller = new Category_Control();
		    controller.loadCategories();

		} catch (SQLException e) {
		    showAlert(AlertType.ERROR, "Database Error", "Error adding category: " + e.getMessage());
		}
    }

    @FXML
    public void CancelClick(MouseEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // Define your showAlert method here
    private void showAlert(AlertType type, String title, String message) {
        // Implement your alert dialog here
    }
}
