package za.ac.cput.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.domain.Applicant;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApplicantService {

    private static final String APPLICANTS_URL =
            ApiConfig.BASE_URL + "/api/applicants";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public ApplicantService() {

        httpClient = HttpClient.newHttpClient();
        mapper = JsonMapperFactory.get();
    }

    // =========================================================
    // GET ALL APPLICANTS
    // =========================================================

    public List<Applicant> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICANTS_URL
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
                "Failed to load applicants."
        );

        return mapper.readValue(
                response.body(),
                new TypeReference<List<Applicant>>() {}
        );
    }

    // =========================================================
    // GET APPLICANT BY PERSON ID
    // =========================================================

    public Applicant getById(int personId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICANTS_URL
                                                + "/"
                                                + personId
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
                "Failed to load applicant."
        );

        return mapper.readValue(
                response.body(),
                Applicant.class
        );
    }

    // =========================================================
    // CREATE APPLICANT
    // =========================================================

    public Applicant create(Applicant applicant)
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        applicant
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICANTS_URL
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
                "Failed to create applicant."
        );

        return mapper.readValue(
                response.body(),
                Applicant.class
        );
    }

    // =========================================================
    // UPDATE APPLICANT
    // =========================================================

    public Applicant update(
            int personId,
            Applicant applicant)
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        applicant
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICANTS_URL
                                                + "/"
                                                + personId
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

        checkResponse(
                response,
                "Failed to update applicant."
        );

        return mapper.readValue(
                response.body(),
                Applicant.class
        );
    }

    // =========================================================
    // DELETE APPLICANT
    // =========================================================

    public boolean delete(int personId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        APPLICANTS_URL
                                                + "/"
                                                + personId
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
                "Failed to delete applicant."
        );

        return true;
    }

    // =========================================================
    // RESPONSE CHECK
    // =========================================================

    private void checkResponse(
            HttpResponse<String> response,
            String operation)
            throws IOException {

        if (response.statusCode() >= 400) {

            throw new IOException(
                    operation
                            + "\nHTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }
    }
}