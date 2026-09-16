package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Department;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.service.DepartmentService;
import za.ac.cput.service.LecturerService;

import java.util.List;
import java.util.Map;

public class EditLecturerController {

    // ============================================================
    // FXML FIELDS
    // ============================================================

    @FXML
    private Label personIdLabel;

    @FXML
    private Label lecturerIdLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField employeeNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<Department> departmentComboBox;


    // ============================================================
    // SERVICES
    // ============================================================

    private final LecturerService lecturerService =
            new LecturerService();

    private final DepartmentService departmentService =
            new DepartmentService();


    // ============================================================
    // CURRENT LECTURER
    // ============================================================

    private Lecturer existingLecturer;

    /*
     * We keep the department ID separately.
     *
     * This prevents the Edit screen from depending on the
     * nested Department object inside Lecturer being populated.
     */
    private Integer existingDepartmentId;


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        /*
         * Display departments in the ComboBox as:
         *
         * ICT - Information and Communication Technology
         *
         * instead of:
         *
         * Department{departmentId=1,...}
         */

        departmentComboBox.setCellFactory(listView ->
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


        /*
         * Display the selected department using the
         * same format.
         */

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


    // ============================================================
    // LOAD LECTURER
    // ============================================================

    public void loadLecturer(int personId) {

        try {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Opening Edit Lecturer"
            );

            System.out.println(
                    "Person ID: " + personId
            );

            System.out.println(
                    "========================================"
            );


            /*
             * ----------------------------------------------------
             * STEP 1:
             * Load the lecturer itself.
             * ----------------------------------------------------
             */

            existingLecturer =
                    lecturerService.getById(personId);


            if (existingLecturer == null) {

                showError(
                        "Lecturer with Person ID "
                                + personId
                                + " was not found."
                );

                return;
            }


            /*
             * ----------------------------------------------------
             * STEP 2:
             * Load the detailed lecturer information.
             *
             * The backend explicitly returns:
             *
             * departmentId
             * departmentCode
             * departmentName
             *
             * This is more reliable than depending on the
             * nested Department object inside Lecturer.
             * ----------------------------------------------------
             */

            Map<String, Object> details =
                    lecturerService.getDetails(personId);


            if (details != null) {

                Object departmentIdObject =
                        details.get("departmentId");


                if (departmentIdObject instanceof Number number) {

                    existingDepartmentId =
                            number.intValue();

                } else if (departmentIdObject != null) {

                    try {

                        existingDepartmentId =
                                Integer.parseInt(
                                        String.valueOf(
                                                departmentIdObject
                                        )
                                );

                    } catch (NumberFormatException ignored) {

                        existingDepartmentId = null;
                    }
                }
            }


            System.out.println(
                    "Existing department ID: "
                            + existingDepartmentId
            );


            /*
             * ----------------------------------------------------
             * STEP 3:
             * Display IDs.
             * These are not editable.
             * ----------------------------------------------------
             */

            personIdLabel.setText(
                    String.valueOf(
                            existingLecturer.getPersonId()
                    )
            );

            lecturerIdLabel.setText(
                    String.valueOf(
                            existingLecturer.getLecturerId()
                    )
            );


            /*
             * ----------------------------------------------------
             * STEP 4:
             * Display lecturer information.
             * ----------------------------------------------------
             */

            firstNameField.setText(
                    safeValue(
                            existingLecturer.getFirstName()
                    )
            );

            lastNameField.setText(
                    safeValue(
                            existingLecturer.getLastName()
                    )
            );

            employeeNumberField.setText(
                    safeValue(
                            existingLecturer.getEmployeeNumber()
                    )
            );

            emailField.setText(
                    safeValue(
                            existingLecturer.getLecturerEmail()
                    )
            );


            /*
             * ----------------------------------------------------
             * STEP 5:
             * Load departments.
             * ----------------------------------------------------
             */

            loadDepartments();


            /*
             * ----------------------------------------------------
             * STEP 6:
             * Select the lecturer's current department.
             * ----------------------------------------------------
             */

            selectCurrentDepartment();


            System.out.println(
                    "Edit Lecturer loaded successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not load lecturer.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // LOAD DEPARTMENTS
    // ============================================================

    private void loadDepartments() {

        try {

            System.out.println(
                    "Loading departments from backend..."
            );


            List<Department> departments =
                    departmentService.getAll();


            if (departments == null) {

                throw new IllegalStateException(
                        "Backend returned no department list."
                );
            }


            System.out.println(
                    "Departments received: "
                            + departments.size()
            );


            departmentComboBox
                    .getItems()
                    .setAll(departments);


            /*
             * Print departments to the console so we can
             * immediately see whether the backend returned them.
             */

            for (Department department : departments) {

                System.out.println(
                        "Department: "
                                + department.getDepartmentId()
                                + " - "
                                + department.getDepartmentCode()
                                + " - "
                                + department.getDepartmentName()
                );
            }


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not load departments.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SELECT CURRENT DEPARTMENT
    // ============================================================

    private void selectCurrentDepartment() {

        if (existingDepartmentId == null) {

            System.out.println(
                    "No existing department ID was returned."
            );

            departmentComboBox
                    .getSelectionModel()
                    .clearSelection();

            return;
        }


        System.out.println(
                "Searching ComboBox for department ID: "
                        + existingDepartmentId
        );


        for (Department department :
                departmentComboBox.getItems()) {

            if (department.getDepartmentId()
                    == existingDepartmentId) {

                departmentComboBox
                        .getSelectionModel()
                        .select(department);


                System.out.println(
                        "Selected department: "
                                + department.getDepartmentCode()
                                + " - "
                                + department.getDepartmentName()
                );

                return;
            }
        }


        System.out.println(
                "Department ID "
                        + existingDepartmentId
                        + " was not found in ComboBox."
        );
    }


    // ============================================================
    // SAVE
    // ============================================================

    @FXML
    protected void onSaveClick() {

        if (existingLecturer == null) {

            showError(
                    "No lecturer has been loaded."
            );

            return;
        }


        String firstName =
                firstNameField.getText() == null
                        ? ""
                        : firstNameField.getText().trim();


        String lastName =
                lastNameField.getText() == null
                        ? ""
                        : lastNameField.getText().trim();


        String employeeNumber =
                employeeNumberField.getText() == null
                        ? ""
                        : employeeNumberField.getText().trim();


        String email =
                emailField.getText() == null
                        ? ""
                        : emailField.getText()
                        .trim()
                        .toLowerCase();


        Department selectedDepartment =
                departmentComboBox
                        .getSelectionModel()
                        .getSelectedItem();


        // ========================================================
        // VALIDATION
        // ========================================================

        if (firstName.isEmpty()) {

            showError(
                    "Please enter the first name."
            );

            return;
        }


        if (lastName.isEmpty()) {

            showError(
                    "Please enter the last name."
            );

            return;
        }


        if (employeeNumber.isEmpty()) {

            showError(
                    "Please enter the employee number."
            );

            return;
        }


        if (email.isEmpty()) {

            showError(
                    "Please enter the email address."
            );

            return;
        }


        if (!email.endsWith("@cput.ac.za")) {

            showError(
                    "Lecturer email must end with @cput.ac.za."
            );

            return;
        }


        if (selectedDepartment == null) {

            showError(
                    "Please select a department."
            );

            return;
        }


        try {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Saving Lecturer"
            );

            System.out.println(
                    "Person ID: "
                            + existingLecturer.getPersonId()
            );

            System.out.println(
                    "Department ID: "
                            + selectedDepartment.getDepartmentId()
            );

            System.out.println(
                    "Department: "
                            + selectedDepartment.getDepartmentCode()
                            + " - "
                            + selectedDepartment.getDepartmentName()
            );

            System.out.println(
                    "========================================"
            );


            /*
             * Rebuild the Lecturer using the original information
             * that we are not editing on this screen.
             *
             * IDs remain unchanged.
             */

            Lecturer updatedLecturer =
                    new Lecturer.Builder()

                            .setPersonId(
                                    existingLecturer.getPersonId()
                            )

                            .setLecturerId(
                                    existingLecturer.getLecturerId()
                            )

                            .setFirstName(
                                    firstName
                            )

                            .setLastName(
                                    lastName
                            )

                            .setDateOfBirth(
                                    existingLecturer.getDateOfBirth()
                            )

                            .setAddress(
                                    existingLecturer.getAddress()
                            )

                            .setContactDetails(
                                    existingLecturer.getContactDetails()
                            )

                            .setGender(
                                    existingLecturer.getGender()
                            )

                            .setRace(
                                    existingLecturer.getRace()
                            )

                            .setEmployeeNumber(
                                    employeeNumber
                            )

                            .setLecturerEmail(
                                    email
                            )

                            .setDepartment(
                                    selectedDepartment
                            )

                            .build();


            /*
             * Send update to backend.
             */

            Lecturer savedLecturer =
                    lecturerService.update(
                            updatedLecturer
                    );


            if (savedLecturer == null) {

                showError(
                        "The lecturer could not be updated."
                );

                return;
            }


            showInformation(
                    "Lecturer updated successfully."
            );


            closeWindow();


        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Could not update lecturer.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // CANCEL
    // ============================================================

    @FXML
    protected void onCancelClick() {

        closeWindow();
    }


    // ============================================================
    // CLOSE WINDOW
    // ============================================================

    private void closeWindow() {

        if (personIdLabel != null
                && personIdLabel.getScene() != null) {

            Stage stage =
                    (Stage) personIdLabel
                            .getScene()
                            .getWindow();

            stage.close();
        }
    }


    // ============================================================
    // SAFE VALUE
    // ============================================================

    private String safeValue(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "";
        }

        return value;
    }


    // ============================================================
    // INFORMATION ALERT
    // ============================================================

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
                "Lecturer Updated"
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    // ============================================================
    // ERROR ALERT
    // ============================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Student Registration System"
        );

        alert.setHeaderText(
                "Edit Lecturer"
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
            Throwable e) {

        StringBuilder message =
                new StringBuilder();

        Throwable current = e;


        while (current != null) {

            message.append(
                    current
                            .getClass()
                            .getSimpleName()
            );


            if (current.getMessage() != null
                    && !current.getMessage()
                    .isBlank()) {

                message.append(
                        ": "
                ).append(
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