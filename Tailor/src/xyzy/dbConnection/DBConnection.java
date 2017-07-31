package xyzy.dbConnection;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import xyzy.model.Product;
import xyzy.model.WriteOff;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Yury on 017 17.07.17.
 */
public class DBConnection {

    public static Connection connection;
    public static Statement statement;

    public DBConnection() {
        this.connect();
        createStatement();
    }

    private void createStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Statement created");
    }

    private void connect() {
        this.connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:tailor.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        System.out.println("Connected to DB");
    }

    public void disconnect() {
        try {
            statement.close();
            connection.close();
            statement = null;
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        System.out.println("Disconnected from DB");
    }

    public void reconnect() {
        this.disconnect();
        this.connect();
        System.out.println("Reconnected DB");
    }

    public Product addProduct(Product product) {

        System.out.println("Try to add product to DB");
        if (product.getTitle().isEmpty()) {
            return null;
        }

        String sql = "INSERT INTO 'product' (title, comment, photo) VALUES (?, ?, ?)";

        FileInputStream fis = null;
        PreparedStatement ps = null;

        try {
            connection.setAutoCommit(false);
            //File file = new File("src/xyzy/Belarus.png");

            ps = connection.prepareStatement(sql);

            ps.setString(1, product.getTitle());
            ps.setString(2, product.getComment());
            if (product.getPhoto() != null) {
                File file = new File(product.getPhoto());
                fis = new FileInputStream(file);
                ps.setBinaryStream(3, fis, (int) file.length());
            }


            ps.executeUpdate();
            connection.commit();

            System.out.println("Product is added");
            sql = "SELECT id FROM product WHERE (title) = ? AND (comment) = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, product.getTitle());
            ps.setString(2, product.getComment());

            Product prd = new Product();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prd.setID(rs.getInt("id"));
                prd.setTitle(product.getTitle());
                prd.setComment(product.getComment());
                if (product.getPhoto() != null) {
                    prd.setPhoto(product.getPhoto());
                    prd.setFoto(product.getFoto());
                }
            }
            return prd;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Product isn't added");
        return null;
    }

    public ArrayList<WriteOff> selectWriteOffs() {
        System.out.println("selectWriteOffs()");
        ArrayList<WriteOff> list = new ArrayList();

        String sql = "SELECT 'writeOff'.id, 'writeOff'.product_size, 'writeOff'.count, 'writeOff'.cutter," +
                "'product'.title, 'product'.photo FROM 'writeOff' JOIN 'product' ON 'writeOff'.id_product = 'product'.id";

        try (ResultSet rs  = statement.executeQuery(sql)){
            while (rs.next()) {
                WriteOff wrt = new WriteOff();
                wrt.setID(rs.getInt("id"));
                wrt.setProductSize(rs.getInt("product_size"));
                wrt.setCount(rs.getInt("count"));
                wrt.setCutter(rs.getString("cutter"));
                wrt.setProductTitle(rs.getString("title"));
                if (rs.getBinaryStream("photo") != null) {
                    wrt.setProductPhoto(new Image(rs.getBinaryStream("photo")));
                }
                //wrt.setDate(rs.getDate("date"));

                list.add(wrt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<Product> selectProducts() {
        System.out.println("selectProducts()");
        ArrayList<Product> list = new ArrayList();

        String sql = "SELECT 'product'.id, 'product'.title, 'product'.comment, 'product'.photo FROM 'product'";

        try (ResultSet rs  = statement.executeQuery(sql)){
            while (rs.next()) {
                Product product = new Product();
                product.setID(rs.getInt("id"));
                product.setTitle(rs.getString("title"));
                product.setComment(rs.getString("comment"));
                if (rs.getBinaryStream("photo") != null) {
                    //product.setPhoto(new Image(rs.getBinaryStream("photo")));
                    product.setFoto(new Image(rs.getBinaryStream("photo")));
                }
                //wrt.setDate(rs.getDate("date"));

                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public WriteOff addWriteOff(WriteOff writeOff) {
        System.out.println("Try to add writeOff to DB");
        if (writeOff.getProductTitle().isEmpty()) {
            return null;
        }

        String sql = "INSERT INTO 'writeOff' (id_product, product_size, count, cutter) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(sql);

            ps.setInt(1, writeOff.getIdProduct());
            ps.setInt(2, writeOff.getProductSize());
            ps.setInt(3, writeOff.getCount());
            ps.setString(4, writeOff.getCutter());
            //ps.setDate(5, currentDate)

            ps.executeUpdate();
            connection.commit();

            System.out.println("WriteOff is added");

            sql = "SELECT id FROM writeOff WHERE (id_product) = ? AND (product_size) = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, writeOff.getIdProduct());
            ps.setInt(2, writeOff.getProductSize());

            WriteOff wrt = new WriteOff();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wrt.setID(rs.getInt("id"));
                wrt.setIdProduct(writeOff.getIdProduct());
                wrt.setProductSize(writeOff.getProductSize());
                wrt.setCount(writeOff.getCount());
                wrt.setCutter(writeOff.getCutter());

            }
            return wrt;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("WriteOff isn't added");
        return null;
    }
}
