package com.yhaguy.gestion.contabilidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.DependsOn;

import com.coreweb.dto.DTO;

@SuppressWarnings("serial")
public class ContableAsientoDTO extends DTO {

	private Date fecha = new Date();
	private String numero = "";
	private String descripcion = "";

	private List<ContableAsientoDetalleDTO> detalles = new ArrayList<ContableAsientoDetalleDTO>();
	
	@DependsOn("detalles")
	public double getTotalDebe() {
		double out = 0;
		for (ContableAsientoDetalleDTO item : this.detalles) {
			out += item.getDebe();
		}
		return out;
	}
	
	@DependsOn("detalles")
	public double getTotalHaber() {
		double out = 0;
		for (ContableAsientoDetalleDTO item : this.detalles) {
			out += item.getDebe();
		}
		return out;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<ContableAsientoDetalleDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<ContableAsientoDetalleDTO> detalles) {
		this.detalles = detalles;
	}
}
