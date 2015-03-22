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

import edu.upc.eetac.dsa.csanchez.books.api.model.Autores;


@Path("/autor")
public class AutoresResource {
	
	
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String GET_AUTOR_BY_ID_QUERY = "select * from autor where name like ?";
	 
	@GET
	@Path("/{name}")
	@Produces(MediaType.BOOKS_API_AUTOR)
	public Autores getAutor(@PathParam("name") String name) {
		Autores autor = new Autores();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_AUTOR_BY_ID_QUERY);
			stmt.setString(1,(name));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				//autor.setAutorid(rs.getInt("autorid"));
				autor.setName(rs.getString("name"));
			}
				else {
					throw new NotFoundException("There's no autor with name ="
					+ name);
			
			}
		}  catch (SQLException e) {
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
	 
		return autor;
	}

	// crear ficha autor
	


	private String INSERT_AUTOR_QUERY = "insert into autor (name) values (?)";
	 
	@POST
	@Consumes(MediaType.BOOKS_API_AUTOR)
	@Produces(MediaType.BOOKS_API_AUTOR)
	public Autores createAutor(Autores autor) {
		validateAutor(autor);
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_AUTOR_QUERY,
					Statement.RETURN_GENERATED_KEYS);
	 
			stmt.setString(1, autor.getName());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int autorid = rs.getInt(1);
	 
				autor = getAutor(Integer.toString(autorid));
			} else {
				// Something has failed...
			}
			
		}  catch (SQLException e) {
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
	 
		return autor;
	}
	
	private void validateAutor(Autores autor) {
		if (autor.getName() == null)
			throw new BadRequestException("Name can't be null.");
		if (autor.getName().length() > 20)
			throw new BadRequestException("Name can't be greater than 20 characters.");
		
	}
	
	//actualizar la ficha de un autor
	
	private String UPDATE_AUTOR_QUERY = "update autor set name = ifnull(?, name) where name like ?";
	 
	@PUT
	@Path("/{name}")
	@Consumes(MediaType.BOOKS_API_AUTOR)
	@Produces(MediaType.BOOKS_API_AUTOR)
	public Autores updateAutor(@PathParam("name") String name, Autores autor) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_AUTOR_QUERY);
			//stmt.setString(1, autor.getName());
			//stmt.setString(2, Integer.valueOf(autorid));
			stmt.setString(1, autor.getName());
			stmt.setString(2, name);
			
			int rows = stmt.executeUpdate();
			if (rows == 1)
				autor = getAutor(name);
			else {
				throw new NotFoundException("There's no autor with name ="
						+ name);
			}
	 
		}  catch (SQLException e) {
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
	 
		return autor;
		}
	
	//borrar la ficha de un autor

	private String DELETE_AUTOR_QUERY = "delete from autor where name like ?";
	 
	@DELETE
	@Path("/{name}")
	public void deleteSting(@PathParam("name") String name) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_AUTOR_QUERY);
			stmt.setString(1,(name));
	 
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no autor with autorid="
						+ name);
				;// Deleting inexistent sting
		}  catch (SQLException e) {
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
}
