package com.yhaguy.util.migracion.alborada;

import com.coreweb.domain.Tipo;
import com.coreweb.domain.TipoTipo;
import com.coreweb.extras.csv.CSV;
import com.yhaguy.domain.Cliente;
import com.yhaguy.domain.Cobrador;
import com.yhaguy.domain.Cobranza;
import com.yhaguy.domain.CuotaDetalle;
import com.yhaguy.domain.Empresa;
import com.yhaguy.domain.Funcionario;
import com.yhaguy.domain.RegisterDomain;
import com.yhaguy.domain.ServicioContrato;
import com.yhaguy.domain.ServicioContratoDetalle;
import com.yhaguy.domain.ServicioContratoNovedad;
import com.yhaguy.domain.ServicioFicha;
import com.yhaguy.domain.ServicioFichaDetalle;
import com.yhaguy.domain.ServicioPlan;
import com.yhaguy.util.Utiles;

public class MigracionAlborada {
	
	/**
	 * migra los tipos..
	 */
	public static void migrarTipos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/tip_ven.csv";
		String src_tadh = "./WEB-INF/docs/MigracionAlborada/tipben.csv";
		String src_par = "./WEB-INF/docs/MigracionAlborada/parentesco.csv";
		String src_boc = "./WEB-INF/docs/MigracionAlborada/bocacobranza.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "codigo", CSV.STRING }, { "nombre", CSV.STRING } };
		String[][] det_tadh = { { "nombre", CSV.STRING } };
		String[][] det_par = { { "par_descri", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);
		CSV csv_tadh = new CSV(cab, det_tadh, src_tadh);
		CSV csv_par = new CSV(cab, det_par, src_par);
		CSV csv_boc = new CSV(cab, det, src_boc);
		
		TipoTipo tt = new TipoTipo();
		tt.setDescripcion("Formas de Pago Alborada");
		rr.saveObject(tt, "sys");
		
		TipoTipo ttadh = new TipoTipo();
		ttadh.setDescripcion("Tipos de Adherentes");
		rr.saveObject(ttadh, "sys");
		
		TipoTipo ttpar = new TipoTipo();
		ttpar.setDescripcion("Parentesco");
		rr.saveObject(ttpar, "sys");
		
		TipoTipo ttboc = new TipoTipo();
		ttboc.setDescripcion("Bocas de Cobranza");
		rr.saveObject(ttboc, "sys");

		csv.start();
		while (csv.hashNext()) {
			String codigo = csv.getDetalleString("codigo");	
			String nombre = csv.getDetalleString("nombre");	
			Tipo t = new Tipo();
			t.setDescripcion(nombre.toUpperCase());
			t.setSigla(codigo);
			t.setTipoTipo(tt);			
			rr.saveObject(t, "sys");			 
			System.out.println("forma pago: " + nombre.toUpperCase());
		}
		
		csv_tadh.start();
		while (csv_tadh.hashNext()) {
			String nombre = csv_tadh.getDetalleString("nombre");				
			Tipo t = new Tipo();
			t.setDescripcion(nombre.toUpperCase());
			t.setSigla("");
			t.setTipoTipo(ttadh);			
			rr.saveObject(t, "sys");			 
			System.out.println("tipo adherente: " + nombre.toUpperCase());
		}
		
		csv_par.start();
		while (csv_par.hashNext()) {
			String nombre = csv_par.getDetalleString("par_descri");				
			Tipo t = new Tipo();
			t.setDescripcion(nombre.toUpperCase());
			t.setSigla("");
			t.setTipoTipo(ttpar);			
			rr.saveObject(t, "sys");			 
			System.out.println("parentesco: " + nombre.toUpperCase());
		}
		
		csv_boc.start();
		while (csv_boc.hashNext()) {
			String codigo = csv_boc.getDetalleString("codigo");	
			String nombre = csv_boc.getDetalleString("nombre");	
			Tipo t = new Tipo();
			t.setDescripcion(nombre.toUpperCase());
			t.setSigla(codigo);
			t.setTipoTipo(ttboc);			
			rr.saveObject(t, "sys");			 
			System.out.println("boca cobranza: " + nombre.toUpperCase());
		}
	}
	
	/**
	 * migra los cobradores..
	 */
	public static void migrarCobradores() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/cobrador.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "nombre", CSV.STRING }, { "direccion", CSV.STRING }, { "telefono", CSV.STRING },
				{ "comision", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String nombre = csv.getDetalleString("nombre");	
			String direccion = csv.getDetalleString("direccion");	
			String telefono = csv.getDetalleString("telefono");	
			String comision = csv.getDetalleString("comision");	
			
			Cobrador cob = new Cobrador();
			cob.setNombre(nombre.toUpperCase());
			cob.setDireccion(direccion.toUpperCase());
			cob.setTelefono(telefono);
			cob.setComision(Double.parseDouble(comision));
			rr.saveObject(cob, "sys");
			 
			System.out.println("cobrador: " + nombre.toUpperCase());
		}
	}
	
	/**
	 * migracion de planes..
	 */
	public static void migrarPlanes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/planes.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "pla_nombre", CSV.STRING }, { "pla_nro", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);
		csv.start();
		while (csv.hashNext()) {
			String nombre = csv.getDetalleString("pla_nombre");	
			String codigo = csv.getDetalleString("pla_nro");
			ServicioPlan pla = new ServicioPlan();
			pla.setDescripcion(nombre.toUpperCase());
			pla.setCodigo(codigo);
			rr.saveObject(pla, "sys");			 
			System.out.println(nombre);
		}
	}
	
	/**
	 * migracion de funcionarios..
	 */
	public static void migrarFuncionarios() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/vendedor.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "nombre", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);
		csv.start();
		while (csv.hashNext()) {
			String nombre = csv.getDetalleString("nombre");	
			
			Empresa emp = new Empresa();
			emp.setRuc("");
			emp.setRazonSocial(nombre);
			rr.saveObject(emp, "sys");
			
			Funcionario fun = new Funcionario();
			fun.setEmpresa(emp);
			fun.setVendedor(true);
			rr.saveObject(fun, "sys");
			 
			System.out.println(nombre.toUpperCase());
		}
	}
	
	/**
	 * migracion de clientes..
	 */
	public static void migrarClientes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/resp_pagos.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "nro_cedula", CSV.STRING }, { "nombres", CSV.STRING }, { "apellidos", CSV.STRING },
				{ "fecha_nac", CSV.STRING }, { "dir_particular", CSV.STRING }, { "tel_particular", CSV.STRING },
				{ "tel_particular2", CSV.STRING }, { "celular", CSV.STRING }, { "celular2", CSV.STRING },
				{ "zona", CSV.STRING }, { "sub_zona", CSV.STRING }, { "lug_trabajo", CSV.STRING },
				{ "sec_trabajo", CSV.STRING }, { "div_trabajo", CSV.STRING }, { "dir_trabajo", CSV.STRING },
				{ "seccion", CSV.STRING }, { "turno", CSV.STRING }, { "barrio", CSV.STRING }, { "tel_laboral", CSV.STRING }, 
				{ "tel_laboral2", CSV.STRING }, { "nivel_economico", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);
		csv.start();
		while (csv.hashNext()) {
			
			String nro_cedula = csv.getDetalleString("nro_cedula"); String lug_trabajo = csv.getDetalleString("lug_trabajo");
			String nombres = csv.getDetalleString("nombres");	String sec_trabajo = csv.getDetalleString("sec_trabajo");
			String apellidos = csv.getDetalleString("apellidos");	String div_trabajo = csv.getDetalleString("div_trabajo");
			String fecha_nac = csv.getDetalleString("fecha_nac");	String dir_trabajo = csv.getDetalleString("dir_trabajo");
			String dir_particular = csv.getDetalleString("dir_particular");	String seccion = csv.getDetalleString("seccion");
			String tel_particular = csv.getDetalleString("tel_particular");	String turno = csv.getDetalleString("turno");
			String tel_particular2 = csv.getDetalleString("tel_particular2");	String barrio = csv.getDetalleString("barrio");
			String celular = csv.getDetalleString("celular");	String tel_laboral = csv.getDetalleString("tel_laboral");
			String celular2 = csv.getDetalleString("celular2");	String tel_laboral2 = csv.getDetalleString("tel_laboral2");
			String zona = csv.getDetalleString("zona");	String nivel_economico = csv.getDetalleString("nivel_economico");
			String sub_zona = csv.getDetalleString("sub_zona");
			
			Empresa emp = new Empresa();
			emp.setRuc(nro_cedula);
			emp.setRazonSocial(nombres + " " + apellidos);
			rr.saveObject(emp, "sys");
			
			Cliente cli = new Cliente();
			cli.setEmpresa(emp);
			cli.setFecha_nac(Utiles.getFecha(fecha_nac, "yyyy-MM-dd hh:mm:ss"));
			cli.setDir_particular(dir_particular);
			cli.setTel_particular(tel_particular);
			cli.setTel_particular2(tel_particular2);
			cli.setCelular(celular);
			cli.setCelular2(celular2);
			cli.setZona(zona);
			cli.setSub_zona(sub_zona);
			cli.setLug_trabajo(lug_trabajo);
			cli.setSec_trabajo(sec_trabajo);
			cli.setDiv_trabajo(div_trabajo);
			cli.setDir_trabajo(dir_trabajo);
			cli.setSeccion(seccion);
			cli.setTurno(turno);
			cli.setBarrio(barrio);
			cli.setTel_laboral(tel_laboral);
			cli.setTel_laboral2(tel_laboral2);
			cli.setNivel_economico(nivel_economico);
			rr.saveObject(cli, "sys");
			
			System.out.println("cliente: " + nombres);
		}
	}
	
	/**
	 * migracion de clientes..
	 */
	public static void migrarClientesAdherentes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/adherentes.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "nro_adh", CSV.STRING }, { "nombres", CSV.STRING }, { "apellidos", CSV.STRING },
				{ "fecha_nac", CSV.STRING }, { "nro_cedula", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);
		csv.start();
		while (csv.hashNext()) {
			
			String nro_cedula = csv.getDetalleString("nro_cedula"); 
			String nombres = csv.getDetalleString("nombres");	
			String apellidos = csv.getDetalleString("apellidos");	
			String fecha_nac = csv.getDetalleString("fecha_nac");	
			String nro_adh = csv.getDetalleString("nro_adh"); 
			
			Empresa emp = new Empresa();
			emp.setRuc(nro_cedula);
			emp.setRazonSocial(nombres + " " + apellidos);
			rr.saveObject(emp, "sys");
			
			Cliente cli = new Cliente();
			cli.setEmpresa(emp);
			cli.setFecha_nac(fecha_nac.equals("NULL") ? null : Utiles.getFecha(fecha_nac, "yyyy-MM-dd hh:mm:ss"));
			cli.setNro_adh(nro_adh);
			rr.saveObject(cli, "sys");
			
			System.out.println("adherente: " + nombres);
		}
	}
	
	/**
	 * migracion de contratos..
	 */
	public static void migrarContratos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/cab_contratos.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "fecha", CSV.STRING }, { "numero", CSV.STRING }, { "estado", CSV.STRING },
				{ "forma_pago", CSV.STRING }, { "cobrador", CSV.STRING }, { "vendedor", CSV.STRING },
				{ "plan", CSV.STRING }, { "titular", CSV.STRING }, { "titular_ced", CSV.STRING }};
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String fecha = csv.getDetalleString("fecha");	
			String numero = csv.getDetalleString("numero");
			String estado = csv.getDetalleString("estado");
			String forma_pago = csv.getDetalleString("forma_pago");
			String cobrador = csv.getDetalleString("cobrador");
			String vendedor = csv.getDetalleString("vendedor");
			String plan = csv.getDetalleString("plan");
			String titular_ced = csv.getDetalleString("titular_ced");
			
			Tipo fp = rr.getTipoPorDescripcion(forma_pago.toUpperCase());
			Cobrador cob = rr.getCobrador(cobrador);
			Funcionario ven = rr.getFuncionario(vendedor);
			ServicioPlan pla = rr.getServicioPlan(plan);
			Cliente cli = rr.getClienteByRuc(titular_ced);
			
			ServicioContrato con = new ServicioContrato();
			con.setFecha(Utiles.getFecha(fecha, "yyyy-MM-dd hh:mm:ss"));
			con.setNumero(numero);
			con.setEstado(estado.toUpperCase());
			con.setForma_pago(fp);
			con.setCobrador(cob);
			con.setAsesor(ven);
			con.setPlan(pla);
			con.setTitular(cli);
			rr.saveObject(con, "sys");
			
			System.out.println("contrato nro. " + numero);
		}
	}	
	
	/**
	 * migracion de detalle contratos (adherentes)..
	 */
	public static void migrarContratosAdherentes() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/det_contratos_adherentes.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "contrato", CSV.STRING }, { "nro_adh", CSV.STRING }, { "parentesco", CSV.STRING },
				{ "fecha_ingreso", CSV.STRING }, { "estado", CSV.STRING }, { "tipo", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String contrato = csv.getDetalleString("contrato");	
			String nro_adh = csv.getDetalleString("nro_adh");
			String parentesco = csv.getDetalleString("parentesco");
			String fecha_ingreso = csv.getDetalleString("fecha_ingreso");
			String estado = csv.getDetalleString("estado");
			String tipo = csv.getDetalleString("tipo");
			
			Tipo par = rr.getTipoPorDescripcion(parentesco.toUpperCase());
			Tipo tip = rr.getTipoPorDescripcion(tipo.toUpperCase());
			Cliente adh = rr.getClienteByNroAdherente(nro_adh);
			ServicioContrato con = rr.getServicioContrato(contrato);
			
			if (con != null) {
				ServicioContratoDetalle con_det = new ServicioContratoDetalle();
				con_det.setAdherente(adh);
				con_det.setFecha_ingreso(fecha_ingreso.equals("NULL") ? null : Utiles.getFecha(fecha_ingreso, "yyyy-MM-dd hh:mm:ss"));
				con_det.setParentesco(par);
				con_det.setTipo(tip);
				con_det.setEstado(estado);
				con.getDetalles().add(con_det);
				
				rr.saveObject(con, "sys");
				
				System.out.println("detalle adh: " + adh.getRazonSocial());
			} else {
				System.err.println(contrato);
			}
		}
	}
	
	/**
	 * migracion de detalle contratos (cuotas)..
	 */
	public static void migrarContratosCuotas() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/det_contratos_cuotas.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "contrato", CSV.STRING }, { "vencimiento", CSV.STRING }, { "cobrador", CSV.STRING },
				{ "forma_pago", CSV.STRING }, { "nro_cuota", CSV.STRING }, { "importe", CSV.STRING },
				{ "saldo", CSV.STRING }, { "tipo", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String contrato = csv.getDetalleString("contrato");	
			String vencimiento = csv.getDetalleString("vencimiento");
			String cobrador = csv.getDetalleString("cobrador");
			String forma_pago = csv.getDetalleString("forma_pago");
			String nro_cuota = csv.getDetalleString("nro_cuota");
			String importe = csv.getDetalleString("importe");
			String saldo = csv.getDetalleString("saldo");
			String tipo = csv.getDetalleString("tipo");
			
			Tipo fp = rr.getTipoPorDescripcion(forma_pago.toUpperCase());
			Cobrador cob = rr.getCobrador(cobrador);
			ServicioContrato con = rr.getServicioContrato(contrato);
			
			if (con != null) {
				CuotaDetalle cuota = new CuotaDetalle();
				cuota.setVencimiento(vencimiento.equals("NULL") ? null : Utiles.getFecha(vencimiento, "yyyy-MM-dd hh:mm:ss"));
				cuota.setCobrador(cob);
				cuota.setForma_pago(fp);
				cuota.setImporteGs(Double.parseDouble(importe));
				cuota.setNro_cuota(Integer.parseInt(nro_cuota));
				cuota.setSaldoGs(Double.parseDouble(saldo));
				cuota.setTipo(tipo);
				con.getDetalle_cuotas().add(cuota);
				rr.saveObject(con, "sys");
				
				System.out.println("cuota: " + contrato);
			} else {
				System.err.println(contrato);
			}
		}
	}
	
	/**
	 * migracion de novedades de contratos..
	 */
	public static void migrarContratosNovedades() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/nov_contra.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "fecha", CSV.STRING }, { "tipo", CSV.STRING }, { "estado", CSV.STRING },
				{ "comentario", CSV.STRING }, { "conplan", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String fecha = csv.getDetalleString("fecha");	
			String tipo = csv.getDetalleString("tipo");
			String estado = csv.getDetalleString("estado");
			String comentario = csv.getDetalleString("comentario");
			String conplan = csv.getDetalleString("conplan");
			
			ServicioContrato con = rr.getServicioContrato(conplan);
			
			if (con != null) {
				ServicioContratoNovedad nov = new ServicioContratoNovedad();
				nov.setComentario(comentario.toUpperCase());
				nov.setEstado(estado.equals("NULL") ? "" : estado.toUpperCase());
				nov.setFecha(fecha.equals("NULL") ? null : Utiles.getFecha(fecha, "yyyy-MM-dd hh:mm:ss"));
				nov.setTipo(tipo);
				con.getNovedades().add(nov);
				rr.saveObject(con, "sys");
				
				System.out.println("novedad: (" + comentario.length() + ") " + comentario.toUpperCase());
			} else {
				System.err.println("novedad: " + comentario.toUpperCase());
			}
		}
	}
	
	/**
	 * migracion de fichas de usuarios..
	 */
	public static void migrarFichas() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/fichas.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "nroficha", CSV.STRING }, { "sucursal", CSV.STRING }, { "nombre", CSV.STRING },
				{ "apellido", CSV.STRING }, { "telefono", CSV.STRING }, { "tipopaciente", CSV.STRING },
				{ "nro_adh", CSV.STRING }, { "aniversa", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String nroficha = csv.getDetalleString("nroficha");	
			String sucursal = csv.getDetalleString("sucursal");
			String telefono = csv.getDetalleString("telefono");
			String nro_adh = csv.getDetalleString("nro_adh");
			
			Cliente adh = rr.getClienteByNroAdherente(nro_adh);
			
			if (adh != null) {
				ServicioFicha ficha = new ServicioFicha();
				ficha.setNumero(nroficha);
				ficha.setSucursal(sucursal);
				ficha.setTelefono(telefono);
				ficha.setCliente(adh);
				
				rr.saveObject(ficha, "sys");
				
				System.out.println("ficha: " + nroficha);
			} else {
				System.err.println(nroficha);
			}
		}
	}
	
	/**
	 * migracion de detalle fichas..
	 */
	public static void migrarFichasDetalles() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/controlatencion.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "nroficha", CSV.STRING }, { "fecha", CSV.STRING }, { "diente", CSV.STRING },
				{ "tratamiento", CSV.STRING }, { "doctor", CSV.STRING }, { "tipo1", CSV.STRING },
				{ "tipo2", CSV.STRING }, { "tipo3", CSV.STRING }, { "tipo4", CSV.STRING }, { "pago", CSV.STRING },
				{ "presupuesto", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String nroficha = csv.getDetalleString("nroficha");	
			String fecha = csv.getDetalleString("fecha");
			String diente = csv.getDetalleString("diente");
			String tratamiento = csv.getDetalleString("tratamiento");
			String doctor = csv.getDetalleString("doctor");
			String tipo1 = csv.getDetalleString("tipo1");
			String tipo2 = csv.getDetalleString("tipo2");
			String tipo3 = csv.getDetalleString("tipo3");
			String tipo4 = csv.getDetalleString("tipo4");
			String pago = csv.getDetalleString("pago");
			String presupuesto = csv.getDetalleString("presupuesto");
			
			ServicioFicha ficha = rr.getServicioFicha(nroficha);
			
			if (ficha != null) {
				ServicioFichaDetalle detalle = new ServicioFichaDetalle();
				detalle.setFecha(fecha.equals("NULL") ? null : Utiles.getFecha(fecha, "yyyy-MM-dd hh:mm:ss"));
				detalle.setDiente(Integer.parseInt(diente));
				detalle.setDoctor(doctor.toUpperCase());
				detalle.setPagoGs(Double.parseDouble(pago));
				detalle.setPresupuestoGs(Double.parseDouble(presupuesto));
				detalle.setTipo_1(tipo1.equals("0") ? false : true);
				detalle.setTipo_2(tipo2.equals("0") ? false : true);
				detalle.setTipo_3(tipo3.equals("0") ? false : true);
				detalle.setTipo_4(tipo4.equals("0") ? false : true);
				detalle.setTratamiento(tratamiento.toUpperCase());
				ficha.getDetalles().add(detalle);
				rr.saveObject(ficha, "sys");
				
				System.out.println("ficha detalle: " + nroficha);
			} else {
				System.err.println(nroficha);
			}
		}
	}
	
	/**
	 * migracion de cobranzas..
	 */
	public static void migrarCobranzas() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/cab_cobranzas.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "cliecod", CSV.STRING }, { "fecha", CSV.STRING }, { "cobrador", CSV.STRING },
				{ "total", CSV.STRING }, { "obs", CSV.STRING }, { "bocacobranza", CSV.STRING },
				{ "nrofact", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String cliecod = csv.getDetalleString("cliecod");	
			String fecha = csv.getDetalleString("fecha");
			String cobrador = csv.getDetalleString("cobrador");
			String total = csv.getDetalleString("total");
			String obs = csv.getDetalleString("obs");
			String bocacobranza = csv.getDetalleString("bocacobranza");
			String nrofact = csv.getDetalleString("nrofact");
			
			Cliente cli = rr.getClienteByRuc(cliecod);
			Tipo bocaCob = null; if(!bocacobranza.equals("NULL")) bocaCob = rr.getTipoPorSigla(bocacobranza);
			Cobrador cobrador_ = rr.getCobrador(cobrador.toUpperCase());
			
			if (cli != null) {
				Cobranza cob = new Cobranza();
				cob.setCliente(cli);
				cob.setBocaCobranza(bocaCob);				
				cob.setCobrador(cobrador_);
				cob.setFecha(fecha.equals("NULL") ? null : Utiles.getFecha(fecha, "yyyy-MM-dd"));
				cob.setImporteGs(Double.parseDouble(total));
				cob.setNumero(nrofact.equals("NULL")? "- - -" : nrofact);
				cob.setObservacion(obs.toUpperCase());
				rr.saveObject(cob, "sys");
				
				System.out.println("cobranza: " + cob.getNumero());
			} else {
				System.err.println("cobranza: " + nrofact);
			}
		}
	}
	
	/**
	 * migracion de detalle cobranzas..
	 */
	public static void migrarCobranzasDetalles() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		String src = "./WEB-INF/docs/MigracionAlborada/det_cobranzas.csv";
		
		String[][] cab = { { "Empresa", CSV.STRING } };
		String[][] det = { { "tipo", CSV.STRING }, { "nro_cuota", CSV.STRING }, { "importe", CSV.STRING },
				{ "con_plan", CSV.STRING } };
		
		CSV csv = new CSV(cab, det, src);

		csv.start();
		while (csv.hashNext()) {

			String nroficha = csv.getDetalleString("nroficha");	
			String fecha = csv.getDetalleString("fecha");
			String diente = csv.getDetalleString("diente");
			String tratamiento = csv.getDetalleString("tratamiento");
			String doctor = csv.getDetalleString("doctor");
			String tipo1 = csv.getDetalleString("tipo1");
			String tipo2 = csv.getDetalleString("tipo2");
			String tipo3 = csv.getDetalleString("tipo3");
			String tipo4 = csv.getDetalleString("tipo4");
			String pago = csv.getDetalleString("pago");
			String presupuesto = csv.getDetalleString("presupuesto");
			
			ServicioFicha ficha = rr.getServicioFicha(nroficha);
			
			if (ficha != null) {
				ServicioFichaDetalle detalle = new ServicioFichaDetalle();
				detalle.setFecha(fecha.equals("NULL") ? null : Utiles.getFecha(fecha, "yyyy-MM-dd hh:mm:ss"));
				detalle.setDiente(Integer.parseInt(diente));
				detalle.setDoctor(doctor.toUpperCase());
				detalle.setPagoGs(Double.parseDouble(pago));
				detalle.setPresupuestoGs(Double.parseDouble(presupuesto));
				detalle.setTipo_1(tipo1.equals("0") ? false : true);
				detalle.setTipo_2(tipo2.equals("0") ? false : true);
				detalle.setTipo_3(tipo3.equals("0") ? false : true);
				detalle.setTipo_4(tipo4.equals("0") ? false : true);
				detalle.setTratamiento(tratamiento.toUpperCase());
				ficha.getDetalles().add(detalle);
				rr.saveObject(ficha, "sys");
				
				System.out.println("ficha detalle: " + nroficha);
			} else {
				System.err.println(nroficha);
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			//MigracionAlborada.migrarTipos();
			//MigracionAlborada.migrarCobradores();
			//MigracionAlborada.migrarPlanes();
			//MigracionAlborada.migrarFuncionarios();
			//MigracionAlborada.migrarClientes();
			//MigracionAlborada.migrarClientesAdherentes();
			//MigracionAlborada.migrarContratos();
			//MigracionAlborada.migrarContratosAdherentes();
			//MigracionAlborada.migrarContratosCuotas();
			//MigracionAlborada.migrarFichas();
			//MigracionAlborada.migrarFichasDetalles();
			//MigracionAlborada.migrarContratosNovedades();
			MigracionAlborada.migrarCobranzas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
