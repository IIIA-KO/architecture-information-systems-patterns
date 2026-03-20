package si.um.feri.jee.movies.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import si.um.feri.jee.movies.vao.Movie;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RestMovieClient {

    private static final String BASE_URL = "http://wildfly:8080/movies/api/movies";
    private static final HttpClient http = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        // 1. GET /api/movies — list all movies
        System.out.println("=== GET all movies ===");
        HttpRequest getAll = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> getAllResponse = http.send(getAll, HttpResponse.BodyHandlers.ofString());
        List<Movie> movies = mapper.readValue(getAllResponse.body(), new TypeReference<>() {});
        movies.forEach(m -> System.out.println("  " + m.getId() + ": " + m.getTitle() + " (" + m.getReleaseYear() + ")"));

        // 2. POST /api/movies — create a new movie
        System.out.println("\n=== POST create movie ===");
        Movie newMovie = new Movie("Inception", "Sci-Fi", 2010, "A mind-bending thriller", "Christopher Nolan");
        String newMovieJson = mapper.writeValueAsString(newMovie);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(newMovieJson))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> postResponse = http.send(postRequest, HttpResponse.BodyHandlers.ofString());
        Movie created = mapper.readValue(postResponse.body(), Movie.class);
        System.out.println("  Created movie with ID: " + created.getId() + ", title: " + created.getTitle());

        // 3. GET /api/movies/{id} — fetch the newly created movie
        System.out.println("\n=== GET movie by ID ===");
        HttpRequest getById = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + created.getId()))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> getByIdResponse = http.send(getById, HttpResponse.BodyHandlers.ofString());
        Movie fetched = mapper.readValue(getByIdResponse.body(), Movie.class);
        System.out.println("  Fetched: " + fetched.getId() + ": " + fetched.getTitle() + " - " + fetched.getDescription());

        // 4. PUT /api/movies/{id} — update the movie title
        System.out.println("\n=== PUT update movie ===");
        fetched.setTitle("Inception (Director's Cut)");
        String updatedJson = mapper.writeValueAsString(fetched);
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + fetched.getId()))
                .PUT(HttpRequest.BodyPublishers.ofString(updatedJson))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> putResponse = http.send(putRequest, HttpResponse.BodyHandlers.ofString());
        Movie updated = mapper.readValue(putResponse.body(), Movie.class);
        System.out.println("  Updated title: " + updated.getTitle());

        System.out.println("\nDone.");
    }
}
