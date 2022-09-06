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
 * BatxDetail 整批入帳明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BatxDetail`")
public class BatxDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5532489009951153305L;

	@EmbeddedId
	private BatxDetailId batxDetailId;

	// 會計日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 整批批號
	/* 暫收冲正固定為RESV00 */
	@Column(name = "`BatchNo`", length = 6, insertable = false, updatable = false)
	private String batchNo;

	// 明細序號
	/* 04.支票兌現時，按額度分拆 */
	@Column(name = "`DetailSeq`", insertable = false, updatable = false)
	private int detailSeq = 0;

	// 還款來源
	/*
	 * CdCode.BatchRepayCode01:匯款轉帳02:銀行扣款03:員工扣款04:支票兌現05:法院扣薪06:理賠金07:代收款-債權協商09:
	 * 其他11:匯款轉帳預先作業90:暫收抵繳99:暫收沖正
	 */
	@Column(name = "`RepayCode`")
	private int repayCode = 0;

	// 檔名
	@Column(name = "`FileName`", length = 50)
	private String fileName;

	// 入帳日期
	@Column(name = "`EntryDate`")
	private int entryDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 銷帳編號
	/* 支票：帳號(9)+票號(7) */
	@Column(name = "`RvNo`", length = 30)
	private String rvNo;

	// 還款類別
	/*
	 * CdCode.RepayType1:期款2:部分償還3:結案4:帳管費5:火險費6:契變手續費7:法務費9:其他11:債協匯入款(
	 * 虛擬帳號為9510500NNNNNNN)12:催收收回
	 */
	@Column(name = "`RepayType`")
	private int repayType = 0;

	// 對帳類別
	/*
	 * CdCode.ReconCodeP01:銀行存款－郵局P02:銀行存款－新光A1~A7
	 * (P03銀行存款－新光匯款轉帳)P04:銀行存款－台新TEM:員工扣薪15/非15???TCK:支票
	 */
	@Column(name = "`ReconCode`", length = 3)
	private String reconCode;

	// 來源會計科目
	/* 11+5+2L4210 其他來源建檔 */
	@Column(name = "`RepayAcCode`", length = 18)
	private String repayAcCode;

	// 還款總金額
	/* 還款時，回寫目前還款總金額 */
	@Column(name = "`AcquiredAmt`")
	private BigDecimal acquiredAmt = new BigDecimal("0");

	// 還款金額
	@Column(name = "`RepayAmt`")
	private BigDecimal repayAmt = new BigDecimal("0");

	// 已作帳金額
	@Column(name = "`AcctAmt`")
	private BigDecimal acctAmt = new BigDecimal("0");

	// 未作帳金額
	@Column(name = "`DisacctAmt`")
	private BigDecimal disacctAmt = new BigDecimal("0");

	// 處理狀態
	/* CdCode.ProcStsCode0:未檢核1:失敗2:人工處理3:檢核錯誤4:檢核正常5:單筆入帳6;批次入帳7;轉暫收D:刪除 */
	@Column(name = "`ProcStsCode`", length = 1)
	private String procStsCode;

	// 處理代碼
	/*
	 * 參照ProcCode分頁00003:溢繳00004:不足利息[可跨額度暫收抵用]00005:積欠期款[可跨額度暫收抵用]00101:正負對沖00102:
	 * 提款(借方)00103:預先作業00104:ACH手續費00105:銀扣清算00106:特殊摘要00110:更正轉帳00120:法院00201:
	 * 存款不足00202:非委託用戶00203:已終止委託用戶00204:無此帳號00205:收受者統編錯誤00206:無此用戶號碼00207:
	 * 用戶號碼不符00208:信用卡額度不足00209:未開卡00210:部分存款不足00211:超過扣款限額00222:帳戶已結清00223:靜止戶00224
	 * :凍結戶00225:帳戶存款遭法院強制執行00226:警示戶00227:該用戶已死亡00228:發動行申請停止入扣帳00291:請參考備註一00299:
	 * 其他00303:已終止代繳 00306:凍結警示戶 00307:支票專戶 00308:帳號錯誤 00309:終止戶
	 * 00310:身分證不符00311:轉出戶 00312:拒絕往來戶 00313:無此編號 00314:編號已存在 00316:管制帳戶 00317:掛失戶
	 * 00318:異常帳戶 00319:編號非英數 00391:期限未扣款
	 * 00398:其他00401:員工扣薪失敗00402:扣款不足00501:退票(支票號碼、支票面額)
	 */
	@Column(name = "`ProcCode`", length = 5)
	private String procCode;

	// 處理說明
	/* jsonformat處理說明+備註(例：不足金額)支票：金額#RP_CHQUEAMTX(16) */
	@Column(name = "`ProcNote`", length = 2000)
	private String procNote;

	// 其他說明
	/* jsonformat */
	@Column(name = "`OtherNote`", length = 2000)
	private String otherNote;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6)
	private String titaTlrNo;

	// 交易序號
	/* 批號後兩碼+明細序號 */
	@Column(name = "`TitaTxtNo`", length = 8)
	private String titaTxtNo;

	// 媒體日期
	@Column(name = "`MediaDate`")
	private int mediaDate = 0;

	// 媒體別
	/* 1:ACH新光2:ACH他行3:郵局4:15日5:非15日 */
	@Column(name = "`MediaKind`", length = 1)
	private String mediaKind;

	// 媒體序號
	@Column(name = "`MediaSeq`")
	private int mediaSeq = 0;

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

	public BatxDetailId getBatxDetailId() {
		return this.batxDetailId;
	}

	public void setBatxDetailId(BatxDetailId batxDetailId) {
		this.batxDetailId = batxDetailId;
	}

	/**
	 * 會計日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 會計日期<br>
	 * 
	 *
	 * @param acDate 會計日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 整批批號<br>
	 * 暫收冲正固定為RESV00
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 整批批號<br>
	 * 暫收冲正固定為RESV00
	 *
	 * @param batchNo 整批批號
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 明細序號<br>
	 * 04.支票兌現時，按額度分拆
	 * 
	 * @return Integer
	 */
	public int getDetailSeq() {
		return this.detailSeq;
	}

	/**
	 * 明細序號<br>
	 * 04.支票兌現時，按額度分拆
	 *
	 * @param detailSeq 明細序號
	 */
	public void setDetailSeq(int detailSeq) {
		this.detailSeq = detailSeq;
	}

	/**
	 * 還款來源<br>
	 * CdCode.BatchRepayCode 01:匯款轉帳 02:銀行扣款 03:員工扣款 04:支票兌現 05:法院扣薪 06:理賠金
	 * 07:代收款-債權協商 09:其他 11:匯款轉帳預先作業 90:暫收抵繳 99:暫收沖正
	 * 
	 * @return Integer
	 */
	public int getRepayCode() {
		return this.repayCode;
	}

	/**
	 * 還款來源<br>
	 * CdCode.BatchRepayCode 01:匯款轉帳 02:銀行扣款 03:員工扣款 04:支票兌現 05:法院扣薪 06:理賠金
	 * 07:代收款-債權協商 09:其他 11:匯款轉帳預先作業 90:暫收抵繳 99:暫收沖正
	 *
	 * @param repayCode 還款來源
	 */
	public void setRepayCode(int repayCode) {
		this.repayCode = repayCode;
	}

	/**
	 * 檔名<br>
	 * 
	 * @return String
	 */
	public String getFileName() {
		return this.fileName == null ? "" : this.fileName;
	}

	/**
	 * 檔名<br>
	 * 
	 *
	 * @param fileName 檔名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 入帳日期<br>
	 * 
	 * @return Integer
	 */
	public int getEntryDate() {
		return StaticTool.bcToRoc(this.entryDate);
	}

	/**
	 * 入帳日期<br>
	 * 
	 *
	 * @param entryDate 入帳日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntryDate(int entryDate) throws LogicException {
		this.entryDate = StaticTool.rocToBc(entryDate);
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
	 * 銷帳編號<br>
	 * 支票：帳號(9)+票號(7)
	 * 
	 * @return String
	 */
	public String getRvNo() {
		return this.rvNo == null ? "" : this.rvNo;
	}

	/**
	 * 銷帳編號<br>
	 * 支票：帳號(9)+票號(7)
	 *
	 * @param rvNo 銷帳編號
	 */
	public void setRvNo(String rvNo) {
		this.rvNo = rvNo;
	}

	/**
	 * 還款類別<br>
	 * CdCode.RepayType 1:期款 2:部分償還 3:結案 4:帳管費 5:火險費 6:契變手續費 7:法務費 9:其他
	 * 11:債協匯入款(虛擬帳號為9510500NNNNNNN) 12:催收收回
	 * 
	 * @return Integer
	 */
	public int getRepayType() {
		return this.repayType;
	}

	/**
	 * 還款類別<br>
	 * CdCode.RepayType 1:期款 2:部分償還 3:結案 4:帳管費 5:火險費 6:契變手續費 7:法務費 9:其他
	 * 11:債協匯入款(虛擬帳號為9510500NNNNNNN) 12:催收收回
	 *
	 * @param repayType 還款類別
	 */
	public void setRepayType(int repayType) {
		this.repayType = repayType;
	}

	/**
	 * 對帳類別<br>
	 * CdCode.ReconCode P01:銀行存款－郵局 P02:銀行存款－新光 A1~A7 (P03銀行存款－新光匯款轉帳) P04:銀行存款－台新
	 * TEM:員工扣薪15/非15??? TCK:支票
	 * 
	 * @return String
	 */
	public String getReconCode() {
		return this.reconCode == null ? "" : this.reconCode;
	}

	/**
	 * 對帳類別<br>
	 * CdCode.ReconCode P01:銀行存款－郵局 P02:銀行存款－新光 A1~A7 (P03銀行存款－新光匯款轉帳) P04:銀行存款－台新
	 * TEM:員工扣薪15/非15??? TCK:支票
	 *
	 * @param reconCode 對帳類別
	 */
	public void setReconCode(String reconCode) {
		this.reconCode = reconCode;
	}

	/**
	 * 來源會計科目<br>
	 * 11+5+2 L4210 其他來源建檔
	 * 
	 * @return String
	 */
	public String getRepayAcCode() {
		return this.repayAcCode == null ? "" : this.repayAcCode;
	}

	/**
	 * 來源會計科目<br>
	 * 11+5+2 L4210 其他來源建檔
	 *
	 * @param repayAcCode 來源會計科目
	 */
	public void setRepayAcCode(String repayAcCode) {
		this.repayAcCode = repayAcCode;
	}

	/**
	 * 還款總金額<br>
	 * 還款時，回寫目前還款總金額
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAcquiredAmt() {
		return this.acquiredAmt;
	}

	/**
	 * 還款總金額<br>
	 * 還款時，回寫目前還款總金額
	 *
	 * @param acquiredAmt 還款總金額
	 */
	public void setAcquiredAmt(BigDecimal acquiredAmt) {
		this.acquiredAmt = acquiredAmt;
	}

	/**
	 * 還款金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRepayAmt() {
		return this.repayAmt;
	}

	/**
	 * 還款金額<br>
	 * 
	 *
	 * @param repayAmt 還款金額
	 */
	public void setRepayAmt(BigDecimal repayAmt) {
		this.repayAmt = repayAmt;
	}

	/**
	 * 已作帳金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAcctAmt() {
		return this.acctAmt;
	}

	/**
	 * 已作帳金額<br>
	 * 
	 *
	 * @param acctAmt 已作帳金額
	 */
	public void setAcctAmt(BigDecimal acctAmt) {
		this.acctAmt = acctAmt;
	}

	/**
	 * 未作帳金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDisacctAmt() {
		return this.disacctAmt;
	}

	/**
	 * 未作帳金額<br>
	 * 
	 *
	 * @param disacctAmt 未作帳金額
	 */
	public void setDisacctAmt(BigDecimal disacctAmt) {
		this.disacctAmt = disacctAmt;
	}

	/**
	 * 處理狀態<br>
	 * CdCode.ProcStsCode 0:未檢核 1:失敗 2:人工處理 3:檢核錯誤 4:檢核正常 5:單筆入帳 6;批次入帳 7;轉暫收 D:刪除
	 * 
	 * @return String
	 */
	public String getProcStsCode() {
		return this.procStsCode == null ? "" : this.procStsCode;
	}

	/**
	 * 處理狀態<br>
	 * CdCode.ProcStsCode 0:未檢核 1:失敗 2:人工處理 3:檢核錯誤 4:檢核正常 5:單筆入帳 6;批次入帳 7;轉暫收 D:刪除
	 *
	 * @param procStsCode 處理狀態
	 */
	public void setProcStsCode(String procStsCode) {
		this.procStsCode = procStsCode;
	}

	/**
	 * 處理代碼<br>
	 * 參照ProcCode分頁 00003:溢繳 00004:不足利息[可跨額度暫收抵用] 00005:積欠期款[可跨額度暫收抵用] 00101:正負對沖
	 * 00102:提款(借方) 00103:預先作業 00104:ACH手續費 00105:銀扣清算 00106:特殊摘要 00110:更正轉帳
	 * 00120:法院 00201:存款不足 00202:非委託用戶 00203:已終止委託用戶 00204:無此帳號 00205:收受者統編錯誤
	 * 00206:無此用戶號碼 00207:用戶號碼不符 00208:信用卡額度不足 00209:未開卡 00210:部分存款不足 00211:超過扣款限額
	 * 00222:帳戶已結清 00223:靜止戶 00224:凍結戶 00225:帳戶存款遭法院強制執行 00226:警示戶 00227:該用戶已死亡
	 * 00228:發動行申請停止入扣帳 00291:請參考備註一 00299:其他 00303:已終止代繳 00306:凍結警示戶 00307:支票專戶
	 * 00308:帳號錯誤 00309:終止戶 00310:身分證不符 00311:轉出戶 00312:拒絕往來戶 00313:無此編號 00314:編號已存在
	 * 00316:管制帳戶 00317:掛失戶 00318:異常帳戶 00319:編號非英數 00391:期限未扣款 00398:其他 00401:員工扣薪失敗
	 * 00402:扣款不足 00501:退票(支票號碼、支票面額)
	 * 
	 * @return String
	 */
	public String getProcCode() {
		return this.procCode == null ? "" : this.procCode;
	}

	/**
	 * 處理代碼<br>
	 * 參照ProcCode分頁 00003:溢繳 00004:不足利息[可跨額度暫收抵用] 00005:積欠期款[可跨額度暫收抵用] 00101:正負對沖
	 * 00102:提款(借方) 00103:預先作業 00104:ACH手續費 00105:銀扣清算 00106:特殊摘要 00110:更正轉帳
	 * 00120:法院 00201:存款不足 00202:非委託用戶 00203:已終止委託用戶 00204:無此帳號 00205:收受者統編錯誤
	 * 00206:無此用戶號碼 00207:用戶號碼不符 00208:信用卡額度不足 00209:未開卡 00210:部分存款不足 00211:超過扣款限額
	 * 00222:帳戶已結清 00223:靜止戶 00224:凍結戶 00225:帳戶存款遭法院強制執行 00226:警示戶 00227:該用戶已死亡
	 * 00228:發動行申請停止入扣帳 00291:請參考備註一 00299:其他 00303:已終止代繳 00306:凍結警示戶 00307:支票專戶
	 * 00308:帳號錯誤 00309:終止戶 00310:身分證不符 00311:轉出戶 00312:拒絕往來戶 00313:無此編號 00314:編號已存在
	 * 00316:管制帳戶 00317:掛失戶 00318:異常帳戶 00319:編號非英數 00391:期限未扣款 00398:其他 00401:員工扣薪失敗
	 * 00402:扣款不足 00501:退票(支票號碼、支票面額)
	 *
	 * @param procCode 處理代碼
	 */
	public void setProcCode(String procCode) {
		this.procCode = procCode;
	}

	/**
	 * 處理說明<br>
	 * jsonformat處理說明+備註(例：不足金額) 支票：金額#RP_CHQUEAMTX(16)
	 * 
	 * @return String
	 */
	public String getProcNote() {
		return this.procNote == null ? "" : this.procNote;
	}

	/**
	 * 處理說明<br>
	 * jsonformat處理說明+備註(例：不足金額) 支票：金額#RP_CHQUEAMTX(16)
	 *
	 * @param procNote 處理說明
	 */
	public void setProcNote(String procNote) {
		this.procNote = procNote;
	}

	/**
	 * 其他說明<br>
	 * jsonformat
	 * 
	 * @return String
	 */
	public String getOtherNote() {
		return this.otherNote == null ? "" : this.otherNote;
	}

	/**
	 * 其他說明<br>
	 * jsonformat
	 *
	 * @param otherNote 其他說明
	 */
	public void setOtherNote(String otherNote) {
		this.otherNote = otherNote;
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
	 * 批號後兩碼+明細序號
	 * 
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 批號後兩碼+明細序號
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 媒體日期<br>
	 * 
	 * @return Integer
	 */
	public int getMediaDate() {
		return StaticTool.bcToRoc(this.mediaDate);
	}

	/**
	 * 媒體日期<br>
	 * 
	 *
	 * @param mediaDate 媒體日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setMediaDate(int mediaDate) throws LogicException {
		this.mediaDate = StaticTool.rocToBc(mediaDate);
	}

	/**
	 * 媒體別<br>
	 * 1:ACH新光 2:ACH他行 3:郵局 4:15日 5:非15日
	 * 
	 * @return String
	 */
	public String getMediaKind() {
		return this.mediaKind == null ? "" : this.mediaKind;
	}

	/**
	 * 媒體別<br>
	 * 1:ACH新光 2:ACH他行 3:郵局 4:15日 5:非15日
	 *
	 * @param mediaKind 媒體別
	 */
	public void setMediaKind(String mediaKind) {
		this.mediaKind = mediaKind;
	}

	/**
	 * 媒體序號<br>
	 * 
	 * @return Integer
	 */
	public int getMediaSeq() {
		return this.mediaSeq;
	}

	/**
	 * 媒體序號<br>
	 * 
	 *
	 * @param mediaSeq 媒體序號
	 */
	public void setMediaSeq(int mediaSeq) {
		this.mediaSeq = mediaSeq;
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
		return "BatxDetail [batxDetailId=" + batxDetailId + ", repayCode=" + repayCode + ", fileName=" + fileName + ", entryDate=" + entryDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", rvNo="
				+ rvNo + ", repayType=" + repayType + ", reconCode=" + reconCode + ", repayAcCode=" + repayAcCode + ", acquiredAmt=" + acquiredAmt + ", repayAmt=" + repayAmt + ", acctAmt=" + acctAmt
				+ ", disacctAmt=" + disacctAmt + ", procStsCode=" + procStsCode + ", procCode=" + procCode + ", procNote=" + procNote + ", otherNote=" + otherNote + ", titaTlrNo=" + titaTlrNo
				+ ", titaTxtNo=" + titaTxtNo + ", mediaDate=" + mediaDate + ", mediaKind=" + mediaKind + ", mediaSeq=" + mediaSeq + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
