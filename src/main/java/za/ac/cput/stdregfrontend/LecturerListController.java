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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import za.ac.cput.domain.Department;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.service.LecturerService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LecturerListController {

    // ============================================================
    // TABLE
    // ============================================================

    @FXML
    private TableView<Lecturer> lecturerTable;

    @FXML
    private TableColumn<Lecturer, Integer> idColumn;

    @FXML
    private TableColumn<Lecturer, Integer> lecturerIdColumn;

    @FXML
    private TableColumn<Lecturer, String> firstNameColumn;

    @FXML
    private TableColumn<Lecturer, String> lastNameColumn;

    @FXML
    private TableColumn<Lecturer, String> employeeNumberColumn;

    @FXML
    private TableColumn<Lecturer, String> emailColumn;

    @FXML
    private TableColumn<Lecturer, String> departmentColumn;


    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    private TextField searchField;


    // ============================================================
    // SERVICE
    // ============================================================

    private final LecturerService lecturerService =
            new LecturerService();


    // ============================================================
    // DATA
    // ============================================================

    private final ObservableList<Lecturer> lecturerData =
            FXCollections.observableArrayList();


    // ============================================================
    // INITIALIZE
    // ============================================================

    /**
     * FRONTEND
     *
     * Initializes the Lecturer Management content screen.
     */
    @FXML
    public void initialize() {

        // --------------------------------------------------------
        // Person ID
        // --------------------------------------------------------

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("personId")
        );


        // --------------------------------------------------------
        // Lecturer ID
        // --------------------------------------------------------

        lecturerIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("lecturerId")
        );


        // --------------------------------------------------------
        // First Name
        // --------------------------------------------------------

        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );


        // --------------------------------------------------------
        // Last Name
        // --------------------------------------------------------

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );


        // --------------------------------------------------------
        // Employee Number
        // --------------------------------------------------------

        employeeNumberColumn.setCellValueFactory(
                new PropertyValueFactory<>("employeeNumber")
        );


        // --------------------------------------------------------
        // Email
        // --------------------------------------------------------

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("lecturerEmail")
        );


        // --------------------------------------------------------
        // Department
        // --------------------------------------------------------

        departmentColumn.setCellValueFactory(
                cellData -> {

                    Lecturer lecturer =
                            cellData.getValue();

                    if (lecturer == null) {

                        return new SimpleStringProperty("");
                    }

                    Department department =
                            lecturer.getDepartment();

                    if (department == null) {

                        return new SimpleStringProperty("");
                    }

                    String departmentText =
                            safe(department.getDepartmentCode())
                                    + " - "
                                    + safe(department.getDepartmentName());

                    return new SimpleStringProperty(
                            departmentText
                    );
                }
        );


        // --------------------------------------------------------
        // Attach data to table
        // --------------------------------------------------------

        lecturerTable.setItems(
                lecturerData
        );


        // --------------------------------------------------------
        // Load from backend
        // --------------------------------------------------------

        loadLecturers();
    }


    // ============================================================
    // LOAD LECTURERS
    // ============================================================

    /**
     * FRONTEND
     *
     * Gets lecturers from the Spring Boot REST API.
     */
    private void loadLecturers() {

        try {

            System.out.println(
                    "Loading lecturers from backend..."
            );

            List<Lecturer> lecturers =
                    lecturerService.getAll();

            lecturerData.setAll(
                    lecturers
            );

            lecturerTable.setItems(
                    lecturerData
            );

            System.out.println(
                    "Lecturers loaded: "
                            + lecturers.size()
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Load Lecturers Error",
                    "Could not load lecturers from the backend.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    /**
     * FRONTEND
     *
     * Searches lecturers by:
     *
     * First Name
     * Last Name
     * Employee Number
     * Email
     */
    @FXML
    private void onSearchClick() {

        String searchText =
                searchField.getText();

        if (searchText == null
                || searchText.trim().isEmpty()) {

            lecturerTable.setItems(
                    lecturerData
            );

            return;
        }

        String search =
                searchText
                        .trim()
                        .toLowerCase();

        List<Lecturer> filteredLecturers =
                lecturerData.stream()
                        .filter(
                                lecturer ->
                                        matchesSearch(
                                                lecturer,
                                                search
                                        )
                        )
                        .collect(
                                Collectors.toList()
                        );

        lecturerTable.setItems(
                FXCollections.observableArrayList(
                        filteredLecturers
                )
        );
    }


    // ============================================================
    // SEARCH MATCH
    // ============================================================

    private boolean matchesSearch(
            Lecturer lecturer,
            String search
    ) {

        if (lecturer == null) {
            return false;
        }

        return contains(
                lecturer.getFirstName(),
                search
        )

                || contains(
                lecturer.getLastName(),
                search
        )

                || contains(
                lecturer.getEmployeeNumber(),
                search
        )

                || contains(
                lecturer.getLecturerEmail(),
                search
        );
    }


    // ============================================================
    // REFRESH
    // ============================================================

    /**
     * FRONTEND
     *
     * Reloads lecturers from the database.
     */
    @FXML
    private void onRefreshClick() {

        searchField.clear();

        loadLecturers();
    }


    // ============================================================
    // ADD LECTURER
    // ============================================================

    /**
     * FRONTEND
     *
     * Opens Add Lecturer as a small dialog.
     *
     * The main Admin Dashboard remains open.
     */
    @FXML
    private void onAddLecturerClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "add-lecturer-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Add Lecturer"
            );

            stage.setScene(
                    new Scene(
                            root,
                            550,
                            500
                    )
            );

            stage.setResizable(false);

            stage.showAndWait();

            loadLecturers();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Add Lecturer Error",
                    "Could not open the Add Lecturer screen.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // VIEW LECTURER
    // ============================================================

    /**
     * FRONTEND
     *
     * Opens the selected lecturer's details.
     */
    @FXML
    private void onViewLecturerClick() {

        Lecturer selectedLecturer =
                lecturerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedLecturer == null) {

            showWarning(
                    "Please select a lecturer first."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "lecturer-details-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            LecturerDetailsController controller =
                    loader.getController();

            controller.loadLecturer(
                    selectedLecturer.getPersonId()
            );

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Lecturer Details"
            );

            stage.setScene(
                    new Scene(
                            root,
                            650,
                            550
                    )
            );

            stage.setResizable(false);

            stage.showAndWait();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "View Lecturer Error",
                    "Could not open lecturer details.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // EDIT LECTURER
    // ============================================================

    /**
     * FRONTEND
     *
     * Opens the selected lecturer for editing.
     */
    @FXML
    private void onEditLecturerClick() {

        Lecturer selectedLecturer =
                lecturerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedLecturer == null) {

            showWarning(
                    "Please select a lecturer first."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "edit-lecturer-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            EditLecturerController controller =
                    loader.getController();

            controller.loadLecturer(
                    selectedLecturer.getPersonId()
            );

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Edit Lecturer"
            );

            stage.setScene(
                    new Scene(
                            root,
                            600,
                            600
                    )
            );

            stage.setResizable(false);

            stage.showAndWait();

            loadLecturers();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Edit Lecturer Error",
                    "Could not open the Edit Lecturer screen.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // DELETE LECTURER
    // ============================================================

    /**
     * FRONTEND
     *
     * Deletes the selected lecturer.
     */
    @FXML
    private void onDeleteLecturerClick() {

        Lecturer selectedLecturer =
                lecturerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedLecturer == null) {

            showWarning(
                    "Please select a lecturer first."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Lecturer"
        );

        confirmation.setHeaderText(
                "Delete Lecturer?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete "
                        + safe(selectedLecturer.getFirstName())
                        + " "
                        + safe(selectedLecturer.getLastName())
                        + "?"
        );

        var result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            try {

                boolean deleted =
                        lecturerService.delete(
                                selectedLecturer.getPersonId()
                        );

                if (deleted) {

                    showInformation(
                            "Lecturer Deleted",
                            "The lecturer was successfully deleted."
                    );

                    loadLecturers();

                } else {

                    showError(
                            "Delete Lecturer Error",
                            "Delete failed.",
                            "The lecturer could not be deleted."
                    );
                }

            } catch (Exception e) {

                e.printStackTrace();

                showError(
                        "Delete Lecturer Error",
                        "Could not delete the lecturer.",
                        getFullErrorMessage(e)
                );
            }
        }
    }


    // ============================================================
    // ASSIGN CLASS
    // ============================================================

    /**
     * FRONTEND
     *
     * Opens the Assign Class dialog for the selected lecturer.
     */
    @FXML
    private void onAssignClassClick() {

        Lecturer selectedLecturer =
                lecturerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedLecturer == null) {

            showWarning(
                    "Please select a lecturer first."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "assign-class-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            AssignClassController controller =
                    loader.getController();

            controller.loadLecturer(
                    selectedLecturer
            );

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Assign Class"
            );

            stage.setScene(
                    new Scene(
                            root,
                            600,
                            500
                    )
            );

            stage.setResizable(false);

            stage.showAndWait();

            loadLecturers();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Assign Class Error",
                    "Could not open the Assign Class screen.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // VIEW ASSIGNED CLASSES
    // ============================================================

    /**
     * FRONTEND
     *
     * Opens the classes assigned to the selected lecturer.
     */
    @FXML
    private void onViewAssignedClassesClick() {

        Lecturer selectedLecturer =
                lecturerTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedLecturer == null) {

            showWarning(
                    "Please select a lecturer first."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "assigned-classes-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            /*
             * If this screen later needs the lecturer object,
             * its controller can receive it here.
             */

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Assigned Classes"
            );

            stage.setScene(
                    new Scene(
                            root,
                            750,
                            550
                    )
            );

            stage.setResizable(false);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Assigned Classes Error",
                    "Could not open assigned classes.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }


    // ============================================================
    // CONTAINS
    // ============================================================

    private boolean contains(
            String value,
            String search
    ) {

        if (value == null
                || search == null) {

            return false;
        }

        return value
                .toLowerCase()
                .contains(search);
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

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

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
                "No Lecturer Selected"
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
                message == null
                        ? "Unknown error."
                        : message
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
                    current.getClass()
                            .getSimpleName()
            );

            if (current.getMessage() != null) {

                message.append(": ")
                        .append(
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