package br.com.farmaciabd.farmaciadb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Vendedor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String cpf;
	
	@NotEmpty
	private String nome;
	
	@Column(unique = true)
	private String telefone;
	
	@NotNull
	private double salario;
	
	@NotNull
	private double percentualComicao;
	

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = 1400.00;
	}
	
	public double getPercentualComicao() {
		return percentualComicao;
	}
	public void setPercentualComicao(double percentualComicao) {
		this.percentualComicao = 0.02;
	}

}
