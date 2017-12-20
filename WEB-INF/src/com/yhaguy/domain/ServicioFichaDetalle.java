package com.yhaguy.domain;

import java.util.Date;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class ServicioFichaDetalle extends Domain {

	private Date fecha;
	private int diente;
	private String tratamiento;
	private String doctor;
	private boolean tipo_1;
	private boolean tipo_2;
	private boolean tipo_3;
	private boolean tipo_4;	
	private double pagoGs;
	private double presupuestoGs;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getDiente() {
		return diente;
	}

	public void setDiente(int diente) {
		this.diente = diente;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public boolean isTipo_1() {
		return tipo_1;
	}

	public void setTipo_1(boolean tipo_1) {
		this.tipo_1 = tipo_1;
	}

	public boolean isTipo_2() {
		return tipo_2;
	}

	public void setTipo_2(boolean tipo_2) {
		this.tipo_2 = tipo_2;
	}

	public boolean isTipo_3() {
		return tipo_3;
	}

	public void setTipo_3(boolean tipo_3) {
		this.tipo_3 = tipo_3;
	}

	public boolean isTipo_4() {
		return tipo_4;
	}

	public void setTipo_4(boolean tipo_4) {
		this.tipo_4 = tipo_4;
	}

	public double getPagoGs() {
		return pagoGs;
	}

	public void setPagoGs(double pagoGs) {
		this.pagoGs = pagoGs;
	}

	public double getPresupuestoGs() {
		return presupuestoGs;
	}

	public void setPresupuestoGs(double presupuestoGs) {
		this.presupuestoGs = presupuestoGs;
	}

}
