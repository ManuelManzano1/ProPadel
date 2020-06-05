package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.modelo.Favorita;
import com.modelo.Imagen;
import com.modelo.JugadoresTorneo;
import com.modelo.Pista;
import com.modelo.Reserva;
import com.modelo.Torneo;
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
				return u;
			}
			
			
		}
		);
	}

	public int registrarUsuario(Usuario u) {
		return template.update("insert into user(usuario,clave,email,nombre,apellidos) values(?,?,?,?,?)",u.getUsuario(),u.getClave(),u.getEmail(),u.getNombre(),u.getApellidos());
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
	public List<Pista> obtenerPistasFiltro(String lugar) {
		return template.query("select * from pista where localizacion='"+lugar+"'", new RowMapper<Pista>() {
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
				f.setIdPista(rs.getInt(2));
				return f;
			}
			
			
		}
		);
	}
	public List<Reserva> obtenerReservas(String usuario) {
		return template.query("select * from reserva where usuario='"+usuario+"'", new RowMapper<Reserva>() {
			@Override
			public Reserva mapRow(ResultSet rs, int row) throws SQLException
			{
				Reserva r = new Reserva();
				r.setIdReserva(rs.getInt(1));
				r.setUsuario(rs.getString(2));
				r.setIdPista(rs.getInt(3));
				r.setFecha(rs.getString(4));
				r.setHora(rs.getString(5));
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

	public int aniadirFavorito(int idPista, String usuario) {
		return template.update("insert into favoritas (usuario,idPista) values(?,?)",usuario,idPista);
		
	}
	public List<Favorita> comprobarFavorito(String usuario,int idPista) {
		return template.query("select * from favoritas where usuario='"+usuario+"' and idPista='"+idPista+"'", new RowMapper<Favorita>() {
			@Override
			public Favorita mapRow(ResultSet rs, int row) throws SQLException
			{
				Favorita f = new Favorita();
				f.setUsuario(rs.getString(1));
				f.setIdPista(rs.getInt(2));
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

	public int eliminarPista(Pista p) {
		return template.update("delete from pista where Id="+p.getId());
		
	}

	public int eliminarImagenesPista(Pista p) {
		return template.update("delete from imagenes where IdP="+p.getId());
		
	}

	public List<Reserva> comprobarReservasPista(Pista p) {
		return template.query("select * from reserva where idPista="+p.getId(),new RowMapper<Reserva>() {
			@Override
			public Reserva mapRow(ResultSet rs, int row) throws SQLException
			{
				Reserva r = new Reserva();
				r.setIdReserva(rs.getInt(1));
				r.setUsuario(rs.getString(2));
				r.setIdPista(rs.getInt(3));
				r.setFecha(rs.getString(4));
				r.setHora(rs.getString(5));
				return r;
			}
			
			
		}
		);
	}
	public List<Pista> obtenerPistas(String nombre) {
		return template.query("select * from pista where nombre='"+nombre+"'", new RowMapper<Pista>() {
			@Override
			public Pista mapRow(ResultSet rs, int row) throws SQLException
			{
				Pista p = new Pista();
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

	public List<Reserva> obtenerHorasReservadas(Reserva r) {
		return template.query("select * from reserva where fecha='"+r.getFecha()+"' and idPista='"+r.getIdPista()+"'", new RowMapper<Reserva>() {
			@Override
			public Reserva mapRow(ResultSet rs, int row) throws SQLException
			{
				Reserva r = new Reserva();
				r.setIdReserva(rs.getInt(1));
				r.setUsuario(rs.getString(2));
				r.setIdPista(rs.getInt(3));
				r.setFecha(rs.getString(4));
				r.setHora(rs.getString(5));
				return r;
				
			}
			
			
		}
		);
	}

	public Pista obtenerPistaPorId(int idPista) {
		return template.queryForObject("select * from pista where Id=?",new Object[] {idPista},new BeanPropertyRowMapper<Pista>(Pista.class));
	}

	public int hacerReserva(Reserva res) {
		return template.update("insert into reserva (usuario,idPista,fecha,hora) values(?,?,?,?)",res.getUsuario(),res.getIdPista(),res.getFecha(),res.getHora());
		
	}

	public List<String> obtenerLocalizaciones() {
		return template.query("select distinct(localizacion) from pista ", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int row) throws SQLException
			{
				return rs.getString(1);
				
			}	
		}
		);
	}

	public int eliminarReserva(int idReserva) {
		return template.update("delete from reserva where idReserva="+idReserva);
		
	}
	
	public List<Reserva> obtenerTodasReservas() {
		return template.query("select * from reserva", new RowMapper<Reserva>() {
			@Override
			public Reserva mapRow(ResultSet rs, int row) throws SQLException
			{
				Reserva r = new Reserva();
				r.setIdReserva(rs.getInt(1));
				r.setUsuario(rs.getString(2));
				r.setIdPista(rs.getInt(3));
				r.setFecha(rs.getString(4));
				r.setHora(rs.getString(5));
				return r;
			}
			
		}
		);
	}

	public Reserva obtenerReserva(int idReserva) {
		return template.queryForObject("select * from reserva where idReserva=?",new Object[] {idReserva},new BeanPropertyRowMapper<Reserva>(Reserva.class));
	}

	public Pista obtenerPista(int idPista) {
		return template.queryForObject("select * from pista where id=?",new Object[] {idPista},new BeanPropertyRowMapper<Pista>(Pista.class));
	}

	public List<Torneo> obtenerTorneos() {
		return template.query("select * from torneo", new RowMapper<Torneo>() {
			@Override
			public Torneo mapRow(ResultSet rs, int row) throws SQLException
			{
				Torneo t = new Torneo();
				t.setIdTorneo(rs.getInt(1));
				t.setNombre(rs.getString(2));
				t.setIdPista(rs.getInt(3));
				t.setNumJugadores(rs.getInt(4));
				t.setNumInscritos(rs.getInt(5));
				t.setInfoPremios(rs.getString(6));
				t.setFecha(rs.getString(7));
				t.setInscripcion(rs.getInt(8));
				return t;
				
			}
			
		}
		);
	}

	public List<JugadoresTorneo> obtenerTorneosUsuario(String usuarioActivo) {
		return template.query("select * from jugadoresTorneo where usuario ='"+usuarioActivo+"'", new RowMapper<JugadoresTorneo>() {
			@Override
			public JugadoresTorneo mapRow(ResultSet rs, int row) throws SQLException
			{
				JugadoresTorneo jt = new JugadoresTorneo();
				jt.setUsuario(rs.getString(1));
				jt.setIdTorneo(rs.getInt(2));
				return jt;
				
			}
			
		}
		);
	}

	public int eliminarInscripcion(int idTorneo, String usuarioActivo) {
		return template.update("delete from jugadoresTorneo where usuario='"+usuarioActivo+"'and"
				+ " idTorneo = "+idTorneo);
	}

	public int aniadirInscripcion(int idTorneo, String usuarioActivo) {
		return template.update("insert into jugadoresTorneo (usuario,idTorneo) values(?,?)",usuarioActivo,idTorneo);
	}

	public Torneo obtenerTorneo(int idTorneo) {
		return template.queryForObject("select * from torneo where idTorneo=?",new Object[] {idTorneo},new BeanPropertyRowMapper<Torneo>(Torneo.class));
	}

	public int eliminarTorneo(int idTorneo) {
		return template.update("delete from torneo where idTorneo="+idTorneo);
		
	}

	public List<JugadoresTorneo> obtenerParticipantes(int idTorneo) {
		return template.query("select * from jugadoresTorneo where idTorneo ='"+idTorneo+"'", new RowMapper<JugadoresTorneo>() {
			@Override
			public JugadoresTorneo mapRow(ResultSet rs, int row) throws SQLException
			{
				JugadoresTorneo jt = new JugadoresTorneo();
				jt.setUsuario(rs.getString(1));
				jt.setIdTorneo(rs.getInt(2));
				return jt;
				
			}
			
		}
		);
	}

	public int aniadirParticipante(int idTorneo) {
		return template.update("update torneo set numInscritos=numInscritos+1 where idTorneo=?",idTorneo);
	}
	public int eliminarParticipante(int idTorneo) {
		return template.update("update torneo set numInscritos=numInscritos-1 where idTorneo=?",idTorneo);
	}

	public int eliminarParticipante(int idTorneo, String participante) {
		return template.update("delete from jugadoresTorneo where idTorneo="+idTorneo+" and usuario='"+participante+"'");
		
	}

	public int aniadirTorneo(Torneo t) {
		return template.update("insert into torneo (nombre,idPista,numJugadores,numInscritos,infoPremios,fecha,inscripcion) values(?,?,?,?,?,?,?)",t.getNombre(),t.getIdPista(),t.getNumJugadores(),t.getNumInscritos(),t.getInfoPremios(),t.getFecha(),t.getInscripcion());
		
	}

	public int eliminarParticipantes(int idTorneo) {
		return template.update("delete from jugadoresTorneo where idTorneo="+idTorneo);
		
	}
		
	}

