package com.st1.ifx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.st1.ifx.domain.Ovr;

public interface OvrRepository extends JpaRepository<Ovr, Long> {

	// find buffers
	// @Query("SELECT t FROM Ovr t INNER JOIN FETCH t.buffers b WHERE t.id = (:id)
	// order
	// by b.index")
	@Query("SELECT t FROM Ovr t JOIN FETCH t.buffers WHERE t.id = (:id)")
	Ovr findOneEager(@Param("id") Long id);

}
