package com.testeAdverum.testeDesenvolvimento.repository;

import org.springframework.data.repository.CrudRepository;

import com.testeAdverum.testeDesenvolvimento.models.Curso_Aluno;

public interface CursoAlunoRepository extends CrudRepository<Curso_Aluno, String> {
	Curso_Aluno findByCodigo(long codigo);
}
