package br.com.vins.inveGo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.vins.inveGo.dto.FurnitureItemsDto;
import br.com.vins.inveGo.dto.ItemTransferDto;
import br.com.vins.inveGo.entities.FurnitureItems;
import br.com.vins.inveGo.entities.ItemMovementHistory;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.service.FurnitureService;
import br.com.vins.inveGo.service.UserService;

@RestController
@RequestMapping("/api/furniture-item")
public class FurnitureController {
	@Autowired
	private FurnitureService service;

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerItem(@RequestBody FurnitureItemsDto item) {
		try {
			service.createItem(item);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Cadastro de item não concluído");
		}
	}

	@PostMapping("/transfer")
	public ResponseEntity<?> transferItem(@RequestBody ItemTransferDto transfer,
			@RequestHeader("Authorization") String token) {
		try {
			User user = userService.getUserFromToken(token);
			HttpStatus status = service.transfer(transfer, user);
			if (status == HttpStatus.OK) {
				return ResponseEntity.ok(null);
			} else {
				return ResponseEntity.status(status).build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/disposed")
	public ResponseEntity<?> disposedItem(@RequestParam String patrimony,
			@RequestHeader("Authorization") String token) {
		try {
			User user = userService.getUserFromToken(token);
			service.disposed(patrimony, user);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Falha ao baixar o item");
		}
	}

	@PostMapping("/search")
	public ResponseEntity<?> searchItem(@RequestBody FurnitureItemsDto search) {
		try {
			List<FurnitureItems> items = service.search(search);
			return ResponseEntity.ok(items);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Falha ao pesquisar por um item");
		}
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<?> getItem(@PathVariable Long id) {
		try {
			FurnitureItems item = service.getItem(id);
			if (item == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado!");
			}

			List<ItemMovementHistory> history = item.getMovementHistory();
			Map<String, Object> response = new HashMap<>();
			response.put("item", item);
			response.put("history", history);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Falha ao recuperar o item!");
		}
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateInfo(@RequestBody FurnitureItemsDto item,
			@RequestHeader("Authorization") String token) {
		try {
			User user = userService.getUserFromToken(token);
			service.updateItem(item, user);

			return ResponseEntity.ok(null);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body("Falha ao atualizar item!");
		}
	}
}
