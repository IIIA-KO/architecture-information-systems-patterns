package si.um.feri.jee.movies.rest;

import jakarta.ws.rs.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import si.um.feri.jee.movies.ejb.MovieServiceLocal;
import si.um.feri.jee.movies.vao.Movie;

import java.util.List;

@Stateless
@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieController {

    @EJB
    private MovieServiceLocal movieService;

    @GET
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Movie movie = movieService.findById(id);
        if (movie == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(movie).build();
    }

    @POST
    public Response create(Movie movie) {
        Movie created = movieService.create(movie);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Movie movie) {
        movie.setId(id);
        movieService.update(movie);
        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        movieService.delete(id);
        return Response.noContent().build();
    }
}
