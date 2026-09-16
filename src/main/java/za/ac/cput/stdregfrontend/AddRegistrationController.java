package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Student;
import za.ac.cput.service.ClassService;
import za.ac.cput.service.RegistrationService;
import za.ac.cput.service.StudentService;

import java.util.List;

public class AddRegistrationController {

    @FXML
    private ComboBox<Student> studentComboBox;

    @FXML
    private ComboBox<Class> classComboBox;


    private final StudentService studentService =
            new StudentService();

    private final ClassService classService =
            new ClassService();

    private final RegistrationService registrationService =
            new RegistrationService();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        loadStudents();

        loadClasses();


        studentComboBox.setCellFactory(
                list -> new javafx.scene.control.ListCell<>() {

                    @Override
                    protected void updateItem(
                            Student student,
                            boolean empty
                    ) {

                        super.updateItem(
                                student,
                                empty
                        );


                        if (empty ||
                                student == null) {

                            setText(null);

                        } else {

                            setText(
                                    safe(
                                            student.getStudentNumber()
                                    )
                                            + " - "
                                            + safe(
                                            student.getFirstName()
                                    )
                                            + " "
                                            + safe(
                                            student.getLastName()
                                    )
                            );
                        }
                    }
                }
        );


        studentComboBox.setButtonCell(
                new javafx.scene.control.ListCell<>() {

                    @Override
                    protected void updateItem(
                            Student student,
                            boolean empty
                    ) {

                        super.updateItem(
                                student,
                                empty
                        );


                        if (empty ||
                                student == null) {

                            setText(null);

                        } else {

                            setText(
                                    safe(
                                            student.getStudentNumber()
                                    )
                                            + " - "
                                            + safe(
                                            student.getFirstName()
                                    )
                                            + " "
                                            + safe(
                                            student.getLastName()
                                    )
                            );
                        }
                    }
                }
        );


        classComboBox.setCellFactory(
                list -> new javafx.scene.control.ListCell<>() {

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

                            String courseCode =
                                    "";

                            if (courseClass.getCourse()
                                    != null) {

                                courseCode =
                                        safe(
                                                courseClass
                                                        .getCourse()
                                                        .getCourseCode()
                                        );
                            }


                            setText(
                                    safe(
                                            courseClass
                                                    .getClassCode()
                                    )
                                            + " - "
                                            + safe(
                                            courseClass
                                                    .getClassName()
                                    )
                                            + " | "
                                            + courseCode
                            );
                        }
                    }
                }
        );


        classComboBox.setButtonCell(
                new javafx.scene.control.ListCell<>() {

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

                            String courseCode =
                                    "";

                            if (courseClass.getCourse()
                                    != null) {

                                courseCode =
                                        safe(
                                                courseClass
                                                        .getCourse()
                                                        .getCourseCode()
                                        );
                            }


                            setText(
                                    safe(
                                            courseClass
                                                    .getClassCode()
                                    )
                                            + " - "
                                            + safe(
                                            courseClass
                                                    .getClassName()
                                    )
                                            + " | "
                                            + courseCode
                            );
                        }
                    }
                }
        );
    }


    // ============================================================
    // LOAD STUDENTS
    // ============================================================

    private void loadStudents() {

        try {

            List<Student> students =
                    studentService.getAll();


            studentComboBox.setItems(
                    FXCollections.observableArrayList(
                            students
                    )
            );


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Student Loading Error",
                    "Could not load students.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // LOAD CLASSES
    // ============================================================

    private void loadClasses() {

        try {

            List<Class> classes =
                    classService.getAll();


            classComboBox.setItems(
                    FXCollections.observableArrayList(
                            classes
                    )
            );


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Class Loading Error",
                    "Could not load classes.\n\n"
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // REGISTER
    // ============================================================

    @FXML
    private void onRegisterClick() {

        Student student =
                studentComboBox.getValue();


        Class courseClass =
                classComboBox.getValue();


        if (student == null) {

            showError(
                    "Validation Error",
                    "Please select a student."
            );

            return;
        }


        if (courseClass == null) {

            showError(
                    "Validation Error",
                    "Please select a class."
            );

            return;
        }


        try {

            boolean alreadyRegistered =
                    registrationService
                            .isStudentRegistered(
                                    student.getStudentId(),
                                    courseClass.getClassId()
                            );


            if (alreadyRegistered) {

                showError(
                        "Registration Error",
                        "This student is already registered for this class."
                );

                return;
            }


            registrationService.registerStudent(
                    student.getStudentId(),
                    courseClass.getClassId()
            );


            showInformation(
                    "Registration Successful",
                    "Student "
                            + safe(
                            student.getStudentNumber()
                    )
                            + " has been registered for "
                            + safe(
                            courseClass.getClassCode()
                    )
                            + "."
            );


            closeWindow();


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Registration Error",
                    "Could not register the student.\n\n"
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
                (Stage) studentComboBox
                        .getScene()
                        .getWindow();

        stage.close();
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
}