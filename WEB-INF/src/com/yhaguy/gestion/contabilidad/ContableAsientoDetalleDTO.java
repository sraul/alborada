package com.yhaguy.gestion.contabilidad;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyPair;

@SuppressWarnings("serial")
public class ContableAsientoDetalleDTO extends DTO {

	private String descripcion = "";
	private double debe = 0.0;
	private double haber = 0.0;
	
	private MyPair cuenta;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getDebe() {
		return debe;
	}

	public void setDebe(double debe) {
		this.debe = debe;
	}

	public double getHaber() {
		return haber;
	}

	public void setHaber(double haber) {
		this.haber = haber;
	}

	public MyPair getCuenta() {
		return cuenta;
	}

	public void setCuenta(MyPair cuenta) {
		this.cuenta = cuenta;
	}	
}
