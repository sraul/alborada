package com.yhaguy.gestion.contabilidad;

import com.coreweb.domain.Domain;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.yhaguy.domain.ContableAsiento;
import com.yhaguy.domain.ContableAsientoDetalle;

public class ContableAsientoAssembler extends Assembler {
	
	static String[] attIguales = new String[] { "fecha", "numero", "descripcion" };

	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		ContableAsiento domain = (ContableAsiento) getDomain(dto, ContableAsiento.class);		
		this.copiarValoresAtributos(dto, domain, attIguales);		
		
		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		ContableAsientoDTO dto = (ContableAsientoDTO) getDTO(domain, ContableAsientoDTO.class);
		this.copiarValoresAtributos(domain, dto, attIguales);
		this.listaDomainToListaDTO(domain, dto, "detalles", new ContableAsientoDetalleAssembler());
		return dto;
	}

}

class ContableAsientoDetalleAssembler extends Assembler {
	
	static String[] attIguales = new String[] { "descripcion", "debe", "haber" };

	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		ContableAsientoDetalle domain = (ContableAsientoDetalle) getDomain(dto, ContableAsientoDetalle.class);
		
		this.copiarValoresAtributos(dto, domain, attIguales);
		this.myPairToDomain(dto, domain, "cuenta");
		
		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		ContableAsientoDetalleDTO dto = (ContableAsientoDetalleDTO) getDTO(domain, ContableAsientoDetalleDTO.class);
		
		this.copiarValoresAtributos(domain, dto, attIguales);
		this.domainToMyPair(domain, dto, "cuenta");
		
		return dto;
	}
	
}
