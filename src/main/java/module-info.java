module ru.ech0p1ng.combiningrentreceipts {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    opens ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx to javafx.fxml;
    exports ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;
}