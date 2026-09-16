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

import za.ac.cput.domain.Course;
import za.ac.cput.domain.Department;
import za.ac.cput.service.CourseService;

import java.io.IOException;
import java.util.List;

public class CourseListController {

    // ============================================================
    // FXML COMPONENTS
    // ============================================================

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, Integer> courseIdColumn;

    @FXML
    private TableColumn<Course, String> courseCodeColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> departmentColumn;

    @FXML
    private TextField searchField;


    // ============================================================
    // SERVICE / DATA
    // ============================================================

    private final CourseService courseService =
            new CourseService();

    private final ObservableList<Course> courseList =
            FXCollections.observableArrayList();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        // --------------------------------------------------------
        // COURSE ID
        // --------------------------------------------------------

        courseIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseId")
        );


        // --------------------------------------------------------
        // COURSE CODE
        // --------------------------------------------------------

        courseCodeColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseCode")
        );


        // --------------------------------------------------------
        // COURSE NAME
        // --------------------------------------------------------

        courseNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("courseName")
        );


        // --------------------------------------------------------
        // DEPARTMENT
        // --------------------------------------------------------
        /*
         * Do NOT use:
         *
         * new PropertyValueFactory<>("department")
         *
         * because that displays Department.toString().
         *
         * Instead, display the department code and name.
         */

        departmentColumn.setCellValueFactory(
                cellData -> {

                    Course course =
                            cellData.getValue();

                    if (course == null ||
                            course.getDepartment() == null) {

                        return new SimpleStringProperty(
                                "Not assigned"
                        );
                    }

                    Department department =
                            course.getDepartment();

                    String code =
                            safe(
                                    department.getDepartmentCode()
                            );

                    String name =
                            safe(
                                    department.getDepartmentName()
                            );

                    String displayText =
                            code
                                    + " - "
                                    + name;

                    return new SimpleStringProperty(
                            displayText
                    );
                }
        );


        // --------------------------------------------------------
        // TABLE DATA
        // --------------------------------------------------------

        courseTable.setItems(
                courseList
        );


        // --------------------------------------------------------
        // LOAD FROM BACKEND
        // --------------------------------------------------------

        loadCourses();
    }


    // ============================================================
    // LOAD COURSES
    // ============================================================

    private void loadCourses() {

        try {

            System.out.println(
                    "Loading courses from backend..."
            );

            List<Course> courses =
                    courseService.getAll();

            courseList.setAll(
                    courses
            );

            System.out.println(
                    "Courses loaded: "
                            + courses.size()
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Course Loading Error",
                    "Could not load courses from the backend.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    private void onSearchClick() {

        String search =
                searchField.getText();

        if (search == null ||
                search.trim().isEmpty()) {

            loadCourses();

            return;
        }

        search =
                search.trim()
                        .toLowerCase();


        try {

            List<Course> courses =
                    courseService.getAll();

            ObservableList<Course> filtered =
                    FXCollections.observableArrayList();


            for (Course course : courses) {

                // ------------------------------------------------
                // COURSE ID
                // ------------------------------------------------

                String id =
                        String.valueOf(
                                course.getCourseId()
                        );


                // ------------------------------------------------
                // COURSE CODE
                // ------------------------------------------------

                String code =
                        course.getCourseCode() == null
                                ? ""
                                : course
                                .getCourseCode()
                                .toLowerCase();


                // ------------------------------------------------
                // COURSE NAME
                // ------------------------------------------------

                String name =
                        course.getCourseName() == null
                                ? ""
                                : course
                                .getCourseName()
                                .toLowerCase();


                // ------------------------------------------------
                // DEPARTMENT
                // ------------------------------------------------

                String departmentText =
                        "";

                if (course.getDepartment() != null) {

                    Department department =
                            course.getDepartment();

                    departmentText =
                            (
                                    safe(
                                            department
                                                    .getDepartmentCode()
                                    )
                                            + " "
                                            + safe(
                                            department
                                                    .getDepartmentName()
                                    )
                            ).toLowerCase();
                }


                // ------------------------------------------------
                // MATCH
                // ------------------------------------------------

                if (id.contains(search)
                        || code.contains(search)
                        || name.contains(search)
                        || departmentText.contains(search)) {

                    filtered.add(
                            course
                    );
                }
            }


            courseList.setAll(
                    filtered
            );


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Search Error",
                    "Could not search courses.\n\n"
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

        loadCourses();
    }


    // ============================================================
    // ADD COURSE
    // ============================================================

    @FXML
    private void onAddCourseClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/add-course-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();


            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Add Course"
            );

            stage.setScene(
                    new Scene(
                            root,
                            600,
                            450
                    )
            );

            stage.showAndWait();


            // Refresh after adding
            loadCourses();


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Add Course Error",
                    "Could not open Add Course.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // VIEW COURSE
    // ============================================================

    @FXML
    private void onViewCourseClick() {

        Course course =
                courseTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (course == null) {

            showInformation(
                    "View Course",
                    "Please select a course first."
            );

            return;
        }


        // --------------------------------------------------------
        // DEPARTMENT
        // --------------------------------------------------------

        String departmentText =
                "Not provided";


        if (course.getDepartment() != null) {

            Department department =
                    course.getDepartment();

            departmentText =
                    safe(
                            department
                                    .getDepartmentCode()
                    )
                            + " - "
                            + safe(
                            department
                                    .getDepartmentName()
                    );
        }


        // --------------------------------------------------------
        // DETAILS
        // --------------------------------------------------------

        String message =
                "Course ID: "
                        + course.getCourseId()

                        + "\n\n"

                        + "Course Code: "
                        + safe(
                        course.getCourseCode()
                )

                        + "\n\n"

                        + "Course Name: "
                        + safe(
                        course.getCourseName()
                )

                        + "\n\n"

                        + "Department: "
                        + departmentText;


        showInformation(
                "Course Details",
                message
        );
    }


    // ============================================================
    // EDIT COURSE
    // ============================================================

    @FXML
    private void onEditCourseClick() {

        Course course =
                courseTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (course == null) {

            showInformation(
                    "Edit Course",
                    "Please select a course first."
            );

            return;
        }


        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/edit-course-view.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            EditCourseController controller =
                    loader.getController();


            controller.setCourse(
                    course
            );


            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Edit Course"
            );

            stage.setScene(
                    new Scene(
                            root,
                            600,
                            450
                    )
            );

            stage.showAndWait();


            // Refresh after editing
            loadCourses();


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Edit Course Error",
                    "Could not open Edit Course.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // DELETE COURSE
    // ============================================================

    @FXML
    private void onDeleteCourseClick() {

        Course course =
                courseTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (course == null) {

            showInformation(
                    "Delete Course",
                    "Please select a course first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Delete Course"
        );


        confirmation.setHeaderText(
                "Delete Course?"
        );


        confirmation.setContentText(
                "Are you sure you want to delete:\n\n"
                        + safe(
                        course.getCourseCode()
                )
                        + " - "
                        + safe(
                        course.getCourseName()
                )
                        + "\n\n"
                        + "This action cannot be undone."
        );


        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response ==
                            ButtonType.OK) {

                        deleteCourse(
                                course
                        );
                    }
                });
    }


    // ============================================================
    // DELETE COURSE
    // ============================================================

    private void deleteCourse(
            Course course
    ) {

        try {

            boolean deleted =
                    courseService.delete(
                            course.getCourseId()
                    );


            if (deleted) {

                showInformation(
                        "Course Deleted",
                        "Course deleted successfully."
                );


                loadCourses();


            } else {

                showError(
                        "Delete Course",
                        "Course was not found."
                );
            }


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Delete Course Error",
                    "Could not delete course.\n\n"
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
    // SAFE VALUE
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