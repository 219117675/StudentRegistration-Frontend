package za.ac.cput.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.domain.Department;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DepartmentService {

    // ============================================================
    // API URL
    // ============================================================

    private static final String DEPARTMENTS_URL =
            ApiConfig.BASE_URL + "/api/departments";


    // ============================================================
    // HTTP CLIENT / JSON MAPPER
    // ============================================================

    private final HttpClient httpClient;

    private final ObjectMapper mapper;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public DepartmentService() {

        this.httpClient =
                HttpClient.newHttpClient();

        this.mapper =
                JsonMapperFactory.get();
    }


    // ============================================================
    // GET ALL DEPARTMENTS
    // ============================================================

    public List<Department> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        DEPARTMENTS_URL
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
                    "Could not load departments. HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }


        JavaType listType =
                mapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                Department.class
                        );


        return mapper.readValue(
                response.body(),
                listType
        );
    }


    // ============================================================
    // GET DEPARTMENT BY ID
    // ============================================================

    public Department getById(
            int departmentId
    )
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        DEPARTMENTS_URL
                                                + "/"
                                                + departmentId
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
                    "Could not load department. HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }


        return mapper.readValue(
                response.body(),
                Department.class
        );
    }


    // ============================================================
    // CREATE DEPARTMENT
    // ============================================================

    public Department create(
            Department department
    )
            throws IOException, InterruptedException {

        if (department == null) {

            throw new IllegalArgumentException(
                    "Department is required."
            );
        }


        String json =
                mapper.writeValueAsString(
                        department
                );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        DEPARTMENTS_URL
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


        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to create department. HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }


        return mapper.readValue(
                response.body(),
                Department.class
        );
    }


    // ============================================================
    // UPDATE DEPARTMENT
    // ============================================================

    public Department update(
            Department department
    )
            throws IOException, InterruptedException {

        if (department == null) {

            throw new IllegalArgumentException(
                    "Department is required."
            );
        }


        if (department.getDepartmentId() <= 0) {

            throw new IllegalArgumentException(
                    "A valid department ID is required."
            );
        }


        int departmentId =
                department.getDepartmentId();


        String json =
                mapper.writeValueAsString(
                        department
                );


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        DEPARTMENTS_URL
                                                + "/"
                                                + departmentId
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


        if (response.statusCode() == 404) {

            throw new IOException(
                    "Department with ID "
                            + departmentId
                            + " was not found."
            );
        }


        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to update department. HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }


        return mapper.readValue(
                response.body(),
                Department.class
        );
    }


    // ============================================================
    // DELETE DEPARTMENT
    // ============================================================

    public boolean delete(
            int departmentId
    )
            throws IOException, InterruptedException {

        if (departmentId <= 0) {

            throw new IllegalArgumentException(
                    "A valid department ID is required."
            );
        }


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        DEPARTMENTS_URL
                                                + "/"
                                                + departmentId
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
                    "Failed to delete department. HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }


        return true;
    }
}