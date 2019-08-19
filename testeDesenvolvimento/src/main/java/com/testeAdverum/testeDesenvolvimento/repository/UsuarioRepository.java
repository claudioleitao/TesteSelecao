package com.testeAdverum.testeDesenvolvimento.repository;

import org.springframework.data.repository.CrudRepository;

import com.testeAdverum.testeDesenvolvimento.models.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {
	Usuario findByCodigo(long codigo);
}
