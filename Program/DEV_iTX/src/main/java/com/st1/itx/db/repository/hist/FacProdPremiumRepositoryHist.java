package com.st1.itx.db.repository.hist;

import java.util.Optional;

import java.math.BigDecimal;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacProdPremium;
import com.st1.itx.db.domain.FacProdPremiumId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdPremiumRepositoryHist extends JpaRepository<FacProdPremium, FacProdPremiumId> {

	// ProdNo = ,AND PremiumLow >= ,AND PremiumLow <=
	public Slice<FacProdPremium> findAllByProdNoIsAndPremiumLowGreaterThanEqualAndPremiumLowLessThanEqual(String prodNo_0, BigDecimal premiumLow_1, BigDecimal premiumLow_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FacProdPremium> findByFacProdPremiumId(FacProdPremiumId facProdPremiumId);

}
