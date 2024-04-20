package br.com.vins.inveGo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vins.inveGo.dto.UserLoginDto;
import br.com.vins.inveGo.dto.UserRegisterDto;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.security.BlacklistToken;
import br.com.vins.inveGo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BlacklistToken tokenBlacklist;

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLogin) {
		try {
			String token = userService.loginUser(userLogin);
			return ResponseEntity.ok(token);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Email ou senha inválidos");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto register) {
		try {
			userService.registerUser(register);
			return ResponseEntity.ok(null);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body("Cadastro não concluído: " + e.getMessage());
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUsers(@RequestHeader("Authorization") String token) {
		try {
			List<User> users = userService.searchUsers();
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token, @PathVariable long id) {
		try {
			User user = userService.getUserFromToken(token);

			HttpStatus status = userService.deleteUser(id,user);

			if (status == HttpStatus.OK) {
				return ResponseEntity.status(status).body("Usuário deletado com sucesso");
			} else if(status == HttpStatus.NOT_FOUND) {
				return ResponseEntity.status(status).body("Usuário não encontrado");
			}else {
				return ResponseEntity.badRequest().build();
			}
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/logout")
	public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
		try {
			tokenBlacklist.invalidateToken(token);
			if (tokenBlacklist.isTokenInvalidated(token)) {
				return ResponseEntity.ok("Logout realizado com sucesso");
			} else {
				return ResponseEntity.badRequest().body("O token não pôde ser invalidado.");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}
