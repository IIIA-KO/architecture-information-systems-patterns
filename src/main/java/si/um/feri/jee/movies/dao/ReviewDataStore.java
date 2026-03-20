package si.um.feri.jee.movies.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import si.um.feri.jee.movies.vao.Review;

import java.util.List;

@Stateless
public class ReviewDataStore {

    @PersistenceContext
    private EntityManager em;

    public List<Review> findByMovieId(Long movieId) {
        return em.createQuery("SELECT r FROM Review r WHERE r.movieId = :movieId ORDER BY r.createdAt DESC", Review.class)
                .setParameter("movieId", movieId)
                .getResultList();
    }

    public List<Review> findAll() {
        return em.createQuery("SELECT r FROM Review r ORDER BY r.createdAt DESC", Review.class)
                .getResultList();
    }

}
