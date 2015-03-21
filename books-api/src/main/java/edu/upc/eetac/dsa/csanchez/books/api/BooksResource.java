package edu.upc.eetac.dsa.csanchez.books.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.csanchez.books.api.model.Books;
import edu.upc.eetac.dsa.csanchez.books.api.model.BooksCollection;

@Path("/books")
public class BooksResource {

		private DataSource ds = DataSourceSPA.getInstance().getDataSource();
		private String GET_BOOKS_QUERY = "select * from libros order by fecha_ed desc";
		
		
//		
		
		@GET
		@Produces(MediaType.BOOKS_API_BOOKS_COLLECTION)
		public BooksCollection getBooks() {
			BooksCollection books = new BooksCollection();
		 
		
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(GET_BOOKS_QUERY);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Books book = new Books();
					book.setLibroid(rs.getInt("libroid"));
					book.setTitulo(rs.getString("Titulo"));
					book.setLengua(rs.getString("Lengua"));
					book.setEdicion(rs.getString("Edicion"));
					book.setFecha_ed(rs.getString("Fecha_ed"));
					book.setFecha_imp(rs.getString("Fecha_imp"));
					book.setEditorial(rs.getString("Editorial"));

					books.addBook(book);
					
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
		 
			return books;
		}
		
	//Libro por id
		
		private String GET_BOOK_BY_ID_QUERY = "select * from libros where libroid=?";
		 
		@GET
		@Path("/{libroid}")
		@Produces(MediaType.BOOKS_API_BOOKS)
		public Books getBookByid(@PathParam("libroid") String libroid) {
			Books book = new Books();
		 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
			
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(GET_BOOK_BY_ID_QUERY);
				stmt.setInt(1, Integer.valueOf(libroid));
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					book.setLibroid(rs.getInt("libroid"));
					book.setTitulo(rs.getString("Titulo"));
					book.setLengua(rs.getString("Lengua"));
					book.setEdicion(rs.getString("Edicion"));
					book.setFecha_ed(rs.getString("Fecha_ed"));
					book.setFecha_imp(rs.getString("Fecha_imp"));
					book.setEditorial(rs.getString("Editorial"));
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
		 
			return book;
		}
		
		//Get libro por titulo
		
		private String GET_BOOK_BY_TITULO = "select * from libros where titulo=?";
		 
		@GET
		@Path("/titulo/{titulo}")
		@Produces(MediaType.BOOKS_API_BOOKS)
		public Books getBookByTitulo(@PathParam("titulo") String titulo,
				@QueryParam("length") int length) {
			
			//
			Books book = new Books();
		 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
			length = (length <= 0) ? 6 : length; 
			
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(GET_BOOK_BY_TITULO);
				//stmt.setInt(1, Integer.valueOf(titulo));
				stmt.setString(1, "%" + titulo + "%");
				//stmt.setInt(2, length);
				
				ResultSet rs = stmt.executeQuery();
				
				
				if (rs.next()) {
					book.setLibroid(rs.getInt("libroid"));
					book.setTitulo(rs.getString("Titulo"));
					book.setLengua(rs.getString("Lengua"));
					book.setEdicion(rs.getString("Edicion"));
					book.setFecha_ed(rs.getString("Fecha_ed"));
					book.setFecha_imp(rs.getString("Fecha_imp"));
					book.setEditorial(rs.getString("Editorial"));
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
		 
			return book;
		}
		
		//CREAR FICHA DE LIBRO
		
		private String INSERT_BOOK_QUERY = "insert into libros (titulo, lengua, edicion, fecha_ed, fecha_imp, editorial) values (?, ?, ?, ?, ?, ?)";
		 
		@POST
		@Consumes(MediaType.BOOKS_API_BOOKS)
		@Produces(MediaType.BOOKS_API_BOOKS)
		public Books createBook(Books book) {
			
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(INSERT_BOOK_QUERY,
						Statement.RETURN_GENERATED_KEYS);
		 
				stmt.setString(1, book.getTitulo());
				stmt.setString(2, book.getLengua());
				stmt.setString(3, book.getEdicion());
				stmt.setString(4, book.getFecha_ed());
				stmt.setString(5, book.getFecha_imp());
				stmt.setString(6, book.getEditorial());
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int libroid = rs.getInt(1);
		 
					book = getBookByid(Integer.toString(libroid));
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
			
			return book;
		}
		
		//borrar una ficha de un libro
		
		private String DELETE_BOOK_QUERY = "delete from libros where libroid=?";
		 
		@DELETE
		@Path("/{libroid}")
		public void deleteBook(@PathParam("libroid") String libroid) {
			
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DELETE_BOOK_QUERY);
				stmt.setInt(1, Integer.valueOf(libroid));
		 
				int rows = stmt.executeUpdate();
				if (rows == 0)
					;// Deleting inexistent book
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
		
	 //actualizar ficha de libro
		
		private String UPDATE_BOOK_QUERY = "update libros set titulo=ifnull(?, titulo), "
				+ "lengua=ifnull(?, lengua), edicion=ifnull(?, eidicion), "
				+ "fecha_ed=ifnull(?, fecha_ed), fecha_imp=ifnull(?, fecha_imp), "
				+ "editorial=ifnull(?, editorial) where libroid=?";
		 
		@PUT
		@Path("/{libroid}")
		@Consumes(MediaType.BOOKS_API_BOOKS)
		@Produces(MediaType.BOOKS_API_BOOKS)
		public Books updateBook(@PathParam("libroid") String libroid, Books book) {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(UPDATE_BOOK_QUERY);
				stmt.setString(1, book.getTitulo());
				stmt.setString(2, book.getLengua());
				stmt.setString(3, book.getEdicion());
				stmt.setString(4, book.getFecha_ed());
				stmt.setString(5, book.getFecha_imp());
				stmt.setString(6, book.getEditorial());
				stmt.setInt(7, Integer.valueOf(libroid));
		 
				int rows = stmt.executeUpdate();
				if (rows == 1)
					book = getBookByid(libroid);
				else {
					;// Updating inexistent sting
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
		 
			return book;
			}
		
		

}
