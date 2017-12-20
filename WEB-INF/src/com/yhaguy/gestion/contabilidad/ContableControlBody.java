package com.yhaguy.gestion.contabilidad;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;

import com.coreweb.componente.WindowPopup;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.yhaguy.BodyApp;
import com.yhaguy.domain.ContableAsiento;

public class ContableControlBody extends BodyApp {
	
	static final String ZUL_INSERT_ITEM = "/yhaguy/gestion/contabilidad/asientosItem.zul";
	
	private ContableAsientoDTO dto = new ContableAsientoDTO();
	
	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	@Override
	public boolean verificarAlGrabar() {
		return true;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		return "";
	}

	@Override
	public Assembler getAss() {
		return new ContableAsientoAssembler();
	}

	@Override
	public DTO getDTOCorriente() {
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (ContableAsientoDTO) dto;		
	}

	@Override
	public DTO nuevoDTO() throws Exception {
		return new ContableAsientoDTO();
	}

	@Override
	public String getEntidadPrincipal() {
		return ContableAsiento.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		return this.getAllDTOs(this.getEntidadPrincipal());
	}
	
	@Command
	public void insertarItem() throws Exception {
		this.insertarItem_();
	}
	
	/**
	 * inserta el item al detalle..
	 */
	private void insertarItem_() throws Exception {
		//this.nvoDetalle = new AjusteStockDetalleDTO();
		
		WindowPopup wp = new WindowPopup();
		//wp.setCheckAC(new ValidadorInsertarItem());
		wp.setDato(this);
		wp.setHigth("300px");
		wp.setModo(WindowPopup.NUEVO);
		wp.setTitulo("Insertar ítem");
		wp.setWidth("400px");
		wp.show(ZUL_INSERT_ITEM);
		if (wp.isClickAceptar()) {
			//this.dto.getDetalles().add(this.nvoDetalle);
		}
		//this.nvoDetalle = null;
	}
	
	/**
	 * GETS / SETS
	 */
	
	@DependsOn("dto.descripcion")
	public boolean isDetalleVisible() {
		return ((!this.dto.getDescripcion().isEmpty()));
	}
	
	public ContableAsientoDTO getDto() {
		return dto;
	}

	public void setDto(ContableAsientoDTO dto) {
		this.dto = dto;
	}
}
