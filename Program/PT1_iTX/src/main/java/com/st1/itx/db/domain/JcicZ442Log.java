package com.st1.itx.db.domain;

import java.io.Serializable;
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
 * JcicZ442Log 前置調解回報無擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ442Log`")
public class JcicZ442Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4164104262633609768L;

	@EmbeddedId
	private JcicZ442LogId jcicZ442LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 是否為最大債權金融機構報送
	/* Y;N */
	@Column(name = "`IsMaxMain`", length = 1)
	private String isMaxMain;

	// 是否為本金融機構債務人
	/* Y;N */
	@Column(name = "`IsClaims`", length = 1)
	private String isClaims;

	// 本金融機構有擔保債權筆數
	@Column(name = "`GuarLoanCnt`")
	private int guarLoanCnt = 0;

	// 依民法第323條計算之信用放款本息餘額
	@Column(name = "`Civil323ExpAmt`")
	private int civil323ExpAmt = 0;

	// 依民法第323條計算之現金卡放款本息餘額
	@Column(name = "`Civil323CashAmt`")
	private int civil323CashAmt = 0;

	// 依民法第323條計算之信用卡本息餘額
	@Column(name = "`Civil323CreditAmt`")
	private int civil323CreditAmt = 0;

	// 依民法第323條計算之保證債權本息餘額
	@Column(name = "`Civil323GuarAmt`")
	private int civil323GuarAmt = 0;

	// 信用放款本金
	@Column(name = "`ReceExpPrin`")
	private int receExpPrin = 0;

	// 信用放款利息
	@Column(name = "`ReceExpInte`")
	private int receExpInte = 0;

	// 信用放款違約金
	@Column(name = "`ReceExpPena`")
	private int receExpPena = 0;

	// 信用放款其他費用
	@Column(name = "`ReceExpOther`")
	private int receExpOther = 0;

	// 現金卡本金
	@Column(name = "`CashCardPrin`")
	private int cashCardPrin = 0;

	// 現金卡利息
	@Column(name = "`CashCardInte`")
	private int cashCardInte = 0;

	// 現金卡違約金
	@Column(name = "`CashCardPena`")
	private int cashCardPena = 0;

	// 現金卡其他費用
	@Column(name = "`CashCardOther`")
	private int cashCardOther = 0;

	// 信用卡本金
	@Column(name = "`CreditCardPrin`")
	private int creditCardPrin = 0;

	// 信用卡利息
	@Column(name = "`CreditCardInte`")
	private int creditCardInte = 0;

	// 信用卡違約金
	@Column(name = "`CreditCardPena`")
	private int creditCardPena = 0;

	// 信用卡其他費用
	@Column(name = "`CreditCardOther`")
	private int creditCardOther = 0;

	// 保證債權本金
	@Column(name = "`GuarObliPrin`")
	private int guarObliPrin = 0;

	// 保證債權利息
	@Column(name = "`GuarObliInte`")
	private int guarObliInte = 0;

	// 保證債權違約金
	@Column(name = "`GuarObliPena`")
	private int guarObliPena = 0;

	// 保證債權其他費用
	@Column(name = "`GuarObliOther`")
	private int guarObliOther = 0;

	// 轉JCIC文字檔日期
	@Column(name = "`OutJcicTxtDate`")
	private int outJcicTxtDate = 0;

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

	public JcicZ442LogId getJcicZ442LogId() {
		return this.jcicZ442LogId;
	}

	public void setJcicZ442LogId(JcicZ442LogId jcicZ442LogId) {
		this.jcicZ442LogId = jcicZ442LogId;
	}

	/**
	 * 流水號<br>
	 * 
	 * @return String
	 */
	public String getUkey() {
		return this.ukey == null ? "" : this.ukey;
	}

	/**
	 * 流水號<br>
	 * 
	 *
	 * @param ukey 流水號
	 */
	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param txSeq 交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 是否為最大債權金融機構報送<br>
	 * Y;N
	 * 
	 * @return String
	 */
	public String getIsMaxMain() {
		return this.isMaxMain == null ? "" : this.isMaxMain;
	}

	/**
	 * 是否為最大債權金融機構報送<br>
	 * Y;N
	 *
	 * @param isMaxMain 是否為最大債權金融機構報送
	 */
	public void setIsMaxMain(String isMaxMain) {
		this.isMaxMain = isMaxMain;
	}

	/**
	 * 是否為本金融機構債務人<br>
	 * Y;N
	 * 
	 * @return String
	 */
	public String getIsClaims() {
		return this.isClaims == null ? "" : this.isClaims;
	}

	/**
	 * 是否為本金融機構債務人<br>
	 * Y;N
	 *
	 * @param isClaims 是否為本金融機構債務人
	 */
	public void setIsClaims(String isClaims) {
		this.isClaims = isClaims;
	}

	/**
	 * 本金融機構有擔保債權筆數<br>
	 * 
	 * @return Integer
	 */
	public int getGuarLoanCnt() {
		return this.guarLoanCnt;
	}

	/**
	 * 本金融機構有擔保債權筆數<br>
	 * 
	 *
	 * @param guarLoanCnt 本金融機構有擔保債權筆數
	 */
	public void setGuarLoanCnt(int guarLoanCnt) {
		this.guarLoanCnt = guarLoanCnt;
	}

	/**
	 * 依民法第323條計算之信用放款本息餘額<br>
	 * 
	 * @return Integer
	 */
	public int getCivil323ExpAmt() {
		return this.civil323ExpAmt;
	}

	/**
	 * 依民法第323條計算之信用放款本息餘額<br>
	 * 
	 *
	 * @param civil323ExpAmt 依民法第323條計算之信用放款本息餘額
	 */
	public void setCivil323ExpAmt(int civil323ExpAmt) {
		this.civil323ExpAmt = civil323ExpAmt;
	}

	/**
	 * 依民法第323條計算之現金卡放款本息餘額<br>
	 * 
	 * @return Integer
	 */
	public int getCivil323CashAmt() {
		return this.civil323CashAmt;
	}

	/**
	 * 依民法第323條計算之現金卡放款本息餘額<br>
	 * 
	 *
	 * @param civil323CashAmt 依民法第323條計算之現金卡放款本息餘額
	 */
	public void setCivil323CashAmt(int civil323CashAmt) {
		this.civil323CashAmt = civil323CashAmt;
	}

	/**
	 * 依民法第323條計算之信用卡本息餘額<br>
	 * 
	 * @return Integer
	 */
	public int getCivil323CreditAmt() {
		return this.civil323CreditAmt;
	}

	/**
	 * 依民法第323條計算之信用卡本息餘額<br>
	 * 
	 *
	 * @param civil323CreditAmt 依民法第323條計算之信用卡本息餘額
	 */
	public void setCivil323CreditAmt(int civil323CreditAmt) {
		this.civil323CreditAmt = civil323CreditAmt;
	}

	/**
	 * 依民法第323條計算之保證債權本息餘額<br>
	 * 
	 * @return Integer
	 */
	public int getCivil323GuarAmt() {
		return this.civil323GuarAmt;
	}

	/**
	 * 依民法第323條計算之保證債權本息餘額<br>
	 * 
	 *
	 * @param civil323GuarAmt 依民法第323條計算之保證債權本息餘額
	 */
	public void setCivil323GuarAmt(int civil323GuarAmt) {
		this.civil323GuarAmt = civil323GuarAmt;
	}

	/**
	 * 信用放款本金<br>
	 * 
	 * @return Integer
	 */
	public int getReceExpPrin() {
		return this.receExpPrin;
	}

	/**
	 * 信用放款本金<br>
	 * 
	 *
	 * @param receExpPrin 信用放款本金
	 */
	public void setReceExpPrin(int receExpPrin) {
		this.receExpPrin = receExpPrin;
	}

	/**
	 * 信用放款利息<br>
	 * 
	 * @return Integer
	 */
	public int getReceExpInte() {
		return this.receExpInte;
	}

	/**
	 * 信用放款利息<br>
	 * 
	 *
	 * @param receExpInte 信用放款利息
	 */
	public void setReceExpInte(int receExpInte) {
		this.receExpInte = receExpInte;
	}

	/**
	 * 信用放款違約金<br>
	 * 
	 * @return Integer
	 */
	public int getReceExpPena() {
		return this.receExpPena;
	}

	/**
	 * 信用放款違約金<br>
	 * 
	 *
	 * @param receExpPena 信用放款違約金
	 */
	public void setReceExpPena(int receExpPena) {
		this.receExpPena = receExpPena;
	}

	/**
	 * 信用放款其他費用<br>
	 * 
	 * @return Integer
	 */
	public int getReceExpOther() {
		return this.receExpOther;
	}

	/**
	 * 信用放款其他費用<br>
	 * 
	 *
	 * @param receExpOther 信用放款其他費用
	 */
	public void setReceExpOther(int receExpOther) {
		this.receExpOther = receExpOther;
	}

	/**
	 * 現金卡本金<br>
	 * 
	 * @return Integer
	 */
	public int getCashCardPrin() {
		return this.cashCardPrin;
	}

	/**
	 * 現金卡本金<br>
	 * 
	 *
	 * @param cashCardPrin 現金卡本金
	 */
	public void setCashCardPrin(int cashCardPrin) {
		this.cashCardPrin = cashCardPrin;
	}

	/**
	 * 現金卡利息<br>
	 * 
	 * @return Integer
	 */
	public int getCashCardInte() {
		return this.cashCardInte;
	}

	/**
	 * 現金卡利息<br>
	 * 
	 *
	 * @param cashCardInte 現金卡利息
	 */
	public void setCashCardInte(int cashCardInte) {
		this.cashCardInte = cashCardInte;
	}

	/**
	 * 現金卡違約金<br>
	 * 
	 * @return Integer
	 */
	public int getCashCardPena() {
		return this.cashCardPena;
	}

	/**
	 * 現金卡違約金<br>
	 * 
	 *
	 * @param cashCardPena 現金卡違約金
	 */
	public void setCashCardPena(int cashCardPena) {
		this.cashCardPena = cashCardPena;
	}

	/**
	 * 現金卡其他費用<br>
	 * 
	 * @return Integer
	 */
	public int getCashCardOther() {
		return this.cashCardOther;
	}

	/**
	 * 現金卡其他費用<br>
	 * 
	 *
	 * @param cashCardOther 現金卡其他費用
	 */
	public void setCashCardOther(int cashCardOther) {
		this.cashCardOther = cashCardOther;
	}

	/**
	 * 信用卡本金<br>
	 * 
	 * @return Integer
	 */
	public int getCreditCardPrin() {
		return this.creditCardPrin;
	}

	/**
	 * 信用卡本金<br>
	 * 
	 *
	 * @param creditCardPrin 信用卡本金
	 */
	public void setCreditCardPrin(int creditCardPrin) {
		this.creditCardPrin = creditCardPrin;
	}

	/**
	 * 信用卡利息<br>
	 * 
	 * @return Integer
	 */
	public int getCreditCardInte() {
		return this.creditCardInte;
	}

	/**
	 * 信用卡利息<br>
	 * 
	 *
	 * @param creditCardInte 信用卡利息
	 */
	public void setCreditCardInte(int creditCardInte) {
		this.creditCardInte = creditCardInte;
	}

	/**
	 * 信用卡違約金<br>
	 * 
	 * @return Integer
	 */
	public int getCreditCardPena() {
		return this.creditCardPena;
	}

	/**
	 * 信用卡違約金<br>
	 * 
	 *
	 * @param creditCardPena 信用卡違約金
	 */
	public void setCreditCardPena(int creditCardPena) {
		this.creditCardPena = creditCardPena;
	}

	/**
	 * 信用卡其他費用<br>
	 * 
	 * @return Integer
	 */
	public int getCreditCardOther() {
		return this.creditCardOther;
	}

	/**
	 * 信用卡其他費用<br>
	 * 
	 *
	 * @param creditCardOther 信用卡其他費用
	 */
	public void setCreditCardOther(int creditCardOther) {
		this.creditCardOther = creditCardOther;
	}

	/**
	 * 保證債權本金<br>
	 * 
	 * @return Integer
	 */
	public int getGuarObliPrin() {
		return this.guarObliPrin;
	}

	/**
	 * 保證債權本金<br>
	 * 
	 *
	 * @param guarObliPrin 保證債權本金
	 */
	public void setGuarObliPrin(int guarObliPrin) {
		this.guarObliPrin = guarObliPrin;
	}

	/**
	 * 保證債權利息<br>
	 * 
	 * @return Integer
	 */
	public int getGuarObliInte() {
		return this.guarObliInte;
	}

	/**
	 * 保證債權利息<br>
	 * 
	 *
	 * @param guarObliInte 保證債權利息
	 */
	public void setGuarObliInte(int guarObliInte) {
		this.guarObliInte = guarObliInte;
	}

	/**
	 * 保證債權違約金<br>
	 * 
	 * @return Integer
	 */
	public int getGuarObliPena() {
		return this.guarObliPena;
	}

	/**
	 * 保證債權違約金<br>
	 * 
	 *
	 * @param guarObliPena 保證債權違約金
	 */
	public void setGuarObliPena(int guarObliPena) {
		this.guarObliPena = guarObliPena;
	}

	/**
	 * 保證債權其他費用<br>
	 * 
	 * @return Integer
	 */
	public int getGuarObliOther() {
		return this.guarObliOther;
	}

	/**
	 * 保證債權其他費用<br>
	 * 
	 *
	 * @param guarObliOther 保證債權其他費用
	 */
	public void setGuarObliOther(int guarObliOther) {
		this.guarObliOther = guarObliOther;
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉JCIC文字檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
		this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
		return "JcicZ442Log [jcicZ442LogId=" + jcicZ442LogId + ", tranKey=" + tranKey + ", isMaxMain=" + isMaxMain + ", isClaims=" + isClaims + ", guarLoanCnt=" + guarLoanCnt + ", civil323ExpAmt="
				+ civil323ExpAmt + ", civil323CashAmt=" + civil323CashAmt + ", civil323CreditAmt=" + civil323CreditAmt + ", civil323GuarAmt=" + civil323GuarAmt + ", receExpPrin=" + receExpPrin
				+ ", receExpInte=" + receExpInte + ", receExpPena=" + receExpPena + ", receExpOther=" + receExpOther + ", cashCardPrin=" + cashCardPrin + ", cashCardInte=" + cashCardInte
				+ ", cashCardPena=" + cashCardPena + ", cashCardOther=" + cashCardOther + ", creditCardPrin=" + creditCardPrin + ", creditCardInte=" + creditCardInte + ", creditCardPena="
				+ creditCardPena + ", creditCardOther=" + creditCardOther + ", guarObliPrin=" + guarObliPrin + ", guarObliInte=" + guarObliInte + ", guarObliPena=" + guarObliPena + ", guarObliOther="
				+ guarObliOther + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
