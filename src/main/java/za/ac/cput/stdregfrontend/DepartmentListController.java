package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import za.ac.cput.domain.Department;
import za.ac.cput.service.DepartmentService;

import java.io.IOException;
import java.util.List;

public class DepartmentListController {

    // ============================================================
    // FXML FIELDS
    // ============================================================

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Department, Integer> departmentIdColumn;

    @FXML
    private TableColumn<Department, String> departmentCodeColumn;

    @FXML
    private TableColumn<Department, String> departmentNameColumn;

    @FXML
    private TextField searchField;


    // ============================================================
    // SERVICE
    // ============================================================

    private final DepartmentService departmentService =
            new DepartmentService();


    // ============================================================
    // DATA
    // ============================================================

    private final ObservableList<Department> departmentList =
            FXCollections.observableArrayList();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        departmentIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("departmentId")
        );

        departmentCodeColumn.setCellValueFactory(
                new PropertyValueFactory<>("departmentCode")
        );

        departmentNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("departmentName")
        );

        departmentTable.setItems(
                departmentList
        );

        loadDepartments();
    }


    // ============================================================
    // LOAD DEPARTMENTS
    // ============================================================

    private void loadDepartments() {

        try {

            List<Department> departments =
                    departmentService.getAll();

            departmentList.setAll(
                    departments
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Department Error",
                    "Could not load departments from the backend.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    private void onSearchClick() {

        String search =
                searchField.getText();

        if (search == null ||
                search.trim().isEmpty()) {

            loadDepartments();

            return;
        }

        search =
                search.trim()
                        .toLowerCase();

        try {

            List<Department> departments =
                    departmentService.getAll();

            ObservableList<Department> filtered =
                    FXCollections.observableArrayList();

            for (Department department : departments) {

                String id =
                        String.valueOf(
                                department.getDepartmentId()
                        );

                String code =
                        department.getDepartmentCode() == null
                                ? ""
                                : department
                                .getDepartmentCode()
                                .toLowerCase();

                String name =
                        department.getDepartmentName() == null
                                ? ""
                                : department
                                .getDepartmentName()
                                .toLowerCase();

                if (id.contains(search)
                        || code.contains(search)
                        || name.contains(search)) {

                    filtered.add(
                            department
                    );
                }
            }

            departmentList.setAll(
                    filtered
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Search Error",
                    "Could not search departments.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // REFRESH
    // ============================================================

    @FXML
    private void onRefreshClick() {

        searchField.clear();

        loadDepartments();
    }


    // ============================================================
    // ADD DEPARTMENT
    // ============================================================

    @FXML
    private void onAddDepartmentClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/add-department-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Add Department"
            );

            stage.setScene(
                    new Scene(
                            root,
                            500,
                            350
                    )
            );

            stage.showAndWait();

            loadDepartments();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Add Department Error",
                    "Could not open Add Department.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // VIEW DEPARTMENT
    // ============================================================

    @FXML
    private void onViewDepartmentClick() {

        Department department =
                departmentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (department == null) {

            showInformation(
                    "View Department",
                    "Please select a department first."
            );

            return;
        }

        String message =
                "Department ID: "
                        + department.getDepartmentId()
                        + "\n\n"
                        + "Department Code: "
                        + safe(
                        department.getDepartmentCode()
                )
                        + "\n\n"
                        + "Department Name: "
                        + safe(
                        department.getDepartmentName()
                );

        showInformation(
                "Department Details",
                message
        );
    }


    // ============================================================
    // EDIT DEPARTMENT
    // ============================================================

    @FXML
    private void onEditDepartmentClick() {

        Department department =
                departmentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (department == null) {

            showInformation(
                    "Edit Department",
                    "Please select a department first."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/edit-department-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            EditDepartmentController controller =
                    loader.getController();

            controller.setDepartment(
                    department
            );

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Edit Department"
            );

            stage.setScene(
                    new Scene(
                            root,
                            500,
                            400
                    )
            );

            stage.showAndWait();

            loadDepartments();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Edit Department Error",
                    "Could not open Edit Department.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // DELETE DEPARTMENT
    // ============================================================

    @FXML
    private void onDeleteDepartmentClick() {

        Department department =
                departmentTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (department == null) {

            showInformation(
                    "Delete Department",
                    "Please select a department first."
            );

            return;
        }

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(
                "Delete Department"
        );

        alert.setHeaderText(
                "Delete Department?"
        );

        alert.setContentText(
                "Are you sure you want to delete:\n\n"
                        + department.getDepartmentCode()
                        + " - "
                        + department.getDepartmentName()
        );

        alert.showAndWait()
                .ifPresent(response -> {

                    if (response == ButtonType.OK) {

                        deleteDepartment(
                                department
                        );
                    }
                });
    }


    // ============================================================
    // DELETE
    // ============================================================

    private void deleteDepartment(
            Department department
    ) {

        try {

            boolean deleted =
                    departmentService.delete(
                            department.getDepartmentId()
                    );

            if (deleted) {

                showInformation(
                        "Department Deleted",
                        "Department deleted successfully."
                );

                loadDepartments();

            } else {

                showError(
                        "Delete Department",
                        "Department was not found."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Delete Department Error",
                    "Could not delete department.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // BACK
    // ============================================================

    @FXML
    private void onBackClick() {

        try {

            HelloApplication.openDashboard(
                    "ADMIN"
            );

        } catch (IOException e) {

            e.printStackTrace();

            showError(
                    "Navigation Error",
                    "Could not return to Admin Dashboard.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SAFE VALUE
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


    // ============================================================
    // INFORMATION
    // ============================================================

    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    // ============================================================
    // ERROR
    // ============================================================

    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    // ============================================================
    // FULL ERROR
    // ============================================================

    private String getFullErrorMessage(
            Throwable e
    ) {

        StringBuilder message =
                new StringBuilder();

        Throwable current =
                e;

        while (current != null) {

            message.append(
                    current.getClass()
                            .getSimpleName()
            );

            if (current.getMessage() != null) {

                message.append(
                        ": "
                );

                message.append(
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