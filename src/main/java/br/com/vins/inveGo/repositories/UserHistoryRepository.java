package br.com.vins.inveGo.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vins.inveGo.entities.UserHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
	List<UserHistory> findByChangeDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}

