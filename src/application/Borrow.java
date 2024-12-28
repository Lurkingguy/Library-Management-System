package application;

public class Borrow {
    private int borrowID;
    private String cusName;
    private String staffName;
    private String title;
    private int quantity;
    private String released_date;
    private String return_date;

    // Constructor
    public Borrow(int borrowID, String cusName, String staffName, String title, int quantity, String released_date, String return_date) {
        this.borrowID = borrowID;
        this.cusName = cusName;
        this.staffName = staffName;
        this.title = title;
        this.quantity = quantity;
        this.released_date = released_date;
        this.return_date = return_date;
    }

    // Getter and Setter for borrowID
    public int getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(int borrowID) {
        this.borrowID = borrowID;
    }

    // Getter and Setter for cusName
    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    // Getter and Setter for staffName
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter and Setter for released_date
    public String getReleased_date() {
        return released_date;
    }

    public void setReleased_date(String released_date) {
        this.released_date = released_date;
    }

    // Getter and Setter for return_date
    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }
}