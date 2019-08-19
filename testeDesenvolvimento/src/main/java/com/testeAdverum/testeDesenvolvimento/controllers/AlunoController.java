package com.testeAdverum.testeDesenvolvimento.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.testeAdverum.testeDesenvolvimento.models.Aluno;
import com.testeAdverum.testeDesenvolvimento.models.Curso;
import com.testeAdverum.testeDesenvolvimento.models.Curso_Aluno;
import com.testeAdverum.testeDesenvolvimento.models.Usuario;
import com.testeAdverum.testeDesenvolvimento.repository.AlunoRepository;
import com.testeAdverum.testeDesenvolvimento.repository.CursoAlunoRepository;
import com.testeAdverum.testeDesenvolvimento.repository.CursoRepository;
import com.testeAdverum.testeDesenvolvimento.repository.UsuarioRepository;

@Controller
public class AlunoController {
	
	private long codigo_Aluno;
	private List<Curso> listCursos1 = new ArrayList<Curso>();
	
	@Autowired
	private AlunoRepository ar;
	@Autowired
	private UsuarioRepository ur;
	@Autowired
	private CursoRepository cr;
	@Autowired
	private CursoAlunoRepository car;
	
	@RequestMapping(value="/cadastroAluno", method=RequestMethod.GET)
	public String form() {
		return "aluno/formAluno";
	}
	
	
	@RequestMapping(value="/cadastroAluno", method=RequestMethod.POST)
	public String form(@Valid Aluno aluno, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("errorMessage","Verifique os campos!");
			return "redirect:/cadastroAluno";
		} else if(achou(aluno)) {
			attributes.addFlashAttribute("errorMessage","Aluno com CPF ou E-Mail já cadastrado!");
			return "redirect:/cadastroAluno";
		}
		
		ar.save(aluno);
		Aluno al = this.getAluno(aluno.getCpf());
		Usuario usuario = new Usuario();
		usuario.setCodigoAluno(al.getCodigo());
		usuario.setEmail(al.getEmail());
		usuario.setSenha(al.getCpf());
		usuario.setIsAdmin(false);
		
		ur.save(usuario);
		attributes.addFlashAttribute("successMessage","Inclusão ocorrido com sucesso!");
		return "redirect:/cadastroAluno";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {		
		return "aluno/formEntrar";
	}
	
	@RequestMapping(value="/entrar", method=RequestMethod.POST)
	public String entrar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("errorMessage","Verifique os campos!");
			return "redirect:/login";
		} else if(!achou(usuario.getEmail(), usuario.getSenha())) {
			attributes.addFlashAttribute("errorMessage","E-mail ou CPF inválidos!");
			return "redirect:/login";
		}
		
		this.codigo_Aluno = this.getCodigoUsuario(usuario.getEmail());
		
		return "redirect:/alunos/"+this.getCodigoUsuario(usuario.getEmail());
	}
	
	@RequestMapping("/alunos")
	public ModelAndView perfilAluno() {
		ModelAndView mv = this.loadCursos();
		
		return mv;
	}

	
	@RequestMapping("/alunos/{codigo}")
	public ModelAndView listaAlunos(@PathVariable long codigo) {
		ModelAndView mv = new ModelAndView("redirect:/alunos");
		Iterable<Curso_Aluno> cursoAlunos = car.findAll();
		List<Curso> cursos1 = new ArrayList<Curso>();
		
		for(Curso_Aluno cursoAluno : cursoAlunos) {
			if (cursoAluno.getCodigoAluno() == codigo) {
				Curso curso = cr.findByCodigo(cursoAluno.getCodigoCurso());
				cursos1.add(curso);
			}
		}
		
		this.listCursos1 = cursos1;
		mv.addObject("cursos1", cursos1);
		
		return mv;
	}

	@RequestMapping("/cursos/{codigo}")
	public String excluirCurso(@PathVariable("codigo") long codigo) {
		Curso curso = cr.findByCodigo(codigo);
		Iterable<Curso_Aluno> listCA = car.findAll();
		
		for(Curso_Aluno ca : listCA) {
			if(ca.getCodigoCurso() == curso.getCodigo()) {
				car.delete(ca);
				break;
			}
		}
		
		//ModelAndView mv = new ModelAndView("redirect:/alunos");
		//mv = this.loadCursos();
	
		return "redirect:/alunos";
	}
	
	private ModelAndView loadCursos() {
		Iterable<Curso> listaCursos = cr.findAll();
		ModelAndView mv = new ModelAndView("aluno/perfilAluno");
		
		Iterable<Curso_Aluno> cursoAlunos = car.findAll();
		List<Curso> cursos1 = new ArrayList<Curso>();
		
		for(Curso_Aluno cursoAluno : cursoAlunos) {
			if (cursoAluno.getCodigoAluno() == this.codigo_Aluno) {
				Curso curso = cr.findByCodigo(cursoAluno.getCodigoCurso());
				cursos1.add(curso);
			}
		}
		
		this.listCursos1 = cursos1;
		
		mv.addObject("listaCursos", listaCursos);
		mv.addObject("cursos1", cursos1);
		
		
		return mv;
	}

	@RequestMapping("/incluirCurso/{codigo}")
	public ModelAndView incluirCurso(@PathVariable("codigo") long codigo) {
		boolean isSalvar = true;
		
		Curso curso = cr.findByCodigo(codigo);
		Aluno aluno = ar.findByCodigo(this.codigo_Aluno);
		System.out.println("codigo:"+ this.codigo_Aluno);
		if(aluno == null) System.out.println("Entrou aqui");

		Curso_Aluno cursoAluno = new Curso_Aluno();
		cursoAluno.setCodigoAluno(aluno.getCodigo());
		cursoAluno.setCodigoCurso(curso.getCodigo());
		ModelAndView mv = new ModelAndView("redirect:/alunos");
		mv.addObject("curso", curso);
				
		for(Curso_Aluno c : car.findAll()) {
			if(c.getCodigoAluno() == aluno.getCodigo() && c.getCodigoCurso() == curso.getCodigo())
				isSalvar = false;
		}
		
		if (isSalvar)
			car.save(cursoAluno);
		
		
		return mv;
	}

	private boolean achou(Aluno aluno) {
		boolean achou = false;
		Iterable<Aluno> alunos = ar.findAll();
		
		for(Aluno a : alunos) {
			if(a.getCpf().equalsIgnoreCase(aluno.getCpf()) || a.getEmail().equalsIgnoreCase(aluno.getEmail()))
				achou = true;
		}
		
		return achou;
	}
	
	private boolean achou(String email, String cpf) {
		boolean achou = false;
		Iterable<Aluno> alunos = ar.findAll();
		
		for(Aluno a : alunos) {
			if(a.getCpf().equalsIgnoreCase(cpf) && a.getEmail().equalsIgnoreCase(email))
				achou = true;
		}
				
		return achou;
	}
	
	private long getCodigoUsuario(String email) {
		long codigo = 0;
		
		Iterable<Usuario> usuarios = ur.findAll();
		
		for(Usuario u : usuarios) {
			if(u.getEmail().equalsIgnoreCase(email))
				codigo = u.getCodigoAluno();
		}
				
		return codigo;
	}
	
	private Aluno getAluno(String cpf) {
		
		Iterable<Aluno> alunos = ar.findAll();
		
		for(Aluno a : alunos) {
			if(a.getCpf().equalsIgnoreCase(cpf))
				return a;
		}
				
		return null;
	}
}
