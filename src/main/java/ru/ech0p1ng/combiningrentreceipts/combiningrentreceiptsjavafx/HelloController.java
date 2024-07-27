package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    List<File> files = new ArrayList<File>();

    @FXML
    private Button addFilesButton;

    @FXML
    private Button clearFilesListButton;

    @FXML
    private VBox vBox;

    @FXML
    void addFilesButtonClick(ActionEvent event) {
        var tempFiles = addFilesViaExplorer();

        files.addAll(tempFiles);

        for (var file : tempFiles) {
            FileRow fileRow = new FileRow(file.getAbsolutePath());

            fileRow.getOpenFileButton()
                    .onActionProperty()
                    .set(openFileEvent -> {
                        //Открытие файла в программе по-умолчанию
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.open(file);
                            } catch (IOException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Ошибка");
                                alert.setHeaderText(e.getMessage());
                                alert.setContentText(e.getStackTrace().toString());
                            }
                        } else {
                            System.out.println("Desktop is not supported on this platform.");
                        }
            });
            fileRow.getRemoveFileButton()
                    .onActionProperty()
                    .set(removeFileEvent -> {
                        vBox.getChildren().remove(fileRow.getFileGridPane());

                        files.remove(file);
                    });

            vBox.getChildren().add(fileRow.getFileGridPane());
        }


    }

    @FXML
    void clearFilesListButtonClick(ActionEvent event) {
        files.clear();
        vBox.getChildren().clear();
    }

    public static List<File> addFilesViaExplorer() {
        FileChooser fileChooser = new FileChooser(); //Класс работы с диалогом выбора и сохранения

        fileChooser.setTitle("Выбрать документы"); //Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"); //Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenMultipleDialog(HelloApplication.getStage()); //Указываем текущую сцену CodeNote.mainStage
    }

}
