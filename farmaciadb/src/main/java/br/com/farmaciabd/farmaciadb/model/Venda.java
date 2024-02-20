package br.com.farmaciabd.farmaciadb.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Venda{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Medicamento> medicamento;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Vendedor vendedor;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Cliente cliente;
	
	@NotEmpty
	private double precoFinal;
	
	
	public List<Medicamento> getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(List<Medicamento> medicamento) {
		this.medicamento =  medicamento;
	}
	
	public Vendedor getVendedor() {
		return vendedor;
	}
	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public double getPrecoFinal() {
		return precoFinal;
	}
	public void setPrecoFinal(double precoFinal) {
		this.precoFinal = precoFinal;
	}
		
}
