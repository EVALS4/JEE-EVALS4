package edu.intech.popback.services;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.intech.popback.dao.DaoFactory;
import edu.intech.popback.exceptions.DaoException;
import edu.intech.popback.models.Figure;
import edu.intech.popback.models.Universe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/universe")
public class UniverseService {
	
	private final static Logger logger = LogManager.getLogger(FigureService.class);

	/**
	 * Renvoie une liste de touts les univers
	 * 
	 * @return Un json des univers
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUniverses(@Context HttpServletRequest req) throws DaoException {

		logger.debug("Universe - > getAllUniverses from : {}", req.getRemoteAddr());
		List<Universe> univers = DaoFactory.getInstance().getUniverseDao().getAllUniverses();

		final GenericEntity<List<Universe>> json = new GenericEntity<>(univers) {
		};

		return Response.ok().entity(json).build();
	}

	/**
	 * Renvoie un univers par son id
	 * 
	 * @param UniverseId L'id de l'univers.
	 * @return Un json de l'univers
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUniverseById(@Context HttpServletRequest req, @PathParam("id") int UniverseId) throws DaoException {

		logger.debug("Universe - > getUniverseById from : {}", req.getRemoteAddr());
		Universe universe = DaoFactory.getInstance().getUniverseDao().getUniverseById(UniverseId);
		final GenericEntity<Universe> json = new GenericEntity<>(universe) {
		};

		return Response.ok().entity(json).build();
	}

	/**
	 * Cr�� un univers
	 * 
	 * @param u L'univers a cr�er
	 * @return Le json de l'univers cr�e
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUniverse(@Context HttpServletRequest req, Universe u) {

		try {
			
			logger.debug("Universe - > createUniverse from : {}", req.getRemoteAddr());
			DaoFactory.getInstance().getUniverseDao().createUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			logger.error(Level.ERROR, e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Couldn't create universe. Check params.\n\n" + e).build();
		}
	}

	/**
	 * Mets � jour un univers
	 * 
	 * @param u L'univers � mettre � jour
	 * @return Le json de l'univers mis � jour
	 */
	@PATCH
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUniverse(@Context HttpServletRequest req, Universe u) {

		try {
			logger.debug("Universe - > updateUniverse from : {}", req.getRemoteAddr());
			DaoFactory.getInstance().getUniverseDao().updateUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Couldn't update universe. Check params.\n\n" + e).build();
		}
	}

	/**
	 * Supprime un univers
	 * 
	 * @param UniverseId L'id de l'univers � supprimer
	 * @return Le json de l'univers supprim�
	 */
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUniverse(@Context HttpServletRequest req, @PathParam("id") int UniverseId) {

		try {
			logger.debug("Universe - > deleteUniverse from : {}", req.getRemoteAddr());
			Universe u = DaoFactory.getInstance().getUniverseDao().getUniverseById(UniverseId);
			DaoFactory.getInstance().getUniverseDao().deleteUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			logger.error(Level.ERROR, e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Couldn't delete universe. Check params.\n\n" + e).build();
		}

	}
	
	/*
	 * Ces m�thodes permettent de r�gler un probl�me de cache que je rencontre quand j'utilise
	 * les m�thodes create, update & delete de FigureService de mon api. Apr�s la suppression les
	 * donn�es (mon tableau "figures" dans univers) ne se mets pas � jour. Le seul moyen que j'ai
	 * trouv� est de cr�er une m�thode dans mon mod�le universe qui ajoute ou supprime une figurine
	 * (qui va aussi mettre � jour la table figurine). C'est pas tr�s propre mais �a fonctionne.
	 * Encore une fois comme dis c'est li� au cache de l'api mes m�thodes fonctionnent correctement,
	 * je dois red�marrer l'api pour que mon tableau de figurines dans ma classe univers se mette � jour.
	 * Et c'est ce tableau l� qui me permete de g�rer l'affichage dans mon front
	 */

	/**
	 * Ajoute la figurine � l'univers
	 * 
	 * @param f la figure � ajouter
	 * @return Le json de l'univers avec la figure ajout�e
	 */
	@POST
	@Path("/addFigure")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFigureToUniverse(@Context HttpServletRequest req, Figure f) {

		try {
			logger.debug("Universe - > addFigureToUniverse from : {}", req.getRemoteAddr());
			Universe u = DaoFactory.getInstance().getUniverseDao().getUniverseById(f.getIdUniverse());
			u.addFigure(f);
			DaoFactory.getInstance().getUniverseDao().updateUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			logger.error(Level.ERROR, e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Couldn't add figure. Check params.\n\n" + e).build();
		}

	}

	/**
	 * La fonction supprime une figurine d'un univers
	 * 
	 * @param UniverseId l'id de la figurine
	 * @return La r�ponse est renvoy�e.
	 */
	@DELETE
	@Path("/removeFigure/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFigureToUniverse(@Context HttpServletRequest req, @PathParam("id") int UniverseId) {

		try {
			logger.debug("Universe - > deleteFigureToUniverse from : {}", req.getRemoteAddr());
			Figure f = DaoFactory.getInstance().getFigureDao().getFigureById(UniverseId);
			Universe u = DaoFactory.getInstance().getUniverseDao().getUniverseById(f.getIdUniverse());
			u.removeFigure(f);
			DaoFactory.getInstance().getUniverseDao().updateUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			logger.error(Level.ERROR, e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Couldn't delete figure. Check params.\n\n" + e).build();
		}

	}
}
