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
import com.yhaguy.domain.Articulo;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioPlan;
import com.yhaguy.domain.ServicioPlanDetalle;

public class ServiciosPlanesViewModel extends SimpleViewModel {
	
	private ServicioPlan n_servicio_plan = new ServicioPlan();
	private ServicioPlanDetalle n_detalle = new ServicioPlanDetalle();
	
	private ServicioPlan selectedPlan;
	
	private String filter_codigo = "";
	private String filter_descripcion = "";
	

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("selectedServicio")
	public void verItem(@BindingParam("item") ServicioPlan item, @BindingParam("parent") Component parent, 
			@BindingParam("comp") Popup comp) {
		this.selectedPlan = item;
		comp.open(parent, "after_end");
	}
	
	@Command
	@NotifyChange({ "n_servicio_plan", "n_detalle" })
	public void addItem() {
		this.n_servicio_plan.getDetalles().add(this.n_detalle);
		this.n_detalle = new ServicioPlanDetalle();
	}
	
	@Command
	@NotifyChange("*")
	public void addPlan(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.n_servicio_plan, this.getLoginNombre());
		this.n_servicio_plan = new ServicioPlan();
		comp.close();
		Clients.showNotification("REGISTRO AGREGADO..");
	}
	
	/**
	 * GETS / SETS
	 */
	
	@SuppressWarnings("unchecked")
	public List<ServicioPlan> getPlanes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<ServicioPlan> out = rr.getObjects(ServicioPlan.class.getName());
		return out;
	}
	
	@DependsOn({ "filter_codigo", "filter_descripcion" })
	public List<Articulo> getServicios() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getArticulos(this.filter_codigo, this.filter_descripcion, "");
	}

	public ServicioPlan getN_servicio_plan() {
		return n_servicio_plan;
	}

	public void setN_servicio_plan(ServicioPlan n_servicio_plan) {
		this.n_servicio_plan = n_servicio_plan;
	}

	public ServicioPlanDetalle getN_detalle() {
		return n_detalle;
	}

	public void setN_detalle(ServicioPlanDetalle n_detalle) {
		this.n_detalle = n_detalle;
	}

	public String getFilter_codigo() {
		return filter_codigo;
	}

	public void setFilter_codigo(String filter_codigo) {
		this.filter_codigo = filter_codigo;
	}

	public String getFilter_descripcion() {
		return filter_descripcion;
	}

	public void setFilter_descripcion(String filter_descripcion) {
		this.filter_descripcion = filter_descripcion;
	}

	public ServicioPlan getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(ServicioPlan selectedServicio) {
		this.selectedPlan = selectedServicio;
	}
}
