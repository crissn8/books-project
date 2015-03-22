package edu.upc.eetac.dsa.csanchez.books.api.model;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.csanchez.books.api.BooksResource;
import edu.upc.eetac.dsa.csanchez.books.api.MediaType;

public class BooksCollection {
	@InjectLinks({
		@InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "create-libro", title = "Create libro", type = MediaType.BOOKS_API_BOOKS)})
		//@InjectLink(value = "/stings?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous stings", type = MediaType.BOOKS_API_BOOKS_COLLECTION, bindings = { @Binding(name = "before", value = "${instance.oldestTimestamp}") }),
		//@InjectLink(value = "/stings?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest stings", type = MediaType.BOOKS_API_BOOKS_COLLECTION, bindings = { @Binding(name = "after", value = "${instance.newestTimestamp}") }) })
private List<Link> links;
 private List<Books> books;
 
 public BooksCollection() {
		super();
		books = new ArrayList<>();
	}



public List<Link> getLinks() {
	return links;
}



public void setLinks(List<Link> links) {
	this.links = links;
}



public List<Books> getBooks() {
	return books;
}


public void setBooks(List<Books> books) {
	this.books = books;
}



public void addBook(Books book) {
	// TODO Auto-generated method stub
	books.add(book);
}







	
}
