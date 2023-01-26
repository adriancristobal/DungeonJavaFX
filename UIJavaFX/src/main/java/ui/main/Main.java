package ui.main;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.screens.principal.PrincipalController;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main {
    @Inject
    FXMLLoader fxmlLoader;

    public void start(@Observes @StartupScene Stage stage) {
        try {
            Parent fxmlParent = fxmlLoader.load(
                    getClass().getResourceAsStream("/fxml/principalScreen.fxml"));

            stage.setScene(new Scene(fxmlParent));
            stage.setTitle("Dungeon");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
