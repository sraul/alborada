package com.yhaguy.gestion.bancos.conciliacion;

import java.util.Date;

import com.coreweb.dto.DTO;
import com.yhaguy.gestion.bancos.libro.BancoMovimientoDTO;
import com.yhaguy.util.Utiles;

@SuppressWarnings("serial")
public class BancoExtractoDetalleDTO extends DTO {

	private String numero = "";
	private String descripcion = "";
	private Date fecha; 
	private double importe = 0;
	private boolean conciliado = false;
	
	private double numero_ = 0;
	private double importe_ = 0;
	
	private BancoMovimientoDTO bancoMovimiento;
	
	/**
	 * @return el importe en String..
	 */
	public String getImporteGs_() {
		return Utiles.getNumberFormat(this.importe);
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public boolean isConciliado() {
		return conciliado;
	}

	public void setConciliado(boolean conciliado) {
		this.conciliado = conciliado;
	}

	public double getNumero_() {
		return numero_;
	}

	public void setNumero_(double numero_) {
		this.numero_ = numero_;
	}

	public double getImporte_() {
		return importe_;
	}

	public void setImporte_(double importe_) {
		this.importe_ = importe_;
	}

	public BancoMovimientoDTO getBancoMovimiento() {
		return bancoMovimiento;
	}

	public void setBancoMovimiento(BancoMovimientoDTO bancoMovimiento) {
		this.bancoMovimiento = bancoMovimiento;
	}	
}
