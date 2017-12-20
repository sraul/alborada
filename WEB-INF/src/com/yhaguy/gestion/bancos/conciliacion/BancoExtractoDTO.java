package com.yhaguy.gestion.bancos.conciliacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyPair;
import com.yhaguy.gestion.bancos.libro.BancoCtaDTO;

@SuppressWarnings("serial")
public class BancoExtractoDTO extends DTO {
	
	private String numero = "";
	private Date desde;
	private Date hasta;
	private boolean cerrado;
	
	private List<BancoExtractoDetalleDTO> detalles1 = new ArrayList<BancoExtractoDetalleDTO>();
	private List<BancoExtractoDetalleDTO> detalles2 = new ArrayList<BancoExtractoDetalleDTO>();
	private MyPair sucursal;
	private BancoCtaDTO banco;
	
	/**
	 * @return el total de detalles 1
	 */
	public double getTotalDetalle1() {
		double out = 0;
		for (BancoExtractoDetalleDTO item : this.detalles1) {
			out += item.getImporte();
		}
		return out;
	}
	
	/**
	 * @return el total de detalles 2
	 */
	public double getTotalDetalle2() {
		double out = 0;
		for (BancoExtractoDetalleDTO item : this.detalles2) {
			out += item.getImporte();
		}
		return out;
	}
	
	/**
	 * @return el total importe de conciliados
	 */
	public double getTotalImporteConciliado() {
		double out = 0;
		for (BancoExtractoDetalleDTO item : this.detalles1) {
			out += item.isConciliado() ? item.getImporte() : 0;
		}
		return out;
	}
	
	/**
	 * @return el total importe de no conciliados
	 */
	public double getTotalImporteNoConciliado() {
		double out = 0;
		for (BancoExtractoDetalleDTO item : this.detalles1) {
			out += item.isConciliado() ? 0 : item.getImporte();
		}
		return out;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	public List<BancoExtractoDetalleDTO> getDetalles1() {
		return detalles1;
	}

	public void setDetalles1(List<BancoExtractoDetalleDTO> detalles) {
		this.detalles1 = detalles;
	}

	public boolean isCerrado() {
		return cerrado;
	}

	public void setCerrado(boolean cerrado) {
		this.cerrado = cerrado;
	}

	public MyPair getSucursal() {
		return sucursal;
	}

	public void setSucursal(MyPair sucursal) {
		this.sucursal = sucursal;
	}

	public BancoCtaDTO getBanco() {
		return banco;
	}

	public void setBanco(BancoCtaDTO banco) {
		this.banco = banco;
	}

	public List<BancoExtractoDetalleDTO> getDetalles2() {
		return detalles2;
	}

	public void setDetalles2(List<BancoExtractoDetalleDTO> detalles2) {
		this.detalles2 = detalles2;
	}
}
