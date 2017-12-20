package com.yhaguy.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class ServicioContrato extends Domain {
	
	private Date fecha = new Date();
	private String numero;
	private String estado;
	private String observacion;
	private int cuotas;
	private double importeGs;
	
	private Tipo forma_pago;
	private Cobrador cobrador;
	private Funcionario asesor;
	private Cliente titular;
	private ServicioPlan plan;
	
	private Set<ServicioContratoDetalle> detalles = new HashSet<ServicioContratoDetalle>();
	private Set<CuotaDetalle> detalle_cuotas = new HashSet<>();
	private Set<ServicioContratoNovedad> novedades = new HashSet<ServicioContratoNovedad>();
	
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Funcionario getAsesor() {
		return asesor;
	}

	public void setAsesor(Funcionario asesor) {
		this.asesor = asesor;
	}

	public Cliente getTitular() {
		return titular;
	}

	public void setTitular(Cliente titular) {
		this.titular = titular;
	}

	public ServicioPlan getPlan() {
		return plan;
	}

	public void setPlan(ServicioPlan plan) {
		this.plan = plan;
		this.importeGs = plan.getTotalImporteGs();
	}

	public int getCuotas() {
		return cuotas;
	}

	public void setCuotas(int cuotas) {
		this.cuotas = cuotas;
	}

	public double getImporteGs() {
		return importeGs;
	}

	public void setImporteGs(double importeGs) {
		this.importeGs = importeGs;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Tipo getForma_pago() {
		return forma_pago;
	}

	public void setForma_pago(Tipo forma_pago) {
		this.forma_pago = forma_pago;
	}

	public Cobrador getCobrador() {
		return cobrador;
	}

	public void setCobrador(Cobrador cobrador) {
		this.cobrador = cobrador;
	}

	public Set<ServicioContratoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<ServicioContratoDetalle> detalles) {
		this.detalles = detalles;
	}

	public Set<CuotaDetalle> getDetalle_cuotas() {
		return detalle_cuotas;
	}

	public void setDetalle_cuotas(Set<CuotaDetalle> detalle_cuotas) {
		this.detalle_cuotas = detalle_cuotas;
	}

	public Set<ServicioContratoNovedad> getNovedades() {
		return novedades;
	}

	public void setNovedades(Set<ServicioContratoNovedad> novedades) {
		this.novedades = novedades;
	}
}
