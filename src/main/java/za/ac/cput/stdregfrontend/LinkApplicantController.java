package za.ac.cput.stdregfrontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import za.ac.cput.domain.Applicant;
import za.ac.cput.domain.Student;
import za.ac.cput.service.ApplicantService;
import za.ac.cput.service.StudentService;

import java.util.List;

public class LinkApplicantController {

    @FXML
    private ComboBox<Applicant> applicantComboBox;

    @FXML
    private Button linkButton;

    @FXML
    private Button cancelButton;

    private final ApplicantService applicantService =
            new ApplicantService();

    private final StudentService studentService =
            new StudentService();

    private int studentId;

    private Student student;

    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    private void initialize() {

        try {

            List<Applicant> applicants =
                    applicantService.getAll();

            applicantComboBox.setItems(
                    FXCollections.observableArrayList(
                            applicants
                    )
            );

        } catch (Exception e) {

            showError(
                    "Unable to Load Applicants",
                    getErrorMessage(e)
            );
        }
    }

    // =========================================================
    // RECEIVE SELECTED STUDENT
    // =========================================================

    public void setStudent(Student student) {

        this.student = student;

        if (student != null) {
            this.studentId = student.getStudentId();
        }
    }

    // =========================================================
    // LINK APPLICANT
    // =========================================================

    @FXML
    private void onLinkApplicantClick() {

        if (student == null) {

            showError(
                    "No Student Selected",
                    "No student was selected."
            );

            return;
        }

        Applicant selectedApplicant =
                applicantComboBox.getValue();

        if (selectedApplicant == null) {

            showError(
                    "No Applicant Selected",
                    "Please select an applicant."
            );

            return;
        }

        try {

            /*
             * The existing StudentService.update()
             * already sends the Student object to:
             *
             * PUT /api/students/{studentId}
             *
             * The backend StudentService already contains:
             *
             * existing.setApplicant(student.getApplicant());
             *
             * Therefore we only need to place the
             * selected Applicant into the Student.
             */

            student.setApplicant(selectedApplicant);

            Student updatedStudent =
                    studentService.update(student);

            if (updatedStudent == null) {

                showError(
                        "Link Failed",
                        "The student could not be updated."
                );

                return;
            }

            showInformation(
                    "Applicant Linked",
                    "Applicant "
                            + selectedApplicant.getApplicantId()
                            + " has been linked to Student "
                            + student.getStudentId()
                            + "."
            );

            closeWindow();

        } catch (Exception e) {

            showError(
                    "Unable to Link Applicant",
                    getErrorMessage(e)
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

        if (cancelButton == null ||
                cancelButton.getScene() == null) {

            return;
        }

        Stage stage =
                (Stage) cancelButton
                        .getScene()
                        .getWindow();

        stage.close();
    }

    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private String getErrorMessage(Exception e) {

        if (e == null) {
            return "An unexpected error occurred.";
        }

        if (e.getMessage() == null ||
                e.getMessage().trim().isEmpty()) {

            return "An unexpected error occurred.";
        }

        return e.getMessage();
    }

    // =========================================================
    // ERROR ALERT
    // =========================================================

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // =========================================================
    // INFORMATION ALERT
    // =========================================================

    private void showInformation(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}