package ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class HelloController {

    @FXML
    private Button addFilesButton;

    @FXML
    private Button clearFilesListButton;

    @FXML
    private TextField filePathTextField;

    @FXML
    private GridPane grid;

    @FXML
    private Button openFileButton;

    @FXML
    private Button removeFileButton;

    @FXML
    private VBox vBox;

    @FXML
    void addFilesButtonClick(ActionEvent event) {
        FileRow fileRow = new FileRow("1");

        vBox.getChildren().add(fileRow.getFileGridPane());
    }

    @FXML
    void clearFilesListButtonClick(ActionEvent event) {

    }







}
