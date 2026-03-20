package si.um.feri.jee.movies.ejb;

import jakarta.ejb.Local;
import si.um.feri.jee.movies.vao.Movie;

import java.util.List;

@Local
public interface MovieServiceLocal {

    Movie create(Movie movie);

    List<Movie> findAll();

    Movie findById(Long id);

    void update(Movie movie);

    void delete(Long id);

    List<Movie> findWithoutDirector();

}
