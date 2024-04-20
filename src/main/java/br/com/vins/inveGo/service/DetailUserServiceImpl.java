package br.com.vins.inveGo.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.vins.inveGo.data.DetailUserData;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.repositories.UserRepository;

@Component
public class DetailUserServiceImpl implements UserDetailsService{

	private final UserRepository repository;

	public DetailUserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> usuario = repository.findByRegistrationNumber(username);
		if(usuario.isEmpty()) {
			throw new UsernameNotFoundException("Usuário com a matrícula[ " + username + "] não encontrado");
		}
		return new DetailUserData(usuario);
	}
}
