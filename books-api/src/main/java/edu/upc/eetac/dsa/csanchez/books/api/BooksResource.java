package edu.upc.eetac.dsa.csanchez.books.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.csanchez.books.api.model.Books;
import edu.upc.eetac.dsa.csanchez.books.api.model.BooksCollection;

@Path("/books")
public class BooksResource {
	
	@Context
	private SecurityContext security;

		private DataSource ds = DataSourceSPA.getInstance().getDataSource();
		private String GET_BOOKS_QUERY = "select * from libros order by fecha_ed desc limit ?";
		/*private String GET_BOOKS_QUERY_FROM_LAST = "select * from libros and "
				+ "fecha_ed ? order by fecha_ed desc";*/
		
		private String GET_AUTORES_BOOK_ID = "select name from autor a, libros l where a.name = "
				+ "l.autor and libroid like ?";
		
		
//Listado de libros		
		
		@GET
		@Produces(MediaType.BOOKS_API_BOOKS_COLLECTION)
		public BooksCollection getBooks(@QueryParam("length") int length) {
			BooksCollection books = new BooksCollection();
		 
		
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmtLibros = null;
			PreparedStatement stmtAutor = null;
			
			try {
				//boolean updateFromLast = after > 0;
				stmtLibros = conn.prepareStatement(GET_BOOKS_QUERY);
				
				
				
				
						//.prepareStatement(GET_BOOKS_QUERY);
				/*if (updateFromLast) {
					stmtLibros.setTimestamp(1, new Timestamp(after));
				} else {
					if (before > 0)
						stmtLibros.setTimestamp(1, new Timestamp(before));
					else
						stmtLibros.setTimestamp(1, null);*/
					length = (length <= 0) ? 5 : length;
					stmtLibros.setInt(1, length);
				
				//stmtLibros = conn.prepareStatement(GET_BOOKS_QUERY);
				
				stmtAutor = conn.prepareStatement(GET_AUTORES_BOOK_ID);
				
				ResultSet rs = stmtLibros.executeQuery();
				
				while (rs.next()) {
					Books book = new Books();
					book.setLibroid(rs.getInt("libroid"));
					book.setTitulo(rs.getString("Titulo"));
					book.setLengua(rs.getString("Lengua"));
					book.setEdicion(rs.getString("Edicion"));
					book.setFecha_ed(rs.getString("Fecha_ed"));
					book.setFecha_imp(rs.getString("Fecha_imp"));
					book.setEditorial(rs.getString("Editorial"));
					book.setAutor(rs.getString("autor"));
				
					
					stmtAutor.setInt(1, Integer.valueOf(book.getLibroid()));
					ResultSet rs2 = stmtAutor.executeQuery();
					while (rs2.next()){
						Books b = new Books();
						b.setAutor(rs2.getString("name"));
	
					}
					

					books.addBook(book);
					
				}
			}  catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmtLibros != null)
						stmtLibros.close();
				/*	if (stmtAutor != null)
						stmtAutor.close();*/
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return books;
		}
		
		
		
		
		//Libro Cacheable
		
		private Books getBookFromDatabase(String libroid) {
			Books book = new Books();
		 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmtLibros = null;
			PreparedStatement stmtAutor = null;
			
			try {
				stmtLibros = conn.prepareStatement(GET_BOOK_BY_ID_QUERY);
				stmtLibros.setInt(1, Integer.valueOf(libroid));
				
				stmtAutor = conn.prepareStatement(GET_AUTORES_BOOK_ID);
				stmtAutor.setInt(1, Integer.valueOf(libroid));
				
				ResultSet rs = stmtLibros.executeQuery();
				if (rs.next()) {
					book.setLibroid(rs.getInt("libroid"));
					book.setTitulo(rs.getString("Titulo"));
					book.setLengua(rs.getString("Lengua"));
					book.setEdicion(rs.getString("Edicion"));
					book.setFecha_ed(rs.getString("Fecha_ed"));
					book.setFecha_imp(rs.getString("Fecha_imp"));
					book.setEditorial(rs.getString("Editorial"));
					book.setAutor(rs.getString("autor"));
					
					stmtAutor.setInt(1, Integer.valueOf(book.getLibroid()));
					ResultSet rs2 = stmtAutor.executeQuery();
					while (rs2.next()){
						Books b = new Books();
						b.setAutor(rs2.getString("name"));
	
					}
					
					
				} else {
					throw new NotFoundException("There's no libro with libroid="
							+ libroid);
				}
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmtLibros != null)
						stmtLibros.close();
					if (stmtAutor != null)
						stmtAutor.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return book;
		}
		
	//Libro por id
		
		private String GET_BOOK_BY_ID_QUERY = "select * from libros where libroid like ?";
		 
		@GET
		@Path("/{libroid}")
		@Produces(MediaType.BOOKS_API_BOOKS)
		public Response getBook(@PathParam("libroid") String libroid,
				@Context Request request) {
		
				// Create CacheControl
				CacheControl cc = new CacheControl();
			 
				Books book = getBookFromDatabase(libroid);
			 
				// Calculate the ETag on last modified date of user resource
				EntityTag eTag = new EntityTag(book.getTitulo() + book.getLengua() + 
						book.getEdicion() + book.getFecha_ed() + book.getFecha_imp() + 
						book.getEditorial()+book.getAutor());
			 
				// Verify if it matched with etag available in http request
				Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);
			 
				// If ETag matches the rb will be non-null;
				// Use the rb to return the response without any further processing
				if (rb != null) {
					return rb.cacheControl(cc).tag(eTag).build();
				}
			 
				// If rb is null then either it is first time request; or resource is
				// modified
				// Get the updated representation and return with Etag attached to it
				rb = Response.ok(book).cacheControl(cc).tag(eTag);
			 
				return rb.build();
			}
		
		
		//Get libros por titulo
		
		private String GET_BOOK_BY_TITULO = "select * from libros where titulo like ?";
		 
		@GET
		@Path("/titulo/{titulo}")
		@Produces(MediaType.BOOKS_API_BOOKS_COLLECTION)
		public BooksCollection getBooksByTitulo(@PathParam("titulo") String titulo) {
			
			BooksCollection books = new BooksCollection();
		 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
			
						
			PreparedStatement stmtLibros = null;
			//PreparedStatement stmtAutor = null;
			try {
				stmtLibros = conn.prepareStatement(GET_BOOK_BY_TITULO);
			
				//stmtAutor = conn.prepareStatement(GET_AUTORES_BOOK_ID);
				
				ResultSet rs = stmtLibros.executeQuery();
				
				
				if (rs.next()) {
					Books book = new Books();
					book.setLibroid(rs.getInt("libroid"));
					book.setTitulo(rs.getString("titulo"));
					book.setLengua(rs.getString("lengua"));
					book.setEdicion(rs.getString("edicion"));
					book.setFecha_ed(rs.getString("fecha_ed"));
					book.setFecha_imp(rs.getString("fecha_imp"));
					book.setEditorial(rs.getString("editorial"));
					book.setAutor(rs.getString("autor"));
					
				/*	stmtAutor.setInt(1, Integer.valueOf(book.getLibroid()));
					ResultSet rs2 = stmtAutor.executeQuery();
					while (rs2.next()){
						Books b = new Books();
						b.setAutor(rs2.getString("name"));
					}*/
					
					books.addBook(book);
				}
				
			}  catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmtLibros != null)
						stmtLibros.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return books;
		}
		
		
		
		private String GET_BOOK_BY_TITLE = "select * from libros where titulo like ? ";
		 
		@GET
		@Path("/{titulo}")
		@Produces(MediaType.BOOKS_API_BOOKS_COLLECTION)
		public BooksCollection getBooksByTitle() {
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
				stmt = conn.prepareStatement(GET_BOOK_BY_TITLE);
				
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Books b = new Books();
					b.setLibroid(rs.getInt("libroid"));
					b.setTitulo(rs.getString("titulo"));
					b.setLengua(rs.getString("lengua"));
					b.setEdicion(rs.getString("edicion"));
					b.setFecha_ed(rs.getString("fecha_ed"));
					b.setFecha_imp(rs.getString("fecha_imp"));
					b.setEditorial(rs.getString("editorial"));
					b.setAutor(rs.getString("autor"));
					
					books.addBook(b);
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
		 
			return books;
		}
		
		
		//Ficha de libro por autor
		

		
		//CREAR FICHA DE LIBRO
		
		
		private String INSERT_BOOK_QUERY = "insert into libros (titulo, lengua, edicion, fecha_ed, fecha_imp, editorial, autor) values (?, ?, ?, ?, ?, ?, ?)";
		//private String GET_AUTHORID_BY_NAME = "select autorid from autor where name like ?";
		//private String INSERT_AUTHORSBOOKS = "insert into RelacionAutorLibros values (?,?);"; 
		
		
		@POST
		@Consumes(MediaType.BOOKS_API_BOOKS)
		@Produces(MediaType.BOOKS_API_BOOKS)
		public Books createBook(Books book) {
			validateBook(book);
			
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
		//	PreparedStatement stmtgetname = null;
			//PreparedStatement stmtrelation = null;
			
			try {
				stmt = conn.prepareStatement(INSERT_BOOK_QUERY,
						Statement.RETURN_GENERATED_KEYS);
		 
				stmt.setString(1, book.getTitulo());
				stmt.setString(2, book.getLengua());
				stmt.setString(3, book.getEdicion());
				stmt.setString(4, book.getFecha_ed());
				stmt.setString(5, book.getFecha_imp());
				stmt.setString(6, book.getEditorial());
				stmt.setString(7, book.getAutor());
				stmt.executeUpdate();
				
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int libroid = rs.getInt(1);
					

		 
					book = getBookFromDatabase(Integer.toString(libroid));
				//	book = getBookByTitulo(Integer.toString(libroid));
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
		
		private void validateBook(Books book) {
			if (book.getTitulo().length() > 100)
				throw new BadRequestException("Titulo can't begreater than 100 characters.");
			if (book.getLengua().length() > 20)
				throw new BadRequestException("Lengua can't be greater than 20 characters.");
			if (book.getEdicion().length() > 20)
				throw new BadRequestException("Edicion can't be greater than 20 characters.");
			if (book.getFecha_ed().length() > 20)
				throw new BadRequestException("Fecha_ed can't be greater than 20 characters.");
			if (book.getFecha_imp().length() > 20)
				throw new BadRequestException("Fecha_imp can't be greater than 20 characters.");
			if (book.getEditorial().length() > 20)
				throw new BadRequestException("Editorial can't be greater than 20 characters.");
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
					throw new NotFoundException("There's no libro with libroid="
							+ libroid);// Deleting inexistent book
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
				+ "lengua=ifnull(?, lengua), edicion=ifnull(?, edicion), "
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
					book = getBookFromDatabase(libroid);
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
