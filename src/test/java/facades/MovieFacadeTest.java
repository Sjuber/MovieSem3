package facades;

import dtos.MovieDTO;
import utils.EMF_Creator;
import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieFacadeTest {
    private static  EntityManagerFactory emf;
    private static  MovieFacade facade;
    
    private Movie m1;
    private Movie m2;
    private Movie m3;
    private Movie m4;
    private MovieDTO mD1;
    
    public MovieFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MovieFacade.getMovieFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }
    
    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        m1 = new Movie(2001, "Harry Potter and the Philosopher's Stone", new String[]{"Daniel Radcliffe", "Emma Watson", "Alan Rickman", "Rupert Grint"});
        m2 = new Movie(2002, "Harry Potter and the Chamber of Secrets", new String[]{"Daniel Radcliffe", "Emma Watson", "Alan Rickman", "Rupert Grint"});
        m3 = new Movie(2019, "Once Upon a Time... in Hollywood", new String[]{"Leonardo DiCaprio", "Brad Pitt", "Margot Robbie"});
        m4 = new Movie(2012, "2012", new String[]{"John Gat", "Jimmy Neutron", "Marylin Manson"});
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Movie").executeUpdate();
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
            em.persist(m4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testMovieCount() {
        assertEquals(4,facade.getMovieCount(),"Expects three movies in the database");
    }
    
    @Test
    public void testGetAllMovies(){
        MovieFacade MF = MovieFacade.getMovieFacade(emf);
        List<MovieDTO> ML = MF.getAllMovies();
        assertTrue(ML != null);
    }

    @Test
    public void testGetMovieById(){
        Integer MDvo = 4;
     MovieFacade MF = MovieFacade.getMovieFacade(emf);
     MovieDTO Movido = MF.getMovieById(4);
     Integer MovD = Movido.getId();
        assertEquals(MDvo,MovD);
    }
    
    @Test
    public void testMovieHasActors(){
       mD1 = new MovieDTO(m1);
       MovieFacade MF = MovieFacade.getMovieFacade(emf);
       String[] testActs = MF.getAllActorsList(mD1);
        assertTrue(testActs != null);
    }
    
    @Test
    public void getMoviesByTitle(){
       String MDTitle = "2012"; // First i set the title i wish to find
       MovieFacade MF = MovieFacade.getMovieFacade(emf);// Then i get a movie facade
       List<MovieDTO> Movido = MF.getMoviesByTitle(MDTitle); //Then we get a list of the movie with the determined title
       MovieDTO TheMovie = Movido.get(0);// then here i set a MovieDTO to be the MovieDTO at position 0, which is the only one
       String MovieTits = TheMovie.getTitle();// Then, we set the title of the movie we get to a String variable
        assertEquals(MDTitle, MovieTits);// At last, i compare the title of the movie aquired, it works for now, grand all grand.
    }



}
