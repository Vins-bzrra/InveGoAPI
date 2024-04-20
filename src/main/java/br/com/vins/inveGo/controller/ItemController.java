package br.com.vins.inveGo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.vins.inveGo.dto.ItemAcquisitonDto;
import br.com.vins.inveGo.service.ItemService;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	
	@Autowired
	private ItemService service;
	
	@PostMapping("/acquisition")
	public ResponseEntity<?> acquisitionRegister(@RequestBody ItemAcquisitonDto acquisition){
		try {
			HttpStatus status = service.registerAcquisition(acquisition);
			if(status == HttpStatus.OK) {
				return ResponseEntity.ok(null);
			}else {
				return ResponseEntity.status(status).body("Dados insulficientes para registrar a compra!");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/moviment-report")
    public ResponseEntity<?> MovimentReport(
        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate
    ) {
        try {
            byte[] reportData = service.generateMovimentReport(startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Defina o tipo de conteúdo apropriado para o seu caso, como PDF
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportData);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
	
	@GetMapping("/change-report")
    public ResponseEntity<?> ChangesReport(
        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate
    ) {
        try {
            byte[] reportData = service.generateChangeReport(startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Defina o tipo de conteúdo apropriado para o seu caso, como PDF
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportData);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
