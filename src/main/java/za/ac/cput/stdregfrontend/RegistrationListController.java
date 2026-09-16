package za.ac.cput.stdregfrontend;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Course;
import za.ac.cput.domain.Registration;
import za.ac.cput.domain.Student;
import za.ac.cput.service.RegistrationService;

import java.io.IOException;
import java.util.List;

public class RegistrationListController {

    // ============================================================
    // FXML
    // ============================================================

    @FXML
    private TableView<Registration> registrationTable;

    @FXML
    private TableColumn<Registration, String> registrationIdColumn;

    @FXML
    private TableColumn<Registration, String> studentColumn;

    @FXML
    private TableColumn<Registration, String> classColumn;

    @FXML
    private TableColumn<Registration, String> courseColumn;

    @FXML
    private TextField searchField;


    // ============================================================
    // SERVICE / DATA
    // ============================================================

    private final RegistrationService registrationService =
            new RegistrationService();

    private final ObservableList<Registration> registrationList =
            FXCollections.observableArrayList();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupTable();

        registrationTable.setItems(
                registrationList
        );

        loadRegistrations();
    }


    // ============================================================
    // TABLE SETUP
    // ============================================================

    private void setupTable() {

        registrationIdColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        data.getValue()
                                                .getRegistrationId()
                                )
                        )
        );


        studentColumn.setCellValueFactory(
                data -> {

                    Registration registration =
                            data.getValue();

                    Student student =
                            registration.getStudent();

                    if (student == null) {

                        return new SimpleStringProperty(
                                "Not assigned"
                        );
                    }

                    String studentNumber =
                            safe(
                                    student.getStudentNumber()
                            );

                    String firstName =
                            safe(
                                    student.getFirstName()
                            );

                    String lastName =
                            safe(
                                    student.getLastName()
                            );

                    return new SimpleStringProperty(
                            studentNumber
                                    + " - "
                                    + firstName
                                    + " "
                                    + lastName
                    );
                }
        );


        classColumn.setCellValueFactory(
                data -> {

                    Registration registration =
                            data.getValue();

                    Class courseClass =
                            registration.getCourseClass();

                    if (courseClass == null) {

                        return new SimpleStringProperty(
                                "Not assigned"
                        );
                    }

                    return new SimpleStringProperty(
                            safe(
                                    courseClass
                                            .getClassCode()
                            )
                                    + " - "
                                    + safe(
                                    courseClass
                                            .getClassName()
                            )
                    );
                }
        );


        courseColumn.setCellValueFactory(
                data -> {

                    Registration registration =
                            data.getValue();

                    Class courseClass =
                            registration.getCourseClass();

                    if (courseClass == null ||
                            courseClass.getCourse() == null) {

                        return new SimpleStringProperty(
                                "Not assigned"
                        );
                    }

                    Course course =
                            courseClass.getCourse();

                    return new SimpleStringProperty(
                            safe(
                                    course.getCourseCode()
                            )
                                    + " - "
                                    + safe(
                                    course.getCourseName()
                            )
                    );
                }
        );
    }


    // ============================================================
    // LOAD REGISTRATIONS
    // ============================================================

    private void loadRegistrations() {

        try {

            System.out.println(
                    "Loading registrations from backend..."
            );

            List<Registration> registrations =
                    registrationService.getAll();

            registrationList.setAll(
                    registrations
            );

            System.out.println(
                    "Registrations loaded: "
                            + registrations.size()
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Registration Loading Error",
                    "Could not load registrations.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    private void onSearchClick() {

        String searchText =
                searchField.getText();


        if (searchText == null ||
                searchText.trim().isEmpty()) {

            loadRegistrations();

            return;
        }


        String search =
                searchText
                        .trim()
                        .toLowerCase();


        try {

            List<Registration> registrations =
                    registrationService.getAll();

            ObservableList<Registration> filtered =
                    FXCollections.observableArrayList();


            for (Registration registration :
                    registrations) {

                String registrationId =
                        String.valueOf(
                                registration
                                        .getRegistrationId()
                        );


                String studentText =
                        getStudentSearchText(
                                registration
                        );


                String classText =
                        getClassSearchText(
                                registration
                        );


                String courseText =
                        getCourseSearchText(
                                registration
                        );


                if (registrationId.contains(search)
                        || studentText.contains(search)
                        || classText.contains(search)
                        || courseText.contains(search)) {

                    filtered.add(
                            registration
                    );
                }
            }


            registrationList.setAll(
                    filtered
            );


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Search Error",
                    "Could not search registrations.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // REFRESH
    // ============================================================

    @FXML
    private void onRefreshClick() {

        searchField.clear();

        loadRegistrations();
    }


    // ============================================================
    // ADD REGISTRATION
    // ============================================================

    @FXML
    private void onAddRegistrationClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/add-registration-view.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            Stage stage =
                    new Stage();


            stage.setTitle(
                    "Register Student"
            );


            stage.setScene(
                    new Scene(
                            root,
                            650,
                            450
                    )
            );


            stage.showAndWait();


            loadRegistrations();


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Add Registration Error",
                    "Could not open Add Registration.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // VIEW REGISTRATION
    // ============================================================

    @FXML
    private void onViewRegistrationClick() {

        Registration selected =
                registrationTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showInformation(
                    "View Registration",
                    "Please select a registration first."
            );

            return;
        }


        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/registration-details-view.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            RegistrationDetailsController controller =
                    loader.getController();


            controller.setRegistration(
                    selected
            );


            Stage stage =
                    new Stage();


            stage.setTitle(
                    "Registration Details"
            );


            stage.setScene(
                    new Scene(
                            root,
                            650,
                            500
                    )
            );


            stage.showAndWait();


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "View Registration Error",
                    "Could not open Registration Details.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // DELETE / UNREGISTER
    // ============================================================

    @FXML
    private void onDeleteRegistrationClick() {

        Registration selected =
                registrationTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showInformation(
                    "Delete Registration",
                    "Please select a registration first."
            );

            return;
        }


        String studentText =
                getStudentSearchText(
                        selected
                );


        String classText =
                getClassSearchText(
                        selected
                );


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Cancel Registration"
        );


        confirmation.setHeaderText(
                "Cancel Registration?"
        );


        confirmation.setContentText(
                "Student:\n"
                        + studentText
                        + "\n\n"
                        + "Class:\n"
                        + classText
                        + "\n\n"
                        + "This action cannot be undone."
        );


        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response ==
                            ButtonType.OK) {

                        unregister(
                                selected
                        );
                    }
                });
    }


    // ============================================================
    // UNREGISTER
    // ============================================================

    private void unregister(
            Registration registration
    ) {

        try {

            Student student =
                    registration.getStudent();

            Class courseClass =
                    registration.getCourseClass();


            if (student == null ||
                    courseClass == null) {

                showError(
                        "Registration Error",
                        "The registration does not contain a valid student and class."
                );

                return;
            }


            boolean removed =
                    registrationService
                            .unregisterStudent(
                                    student.getStudentId(),
                                    courseClass.getClassId()
                            );


            if (removed) {

                showInformation(
                        "Registration Cancelled",
                        "The student has been unregistered from the class."
                );


                loadRegistrations();


            } else {

                showError(
                        "Registration Error",
                        "The registration could not be found."
                );
            }


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unregister Error",
                    "Could not cancel the registration.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // BACK
    // ============================================================

    @FXML
    private void onBackClick() {

        try {

            HelloApplication.openDashboard(
                    "ADMIN"
            );


        } catch (IOException e) {

            e.printStackTrace();

            showError(
                    "Navigation Error",
                    "Could not return to Admin Dashboard.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH TEXT HELPERS
    // ============================================================

    private String getStudentSearchText(
            Registration registration
    ) {

        if (registration == null ||
                registration.getStudent() == null) {

            return "";
        }


        Student student =
                registration.getStudent();


        return (
                safe(
                        student.getStudentNumber()
                )
                        + " "
                        + safe(
                        student.getFirstName()
                )
                        + " "
                        + safe(
                        student.getLastName()
                )
                        + " "
                        + safe(
                        student.getStudentEmail()
                )
        ).toLowerCase();
    }


    private String getClassSearchText(
            Registration registration
    ) {

        if (registration == null ||
                registration.getCourseClass() == null) {

            return "";
        }


        Class courseClass =
                registration.getCourseClass();


        return (
                safe(
                        courseClass.getClassCode()
                )
                        + " "
                        + safe(
                        courseClass.getClassName()
                )
        ).toLowerCase();
    }


    private String getCourseSearchText(
            Registration registration
    ) {

        if (registration == null ||
                registration.getCourseClass() == null ||
                registration.getCourseClass()
                        .getCourse() == null) {

            return "";
        }


        Course course =
                registration.getCourseClass()
                        .getCourse();


        return (
                safe(
                        course.getCourseCode()
                )
                        + " "
                        + safe(
                        course.getCourseName()
                )
        ).toLowerCase();
    }


    // ============================================================
    // SAFE TEXT
    // ============================================================

    private String safe(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not provided";
        }

        return value;
    }


    // ============================================================
    // INFORMATION
    // ============================================================

    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                title
        );


        alert.setHeaderText(
                null
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
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(
                title
        );


        alert.setHeaderText(
                null
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // ============================================================
    // FULL ERROR MESSAGE
    // ============================================================

    private String getFullErrorMessage(
            Throwable e
    ) {

        StringBuilder message =
                new StringBuilder();


        Throwable current =
                e;


        while (current != null) {

            message.append(
                    current
                            .getClass()
                            .getSimpleName()
            );


            if (current.getMessage() != null) {

                message.append(
                        ": "
                );


                message.append(
                        current.getMessage()
                );
            }


            message.append(
                    "\n\n"
            );


            current =
                    current.getCause();
        }


        return message.toString();
    }
}