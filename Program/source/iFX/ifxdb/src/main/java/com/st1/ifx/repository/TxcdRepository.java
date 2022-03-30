package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.st1.ifx.domain.Txcd;

public interface TxcdRepository extends JpaRepository<Txcd, String> {

	@Query("select t from Txcd t where t.type <> :type ORDER BY t.sbtyp,t.type,t.txcd")
	public List<Txcd> findNotTypeOf(@Param("type") int type);

}
