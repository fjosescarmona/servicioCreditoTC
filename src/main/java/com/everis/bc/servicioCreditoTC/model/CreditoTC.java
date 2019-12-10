package com.everis.bc.servicioCreditoTC.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="c_ctarjetacredito")
public class CreditoTC {
	@Id
	private String id;
	@NotNull
	private String nro_tarjeta;
	@NotNull
	private String tipo;
	@NotNull
	private double saldo;
	@NotNull
	private double limite;
	
	private List<Listas> titulares;
	
	public String getNro_tarjeta() {
		return nro_tarjeta;
	}

	public void setNro_tarjeta(String nro_tarjeta) {
		this.nro_tarjeta = nro_tarjeta;
	}

	public double getLimite() {
		return limite;
	}

	public void setLimite(double limite) {
		this.limite = limite;
	}

	
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public List<Listas> getTitulares() {
		return titulares;
	}

	public void setTitulares(List<Listas> titulares) {
		this.titulares = titulares;
	}


}
