package com.boymask.rysolvia.database.status;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "status")
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	private long token_totali;
	
	private long bollette_totali;

	private BigDecimal incassoTotale;

	public long getToken_totali() {
		return token_totali;
	}

	public void setToken_totali(long token_totali) {
		this.token_totali = token_totali;
	}

	public long getBollette_totali() {
		return bollette_totali;
	}

	public void setBollette_totali(long bollette_totali) {
		this.bollette_totali = bollette_totali;
	}

	public BigDecimal getIncassoTotale() {
		return incassoTotale;
	}

	public void setIncassoTotale(BigDecimal incassoTotale) {
		this.incassoTotale = incassoTotale;
	}

	// getters/setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
