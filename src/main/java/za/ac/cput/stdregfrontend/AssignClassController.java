package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.service.ClassService;

import java.util.List;

public class AssignClassController {

    // ============================================================
    // FXML COMPONENTS
    // ============================================================

    @FXML
    private Label lecturerNameLabel;

    @FXML
    private Label lecturerIdLabel;

    @FXML
    private ComboBox<Class> classComboBox;


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

        setupClassComboBox();
    }


    // ============================================================
    // CLASS COMBO BOX
    // ============================================================

    private void setupClassComboBox() {

        classComboBox.setCellFactory(
                listView ->
                        new ListCell<>() {

                            @Override
                            protected void updateItem(
                                    Class courseClass,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        courseClass,
                                        empty
                                );


                                if (empty ||
                                        courseClass == null) {

                                    setText(null);

                                } else {

                                    setText(
                                            courseClass.getClassCode()
                                                    + " - "
                                                    + courseClass.getClassName()
                                    );
                                }
                            }
                        }
        );


        classComboBox.setButtonCell(
                new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Class courseClass,
                            boolean empty
                    ) {

                        super.updateItem(
                                courseClass,
                                empty
                        );


                        if (empty ||
                                courseClass == null) {

                            setText(null);

                        } else {

                            setText(
                                    courseClass.getClassCode()
                                            + " - "
                                            + courseClass.getClassName()
                            );
                        }
                    }
                }
        );
    }


    // ============================================================
    // LOAD SELECTED LECTURER
    // ============================================================

    /**
     * Receives the lecturer selected from LecturerListController.
     */
    public void loadLecturer(
            Lecturer lecturer
    ) {

        if (lecturer == null) {

            showError(
                    "No lecturer information was provided."
            );

            return;
        }


        this.lecturer = lecturer;


        // --------------------------------------------------------
        // DISPLAY LECTURER NAME
        // --------------------------------------------------------

        lecturerNameLabel.setText(
                lecturer.getFirstName()
                        + " "
                        + lecturer.getLastName()
        );


        // --------------------------------------------------------
        // DISPLAY PERSON ID
        // --------------------------------------------------------

        lecturerIdLabel.setText(
                String.valueOf(
                        lecturer.getPersonId()
                )
        );


        // --------------------------------------------------------
        // LOAD AVAILABLE CLASSES
        // --------------------------------------------------------

        loadAvailableClasses();
    }


    // ============================================================
    // LOAD AVAILABLE CLASSES
    // ============================================================

    private void loadAvailableClasses() {

        if (lecturer == null) {

            showWarning(
                    "No lecturer has been selected."
            );

            return;
        }


        try {

            int lecturerPersonId =
                    lecturer.getPersonId();


            System.out.println(
                    "Loading available classes for lecturer: "
                            + lecturerPersonId
            );


            List<Class> classes =
                    classService
                            .getAvailableClassesForLecturer(
                                    lecturerPersonId
                            );


            if (classes == null) {

                showError(
                        "Lecturer was not found."
                );

                return;
            }


            classComboBox.setItems(
                    FXCollections.observableArrayList(
                            classes
                    )
            );


            // ----------------------------------------------------
            // CLEAR CURRENT SELECTION
            // ----------------------------------------------------

            classComboBox
                    .getSelectionModel()
                    .clearSelection();


            System.out.println(
                    "Available classes loaded: "
                            + classes.size()
            );


            // ----------------------------------------------------
            // NO CLASSES AVAILABLE
            // ----------------------------------------------------

            if (classes.isEmpty()) {

                showInformation(
                        "No Classes Available",
                        "There are currently no unassigned classes "
                                + "available for this lecturer's department."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Could not load available classes.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // ASSIGN CLASS
    // ============================================================

    @FXML
    protected void onAssignClick() {

        // --------------------------------------------------------
        // CHECK LECTURER
        // --------------------------------------------------------

        if (lecturer == null) {

            showWarning(
                    "No lecturer has been selected."
            );

            return;
        }


        // --------------------------------------------------------
        // CHECK CLASS
        // --------------------------------------------------------

        Class selectedClass =
                classComboBox
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedClass == null) {

            showWarning(
                    "Please select a class first."
            );

            return;
        }


        // --------------------------------------------------------
        // ASSIGN CLASS
        // --------------------------------------------------------

        try {

            System.out.println(
                    "Assigning class "
                            + selectedClass.getClassId()
                            + " to lecturer "
                            + lecturer.getPersonId()
            );


            Class assignedClass =
                    classService.assignLecturer(
                            selectedClass.getClassId(),
                            lecturer.getPersonId()
                    );


            if (assignedClass == null) {

                showError(
                        "The class could not be assigned."
                );

                return;
            }


            // ----------------------------------------------------
            // SUCCESS
            // ----------------------------------------------------

            showSuccess(
                    "Class Assigned",
                    "Class "
                            + assignedClass.getClassCode()
                            + " was successfully assigned to "
                            + lecturer.getFirstName()
                            + " "
                            + lecturer.getLastName()
                            + "."
            );


            // ----------------------------------------------------
            // IMPORTANT
            //
            // Reload the available classes.
            //
            // The class just assigned will disappear from this
            // list because it now has a lecturer.
            // ----------------------------------------------------

            loadAvailableClasses();


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Could not assign class.\n\n"
                            + getErrorMessage(e)
            );
        }
    }


    // ============================================================
    // CANCEL / CLOSE
    // ============================================================

    @FXML
    protected void onCancelClick() {

        Stage stage =
                (Stage) classComboBox
                        .getScene()
                        .getWindow();


        stage.close();
    }


    // ============================================================
    // SUCCESS ALERT
    // ============================================================

    private void showSuccess(
            String title,
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
                title
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // ============================================================
    // INFORMATION ALERT
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
                "Student Registration System"
        );


        alert.setHeaderText(
                title
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // ============================================================
    // WARNING ALERT
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
                "Assign Class"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // ============================================================
    // ERROR ALERT
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
                "Assign Class"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
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
}