module ru.ech0p1ng.combiningrentreceipts {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires static lombok;
    requires static org.apache.pdfbox;
    opens ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx to javafx.fxml;
    exports ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;
}