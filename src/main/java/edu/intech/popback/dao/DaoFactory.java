package edu.intech.popback.dao;

import edu.intech.popback.dao.impl.bdd.FigureDaoBdd;
import edu.intech.popback.dao.impl.bdd.UniverseDaoBdd;
import edu.intech.popback.dao.interfaces.IFigureDao;
import edu.intech.popback.dao.interfaces.IUniverseDao;

public class DaoFactory {

	private static DaoFactory instance;
	private IFigureDao figureDao;
	private IUniverseDao universeDao;
	
	private DaoFactory() {}
	
	/**
	 * Si l'instance est nulle, cr�er une nouvelle instance de DaoFactory
	 * 
	 * @return L'instance de la classe DaoFactory.
	 */
	public static DaoFactory getInstance() {
		if (instance == null)
			instance = new DaoFactory();
		return instance;
	}
	
	/**
	 * Si figureDao est nul, alors cr�er un nouveau FigureDaoBdd et l'attribuer au figureDao
	 * 
	 * @return L'objet FigureDaoBdd.
	 */
	public IFigureDao getFigureDao() {
		if(this.figureDao == null) this.figureDao = new FigureDaoBdd();
		return figureDao;
	}

	/**
	 * Cette fonction d�finit la variable figureDao sur le param�tre figureDao
	 * 
	 * @param figureDao L'objet IFigureDao � attribuer � la variable figureDao
	 */
	public void setFigureDao(IFigureDao figureDao) {
		this.figureDao = figureDao;
	}

	/**
	 * Si l'universDao est nul, alors cr�er un nouveau UniverseDaoBdd et l'attribuer au universeDao
	 * 
	 * @return L'objet UniverseDaoBdd.
	 */
	public IUniverseDao getUniverseDao() {
		if(this.universeDao == null) this.universeDao = new UniverseDaoBdd();
		return universeDao;
	}

	/**
	 * > Cette fonction d�finit la variable universeDao � la valeur du param�tre universeDao
	 * 
	 * @param universeDao La valeur � attribuer � universeDao
	 */
	public void setUniverseDao(IUniverseDao universeDao) {
		this.universeDao = universeDao;
	}
}
