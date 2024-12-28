package application;

public class Return {
    private int borrowID;
    private String customerName;
    private String title; // Thay thế trường bookID bằng title
    private int quantity;
    private String returnDate;
    private String returned_date;
    private String status;

    public Return(int borrowID, String customerName, String title, int quantity, String returnDate, String returned_date, String status) {
        this.borrowID = borrowID;
        this.customerName = customerName;
        this.title = title;
        this.quantity = quantity;
        this.returnDate = returnDate;
        this.returned_date = returned_date;
        this.status = status;
    }


    public int getBorrowID() {
        return borrowID;
    }

    public String getCusName() {
        return customerName;
    }


    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getReturned_date() {
        return returned_date;
    }

    public String getStatus() {
        return status;
    }
}
