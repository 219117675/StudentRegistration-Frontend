package za.ac.cput.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.ac.cput.domain.Class;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ClassService {

    private static final String CLASSES_URL =
            ApiConfig.BASE_URL + "/api/classes";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ClassService() {

        httpClient =
                HttpClient.newHttpClient();

        mapper =
                JsonMapperFactory.get();
    }


    // ============================================================
    // GET ALL CLASSES
    // ============================================================

    public List<Class> getAll()
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        CLASSES_URL
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
                    "Failed to load classes.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                mapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                Class.class
                        )
        );
    }


    // ============================================================
    // GET CLASS BY ID
    // ============================================================

    public Class getById(int classId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        CLASSES_URL
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

        if (response.statusCode() == 404) {

            return null;
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to load class.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Class.class
        );
    }


    // ============================================================
    // CREATE CLASS
    // ============================================================

    public Class create(Class courseClass)
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        courseClass
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        CLASSES_URL
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
                    "Failed to create class.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Class.class
        );
    }


    // ============================================================
    // UPDATE CLASS
    // ============================================================

    public Class update(Class courseClass)
            throws IOException, InterruptedException {

        String json =
                mapper.writeValueAsString(
                        courseClass
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        CLASSES_URL
                                                + "/"
                                                + courseClass
                                                .getClassId()
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

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to update class.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Class.class
        );
    }


    // ============================================================
    // DELETE CLASS
    // ============================================================

    public boolean delete(int classId)
            throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        CLASSES_URL
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

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "Failed to delete class.\n"
                            + "HTTP "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return true;
    }


    // ============================================================
    // GET AVAILABLE CLASSES FOR LECTURER
    // ============================================================

    public List<Class> getAvailableClassesForLecturer(
            int lecturerPersonId
    )
            throws IOException, InterruptedException {

        String url =
                CLASSES_URL
                        + "/available/lecturer/"
                        + lecturerPersonId;

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

            return null;
        }

        if (response.statusCode() != 200) {

            throw new IOException(
                    "Failed to load available classes.\n"
                            + "HTTP Status: "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                mapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                Class.class
                        )
        );
    }


    // ============================================================
    // GET ASSIGNED CLASSES FOR LECTURER
    // ============================================================

    public List<Class> getAssignedClassesForLecturer(
            int lecturerPersonId
    )
            throws IOException, InterruptedException {

        String url =
                CLASSES_URL
                        + "/assigned/lecturer/"
                        + lecturerPersonId;

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

            return null;
        }

        if (response.statusCode() != 200) {

            throw new IOException(
                    "Failed to load assigned classes.\n"
                            + "HTTP Status: "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                mapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                Class.class
                        )
        );
    }


    // ============================================================
    // ASSIGN LECTURER TO CLASS
    // ============================================================

    public Class assignLecturer(
            int classId,
            int lecturerPersonId
    )
            throws IOException, InterruptedException {

        String url =
                CLASSES_URL
                        + "/"
                        + classId
                        + "/lecturer/"
                        + lecturerPersonId;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .PUT(
                                HttpRequest.BodyPublishers.noBody()
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            throw new IOException(
                    "Failed to assign lecturer.\n"
                            + "HTTP Status: "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return mapper.readValue(
                response.body(),
                Class.class
        );
    }


    // ============================================================
    // UNASSIGN LECTURER
    // ============================================================

    public Class unassignLecturer(
            int classId
    )
            throws IOException, InterruptedException {

        String url =
                CLASSES_URL
                        + "/"
                        + classId
                        + "/unassign";


        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )
                        .PUT(
                                HttpRequest.BodyPublishers.noBody()
                        )
                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );


        if (response.statusCode() != 200) {

            throw new IOException(
                    "Failed to unassign lecturer.\n"
                            + "HTTP Status: "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }


        return mapper.readValue(
                response.body(),
                Class.class
        );
    }
}