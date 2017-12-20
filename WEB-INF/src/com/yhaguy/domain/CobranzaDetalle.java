package com.yhaguy.domain;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class CobranzaDetalle extends Domain {

	private int nro_cuota;
	private String tipo;
	private double importeGs;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}

	public int getNro_cuota() {
		return nro_cuota;
	}

	public void setNro_cuota(int nro_cuota) {
		this.nro_cuota = nro_cuota;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getImporteGs() {
		return importeGs;
	}

	public void setImporteGs(double importeGs) {
		this.importeGs = importeGs;
	}

}
