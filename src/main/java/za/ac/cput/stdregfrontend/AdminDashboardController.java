package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import za.ac.cput.service.Session;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label accountLabel;

    @FXML
    private StackPane contentArea;


    @FXML
    public void initialize() {

        if (!Session.isLoggedIn()) {
            return;
        }

        String firstName =
                Session.getFirstName();

        String lastName =
                Session.getLastName();

        String email =
                Session.getEmail();

        String fullName =
                ((firstName == null ? "" : firstName)
                        + " "
                        + (lastName == null ? "" : lastName))
                        .trim();

        if (fullName.isEmpty()) {
            fullName = "Administrator";
        }

        if (welcomeLabel != null) {
            welcomeLabel.setText(
                    "Welcome, " + fullName
            );
        }

        if (accountLabel != null) {
            accountLabel.setText(
                    email == null ? "" : email
            );
        }
    }


    @FXML
    private void handleApplicants() {

        openView(
                "/za/ac/cput/stdregfrontend/applicant-list-view.fxml"
        );
    }


    @FXML
    private void handleApplications() {

        openView(
                "/za/ac/cput/stdregfrontend/application-list-view.fxml"
        );
    }


    @FXML
    private void handleStudents() {

        openView(
                "/za/ac/cput/stdregfrontend/student-list-view.fxml"
        );
    }


    @FXML
    private void handleDepartments() {

        openView(
                "/za/ac/cput/stdregfrontend/department-list-view.fxml"
        );
    }


    @FXML
    private void handleCourses() {

        openView(
                "/za/ac/cput/stdregfrontend/course-list-view.fxml"
        );
    }


    @FXML
    private void handleLecturers() {

        openView(
                "/za/ac/cput/stdregfrontend/lecturer-list-view.fxml"
        );
    }


    @FXML
    private void handleClasses() {

        openView(
                "/za/ac/cput/stdregfrontend/manage-classes-view.fxml"
        );
    }


    @FXML
    private void handleRegistrations() {

        openView(
                "/za/ac/cput/stdregfrontend/registration-list-view.fxml"
        );
    }


    @FXML
    private void handleReports() {

        showInformation(
                "Reports",
                "Reports will be implemented next."
        );
    }


    @FXML
    private void handleLogout() {

        Session.logout();

        try {

            HelloApplication.openLoginWindow();

        } catch (IOException e) {

            showError(
                    "Logout Error",
                    "Could not return to the login screen.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    /**
     * FRONTEND
     *
     * Loads a module inside the existing Admin Dashboard.
     *
     * The sidebar and header remain visible.
     */
    private void openView(
            String fxmlPath
    ) {

        try {

            if (contentArea == null) {

                throw new IOException(
                        "Dashboard content area is not available."
                );
            }

            if (getClass().getResource(fxmlPath) == null) {

                throw new IOException(
                        "FXML file was not found:\n"
                                + fxmlPath
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxmlPath)
                    );

            Parent root =
                    loader.load();

            /*
             * ONLY replace the content area.
             *
             * The Admin Dashboard itself does not change.
             */
            contentArea
                    .getChildren()
                    .setAll(root);

        } catch (IOException |
                 RuntimeException e) {

            e.printStackTrace();

            showError(
                    "Navigation Error",
                    "Could not open:\n"
                            + fxmlPath
                            + "\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


    private String getFullErrorMessage(
            Throwable e
    ) {

        StringBuilder message =
                new StringBuilder();

        Throwable current =
                e;

        while (current != null) {

            message.append(
                    current.getClass()
                            .getSimpleName()
            );

            if (current.getMessage() != null) {

                message.append(": ");

                message.append(
                        current.getMessage()
                );
            }

            message.append("\n\n");

            current =
                    current.getCause();
        }

        return message.toString();
    }
}