package xyzy.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.awt.image.ImageDecoder;
import xyzy.MainApp;
import xyzy.dbConnection.DBConnection;
import xyzy.model.Product;

import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Yury on 021 21.07.17.
 */
public class ProductController {

    public ImageView productPhoto;
    public File filePhoto;
    public TableView<Product> tProduct;
    public TableColumn<Product, Number> IDColumn;
    public TableColumn<Product, String> TitleColumn;
    public TableColumn<Product, String> CommentColumn;
    public TextField tProductTitle;
    public TextField tProductComment;

    private Stage dialogStage;

    // Ссылка на главное приложение.
    private MainApp mainApp;
    // коннектор к базе
    private DBConnection db;

    ObservableList oProductList = FXCollections.observableArrayList();

    public ProductController() {
        System.out.println("ProductController created");
        db = new DBConnection();
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        ArrayList<Product> listProducts = db.selectProducts();
        for (int i = 0; i < listProducts.size(); i++) {
            oProductList.add(listProducts.get(i));
        }
        tProduct.setItems(oProductList);

        IDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Product, Number> param) {
                return new SimpleLongProperty(param.getValue().getID());
            }
        });

        TitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> param) {
                return new SimpleStringProperty(param.getValue().getTitle());
            }
        });

        CommentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> param) {
                return new SimpleStringProperty(param.getValue().getComment());
            }
        });

        //productPhoto.s
        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        tProduct.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductPhoto(newValue));

        tProductTitle.textProperty().addListener((observable, oldValue, newValue) -> validate(tProductTitle));
    }

    private void validate(TextField tf) {
        System.out.println("Validate");
        ObservableList<String> styleClass = tf.getStyleClass();
        if (tf.getText().trim().length()==0) {
            System.out.println("Validate == 0");
            if (! styleClass.contains("error")) {
                System.out.println("Validate error");
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            System.out.println("Validate else");
            styleClass.removeAll(Collections.singleton("error"));
        }
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (tProductTitle.getText() == null || tProductTitle.getText().length() == 0) {
            errorMessage += "Введите название модели\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Неверный ввод");
            alert.setHeaderText("Пожалуйста, заполните необходимые поля");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    /**
     * Отображает фото выбранной модели.
     * Если указанная модель = null, то поле imageView очищается.
     *
     * @param product — модель типа Product или null
     */
    private void showProductPhoto(Product product) {
        System.out.println("Show Product Photo");
        if (product != null) {
            productPhoto.setImage(product.getFoto());
        }
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public void clickAddProductPhoto(ActionEvent actionEvent) {
        System.out.println("Select photo clicked");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                //new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                //new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                //new FileChooser.ExtensionFilter("All Files", "*.*")
                );
        dialogStage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            filePhoto = selectedFile;
            Image image = new Image(selectedFile.toURI().toString());
            productPhoto.setImage(image);
            System.out.println("Photo has been selected!");
        }
    }

    public void clickAddProduct(ActionEvent actionEvent) {
        System.out.println("Add product clicked");
        if (isInputValid()) {
            if (tProductTitle.getText() != null) {
                Product product = new Product();

                String title = tProductTitle.getText();
                String comment = tProductComment.getText();

                product.setTitle(title);
                product.setComment(comment);

                if (filePhoto != null) {
                    String photo = filePhoto.getAbsolutePath();
                    product.setPhoto(photo);
                    product.setFoto(new Image(filePhoto.toURI().toString()));
                }

                Product result = db.addProduct(product);

                if (result != null) {
                    oProductList.add(result);
                    System.out.println("Product is added to observable list");
                    tProductTitle.clear();
                    tProductComment.clear();
                    productPhoto.setImage(null);
                }
            }
            dialogStage.close();
        }
    }
}
