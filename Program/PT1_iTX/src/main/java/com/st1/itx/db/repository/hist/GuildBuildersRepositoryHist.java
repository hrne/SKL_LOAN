package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.GuildBuilders;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface GuildBuildersRepositoryHist extends JpaRepository<GuildBuilders, Integer> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<GuildBuilders> findByCustNo(int custNo);

}
