package com.yhaguy.gestion.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Window;

import com.coreweb.control.SimpleViewModel;
import com.coreweb.util.AutoNumeroControl;
import com.coreweb.util.MyArray;
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.Funcionario;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioTecnico;
import com.yhaguy.domain.ServicioTecnicoDetalle;
import com.yhaguy.domain.Venta;
import com.yhaguy.gestion.reportes.formularios.ReportesViewModel;
import com.yhaguy.util.Utiles;

public class ServicioTecnicoViewModel extends SimpleViewModel {
	
	private ServicioTecnico nvoServicio;
	private ServicioTecnico selectedServicio;
	private ServicioTecnicoDetalle nvoDetalle = new ServicioTecnicoDetalle();
	private Venta selectedVenta;
	
	private String filterRazonSocial = "";
	private String filterRuc = "";
	private String filterNumero = "";
	
	private String filterFechaDD = "";
	private String filterFechaMM = "";
	private String filterFechaAA = "";
	private String filterNumeroServicio = "";
	private String filterRazonSocial_ = "";
	private String filterReceptor = "";
	private String filterTecnico = "";
	
	private Window win;
	
	@Init(superclass = true)
	public void init() {
		try {
			this.inicializarServicio();
			this.inicializarDetalle();
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
	@NotifyChange("nvoServicio")
	public void addFactura(@BindingParam("comp") Bandbox comp) {
		this.nvoServicio.getFacturas().add(this.selectedVenta);
		comp.close();
		this.selectedVenta = null;
		this.filterNumero = "";
	}
	
	@Command
	@NotifyChange("*")
	public void addDetalle() {
		this.nvoServicio.getDetalles().add(this.nvoDetalle);
		this.inicializarDetalle();
	}
	
	@Command
	@NotifyChange("*")
	public void addServicioTecnico(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		this.nvoServicio.setNumero("SER-TEC-" + AutoNumeroControl.getAutoNumero("SER-TEC-", 7));
		rr.saveObject(this.nvoServicio, this.getLoginNombre());
		this.imprimirOrdenServicio(this.nvoServicio);
		this.inicializarServicio();
		this.inicializarDetalle();
		comp.close();
	}
	
	@Command
	@NotifyChange("*")
	public void saveServicioTecnico(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.selectedServicio, this.getLoginNombre());
		this.imprimirInformeTecnico(this.selectedServicio);		
		this.selectedServicio = null;
		comp.close();
	}
	
	@Command
	public void imprimirOrden() throws Exception {
		this.imprimirOrdenServicio(this.selectedServicio);
	}
	
	@Command
	public void informeTecnico() throws Exception {
		this.imprimirInformeTecnico(this.selectedServicio);
	}
	
	@Command
	@NotifyChange("nvoServicio")
	public void deleteItem(@BindingParam("item") ServicioTecnicoDetalle item) {
		this.nvoServicio.getDetalles().remove(item);
	}
	
	/**
	 * inicializa el servicio..
	 */
	private void inicializarServicio() throws Exception {
		this.nvoServicio = new ServicioTecnico();
		this.nvoServicio.setFecha(new Date());
		this.nvoServicio.setNumero("SER-TEC-" + AutoNumeroControl.getAutoNumero("SER-TEC-", 7, true));
	}
	
	/**
	 * inicializa el detalle del servicio..
	 */
	private void inicializarDetalle() {
		this.nvoDetalle = new ServicioTecnicoDetalle();
		this.nvoDetalle.setEstado("...");
		this.nvoDetalle.setVerifica_carga("...");
		this.nvoDetalle.setVerifica_borne("...");
		this.nvoDetalle.setVerifica_celda("...");
		this.nvoDetalle.setVerifica_conexion("...");
		this.nvoDetalle.setObservacion("");
	}
	
	/**
	 * Despliega el Reporte de Orden de Servicio Tecnico..
	 */
	private void imprimirOrdenServicio(ServicioTecnico orden) throws Exception {		
		String source = ReportesViewModel.SOURCE_ORDEN_SERV_TEC;
		Map<String, Object> params = new HashMap<String, Object>();
		JRDataSource dataSource = new ServicioTecnicoDataSource(orden);
		params.put("Fecha", Utiles.getDateToString(orden.getFecha(), Utiles.DD_MM_YYYY));
		params.put("NroReclamo", orden.getNumero());
		params.put("Receptor", orden.getReceptor());
		params.put("Cliente", orden.getCliente().getRazonSocial());
		params.put("Usuario", getUs().getNombre());
		this.imprimirComprobante(source, params, dataSource, ReportesViewModel.FORMAT_PDF);
	}
	
	/**
	 * Despliega el Reporte de Informe Tecnico..
	 */
	private void imprimirInformeTecnico(ServicioTecnico orden) throws Exception {		
		String source = ReportesViewModel.SOURCE_INFORME_TECNICO;
		Map<String, Object> params = new HashMap<String, Object>();
		JRDataSource dataSource = new InformeTecnicoDataSource(orden);
		params.put("Fecha", Utiles.getDateToString(orden.getFecha(), Utiles.DD_MM_YYYY));
		params.put("NroReclamo", orden.getNumero());
		params.put("Receptor", orden.getReceptor());
		params.put("Cliente", orden.getCliente().getRazonSocial());
		params.put("Usuario", getUs().getNombre());
		this.imprimirComprobante(source, params, dataSource, ReportesViewModel.FORMAT_PDF);
	}
	
	/**
	 * Despliega el comprobante en un pdf para su impresion..
	 */
	private void imprimirComprobante(String source,
			Map<String, Object> parametros, JRDataSource dataSource, Object[] format) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", source);
		params.put("parametros", parametros);
		params.put("dataSource", dataSource);
		params.put("format", format);

		this.win = (Window) Executions.createComponents(
				ReportesViewModel.ZUL_REPORTES, this.mainComponent, params);
		this.win.doModal();
	}

	
	/**
	 * DataSource del Servicio Tecnico..
	 */
	class ServicioTecnicoDataSource implements JRDataSource {

		List<ServicioTecnicoDetalle> detalle = new ArrayList<ServicioTecnicoDetalle>();
		List<MyArray> dets = new ArrayList<MyArray>();

		public ServicioTecnicoDataSource(ServicioTecnico orden) {
			for (Venta fac : orden.getFacturas()) {
				this.dets.add(new MyArray("FACTURAS", Utiles.getDateToString(
						fac.getFecha(), Utiles.DD_MM_YYYY)
						+ " - "
						+ fac.getTipoMovimiento().getDescripcion()
						+ " - "
						+ fac.getNumero()));
			}
			for (ServicioTecnicoDetalle item : orden.getDetalles()) {
				this.dets.add(new MyArray("BATERÍAS", 
						item.getArticulo().getCodigoInterno() + " - " +
						item.getArticulo().getDescripcion()));
			}
			for (ServicioTecnicoDetalle item : orden.getDetalles()) {
				this.dets.add(new MyArray("ESTADO A SIMPLE VISTA", 
						item.getArticulo().getCodigoInterno() + " - ESTADO: " +
						item.getEstado() + " - CARGA: " + item.getVerifica_carga() 
						+ " - BORNE: " + item.getVerifica_borne()
						+ " - CELDA: " + item.getVerifica_celda()
						+ " - CONEXION: " + item.getVerifica_conexion()));
			}
		}

		private int index = -1;

		@Override
		public Object getFieldValue(JRField field) throws JRException {
			Object value = null;
			String fieldName = field.getName();
			MyArray item = this.dets.get(index);
			if ("TituloDetalle".equals(fieldName)) {
				value = item.getPos1();
			} else if ("Descripcion".equals(fieldName)) {
				value = item.getPos2();
			}
			return value;
		}

		@Override
		public boolean next() throws JRException {
			if (index < dets.size() - 1) {
				index++;
				return true;
			}
			return false;
		}
	}
	
	/**
	 * DataSource del Informe Tecnico..
	 */
	class InformeTecnicoDataSource implements JRDataSource {

		List<ServicioTecnicoDetalle> detalle = new ArrayList<ServicioTecnicoDetalle>();
		List<MyArray> dets = new ArrayList<MyArray>();

		public InformeTecnicoDataSource(ServicioTecnico orden) {
			for (ServicioTecnicoDetalle item : orden.getDetalles()) {
				if (item.getDiagnostico() != null) {
					this.dets.add(new MyArray("CÓDIGO - DIAGNÓSTICO", 
							item.getArticulo().getCodigoInterno() + " - " +
							item.getDiagnostico().toUpperCase(), 
							"CORRESPONDE EL CAMBIO: " + (item.isVerifica_reposicion()? "SI" : "NO")));
				}
			}
		}

		private int index = -1;

		@Override
		public Object getFieldValue(JRField field) throws JRException {
			Object value = null;
			String fieldName = field.getName();
			MyArray item = this.dets.get(index);
			if ("TituloDetalle".equals(fieldName)) {
				value = item.getPos1();
			} else if ("Descripcion".equals(fieldName)) {
				value = item.getPos2();
			} else if ("Reposicion".equals(fieldName)) {
				value = item.getPos3();
			}
			return value;
		}

		@Override
		public boolean next() throws JRException {
			if (index < dets.size() - 1) {
				index++;
				return true;
			}
			return false;
		}
	}

	/**
	 * GETS / SETS
	 */
	
	@DependsOn({ "filterFechaDD", "filterFechaMM", "filterFechaAA",
			"filterNumeroServicio", "filterRazonSocial_", "filterReceptor", "filterTecnico" })
	public List<ServicioTecnico> getServiciosTecnicos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getServiciosTecnicos(this.getFilterFecha(), this.filterNumeroServicio, 
				this.filterRazonSocial_, this.filterReceptor, this.filterTecnico);
	}
	
	@DependsOn({ "filterRazonSocial", "filterRuc" })
	public List<Cliente> getClientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getClientes(this.filterRazonSocial, this.filterRuc);
	}
	
	@DependsOn({ "nvoServicio.cliente", "filterNumero" })
	public List<Venta> getFacturas() throws Exception {
		if (this.nvoServicio.getCliente() == null || this.filterNumero.isEmpty()) {
			return new ArrayList<Venta>();
		}
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getVentas(this.filterNumero, this.nvoServicio.getCliente().getId());
	}
	
	@DependsOn("nvoServicio")
	public boolean isDetalleVisible() {
		return this.nvoServicio.getTecnico() != null
				&& this.nvoServicio.getReceptor() != null
				&& this.nvoServicio.getCliente() != null
				&& this.nvoServicio.getFacturas().size() > 0;
	}
	
	@DependsOn("nvoDetalle.articulo")
	public boolean isAddDetalleDisabled() {
		return this.nvoDetalle.getArticulo() == null;
	}
	
	/**
	 * @return los preparadores de pedido..
	 */
	public List<String> getTecnicos() throws Exception {
		List<String> out = new ArrayList<String>();
		RegisterDomain rr = RegisterDomain.getInstance();
		for (Funcionario func : rr.getFuncionariosDeposito()) {
			out.add(func.getRazonSocial().toUpperCase());
		}
		return out;
	}
	
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
	
	public ServicioTecnico getNvoServicio() {
		return nvoServicio;
	}

	public void setNvoServicio(ServicioTecnico nvoServicio) {
		this.nvoServicio = nvoServicio;
	}

	public String getFilterRazonSocial() {
		return filterRazonSocial;
	}

	public void setFilterRazonSocial(String filterRazonSocial) {
		this.filterRazonSocial = filterRazonSocial;
	}

	public String getFilterRuc() {
		return filterRuc;
	}

	public void setFilterRuc(String filterRuc) {
		this.filterRuc = filterRuc;
	}

	public String getFilterNumero() {
		return filterNumero;
	}

	public void setFilterNumero(String filterNumero) {
		this.filterNumero = filterNumero;
	}

	public Venta getSelectedVenta() {
		return selectedVenta;
	}

	public void setSelectedVenta(Venta selectedVenta) {
		this.selectedVenta = selectedVenta;
	}

	public ServicioTecnicoDetalle getNvoDetalle() {
		return nvoDetalle;
	}

	public void setNvoDetalle(ServicioTecnicoDetalle nvoDetalle) {
		this.nvoDetalle = nvoDetalle;
	}

	public ServicioTecnico getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(ServicioTecnico selectedServicio) {
		this.selectedServicio = selectedServicio;
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

	public String getFilterNumeroServicio() {
		return filterNumeroServicio;
	}

	public void setFilterNumeroServicio(String filterNumeroServicio) {
		this.filterNumeroServicio = filterNumeroServicio;
	}

	public String getFilterRazonSocial_() {
		return filterRazonSocial_;
	}

	public void setFilterRazonSocial_(String filterRazonSocial_) {
		this.filterRazonSocial_ = filterRazonSocial_;
	}

	public String getFilterReceptor() {
		return filterReceptor;
	}

	public void setFilterReceptor(String filterReceptor) {
		this.filterReceptor = filterReceptor;
	}

	public String getFilterTecnico() {
		return filterTecnico;
	}

	public void setFilterTecnico(String filterTecnico) {
		this.filterTecnico = filterTecnico;
	}
}
