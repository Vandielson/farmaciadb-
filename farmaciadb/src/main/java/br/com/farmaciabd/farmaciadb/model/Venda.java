package br.com.farmaciabd.farmaciadb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Cliente cliente;
    
    @ManyToOne
    private Vendedor vendedor;
    
    @ManyToMany
    private List<Medicamento> medicamentos;

    @ManyToMany
    private List<Promocao> promocao;

    @NotNull
    private double precoFinal;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    

    public Venda() {
        this.medicamentos = new ArrayList<>();
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public List<Promocao> getPromocao() {
		return promocao;
	}

	public void setPromocao(Iterable<Promocao> promocoes) {

        this.promocao = (List<Promocao>) promocoes;
	}

	public double getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(double precoFinal) {
        this.precoFinal = precoFinal;
    }
    
    public void calcularPrecoFinal() {
        double precoTotal = 0.0;
        for (Medicamento medicamento : medicamentos) {
            double precoItem = medicamento.getPreco();
            if(medicamento.getPromocao() != null) {
            	precoItem -= medicamento.getDesconto();
            }
            precoTotal += precoItem;
        }
        this.precoFinal = precoTotal;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Iterable<Medicamento> medicamentos) {
        this.medicamentos = (List<Medicamento>) medicamentos;
    }

}
