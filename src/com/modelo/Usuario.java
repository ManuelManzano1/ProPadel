package com.modelo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class Usuario {
	@NotEmpty(message="El campo no puede estar vacío")
	@Email(message="no es correo válido")
	private String email;
	@NotEmpty(message="El campo no puede estar vacío")
	private String clave;
	@NotEmpty(message="El campo no puede estar vacío")
	private String usuario;
	private String tipo;
	@NotEmpty(message="El campo no puede estar vacío")
	private String nombre;
	@NotEmpty(message="El campo no puede estar vacío")
	private String apellidos;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	
	
	
}
