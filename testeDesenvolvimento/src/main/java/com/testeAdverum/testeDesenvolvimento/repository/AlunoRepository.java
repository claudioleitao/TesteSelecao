package com.testeAdverum.testeDesenvolvimento.repository;

import org.springframework.data.repository.CrudRepository;

import com.testeAdverum.testeDesenvolvimento.models.Aluno;

public interface AlunoRepository extends CrudRepository<Aluno, String> {
	Aluno findByCodigo(long codigo);
}
