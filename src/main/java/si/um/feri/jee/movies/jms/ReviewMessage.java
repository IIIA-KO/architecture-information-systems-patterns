package si.um.feri.jee.movies.jms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMessage implements Serializable {

    private Long movieId;
    private String author;
    private int rating;
    private String comment;

}
