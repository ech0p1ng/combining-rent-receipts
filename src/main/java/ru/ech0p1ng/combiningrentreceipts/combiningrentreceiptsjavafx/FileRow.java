package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.File;

public class FileRow {
    private String filePath;
    private String directoryPath;
    private TextField filePathTextField;
    private Button openFileButton;
    private Button openDirectoryButton;
    private Button removeFileButton;
    private GridPane fileGridPane;

    public FileRow(File file) {
        this.filePath = file.getAbsolutePath();
        this.directoryPath = file.getParent();


        filePathTextField = new TextField(filePath);
        filePathTextField.setEditable(false);

        openFileButton = getStyledButton("Открыть");
        openDirectoryButton = getStyledButton("Папка");
        removeFileButton = getStyledButton("Удалить");

        fileGridPane = new GridPane();

        var firstColumn = new ColumnConstraints();
        firstColumn.setMaxWidth(Double.MAX_VALUE);
        firstColumn.setFillWidth(true);
        firstColumn.setMaxWidth(-1);
        firstColumn.setHgrow(Priority.SOMETIMES);


        fileGridPane.getColumnConstraints().addAll(
                firstColumn,
                getStyledColumn(),
                getStyledColumn(),
                getStyledColumn());
        fileGridPane.getRowConstraints().add(getStyledRow());

        fileGridPane.add(filePathTextField, 0, 0);
        fileGridPane.add(openFileButton, 1, 0);
        fileGridPane.add(openDirectoryButton, 2, 0);
        fileGridPane.add(removeFileButton, 3, 0);

        VBox.setMargin(fileGridPane, new Insets(0, 0, 5, 0));
    }

    private static Button getStyledButton(String text) {
        Button button = new Button(text);
        int height = 25;
        int width = 70;

        button.setMinWidth(width);
        button.setPrefWidth(width);
        button.setMaxWidth(width);

        button.setMinHeight(height);
        button.setPrefHeight(height);
        button.setMaxHeight(height);

        return button;
    }

    private static RowConstraints getStyledRow() {
        RowConstraints rowConstraints = new RowConstraints();
        int height = 25;
        rowConstraints.setMaxHeight(height);
        rowConstraints.setMinHeight(height);
        rowConstraints.setPrefHeight(height);
        return rowConstraints;
    }

    private static ColumnConstraints getStyledColumn() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        int width = 75;
        columnConstraints.setHalignment(HPos.RIGHT);
        columnConstraints.setMinWidth(width);
        columnConstraints.setMaxWidth(width);

        return columnConstraints;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public TextField getFilePathTextField() {
        return filePathTextField;
    }

    public GridPane getFileGridPane() {
        return fileGridPane;
    }

    public Button getOpenFileButton() {
        return openFileButton;
    }

    public Button getRemoveFileButton() {
        return removeFileButton;
    }

    public Button getOpenDirectoryButton() {
        return openDirectoryButton;
    }
}
