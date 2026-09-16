package za.ac.cput.stdregfrontend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import za.ac.cput.service.LoginResponse;
import za.ac.cput.service.LoginService;
import za.ac.cput.service.Session;

import java.util.concurrent.CompletableFuture;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginButton;

    private final LoginService loginService =
            new LoginService();


    @FXML
    public void initialize() {

        emailField.setOnAction(
                event -> passwordField.requestFocus()
        );

        passwordField.setOnAction(
                event -> handleLogin()
        );
    }


    @FXML
    private void handleLogin() {

        String email =
                emailField.getText() == null
                        ? ""
                        : emailField.getText().trim();

        String password =
                passwordField.getText() == null
                        ? ""
                        : passwordField.getText();


        if (email.isEmpty()) {

            showMessage(
                    "Please enter your email."
            );

            emailField.requestFocus();

            return;
        }


        if (password.isEmpty()) {

            showMessage(
                    "Please enter your password."
            );

            passwordField.requestFocus();

            return;
        }


        setControlsDisabled(true);

        showMessage(
                "Signing in..."
        );


        CompletableFuture
                .supplyAsync(() -> {

                    try {

                        return loginService.login(
                                email,
                                password
                        );

                    } catch (Exception e) {

                        throw new RuntimeException(e);
                    }

                })
                .whenComplete(
                        (response, error) -> {

                            Platform.runLater(() -> {

                                setControlsDisabled(false);


                                /*
                                 * BACKEND CONNECTION ERROR
                                 */
                                if (error != null) {

                                    error.printStackTrace();

                                    showMessage(
                                            "Unable to connect to the backend server."
                                    );

                                    return;
                                }


                                /*
                                 * NO RESPONSE
                                 */
                                if (response == null) {

                                    showMessage(
                                            "No response received from the server."
                                    );

                                    return;
                                }


                                /*
                                 * LOGIN FAILED
                                 */
                                if (!response.isSuccess()) {

                                    showMessage(
                                            response.getMessage()
                                    );

                                    passwordField.clear();

                                    passwordField.requestFocus();

                                    return;
                                }


                                /*
                                 * LOGIN SUCCESSFUL
                                 *
                                 * Store the logged-in user in
                                 * the application session.
                                 */
                                Session.setCurrentUser(
                                        response
                                );


                                /*
                                 * Open the correct dashboard
                                 * based on the role returned
                                 * by the backend.
                                 */
                                openDashboard(
                                        response.getRole()
                                );

                            });
                        }
                );
    }


    /**
     * Opens the dashboard belonging to
     * the authenticated user's role.
     */
    private void openDashboard(
            String role) {

        /*
         * Make sure the backend actually
         * returned a role.
         */
        if (role == null ||
                role.trim().isEmpty()) {

            showMessage(
                    "Login succeeded, but no account role was returned."
            );

            Session.logout();

            return;
        }


        String normalizedRole =
                role.trim().toUpperCase();


        try {

            /*
             * ADMIN
             */
            if (normalizedRole.equals("ADMIN")) {

                HelloApplication.openDashboard(
                        "ADMIN"
                );

                return;
            }


            /*
             * LECTURER
             */
            if (normalizedRole.equals("LECTURER")) {

                HelloApplication.openDashboard(
                        "LECTURER"
                );

                return;
            }


            /*
             * STUDENT
             */
            if (normalizedRole.equals("STUDENT")) {

                HelloApplication.openDashboard(
                        "STUDENT"
                );

                return;
            }


            /*
             * UNKNOWN ROLE
             */
            showMessage(
                    "Unknown account role: "
                            + role
            );

            Session.logout();


        } catch (Exception e) {

            e.printStackTrace();

            Session.logout();

            showMessage(
                    "Unable to open the dashboard."
            );
        }
    }


    /**
     * Displays a message on the login screen.
     */
    private void showMessage(
            String message) {

        messageLabel.setText(
                message == null
                        ? ""
                        : message
        );
    }


    /**
     * Prevents multiple login requests
     * while the backend request is running.
     */
    private void setControlsDisabled(
            boolean disabled) {

        emailField.setDisable(disabled);

        passwordField.setDisable(disabled);

        loginButton.setDisable(disabled);
    }
}