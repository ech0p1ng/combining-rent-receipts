package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import ru.ech0p1ng.combiningRentReceipts.ImageOrientation;
import ru.ech0p1ng.combiningRentReceipts.Receipt;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    List<File> pdfFiles = new ArrayList<>();
    ImageOrientation orientation = ImageOrientation.HORIZONTAL;

    @FXML
    private Button addFilesButton;
    @FXML
    private Button clearFilesListButton;
    @FXML
    private Button mergePdfFilesToPngButton;
    @FXML
    private Button openImageButton;
    @FXML
    private Button openImageFolderButton;
    @FXML
    private Button printImageButton;
    @FXML
    private TextField resultFilePathTextField;
    @FXML
    private VBox vBox;

    private static void showMessageBox(Alert.AlertType alertType, String header, String content) {
        String title = "";

        switch (alertType) {
            case INFORMATION -> title = "Информация";
            case ERROR -> title = "Ошибка!";
            case WARNING -> title = "Внимание!";
            case CONFIRMATION -> title = "Подтверждение";
            case NONE -> title = "";
        }

        Alert alert = new Alert(alertType);

        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.setContentText(content);
        alert.showAndWait();
    }

    private List<File> addFilesViaExplorer() {
        FileChooser fileChooser = new FileChooser(); //Класс работы с диалогом выбора и сохранения

        fileChooser.setTitle("Выбрать документы"); //Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"); //Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenMultipleDialog(HelloApplication.getStage()); //Указываем текущую сцену CodeNote.mainStage
    }

    private File saveFile() {
        FileChooser fileChooser = new FileChooser(); //Класс работы с диалогом выбора и сохранения
        fileChooser.setInitialFileName("Чек " + dateTimeToString(LocalDateTime.now()));
        fileChooser.setTitle("Сохранить документ"); //Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"); //Расширение
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(HelloApplication.getStage());
    }

    private String formatDateTimeNumber(int number) {
        String result = String.valueOf(number);
        while (result.length() < 2) result = "0" + result;
        return result;
    }

    private String dateTimeToString(LocalDateTime dateTime) {
        String year = formatDateTimeNumber(dateTime.getYear());
        String month = formatDateTimeNumber(dateTime.getMonthValue());
        String day = formatDateTimeNumber(dateTime.getDayOfMonth());
        String hour = formatDateTimeNumber(dateTime.getHour());
        String minute = formatDateTimeNumber(dateTime.getMinute());
        String second = formatDateTimeNumber(dateTime.getSecond());
        return year + "-" + month + "-" + day + " " + hour + "-" + minute + "-" + second;
    }

    private void openFileInDefaultProgram(File file) {
        //Открытие файла в программе по-умолчанию
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                showMessageBox(Alert.AlertType.ERROR, e.getMessage(), e.getStackTrace().toString());
            }
        } else {
            showMessageBox(Alert.AlertType.ERROR, "Файловый менеджер", "Невозможно открыть файловый менеджер");
        }
    }

    private void openFolderInExplorer(File directory) {
        //Открытие файла в программе по-умолчанию
        if (directory.isDirectory())
            openFileInDefaultProgram(directory);
    }

    private void printFile(File file) {
        try {
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
            PrintService service = PrintServiceLookup.lookupDefaultPrintService(); //выбор принтера по умолчанию
            InputStream inputStream = new FileInputStream(file);
            Doc doc = new SimpleDoc(inputStream, flavor, null);

            if (service != null) {
                DocPrintJob job = service.createPrintJob();
                PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

                //Разворот изображения если оно ориентировано горизонтально
                if (orientation == ImageOrientation.HORIZONTAL) {
                    attributeSet.add(OrientationRequested.LANDSCAPE);
                } else if (orientation == ImageOrientation.VERTICAL) {
                    attributeSet.add(OrientationRequested.PORTRAIT);
                }

                job.print(doc, attributeSet);
            } else {
                throw new Exception("Принтер не найден!");
            }
        } catch (Exception ex) {
            showMessageBox(Alert.AlertType.ERROR, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    @FXML
    void addFilesButtonClick(ActionEvent event) {
        List<File> tempFiles;
        try {
            tempFiles = new ArrayList<>(addFilesViaExplorer());
        } catch (Exception e) {
            return;
        }

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
            showMessageBox(Alert.AlertType.WARNING, "Повторяющиеся файлы",
                    "Следующие файлы уже были ранее добавлены:\n" + cloneFilesPaths);
        }

        if (tempFiles != null) {
            pdfFiles.addAll(tempFiles);

            for (var file : tempFiles) {
                FileRow fileRow = new FileRow(file);

                //Действие кнопки открытия файла
                fileRow.getOpenFileButton().setOnAction(openFileEvent -> {
                    openFileInDefaultProgram(file);
                });

                //Действие кнопки открытия папки
                fileRow.getOpenDirectoryButton().setOnAction(openDirectoryEvent -> {
                    openFolderInExplorer(file.getParentFile());
                });

                //Действие кнопки удаления файла
                fileRow.getRemoveFileButton().setOnAction(removeFileEvent -> {
                    vBox.getChildren().remove(fileRow.getFileGridPane());
                    pdfFiles.remove(file);
                    if (pdfFiles.isEmpty()) {
                        mergePdfFilesToPngButton.setDisable(true);
                        clearFilesListButton.setDisable(true);
                    }
                });

                vBox.getChildren().add(fileRow.getFileGridPane());

                mergePdfFilesToPngButton.setDisable(false);
                clearFilesListButton.setDisable(false);
            }
        }
    }

    @FXML
    void clearFilesListButtonClick(ActionEvent event) {
        vBox.getChildren().clear();
        pdfFiles.clear();

        openImageButton.setDisable(true);
        openImageFolderButton.setDisable(true);
        printImageButton.setDisable(true);
        mergePdfFilesToPngButton.setDisable(true);
        clearFilesListButton.setDisable(true);
    }

    @FXML
    void mergePdfFilesToPng(ActionEvent event) {
        var buttonText = mergePdfFilesToPngButton.getText();
        mergePdfFilesToPngButton.setText("Обработка...");
        mergePdfFilesToPngButton.setDisable(true);

        var savedFile = saveFile();

        if (savedFile != null) {
            List<String> pdfFilesPaths = new ArrayList<>();
            for (var file : pdfFiles) {
                pdfFilesPaths.add(file.getAbsolutePath());
            }

            Receipt receipt = new Receipt(pdfFilesPaths, savedFile.getAbsolutePath());
            receipt.render(orientation);

            resultFilePathTextField.setText(savedFile.getAbsolutePath());

            openImageButton.setOnAction(openResultEvent -> openFileInDefaultProgram(savedFile));
            openImageFolderButton.setOnAction(openResultFolderEvent -> openFolderInExplorer(savedFile.getParentFile()));
            printImageButton.setOnAction(printResultEvent -> printFile(savedFile));

            openImageButton.setDisable(false);
            openImageFolderButton.setDisable(false);
            printImageButton.setDisable(false);
        }

        mergePdfFilesToPngButton.setText(buttonText);
        mergePdfFilesToPngButton.setDisable(false);
    }
}