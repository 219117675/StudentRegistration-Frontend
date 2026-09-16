package za.ac.cput.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.ac.cput.domain.Application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ApplicationService {

    private static final String APPLICATIONS_URL =
            ApiConfig.BASE_URL + "/api/applications";

    private final HttpClient httpClient;

    private final ObjectMapper mapper;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ApplicationService() {

        httpClient =
                HttpClient.newHttpClient();

        mapper =
                JsonMapperFactory.get();
    }


    // ============================================================
    // GET ALL
    // ============================================================

    public List<Application> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
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
                "Failed to load applications."
        );


        JavaType listType =
                mapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                Application.class
                        );


        return mapper.readValue(
                response.body(),
                listType
        );
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    public Application getById(
            int applicationId
    )
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
                                                + "/"
                                                + applicationId
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


        checkResponse(
                response,
                "Failed to load application."
        );


        return mapper.readValue(
                response.body(),
                Application.class
        );
    }


    // ============================================================
    // CREATE
    // ============================================================

    public Application create(
            Application application
    )
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        application
                );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        json
                                )
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        checkResponse(
                response,
                "Failed to create application."
        );


        return mapper.readValue(
                response.body(),
                Application.class
        );
    }


    // ============================================================
    // UPDATE
    // ============================================================

    public Application update(
            Application application
    )
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        application
                );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
                                                + "/"
                                                + application
                                                .getApplicationId()
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .PUT(
                                HttpRequest.BodyPublishers.ofString(
                                        json
                                )
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        checkResponse(
                response,
                "Failed to update application."
        );


        return mapper.readValue(
                response.body(),
                Application.class
        );
    }


    // ============================================================
    // DELETE
    // ============================================================

    public void delete(
            int applicationId
    )
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
                                                + "/"
                                                + applicationId
                                )
                        )
                        .DELETE()
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        checkResponse(
                response,
                "Failed to delete application."
        );
    }


    // ============================================================
    // APPROVE APPLICATION
    // ============================================================

    public Map<String, Object> approve(
            int applicationId
    )
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
                                                + "/"
                                                + applicationId
                                                + "/approve"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.noBody()
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        checkResponse(
                response,
                "Failed to approve application."
        );


        JavaType mapType =
                mapper.getTypeFactory()
                        .constructMapType(
                                Map.class,
                                String.class,
                                Object.class
                        );


        return mapper.readValue(
                response.body(),
                mapType
        );
    }


    // ============================================================
    // REJECT APPLICATION
    // ============================================================

    public Application reject(
            int applicationId
    )
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICATIONS_URL
                                                + "/"
                                                + applicationId
                                                + "/reject"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.noBody()
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        checkResponse(
                response,
                "Failed to reject application."
        );


        return mapper.readValue(
                response.body(),
                Application.class
        );
    }


    // ============================================================
    // RESPONSE CHECK
    // ============================================================

    private void checkResponse(
            HttpResponse<String> response,
            String message
    )
            throws IOException {

        if (response.statusCode() >= 400) {

            throw new IOException(
                    message
                            + "\nHTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }
    }
}