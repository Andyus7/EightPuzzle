module com.prograiii.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.prograiii.demo to javafx.fxml;
    exports com.prograiii.demo;
}