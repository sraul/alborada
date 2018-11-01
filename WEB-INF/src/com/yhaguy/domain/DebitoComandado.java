package com.yhaguy.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.DependsOn;

import com.coreweb.domain.Domain;
import com.yhaguy.util.Utiles;

@SuppressWarnings("serial")
public class DebitoComandado extends Domain {
	
	public static final String TIPO_DEBITO = "D";
	public static final String TIPO_CREDITO = "C";
	public static final String TIPO_CHEQUE = "H";
	public static final String TIPO_COBRO = "F";
	
	public static final String MONEDA_GS = "0";
	public static final String MONEDA_DS = "1";

	private String ITIREG;
	private String ITITRA = "09";
	private String ICDSRV = "EA5";
	private String ICTDEB;
	private String IBCOCR = "017";
	private String ICTCRE = "29450239";
	private String ITCRDB = "C";
	private String IORDEN;
	private String IMONED = "0";
	private String IMTOTR;
	private String IMTOT2;
	private String INRODO;
	private String ITIFAC;
	private String INRFAC;
	private String INRCUO;
	private String IFCHCR;
	private String IFCHC2;
	private String ICEPTO = "PAGO DE CUOTAS";
	private String INRREF;
	private String IFECCA;
	private String IHORCA;
	private String IUSUCA;
	private Date IFCHCR_;
	
	private Cliente paciente;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}
	
	@DependsOn("ITCRDB")
	public String getTipo() {
		if(this.ITCRDB == null) return "";
		return this.ITCRDB.equals(TIPO_DEBITO)? "DEBITO" : this.ITCRDB.equals(TIPO_CREDITO)? "CREDITO" : this.ITCRDB.equals(TIPO_CHEQUE)? "CHEQUE" : this.ITCRDB.equals(TIPO_COBRO)? "COBRO" : "";
	}
	
	@DependsOn("IMONED")
	public String getMoneda() {
		if(this.IMONED == null) return "";
		return this.IMONED.equals(MONEDA_GS)? "GUARANIES" : this.IMONED.equals(MONEDA_DS)? "DOLARES" : "";
	}
	
	/**
	 * @return los tipos..
	 */
	public List<String> getTipos() {
		List<String> out = new ArrayList<String>();
		out.add(TIPO_DEBITO);
		out.add(TIPO_CREDITO);
		out.add(TIPO_CHEQUE);
		out.add(TIPO_COBRO);
		return out;
	}
	
	/**
	 * @return las monedas..
	 */
	public List<String> getMonedas() {
		List<String> out = new ArrayList<String>();
		out.add(MONEDA_GS);
		out.add(MONEDA_DS);
		return out;
	}
	
	@DependsOn("ICTDEB")
	public String getICTDEB_() {
		if(this.ICTDEB == null) return StringUtils.leftPad("", 10, "0");
		return StringUtils.leftPad(this.ICTDEB, 10, "0");
	}
	
	@DependsOn("ICTCRE")
	public String getICTCRE_() {
		if(this.ICTCRE == null) return StringUtils.leftPad("", 10, "0");
		return StringUtils.leftPad(this.ICTCRE, 10, "0");
	}
	
	@DependsOn("IORDEN")
	public String getIORDEN_() {
		if(this.IORDEN == null) return StringUtils.leftPad("", 50, " ");
		return StringUtils.rightPad(this.IORDEN, 50, " ");
	}
	
	@DependsOn("IMTOTR")
	public String getIMTOTR_() {
		if(this.IMTOTR == null) return StringUtils.leftPad("", 15, "0");
		return StringUtils.leftPad(new Double(this.IMTOTR).intValue() + "", 13, "0") + "00";
	}

	@DependsOn("IMTOT2")
	public String getIMTOT2_() {
		if(this.IMTOT2 == null) return StringUtils.leftPad("", 15, "0");
		return StringUtils.leftPad(new Double(this.IMTOT2).intValue() + "", 13, "0") + "00";
	}
	
	@DependsOn("INRODO")
	public String getINRODO_() {
		if(this.INRODO == null) return StringUtils.leftPad("", 12, " ");
		return StringUtils.rightPad(this.INRODO, 12, " ");
	}
	
	@DependsOn("ITIFAC")
	public String getITIFAC_() {
		if(this.ITIFAC == null) return StringUtils.leftPad("", 1, "0");
		return StringUtils.leftPad(this.ITIFAC, 1, "0");
	}
	
	@DependsOn("INRFAC")
	public String getINRFAC_() {
		if(this.INRFAC == null) return StringUtils.leftPad("", 20, " ");
		return StringUtils.rightPad(this.INRFAC, 20, " ");
	}
	
	@DependsOn("INRCUO")
	public String getINRCUO_() {
		if(this.INRCUO == null) return StringUtils.leftPad("", 3, "0");
		return StringUtils.leftPad(this.INRCUO, 3, "0");
	}
	
	@DependsOn("IFCHCR")
	public String getIFCHCR__() {
		if(this.IFCHCR == null) return StringUtils.leftPad("", 8, "0");
		return StringUtils.leftPad(this.IFCHCR, 8, "0");
	}
	
	@DependsOn("IFCHC2")
	public String getIFCHC2_() {
		if(this.IFCHC2 == null) return StringUtils.leftPad("", 8, "0");
		return StringUtils.leftPad(this.IFCHC2, 8, "0");
	}
	
	@DependsOn("ICEPTO")
	public String getICEPTO_() {
		if(this.ICEPTO == null) return StringUtils.leftPad("", 50, " ");
		return StringUtils.rightPad(this.ICEPTO, 50, " ");
	}
	
	@DependsOn("INRREF")
	public String getINRREF_() {
		if(this.INRREF == null) return StringUtils.leftPad("", 15, " ");
		return StringUtils.rightPad(this.INRREF, 15, " ");
	}
	
	@DependsOn("IFECCA")
	public String getIFECCA_() {
		if(this.IFECCA == null) return StringUtils.leftPad("", 8, "0");
		return StringUtils.leftPad(this.IFECCA, 8, "0");
	}
	
	@DependsOn("IHORCA")
	public String getIHORCA_() {
		if(this.IHORCA == null) return StringUtils.leftPad("", 6, "0");
		return StringUtils.leftPad(this.IHORCA, 6, "0");
	}
	
	@DependsOn("IUSUCA")
	public String getIUSUCA_() {
		if(this.IUSUCA == null) return StringUtils.leftPad("", 10, " ");
		return StringUtils.rightPad(this.IUSUCA, 10, " ");
	}
	
	public String getITIREG() {
		return ITIREG;
	}

	public void setITIREG(String iTIREG) {
		ITIREG = iTIREG;
	}

	public String getITITRA() {
		return ITITRA;
	}

	public void setITITRA(String iTITRA) {
		ITITRA = iTITRA;
	}

	public String getICDSRV() {
		return ICDSRV;
	}

	public void setICDSRV(String iCDSRV) {
		ICDSRV = iCDSRV;
	}

	public String getICTDEB() {
		return ICTDEB;
	}

	public void setICTDEB(String iCTDEB) {
		ICTDEB = iCTDEB;
	}

	public String getIBCOCR() {
		return IBCOCR;
	}

	public void setIBCOCR(String iBCOCR) {
		IBCOCR = iBCOCR;
	}

	public String getICTCRE() {
		return ICTCRE;
	}

	public void setICTCRE(String iCTCRE) {
		ICTCRE = iCTCRE;
	}

	public String getITCRDB() {
		return ITCRDB;
	}

	public void setITCRDB(String iTCRDB) {
		ITCRDB = iTCRDB;
	}

	public String getIORDEN() {
		return IORDEN;
	}

	public void setIORDEN(String iORDEN) {
		IORDEN = iORDEN;
	}

	public String getIMONED() {
		return IMONED;
	}

	public void setIMONED(String iMONED) {
		IMONED = iMONED;
	}

	public String getIMTOTR() {
		return IMTOTR;
	}

	public void setIMTOTR(String iMTOTR) {
		IMTOTR = iMTOTR;
	}

	public String getIMTOT2() {
		return IMTOT2;
	}

	public void setIMTOT2(String iMTOT2) {
		IMTOT2 = iMTOT2;
	}

	public String getINRODO() {
		return INRODO;
	}

	public void setINRODO(String iNRODO) {
		INRODO = iNRODO;
	}

	public String getITIFAC() {
		return ITIFAC;
	}

	public void setITIFAC(String iTIFAC) {
		ITIFAC = iTIFAC;
	}

	public String getINRFAC() {
		return INRFAC;
	}

	public void setINRFAC(String iNRFAC) {
		INRFAC = iNRFAC;
	}

	public String getINRCUO() {
		return INRCUO;
	}

	public void setINRCUO(String iNRCUO) {
		INRCUO = iNRCUO;
	}

	public String getIFCHCR() {
		return IFCHCR;
	}

	public void setIFCHCR(String iFCHCR) {
		IFCHCR = iFCHCR;
	}

	public String getIFCHC2() {
		return IFCHC2;
	}

	public void setIFCHC2(String iFCHC2) {
		IFCHC2 = iFCHC2;
	}

	public String getICEPTO() {
		return ICEPTO;
	}

	public void setICEPTO(String iCEPTO) {
		ICEPTO = iCEPTO;
	}

	public String getINRREF() {
		return INRREF;
	}

	public void setINRREF(String iNRREF) {
		INRREF = iNRREF;
	}

	public String getIFECCA() {
		return IFECCA;
	}

	public void setIFECCA(String iFECCA) {
		IFECCA = iFECCA;
	}

	public String getIHORCA() {
		return IHORCA;
	}

	public void setIHORCA(String iHORCA) {
		IHORCA = iHORCA;
	}

	public String getIUSUCA() {
		return IUSUCA;
	}

	public void setIUSUCA(String iUSUCA) {
		IUSUCA = iUSUCA;
	}

	public Cliente getPaciente() {
		return paciente;
	}

	public void setPaciente(Cliente paciente) {
		this.paciente = paciente;
	}

	public Date getIFCHCR_() {
		return IFCHCR_;
	}

	public void setIFCHCR_(Date iFCHCR_) {
		IFCHCR_ = iFCHCR_;
		this.IFCHCR = Utiles.getDateToString(iFCHCR_, Utiles.YYYYMMDD);
	}

}
