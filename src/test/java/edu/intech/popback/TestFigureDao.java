package edu.intech.popback;

import org.junit.jupiter.api.TestMethodOrder;

import edu.intech.popback.dao.DaoFactory;
import edu.intech.popback.dao.interfaces.IFigureDao;
import edu.intech.popback.exceptions.DaoException;
import edu.intech.popback.models.Figure;
import edu.intech.popback.services.FigureService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@TestMethodOrder(OrderAnnotation.class) // Permet d'ordonner les tests
class TestFigurinesDao {

	private static Figure figure = null;
    private static IFigureDao figureDao;
    
    private final static Logger logger = LogManager.getLogger(FigureService.class);

    /**
     * Cette fonction est utilis�e pour cr�er une nouvelle instance de figureDao avant chaque test
    */
    @BeforeEach
    void setUp() throws Exception {
        figureDao = DaoFactory.getInstance().getFigureDao(); 
    }

   /**
    * Cette fonction permet d'effacer la m�moire de la figure cr��e apr�s chaque test
    */
    @AfterEach
    void tearDown() throws Exception {
        figureDao = null;
    }
    
    /**
     * La fonction de test cr�e une figure, puis v�rifie que l'ID de la figure n'est pas �gal � 0
     */
    @Test
    @Order(1)
    void createFigure() throws DaoException {
        logger.debug("Test - > createFigure");
        // Cr�ation d'une nouvelle figurine avec le nom "Test", l'URL de l'image "https://google.com"
        // et l'id 1 (en auto increment l'id sera 0)
        figure = new Figure("Test", "https://google.com", 1); 
        assertEquals(figure.getId(), 0);
       // Cr�er une nouvelle figure � partir de la figure cr��e pr�c�demment pour v�rifier que l'id change
        figureDao.createFigure(figure); 
        assertTrue(figure.getId() >= 0); 
        logger.debug("Test - > createFigure - Fin");
    }
    
    /**
     * Cette fonction teste la fonction getAllFigures() de la classe FigureDao
     * Elle v�rifie que la liste de figures renvoy�e par la fonction est bien non vide
     */
    @Test
    @Order(2)
    void getAllFigures() throws DaoException { 
        logger.debug("Test - > getAllFigures - D�but");
        List<Figure> figurineList = figureDao.getAllFigures(); //On get la liste compl�te
        assertTrue(figurineList.size() >= 1);
        logger.debug("Test - > getFigures - Fin");
    }

    /**
     * Cette fonction teste la fonction getFigureById() de la classe FigureDao
     */
    @Test
    @Order(3)
    public void getFigureById() throws DaoException { 
        logger.debug("Test - > getFigureById - D�but");
        Figure f = figureDao.getFigureById(figure.getId());
        assertEquals("Test", f.getName()); // Compare les donn�es de la figure cr��e avec celles de la figure r�cup�r�e
        assertEquals("Test", f.getImageURL());
        logger.debug("Test - > getFigureById - Fin");
    }

    /**
     * La fonction deleteFigure() teste la suppression d'une figure
     */
    @Test
    @Order(4)
    public void deleteFigure() throws DaoException { 
        logger.debug("Test - > deleteFigure - D�but");
        figureDao.deleteFigure(figure);
       // V�rifier que la figure a �t� supprim�e en essayant de la r�cup�rer par son ID
        assertThrows(NoResultException.class, () -> {
            figureDao.getFigureById(figure.getId());
        });
        logger.debug("Test - > deleteFigure - Fin");
    }
    
}