package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import za.ac.cput.domain.Address;
import za.ac.cput.domain.Applicant;
import za.ac.cput.domain.ContactDetails;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;
import za.ac.cput.service.ApplicantService;

import java.time.LocalDate;

public class AddApplicantController {

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

    @FXML
    private TextField contactEmailField;

    @FXML
    private TextField phoneNumberField;

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

    private final ApplicantService applicantService =
            new ApplicantService();

    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        genderComboBox.setItems(
                javafx.collections.FXCollections.observableArrayList(
                        Gender.values()
                )
        );

        raceComboBox.setItems(
                javafx.collections.FXCollections.observableArrayList(
                        Race.values()
                )
        );
    }

    // =========================================================
    // SAVE APPLICANT
    // =========================================================

    @FXML
    private void onSaveClick() {

        String firstName =
                firstNameField.getText()
                        .trim();

        String lastName =
                lastNameField.getText()
                        .trim();

        LocalDate dateOfBirth =
                dateOfBirthPicker.getValue();

        Gender gender =
                genderComboBox.getValue();

        Race race =
                raceComboBox.getValue();

        String contactEmail =
                contactEmailField.getText()
                        .trim();

        String phoneNumber =
                phoneNumberField.getText()
                        .trim();

        String street =
                streetField.getText()
                        .trim();

        String suburb =
                suburbField.getText()
                        .trim();

        String city =
                cityField.getText()
                        .trim();

        String province =
                provinceField.getText()
                        .trim();

        String postalCode =
                postalCodeField.getText()
                        .trim();

        // -----------------------------------------------------
        // VALIDATION
        // -----------------------------------------------------

        if (firstName.isEmpty()) {

            showWarning(
                    "First name is required."
            );

            return;
        }

        if (lastName.isEmpty()) {

            showWarning(
                    "Last name is required."
            );

            return;
        }

        if (dateOfBirth == null) {

            showWarning(
                    "Date of birth is required."
            );

            return;
        }

        if (dateOfBirth.isAfter(
                LocalDate.now()
        )) {

            showWarning(
                    "Date of birth cannot be in the future."
            );

            return;
        }

        if (gender == null) {

            showWarning(
                    "Please select a gender."
            );

            return;
        }

        if (race == null) {

            showWarning(
                    "Please select a race."
            );

            return;
        }

        if (contactEmail.isEmpty()) {

            showWarning(
                    "Contact email is required."
            );

            return;
        }

        if (phoneNumber.isEmpty()) {

            showWarning(
                    "Phone number is required."
            );

            return;
        }

        // -----------------------------------------------------
        // ADDRESS
        // -----------------------------------------------------

        Address address =
                new Address(
                        0,
                        street,
                        suburb,
                        city,
                        postalCode,
                        province
                );

        // -----------------------------------------------------
        // CONTACT DETAILS
        // -----------------------------------------------------

        ContactDetails contactDetails =
                new ContactDetails(
                        0,
                        contactEmail,
                        phoneNumber
                );

        // -----------------------------------------------------
        // BUILD APPLICANT
        //
        // applicantId = 0
        // personId = 0
        //
        // Backend generates the identities.
        // -----------------------------------------------------

        Applicant applicant =
                new Applicant.Builder()
                        .setApplicantId(0)
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setDateOfBirth(dateOfBirth)
                        .setGender(gender)
                        .setRace(race)
                        .setAddress(address)
                        .setContactDetails(contactDetails)
                        .build();

        // -----------------------------------------------------
        // SAVE THROUGH REST API
        // -----------------------------------------------------

        try {

            Applicant savedApplicant =
                    applicantService.create(
                            applicant
                    );

            showInformation(
                    "Applicant created successfully.\n\n"
                            + "Applicant ID: "
                            + savedApplicant.getApplicantId()
                            + "\n"
                            + "Person ID: "
                            + savedApplicant.getPersonId()
            );

            closeWindow();

        } catch (Exception e) {

            showError(
                    "Could not create applicant.\n\n"
                            + e.getMessage()
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
    // CLOSE WINDOW
    // =========================================================

    private void closeWindow() {

        Stage stage =
                (Stage) firstNameField
                        .getScene()
                        .getWindow();

        stage.close();
    }

    // =========================================================
    // ALERTS
    // =========================================================

    private void showWarning(
            String message) {

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
            String message) {

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
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Applicant Error"
        );

        alert.setHeaderText(
                "Operation Failed"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}