package com.yhaguy.gestion.reparto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

import com.coreweb.componente.BuscarElemento;
import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.componente.ViewPdf;
import com.coreweb.componente.WindowPopup;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.agenda.ControlAgendaEvento;
import com.coreweb.extras.browser.Browser;
import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.AutoNumeroControl;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.yhaguy.BodyApp;
import com.yhaguy.Configuracion;
import com.yhaguy.ID;
import com.yhaguy.domain.Proveedor;
import com.yhaguy.domain.Reparto;
import com.yhaguy.domain.Venta;
import com.yhaguy.gestion.venta.AssemblerVenta;
import com.yhaguy.gestion.venta.VentaDTO;
import com.yhaguy.util.Utiles;
import com.yhaguy.util.reporte.ReporteYhaguy;

@SuppressWarnings("unchecked")
public class RepartoViewModel extends BodyApp {

	static final String NRO_REP = Configuracion.NRO_REPARTO;
	static final String ZUL_ENTREGA = "/yhaguy/gestion/reparto/entrega.zul";

	private RepartoDTO dto;
	private RepartoDetalleDTO dtoDetalle;
	private MyArray selectedItem;
	private List<MyArray> selectedMovimientosReparto;
	private List<MyArray> listaRomaneo;
	private List<RepartoDetalleDTO> selectedItems;
	private List<MyArray> vehiculos;
	private List<MyArray> itemsReparto;

	private MyArray selectedVehiculo;
	private MyPair selectedRepartidor;

	private boolean solicitarCabecera;
	private String itemsEliminar;

	private Map<Object, MyArray> cantPorArticulo;
	private String comprobantes;

	private String mensajeValidacion;

	@Wire
	private Popup popDetalle;

	@Wire
	private Textbox txNro;

	@Init(superclass = true)
	public void init() throws Exception {
		this.inicializar();
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	@Override
	public Assembler getAss() {
		return new AssemblerReparto();
	}

	@Override
	public DTO getDTOCorriente() {
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.selectedItems = null;
		this.dto = (RepartoDTO) dto;
	}

	@Override
	public DTO nuevoDTO() throws Exception {
		this.solicitarCabecera = true;
		RepartoDTO dto = new RepartoDTO();
		this.sugerirValores(dto);
		return dto;
	}

	@Override
	public String getEntidadPrincipal() {
		return Reparto.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		return this.getAllDTOs(this.getEntidadPrincipal());
	}

	@Override
	public Browser getBrowser() {
		return new RepartoBrowser();
	}

	@Override
	public boolean getImprimirDeshabilitado() throws Exception {
		long actual = this.dto.getEstadoReparto().getId().longValue();
		long preparacion = this.getEstadoPreparacion().getId().longValue();
		return (actual == preparacion);
	}

	@Override
	public int getCtrAgendaTipo() {
		return ControlAgendaEvento.NORMAL;
	}

	@Override
	public String getCtrAgendaKey() {
		return this.dto.getNumero();
	}

	@Override
	public String getCtrAgendaTitulo() {
		return "[Pedido: " + this.getCtrAgendaKey() + "]";
	}

	@Override
	public boolean getAgendaDeshabilitado() throws Exception {
		return this.dto.esNuevo();
	}

	@Override
	public void showImprimir() {
		this.imprimir();
	}

	@Override
	public boolean verificarAlGrabar() {
		return this.validarFormulario();
	}

	@Override
	public String textoErrorVerificarGrabar() {
		return this.mensajeValidacion;
	}

	/******************** COMANDOS *********************/

	@Command
	@NotifyChange("*")
	public void confirmarEnvio() throws Exception {
		this.confirmarEnvioReparto();
	}

	@Command
	@NotifyChange("*")
	public void confirmarEntrega() throws Exception {
		this.confirmarEntregaReparto();
	}

	/**
	 * Muestra en un popup el detalle del item..
	 */
	@Command
	@NotifyChange("itemsReparto")
	public void verDetalle(@BindingParam("item") RepartoDetalleDTO item,
			@BindingParam("comp") Component comp) {
		this.itemsReparto = (List<MyArray>) item.getDetalle().getPos6();
		this.popDetalle.open(comp, "after_end");
	}

	@Command
	@NotifyChange("*")
	public void abrirVentanaInsertarDetalle() throws Exception {
		this.insertarItem();
	}

	@Command
	@NotifyChange("*")
	public void buscarProveedor() throws Exception {
		this.buscarProveedores();
	}

	@Command
	@NotifyChange("*")
	public void eliminarItem() {
		this.eliminarItems();
	}

	/***************************************************/

	/******************** FUNCIONES ********************/

	/**
	 * inicializa valores por defecto..
	 */
	private void inicializar() throws Exception {
		this.dto = new RepartoDTO();
		this.dtoDetalle = new RepartoDetalleDTO();
		this.selectedItems = new ArrayList<RepartoDetalleDTO>();
		this.vehiculos = new ArrayList<MyArray>();
		this.selectedVehiculo = new MyArray();
		this.selectedRepartidor = new MyPair();
		this.solicitarCabecera = false;
		this.cantPorArticulo = new HashMap<Object, MyArray>();

		this.vehiculos = ((AssemblerReparto) this.getAss())
				.getVehiculosSucursalMyArray(this.getAcceso()
						.getSucursalOperativa().getId());
	}

	/**
	 * inicializa valores por defecto..
	 */
	private void sugerirValores(RepartoDTO dto) {
		dto.setNumero("");
		dto.setCreador(this.getAcceso().getFuncionario());
		dto.setFechaCreacion(new Date());
		dto.setEstadoReparto(this.getEstadoPreparacion());
		dto.setSucursal(this.getAcceso().getSucursalOperativa());
		dto.setObservaciones("Sin obs..");
		dto.setTipoReparto(this.isEsRepartoYhaguy() ? this
				.getTipoRepartoYhaguy() : new MyPair());
		txNro.focus();
	}

	/**
	 * impresion del reparto..
	 */
	private void imprimir() {
		List<Object[]> data = new ArrayList<>();

		for (RepartoDetalleDTO d : this.dto.getDetalles()) {
			Object[] obj = new Object[4];
			obj[0] = d.getDetalle().getPos2();
			obj[1] = d.getTipoMovimiento().getPos1();
			obj[2] = d.getDetalle().getPos4();
			obj[3] = d.getDetalle().getPos10();
			data.add(obj);
		}

		ReporteReparto r = new ReporteReparto(this.dto);
		r.setDatosReporte(data);
		r.setApaisada();
		ViewPdf vp = new ViewPdf();
		vp.setBotonCancelar(false);
		vp.setBotonImprimir(false);
		vp.showReporte(r, this);
	}

	/**
	 * inserta un item al detalle..
	 */
	private void insertarItem() throws Exception {
		
		this.selectedMovimientosReparto = new ArrayList<MyArray>();
		WindowPopup w = new WindowPopup();
		w.setModo(WindowPopup.NUEVO);
		w.setTitulo("Movimientos para Reparto");
		w.setWidth("1000px");
		w.setHigth("80%");
		w.setDato(this);
		w.setCheckAC(new ValidadorInsertarItem(this));
		w.show(Configuracion.INSERTAR_DETALLE_REPARTO_ZUL);

		if (w.isClickAceptar()) {
			for(MyArray item : this.selectedMovimientosReparto){
				
				this.setDtoDetalle(new RepartoDetalleDTO());
				
				long idMov = (long) item.getPos1();
				MyArray tipoMov = (MyArray) item.getPos9();

				this.getDtoDetalle().setIdMovimiento(idMov);
				this.getDtoDetalle().setTipoMovimiento(tipoMov);
				this.getDtoDetalle().setPeso((double) item.getPos11());
				this.getDtoDetalle().setDetalle(item);

				this.dto.getDetalles().add(this.getDtoDetalle());

				this.addEventoAgenda("El ítem, factura "
						+ item.getPos2() + ", se insertó.");

			}
		}
		txNro.focus();
	}

	/**
	 * Busqueda de proveedores..
	 */
	private void buscarProveedores() throws Exception {

		String[] attProveedor = { "empresa.codigoEmpresa",
				"empresa.razonSocial", "empresa.ruc" };
		String[] columnas = { "Código", "Razon Social", "Ruc" };

		BuscarElemento be = new BuscarElemento();
		be.setTitulo("Buscar Proveedor");
		be.setClase(Proveedor.class);
		be.setAtributos(attProveedor);
		be.setNombresColumnas(columnas);
		be.setWidth("800px");
		be.show("");
		if (be.isClickAceptar() == true) {
			this.dto.setProveedor(be.getSelectedItem());
		}
	}

	/**
	 * elimina items del detalle..
	 */
	private void eliminarItems() {
		if (this.confirmarEliminarItem() == true) {
			for (Object obj : this.selectedItems) {
				RepartoDetalleDTO item;
				if (obj instanceof RepartoDetalleDTO) {
					item = (RepartoDetalleDTO) obj;
					this.dto.getItemsEliminados().add(item);
					this.dto.getDetalles().remove(item);
					this.addEventoAgenda("El ítem, factura "
							+ item.getDetalle().getPos2()
							+ ", fue eliminado de la lista de reparto.");
				}
			}
		}
		this.selectedItems = new ArrayList<RepartoDetalleDTO>();
	}

	/**
	 * @return booleano que confirma la eliminacion de items..
	 */
	private boolean confirmarEliminarItem() {
		this.itemsEliminar = "Esta seguro de eliminar los sgts ítems: \n";
		for (Object obj : this.selectedItems) {
			RepartoDetalleDTO item;

			if (obj instanceof RepartoDetalleDTO) {
				item = (RepartoDetalleDTO) obj;
				this.itemsEliminar += "\n - " + item.getIdMovimiento() + " "
						+ item.getTipoMovimiento().getPos1();
			}
		}
		return this.mensajeSiNo(this.itemsEliminar);
	}

	/**
	 * confirma el envio del reparto..
	 */
	private void confirmarEnvioReparto() throws Exception {
		if (this.mensajeSiNo("Esta seguro de confirmar el envio..") == false)
			return;

		this.dto.setEstadoReparto(this.getEstadoEntregado());
		this.dto.setReadonly();
		this.dto = (RepartoDTO) this.saveDTO(this.dto);
		this.setEstadoABMConsulta();
		this.addEventoAgenda("Se ha confirmado el envio..");
		this.grabarEventosAgenda();
		Clients.showNotification("Reparto Confirmado..");
		
		for (RepartoDetalleDTO item : this.dto.getDetallesVentas()) {
			this.setDatosReparto(item);
		}
	}
	
	/**
	 * setea la informacion del reparto a la venta..
	 */
	private void setDatosReparto(RepartoDetalleDTO item) throws Exception {
		Assembler assVenta = new AssemblerVenta();
		Date finTraslado_ = this.m.agregarDias(new Date(), 3);
		String traslado = this.m.dateToString(new Date(), Misc.DD_MM_YYYY);
		String finTraslado = this.m.dateToString(finTraslado_, Misc.DD_MM_YYYY);

		VentaDTO vta = (VentaDTO) this.getDTOById(Venta.class.getName(),
				item.getIdMovimiento(), assVenta);
		vta.setPuntoPartida("ruta mcal. estigarribia nro. 287 km 9.5");
		vta.setFechaTraslado(traslado);
		vta.setFechaFinTraslado(finTraslado);
		vta.setRepartidor(this.dto.getRepartidor().getText());
		vta.setCedulaRepartidor(this.dto.getRepartidor().getSigla());
		vta.setMarcaVehiculo((String) this.dto.getVehiculo().getPos1());
		vta.setChapaVehiculo((String) this.dto.getVehiculo().getPos4());
		vta.setEstado(this.getDtoUtil().getEstadoRepartoEntregado());
		this.saveDTO(vta, assVenta);
	}

	/**
	 * confirma la entrega del reparto.. especifica las facturas que fueron
	 * entregadas y aquellas que no las vuelve a habilitar para reparto..
	 */
	private void confirmarEntregaReparto() throws Exception {
		String mensaje = "Esta seguro de confirmar el reparto..\n "
				+ this.getMensajeEntrega();

		if (this.mensajeSiNo(mensaje) == false)
			return;

		this.dto.setFechaRecepcion(new Date());
		this.dto.setReceptor(this.getAcceso().getFuncionario());
		this.dto.setEstadoReparto(getEstadoEntregado());
		this.dto.setReadonly();
		this.dto = (RepartoDTO) this.saveDTO(this.dto);
		this.setEstadoABMConsulta();

		Clients.showNotification("Reparto Entregado..");
	}

	/**
	 * @return validacion del formulario..
	 */
	private boolean validarFormulario() {
		boolean out = true;
		this.mensajeValidacion = "No se puede realizar la operación debido a \n";

		try {

			if (this.dto.getDetalles().size() == 0) {
				out = false;
				this.mensajeValidacion += "\n - Debe ingresar al menos un item..";
			}

			if (out == true && this.dto.esNuevo()) {
				String nro = AutoNumeroControl.getAutoNumeroKey(NRO_REP, 7);
				dto.setNumero(nro);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.mensajeError("Ocurrió un error al grabar..");
		}

		return out;
	}

	/***************************************************/

	/********************** GET/SET ********************/

	public boolean isEsRepartoYhaguy() {
		return this.getAliasFormularioCorriente().compareTo(ID.F_REPARTO_ABM) == 0;
	}

	@DependsOn({ "dto.vehiculo", "dto.repartidor" })
	public boolean isDetalleVisible() {
		return (this.dto.getVehiculo().esNuevo() == false && this.dto.getRepartidor().esNuevo() == false);
	}

	@DependsOn("detalleVisible")
	public boolean isSolicitarCabeceraVisible() {
		return this.isDetalleVisible() == false;
	}

	@DependsOn("dto")
	public boolean isColEntregaVisible() {
		return !this.dto.isPreparacion();
	}

	@DependsOn({ "selectedItems", "deshabilitado" })
	public boolean isEliminarItemDisabled() {
		return (this.getSelectedItems() == null)
				|| (this.getSelectedItems().size() == 0)
				|| this.isDeshabilitado() || !this.dto.isPreparacion();
	}

	@DependsOn("deshabilitado")
	public boolean isInsertarItemDisabled() {
		return this.isDeshabilitado() || !this.dto.isPreparacion();
	}

	@DependsOn({ "dto.detalles" })
	public boolean isRomaneoEnable() {
		return (this.dto.getDetalles().size() > 0 == true)
				&& (this.isDeshabilitado() == false);
	}

	@DependsOn({ "dto", "deshabilitado" })
	public boolean isConfirmarEnvioDisabled() {
		return this.isDeshabilitado() || this.dto.esNuevo()
				|| !this.dto.isPreparacion();
	}

	@DependsOn("deshabilitado")
	public boolean isConfirmarEntregaDisabled() {
		return this.isDeshabilitado() || !this.dto.isEnTransito();
	}

	public List<MyPair> getRepartidores() {
		List<MyPair> out = new ArrayList<MyPair>();
		for (MyArray item : this.getDtoUtil().getFuncionarios()) {
			MyPair func = new MyPair(item.getId(), (String) item.getPos1());
			out.add(func);
		}
		return out;
	}

	private MyPair getEstadoPreparacion() {
		return this.getDtoUtil().getEstadoRepartoPreparacion();
	}

	private MyPair getEstadoEntregado() {
		return this.getDtoUtil().getEstadoRepartoEntregado();
	}

	private MyPair getTipoRepartoYhaguy() {
		return this.getDtoUtil().getTipoRepartoYhaguy();
	}

	public boolean getCheckmarkHabilitado() {
		boolean out = false;
		if (this.isDeshabilitado() == false
				&& this.dto.getDetalles().size() > 0) {
			out = true;
		}
		return out;
	}

	private String getMensajeEntrega() {
		String out = "- Total entregado: " + this.dto.getEntregas(true).size()
				+ "\n - Total sin entregar: "
				+ this.dto.getEntregas(false).size();
		return out;
	}

	public boolean isSolicitarCabecera() {
		return solicitarCabecera;
	}

	public void setSolicitarCabecera(boolean solicitarCabecera) {
		this.solicitarCabecera = solicitarCabecera;
	}

	public Map<Object, MyArray> getCantPorArticulo() {
		return cantPorArticulo;
	}

	public void setCantPorArticulo(Map<Object, MyArray> cantPorArticulo) {
		this.cantPorArticulo = cantPorArticulo;
	}

	public String getComprobantes() {
		return comprobantes;
	}

	public void setComprobantes(String comprobantes) {
		this.comprobantes = comprobantes;
	}

	public List<MyArray> getVehiculos() {
		return vehiculos;
	}

	public void setVehiculos(List<MyArray> vehiculos) {
		this.vehiculos = vehiculos;
	}

	public RepartoDTO getDto() {
		return dto;
	}

	public void setDto(RepartoDTO dto) {
		this.dto = dto;
	}

	public RepartoDetalleDTO getDtoDetalle() {
		return dtoDetalle;
	}

	public void setDtoDetalle(RepartoDetalleDTO dtoDetalle) {
		this.dtoDetalle = dtoDetalle;
	}

	public List<MyArray> getListaRomaneo() {
		return listaRomaneo;
	}

	public void setListaRomaneo(List<MyArray> listaRomaneo) {
		this.listaRomaneo = listaRomaneo;
	}

	public List<RepartoDetalleDTO> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<RepartoDetalleDTO> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public MyArray getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(MyArray selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<MyArray> getItemsReparto() {
		return itemsReparto;
	}

	public void setItemsReparto(List<MyArray> itemsReparto) {
		this.itemsReparto = itemsReparto;
	}

	public MyArray getSelectedVehiculo() {
		return selectedVehiculo;
	}

	public void setSelectedVehiculo(MyArray selectedVehiculo) {
		this.selectedVehiculo = selectedVehiculo;
	}

	public MyPair getSelectedRepartidor() {
		return selectedRepartidor;
	}

	public void setSelectedRepartidor(MyPair selectedRepartidor) {
		this.selectedRepartidor = selectedRepartidor;
	}

	public List<MyArray> getSelectedMovimientosReparto() {
		return selectedMovimientosReparto;
	}

	public void setSelectedMovimientosReparto(
			List<MyArray> selectedMovimientosReparto) {
		this.selectedMovimientosReparto = selectedMovimientosReparto;
	}

}

/**
 * Validador insertar item..
 */
class ValidadorInsertarItem implements VerificaAceptarCancelar {

	private RepartoViewModel ctr;
	private String msgError;

	public ValidadorInsertarItem(RepartoViewModel ctr) {
		this.ctr = ctr;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.msgError = "No se puede realizar la operación debido a: \n";

		List<MyArray> selectedItems = ctr.getSelectedMovimientosReparto();

		if (selectedItems.size() == 0) {
			this.msgError += "\n - Debe seleccionar al menos un ítem..";
			out = false;
		}

		return out;
	}

	@Override
	public String textoVerificarAceptar() {
		return msgError;
	}

	@Override
	public boolean verificarCancelar() {
		return true;
	}

	@Override
	public String textoVerificarCancelar() {
		return "Error al Cancelar";
	}
}

/**
 * Reporte del Reparto..
 */
class ReporteReparto extends ReporteYhaguy {

	private static String TITULO = "Planilla de Reparto";
	private static String URL_ARCHIVO = "repartos";
	private static String NOMBRE_ARCHIVO = "Reparto";

	// Columnas del Reporte
	static DatosColumnas col1 = new DatosColumnas("Nro. Documento",
			TIPO_STRING, 50);
	static DatosColumnas col2 = new DatosColumnas("Tipo", TIPO_STRING, 50);
	static DatosColumnas col3 = new DatosColumnas("Cliente", TIPO_STRING);
	static DatosColumnas col4 = new DatosColumnas("Dirección", TIPO_STRING);

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();

	private RepartoDTO reparto;

	public ReporteReparto(RepartoDTO reparto) {
		this.reparto = reparto;
	}

	static {
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
	};

	@Override
	public void informacionReporte() {
		this.setTitulo(TITULO);
		this.setDirectorio(URL_ARCHIVO);
		this.setNombreArchivo(NOMBRE_ARCHIVO);
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
		this.setBorrarDespuesDeVer(true);
	}

	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		String nro = this.reparto.getNumero();
		String creacion = Utiles.getDateToString(this.reparto.getFechaCreacion(), Utiles.DD_MM_YYYY_HH_MM_SS);
		String creador = (String) this.reparto.getCreador().getPos1();
		String vehiculo = (String) this.reparto.getVehiculo().getPos1();
		String repartidor = this.reparto.getRepartidor().getText();

		VerticalListBuilder out = null;
		out = cmp.verticalList();

		out.add(cmp.horizontalFlowList().add(this.textoParValor("Número", nro)));
		out.add(cmp.horizontalFlowList().add(
				this.textoParValor("Creacion", creacion + " - " + creador)));
		out.add(cmp.horizontalFlowList().add(
				this.textoParValor("Vehiculo", vehiculo)));
		out.add(cmp.horizontalFlowList().add(
				this.textoParValor("Repartidor", repartidor)));
		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}

}