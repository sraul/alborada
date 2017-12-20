package com.yhaguy.domain;

import java.util.Date;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class ServicioContratoDetalle extends Domain {

	private Date fecha_ingreso;
	private Date fecha_salida;
	private String estado;
	
	private Cliente adherente;
	private Tipo parentesco;
	private Tipo tipo;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}

	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public Date getFecha_salida() {
		return fecha_salida;
	}

	public void setFecha_salida(Date fecha_salida) {
		this.fecha_salida = fecha_salida;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Cliente getAdherente() {
		return adherente;
	}

	public void setAdherente(Cliente adherente) {
		this.adherente = adherente;
	}

	public Tipo getParentesco() {
		return parentesco;
	}

	public void setParentesco(Tipo parentesco) {
		this.parentesco = parentesco;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
}
