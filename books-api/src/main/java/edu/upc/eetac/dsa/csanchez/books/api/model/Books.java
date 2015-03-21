package edu.upc.eetac.dsa.csanchez.books.api.model;

public class Books {
	
	private int libroid;
	private String titulo;
	private String lengua;
	private String edicion;
	private String fecha_ed;
	private String fecha_imp;
	private String editorial;
	
	
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
