package com.yhaguy.gestion.compras.importacion;

import com.coreweb.domain.Domain;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.yhaguy.domain.ImportacionGastoImprevisto;
import com.yhaguy.domain.ImportacionPedidoCompra;
import com.yhaguy.domain.ImportacionPedidoCompraDetalle;
import com.yhaguy.gestion.articulos.AssemblerArticulo;
import com.yhaguy.gestion.empresa.AssemblerProveedor;

public class AssemblerImportacionPedidoCompra extends Assembler {

	private static String[] attIgualesImportacionPedidoCompra = {
			"numeroPedidoCompra", "fechaCreacion", "fechaCierre", "observacion",
			"cambio", "confirmadoImportacion", "confirmadoVentas",
			"confirmadoAdministracion", "propietarioActual",
			"pedidoConfirmado", "importacionConfirmada", "cifProrrateado",
			"totalImporteGs", "totalImporteDs" };

	private static String[] attMoneda = { "descripcion", "sigla" };	
	private static String[] attCondicion = { "descripcion", "plazo" };	
	private static String[] attTipoMovimiento = { "descripcion" };
	private static String[] attSubDiario = { "numero", "fecha", "descripcion", "confirmado"};

	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		ImportacionPedidoCompra domain = (ImportacionPedidoCompra) getDomain(
				dto, ImportacionPedidoCompra.class);

		this.copiarValoresAtributos(dto, domain,
				attIgualesImportacionPedidoCompra);
		this.myArrayToDomain(dto, domain, "proveedorCondicionPago");
		this.myPairToDomain(dto, domain, "estado");
		this.myArrayToDomain(dto, domain, "moneda");
		this.myArrayToDomain(dto, domain, "tipoMovimiento");
		this.myPairToDomain(dto, domain, "tipo");
		this.myPairToDomain(dto, domain, "deposito");
		
		this.listaDTOToListaDomain(dto, domain,
				"importacionPedidoCompraDetalle", true, true, new AssemblerImportacionPedidoCompraDetalle());

		this.listaDTOToListaDomain(dto, domain, "importacionFactura", true, true, new AssemblerImportacionFactura());

		this.listaDTOToListaDomain(dto, domain, "gastosImprevistos", true, true, new AssemblerImportacionGastoImprevisto());
		
		this.hijoDtoToHijoDomain(dto, domain, "proveedor", new AssemblerProveedor(), false);
		this.hijoDtoToHijoDomain(dto, domain, "resumenGastosDespacho", new AssemblerGastosDespacho(), true);

		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {

		ImportacionPedidoCompraDTO dto = (ImportacionPedidoCompraDTO) getDTO(
				domain, ImportacionPedidoCompraDTO.class);

		this.copiarValoresAtributos(domain, dto, attIgualesImportacionPedidoCompra);
		this.domainToMyArray(domain, dto, "proveedorCondicionPago", attCondicion);
		this.domainToMyPair(domain, dto, "estado", "descripcion");
		this.domainToMyArray(domain, dto, "moneda", attMoneda);
		this.domainToMyArray(domain, dto, "tipoMovimiento", attTipoMovimiento);
		this.domainToMyPair(domain, dto, "tipo", "descripcion");
		this.domainToMyPair(domain, dto, "deposito");
		this.domainToMyArray(domain, dto, "subDiario", attSubDiario);
		
		this.listaDomainToListaDTO(domain, dto, "importacionPedidoCompraDetalle",
				new AssemblerImportacionPedidoCompraDetalle());

		this.listaDomainToListaDTO(domain, dto, "importacionFactura", new AssemblerImportacionFactura());
		
		this.listaDomainToListaDTO(domain, dto, "gastosImprevistos", new AssemblerImportacionGastoImprevisto());

		this.hijoDomainToHijoDTO(domain, dto, "proveedor", new AssemblerProveedor());

		this.hijoDomainToHijoDTO(domain, dto, "resumenGastosDespacho", new AssemblerGastosDespacho());

		return dto;
	}
}

// **************************************************************************

class AssemblerImportacionPedidoCompraDetalle extends Assembler {

	private static String[] attIgualesImportacionPedidoCompraDetalle = {
			"cantidad", "ultimoCostoDs", "fechaUltimoCosto", "costoProformaGs", 
			"costoProformaDs", "observacion"};

	@Override
	public Domain dtoToDomain(DTO dtoPD) throws Exception {

		ImportacionPedidoCompraDetalleDTO dto = (ImportacionPedidoCompraDetalleDTO) dtoPD;
		ImportacionPedidoCompraDetalle domain = (ImportacionPedidoCompraDetalle) getDomain(
				dto, ImportacionPedidoCompraDetalle.class);

		this.copiarValoresAtributos(dto, domain,
				attIgualesImportacionPedidoCompraDetalle);
		
		this.hijoDtoToHijoDomain(dto, domain, "articulo",
				new AssemblerArticulo(), false);

		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {

		ImportacionPedidoCompraDetalleDTO dto = (ImportacionPedidoCompraDetalleDTO) getDTO(
				domain, ImportacionPedidoCompraDetalleDTO.class);

		this.copiarValoresAtributos(domain, dto,
				attIgualesImportacionPedidoCompraDetalle);
		
		this.hijoDomainToHijoDTO(domain, dto, "articulo",
				new AssemblerArticulo());

		return dto;
	}

}

class AssemblerImportacionGastoImprevisto extends Assembler{

	private static String[] attIguales = {"importeGs", "importeDs"};
	private static String[] attProveedor = {"codigoEmpresa", "razonSocial", "ruc"};
	
	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		ImportacionGastoImprevisto domain = (ImportacionGastoImprevisto) getDomain(dto, ImportacionGastoImprevisto.class);
		
		this.copiarValoresAtributos(dto, domain, attIguales);
		this.myArrayToDomain(dto, domain, "proveedor");
		
		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		ImportacionGastoImprevistoDTO dto = (ImportacionGastoImprevistoDTO) getDTO(domain, ImportacionGastoImprevistoDTO.class);
		
		this.copiarValoresAtributos(domain, dto, attIguales);
		this.domainToMyArray(domain, dto, "proveedor", attProveedor);
		
		return dto;
	}	
}




