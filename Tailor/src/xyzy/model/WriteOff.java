package xyzy.model;

import javafx.scene.image.Image;

import java.sql.Date;

/**
 * Created by Yury on 017 17.07.17.
 */
public class WriteOff {
    private long ID;
    private long idProduct;
    private int productSize;
    private int count;
    private char cutter; // 2 типа основной и интернет закройщики
    private Date date;

    private String productTitle;
    private Image productImage;


    public void setID(long ID) {
        this.ID = ID;
    }
    public long getID() {
        return ID;
    }

    public void setIdProduct(long idProduct) {
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

    public void setCutter(char cutter) {
        this.cutter = cutter;
    }
    public char getCutter() {
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
