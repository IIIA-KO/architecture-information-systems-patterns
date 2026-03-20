package si.um.feri.jee.movies.dao;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import si.um.feri.jee.movies.vao.Movie;

@Singleton
@Startup
public class DataSeeder {

    @EJB
    private MovieDataStore movieDataStore;

    @PostConstruct
    public void seed() {
        if (movieDataStore.count() == 0) {
            movieDataStore.create(new Movie("Star Wars: Episode IV - A New Hope",
                    "Sci-Fi", 1977,
                    "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station.",
                    "George Lucas")
            );
            movieDataStore.create(new Movie("It",
                    "Horror", 2017,
                    "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster.",
                    "Andy Muschietti")
            );
            movieDataStore.create(new Movie("Pulp Fiction",
                    "Crime", 1994,
                    "The lives of two mob hitmen, a boxer, a gangster and his wife intertwine in four tales of violence and redemption.",
                    "Quentin Tarantino")
            );
            movieDataStore.create(new Movie("Untitled Mystery Project",
                    "Mystery", 2025,
                    "A mysterious film currently in development with no announced director.",
                    null)
            );
        }
    }

}
