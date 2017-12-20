package com.yhaguy.gestion.alborada.servicios;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Popup;

import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.Tipo;
import com.yhaguy.Configuracion;
import com.yhaguy.domain.Articulo;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.TipoMovimiento;

public class ServiciosViewModel extends SimpleViewModel {
	
	private String filter_codigo = "";
	private String filter_descripcion = "";
	private String filter_especialidad = "";
	
	private Articulo n_servicio = new Articulo();

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("*")
	public void addServicio(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.n_servicio, this.getLoginNombre());
		this.n_servicio = new Articulo();
		comp.close();
		Clients.showNotification("REGISTRO AGREGADO..");
	}
	
	/**
	 * GETS / SETS
	 */
	@DependsOn({ "filter_codigo", "filter_descripcion", "filter_especialidad" })
	public List<Articulo> getServicios() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getArticulos(this.filter_codigo, this.filter_descripcion, this.filter_especialidad);
	}
	
	/**
	 * @return las especialidades..
	 */
	public List<Tipo> getEspecialidades() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getTipos(Configuracion.ID_TIPO_ARTICULO_FAMILIA);
	}
	
	/**
	 * @return las prestaciones..
	 */
	public List<TipoMovimiento> getPrestaciones() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getPrestaciones();
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

	public String getFilter_especialidad() {
		return filter_especialidad;
	}

	public void setFilter_especialidad(String filter_especialidad) {
		this.filter_especialidad = filter_especialidad;
	}

	public Articulo getN_servicio() {
		return n_servicio;
	}

	public void setN_servicio(Articulo n_servicio) {
		this.n_servicio = n_servicio;
	}
	
}
