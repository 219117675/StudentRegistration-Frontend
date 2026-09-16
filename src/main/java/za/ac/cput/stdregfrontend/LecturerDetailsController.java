package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import za.ac.cput.service.LecturerService;

import java.util.List;
import java.util.Map;

public class LecturerDetailsController {

    // =========================================================
    // PERSONAL INFORMATION
    // =========================================================

    @FXML
    private Label personIdLabel;

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


    // =========================================================
    // EMPLOYMENT INFORMATION
    // =========================================================

    @FXML
    private Label lecturerIdLabel;

    @FXML
    private Label employeeNumberLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label departmentLabel;


    // =========================================================
    // CONTACT INFORMATION
    // =========================================================

    @FXML
    private Label contactEmailLabel;

    @FXML
    private Label phoneNumberLabel;


    // =========================================================
    // ADDRESS INFORMATION
    // =========================================================

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


    // =========================================================
    // ASSIGNED CLASSES
    // =========================================================

    @FXML
    private Label classesLabel;


    // =========================================================
    // SERVICE
    // =========================================================

    private final LecturerService lecturerService =
            new LecturerService();


    // =========================================================
    // LOAD LECTURER
    // =========================================================

    public void loadLecturer(int personId) {

        try {

            System.out.println(
                    "Loading lecturer details for Person ID: "
                            + personId
            );

            Map<String, Object> details =
                    lecturerService.getDetails(personId);

            if (details == null || details.isEmpty()) {

                showError(
                        "Lecturer with Person ID "
                                + personId
                                + " was not found."
                );

                return;
            }

            /*
             * Print the complete response.
             *
             * This is useful while we are connecting
             * the frontend to the backend.
             */
            System.out.println(
                    "Lecturer details received:"
            );

            System.out.println(details);


            // =====================================================
            // PERSONAL INFORMATION
            // =====================================================

            setLabel(
                    personIdLabel,
                    details.get("personId")
            );

            setLabel(
                    firstNameLabel,
                    details.get("firstName")
            );

            setLabel(
                    lastNameLabel,
                    details.get("lastName")
            );

            setLabel(
                    dateOfBirthLabel,
                    details.get("dateOfBirth")
            );

            setLabel(
                    genderLabel,
                    details.get("gender")
            );

            setLabel(
                    raceLabel,
                    details.get("race")
            );


            // =====================================================
            // EMPLOYMENT INFORMATION
            // =====================================================

            setLabel(
                    lecturerIdLabel,
                    details.get("lecturerId")
            );

            setLabel(
                    employeeNumberLabel,
                    details.get("employeeNumber")
            );

            setLabel(
                    emailLabel,
                    details.get("lecturerEmail")
            );


            // =====================================================
            // DEPARTMENT
            // =====================================================

            displayDepartment(details);


            // =====================================================
            // CONTACT INFORMATION
            // =====================================================

            displayContactDetails(details);


            // =====================================================
            // ADDRESS INFORMATION
            // =====================================================

            displayAddress(details);


            // =====================================================
            // ASSIGNED CLASSES
            // =====================================================

            displayClasses(details);


            System.out.println(
                    "Lecturer details loaded successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Unable to load lecturer details:\n"
                            + getErrorMessage(e)
            );
        }
    }


    // =========================================================
    // DISPLAY DEPARTMENT
    // =========================================================

    private void displayDepartment(
            Map<String, Object> details) {

        Object departmentCode =
                details.get("departmentCode");

        Object departmentName =
                details.get("departmentName");


        if (departmentCode == null
                && departmentName == null) {

            departmentLabel.setText("-");

            return;
        }


        if (departmentCode != null
                && departmentName != null) {

            departmentLabel.setText(
                    String.valueOf(departmentCode)
                            + " - "
                            + String.valueOf(departmentName)
            );

            return;
        }


        if (departmentCode != null) {

            departmentLabel.setText(
                    String.valueOf(departmentCode)
            );

            return;
        }


        departmentLabel.setText(
                String.valueOf(departmentName)
        );
    }


    // =========================================================
    // DISPLAY CONTACT DETAILS
    // =========================================================

    private void displayContactDetails(
            Map<String, Object> details) {

        Object contactObject =
                details.get("contactDetails");


        /*
         * Backend returns:
         *
         * "contactDetails": {
         *     "email": "...",
         *     "phoneNumber": "..."
         * }
         */

        if (!(contactObject instanceof Map<?, ?> contactMap)) {

            contactEmailLabel.setText("-");
            phoneNumberLabel.setText("-");

            return;
        }


        Object contactEmail =
                contactMap.get("email");

        Object phoneNumber =
                contactMap.get("phoneNumber");


        setLabel(
                contactEmailLabel,
                contactEmail
        );

        setLabel(
                phoneNumberLabel,
                phoneNumber
        );
    }


    // =========================================================
    // DISPLAY ADDRESS
    // =========================================================

    private void displayAddress(
            Map<String, Object> details) {

        Object addressObject =
                details.get("address");


        /*
         * Backend returns:
         *
         * "address": {
         *     "street": "...",
         *     "suburb": "...",
         *     "city": "...",
         *     "province": "...",
         *     "postalCode": "..."
         * }
         */

        if (!(addressObject instanceof Map<?, ?> addressMap)) {

            streetLabel.setText("-");
            suburbLabel.setText("-");
            cityLabel.setText("-");
            provinceLabel.setText("-");
            postalCodeLabel.setText("-");

            return;
        }


        Object street =
                addressMap.get("street");

        Object suburb =
                addressMap.get("suburb");

        Object city =
                addressMap.get("city");

        Object province =
                addressMap.get("province");

        Object postalCode =
                addressMap.get("postalCode");


        setLabel(
                streetLabel,
                street
        );

        setLabel(
                suburbLabel,
                suburb
        );

        setLabel(
                cityLabel,
                city
        );

        setLabel(
                provinceLabel,
                province
        );

        setLabel(
                postalCodeLabel,
                postalCode
        );
    }


    // =========================================================
    // DISPLAY ASSIGNED CLASSES
    // =========================================================

    private void displayClasses(
            Map<String, Object> details) {

        Object classesObject =
                details.get("classes");


        /*
         * Backend returns:
         *
         * "classes": [
         *     {
         *         "classId": ...,
         *         "classCode": "...",
         *         "className": "..."
         *     }
         * ]
         */

        if (!(classesObject instanceof List<?> classes)) {

            classesLabel.setText(
                    "No classes assigned."
            );

            return;
        }


        if (classes.isEmpty()) {

            classesLabel.setText(
                    "No classes assigned."
            );

            return;
        }


        StringBuilder classText =
                new StringBuilder();


        for (Object classObject : classes) {

            if (classObject instanceof Map<?, ?> classMap) {

                Object classCode =
                        classMap.get("classCode");

                Object className =
                        classMap.get("className");


                if (classCode != null
                        && className != null) {

                    classText
                            .append(classCode)
                            .append(" - ")
                            .append(className);

                } else if (classCode != null) {

                    classText.append(
                            classCode
                    );

                } else if (className != null) {

                    classText.append(
                            className
                    );

                } else {

                    classText.append(
                            classMap
                    );
                }

            } else {

                classText.append(
                        String.valueOf(classObject)
                );
            }


            classText.append("\n");
        }


        classesLabel.setText(
                classText.toString().trim()
        );
    }


    // =========================================================
    // SAFE LABEL SETTER
    // =========================================================

    private void setLabel(
            Label label,
            Object value) {

        if (label == null) {

            throw new IllegalStateException(
                    "FXML Label was not injected."
            );
        }


        if (value == null) {

            label.setText("-");

            return;
        }


        String text =
                String.valueOf(value);


        if (text.isBlank()) {

            label.setText("-");

        } else {

            label.setText(text);
        }
    }


    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private String getErrorMessage(
            Exception e) {

        if (e.getMessage() != null
                && !e.getMessage().isBlank()) {

            return e.getMessage();
        }


        if (e.getCause() != null
                && e.getCause().getMessage() != null
                && !e.getCause().getMessage().isBlank()) {

            return e.getCause().getMessage();
        }


        return e.getClass().getSimpleName();
    }


    // =========================================================
    // SHOW ERROR
    // =========================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Lecturer Details"
        );

        alert.setHeaderText(
                "Unable to load lecturer"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    // =========================================================
    // CLOSE
    // =========================================================

    @FXML
    private void onCloseClick() {

        Stage stage =
                (Stage) personIdLabel
                        .getScene()
                        .getWindow();

        stage.close();
    }
}