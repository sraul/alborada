package com.yhaguy.gestion.contabilidad;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.Config;
import com.coreweb.control.SoloViewModel;

public class ContableSimpleViewModel extends SoloViewModel {
	
	private ContableControlBody dato;

	@Init(superclass = true)
	public void init(@ExecutionArgParam(Config.DATO_SOLO_VIEW_MODEL) ContableControlBody dato) {
		this.dato = dato;
		this.setAliasFormularioCorriente(dato.getAliasFormularioCorriente());
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	public ContableControlBody getDato() {
		return dato;
	}

	public void setDato(ContableControlBody dato) {
		this.dato = dato;
	}
}
