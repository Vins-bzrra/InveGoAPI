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

import br.com.vins.inveGo.dto.FurnitureItemsDto;
import br.com.vins.inveGo.dto.ItemTransferDto;
import br.com.vins.inveGo.entities.FurnitureItems;
import br.com.vins.inveGo.entities.ItemMovementHistory;
import br.com.vins.inveGo.entities.User;
import br.com.vins.inveGo.repositories.FurnitureItemsRepository;
import br.com.vins.inveGo.repositories.ItemMovementHistoryRepository;

@Service
public class FurnitureService {
	@Autowired
	private FurnitureItemsRepository repository;
	
	@Autowired
	private ItemMovementHistoryRepository movementHistoryRepository;
	
	@Autowired
	private ItemService itemService;

	public void createItem(FurnitureItemsDto item) {
		try {
			FurnitureItems furnitureItems = new FurnitureItems();
			
			furnitureItems.setBrand(item.getBrand());
	        furnitureItems.setModel(item.getModel());
	        
	        if (itemService.isPatrimonyInUse(item.getPatrimony())) {
				throw new RuntimeException("O número de patrimônio já está em uso por outro item.");
			}
	        
	        furnitureItems.setPatrimony(item.getPatrimony());
	        furnitureItems.setAcquisitionDate(item.getAcquisitionDate());
	        furnitureItems.setSupplier(item.getSupplier());
	        furnitureItems.setUnitLocation(item.getUnitLocation());
	        furnitureItems.setStatus(item.getStatus());
	        furnitureItems.setPurchasePrice(item.getPurchasePrice());
	        furnitureItems.setCurrentOwner(item.getCurrentOwner());
	        furnitureItems.setCategory(item.getCategory());
	        furnitureItems.setDescription(item.getDescription());
	        furnitureItems.setProcessNumber(item.getProcessNumber());
	        furnitureItems.setMaterial(item.getMaterial());
	        furnitureItems.setDimensions(item.getDimensions());
	        furnitureItems.setColor(item.getColor());

	        repository.save(furnitureItems);
			
		} catch (Exception e) {
			throw new RuntimeException("Falha ao registrar o item", e);
		}	
	}
	
	public HttpStatus transfer(ItemTransferDto transfer, User user) {
		try {
			String patrimony = transfer.getPatrimony();
			String newOwner = transfer.getNewOwner();
			String newUnitLocation = transfer.getNewUnitLocation();

			Optional<FurnitureItems> optionalItem = repository.findByPatrimony(patrimony);
			if (optionalItem.isEmpty()) {
				throw new RuntimeException("Item não localizado!");
			}

			FurnitureItems item = optionalItem.get();
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
			Optional<FurnitureItems> optionalItem = repository.findByPatrimony(patrimony);
			if (optionalItem.isEmpty()) {
				throw new RuntimeException("Item não localizado!");
			}
			FurnitureItems item = optionalItem.get();
			
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
	
	public List<FurnitureItems> search(FurnitureItemsDto search) {
	    try {
	        Specification<FurnitureItems> specification = (root, query, criteriaBuilder) -> {
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

				if (search.getProcessNumber() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("processNumber")), "%" + search.getProcessNumber().toUpperCase() + "%"));
				}

				if (search.getDescription() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + search.getDescription().toUpperCase() + "%"));
				}

				if (search.getMaterial() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("material")), "%" + search.getMaterial().toUpperCase() + "%"));
				}

				if (search.getDimensions() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("dimensions")), "%" + search.getDimensions().toUpperCase() + "%"));
				}

				if (search.getColor() != null) {
				    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("color")), "%" + search.getColor().toUpperCase() + "%"));
				}

	            // Combina todas as condições com o operador "AND" e retorna o resultado
	            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	        };

	        return repository.findAll(specification);

	    } catch (Exception e) {
	        throw new RuntimeException("Falha ao pesquisar por um item!");
	    }
	}
	
	public FurnitureItems getItem(Long id) {

		return repository.findById(id).orElse(null);
	}
	
	public void updateItem(FurnitureItemsDto itemDto, User user) {
	    try {
	        Optional<FurnitureItems> optionalItem = repository.findById(itemDto.getId());
	        if (optionalItem.isPresent()) {
	        	FurnitureItems item = optionalItem.get();
	            String patrimony = item.getPatrimony();
	            if (itemDto.getPatrimony() != null && !itemDto.getPatrimony().isBlank()
						&& !itemDto.getPatrimony().equals(patrimony)) {
					if (itemService.isPatrimonyInUse(itemDto.getPatrimony())) {
						throw new RuntimeException("O número de patrimônio já está em uso por outro item.");
					}

				}

	            if (itemDto.getBrand() != null && !itemDto.getBrand().isEmpty()) {
	            	itemService.registerItemChange("brand", item.getBrand(), itemDto.getBrand(), item.getPatrimony(), user);
	                item.setBrand(itemDto.getBrand());
	            }
	            if (itemDto.getModel() != null && !itemDto.getModel().isEmpty()) {
	            	itemService.registerItemChange("model", item.getModel(), itemDto.getModel(), item.getPatrimony(), user);
	                item.setModel(itemDto.getModel());
	            }
	            if (itemDto.getAcquisitionDate() != null) {
	            	itemService.registerItemChange("acquisitionDate", item.getAcquisitionDate().toString(), itemDto.getAcquisitionDate().toString(), item.getPatrimony(), user);
	                item.setAcquisitionDate(itemDto.getAcquisitionDate());
	            }
	            if (itemDto.getSupplier() != null && !itemDto.getSupplier().isEmpty()) {
	            	itemService.registerItemChange("supplier", item.getSupplier(), itemDto.getSupplier(), item.getPatrimony(), user);
	                item.setSupplier(itemDto.getSupplier());
	            }
	            if (itemDto.getUnitLocation() != null && !itemDto.getUnitLocation().isEmpty()) {
	            	itemService.registerItemChange("unitLocation", item.getUnitLocation(), itemDto.getUnitLocation(), item.getPatrimony(), user);
	                item.setUnitLocation(itemDto.getUnitLocation());
	            }
	            if (itemDto.getStatus() != null && !itemDto.getStatus().isEmpty()) {
	            	itemService.registerItemChange("status", item.getStatus(), itemDto.getStatus(), item.getPatrimony(), user);
	                item.setStatus(itemDto.getStatus());
	            }
	            if (itemDto.getPurchasePrice() != 0.0 && itemDto.getPurchasePrice() != item.getPurchasePrice()) {
					String priceItem = itemService.purchasePriceToString(item.getPurchasePrice());
					String priceDto = itemService.purchasePriceToString(itemDto.getPurchasePrice());
					itemService.registerItemChange("purchasePrice", priceItem, priceDto, item.getPatrimony(), user);
					item.setPurchasePrice(itemDto.getPurchasePrice());
				}
	            if (itemDto.getCurrentOwner() != null && !itemDto.getCurrentOwner().isEmpty()) {
	            	itemService.registerItemChange("currentOwner", item.getCurrentOwner(), itemDto.getCurrentOwner(), item.getPatrimony(), user);
	                item.setCurrentOwner(itemDto.getCurrentOwner());
	            }
	            if (itemDto.getCategory() != null && !itemDto.getCategory().isEmpty()) {
	            	itemService.registerItemChange("category", item.getCategory(), itemDto.getCategory(), item.getPatrimony(), user);
	                item.setCategory(itemDto.getCategory());
	            }
	            if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
	            	itemService.registerItemChange("Description", item.getDescription(), itemDto.getDescription(), item.getPatrimony(), user);
	                item.setDescription(itemDto.getDescription());
	            }
	            if (itemDto.getProcessNumber() != null && !itemDto.getProcessNumber().isEmpty()) {
	            	itemService.registerItemChange("processNumber", item.getProcessNumber(), itemDto.getProcessNumber(), item.getPatrimony(), user);
	                item.setProcessNumber(itemDto.getProcessNumber());
	            }
	            if (itemDto.getMaterial() != null && !itemDto.getMaterial().isEmpty()) {
	            	itemService.registerItemChange("material", item.getMaterial(), itemDto.getMaterial(), item.getPatrimony(), user);
	                item.setMaterial(itemDto.getMaterial());
	            }
	            if (itemDto.getDimensions() != null && !itemDto.getDimensions().isEmpty()) {
	            	itemService.registerItemChange("dimensions", item.getDimensions(), itemDto.getDimensions(), item.getPatrimony(), user);
	                item.setDimensions(itemDto.getDimensions());
	            }
	            if (itemDto.getColor() != null && !itemDto.getColor().isEmpty()) {
	            	itemService.registerItemChange("color", item.getColor(), itemDto.getColor(), item.getPatrimony(), user);
	                item.setColor(itemDto.getColor());
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
