package za.ac.cput.stdregfrontend;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Registration;
import za.ac.cput.domain.Student;
import za.ac.cput.service.RegistrationService;
import za.ac.cput.service.Session;
import za.ac.cput.service.StudentService;

import java.io.IOException;
import java.util.List;

public class StudentDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label studentNumberLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label courseLabel;

    @FXML
    private Label registeredCountLabel;

    @FXML
    private Label availableCountLabel;

    @FXML
    private TableView<Class> availableClassesTable;

    @FXML
    private TableColumn<Class, Integer> availableIdColumn;

    @FXML
    private TableColumn<Class, String> availableCodeColumn;

    @FXML
    private TableColumn<Class, String> availableNameColumn;

    @FXML
    private TableColumn<Class, String> availableCourseColumn;

    @FXML
    private TableView<Registration> registeredClassesTable;

    @FXML
    private TableColumn<Registration, Integer> registrationIdColumn;

    @FXML
    private TableColumn<Registration, String> registeredCodeColumn;

    @FXML
    private TableColumn<Registration, String> registeredNameColumn;

    @FXML
    private TableColumn<Registration, String> registeredCourseColumn;

    @FXML
    private Button registerButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button profileButton;

    private final StudentService studentService =
            new StudentService();

    private final RegistrationService registrationService =
            new RegistrationService();

    private final ObservableList<Class> availableClasses =
            FXCollections.observableArrayList();

    private final ObservableList<Registration> registeredClasses =
            FXCollections.observableArrayList();

    private Student currentStudent;

    @FXML
    public void initialize() {

        configureAvailableClassesTable();

        configureRegisteredClassesTable();

        loadStudentInformation();
    }

    private void configureAvailableClassesTable() {

        if (availableClassesTable == null) {
            return;
        }

        if (availableIdColumn != null) {

            availableIdColumn.setCellValueFactory(
                    cellData ->
                            new javafx.beans.property.SimpleObjectProperty<>(
                                    cellData.getValue()
                                            .getClassId()
                            )
            );
        }

        if (availableCodeColumn != null) {

            availableCodeColumn.setCellValueFactory(
                    cellData -> {

                        Class courseClass =
                                cellData.getValue();

                        if (courseClass == null) {
                            return new SimpleStringProperty("");
                        }

                        return new SimpleStringProperty(
                                safe(courseClass.getClassCode())
                        );
                    }
            );
        }

        if (availableNameColumn != null) {

            availableNameColumn.setCellValueFactory(
                    cellData -> {

                        Class courseClass =
                                cellData.getValue();

                        if (courseClass == null) {
                            return new SimpleStringProperty("");
                        }

                        return new SimpleStringProperty(
                                safe(courseClass.getClassName())
                        );
                    }
            );
        }

        if (availableCourseColumn != null) {

            availableCourseColumn.setCellValueFactory(
                    cellData -> {

                        Class courseClass =
                                cellData.getValue();

                        if (courseClass == null ||
                                courseClass.getCourse() == null) {

                            return new SimpleStringProperty("");
                        }

                        return new SimpleStringProperty(
                                safe(
                                        courseClass
                                                .getCourse()
                                                .getCourseName()
                                )
                        );
                    }
            );
        }

        availableClassesTable.setItems(
                availableClasses
        );
    }

    private void configureRegisteredClassesTable() {

        if (registeredClassesTable == null) {
            return;
        }

        if (registrationIdColumn != null) {

            registrationIdColumn.setCellValueFactory(
                    cellData ->
                            new javafx.beans.property.SimpleObjectProperty<>(
                                    cellData.getValue()
                                            .getRegistrationId()
                            )
            );
        }

        if (registeredCodeColumn != null) {

            registeredCodeColumn.setCellValueFactory(
                    cellData -> {

                        Registration registration =
                                cellData.getValue();

                        if (registration == null ||
                                registration.getCourseClass() == null) {

                            return new SimpleStringProperty("");
                        }

                        return new SimpleStringProperty(
                                safe(
                                        registration
                                                .getCourseClass()
                                                .getClassCode()
                                )
                        );
                    }
            );
        }

        if (registeredNameColumn != null) {

            registeredNameColumn.setCellValueFactory(
                    cellData -> {

                        Registration registration =
                                cellData.getValue();

                        if (registration == null ||
                                registration.getCourseClass() == null) {

                            return new SimpleStringProperty("");
                        }

                        return new SimpleStringProperty(
                                safe(
                                        registration
                                                .getCourseClass()
                                                .getClassName()
                                )
                        );
                    }
            );
        }

        if (registeredCourseColumn != null) {

            registeredCourseColumn.setCellValueFactory(
                    cellData -> {

                        Registration registration =
                                cellData.getValue();

                        if (registration == null ||
                                registration.getCourseClass() == null ||
                                registration.getCourseClass()
                                        .getCourse() == null) {

                            return new SimpleStringProperty("");
                        }

                        return new SimpleStringProperty(
                                safe(
                                        registration
                                                .getCourseClass()
                                                .getCourse()
                                                .getCourseName()
                                )
                        );
                    }
            );
        }

        registeredClassesTable.setItems(
                registeredClasses
        );
    }

    private void loadStudentInformation() {

        Integer personId =
                Session.getPersonId();

        if (personId == null ||
                personId <= 0) {

            showError(
                    "No logged-in student was found."
            );

            return;
        }

        try {

            List<Student> students =
                    studentService.getAll();

            currentStudent = null;

            if (students != null) {

                for (Student student : students) {

                    if (student == null) {
                        continue;
                    }

                    if (student.getPersonId() == personId) {

                        currentStudent = student;

                        break;
                    }
                }
            }

            if (currentStudent == null) {

                showError(
                        "Could not find your student profile "
                                + "in the database."
                );

                return;
            }

            updateStudentInformation();

            loadRegisteredClasses();

            loadAvailableClasses();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not load student information.\n\n"
                            + e.getMessage()
            );
        }
    }

    private void updateStudentInformation() {

        if (currentStudent == null) {
            return;
        }

        String firstName =
                safe(currentStudent.getFirstName());

        String lastName =
                safe(currentStudent.getLastName());

        String fullName =
                (firstName + " " + lastName).trim();

        if (welcomeLabel != null) {

            welcomeLabel.setText(
                    fullName.isEmpty()
                            ? "Welcome, Student"
                            : "Welcome, " + fullName
            );
        }

        if (studentNumberLabel != null) {

            studentNumberLabel.setText(
                    safe(
                            currentStudent
                                    .getStudentNumber()
                    )
            );
        }

        if (emailLabel != null) {

            emailLabel.setText(
                    safe(
                            currentStudent
                                    .getStudentEmail()
                    )
            );
        }

        if (courseLabel != null) {

            courseLabel.setText(
                    getStudentCourseName()
            );
        }
    }

    private String getStudentCourseName() {

        if (currentStudent == null) {
            return "Not available";
        }

        if (currentStudent.getApplicant() == null) {
            return "Not available";
        }

        return "Enrolled Course";
    }

    private void loadRegisteredClasses() {

        if (currentStudent == null) {
            return;
        }

        int studentId =
                currentStudent.getStudentId();

        if (studentId <= 0) {

            showError(
                    "Your student ID could not be determined."
            );

            return;
        }

        try {

            List<Registration> result =
                    registrationService.getForStudent(
                            studentId
                    );

            registeredClasses.clear();

            if (result != null) {

                registeredClasses.addAll(
                        result
                );
            }

            updateRegisteredCount();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not load your registered classes.\n\n"
                            + e.getMessage()
            );
        }
    }

    private void loadAvailableClasses() {

        /*
         * Available classes will be loaded from the
         * Class REST API once ClassService is connected.
         *
         * No hardcoded class data is used here.
         */

        availableClasses.clear();

        updateAvailableCount();
    }

    @FXML
    private void handleRegister() {

        if (currentStudent == null) {

            showError(
                    "Your student profile has not been loaded."
            );

            return;
        }

        if (availableClassesTable == null) {
            return;
        }

        Class selectedClass =
                availableClassesTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedClass == null) {

            showInformation(
                    "Please select a class first."
            );

            return;
        }

        int studentId =
                currentStudent.getStudentId();

        int classId =
                selectedClass.getClassId();

        try {

            boolean alreadyRegistered =
                    registrationService
                            .isStudentRegistered(
                                    studentId,
                                    classId
                            );

            if (alreadyRegistered) {

                showInformation(
                        "You are already registered "
                                + "for this class."
                );

                return;
            }

            registrationService.registerStudent(
                    studentId,
                    classId
            );

            showInformation(
                    "Registration successful."
            );

            loadRegisteredClasses();

            loadAvailableClasses();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not register for the class.\n\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void handleRefresh() {

        loadStudentInformation();
    }

    @FXML
    private void handleProfile() {

        showInformation(
                "Student profile module will be connected "
                        + "to the database next."
        );
    }

    @FXML
    private void handleLogout() {

        Session.logout();

        try {

            HelloApplication.openLoginWindow();

        } catch (IOException e) {

            e.printStackTrace();

            showError(
                    "Could not return to the login screen.\n\n"
                            + e.getMessage()
            );
        }
    }

    private void updateRegisteredCount() {

        if (registeredCountLabel == null) {
            return;
        }

        registeredCountLabel.setText(
                String.valueOf(
                        registeredClasses.size()
                )
        );
    }

    private void updateAvailableCount() {

        if (availableCountLabel == null) {
            return;
        }

        availableCountLabel.setText(
                String.valueOf(
                        availableClasses.size()
                )
        );
    }

    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }

    private void showInformation(String message) {

        Platform.runLater(() -> {

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Student Registration System"
            );

            alert.setHeaderText(
                    "Student Dashboard"
            );

            alert.setContentText(
                    message
            );

            alert.showAndWait();
        });
    }

    private void showError(String message) {

        Platform.runLater(() -> {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setTitle(
                    "Student Registration System"
            );

            alert.setHeaderText(
                    "Student Dashboard"
            );

            alert.setContentText(
                    message
            );

            alert.showAndWait();
        });
    }
}