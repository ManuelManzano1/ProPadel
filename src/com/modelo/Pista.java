package com.modelo;

import javax.validation.constraints.NotEmpty;

public class Pista {
	private int id;
	@NotEmpty(message="El campo no puede estar vacío")
	private String nombre;
	@NotEmpty(message="El campo no puede estar vacío")
	private String localizacion;
	@NotEmpty(message="El campo no puede estar vacío")
	private String imagen;
	@NotEmpty(message="El campo no puede estar vacío")
	private String info;
	
	public Pista() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
