package com.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.modelo.JugadoresTorneo;
import com.modelo.Pista;
import com.modelo.PistaReserva;
import com.modelo.Reserva;
import com.modelo.Torneo;
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
	String usuarioActivo="";
	Reserva res;
	HttpSession sesion;
	int idTorneo=0;
	
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
			if(usuario.get(0).getUsuario().equalsIgnoreCase("admin")) {
				ModelAndView modelo = new ModelAndView("inicioAdmin");
				usuarioActivo = usuario.get(0).getUsuario();
				sesion.setAttribute("usuario", usuarioActivo);
				modelo.addObject("pistas", pistas);
				return modelo;
			}
			else {
				
				ModelAndView modelo=new ModelAndView("inicioUsuario");
				List<Favorita> favoritas = dao.obtenerFavoritas(usuario.get(0).getUsuario());
				List<Reserva> reservas = dao.obtenerReservas(usuario.get(0).getUsuario());
				List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
				List<String> listaLugares = dao.obtenerLocalizaciones();
				usuarioActivo = usuario.get(0).getUsuario();
				sesion.setAttribute("usuario", usuarioActivo);
				modelo.addObject("numFavoritas", favoritas.size());
				modelo.addObject("numReservas", reservasActivas.size());
				modelo.addObject("pistas", pistas);
				modelo.addObject("lugares",listaLugares);
				return modelo;
			}
		}
		else {
			ModelAndView modelo = new ModelAndView("login");
			modelo.addObject("vacio", 1);
			return modelo;
		}
	
	}
	@RequestMapping(value ="/cargarInicioUsuario",method = RequestMethod.GET)
	public ModelAndView cargarInicioUsuario() {
			List<Pista> pistas = dao.obtenerPistas();
			ModelAndView modelo=new ModelAndView("inicioUsuario");
			List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
			List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
			List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
			List<String> listaLugares = dao.obtenerLocalizaciones();
			modelo.addObject("numFavoritas", favoritas.size());
			modelo.addObject("numReservas", reservasActivas.size());
			modelo.addObject("pistas", pistas);
			modelo.addObject("lugares",listaLugares);
			return modelo;
	}
	
	@RequestMapping(value ="/cargarInicioAdmin",method = RequestMethod.GET)
	public ModelAndView cargarInicioAdmin() {
		List<Pista> pistas = dao.obtenerPistas();
			ModelAndView modelo = new ModelAndView("inicioAdmin");
			modelo.addObject("pistas", pistas);
			return modelo;
	}
	private List<Reserva> eliminarReservasAntiguas(List<Reserva> reservas) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formato1 = new SimpleDateFormat("HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		List<Reserva> reservasActivas = new ArrayList<>();
		String fecha = formato.format(new Date());
		String hora = formato1.format(new Date());
		for(int i=0;i<reservas.size();i++) {
			try {
				int res = formato.parse(reservas.get(i).getFecha()).compareTo(formato.parse(fecha));
				if(res>0) {
					reservasActivas.add(reservas.get(i));
				}
				else if(res==0) {
					if(formato1.parse(reservas.get(i).getHora()).after(formato1.parse(hora))) {
						reservasActivas.add(reservas.get(i));
					}
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
			u.setClave(generarClaveProvisional());
			dao.modificarClave(u.getEmail(),u.getClave());
			enviarMailRecuperarContraseña(u);
			return new ModelAndView("login");
		}
		else {
			ModelAndView modelo = new ModelAndView("recuperarPassword");
			modelo.addObject("existe", 1);
			modelo.addObject("command",new Usuario());
			return modelo;
		}
	
	}
	
	private String generarClaveProvisional() {
		String letras = "ABCDEGHIJLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz";
		String pass="";
		Random r = new Random();
		for(int i=0;i<6;i++) {
			String letra = Character.toString(letras.charAt(r.nextInt(49)));
			pass = pass+letra;
		}
		return pass;
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
	private void enviarMailRecuperarContraseña(Usuario u) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apppropadel@gmail.com");
		message.setTo(u.getEmail());
		message.setSubject("Restaurar contraseña");
		String cuerpo="Su contraseña provisional es:"+u.getClave()+"\nAcceda a su cuenta para modificarla";
		message.setText(cuerpo);
		mailSender.send(message);
		
	}
	@RequestMapping(value ="/logout",method = RequestMethod.GET)
	public ModelAndView logout(Model modelo) {
		return new ModelAndView("login");
		
	}
	@RequestMapping(value ="/pista",method = RequestMethod.GET)
	public ModelAndView cargarPista(@RequestParam("pista")String nombrePista) {
		Pista pista = dao.obtenerPista(nombrePista);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("command",new Reserva());
		modelo.addObject("hora", 0);
		List<Favorita> f = dao.comprobarFavorito(usuarioActivo,pista.getId());
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
		return new ModelAndView("redirect:/cargarInicioAdmin");
		
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
		return "redirect:/cargarInicioAdmin";
		
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
				p = dao.obtenerPista(p.getNombre());
				dao.aniadirImagen(p);
				return new ModelAndView("redirect:/cargarInicioAdmin");
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
		Pista pista = dao.obtenerPista(nombrePista);
		dao.aniadirFavorito(pista.getId(),usuarioActivo);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("hora", 0);
		modelo.addObject("command",new Reserva());
		modelo.addObject("favorito", 1);
		
		return modelo;
		
	}
	@RequestMapping(value ="/eliminarFav",method = RequestMethod.GET)
	public ModelAndView eliminarFavorita(@RequestParam("pista")String nombrePista) {
		Pista pista = dao.obtenerPista(nombrePista);
		dao.eliminarFavorito(pista.getId(),usuarioActivo);
		List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
		ModelAndView modelo = new ModelAndView("pista");
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pista", pista);
		modelo.addObject("imagenes", imagenes);
		modelo.addObject("hora", 0);
		modelo.addObject("command",new Reserva());
		modelo.addObject("favorito", 0);
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
			return "redirect:/cargarInicioAdmin";
		}
		else {
			dao.modificarUsuario(u);
			return "redirect:/cargarInicioUsuario";
		}
		
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
			return new ModelAndView("redirect:/cargarInicioAdmin");
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
		SimpleDateFormat formatoHoras = new SimpleDateFormat("HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String hora = formatoHoras.format(new Date());
		res = re;
		if(re.getFecha().substring(2, 3).equals("-")&&re.getFecha().substring(5, 6).equals("-")&&re.getFecha().length()==10) {
			try {
				if(formato.parse(re.getFecha()).after(calendar.getTime())) {
					List<String> listaHoras = realizarListaHoras();
					List<Reserva> listaReservadas = dao.obtenerHorasReservadas(res);
					for(int i=0;i<listaReservadas.size();i++) {
						for(int j=0;j<listaHoras.size();j++) {
							if(listaReservadas.get(i).getHora().toString().equalsIgnoreCase(listaHoras.get(j))) {
								listaHoras.remove(j);
							}
						}
					}
					//Codigo para eliminar las horas anteriores a la actual
					if(res.getFecha().equalsIgnoreCase(formato.format(new Date()))) {
					while(true) {
						if(Integer.parseInt(hora.substring(0, 2))>Integer.parseInt(listaHoras.get(0).substring(0, 2))) {
							listaHoras.remove(0);
						}
						else if(Integer.parseInt(hora.substring(0, 2))==Integer.parseInt(listaHoras.get(0).substring(0, 2))) {
							if(Integer.parseInt(hora.substring(3, 5))>Integer.parseInt(listaHoras.get(0).substring(3, 5))) {
								listaHoras.remove(0);
							}
						}
						else {
							break;
						}
						
						}
					}
					//Carga las horas disponibles
					
					Pista pista = dao.obtenerPistaPorId(re.getIdPista());
					List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
					ModelAndView modelo = new ModelAndView("pista");
					List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
					List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
					List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
					modelo.addObject("numFavoritas", favoritas.size());
					modelo.addObject("numReservas", reservasActivas.size());
					modelo.addObject("pista", pista);
					modelo.addObject("imagenes", imagenes);
					modelo.addObject("command",new Reserva());
					modelo.addObject("hora", 1);
					modelo.addObject("listaHoras",listaHoras);
					List<Favorita> f = dao.comprobarFavorito(usuarioActivo,pista.getId());
					if(f.size()<1) {
						modelo.addObject("favorito", 0);
					}
					else {
						modelo.addObject("favorito", 1);
					}
					return modelo;
				}
				else {
					//Carga la misma pagina pero indicando que la fecha introducida es anterior
					Pista pista = dao.obtenerPistaPorId(re.getIdPista());
					List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
					ModelAndView modelo = new ModelAndView("pista");
					List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
					List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
					List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
					modelo.addObject("numFavoritas", favoritas.size());
					modelo.addObject("numReservas", reservasActivas.size());
					modelo.addObject("pista", pista);
					modelo.addObject("imagenes", imagenes);
					modelo.addObject("command",new Reserva());
					modelo.addObject("hora", 0);
					modelo.addObject("fecha", 1);
					List<Favorita> f = dao.comprobarFavorito(usuarioActivo,pista.getId());
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
			//Carga la misma pagina pero indicando que el formato no es el adecuado
			Pista pista = dao.obtenerPistaPorId(re.getIdPista());
			List<Imagen> imagenes = dao.obtenerImagenesPista(pista.getId());
			ModelAndView modelo = new ModelAndView("pista");
			List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
			List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
			List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
			modelo.addObject("numFavoritas", favoritas.size());
			modelo.addObject("numReservas", reservasActivas.size());
			modelo.addObject("pista", pista);
			modelo.addObject("imagenes", imagenes);
			modelo.addObject("command",new Reserva());
			modelo.addObject("hora", 0);
			modelo.addObject("formato", 1);
			List<Favorita> f = dao.comprobarFavorito(usuarioActivo,pista.getId());
			if(f.size()<1) {
				modelo.addObject("favorito", 0);
			}
			else {
				modelo.addObject("favorito", 1);
			}
			return modelo;
		}
	}
	@RequestMapping(value ="/hacerReserva",method = RequestMethod.GET)
	public String hacerReserva(@RequestParam("hora")String hora) {
		res.setHora(hora);
		dao.hacerReserva(res);
		return "redirect:/cargarInicioUsuario";
	}
	@RequestMapping(value ="/filtro",method = RequestMethod.GET)
	public ModelAndView filtro(@RequestParam("lugar")String lugar) {
		ModelAndView modelo=new ModelAndView("inicioUsuario");
		List<Pista> pistasFiltro = dao.obtenerPistasFiltro(lugar);
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		List<String> listaLugares = dao.obtenerLocalizaciones();
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pistas", pistasFiltro);
		modelo.addObject("lugares",listaLugares);
		return modelo;
	}
	
	@RequestMapping(value ="/listaFavoritos",method = RequestMethod.GET)
	public ModelAndView listaFavoritos() {
		ModelAndView modelo=new ModelAndView("inicioUsuario");
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Pista> pistas = new ArrayList<>();
		
		for(int i=0;i<favoritas.size();i++) {
			pistas.add(dao.obtenerPistaPorId(favoritas.get(i).getIdPista()));
		}
		List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("pistas", pistas);
		return modelo;
	}
	
	@RequestMapping(value ="/listaReservas",method = RequestMethod.GET)
	public ModelAndView listaReservas() {
		ModelAndView modelo=new ModelAndView("listaReservas");
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		List<PistaReserva> reservasPistas = new ArrayList<>();
		for(int i=0;i<reservasActivas.size();i++) {
			PistaReserva pr = new PistaReserva();
			pr.setIdReserva(reservasActivas.get(i).getIdReserva());
			Pista p = dao.obtenerPistaPorId(reservasActivas.get(i).getIdPista());
			pr.setNombre(p.getNombre());
			pr.setLocalizacion(p.getLocalizacion());
			pr.setImagen(p.getImagen());
			pr.setFecha(reservasActivas.get(i).getFecha());
			pr.setHora(reservasActivas.get(i).getHora());
			reservasPistas.add(pr);
		}
		modelo.addObject("numFavoritas", favoritas.size());
		modelo.addObject("numReservas", reservasActivas.size());
		modelo.addObject("reservas", reservasPistas);
		return modelo;
	}
	
	@RequestMapping(value ="/eliminarReserva",method = RequestMethod.GET)
	public ModelAndView eliminarReserva(@RequestParam("id")int idReserva) {
		Reserva reserva = dao.obtenerReserva(idReserva);
		dao.eliminarReserva(reserva.getIdReserva());
		if(usuarioActivo.equalsIgnoreCase("admin")) {
			enviarEmailReservaEliminada(reserva);
			return new ModelAndView("redirect:/cargarInicioAdmin");
		}
		else {
			return new ModelAndView("redirect:/cargarInicioUsuario");
		}
		
	}
	
	private void enviarEmailReservaEliminada(Reserva reserva) {
		Usuario u = dao.obtenerUsuario(reserva.getUsuario());
		Pista p  = dao.obtenerPista(reserva.getIdPista());
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apppropadel@gmail.com");
		message.setTo(u.getEmail());
		message.setSubject("Reserva cancelada");
		String cuerpo="Estimado "+u.getNombre()+"\n"
				+ "Su reserva del día "+reserva.getFecha()+" a las "+reserva.getHora()
						+ " en la pista "+p.getNombre()+" ha sido cancelada por el administrador ";
		message.setText(cuerpo);
		mailSender.send(message);
		
	}
	@RequestMapping(value ="/listaReservasAdmin",method = RequestMethod.GET)
	public ModelAndView listaReservasUsuario() {
		ModelAndView modelo=new ModelAndView("listaReservasAdmin");
		List<Reserva> reservas = dao.obtenerTodasReservas();
		List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
		List<PistaReserva> reservasPistas = new ArrayList<>();
		for(int i=0;i<reservasActivas.size();i++) {
			PistaReserva pr = new PistaReserva();
			pr.setIdReserva(reservasActivas.get(i).getIdReserva());
			Pista p = dao.obtenerPistaPorId(reservasActivas.get(i).getIdPista());
			pr.setNombre(p.getNombre());
			pr.setLocalizacion(p.getLocalizacion());
			pr.setImagen(p.getImagen());
			pr.setFecha(reservasActivas.get(i).getFecha());
			pr.setHora(reservasActivas.get(i).getHora());
			pr.setUsuario(reservasActivas.get(i).getUsuario());
			reservasPistas.add(pr);
		}
		modelo.addObject("reservas", reservasPistas);
		return modelo;
	}
	@RequestMapping(value ="/listaTorneos",method = RequestMethod.GET)
	public ModelAndView listaTorneos() {
		ModelAndView modelo=new ModelAndView("listaTorneos");
		List<Favorita> favoritas = dao.obtenerFavoritas(usuarioActivo);
		List<Torneo> torneos = dao.obtenerTorneos();
		List<Torneo> torneosActuales = eliminarTorneosAntiguos(torneos);
		if(usuarioActivo.equalsIgnoreCase("admin")) {
			modelo.addObject("torneos", torneosActuales);
			modelo.addObject("admin",1);
		}
		else {
			List<JugadoresTorneo> listaTorneosUsuario = dao.obtenerTorneosUsuario(usuarioActivo);
			for(int i=0;i<torneos.size();i++) {
				Pista p = dao.obtenerPista(torneos.get(i).getIdPista());
				torneos.get(i).setNombrePista(p.getNombre());
				torneos.get(i).setLocalizacion(p.getLocalizacion());
				if(torneos.get(i).getNumInscritos()<torneos.get(i).getNumJugadores()) {
					for(int j=0;j<listaTorneosUsuario.size();j++) {
						if(listaTorneosUsuario.get(j).getIdTorneo()==torneos.get(i).getIdTorneo()) {
							torneos.get(i).setInscrito(1);
						}
					}
				}
				else {
					torneos.get(i).setLleno(1);
					torneos.get(i).setInscrito(-2);
				}
			}
			List<Reserva> reservas = dao.obtenerReservas(usuarioActivo);
			List<Reserva> reservasActivas = eliminarReservasAntiguas(reservas);
			modelo.addObject("numFavoritas", favoritas.size());
			modelo.addObject("numReservas", reservasActivas.size());
			modelo.addObject("torneos", torneosActuales);
			modelo.addObject("admin",0);
		}
		return modelo;
	}

	private List<Torneo> eliminarTorneosAntiguos(List<Torneo> torneos) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		List<Torneo> torneosActuales = new ArrayList<>();
		for(int i=0;i<torneos.size();i++) {
			try {
				if(formato.parse(torneos.get(i).getFecha()).after(new Date())) {
					torneosActuales.add(torneos.get(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return torneosActuales;
	}
	@RequestMapping(value ="/eliminarInscripcion",method = RequestMethod.GET)
	public String eliminarInscripcion(@RequestParam("torneo")int idTorneo) {
		dao.eliminarInscripcion(idTorneo,usuarioActivo);
		dao.eliminarParticipante(idTorneo);
		return "redirect:/cargarInicioUsuario";
	}
	
	@RequestMapping(value ="/aniadirInscripcion",method = RequestMethod.GET)
	public String aniadirInscripcion(@RequestParam("torneo")int idTorneo) {
		dao.aniadirInscripcion(idTorneo,usuarioActivo);
		dao.aniadirParticipante(idTorneo);
		return "redirect:/cargarInicioUsuario";
	}
	
	@RequestMapping(value ="/eliminarTorneo",method = RequestMethod.GET)
	public ModelAndView eliminarTorneo(@RequestParam("torneo")int idTorneo) {
		List<JugadoresTorneo> participantes = dao.obtenerParticipantes(idTorneo);
		enviarEmailCancelacionTorneo(participantes);
		dao.eliminarTorneo(idTorneo);
		dao.eliminarParticipantes(idTorneo);
		return new ModelAndView("redirect:/cargarInicioAdmin");
	}
	private void enviarEmailCancelacionInscripcion(List<JugadoresTorneo> participantes) {
		for(int i=0;i<participantes.size();i++) {
			Usuario u = dao.obtenerUsuario(participantes.get(i).getUsuario());
			Torneo t = dao.obtenerTorneo(participantes.get(i).getIdTorneo());
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("apppropadel@gmail.com");
			message.setTo(u.getEmail());
			message.setSubject("Anulacion inscripcion");
			String cuerpo="Su inscripción para el torneo "+t.getNombre()+" programado para la fecha "+t.getFecha()+" ha sido cancelada";
			message.setText(cuerpo);
			mailSender.send(message);
			
		}
	}
	private void enviarEmailCancelacionTorneo(List<JugadoresTorneo> participantes) {
		for(int i=0;i<participantes.size();i++) {
			Usuario u = dao.obtenerUsuario(participantes.get(i).getUsuario());
			Torneo t = dao.obtenerTorneo(participantes.get(i).getIdTorneo());
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("apppropadel@gmail.com");
			message.setTo(u.getEmail());
			message.setSubject("Anulacion inscripcion");
			String cuerpo="El torneo "+t.getNombre()+" programado para la fecha "+t.getFecha()+" ha sido cancelado";
			message.setText(cuerpo);
			mailSender.send(message);
			
		}
	}
	@RequestMapping(value ="/accederTorneo",method = RequestMethod.GET)
	public ModelAndView accederTorneo(@RequestParam("torneo")int idTorneod) {
		idTorneo = idTorneod;
		Torneo t = dao.obtenerTorneo(idTorneo);
		List<JugadoresTorneo> participantes = dao.obtenerParticipantes(idTorneo);
		ModelAndView modelo = new ModelAndView("torneo");
		modelo.addObject("participantes",participantes);
		modelo.addObject("t", t);
		return modelo;
	}
	@RequestMapping(value ="/eliminarParticipante",method = RequestMethod.GET)
	public ModelAndView accederTorneo(@RequestParam("participante")String participante) {
		dao.eliminarParticipante(idTorneo,participante);
		dao.eliminarParticipante(idTorneo);
		JugadoresTorneo jt = new JugadoresTorneo();
		jt.setUsuario(participante);
		jt.setIdTorneo(idTorneo);
		List<JugadoresTorneo> lista = new ArrayList<>();
		lista.add(jt);
		enviarEmailCancelacionInscripcion(lista);
		return new ModelAndView("redirect:/cargarInicioAdmin");
	}
	
	@RequestMapping(value ="/accederAniadirTorneo",method = RequestMethod.GET)
	public ModelAndView accederAniadirTorneo() {
		List<Pista> pistas = dao.obtenerPistas();
		ModelAndView modelo = new ModelAndView("aniadirTorneo");
		modelo.addObject("pistas",pistas);
		modelo.addObject("command",new Torneo());
		return modelo;
	
	}
	
	@RequestMapping(value ="/aniadirTorneo",method = RequestMethod.POST)
	public ModelAndView aniadirTorneo(@ModelAttribute("torneo")Torneo t) {
		List<Pista> pistas = dao.obtenerPistas();
		ModelAndView modelo = new ModelAndView("aniadirTorneo");
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		if(t.getNombre().isEmpty()||t.getIdPista()==0||t.getNumJugadores()==0||t.getInfoPremios().isEmpty()||t.getFecha().isEmpty()) {
			modelo.addObject("pistas",pistas);
			modelo.addObject("command",new Torneo());
			modelo.addObject("vacio", 1);
			return modelo;
		}
		else {
			if(t.getFecha().substring(2, 3).equals("-")&&t.getFecha().substring(5, 6).equals("-")&&t.getFecha().length()==16) {
				try {
					if(formato.parse(t.getFecha()).after(new Date())) {
						t.setNumInscritos(0);
						dao.aniadirTorneo(t);
						return new ModelAndView("redirect:/cargarInicioAdmin");
					}
					else {
						modelo.addObject("pistas",pistas);
						modelo.addObject("command",new Torneo());
						modelo.addObject("fecha", 1);
						return modelo;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			else {
				modelo.addObject("pistas",pistas);
				modelo.addObject("command",new Torneo());
				modelo.addObject("formato", 1);
				return modelo;
			}
		}
	}

}
