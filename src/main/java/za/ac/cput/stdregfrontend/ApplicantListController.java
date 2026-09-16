package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import za.ac.cput.domain.Applicant;
import za.ac.cput.service.ApplicantService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicantListController {

    // ============================================================
    // FXML COMPONENTS
    // ============================================================

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Applicant> applicantTable;

    @FXML
    private TableColumn<Applicant, Integer> personIdColumn;

    @FXML
    private TableColumn<Applicant, Integer> applicantIdColumn;

    @FXML
    private TableColumn<Applicant, String> firstNameColumn;

    @FXML
    private TableColumn<Applicant, String> lastNameColumn;

    @FXML
    private TableColumn<Applicant, String> genderColumn;

    @FXML
    private TableColumn<Applicant, String> raceColumn;


    // ============================================================
    // SERVICE
    // ============================================================

    private final ApplicantService applicantService =
            new ApplicantService();


    // ============================================================
    // TABLE DATA
    // ============================================================

    private final ObservableList<Applicant> applicantList =
            FXCollections.observableArrayList();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        personIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("personId")
        );

        applicantIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("applicantId")
        );

        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );

        genderColumn.setCellValueFactory(
                new PropertyValueFactory<>("gender")
        );

        raceColumn.setCellValueFactory(
                new PropertyValueFactory<>("race")
        );

        applicantTable.setItems(applicantList);

        loadApplicants();
    }


    // ============================================================
    // LOAD APPLICANTS
    // ============================================================

    private void loadApplicants() {

        try {

            List<Applicant> applicants =
                    applicantService.getAll();

            applicantList.setAll(applicants);

        } catch (Exception e) {

            showError(
                    "Load Applicants Error",
                    "Could not load applicants from the backend.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    private void onSearchClick() {

        String searchText =
                searchField.getText();

        if (searchText == null ||
                searchText.trim().isEmpty()) {

            loadApplicants();

            return;
        }

        String search =
                searchText.trim().toLowerCase();

        try {

            List<Applicant> applicants =
                    applicantService.getAll();

            List<Applicant> filteredApplicants =
                    applicants.stream()
                            .filter(applicant -> {

                                String applicantId =
                                        String.valueOf(
                                                applicant.getApplicantId()
                                        );

                                String personId =
                                        String.valueOf(
                                                applicant.getPersonId()
                                        );

                                String firstName =
                                        safe(
                                                applicant.getFirstName()
                                        );

                                String lastName =
                                        safe(
                                                applicant.getLastName()
                                        );

                                String gender =
                                        applicant.getGender() == null
                                                ? ""
                                                : applicant.getGender()
                                                .toString();

                                String race =
                                        applicant.getRace() == null
                                                ? ""
                                                : applicant.getRace()
                                                .toString();

                                return applicantId
                                        .toLowerCase()
                                        .contains(search)

                                        || personId
                                        .toLowerCase()
                                        .contains(search)

                                        || firstName
                                        .toLowerCase()
                                        .contains(search)

                                        || lastName
                                        .toLowerCase()
                                        .contains(search)

                                        || gender
                                        .toLowerCase()
                                        .contains(search)

                                        || race
                                        .toLowerCase()
                                        .contains(search);
                            })
                            .collect(Collectors.toList());

            applicantList.setAll(filteredApplicants);

        } catch (Exception e) {

            showError(
                    "Search Error",
                    "Could not search applicants.\n\n"
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

        loadApplicants();
    }


    // ============================================================
    // ADD APPLICANT
    // ============================================================

    @FXML
    private void onAddClick() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/add-applicant-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Add Applicant"
            );

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            loadApplicants();

        } catch (IOException e) {

            showError(
                    "Add Applicant Error",
                    "Could not open the Add Applicant screen.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // VIEW APPLICANT
    // ============================================================

    @FXML
    private void onViewClick() {

        Applicant selectedApplicant =
                applicantTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedApplicant == null) {

            showInformation(
                    "View Applicant",
                    "Please select an applicant first."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/applicant-details-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            ApplicantDetailsController controller =
                    loader.getController();

            controller.loadApplicant(
                    selectedApplicant.getPersonId()
            );

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Applicant Details - "
                            + selectedApplicant.getApplicantId()
            );

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            showError(
                    "View Applicant Error",
                    "Could not open the Applicant Details screen.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // DELETE APPLICANT
    // ============================================================

    @FXML
    private void onDeleteClick() {

        Applicant selectedApplicant =
                applicantTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedApplicant == null) {

            showInformation(
                    "Delete Applicant",
                    "Please select an applicant first."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Applicant"
        );

        confirmation.setHeaderText(
                "Delete Applicant"
        );

        confirmation.setContentText(
                "Are you sure you want to delete applicant "
                        + selectedApplicant.getApplicantId()
                        + " - "
                        + selectedApplicant.getFirstName()
                        + " "
                        + selectedApplicant.getLastName()
                        + "?"
        );

        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response == ButtonType.OK) {

                        try {

                            applicantService.delete(
                                    selectedApplicant.getPersonId()
                            );

                            showInformation(
                                    "Delete Applicant",
                                    "Applicant deleted successfully."
                            );

                            loadApplicants();

                        } catch (Exception e) {

                            showError(
                                    "Delete Applicant Error",
                                    "Could not delete the applicant.\n\n"
                                            + getFullErrorMessage(e)
                            );
                        }
                    }
                });
    }


    // ============================================================
    // BACK TO ADMIN DASHBOARD
    // ============================================================

    @FXML
    private void onBackClick(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/za/ac/cput/stdregfrontend/admin-dashboard-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            /*
             * Get the actual Back button that
             * triggered this event.
             */
            Node source =
                    (Node) event.getSource();

            /*
             * Get the current application window.
             */
            Stage currentStage =
                    (Stage) source
                            .getScene()
                            .getWindow();

            /*
             * Replace the current scene.
             */
            currentStage.setScene(
                    new Scene(root)
            );

            currentStage.setTitle(
                    "Admin Dashboard"
            );

            currentStage.show();

        } catch (IOException | RuntimeException e) {

            e.printStackTrace();

            showError(
                    "Navigation Error",
                    "Could not return to the Admin Dashboard.\n\n"
                            + getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }


    // ============================================================
    // INFORMATION ALERT
    // ============================================================

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


    // ============================================================
    // ERROR ALERT
    // ============================================================

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
                message == null ||
                        message.trim().isEmpty()
                        ? "Unknown error."
                        : message
        );

        alert.showAndWait();
    }


    // ============================================================
    // FULL ERROR MESSAGE
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

                message.append(": ");

                message.append(
                        current.getMessage()
                );
            }

            message.append("\n\n");

            current =
                    current.getCause();
        }

        return message.toString();
    }
}