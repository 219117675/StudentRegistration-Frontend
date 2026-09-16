package za.ac.cput.stdregfrontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import za.ac.cput.domain.Lecturer;
import za.ac.cput.service.LecturerService;

import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    // TEMPORARY: this is just a connectivity smoke test for step 1.
    // It gets replaced once the real Lecturer list screen (step 2) is in.
    private final LecturerService lecturerService = new LecturerService();

    @FXML
    protected void onHelloButtonClick() {
        try {
            List<Lecturer> lecturers = lecturerService.getAll();
            welcomeText.setText("Connected to backend - found " + lecturers.size() + " lecturer(s).");
        } catch (Exception e) {
            welcomeText.setText("Could not reach backend: " + e.getMessage());
        }
    }
}