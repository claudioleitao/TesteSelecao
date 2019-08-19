package com.testeAdverum.testeDesenvolvimento.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testeAdverum.testeDesenvolvimento.models.Curso;
import com.testeAdverum.testeDesenvolvimento.models.Usuario;
import com.testeAdverum.testeDesenvolvimento.repository.CursoRepository;

@Controller
public class CursoController {
	@Autowired
	private CursoRepository cr;
	
	@RequestMapping(value="/loginAdministrador", method=RequestMethod.GET)
	public String loginAdministrador() {
		return "administrador/formEntrarAdministrador";
	}

	@RequestMapping(value="/entrarAdministrador", method=RequestMethod.POST)
	public String entrarAdministrador(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("errorMessage","Verifique os campos!");
			return "redirect:/loginAdministrador";
		} else if(!usuario.getEmail().equalsIgnoreCase("admin") && !usuario.getSenha().equalsIgnoreCase("admin")) {
			attributes.addFlashAttribute("errorMessage","Usuário ou Senha inválidos!");
			return "redirect:/loginAdministrador";
		}
				
		return "redirect:/administrador";
	}
	
	@RequestMapping(value="/administrador", method=RequestMethod.GET)
	public ModelAndView administrador() {
		Iterable<Curso> cursos = cr.findAll();
		
		ModelAndView mv = new ModelAndView("administrador/perfilAdministrador");
		mv.addObject("cursos", cursos);
		
		return mv;
	}
	
	@RequestMapping(value="/salvarCurso", method=RequestMethod.POST)
	public String salvarCurso(@Valid Curso curso, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("errorMessage","Verifique os campos!");
			return "redirect:/entrarAdministrador";
		} else if(achou(curso)) {
			attributes.addFlashAttribute("errorMessage","Curso já cadastrado!");
			return "redirect:/entrarAdministrador";
		}
		
		attributes.addFlashAttribute("successMessage","Curso incluído com sucesso!");
		cr.save(curso);
		
		return "redirect:/administrador";
	}
	
	@RequestMapping("/administrador/{codigo}")
	public ModelAndView excluirCurso(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
		Curso curso = cr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("redirect:/administrador");
		mv.addObject("curso", curso);
		
		cr.delete(curso);
		
		return mv;
	}
	
	private boolean achou(Curso curso) {
		boolean achou = false;
		Iterable<Curso> cursos = cr.findAll();
		
		for(Curso c : cursos) {
			if(c.getNome().equalsIgnoreCase(curso.getNome()))
				achou = true;
		}
		
		return achou;
	}
}
