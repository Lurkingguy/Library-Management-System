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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Return_Control {
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
    Button btnDashboard;
    @FXML
    Button btnCustomer;
    @FXML
    Button btnCategory;
    @FXML
    Button btnLogout;

    // Func
    @FXML
    TextField txtSearch;
    @FXML
    Button btnSearch;
    @FXML
    Button btnAddReturn;
    @FXML
    Button btnUpdateReturn;
    @FXML
    Button btnDeleteReturn;

    // view
    @FXML
    private TableView<Return> tbReturn;
    @FXML
    private TableColumn<Return, Integer> borrowIDColumn;
    @FXML
    private TableColumn<Return, Integer> customerNameColumn;
    @FXML
    private TableColumn<Return, Integer> titleColumn;
    @FXML
    private TableColumn<Return, Integer> quantityColumn;
    @FXML
    private TableColumn<Return, String> returnDateColumn;
    @FXML
    private TableColumn<Return, String> returnedDateColumn;
    @FXML
    private TableColumn<Return, String> statusColumn;
	private String loggedInUserName;

    @FXML
    public void initialize() {
        borrowIDColumn.setCellValueFactory(new PropertyValueFactory<>("borrowID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnedDateColumn.setCellValueFactory(new PropertyValueFactory<>("returned_date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data into the table
        loadReturns();
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

    @FXML
    public void loadReturns() {
        ObservableList<Return> returns = FXCollections.observableArrayList();

        String query = "SELECT rr.borrowID, c.cusName, b.title, br.quantity, br.return_date, rr.returned_date, " +
                "CASE WHEN rr.returned_date IS NOT NULL THEN 'Returned' ELSE 'Not Returned' END AS status " +
                "FROM borrow_record br " +
                "LEFT JOIN return_record rr ON br.borrowID = rr.borrowID " +
                "LEFT JOIN customer c ON br.cusID = c.cusID " +
                "LEFT JOIN book b ON br.bookID = b.bookID " +
                "WHERE rr.borrowID > 0";

        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int borrowID = rs.getInt("borrowID");
                String customerName = rs.getString("cusName");
                String title = rs.getString("title");
                int quantity = rs.getInt("quantity");
                String returnDate = rs.getString("return_date");
                String returned_date = rs.getString("returned_date");
                String status = rs.getString("status");

                Return ret = new Return(borrowID, customerName, title, quantity, returnDate, returned_date, status);
                returns.add(ret);
            }

            tbReturn.setItems(returns);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading return records: " + e.getMessage());
        }
    }
    
    
    @FXML
    public void SearchClick(MouseEvent event) {
        String searchText = txtSearch.getText();
        ObservableList<Return> returns = FXCollections.observableArrayList();

        String query = "SELECT rr.borrowID, c.cusName, b.title, br.quantity, br.return_date, rr.returned_date, " +
                       "CASE WHEN rr.returned_date IS NOT NULL THEN 'Returned' ELSE 'Not Returned' END AS status " +
                       "FROM borrow_record br " +
                       "LEFT JOIN return_record rr ON br.borrowID = rr.borrowID " +
                       "LEFT JOIN customer c ON br.cusID = c.cusID " +
                       "LEFT JOIN book b ON br.bookID = b.bookID " +
                       "WHERE rr.borrowID = ? ";

        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            try {
                int borrowID = Integer.parseInt(searchText);
                pstmt.setInt(1, borrowID);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Borrow ID must be an integer.");
                return;
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int borrowID = rs.getInt("borrowID");
                String customerName = rs.getString("cusName");
                String title = rs.getString("title");
                int quantity = rs.getInt("quantity");
                String returnDate = rs.getString("return_date");
                String returned_date = rs.getString("returned_date");
                String status = rs.getString("status");

                Return ret = new Return(borrowID, customerName, title, quantity, returnDate, returned_date, status);
                returns.add(ret);
            }

            tbReturn.setItems(returns);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching return records: " + e.getMessage());
        }
    }


    @FXML
    public TableView<Return> getTbReturn() {
        return tbReturn;
    }

    @FXML
    public void AddReturnClick(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Update_Return.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add Return Information");
        stage.showAndWait(); // Wait for the update return window to close
        loadReturns(); // Refresh the return list after updating
                
    }

    @FXML
    public void UpdateReturnClick(MouseEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Update_Return.fxml"));
         Parent root = loader.load();
        
        Update_Return_Control controller = loader.getController();
        controller.setTbReturn(tbReturn); // Pass selected return to the update controller

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Update Return Information");
        stage.showAndWait(); // Wait for the update return window to close
        loadReturns(); // Refresh the return list after updating
                
    }

    @FXML
    public void DeleteReturnClick(MouseEvent event) {
        Return selectedReturn = tbReturn.getSelectionModel().getSelectedItem();
        if (selectedReturn != null) {
            String query = "DELETE FROM return_record WHERE borrowID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, selectedReturn.getBorrowID());
                pstmt.executeUpdate();
                tbReturn.getItems().remove(selectedReturn);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Return record deleted successfully.");

                // Refresh the return list in the main view
                loadReturns();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting return record: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a return record to delete.");
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
