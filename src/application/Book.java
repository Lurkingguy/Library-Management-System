package application;


public class Book {
    private int bookID;
    private String title;
    private String categoryName;    // Add this field
    private String author;
    private int publicationYear;
    private int quantity;
    private String status;

    public Book(int bookID, String title, String categoryName, String author, int publicationYear, int quantity, String status) {
        this.bookID = bookID;
        this.title = title;
        this.categoryName = categoryName;
        this.author = author;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.status = status;
    }
    
    // Constructor chỉ với các thuộc tính cần thiết cho Dashboard
    public Book(int bookID, String title, String author, int quantity) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }
    
    
    // Add getter for categoryName
    public String getCategoryName() {
        return categoryName;
    }

    // Getter methods
    public int getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}

