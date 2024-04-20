package br.com.vins.inveGo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.vins.inveGo.dto.ITItemsDto;
import br.com.vins.inveGo.dto.ItemTransferDto;
import br.com.vins.inveGo.entities.ITItems;
import br.com.vins.inveGo.entities.ItemMovementHistory;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.repositories.ITItemsRepository;
import br.com.vins.inveGo.repositories.ItemMovementHistoryRepository;

@Service
public class ITItemsService {

	@Autowired
	private ITItemsRepository repository;

	@Autowired
	private ItemMovementHistoryRepository movementHistoryRepository;

	@Autowired
	private ItemService itemService;

	public void createItem(ITItemsDto item) {
		try {
			ITItems itItem = new ITItems();
			itItem.setBrand(item.getBrand());
			itItem.setAcquisitionDate(item.getAcquisitionDate());
			itItem.setCategory(item.getCategory());
			itItem.setCurrentOwner(item.getCurrentOwner());
			itItem.setDescription(item.getDescription());
			itItem.setModel(item.getModel());

			if (itemService.isPatrimonyInUse(item.getPatrimony())) {
				throw new RuntimeException("O número de patrimônio já está em uso por outro item.");
			}

			if (itemService.isSerialUnique(item.getSerialNumber())) {
				throw new RuntimeException("O Serial informado já está registrado em outro item.");
			}

			itItem.setPatrimony(item.getPatrimony());
			itItem.setPurchasePrice(item.getPurchasePrice());
			itItem.setSerialNumber(item.getSerialNumber());
			itItem.setStatus(item.getStatus());
			itItem.setSupplier(item.getSupplier());
			itItem.setUnitLocation(item.getUnitLocation());
			itItem.setProcessNumber(item.getProcessNumber());

			repository.save(itItem);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Falha ao registrar o item", e);
		}
	}

	public HttpStatus transfer(ItemTransferDto transfer, User user) {
		try {
			String patrimony = transfer.getPatrimony();
			String newOwner = transfer.getNewOwner();
			String newUnitLocation = transfer.getNewUnitLocation();

			Optional<ITItems> optionalItem = repository.findByPatrimony(patrimony);
			if (optionalItem.isEmpty()) {
				throw new RuntimeException("Item não localizado!");
			}

			ITItems item = optionalItem.get();
			String previousOwner = item.getCurrentOwner();
			String previousUnitLocation = item.getUnitLocation();

			boolean isValidNewUnitLocation = newUnitLocation != null && !newUnitLocation.isEmpty()
					&& newUnitLocation.length() <= 150;

			boolean isValidNewOwner = newOwner != null && !newOwner.isEmpty() && newOwner.length() <= 100;

			if (isValidNewOwner && !isValidNewUnitLocation) {
				item.setCurrentOwner(newOwner);
			} else if (!isValidNewOwner && isValidNewUnitLocation) {
				item.setUnitLocation(newUnitLocation);
			} else if (isValidNewOwner && isValidNewUnitLocation) {
				item.setCurrentOwner(newOwner);
				item.setUnitLocation(newUnitLocation);
			} else {
				throw new RuntimeException("Nenhuma opção de transferência preenchida");
			}

			ItemMovementHistory movementHistory = new ItemMovementHistory();
			movementHistory.setItem(item);
			movementHistory.setPreviousOwner(previousOwner);
			movementHistory.setPreviousUnitLocation(previousUnitLocation);
			movementHistory.setNewOwner(item.getCurrentOwner());
			movementHistory.setNewUnitLocation(item.getUnitLocation());
			movementHistory.setMovementType("Transferência");
			movementHistory.setNameUser(user.getName() + " " + user.getLastName());
			movementHistory.setRegistrationUser(user.getRegistrationNumber());
			movementHistory.setMovementDateTime(LocalDateTime.now());

			movementHistoryRepository.save(movementHistory);

			repository.save(item);

			return HttpStatus.OK;
		} catch (Exception e) {
			return HttpStatus.BAD_GATEWAY;
		}
	}

	@Transactional
	public void disposed(String patrimony, User user) {
		try {
			Optional<ITItems> optionalItem = repository.findByPatrimony(patrimony);
			if (optionalItem.isEmpty()) {
				throw new RuntimeException("Item não localizado!");
			}
			ITItems item = optionalItem.get();

			ItemMovementHistory movementHistory = new ItemMovementHistory();
			movementHistory.setItem(item);
			movementHistory.setPreviousOwner(item.getCurrentOwner());
			movementHistory.setPreviousUnitLocation(item.getUnitLocation());
			movementHistory.setNewOwner("Item Baixado");
			movementHistory.setNewUnitLocation("Item Baixado");
			movementHistory.setMovementType("Baixa");
			movementHistory.setNameUser(user.getName() + " " + user.getLastName());
			movementHistory.setRegistrationUser(user.getRegistrationNumber());
			movementHistory.setMovementDateTime(LocalDateTime.now());
			movementHistoryRepository.save(movementHistory);

			item.setStatus("Baixado");
			item.setCurrentOwner("Baixado");
			item.setUnitLocation("Baixado");

			repository.save(item);
		} catch (Exception e) {
			throw new RuntimeException("Falha ao baixar o item!");
		}

	}

	public List<ITItems> search(ITItemsDto search) {
		try {
			Specification<ITItems> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = new ArrayList<>();

				if (search.getBrand() != null) {
					predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("brand")), "%" + search.getBrand().toUpperCase() + "%"));
				}

				if (search.getModel() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("model")), "%" + search.getModel().toUpperCase() + "%"));
				}

				if (search.getPatrimony() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("patrimony")), "%" + search.getPatrimony().toUpperCase() + "%"));
				}

				if (search.getAcquisitionDate() != null) {
				    predicates.add(criteriaBuilder.equal(root.get("acquisitionDate"), search.getAcquisitionDate()));
				}

				if (search.getSupplier() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("supplier")), "%" + search.getSupplier().toUpperCase() + "%"));
				}

				if (search.getUnitLocation() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("unitLocation")), "%" + search.getUnitLocation().toUpperCase() + "%"));
				}

				if (search.getStatus() != null) {
				    predicates.add(criteriaBuilder.equal(root.get("status"), search.getStatus()));
				}

				if (search.getPurchasePrice() > 0) {
				    predicates.add(criteriaBuilder.equal(root.get("purchasePrice"), search.getPurchasePrice()));
				}

				if (search.getCurrentOwner() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("currentOwner")), "%" + search.getCurrentOwner().toUpperCase() + "%"));
				}

				if (search.getCategory() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("category")), "%" + search.getCategory().toUpperCase() + "%"));
				}

				if (search.getSerialNumber() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("serialNumber")), "%" + search.getSerialNumber().toUpperCase() + "%"));
				}

				if (search.getProcessNumber() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("processNumber")), "%" + search.getProcessNumber().toUpperCase() + "%"));
				}

				if (search.getDescription() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + search.getDescription().toUpperCase() + "%"));
				}

				// Combina todas as condições com o operador "AND" e retorna o resultado
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			return repository.findAll(specification);

		} catch (Exception e) {
			throw new RuntimeException("Falha ao pesquisar por um item!");
		}

	}

	public ITItems getItem(Long id) {

		return repository.findById(id).orElse(null);
	}

	@Transactional
	public void updateItem(ITItemsDto itemDto, User user) {
		try {
			Optional<ITItems> optionalItem = repository.findById(itemDto.getId());
			if (optionalItem.isPresent()) {
				ITItems item = optionalItem.get();
				String patrimony = item.getPatrimony();
				if (itemDto.getPatrimony() != null && !itemDto.getPatrimony().isBlank()
						&& !itemDto.getPatrimony().equals(patrimony)) {
					if (itemService.isPatrimonyInUse(itemDto.getPatrimony())) {
						throw new RuntimeException("O número de patrimônio já está em uso por outro item.");
					}

				}

				if (itemDto.getSerialNumber() != null && !itemDto.getSerialNumber().isBlank()
						&& !itemDto.getSerialNumber().equals(item.getSerialNumber())) {
					if (itemService.isSerialUnique(itemDto.getSerialNumber())) {
						throw new RuntimeException("O Serial informado já está registrado em outro item.");
					}

				}

				if (itemDto.getBrand() != null && !itemDto.getBrand().isEmpty()
						&& !itemDto.getBrand().equals(item.getBrand())) {
					itemService.registerItemChange("brand", item.getBrand(), itemDto.getBrand(), item.getPatrimony(), user);
					item.setBrand(itemDto.getBrand());
				}

				if (itemDto.getModel() != null && !itemDto.getModel().isEmpty()
						&& !itemDto.getModel().equals(item.getModel())) {
					itemService.registerItemChange("model", item.getModel(), itemDto.getModel(), item.getPatrimony(), user);
					item.setModel(itemDto.getModel());
				}

				if (itemDto.getAcquisitionDate() != null
						&& !itemDto.getAcquisitionDate().equals(item.getAcquisitionDate())) {
					itemService.registerItemChange("acquisitionDate", item.getAcquisitionDate().toString(),
							itemDto.getAcquisitionDate().toString(), item.getPatrimony(), user);
					item.setAcquisitionDate(itemDto.getAcquisitionDate());
				}

				if (itemDto.getSupplier() != null && !itemDto.getSupplier().isEmpty()
						&& !itemDto.getSupplier().equals(item.getSupplier())) {
					itemService.registerItemChange("supplier", item.getSupplier(), itemDto.getSupplier(), item.getPatrimony(), user);
					item.setSupplier(itemDto.getSupplier());
				}

				if (itemDto.getUnitLocation() != null && !itemDto.getUnitLocation().isEmpty()
						&& !itemDto.getUnitLocation().equals(item.getUnitLocation())) {
					itemService.registerItemChange("unitLocation", item.getUnitLocation(), itemDto.getUnitLocation(), item.getPatrimony(),
							user);
					item.setUnitLocation(itemDto.getUnitLocation());
				}

				if (itemDto.getStatus() != null && !itemDto.getStatus().isEmpty()
						&& !itemDto.getStatus().equals(item.getStatus())) {
					itemService.registerItemChange("status", item.getStatus(), itemDto.getStatus(), item.getPatrimony(), user);
					item.setStatus(itemDto.getStatus());
				}

				if (itemDto.getPurchasePrice() != 0.0 && itemDto.getPurchasePrice() != item.getPurchasePrice()) {
					String priceItem = itemService.purchasePriceToString(item.getPurchasePrice());
					String priceDto = itemService.purchasePriceToString(itemDto.getPurchasePrice());
					itemService.registerItemChange("purchasePrice", priceItem, priceDto, item.getPatrimony(), user);
					item.setPurchasePrice(itemDto.getPurchasePrice());
				}

				if (itemDto.getCurrentOwner() != null && !itemDto.getCurrentOwner().isEmpty()
						&& !itemDto.getCurrentOwner().equals(item.getCurrentOwner())) {
					itemService.registerItemChange("currentOwner", item.getCurrentOwner(), itemDto.getCurrentOwner(), item.getPatrimony(),
							user);
					item.setCurrentOwner(itemDto.getCurrentOwner());
				}

				if (itemDto.getCategory() != null && !itemDto.getCategory().isEmpty()
						&& !itemDto.getCategory().equals(item.getCategory())) {
					itemService.registerItemChange("category", item.getCategory(), itemDto.getCategory(), item.getPatrimony(), user);
					item.setCategory(itemDto.getCategory());
				}

				if (itemDto.getSerialNumber() != null && !itemDto.getSerialNumber().isEmpty()
						&& !itemDto.getSerialNumber().equals(item.getSerialNumber())) {
					itemService.registerItemChange("serialNumber", item.getSerialNumber(), itemDto.getSerialNumber(), item.getPatrimony(),
							user);
					item.setSerialNumber(itemDto.getSerialNumber());
				}

				if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()
						&& !itemDto.getDescription().equals(item.getDescription())) {
					itemService.registerItemChange("Description", item.getDescription(), itemDto.getDescription(), item.getPatrimony(),
							user);
					item.setDescription(itemDto.getDescription());
				}

				if (itemDto.getProcessNumber() != null && !itemDto.getProcessNumber().isEmpty()
						&& !itemDto.getProcessNumber().equals(item.getProcessNumber())) {
					itemService.registerItemChange("processNumber", item.getProcessNumber(), itemDto.getProcessNumber(),
							item.getPatrimony(), user);
					item.setProcessNumber(itemDto.getProcessNumber());
				}

				if (itemDto.getPatrimony() != null && !itemDto.getPatrimony().isEmpty()
						&& !itemDto.getPatrimony().equals(item.getPatrimony())) {
					itemService.registerItemChange("patrimony", item.getPatrimony(), itemDto.getPatrimony(), item.getPatrimony(), user);
					item.setPatrimony(itemDto.getPatrimony());
				}
				repository.save(item);
			} else {
				throw new RuntimeException("Item não encontrado");
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Falha ao atualizar o item", e);
		}
		

	}

}
