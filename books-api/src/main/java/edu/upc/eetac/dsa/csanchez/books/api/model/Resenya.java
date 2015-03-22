package edu.upc.eetac.dsa.csanchez.books.api.model;

public class Resenya {
	
	private int resenyaid;
	private String username;
	private String name;
	private long last_modified;
	private long creation_timestamp;
	private String texto;
	private int libroid;
	
	public int getResenyaid() {
		return resenyaid;
	}
	public void setResenyaid(int resenyaid) {
		this.resenyaid = resenyaid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(long last_modified) {
		this.last_modified = last_modified;
	}
	public long getCreation_timestamp() {
		return creation_timestamp;
	}
	public void setCreation_timestamp(long creation_timestamp) {
		this.creation_timestamp = creation_timestamp;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getLibroid() {
		return libroid;
	}
	public void setLibroid(int libroid) {
		this.libroid = libroid;
	}
	

}
