package com.yhaguy.gestion.alborada.pacientes;

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

import com.coreweb.control.SimpleViewModel;
import com.coreweb.util.AutoNumeroControl;
import com.yhaguy.Configuracion;
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.Cobranza;
import com.yhaguy.domain.CtaCteEmpresaMovimiento;
import com.yhaguy.domain.Empresa;
import com.yhaguy.domain.Recibo;
import com.yhaguy.domain.ReciboDetalle;
import com.yhaguy.domain.ReciboFormaPago;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioContrato;
import com.yhaguy.domain.ServicioControlAtencion;
import com.yhaguy.domain.ServicioFicha;

public class PacientesViewModel extends SimpleViewModel {
	
	static final String FILTRO_RECIBOS = "RECIBOS";
	
	static final String TODOS = "Todos";
	static final String PENDIENTES = "Pendientes";
	static final String VENCIDOS = "Vencidos";
	
	private Empresa n_empresa = new Empresa();
	private Cliente n_paciente = new Cliente();
	private Cliente selectedPaciente;
	
	private ServicioContrato selectedContrato;
	private ServicioFicha selectedFicha;
	
	private String filter_cedula = "";
	private String filter_nombre = "";
	private String filter_telefono = "";
	private String filter_direccion = "";
	
	private String filterFechaDD = "";
	private String filterFechaMM = "";
	private String filterFechaAA = "";
	
	private String selectedFilter = TODOS;
	
	private Recibo n_recibo;
	private List<ReciboDetalle> selectedDetalles;

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("*")
	public void addCliente(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.n_empresa, this.getLoginNombre());
		this.n_paciente.setEmpresa(this.n_empresa);
		rr.saveObject(this.n_paciente, this.getLoginNombre());
		this.n_paciente = new Cliente();
		this.n_empresa = new Empresa();
		comp.close();
		Clients.showNotification("REGISTRO AGREGADO..");
	}
	
	@Command
	@NotifyChange({ "n_recibo", "saldos" })
	public void newRecibo(@BindingParam("comp") Popup comp, @BindingParam("parent") Component parent) throws Exception {
		this.selectedDetalles = null;
		RegisterDomain rr = RegisterDomain.getInstance();
		this.n_recibo = new Recibo();
		this.n_recibo.setCliente(this.selectedPaciente);
		this.n_recibo.setTipoMovimiento(rr.getTipoMovimientoBySigla(Configuracion.SIGLA_TM_RECIBO_COBRO));
		this.n_recibo.setFechaEmision(new Date());
		this.n_recibo.setNumero(AutoNumeroControl.getAutoNumero(FILTRO_RECIBOS, 7, true));
		comp.open(parent, "after_start");
	}
	
	@Command
	@NotifyChange("n_recibo")
	public void addFormaPago() throws Exception {
		double importe = 0;
		for (ReciboDetalle det : this.selectedDetalles) {
			importe += det.getMontoGs();
		}
		RegisterDomain rr = RegisterDomain.getInstance();
		ReciboFormaPago fp = new ReciboFormaPago();
		fp.setDescripcion("Efectivo Gs.");
		fp.setMoneda(rr.getTipoPorSigla(Configuracion.SIGLA_MONEDA_GUARANI));
		fp.setMontoGs(importe);
		fp.setTipo(rr.getTipoPorSigla(Configuracion.SIGLA_FORMA_PAGO_EFECTIVO));
		this.n_recibo.getFormasPago().add(fp);
	}
	
	@Command
	@NotifyChange("*")
	public void saveRecibo(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		this.n_recibo.getDetalles().addAll(this.selectedDetalles);
		rr.saveObject(this.n_recibo, this.getLoginNombre());
		this.n_recibo = null;
		this.selectedDetalles = null;
		comp.close();
		Clients.showNotification("REGISTRO GUARDADO..");
	}
	
	@Command
	@NotifyChange("selectedFilter")
	public void selectFilter(@BindingParam("filter") int filter) {
		if (filter == 1) {
			this.selectedFilter = TODOS;
		} else if (filter == 2) {
			this.selectedFilter = PENDIENTES;
		} else {
			this.selectedFilter = VENCIDOS;
		}
	}
	
	/**
	 * GETS / SETS
	 */
	@DependsOn({ "filter_cedula", "filter_nombre", "filter_telefono", "filter_direccion" })
	public List<Cliente> getPacientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();		
		return rr.getClientes(this.filter_cedula, this.filter_nombre, this.filter_telefono, this.filter_direccion);
	}
	
	@DependsOn("selectedPaciente")
	public List<ServicioContrato> getContratos() throws Exception {
		if(this.selectedPaciente == null) return new ArrayList<ServicioContrato>();
		RegisterDomain rr = RegisterDomain.getInstance();
		List<ServicioContrato> out = rr.getServicioContratos(this.selectedPaciente.getId());
		return out;
	}
	
	@DependsOn("selectedPaciente")
	public List<Cobranza> getCobranzas() throws Exception {
		if(this.selectedPaciente == null) return new ArrayList<Cobranza>();
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Cobranza> out = rr.getCobranzas(this.selectedPaciente.getId());
		return out;
	}
	
	@DependsOn("selectedContrato")
	public List<CtaCteEmpresaMovimiento> getCuotas() throws Exception {
		if(this.selectedContrato == null) return new ArrayList<CtaCteEmpresaMovimiento>();
		RegisterDomain rr = RegisterDomain.getInstance();
		List<CtaCteEmpresaMovimiento> out = rr.getServicioContratoCuotas(this.selectedContrato.getId());
		return out;
	}
	
	@DependsOn({ "filterFechaAA", "filterFechaMM", "filterFechaDD" })
	public List<ServicioControlAtencion> getControlAtenciones() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getServicioControlAtenciones(this.getFilterFecha(), false);
	}
	
	@DependsOn({ "filterFechaAA", "filterFechaMM", "filterFechaDD" })
	public List<ServicioControlAtencion> getAgendamientos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getServicioControlAtenciones(this.getFilterFecha(), true);
	}
	
	@DependsOn("n_recibo.cliente")
	public List<ReciboDetalle> getSaldos() throws Exception {
		List<ReciboDetalle> out = new ArrayList<ReciboDetalle>();
		if(this.n_recibo == null || this.n_recibo.getCliente() == null) return out;
		RegisterDomain rr = RegisterDomain.getInstance();
		List<CtaCteEmpresaMovimiento> cuotas = rr.getMovimientosConSaldo(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE, this.n_recibo.getCliente().getIdEmpresa());
		for (CtaCteEmpresaMovimiento cuota : cuotas) {
			ReciboDetalle det = new ReciboDetalle();
			det.setMovimiento(cuota);
			det.setMontoGs(cuota.getSaldo());
			out.add(det);
		}
		return out;
	}
	
	@DependsOn("selectedPaciente")
	public List<CtaCteEmpresaMovimiento> getMovimientos() throws Exception {
		if(this.selectedPaciente == null) return new ArrayList<CtaCteEmpresaMovimiento>();
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getMovimientosConSaldo(Configuracion.SIGLA_CTA_CTE_CARACTER_MOV_CLIENTE, this.selectedPaciente.getIdEmpresa());
	}
	
	@DependsOn("selectedPaciente")
	public List<Recibo> getRecibos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getRecibos("");
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

	public String getFilter_cedula() {
		return filter_cedula;
	}

	public void setFilter_cedula(String filter_cedula) {
		this.filter_cedula = filter_cedula;
	}

	public String getFilter_nombre() {
		return filter_nombre;
	}

	public void setFilter_nombre(String filter_nombre) {
		this.filter_nombre = filter_nombre;
	}

	public String getFilter_telefono() {
		return filter_telefono;
	}

	public void setFilter_telefono(String filter_telefono) {
		this.filter_telefono = filter_telefono;
	}

	public String getFilter_direccion() {
		return filter_direccion;
	}

	public void setFilter_direccion(String filter_direccion) {
		this.filter_direccion = filter_direccion;
	}

	public Cliente getN_paciente() {
		return n_paciente;
	}

	public void setN_paciente(Cliente n_cliente) {
		this.n_paciente = n_cliente;
	}

	public Empresa getN_empresa() {
		return n_empresa;
	}

	public void setN_empresa(Empresa n_empresa) {
		this.n_empresa = n_empresa;
	}

	public Cliente getSelectedPaciente() {
		return selectedPaciente;
	}

	public void setSelectedPaciente(Cliente selectedPaciente) {
		this.selectedPaciente = selectedPaciente;
	}

	public ServicioContrato getSelectedContrato() {
		return selectedContrato;
	}

	public void setSelectedContrato(ServicioContrato selectedContrato) {
		this.selectedContrato = selectedContrato;
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

	public Recibo getN_recibo() {
		return n_recibo;
	}

	public void setN_recibo(Recibo n_recibo) {
		this.n_recibo = n_recibo;
	}

	public List<ReciboDetalle> getSelectedDetalles() {
		return selectedDetalles;
	}

	public void setSelectedDetalles(List<ReciboDetalle> selectedDetalles) {
		this.selectedDetalles = selectedDetalles;
	}

	public String getSelectedFilter() {
		return selectedFilter;
	}

	public void setSelectedFilter(String selectedFilter) {
		this.selectedFilter = selectedFilter;
	}

	public ServicioFicha getSelectedFicha() {
		return selectedFicha;
	}

	public void setSelectedFicha(ServicioFicha selectedFicha) {
		this.selectedFicha = selectedFicha;
	}
}
