package br.com.vins.inveGo.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.vins.inveGo.dto.ItemAcquisitonDto;
import br.com.vins.inveGo.entities.ItemAcquisition;
import br.com.vins.inveGo.entities.ItemChange;
import br.com.vins.inveGo.entities.ItemMovementHistory;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.repositories.FurnitureItemsRepository;
import br.com.vins.inveGo.repositories.ITItemsRepository;
import br.com.vins.inveGo.repositories.ItemAcquisitionRepository;
import br.com.vins.inveGo.repositories.ItemChangeRepository;
import br.com.vins.inveGo.repositories.ItemMovementHistoryRepository;
import br.com.vins.inveGo.repositories.OthersItemsRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ItemService {

	@Autowired
	private ItemAcquisitionRepository acquisitionRepository;

	@Autowired
	private ITItemsRepository itRepository;

	@Autowired
	private FurnitureItemsRepository furnitureRepository;

	@Autowired
	private OthersItemsRepository othersRepository;

	@Autowired
	private ItemChangeRepository changeRepository;

	@Autowired
	private ItemMovementHistoryRepository historyRepository;

	public HttpStatus registerAcquisition(ItemAcquisitonDto acquisition) {
		try {
			if (acquisition.getSupplier() == null || acquisition.getSupplier().isEmpty()
					|| acquisition.getReceiver() == null || acquisition.getReceiver().isEmpty()
					|| acquisition.getProcessNumber() == null || acquisition.getProcessNumber().isEmpty()
					|| acquisition.getInvoiceNumber() == null || acquisition.getInvoiceNumber().isEmpty()
					|| acquisition.getCategory() == null || acquisition.getCategory().isEmpty()
					|| acquisition.getBrand() == null || acquisition.getBrand().isEmpty()
					|| acquisition.getModel() == null || acquisition.getModel().isEmpty()
					|| acquisition.getQuantity() == null || acquisition.getQuantity().isEmpty()
					|| acquisition.getResponsibleUser() == null || acquisition.getResponsibleUser().isEmpty()
					|| acquisition.getDescription() == null || acquisition.getDescription().isEmpty()
					|| acquisition.getReceptionDate() == null) {

				return HttpStatus.BAD_REQUEST;
			}

			ItemAcquisition newAcquisition = new ItemAcquisition();

			newAcquisition.setSupplier(acquisition.getSupplier());
			newAcquisition.setReceiver(acquisition.getReceiver());
			newAcquisition.setProcessNumber(acquisition.getProcessNumber());
			newAcquisition.setInvoiceNumber(acquisition.getInvoiceNumber());
			newAcquisition.setCategory(acquisition.getCategory());
			newAcquisition.setBrand(acquisition.getBrand());
			newAcquisition.setModel(acquisition.getModel());
			newAcquisition.setQuantity(acquisition.getQuantity());
			newAcquisition.setResponsibleUser(acquisition.getResponsibleUser());
			newAcquisition.setDescription(acquisition.getDescription());
			newAcquisition.setReceptionDate(acquisition.getReceptionDate());

			acquisitionRepository.save(newAcquisition);

			return HttpStatus.OK;
		} catch (Exception e) {
			throw new RuntimeException("Falha ao registrar a compra!");
		}

	}

	public String purchasePriceToString(Double purchasePrice) {
		String string = Double.toString(purchasePrice);
		return string;
	}

	public boolean isPatrimonyInUse(String numeroPatrimonio) {
		boolean itInUse = !itRepository.findByPatrimony(numeroPatrimonio).isEmpty();
		boolean furnitureInUse = !furnitureRepository.findByPatrimony(numeroPatrimonio).isEmpty();
		boolean othersInUse = !othersRepository.findByPatrimony(numeroPatrimonio).isEmpty();

		return itInUse || furnitureInUse || othersInUse;
	}

	public boolean isSerialUnique(String serial) {
		boolean itUse = !itRepository.findBySerialNumber(serial).isEmpty();
		boolean othersUse = !othersRepository.findBySerialNumber(serial).isEmpty();
		return itUse || othersUse;
	}

	@Transactional
	public void registerItemChange(String attributeName, String oldValue, String newValue, String patrimony,
			User user) {
		try {
			if (attributeName == null || attributeName.isBlank() || oldValue == null || oldValue.isBlank()
					|| newValue == null || newValue.isBlank() || patrimony == null || patrimony.isBlank()
					|| user == null) {
				throw new IllegalArgumentException("Argumentos inválidos para registrar a mudança do item.");
			}

			ItemChange changeLog = new ItemChange();
			changeLog.setPatrimonyItem(patrimony);
			changeLog.setAttributeName(attributeName);
			changeLog.setOldValue(oldValue);
			changeLog.setNewValue(newValue);
			changeLog.setChangeDateTime(LocalDateTime.now());
			changeLog.setUserRegistration(user.getRegistrationNumber());
			changeLog.setUserName(user.getName() + " " + user.getLastName());

			changeRepository.save(changeLog);

		} catch (DataAccessException e) {
			throw new RuntimeException("Falha ao registrar mudança do item: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw e; 
		} catch (Exception e) {
			throw new RuntimeException("Falha ao registrar mudança do item.");
		}
	}
	
	public byte[] generateMovimentReport(LocalDateTime startDate, LocalDateTime endDate) {
	    try {
	        List<ItemMovementHistory> history = historyRepository.findByMovementDateTimeBetween(startDate, endDate);
	        if (history.isEmpty()) {
	            throw new RuntimeException("Não há registros de movimento dentro do período especificado.");
	        }

	        // Carregar o arquivo do relatório Jasper
	        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/modelMoviment.jrxml"));
	        
	        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        String formattedStartDate = startDate.format(targetFormatter);
	        String formattedEndDate = endDate.format(targetFormatter);

	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("startDate", formattedStartDate);
	        parameters.put("endDate", formattedEndDate);

	        // Crie uma fonte de dados a partir da lista de histórico
	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(history);

	        // Compile o relatório
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

	        JRPdfExporter pdfExporter = new JRPdfExporter();
	        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));

	        // Exporta o relatório para um arquivo PDF
	        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
	        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
	        pdfExporter.exportReport();

	        // Retorna o PDF como um array de bytes
	        return pdfStream.toByteArray();
	    } catch (JRException e) {
	        throw new RuntimeException("Erro ao gerar o relatório. Por favor, tente novamente.");
	    }
	}

	public byte[] generateChangeReport(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			List<ItemChange> history = changeRepository.findByChangeDateTimeBetween(startDate, endDate);
			if (history.isEmpty()) {
				throw new RuntimeException("Não há registros de movimento dentro do período especificado.");
			}

			// Carregar o arquivo do relatório Jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/modelChange.jrxml"));
			
			DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedStartDate = startDate.format(targetFormatter);
			String formattedEndDate = endDate.format(targetFormatter);

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("startDate", formattedStartDate);
			parameters.put("endDate", formattedEndDate);

			// Crie uma fonte de dados a partir da lista de histórico
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(history);

			// Compile o relatório
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			JRPdfExporter pdfExporter = new JRPdfExporter();
	        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));

	        // Exporta o relatório para um arquivo PDF
	        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
	        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfStream));
	        pdfExporter.exportReport();

	        // Retorna o PDF como um array de bytes
	        return pdfStream.toByteArray();
		} catch (JRException e) {
			throw new RuntimeException("Erro ao gerar o relatório. Por favor, tente novamente.");
		}
	}

}
