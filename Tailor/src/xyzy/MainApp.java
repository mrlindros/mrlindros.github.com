package xyzy;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xyzy.view.ProductController;
import xyzy.view.RootLayoutController;
import xyzy.view.WriteOffController;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Tailor");

        // Устанавливаем иконку приложения.
        this.primaryStage.getIcons().add(new Image("file:src/xyzy/Belarus.png"));

        initRootLayout();
        showOrders();
    }

    /**
     * Инициализирует корневой макет.
     */
    private void initRootLayout() throws IOException {
        System.out.println("Init root panel");

        // Загружаем корневой макет из fxml файла.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
        rootLayout = loader.load();

        // Отображаем сцену, содержащую корневой макет.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        // Даём контроллеру доступ к главному приложению.
        RootLayoutController controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.show();
    }

    /**
     * Показывает в корневом макете сведения о списании готовых продуктов.
     */
    public void showOrders() throws IOException {
        System.out.println("Show orders");

        // Загружаем сведения об адресатах.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/WriteOff.fxml"));
        AnchorPane writeOff = (AnchorPane) loader.load();

        // Помещаем сведения об адресатах в центр корневого макета.
        rootLayout.setCenter(writeOff);

        // Даём контроллеру доступ к главному приложению.
        WriteOffController controller = loader.getController();
        controller.setMainApp(this);
    }

    public static void main(String[] args) {
        System.out.println("Start");
        launch(args);
    }
}
