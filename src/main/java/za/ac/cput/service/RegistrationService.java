package za.ac.cput.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.domain.Registration;
import za.ac.cput.service.ApiConfig;
import za.ac.cput.service.JsonMapperFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RegistrationService {

    private static final String REGISTRATIONS_URL =
            ApiConfig.BASE_URL + "/api/registrations";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public RegistrationService() {
        httpClient = HttpClient.newHttpClient();
        mapper = JsonMapperFactory.get();
    }

    // ============================================================
    // GET ALL REGISTRATIONS
    // ============================================================

    public List<Registration> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(REGISTRATIONS_URL))
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        checkResponse(response, "load registrations");

        return mapper.readValue(
                response.body(),
                new TypeReference<List<Registration>>() {}
        );
    }

    // ============================================================
    // GET REGISTRATION BY ID
    // ============================================================

    public Registration getById(int registrationId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/"
                                                + registrationId
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

        checkResponse(response, "load registration");

        return mapper.readValue(
                response.body(),
                Registration.class
        );
    }

    // ============================================================
    // GET REGISTRATIONS FOR A STUDENT
    // ============================================================

    public List<Registration> getForStudent(int studentId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/student/"
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

        checkResponse(
                response,
                "load student registrations"
        );

        return mapper.readValue(
                response.body(),
                new TypeReference<List<Registration>>() {}
        );
    }

    // ============================================================
    // GET REGISTRATIONS FOR A CLASS
    // ============================================================

    public List<Registration> getForClass(int classId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/class/"
                                                + classId
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        checkResponse(
                response,
                "load class registrations"
        );

        return mapper.readValue(
                response.body(),
                new TypeReference<List<Registration>>() {}
        );
    }

    // ============================================================
    // CHECK WHETHER STUDENT IS REGISTERED
    // ============================================================

    public boolean isStudentRegistered(
            int studentId,
            int classId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/check/"
                                                + studentId
                                                + "/"
                                                + classId
                                )
                        )
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        checkResponse(
                response,
                "check student registration"
        );

        return Boolean.parseBoolean(
                response.body()
        );
    }

    // ============================================================
    // REGISTER STUDENT FOR CLASS
    // ============================================================

    public Registration registerStudent(
            int studentId,
            int classId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/register/"
                                                + studentId
                                                + "/"
                                                + classId
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .noBody()
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        checkResponse(
                response,
                "register student"
        );

        return mapper.readValue(
                response.body(),
                Registration.class
        );
    }

    // ============================================================
    // UNREGISTER STUDENT FROM CLASS
    // ============================================================

    public boolean unregisterStudent(
            int studentId,
            int classId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/unregister/"
                                                + studentId
                                                + "/"
                                                + classId
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

        checkResponse(
                response,
                "unregister student"
        );

        return true;
    }

    // ============================================================
    // CREATE REGISTRATION
    // ============================================================

    public Registration create(
            Registration registration)
            throws IOException, InterruptedException {

        if (registration == null) {
            throw new IllegalArgumentException(
                    "Registration is required."
            );
        }

        String json =
                mapper.writeValueAsString(
                        registration
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
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

        checkResponse(
                response,
                "create registration"
        );

        return mapper.readValue(
                response.body(),
                Registration.class
        );
    }

    // ============================================================
    // UPDATE REGISTRATION
    // ============================================================

    public Registration update(
            int registrationId,
            Registration registration)
            throws IOException, InterruptedException {

        if (registration == null) {
            throw new IllegalArgumentException(
                    "Registration is required."
            );
        }

        String json =
                mapper.writeValueAsString(
                        registration
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/"
                                                + registrationId
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

        checkResponse(
                response,
                "update registration"
        );

        return mapper.readValue(
                response.body(),
                Registration.class
        );
    }

    // ============================================================
    // DELETE REGISTRATION
    // ============================================================

    public boolean delete(int registrationId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        REGISTRATIONS_URL
                                                + "/"
                                                + registrationId
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

        checkResponse(
                response,
                "delete registration"
        );

        return true;
    }

    // ============================================================
    // RESPONSE HANDLING
    // ============================================================

    private void checkResponse(
            HttpResponse<String> response,
            String operation)
            throws IOException {

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to "
                            + operation
                            + ".\nHTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }
    }
}