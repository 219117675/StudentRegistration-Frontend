package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import za.ac.cput.domain.Student;
import za.ac.cput.service.StudentService;

import java.io.IOException;
import java.util.List;

public class StudentListController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, Integer> personIdColumn;

    @FXML
    private TableColumn<Student, Integer> studentIdColumn;

    @FXML
    private TableColumn<Student, String> studentNumberColumn;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, String> emailColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private final StudentService studentService =
            new StudentService();

    private final ObservableList<Student> studentList =
            FXCollections.observableArrayList();


    /**
     * FRONTEND
     * Initializes the Student Management screen.
     */
    @FXML
    public void initialize() {

        personIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("personId")
        );

        studentIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentId")
        );

        studentNumberColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentNumber")
        );

        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentEmail")
        );

        studentTable.setItems(studentList);

        loadStudents();
    }


    /**
     * FRONTEND
     * Loads students from the Spring Boot REST API.
     */
    private void loadStudents() {

        try {

            List<Student> students =
                    studentService.getAll();

            studentList.setAll(students);

        } catch (Exception e) {

            showError(
                    "Load Students Error",
                    "Could not load students from the backend.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Searches students by student number,
     * first name, last name or email.
     */
    @FXML
    private void onSearchStudentClick() {

        String searchText = searchField.getText();

        if (searchText == null ||
                searchText.trim().isEmpty()) {

            loadStudents();
            return;
        }

        String search =
                searchText.trim().toLowerCase();

        try {

            List<Student> students =
                    studentService.getAll();

            List<Student> filteredStudents =
                    students.stream()
                            .filter(student -> {

                                String studentNumber =
                                        safe(student.getStudentNumber());

                                String firstName =
                                        safe(student.getFirstName());

                                String lastName =
                                        safe(student.getLastName());

                                String email =
                                        safe(student.getStudentEmail());

                                return studentNumber
                                        .toLowerCase()
                                        .contains(search)

                                        || firstName
                                        .toLowerCase()
                                        .contains(search)

                                        || lastName
                                        .toLowerCase()
                                        .contains(search)

                                        || email
                                        .toLowerCase()
                                        .contains(search);
                            })
                            .toList();

            studentList.setAll(filteredStudents);

        } catch (Exception e) {

            showError(
                    "Search Error",
                    "Could not search students.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Refreshes the student table.
     */
    @FXML
    private void onRefreshStudentClick() {

        searchField.clear();

        loadStudents();
    }


    /**
     * FRONTEND
     * Opens the Add Student screen.
     */
    @FXML
    private void onAddStudentClick() {

        try {

            String fxmlPath =
                    "/za/ac/cput/stdregfrontend/add-student-view.fxml";

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxmlPath)
                    );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Student");

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            loadStudents();

        } catch (IOException e) {

            showError(
                    "Add Student Error",
                    "Could not open the Add Student screen.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Opens the Student Details screen.
     */
    @FXML
    private void onViewStudentClick() {

        Student selectedStudent =
                studentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedStudent == null) {

            showInformation(
                    "View Student",
                    "Please select a student first."
            );

            return;
        }

        try {

            String fxmlPath =
                    "/za/ac/cput/stdregfrontend/student-details-view.fxml";

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxmlPath)
                    );

            Parent root = loader.load();

            StudentDetailsController controller =
                    loader.getController();

            controller.loadStudent(
                    selectedStudent.getStudentId()
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Student Details - "
                            + selectedStudent.getStudentNumber()
            );

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            showError(
                    "View Student Error",
                    "Could not open the Student Details screen.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Opens the Edit Student screen.
     */
    @FXML
    private void onEditStudentClick() {

        Student selectedStudent =
                studentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedStudent == null) {

            showInformation(
                    "Edit Student",
                    "Please select a student first."
            );

            return;
        }

        try {

            String fxmlPath =
                    "/za/ac/cput/stdregfrontend/edit-student-view.fxml";

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxmlPath)
                    );

            Parent root = loader.load();

            EditStudentController controller =
                    loader.getController();

            controller.setStudent(
                    selectedStudent
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Edit Student - "
                            + selectedStudent.getStudentNumber()
            );

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            loadStudents();

        } catch (IOException e) {

            showError(
                    "Edit Student Error",
                    "Could not open the Edit Student screen.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Deletes the selected student.
     */
    @FXML
    private void onDeleteStudentClick() {

        Student selectedStudent =
                studentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedStudent == null) {

            showInformation(
                    "Delete Student",
                    "Please select a student first."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Student"
        );

        confirmation.setHeaderText(
                "Delete Student"
        );

        confirmation.setContentText(
                "Are you sure you want to delete "
                        + selectedStudent.getStudentNumber()
                        + " - "
                        + selectedStudent.getFirstName()
                        + " "
                        + selectedStudent.getLastName()
                        + "?"
        );

        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response == ButtonType.OK) {

                        try {

                            studentService.delete(
                                    selectedStudent.getStudentId()
                            );

                            showInformation(
                                    "Delete Student",
                                    "Student deleted successfully."
                            );

                            loadStudents();

                        } catch (Exception e) {

                            showError(
                                    "Delete Student Error",
                                    "Could not delete the student.",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }


    /**
     * FRONTEND
     * Opens the Link Applicant screen.
     */
    @FXML
    private void onLinkApplicantClick() {

        Student selectedStudent =
                studentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedStudent == null) {

            showInformation(
                    "Link Applicant",
                    "Please select a student first."
            );

            return;
        }

        try {

            String fxmlPath =
                    "/za/ac/cput/stdregfrontend/link-applicant-view.fxml";

            if (getClass().getResource(fxmlPath) == null) {

                throw new IOException(
                        "Link Applicant FXML was not found at: "
                                + fxmlPath
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxmlPath)
                    );

            Parent root = loader.load();

            LinkApplicantController controller =
                    loader.getController();

            controller.setStudent(
                    selectedStudent
            );

            Stage stage = new Stage();

            stage.setTitle(
                    "Link Applicant to Student"
            );

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            /*
             * Reload the student list from the backend
             * after the applicant has been linked.
             */
            loadStudents();

        } catch (Exception e) {

            showError(
                    "Link Applicant Error",
                    "Could not open the Link Applicant screen.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Returns to the Admin Dashboard.
     */
    @FXML
    private void onBackClick(ActionEvent event) {

        try {

            String dashboardPath =
                    "/za/ac/cput/stdregfrontend/admin-dashboard-view.fxml";

            if (getClass().getResource(dashboardPath) == null) {

                throw new IOException(
                        "Admin Dashboard FXML was not found at: "
                                + dashboardPath
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(dashboardPath)
                    );

            Parent root = loader.load();

            Node source =
                    (Node) event.getSource();

            Scene currentScene =
                    source.getScene();

            if (currentScene == null) {

                throw new IOException(
                        "The Back button is not attached to a scene."
                );
            }

            Stage currentStage =
                    (Stage) currentScene.getWindow();

            currentStage.setScene(
                    new Scene(root)
            );

            currentStage.setTitle(
                    "Admin Dashboard"
            );

            currentStage.show();

        } catch (Exception e) {

            showError(
                    "Navigation Error",
                    "Could not return to the Admin Dashboard.",
                    e.getMessage()
            );
        }
    }


    /**
     * FRONTEND
     * Safely handles null String values.
     */
    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }


    /**
     * FRONTEND
     * Displays an information message.
     */
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


    /**
     * FRONTEND
     * Displays an error message.
     */
    private void showError(
            String title,
            String header,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.setContentText(
                message == null ||
                        message.trim().isEmpty()
                        ? "Unknown error."
                        : message
        );

        alert.showAndWait();
    }
}