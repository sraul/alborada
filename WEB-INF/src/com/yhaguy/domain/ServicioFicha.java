package com.yhaguy.domain;

import java.util.HashSet;
import java.util.Set;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class ServicioFicha extends Domain {
	
	private String numero;
	private String sucursal;
	private String telefono;
	
	private Cliente cliente;
	private Set<ServicioFichaDetalle> detalles = new HashSet<ServicioFichaDetalle>();

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

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente usuario) {
		this.cliente = usuario;
	}

	public Set<ServicioFichaDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<ServicioFichaDetalle> detalles) {
		this.detalles = detalles;
	}

}
