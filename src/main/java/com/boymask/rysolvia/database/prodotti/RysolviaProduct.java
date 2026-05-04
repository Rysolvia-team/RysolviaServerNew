package com.boymask.rysolvia.database.prodotti;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "rysolvia_products")
public class RysolviaProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	private String name;
	private long prezzo=0;
	private String description;

	private int numero_bollette;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumero_bollette() {
		return numero_bollette;
	}

	public void setNumero_bollette(int numero_bollette) {
		this.numero_bollette = numero_bollette;
	}

	public long getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(long prezzo) {
		this.prezzo = prezzo;
	}

	

}
