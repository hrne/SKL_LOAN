package com.st1.ifx.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.st1.ifx.domain.UserPubd;

public interface UserPubReporitory extends JpaRepository<UserPubd, Long> {

	@Lock(LockModeType.PESSIMISTIC_READ)
	UserPubd findByTableNameAndXey(String tableName, String key);

	@Lock(LockModeType.PESSIMISTIC_READ)
	List<UserPubd> findByTableName(String tableName);

	@Lock(LockModeType.PESSIMISTIC_READ)
	UserPubd findByTableNameAndScriptSessionId(String tableName, String scriptSessionid);

	@Query("select count(x) from UserPubd x where x.tableName = (:tableName)")
	Long getTableNameCounts(@Param("tableName") String tableName);
}
