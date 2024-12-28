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

public class Customer_Control {
	
    // sidebar
    @FXML
    Label staffName;
    @FXML
    Button btnLoan;
    @FXML
    Button btnBook;
    @FXML
    Button btnBorrow;
    @FXML
    Button btnReturn;
    @FXML
    Button btnDashboard;
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
    Button btnAddCustomer;
    @FXML
    Button btnUpdateCustomer;
    @FXML
    Button btnDeleteCustomer;
    
    // view
    @FXML
    TableView<Customer> tbCustomer;
    @FXML
    private TableColumn<Customer, Integer> colCusID;
    @FXML
    private TableColumn<Customer, String> colCusName;
    @FXML
    private TableColumn<Customer, String> colPhone;
    
    @FXML
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
	private String loggedInUserName;

    @FXML
    public void initialize() {
        // Initialize columns
        colCusID.setCellValueFactory(new PropertyValueFactory<>("cusID"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Load data into TableView
        loadCustomers();
    }


    public void setLoggedInUserName(String userName) {
        this.loggedInUserName = userName;
        staffName.setText(loggedInUserName);                        // Set the label text đây nè
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

    @FXML
    public void loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String query = "SELECT * FROM customer";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int cusID = rs.getInt("cusID");
                String cusName = rs.getString("cusName");
                String phone = rs.getString("phone");

                Customer customer = new Customer(cusID, cusName, phone);
                customers.add(customer);
            }

            tbCustomer.setItems(customers);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Error loading customers: " + e.getMessage());
        }
    }
    
    @FXML
    public TableView<Customer> getTbCustomer() {
        return tbCustomer;
    }
    
    @FXML
    public void SearchClick(MouseEvent event) {
        String searchText = txtSearch.getText();
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String query = "SELECT * FROM customer WHERE cusName LIKE ? OR phone LIKE ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int cusID = rs.getInt("cusID");
                String cusName = rs.getString("cusName");
                String phone = rs.getString("phone");

                Customer customer = new Customer(cusID, cusName, phone);
                customers.add(customer);
            }

            tbCustomer.setItems(customers);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching customers: " + e.getMessage());
        }
    }


    @FXML
    public void AddCustomerClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/New_Customer.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add New Customer Information");
        stage.showAndWait(); // Wait for the new customer window to close
        loadCustomers(); // Refresh the customer list after adding
    }

    @FXML
    public void UpdateCustomerClick(MouseEvent event) throws IOException {
        Customer selectedCustomer = tbCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Update_Customer.fxml"));
            Parent root = loader.load();
            
            Update_Customer_Control controller = loader.getController();
            controller.setTbCustomer(tbCustomer);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Update Customer Information");
            stage.showAndWait(); // Wait for the update customer window to close
            loadCustomers(); // Refresh the customer list after updating
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to update.");
        }
    }

    @FXML
    public void DeleteCustomerClick(MouseEvent event) {
        Customer selectedCustomer = tbCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            String query = "DELETE FROM customer WHERE cusID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, selectedCustomer.getCusID());
                pstmt.executeUpdate();
                tbCustomer.getItems().remove(selectedCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");

                // Refresh the customer list in the main view
                Customer_Control controller = new Customer_Control();
                controller.loadCustomers();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting customer: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to delete.");
        }
    }

    public int sumCustomer() {
        int sumCustomer = 0;
        String query = "SELECT COUNT(*) AS totalCustomers FROM customer";

        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
            	sumCustomer = rs.getInt("totalCustomers");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error calculating total borrowed quantity: " + e.getMessage());
        }
        System.out.println("Total Borrowed Quantity: " + sumCustomer); // Debug statement
        return sumCustomer;
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
