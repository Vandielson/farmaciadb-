package br.com.farmaciabd.farmaciadb.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne(cascade = CascadeType.ALL)
    private Promocao promocao;

    @NotNull
    private double precoFinal;

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

	public Promocao getPromocao() {
		return promocao;
	}

	public void setPromocao(Promocao promocao) {
		this.promocao = promocao;
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
            precoTotal += precoItem;
        }
        if (promocao != null) {
            precoTotal -= precoTotal * promocao.getDesconto() / 100.0;
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
