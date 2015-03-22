package edu.upc.eetac.dsa.csanchez.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.csanchez.books.api.BooksResource;
import edu.upc.eetac.dsa.csanchez.books.api.MediaType;


public class Books {
	
	@InjectLinks({
		@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "libros", title = "Latest libros", type = MediaType.BOOKS_API_BOOKS_COLLECTION),
		@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Libro", type = MediaType.BOOKS_API_BOOKS, method = "getBookFromDatabase", bindings = @Binding(name = "libroid", value = "${instance.libroid}")) })
	
	private List<Link> links;
	private int libroid;
	private String titulo;

	private String lengua;
	private String edicion;
	private String fecha_ed;
	private String fecha_imp;
	private String editorial;
	private String autor;
	
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public int getLibroid() {
		return libroid;
	}
	public void setLibroid(int libroid) {
		this.libroid = libroid;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getLengua() {
		return lengua;
	}
	public void setLengua(String lengua) {
		this.lengua = lengua;
	}
	public String getEdicion() {
		return edicion;
	}
	public void setEdicion(String edicion) {
		this.edicion = edicion;
	}
	public String getFecha_ed() {
		return fecha_ed;
	}
	public void setFecha_ed(String fecha_ed) {
		this.fecha_ed = fecha_ed;
	}
	public String getFecha_imp() {
		return fecha_imp;
	}
	public void setFecha_imp(String fecha_imp) {
		this.fecha_imp = fecha_imp;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

}
