package si.um.feri.jee.movies.jsf;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import si.um.feri.jee.movies.dao.ReviewDataStore;
import si.um.feri.jee.movies.jms.ReviewMessage;
import si.um.feri.jee.movies.jms.ReviewProducer;
import si.um.feri.jee.movies.vao.Review;

import java.io.Serializable;
import java.util.List;

@Named("reviewBean")
@SessionScoped
@Getter
@Setter
public class ReviewBean implements Serializable {

    @EJB
    private ReviewProducer reviewProducer;

    @EJB
    private ReviewDataStore reviewDataStore;

    private Long movieId;
    private String author;
    private int rating = 3;
    private String comment;

    public String submitReview() {
        ReviewMessage msg = new ReviewMessage(movieId, author, rating, comment);
        reviewProducer.sendReview(msg);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Review submitted for processing!", null));

        // Reset form
        movieId = null;
        author = null;
        rating = 3;
        comment = null;

        return "reviews?faces-redirect=true";
    }

    public List<Review> getAllReviews() {
        return reviewDataStore.findAll();
    }

    public List<Review> getReviewsForMovie(Long movieId) {
        return reviewDataStore.findByMovieId(movieId);
    }

}
