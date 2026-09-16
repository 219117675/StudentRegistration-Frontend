package za.ac.cput.stdregfrontend;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SplashController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void startSplash() {

        PauseTransition pause =
                new PauseTransition(
                        Duration.seconds(2)
                );

        pause.setOnFinished(
                event -> openLogin()
        );

        pause.play();
    }

    private void openLogin() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/login-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Scene scene =
                    new Scene(
                            root,
                            1100,
                            700
                    );

            stage.setScene(scene);

            stage.setTitle(
                    "Student Registration System - Login"
            );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}