package com.yhaguy.gestion.alborada.servicios;

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
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.Funcionario;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioContrato;
import com.yhaguy.domain.ServicioPlan;
import com.yhaguy.gestion.comun.ControlCuentaCorriente;

public class ServiciosContratosViewModel extends SimpleViewModel {

	private ServicioContrato selectedContrato;
	private ServicioContrato n_contrato = new ServicioContrato();
	private Cliente selectedAdherente;
	
	private String filter_fecha = "";
	private String filter_numero = "";
	private String filter_titular = "";
	private String filter_plan = "";
	private String filter_asesor = "";
	private String filter_adherentes = "";
	
	private String filter_cedula_ = "";
	private String filter_razonsocial_ = "";
	
	@Init(superclass = true)
	public void init() {
		try {
			this.n_contrato.setNumero(AutoNumeroControl.getAutoNumero("CONTRATO", 5, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange({ "n_contrato", "selectedAdherente" })
	public void addAdherente() {
		if(this.selectedAdherente == null) return;
		//this.n_contrato.getAdherentes().add(this.selectedAdherente);
		this.selectedAdherente = null;
	}
	
	@Command
	@NotifyChange("*")
	public void addContrato(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		this.n_contrato.setNumero(AutoNumeroControl.getAutoNumero("CONTRATO", 5));
		rr.saveObject(this.n_contrato, this.getLoginNombre());
		ControlCuentaCorriente.addContrato(this.n_contrato, this.n_contrato.getCuotas(), this.getLoginNombre());
		this.n_contrato = new ServicioContrato();
		comp.close();
		Clients.showNotification("REGISTRO AGREGADO..");
	}
	
	@Command
	@NotifyChange("selectedContrato")
	public void verAdherentes(@BindingParam("item") ServicioContrato item, @BindingParam("parent") Component parent,
			@BindingParam("comp") Popup comp) {
		this.selectedContrato = item;
		comp.open(parent, "after_end");
	}
	
	/**
	 * GETS / SETS
	 */
	
	@SuppressWarnings("unchecked")
	public List<ServicioContrato> getContratos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<ServicioContrato> out = rr.getObjects(ServicioContrato.class.getName());
		return out;
	}
	
	@DependsOn({ "filter_razonsocial_", "filter_cedula_" })
	public List<Cliente> getPacientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getClientes(this.filter_razonsocial_, this.filter_cedula_);
	}
	
	/**
	 * @return los asesores..
	 */
	public List<Funcionario> getAsesores() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getVendedores();
	}
	
	@SuppressWarnings("unchecked")
	public List<ServicioPlan> getPlanes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getObjects(ServicioPlan.class.getName());
	}

	public ServicioContrato getSelectedContrato() {
		return selectedContrato;
	}

	public void setSelectedContrato(ServicioContrato selectedContrato) {
		this.selectedContrato = selectedContrato;
	}

	public ServicioContrato getN_contrato() {
		return n_contrato;
	}

	public void setN_contrato(ServicioContrato n_contrato) {
		this.n_contrato = n_contrato;
	}

	public String getFilter_fecha() {
		return filter_fecha;
	}

	public void setFilter_fecha(String filter_fecha) {
		this.filter_fecha = filter_fecha;
	}

	public String getFilter_numero() {
		return filter_numero;
	}

	public void setFilter_numero(String filter_numero) {
		this.filter_numero = filter_numero;
	}

	public String getFilter_titular() {
		return filter_titular;
	}

	public void setFilter_titular(String filter_titular) {
		this.filter_titular = filter_titular;
	}

	public String getFilter_plan() {
		return filter_plan;
	}

	public void setFilter_plan(String filter_plan) {
		this.filter_plan = filter_plan;
	}

	public String getFilter_asesor() {
		return filter_asesor;
	}

	public void setFilter_asesor(String filter_asesor) {
		this.filter_asesor = filter_asesor;
	}

	public String getFilter_adherentes() {
		return filter_adherentes;
	}

	public void setFilter_adherentes(String filter_adherentes) {
		this.filter_adherentes = filter_adherentes;
	}

	public String getFilter_cedula_() {
		return filter_cedula_;
	}

	public void setFilter_cedula_(String filter_cedula_) {
		this.filter_cedula_ = filter_cedula_;
	}

	public String getFilter_razonsocial_() {
		return filter_razonsocial_;
	}

	public void setFilter_razonsocial_(String filter_razonsocial_) {
		this.filter_razonsocial_ = filter_razonsocial_;
	}

	public Cliente getSelectedAdherente() {
		return selectedAdherente;
	}

	public void setSelectedAdherente(Cliente n_adherente) {
		this.selectedAdherente = n_adherente;
	}
}
