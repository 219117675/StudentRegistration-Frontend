package za.ac.cput.stdregfrontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage)
            throws IOException {

        primaryStage = stage;

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/za/ac/cput/stdregfrontend/splash-view.fxml"
                        )
                );

        Parent root =
                loader.load();

        SplashController controller =
                loader.getController();

        controller.setStage(stage);

        Scene scene =
                new Scene(
                        root,
                        1100,
                        700
                );

        stage.setTitle(
                "Student Registration System"
        );

        stage.setScene(scene);

        stage.show();

        controller.startSplash();
    }


    public static void openLoginWindow()
            throws IOException {

        FXMLLoader loader =
                new FXMLLoader(
                        HelloApplication.class.getResource(
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

        primaryStage.setTitle(
                "Student Registration System - Login"
        );

        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void openDashboard(
            String role)
            throws IOException {

        String fxml;

        switch (role.toUpperCase()) {

            case "ADMIN":

                fxml =
                        "/za/ac/cput/stdregfrontend/admin-dashboard-view.fxml";

                break;

            case "LECTURER":

                fxml =
                        "/za/ac/cput/stdregfrontend/lecturer-dashboard-view.fxml";

                break;

            case "STUDENT":

                fxml =
                        "/za/ac/cput/stdregfrontend/student-dashboard-view.fxml";

                break;

            default:

                throw new IllegalArgumentException(
                        "Unknown role: " + role
                );
        }


        FXMLLoader loader =
                new FXMLLoader(
                        HelloApplication.class.getResource(
                                fxml
                        )
                );

        Parent root =
                loader.load();

        Scene scene =
                new Scene(
                        root,
                        1200,
                        750
                );

        primaryStage.setTitle(
                "Student Registration System - "
                        + role
        );

        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {

        launch();
    }
}