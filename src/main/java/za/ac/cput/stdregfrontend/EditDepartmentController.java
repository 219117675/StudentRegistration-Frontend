package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Department;
import za.ac.cput.service.DepartmentService;

public class EditDepartmentController {

    @FXML
    private TextField departmentIdField;

    @FXML
    private TextField departmentCodeField;

    @FXML
    private TextField departmentNameField;


    private final DepartmentService departmentService =
            new DepartmentService();


    private Department department;


    // =========================================================
    // LOAD DEPARTMENT
    // =========================================================

    public void setDepartment(
            Department department
    ) {

        this.department =
                department;


        departmentIdField.setText(
                String.valueOf(
                        department.getDepartmentId()
                )
        );


        departmentCodeField.setText(
                department.getDepartmentCode()
        );


        departmentNameField.setText(
                department.getDepartmentName()
        );
    }


    // =========================================================
    // SAVE
    // =========================================================

    @FXML
    private void onSaveClick() {

        if (department == null) {

            showError(
                    "Edit Department",
                    "No department was selected."
            );

            return;
        }


        String code =
                departmentCodeField.getText() == null
                        ? ""
                        : departmentCodeField
                        .getText()
                        .trim()
                        .toUpperCase();


        String name =
                departmentNameField.getText() == null
                        ? ""
                        : departmentNameField
                        .getText()
                        .trim();


        if (code.isEmpty()) {

            showError(
                    "Validation Error",
                    "Department code is required."
            );

            return;
        }


        if (name.isEmpty()) {

            showError(
                    "Validation Error",
                    "Department name is required."
            );

            return;
        }


        Department updatedDepartment =
                new Department.Builder()
                        .setDepartmentId(
                                department.getDepartmentId()
                        )
                        .setDepartmentCode(code)
                        .setDepartmentName(name)
                        .build();


        try {

            departmentService.update(
                    updatedDepartment
            );


            showInformation(
                    "Department Updated",
                    "Department updated successfully."
            );


            closeWindow();


        } catch (Exception e) {

            showError(
                    "Update Department",
                    e.getMessage()
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
    // CLOSE
    // =========================================================

    private void closeWindow() {

        Stage stage =
                (Stage) departmentCodeField
                        .getScene()
                        .getWindow();

        stage.close();
    }


    // =========================================================
    // DIALOGS
    // =========================================================

    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


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
        alert.setContentText(
                message == null
                        ? "Unknown error."
                        : message
        );

        alert.showAndWait();
    }
}