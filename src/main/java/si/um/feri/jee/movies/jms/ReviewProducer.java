package si.um.feri.jee.movies.jms;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import lombok.extern.java.Log;

@Log
@Stateless
public class ReviewProducer {

    @Resource(lookup = "java:/jms/queue/ReviewQueue")
    private Queue queue;

    @Inject
    private JMSContext jmsContext;

    public void sendReview(ReviewMessage msg) {
        jmsContext.createProducer().send(queue, msg);
        log.info("Review submitted to queue for movie ID: " + msg.getMovieId());
    }

}
