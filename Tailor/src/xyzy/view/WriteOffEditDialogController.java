package xyzy.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import xyzy.dbConnection.DBConnection;
import xyzy.model.Product;
import xyzy.model.WriteOff;

import java.util.ArrayList;

/**
 * Created by Yury on 026 26.07.17.
 */
public class WriteOffEditDialogController {

    @FXML
    private ChoiceBox<String> cutterChoiceBox;
    @FXML
    private ChoiceBox<String> modelTitleChoiceBox;
    @FXML
    private TextField sizeField;
    @FXML
    private TextField countField;
    @FXML
    private DatePicker dateWriteOffPicker;

    private Stage dialogStage;
    private WriteOff writeOff;
    private boolean okClicked = false;

    ObservableList<WriteOff> oWriteOffList;

    // коннектор к базе
    private DBConnection db;

    public WriteOffEditDialogController() {
        System.out.println("WriteOffEditDialogController created");
        db = new DBConnection();
    }
    ArrayList<Product> listProducts;
    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {

        listProducts = db.selectProducts();
        for (int i = 0; i < listProducts.size(); i++) {
            modelTitleChoiceBox.getItems().add(listProducts.get(i).getTitle());
        }
        cutterChoiceBox.getItems().add("Основной");
        cutterChoiceBox.getItems().add("Интернет");
        cutterChoiceBox.getItems().add("he");
        cutterChoiceBox.getItems().add("she");
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Задаёт транзакцию списания, информацию о которой будем менять.
     *
     * @param writeOff
     */
    public void setWriteOff(WriteOff writeOff) {
        this.writeOff = writeOff;

        modelTitleChoiceBox.setValue(writeOff.getProductTitle());
        sizeField.setText(String.valueOf(writeOff.getProductSize()));
        cutterChoiceBox.setValue(writeOff.getCutter());
        countField.setText(String.valueOf(writeOff.getCount()));

    }

    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk(ActionEvent actionEvent) {
        System.out.println("handleOk()");
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        System.out.println("handleCancel()");
        dialogStage.close();
    }

    public void setListWriteOff(ObservableList<WriteOff> oWriteOffList) {
        /*this.oWriteOffList = oWriteOffList;
        for (int i = 0; i < oWriteOffList.size(); i++) {
            modelTitleChoiceBox.getItems().add(oWriteOffList.get(i).getProductTitle());
        }*/

    }
}
