package com.testeAdverum.testeDesenvolvimento.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Curso_Aluno implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codigo;
	
	private long codigoAluno;
	private long codigoCurso;
	
	public long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	
	public long getCodigoAluno() {
		return codigoAluno;
	}
	
	public void setCodigoAluno(long codigoAluno) {
		this.codigoAluno = codigoAluno;
	}
	
	public long getCodigoCurso() {
		return codigoCurso;
	}
	
	public void setCodigoCurso(long codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
}
