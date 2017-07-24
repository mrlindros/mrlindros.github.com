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
import xyzy.model.WriteOff;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Yury on 017 17.07.17.
 */
public class WriteOffController {

    public TableView tWriteOff;
    public TableColumn<WriteOff, String> titleColumn;
    public TableColumn<WriteOff, Number> sizeColumn;
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
        ArrayList<WriteOff> listWriteOffs = db.selectWriteOffs();
        for (int i = 0; i < listWriteOffs.size(); i++) {
            oWriteOffList.add(listWriteOffs.get(i));
        }
        tWriteOff.setItems(oWriteOffList);

        titleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WriteOff, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<WriteOff, String> param) {
                return new SimpleStringProperty(String.valueOf(param.getValue().getProductTitle()));
            }
        });

        sizeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WriteOff, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<WriteOff, Number> param) {
                return new SimpleIntegerProperty(param.getValue().getProductSize());
            }
        });
        tWriteOff.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //iProductPhoto.setImage(new Image(newValue.toString()));
                iProductPhoto.setImage(new Image("file:src/xyzy/Belarus.png"));
            }
            else
            {
                iProductPhoto.setImage(null);
            }
        });
    }
}
