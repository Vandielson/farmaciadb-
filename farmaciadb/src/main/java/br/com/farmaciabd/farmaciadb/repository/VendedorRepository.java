package br.com.farmaciabd.farmaciadb.repository;

import br.com.farmaciabd.farmaciadb.model.Vendedor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface VendedorRepository extends CrudRepository<Vendedor, Long>{
    
    Vendedor findByCpf(String cpf);
    
    List<Vendedor> findByNome(String nome);

    Vendedor findByTelefone(String telefone);

    Optional<Vendedor> findById(long id);

    Iterable<Vendedor> findAll();

}
