package za.ac.cput.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.ac.cput.domain.Course;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CourseService {

    // ============================================================
    // API URL
    // ============================================================

    private static final String COURSES_URL =
            ApiConfig.BASE_URL + "/api/courses";


    // ============================================================
    // HTTP / JSON
    // ============================================================

    private final HttpClient httpClient;

    private final ObjectMapper mapper;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public CourseService() {

        httpClient =
                HttpClient.newHttpClient();

        mapper =
                JsonMapperFactory.get();
    }


    // ============================================================
    // GET ALL COURSES
    // ============================================================

    public List<Course> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        COURSES_URL
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
                    "Could not load courses.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        JavaType listType =
                mapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                Course.class
                        );

        return mapper.readValue(
                response.body(),
                listType
        );
    }


    // ============================================================
    // GET COURSE BY ID
    // ============================================================

    public Course getById(
            int courseId
    )
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        COURSES_URL
                                                + "/"
                                                + courseId
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
                    "Could not load course.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Course.class
        );
    }


    // ============================================================
    // CREATE COURSE
    // ============================================================

    public Course create(
            Course course
    )
            throws IOException, InterruptedException {

        if (course == null) {

            throw new IllegalArgumentException(
                    "Course is required."
            );
        }

        String json =
                mapper.writeValueAsString(
                        course
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        COURSES_URL
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

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to create course.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Course.class
        );
    }


    // ============================================================
    // UPDATE COURSE
    // ============================================================

    public Course update(
            Course course
    )
            throws IOException, InterruptedException {

        if (course == null) {

            throw new IllegalArgumentException(
                    "Course is required."
            );
        }

        if (course.getCourseId() <= 0) {

            throw new IllegalArgumentException(
                    "A valid course ID is required."
            );
        }

        String json =
                mapper.writeValueAsString(
                        course
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        COURSES_URL
                                                + "/"
                                                + course.getCourseId()
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

            throw new IOException(
                    "Course with ID "
                            + course.getCourseId()
                            + " was not found."
            );
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to update course.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Course.class
        );
    }


    // ============================================================
    // DELETE COURSE
    // ============================================================

    public boolean delete(
            int courseId
    )
            throws IOException, InterruptedException {

        if (courseId <= 0) {

            throw new IllegalArgumentException(
                    "A valid course ID is required."
            );
        }

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        COURSES_URL
                                                + "/"
                                                + courseId
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
                    "Failed to delete course.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return true;
    }
}