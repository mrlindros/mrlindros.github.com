package xyzy.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import xyzy.MainApp;
import xyzy.dbConnection.DBConnection;
import xyzy.model.Product;
import xyzy.model.WriteOff;

import java.io.IOException;
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


    private Stage primaryStage;


    ObservableList<WriteOff> oWriteOffList = FXCollections.observableArrayList();

    public ObservableList<WriteOff> getWriteOffList() {
        return oWriteOffList;
    }

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

        tWriteOff.getSelectionModel().select(0);
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

    /**
     * Вызывается, когда пользователь кликает по кнопке Добавить
     * Открывает диалоговое окно с дополнительной информацией.
     */
    @FXML
    private void clickAddNewWriteOff(ActionEvent actionEvent) throws IOException {

        System.out.println("WriteOffController.clickAddNewWriteOff()");

        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/WriteOffEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактор списания модели");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём выбранную транзакцию списания модели в контроллер.
            WriteOffEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            //controller.setWriteOff(writeOff);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
            //return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Редактировать...
     * Открывает диалоговое окно для изменения выбранной транзакции списания.
     */
    @FXML
    private void clickEditWriteOff(ActionEvent actionEvent) {
        System.out.println("clickEditWriteOff()");
        WriteOff selectedWriteOff = tWriteOff.getSelectionModel().getSelectedItem();

        if (selectedWriteOff == null) {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Не выбрана транзакция");
            alert.setContentText("Пожалуйста, выберите необходимую для редактирования запись");

            alert.showAndWait();

            return;
        }
        else {
            boolean okClicked = showWriteOffEditDialog(selectedWriteOff);
            if (okClicked) {
               // showPersonDetails(selectedPerson);
            }
        }
/*
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/WriteOffEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактор списания модели");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём выбранную транзакцию списания модели в контроллер.
            WriteOffEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWriteOff(selectedWriteOff);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
            //return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }
*/
    }

    private boolean showWriteOffEditDialog(WriteOff writeOff)
    {
        System.out.println("showWriteOffEditDialog()");

        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/WriteOffEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактор списания модели");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём выбранную транзакцию списания модели в контроллер.
            WriteOffEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWriteOff(writeOff);
            //controller.setListWriteOff(oWriteOffList);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
