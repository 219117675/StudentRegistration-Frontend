package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Address;
import za.ac.cput.domain.ContactDetails;
import za.ac.cput.domain.Department;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.domain.Race;

import za.ac.cput.service.DepartmentService;
import za.ac.cput.service.LecturerService;

import java.time.LocalDate;
import java.util.List;

public class AddLecturerController {

    /*
     * ============================================================
     * PERSONAL INFORMATION
     * ============================================================
     */

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


    /*
     * ============================================================
     * EMPLOYMENT INFORMATION
     * ============================================================
     */

    @FXML
    private TextField lecturerEmailField;

    @FXML
    private ComboBox<Department> departmentComboBox;


    /*
     * ============================================================
     * LOGIN INFORMATION
     * ============================================================
     */

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;


    /*
     * ============================================================
     * CONTACT INFORMATION
     * ============================================================
     */

    @FXML
    private TextField contactEmailField;

    @FXML
    private TextField phoneNumberField;


    /*
     * ============================================================
     * ADDRESS INFORMATION
     * ============================================================
     */

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


    /*
     * ============================================================
     * SERVICES
     * ============================================================
     */

    private final LecturerService lecturerService =
            new LecturerService();

    private final DepartmentService departmentService =
            new DepartmentService();


    /*
     * ============================================================
     * INITIALIZE
     * ============================================================
     */

    @FXML
    public void initialize() {

        loadGenders();

        loadRaces();

        configureDepartmentComboBox();

        loadDepartments();
    }


    /*
     * ============================================================
     * LOAD GENDERS
     * ============================================================
     */

    private void loadGenders() {

        genderComboBox.setItems(
                FXCollections.observableArrayList(
                        Gender.values()
                )
        );
    }


    /*
     * ============================================================
     * LOAD RACES
     * ============================================================
     */

    private void loadRaces() {

        raceComboBox.setItems(
                FXCollections.observableArrayList(
                        Race.values()
                )
        );
    }


    /*
     * ============================================================
     * CONFIGURE DEPARTMENT COMBOBOX
     * ============================================================
     */

    private void configureDepartmentComboBox() {

        departmentComboBox.setCellFactory(
                listView ->
                        new ListCell<Department>() {

                            @Override
                            protected void updateItem(
                                    Department department,
                                    boolean empty) {

                                super.updateItem(
                                        department,
                                        empty
                                );

                                if (empty || department == null) {

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
                new ListCell<Department>() {

                    @Override
                    protected void updateItem(
                            Department department,
                            boolean empty) {

                        super.updateItem(
                                department,
                                empty
                        );

                        if (empty || department == null) {

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


    /*
     * ============================================================
     * LOAD DEPARTMENTS
     * ============================================================
     */

    private void loadDepartments() {

        try {

            List<Department> departments =
                    departmentService.getAll();

            departmentComboBox.setItems(
                    FXCollections.observableArrayList(
                            departments
                    )
            );

        } catch (Exception e) {

            showError(
                    "Could not load departments:\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    /*
     * ============================================================
     * SAVE LECTURER
     * ============================================================
     */

    @FXML
    protected void onSaveClick() {

        try {

            /*
             * ----------------------------------------------------
             * PERSONAL INFORMATION
             * ----------------------------------------------------
             */

            String firstName =
                    firstNameField
                            .getText()
                            .trim();

            String lastName =
                    lastNameField
                            .getText()
                            .trim();

            LocalDate dateOfBirth =
                    dateOfBirthPicker.getValue();

            Gender gender =
                    genderComboBox.getValue();

            Race race =
                    raceComboBox.getValue();


            /*
             * ----------------------------------------------------
             * EMPLOYMENT INFORMATION
             * ----------------------------------------------------
             */

            String lecturerEmail =
                    lecturerEmailField
                            .getText()
                            .trim();

            Department department =
                    departmentComboBox.getValue();


            /*
             * ----------------------------------------------------
             * LOGIN INFORMATION
             * ----------------------------------------------------
             */

            String password =
                    passwordField
                            .getText();

            String confirmPassword =
                    confirmPasswordField
                            .getText();


            /*
             * ----------------------------------------------------
             * CONTACT INFORMATION
             * ----------------------------------------------------
             */

            String contactEmail =
                    contactEmailField
                            .getText()
                            .trim();

            String phoneNumber =
                    phoneNumberField
                            .getText()
                            .trim();


            /*
             * ----------------------------------------------------
             * ADDRESS INFORMATION
             * ----------------------------------------------------
             */

            String street =
                    streetField
                            .getText()
                            .trim();

            String suburb =
                    suburbField
                            .getText()
                            .trim();

            String city =
                    cityField
                            .getText()
                            .trim();

            String province =
                    provinceField
                            .getText()
                            .trim();

            String postalCode =
                    postalCodeField
                            .getText()
                            .trim();


            /*
             * ====================================================
             * VALIDATION
             * ====================================================
             */

            if (firstName.isEmpty()) {

                showError("Please enter the first name.");

                return;
            }

            if (lastName.isEmpty()) {

                showError("Please enter the last name.");

                return;
            }

            if (dateOfBirth == null) {

                showError("Please select the date of birth.");

                return;
            }

            if (gender == null) {

                showError("Please select the gender.");

                return;
            }

            if (race == null) {

                showError("Please select the race.");

                return;
            }

            if (lecturerEmail.isEmpty()) {

                showError("Please enter the lecturer email.");

                return;
            }

            if (!lecturerEmail.toLowerCase()
                    .endsWith("@cput.ac.za")) {

                showError(
                        "Lecturer email must end with @cput.ac.za."
                );

                return;
            }

            if (department == null) {

                showError("Please select a department.");

                return;
            }


            /*
             * ----------------------------------------------------
             * PASSWORD VALIDATION
             * ----------------------------------------------------
             */

            if (password == null || password.isBlank()) {

                showError(
                        "Please enter a login password."
                );

                return;
            }

            if (password.length() < 6) {

                showError(
                        "Password must contain at least 6 characters."
                );

                return;
            }

            if (confirmPassword == null
                    || confirmPassword.isBlank()) {

                showError(
                        "Please confirm the login password."
                );

                return;
            }

            if (!password.equals(confirmPassword)) {

                showError(
                        "Password and Confirm Password do not match."
                );

                return;
            }


            /*
             * ----------------------------------------------------
             * CONTACT VALIDATION
             * ----------------------------------------------------
             */

            if (contactEmail.isEmpty()) {

                showError(
                        "Please enter the contact email."
                );

                return;
            }

            if (phoneNumber.isEmpty()) {

                showError(
                        "Please enter the phone number."
                );

                return;
            }


            /*
             * ----------------------------------------------------
             * ADDRESS VALIDATION
             * ----------------------------------------------------
             */

            if (street.isEmpty()) {

                showError("Please enter the street.");

                return;
            }

            if (suburb.isEmpty()) {

                showError("Please enter the suburb.");

                return;
            }

            if (city.isEmpty()) {

                showError("Please enter the city.");

                return;
            }

            if (province.isEmpty()) {

                showError("Please enter the province.");

                return;
            }

            if (postalCode.isEmpty()) {

                showError("Please enter the postal code.");

                return;
            }


            /*
             * ====================================================
             * CREATE ADDRESS
             * ====================================================
             */

            Address address =
                    new Address(
                            0,
                            street,
                            suburb,
                            city,
                            postalCode,
                            province
                    );


            /*
             * ====================================================
             * CREATE CONTACT DETAILS
             * ====================================================
             */

            ContactDetails contactDetails =
                    new ContactDetails(
                            0,
                            contactEmail,
                            phoneNumber
                    );


            /*
             * ====================================================
             * CREATE LECTURER
             * ====================================================
             */

            Lecturer lecturer =
                    new Lecturer.Builder()

                            .setFirstName(
                                    firstName
                            )

                            .setLastName(
                                    lastName
                            )

                            .setDateOfBirth(
                                    dateOfBirth
                            )

                            .setAddress(
                                    address
                            )

                            .setContactDetails(
                                    contactDetails
                            )

                            .setGender(
                                    gender
                            )

                            .setRace(
                                    race
                            )

                            .setLecturerEmail(
                                    lecturerEmail
                            )

                            .setDepartment(
                                    department
                            )

                            .build();


            /*
             * ====================================================
             * SEND LECTURER + PASSWORD TO BACKEND
             * ====================================================
             */

            Lecturer savedLecturer =
                    lecturerService.create(
                            lecturer,
                            password
                    );


            /*
             * ====================================================
             * SUCCESS MESSAGE
             * ====================================================
             */

            showSuccess(
                    "Lecturer added successfully."
                            + "\n\n"
                            + "Name: "
                            + savedLecturer.getFirstName()
                            + " "
                            + savedLecturer.getLastName()
                            + "\n"
                            + "Person ID: "
                            + savedLecturer.getPersonId()
                            + "\n"
                            + "Lecturer ID: "
                            + savedLecturer.getLecturerId()
                            + "\n"
                            + "Employee Number: "
                            + savedLecturer.getEmployeeNumber()
                            + "\n"
                            + "Department: "
                            + department.getDepartmentCode()
                            + "\n\n"
                            + "Login Email: "
                            + lecturerEmail
            );


            closeWindow();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not add lecturer:\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    /*
     * ============================================================
     * CANCEL
     * ============================================================
     */

    @FXML
    protected void onCancelClick() {

        closeWindow();
    }


    /*
     * ============================================================
     * CLOSE WINDOW
     * ============================================================
     */

    private void closeWindow() {

        if (firstNameField != null
                && firstNameField.getScene() != null) {

            Stage stage =
                    (Stage) firstNameField
                            .getScene()
                            .getWindow();

            stage.close();
        }
    }


    /*
     * ============================================================
     * ERROR
     * ============================================================
     */

    private void showError(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Student Registration System"
        );

        alert.setHeaderText(
                "Add Lecturer"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    /*
     * ============================================================
     * SUCCESS
     * ============================================================
     */

    private void showSuccess(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Student Registration System"
        );

        alert.setHeaderText(
                "Lecturer Added"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    /*
     * ============================================================
     * FULL ERROR MESSAGE
     * ============================================================
     */

    private String getFullErrorMessage(
            Exception e) {

        if (e.getMessage() != null
                && !e.getMessage().isBlank()) {

            return e.getMessage();
        }

        if (e.getCause() != null
                && e.getCause().getMessage() != null) {

            return e.getCause().getMessage();
        }

        return e.toString();
    }
}