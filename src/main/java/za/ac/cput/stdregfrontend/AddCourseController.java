package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Course;
import za.ac.cput.domain.Department;
import za.ac.cput.service.CourseService;
import za.ac.cput.service.DepartmentService;

import java.util.List;

public class AddCourseController {

    @FXML
    private TextField courseCodeField;

    @FXML
    private TextField courseNameField;

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    private final CourseService courseService =
            new CourseService();

    private final DepartmentService departmentService =
            new DepartmentService();

    private final ObservableList<Department> departments =
            FXCollections.observableArrayList();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupDepartmentComboBox();

        loadDepartments();
    }


    // ============================================================
    // DEPARTMENT COMBO BOX
    // ============================================================

    private void setupDepartmentComboBox() {

        departmentComboBox.setItems(
                departments
        );

        departmentComboBox.setCellFactory(
                listView ->
                        new javafx.scene.control.ListCell<>() {

                            @Override
                            protected void updateItem(
                                    Department department,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        department,
                                        empty
                                );

                                if (empty ||
                                        department == null) {

                                    setText(null);

                                } else {

                                    setText(
                                            department.getDepartmentCode()
                                                    + " - "
                                                    + department.getDepartmentName()
                                    );
                                }
                            }
                        }
        );

        departmentComboBox.setButtonCell(
                new javafx.scene.control.ListCell<>() {

                    @Override
                    protected void updateItem(
                            Department department,
                            boolean empty
                    ) {

                        super.updateItem(
                                department,
                                empty
                        );

                        if (empty ||
                                department == null) {

                            setText(null);

                        } else {

                            setText(
                                    department.getDepartmentCode()
                                            + " - "
                                            + department.getDepartmentName()
                            );
                        }
                    }
                }
        );
    }


    // ============================================================
    // LOAD DEPARTMENTS
    // ============================================================

    private void loadDepartments() {

        try {

            List<Department> result =
                    departmentService.getAll();

            departments.setAll(
                    result
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Department Error",
                    "Could not load departments from the backend.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // SAVE
    // ============================================================

    @FXML
    private void onSaveClick() {

        String courseCode =
                courseCodeField.getText();

        String courseName =
                courseNameField.getText();

        Department department =
                departmentComboBox.getValue();


        // --------------------------------------------------------
        // VALIDATION
        // --------------------------------------------------------

        if (courseCode == null ||
                courseCode.trim().isEmpty()) {

            showWarning(
                    "Course code is required."
            );

            courseCodeField.requestFocus();

            return;
        }


        if (courseName == null ||
                courseName.trim().isEmpty()) {

            showWarning(
                    "Course name is required."
            );

            courseNameField.requestFocus();

            return;
        }


        if (department == null) {

            showWarning(
                    "Please select a department."
            );

            departmentComboBox.requestFocus();

            return;
        }


        // --------------------------------------------------------
        // BUILD COURSE
        // --------------------------------------------------------

        Course course =
                new Course.Builder()
                        .setCourseId(0)
                        .setCourseCode(
                                courseCode
                                        .trim()
                                        .toUpperCase()
                        )
                        .setCourseName(
                                courseName.trim()
                        )
                        .setDepartment(
                                department
                        )
                        .build();


        // --------------------------------------------------------
        // SEND TO BACKEND
        // --------------------------------------------------------

        try {

            Course saved =
                    courseService.create(
                            course
                    );


            showInformation(
                    "Course Created",
                    "Course created successfully.\n\n"
                            + "Course ID: "
                            + saved.getCourseId()
                            + "\n"
                            + "Course Code: "
                            + saved.getCourseCode()
                            + "\n"
                            + "Course Name: "
                            + saved.getCourseName()
            );


            closeWindow();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Create Course Error",
                    "Could not create the course.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // CANCEL
    // ============================================================

    @FXML
    private void onCancelClick() {

        closeWindow();
    }


    // ============================================================
    // CLOSE
    // ============================================================

    private void closeWindow() {

        Stage stage =
                (Stage) cancelButton
                        .getScene()
                        .getWindow();

        stage.close();
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
                "Course Management"
        );

        alert.setHeaderText(
                "Attention"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
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
}