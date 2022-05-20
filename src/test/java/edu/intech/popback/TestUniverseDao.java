package edu.intech.popback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import edu.intech.popback.dao.DaoFactory;
import edu.intech.popback.dao.interfaces.IUniverseDao;
import edu.intech.popback.exceptions.DaoException;
import edu.intech.popback.models.Universe;
import edu.intech.popback.services.FigureService;

@TestMethodOrder(OrderAnnotation.class) // Permet d'ordonner les tests
public class TestUniverseDao {

	private static Universe universe = null;
    private static IUniverseDao universeDao;
    
    private final static Logger logger = LogManager.getLogger(FigureService.class);

    @BeforeEach
    void setUp() throws Exception {
        universeDao = DaoFactory.getInstance().getUniverseDao(); // On cr�e une nouvelle instance de universeDao
    }

    @AfterEach
    void tearDown() throws Exception {
        universeDao = null;
    }
    
    @Test
    @Order(1)
    void createUniverse() throws DaoException {
        logger.debug("Test - > createUniverse - D�but");
        universe = new Universe("Test", "https://google.com"); //On stocke en local l'univers cr�e pour le r�utiliser par la suite
        assertEquals(universe.getId(), 0);
        universeDao.createUniverse(universe); //On cr�e la universe
        assertTrue(universe.getId() >= 0); //On v�rifie que l'ID de l'univers est bien diff�rent de 0
        logger.debug("Test - > createUniverse - Fin");
    }
    
    @Test
    @Order(2)
    void getAllUniverses() throws DaoException { //On test la r�cup�ration des univers
        logger.debug("Test - > getAllUniverses - D�but");
        List<Universe> figurineList = universeDao.getAllUniverses(); //On get la liste compl�te
        assertTrue(figurineList.size() >= 1);
        logger.debug("Test - > getAllUniverses - Fin");
    }

    @Test
    @Order(3)
    public void getUniverseById() throws DaoException { //On test la r�cup�ration d'un univers
        logger.debug("Test - > getUniverseById - D�but");
        Universe f = universeDao.getUniverseById(universe.getId());
        assertEquals("Test", f.getName()); //On compare les donn�es de l'univers avec celles utilis�s au d�part
        assertEquals("Test", f.getImageURL());
        logger.debug("Test - > getUniverseById - Fin");
    }

    @Test
    @Order(4)
    public void deleteUniverse() throws DaoException { //On test la suppression d'un univers
        logger.debug("Test - > deleteUniverse - D�but");
        universeDao.deleteUniverse(universe);
        assertThrows(NoResultException.class, () -> {
            universeDao.getUniverseById(universe.getId());
        });
        logger.debug("Test - > deleteUniverse - Fin");
    }
}
