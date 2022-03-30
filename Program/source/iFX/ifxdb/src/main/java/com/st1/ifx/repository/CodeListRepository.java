package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.st1.ifx.domain.CodeList;

public interface CodeListRepository extends JpaRepository<CodeList, Long>, QuerydslPredicateExecutor<CodeList> {

	List<CodeList> findByHelpAndSegment(String help, String segment);

	CodeList findByHelpAndSegmentAndXey(String help, String segment, String key);
}
