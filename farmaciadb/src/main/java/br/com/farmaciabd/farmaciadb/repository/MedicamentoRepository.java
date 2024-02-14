
package br.com.farmaciabd.farmaciadb.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import br.com.farmaciabd.farmaciadb.model.Medicamento;
import jakarta.validation.Valid;

public interface MedicamentoRepository extends CrudRepository<Medicamento, Long> {
	
	Optional<Medicamento> findById(long id);
	
	List<Medicamento> findByNome(String nome);
	
	Iterable<Medicamento> findAll();
	
}

