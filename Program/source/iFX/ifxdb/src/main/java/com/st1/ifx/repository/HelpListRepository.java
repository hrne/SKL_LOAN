package com.st1.ifx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.st1.ifx.domain.HelpList;

public interface HelpListRepository extends JpaRepository<HelpList, Long>, QuerydslPredicateExecutor<HelpList> {

}
