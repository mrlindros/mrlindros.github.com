package xyzy.model;

import javafx.scene.image.Image;

import java.sql.Date;

/**
 * Created by Yury on 017 17.07.17.
 */
public class WriteOff {
    private int ID;
    private int idProduct;
    private int productSize;
    private int count;
    private String cutter; // 2 типа основной и интернет закройщики
    private Date date;

    private String productTitle;
    private Image productImage;


    public void setID(int ID) {
        this.ID = ID;
    }
    public long getID() {
        return ID;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
    public long getIdProduct() {
        return idProduct;
    }

    public void setProductSize(int productSize) {
        this.productSize = productSize;
    }
    public int getProductSize() {
        return productSize;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setCutter(String cutter) {
        this.cutter = cutter;
    }
    public String getCutter() {
        return cutter;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return this.date;
    }


    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public String getProductTitle() {
        return productTitle;
    }

    public void setProductPhoto(Image productImage) {
        this.productImage = productImage;
    }
    public Image getProductImage() {
        return productImage;
    }
}
