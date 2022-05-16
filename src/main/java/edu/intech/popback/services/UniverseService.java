package edu.intech.popback.services;

import java.util.List;

import edu.intech.popback.dao.DaoFactory;
import edu.intech.popback.exceptions.DaoException;
import edu.intech.popback.models.Figure;
import edu.intech.popback.models.Universe;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/universe")
public class UniverseService {

	/**
	 * Renvoie une liste de touts les univers
	 * 
	 * @return Un json des univers
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUniverses() throws DaoException {

		List<Universe> universe = DaoFactory.getInstance().getUniverseDao().getAllUniverses();
		
		for(Universe u : universe) {
            List<Figure> list = DaoFactory.getInstance().getFigureDao().getFiguresByIdUniverse(u.getId());
            u.setFigures(list);
        };
        
		final GenericEntity<List<Universe>> json = new GenericEntity<>(universe) {
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
	public Response getUniverseById(@PathParam("id") int UniverseId) throws DaoException {

		Universe universe = DaoFactory.getInstance().getUniverseDao().getUniverseById(UniverseId);
		final GenericEntity<Universe> json = new GenericEntity<>(universe) {
		};

		return Response.ok().entity(json).build();
	}

	/**
	 * Cr�e un univers
	 * 
	 * @param u L'univers a cr�er 
	 * @return Le json de l'univers cr�e
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUniverse(Universe u) {

		try {
			DaoFactory.getInstance().getUniverseDao().createUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Couldn't create universe. Check params.\n\n" + e)
					.build();
		}
	}
	
	/**
	 * Met � jour un univers
	 * 
	 * @param u L'univers � mettre a jour
	 * @return Le json de l'univers mis a jour
	 */
	//ca reset le tableau de figures
	@PATCH
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUniverse(Universe u) {

		try {
			DaoFactory.getInstance().getUniverseDao().updateUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Couldn't update universe. Check params.\n\n" + e)
					.build();
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
	public Response deleteUniverse(@PathParam("id") int UniverseId) {

		try {
			Universe u = DaoFactory.getInstance().getUniverseDao().getUniverseById(UniverseId);
			DaoFactory.getInstance().getUniverseDao().deleteUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Couldn't delete universe. Check params.\n\n" + e)
					.build();
		}

	}
	
	/**
	 * Ajoute la figurine � l'univers
	 * 
	 * @param UniverseId l'id de l'univers sur lequel ajouter la figure
	 * @param f la figure � ajouter
	 * @return Le json de l'univers avec la figure ajout�e
	 */
	@POST
	@Path("/{id}/addFigure")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFigureToUniverse(@PathParam("id") int UniverseId, Figure f) {

		try {
			Universe u = DaoFactory.getInstance().getUniverseDao().getUniverseById(UniverseId);
			u.addFigure(f);
			DaoFactory.getInstance().getUniverseDao().updateUniverse(u);
			return Response.ok().entity(u).build();

		} catch (DaoException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Couldn't delete universe. Check params.\n\n" + e)
					.build();
		}

	}
}
