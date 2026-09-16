package za.ac.cput.stdregfrontend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import za.ac.cput.domain.Applicant;
import za.ac.cput.domain.Application;
import za.ac.cput.domain.Course;
import za.ac.cput.service.ApplicantService;
import za.ac.cput.service.ApplicationService;
import za.ac.cput.service.CourseService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationListController {

    // ============================================================
    // TABLE
    // ============================================================

    @FXML
    private TableView<Application> applicationTable;


    @FXML
    private TableColumn<Application, Number> applicationIdColumn;


    @FXML
    private TableColumn<Application, Number> applicantIdColumn;


    @FXML
    private TableColumn<Application, String> applicantNameColumn;


    @FXML
    private TableColumn<Application, String> courseCodeColumn;


    @FXML
    private TableColumn<Application, String> courseNameColumn;


    @FXML
    private TableColumn<Application, String> statusColumn;


    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    private TextField searchField;


    // ============================================================
    // BUTTONS
    // ============================================================

    @FXML
    private Button addButton;


    @FXML
    private Button editButton;


    @FXML
    private Button deleteButton;


    @FXML
    private Button approveButton;


    @FXML
    private Button rejectButton;


    @FXML
    private Button refreshButton;


    @FXML
    private Button backButton;


    // ============================================================
    // SERVICES
    // ============================================================

    private final ApplicationService applicationService =
            new ApplicationService();


    private final ApplicantService applicantService =
            new ApplicantService();


    private final CourseService courseService =
            new CourseService();


    // ============================================================
    // DATA
    // ============================================================

    private final ObservableList<Application> applicationList =
            FXCollections.observableArrayList();


    private List<Application> allApplications;


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupTable();

        loadApplications();


        if (searchField != null) {

            searchField.textProperty().addListener(
                    (observable, oldValue, newValue) ->
                            filterApplications(newValue)
            );
        }
    }


    // ============================================================
    // SETUP TABLE
    // ============================================================

    private void setupTable() {

        applicationIdColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getApplicationId()
                        )
        );


        applicantIdColumn.setCellValueFactory(
                data -> {

                    Applicant applicant =
                            data.getValue()
                                    .getApplicant();


                    if (applicant == null) {

                        return new SimpleIntegerProperty(0);
                    }


                    return new SimpleIntegerProperty(
                            applicant.getApplicantId()
                    );
                }
        );


        applicantNameColumn.setCellValueFactory(
                data -> {

                    Applicant applicant =
                            data.getValue()
                                    .getApplicant();


                    if (applicant == null) {

                        return new SimpleStringProperty(
                                "Not Assigned"
                        );
                    }


                    String firstName =
                            safe(
                                    applicant.getFirstName()
                            );


                    String lastName =
                            safe(
                                    applicant.getLastName()
                            );


                    String fullName =
                            (
                                    firstName
                                            + " "
                                            + lastName
                            ).trim();


                    if (fullName.isEmpty()) {

                        fullName =
                                "Not Assigned";
                    }


                    return new SimpleStringProperty(
                            fullName
                    );
                }
        );


        courseCodeColumn.setCellValueFactory(
                data -> {

                    Course course =
                            data.getValue()
                                    .getCourse();


                    if (course == null) {

                        return new SimpleStringProperty(
                                ""
                        );
                    }


                    return new SimpleStringProperty(
                            safe(
                                    course.getCourseCode()
                            )
                    );
                }
        );


        courseNameColumn.setCellValueFactory(
                data -> {

                    Course course =
                            data.getValue()
                                    .getCourse();


                    if (course == null) {

                        return new SimpleStringProperty(
                                ""
                        );
                    }


                    return new SimpleStringProperty(
                            safe(
                                    course.getCourseName()
                            )
                    );
                }
        );


        statusColumn.setCellValueFactory(
                data -> {

                    Application.Status status =
                            data.getValue()
                                    .getStatus();


                    if (status == null) {

                        return new SimpleStringProperty(
                                "PENDING"
                        );
                    }


                    return new SimpleStringProperty(
                            status.name()
                    );
                }
        );


        applicationTable.setItems(
                applicationList
        );
    }


    // ============================================================
    // LOAD APPLICATIONS
    // ============================================================

    private void loadApplications() {

        try {

            allApplications =
                    applicationService.getAll();


            if (allApplications == null) {

                allApplications =
                        FXCollections.observableArrayList();
            }


            applicationList.setAll(
                    allApplications
            );


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Load Applications Error",
                    "Could not load applications from the backend.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // SEARCH
    // ============================================================

    private void filterApplications(
            String searchText
    ) {

        if (allApplications == null) {

            return;
        }


        if (searchText == null
                || searchText.trim().isEmpty()) {

            applicationList.setAll(
                    allApplications
            );

            return;
        }


        String search =
                searchText
                        .trim()
                        .toLowerCase();


        List<Application> filteredApplications =
                allApplications.stream()
                        .filter(
                                application ->
                                        matchesSearch(
                                                application,
                                                search
                                        )
                        )
                        .collect(
                                Collectors.toList()
                        );


        applicationList.setAll(
                filteredApplications
        );
    }


    // ============================================================
    // SEARCH MATCH
    // ============================================================

    private boolean matchesSearch(
            Application application,
            String search
    ) {

        if (application == null) {

            return false;
        }


        Applicant applicant =
                application.getApplicant();


        Course course =
                application.getCourse();


        String applicationId =
                String.valueOf(
                        application.getApplicationId()
                );


        String applicantId =
                applicant == null
                        ? ""
                        : String.valueOf(
                        applicant.getApplicantId()
                );


        String applicantName =
                applicant == null
                        ? ""
                        : (
                        safe(
                                applicant.getFirstName()
                        )
                                + " "
                                + safe(
                                applicant.getLastName()
                        )
                );


        String courseCode =
                course == null
                        ? ""
                        : safe(
                        course.getCourseCode()
                );


        String courseName =
                course == null
                        ? ""
                        : safe(
                        course.getCourseName()
                );


        String status =
                application.getStatus() == null
                        ? ""
                        : application
                        .getStatus()
                        .name();


        return applicationId
                .toLowerCase()
                .contains(search)

                || applicantId
                .toLowerCase()
                .contains(search)

                || applicantName
                .toLowerCase()
                .contains(search)

                || courseCode
                .toLowerCase()
                .contains(search)

                || courseName
                .toLowerCase()
                .contains(search)

                || status
                .toLowerCase()
                .contains(search);
    }


    // ============================================================
    // ADD APPLICATION
    // ============================================================

    @FXML
    private void onAddApplicationClick() {

        openApplicationDialog(
                null
        );
    }


    // ============================================================
    // EDIT APPLICATION
    // ============================================================

    @FXML
    private void onEditApplicationClick() {

        Application selected =
                applicationTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showWarning(
                    "Please select an application first."
            );

            return;
        }


        if (selected.getStatus()
                == Application.Status.APPROVED) {

            showWarning(
                    "An approved application cannot be edited."
            );

            return;
        }


        openApplicationDialog(
                selected
        );
    }


    // ============================================================
    // DELETE APPLICATION
    // ============================================================

    @FXML
    private void onDeleteApplicationClick() {

        Application selected =
                applicationTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showWarning(
                    "Please select an application first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Delete Application"
        );


        confirmation.setHeaderText(
                "Delete Application?"
        );


        confirmation.setContentText(
                "Are you sure you want to delete application "
                        + selected.getApplicationId()
                        + "?"
        );


        confirmation.showAndWait()
                .ifPresent(
                        button -> {

                            if (button == ButtonType.OK) {

                                deleteApplication(
                                        selected
                                );
                            }
                        }
                );
    }


    // ============================================================
    // DELETE APPLICATION
    // ============================================================

    private void deleteApplication(
            Application application
    ) {

        try {

            applicationService.delete(
                    application.getApplicationId()
            );


            showInformation(
                    "Application Deleted",
                    "Application deleted successfully."
            );


            loadApplications();


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Delete Application Error",
                    "Could not delete the application.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // APPROVE APPLICATION
    // ============================================================

    @FXML
    private void onApproveApplicationClick() {

        Application selected =
                applicationTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showWarning(
                    "Please select an application first."
            );

            return;
        }


        if (selected.getStatus()
                == Application.Status.APPROVED) {

            showWarning(
                    "This application has already been approved."
            );

            return;
        }


        if (selected.getStatus()
                == Application.Status.REJECTED) {

            showWarning(
                    "A rejected application cannot be approved."
            );

            return;
        }


        Applicant applicant =
                selected.getApplicant();


        Course course =
                selected.getCourse();


        if (applicant == null) {

            showWarning(
                    "This application does not have an applicant."
            );

            return;
        }


        if (course == null) {

            showWarning(
                    "This application does not have a course."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Approve Application"
        );


        confirmation.setHeaderText(
                "Approve Application "
                        + selected.getApplicationId()
                        + "?"
        );


        confirmation.setContentText(
                "Applicant: "
                        + safe(
                        applicant.getFirstName()
                )
                        + " "
                        + safe(
                        applicant.getLastName()
                )
                        + "\n\n"
                        + "Course: "
                        + safe(
                        course.getCourseCode()
                )
                        + " - "
                        + safe(
                        course.getCourseName()
                )
                        + "\n\n"
                        + "Approving this application will automatically "
                        + "create a Student account."
        );


        confirmation.showAndWait()
                .ifPresent(
                        button -> {

                            if (button == ButtonType.OK) {

                                approveApplication(
                                        selected
                                );
                            }
                        }
                );
    }


    // ============================================================
    // APPROVE APPLICATION
    // ============================================================

    private void approveApplication(
            Application application
    ) {

        try {

            Map<String, Object> result =
                    applicationService.approve(
                            application
                                    .getApplicationId()
                    );


            Object studentNumber =
                    result == null
                            ? null
                            : result.get(
                            "studentNumber"
                    );


            Object studentEmail =
                    result == null
                            ? null
                            : result.get(
                            "studentEmail"
                    );


            Object temporaryPassword =
                    result == null
                            ? null
                            : result.get(
                            "temporaryPassword"
                    );


            StringBuilder message =
                    new StringBuilder();


            message.append(
                    "Application approved successfully."
            );


            if (studentNumber != null) {

                message.append(
                        "\n\nStudent Number: "
                );

                message.append(
                        studentNumber
                );
            }


            if (studentEmail != null) {

                message.append(
                        "\nStudent Email: "
                );

                message.append(
                        studentEmail
                );
            }


            if (temporaryPassword != null) {

                message.append(
                        "\nTemporary Password: "
                );

                message.append(
                        temporaryPassword
                );
            }


            message.append(
                    "\n\nThe student has been created automatically."
            );


            showInformation(
                    "Application Approved",
                    message.toString()
            );


            loadApplications();


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Approve Application Error",
                    "Could not approve the application.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // REJECT APPLICATION
    // ============================================================

    @FXML
    private void onRejectApplicationClick() {

        Application selected =
                applicationTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selected == null) {

            showWarning(
                    "Please select an application first."
            );

            return;
        }


        if (selected.getStatus()
                == Application.Status.APPROVED) {

            showWarning(
                    "An approved application cannot be rejected."
            );

            return;
        }


        if (selected.getStatus()
                == Application.Status.REJECTED) {

            showWarning(
                    "This application has already been rejected."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Reject Application"
        );


        confirmation.setHeaderText(
                "Reject Application "
                        + selected.getApplicationId()
                        + "?"
        );


        confirmation.setContentText(
                "Are you sure you want to reject this application?"
        );


        confirmation.showAndWait()
                .ifPresent(
                        button -> {

                            if (button == ButtonType.OK) {

                                rejectApplication(
                                        selected
                                );
                            }
                        }
                );
    }


    // ============================================================
    // REJECT APPLICATION
    // ============================================================

    private void rejectApplication(
            Application application
    ) {

        try {

            applicationService.reject(
                    application
                            .getApplicationId()
            );


            showInformation(
                    "Application Rejected",
                    "Application rejected successfully."
            );


            loadApplications();


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Reject Application Error",
                    "Could not reject the application.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // REFRESH
    // ============================================================

    @FXML
    private void onRefreshApplicationClick() {

        if (searchField != null) {

            searchField.clear();
        }


        loadApplications();
    }


    // ============================================================
    // BACK TO ADMIN DASHBOARD
    // ============================================================

    @FXML
    private void onBackClick() {

        try {

            String dashboardPath =
                    "/za/ac/cput/stdregfrontend/"
                            + "admin-dashboard-view.fxml";


            if (getClass().getResource(
                    dashboardPath
            ) == null) {

                showError(
                        "Navigation Error",
                        "Admin Dashboard not found.",
                        "Could not find:\n"
                                + dashboardPath
                );

                return;
            }


            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    dashboardPath
                            )
                    );


            Parent root =
                    loader.load();


            Stage stage =
                    (Stage) backButton
                            .getScene()
                            .getWindow();


            Scene scene =
                    new Scene(root);


            stage.setScene(
                    scene
            );


            stage.setTitle(
                    "Admin Dashboard"
            );


            stage.show();


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Navigation Error",
                    "Could not return to the Admin Dashboard.",
                    getFullErrorMessage(e)
            );
        }
    }


    // ============================================================
    // APPLICATION DIALOG
    // ============================================================

    private void openApplicationDialog(
            Application existingApplication
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();


        if (existingApplication == null) {

            dialog.setTitle(
                    "Add Application"
            );

            dialog.setHeaderText(
                    "Create a new application"
            );

        } else {

            dialog.setTitle(
                    "Edit Application"
            );

            dialog.setHeaderText(
                    "Edit application information"
            );
        }


        // ========================================================
        // BUTTONS
        // ========================================================

        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );


        ButtonType cancelButton =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        cancelButton
                );


        // ========================================================
        // GRID
        // ========================================================

        GridPane grid =
                new GridPane();


        grid.setHgap(15);

        grid.setVgap(15);


        grid.setPadding(
                new javafx.geometry.Insets(
                        20
                )
        );


        // ========================================================
        // APPLICANT
        // ========================================================

        Label applicantLabel =
                new Label(
                        "Applicant:"
                );


        ComboBox<Applicant> applicantCombo =
                new ComboBox<>();


        applicantCombo.setPrefWidth(
                350
        );


        applicantCombo.setPromptText(
                "Select Applicant"
        );


        // ========================================================
        // COURSE
        // ========================================================

        Label courseLabel =
                new Label(
                        "Course:"
                );


        ComboBox<Course> courseCombo =
                new ComboBox<>();


        courseCombo.setPrefWidth(
                350
        );


        courseCombo.setPromptText(
                "Select Course"
        );


        // ========================================================
        // LOAD APPLICANTS
        // ========================================================

        try {

            List<Applicant> applicants =
                    applicantService.getAll();


            if (applicants != null) {

                applicantCombo.getItems()
                        .setAll(
                                applicants
                        );
            }


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Applicant Loading Error",
                    "Could not load applicants.",
                    getFullErrorMessage(e)
            );

            return;
        }


        // ========================================================
        // LOAD COURSES
        // ========================================================

        try {

            List<Course> courses =
                    courseService.getAll();


            if (courses != null) {

                courseCombo.getItems()
                        .setAll(
                                courses
                        );
            }


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Course Loading Error",
                    "Could not load courses.",
                    getFullErrorMessage(e)
            );

            return;
        }


        // ========================================================
        // APPLICANT CELL
        // ========================================================

        applicantCombo.setCellFactory(
                listView ->
                        new ListCell<Applicant>() {

                            @Override
                            protected void updateItem(
                                    Applicant applicant,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        applicant,
                                        empty
                                );


                                if (empty
                                        || applicant == null) {

                                    setText(null);

                                } else {

                                    setText(
                                            applicant
                                                    .getApplicantId()
                                                    + " - "
                                                    + safe(
                                                    applicant
                                                            .getFirstName()
                                            )
                                                    + " "
                                                    + safe(
                                                    applicant
                                                            .getLastName()
                                            )
                                    );
                                }
                            }
                        }
        );


        applicantCombo.setButtonCell(
                new ListCell<Applicant>() {

                    @Override
                    protected void updateItem(
                            Applicant applicant,
                            boolean empty
                    ) {

                        super.updateItem(
                                applicant,
                                empty
                        );


                        if (empty
                                || applicant == null) {

                            setText(null);

                        } else {

                            setText(
                                    applicant
                                            .getApplicantId()
                                            + " - "
                                            + safe(
                                            applicant
                                                    .getFirstName()
                                    )
                                            + " "
                                            + safe(
                                            applicant
                                                    .getLastName()
                                    )
                            );
                        }
                    }
                }
        );


        // ========================================================
        // COURSE CELL
        // ========================================================

        courseCombo.setCellFactory(
                listView ->
                        new ListCell<Course>() {

                            @Override
                            protected void updateItem(
                                    Course course,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        course,
                                        empty
                                );


                                if (empty
                                        || course == null) {

                                    setText(null);

                                } else {

                                    setText(
                                            safe(
                                                    course
                                                            .getCourseCode()
                                            )
                                                    + " - "
                                                    + safe(
                                                    course
                                                            .getCourseName()
                                            )
                                    );
                                }
                            }
                        }
        );


        courseCombo.setButtonCell(
                new ListCell<Course>() {

                    @Override
                    protected void updateItem(
                            Course course,
                            boolean empty
                    ) {

                        super.updateItem(
                                course,
                                empty
                        );


                        if (empty
                                || course == null) {

                            setText(null);

                        } else {

                            setText(
                                    safe(
                                            course
                                                    .getCourseCode()
                                    )
                                            + " - "
                                            + safe(
                                            course
                                                    .getCourseName()
                                    )
                            );
                        }
                    }
                }
        );


        // ========================================================
        // EXISTING VALUES
        // ========================================================

        if (existingApplication != null) {

            Applicant existingApplicant =
                    existingApplication
                            .getApplicant();


            Course existingCourse =
                    existingApplication
                            .getCourse();


            if (existingApplicant != null) {

                applicantCombo.getItems()
                        .stream()
                        .filter(
                                applicant ->
                                        applicant
                                                .getApplicantId()
                                                ==
                                                existingApplicant
                                                        .getApplicantId()
                        )
                        .findFirst()
                        .ifPresent(
                                applicantCombo::setValue
                        );
            }


            if (existingCourse != null) {

                courseCombo.getItems()
                        .stream()
                        .filter(
                                course ->
                                        course
                                                .getCourseId()
                                                ==
                                                existingCourse
                                                        .getCourseId()
                        )
                        .findFirst()
                        .ifPresent(
                                courseCombo::setValue
                        );
            }
        }


        // ========================================================
        // ADD COMPONENTS
        // ========================================================

        grid.add(
                applicantLabel,
                0,
                0
        );


        grid.add(
                applicantCombo,
                1,
                0
        );


        grid.add(
                courseLabel,
                0,
                1
        );


        grid.add(
                courseCombo,
                1,
                1
        );


        dialog.getDialogPane()
                .setContent(
                        grid
                );


        // ========================================================
        // SAVE
        // ========================================================

        dialog.setResultConverter(
                button -> {

                    if (button != saveButton) {

                        return null;
                    }


                    Applicant applicant =
                            applicantCombo.getValue();


                    Course course =
                            courseCombo.getValue();


                    if (applicant == null) {

                        showWarning(
                                "Please select an applicant."
                        );

                        return null;
                    }


                    if (course == null) {

                        showWarning(
                                "Please select a course."
                        );

                        return null;
                    }


                    try {

                        // ==================================================
                        // CREATE
                        // ==================================================

                        if (existingApplication == null) {

                            Application application =
                                    new Application.Builder()
                                            .setApplicationId(0)
                                            .setApplicant(
                                                    applicant
                                            )
                                            .setCourse(
                                                    course
                                            )
                                            .setStatus(
                                                    Application.Status.PENDING
                                            )
                                            .build();


                            Application created =
                                    applicationService.create(
                                            application
                                    );


                            if (created == null) {

                                showError(
                                        "Application Error",
                                        "Application was not created.",
                                        "The backend did not return "
                                                + "a created application."
                                );

                                return null;
                            }


                            showInformation(
                                    "Application Created",
                                    "Application created successfully."
                            );


                        }

                        // ==================================================
                        // UPDATE
                        // ==================================================

                        else {

                            Application updatedApplication =
                                    new Application.Builder()
                                            .setApplicationId(
                                                    existingApplication
                                                            .getApplicationId()
                                            )
                                            .setApplicant(
                                                    applicant
                                            )
                                            .setCourse(
                                                    course
                                            )
                                            .setStatus(
                                                    existingApplication
                                                            .getStatus()
                                            )
                                            .build();


                            Application updated =
                                    applicationService.update(
                                            updatedApplication
                                    );


                            if (updated == null) {

                                showError(
                                        "Application Error",
                                        "Application was not updated.",
                                        "The backend did not return "
                                                + "an updated application."
                                );

                                return null;
                            }


                            showInformation(
                                    "Application Updated",
                                    "Application updated successfully."
                            );
                        }


                        loadApplications();


                    } catch (Exception e) {

                        e.printStackTrace();


                        showError(
                                "Save Application Error",
                                "Could not save the application.",
                                getFullErrorMessage(e)
                        );
                    }


                    return button;
                }
        );


        dialog.showAndWait();
    }


    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safe(
            String value
    ) {

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
    // WARNING ALERT
    // ============================================================

    private void showWarning(
            String message
    ) {

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


    // ============================================================
    // ERROR ALERT
    // ============================================================

    private void showError(
            String title,
            String header,
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
                header
        );


        alert.setContentText(
                message == null
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

            if (current.getMessage() != null
                    && !current.getMessage()
                    .trim()
                    .isEmpty()) {

                message.append(
                                current.getClass()
                                        .getSimpleName()
                        )
                        .append(": ")
                        .append(
                                current.getMessage()
                        )
                        .append("\n\n");
            }


            current =
                    current.getCause();
        }


        if (message.length() == 0) {

            return "An unexpected error occurred.";
        }


        return message.toString();
    }
}