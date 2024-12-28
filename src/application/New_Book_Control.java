package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class New_Book_Control {

    @FXML
    private TextField txtTitle; // fx:id should be txtTitle
    @FXML
    private TextField txtAuthor; // fx:id should be txtAuthor
    @FXML
    private TextField txtQuantity; // fx:id should be txtQuantity
    @FXML
    private ComboBox<Category> cbCategory; // fx:id should be cbCategory
    @FXML
    private TextField txtYear; // fx:id should be txtYear
    @FXML
    private Button btnCreate; // fx:id should be btnCreate
    @FXML
    private Button btnCancel; // fx:id should be btnCancel
    
    @FXML
    private List<Category> categoriesList = new ArrayList<>();
    
    @FXML
    private String loggedInUserName;

    @FXML
    public void initialize() {
        populateCategoryComboBox();
    }

    public void setLoggedInUserName(String userName) {
        this.loggedInUserName = userName;
    }

    private void populateCategoryComboBox() {
        String query = "SELECT cateID, cate FROM category";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int cateID = rs.getInt("cateID");
                String category = rs.getString("cate");
                categoriesList.add(new Category(cateID, category));
                cbCategory.getItems().add(new Category(cateID, category));
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading categories: " + e.getMessage());
        }
    }

    @FXML
    public void CreateClick(MouseEvent event) {
        
    	String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String quantityText = txtQuantity.getText();
        Category selectedCategory = cbCategory.getValue();
        String yearText = txtYear.getText();

        if (title.isEmpty() || author.isEmpty() || quantityText.isEmpty() || selectedCategory == null || yearText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        int quantity;
        int year;

        try {
            quantity = Integer.parseInt(quantityText);
            year = Integer.parseInt(yearText);
        } 
        
        catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity and Year must be valid numbers.");
            return;
        }

        String query = "INSERT INTO book (title, author, quantity, cateID, publication_year, status) VALUES (?, ?, ?, ?, ?, 'Available')";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, selectedCategory.getCateID());
            pstmt.setInt(5, year);

            pstmt.executeUpdate();

            // Show success message
            showAlert(AlertType.INFORMATION, "Success", "Book added successfully.");

            // Close the current stage
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.close();

            // Refresh the category list in the main view
            Book_Control controller = new Book_Control();
            controller.loadBooks();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding book: " + e.getMessage());
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

    
    // Define Category class to hold cateID and category name
    private static class Category {
    	
        private int cateID;
        private String category;

        public Category(int cateID, String category) {
            this.cateID = cateID;
            this.category = category;
        }

        public int getCateID() {
            return cateID;
        }

        public String getCategory() {
            return category;
        }

        @Override
        public String toString() {
            return category;
        }
    }
}
