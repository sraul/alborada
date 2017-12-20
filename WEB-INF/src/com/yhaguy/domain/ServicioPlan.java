package com.yhaguy.domain;

import java.util.HashSet;
import java.util.Set;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class ServicioPlan extends Domain {
	
	private String codigo;
	private String descripcion;
	
	private Set<ServicioPlanDetalle> detalles = new HashSet<ServicioPlanDetalle>();
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}
	
	public double getTotalImporteGs() {
		double out = 0;
		for (ServicioPlanDetalle item : this.detalles) {
			out += item.getPrecioGs();
		}
		return out;
	}
	
	public String getAbreviatura() {
		return this.descripcion.substring(0, 1);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<ServicioPlanDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<ServicioPlanDetalle> detalles) {
		this.detalles = detalles;
	}
}
