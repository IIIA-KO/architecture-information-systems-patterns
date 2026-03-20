package si.um.feri.jee.movies.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.extern.java.Log;
import si.um.feri.jee.movies.dao.MovieDataStore;
import si.um.feri.jee.movies.vao.Movie;

import java.util.List;

@Log
@Stateless
public class MovieServiceBean implements MovieServiceLocal, MovieServiceRemote {

    @EJB
    private MovieDataStore dataStore;

    @Override
    public Movie create(Movie movie) {
        log.info("Creating movie: " + movie.getTitle());
        return dataStore.create(movie);
    }

    @Override
    public List<Movie> findAll() {
        return dataStore.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return dataStore.findById(id).orElse(null);
    }

    @Override
    public void update(Movie movie) {
        log.info("Updating movie: " + movie.getTitle());
        dataStore.update(movie);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting movie with id: " + id);
        dataStore.delete(id);
    }

    @Override
    public List<Movie> findWithoutDirector() {
        return dataStore.findWithoutDirector();
    }

    // --- Remote interface ---

    @Override
    public long getMovieCount() {
        return dataStore.count();
    }

    @Override
    public List<Movie> findNewest(int limit) {
        return dataStore.findNewest(limit);
    }

}
