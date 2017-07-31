package xyzy.model;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by Yury on 021 21.07.17.
 */
public class Product {
    private int ID;
    private String title;
    private String comment;
    private String photo;
    private Image foto;

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setPhoto(String photo) {
        System.out.print("Set Photo as STRINGPATH = ");
        System.out.println(photo);
        this.photo = photo;
    }
    //Public void setPhoto(Image photo) {
    //    this.photo = photo.toString();
    //}
    public String getPhoto() {
        System.out.print("Get Photo as STRINGPATH = ");
        System.out.println(photo);
        return photo;
    }

    public void setFoto(Image foto) {
        System.out.print("Set Foto as IMAGE = ");
        System.out.println(foto);
        this.foto = foto;
    }
    public Image getFoto() {
        System.out.print("Get Foto as IMAGE = ");
        System.out.println(foto);
        return foto;
    }

}
