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
 * Ifrs9FacData IFRS9額度資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ifrs9FacData`")
public class Ifrs9FacData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5039538859959855794L;

	@EmbeddedId
	private Ifrs9FacDataId ifrs9FacDataId;

	// 資料年月
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 核准號碼
	@Column(name = "`ApplNo`")
	private int applNo = 0;

	// 借款人ID / 統編
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 已核撥記號
	/* 0: 未核撥 1: 已核撥 */
	@Column(name = "`DrawdownFg`")
	private int drawdownFg = 0;

	// 核准日期(額度)
	/* 1.優先取用對保日期2.無對保日採用准駁日 */
	@Column(name = "`ApproveDate`")
	private int approveDate = 0;

	// 動支期限
	@Column(name = "`UtilDeadline`")
	private int utilDeadline = 0;

	// 初貸日期
	/* 額度初貸日 */
	@Column(name = "`FirstDrawdownDate`")
	private int firstDrawdownDate = 0;

	// 到期日(額度)
	@Column(name = "`MaturityDate`")
	private int maturityDate = 0;

	// 核准金額
	/* 額度核准金額 */
	@Column(name = "`LineAmt`")
	private BigDecimal lineAmt = new BigDecimal("0");

	// 帳管費
	@Column(name = "`AcctFee`")
	private BigDecimal acctFee = new BigDecimal("0");

	// 法務費
	/* 額度匯總 */
	@Column(name = "`LawFee`")
	private BigDecimal lawFee = new BigDecimal("0");

	// 火險費
	/* 額度匯總 */
	@Column(name = "`FireFee`")
	private BigDecimal fireFee = new BigDecimal("0");

	// 初貸時約定還本寬限期
	/* 約定客戶得只繳息不繳本之寬限期。以月為單位，例如3年寬限期，則本欄位值為36 */
	@Column(name = "`GracePeriod`")
	private int gracePeriod = 0;

	// 契約當時還款方式(月底日)
	/* 1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法5.按月撥款收息(逆向貸款) */
	@Column(name = "`AmortizedCode`", length = 1)
	private String amortizedCode;

	// 契約當時利率調整方式(月底日)
	/* 1: 機動 2: 固定3: 定期機動 */
	@Column(name = "`RateCode`", length = 1)
	private String rateCode;

	// 契約約定當時還本週期(月底日)
	/* 若為到期還本，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。 */
	@Column(name = "`RepayFreq`")
	private int repayFreq = 0;

	// 契約約定當時繳息週期(月底日)
	/* 若為到期繳息，則填入0；若按月還本，則填入1；季繳，3；半年，6；年繳,12。 */
	@Column(name = "`PayIntFreq`")
	private int payIntFreq = 0;

	// IFRS階梯商品別
	/* 空白=非階梯式;A=固定階梯;B=浮動階梯; */
	@Column(name = "`Ifrs9StepProdCode`", length = 1)
	private String ifrs9StepProdCode;

	// 授信行業別
	@Column(name = "`IndustryCode`", length = 6)
	private String industryCode;

	// 擔保品類別
	/* 以對應至JCIC的類別 */
	@Column(name = "`ClTypeJCIC`", length = 2)
	private String clTypeJCIC;

	// 擔保品地區別
	@Column(name = "`CityCode`", length = 2)
	private String cityCode;

	// 擔保品鄉鎮區
	@Column(name = "`AreaCode`", length = 3)
	private String areaCode;

	// 擔保品郵遞區號
	@Column(name = "`Zip3`", length = 3)
	private String zip3;

	// 商品利率代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

	// 是否為協議商品
	/* Y:是 N:否 */
	@Column(name = "`AgreementFg`", length = 1)
	private String agreementFg;

	// 企金別
	/* 共用代碼檔0:個金1:企金2:企金自然人 */
	@Column(name = "`EntCode`", length = 1)
	private String entCode;

	// 資產五分類代號
	@Column(name = "`AssetClass`")
	private int assetClass = 0;

	// 產品別
	/* 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc */
	@Column(name = "`Ifrs9ProdCode`", length = 2)
	private String ifrs9ProdCode;

	// 原始鑑價金額
	@Column(name = "`EvaAmt`")
	private BigDecimal evaAmt = new BigDecimal("0");

	// 累計撥款金額(額度層)
	@Column(name = "`UtilAmt`")
	private BigDecimal utilAmt = new BigDecimal("0");

	// 已動用餘額(額度層)
	/* 已動用額度餘額(循環動用還款時會減少,非循環動用還款時不會減少) */
	@Column(name = "`UtilBal`")
	private BigDecimal utilBal = new BigDecimal("0");

	// 本金餘額(額度層)合計
	/* 本額度項下所有撥款序號本金餘額合計 */
	@Column(name = "`TotalLoanBal`")
	private BigDecimal totalLoanBal = new BigDecimal("0");

	// 該筆額度是否可循環動用
	/* 0: 非循環動用 1: 循環動用 */
	@Column(name = "`RecycleCode`")
	private int recycleCode = 0;

	// 該筆額度是否為不可撤銷
	/* 0: 可撤銷 1: 不可撤銷 */
	@Column(name = "`IrrevocableFlag`")
	private int irrevocableFlag = 0;

	// 暫收款金額(台幣)
	@Column(name = "`TempAmt`")
	private BigDecimal tempAmt = new BigDecimal("0");

	// 帳冊別
	/* 000:全公司 (舊:null 一般) */
	@Column(name = "`AcBookCode`", length = 3)
	private String acBookCode;

	// 區隔帳冊
	/* 00A:傳統帳冊 201:利變年金帳冊 */
	@Column(name = "`AcSubBookCode`", length = 3)
	private String acSubBookCode;

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

	public Ifrs9FacDataId getIfrs9FacDataId() {
		return this.ifrs9FacDataId;
	}

	public void setIfrs9FacDataId(Ifrs9FacDataId ifrs9FacDataId) {
		this.ifrs9FacDataId = ifrs9FacDataId;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param dataYM 資料年月
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
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
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 * @return Integer
	 */
	public int getApplNo() {
		return this.applNo;
	}

	/**
	 * 核准號碼<br>
	 * 
	 *
	 * @param applNo 核准號碼
	 */
	public void setApplNo(int applNo) {
		this.applNo = applNo;
	}

	/**
	 * 借款人ID / 統編<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 借款人ID / 統編<br>
	 * 
	 *
	 * @param custId 借款人ID / 統編
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 已核撥記號<br>
	 * 0: 未核撥 1: 已核撥
	 * 
	 * @return Integer
	 */
	public int getDrawdownFg() {
		return this.drawdownFg;
	}

	/**
	 * 已核撥記號<br>
	 * 0: 未核撥 1: 已核撥
	 *
	 * @param drawdownFg 已核撥記號
	 */
	public void setDrawdownFg(int drawdownFg) {
		this.drawdownFg = drawdownFg;
	}

	/**
	 * 核准日期(額度)<br>
	 * 1.優先取用對保日期 2.無對保日採用准駁日
	 * 
	 * @return Integer
	 */
	public int getApproveDate() {
		return StaticTool.bcToRoc(this.approveDate);
	}

	/**
	 * 核准日期(額度)<br>
	 * 1.優先取用對保日期 2.無對保日採用准駁日
	 *
	 * @param approveDate 核准日期(額度)
	 * @throws LogicException when Date Is Warn
	 */
	public void setApproveDate(int approveDate) throws LogicException {
		this.approveDate = StaticTool.rocToBc(approveDate);
	}

	/**
	 * 動支期限<br>
	 * 
	 * @return Integer
	 */
	public int getUtilDeadline() {
		return StaticTool.bcToRoc(this.utilDeadline);
	}

	/**
	 * 動支期限<br>
	 * 
	 *
	 * @param utilDeadline 動支期限
	 * @throws LogicException when Date Is Warn
	 */
	public void setUtilDeadline(int utilDeadline) throws LogicException {
		this.utilDeadline = StaticTool.rocToBc(utilDeadline);
	}

	/**
	 * 初貸日期<br>
	 * 額度初貸日
	 * 
	 * @return Integer
	 */
	public int getFirstDrawdownDate() {
		return StaticTool.bcToRoc(this.firstDrawdownDate);
	}

	/**
	 * 初貸日期<br>
	 * 額度初貸日
	 *
	 * @param firstDrawdownDate 初貸日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
		this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
	}

	/**
	 * 到期日(額度)<br>
	 * 
	 * @return Integer
	 */
	public int getMaturityDate() {
		return StaticTool.bcToRoc(this.maturityDate);
	}

	/**
	 * 到期日(額度)<br>
	 * 
	 *
	 * @param maturityDate 到期日(額度)
	 * @throws LogicException when Date Is Warn
	 */
	public void setMaturityDate(int maturityDate) throws LogicException {
		this.maturityDate = StaticTool.rocToBc(maturityDate);
	}

	/**
	 * 核准金額<br>
	 * 額度核准金額
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLineAmt() {
		return this.lineAmt;
	}

	/**
	 * 核准金額<br>
	 * 額度核准金額
	 *
	 * @param lineAmt 核准金額
	 */
	public void setLineAmt(BigDecimal lineAmt) {
		this.lineAmt = lineAmt;
	}

	/**
	 * 帳管費<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAcctFee() {
		return this.acctFee;
	}

	/**
	 * 帳管費<br>
	 * 
	 *
	 * @param acctFee 帳管費
	 */
	public void setAcctFee(BigDecimal acctFee) {
		this.acctFee = acctFee;
	}

	/**
	 * 法務費<br>
	 * 額度匯總
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLawFee() {
		return this.lawFee;
	}

	/**
	 * 法務費<br>
	 * 額度匯總
	 *
	 * @param lawFee 法務費
	 */
	public void setLawFee(BigDecimal lawFee) {
		this.lawFee = lawFee;
	}

	/**
	 * 火險費<br>
	 * 額度匯總
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFireFee() {
		return this.fireFee;
	}

	/**
	 * 火險費<br>
	 * 額度匯總
	 *
	 * @param fireFee 火險費
	 */
	public void setFireFee(BigDecimal fireFee) {
		this.fireFee = fireFee;
	}

	/**
	 * 初貸時約定還本寬限期<br>
	 * 約定客戶得只繳息不繳本之寬限期。 以月為單位，例如3年寬限期，則本欄位值為36
	 * 
	 * @return Integer
	 */
	public int getGracePeriod() {
		return this.gracePeriod;
	}

	/**
	 * 初貸時約定還本寬限期<br>
	 * 約定客戶得只繳息不繳本之寬限期。 以月為單位，例如3年寬限期，則本欄位值為36
	 *
	 * @param gracePeriod 初貸時約定還本寬限期
	 */
	public void setGracePeriod(int gracePeriod) {
		this.gracePeriod = gracePeriod;
	}

	/**
	 * 契約當時還款方式(月底日)<br>
	 * 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本) 3.本息平均法(期金) 4.本金平均法 5.按月撥款收息(逆向貸款)
	 * 
	 * @return String
	 */
	public String getAmortizedCode() {
		return this.amortizedCode == null ? "" : this.amortizedCode;
	}

	/**
	 * 契約當時還款方式(月底日)<br>
	 * 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本) 3.本息平均法(期金) 4.本金平均法 5.按月撥款收息(逆向貸款)
	 *
	 * @param amortizedCode 契約當時還款方式(月底日)
	 */
	public void setAmortizedCode(String amortizedCode) {
		this.amortizedCode = amortizedCode;
	}

	/**
	 * 契約當時利率調整方式(月底日)<br>
	 * 1: 機動 2: 固定 3: 定期機動
	 * 
	 * @return String
	 */
	public String getRateCode() {
		return this.rateCode == null ? "" : this.rateCode;
	}

	/**
	 * 契約當時利率調整方式(月底日)<br>
	 * 1: 機動 2: 固定 3: 定期機動
	 *
	 * @param rateCode 契約當時利率調整方式(月底日)
	 */
	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	/**
	 * 契約約定當時還本週期(月底日)<br>
	 * 若為到期還本，則填入0； 若按月還本，則填入1； 季繳，3； 半年，6； 年繳,12。
	 * 
	 * @return Integer
	 */
	public int getRepayFreq() {
		return this.repayFreq;
	}

	/**
	 * 契約約定當時還本週期(月底日)<br>
	 * 若為到期還本，則填入0； 若按月還本，則填入1； 季繳，3； 半年，6； 年繳,12。
	 *
	 * @param repayFreq 契約約定當時還本週期(月底日)
	 */
	public void setRepayFreq(int repayFreq) {
		this.repayFreq = repayFreq;
	}

	/**
	 * 契約約定當時繳息週期(月底日)<br>
	 * 若為到期繳息，則填入0； 若按月還本，則填入1； 季繳，3； 半年，6； 年繳,12。
	 * 
	 * @return Integer
	 */
	public int getPayIntFreq() {
		return this.payIntFreq;
	}

	/**
	 * 契約約定當時繳息週期(月底日)<br>
	 * 若為到期繳息，則填入0； 若按月還本，則填入1； 季繳，3； 半年，6； 年繳,12。
	 *
	 * @param payIntFreq 契約約定當時繳息週期(月底日)
	 */
	public void setPayIntFreq(int payIntFreq) {
		this.payIntFreq = payIntFreq;
	}

	/**
	 * IFRS階梯商品別<br>
	 * 空白=非階梯式; A=固定階梯; B=浮動階梯;
	 * 
	 * @return String
	 */
	public String getIfrs9StepProdCode() {
		return this.ifrs9StepProdCode == null ? "" : this.ifrs9StepProdCode;
	}

	/**
	 * IFRS階梯商品別<br>
	 * 空白=非階梯式; A=固定階梯; B=浮動階梯;
	 *
	 * @param ifrs9StepProdCode IFRS階梯商品別
	 */
	public void setIfrs9StepProdCode(String ifrs9StepProdCode) {
		this.ifrs9StepProdCode = ifrs9StepProdCode;
	}

	/**
	 * 授信行業別<br>
	 * 
	 * @return String
	 */
	public String getIndustryCode() {
		return this.industryCode == null ? "" : this.industryCode;
	}

	/**
	 * 授信行業別<br>
	 * 
	 *
	 * @param industryCode 授信行業別
	 */
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	/**
	 * 擔保品類別<br>
	 * 以對應至JCIC的類別
	 * 
	 * @return String
	 */
	public String getClTypeJCIC() {
		return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
	}

	/**
	 * 擔保品類別<br>
	 * 以對應至JCIC的類別
	 *
	 * @param clTypeJCIC 擔保品類別
	 */
	public void setClTypeJCIC(String clTypeJCIC) {
		this.clTypeJCIC = clTypeJCIC;
	}

	/**
	 * 擔保品地區別<br>
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 擔保品地區別<br>
	 * 
	 *
	 * @param cityCode 擔保品地區別
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 擔保品鄉鎮區<br>
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 擔保品鄉鎮區<br>
	 * 
	 *
	 * @param areaCode 擔保品鄉鎮區
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * 擔保品郵遞區號<br>
	 * 
	 * @return String
	 */
	public String getZip3() {
		return this.zip3 == null ? "" : this.zip3;
	}

	/**
	 * 擔保品郵遞區號<br>
	 * 
	 *
	 * @param zip3 擔保品郵遞區號
	 */
	public void setZip3(String zip3) {
		this.zip3 = zip3;
	}

	/**
	 * 商品利率代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 商品利率代碼<br>
	 * 
	 *
	 * @param prodNo 商品利率代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}

	/**
	 * 是否為協議商品<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getAgreementFg() {
		return this.agreementFg == null ? "" : this.agreementFg;
	}

	/**
	 * 是否為協議商品<br>
	 * Y:是 N:否
	 *
	 * @param agreementFg 是否為協議商品
	 */
	public void setAgreementFg(String agreementFg) {
		this.agreementFg = agreementFg;
	}

	/**
	 * 企金別<br>
	 * 共用代碼檔 0:個金 1:企金 2:企金自然人
	 * 
	 * @return String
	 */
	public String getEntCode() {
		return this.entCode == null ? "" : this.entCode;
	}

	/**
	 * 企金別<br>
	 * 共用代碼檔 0:個金 1:企金 2:企金自然人
	 *
	 * @param entCode 企金別
	 */
	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	/**
	 * 資產五分類代號<br>
	 * 
	 * @return Integer
	 */
	public int getAssetClass() {
		return this.assetClass;
	}

	/**
	 * 資產五分類代號<br>
	 * 
	 *
	 * @param assetClass 資產五分類代號
	 */
	public void setAssetClass(int assetClass) {
		this.assetClass = assetClass;
	}

	/**
	 * 產品別<br>
	 * 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
	 * 
	 * @return String
	 */
	public String getIfrs9ProdCode() {
		return this.ifrs9ProdCode == null ? "" : this.ifrs9ProdCode;
	}

	/**
	 * 產品別<br>
	 * 作為群組分類。Ex:1=員工；2=車貸；3＝房貸；4＝政府優惠貸款…etc
	 *
	 * @param ifrs9ProdCode 產品別
	 */
	public void setIfrs9ProdCode(String ifrs9ProdCode) {
		this.ifrs9ProdCode = ifrs9ProdCode;
	}

	/**
	 * 原始鑑價金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getEvaAmt() {
		return this.evaAmt;
	}

	/**
	 * 原始鑑價金額<br>
	 * 
	 *
	 * @param evaAmt 原始鑑價金額
	 */
	public void setEvaAmt(BigDecimal evaAmt) {
		this.evaAmt = evaAmt;
	}

	/**
	 * 累計撥款金額(額度層)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getUtilAmt() {
		return this.utilAmt;
	}

	/**
	 * 累計撥款金額(額度層)<br>
	 * 
	 *
	 * @param utilAmt 累計撥款金額(額度層)
	 */
	public void setUtilAmt(BigDecimal utilAmt) {
		this.utilAmt = utilAmt;
	}

	/**
	 * 已動用餘額(額度層)<br>
	 * 已動用額度餘額(循環動用還款時會減少,非循環動用還款時不會減少)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getUtilBal() {
		return this.utilBal;
	}

	/**
	 * 已動用餘額(額度層)<br>
	 * 已動用額度餘額(循環動用還款時會減少,非循環動用還款時不會減少)
	 *
	 * @param utilBal 已動用餘額(額度層)
	 */
	public void setUtilBal(BigDecimal utilBal) {
		this.utilBal = utilBal;
	}

	/**
	 * 本金餘額(額度層)合計<br>
	 * 本額度項下所有撥款序號本金餘額合計
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalLoanBal() {
		return this.totalLoanBal;
	}

	/**
	 * 本金餘額(額度層)合計<br>
	 * 本額度項下所有撥款序號本金餘額合計
	 *
	 * @param totalLoanBal 本金餘額(額度層)合計
	 */
	public void setTotalLoanBal(BigDecimal totalLoanBal) {
		this.totalLoanBal = totalLoanBal;
	}

	/**
	 * 該筆額度是否可循環動用<br>
	 * 0: 非循環動用 1: 循環動用
	 * 
	 * @return Integer
	 */
	public int getRecycleCode() {
		return this.recycleCode;
	}

	/**
	 * 該筆額度是否可循環動用<br>
	 * 0: 非循環動用 1: 循環動用
	 *
	 * @param recycleCode 該筆額度是否可循環動用
	 */
	public void setRecycleCode(int recycleCode) {
		this.recycleCode = recycleCode;
	}

	/**
	 * 該筆額度是否為不可撤銷<br>
	 * 0: 可撤銷 1: 不可撤銷
	 * 
	 * @return Integer
	 */
	public int getIrrevocableFlag() {
		return this.irrevocableFlag;
	}

	/**
	 * 該筆額度是否為不可撤銷<br>
	 * 0: 可撤銷 1: 不可撤銷
	 *
	 * @param irrevocableFlag 該筆額度是否為不可撤銷
	 */
	public void setIrrevocableFlag(int irrevocableFlag) {
		this.irrevocableFlag = irrevocableFlag;
	}

	/**
	 * 暫收款金額(台幣)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTempAmt() {
		return this.tempAmt;
	}

	/**
	 * 暫收款金額(台幣)<br>
	 * 
	 *
	 * @param tempAmt 暫收款金額(台幣)
	 */
	public void setTempAmt(BigDecimal tempAmt) {
		this.tempAmt = tempAmt;
	}

	/**
	 * 帳冊別<br>
	 * 000:全公司 (舊:null 一般)
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * 000:全公司 (舊:null 一般)
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 00A:傳統帳冊 201:利變年金帳冊
	 * 
	 * @return String
	 */
	public String getAcSubBookCode() {
		return this.acSubBookCode == null ? "" : this.acSubBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 00A:傳統帳冊 201:利變年金帳冊
	 *
	 * @param acSubBookCode 區隔帳冊
	 */
	public void setAcSubBookCode(String acSubBookCode) {
		this.acSubBookCode = acSubBookCode;
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
		return "Ifrs9FacData [ifrs9FacDataId=" + ifrs9FacDataId + ", applNo=" + applNo + ", custId=" + custId + ", drawdownFg=" + drawdownFg + ", approveDate=" + approveDate + ", utilDeadline="
				+ utilDeadline + ", firstDrawdownDate=" + firstDrawdownDate + ", maturityDate=" + maturityDate + ", lineAmt=" + lineAmt + ", acctFee=" + acctFee + ", lawFee=" + lawFee + ", fireFee="
				+ fireFee + ", gracePeriod=" + gracePeriod + ", amortizedCode=" + amortizedCode + ", rateCode=" + rateCode + ", repayFreq=" + repayFreq + ", payIntFreq=" + payIntFreq
				+ ", ifrs9StepProdCode=" + ifrs9StepProdCode + ", industryCode=" + industryCode + ", clTypeJCIC=" + clTypeJCIC + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", zip3=" + zip3
				+ ", prodNo=" + prodNo + ", agreementFg=" + agreementFg + ", entCode=" + entCode + ", assetClass=" + assetClass + ", ifrs9ProdCode=" + ifrs9ProdCode + ", evaAmt=" + evaAmt
				+ ", utilAmt=" + utilAmt + ", utilBal=" + utilBal + ", totalLoanBal=" + totalLoanBal + ", recycleCode=" + recycleCode + ", irrevocableFlag=" + irrevocableFlag + ", tempAmt=" + tempAmt
				+ ", acBookCode=" + acBookCode + ", acSubBookCode=" + acSubBookCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
