package com.yhaguy.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class Cobranza extends Domain {

	private Date fecha;
	private String numero;
	private String observacion;
	private double importeGs;	
	
	private Cliente cliente;
	private Cobrador cobrador;
	private Tipo bocaCobranza;
	
	private Set<CobranzaDetalle> detalles = new HashSet<CobranzaDetalle>();
	
	@Override
	public int compareTo(Object arg0) {
		return -1;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getImporteGs() {
		return importeGs;
	}

	public void setImporteGs(double importeGs) {
		this.importeGs = importeGs;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cobrador getCobrador() {
		return cobrador;
	}

	public void setCobrador(Cobrador cobrador) {
		this.cobrador = cobrador;
	}

	public Tipo getBocaCobranza() {
		return bocaCobranza;
	}

	public void setBocaCobranza(Tipo bocaCobranza) {
		this.bocaCobranza = bocaCobranza;
	}

	public Set<CobranzaDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<CobranzaDetalle> detalles) {
		this.detalles = detalles;
	}
}
