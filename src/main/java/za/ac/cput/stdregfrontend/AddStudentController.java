package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Address;
import za.ac.cput.domain.ContactDetails;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;
import za.ac.cput.domain.Student;
import za.ac.cput.service.StudentService;

import java.time.LocalDate;

public class AddStudentController {

    // =========================================================
    // PERSONAL INFORMATION
    // =========================================================

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private ComboBox<Gender> genderComboBox;

    @FXML
    private ComboBox<Race> raceComboBox;


    // =========================================================
    // ADDRESS
    // =========================================================

    @FXML
    private TextField streetField;

    @FXML
    private TextField suburbField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField provinceField;

    @FXML
    private TextField postalCodeField;


    // =========================================================
    // CONTACT DETAILS
    // =========================================================

    @FXML
    private TextField contactEmailField;

    @FXML
    private TextField phoneNumberField;


    // =========================================================
    // LOGIN PASSWORD
    // =========================================================

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;


    // =========================================================
    // SERVICE
    // =========================================================

    private final StudentService studentService =
            new StudentService();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    private void initialize() {

        genderComboBox.setItems(
                FXCollections.observableArrayList(
                        Gender.values()
                )
        );

        raceComboBox.setItems(
                FXCollections.observableArrayList(
                        Race.values()
                )
        );
    }


    // =========================================================
    // SAVE STUDENT
    // =========================================================

    @FXML
    private void onSaveStudentClick() {

        try {

            // -------------------------------------------------
            // PERSONAL INFORMATION
            // -------------------------------------------------

            String firstName =
                    firstNameField.getText() == null
                            ? ""
                            : firstNameField.getText().trim();

            String lastName =
                    lastNameField.getText() == null
                            ? ""
                            : lastNameField.getText().trim();

            LocalDate dateOfBirth =
                    dateOfBirthPicker.getValue();

            Gender gender =
                    genderComboBox.getValue();

            Race race =
                    raceComboBox.getValue();


            // -------------------------------------------------
            // VALIDATION
            // -------------------------------------------------

            if (firstName.isEmpty()) {

                showError(
                        "Validation Error",
                        "First name is required."
                );

                return;
            }


            if (lastName.isEmpty()) {

                showError(
                        "Validation Error",
                        "Last name is required."
                );

                return;
            }


            if (dateOfBirth == null) {

                showError(
                        "Validation Error",
                        "Date of birth is required."
                );

                return;
            }


            if (gender == null) {

                showError(
                        "Validation Error",
                        "Please select a gender."
                );

                return;
            }


            if (race == null) {

                showError(
                        "Validation Error",
                        "Please select a race."
                );

                return;
            }


            // -------------------------------------------------
            // PERSONAL EMAIL
            // -------------------------------------------------

            String personalEmail =
                    contactEmailField.getText() == null
                            ? ""
                            : contactEmailField.getText().trim();


            if (personalEmail.isEmpty()) {

                showError(
                        "Validation Error",
                        "Personal email is required."
                );

                return;
            }


            if (!isValidEmail(personalEmail)) {

                showError(
                        "Validation Error",
                        "Please enter a valid personal email address."
                );

                return;
            }


            // -------------------------------------------------
            // PASSWORD
            // -------------------------------------------------

            String password =
                    passwordField.getText();

            String confirmPassword =
                    confirmPasswordField.getText();


            if (password == null ||
                    password.isEmpty()) {

                showError(
                        "Validation Error",
                        "Password is required."
                );

                return;
            }


            if (password.length() < 6) {

                showError(
                        "Validation Error",
                        "Password must be at least 6 characters."
                );

                return;
            }


            if (!password.equals(confirmPassword)) {

                showError(
                        "Validation Error",
                        "Passwords do not match."
                );

                return;
            }


            // -------------------------------------------------
            // ADDRESS
            // -------------------------------------------------

            String street =
                    streetField.getText() == null
                            ? ""
                            : streetField.getText().trim();

            String suburb =
                    suburbField.getText() == null
                            ? ""
                            : suburbField.getText().trim();

            String city =
                    cityField.getText() == null
                            ? ""
                            : cityField.getText().trim();

            String province =
                    provinceField.getText() == null
                            ? ""
                            : provinceField.getText().trim();

            String postalCode =
                    postalCodeField.getText() == null
                            ? ""
                            : postalCodeField.getText().trim();


            Address address =
                    new Address(
                            0,
                            street,
                            suburb,
                            city,
                            postalCode,
                            province
                    );


            // -------------------------------------------------
            // CONTACT DETAILS
            // -------------------------------------------------

            String phoneNumber =
                    phoneNumberField.getText() == null
                            ? ""
                            : phoneNumberField.getText().trim();


            /*
             * Personal email belongs to ContactDetails.
             *
             * It is NOT the generated CPUT student email.
             */
            ContactDetails contactDetails =
                    new ContactDetails(
                            0,
                            personalEmail,
                            phoneNumber
                    );


            // -------------------------------------------------
            // BUILD STUDENT
            // -------------------------------------------------

            Student student =
                    Student.builder()
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .setDateOfBirth(dateOfBirth)
                            .setGender(gender)
                            .setRace(race)
                            .setAddress(address)
                            .setContactDetails(contactDetails)
                            .build();


            // -------------------------------------------------
            // CREATE STUDENT
            // -------------------------------------------------

            Student savedStudent =
                    studentService.create(
                            student,
                            password
                    );


            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Student Created"
            );

            alert.setHeaderText(
                    "Student created successfully"
            );

            alert.setContentText(
                    "Student Number: "
                            + savedStudent.getStudentNumber()
                            + "\n\n"
                            + "CPUT Student Email: "
                            + savedStudent.getStudentEmail()
                            + "\n\n"
                            + "Personal Email: "
                            + personalEmail
            );

            alert.showAndWait();


            closeWindow();


        } catch (Exception e) {

            String message =
                    e.getMessage();


            if (message == null ||
                    message.trim().isEmpty()) {

                message =
                        "An unexpected error occurred.";
            }


            showError(
                    "Unable to Create Student",
                    message
            );
        }
    }


    // =========================================================
    // CANCEL
    // =========================================================

    @FXML
    private void onCancelClick() {

        closeWindow();
    }


    // =========================================================
    // EMAIL VALIDATION
    // =========================================================

    private boolean isValidEmail(
            String email
    ) {

        return email != null &&
                email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                );
    }


    // =========================================================
    // CLOSE WINDOW
    // =========================================================

    private void closeWindow() {

        if (firstNameField == null ||
                firstNameField.getScene() == null) {

            return;
        }


        Stage stage =
                (Stage) firstNameField
                        .getScene()
                        .getWindow();


        stage.close();
    }


    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String title,
            String message
    ) {

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