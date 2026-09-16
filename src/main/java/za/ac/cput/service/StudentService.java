package za.ac.cput.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.domain.Student;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class StudentService {

    private static final String STUDENTS_URL =
            ApiConfig.BASE_URL + "/api/students";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public StudentService() {

        httpClient = HttpClient.newHttpClient();

        mapper = JsonMapperFactory.get();
    }


    // ============================================================
    // GET ALL STUDENTS
    // ============================================================

    public List<Student> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        STUDENTS_URL
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to load students.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                new TypeReference<List<Student>>() {}
        );
    }


    // ============================================================
    // GET STUDENT BY STUDENT ID
    // ============================================================

    public Student getById(int studentId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        STUDENTS_URL
                                                + "/"
                                                + studentId
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            return null;
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to load student.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Student.class
        );
    }


    // ============================================================
    // GET COMPLETE STUDENT DETAILS
    // ============================================================
    //
    // This endpoint returns:
    //
    // {
    //     "student": {...},
    //     "application": {...},
    //     "course": {...},
    //     "registeredClasses": [...]
    // }
    //
    // The nested "student" object is converted into an actual
    // Student object before being returned to the controller.
    //
    // ============================================================

    public Map<String, Object> getStudentDetails(
            int studentId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        STUDENTS_URL
                                                + "/"
                                                + studentId
                                                + "/details"
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            return null;
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to load student details.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        // --------------------------------------------------------
        // First convert the complete response to a Map
        // --------------------------------------------------------

        Map<String, Object> details =
                mapper.readValue(
                        response.body(),
                        new TypeReference<Map<String, Object>>() {}
                );

        // --------------------------------------------------------
        // Convert nested student map into Student object
        // --------------------------------------------------------

        Object studentObject =
                details.get("student");

        if (studentObject != null) {

            Student student =
                    mapper.convertValue(
                            studentObject,
                            Student.class
                    );

            details.put(
                    "student",
                    student
            );
        }

        return details;
    }


    // ============================================================
    // CREATE STUDENT WITH PASSWORD
    // ============================================================
    //
    // Sends:
    //
    // {
    //     "student": {
    //         ...
    //     },
    //     "password": "..."
    // }
    //
    // The backend then:
    // 1. Generates Student ID
    // 2. Generates Student Number
    // 3. Generates Student Email
    // 4. Creates the Student
    // 5. Creates the Student Account
    // 6. Uses the supplied password for login
    //
    // ============================================================

    public Student create(
            Student student,
            String password)
            throws IOException, InterruptedException {

        if (student == null) {

            throw new IllegalArgumentException(
                    "Student is required."
            );
        }

        if (password == null ||
                password.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Student password is required."
            );
        }

        // --------------------------------------------------------
        // Create request body
        // --------------------------------------------------------

        Map<String, Object> requestBody =
                Map.of(
                        "student",
                        student,
                        "password",
                        password.trim()
                );

        String json =
                mapper.writeValueAsString(
                        requestBody
                );

        // --------------------------------------------------------
        // Build HTTP request
        // --------------------------------------------------------

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        STUDENTS_URL
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();

        // --------------------------------------------------------
        // Send request
        // --------------------------------------------------------

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        // --------------------------------------------------------
        // Handle errors
        // --------------------------------------------------------

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to create student.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        // --------------------------------------------------------
        // Convert response to Student
        // --------------------------------------------------------

        return mapper.readValue(
                response.body(),
                Student.class
        );
    }


    // ============================================================
    // CREATE STUDENT WITHOUT PASSWORD
    // ============================================================
    //
    // Kept for compatibility with existing frontend code.
    //
    // New Student creation screens should use:
    //
    // create(student, password)
    //
    // ============================================================

    public Student create(Student student)
            throws IOException, InterruptedException {

        return create(
                student,
                "student123"
        );
    }


    // ============================================================
    // UPDATE STUDENT
    // ============================================================

    public Student update(Student student)
            throws IOException, InterruptedException {

        if (student == null) {

            throw new IllegalArgumentException(
                    "Student is required."
            );
        }

        if (student.getStudentId() <= 0) {

            throw new IllegalArgumentException(
                    "A valid student ID is required."
            );
        }

        String json =
                mapper.writeValueAsString(
                        student
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        STUDENTS_URL
                                                + "/"
                                                + student.getStudentId()
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .PUT(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            return null;
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to update student.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Student.class
        );
    }


    // ============================================================
    // DELETE STUDENT
    // ============================================================

    public boolean delete(int studentId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        STUDENTS_URL
                                                + "/"
                                                + studentId
                                )
                        )
                        .DELETE()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            return false;
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to delete student.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return true;
    }
}