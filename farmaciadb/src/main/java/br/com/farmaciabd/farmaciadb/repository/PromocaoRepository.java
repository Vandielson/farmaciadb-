package br.com.farmaciabd.farmaciadb.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.farmaciabd.farmaciadb.model.Promocao;
import org.springframework.data.repository.query.Param;

public interface PromocaoRepository extends CrudRepository<Promocao, Long> {

    Optional<Promocao> findById(long id);

    Iterable<Promocao> findAll();
    
    Promocao findByMedicamentoId(long id);

    @Query("SELECT p FROM Promocao p " +
            "JOIN p.medicamento m " +
            "WHERE (:dataInicio IS NULL OR p.dataInicio >= :dataInicio) " +
            "  AND (:dataFim IS NULL OR p.dataFim <= :dataFim) " +
            "  AND (:idMedicamento IS NULL OR m.id = :idMedicamento)")
    List<Promocao> findByFilter(@Param("dataInicio") Date dataInicio,
                                @Param("dataFim") Date dataFim,
                                @Param("idMedicamento") Long idMedicamento);

	
}
