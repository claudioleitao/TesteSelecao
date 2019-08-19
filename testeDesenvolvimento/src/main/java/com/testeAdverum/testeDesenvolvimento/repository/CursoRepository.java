package com.testeAdverum.testeDesenvolvimento.repository;

import org.springframework.data.repository.CrudRepository;

import com.testeAdverum.testeDesenvolvimento.models.Curso;

public interface CursoRepository extends CrudRepository<Curso, String> {
	Curso findByCodigo(long codigo);
}
