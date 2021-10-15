package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacProdPremium 商品參數副檔年繳保費優惠減碼<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacProdPremiumId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4760665412854722347L;

// 商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo = " ";

	// 保戶壽險年繳化保費(含)以上
	@Column(name = "`PremiumLow`")
	private BigDecimal premiumLow = new BigDecimal("0");

	public FacProdPremiumId() {
	}

	public FacProdPremiumId(String prodNo, BigDecimal premiumLow) {
		this.prodNo = prodNo;
		this.premiumLow = premiumLow;
	}

	/**
	 * 商品代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 商品代碼<br>
	 * 
	 *
	 * @param prodNo 商品代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	/**
	 * 保戶壽險年繳化保費(含)以上<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPremiumLow() {
		return this.premiumLow;
	}

	/**
	 * 保戶壽險年繳化保費(含)以上<br>
	 * 
	 *
	 * @param premiumLow 保戶壽險年繳化保費(含)以上
	 */
	public void setPremiumLow(BigDecimal premiumLow) {
		this.premiumLow = premiumLow;
	}

	@Override
	public int hashCode() {
		return Objects.hash(prodNo, premiumLow);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		FacProdPremiumId facProdPremiumId = (FacProdPremiumId) obj;
		return prodNo.equals(facProdPremiumId.prodNo) && premiumLow == facProdPremiumId.premiumLow;
	}

	@Override
	public String toString() {
		return "FacProdPremiumId [prodNo=" + prodNo + ", premiumLow=" + premiumLow + "]";
	}
}
