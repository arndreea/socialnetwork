module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens socialnetwork to javafx.fxml;
    exports socialnetwork;

    opens socialnetwork.controller to javafx.fxml;
    exports socialnetwork.controller;

    opens socialnetwork.model to javafx.fxml;
    exports socialnetwork.model;


}