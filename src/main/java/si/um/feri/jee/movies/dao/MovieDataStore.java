package si.um.feri.jee.movies.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import si.um.feri.jee.movies.vao.Movie;

import java.util.List;
import java.util.Optional;

@Stateless
public class MovieDataStore {

    @PersistenceContext
    private EntityManager em;

    public Movie create(Movie movie) {
        em.persist(movie);
        return movie;
    }

    public List<Movie> findAll() {
        return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(em.find(Movie.class, id));
    }

    public void update(Movie movie) {
        Movie existing = em.find(Movie.class, movie.getId());
        if (existing != null) {
            existing.setTitle(movie.getTitle());
            existing.setGenre(movie.getGenre());
            existing.setReleaseYear(movie.getReleaseYear());
            existing.setDescription(movie.getDescription());
            existing.setDirector(movie.getDirector());
        }
    }

    public void delete(Long id) {
        Movie movie = em.find(Movie.class, id);
        if (movie != null) {
            em.remove(movie);
        }
    }

    public List<Movie> findWithoutDirector() {
        return em.createQuery(
                "SELECT m FROM Movie m WHERE m.director IS NULL OR TRIM(m.director) = ''",
                Movie.class
        ).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(m) FROM Movie m", Long.class).getSingleResult();
    }

    public List<Movie> findNewest(int limit) {
        return em.createQuery("SELECT m FROM Movie m ORDER BY m.releaseYear DESC", Movie.class)
                .setMaxResults(limit)
                .getResultList();
    }

}
