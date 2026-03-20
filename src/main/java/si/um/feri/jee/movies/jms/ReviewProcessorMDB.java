package si.um.feri.jee.movies.jms;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.java.Log;
import si.um.feri.jee.movies.vao.Movie;
import si.um.feri.jee.movies.vao.Review;

import java.util.List;
import java.util.logging.Level;

@Log
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/ReviewQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class ReviewProcessorMDB implements MessageListener {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            ReviewMessage msg = (ReviewMessage) objectMessage.getObject();

            log.info("Processing review for movie ID: " + msg.getMovieId() + " by " + msg.getAuthor());

            Review review = new Review();
            review.setMovieId(msg.getMovieId());
            review.setAuthor(msg.getAuthor());
            review.setRating(msg.getRating());
            review.setComment(msg.getComment());

            // Validate movie exists
            Movie movie = em.find(Movie.class, msg.getMovieId());
            if (movie == null) {
                review.setStatus("REJECTED");
                em.persist(review);
                log.warning("Review REJECTED: Movie with ID " + msg.getMovieId() + " does not exist");
                return;
            }

            // Validate rating range
            if (msg.getRating() < 1 || msg.getRating() > 5) {
                review.setStatus("REJECTED");
                em.persist(review);
                log.warning("Review REJECTED: Rating " + msg.getRating() + " is out of range (1-5)");
                return;
            }

            // Check for duplicate (same author + movieId)
            List<Review> duplicates = em.createQuery(
                            "SELECT r FROM Review r WHERE r.movieId = :movieId AND r.author = :author AND r.status = 'APPROVED'",
                            Review.class)
                    .setParameter("movieId", msg.getMovieId())
                    .setParameter("author", msg.getAuthor())
                    .getResultList();

            if (!duplicates.isEmpty()) {
                review.setStatus("REJECTED");
                em.persist(review);
                log.warning("Review REJECTED: Duplicate review by " + msg.getAuthor() + " for movie ID " + msg.getMovieId());
                return;
            }

            // All validations passed
            review.setStatus("APPROVED");
            em.persist(review);
            log.info("Review APPROVED for movie '" + movie.getTitle() + "' by " + msg.getAuthor() + " with rating " + msg.getRating());

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error processing review message", e);
        }
    }

}
