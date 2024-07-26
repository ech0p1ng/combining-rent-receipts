package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }

    public static List<File> addFiles() {
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выбора и сохранения

        fileChooser.setTitle("Выбрать документы");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"); //Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenMultipleDialog(HelloApplication.getStage()); //Указываем текущую сцену CodeNote.mainStage
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Объединение чеков за квартплату");
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
        stage.setScene(scene);
        stage.show();
        HelloApplication.stage = stage;
    }
}