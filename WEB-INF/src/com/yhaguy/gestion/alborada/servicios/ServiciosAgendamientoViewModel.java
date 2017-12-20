package com.yhaguy.gestion.alborada.servicios;

import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
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
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioControlAtencion;
import com.yhaguy.util.Utiles;

public class ServiciosAgendamientoViewModel extends SimpleViewModel {
	
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
		this.n_controlatencion.setAgendamiento(true);
		this.n_controlatencion.setRealizado(false);
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
	public void realizarAtencion(@BindingParam("item") ServicioControlAtencion item) throws Exception {
		if (item.isRealizado()) return;
		RegisterDomain rr = RegisterDomain.getInstance();
		item.setRealizado(true);
		ServicioControlAtencion sc = new ServicioControlAtencion();
		sc.setAgendamiento(false);
		sc.setContrato(item.getContrato());
		sc.setDescripcion(item.getDescripcion());
		sc.setFecha(new Date());
		sc.setPaciente(item.getPaciente());
		sc.setRealizado(true);
		rr.saveObject(item, this.getLoginNombre());
		rr.saveObject(sc, this.getLoginNombre());
		BindUtils.postNotifyChange(null, null, item, "*");
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
		return rr.getServicioControlAtenciones(this.getFilterFecha(), true);
	}
	
	@DependsOn({ "filter_razonsocial", "filter_cedula" })
	public List<Cliente> getPacientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getClientes(this.filter_razonsocial, this.filter_cedula);
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
