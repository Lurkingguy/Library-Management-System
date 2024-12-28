package application;

public class Category {
    private int cateID;
    private String cate;

    public Category(int cateID, String cate) {
        this.cateID = cateID;
        this.cate = cate;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
}
