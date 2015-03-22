package edu.upc.eetac.dsa.csanchez.books.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
 
import edu.upc.eetac.dsa.csanchez.books.api.model.BooksRootAPI;


@Path("/")
public class BooksRootAPIResource {

	@GET
	public BooksRootAPI getRootAPI() {
		BooksRootAPI api = new BooksRootAPI();
		return api;
	}
}
