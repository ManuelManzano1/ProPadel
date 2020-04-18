package com.controller;


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
				sesion.setAttribute("tipo", "A");
				modelo.addObject("pistas", pistas);
				sesionP=sesion;
				return modelo;
			}
			else {
				
				ModelAndView modelo=new ModelAndView("inicioUsuario");
				List<Favorita> favoritas = dao.obtenerFavoritas(usuario.get(0).getUsuario());
				List<Reserva> reservas = dao.obtenerReservas(usuario.get(0).getUsuario());
				sesion.setAttribute("usuario", usuario.get(0).getUsuario());
				sesion.setAttribute("tipo", "U");
				modelo.addObject("numFavoritas", favoritas.size());
				modelo.addObject("numReservas", reservas.size());
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
	public ModelAndView cargarInicio(@ModelAttribute("us")Usuario u,HttpSession sesion) {
		
			List<Pista> pistas = dao.obtenerPistas();
			if(sesionP.getAttribute("tipo").toString().equalsIgnoreCase("A")) {
				ModelAndView modelo = new ModelAndView("inicioAdmin");
				modelo.addObject("pistas", pistas);
				sesionP=sesion;
				return modelo;
			}
			else {
				
				ModelAndView modelo=new ModelAndView("inicioUsuario");
				List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
				List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
				modelo.addObject("numFavoritas", favoritas.size());
				modelo.addObject("numReservas", reservas.size());
				modelo.addObject("pistas", pistas);
				sesionP=sesion;
				
				return modelo;
			}
	
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
		sesionP.setAttribute("usuario", null);;
		return new ModelAndView("login");
		
	}
	@RequestMapping(value ="/pista",method = RequestMethod.GET)
	public ModelAndView cargarPista(@RequestParam("pista")String nombrePista,HttpSession sesion) {
		sesion = sesionP;
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
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
	public ModelAndView accesoEditarPista(@RequestParam("pista")String nombrePista,HttpSession sesion) {
		sesion = sesionP;
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("editarPista");
		modelo.addObject("command", pista);
		modelo.addObject("imagenes", imagenes);
		return modelo;
		
	}
	@RequestMapping(value ="/guardarCambiosPista",method = RequestMethod.POST)
	public String guardarCambiosPista(@ModelAttribute("pista")Pista p,HttpSession sesion) {
		dao.modificarPista(p);
		return "redirect:/cargarInicio";
		
	}
	@RequestMapping(value ="/accederAniadirImagenesPista",method = RequestMethod.GET)
	public ModelAndView accesoEditarImagenesPista(@RequestParam("pista")String nombrePista,HttpSession sesion) {
		sesion = sesionP;
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
	public String aniadirImagen(@ModelAttribute("imagen")Imagen i,HttpSession sesion) {
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
	public String aniadirPista(@ModelAttribute("pista")Pista p) {
		dao.aniadirPista(p);
		dao.aniadirImagen(p);
		return "redirect:/cargarInicio";
		
	}
	@RequestMapping(value ="/aniadirFav",method = RequestMethod.GET)
	public ModelAndView aniadirFavorita(@RequestParam("pista")String nombrePista) {
		dao.aniadirFavorito(nombrePista,sesionP.getAttribute("usuario").toString());
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(sesionP.getAttribute("usuario").toString());
		List<Reserva> reservas = dao.obtenerReservas(sesionP.getAttribute("usuario").toString());
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
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
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
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
