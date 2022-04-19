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
 * JcicZ041Log 協商開始暨停催通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ041Log`")
public class JcicZ041Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1472345354112432355L;

	@EmbeddedId
	private JcicZ041LogId jcicZ041LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增,C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 停催日期
	/* 西元年月 */
	@Column(name = "`ScDate`")
	private int scDate = 0;

	// 協商開始日
	/* 西元年月 */
	@Column(name = "`NegoStartDate`")
	private int negoStartDate = 0;

	// 非金融機構債權金額
	/* 單位新台幣元,右靠左補零。指債務人於申請前置協商時檢附之民間債權人清冊中,各項非金融機構債權之總金額,以債務人自行填寫之金額為主。 */
	@Column(name = "`NonFinClaimAmt`")
	private int nonFinClaimAmt = 0;

	// 轉出JCIC文字檔日期
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

	public JcicZ041LogId getJcicZ041LogId() {
		return this.jcicZ041LogId;
	}

	public void setJcicZ041LogId(JcicZ041LogId jcicZ041LogId) {
		this.jcicZ041LogId = jcicZ041LogId;
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
	 * A:新增,C:異動
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增,C:異動
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 停催日期<br>
	 * 西元年月
	 * 
	 * @return Integer
	 */
	public int getScDate() {
		return StaticTool.bcToRoc(this.scDate);
	}

	/**
	 * 停催日期<br>
	 * 西元年月
	 *
	 * @param scDate 停催日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setScDate(int scDate) throws LogicException {
		this.scDate = StaticTool.rocToBc(scDate);
	}

	/**
	 * 協商開始日<br>
	 * 西元年月
	 * 
	 * @return Integer
	 */
	public int getNegoStartDate() {
		return StaticTool.bcToRoc(this.negoStartDate);
	}

	/**
	 * 協商開始日<br>
	 * 西元年月
	 *
	 * @param negoStartDate 協商開始日
	 * @throws LogicException when Date Is Warn
	 */
	public void setNegoStartDate(int negoStartDate) throws LogicException {
		this.negoStartDate = StaticTool.rocToBc(negoStartDate);
	}

	/**
	 * 非金融機構債權金額<br>
	 * 單位新台幣元,右靠左補零。指債務人於申請前置協商時檢附之民間債權人清冊中,各項非金融機構債權之總金額,以債務人自行填寫之金額為主。
	 * 
	 * @return Integer
	 */
	public int getNonFinClaimAmt() {
		return this.nonFinClaimAmt;
	}

	/**
	 * 非金融機構債權金額<br>
	 * 單位新台幣元,右靠左補零。指債務人於申請前置協商時檢附之民間債權人清冊中,各項非金融機構債權之總金額,以債務人自行填寫之金額為主。
	 *
	 * @param nonFinClaimAmt 非金融機構債權金額
	 */
	public void setNonFinClaimAmt(int nonFinClaimAmt) {
		this.nonFinClaimAmt = nonFinClaimAmt;
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉出JCIC文字檔日期
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
		return "JcicZ041Log [jcicZ041LogId=" + jcicZ041LogId + ", tranKey=" + tranKey + ", scDate=" + scDate + ", negoStartDate=" + negoStartDate + ", nonFinClaimAmt=" + nonFinClaimAmt
				+ ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
