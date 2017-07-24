package xyzy.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xyzy.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;


/**
 * Контроллер для корневого макета. Корневой макет предоставляет базовый
 * макет приложения, содержащий строку меню и место, где будут размещены
 * остальные элементы JavaFX.
 *
 * Created by Yury on 012 12.07.17.
 */
public class RootLayoutController {
    // Ссылка на главное приложение
    private MainApp mainApp;

    private FXMLLoader fxmlLoaderProduct = null;
    private Parent fxmlAddProduct;
    private ProductController productController;
    private Stage productDialogStage;
    private Stage mainStage;

    /**
     * Вызывается главным приложением, чтобы оставить ссылку на самого себя.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        System.out.println("setMainApp");
        this.mainApp = mainApp;
    }

    /**
     * Открывает диалоговое окно about.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Tailor");
        alert.setHeaderText("About");
        alert.setContentText("Author: Yury Zybaila\nPhone: +375(29)-525-54-67");
        alert.showAndWait();
    }

    /**
     * Добавить/посмотреть модели.
     */
    @FXML
    private void onShowProducts(ActionEvent actionEvent) {
        try {
            if (fxmlLoaderProduct == null) {
                fxmlLoaderProduct = new FXMLLoader();
                fxmlLoaderProduct.setLocation(getClass().getResource("Product.fxml"));
                fxmlAddProduct = fxmlLoaderProduct.load();
                productController = fxmlLoaderProduct.getController();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (productDialogStage == null) {
            productDialogStage = new Stage();
            productDialogStage.setTitle("Products");
            productDialogStage.setMinHeight(300);
            productDialogStage.setMinWidth(500);
            productDialogStage.setResizable(true);
            productDialogStage.setScene(new Scene(fxmlAddProduct));
            productDialogStage.initModality(Modality.WINDOW_MODAL);
            productDialogStage.initOwner(mainStage);
        }

        productDialogStage.showAndWait(); // для ожидания закрытия окна
    }

    /**
     * Закрывает приложение.
     */
    @FXML
    private void handleExit() {
        System.out.println("Exit");
        System.exit(0);
    }
}