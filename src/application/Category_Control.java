package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Category_Control {
    // sidebar
    @FXML
    Label staffName;
    @FXML
    Button btnLoan;
    @FXML
    Button btnDashboard;
    @FXML
    Button btnBorrow;
    @FXML
    Button btnReturn;
    @FXML
    Button btnCustomer;
    @FXML
    Button btnBook;
    @FXML
    Button btnLogout;

    // func
    @FXML
    TextField txtSearch;
    @FXML
    Button btnSearch;
    @FXML
    Button btnAddCategory;
    @FXML
    Button btnUpdateCategory;
    @FXML
    Button btnDeleteCategory;

    // view
    @FXML
    private TableView<Category> tbCategory;
    @FXML
    private TableColumn<Category, Integer> colCateID;
    @FXML
    private TableColumn<Category, String> colCate;
    
    @FXML
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();
	private String loggedInUserName;

    @FXML
    public void initialize() {
        // Initialize columns
        colCateID.setCellValueFactory(new PropertyValueFactory<>("cateID"));
        colCate.setCellValueFactory(new PropertyValueFactory<>("cate"));

        // Load data into TableView
        loadCategories();
    }
    
    @FXML
    public TableView<Category> getTbCategory() {
        return tbCategory;
    }

    public void setLoggedInUserName(String userName) {
        this.loggedInUserName = userName;
        staffName.setText(loggedInUserName); // Set the label text here
    }

    @FXML
    public void DashboardClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Dashboard.fxml"));
        Parent root = loader.load();
        
        Dashboard_Control DashboardControl = loader.getController();
        DashboardControl.setLoggedInUserName(loggedInUserName);
        
        Stage stage = (Stage) btnDashboard.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }


    @FXML
    public void BookClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Book.fxml"));
        Parent root = loader.load();
        
        Book_Control BookControl = loader.getController();
        BookControl.setLoggedInUserName(loggedInUserName);
        
        Stage stage = (Stage) btnBook.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Book Information");
        stage.show();
    }

    @FXML
    public void BorrowClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Borrow.fxml"));
        Parent root = loader.load();
        
        Borrow_Control BorrowControl = loader.getController();
        BorrowControl.setLoggedInUserName(loggedInUserName);
        
        Stage stage = (Stage) btnBorrow.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Borrow Information");
        stage.show();
    }

    @FXML
    public void ReturnClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Return.fxml"));
        Parent root = loader.load();
        
        Return_Control ReturnControl = loader.getController();
        ReturnControl.setLoggedInUserName(loggedInUserName);
        
        Stage stage = (Stage) btnReturn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Return Information");
        stage.show();
    }

    @FXML
    public void CustomerClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Customer.fxml"));
        Parent root = loader.load();
        
        Customer_Control CustomerControl = loader.getController();
        CustomerControl.setLoggedInUserName(loggedInUserName);
        
        Stage stage = (Stage) btnCustomer.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Customer Information");
        stage.show();
    }

    @FXML
    public void LogoutClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    @FXML
    public void loadCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        String query = "SELECT * FROM category";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int cateID = rs.getInt("cateID");
                String cate = rs.getString("cate");

                Category category = new Category(cateID, cate);
                categories.add(category);
            }

            tbCategory.setItems(categories);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Error loading categories: " + e.getMessage());
        }
    }
    @FXML
    public void SearchClick(MouseEvent event) {
    	String searchText = txtSearch.getText();
    	ObservableList<Category> categories = FXCollections.observableArrayList();

        String query = "SELECT * FROM category WHERE cate LIKE ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
            	int cateID = rs.getInt("cateID");
                String cate = rs.getString("cate");
             
                Category category = new Category(cateID, cate);
                categories.add(category);
            }

            tbCategory.setItems(categories);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Error fetching books: " + e.getMessage());
        }
    }

    @FXML
    public void AddCategoryClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/New_Category.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add New Category Information");
        stage.showAndWait(); // Wait for the new category window to close
        loadCategories(); // Refresh the category list after adding
    }

    @FXML
    public void UpdateCategoryClick(MouseEvent event) throws IOException {
        Category selectedCategory = tbCategory.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Update_Category.fxml"));
            Parent root = loader.load();
            
            Update_Category_Control controller = loader.getController();
            controller.setTbCategory(tbCategory);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Update Category Information");
            stage.showAndWait(); // Wait for the update category window to close
            loadCategories(); // Refresh the category list after updating
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a category to update.");
        }
    }

    @FXML
    public void DeleteCategoryClick(MouseEvent event) {
        Category selectedCategory = tbCategory.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            String query = "DELETE FROM category WHERE cateID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, selectedCategory.getCateID());
                pstmt.executeUpdate();
                categoryList.remove(selectedCategory);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully.");
                
                // Refresh the category list in the main view
    		    Category_Control controller = new Category_Control();
    		    controller.loadCategories();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting category: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a category to delete.");
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
