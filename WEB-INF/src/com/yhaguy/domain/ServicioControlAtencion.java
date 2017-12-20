package com.yhaguy.domain;

import java.util.Date;

import org.zkoss.bind.annotation.DependsOn;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class ServicioControlAtencion extends Domain {

	private Date fecha;
	private String descripcion;	
	private boolean agendamiento;
	private boolean realizado;
	
	private String mes_pago;
	private String forma_pago;
	private double importe_pago;
	private double saldo;
	
	private Cliente paciente;
	private ServicioContrato contrato;
	
	
	@Override
	public int compareTo(Object arg0) {
		return -1;
	}

	@DependsOn("contrato")
	public String getDenominacion() {
		if (this.contrato == null) return "";
		if (this.contrato.getTitular().getId().longValue() == this.paciente.getId().longValue()) {
			return "TITULAR";
		} else {
			return "ADHERENTE";
		}
	}

	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Cliente getPaciente() {
		return paciente;
	}


	public void setPaciente(Cliente paciente) {
		this.paciente = paciente;
	}


	public ServicioContrato getContrato() {
		return contrato;
	}


	public void setContrato(ServicioContrato contrato) {
		this.contrato = contrato;
	}


	public boolean isAgendamiento() {
		return agendamiento;
	}


	public void setAgendamiento(boolean agendamiento) {
		this.agendamiento = agendamiento;
	}


	public boolean isRealizado() {
		return realizado;
	}


	public void setRealizado(boolean realizado) {
		this.realizado = realizado;
	}


	public String getMes_pago() {
		return mes_pago;
	}


	public void setMes_pago(String mes_pago) {
		this.mes_pago = mes_pago;
	}


	public String getForma_pago() {
		return forma_pago;
	}


	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
	}


	public double getImporte_pago() {
		return importe_pago;
	}


	public void setImporte_pago(double importe_pago) {
		this.importe_pago = importe_pago;
	}


	public double getSaldo() {
		return saldo;
	}


	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
