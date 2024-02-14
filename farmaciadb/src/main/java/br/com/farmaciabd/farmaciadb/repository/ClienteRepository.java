package br.com.farmaciabd.farmaciadb.repository;

import br.com.farmaciabd.farmaciadb.model.Cliente;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Long>{
	
	Optional<Cliente> findById(long id);
	
	List<Cliente> findByNome(String nome);
	
	Iterable<Cliente> findAll();

	Cliente findByCpf(String cpf);

	Cliente findByTelefone(String telefone);

}
