package za.ac.cput.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.ac.cput.domain.Lecturer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class LecturerService {

    private static final String LECTURERS_URL =
            ApiConfig.BASE_URL + "/api/lecturers";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public LecturerService() {

        this.httpClient =
                HttpClient.newHttpClient();

        this.mapper =
                JsonMapperFactory.get();
    }


    /*
     * ============================================================
     * GET ALL LECTURERS
     * ============================================================
     */

    public List<Lecturer> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        LECTURERS_URL
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to load lecturers. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                new TypeReference<List<Lecturer>>() {}
        );
    }


    /*
     * ============================================================
     * GET LECTURER BY PERSON ID
     * ============================================================
     */

    public Lecturer getById(int personId)
            throws IOException, InterruptedException {

        String url =
                LECTURERS_URL
                        + "/"
                        + personId;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            throw new IOException(
                    "Lecturer with Person ID "
                            + personId
                            + " was not found."
            );
        }

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to load lecturer. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Lecturer.class
        );
    }


    /*
     * ============================================================
     * GET COMPLETE LECTURER DETAILS
     * ============================================================
     */

    public Map<String, Object> getDetails(int personId)
            throws IOException, InterruptedException {

        String url =
                LECTURERS_URL
                        + "/"
                        + personId
                        + "/details";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            throw new IOException(
                    "Lecturer with Person ID "
                            + personId
                            + " was not found."
            );
        }

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to load lecturer details. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                new TypeReference<Map<String, Object>>() {}
        );
    }


    /*
     * ============================================================
     * CREATE LECTURER
     * ============================================================
     *
     * Backwards-compatible method.
     *
     * This still works for any existing code that calls:
     *
     * lecturerService.create(lecturer)
     *
     * The backend will use its existing default behaviour.
     */

    public Lecturer create(Lecturer lecturer)
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        lecturer
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        LECTURERS_URL
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

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to create lecturer. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Lecturer.class
        );
    }


    /*
     * ============================================================
     * CREATE LECTURER WITH PASSWORD
     * ============================================================
     */

    public Lecturer create(
            Lecturer lecturer,
            String password)
            throws IOException, InterruptedException {

        if (lecturer == null) {

            throw new IllegalArgumentException(
                    "Lecturer cannot be null."
            );
        }

        if (password == null
                || password.isBlank()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        if (password.length() < 6) {

            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters."
            );
        }

        LecturerCreateRequest requestBody =
                new LecturerCreateRequest(
                        lecturer,
                        password
                );

        String json =
                mapper.writeValueAsString(
                        requestBody
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        LECTURERS_URL
                                                + "/with-password"
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

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to create lecturer. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Lecturer.class
        );
    }


    /*
     * ============================================================
     * UPDATE LECTURER
     * ============================================================
     */

    public Lecturer update(Lecturer lecturer)
            throws IOException, InterruptedException {

        if (lecturer == null) {

            throw new IllegalArgumentException(
                    "Lecturer cannot be null."
            );
        }

        int personId =
                lecturer.getPersonId();

        if (personId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid lecturer Person ID."
            );
        }

        String url =
                LECTURERS_URL
                        + "/"
                        + personId;

        String json =
                mapper.writeValueAsString(
                        lecturer
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
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

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to update lecturer. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Lecturer.class
        );
    }


    /*
     * ============================================================
     * DELETE LECTURER
     * ============================================================
     */

    public boolean delete(int personId)
            throws IOException, InterruptedException {

        if (personId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid lecturer Person ID."
            );
        }

        String url =
                LECTURERS_URL
                        + "/"
                        + personId;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
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

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Failed to delete lecturer. HTTP "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return true;
    }


    /*
     * ============================================================
     * REQUEST CLASS
     * ============================================================
     */

    public static class LecturerCreateRequest {

        private Lecturer lecturer;
        private String password;

        public LecturerCreateRequest() {
        }

        public LecturerCreateRequest(
                Lecturer lecturer,
                String password) {

            this.lecturer = lecturer;
            this.password = password;
        }

        public Lecturer getLecturer() {

            return lecturer;
        }

        public void setLecturer(
                Lecturer lecturer) {

            this.lecturer = lecturer;
        }

        public String getPassword() {

            return password;
        }

        public void setPassword(
                String password) {

            this.password = password;
        }
    }
}