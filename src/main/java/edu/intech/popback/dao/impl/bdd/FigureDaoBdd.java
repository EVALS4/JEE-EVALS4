package edu.intech.popback.dao.impl.bdd;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import edu.intech.popback.dao.interfaces.IFigureDao;
import edu.intech.popback.exceptions.DaoException;
import edu.intech.popback.models.Figure;

public class FigureDaoBdd implements IFigureDao {

	/**
	 * Cr��e une nouvelle figurine dans la base de donn�es
	 * 
	 * @param f la figurine � cr�er
	 * @return La figurine qui a �t� cr��e.
	 */
	@Override
	public Figure createFigure(Figure f) throws DaoException {
		try {
			DaoBddHelper.beginTransaction();
			DaoBddHelper.getEm().persist(f);
			DaoBddHelper.commitTransaction();
			return f;
		} catch (PersistenceException e) {
			DaoBddHelper.rollbackTransaction();
			throw new DaoException("Saving: " + f.getName() + " in DB went wrong", e);
		}
	}

	/**
	 * Obtenir toutes les figurines de la base de donn�es.
	 * 
	 * @return Une liste de toutes les figurines.
	 */
	@Override
	public List<Figure> getAllFigures() {
		TypedQuery<Figure> query = DaoBddHelper.getEm().createNamedQuery("figures.getAllFigures", Figure.class);
		return query.getResultList();
	}

	/**
	 * Renvoi une figurine par son id
	 * 
	 * @param FigureId l'id de la figurine � renvoyer
	 * @return La figurine correspondante � l'id.
	 */
	@Override
	public Figure getFigureById(int FigureId) {
		TypedQuery<Figure> query = DaoBddHelper.getEm().createNamedQuery("figures.getFigureById", Figure.class);
		query.setParameter("id", FigureId);
		return query.getSingleResult();
	}

	/**
	 * Met à jour une figurine dans la base de donn�es
	 * 
	 * @param f la figurine � mettre � jour
	 * @return Lea figurine mise � jour.
	 */
	@Override
	public Figure updateFigure(Figure f) throws DaoException {
		try {
			DaoBddHelper.beginTransaction();
			DaoBddHelper.getEm().merge(f);
			DaoBddHelper.commitTransaction();
			return f;
		} catch (PersistenceException e) {
			DaoBddHelper.rollbackTransaction();
			throw new DaoException("Updating: " + f.getName() + " in DB went wrong", e);
		}
	}

	/**
	 * Supprime une figurine de la base de donn�es
	 * 
	 * @param f la figurine � supprimer
	 */
	@Override
	public void deleteFigure(Figure f) throws DaoException {
		try {
			DaoBddHelper.beginTransaction();
			DaoBddHelper.getEm().remove(f);
			DaoBddHelper.commitTransaction();
		} catch (PersistenceException e) {
			DaoBddHelper.rollbackTransaction();
			throw new DaoException("Deleting: " + f.getName() + " in DB went wrong", e);
		}
	}
}
