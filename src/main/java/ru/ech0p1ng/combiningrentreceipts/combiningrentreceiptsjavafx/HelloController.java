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
    @FXML
    private Button addFilesButton;

    @FXML
    private Button clearFilesListButton;

    @FXML
    private Button mergePdfFilesToPngButton;

    @FXML
    private VBox vBox;

    List<File> pdfFiles = new ArrayList<>();

    @FXML
    void addFilesButtonClick(ActionEvent event) {
        List<File> tempFiles;
        try {
            tempFiles = new ArrayList<>(addFilesViaExplorer());
        } catch (Exception e) { return; }

        StringBuilder cloneFilesPaths = new StringBuilder();
        List<File> cloneFiles = new ArrayList<>();

        for (var file : tempFiles) {
            if (pdfFiles.contains(file)) {
                cloneFilesPaths.append(file.getAbsolutePath()).append(",\n");
                cloneFiles.add(file);
            }
        }

        if (!cloneFiles.isEmpty()) {
            tempFiles.removeAll(cloneFiles);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Внимание!");
            alert.setHeaderText("Повторяющиеся файлы");

            alert.setContentText("Следующие файлы уже были ранее добавлены:\n" + cloneFilesPaths);
            alert.showAndWait();
        }

        if (tempFiles != null) {
            pdfFiles.addAll(tempFiles);

            for (var file : tempFiles) {
                FileRow fileRow = new FileRow(file.getAbsolutePath());

                //Действие кнопки открытия файла
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
                                    alert.showAndWait();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Ошибка");
                                alert.setHeaderText("Файловый менеджер");
                                alert.setContentText("Невозможно открыть файловый менеджер");
                                alert.showAndWait();
                            }
                        });

                //Действие кнопки закрытия файла
                fileRow.getRemoveFileButton()
                        .onActionProperty()
                        .set(removeFileEvent -> {
                            vBox.getChildren().remove(fileRow.getFileGridPane());

                            pdfFiles.remove(file);
                        });

                vBox.getChildren().add(fileRow.getFileGridPane());
            }
        }
    }

    @FXML
    void clearFilesListButtonClick(ActionEvent event) {
        vBox.getChildren().clear();
        pdfFiles.clear();
    }

    public static List<File> addFilesViaExplorer() {
        FileChooser fileChooser = new FileChooser(); //Класс работы с диалогом выбора и сохранения

        fileChooser.setTitle("Выбрать документы"); //Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"); //Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenMultipleDialog(HelloApplication.getStage()); //Указываем текущую сцену CodeNote.mainStage
    }

    @FXML
    void mergePdfFilesToPng(ActionEvent event) {

    }

}
