package com.yhaguy.gestion.bancos.libro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;

import com.coreweb.componente.ViewPdf;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.browser.Browser;
import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.MyArray;
import com.yhaguy.BodyApp;
import com.yhaguy.domain.BancoCta;
import com.yhaguy.domain.BancoMovimiento;
import com.yhaguy.domain.CuentaContable;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.util.Utiles;
import com.yhaguy.util.reporte.ReporteYhaguy;

public class BancoControlBody extends BodyApp {

	private BancoCtaDTO dto = new BancoCtaDTO();
	private BancoMovimientoDTO dtoMovimiento = new BancoMovimientoDTO();
	private BancoMovimientoDTO selectedMovimento = new BancoMovimientoDTO();
	
	private List<BancoMovimientoDTO> movimientos = new ArrayList<BancoMovimientoDTO>();	
	private MyArray selectedMes;
	private String selectedAnho = Utiles.getAnhoActual();

	@Init(superclass = true)
	public void init() {
		try {
			this.selectedMes = Utiles.getMesCorriente(this.selectedAnho);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	@Override
	public Assembler getAss() {
		return new AssemblerBancoCtaCte();
	}

	@Override
	public DTO getDTOCorriente() {
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (BancoCtaDTO) dto;
	}

	@Override
	public DTO nuevoDTO() throws Exception {
		return new BancoCtaDTO();
	}

	@Override
	public String getEntidadPrincipal() {
		return BancoCta.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		return this.getAllDTOs(this.getEntidadPrincipal());
	}

	public Browser getBrowser() {
		return new BancoBrowser();
	}
	
	@DependsOn("movimientos")
	public double getTotalDebe() {
		double out = 0;
		for (BancoMovimientoDTO movim : this.movimientos) {
			out += movim.getDebe();
		}
		return out;
	}
	
	@DependsOn("movimientos")
	public double getTotalHaber() {
		double out = 0;
		for (BancoMovimientoDTO movim : this.movimientos) {
			out += movim.getHaber();
		}
		return out;
	}
	
	@DependsOn("movimientos")
	public double getTotalSaldo() {
		return this.getTotalDebe() - this.getTotalHaber();
	}

	@Override
	public boolean verificarAlGrabar() {
		boolean grabar = true;
		if (grabar == true && this.dto.esNuevo() == true) {
			CuentaContable cuenta = new CuentaContable();
			cuenta.setAlias("CT-BANCO- " + this.dto.getBanco().getPos1());
		}
		return grabar;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		return "";
	}
	
	@Command
	public void imprimir() {
		this.imprimirLibroBanco();
	}
	
	/**
	 * FUNCIONES..
	 */
	
	/**
	 * imprimir libro banco
	 */
	private void imprimirLibroBanco() {
		List<Object[]> data = new ArrayList<Object[]>();

		for (BancoMovimientoDTO movim : this.movimientos) {
			Object[] obj1 = new Object[] {
					movim.getTipoMovimiento().getPos1(),
					Utiles.getDateToString(movim.getFecha(), Utiles.DD_MM_YYYY),
					movim.getNroReferencia(), movim.getDescripcion().toUpperCase(),
					movim.getDebe(), movim.getHaber(), movim.getSaldo() };
			data.add(obj1);
		}

		ReporteYhaguy rep = new ReporteLibroBanco((String) this.dto.getBanco()
				.getPos1(), (String) this.selectedMes.getPos1() + " - " + this.selectedAnho, this
				.getSucursal().getText());
		rep.setDatosReporte(data);
		rep.setApaisada();

		ViewPdf vp = new ViewPdf();
		vp.setBotonImprimir(false);
		vp.setBotonCancelar(false);
		vp.showReporte(rep, this);
	}
	
	
	/**
	 * GETS / SETS
	 */	
	@DependsOn({ "selectedMes", "selectedAnho", "dto" })
	public List<BancoMovimientoDTO> getMovimientos_() throws Exception {	
		Date desde = (Date) this.selectedMes.getPos2();
		Date hasta = (Date) this.selectedMes.getPos3();
		List<BancoMovimientoDTO> out = new ArrayList<BancoMovimientoDTO>();
		AssemblerBancoMovimiento ass = new AssemblerBancoMovimiento();		
		RegisterDomain rr = RegisterDomain.getInstance();
		List<BancoMovimiento> list = rr.getBancoMovimientos(desde, hasta, this.dto.getId());
		for (BancoMovimiento movim : list) {
			BancoMovimientoDTO bdto = (BancoMovimientoDTO) ass.domainToDto(movim);
			out.add(bdto);
		}
		
		double saldo = 0;
		for (BancoMovimientoDTO movim : out) {
			saldo += movim.getDebe() - movim.getHaber();
			movim.setSaldo(saldo);
		}
		
		this.movimientos = out;
		BindUtils.postNotifyChange(null, null, this, "totalDebe");
		BindUtils.postNotifyChange(null, null, this, "totalHaber");
		BindUtils.postNotifyChange(null, null, this, "totalSaldo");
		return out;
	}
	
	/**
	 * @return los meses..
	 */
	@DependsOn("selectedAnho")
	public List<MyArray> getMeses() throws Exception {
		List<MyArray> out = new ArrayList<MyArray>();
		Map<Integer, MyArray> meses = Utiles.getMeses(this.selectedAnho);
		for (Integer nromes : meses.keySet()) {
			out.add(meses.get(nromes));
		}
		return out;
	}
	
	public List<String> getAnhos() {
		return Utiles.getAnhos();
	}
	
	public List<BancoMovimientoDTO> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<BancoMovimientoDTO> movimientos) {
		this.movimientos = movimientos;
	}

	public BancoMovimientoDTO getSelectedMovimento() {
		return selectedMovimento;
	}

	public void setSelectedMovimento(BancoMovimientoDTO selectedMovimento) {
		this.selectedMovimento = selectedMovimento;
	}

	public boolean isTabsVisible() {
		boolean out = true;

		if (this.dto.esNuevo() == true) {
			out = false;
		}

		return out;

	}

	public MyArray getSelectedMes() {
		return selectedMes;
	}

	public void setSelectedMes(MyArray selectedMes) {
		this.selectedMes = selectedMes;
	}
	
	public BancoCtaDTO getDto() {
		return dto;
	}

	public void setDto(BancoCtaDTO dto) {
		this.dto = dto;
	}

	public BancoMovimientoDTO getDtoMovimiento() {
		return dtoMovimiento;
	}

	public void setDtoMovimiento(BancoMovimientoDTO dtoMovimiento) {
		this.dtoMovimiento = dtoMovimiento;
	}

	public String getSelectedAnho() {
		return selectedAnho;
	}

	public void setSelectedAnho(String selectedAnho) throws Exception {
		this.selectedAnho = selectedAnho;
		this.selectedMes = Utiles.getMes((Date) this.selectedMes.getPos2(), this.selectedAnho);
		BindUtils.postNotifyChange(null, null, this, "selectedMes");
	}
}

/**
 * Reporte de Libro Banco..
 */
class ReporteLibroBanco extends ReporteYhaguy {
	
	private String banco;
	private String periodo;
	private String sucursal;
	
	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col1 = new DatosColumnas("Concepto", TIPO_STRING, 50);
	static DatosColumnas col2 = new DatosColumnas("Fecha", TIPO_STRING, 30);
	static DatosColumnas col3 = new DatosColumnas("Número", TIPO_STRING, 30);
	static DatosColumnas col4 = new DatosColumnas("Origen", TIPO_STRING);
	static DatosColumnas col5 = new DatosColumnas("Debe", TIPO_DOUBLE_GS, 40, true);
	static DatosColumnas col6 = new DatosColumnas("Haber", TIPO_DOUBLE_GS, 40, true);
	static DatosColumnas col7 = new DatosColumnas("Saldo", TIPO_DOUBLE_GS, 40, true);
	
	public ReporteLibroBanco(String banco, String periodo, String sucursal) {
		this.banco = banco;
		this.periodo = periodo;
		this.sucursal = sucursal;
	}
	
	static {
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
	}

	@Override
	public void informacionReporte() {
		this.setTitulo("Libro Banco");
		this.setDirectorio("banco");
		this.setNombreArchivo("librobanco-");
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
		this.setBorrarDespuesDeVer(true);
	}
	
	/**
	 * cabecera del reporte..
	 */
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		VerticalListBuilder out = cmp.verticalList();

		out.add(cmp.horizontalFlowList().add(this.texto("")));

		out.add(cmp
				.horizontalFlowList()
				.add(this.textoParValor("Banco", this.banco))
				.add(cmp.horizontalFlowList()
						.add(this.textoParValor("Periodo", this.periodo))
						.add(this.textoParValor("Sucursal", this.sucursal))));

		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}
}
