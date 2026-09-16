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

public class EditCourseController {

    @FXML
    private TextField courseIdField;

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


    private Course existingCourse;


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        courseIdField.setEditable(false);

        courseCodeField.setEditable(false);

        setupDepartmentComboBox();

        loadDepartments();
    }


    // ============================================================
    // SET COURSE
    // ============================================================

    public void setCourse(
            Course course
    ) {

        this.existingCourse =
                course;

        if (course == null) {
            return;
        }

        courseIdField.setText(
                String.valueOf(
                        course.getCourseId()
                )
        );

        courseCodeField.setText(
                course.getCourseCode()
        );

        courseNameField.setText(
                course.getCourseName()
        );


        if (course.getDepartment() != null) {

            int departmentId =
                    course.getDepartment()
                            .getDepartmentId();

            departmentComboBox.getItems()
                    .stream()
                    .filter(department ->
                            department.getDepartmentId()
                                    == departmentId
                    )
                    .findFirst()
                    .ifPresent(
                            departmentComboBox::setValue
                    );
        }
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

            /*
             * If setCourse() was called before the
             * departments finished loading, select
             * the existing department now.
             */

            if (existingCourse != null &&
                    existingCourse.getDepartment() != null) {

                int departmentId =
                        existingCourse
                                .getDepartment()
                                .getDepartmentId();

                departmentComboBox.getItems()
                        .stream()
                        .filter(department ->
                                department.getDepartmentId()
                                        == departmentId
                        )
                        .findFirst()
                        .ifPresent(
                                departmentComboBox::setValue
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Department Error",
                    "Could not load departments.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // SAVE CHANGES
    // ============================================================

    @FXML
    private void onSaveClick() {

        if (existingCourse == null) {

            showError(
                    "Edit Course",
                    "No course was selected."
            );

            return;
        }

        String courseName =
                courseNameField.getText();

        Department department =
                departmentComboBox.getValue();


        // --------------------------------------------------------
        // VALIDATION
        // --------------------------------------------------------

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
        // BUILD UPDATED COURSE
        // --------------------------------------------------------

        Course updatedCourse =
                new Course.Builder()
                        .setCourseId(
                                existingCourse
                                        .getCourseId()
                        )
                        .setCourseCode(
                                existingCourse
                                        .getCourseCode()
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
                    courseService.update(
                            updatedCourse
                    );

            showInformation(
                    "Course Updated",
                    "Course updated successfully.\n\n"
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
                    "Update Course Error",
                    "Could not update the course.\n\n"
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