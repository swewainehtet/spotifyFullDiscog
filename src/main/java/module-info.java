module com.example.spotifyfulldiscog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires se.michaelthelin.spotify;
    requires org.apache.httpcomponents.core5.httpcore5;

    opens com.example.spotifyfulldiscog to javafx.fxml;
    exports com.example.spotifyfulldiscog;
}