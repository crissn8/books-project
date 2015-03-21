package edu.upc.eetac.dsa.csanchez.books.api.model;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

public class BooksCollection {
 private List<Books> books;
 
 public BooksCollection() {
		super();
		books = new ArrayList<>();
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
