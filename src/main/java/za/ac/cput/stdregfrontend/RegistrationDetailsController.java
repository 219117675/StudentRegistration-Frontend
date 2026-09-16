package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import za.ac.cput.domain.Class;
import za.ac.cput.domain.Course;
import za.ac.cput.domain.Department;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.domain.Registration;
import za.ac.cput.domain.Student;

public class RegistrationDetailsController {

    @FXML
    private Label registrationIdLabel;

    @FXML
    private Label studentNumberLabel;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentEmailLabel;

    @FXML
    private Label classCodeLabel;

    @FXML
    private Label classNameLabel;

    @FXML
    private Label courseCodeLabel;

    @FXML
    private Label courseNameLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label lecturerLabel;


    // ============================================================
    // LOAD REGISTRATION
    // ============================================================

    public void setRegistration(
            Registration registration
    ) {

        if (registration == null) {

            return;
        }


        registrationIdLabel.setText(
                String.valueOf(
                        registration.getRegistrationId()
                )
        );


        Student student =
                registration.getStudent();


        if (student != null) {

            studentNumberLabel.setText(
                    safe(
                            student.getStudentNumber()
                    )
            );


            studentNameLabel.setText(
                    safe(
                            student.getFirstName()
                    )
                            + " "
                            + safe(
                            student.getLastName()
                    )
            );


            studentEmailLabel.setText(
                    safe(
                            student.getStudentEmail()
                    )
            );

        } else {

            studentNumberLabel.setText(
                    "Not assigned"
            );

            studentNameLabel.setText(
                    "Not assigned"
            );

            studentEmailLabel.setText(
                    "Not assigned"
            );
        }


        Class courseClass =
                registration.getCourseClass();


        if (courseClass != null) {

            classCodeLabel.setText(
                    safe(
                            courseClass.getClassCode()
                    )
            );


            classNameLabel.setText(
                    safe(
                            courseClass.getClassName()
                    )
            );


            Lecturer lecturer =
                    courseClass.getLecturer();


            if (lecturer != null) {

                lecturerLabel.setText(
                        safe(
                                lecturer.getFirstName()
                        )
                                + " "
                                + safe(
                                lecturer.getLastName()
                        )
                );

            } else {

                lecturerLabel.setText(
                        "Not assigned"
                );
            }


            Course course =
                    courseClass.getCourse();


            if (course != null) {

                courseCodeLabel.setText(
                        safe(
                                course.getCourseCode()
                        )
                );


                courseNameLabel.setText(
                        safe(
                                course.getCourseName()
                        )
                );


                Department department =
                        course.getDepartment();


                if (department != null) {

                    departmentLabel.setText(
                            safe(
                                    department
                                            .getDepartmentCode()
                            )
                                    + " - "
                                    + safe(
                                    department
                                            .getDepartmentName()
                            )
                    );

                } else {

                    departmentLabel.setText(
                            "Not assigned"
                    );
                }

            } else {

                courseCodeLabel.setText(
                        "Not assigned"
                );

                courseNameLabel.setText(
                        "Not assigned"
                );

                departmentLabel.setText(
                        "Not assigned"
                );
            }

        } else {

            classCodeLabel.setText(
                    "Not assigned"
            );

            classNameLabel.setText(
                    "Not assigned"
            );

            courseCodeLabel.setText(
                    "Not assigned"
            );

            courseNameLabel.setText(
                    "Not assigned"
            );

            departmentLabel.setText(
                    "Not assigned"
            );

            lecturerLabel.setText(
                    "Not assigned"
            );
        }
    }


    // ============================================================
    // CLOSE
    // ============================================================

    @FXML
    private void onCloseClick() {

        Stage stage =
                (Stage) registrationIdLabel
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
}