package com.yhaguy.gestion.alborada.comisiones;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;

import com.coreweb.componente.ViewPdf;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.extras.reporte.DatosColumnas;
import com.yhaguy.domain.Comisiones;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.util.reporte.ReporteYhaguy;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

public class ComisionesViewModel extends SimpleViewModel {
	
	private String filter_nombre = "";
	private String filter_cedula = "";
	private String filter_cuota = "";
	private String filter_vencimiento = "";
	private String filter_vendedor = "";
	private long filter_dias = 0;
	
	private double totalImporte = 0;

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	public void reporte() throws Exception {
		List<Object[]> data = new ArrayList<Object[]>();
		for (Comisiones item : this.getComisiones()) {
			Object[] obj = new Object[] { item.getNombre_apellido(), item.getCedula(), item.getNro_cuota(),
					item.getFecha_vencimiento_(), item.getImporte(), item.getVendedor(), item.getCelular(),
					item.getCelular_2(), item.getTel_particular(), item.getTel_particular_2(), item.getTel_laboral(),
					item.getTel_laboral_2() };
			data.add(obj);
		}

		ReporteComisiones rep = new ReporteComisiones();
		rep.setDatosReporte(data);
		rep.setApaisada();

		ViewPdf vp = new ViewPdf();
		vp.setBotonImprimir(false);
		vp.setBotonCancelar(false);
		vp.showReporte(rep, this);
	}
	
	
	/**
	 * GETS / SETS
	 */
	
	@DependsOn({ "filter_nombre", "filter_cedula", "filter_cuota", "filter_vencimiento", "filter_vendedor", "filter_dias" })
	public List<Comisiones> getComisiones() throws Exception {
		List<Comisiones> list = new ArrayList<Comisiones>();
		this.totalImporte = 0;
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Comisiones> out = rr.getComisiones(this.filter_nombre, this.filter_cedula, this.filter_cuota, this.filter_vencimiento, this.filter_vendedor, "", "", "");
		for (Comisiones item : out) {
			if (item.getDiasVencimiento() > this.filter_dias) {
				this.totalImporte += item.getImporte();
				list.add(item);
			}			
		}
		BindUtils.postNotifyChange(null, null, this, "totalImporte");
		return list;
	}

	public String getFilter_nombre() {
		return filter_nombre;
	}

	public void setFilter_nombre(String filter_nombre) {
		this.filter_nombre = filter_nombre;
	}

	public String getFilter_cedula() {
		return filter_cedula;
	}

	public void setFilter_cedula(String filter_cedula) {
		this.filter_cedula = filter_cedula;
	}

	public String getFilter_cuota() {
		return filter_cuota;
	}

	public void setFilter_cuota(String filter_cuota) {
		this.filter_cuota = filter_cuota;
	}

	public String getFilter_vencimiento() {
		return filter_vencimiento;
	}

	public void setFilter_vencimiento(String filter_vencimiento) {
		this.filter_vencimiento = filter_vencimiento;
	}

	public String getFilter_vendedor() {
		return filter_vendedor;
	}

	public void setFilter_vendedor(String filter_vendedor) {
		this.filter_vendedor = filter_vendedor;
	}

	public double getTotalImporte() {
		return totalImporte;
	}

	public void setTotalImporte(double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public long getFilter_dias() {
		return filter_dias;
	}

	public void setFilter_dias(long filter_dias) {
		this.filter_dias = filter_dias;
	}
}

/**
 * Reporte de comisiones..
 */
class ReporteComisiones extends ReporteYhaguy {

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col1 = new DatosColumnas("Usuario", TIPO_STRING);
	static DatosColumnas col2 = new DatosColumnas("Cedula", TIPO_STRING);
	static DatosColumnas col3 = new DatosColumnas("Cuota", TIPO_STRING);
	static DatosColumnas col4 = new DatosColumnas("Vencimiento", TIPO_STRING);
	static DatosColumnas col5 = new DatosColumnas("Importe", TIPO_DOUBLE, true);
	static DatosColumnas col6 = new DatosColumnas("Vendedor", TIPO_STRING);
	static DatosColumnas col7 = new DatosColumnas("Celular", TIPO_STRING);
	static DatosColumnas col8 = new DatosColumnas("Celular 2", TIPO_STRING);
	static DatosColumnas col9 = new DatosColumnas("Particular", TIPO_STRING);
	static DatosColumnas col10 = new DatosColumnas("Particular 2", TIPO_STRING);
	static DatosColumnas col11 = new DatosColumnas("Laboral", TIPO_STRING);
	static DatosColumnas col12 = new DatosColumnas("Laboral 2", TIPO_STRING);

	public ReporteComisiones() {	
	}

	static {
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
	}

	@Override
	public void informacionReporte() {
		this.setTitulo("Comisiones");
		this.setDirectorio("ventas");
		this.setNombreArchivo("comision-");
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
	}

	/**
	 * cabecera del reporte..
	 */
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {
		VerticalListBuilder out = cmp.verticalList();
		out.add(cmp.horizontalFlowList().add(this.texto("")));
		return out;
	}
}
