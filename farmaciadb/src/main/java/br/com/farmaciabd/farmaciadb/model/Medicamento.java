package br.com.farmaciabd.farmaciadb.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Medicamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToMany(mappedBy = "medicamentos")
    private List<Venda> vendas;
	
	@NotEmpty
	private String nome;
	
	@NotEmpty
	private String dosagem;
	
	@NotNull
	private double preco;
	
	@NotNull
	private int quantidade;

	@OneToOne(mappedBy = "medicamento")
	private Promocao promocao;

	public Promocao getPromocao() {
		return promocao;
	}

	public double getDesconto (){
		return this.preco * promocao.getDesconto() / 100.0;
	}
	
	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDosagem() {
		return dosagem;
	}
	public void setDosagem(String dosagem) {
		this.dosagem = dosagem;
	}
	
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
}