package br.com.vins.inveGo.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.vins.inveGo.dto.UserLoginDto;
import br.com.vins.inveGo.dto.UserRegisterDto;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.entities.UserHistory;
import br.com.vins.inveGo.filters.AuthenticateFilter;
import br.com.vins.inveGo.filters.ValidadeFilter;
import br.com.vins.inveGo.repositories.UserHistoryRepository;
import br.com.vins.inveGo.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserHistoryRepository historyRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String loginUser(UserLoginDto login) throws Exception {
		User user = userRepository.findByRegistrationNumber(login.getRegistrationNumber()).orElseThrow(() -> new Exception("Usuário não encontrado"));
		if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
			throw new Exception("Senha inválida");
		}

		String token = JWT.create().withSubject(user.getId().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + AuthenticateFilter.TOKEN_EXPIRACAO))
				.sign(Algorithm.HMAC512(AuthenticateFilter.TOKEN_SENHA));

		return token;
	}
	
	public void registerUser(UserRegisterDto register) {
		try {
			String name = register.getName();
			String lastName = register.getLastName();
			String registartionNumber = register.getRegistrationNumber();
			String password = register.getPassword();

			String encodedPassword = passwordEncoder.encode(password);
			User user = new User();
			user.setName(name);
			user.setLastName(lastName);
			user.setRegistrationNumber(registartionNumber);
			user.setPassword(encodedPassword);
	
			userRepository.save(user);

		} catch (Exception e) {
			throw new RuntimeException("Falha ao registrar o usuário", e);
		}
	}
	
	public List<User> searchUsers() {
		return userRepository.findAll();
	}
	
	@Transactional
	public HttpStatus deleteUser(long id,User responsibleUser) {
		try {
			User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
			UserHistory history = new UserHistory();
			history.setNameUserRemoved(user.getName());
			history.setLastNameUserRemoved(user.getLastName());
			history.setRegistrationNumberUserRemoved(user.getRegistrationNumber());
			history.setRegistrationNumberResponsibleUser(responsibleUser.getRegistrationNumber());
			history.setChangeDateTime(LocalDateTime.now());
			historyRepository.save(history);
			userRepository.delete(user);
			return HttpStatus.OK;
		} catch (RuntimeException e) {
			return HttpStatus.NOT_FOUND;
		} 
		catch (Exception e) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}

	}

	public User getUserFromToken(String token) throws JWTVerificationException {
		try {
			token = token.replace(ValidadeFilter.ATRIBUTO_PREFIXO, "");
			Algorithm algorithm = Algorithm.HMAC512(AuthenticateFilter.TOKEN_SENHA);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			Long userId = Long.parseLong(jwt.getSubject());
			User user = userRepository.findById(userId).orElse(null);
			return user;
		} catch (JWTVerificationException e) {
			throw new IllegalArgumentException("Token inválido");
		}
	}
}
