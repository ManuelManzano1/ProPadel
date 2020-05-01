package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.modelo.Favorita;
import com.modelo.Imagen;
import com.modelo.Pista;
import com.modelo.Reserva;
import com.modelo.Usuario;


public class Dao {
	 JdbcTemplate template;

	 public JdbcTemplate getTemplate() {
	 	return template;
	 }

	 public void setTemplate(JdbcTemplate template) {
	 	this.template = template;
	 }

	public List<Usuario> comprobarLogin(Usuario u) {
		return template.query("select * from user where usuario='"+u.getUsuario()+"' and clave='"+u.getClave()+"'", new RowMapper<Usuario>() {
			@Override
			public Usuario mapRow(ResultSet rs, int row) throws SQLException
			{
				Usuario u=new Usuario();
				u.setUsuario(rs.getString(1));
				u.setClave(rs.getString(2));
				u.setEmail(rs.getString(3));
				u.setNombre(rs.getString(4));
				u.setApellidos(rs.getString(5));
				u.setTipo(rs.getString(6));
				return u;
			}
			
			
		}
		);
	}

	public int registrarUsuario(Usuario u) {
		return template.update("insert into user(usuario,clave,email,nombre,apellidos,tipo) values(?,?,?,?,?,'U')",u.getUsuario(),u.getClave(),u.getEmail(),u.getNombre(),u.getApellidos());
	}

	public List<Usuario> comprobarEmail(Usuario u) {
		return template.query("select * from user where email='"+u.getEmail()+"'", new RowMapper<Usuario>() {
			@Override
			public Usuario mapRow(ResultSet rs, int row) throws SQLException
			{
				Usuario u=new Usuario();
				u.setUsuario(rs.getString(1));
				u.setClave(rs.getString(2));
				u.setEmail(rs.getString(3));
				u.setNombre(rs.getString(4));
				u.setApellidos(rs.getString(5));
				u.setTipo(rs.getString(6));
				return u;
			}
			
			
		}
		);
	}
	public List<Usuario> comprobarUsuario(Usuario u) {
		return template.query("select * from user where usuario='"+u.getUsuario()+"'", new RowMapper<Usuario>() {
			@Override
			public Usuario mapRow(ResultSet rs, int row) throws SQLException
			{
				Usuario u=new Usuario();
				u.setUsuario(rs.getString(1));
				u.setClave(rs.getString(2));
				u.setEmail(rs.getString(3));
				u.setNombre(rs.getString(4));
				u.setApellidos(rs.getString(5));
				u.setTipo(rs.getString(6));
				return u;
			}
			
			
		}
		);
	}

	public int modificarClave(String email, String clave) {
		return template.update("update user set clave=? where email=?",clave,email);
	}

	public List<Pista> obtenerPistas() {
		return template.query("select * from pista", new RowMapper<Pista>() {
			@Override
			public Pista mapRow(ResultSet rs, int row) throws SQLException
			{
				Pista p=new Pista();
				p.setId(rs.getInt(1));
				p.setNombre(rs.getString(2));
				p.setLocalizacion(rs.getString(3));
				p.setImagen(rs.getString(4));
				p.setInfo(rs.getString(5));
				return p;
			}
			
			
		}
		);
	}

	public List<Favorita> obtenerFavoritas(String usuario) {
		return template.query("select * from favoritas where usuario='"+usuario+"'", new RowMapper<Favorita>() {
			@Override
			public Favorita mapRow(ResultSet rs, int row) throws SQLException
			{
				Favorita f=new Favorita();
				f.setUsuario(rs.getString(1));
				f.setNombrePista(rs.getString(2));
				return f;
			}
			
			
		}
		);
	}
	public List<Reserva> obtenerReservas(String usuario) {
		return template.query("select * from reserva where usuario='"+usuario+"' and fecha > now()+INTERVAL+2 hour ", new RowMapper<Reserva>() {
			@Override
			public Reserva mapRow(ResultSet rs, int row) throws SQLException
			{
				Reserva r = new Reserva();
				r.setUsuario(rs.getString(1));
				r.setIdPista(rs.getInt(2));
				r.setFecha(new Date(rs.getDate(3).getTime()));
				return r;
			}
			
			
		}
		);
	}

	public List<Imagen> obtenerImagenesPista(int idPista) {
		return template.query("select * from imagenes where idP='"+idPista+"'", new RowMapper<Imagen>() {
			@Override
			public Imagen mapRow(ResultSet rs, int row) throws SQLException
			{
				Imagen i = new Imagen();
				i.setIdPista(rs.getInt(1));
				i.setImagen(rs.getString(2));
				return i;
			}
			
			
		}
		);
	}
	public Pista obtenerPista(String nombre) {
		return template.queryForObject("select * from pista where nombre=?",new Object[] {nombre},new BeanPropertyRowMapper<Pista>(Pista.class));
	}

	public int aniadirFavorito(String nombrePista, String usuario) {
		return template.update("insert into favoritas (usuario,NombrePista) values(?,?)",usuario,nombrePista);
		
	}
	public List<Favorita> comprobarFavorito(String usuario,int idPista) {
		return template.query("select * from favoritas where usuario='"+usuario+"' and idPista='"+idPista+"'", new RowMapper<Favorita>() {
			@Override
			public Favorita mapRow(ResultSet rs, int row) throws SQLException
			{
				Favorita f = new Favorita();
				f.setUsuario(rs.getString(1));
				f.setNombrePista(rs.getString(2));
				return f;
			}
			
			
		}
		);
	}
	public int eliminarFavorito(int idPista, String usuario) {
		return template.update("delete from favoritas where usuario='"+usuario+"'and idPista='"+idPista+"'");
		
	}

	public int modificarPista(Pista p) {
		return template.update("update pista set nombre=?,localizacion=?,imagen=?, info=? where id=?",p.getNombre(),p.getLocalizacion(),p.getImagen(),p.getInfo(),p.getId());
		
	}

	public int aniadirImagen(Imagen i) {
		return template.update("insert into imagenes (idP,imagen) values(?,?)",i.getIdPista(),i.getImagen());
		
	}

	public int aniadirPista(Pista p) {
		return template.update("insert into pista (nombre,localizacion,imagen,info) values(?,?,?,?)",p.getNombre(),p.getLocalizacion(),p.getImagen(),p.getInfo());
		
	}

	public int aniadirImagen(Pista p) {
		return template.update("insert into imagenes (idP,imagen) values(?,?)",p.getId(),p.getImagen());
		
	}

	public Usuario obtenerUsuario(String usuario) {
		return template.queryForObject("select * from user where usuario=?",new Object[] {usuario},new BeanPropertyRowMapper<Usuario>(Usuario.class));
	}

	public int modificarUsuario(Usuario u) {
		return template.update("update user set clave=?,nombre=?,apellidos=? where usuario=?",u.getClave(),u.getNombre(),u.getApellidos(),u.getUsuario());
		
	}
	public int modificarAdmin(Usuario u) {
		return template.update("update user set clave=? where usuario=?",u.getClave(),u.getUsuario());
		
	}
		
	}

