package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.st1.ifx.domain.LocalRim;

public interface LocalRimRepository extends JpaRepository<LocalRim, Long>, QuerydslPredicateExecutor<LocalRim> {

	LocalRim findByTableNameAndXey(String tableName, String key);

	List<LocalRim> findByTableName(String tableName);

}
