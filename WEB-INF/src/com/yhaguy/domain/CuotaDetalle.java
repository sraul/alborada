package com.yhaguy.domain;

import java.util.Date;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class CuotaDetalle extends Domain {

	private int nro_cuota;
	private Date vencimiento;
	private double importeGs;
	private double saldoGs;
	private String tipo;
	
	private Cobrador cobrador;
	private Tipo forma_pago;
	
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

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public double getImporteGs() {
		return importeGs;
	}

	public void setImporteGs(double importeGs) {
		this.importeGs = importeGs;
	}

	public double getSaldoGs() {
		return saldoGs;
	}

	public void setSaldoGs(double saldoGs) {
		this.saldoGs = saldoGs;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Cobrador getCobrador() {
		return cobrador;
	}

	public void setCobrador(Cobrador cobrador) {
		this.cobrador = cobrador;
	}

	public Tipo getForma_pago() {
		return forma_pago;
	}

	public void setForma_pago(Tipo forma_pago) {
		this.forma_pago = forma_pago;
	}
}
