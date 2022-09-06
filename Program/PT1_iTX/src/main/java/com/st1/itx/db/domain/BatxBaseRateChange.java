package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BatxBaseRateChange 整批指標利率調整檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BatxBaseRateChange`")
public class BatxBaseRateChange implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 531364140134739516L;

	@EmbeddedId
	private BatxBaseRateChangeId batxBaseRateChangeId;

	// 調整日期
	@Column(name = "`AdjDate`", insertable = false, updatable = false)
	private int adjDate = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

	// 指標利率代碼
	/* CdCode.ProdBaseRateCode01:保單分紅利率02:郵政儲金利率 99:自訂利率 */
	@Column(name = "`BaseRateCode`", length = 2)
	private String baseRateCode;

	// 原指標利率
	/* 前次指標利率 */
	@Column(name = "`OriBaseRate`")
	private BigDecimal oriBaseRate = new BigDecimal("0");

	// 指標利率生效日
	@Column(name = "`BaseRateEffectDate`")
	private int baseRateEffectDate = 0;

	// 指標利率
	/* 本次指標利率 */
	@Column(name = "`BaseRate`")
	private BigDecimal baseRate = new BigDecimal("0");

	// 適用利率
	/* 更新後的借戶利率檔適用利率 */
	@Column(name = "`FitRate`")
	private BigDecimal fitRate = new BigDecimal("0");

	// 放款利率變動檔生效日
	/* 利率未變動為零 */
	@Column(name = "`TxEffectDate`")
	private int txEffectDate = 0;

	// jason格式紀錄欄
	@Column(name = "`JsonFields`", length = 2000)
	private String jsonFields;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6)
	private String titaTlrNo;

	// 交易序號
	@Column(name = "`TitaTxtNo`", length = 8)
	private String titaTxtNo;

	// 建檔日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 最後更新日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	// 最後更新人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	public BatxBaseRateChangeId getBatxBaseRateChangeId() {
		return this.batxBaseRateChangeId;
	}

	public void setBatxBaseRateChangeId(BatxBaseRateChangeId batxBaseRateChangeId) {
		this.batxBaseRateChangeId = batxBaseRateChangeId;
	}

	/**
	 * 調整日期<br>
	 * 
	 * @return Integer
	 */
	public int getAdjDate() {
		return StaticTool.bcToRoc(this.adjDate);
	}

	/**
	 * 調整日期<br>
	 * 
	 *
	 * @param adjDate 調整日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAdjDate(int adjDate) throws LogicException {
		this.adjDate = StaticTool.rocToBc(adjDate);
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
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
	 * 指標利率代碼<br>
	 * CdCode.ProdBaseRateCode 01:保單分紅利率 02:郵政儲金利率 99:自訂利率
	 * 
	 * @return String
	 */
	public String getBaseRateCode() {
		return this.baseRateCode == null ? "" : this.baseRateCode;
	}

	/**
	 * 指標利率代碼<br>
	 * CdCode.ProdBaseRateCode 01:保單分紅利率 02:郵政儲金利率 99:自訂利率
	 *
	 * @param baseRateCode 指標利率代碼
	 */
	public void setBaseRateCode(String baseRateCode) {
		this.baseRateCode = baseRateCode;
	}

	/**
	 * 原指標利率<br>
	 * 前次指標利率
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOriBaseRate() {
		return this.oriBaseRate;
	}

	/**
	 * 原指標利率<br>
	 * 前次指標利率
	 *
	 * @param oriBaseRate 原指標利率
	 */
	public void setOriBaseRate(BigDecimal oriBaseRate) {
		this.oriBaseRate = oriBaseRate;
	}

	/**
	 * 指標利率生效日<br>
	 * 
	 * @return Integer
	 */
	public int getBaseRateEffectDate() {
		return StaticTool.bcToRoc(this.baseRateEffectDate);
	}

	/**
	 * 指標利率生效日<br>
	 * 
	 *
	 * @param baseRateEffectDate 指標利率生效日
	 * @throws LogicException when Date Is Warn
	 */
	public void setBaseRateEffectDate(int baseRateEffectDate) throws LogicException {
		this.baseRateEffectDate = StaticTool.rocToBc(baseRateEffectDate);
	}

	/**
	 * 指標利率<br>
	 * 本次指標利率
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBaseRate() {
		return this.baseRate;
	}

	/**
	 * 指標利率<br>
	 * 本次指標利率
	 *
	 * @param baseRate 指標利率
	 */
	public void setBaseRate(BigDecimal baseRate) {
		this.baseRate = baseRate;
	}

	/**
	 * 適用利率<br>
	 * 更新後的借戶利率檔適用利率
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFitRate() {
		return this.fitRate;
	}

	/**
	 * 適用利率<br>
	 * 更新後的借戶利率檔適用利率
	 *
	 * @param fitRate 適用利率
	 */
	public void setFitRate(BigDecimal fitRate) {
		this.fitRate = fitRate;
	}

	/**
	 * 放款利率變動檔生效日<br>
	 * 利率未變動為零
	 * 
	 * @return Integer
	 */
	public int getTxEffectDate() {
		return StaticTool.bcToRoc(this.txEffectDate);
	}

	/**
	 * 放款利率變動檔生效日<br>
	 * 利率未變動為零
	 *
	 * @param txEffectDate 放款利率變動檔生效日
	 * @throws LogicException when Date Is Warn
	 */
	public void setTxEffectDate(int txEffectDate) throws LogicException {
		this.txEffectDate = StaticTool.rocToBc(txEffectDate);
	}

	/**
	 * jason格式紀錄欄<br>
	 * 
	 * @return String
	 */
	public String getJsonFields() {
		return this.jsonFields == null ? "" : this.jsonFields;
	}

	/**
	 * jason格式紀錄欄<br>
	 * 
	 *
	 * @param jsonFields jason格式紀錄欄
	 */
	public void setJsonFields(String jsonFields) {
		this.jsonFields = jsonFields;
	}

	/**
	 * 經辦<br>
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 *
	 * @param createDate 建檔日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建檔人員<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建檔人員<br>
	 * 
	 *
	 * @param createEmpNo 建檔人員
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 *
	 * @param lastUpdate 最後更新日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後更新人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	@Override
	public String toString() {
		return "BatxBaseRateChange [batxBaseRateChangeId=" + batxBaseRateChangeId + ", prodNo=" + prodNo + ", baseRateCode=" + baseRateCode + ", oriBaseRate=" + oriBaseRate + ", baseRateEffectDate="
				+ baseRateEffectDate + ", baseRate=" + baseRate + ", fitRate=" + fitRate + ", txEffectDate=" + txEffectDate + ", jsonFields=" + jsonFields + ", titaTlrNo=" + titaTlrNo + ", titaTxtNo="
				+ titaTxtNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
