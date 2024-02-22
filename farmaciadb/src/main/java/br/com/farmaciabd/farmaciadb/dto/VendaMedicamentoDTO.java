package br.com.farmaciabd.farmaciadb.dto;

public class VendaMedicamentoDTO {
    private Long medicamentoId;
    private Integer quantidade;

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
