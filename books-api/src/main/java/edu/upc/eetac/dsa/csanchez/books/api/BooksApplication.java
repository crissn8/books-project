package edu.upc.eetac.dsa.csanchez.books.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class BooksApplication extends ResourceConfig {
	
	public BooksApplication(){
		
		super();
		register(DeclarativeLinkingFeature.class); 
	}

}
