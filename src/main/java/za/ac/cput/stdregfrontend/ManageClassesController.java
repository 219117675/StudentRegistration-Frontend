package za.ac.cput.stdregfrontend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Course;
import za.ac.cput.service.ClassService;
import za.ac.cput.service.CourseService;

import java.io.IOException;
import java.util.List;

public class ManageClassesController {

    // ============================================================
    // FXML COMPONENTS
    // ============================================================

    @FXML
    private TableView<Class> classTable;

    @FXML
    private TableColumn<Class, Number> idColumn;

    @FXML
    private TableColumn<Class, String> codeColumn;

    @FXML
    private TableColumn<Class, String> nameColumn;

    @FXML
    private TableColumn<Class, String> courseColumn;

    @FXML
    private TableColumn<Class, String> lecturerColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button closeButton;


    // ============================================================
    // SERVICES
    // ============================================================

    private final ClassService classService =
            new ClassService();

    private final CourseService courseService =
            new CourseService();


    // ============================================================
    // DATA
    // ============================================================

    private final ObservableList<Class> classList =
            FXCollections.observableArrayList();

    private List<Class> allClasses;


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupTable();

        loadClasses();

        searchField.textProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                filterClasses(newValue)
                );
    }


    // ============================================================
    // TABLE SETUP
    // ============================================================

    private void setupTable() {

        // --------------------------------------------------------
        // ID
        // --------------------------------------------------------

        idColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue().getClassId()
                        )
        );


        // --------------------------------------------------------
        // CLASS CODE
        // --------------------------------------------------------

        codeColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getClassCode()
                        )
        );


        // --------------------------------------------------------
        // CLASS NAME
        // --------------------------------------------------------

        nameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getClassName()
                        )
        );


        // --------------------------------------------------------
        // COURSE
        // --------------------------------------------------------

        courseColumn.setCellValueFactory(
                data -> {

                    Course course =
                            data.getValue().getCourse();

                    String text =
                            course == null
                                    ? ""
                                    : course.getCourseCode();

                    return new SimpleStringProperty(text);
                }
        );


        // --------------------------------------------------------
        // LECTURER
        // --------------------------------------------------------

        lecturerColumn.setCellValueFactory(
                data -> {

                    if (data.getValue().getLecturer()
                            == null) {

                        return new SimpleStringProperty(
                                "Not Assigned"
                        );
                    }

                    return new SimpleStringProperty(
                            data.getValue()
                                    .getLecturer()
                                    .getFirstName()
                                    + " "
                                    + data.getValue()
                                    .getLecturer()
                                    .getLastName()
                    );
                }
        );


        classTable.setItems(classList);
    }


    // ============================================================
    // LOAD CLASSES
    // ============================================================

    private void loadClasses() {

        try {

            allClasses =
                    classService.getAll();

            classList.setAll(allClasses);

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not load classes.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    private void filterClasses(
            String searchText
    ) {

        if (allClasses == null) {
            return;
        }

        if (searchText == null ||
                searchText.isBlank()) {

            classList.setAll(allClasses);

            return;
        }


        String search =
                searchText
                        .trim()
                        .toLowerCase();


        List<Class> filtered =
                allClasses.stream()
                        .filter(courseClass -> {

                            String code =
                                    courseClass
                                            .getClassCode();

                            String name =
                                    courseClass
                                            .getClassName();

                            String courseCode =
                                    courseClass.getCourse() == null
                                            ? ""
                                            : courseClass
                                            .getCourse()
                                            .getCourseCode();


                            return
                                    code.toLowerCase()
                                            .contains(search)

                                            ||

                                            name.toLowerCase()
                                                    .contains(search)

                                            ||

                                            courseCode.toLowerCase()
                                                    .contains(search);
                        })
                        .toList();


        classList.setAll(filtered);
    }


    // ============================================================
    // ADD CLASS
    // ============================================================

    @FXML
    private void onAddClassClick() {

        openClassDialog(null);
    }


    // ============================================================
    // EDIT CLASS
    // ============================================================

    @FXML
    private void onEditClassClick() {

        Class selectedClass =
                classTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedClass == null) {

            showWarning(
                    "Please select a class first."
            );

            return;
        }


        openClassDialog(selectedClass);
    }


    // ============================================================
    // DELETE CLASS
    // ============================================================

    @FXML
    private void onDeleteClassClick() {

        Class selectedClass =
                classTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedClass == null) {

            showWarning(
                    "Please select a class first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Class"
        );

        confirmation.setHeaderText(
                "Delete Class?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete "
                        + selectedClass.getClassCode()
                        + "?"
        );


        var result =
                confirmation.showAndWait();


        if (result.isEmpty() ||
                result.get() != ButtonType.OK) {

            return;
        }


        try {

            classService.delete(
                    selectedClass.getClassId()
            );


            showInformation(
                    "Class deleted successfully."
            );


            loadClasses();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not delete class.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // REFRESH
    // ============================================================

    @FXML
    private void onRefreshClick() {

        searchField.clear();

        loadClasses();
    }


    // ============================================================
    // CLOSE → ADMIN DASHBOARD
    // ============================================================

    @FXML
    private void onCloseClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/admin-dashboard-view.fxml"
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
                    "Student Registration System - Admin Dashboard"
            );

            stage.show();


        } catch (IOException e) {

            e.printStackTrace();

            showError(
                    "Could not return to the Admin Dashboard.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // CLASS DIALOG
    // ============================================================

    private void openClassDialog(
            Class existingClass
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();


        dialog.setTitle(
                existingClass == null
                        ? "Add Class"
                        : "Edit Class"
        );


        dialog.setHeaderText(
                existingClass == null
                        ? "Create a new class"
                        : "Edit class information"
        );


        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );


        ButtonType cancelButton =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        cancelButton
                );


        javafx.scene.layout.GridPane grid =
                new javafx.scene.layout.GridPane();

        grid.setHgap(12);

        grid.setVgap(12);

        grid.setPadding(
                new javafx.geometry.Insets(20)
        );


        // --------------------------------------------------------
        // CLASS CODE
        // --------------------------------------------------------

        TextField codeField =
                new TextField();

        codeField.setPromptText(
                "Example: ICT101-A"
        );


        // --------------------------------------------------------
        // CLASS NAME
        // --------------------------------------------------------

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Example: Programming Group A"
        );


        // --------------------------------------------------------
        // COURSE
        // --------------------------------------------------------

        ComboBox<Course> courseCombo =
                new ComboBox<>();

        courseCombo.setPrefWidth(300);


        try {

            List<Course> courses =
                    courseService.getAll();

            courseCombo.getItems()
                    .setAll(courses);

        } catch (Exception e) {

            showError(
                    "Could not load courses.\n\n"
                            + e.getMessage()
            );

            return;
        }


        // --------------------------------------------------------
        // COURSE DISPLAY
        // --------------------------------------------------------

        courseCombo.setCellFactory(
                listView ->
                        new ListCell<>() {

                            @Override
                            protected void updateItem(
                                    Course course,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        course,
                                        empty
                                );


                                if (empty ||
                                        course == null) {

                                    setText(null);

                                } else {

                                    setText(
                                            course.getCourseCode()
                                                    + " - "
                                                    + course.getCourseName()
                                    );
                                }
                            }
                        }
        );


        courseCombo.setButtonCell(
                new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Course course,
                            boolean empty
                    ) {

                        super.updateItem(
                                course,
                                empty
                        );


                        if (empty ||
                                course == null) {

                            setText(null);

                        } else {

                            setText(
                                    course.getCourseCode()
                                            + " - "
                                            + course.getCourseName()
                            );
                        }
                    }
                }
        );


        // --------------------------------------------------------
        // EDIT VALUES
        // --------------------------------------------------------

        if (existingClass != null) {

            codeField.setText(
                    existingClass.getClassCode()
            );


            nameField.setText(
                    existingClass.getClassName()
            );


            Course existingCourse =
                    existingClass.getCourse();


            if (existingCourse != null) {

                courseCombo.getItems()
                        .stream()
                        .filter(course ->
                                course.getCourseId()
                                        == existingCourse
                                        .getCourseId()
                        )
                        .findFirst()
                        .ifPresent(
                                courseCombo::setValue
                        );
            }
        }


        // --------------------------------------------------------
        // GRID
        // --------------------------------------------------------

        grid.add(
                new Label("Class Code:"),
                0,
                0
        );

        grid.add(
                codeField,
                1,
                0
        );


        grid.add(
                new Label("Class Name:"),
                0,
                1
        );

        grid.add(
                nameField,
                1,
                1
        );


        grid.add(
                new Label("Course:"),
                0,
                2
        );

        grid.add(
                courseCombo,
                1,
                2
        );


        dialog.getDialogPane()
                .setContent(grid);


        // ========================================================
        // SAVE
        // ========================================================

        dialog.setResultConverter(
                button -> {

                    if (button != saveButton) {
                        return null;
                    }


                    String code =
                            codeField
                                    .getText()
                                    .trim();


                    String name =
                            nameField
                                    .getText()
                                    .trim();


                    Course course =
                            courseCombo
                                    .getValue();


                    // ------------------------------------------------
                    // VALIDATION
                    // ------------------------------------------------

                    if (code.isEmpty()) {

                        showWarning(
                                "Class code is required."
                        );

                        return null;
                    }


                    if (name.isEmpty()) {

                        showWarning(
                                "Class name is required."
                        );

                        return null;
                    }


                    if (course == null) {

                        showWarning(
                                "Please select a course."
                        );

                        return null;
                    }


                    // =================================================
                    // CREATE
                    // =================================================

                    try {

                        if (existingClass == null) {

                            /*
                             * ID = 0.
                             *
                             * Backend generates
                             * the real class ID.
                             */

                            Class newClass =
                                    new Class.Builder()
                                            .setClassId(0)
                                            .setClassCode(code)
                                            .setClassName(name)
                                            .setCourse(course)
                                            .setLecturer(null)
                                            .build();


                            classService.create(
                                    newClass
                            );


                            showInformation(
                                    "Class created successfully."
                            );


                        } else {

                            // =========================================
                            // UPDATE
                            // =========================================

                            Class updatedClass =
                                    new Class.Builder()
                                            .setClassId(
                                                    existingClass
                                                            .getClassId()
                                            )
                                            .setClassCode(code)
                                            .setClassName(name)
                                            .setCourse(course)
                                            .setLecturer(
                                                    existingClass
                                                            .getLecturer()
                                            )
                                            .build();


                            classService.update(
                                    updatedClass
                            );


                            showInformation(
                                    "Class updated successfully."
                            );
                        }


                        loadClasses();


                    } catch (Exception e) {

                        e.printStackTrace();

                        showError(
                                "Could not save class.\n\n"
                                        + e.getMessage()
                        );
                    }


                    return button;
                }
        );


        dialog.showAndWait();
    }


    // ============================================================
    // ALERTS
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
                "Attention"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


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
                "Error"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}