package br.com.vins.inveGo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vins.inveGo.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByRegistrationNumber(String registrationNumber);

	Optional<User> findById(Long id);
	
}
