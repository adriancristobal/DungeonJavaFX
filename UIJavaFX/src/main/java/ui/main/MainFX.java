package ui.main;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class MainFX {
    @Inject
    FXMLLoader fxmlLoader;

    public void start(@Observes @StartupScene Stage stage) {
        try {
            InputStream is = getClass().getResourceAsStream("/fxml/principal.fxml");
            Parent fxmlParent = fxmlLoader.load(
                    getClass().getResourceAsStream("/fxml/principal.fxml"));
            stage.setScene(new Scene(fxmlParent));
            stage.setTitle("Dungeon");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
