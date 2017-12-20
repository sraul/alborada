package com.yhaguy.gestion.alborada.bancos;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Popup;

import com.coreweb.control.SimpleViewModel;
import com.yhaguy.Configuracion;
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.DebitoComandado;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.util.Utiles;

public class DebitosComandadosViewModel extends SimpleViewModel {

	private DebitoComandado n_debitoComandado = new DebitoComandado();
	private List<DebitoComandado> selectedItems = new ArrayList<DebitoComandado>();
	private DebitoComandado s_debitoComandado;
	
	private String filter_razonsocial = "";
	private String filter_cedula = "";
	
	@Init(superclass = true)
	public void init() {
		this.n_debitoComandado.setIUSUCA(this.getLoginNombre().toUpperCase());
		this.n_debitoComandado.setIFECCA(Utiles.getDateToString(new Date(), "yyyyMMdd"));
		this.n_debitoComandado.setIHORCA(Utiles.getDateToString(new Date(), "HHmmss"));
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("*")
	public void addDebitoComandado(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.n_debitoComandado, this.getLoginNombre());
		this.n_debitoComandado = new DebitoComandado();
		this.n_debitoComandado.setIUSUCA(this.getLoginNombre().toUpperCase());
		comp.close();
		Clients.showNotification("REGISTRO GUARDADO..");
	}
	
	@Command
	@NotifyChange("*")
	public void saveDebitoComandado(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.s_debitoComandado, this.getLoginNombre());
		this.selectedItems = new ArrayList<DebitoComandado>();
		this.s_debitoComandado = null;
		comp.close();
		Clients.showNotification("REGISTRO GUARDADO..");
	}
	
	@Command
	@NotifyChange("*")
	public void deleteDebitoComandado() throws Exception {
		if(!this.mensajeSiNo("DESEA ELIMINAR LOS REGISTROS SELECCIONADOS.."))
			return;
		RegisterDomain rr = RegisterDomain.getInstance();
		for (DebitoComandado deb : this.selectedItems) {
			rr.deleteObject(deb);
		}
		this.selectedItems = new ArrayList<DebitoComandado>();
		this.s_debitoComandado = null;
		Clients.showNotification("REGISTROS ELIMINADOS..");
	}
	
	@Command
	@NotifyChange("*")
	public void generarArchivo() throws Exception {
		PrintWriter writer = new PrintWriter(Configuracion.pathDebitosComandados + "TRANSFER.txt", "UTF-8");
		for (DebitoComandado deb : this.selectedItems) {
			String text = "D" + deb.getITITRA() + deb.getICDSRV() + deb.getICTDEB_() + deb.getIBCOCR() + deb.getICTCRE_()
					+ deb.getITCRDB() + deb.getIORDEN_() + deb.getIMONED() + deb.getIMTOTR_() + deb.getIMTOT2_() + deb.getINRODO_()
					+ deb.getITIFAC_() + deb.getINRFAC_() + deb.getINRCUO_() + deb.getIFCHCR__() + deb.getIFCHC2_() + deb.getICEPTO_()
					+ deb.getINRREF_() + deb.getIFECCA_() + deb.getIHORCA_() + deb.getIUSUCA_();
			writer.print(text + "\r\n");
		}
		for (DebitoComandado deb : this.selectedItems) {
			String text = "C" + deb.getITITRA() + deb.getICDSRV() + deb.getICTDEB_() + StringUtils.leftPad("", 3, "0") + StringUtils.leftPad("", 10, "0")
					+ StringUtils.leftPad("", 1, " ") + StringUtils.leftPad("", 50, " ") + StringUtils.leftPad("", 1, "0") + deb.getIMTOTR_() + StringUtils.leftPad("", 15, "0") + StringUtils.leftPad("", 12, " ")
					+ StringUtils.leftPad("", 1, "0") + StringUtils.leftPad("", 20, " ") + StringUtils.leftPad("", 3, "0") + StringUtils.leftPad("", 8, "0") + StringUtils.leftPad("", 8, "0") + StringUtils.leftPad("", 50, " ")
					+ StringUtils.leftPad("", 15, " ") + StringUtils.leftPad("", 8, "0") + StringUtils.leftPad("", 6, "0") + StringUtils.leftPad("", 10, "");
			writer.print(text + "\r\n");
		}
		writer.close();
		
		Filedownload.save("/yhaguy/archivos/debitos_comandados/TRANSFER.txt", null);
		this.selectedItems = new ArrayList<DebitoComandado>();
	}
	
	public static void main(String[] args) throws Exception{
		PrintWriter writer = new PrintWriter("/home/sergio/test.txt", "UTF-8");
		writer.println("The first line");
		writer.println("The second line");
		writer.close();
	}
	
	/**
	 * GETS / SETS
	 */
	
	/**
	 * @return los debitos comandados..
	 */
	@SuppressWarnings("unchecked")
	public List<DebitoComandado> getDebitosComandados() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getObjects(DebitoComandado.class.getName());
	}
	
	@DependsOn({ "filter_razonsocial", "filter_cedula" })
	public List<Cliente> getPacientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getClientes(this.filter_razonsocial, this.filter_cedula);
	}

	public DebitoComandado getN_debitoComandado() {
		return n_debitoComandado;
	}

	public void setN_debitoComandado(DebitoComandado n_debitoComandado) {
		this.n_debitoComandado = n_debitoComandado;
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

	public List<DebitoComandado> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<DebitoComandado> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public DebitoComandado getS_debitoComandado() {
		return s_debitoComandado;
	}

	public void setS_debitoComandado(DebitoComandado s_debitoComandado) {
		this.s_debitoComandado = s_debitoComandado;
	}	
}
