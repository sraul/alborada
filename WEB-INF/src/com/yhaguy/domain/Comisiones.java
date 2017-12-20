package com.yhaguy.domain;

import java.util.Date;

import com.coreweb.domain.Domain;
import com.yhaguy.util.Utiles;

@SuppressWarnings("serial")
public class Comisiones extends Domain {

	private String cedula;
	private String nombre_apellido;
	private String fecha_vencimiento;
	private String nro_cuota;
	private double importe;
	private String vendedor;
	private String celular;
	private String celular_2;
	private String tel_particular;
	private String tel_particular_2;
	private String tel_laboral;
	private String tel_laboral_2;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}
	
	public long getDiasVencimiento() throws Exception {
		return Utiles.diasEntreFechas(Utiles.getFecha(this.fecha_vencimiento, "yyyy-MM-dd"), new Date());
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre_apellido() {
		return nombre_apellido;
	}

	public void setNombre_apellido(String nombre_apellido) {
		this.nombre_apellido = nombre_apellido;
	}

	public String getFecha_vencimiento() {
		return fecha_vencimiento;
	}
	
	public String getFecha_vencimiento_() {
		return fecha_vencimiento.replace("00:00:00", "");
	}

	public void setFecha_vencimiento(String fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}

	public String getNro_cuota() {
		return nro_cuota;
	}

	public void setNro_cuota(String nro_cuota) {
		this.nro_cuota = nro_cuota;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCelular_2() {
		return celular_2;
	}

	public void setCelular_2(String celular_2) {
		this.celular_2 = celular_2;
	}

	public String getTel_particular() {
		return tel_particular;
	}

	public void setTel_particular(String tel_particular) {
		this.tel_particular = tel_particular;
	}

	public String getTel_particular_2() {
		return tel_particular_2;
	}

	public void setTel_particular_2(String tel_particular_2) {
		this.tel_particular_2 = tel_particular_2;
	}

	public String getTel_laboral() {
		return tel_laboral;
	}

	public void setTel_laboral(String tel_laboral) {
		this.tel_laboral = tel_laboral;
	}

	public String getTel_laboral_2() {
		return tel_laboral_2;
	}

	public void setTel_laboral_2(String tel_laboral_2) {
		this.tel_laboral_2 = tel_laboral_2;
	}

}
