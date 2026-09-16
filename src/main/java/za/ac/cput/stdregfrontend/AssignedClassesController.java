package za.ac.cput.stdregfrontend;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.service.ClassService;

import java.io.IOException;
import java.util.List;

public class AssignedClassesController {

    // ============================================================
    // FXML COMPONENTS
    // ============================================================

    @FXML
    private Label lecturerNameLabel;

    @FXML
    private Label lecturerIdLabel;

    @FXML
    private TableView<Class> assignedClassesTable;

    @FXML
    private TableColumn<Class, String> classCodeColumn;

    @FXML
    private TableColumn<Class, String> classNameColumn;

    @FXML
    private TableColumn<Class, String> courseCodeColumn;

    @FXML
    private TableColumn<Class, String> courseNameColumn;

    @FXML
    private TableColumn<Class, String> departmentColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Button unassignButton;

    @FXML
    private Button closeButton;


    // ============================================================
    // SERVICE
    // ============================================================

    private final ClassService classService =
            new ClassService();


    // ============================================================
    // DATA
    // ============================================================

    private Lecturer lecturer;


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupTable();
    }


    // ============================================================
    // LOAD LECTURER
    // ============================================================

    public void loadLecturer(
            Lecturer lecturer
    ) {

        this.lecturer = lecturer;


        if (lecturer == null) {

            showWarning(
                    "No lecturer was selected."
            );

            return;
        }


        lecturerNameLabel.setText(
                lecturer.getFirstName()
                        + " "
                        + lecturer.getLastName()
        );


        lecturerIdLabel.setText(
                String.valueOf(
                        lecturer.getPersonId()
                )
        );


        loadAssignedClasses();
    }


    // ============================================================
    // TABLE SETUP
    // ============================================================

    private void setupTable() {

        // --------------------------------------------------------
        // CLASS CODE
        // --------------------------------------------------------

        classCodeColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getClassCode()
                        )
        );


        // --------------------------------------------------------
        // CLASS NAME
        // --------------------------------------------------------

        classNameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getClassName()
                        )
        );


        // --------------------------------------------------------
        // COURSE CODE
        // --------------------------------------------------------

        courseCodeColumn.setCellValueFactory(
                cellData -> {

                    Class courseClass =
                            cellData.getValue();


                    if (courseClass == null ||
                            courseClass.getCourse() == null) {

                        return new SimpleStringProperty("");
                    }


                    return new SimpleStringProperty(
                            courseClass
                                    .getCourse()
                                    .getCourseCode()
                    );
                }
        );


        // --------------------------------------------------------
        // COURSE NAME
        // --------------------------------------------------------

        courseNameColumn.setCellValueFactory(
                cellData -> {

                    Class courseClass =
                            cellData.getValue();


                    if (courseClass == null ||
                            courseClass.getCourse() == null) {

                        return new SimpleStringProperty("");
                    }


                    return new SimpleStringProperty(
                            courseClass
                                    .getCourse()
                                    .getCourseName()
                    );
                }
        );


        // --------------------------------------------------------
        // DEPARTMENT
        // --------------------------------------------------------

        departmentColumn.setCellValueFactory(
                cellData -> {

                    Class courseClass =
                            cellData.getValue();


                    if (courseClass == null ||
                            courseClass.getCourse() == null ||
                            courseClass
                                    .getCourse()
                                    .getDepartment() == null) {

                        return new SimpleStringProperty("");
                    }


                    return new SimpleStringProperty(
                            courseClass
                                    .getCourse()
                                    .getDepartment()
                                    .getDepartmentName()
                    );
                }
        );


        assignedClassesTable.setItems(
                FXCollections.observableArrayList()
        );
    }


    // ============================================================
    // LOAD ASSIGNED CLASSES
    // ============================================================

    private void loadAssignedClasses() {

        if (lecturer == null) {

            return;
        }


        try {

            int lecturerPersonId =
                    lecturer.getPersonId();


            System.out.println(
                    "Loading assigned classes for lecturer: "
                            + lecturerPersonId
            );


            List<Class> classes =
                    classService
                            .getAssignedClassesForLecturer(
                                    lecturerPersonId
                            );


            if (classes == null) {

                showWarning(
                        "Lecturer was not found."
                );

                return;
            }


            assignedClassesTable.setItems(
                    FXCollections.observableArrayList(
                            classes
                    )
            );


            System.out.println(
                    "Assigned classes loaded: "
                            + classes.size()
            );


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Could not load assigned classes.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // REFRESH
    // ============================================================

    @FXML
    protected void onRefreshClick() {

        loadAssignedClasses();
    }


    // ============================================================
    // UNASSIGN LECTURER
    // ============================================================

    @FXML
    protected void onUnassignClick() {

        // --------------------------------------------------------
        // MAKE SURE A CLASS IS SELECTED
        // --------------------------------------------------------

        Class selectedClass =
                assignedClassesTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedClass == null) {

            showWarning(
                    "Please select a class first."
            );

            return;
        }


        // --------------------------------------------------------
        // MAKE SURE LECTURER EXISTS
        // --------------------------------------------------------

        if (lecturer == null) {

            showWarning(
                    "No lecturer was selected."
            );

            return;
        }


        // --------------------------------------------------------
        // CONFIRMATION
        // --------------------------------------------------------

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Unassign Lecturer"
        );


        confirmation.setHeaderText(
                "Unassign Class?"
        );


        confirmation.setContentText(
                "Are you sure you want to unassign "
                        + selectedClass.getClassCode()
                        + " from "
                        + lecturer.getFirstName()
                        + " "
                        + lecturer.getLastName()
                        + "?"
        );


        var result =
                confirmation.showAndWait();


        if (result.isEmpty() ||
                result.get()
                        != javafx.scene.control.ButtonType.OK) {

            return;
        }


        // --------------------------------------------------------
        // SEND REQUEST TO BACKEND
        // --------------------------------------------------------

        try {

            Class updatedClass =
                    classService.unassignLecturer(
                            selectedClass.getClassId()
                    );


            if (updatedClass == null) {

                showError(
                        "The lecturer could not be unassigned."
                );

                return;
            }


            // ----------------------------------------------------
            // SUCCESS
            // ----------------------------------------------------

            showInformation(
                    "Class "
                            + selectedClass.getClassCode()
                            + " has been successfully unassigned "
                            + "from "
                            + lecturer.getFirstName()
                            + " "
                            + lecturer.getLastName()
                            + "."
            );


            // ----------------------------------------------------
            // RELOAD TABLE
            //
            // The class should disappear because it is no longer
            // assigned to this lecturer.
            // ----------------------------------------------------

            loadAssignedClasses();


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Could not unassign the lecturer.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // CLOSE → LECTURER LIST
    // ============================================================

    @FXML
    protected void onCloseClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/lecturer-list-view.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            Stage stage =
                    (Stage) closeButton
                            .getScene()
                            .getWindow();


            Scene scene =
                    new Scene(root);


            stage.setScene(scene);


            stage.setTitle(
                    "Student Registration System - Lecturers"
            );


            stage.show();


        } catch (IOException e) {

            e.printStackTrace();


            showError(
                    "Could not return to the Lecturer List.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // ERROR MESSAGE
    // ============================================================

    private String getErrorMessage(
            Exception e
    ) {

        if (e.getMessage() != null &&
                !e.getMessage().isBlank()) {

            return e.getMessage();
        }


        if (e.getCause() != null &&
                e.getCause().getMessage() != null) {

            return e.getCause().getMessage();
        }


        return "An unexpected error occurred.";
    }


    // ============================================================
    // INFORMATION
    // ============================================================

    private void showInformation(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Student Registration System"
        );


        alert.setHeaderText(
                "Success"
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
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );


        alert.setTitle(
                "Student Registration System"
        );


        alert.setHeaderText(
                "Assigned Classes"
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
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(
                "Student Registration System"
        );


        alert.setHeaderText(
                "Assigned Classes"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }
}