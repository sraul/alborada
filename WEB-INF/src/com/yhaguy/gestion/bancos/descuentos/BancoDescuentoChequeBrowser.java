package com.yhaguy.gestion.bancos.descuentos;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.Config;
import com.coreweb.extras.browser.Browser;
import com.coreweb.extras.browser.ColumnaBrowser;
import com.yhaguy.domain.BancoDescuentoCheque;

public class BancoDescuentoChequeBrowser extends Browser {
	
	private String where = "";
	
	public BancoDescuentoChequeBrowser(String where) {
		this.where = "(" + where + ")";
	}
	
	@Override
	public void setingInicial() {
		this.setWidthWindows("1024px");
		this.setHigthWindows("480px");
		this.addOrden("fecha");
	}

	@Override
	public List<ColumnaBrowser> getColumnasBrowser() {
		// TODO Auto-generated method stub

		ColumnaBrowser col1 = new ColumnaBrowser();
		ColumnaBrowser col2 = new ColumnaBrowser();
		ColumnaBrowser col3 = new ColumnaBrowser();
		ColumnaBrowser col5 = new ColumnaBrowser();
		ColumnaBrowser col6 = new ColumnaBrowser();

		col1.setCampo("id");
		col1.setTitulo("ID");
		col1.setTipo(LONG_BOX);
		
		col2.setCampo("fecha");
		col2.setTitulo("Fecha");
		col2.setTipo(Config.TIPO_DATE);
		col2.setComponente(Browser.LABEL_DATE);
		
		col3.setCampo("totalChequesDescontado");
		col3.setTitulo("Total Cheques");
		col3.setTipo(LABEL_NUMERICO);
		
		col5.setCampo("observacion");
		col5.setTitulo("Detalles");
		
		col6.setCampo("auxi");
		col6.setWhere(this.where);
		col6.setWidthColumna("0px");
		
		List<ColumnaBrowser> columnas = new ArrayList<ColumnaBrowser>();
		columnas.add(col2);
		columnas.add(col5);
		columnas.add(col6);
		return columnas;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntidadPrincipal() {
		return BancoDescuentoCheque.class;
	}

	@Override
	public String getTituloBrowser() {
		return "Descuento de Cheques";
	}

}
