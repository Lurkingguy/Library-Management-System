package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Update_Category_Control {

    @FXML
    private TextField txtCategoryName;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;

    @FXML
    private TableView<Category> tbCategory;

    public void setTbCategory(TableView<Category> tbCategory) {
        this.tbCategory = tbCategory;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void UpdateClick(MouseEvent event) {
        if (tbCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Internal Error", "Category table view is not properly initialized.");
            return;
        }

        Category selectedCategory = tbCategory.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            String categoryName = txtCategoryName.getText();

            if (categoryName.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a category name.");
                return;
            }

            String query = "UPDATE category SET cate = ? WHERE cateID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, categoryName);
                pstmt.setInt(2, selectedCategory.getCateID());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Category updated successfully.");

                    // Close the current stage
                    Stage stage = (Stage) btnUpdate.getScene().getWindow();
                    stage.close();

                    // Refresh the category list in the main view
                    Category_Control controller = new Category_Control();
                    controller.loadCategories();

                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error updating category. Category ID might not exist.");
                }

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating category: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a category to update.");
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
