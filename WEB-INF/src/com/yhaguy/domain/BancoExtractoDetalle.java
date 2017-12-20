package com.yhaguy.domain;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class BancoExtractoDetalle extends Domain {
	
	private String numero;
	private String descripcion;
	private double importe;
	private boolean conciliado;
	
	private BancoMovimiento bancoMovimiento;

	@Override
	public int compareTo(Object o) {
		return -1;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double monto) {
		this.importe = monto;
	}

	public BancoMovimiento getBancoMovimiento() {
		return bancoMovimiento;
	}

	public void setBancoMovimiento(BancoMovimiento bancoMovimiento) {
		this.bancoMovimiento = bancoMovimiento;
	}

	public boolean isConciliado() {
		return conciliado;
	}

	public void setConciliado(boolean conciliado) {
		this.conciliado = conciliado;
	}
}
