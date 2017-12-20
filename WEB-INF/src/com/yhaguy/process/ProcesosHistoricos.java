package com.yhaguy.process;

import java.util.Date;
import java.util.List;

import com.yhaguy.domain.BancoChequeTercero;
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.HistoricoMovimientos;
import com.yhaguy.domain.NotaCredito;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.Venta;
import com.yhaguy.util.Utiles;

public class ProcesosHistoricos {

	/**
	 * procesa el historico de movimientos 
	 */
	public static void addHistoricoMovimientos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		Date desde = Utiles.getFecha("01-01-2016 00:00:00");
		
		rr.deleteAllObjects(HistoricoMovimientos.class.getName());
		
		List<Cliente> clientes = rr.getClientes();
		for (Cliente cliente : clientes) {
			double totalVCD = 0;
			double totalVCR = 0;
			double totalNCR = 0;
			double totalSAL = 0;
			double totalCHE = 0;
			List<Venta> vtasContado = rr.getVentasContado(desde, new Date(), cliente.getId());
			List<Venta> vtasCredito = rr.getVentasCredito(desde, new Date(), cliente.getId());
			List<NotaCredito> notasCredito = rr.getNotasCreditoVenta(desde, new Date(), cliente.getId());
			
			for (Venta cont : vtasContado) {
				totalVCD += cont.getTotalImporteGs();
			}
			for (Venta cre : vtasCredito) {
				totalVCR += cre.getTotalImporteGs();
			}
			for (NotaCredito nc : notasCredito) {
				totalNCR += nc.getImporteGs();
			}
			
			if (totalVCD > 0 || totalVCR > 0) {
				
				totalSAL = rr.getSaldoCtaCte(cliente.getEmpresa().getId());
				List<BancoChequeTercero> cheques = rr.getChequesPendientesClientes(cliente.getId());
				for (BancoChequeTercero cheque : cheques) {
					totalCHE += cheque.getMonto();
				}
				
				HistoricoMovimientos hist = new HistoricoMovimientos();
				hist.setIdCliente(cliente.getId());
				hist.setRuc(cliente.getRuc());
				hist.setRazonSocial(cliente.getRazonSocial());
				hist.setTotalVentasContado(totalVCD);
				hist.setTotalVentasCredito(totalVCR);
				hist.setTotalNotasDeCredito(totalNCR);
				hist.setTotalSaldoGs(totalSAL);
				hist.setTotalChequePendientesGs(totalCHE);
				rr.saveObject(hist, "process");				
				System.out.println(cliente.getRazonSocial());
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			ProcesosHistoricos.addHistoricoMovimientos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
