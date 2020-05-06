package com.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dao.Dao;
import com.modelo.Favorita;
import com.modelo.Imagen;
import com.modelo.Pista;
import com.modelo.Reserva;
import com.modelo.Usuario;



@Controller
public class Controlador {
	//Generar inyecciones de despendencias de un tipo de objeto
	@Autowired
	Dao dao;
	@Autowired
	public JavaMailSender emailSender;
	@Autowired
	public MailSender mailSender;
	HttpSession sesionP;
	Reserva r;
	
	/*
	 * Model and view composición del modelo lógico de datos
	 * y vista. Representa la vista y el modelo devueltos por un controlador
	 */
	
	@RequestMapping(value ="/login")
	public String login(Model modelo) {
		modelo.addAttribute("command",new Usuario());
		modelo.addAttribute("vacio", 0);
		return "login";
		
}
	@RequestMapping(value ="/comprobarLogin",method = RequestMethod.POST)
	public ModelAndView comprobarLogin(@ModelAttribute("us")Usuario u,HttpSession sesion) {
		List<Usuario> usuario = dao.comprobarLogin(u);
		if(usuario.size()>0) {
			List<Pista> pistas = dao.obtenerPistas();
			if(usuario.get(0).getTipo().equalsIgnoreCase("A")) {
				ModelAndView modelo = new ModelAndView("inicioAdmin");
				sesion.setAttribute("usuario", usuario.get(0).getUsuario());
				modelo.addObject("pistas", pistas);
				sesionP=sesion;
				return modelo;
			}
			else {
				
				ModelAndView modelo=new ModelAndView("inicioUsuario");
				List<Favorita> favoritas = dao.obtenerFavoritas(usuario.get(0).getUsuario());
				List<Reserva> reservas = dao.obtenerReservas(usuario.get(0).getUsuario());
				List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
				sesion.setAttribute("usuario", usuario.get(0).getUsuario());
				modelo.addObject("numFavoritas", favoritas.size());
				modelo.addObject("numReservas", reservasActivas.size());
				modelo.addObject("pistas", pistas);
				sesionP=sesion;
				
				return modelo;
			}
		}
		else {
			ModelAndView modelo = new ModelAndView("login");
			modelo.addObject("vacio", 1);
			return modelo;
		}
	
	}
	@RequestMapping(value ="/cargarInicio",method = RequestMethod.GET)
	public ModelAndView cargarInicio(@ModelAttribute("us")Usuario u) {
			List<Pista> pistas = dao.obtenerPistas();
			if(sesionP.getAttribute("usuario").toString().equalsIgnoreCase("admin")) {
				ModelAndView modelo = new ModelAndView("inicioAdmin");
				modelo.addObject("pistas", pistas);
				return modelo;
			}
			else {
				
				ModelAndView modelo=new ModelAndView("inicioUsuario");
				List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
				List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
				List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
				modelo.addObject("numFavoritas", favoritas.size());
				modelo.addObject("numReservas", reservasActivas.size());
				modelo.addObject("pistas", pistas);
				return modelo;
			}
	
	}
	private List<Reserva> eliminarReservasAntiguas(List<Reserva> reservas) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		List<Reserva> reservasActivas = new ArrayList<>();
		for(int i=0;i<reservas.size();i++) {
			try {
				if(formato.parse(reservas.get(i).getFecha()).after(new Date())) {
					reservasActivas.add(reservas.get(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reservasActivas;
	}
	@RequestMapping(value ="/comprobarClave",method = RequestMethod.GET)
	public String cargarRecuperacion(Model modelo) {
		modelo.addAttribute("command",new Usuario());
		return "recuperarPassword";
		
}
	@RequestMapping(value ="/comprobarMail",method = RequestMethod.POST)
	public ModelAndView recuperarContraseña(@ModelAttribute("us")Usuario u) {
		List<Usuario> usuario = dao.comprobarEmail(u);
		if(usuario.size()>0) {
			dao.modificarClave(u.getEmail(),u.getClave());
			enviarMailRecuperarContraseña(u.getEmail());
			return new ModelAndView("login");
		}
		else {
			ModelAndView modelo = new ModelAndView("recuperarPassword");
			modelo.addObject("existe", 1);
			return modelo;
		}
	
	}
	
	@RequestMapping(value ="/registro",method = RequestMethod.GET)
	public String registro(Model modelo) {
		modelo.addAttribute("user",new Usuario());
		return "registro";
		
}
	@RequestMapping(value ="/registrarUsuario",method = RequestMethod.POST)
	public ModelAndView registrarUsuario(@Valid @ModelAttribute("user")Usuario u,BindingResult result) {
		if(result.hasErrors()) {
			return new ModelAndView("registro");
		}
		else {
			if(dao.comprobarUsuario(u).size()>0) {
				ModelAndView modelo = new ModelAndView("registro");
				modelo.addObject("repetido",1);
				return modelo;
			}
			else {
			if(dao.comprobarEmail(u).size()>0) {
				ModelAndView modelo = new ModelAndView("registro");
				modelo.addObject("repetido",1);
				return modelo;
			}
			else {
			try {
				dao.registrarUsuario(u);
				enviarMailRegistro(u);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("login");
		}
			}
		}
	}
	private void enviarMailRegistro(Usuario u) throws MessagingException{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apppropadel@gmail.com");
		message.setTo(u.getEmail());
		message.setSubject("Confirmación de registro");
		String cuerpo="Confirmación de registro\n"
				+ "Bienvenido:"+u.getNombre()+"\n"
				+ "Tu nombre de usuario es:"+u.getUsuario();
		message.setText(cuerpo);
		mailSender.send(message);
		
	}
	private void enviarMailRecuperarContraseña(String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apppropadel@gmail.com");
		message.setTo(email);
		message.setSubject("Restaurar contraseña");
		String cuerpo="Su contraseña ha sido restaurada";
		message.setText(cuerpo);
		mailSender.send(message);
		
	}
	@RequestMapping(value ="/logout",method = RequestMethod.GET)
	public ModelAndView logout(Model modelo) {
		sesionP.setAttribute("usuario", null);
		return new ModelAndView("login");
		
	}
	@RequestMapping(value ="/pista",method = RequestMethod.GET)
	public ModelAndView cargarPista(@RequestParam("pista")String nombrePista) {
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("command",new Reserva());
		modelo.addObject("hora", 0);
		List<Favorita> f = dao.comprobarFavorito(sesionP.getAttribute("usuario").toString(),pista.getId());
		if(f.size()<1) {
			modelo.addObject("favorito", 0);
		}
		else {
			modelo.addObject("favorito", 1);
		}
		return modelo;
		
	}
	@RequestMapping(value ="/editarPista",method = RequestMethod.GET)
	public ModelAndView accesoEditarPista(@RequestParam("pista")String nombrePista) {
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("editarPista");
		modelo.addObject("command", pista);
		modelo.addObject("imagenes", imagenes);
		return modelo;
		
	}
	@RequestMapping(value ="/guardarCambiosPista",method = RequestMethod.POST)
	public ModelAndView guardarCambiosPista(@ModelAttribute("pista")Pista p) {
		dao.modificarPista(p);
		return new ModelAndView("redirect:/cargarInicio");
		
	}
	@RequestMapping(value ="/accederAniadirImagenesPista",method = RequestMethod.GET)
	public ModelAndView accesoEditarImagenesPista(@RequestParam("pista")String nombrePista) {
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("aniadirImagen");
		Imagen i = new Imagen();
		i.setIdPista(pista.getId());
		i.setImagen("");
		modelo.addObject("command", i);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("nombrePista", nombrePista);
		return modelo;
		
	}
	@RequestMapping(value ="/aniadirImagen",method = RequestMethod.POST)
	public String aniadirImagen(@ModelAttribute("imagen")Imagen i) {
		dao.aniadirImagen(i);
		return "redirect:/cargarInicio";
		
	}
	@RequestMapping(value ="/accederAniadirPista",method = RequestMethod.GET)
	public ModelAndView accederAniadirPista() {
		ModelAndView modelo = new ModelAndView("aniadirPista");
		modelo.addObject("command", new Pista());
		return modelo;
		
	}
	@RequestMapping(value ="/aniadirPista",method = RequestMethod.POST)
	public ModelAndView aniadirPista(@ModelAttribute("pista")Pista p) {
		if(p.getNombre()=="") {
			ModelAndView modelo = new ModelAndView("aniadirPista");
			modelo.addObject("command", new Pista());
			return modelo;
		}
		else {
			List<Pista> pistas = dao.obtenerPistas(p.getNombre());
			if(pistas.size()<1) {
				dao.aniadirPista(p);
				dao.aniadirImagen(p);
				return new ModelAndView("redirect:/cargarInicio");
			}
			else {
				ModelAndView modelo = new ModelAndView("aniadirPista");
				modelo.addObject("repetido", 1);
				modelo.addObject("command", new Pista());
				return modelo;
			}
		}
		
	}
	@RequestMapping(value ="/aniadirFav",method = RequestMethod.GET)
	public ModelAndView aniadirFavorita(@RequestParam("pista")String nombrePista) {
		dao.aniadirFavorito(nombrePista,sesionP.getAttribute("usuario").toString());
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("hora", 0);
		List<Favorita> f = dao.comprobarFavorito(sesionP.getAttribute("usuario").toString(),pista.getId());
		if(f.size()<1) {
			modelo.addObject("favorito", 0);
		}
		else {
			modelo.addObject("favorito", 1);
		}
		return modelo;
		
	}
	@RequestMapping(value ="/eliminarFav",method = RequestMethod.GET)
	public ModelAndView eliminarFavorita(@RequestParam("pista")String nombrePista) {
		Pista pista = dao.obtenerPista(nombrePista);
		dao.eliminarFavorito(pista.getId(),sesionP.getAttribute("usuario").toString());
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("hora", 0);
		List<Favorita> f = dao.comprobarFavorito(sesionP.getAttribute("usuario").toString(),pista.getId());
		if(f.size()<1) {
			modelo.addObject("favorito", 0);
		}
		else {
			modelo.addObject("favorito", 1);
		}
		return modelo;
		
	}
	@RequestMapping(value ="/perfil",method = RequestMethod.GET)
	public ModelAndView accederPerfil(@RequestParam("usuario")String usuario) {
		Usuario u = dao.obtenerUsuario(usuario);
		ModelAndView modelo = new ModelAndView("perfil");
		if(usuario.equalsIgnoreCase("admin")) {
			modelo.addObject("command", u);
			modelo.addObject("usuario", 1);
		}
		else {
			List<Favorita> favoritas = dao.obtenerFavoritas(usuario);
			List<Reserva> reservas = dao.obtenerReservas(usuario);
			List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
			modelo.addObject("command", u);
			modelo.addObject("numFavoritas", favoritas.size());
			modelo.addObject("numReservas", reservasActivas.size());
			modelo.addObject("usuario", 0);
		}
		return modelo;
		
	}
	@RequestMapping(value ="/modificarPerfil",method = RequestMethod.POST)
	public String modificarPerfil(@ModelAttribute("usuario")Usuario u) {
		if(u.getUsuario().equalsIgnoreCase("admin")) {
			dao.modificarAdmin(u);
		}
		else {
			dao.modificarUsuario(u);
		}
		return "redirect:/cargarInicio";
		
	}
	@RequestMapping(value ="/habilitarEliminarPista",method = RequestMethod.GET)
	public ModelAndView accederEliminarPista() {
		List<Pista> pistas = dao.obtenerPistas();
		ModelAndView modelo = new ModelAndView("inicioAdmin");
		modelo.addObject("eliminar", 1);
		modelo.addObject("pistas", pistas);
		return modelo;
	}
	@RequestMapping(value ="/eliminarPista",method = RequestMethod.GET)
	public ModelAndView eliminarPista(@RequestParam("pista")String nombrePista) {
		Pista p = dao.obtenerPista(nombrePista);
		List<Reserva> listaReservas = dao.comprobarReservasPista(p);
		if(listaReservas.size()>0) {
			List<Pista> pistas = dao.obtenerPistas();
			ModelAndView modelo = new ModelAndView("inicioAdmin");
			modelo.addObject("pistas", pistas);
			modelo.addObject("reservas", 1);
			return modelo;
		}
		else {
			dao.eliminarPista(p);
			dao.eliminarImagenesPista(p);
			return new ModelAndView("redirect:/cargarInicio");
		}
		
		
	}
	public List<String> realizarListaHoras(){
		List<String> listaHoras = new ArrayList<>();
		listaHoras.add("09:00");
		listaHoras.add("10:00");
		listaHoras.add("11:00");
		listaHoras.add("12:00");
		listaHoras.add("13:00");
		listaHoras.add("14:00");
		listaHoras.add("15:00");
		listaHoras.add("16:00");
		listaHoras.add("17:00");
		listaHoras.add("18:00");
		listaHoras.add("19:00");
		listaHoras.add("20:00");
		listaHoras.add("21:00");
		return listaHoras;
	}
	@RequestMapping(value ="/cargarHoras",method = RequestMethod.POST)
	public ModelAndView cargarHoras(@ModelAttribute("reserva")Reserva re) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		r = re;
		if(re.getFecha().substring(2, 3).equals("-")&&re.getFecha().substring(5, 6).equals("-")&&re.getFecha().length()==10) {
			try {
				if(formato.parse(re.getFecha()).after(new Date())) {
					List<String> listaHoras = realizarListaHoras();
					List<Reserva> listaReservadas = dao.obtenerHorasReservadas(r);
					for(int i=0;i<listaReservadas.size();i++) {
						for(int j=0;j<listaHoras.size();j++) {
							if(listaReservadas.get(i).getHora().toString().equalsIgnoreCase(listaHoras.get(j))) {
								listaHoras.remove(j);
							}
						}
					}
					for(String hora:listaHoras) {
						System.out.println("dsds"+hora);
					}
					return new ModelAndView("login");
				}
				else {
					Pista pista = dao.obtenerPistaPorId(re.getIdPista());
					List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
					ModelAndView modelo = new ModelAndView("pista");
					List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
					List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
					List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
					modelo.addObject("numFavoritas", favoritas.size());
					modelo.addObject("numReservas", reservasActivas.size());
					modelo.addObject("pista", pista);
					modelo.addObject("imagenes", imagenes);
					modelo.addObject("command",new Reserva());
					modelo.addObject("hora", 0);
					modelo.addObject("fecha", 1);
					List<Favorita> f = dao.comprobarFavorito(sesionP.getAttribute("usuario").toString(),pista.getId());
					if(f.size()<1) {
						modelo.addObject("favorito", 0);
					}
					else {
						modelo.addObject("favorito", 1);
					}
					return modelo;
				}
			} catch (ParseException e) {
				return null;
			}
		}
		else {
			Pista pista = dao.obtenerPistaPorId(re.getIdPista());
			List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
			ModelAndView modelo = new ModelAndView("pista");
			List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
			List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
			List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
			modelo.addObject("numFavoritas", favoritas.size());
			modelo.addObject("numReservas", reservasActivas.size());
			modelo.addObject("pista", pista);
			modelo.addObject("imagenes", imagenes);
			modelo.addObject("command",new Reserva());
			modelo.addObject("hora", 0);
			modelo.addObject("formato", 1);
			List<Favorita> f = dao.comprobarFavorito(sesionP.getAttribute("usuario").toString(),pista.getId());
			if(f.size()<1) {
				modelo.addObject("favorito", 0);
			}
			else {
				modelo.addObject("favorito", 1);
			}
			return modelo;
		}
	}
}
