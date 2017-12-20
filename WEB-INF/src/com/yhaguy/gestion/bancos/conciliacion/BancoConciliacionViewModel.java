package com.yhaguy.gestion.bancos.conciliacion;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.browser.Browser;
import com.coreweb.extras.csv.CSV;
import com.coreweb.util.AutoNumeroControl;
import com.coreweb.util.Misc;
import com.yhaguy.BodyApp;
import com.yhaguy.Configuracion;
import com.yhaguy.domain.BancoCta;
import com.yhaguy.domain.BancoExtracto;
import com.yhaguy.domain.BancoMovimiento;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.gestion.bancos.libro.AssemblerBancoCtaCte;
import com.yhaguy.gestion.bancos.libro.AssemblerBancoMovimiento;
import com.yhaguy.gestion.bancos.libro.BancoCtaDTO;
import com.yhaguy.gestion.bancos.libro.BancoMovimientoDTO;
import com.yhaguy.gestion.bancos.libro.ControlBancoMovimiento;
import com.yhaguy.gestion.reportes.formularios.ReportesViewModel;
import com.yhaguy.util.Utiles;

public class BancoConciliacionViewModel extends BodyApp {
	
	static final String PATH = Configuracion.pathConciliaciones;
	final static String KEY_NRO = "CONCI";

	static final String[][] CAB = { { "Empresa", CSV.STRING } };
	static final String[][] DET = { { "NUMERO", CSV.STRING },
			{ "DESCRIPCION", CSV.STRING }, { "DEBE", CSV.STRING },
			{ "HABER", CSV.STRING }, { "SALDO", CSV.STRING } };
	
	private BancoExtractoDTO dto = new BancoExtractoDTO();
	private Map<String, BancoExtractoDetalleDTO> detalles1;
	private Map<String, List<BancoExtractoDetalleDTO>> detalles2;
	
	private BancoExtractoDetalleDTO selectedItem1;
	private BancoExtractoDetalleDTO selectedItem2;
	private List<BancoExtractoDetalleDTO> selectedItems2;
	
	private boolean selectDetalle1 = false;
	private String mensajeValidacion = "";
	
	private Window win;
	
	
	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	@Override
	public boolean verificarAlGrabar() {
		try {
			return this.validarDatos();
		} catch (Exception e) {
			e.printStackTrace();
			this.mensajePopupTemporalWarning("Hubo un error al intentar guardar..");
			return false;
		}
	}

	@Override
	public String textoErrorVerificarGrabar() {
		return this.mensajeValidacion;
	}

	@Override
	public Assembler getAss() {
		return new BancoExtractoAssembler();
	}

	@Override
	public DTO getDTOCorriente() {
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (BancoExtractoDTO) dto;		
	}

	@Override
	public DTO nuevoDTO() throws Exception {
		BancoExtractoDTO nDto = new BancoExtractoDTO();
		this.sugerirValores(nDto);
		return nDto;
	}

	@Override
	public String getEntidadPrincipal() {
		return BancoExtracto.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		return this.getAllDTOs(getEntidadPrincipal());
	}
	
	@Override
	public Browser getBrowser() {
		return new BancoConciliacionBrowser();
	}
	
	/**
	 * COMANDOS
	 */
	
	@Command
	@NotifyChange("*")
	public void importarMovimientos() {
		try {
			this.importarMovimientosBanco();
			this.mensajePopupTemporal("Movimientos correctamente importados..");
		} catch (Exception e) {
			Clients.showNotification(
					"Hubo un error al importar los movimientos..",
					Clients.NOTIFICATION_TYPE_ERROR, null, null, 0);
			e.printStackTrace();
		}
	}
	
	@Command 
	@NotifyChange("*")
	public void uploadFile(@BindingParam("file") Media file) {
		try {
			Misc misc = new Misc();
			String name = this.dto.getNumero();
			boolean isText = "text/csv".equals(file.getContentType());
			InputStream file_ = new ByteArrayInputStream(isText ? file.getStringData().getBytes() : file.getByteData());
			misc.uploadFile(PATH, name, ".csv", file_);
			this.csvConciliacion();
		} catch (Exception e) {
			e.printStackTrace();
			Clients.showNotification(
					"Hubo un problema al intentar subir el archivo..",
					Clients.NOTIFICATION_TYPE_ERROR, null, null, 0);
		}
	}
	
	@Command
	@NotifyChange({ "selectedItem1", "selectDetalle1" })
	public void selectDetalle1() {
		this.selectDetalle1 = true;
		this.selectedItem1 = null;
		for (BancoExtractoDetalleDTO item : this.dto.getDetalles1()) {
			String nro1 = this.selectedItem2.getNumero();
			String nro2 = item.getNumero();
			if (nro1.equals(nro2)) {
				this.selectedItem1 = item;
			}
		}
	}
	
	@Command
	@NotifyChange({ "selectedItem2", "selectedItems2", "selectDetalle1" })
	public void selectDetalle2() {
		this.selectDetalle1 = false;
		this.selectedItem2 = null;
		this.selectedItems2 = new ArrayList<BancoExtractoDetalleDTO>();
		for (BancoExtractoDetalleDTO item : this.dto.getDetalles2()) {
			String nro1 = (String) this.selectedItem1.getNumero();
			String nro2 = (String) item.getNumero();
			if (nro1.equals(nro2)) {
				this.selectedItems2.add(item);
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void conciliar() {
		this.conciliarMovimiento();
	}
	
	@Command
	public void resumen() {
		this.resumenConciliacion();
	}
	
	@Command
	@NotifyChange("*")
	public void confirmar() {
		try {
			this.confirmarConciliacion();
		} catch (Exception e) {
			e.printStackTrace();
			this.mensajePopupTemporalWarning("Hubo un error al confirmar", 5000);
		}
	}
	
	
	/**
	 * FUNCIONES
	 */
	
	private void sugerirValores(BancoExtractoDTO dto) throws Exception {
		dto.setNumero(KEY_NRO + "-" + AutoNumeroControl.getAutoNumero(KEY_NRO, 5, true));
		dto.setSucursal(this.getSucursal());
	}
	
	/**
	 * csv de conciliacion..
	 */
	private void csvConciliacion() {
		try {
			this.detalles2 = new HashMap<String, List<BancoExtractoDetalleDTO>>();
			List<BancoExtractoDetalleDTO> list = new ArrayList<BancoExtractoDetalleDTO>();
			
			CSV csv = new CSV(CAB, DET, PATH + this.dto.getNumero() + ".csv", ';');

			csv.start();
			while (csv.hashNext()) {
				String numero = csv.getDetalleString("NUMERO");
				String descripcion = csv.getDetalleString("DESCRIPCION");
				String debe = csv.getDetalleString("DEBE");
				String haber = csv.getDetalleString("HABER");
				
				String[] numero_ = numero.split(" ");
				double importe;
				if (!debe.equals("0")) {
					importe = Double.parseDouble(debe.replace(".", ""));
				} else {
					importe = Double.parseDouble(haber.replace(".", ""));
				}
				
				BancoExtractoDetalleDTO det = new BancoExtractoDetalleDTO();
				det.setNumero(numero_[1]);
				det.setDescripcion(descripcion);
				det.setImporte(importe);
				det.setConciliado(false);
				det.setNumero_(Double.parseDouble(numero_[1]));
				det.setImporte_(importe);
				list.add(det);
				
				List<BancoExtractoDetalleDTO> dets = this.detalles2.get(numero_[1]);
				if (dets == null) {
					List<BancoExtractoDetalleDTO> ldet = new ArrayList<BancoExtractoDetalleDTO>();
					ldet.add(det);
					this.detalles2.put(numero_[1], ldet);					
				} else {
					dets.add(det);
					BancoExtractoDetalleDTO det_ = dets.get(0);
					double imp1 = det_.getImporte();
					det.setImporte_(imp1 + importe);
				}
			}
			
			for (String key : this.detalles2.keySet()) {
				List<BancoExtractoDetalleDTO> dets2 = this.detalles2.get(key);
				String key_ = key + ";" + dets2.get(0).getImporte_();
				BancoExtractoDetalleDTO det1 = this.detalles1.get(key_);
				if (det1 != null) {
					det1.setConciliado(true);
					for (BancoExtractoDetalleDTO item : dets2) {
						item.setConciliado(true);
					}
				}
			}
			
			this.dto.getDetalles2().addAll(list);
			this.mensajePopupTemporal("Archivo correctamente importado..");
		} catch (Exception e) {
			e.printStackTrace();
			Clients.showNotification(
					"Hubo un problema al leer el archivo..",
					Clients.NOTIFICATION_TYPE_ERROR, null, null, 0);
		}
	}
	
	/**
	 * importa los movimientos de banco..
	 */
	private void importarMovimientosBanco() throws Exception {
		this.detalles1 = new HashMap<String, BancoExtractoDetalleDTO>();
		List<BancoExtractoDetalleDTO> list = new ArrayList<BancoExtractoDetalleDTO>();
		AssemblerBancoMovimiento assBm = new AssemblerBancoMovimiento();
		RegisterDomain rr = RegisterDomain.getInstance();
		List<BancoMovimiento> movims = rr.getBancoMovimientosNoConciliados(this.dto
				.getDesde(), this.dto.getHasta(), this.dto.getBanco().getId());
		
		for (BancoMovimiento movim : movims) {
			BancoExtractoDetalleDTO det = new BancoExtractoDetalleDTO();
			det.setNumero(movim.getNroReferencia());
			det.setDescripcion(movim.getTipoMovimiento().getDescripcion());
			det.setImporte(movim.getMonto());
			det.setConciliado(false);
			det.setBancoMovimiento((BancoMovimientoDTO) assBm.domainToDto(movim));
			
			try {
				det.setNumero_(Double.parseDouble(movim.getNroReferencia()));
			} catch (Exception e) {
				det.setNumero_(0.0);
			}			
			
			if (!movim.isSaldoInicial()) {
				list.add(det);
				this.detalles1.put(movim.getNroReferencia() + ";" + movim.getMonto(), det);
			}
		}
		this.dto.getDetalles1().addAll(list);
	}
	
	/**
	 * conciliacion manual..
	 */
	private void conciliarMovimiento() {
		if (this.mensajeSiNo("Desea conciliar el movimiento..?")) {
			this.selectedItem1.setConciliado(true);
			for (BancoExtractoDetalleDTO item : this.selectedItems2) {
				item.setConciliado(true);
			}
		}
	}
	
	/**
	 * confirmar conciliacion..
	 */
	private void confirmarConciliacion() throws Exception {
		if (!this.mensajeSiNo("Desea confirmar la conciliacion..?")) {
			return;
		}
		this.dto.setCerrado(true);
		this.dto.setReadonly();
		this.dto = (BancoExtractoDTO) this.saveDTO(this.dto);
		ControlBancoMovimiento.conciliarMovimientos(this.dto, this.getLoginNombre());
		this.setEstadoABMConsulta();
		Clients.showNotification("Conciliacion confirmada..");
	}
	
	/**
	 * impresion del resumen de conciliacion..
	 */
	private void resumenConciliacion() {		
		String source = ReportesViewModel.SOURCE_RESUMEN_CONCILIACION;
		Map<String, Object> params = new HashMap<String, Object>();
		JRDataSource dataSource = new ResumenConciliacioDataSource(this.dto);
		params.put("Usuario", getUs().getNombre());
		params.put("ConciliacionNro", this.dto.getNumero());
		params.put("Banco", this.dto.getBanco().getBanco().getPos1());
		params.put("Desde", Utiles.getDateToString(this.dto.getDesde(), Utiles.DD_MM_YY));
		params.put("Hasta", Utiles.getDateToString(this.dto.getHasta(), Utiles.DD_MM_YY));
		imprimirJasper(source, params, dataSource, ReportesViewModel.FORMAT_PDF);
	}
	
	/**
	 * @return la validacion de datos..
	 */
	private boolean validarDatos() throws Exception {
		boolean out = true;
		this.mensajeValidacion = "No se pudo completar la operación debido a: ";
		if (this.dto.getDetalles1().isEmpty()) {
			out = false;
			this.mensajeValidacion += "\n - Debe importar los movimientos..";
		}
		if (this.dto.getDetalles2().isEmpty()) {
			out = false;
			this.mensajeValidacion += "\n - Debe importar el extracto..";
		}
		if (this.dto.esNuevo() && out) {
			this.dto.setNumero(KEY_NRO + "-" + AutoNumeroControl.getAutoNumero(KEY_NRO, 5));
		}
		return out;
	}
	
	/**
	 * Despliega el reporte en un pdf para su impresion..
	 */
	private void imprimirJasper(String source, Map<String, Object> parametros,
			JRDataSource dataSource, Object[] format) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", source);
		params.put("parametros", parametros);
		params.put("dataSource", dataSource);
		params.put("format", format);

		this.win = (Window) Executions
				.createComponents(
						com.yhaguy.gestion.reportes.formularios.ReportesViewModel.ZUL_REPORTES,
						this.mainComponent, params);
		this.win.doModal();
	}
	
	
	/**
	 * GETS / SETS
	 */
	
	@DependsOn({ "dto.banco", "dto.desde", "dto.hasta" })
	public boolean isDetalleVisible() {
		return (this.dto.getBanco() != null && this.dto.getDesde() != null && this.dto.getHasta() != null); 
	}
	
	@DependsOn({ "selectedItem1", "selectedItems2" })
	public boolean isConciliarEnable() {
		return this.selectedItem1 != null
				&& !(this.selectedItem1.isConciliado())
				&& this.selectedItems2 != null
				&& !this.selectedItems2.isEmpty();
	}
	
	public List<BancoCtaDTO> getBancos() throws Exception {
		List<BancoCtaDTO> out = new ArrayList<BancoCtaDTO>();
		for (DTO dto : this.getAllDTOs(BancoCta.class.getName(), new AssemblerBancoCtaCte())) {
			BancoCtaDTO bdto = (BancoCtaDTO) dto;
			out.add(bdto);
		}
		return out;
	}
	
	public BancoExtractoDTO getDto() {
		return dto;
	}

	public void setDto(BancoExtractoDTO dto) {
		this.dto = dto;
	}

	public boolean isSelectDetalle1() {
		return selectDetalle1;
	}

	public void setSelectDetalle1(boolean selectDetalle1) {
		this.selectDetalle1 = selectDetalle1;
	}

	public BancoExtractoDetalleDTO getSelectedItem1() {
		return selectedItem1;
	}

	public void setSelectedItem1(BancoExtractoDetalleDTO selectedItem1) {
		this.selectedItem1 = selectedItem1;
	}

	public BancoExtractoDetalleDTO getSelectedItem2() {
		return selectedItem2;
	}

	public void setSelectedItem2(BancoExtractoDetalleDTO selectedItem2) {
		this.selectedItem2 = selectedItem2;
	}

	public List<BancoExtractoDetalleDTO> getSelectedItems2() {
		return selectedItems2;
	}

	public void setSelectedItems2(List<BancoExtractoDetalleDTO> selectedItems2) {
		this.selectedItems2 = selectedItems2;
	}
}

/**
 * DataSource de Resumen Conciliacion..
 */
class ResumenConciliacioDataSource implements JRDataSource {
	
	private BancoExtractoDTO dto;
	
	public ResumenConciliacioDataSource(BancoExtractoDTO dto) {
		this.dto = dto;
		// ordena la lista segun conciliacion..
		Collections.sort(this.dto.getDetalles1(), new Comparator<BancoExtractoDetalleDTO>() {
			@Override
			public int compare(BancoExtractoDetalleDTO o1,
					BancoExtractoDetalleDTO o2) {
				int con1 = o1.isConciliado() ? 0 : 1;
				int con2 = o2.isConciliado() ? 0 : 1;
				return con1 - con2;
			}
		});
	}

	private int index = -1;

	@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		BancoExtractoDetalleDTO det = this.dto.getDetalles1().get(index);

		if ("Numero".equals(fieldName)) {
			value = det.getNumero();
		} else if ("Concepto".equals(fieldName)) {
			value = det.getDescripcion();
		} else if ("Importe".equals(fieldName)) {
			value = det.getImporteGs_();
		}  else if ("TituloDetalle".equals(fieldName)) {
			value = det.isConciliado() ? "MOVIMIENTOS CONCILIADOS" : "MOVIMIENTOS NO CONCILIADOS";
		} else if ("TotalImporte".equals(fieldName)) {
			value = Utiles.getNumberFormat(det.isConciliado() ? dto.getTotalImporteConciliado() : dto.getTotalImporteNoConciliado());
		}
		return value;
	}

	@Override
	public boolean next() throws JRException {
		if (index < this.dto.getDetalles1().size() - 1) {
			index++;
			return true;
		}
		return false;
	}
}
