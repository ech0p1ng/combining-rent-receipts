module ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;

    opens ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx to javafx.fxml;
    exports ru.ech0p1ng.combiningrentreceipts.combiningrentreceiptsjavafx;
}