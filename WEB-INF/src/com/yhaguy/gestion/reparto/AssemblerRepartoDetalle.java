package com.yhaguy.gestion.reparto;

import com.coreweb.domain.Domain;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.yhaguy.Configuracion;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.RepartoDetalle;
import com.yhaguy.domain.Transferencia;
import com.yhaguy.domain.Venta;

public class AssemblerRepartoDetalle extends Assembler {

	private static String[] attIgualesRepartoDetalle = { "idMovimiento",
			"observacion", "entregado", "peso" };

	@Override
	public Domain dtoToDomain(DTO dtoR) throws Exception {
		RepartoDetalleDTO dto = (RepartoDetalleDTO) dtoR;
		RepartoDetalle domain = (RepartoDetalle) getDomain(dto,
				RepartoDetalle.class);

		this.copiarValoresAtributos(dto, domain, attIgualesRepartoDetalle);
		this.myArrayToDomain(dto, domain, "tipoMovimiento");

		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		RepartoDetalleDTO dto = (RepartoDetalleDTO) getDTO(domain,
				RepartoDetalleDTO.class);

		this.copiarValoresAtributos(domain, dto, attIgualesRepartoDetalle);
		this.domainToMyArray(domain, dto, "tipoMovimiento", new String[] {
				"descripcion", "sigla" });

		this.setDetalle((RepartoDetalle) domain, dto);

		return dto;
	}

	/**
	 * setea el detalle segun el tipo de movimiento..
	 */
	private void setDetalle(RepartoDetalle det, RepartoDetalleDTO dto) throws Exception {		
		
		MyArray detalle = new MyArray();
		RepartoSimpleVM ctr = new RepartoSimpleVM();
		String sigla = det.getTipoMovimiento().getSigla();
		long idMovimiento = det.getIdMovimiento();
		
		if (this.isVenta(sigla)) {
			Venta venta = this.getVenta(idMovimiento);
			detalle = ctr.getVentaMyArray(venta);
			
		} else {
			Transferencia transf = this.getTransferencia(idMovimiento);
			detalle = ctr.getTransferenciaMyArray(transf);
		}		
		dto.setDetalle(detalle);
	}
	
	/**
	 * @return si es venta o no segun la sigla..
	 */
	private boolean isVenta(String sigla) {
		return sigla.equals(Configuracion.SIGLA_TM_PEDIDO_VENTA)
				|| sigla.equals(Configuracion.SIGLA_TM_FAC_VENTA_CONTADO)
				|| sigla.equals(Configuracion.SIGLA_TM_FAC_VENTA_CREDITO);
	}	
	
	/**
	 * @return la transferencia..
	 */
	private Transferencia getTransferencia(long id) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return (Transferencia) rr.getObject(Transferencia.class.getName(), id);
	}
	
	/**
	 * @return la venta..
	 */
	private Venta getVenta(long id) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return (Venta) rr.getObject(Venta.class.getName(), id);
	}
}
