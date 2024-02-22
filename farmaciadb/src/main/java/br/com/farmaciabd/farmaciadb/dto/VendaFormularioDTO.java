package br.com.farmaciabd.farmaciadb.dto;

import java.util.List;

public class VendaFormularioDTO {
    private long clienteId;
    private long vendedorId;
    private List<VendaMedicamentoDTO> medicamentosVenda;

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    public long getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(long vendedorId) {
        this.vendedorId = vendedorId;
    }

    public List<VendaMedicamentoDTO> getMedicamentosVenda() {
        return medicamentosVenda;
    }

    public void setMedicamentosVenda(List<VendaMedicamentoDTO> medicamentosVenda) {
        this.medicamentosVenda = medicamentosVenda;
    }
}
