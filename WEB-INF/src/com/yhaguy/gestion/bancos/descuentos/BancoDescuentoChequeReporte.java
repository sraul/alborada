package com.yhaguy.gestion.bancos.descuentos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.yhaguy.util.Utiles;
import com.yhaguy.util.reporte.ReporteYhaguy;

public class BancoDescuentoChequeReporte extends ReporteYhaguy {

	BancoDescuentoChequeDTO descuentoCheques;
	Misc misc = new Misc();
	String titulo = "";

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col1 = new DatosColumnas("Vto.", TIPO_STRING, 35);
	static DatosColumnas col2 = new DatosColumnas("Número", TIPO_STRING, 35);
	static DatosColumnas col3 = new DatosColumnas("Banco", TIPO_STRING, 50);	
	static DatosColumnas col4 = new DatosColumnas("Librador", TIPO_STRING, false);
	static DatosColumnas col5 = new DatosColumnas("Monto", TIPO_DOUBLE, 35, true);

	public BancoDescuentoChequeReporte(BancoDescuentoChequeDTO descuentoCheques, String titulo) {

		this.descuentoCheques = descuentoCheques;
		this.titulo = titulo;
		List<Object[]> data = new ArrayList<Object[]>();
		for (MyArray m : descuentoCheques.getCheques_()) {
			Date vto = (Date) m.getPos1();
			String nro = (String) m.getPos3();
			String librador = m.getPos4().toString().toUpperCase();
			String banco = "";
			if (m.getPos2() instanceof MyPair) {
				banco = ((MyPair) m.getPos2()).getText().toUpperCase();
			} else {
				banco = m.getPos2().toString().toUpperCase();
			}
			Object[] obj1 = new Object[] {
					misc.dateToString(vto, Misc.DD_MM_YYYY), nro,
					banco,
					Utiles.getMaxLength(librador, 25), (double) m.getPos5() };
			data.add(obj1);
		}
		this.setDatosReporte(data);
	}

	static {
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
	}

	@Override
	public void informacionReporte() {

		this.setTitulo(this.titulo);
		this.setDirectorio("");
		this.setBorrarDespuesDeVer(true);
		this.setNombreArchivo(this.titulo);
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
	}

	/**
	 * cabecera del reporte..
	 */
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		Long id = this.descuentoCheques.getId();
		Date fecha = this.descuentoCheques.getFecha();
		String sucursal = this.descuentoCheques.getSucursalApp().getText();
		double totalCheques = this.descuentoCheques.getTotalChequesDescontado();
		String observaciones = this.descuentoCheques.getObservacion();

		VerticalListBuilder out = cmp.verticalList();

		out.add(cmp.horizontalFlowList()
				.add(this.textoParValor("Número", id))
				.add(this.textoParValor("Fecha", misc.dateToString(fecha, Misc.DD_MM_YYYY))));
		out.add(cmp.horizontalFlowList().add(this.texto("")));
		out.add(cmp.horizontalFlowList().add(this.textoParValor("Total Descontado", Utiles.getNumberFormat(totalCheques) + " Gs."))
				.add(this.textoParValor("Sucursal", sucursal)));
		out.add(cmp.horizontalFlowList().add(this.texto("")));
		out.add(this.textoParValor("Observaciones", observaciones));		
		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}
}