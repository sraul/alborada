package com.yhaguy.gestion.contabilidad;

import java.util.Date;
import java.util.List;

import com.coreweb.util.AutoNumeroControl;
import com.yhaguy.domain.ContableAsiento;
import com.yhaguy.domain.ContableAsientoDetalle;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.Venta;

public class ContableControl {

	/**
	 * Generacion de asientos de venta credito..
	 */
	public static void generarAsientosVentaCredito(Date desde, Date hasta,
			String user) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Venta> vtas = rr.getVentasCredito(desde, hasta, 0);
		for (Venta venta : vtas) {
			if (!venta.isAnulado()) {
				ContableAsiento asiento = new ContableAsiento();
				asiento.setFecha(venta.getFecha());
				asiento.setDescripcion("SEGUN FAC. VTA. CREDITO NRO. " + venta.getNumero());
				asiento.setNumero(AutoNumeroControl.getAutoNumero("ASIENTOS", 7));

				ContableAsientoDetalle det1 = new ContableAsientoDetalle();
				det1.setDescripcion("Generado..");
				det1.setCuenta(rr.getCuentaContableByAlias_("CT-CL-VA"));
				det1.setDebe(venta.getTotalImporteGs());
				det1.setHaber(0.0);

				ContableAsientoDetalle det2 = new ContableAsientoDetalle();
				det2.setDescripcion("Generado..");
				det2.setCuenta(rr.getCuentaContableByAlias_("CT-IVA-VENTAS"));
				det2.setDebe(0.0);
				det2.setHaber(venta.getTotalIva10());

				ContableAsientoDetalle det3 = new ContableAsientoDetalle();
				det3.setDescripcion("Generado..");
				det3.setCuenta(rr.getCuentaContableByAlias_("CT-MERCADERIA-GRABADA"));
				det3.setDebe(0.0);
				det3.setHaber(venta.getTotalImporteGs() - venta.getTotalIva10());

				asiento.getDetalles().add(det1);
				asiento.getDetalles().add(det2);
				asiento.getDetalles().add(det3);

				rr.saveObject(asiento, user);
			}
		}
	}
	
}
