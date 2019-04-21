module transmissionremote {

    requires jdk.localedata;
    requires java.desktop;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.commons.io;
    requires cordelia;
    requires gson;
    requires java.sql;
    requires jcabi.log;
    requires ch.qos.logback.classic;

    opens com.sergeybochkov.transmissionremote to gson, javafx.fxml;
    opens com.sergeybochkov.transmissionremote.frm to javafx.fxml;
    opens com.sergeybochkov.transmissionremote.ui.cell to javafx.fxml;
    opens com.sergeybochkov.transmissionremote.ui.elems to javafx.fxml;
    opens com.sergeybochkov.transmissionremote.model to gson;

    exports com.sergeybochkov.transmissionremote;
}