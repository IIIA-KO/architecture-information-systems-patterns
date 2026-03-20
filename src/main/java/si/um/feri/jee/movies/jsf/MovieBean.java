package si.um.feri.jee.movies.jsf;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import si.um.feri.jee.movies.ejb.MovieServiceLocal;
import si.um.feri.jee.movies.vao.Movie;

import java.io.Serializable;
import java.util.List;

@Named("movieBean")
@SessionScoped
@Getter
@Setter
public class MovieBean implements Serializable {

    @EJB
    private MovieServiceLocal movieService;

    private Movie editedMovie = new Movie();
    private Movie selectedMovie;
    private boolean editing;

    // --- List ---

    public List<Movie> getAllMovies() {
        return movieService.findAll();
    }

    public List<Movie> getMoviesWithoutDirector() {
        return movieService.findWithoutDirector();
    }

    // --- Create ---

    public String prepareNewMovie() {
        editedMovie = new Movie();
        editing = false;
        return "movie-form?faces-redirect=true";
    }

    // --- Edit ---

    public String prepareEditMovie(Long id) {
        Movie m = movieService.findById(id);
        if (m != null) {
            editedMovie = new Movie(m.getTitle(), m.getGenre(), m.getReleaseYear(), m.getDescription(), m.getDirector());
            editedMovie.setId(m.getId());
            editing = true;
        }
        return "movie-form?faces-redirect=true";
    }

    // --- Save (create or update) ---

    public String saveMovie() {
        if (editing) {
            movieService.update(editedMovie);
        } else {
            movieService.create(editedMovie);
        }
        editedMovie = new Movie();
        editing = false;
        return "movies?faces-redirect=true";
    }

    // --- Delete ---

    public String deleteMovie(Long id) {
        movieService.delete(id);
        return "movies?faces-redirect=true";
    }

    // --- Details ---

    public String viewDetails(Long id) {
        selectedMovie = movieService.findById(id);
        return "movie-details?faces-redirect=true";
    }

}
