package edu.upc.eetac.dsa.csanchez.books.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.csanchez.books.api.model.Resenya;
import edu.upc.eetac.dsa.csanchez.books.api.model.ResenyaCollection;

@Path("/resenya")
public class ResenyaResource {
	
	@Context
	private SecurityContext security;

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String RESENYA_POR_ID = "select * from resenya where resenyaid = ?";
	
	
	 
	@GET
	@Path("/{resenyaid}")
	@Produces(MediaType.BOOKS_API_RESENYA)
	public Resenya getResenya(@PathParam("resenyaid") String resenyaid) {
		Resenya resenya = new Resenya ();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(RESENYA_POR_ID);
			stmt.setInt(1, Integer.valueOf(resenyaid));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				resenya.setResenyaid(rs.getInt("resenyaid"));
				resenya.setUsername(rs.getString("username"));
				resenya.setName(rs.getString("name"));
				resenya.setTexto(rs.getString("texto"));
				resenya.setLibroid(rs.getInt("libroid"));
				resenya.setLast_modified(rs.getTimestamp("last_modified")
						.getTime());
				resenya.setCreation_timestamp(rs
						.getTimestamp("creation_timestamp").getTime());
			}
			else {
				throw new NotFoundException("There's no resenya with resenyaid="
				+ resenyaid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return resenya;
	}
	
	//Get Reseñas
	
	private String GET_STINGS_QUERY = "select * from resenya";
	 
	@GET
	@Produces(MediaType.BOOKS_API_RESENYA_COLLECTION)
	public ResenyaCollection getResenyas() {
		ResenyaCollection resenya = new ResenyaCollection();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_STINGS_QUERY);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Resenya res = new Resenya();
				res.setResenyaid(rs.getInt("resenyaid"));
				res.setUsername(rs.getString("username"));
				res.setName(rs.getString("name"));
				res.setTexto(rs.getString("texto"));
				res.setLibroid(rs.getInt("libroid"));
				res.setLast_modified(rs.getTimestamp("last_modified")
						.getTime());
				res.setCreation_timestamp(rs
						.getTimestamp("creation_timestamp").getTime());
				resenya.addResenya(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return resenya;
	}
	
// CREAR RESEÑA
	
	
	private String INSERT_STING_QUERY = "insert into resenya (username, name, texto, libroid) values (?, ?, ?, ?)";
	 
	@POST
	@Consumes(MediaType.BOOKS_API_RESENYA)
	@Produces(MediaType.BOOKS_API_RESENYA)
	public Resenya createResenya(Resenya resenya) {
		validateResenya(resenya);
		//validateUserPermision(username);
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_STING_QUERY,
					Statement.RETURN_GENERATED_KEYS);
	 
			stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(2, resenya.getName());
			stmt.setString(3, resenya.getTexto());
			stmt.setInt(4, resenya.getLibroid());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int resenyaid = rs.getInt(1);
	 
				resenya = getResenya(Integer.toString(resenyaid));
			} 
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return resenya;
	}
	
	private void validateResenya(Resenya resenya) {
		if (resenya.getUsername() == null)
			throw new BadRequestException("username can't be null.");
		if (resenya.getName() == null)
			throw new BadRequestException("name can't be null.");
		if (resenya.getUsername().length() > 20)
			throw new BadRequestException("username can't be greater than 20 characters.");
		if (resenya.getName().length() > 20)
			throw new BadRequestException("Name can't be greater than 20 characters.");
		if (resenya.getTexto() == null)
			throw new BadRequestException("texto can't be null.");
		if (resenya.getTexto().length() > 500);
		throw new BadRequestException("texto can't be greater than 500 characters.");
	}
	
	/*private void validateUserPermision(String username) {
		if (!security.getUserPrincipal().getName().equals(username))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}*/
	

	//BORRAR RESEÑA
	
	private String DELETE_STING_QUERY = "delete from resenya where resenyaid=?";
	 
	@DELETE
	@Path("/{resenyaid}")
	public void deleteResenya(@PathParam("resenyaid") String resenyaid) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_STING_QUERY);
			stmt.setInt(1, Integer.valueOf(resenyaid));
	 
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no resenya with resenyaid ="
						+ resenyaid);
				;// Deleting inexistent sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	//ACTUALIZAR UNA RESEÑA
	
	private String UPDATE_STING_QUERY = "update resenya set name=ifnull(?, name), "
			+ "texto=ifnull(?, texto) where resenyaid=?";
	 
	@PUT
	@Path("/{resenyaid}")
	@Consumes(MediaType.BOOKS_API_RESENYA)
	@Produces(MediaType.BOOKS_API_RESENYA)
	public Resenya updateResenya(@PathParam("resenyaid") String resenyaid, Resenya resenya) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_STING_QUERY);
			stmt.setString(1, resenya.getName());
			stmt.setString(2, resenya.getTexto());
			stmt.setInt(3, Integer.valueOf(resenyaid));
	 
			int rows = stmt.executeUpdate();
			if (rows == 1)
				resenya = getResenya(resenyaid);
			else {
				throw new NotFoundException("There's no resenya with resenyaid="
						+ resenyaid);
			}
	 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return resenya;
		}
	
	
}
