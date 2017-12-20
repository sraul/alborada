package com.yhaguy.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class Cliente extends Domain {
	
	private boolean ventaCredito;
	private double limiteCredito;
	
	private Date fecha_nac;
	private String dir_particular;	
	private String tel_particular;
	private String tel_particular2;	
	private String celular;
	private String celular2;
	private String tel_laboral;	
	private String tel_laboral2;
	private String zona;
	private String sub_zona;	
	private String lug_trabajo;
	private String sec_trabajo;
	private String div_trabajo;
	private String dir_trabajo;
	private String seccion;
	private String turno;
	private String barrio;
	private String nivel_economico;
	private String nro_adh;
	
	private Empresa empresa;
	private Tipo estadoCliente;
	private Tipo categoriaCliente;
	private Tipo tipoCliente;
	private CuentaContable cuentaContable;
	private Set<ContactoInterno> contactosInternos = new HashSet<ContactoInterno>();
	private Funcionario cobrador;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}	
	
	/**
	 * @return las fichas..
	 */
	public List<ServicioFicha> getFichas() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getFichas(this.getId());
	}
	
	/**
	 * @return los contratos vigentes del paciente..
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioContrato> getContratosVigentes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getObjects(ServicioContrato.class.getName());
	}
	
	/**
	 * @return los cheques rechazados del cliente..
	 */
	public List<BancoChequeTercero> getChequesRechazados() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getChequesRechazados(this.id);
	}
	
	/**
	 * @return la descripcion del rubro..
	 */
	public String getRubro() {
		String out = "NO DEFINIDO";
		if (this.empresa.getRubroEmpresas().size() > 0) {
			for (Tipo rubro : this.empresa.getRubroEmpresas()) {
				out = rubro.getDescripcion();
			}
		}
		return out;
	}
	
	public String getDescripcion() {
		return this.empresa.getRazonSocial();
	}
	
	public void setDescripcion(String descripcion) {
	}

	public String getNombre() {
		return this.getEmpresa().getNombre();
	}

	public void setNombre(String nombre) {
	}
	
	public String getDireccion() {
		return this.empresa.getDireccion_();
	}
	
	public void setDireccion(String direccion) {
	}
	
	public void setTelefono(String direccion) {
	}

	public String getCodigoEmpresa() {
		return this.empresa.getCodigoEmpresa();
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
	}
	
	public boolean isCuentaBloqueada() {
		return this.empresa.isCuentaBloqueada();
	}
	
	public void setCuentaBloqueada(boolean cuentabloqueada) {
	}

	public Tipo getEstadoCliente() {
		return estadoCliente;
	}

	public void setEstadoCliente(Tipo estadoCliente) {
		this.estadoCliente = estadoCliente;
	}

	public Tipo getCategoriaCliente() {
		return categoriaCliente;
	}

	public void setCategoriaCliente(Tipo categoriaCliente) {
		this.categoriaCliente = categoriaCliente;
	}

	public Tipo getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(Tipo tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public CuentaContable getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(CuentaContable cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public Set<ContactoInterno> getContactosInternos() {
		return contactosInternos;
	}

	public void setContactosInternos(Set<ContactoInterno> vendedores) {
		this.contactosInternos = vendedores;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public Set<Sucursal> getSucursales() {
		Set<Sucursal> sucursales = this.empresa.getSucursales();
		return sucursales;
	}

	public Set<Contacto> getContactos() {
		Set<Contacto> contactos = this.empresa.getContactos();
		return contactos;
	}

	public long getIdEmpresa() {
		return empresa.getId();
	}

	public void setIdEmpresa(long idEmpresa) {
	}

	public String getRazonSocial() {
		String razonSocial = this.empresa.getRazonSocial();
		return razonSocial.toUpperCase();
	}

	public void setRazonSocial(String razonSocial) {
		this.empresa.setRazonSocial(razonSocial);
	}
	
	public String getNombreFantasia() {
		return this.empresa.getNombre();
	}
	
	public void setNombreFantasia(String nombre) {
	}

	public String getRuc() {
		String ruc = this.empresa.getRuc();
		return ruc;
	}

	public void setRuc(String ruc) {
		this.empresa.setRuc(ruc);
	}
	
	public Date getFechaIngreso() {
		Date fechaIngreso = this.empresa.getFechaIngreso();
		return fechaIngreso;
	}

	public String getDescripcionEstado() {
		String descripcion = this.estadoCliente.getDescripcion();
		return descripcion;
	}

	public String getDescripcionCategoria() {
		String descripcion = this.categoriaCliente.getDescripcion();
		return descripcion;
	}

	public String getDescripcionTipoCliente() {
		String descripcion = this.tipoCliente.getDescripcion();
		return descripcion;
	}

	@SuppressWarnings("rawtypes")
	public String getDescripcionRubroEmpresa() {
		String out = "";
		Set<Tipo> sr = this.empresa.getRubroEmpresas();
		for (Iterator iterator = sr.iterator(); iterator.hasNext();) {
			Tipo r = (Tipo) iterator.next();
			out += "[" + r.getDescripcion() + "] ";

		}
		return out.trim();
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void getRuc(String ruc) {
		ruc = this.empresa.getRuc();
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public boolean isVentaCredito() {
		return ventaCredito;
	}

	public void setVentaCredito(boolean ventaCredito) {
		this.ventaCredito = ventaCredito;
	}

	public Funcionario getCobrador() {
		return cobrador;
	}

	public void setCobrador(Funcionario cobrador) {
		this.cobrador = cobrador;
	}

	public Date getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(Date fecha_nac) {
		this.fecha_nac = fecha_nac;
	}

	public String getDir_trabajo() {
		return dir_trabajo;
	}

	public void setDir_trabajo(String dir_trabajo) {
		this.dir_trabajo = dir_trabajo;
	}

	public String getDir_particular() {
		return dir_particular;
	}

	public void setDir_particular(String dir_particular) {
		this.dir_particular = dir_particular;
	}

	public String getSeccion() {
		return seccion;
	}

	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	public String getTel_particular() {
		return tel_particular;
	}

	public void setTel_particular(String tel_particular) {
		this.tel_particular = tel_particular;
	}

	public String getTel_particular2() {
		return tel_particular2;
	}

	public void setTel_particular2(String tel_particular2) {
		this.tel_particular2 = tel_particular2;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTel_laboral() {
		return tel_laboral;
	}

	public void setTel_laboral(String tel_laboral) {
		this.tel_laboral = tel_laboral;
	}

	public String getCelular2() {
		return celular2;
	}

	public void setCelular2(String celular2) {
		this.celular2 = celular2;
	}

	public String getTel_laboral2() {
		return tel_laboral2;
	}

	public void setTel_laboral2(String tel_laboral2) {
		this.tel_laboral2 = tel_laboral2;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getNivel_economico() {
		return nivel_economico;
	}

	public void setNivel_economico(String nivel_economico) {
		this.nivel_economico = nivel_economico;
	}

	public String getSub_zona() {
		return sub_zona;
	}

	public void setSub_zona(String sub_zona) {
		this.sub_zona = sub_zona;
	}

	public String getLug_trabajo() {
		return lug_trabajo;
	}

	public void setLug_trabajo(String lug_trabajo) {
		this.lug_trabajo = lug_trabajo;
	}

	public String getSec_trabajo() {
		return sec_trabajo;
	}

	public void setSec_trabajo(String sec_trabajo) {
		this.sec_trabajo = sec_trabajo;
	}

	public String getDiv_trabajo() {
		return div_trabajo;
	}

	public void setDiv_trabajo(String div_trabajo) {
		this.div_trabajo = div_trabajo;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getNro_adh() {
		return nro_adh;
	}

	public void setNro_adh(String nro_adh) {
		this.nro_adh = nro_adh;
	}
}
