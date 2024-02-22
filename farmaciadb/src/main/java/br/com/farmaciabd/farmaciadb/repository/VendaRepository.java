package br.com.farmaciabd.farmaciadb.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.farmaciabd.farmaciadb.model.Venda;
import org.springframework.data.repository.query.Param;

public interface VendaRepository extends CrudRepository<Venda, Long> {

    Optional<Venda> findById(long id);

    Iterable<Venda> findAll();

    @Query("SELECT v FROM Venda v " +
            "JOIN v.medicamentos m " +
            "WHERE (:dataInicio IS NULL OR v.createdAt >= :dataInicio) " +
            "  AND (:dataFim IS NULL OR v.createdAt <= :dataFim) " +
            "  AND (:idMedicamentos IS NULL OR m.id IN :idMedicamentos) " +
            "  AND (:valorVendaMin IS NULL OR v.precoFinal >= :valorVendaMin) " +
            "  AND (:valorVendaMax IS NULL OR v.precoFinal <= :valorVendaMax)")
    List<Venda> findByFilter(@Param("dataInicio") Date dataInicio,
                             @Param("dataFim") Date dataFim,
                             @Param("idMedicamentos") List<Long> idMedicamentos,
                             @Param("valorVendaMin") Double valorVendaMin,
                             @Param("valorVendaMax") Double valorVendaMax);



}
