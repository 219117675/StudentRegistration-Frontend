package za.ac.cput.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public LoginService() {

        client = HttpClient.newHttpClient();

        objectMapper = new ObjectMapper();
    }

    public LoginResponse login(
            String email,
            String password) throws Exception {

        LoginRequest requestBody =
                new LoginRequest(
                        email,
                        password
                );

        String json =
                objectMapper.writeValueAsString(
                        requestBody
                );

        /*
         * IMPORTANT:
         *
         * Backend endpoint is:
         *
         * /api/auth/login
         *
         * NOT:
         *
         * /auth/login
         */
        String loginUrl =
                ApiConfig.BASE_URL
                        + "/api/auth/login";

        System.out.println(
                "LOGIN REQUEST URL: "
                        + loginUrl
        );

        System.out.println(
                "LOGIN EMAIL: "
                        + email
        );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(loginUrl)
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
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        System.out.println(
                "LOGIN HTTP STATUS: "
                        + response.statusCode()
        );

        System.out.println(
                "LOGIN RESPONSE: "
                        + response.body()
        );

        if (response.body() == null ||
                response.body().isBlank()) {

            LoginResponse result =
                    new LoginResponse();

            result.setSuccess(false);

            result.setMessage(
                    "No response received from server."
            );

            return result;
        }

        /*
         * The backend returns 401 for invalid
         * credentials. We still deserialize the
         * response because it contains our normal
         * LoginResponse structure.
         */
        return objectMapper.readValue(
                response.body(),
                LoginResponse.class
        );
    }


    private static class LoginRequest {

        private final String email;
        private final String password;

        public LoginRequest(
                String email,
                String password) {

            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}