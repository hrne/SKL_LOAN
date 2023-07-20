package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcDetailId;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * Call By 1.帳務類交易 Ex.L3100 撥款  2.傳票類交易(L6201 其他傳票輸入、L
 *
 * ========================== 交易程式帳務處理步驟 ==============================
 * 
 *  if TxCom.isBookAcYes{
 *
 *    Step1. 先借後貸，將分錄放入ArrayList<AcDetail> lAcDetail
 *
 *       Case1 撥款交易 借： 短期擔保放款 lAcDetail.add  
 *                        貸： 收付欄 call acPaymentCom
 * 
 *       Case2 還款交易 借： 收付欄 call acPaymentCom 
 *                        貸 : 短期擔保放款 lAcDetail.add
 *                             利息收入       lAcDetail.add
 *
 *	  Step2. 將會計分錄ArrayList放入TxBuffer		
 *           this.txBuffer.addAllAcDetailList(lAcDetail);
 *
 *    Step3. Call acDetailCom 產生會計分錄
 *	  	 	 acDetailCom.setTxBuffer(this.txBuffer);
 *			 acDetailCom.run(titaVo);
 * 
 *  }
 *
 * ============================ 會計分錄的參數及設定值 ===========================
 * 
 * ------------------- 必要參數 -------------------
 * 
 * 1. 借貸別 DbCr VARCHAR2(1) D-借，C-貸
 * 
 * 2. 業務科目或會科子細目 業務科目 AcctCode VARCHAR2(3) 會科子細目 AcNoCode VARCHAR2(11)+AcSubCode VARCHAR2(5)+AcDtlCode VARCHAR2(2)
 *
 * 3. 記帳金額 TxAmt DECIMAL(16,2)
 *
 * 
 * ------------------- 選擇參數 -------------------
 *
 * 4. 戶號+額度+撥款 CustNo DECIMAL(7)+FacmNo DECIMAL(3)+BormNo DECIMAL(3)
 *
 * 5. 彙總別 SumNo VARCHAR2(3)
 * 
 * 6. 摘要代號 DscptNo VARCHAR2(4)
 *
 * 7. 傳票摘要 SlipNote NVARCHAR2(80)
 * 
 * 8. 銷帳科目記號 ReceivableFlag DECIMAL(1) 
 *    0－非銷帳科目(會計科子細目設定檔帶入)
 *    1－會計銷帳科目(會計科子細目設定檔帶入)
 *    2－業務銷帳科目(會計科子細目設定檔帶入)
 *    3－未收費用、4-短繳期金、5-另收欠款(銷帳檔帶入)
 *    8－核心銷帳碼科目，須以銷帳編號(銷帳碼彙總)上傳核心(會計科子細目設定檔帶入)
 *
 * 9. 銷帳編號 RvNo VARCHAR2(30)
 *    銷帳科目記號=1時，由會計銷帳檔處理公用程式自動編號；其他由業務自行編號
 * 
 * 10. 帳冊別記號 AcBookFlag DECIMAL(1)
 *    0: 不細分(會計科子細目設定檔帶入)
 *    1: 兼全帳冊與特殊帳冊(會計科子細目設定檔帶入)
 *    2: 特殊帳冊之應收調撥款(會計科子細目設定檔帶入)
 *       ※明細檔無(只寫入總帳檔)，特殊帳冊之核心傳票媒體檔，係自動產生。
 *    3: 指定帳冊(業務交易設定)
 *       ※L6201:其他傳票輸入、L618D各項提存
 *       
 * 11.區隔帳冊  AcSubBookCode VARCHAR2(3)
 *    帳冊別記號=0,2時，不須處理
 *    帳冊別記號=1時，由帳冊別處理公用程式設定
 *    帳冊別記號=3時，由業務交易帶入
 *    ※ 帳冊別AcBookCode  VARCHAR2(3)由系統參數設定檔帶入(000：全公司)
 * -------------------會計科子細目設定值  -------------------
 * 12. 業務科目記號 AcctFlag DECIMAL(1) 
 *    0: 非業務科目 1: 資負明細科目（放款、催收款項..) 
 *    
 * ------------------- TITA值 -------------------
 * 
 * 13. 記帳單位別 BranchNo VARCHAR2(4) 預設TitaAcBrNo
 * 
 * 14. 幣別 CurrencyCode VARCHAR2(2) 預設TitaCurNm
 *
 * 15. 登錄單位別 TitaKinbr VARCHAR2(4)
 *
 * 16. 登錄經辦 TitaTlrNo VARCHAR2(6)
 *
 * 17. 登錄交易序號 TitaTxtNo DECIMAL(8)
 *
 * 18. 交易代號 TitaTxCd VARCHAR2(5)
 *
 * 19. 業務類別 TitaSecNo VARCHAR2(2)
 *
 * 20. 整批批號 TitaBatchNo VARCHAR2(6)
 *
 * 21. 整批明細序號 TitaBatchSeq VARCHAR2(6)
 * 
 * 22. 核准主管 TitaSupNo VARCHAR2(6)
 *
 * 23. 作業模式 TitaRelCd DECIMAL(1)
 *
 * ------------------- 自動設定 -------------------
 *
 * 24. 登放日期 RelDy DECIMAL(8) 自動帶入TxCom.RelDy
 *
 * 25. 登放序號 RelNo VARCHAR2(18) 自動帶入TxCom.RelNo
 *
 * 26. 分錄序號 AcSeq DECIMAL(4) 自動續編(+1)
 *
 * 27. 會計日期 AcDate DECIMAL(8) 自動帶入TxCom.Tbsdy
 *     L618D 各項提存作業 ，由交易帶入(月底提存帳務，可能於換日後執行)                               
 * 
 * 28. 傳票批號 SlipBatNo DECIMAL(2) 0,由AcEnterCom(會計入帳程式)更新
 *     L618D 各項提存作業 ，由交易帶入(提存>= 90)   
 *     
 * 29. 傳票號碼 SlipNo DECIMAL(6) 0,由AcEnterCom(會計入帳程式)更新
 *
 * 30. 入總帳記號 EntAc DECIMAL(1) 0,由AcEnterCom(會計入帳程式)更新
 * 
 * 
 * ============================  處理說明  ===========================
 * 
 * LOOP READ ArrayList
 *  1. CHECK INPUT PARAMETER
 *  1.1 主管放行 ---> PROCESS END 
 *  1.2 訂正 ---> PROCESS END
 * 
 *  2. Select CdAcCode Table BY 業務科目或會科子細目 
 *  2.1 CHECK InuseFlag 放款部使用記號 = 0: 可以使用
 *  2.2 CHECK ClassCode 科子目級別 = 0: 可入帳科目
 * 
 *  3. Set Default Value
 * 
 *  3.1 轉換業務科目 : 
 *      1.短繳期金由 ZXX 轉 3XX
 *      2.根據系統參數設定檔的專戶戶號轉換專戶業務科目
 *        TAV -> TLD 0610940戶號(TLD 放款部專戶)
 *               T10 0601776戶號(T10 前置協商收款專戶)
 *               
 *  3.2 額度編號、撥款序號檢核        
 *  3.3 核心銷帳碼科目檢查     
 * 
 *  4. 設定 jsonFields 欄 
 *  4.1 CaseCloseCode 結案區分、RenewCode展期記號
 *  4.2  StampTaxFreeAmt 免印花稅金額( 還款來源 = 4.支票兌現 && 業務科目 = 利息收入(Ixx))
 *  
 *  5. Set 分錄序號 AcDetail.AcSeq && TxCom.AcSeq
 * 
 * LOOP END
 * 
 */

/**
 * 產生會計分錄<BR>
 * run ：產生會計分錄 call by LXXXX<BR>
 * 1).將交易準備的會計分錄，結合會計科子細目設定檔內容，寫入會計分錄工作區<BR>
 * 2).含交易call收付欄出帳(AcPaymentCom)、回收費用帳務處理(BaTxCom)，產生的會計分錄<BR>
 * 
 * @author st1
 *
 */
@Component("acDetailCom")
@Scope("prototype")
public class AcDetailCom extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	private CdAcCode tCdAcCode = new CdAcCode();
	private CdAcCodeId CdAcCodeId = new CdAcCodeId();
	private AcDetailId tAcDetailId = new AcDetailId();
	private AcDetail tAcDetail = new AcDetail();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcDetailCom.... : acListsize=" + this.txBuffer.getAcDetailList().size());

		for (int i = 0; i < this.txBuffer.getAcDetailList().size(); i++) {
			this.info("AcDetailCom AcDetail = " + i + this.txBuffer.getAcDetailList().get(i));
		}
		// this.titaVo = titaVo;
		int i = 1;
		// 檢核輸入
		this.checkInout(titaVo);

		List<AcDetail> acList = new ArrayList<AcDetail>();

		/* logic process -Start */
		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			if (ac.getTxAmt().compareTo(BigDecimal.ZERO) > 0) {
				tAcDetailId = new AcDetailId();
				tAcDetail = new AcDetail();
				tAcDetail.setEntAc(0); // 入總帳記號，由AcEnterCom(會計入帳程式)更新
				/*-----------   會計科子細目設定檔    ----------- */
				findAcCode(ac, titaVo); //
				tAcDetail.setAcNoCode(tCdAcCode.getAcNoCode()); // 科子細目
				tAcDetail.setAcSubCode(tCdAcCode.getAcSubCode());
				tAcDetail.setAcDtlCode(tCdAcCode.getAcDtlCode());
				tAcDetail.setAcctFlag(tCdAcCode.getAcctFlag());
				tAcDetail.setAcctCode(tCdAcCode.getAcctCode());

				// 銷帳科目記號
				// 0－非銷帳科目 1－會計銷帳科目 2－業務銷帳科目 8-核心銷帳碼科目=> CdAcCode
				// 3-未收款, 4-短繳期金, 5-另收欠款 => 銷帳檔帶入
				if (ac.getReceivableFlag() >= 3 && ac.getReceivableFlag() <= 5) {
					tAcDetail.setReceivableFlag(ac.getReceivableFlag());
				} else {
					tAcDetail.setReceivableFlag(tCdAcCode.getReceivableFlag()); // 銷帳科目記號
				}

				// 銷帳編號
				// 銷帳科目記號=1時，由會計銷帳檔處理公用程式自動編號；其他由業務自行編號
				tAcDetail.setRvNo(ac.getRvNo()); // 銷帳編號

				/*----------- 必要參數 ----------*/
				tAcDetail.setDbCr(ac.getDbCr()); // 借貸別 D -借，C-貸

				// 記帳金額，台幣無金額小數點
				if ("TWD".equals(titaVo.getCurName()) || titaVo.getCurName().trim().isEmpty())
					tAcDetail.setTxAmt(ac.getTxAmt().setScale(0, RoundingMode.HALF_UP)); // 記帳金額
				else
					tAcDetail.setTxAmt(ac.getTxAmt());

				/*--------- 選擇參數  -------------------*/
				// 戶號+額度+撥款
				if (ac.getAcctCode().equals("TLD")) { // TLD,T10 => 專戶
					tAcDetail.setCustNo(this.txBuffer.getSystemParas().getLoanDeptCustNo());
				} else if (ac.getAcctCode().equals("T10")) {
					tAcDetail.setCustNo(this.txBuffer.getSystemParas().getNegDeptCustNo());
				} else {
					tAcDetail.setCustNo(ac.getCustNo());
					tAcDetail.setFacmNo(ac.getFacmNo());
					tAcDetail.setBormNo(ac.getBormNo());
				}
				tAcDetail.setSlipSumNo(ac.getSlipSumNo()); // 彙總傳票批號
				tAcDetail.setSumNo(ac.getSumNo());// 彙總別
				tAcDetail.setDscptCode(ac.getDscptCode()); // 摘要代號
				tAcDetail.setSlipNote(ac.getSlipNote()); // 傳票摘要
				tAcDetail.setTitaHCode(ac.getTitaHCode()); // 訂正別

				/*----------- TITA值 -----------*/
				tAcDetail.setBranchNo(titaVo.getAcbrNo()); // 記帳單位別
				if (titaVo.getCurName().trim().isEmpty())
					tAcDetail.setCurrencyCode("TWD");
				else
					tAcDetail.setCurrencyCode(titaVo.getCurName()); // 幣別 */
				tAcDetail.setBranchNo(titaVo.getAcbrNo()); // 記帳單位別
				tAcDetail.setTitaKinbr(titaVo.getKinbr()); // 登錄單位別
				tAcDetail.setTitaTlrNo(titaVo.getTlrNo()); // 登錄經辦
				tAcDetail.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo())); // 登錄交易序號
				tAcDetail.setTitaTxCd(titaVo.getTxcd()); // 交易代號
				tAcDetail.setTitaSecNo(titaVo.getSecNo()); // 業務類別
				tAcDetail.setTitaBatchNo(titaVo.getBacthNo()); // 整批批號
				tAcDetail.setTitaBatchSeq(titaVo.getBatchSeq()); // 整批明細序號
				tAcDetail.setTitaSupNo(titaVo.getEmpNos()); // 核准主管
				tAcDetail.setTitaRelCd(parse.stringToInteger(titaVo.getRelCode())); // 作業模式
				/*------------jsonFields -----------*/
				settingjsonfields(ac, titaVo);

				/*----------- 自動設定 -----------*/
				tAcDetailId.setRelDy(this.txBuffer.getTxCom().getReldy()); // 登放日期
				tAcDetailId.setRelTxseq(this.txBuffer.getTxCom().getRelNo()); // 登放序號

				// 分錄序號，由AcEnterComt重編
				if (ac.getAcSeq() > 0)
					tAcDetailId.setAcSeq(ac.getAcSeq());
				else
					tAcDetailId.setAcSeq(i++);
				// AcDetailId
				tAcDetail.setAcDetailId(tAcDetailId);
				// 會計日期
				if (ac.getAcDate() == 0) {
					tAcDetail.setAcDate(titaVo.getEntDyI());
				} else {
					tAcDetail.setAcDate(ac.getAcDate());
				}
				// 帳冊別AcBookCode VARCHAR2(3)由系統參數設定檔帶入(000：全公司)
				tAcDetail.setAcBookCode(this.txBuffer.getSystemParas().getAcBookCode());
				// 帳冊別記號
				// 0:不細分，區隔帳冊固定為00A:傳統帳冊
				// 1: 細分，區隔帳冊By戶號設定(AcBookCom)或由業務交易指定
				// 2: 中介，應收調撥款科目，明細檔無(只寫入總帳檔)，應收調撥款之核心傳票媒體檔，係自動產生。
				// 3: 指定，區隔帳冊由業務交易指定(L6201:其他傳票輸入、L618D:各項提存、L3230暫收款退還(作業項目： 16.3200億專案)
				if (tCdAcCode.getAcBookFlag() == 1 && ac.getAcBookFlag() > 0) {
					tAcDetail.setAcBookFlag(ac.getAcBookFlag());
				} else {
					tAcDetail.setAcBookFlag(tCdAcCode.getAcBookFlag());
				}
				if (tAcDetail.getAcBookFlag() == 0) {
					tAcDetail.setAcSubBookCode(this.txBuffer.getSystemParas().getAcSubBookCode());
				} else {
					tAcDetail.setAcSubBookCode(ac.getAcSubBookCode());
				}
				// 傳票批號、傳票號碼 ，若未指定則由AcEnterCom(會計入帳程式)更新
				tAcDetail.setSlipBatNo(ac.getSlipBatNo());
				tAcDetail.setSlipNo(ac.getSlipNo());

				/*----------- 放入ArrayList -----------*/
				acList.add(tAcDetail);
				this.info("AcDetailCom  AcDetail : " + tAcDetail);
			}
		}
		/* logic process -End */

		/* 將處裡完的acList 放回txBuffer */
		this.txBuffer.setAcDetailList(acList);

		this.info("AcDetailCom end : " + this.txBuffer.getAcDetailList().toString());

		return null;

	}

	/* 檢核輸入 */
	private void checkInout(TitaVo titaVo) throws LogicException {
		// empty
		if (this.txBuffer == null) {
			throw new LogicException("E6001", "AcDetailCom 分錄暫存區為空值!! "); // E6001 分錄檢核有誤
		}
		// 業務類別 01:撥款匯款 02:支票繳款 03:債協 09:放款
		String secnos[] = { "01", "02", "03", "09" };
		List<String> lsecnos = Arrays.asList(secnos);
		if (!lsecnos.contains(titaVo.getSecNo())) {
			throw new LogicException("E6001", "AcDetailCom 業務類別有誤,  SecNo =" + titaVo.getSecNo());
		}
		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			// 借貸別
			String dbcrs[] = { "C", "D" };
			List<String> ldbcrs = Arrays.asList(dbcrs);
			if (!ldbcrs.contains(ac.getDbCr())) {
				throw new LogicException("E6001", "AcDetailCom 借貸別有誤,  DbCr =" + ac.getDbCr() + ac);
			}
			// 金額
			if (ac.getTxAmt().compareTo(BigDecimal.ZERO) < 0) {
				throw new LogicException("E6001", "AcDetailCom 金額小於零, amount =" + ac.getTxAmt() + ac);
			}
			// 支票繳款業務別
			if ("L3210".equals(titaVo.getTxcd()) && "RCK".equals(ac.getAcctCode()) && !"02".equals(titaVo.getSecNo())) {
				throw new LogicException("E6001", "AcDetailCom 支票業務別需為02," + titaVo.getSecNo());
			}
			// 撥款匯款業務別 2xx ,201:整批匯款 202:單筆匯款 204:退款台新(存款憑條) 205:退款他行(整批匯款) 211:退款新光(存款憑條)
			if (ac.getSumNo() != null && ac.getSumNo().length() == 3 && ac.getSumNo().startsWith("2")
					&& !"01".equals(titaVo.getSecNo())) {
				throw new LogicException("E6001", "AcDetailCom 撥款匯款業務別需為01," + titaVo.getSecNo());
			}
		}
	}

	/* 設定 jsonFields 欄 */
	private void settingjsonfields(AcDetail ac, TitaVo titaVo) throws LogicException {
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		if (ac.getJsonFields() != null) {
			tTempVo = tTempVo.getVo(ac.getJsonFields());
		}
		// 1. CaseCloseCode 結案區分、RenewCode展期記號
//	    交易代號 = L3410 結案登錄-可欠繳 
//			    L3420 結案登錄-不可欠繳		
		if (titaVo.getTxcd().equals("L3410") || titaVo.getTxcd().equals("L3420")) {
			tTempVo.putParam("CaseCloseCode", titaVo.getParam("CaseCloseCode"));
		}

		// 2. StampTaxFreeAmt 免印花稅金額
// 還款來源 = 4.支票兌現 && 業務科目 = 利息收入(Ixx)	
		if (titaVo.get("RpCode1") != null && parse.stringToInteger(titaVo.getParam("RpCode1")) == 4
				&& "I".equals(tAcDetail.getAcctCode().substring(0, 1))) {
			tTempVo.putParam("StampTaxFreeAmt", tAcDetail.getTxAmt());
		}
		// 3.銷帳業務科目，銷帳科目與入帳科目不同時寫入(ex.Z10->310)
		if (!ac.getAcctCode().equals(tCdAcCode.getAcctCode())) {
			tTempVo.putParam("RvAcctCode", ac.getAcctCode());
		}
		// 4.收付欄對帳費別 ex.A1~A6
		if (titaVo.get("RpAcctCode1") != null ) {
			tTempVo.putParam("ReconCode", titaVo.get("RpAcctCode1").trim());			
		}

		tAcDetail.setJsonFields(tTempVo.getJsonString());
	}

	/* 會計科子細目設定檔 */
	private void findAcCode(AcDetail ac, TitaVo titaVo) throws LogicException {
		// step 1. 轉換業務科目

		String acctCode = ac.getAcctCode();

// 1. 轉換短繳期金
// 銷帳科目記號ReceivableFlag = 4-短繳期金 		               	         
// Z10	短繳期金-短期擔保放款	  310	短期擔保放款
// Z20	短繳期金-中期擔保放款	  320	中期擔保放款
// Z30	短繳期金-長期擔保放款	  330	長期擔保放款
// Z40	短繳期金-三十年房貸	  340	三十年房貸
		if (ac.getReceivableFlag() == 4) {
			if (ac.getRvNo().length() < 3) {
				throw new LogicException("E6001", "AcDetailCom 短繳期金需有撥款序號"); // E6001 分錄檢核有誤
			}
			ac.setBormNo(parse.stringToInteger(ac.getRvNo().substring(0, 3)));
			if (acctCode.substring(0, 1).equals("Z")) {
				acctCode = "3" + acctCode.substring(1, 3);
			}
		}

// 2. 轉換費用攤提
		// 企金費用攤提，未到期收入轉入TSL暫收款－費用攤提
		// F12企金帳管費收入、 F27聯貸管理費收入
		// 聯貸手續費:SL-費用代號(2)-流水號(3)-攤提年月(YYYMM)
		if (acctCode.equals("F12") || acctCode.equals("F27")) {
			if (ac.getReceivableFlag() == 3 && ac.getRvNo().length() >= 15
					&& parse.isNumeric(ac.getRvNo().substring(10, 15))) {
				if (parse.stringToInteger(ac.getRvNo().substring(10, 15)) > titaVo.getEntDyI() / 100) {
					acctCode = "TSL";
					ac.setReceivableFlag(0);
				}
			}
		}

		// step 2. 暫收款額度編號檢核
		if (acctCode.equals("TAV")) {
			if (ac.getCustNo() == this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
				if (ac.getFacmNo() > 0) {
					throw new LogicException("E6001", "AcDetailCom 放款部專戶額度編號須為零"); // E6001 分錄檢核有誤
				}
			} else if (ac.getCustNo() == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
				if (ac.getFacmNo() > 0) {
					throw new LogicException("E6001", "AcDetailCom 前置協商收款專戶額度編號須為零"); // E6001 分錄檢核有誤
				}
			} else {
				if (ac.getFacmNo() == 0) {
					throw new LogicException("E6001", "AcDetailCom 暫收可抵繳科目需有額度編號"); // E6001 分錄檢核有誤
				}
			}
		}
		// step 3.Find CdAcCode
		if (acctCode != null && acctCode.trim().length() > 0) {
			tCdAcCode = cdAcCodeService.acCodeAcctFirst(acctCode, titaVo);
		} else {
			CdAcCodeId.setAcNoCode(ac.getAcNoCode());
			CdAcCodeId.setAcSubCode(ac.getAcSubCode());
			CdAcCodeId.setAcDtlCode(ac.getAcDtlCode());
			tCdAcCode = cdAcCodeService.findById(CdAcCodeId, titaVo);
		}
		if (tCdAcCode == null) {
			throw new LogicException("E6001", "AcDetailCom 科目有誤" + acctCode + ' ' + CdAcCodeId + ac); // E6001 分錄檢核有誤
		}
		if (tCdAcCode.getInuseFlag() > 0) {
			throw new LogicException("E6001", "AcDetailCom 科目不可使用" + acctCode + ' ' + CdAcCodeId + ac);
		}
		if (tCdAcCode.getClassCode() > 0) {
			throw new LogicException("E6001", "AcDetailCom 科子目級別錯誤" + acctCode + ' ' + CdAcCodeId + ac);
		}
		// step 4. 資負明細科目（放款、催收款項..)需有撥款序號
		if (tCdAcCode.getAcctFlag() == 1 && ac.getBormNo() == 0) {
			throw new LogicException("E6001", "AcDetailCom 資負明細科目需有撥款編號"); // E6001 分錄檢核有誤誤
		}
		// step 5. 核心銷帳碼科目檢查
		if (tCdAcCode.getReceivableFlag() == 8 && ac.getRvNo() == null) {
			throw new LogicException("E6001", "AcDetailCom 核心銷帳碼科目需有銷帳編號"); // E6001 分錄檢核有誤
		}
	}
}
