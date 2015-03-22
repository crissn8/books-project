package edu.upc.eetac.dsa.csanchez.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;
 
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;
 
import edu.upc.eetac.dsa.csanchez.books.api.BooksRootAPIResource;
import edu.upc.eetac.dsa.csanchez.books.api.MediaType;
import edu.upc.eetac.dsa.csanchez.books.api.BooksResource;
 
public class BooksRootAPI {
	@InjectLinks({
            @InjectLink(resource = BooksRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Books Root API"),
            @InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "collection", title = "Latest books", type = MediaType.BOOKS_API_BOOKS_COLLECTION),
            @InjectLink(resource = BooksResource.class, style = Style.ABSOLUTE, rel = "crear-libro", title = "Create new libro", type = MediaType.BOOKS_API_BOOKS)})
    	private List<Link> links;
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
}