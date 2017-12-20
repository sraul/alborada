package com.yhaguy.gestion.contabilidad;

public class BeanLibroVenta {

	private String fecha;
	private String concepto;
	private String numero;
	private String razonSocial;
	private String ruc;
	private String gravado5;
	private String gravado10;
	private String iva5;
	private String iva10;
	private String total;

	public BeanLibroVenta(String fecha, String concepto, String numero,
			String razonSocial, String ruc, String gravado10, String iva10,
			String gravado5, String iva5, String total) {
		this.fecha = fecha;
		this.concepto = concepto;
		this.numero = numero;
		this.razonSocial = razonSocial;
		this.ruc = ruc;
		this.gravado10 = gravado10;
		this.iva10 = iva10;
		this.gravado5 = gravado5;
		this.iva5 = iva5;
		this.total = total;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getGravado5() {
		return gravado5;
	}

	public void setGravado5(String gravado5) {
		this.gravado5 = gravado5;
	}

	public String getGravado10() {
		return gravado10;
	}

	public void setGravado10(String gravado10) {
		this.gravado10 = gravado10;
	}

	public String getIva5() {
		return iva5;
	}

	public void setIva5(String iva5) {
		this.iva5 = iva5;
	}

	public String getIva10() {
		return iva10;
	}

	public void setIva10(String iva10) {
		this.iva10 = iva10;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
