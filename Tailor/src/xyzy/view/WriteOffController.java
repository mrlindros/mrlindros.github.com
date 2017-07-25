package xyzy.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import xyzy.MainApp;
import xyzy.dbConnection.DBConnection;
import xyzy.model.Product;
import xyzy.model.WriteOff;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Yury on 017 17.07.17.
 */
public class WriteOffController {

    public TableView<WriteOff> tWriteOff;
    public TableColumn<WriteOff, String> titleColumn;
    public TableColumn<WriteOff, Number> sizeColumn;
    public TableColumn<WriteOff, String> cutterColumn;
    public TableColumn<WriteOff, Number> countColumn;
    public ImageView iProductPhoto;

    // Ссылка на главное приложение.
    private MainApp mainApp;
    // коннектор к базе
    private DBConnection db;

    ObservableList oWriteOffList = FXCollections.observableArrayList();

    public WriteOffController() {
        System.out.println("WriteOffController created");
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

    public void initialize() {
        System.out.println("Initialize of WriteOffController");
        ArrayList<WriteOff> listWriteOffs = db.selectWriteOffs();

        for (int i = 0; i < listWriteOffs.size(); i++) {
            oWriteOffList.add(listWriteOffs.get(i));
        }
        tWriteOff.setItems(oWriteOffList);

        titleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WriteOff, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WriteOff, String> param) {
                return new SimpleStringProperty(param.getValue().getProductTitle());
            }
        });

        sizeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WriteOff, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<WriteOff, Number> param) {
                return new SimpleIntegerProperty(param.getValue().getProductSize());
            }
        });

        cutterColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WriteOff, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WriteOff, String> param) {
                return new SimpleStringProperty(param.getValue().getCutter());
            }
        });

        countColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WriteOff, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<WriteOff, Number> param) {
                return new SimpleIntegerProperty(param.getValue().getCount());
            }
        });

        tWriteOff.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductPhoto(newValue));
    }

    /**
     * Отображает фото модели выбранного заказа.
     * Если указанная модель = null, то поле imageView очищается.
     *
     * @param writeOff — тип WriteOff или null
     */
    private void showProductPhoto(WriteOff writeOff) {
        System.out.println("Show Product Photo");
        if (writeOff != null) {
            iProductPhoto.setImage(writeOff.getProductImage());
        }
    }
}
