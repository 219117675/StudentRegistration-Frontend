package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import za.ac.cput.domain.Department;
import za.ac.cput.service.DepartmentService;

public class AddDepartmentController {

    @FXML
    private TextField departmentCodeField;

    @FXML
    private TextField departmentNameField;


    private final DepartmentService departmentService =
            new DepartmentService();


    @FXML
    private void onSaveClick() {

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


        Department department =
                new Department.Builder()
                        .setDepartmentCode(code)
                        .setDepartmentName(name)
                        .build();


        try {

            Department saved =
                    departmentService.create(
                            department
                    );


            showInformation(
                    "Department Created",
                    "Department created successfully."
                            + "\n\n"
                            + "ID: "
                            + saved.getDepartmentId()
                            + "\n"
                            + "Code: "
                            + saved.getDepartmentCode()
                            + "\n"
                            + "Name: "
                            + saved.getDepartmentName()
            );


            closeWindow();


        } catch (Exception e) {

            showError(
                    "Create Department",
                    e.getMessage()
            );
        }
    }


    @FXML
    private void onCancelClick() {

        closeWindow();
    }


    private void closeWindow() {

        Stage stage =
                (Stage) departmentCodeField
                        .getScene()
                        .getWindow();

        stage.close();
    }


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