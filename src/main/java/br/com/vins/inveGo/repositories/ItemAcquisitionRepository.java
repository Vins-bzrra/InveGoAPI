package br.com.vins.inveGo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vins.inveGo.entities.ItemAcquisition;

@Repository
public interface ItemAcquisitionRepository extends JpaRepository<ItemAcquisition, Long>{
	
}
