package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import za.ac.cput.domain.Address;
import za.ac.cput.domain.Applicant;
import za.ac.cput.domain.ContactDetails;
import za.ac.cput.service.ApplicantService;

public class ApplicantDetailsController {

    @FXML
    private Label personIdLabel;

    @FXML
    private Label applicantIdLabel;

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

    @FXML
    private Label contactEmailLabel;

    @FXML
    private Label phoneNumberLabel;

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

    private final ApplicantService applicantService =
            new ApplicantService();


    // =========================================================
    // LOAD APPLICANT
    // =========================================================

    public void loadApplicant(int personId) {

        try {

            Applicant applicant =
                    applicantService.getById(personId);

            if (applicant == null) {

                clearDetails();

                return;
            }

            displayApplicant(applicant);

        } catch (Exception e) {

            e.printStackTrace();

            clearDetails();
        }
    }


    // =========================================================
    // DISPLAY APPLICANT
    // =========================================================

    private void displayApplicant(
            Applicant applicant) {

        personIdLabel.setText(
                String.valueOf(
                        applicant.getPersonId()
                )
        );

        applicantIdLabel.setText(
                String.valueOf(
                        applicant.getApplicantId()
                )
        );

        firstNameLabel.setText(
                safeText(
                        applicant.getFirstName()
                )
        );

        lastNameLabel.setText(
                safeText(
                        applicant.getLastName()
                )
        );

        dateOfBirthLabel.setText(
                applicant.getDateOfBirth() != null
                        ? applicant.getDateOfBirth().toString()
                        : "-"
        );

        genderLabel.setText(
                applicant.getGender() != null
                        ? applicant.getGender().toString()
                        : "-"
        );

        raceLabel.setText(
                applicant.getRace() != null
                        ? applicant.getRace().toString()
                        : "-"
        );


        // =====================================================
        // CONTACT DETAILS
        // =====================================================

        ContactDetails contactDetails =
                applicant.getContactDetails();

        if (contactDetails != null) {

            contactEmailLabel.setText(
                    safeText(
                            contactDetails.getEmail()
                    )
            );

            phoneNumberLabel.setText(
                    safeText(
                            contactDetails.getPhoneNumber()
                    )
            );

        } else {

            contactEmailLabel.setText("-");
            phoneNumberLabel.setText("-");
        }


        // =====================================================
        // ADDRESS
        // =====================================================

        Address address =
                applicant.getAddress();

        if (address != null) {

            streetLabel.setText(
                    safeText(
                            address.getStreet()
                    )
            );

            suburbLabel.setText(
                    safeText(
                            address.getSuburb()
                    )
            );

            cityLabel.setText(
                    safeText(
                            address.getCity()
                    )
            );

            provinceLabel.setText(
                    safeText(
                            address.getProvince()
                    )
            );

            postalCodeLabel.setText(
                    safeText(
                            address.getPostalCode()
                    )
            );

        } else {

            streetLabel.setText("-");
            suburbLabel.setText("-");
            cityLabel.setText("-");
            provinceLabel.setText("-");
            postalCodeLabel.setText("-");
        }
    }


    // =========================================================
    // CLEAR DETAILS
    // =========================================================

    private void clearDetails() {

        personIdLabel.setText("-");
        applicantIdLabel.setText("-");
        firstNameLabel.setText("-");
        lastNameLabel.setText("-");
        dateOfBirthLabel.setText("-");
        genderLabel.setText("-");
        raceLabel.setText("-");
        contactEmailLabel.setText("-");
        phoneNumberLabel.setText("-");
        streetLabel.setText("-");
        suburbLabel.setText("-");
        cityLabel.setText("-");
        provinceLabel.setText("-");
        postalCodeLabel.setText("-");
    }


    // =========================================================
    // SAFE TEXT
    // =========================================================

    private String safeText(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "-";
        }

        return value;
    }


    // =========================================================
    // CLOSE
    // =========================================================

    @FXML
    private void onCloseClick(
            javafx.event.ActionEvent event) {

        Node source =
                (Node) event.getSource();

        Stage stage =
                (Stage) source
                        .getScene()
                        .getWindow();

        stage.close();
    }
}