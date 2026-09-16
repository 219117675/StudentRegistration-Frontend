package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import za.ac.cput.domain.Address;
import za.ac.cput.domain.ContactDetails;
import za.ac.cput.domain.Student;
import za.ac.cput.service.StudentService;

import java.util.Map;

public class StudentDetailsController {

    // ============================================================
    // PERSONAL INFORMATION
    // ============================================================

    @FXML
    private Label personIdLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label raceLabel;


    // ============================================================
    // STUDENT INFORMATION
    // ============================================================

    @FXML
    private Label studentNumberLabel;

    @FXML
    private Label studentEmailLabel;


    // ============================================================
    // CONTACT INFORMATION
    // ============================================================

    @FXML
    private Label contactEmailLabel;

    @FXML
    private Label phoneNumberLabel;


    // ============================================================
    // ADDRESS INFORMATION
    // ============================================================

    @FXML
    private Label streetLabel;

    @FXML
    private Label suburbLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label provinceLabel;

    @FXML
    private Label postalCodeLabel;


    // ============================================================
    // ACADEMIC INFORMATION
    // ============================================================

    @FXML
    private Label applicationIdLabel;

    @FXML
    private Label courseLabel;

    @FXML
    private Label registeredClassesLabel;


    // ============================================================
    // SERVICE
    // ============================================================

    private final StudentService studentService =
            new StudentService();


    // ============================================================
    // LOAD STUDENT
    // ============================================================

    public void loadStudent(int studentId) {

        try {

            Map<String, Object> details =
                    studentService.getStudentDetails(studentId);

            if (details == null) {

                showError(
                        "Student Not Found",
                        "The selected student could not be found."
                );

                closeWindow();

                return;
            }

            // ----------------------------------------------------
            // STUDENT
            // ----------------------------------------------------

            Object studentObject =
                    details.get("student");

            if (!(studentObject instanceof Student)) {

                showError(
                        "Student Error",
                        "Student information could not be loaded."
                );

                return;
            }

            Student student =
                    (Student) studentObject;

            displayStudent(student);


            // ----------------------------------------------------
            // APPLICATION
            // ----------------------------------------------------

            Object application =
                    details.get("application");

            if (application == null) {

                applicationIdLabel.setText(
                        "No application"
                );

            } else {

                applicationIdLabel.setText(
                        String.valueOf(
                                getObjectValue(
                                        application,
                                        "applicationId"
                                )
                        )
                );
            }


            // ----------------------------------------------------
            // COURSE
            // ----------------------------------------------------

            Object course =
                    details.get("course");

            if (course == null) {

                courseLabel.setText(
                        "No course assigned"
                );

            } else {

                String courseName =
                        getObjectValue(
                                course,
                                "courseName"
                        );

                if (courseName == null ||
                        courseName.equals("null")) {

                    courseName =
                            getObjectValue(
                                    course,
                                    "name"
                            );
                }

                courseLabel.setText(
                        safe(courseName)
                );
            }


            // ----------------------------------------------------
            // REGISTERED CLASSES
            // ----------------------------------------------------

            Object registeredClasses =
                    details.get("registeredClasses");

            if (registeredClasses == null) {

                registeredClassesLabel.setText(
                        "No registered classes"
                );

            } else {

                String classesText =
                        registeredClasses.toString();

                if (classesText.trim().isEmpty()
                        || classesText.equals("[]")) {

                    registeredClassesLabel.setText(
                            "No registered classes"
                    );

                } else {

                    registeredClassesLabel.setText(
                            classesText
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unable to Load Student",
                    e.getMessage() == null
                            ? "An unexpected error occurred."
                            : e.getMessage()
            );
        }
    }


    // ============================================================
    // DISPLAY STUDENT
    // ============================================================

    private void displayStudent(Student student) {

        // --------------------------------------------------------
        // PERSONAL INFORMATION
        // --------------------------------------------------------

        personIdLabel.setText(
                String.valueOf(
                        student.getPersonId()
                )
        );

        studentIdLabel.setText(
                String.valueOf(
                        student.getStudentId()
                )
        );

        firstNameLabel.setText(
                safe(student.getFirstName())
        );

        lastNameLabel.setText(
                safe(student.getLastName())
        );

        dateOfBirthLabel.setText(
                student.getDateOfBirth() == null
                        ? "Not provided"
                        : student.getDateOfBirth()
                        .toString()
        );

        genderLabel.setText(
                student.getGender() == null
                        ? "Not provided"
                        : student.getGender()
                        .toString()
        );

        raceLabel.setText(
                student.getRace() == null
                        ? "Not provided"
                        : student.getRace()
                        .toString()
        );


        // --------------------------------------------------------
        // STUDENT INFORMATION
        // --------------------------------------------------------

        studentNumberLabel.setText(
                safe(student.getStudentNumber())
        );

        studentEmailLabel.setText(
                safe(student.getStudentEmail())
        );


        // --------------------------------------------------------
        // CONTACT DETAILS
        // --------------------------------------------------------

        ContactDetails contactDetails =
                student.getContactDetails();

        if (contactDetails == null) {

            contactEmailLabel.setText(
                    "Not provided"
            );

            phoneNumberLabel.setText(
                    "Not provided"
            );

        } else {

            contactEmailLabel.setText(
                    safe(contactDetails.getEmail())
            );

            phoneNumberLabel.setText(
                    safe(
                            contactDetails.getPhoneNumber()
                    )
            );
        }


        // --------------------------------------------------------
        // ADDRESS
        // --------------------------------------------------------

        Address address =
                student.getAddress();

        if (address == null) {

            streetLabel.setText(
                    "Not provided"
            );

            suburbLabel.setText(
                    "Not provided"
            );

            cityLabel.setText(
                    "Not provided"
            );

            provinceLabel.setText(
                    "Not provided"
            );

            postalCodeLabel.setText(
                    "Not provided"
            );

        } else {

            streetLabel.setText(
                    safe(address.getStreet())
            );

            suburbLabel.setText(
                    safe(address.getSuburb())
            );

            cityLabel.setText(
                    safe(address.getCity())
            );

            provinceLabel.setText(
                    safe(address.getProvince())
            );

            postalCodeLabel.setText(
                    safe(address.getPostalCode())
            );
        }
    }


    // ============================================================
    // OBJECT VALUE HELPER
    // ============================================================

    private String getObjectValue(
            Object object,
            String propertyName) {

        if (object == null) {
            return null;
        }

        try {

            java.lang.reflect.Method getter =
                    object.getClass().getMethod(
                            "get"
                                    + Character.toUpperCase(
                                    propertyName.charAt(0)
                            )
                                    + propertyName.substring(1)
                    );

            Object value =
                    getter.invoke(object);

            return value == null
                    ? null
                    : String.valueOf(value);

        } catch (Exception ignored) {

            return null;
        }
    }


    // ============================================================
    // SAFE TEXT
    // ============================================================

    private String safe(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not provided";
        }

        return value;
    }


    // ============================================================
    // CLOSE
    // ============================================================

    @FXML
    private void onCloseClick() {

        closeWindow();
    }


    private void closeWindow() {

        Stage stage =
                (Stage) personIdLabel
                        .getScene()
                        .getWindow();

        stage.close();
    }


    // ============================================================
    // ERROR DIALOG
    // ============================================================

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}