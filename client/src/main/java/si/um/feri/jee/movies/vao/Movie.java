package si.um.feri.jee.movies.vao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Movie implements Serializable {

    private Long id;
    private String title;
    private String genre;
    private int releaseYear;
    private String description;
    private String director;

    public Movie(String title, String genre, int releaseYear, String description, String director) {
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.description = description;
        this.director = director;
    }

}
