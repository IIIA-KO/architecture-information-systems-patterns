package si.um.feri.jee.movies.ejb;

import jakarta.ejb.Remote;
import si.um.feri.jee.movies.vao.Movie;

import java.util.List;

@Remote
public interface MovieServiceRemote {

    List<Movie> findAll();

    List<Movie> findWithoutDirector();

    long getMovieCount();

    List<Movie> findNewest(int limit);

}
