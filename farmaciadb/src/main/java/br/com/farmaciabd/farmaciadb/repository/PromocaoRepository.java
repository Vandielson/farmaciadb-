package br.com.farmaciabd.farmaciadb.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.farmaciabd.farmaciadb.model.Promocao;

public interface PromocaoRepository extends CrudRepository<Promocao, Long> {

    Optional<Promocao> findById(long id);

    Iterable<Promocao> findAll();
    
    Promocao findByMedicamentoId(long id);
	
}
