package edu.upc.eetac.dsa.csanchez.books.api.model;

import java.util.ArrayList;
import java.util.List;

public class ResenyaCollection {
	private List<Resenya> resenya;

	 public ResenyaCollection() {
			super();
			resenya = new ArrayList<>();
		}

	public List<Resenya> getResenya() {
		return resenya;
	}

	public void setResenya(List<Resenya> resenya) {
		this.resenya = resenya;
	}

	public void addResenya(Resenya res) {
		// TODO Auto-generated method stub
		resenya.add(res);
	}


	
	

}
