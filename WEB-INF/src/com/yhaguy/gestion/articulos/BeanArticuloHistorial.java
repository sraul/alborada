package com.yhaguy.gestion.articulos;

public class BeanArticuloHistorial {

	private String fecha;
	private String numero;
	private String concepto;
	private String entrada;
	private String salida;
	private String importe;

	public BeanArticuloHistorial(String fecha, String numero, String concepto,
			String entrada, String salida, String importe) {
		this.fecha = fecha;
		this.numero = numero;
		this.concepto = concepto;
		this.entrada = entrada;
		this.salida = salida;
		this.importe = importe;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getSalida() {
		return salida;
	}

	public void setSalida(String salida) {
		this.salida = salida;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}
}
