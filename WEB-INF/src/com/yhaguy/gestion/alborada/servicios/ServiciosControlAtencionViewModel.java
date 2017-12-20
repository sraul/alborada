package com.yhaguy.gestion.alborada.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Popup;

import com.coreweb.componente.ViewPdf;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.extras.reporte.DatosColumnas;
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioControlAtencion;
import com.yhaguy.util.Utiles;
import com.yhaguy.util.reporte.ReporteYhaguy;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

public class ServiciosControlAtencionViewModel extends SimpleViewModel {
	
	private ServicioControlAtencion n_controlatencion;
	
	private String filter_razonsocial = "";
	private String filter_cedula = "";

	private String filterFechaDD = "";
	private String filterFechaMM = "";
	private String filterFechaAA = "";
	
	@Init(superclass = true)
	public void init() {
		try {
			this.filterFechaDD = Utiles.getDateToString(new Date(), "dd");
			this.filterFechaMM = "" + Utiles.getNumeroMesCorriente();
			this.filterFechaAA = Utiles.getAnhoActual();
			if (this.filterFechaMM.length() == 1) {
				this.filterFechaMM = "0" + this.filterFechaMM;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("n_controlatencion")
	public void newControlAtencion(@BindingParam("comp") Popup comp, @BindingParam("parent") Component parent) {
		this.n_controlatencion = new ServicioControlAtencion();
		this.n_controlatencion.setFecha(new Date());
		this.n_controlatencion.setAgendamiento(false);
		this.n_controlatencion.setRealizado(true);
		comp.open(parent, "after_start");
	}
	
	@Command
	@NotifyChange("*")
	public void addControlAtencion(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		this.n_controlatencion.setDescripcion(this.n_controlatencion.getDescripcion().toUpperCase());
		rr.saveObject(this.n_controlatencion, this.getLoginNombre());
		this.n_controlatencion = null;
		Clients.showNotification("REGISTRO AGREGADO..");
		comp.close();
	}
	
	@Command
	public void imprimir() throws Exception {
		this.reporteAtencionDiaria();
	}
	
	/**
	 * reporte de atencion diaria..
	 */
	private void reporteAtencionDiaria() throws Exception {
		List<Object[]> data = new ArrayList<Object[]>();
		for (ServicioControlAtencion item : this.getControlAtenciones()) {
			Object[] obj = new Object[] { Utiles.getDateToString(item.getFecha(), "dd-MM-yy hh:mm"),
					item.getPaciente().getRazonSocial(),
					item.getContrato().getPlan().getDescripcion().substring(0, 3), item.getDenominacion().substring(0, 3), item.getDescripcion(),
					item.getMes_pago(), item.getForma_pago().substring(0, 3), item.getImporte_pago()};
			data.add(obj);
		}

		ReporteAtencion rep = new ReporteAtencion();
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
	
	/**
	 * @return el filtro de fecha..
	 */
	private String getFilterFecha() {
		if (this.filterFechaAA.isEmpty() && this.filterFechaDD.isEmpty() && this.filterFechaMM.isEmpty())
			return "";
		if (this.filterFechaAA.isEmpty())
			return this.filterFechaMM + "-" + this.filterFechaDD;
		if (this.filterFechaMM.isEmpty())
			return this.filterFechaAA;
		if (this.filterFechaMM.isEmpty() && this.filterFechaDD.isEmpty())
			return this.filterFechaAA;
		return this.filterFechaAA + "-" + this.filterFechaMM + "-" + this.filterFechaDD;
	}
	
	@DependsOn({ "filterFechaAA", "filterFechaMM", "filterFechaDD" })
	public List<ServicioControlAtencion> getControlAtenciones() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getServicioControlAtenciones(this.getFilterFecha(), false);
	}
	
	@DependsOn({ "filter_razonsocial", "filter_cedula" })
	public List<Cliente> getPacientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getClientes(this.filter_razonsocial, this.filter_cedula);
	}
	
	/**
	 * @return los meses..
	 */
	public List<String> getMeses() {
		return Utiles.getMeses_();
	}
	
	/**
	 * @return las formas de pago..
	 */
	public List<String> getFormasPago() {
		List<String> out = new ArrayList<String>();
		out.add("EFECTIVO");
		out.add("TARJ.CREDITO");
		return out;
	}
	
	public ServicioControlAtencion getN_controlatencion() {
		return n_controlatencion;
	}

	public void setN_controlatencion(ServicioControlAtencion n_controlatencion) {
		this.n_controlatencion = n_controlatencion;
	}

	public String getFilter_razonsocial() {
		return filter_razonsocial;
	}

	public void setFilter_razonsocial(String filter_razonsocial) {
		this.filter_razonsocial = filter_razonsocial;
	}

	public String getFilter_cedula() {
		return filter_cedula;
	}

	public void setFilter_cedula(String filter_cedula) {
		this.filter_cedula = filter_cedula;
	}

	public String getFilterFechaDD() {
		return filterFechaDD;
	}

	public void setFilterFechaDD(String filterFechaDD) {
		this.filterFechaDD = filterFechaDD;
	}

	public String getFilterFechaMM() {
		return filterFechaMM;
	}

	public void setFilterFechaMM(String filterFechaMM) {
		this.filterFechaMM = filterFechaMM;
	}

	public String getFilterFechaAA() {
		return filterFechaAA;
	}

	public void setFilterFechaAA(String filterFechaAA) {
		this.filterFechaAA = filterFechaAA;
	}
}

/**
 * Reporte de atencion diaria..
 */
class ReporteAtencion extends ReporteYhaguy {

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col1 = new DatosColumnas("Fecha", TIPO_STRING, 65);
	static DatosColumnas col2 = new DatosColumnas("Usuario", TIPO_STRING);
	static DatosColumnas col3 = new DatosColumnas("Plan", TIPO_STRING, 40);
	static DatosColumnas col4 = new DatosColumnas("Tit./Adh", TIPO_STRING, 40);
	static DatosColumnas col5 = new DatosColumnas("Descripcion", TIPO_STRING);
	static DatosColumnas col6 = new DatosColumnas("Mes Pago", TIPO_STRING);
	static DatosColumnas col7 = new DatosColumnas("F.Pago", TIPO_STRING, 40);
	static DatosColumnas col8 = new DatosColumnas("Importe Gs.", TIPO_DOUBLE, true);

	public ReporteAtencion() {	
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
	}

	@Override
	public void informacionReporte() {
		this.setTitulo("Atencion Diaria");
		this.setDirectorio("ventas");
		this.setNombreArchivo("atencion-");
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
