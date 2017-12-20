package com.yhaguy.gestion.comun;

import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.yhaguy.Configuracion;
import com.yhaguy.domain.BancoChequeTercero;
import com.yhaguy.domain.BancoDescuentoCheque;
import com.yhaguy.domain.CtaCteEmpresaMovimiento;
import com.yhaguy.domain.Empresa;
import com.yhaguy.domain.Gasto;
import com.yhaguy.domain.RecaudacionCentral;
import com.yhaguy.domain.Recibo;
import com.yhaguy.domain.ReciboDetalle;
import com.yhaguy.domain.ReciboFormaPago;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioContrato;
import com.yhaguy.domain.TipoMovimiento;
import com.yhaguy.gestion.bancos.libro.ControlBancoMovimiento;
import com.yhaguy.gestion.notacredito.NotaCreditoDTO;
import com.yhaguy.gestion.notadebito.NotaDebitoDTO;
import com.yhaguy.util.Utiles;

public class ControlCuentaCorriente {

	/**
	 * agregar movimiento nota credito compra..
	 */
	public static void addNotaCreditoCompra(NotaCreditoDTO dto, String user) 
			throws Exception {		
		RegisterDomain rr = RegisterDomain.getInstance();
		MyArray factura = dto.getFacturas().get(0);
		TipoMovimiento tm = rr.getTipoMovimientoById(((MyPair) factura.getPos4()).getId());
		
		CtaCteEmpresaMovimiento ctmCompra = rr
				.getCtaCteMovimientoByIdMovimiento(factura.getId(), tm.getSigla());
		
		// Gasto Contado de Caja Chica..no generan movimientos en cta cte.
		if (ctmCompra == null && tm.getSigla().equals(Configuracion.SIGLA_TM_FAC_GASTO_CONTADO)) {
			return;
		}
		
		CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
		ctm.setTipoMovimiento(rr.getTipoMovimientoBySigla(Configuracion.SIGLA_TM_NOTA_CREDITO_COMPRA));
		ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_PROVEEDOR));
		ctm.setFechaEmision(dto.getFechaEmision());
		ctm.setFechaVencimiento(dto.getFechaEmision());
		ctm.setIdEmpresa((long) dto.getProveedor().getPos4());
		ctm.setIdMovimientoOriginal(dto.getId());
		ctm.setIdVendedor(dto.getVendedor().getId());
		ctm.setImporteOriginal(dto.getImporteGs());
		ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		ctm.setNroComprobante(dto.getNumero());
		ctm.setSucursal(rr.getSucursalAppById(dto.getSucursal().getId()));
		
		double saldo = ctmCompra.getSaldoFinal() - dto.getImporteGs();
		
		if (saldo <= 0) {
			ctmCompra.setSaldo(0);
			ctm.setSaldo(saldo);
		} else {
			ctmCompra.setSaldo(saldo);
			ctm.setSaldo(0);
		}
		
		rr.saveObject(ctmCompra, user);
		rr.saveObject(ctm, user);
	}	
	
	/**
	 * agregar movimiento recibo de pago..
	 */
	public static void addReciboDePago(long idOrdenPago, String user) throws Exception {		
		RegisterDomain rr = RegisterDomain.getInstance();
		Recibo pago = rr.getOrdenPagoById(idOrdenPago);
		
		CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
		ctm.setTipoMovimiento(pago.getTipoMovimiento());
		ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_PROVEEDOR));
		ctm.setFechaEmision(pago.getFechaEmision());
		ctm.setIdEmpresa(pago.getProveedor().getIdEmpresa());
		ctm.setIdMovimientoOriginal(pago.getId());
		ctm.setIdVendedor(0);
		ctm.setImporteOriginal(pago.getTotalImporteGs());
		ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		ctm.setNroComprobante(pago.getNumero());
		ctm.setSucursal(pago.getSucursal());
		ctm.setSaldo(0);
		
		for (ReciboDetalle det : pago.getDetalles()) {
			if (det.getMovimiento() != null) {
				CtaCteEmpresaMovimiento ctmCompra = det.getMovimiento();
				ctmCompra.setSaldo(ctmCompra.getSaldo() - det.getMontoGs());
				rr.saveObject(ctmCompra, user);
			}
		}	
		rr.saveObject(ctm, user);
	}
	
	/**
	 * agregar movimiento recibo de cobro..
	 */
	public static void addReciboDeCobro(long idRecibo, String user) throws Exception {		
		RegisterDomain rr = RegisterDomain.getInstance();
		Recibo cobro = (Recibo) rr.getObject(Recibo.class.getName(), idRecibo);
		
		CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
		ctm.setTipoMovimiento(cobro.getTipoMovimiento());
		ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE));
		ctm.setFechaEmision(cobro.getFechaEmision());
		ctm.setIdEmpresa(cobro.getCliente().getIdEmpresa());
		ctm.setIdMovimientoOriginal(cobro.getId());
		ctm.setIdVendedor(0);
		ctm.setImporteOriginal(cobro.getTotalImporteGs());
		ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		ctm.setNroComprobante(cobro.getNumero());
		ctm.setSucursal(cobro.getSucursal());
		ctm.setSaldo(0);
		
		for (ReciboDetalle det : cobro.getDetalles()) {
			CtaCteEmpresaMovimiento ctmVenta = det.getMovimiento();
			ctmVenta.setSaldo(ctmVenta.getSaldo() - det.getMontoGs());
			rr.saveObject(ctmVenta, user);
		}	
		rr.saveObject(ctm, user);
		ControlCuentaCorriente.addRecaudacionCentral(idRecibo, user);
		
		if (cobro.isCancelacionCheque()) {
			ControlBancoMovimiento.reembolsoChequeRechazado(cobro, user);
		}
	}
	
	/**
	 * agregar movimiento recaudacion casa central..
	 */
	public static void addRecaudacionCentral(long idRecibo, String user) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		Recibo cobro = (Recibo) rr.getObject(Recibo.class.getName(), idRecibo);
		
		for (ReciboFormaPago fp : cobro.getFormasPago()) {
			if (fp.isRecaudacionCentral()) {
				RecaudacionCentral rcc = new RecaudacionCentral();
				rcc.setNumeroRecibo(cobro.getNumero());
				rcc.setImporteGs(fp.getMontoGs());
				rcc.setSaldoGs(fp.getMontoGs());
				rcc.setNumeroCheque("");
				rcc.setNumeroDeposito("");
				rcc.setRazonSocial(cobro.getCliente().getRazonSocial());
				rr.saveObject(rcc, user);
			}
		}
	}
	
	/**
	 * agregar movimiento cheque rechazado..
	 */
	public static void addChequeRechazado(long idCheque, String user) throws Exception {		
		RegisterDomain rr = RegisterDomain.getInstance();
		BancoChequeTercero cheque = (BancoChequeTercero) rr.getObject(BancoChequeTercero.class.getName(), idCheque);
		
		CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
		ctm.setTipoMovimiento(rr.getTipoMovimientoBySigla(Configuracion.SIGLA_TM_CHEQUE_RECHAZADO));
		ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE));
		ctm.setFechaEmision(cheque.getFecha());
		ctm.setIdEmpresa(cheque.getCliente().getIdEmpresa());
		ctm.setIdMovimientoOriginal(cheque.getId());
		ctm.setIdVendedor(0);
		ctm.setImporteOriginal(cheque.getMonto());
		ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		ctm.setNroComprobante(cheque.getNumero());
		ctm.setSucursal(cheque.getSucursalApp());
		ctm.setSaldo(cheque.getMonto());	
		rr.saveObject(ctm, user);
	}
	
	/**
	 * agregar movimiento prestamo casa central..
	 */
	public static void addPrestamoCentral(long idPrestamo, String user)
			throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		BancoDescuentoCheque prestamo = (BancoDescuentoCheque) rr.getObject(BancoDescuentoCheque.class.getName(), idPrestamo);
		CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
		ctm.setTipoMovimiento(rr.getTipoMovimientoBySigla(Configuracion.SIGLA_TM_PRESTAMO_CASA_CENTRAL));
		ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE));
		ctm.setFechaEmision(prestamo.getFecha());
		ctm.setIdEmpresa(Configuracion.ID_EMPRESA_YHAGUY_CENTRAL);
		ctm.setIdMovimientoOriginal(prestamo.getId());
		ctm.setIdVendedor(0);
		ctm.setImporteOriginal(prestamo.getTotalImporteGs());
		ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		ctm.setNroComprobante("PRESTAMO NRO. " + prestamo.getId());
		ctm.setSucursal(prestamo.getSucursalApp());
		ctm.setSaldo(prestamo.getTotalImporteGs());
		rr.saveObject(ctm, user);
	}
	
	/**
	 * agregar movimiento nota credito compra..
	 */
	public static void addNotaDebitoVenta(NotaDebitoDTO dto, String user) 
			throws Exception {		
		RegisterDomain rr = RegisterDomain.getInstance();		
		
		CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
		ctm.setTipoMovimiento(rr.getTipoMovimientoBySigla(Configuracion.SIGLA_TM_NOTA_DEBITO_VENTA));
		ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE));
		ctm.setFechaEmision(dto.getFecha());
		ctm.setFechaVencimiento(dto.getFecha());
		ctm.setIdEmpresa(dto.getCliente().getEmpresa().getId());
		ctm.setIdMovimientoOriginal(dto.getId());
		ctm.setIdVendedor(0);
		ctm.setImporteOriginal(dto.getTotalImporteGs());
		ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		ctm.setNroComprobante(dto.getNumero());
		ctm.setSucursal(rr.getSucursalAppById(dto.getSucursal().getId()));
		ctm.setSaldo(dto.getTotalImporteGs());
		rr.saveObject(ctm, user);
	}
	
	/**
	 * cancela los saldos con debitos bancarios generados desde gastos..
	 */
	public static void cancelarSaldoConDebitoBancario(long idGasto, String user) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		Gasto gasto = rr.getGastoById(idGasto);
		for (CtaCteEmpresaMovimiento movim : gasto.getAplicacionesDebitoBancario()) {
			movim.setSaldo(0);
			rr.saveObject(movim, user);
		}
	}
	
	/**
	 * bloquea la cuenta del cliente..
	 */
	public static void bloquearCliente(long idEmpresa, String motivo, String user) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		Empresa emp = rr.getEmpresaById(idEmpresa);
		emp.setCuentaBloqueada(true);
		emp.setMotivoBloqueo(motivo);
		rr.saveObject(emp, user);
	}
	
	/**
	 * agregar movimiento recibo de cobro..
	 */
	public static void addContrato(ServicioContrato contrato, int cuotas, String user) throws Exception {		
		RegisterDomain rr = RegisterDomain.getInstance();
		
		for (int i = 0; i < cuotas; i++) {
			CtaCteEmpresaMovimiento ctm = new CtaCteEmpresaMovimiento();
			ctm.setTipoMovimiento(rr.getTipoMovimientoBySigla(Configuracion.SIGLA_TM_CONTRATO_SERVICIO_ODONTOLOGIA));
			ctm.setTipoCaracterMovimiento(rr.getTipoPorSigla(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE));
			ctm.setFechaEmision(contrato.getFecha());
			ctm.setFechaVencimiento(Utiles.agregarDias(contrato.getFecha(), (30 * (i+1))));
			ctm.setIdEmpresa(contrato.getTitular().getIdEmpresa());
			ctm.setIdMovimientoOriginal(contrato.getId());
			ctm.setIdVendedor(contrato.getAsesor().getId());
			ctm.setImporteOriginal(contrato.getImporteGs());
			ctm.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
			ctm.setNroComprobante(contrato.getNumero());
			ctm.setSucursal(rr.getSucursalAppById(2));
			ctm.setSaldo(contrato.getImporteGs());
			rr.saveObject(ctm, user);
		}		
		
	}
}
