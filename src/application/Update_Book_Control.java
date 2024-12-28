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

public class Update_Book_Control {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtQuantity;
    @FXML
    private ComboBox<Category> cbCategory;
    @FXML
    private TextField txtYear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    
    @FXML
    private int bookID; // Store the book ID that is being updated
    
    @FXML
    private TableView<Book> tbBook;          // Reference to tbBook from BookControl

    public void setTbBook(TableView<Book> tbBook) {
        this.tbBook = tbBook;
    }

    @FXML
    public void initialize() {
        populateCategoryComboBox();
    }

    public void initData(int bookID, String title, String author, int quantity, int categoryID, int year) {
        this.bookID = bookID;
        txtTitle.setText(title);
        txtAuthor.setText(author);
        txtQuantity.setText(String.valueOf(quantity));
        selectCategory(categoryID);
        txtYear.setText(String.valueOf(year));
    }

    private void populateCategoryComboBox() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        String query = "SELECT cateID, cate FROM category";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int cateID = rs.getInt("cateID");
                String category = rs.getString("cate");
                categories.add(new Category(cateID, category));
            }

            cbCategory.setItems(categories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading categories: " + e.getMessage());
        }
    }

    private void selectCategory(int categoryID) {
        for (Category category : cbCategory.getItems()) {
            if (category.getCateID() == categoryID) {
                cbCategory.getSelectionModel().select(category);
                return;
            }
        }
    }

    @FXML
    public void UpdateClick(MouseEvent event) {
        Book selectedBook = tbBook.getSelectionModel().getSelectedItem();
        if (tbBook == null) {
            showAlert(Alert.AlertType.ERROR, "Internal Error", "Category table view is not properly initialized.");
            return;
        }
        if (selectedBook != null) {
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
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity and Year must be valid numbers.");
                return;
            }

            String query = "UPDATE book SET title = ?, author = ?, quantity = ?, cateID = ?, publication_year = ? WHERE bookID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query))
            {
                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, selectedCategory.getCateID());
                pstmt.setInt(5, year);
                pstmt.setInt(6, selectedBook.getBookID());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Book updated successfully.");

                    // Close the current stage
                    Stage stage = (Stage) btnUpdate.getScene().getWindow();
                    stage.close();

                    // Refresh the book list in the main view
                    if (tbBook != null) {
                        Book_Control controller = new Book_Control();
                        controller.loadBooks();
                    }

                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error updating book. Book ID might not exist.");
                }

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating book: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to update.");
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
