package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.service.ClassService;
import za.ac.cput.service.Session;

import java.io.IOException;
import java.util.List;

public class LecturerDashboardController {

    // ============================================================
    // FXML COMPONENTS
    // ============================================================

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label accountLabel;

    @FXML
    private Label personIdLabel;

    @FXML
    private Label classCountLabel;

    @FXML
    private TableView<Class> classesTable;

    @FXML
    private TableColumn<Class, Number> classIdColumn;

    @FXML
    private TableColumn<Class, String> classCodeColumn;

    @FXML
    private TableColumn<Class, String> classNameColumn;

    @FXML
    private TableColumn<Class, String> courseColumn;

    @FXML
    private TableColumn<Class, String> departmentColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button logoutButton;


    // ============================================================
    // SERVICE
    // ============================================================

    private final ClassService classService =
            new ClassService();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupTable();

        loadLecturerInformation();

        loadMyClasses();
    }


    // ============================================================
    // SETUP TABLE
    // ============================================================

    private void setupTable() {

        classIdColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleIntegerProperty(
                                data.getValue().getClassId()
                        )
        );


        classCodeColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().getClassCode()
                        )
        );


        classNameColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().getClassName()
                        )
        );


        courseColumn.setCellValueFactory(
                data -> {

                    Class courseClass =
                            data.getValue();

                    if (courseClass == null ||
                            courseClass.getCourse() == null) {

                        return new javafx.beans.property
                                .SimpleStringProperty("");
                    }

                    return new javafx.beans.property
                            .SimpleStringProperty(
                            courseClass
                                    .getCourse()
                                    .getCourseCode()
                    );
                }
        );


        departmentColumn.setCellValueFactory(
                data -> {

                    Class courseClass =
                            data.getValue();

                    if (courseClass == null ||
                            courseClass.getCourse() == null ||
                            courseClass
                                    .getCourse()
                                    .getDepartment() == null) {

                        return new javafx.beans.property
                                .SimpleStringProperty("");
                    }

                    return new javafx.beans.property
                            .SimpleStringProperty(
                            courseClass
                                    .getCourse()
                                    .getDepartment()
                                    .getDepartmentName()
                    );
                }
        );
    }


    // ============================================================
    // LOAD LECTURER INFORMATION
    // ============================================================

    private void loadLecturerInformation() {

        if (!Session.isLoggedIn()) {

            welcomeLabel.setText(
                    "Welcome"
            );

            accountLabel.setText(
                    "No active session"
            );

            personIdLabel.setText(
                    "-"
            );

            return;
        }


        String firstName =
                Session.getFirstName();

        String lastName =
                Session.getLastName();

        String email =
                Session.getEmail();

        Integer personId =
                Session.getPersonId();


        if (firstName == null ||
                firstName.isBlank()) {

            firstName = "Lecturer";
        }


        if (lastName == null ||
                lastName.isBlank()) {

            lastName = "";
        }


        welcomeLabel.setText(
                "Welcome, "
                        + firstName
                        + " "
                        + lastName
        );


        if (email != null &&
                !email.isBlank()) {

            accountLabel.setText(
                    email
            );

        } else {

            accountLabel.setText(
                    "Lecturer Account"
            );
        }


        personIdLabel.setText(
                personId == null
                        ? "-"
                        : String.valueOf(personId)
        );
    }


    // ============================================================
    // LOAD MY CLASSES
    // ============================================================

    private void loadMyClasses() {

        Integer personId =
                Session.getPersonId();


        if (personId == null ||
                personId <= 0) {

            classesTable.setItems(
                    FXCollections.observableArrayList()
            );

            classCountLabel.setText(
                    "0"
            );

            showWarning(
                    "No lecturer is currently logged in."
            );

            return;
        }


        try {

            System.out.println(
                    "Loading classes for lecturer Person ID: "
                            + personId
            );


            List<Class> classes =
                    classService
                            .getAssignedClassesForLecturer(
                                    personId
                            );


            if (classes == null) {

                classes =
                        FXCollections.observableArrayList();
            }


            classesTable.setItems(
                    FXCollections.observableArrayList(
                            classes
                    )
            );


            classCountLabel.setText(
                    String.valueOf(
                            classes.size()
                    )
            );


            System.out.println(
                    "My classes loaded: "
                            + classes.size()
            );


        } catch (Exception e) {

            e.printStackTrace();


            classCountLabel.setText(
                    "0"
            );


            showError(
                    "Could not load your classes.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // REFRESH
    // ============================================================

    @FXML
    private void handleRefresh() {

        loadLecturerInformation();

        loadMyClasses();
    }


    // ============================================================
    // PROFILE
    // ============================================================

    @FXML
    private void handleProfile() {

        showInformation(
                "My Profile\n\n"
                        + "Name: "
                        + getFullName()
                        + "\n"
                        + "Email: "
                        + safeValue(
                        Session.getEmail()
                )
                        + "\n"
                        + "Person ID: "
                        + safeValue(
                        Session.getPersonId()
                )
        );
    }


    // ============================================================
    // LOGOUT
    // ============================================================

    @FXML
    private void handleLogout() {

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Logout"
        );


        confirmation.setHeaderText(
                "Logout from Student Registration System?"
        );


        confirmation.setContentText(
                "You will be returned to the login screen."
        );


        var result =
                confirmation.showAndWait();


        if (result.isEmpty() ||
                result.get()
                        != javafx.scene.control.ButtonType.OK) {

            return;
        }


        Session.logout();


        try {

            HelloApplication.openLoginWindow();

        } catch (IOException e) {

            e.printStackTrace();

            showError(
                    "Could not return to the login screen.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // FULL NAME
    // ============================================================

    private String getFullName() {

        String firstName =
                Session.getFirstName();

        String lastName =
                Session.getLastName();


        if (firstName == null) {

            firstName = "";
        }


        if (lastName == null) {

            lastName = "";
        }


        String fullName =
                (
                        firstName
                                + " "
                                + lastName
                ).trim();


        if (fullName.isBlank()) {

            return "Lecturer";
        }


        return fullName;
    }


    // ============================================================
    // SAFE VALUE
    // ============================================================

    private String safeValue(
            Object value) {

        if (value == null) {

            return "-";
        }


        String text =
                String.valueOf(value);


        if (text.isBlank()) {

            return "-";
        }


        return text;
    }


    // ============================================================
    // ERROR MESSAGE
    // ============================================================

    private String getErrorMessage(
            Exception e) {

        if (e.getMessage() != null &&
                !e.getMessage().isBlank()) {

            return e.getMessage();
        }


        if (e.getCause() != null &&
                e.getCause().getMessage() != null &&
                !e.getCause().getMessage().isBlank()) {

            return e.getCause().getMessage();
        }


        return "An unexpected error occurred.";
    }


    // ============================================================
    // INFORMATION
    // ============================================================

    private void showInformation(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Student Registration System"
        );


        alert.setHeaderText(
                "Lecturer"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // ============================================================
    // WARNING
    // ============================================================

    private void showWarning(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );


        alert.setTitle(
                "Student Registration System"
        );


        alert.setHeaderText(
                "Lecturer Dashboard"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // ============================================================
    // ERROR
    // ============================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(
                "Student Registration System"
        );


        alert.setHeaderText(
                "Lecturer Dashboard"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }
}