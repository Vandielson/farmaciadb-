package br.com.farmaciabd.farmaciadb.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.farmaciabd.farmaciadb.model.Venda;

public interface VendaRepository extends CrudRepository<Venda, Long> {

    Optional<Venda> findById(long id);

    Iterable<Venda> findAll();

}
