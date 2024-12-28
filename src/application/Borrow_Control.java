package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Borrow_Control {
	// sidebar
    @FXML
    Label staffName;
    @FXML
    Button btnLoan;
    @FXML
    Button btnBook;
    @FXML
    Button btnDashboard;
    @FXML
    Button btnReturn;
    @FXML
    Button btnCustomer;
    @FXML
    Button btnCategory;
    @FXML
    Button btnLogout;
    
    // func
    @FXML
    TextField txtSearch;
    @FXML
    Button btnSearch;
    @FXML
    Button btnAddBorrow;
    @FXML
    Button btnUpdateBorrow;
    @FXML
    Button btnDeleteBorrow;
    
    // view
    @FXML
    TableView<Borrow> tbBorrow;
    @FXML
    TableColumn<Borrow, Integer> borrowIDColumn;
    @FXML
    TableColumn<Borrow, String> customerNameColumn;
    @FXML
    TableColumn<Borrow, String> staffNameColumn;
    @FXML
    TableColumn<Borrow, String> bookTitleColumn;
    @FXML
    TableColumn<Borrow, Integer> quantityColumn;
    @FXML
    TableColumn<Borrow, String> borrowDateColumn;
    @FXML
    TableColumn<Borrow, String> returnDateColumn;
    
    @FXML
    private ObservableList<Book> availableBooks;
	private String loggedInUserName;

    @FXML
    public void initialize() {
        borrowIDColumn.setCellValueFactory(new PropertyValueFactory<>("borrowID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("released_date"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("return_date"));
        loadBorrows();
    }

    public void initData(ObservableList<Book> availableBooks) {
        this.availableBooks = availableBooks;
    }
    
    public void setLoggedInUserName(String userName) {
        this.loggedInUserName = userName;
        staffName.setText(loggedInUserName); // Set the label text here
    }


    @FXML
    public void DashboardClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Dashboard.fxml"));
            Parent root = loader.load();
            
            Dashboard_Control DashboardControl = loader.getController();
            DashboardControl.setLoggedInUserName(loggedInUserName);
            
            Stage stage = (Stage) btnDashboard.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void BookClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Book.fxml"));
        Parent root = loader.load();
        
        Book_Control BookControl = loader.getController();
        BookControl.setLoggedInUserName(loggedInUserName); // Pass the logged in username to NewBookControl
        
        Stage stage = (Stage) btnBook.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("New Book");
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
    public void CategoryClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Category.fxml"));
        Parent root = loader.load();
        
        Category_Control CategoryControl = loader.getController();
        CategoryControl.setLoggedInUserName(loggedInUserName);
        
        Stage stage = (Stage) btnCategory.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Category Information");
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

    // table borrow
    @FXML
    public void loadBorrows() {
        ObservableList<Borrow> borrows = FXCollections.observableArrayList();
        String query = "SELECT br.borrowID, c.cusName, s.staffName, b.title, br.quantity, br.released_date, br.return_date " +
                "FROM borrow_record br JOIN customer c ON br.cusID = c.cusID " +
                "JOIN staff s ON br.staffID = s.staffID JOIN book b ON br.bookID = b.bookID";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int borrowID = rs.getInt("borrowID");
                String cusName = rs.getString("cusName");
                String staffName = rs.getString("staffName");
                String title = rs.getString("title");
                int quantity = rs.getInt("quantity");
                String released_date = rs.getString("released_date");
                String return_date = rs.getString("return_date");
                Borrow borrow = new Borrow(borrowID, cusName, staffName, title, quantity, released_date, return_date);
                borrows.add(borrow);
            }
            tbBorrow.setItems(borrows);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading borrows: " + e.getMessage());
        }
    }

    @FXML
    public void SearchClick(MouseEvent event) {
        String searchText = txtSearch.getText();
        ObservableList<Borrow> borrows = FXCollections.observableArrayList();
        String query = "SELECT br.borrowID, c.cusName, s.staffName, b.title, br.quantity, br.released_date, br.return_date " +
                "FROM borrow_record br " +
                "JOIN customer c ON br.cusID = c.cusID " +
                "JOIN staff s ON br.staffID = s.staffID " +
                "JOIN book b ON br.bookID = b.bookID " +
                "WHERE br.borrowID LIKE ? OR c.cusName LIKE ? OR b.title LIKE ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the placeholders
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, "%" + searchText + "%");
            pstmt.setString(3, "%" + searchText + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int borrowID = rs.getInt("borrowID");
                String cusName = rs.getString("cusName");
                String staffName = rs.getString("staffName");
                String title = rs.getString("title");
                int quantity = rs.getInt("quantity");
                String released_date = rs.getString("released_date");
                String return_date = rs.getString("return_date");
                Borrow borrow = new Borrow(borrowID, cusName, staffName, title, quantity, released_date, return_date);
                borrows.add(borrow);
            }

            tbBorrow.setItems(borrows);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error searching borrows: " + e.getMessage());
        }
    }

    @FXML
    public TableView<Borrow> getTbBorrow() {
        return tbBorrow;
    }
    
    @FXML
    public void AddBorrowClick(MouseEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/New_Borrow.fxml"));
         Parent root = loader.load();
         New_Borrow_Control newBorrowControl = loader.getController();
         newBorrowControl.setLoggedInUserID(Login_Control.getLoggedInUserID()); // Pass the logged in user ID

         Stage stage = new Stage();
         stage.setScene(new Scene(root));
         stage.setTitle("Add New Borrow Record");
         stage.showAndWait(); // Wait for the update book window to close
         loadBorrows(); 
    }

    @FXML
    public void UpdateBorrowClick(MouseEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Update_Borrow.fxml"));
         Parent root = loader.load();
        
        Update_Borrow_Control controller = loader.getController();
        controller.setTbBorrow(tbBorrow); // Pass tbBook reference to UpdateBookControl

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Update Borrow Information");
        stage.showAndWait(); // Wait for the update book window to close
        loadBorrows(); // Refresh the book list after updating
    }

    @FXML
    public void DeleteBorrowClick(MouseEvent event) {
        Borrow selectedBorrow = tbBorrow.getSelectionModel().getSelectedItem();
        if (selectedBorrow != null) {
            String query = "DELETE FROM borrow_record WHERE borrowID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, selectedBorrow.getBorrowID());
                pstmt.executeUpdate();
                tbBorrow.getItems().remove(selectedBorrow);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Borrow record deleted successfully.");

                // Refresh the borrow list in the main view
                loadBorrows();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting borrow record: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a borrow record to delete.");
        }
    }
    

    public int calculateTotalBorrowedQuantity() {
        int totalBorrowed = 0;
        String query = "SELECT SUM(quantity) AS totalBorrowed FROM borrow_record";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalBorrowed = rs.getInt("totalBorrowed");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error calculating total borrowed quantity: " + e.getMessage());
        }
        
        return totalBorrowed;
    }




    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}