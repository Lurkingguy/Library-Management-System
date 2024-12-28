package application;

public class Customer {
    private int cusID;
    private String cusName;
    private String phone;

    public Customer(int cusID, String cusName, String phone) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.phone = phone;
    }

    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
