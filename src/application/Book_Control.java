package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Book_Control {
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
    Button btnCategory;
    @FXML
    Button btnLogout;

    // func
    @FXML
    TextField txtSearch;
    @FXML
    Button btnSearch;
    @FXML
    Button btnAddBook;
    @FXML
    Button btnUpdateBook;
    @FXML
    Button btnDeleteBook;

    // view
    @FXML
    private TableView<Book> tbBook;
    @FXML
    private TableColumn<Book, Integer> colId;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colCategory;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, Integer> colPublicationYear;
    @FXML
    private TableColumn<Book, Integer> colQuantity;
    @FXML
    private TableColumn<Book, String> colStatus;
    
    @FXML
    private String loggedInUserName;
    
    
    @FXML
    public void initialize() {
        // Initialize columns
        colId.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublicationYear.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data into TableView
        loadBooks();
    }
    
    public void setLoggedInUserName(String userName) {
        this.loggedInUserName = userName;
        staffName.setText(loggedInUserName);                                    // Set the label text here
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
    public void loadBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();

        String query = "SELECT b.bookID, b.title, c.cate, b.author, b.publication_year, b.quantity, b.status " +
                       "FROM book b " +
                       "JOIN category c ON b.cateID = c.cateID";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int bookID = rs.getInt("bookID");
                String title = rs.getString("title");
                String categoryName = rs.getString("cate");
                String author = rs.getString("author");
                int publicationYear = rs.getInt("publication_year");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                Book book = new Book(bookID, title, categoryName, author, publicationYear, quantity, status);
                books.add(book);
            }

            tbBook.setItems(books);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading books: " + e.getMessage());
        }
    }

    @FXML
    public TableView<Book> getTbBook() {
        return tbBook;
    }

    @FXML
    public void SearchClick(MouseEvent event) {
        String searchText = txtSearch.getText();
        ObservableList<Book> books = FXCollections.observableArrayList();

        String query = "SELECT b.bookID, b.title, c.cate, b.author, b.publication_year, b.quantity, b.status " +
                       "FROM book b " +
                       "JOIN category c ON b.cateID = c.cateID " +
                       "WHERE b.title LIKE ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookID = rs.getInt("bookID");
                String title = rs.getString("title");
                String categoryName = rs.getString("cate");
                String author = rs.getString("author");
                int publicationYear = rs.getInt("publication_year");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                Book book = new Book(bookID, title, categoryName, author, publicationYear, quantity, status);
                books.add(book);
            }

            tbBook.setItems(books);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Error fetching books: " + e.getMessage());
        }
    }

    @FXML
    public void AddBookClick(MouseEvent event) throws IOException {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/New_Book.fxml"));
         Parent root = loader.load();
        
         Stage stage = new Stage();
         stage.setScene(new Scene(root));
         stage.setTitle("Add New Book Record");
         stage.showAndWait(); 
         loadBooks();
    }

    @FXML
    public void UpdateBookClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_final/Update_Book.fxml"));
        Parent root = loader.load();

        Update_Book_Control controller = loader.getController();
        controller.setTbBook(tbBook);                  // Pass tbBook reference to UpdateBookControl

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Update Book Information");
        stage.showAndWait();                           // Wait for the update book window to close
        loadBooks();                                  // Refresh the book list after updating
    }

    @FXML
    public void DeleteBookClick(MouseEvent event) {
        Book selectedBook = tbBook.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String query = "DELETE FROM book WHERE bookID = ?";
            try (Connection conn = Connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, selectedBook.getBookID());
                pstmt.executeUpdate();
                tbBook.getItems().remove(selectedBook);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book deleted successfully.");

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting book: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "Please select a book to delete.");
        }
    }
    
    public int calculateTotalQuantity() {
        int totalQuantity = 0;
        String query = "SELECT SUM(quantity) AS totalQuantity FROM book";
        
        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                totalQuantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error calculating total quantity: " + e.getMessage());
        }
        
        return totalQuantity;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
