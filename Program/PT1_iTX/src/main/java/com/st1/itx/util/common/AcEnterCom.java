package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

//*  ----------------------- AcEnterCom 會計入帳處理 ------------------ 
//*      
//*   一、執行流程 
//* 
//*    A.一段式交易
//*      1.登錄
//*        step 1: ApCtl CALL TxPgm      
//*                     1).產生交易會計分錄         
//*                     2).維護撥款匯款檔(退款)
//*        step 2: ApCtl CALL AcEnter(0.正常) 
//*                     1).新增正常分錄(EntAc=1.已入帳)
//*                     2).產生分錄清單
//*      2.帳務訂正(同會計日、同批號、訂正經辦登錄交易)
//*        step 1: ApCtl CALL TxPgm      
//*                     1).維護撥款匯款檔(退款)
//*        step 2: ApCtl CALL AcEnter(1.當日訂正  2.隔日訂正) 
//*                     1).更新正常分錄(同會計日、同批號、訂正經辦登錄交易=>EntAc=0.未入帳，else 1.已入帳) 
//*                     2).產生反向分錄(同會計日、同批號、訂正經辦登錄交易=>EntAc=0.未入帳，else 1.已入帳) 
//*                     3).產生分錄清單(反向)
//*   B.二段式交易(經辦)
//*      1.經辦登錄
//*        step 1: ApCtl CALL TxPgm      
//*                     1).產生交易會計分錄(EntAc=0.未入帳)         
//*                     2).維護撥款匯款檔(撥款)
//*        step 2: ApCtl CALL AcEnter(0.正常) 
//*                     1).新增正常分錄(EntAc=0.未入帳)
//*                     2).產生分錄清單
//*      2.經辦修正(限同會計日、同分錄科目金額=>交易控制)
//*        step 1: ApCtl CALL AcEnter(1.當日訂正) 
//*                     1).更新正常分錄(EntAc=4 修正暫留) 
//*                     2).不產生分錄清單(反向) 
//*        step 2: ApCtl CALL TxPgm      
//*                     1).維護撥款匯款檔(撥款)
//*        step 3: ApCtl CALL AcEnter(0.正常) 
//*                     1).刪除正常分錄(EntAc=4 修正暫留)  
//*                     2).新增正常分錄(EntAc=0.未入帳)
//*                     3).產生分錄清單
//*      3.經辦訂正
//*        step 1: ApCtl CALL TxPgm      
//*                     1).維護撥款匯款檔(撥款)
//*        step 2: ApCtl CALL AcEnter(0.正常) 
//*                     1).更新正常分錄(同會計日、同批號、訂正經辦登錄交易=>EntAc=0.未入帳，else 1.已入帳) 
//*                     2).產生反向分錄(同會計日、同批號、訂正經辦登錄交易=>EntAc=0.未入帳，else 1.已入帳) 
//*                     3).產生分錄清單(反向)
//*   C.二段式交易(主管)
//*    1.主管放行 
//*        step 1: ApCtl CALL TxPgm      
//*                     1).維護撥款匯款檔(撥款)
//*        step 2: ApCtl CALL AcEnter(0.正常) 
//*                     1).更新正常分錄(1.已入帳) 
//*                     2).產生分錄清單
//*    2.主管放行訂正
//*        step 1: ApCtl CALL TxPgm      
//*                     1).維護撥款匯款檔(撥款)
//*        step 2: ApCtl CALL AcEnter(0.正常) 
//*                     1).更新正常分錄(同會計日、同批號、訂正經辦登錄交易=>EntAc=0.未入帳，else 1.已入帳) 
//*                     2).產生反向分錄(同會計日、同批號、訂正經辦登錄交易=>EntAc=0.未入帳，else 1.已入帳)，不更新DB 
//*                     3).不產生分錄清單
//* 
//*   二、處理時機                         
//*                                            登帳(一段式、二段式登錄)     入帳(一段式、二段式放行)    
//*    1.call AcBookCom.run.更新會計分錄的帳冊別	 V
//*    2.call AcReceivableCom 銷帳科目處理  	     V
//*    3.call AcLoanRenewCom 借新還舊處理    	     V 
//*    4.call AcNegCom 債協暫收款會計帳務明細檔      V
//*    5.call AcMainCom 總帳入帳處理    	                                   V
//*    6.call AcTxFormCom 交易分錄清單  	         V                         V   
//*    7.AcDetail(會計帳務明細檔)更新                        V                         V                        
//*    8.call AcCollListCom 更新法催紀錄清單檔                                                      V
//* 
//*   三、業務關帳檢核                     
//* 
//*    業務類別                                   關帳狀態:1-關帳時(顯示錯誤訊息)， 0-開帳/2-關帳取消時通過            
//*    01-撥款匯款(撥款、暫收退還(非退票))                  個別業務不可交易          
//*    02-支票繳款                                個別業務不可交易     
//*    09-放款                                    全部業務不可交易   
//* 
//*   四、傳票批號、 傳票號碼 
//*
//*     業務類別  SecNo    傳票批號 SlipBatNo      傳票號碼 SlipNo
//*	    01:撥款匯款               放款業務批號                      放款業務傳票號碼
//*	    02:支票繳款               固定 11                               放款業務傳票號碼
//* 	09:放款                      提存>= 90                           放款業務傳票號碼
//*     09:放款            放款業務批號           放款業務傳票號碼
//*
//*  參數：
//* --- Input ---
//*      
//*  1).帳務訂正記號 
//*     AcHCode        DECIMAL(1)        
//*     0.正常
//*     1.訂正       
//*     2.沖正        
//* 
//*  2).會計帳務明細 ArrayList
//* 
//* --- Oput ---
//*  1).帳務訂正記號 
//*     AcHCode  DECIMAL(1) 
//*     0-正常 
//*     1-訂正未放行(刪除明細)
//*     2-訂正或沖正(要入總帳) 
//*     3-訂正(不入總帳)
//*
//*  2).會計帳務明細 ArrayList
//*     EntAc       入總帳記號   DECIMAL(1) 
//*      0:未入帳
//*      1:已入帳
//*     TitaHCode	訂正別	   VARCHAR2	1		
//*      0:正常
//*      1:訂正
//*      2:被訂正
//*      3:沖正
//*      4:被沖正
//*  3).FM101:交易分錄清單

/**
 * 會計入帳處理<BR>
 * run ： call by ApControl<BR>
 * 1.call AcBookCom 帳冊別處理<BR>
 * 2.call AcReceivableCom 銷帳科目處理 <BR>
 * 3.call AcLoanRenewCom 借新還舊處理<BR>
 * 4.call AcNegCom 債協暫收款處理<BR>
 * 5.call AcMainCom 總帳入帳處理<BR>
 * 6.call AcTxFormCom 交易分錄清單<BR>
 * 7.call AcCollListCom 更新法催紀錄清單檔 <BR>
 * 8.會計帳務明細檔更新<BR>
 * 
 * @author st1
 *
 */
@Component("acEnterCom")
@Scope("prototype")
public class AcEnterCom extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AcCloseService acCloseService;

	@Autowired
	public AcDetailService acDetailService;

	@Autowired
	public AcBookCom acBookCom;

	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	public AcLoanRenewCom acLoanRenewCom;

	@Autowired
	public AcNegCom acNegCom;

	@Autowired
	public AcMainCom acMainCom;

	@Autowired
	public AcTxFormCom acTxFormCom;

	@Autowired
	public AcCollListCom acCollListCom;

	@Autowired
	public AcDetailCom acDetailCom;

	@Autowired
	public LoanCom loanCom;

	private TitaVo titaVo;

	private List<AcDetail> acList = new ArrayList<AcDetail>();

	private AcClose tAcClose = new AcClose();
	private AcCloseId acCloseId = new AcCloseId();

	private int AcHCode = 0; // 帳務訂正記號 0-正常 1-訂正(刪除帳務) 2-沖正帳務(要入帳) 3-沖正帳務(訂正、不入帳)
	private int RelDy = 0; // 登放日期
	private String RelTxseq; // 登放序號
	private int AcDate = 0; // 會計日期

	private int SlipBatNo = 0; // 傳票批號
	private int SlipNo = 0; // 傳票號碼
	private int SlipBatNoModify = 0; // 訂正前傳票批號
	private int SlipNoModify = 0; // 訂正前傳票號碼
	private String SecNo = ""; // 業務類別
	private int acSeq; // 分錄序號
	private String TitaBatchNoModify; // 整批批號

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;

		// AcHCode 帳務訂正記號
		AcHCode = this.txBuffer.getTxCom().getBookAcHcode();

		this.info("AcEnterCom.... : AcHCode=" + AcHCode);

		// 開帳狀態檢核(非提存入賬)
		if (!"L618D".equals(titaVo.getTxcd())) {
			acCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			acCloseId.setBranchNo(titaVo.getAcbrNo());
			acCloseId.setSecNo("09"); // 業務類別: 09-放款
			tAcClose = acCloseService.findById(acCloseId, titaVo); // holdById
			if (tAcClose == null) {
				throw new LogicException(titaVo, "E6005", "未執行 L6880_系統換日"); // 檢查錯誤
			}
			switch (tAcClose.getClsFg()) {
			case 0: // 0:開帳
				break;
			case 1: // 1:關帳
				throw new LogicException(titaVo, "E6005", "已關帳(09:放款)"); // 非開帳狀態
			case 2: // 2:關帳取消
				break;
			case 3: // 3:夜間批次執行中
				throw new LogicException(titaVo, "E6005", "夜間批次執行中"); // 非開帳狀態
			case 4: // 4.夜間批次執行完畢
				throw new LogicException(titaVo, "E6005", "夜間批次執行完畢，需續執行 L6880_系統換日"); // 非開帳狀態
			}
		}

		// 登放日期、登放序號
		if (titaVo.isHcodeErase()) {
			RelDy = titaVo.getOrgEntdyI() + 19110000; // 登放日期
		} else {
			RelDy = titaVo.getEntDyI() + 19110000; // 登放日期
		}
		RelDy = this.txBuffer.getTxCom().getReldy() + 19110000; // 登放日期
		RelTxseq = this.txBuffer.getTxCom().getRelNo(); // 登放序號

		// 交易進行狀態
//		    titaVo.getActFgS()                         
//		    1STEP TX -> 0 , 2STEP TX -> 1,2
//		    titaVo.isActfgEntry()      登帳               0,1
//		    titaVo.isActfgRelease()    入帳               0,2
//		    titaVo.isActfgSuprele()    主管放行        2 
		//
		// 交易訂正記號
//		    titaVo.getAcHCode()                      
//		    titaVo.isAcHCodeNormal()     正常交易           0
//		    titaVo.isAcHCodeErase()      訂正交易           1
//		    titaVo.isAcHCodeModify()     修改交易           2

		// 1.prepare acList
//          1.修改交易刪除舊分錄，新增新分錄(沿用修改前傳票批號、傳票號碼、分錄序號、整批批號)
//          2.訂正或主管放行， Load from AcDetail DB 
//	    	3.沖正產生借貸相反之新分錄 
//          4.正常交易，from txBuffer
		Slice<AcDetail> slAcList = acDetailService.acdtlRelTxseqEq(RelDy, RelTxseq, this.index, Integer.MAX_VALUE,
				titaVo); // findByTxseq
		if (slAcList != null) {
			//
			if (titaVo.isHcodeModify() && AcHCode == 0) {
				for (AcDetail ac : slAcList.getContent()) {
					if (ac.getEntAc() == 4) {
						if (SlipBatNoModify == 0) {
							SlipBatNoModify = ac.getSlipBatNo();
							SlipNoModify = ac.getSlipNo() - 1;
							TitaBatchNoModify = ac.getTitaBatchNo();
							acSeq = ac.getAcSeq() - 1;
						}
						acList.add(ac);
					}
					this.info("AcDetail delete " + ac.toString());
				}
				try {
					acDetailService.deleteAll(acList, titaVo); // delete AcDetail
				} catch (DBException e) {
					throw new LogicException(titaVo, "E6003", "AcDetail delete " + e.getErrorMsg());
				}
			} else
				for (AcDetail ac : slAcList.getContent()) {
					acSeq++;
					// load 0.正常分錄
					if ("0".equals(ac.getTitaHCode())) {
						acList.add(ac);
					}
				}
		}

		// 起帳，分錄序號續編
		if (AcHCode == 0 && !titaVo.isActfgSuprele()) {
			acList = this.txBuffer.getAcDetailList();
			if (acList != null) {
				for (AcDetail ac : acList) {
					acSeq++;
					ac.setAcSeq(acSeq);
					ac.getAcDetailId().setAcSeq(acSeq);
					ac.setTitaHCode("0");

					this.info("2 acseq" + acSeq + ac.toString());
				}
			}
		}

		// 無分錄
		if (acList == null || acList.size() == 0) {
			this.info("AcEnterCom skip size = 0 AcHCode=" + AcHCode);
			return this.sendList();
		}
		// 業務類別
		SecNo = acList.get(0).getTitaSecNo();

		// 傳票批號 >= 90 提存入帳 => 僅更新AcDetail會計帳務明細檔，訂正採沖正處理
		if (acList.get(0).getSlipBatNo() >= 90) {
			AcDate = acList.get(0).getAcDate();
			// 會計帳務明細
			if (AcHCode > 0) {
				AcHCode = 2;
				this.txBuffer.getTxCom().setBookAcHcode(AcHCode);
				procAcHCode90(acList);
				acList = this.txBuffer.getAcDetailList(); // 沖正分錄
			}
			// acList檢核
			procAcListCheck();
			// 業務關帳檢核
			procAccloseCheck();
			// 更新會計帳務明細檔
			procAcDetailUpdate();

			return this.sendList();
		}

		// 會計日期
		AcDate = titaVo.getEntDyI();

// AcHCode 帳務訂正記號 0-正常 1-訂正(刪除帳務) 2-沖正帳務(要入帳) 3-沖正帳務(訂正、不入帳)
// ApControl => 0.正常 1.訂正(本日訂正) 2.沖正(隔日訂正)
		// 本日訂正
		// 1. 經辦修正的訂正交易 AcHCode = 1
		// 2,3 經辦訂正

		if (AcHCode == 1) {
			if (titaVo.isHcodeModify()) {
				AcHCode = 1;
			} else {
				AcHCode = 2;
				getAcHCode(); // AcHcode 2,3
			}
			this.txBuffer.getTxCom().setBookAcHcode(AcHCode);
		}
		// 核准主管: 訂正或主管放行
		if (AcHCode == 1 || titaVo.isActfgSuprele()) {
			for (AcDetail ac : acList) {
				if (titaVo.isActfgSuprele()) {
					ac.setTitaSupNo(titaVo.getTlrNo());
				}
				ac.setTitaSupNo(titaVo.getEmpNos());
			}
			this.txBuffer.setAcDetailList(acList);
		}

		// 入帳更新(以正常分錄入帳)
		if (AcHCode == 0 || AcHCode == 1) {
			this.txBuffer.setAcDetailList(acList);
			this.procUdate();
		}

		// 入帳更新(以沖正分錄入帳)
		if (AcHCode == 2 || AcHCode == 3) {
			procAcHCode2(acList);
			acList = this.txBuffer.getAcDetailList(); // 沖正分錄
			// 入帳更新(以沖正分錄更新)
			this.txBuffer.setAcDetailList(acList);
			this.procUdate();
		}

		// 11.交易分錄清單 AcTxFormCom 登帳
		// 整批入帳、修改及主管的訂正交易不印交易分錄清單
		if (titaVo.isTrmtypBatch() || ((titaVo.isHcodeModify() || titaVo.isActfgSuprele()) && AcHCode > 0)) {
		} else {
			this.txBuffer.setAcDetailList(acList);
			acTxFormCom.setTxBuffer(this.txBuffer);
			this.addAllList(acTxFormCom.run(titaVo));
		}

		// 修正交易的訂正正需清空，放修改後的分錄
		if (titaVo.isHcodeModify() && AcHCode > 0) {
			this.txBuffer.setAcDetailList(new ArrayList<AcDetail>());
		}

		// Process End
		return this.sendList();

	}

	// 入帳更新
	private void procUdate() throws LogicException {
		this.info("procUdate size=" + acList.size());
		if (acList.size() == 0) {
			return;
		}

		// 2.acList檢核
		procAcListCheck();

		// 3.業務關帳檢核
		procAccloseCheck();

		// 4.帳冊別處理 AcBookCom 登帳
		if (titaVo.isActfgEntry()) {
			acBookCom.setTxBuffer(this.txBuffer);
			acBookCom.run(titaVo);
		}

		// 5.銷帳科目處理 AcReceivableCom 登帳
		if (titaVo.isActfgEntry()) {
			acReceivableCom.setTxBuffer(this.txBuffer);
			acReceivableCom.run(titaVo);
		}

		// 6.會計借新還舊處理 AcReceivableCom 登帳
		if (titaVo.isActfgEntry()) {
			acLoanRenewCom.setTxBuffer(this.txBuffer);
			acLoanRenewCom.run(titaVo);
		}

		// 7.產生債協入帳明細 AcNegCom 入帳
		if (titaVo.isActfgRelease()) {
			acNegCom.setTxBuffer(this.txBuffer);
			acNegCom.run(titaVo);
		}

		// 8.總帳入帳處理 AcMainCom 入帳
		if (titaVo.isActfgRelease()) {
			acMainCom.setTxBuffer(this.txBuffer);
			acMainCom.upd(this.txBuffer.getTxCom().getBookAcHcode(), this.txBuffer.getAcDetailList(), titaVo); // 入帳更新
		}

		// 9.會計帳務明細 登帳,入帳, 主管放行
		procAcDetailUpdate();

		// 10.更新法催紀錄清單檔 AcCollListCom 入帳
		if (titaVo.isActfgRelease()) {
			acCollListCom.setTxBuffer(this.txBuffer);
			acCollListCom.run(titaVo); // 入帳更新
		}
	}

	/* 業務關帳檢核 */
	private void procAccloseCheck() throws LogicException {
//     1).檢核放款業務，若關帳則全部業務不可交易
//     2).檢核個別業務，若關帳則該業務不可交易
//     3).取得傳票批號、傳票號碼，更新傳票號碼     
//        SlipBatNo 傳票批號 
//         1.02:支票繳款 => 固定 11
//         2.提存        >= 90
//         2.其他        => 放款業務關帳 + 1
//    業務類別  SecNo    傳票批號 SlipBatNo      傳票號碼 SlipNo
//		01:撥款匯款               放款業務批號                 放款業務傳票號碼
//		02:支票繳款               固定 11                          放款業務傳票號碼
//		09:放款                      提存>= 90                      放款業務傳票號碼
//		09:放款                      放款業務批號                 放款業務傳票號碼
		// 放款業務更新

		acCloseId.setAcDate(AcDate);
		acCloseId.setBranchNo(titaVo.getAcbrNo());
		acCloseId.setSecNo("09"); // 業務類別: 09-放款
		tAcClose = acCloseService.holdById(acCloseId, titaVo); // holdById
		if (tAcClose == null) {
			tAcClose = new AcClose();
			tAcClose.setAcCloseId(acCloseId);
			tAcClose.setClsFg(0);
			tAcClose.setBatNo(1);
			tAcClose.setClsNo(0);
			tAcClose.setSlipNo(0);
			try {
				acCloseService.insert(tAcClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "Acclose insert " + e.getErrorMsg());
			}
		}
		SlipNo = tAcClose.getSlipNo(); // 傳票號碼
		// 登帳且非修正且非主管=>更新傳票號碼
		if (titaVo.isActfgEntry() && !titaVo.isHcodeModify() && !titaVo.isActfgSuprele()) {
			tAcClose.setSlipNo(SlipNo + acList.size());
		}
		try {
			acCloseService.update(tAcClose, titaVo); // update AcClose
		} catch (DBException e) {
			throw new LogicException(titaVo, "E6003", "Acclose update " + tAcClose + e.getErrorMsg());
		}

		// 傳票批號 ==> >= 90 :提存入帳，支票繳款:固定 11
		if (acList.get(0).getSlipBatNo() >= 90) {
			SlipBatNo = acList.get(0).getSlipBatNo();
		} else if ("02".equals(SecNo)) {
			SlipBatNo = 11;
		} else {
			SlipBatNo = tAcClose.getClsNo() + 1; // 傳票批號
			// 關帳狀態:0-開帳 1-關帳 2-關帳取消
			if (tAcClose.getClsFg() == 1) {
				throw new LogicException(titaVo, "E6003", "放款業務已關帳  ");
			}
			// 訂正傳票批號需相同
			if (AcHCode == 1 && SlipBatNo != acList.get(0).getSlipBatNo()) {
				throw new LogicException(titaVo, "E6003",
						"再開帳後，不可訂正前批關帳之帳務, " + SlipBatNo + "<>" + acList.get(0).getSlipBatNo());
			}
		}

		// 個別業務更新
		if (!"09".equals(SecNo)) {
			acCloseId.setSecNo(SecNo);
			tAcClose = acCloseService.holdById(acCloseId, titaVo); // findById
			if (tAcClose == null) {
				tAcClose = new AcClose();
				tAcClose.setAcCloseId(acCloseId);
				tAcClose.setClsFg(0);
				tAcClose.setBatNo(1);
				tAcClose.setClsNo(0);
				tAcClose.setSlipNo(acList.size());
				try {
					acCloseService.insert(tAcClose, titaVo);
				} catch (

				DBException e) {
					throw new LogicException(titaVo, "E6003", "Acclose insert " + e.getErrorMsg());
				}
			} else {
				if (tAcClose.getClsFg() == 1) {
					throw new LogicException(titaVo, "E6003", "個別業務已關帳  " + SecNo);
				}
				// 登帳且非修改=>更新傳票號碼
				if (titaVo.isActfgEntry() && !titaVo.isHcodeModify()) {
					tAcClose.setSlipNo(tAcClose.getSlipNo() + acList.size());
					try {
						acCloseService.update(tAcClose, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E6003", "Acclose update " + e.getErrorMsg());
					}
				}
			}
		}
	}

	/* AcList檢核 */
	private void procAcListCheck() throws LogicException {
		BigDecimal DbAmt = BigDecimal.ZERO;
		BigDecimal CrAmt = BigDecimal.ZERO;

		for (AcDetail ac : acList) {
			if (ac.getEntAc() == 0) {
				if (ac.getDbCr().equals("D"))
					DbAmt = DbAmt.add(ac.getTxAmt());
				else
					CrAmt = CrAmt.add(ac.getTxAmt());
			}
		}

		String checkAc = "";
		if (DbAmt.compareTo(CrAmt) == 0) {
			checkAc = "equal !! ";
		} else {
			checkAc = "unequal !! ";
		}
		for (AcDetail ac : acList) {
			this.info(checkAc + ac.getDbCr() + " " + ac.getAcctCode() + " " + FormatUtil.padLeft("" + ac.getTxAmt(), 11)
					+ " " + ac.getCustNo() + "-" + ac.getFacmNo() + "-" + ac.getBormNo() + " " + ac.getSumNo() + " "
					+ ac.getRvNo());
		}
		this.info(checkAc + "DB=" + DbAmt.setScale(0, BigDecimal.ROUND_HALF_UP) + ", Cr=" + CrAmt + ", diff="
				+ DbAmt.subtract(CrAmt));

		if (DbAmt.compareTo(CrAmt) != 0) {
			throw new LogicException(titaVo, "E6003", "借貸不平，借方=" + DbAmt.setScale(0, BigDecimal.ROUND_HALF_UP) + ", 貸方="
					+ CrAmt + ", 差額=" + DbAmt.subtract(CrAmt));
		}
	}

	/* 會計帳務明細檔更新 */
	private void procAcDetailUpdate() throws LogicException {
//      訂正記號                                                                                 入總帳記號     
//	     0.正常             入帳  titaVo.isActfgRelease()            1:已入帳                                                          
//	     1.訂正      主管放行  titaVo.isActfgSuprele()            0:未入帳
//	     2.沖正             新分錄                                                             3.沖正
//                                                            
///	                                
//       登帳、正常/訂正   titaVo.isActfgEntry() AND AcHCode IN(0,2) THEN CREATE 
//       登帳、訂正            titaVo.isActfgEntry() AND AcHCode = 1     THEN DELETE 
//       主管放行                      titaVo.isActfgSuprele()                 THEN UPDATE

// 傳票批號
//   1.02:支票繳款 => 固定 11
//   2.其他             => 放款傳票批號		
// 傳票號碼             => 放款傳票號碼
// 登帳且非修改=>更新傳票批號、傳票號碼、 修正的正常分錄沿用原傳票批號、傳票號碼 
// EntAc  0.未入帳 1.已入帳   4-訂正(出分錄清單用)

		// AcHCode=0,1=>以正常分錄入帳、2,3=>以沖正分錄入帳
		int j = 0;
		for (AcDetail ac : acList) {
			if (AcHCode == 0 && titaVo.isActfgRelease()) {
				ac.setEntAc(1);
			}
			if (AcHCode == 1 && titaVo.isActfgEntry()) {
				ac.setEntAc(4);
			}
			if (AcHCode == 1 && titaVo.isActfgSuprele()) {
				ac.setEntAc(0);
			}
			if (AcHCode == 2) {
				ac.setEntAc(1);
			}
			if (AcHCode == 3) {
				ac.setEntAc(0);
			}
			j++;
			// 登帳且非修改=>更新傳票批號、傳票號碼
			if (titaVo.isActfgEntry() && !titaVo.isHcodeModify()) {
				ac.setSlipBatNo(SlipBatNo);
				ac.setSlipNo(SlipNo + j);
			}
			// 修正的正常分錄沿用原傳票批號、傳票號碼、整批批號
			if (titaVo.isHcodeModify() && AcHCode == 0) {
				ac.setSlipBatNo(SlipBatNoModify);
				ac.setSlipNo(SlipNoModify + j);
				ac.setTitaBatchNo(TitaBatchNoModify);
			}
			if (titaVo.isActfgSuprele()) {
				ac.setTitaSupNo(titaVo.getTlrNo());
			}
			this.info("procAcDetailUpdate" + ac.toString());
		}

		// 主管放行，更新正常分錄，不新增沖正分錄
		if (titaVo.isActfgSuprele()) {
			if (titaVo.isHcodeNormal()) {
				try {
					acDetailService.updateAll(acList, titaVo); // update AcDetail
				} catch (DBException e) {
					throw new LogicException(titaVo, "E6003", "AcDetail update " + e.getErrorMsg());
				}
			}
			return;
		}

		// 經辦登錄或訂正 => 更新(2段式修改的訂正)、新增
		if (titaVo.isActfgEntry()) {
			if (AcHCode == 1) {
				try {
					acDetailService.updateAll(acList, titaVo); // update AcDetail
				} catch (DBException e) {
					throw new LogicException(titaVo, "E6003", "AcDetail update " + e.getErrorMsg());
				}
			} else {
				try {
					acDetailService.insertAll(acList, titaVo); // insert AcDetail
				} catch (DBException e) {
					throw new LogicException(titaVo, "E6003", "AcDetail insert " + e.getErrorMsg());
				}
			}

		}
	}

	/* 沖正帳務是否要入帳 */
	private void getAcHCode() throws LogicException {
		// AcHcode 帳務訂正記號 0-正常 1-訂正未放行(刪除、不入帳) 2-沖正帳務(沖正、要入帳) 3-沖正帳務(訂正、不入帳)

		// 隔日訂正 => 2-沖正帳務(沖正、要入帳)
		if (AcDate != acList.get(0).getAcDate()) {
			AcHCode = 2;
			this.info("getAcHCode AcHCode=2, AcDate=" + acList.get(0).getAcDate());
			return;
		}
		// 支票繳款 => 3-沖正帳務(訂正、不入帳)
		if ("02".equals(SecNo)) {
			AcHCode = 3;
			this.info("getAcHCode AcHCod=3,SecNo=" + SecNo);
			return;
		}
		// 傳票批號不同 => 2-沖正帳務(沖正、要入帳)
		acCloseId.setAcDate(AcDate);
		acCloseId.setBranchNo(titaVo.getAcbrNo());
		acCloseId.setSecNo("09"); // 業務類別: 09-放款
		tAcClose = acCloseService.findById(acCloseId, titaVo);
		if (tAcClose != null && acList.get(0).getSlipBatNo() != (tAcClose.getClsNo() + 1)) {
			AcHCode = 2;
			this.info("getAcHCode AcHCode=2, SlipBatNo=" + acList.get(0).getSlipBatNo() + "/"
					+ (tAcClose.getClsNo() + 1));
			return;
		}
		// 訂正整批彙總傳票 2-沖正帳務(沖正、要入帳)
		if (acList.get(0).getSlipSumNo() > 0) {
			this.info("getAcHCode AcHCode=2, SlipSumNo=" + acList.get(0).getSlipSumNo());
			AcHCode = 2;
			return;
		}
		// else
		AcHCode = 3;
		this.info("getAcHCode AcHCode=3");
	}

	/* 處理沖正 */
	private void procAcHCode2(List<AcDetail> acList0) throws LogicException {
//	     1.更新入總帳記號
//	     2.主管放行訂正不更新訂正別
//	     3.依原分錄產生借貸相反之新分錄
		AcDetail acDetail = new AcDetail();
		List<AcDetail> acList2 = new ArrayList<AcDetail>();

		this.info("AcEnterCom procAcHCode2 AcHCode=" + AcHCode);
		for (int i = acList0.size() - 1; i >= 0; i--) {
			AcDetail ac = acList0.get(i);
			// 原分錄
			if (AcHCode == 3) {
				ac.setEntAc(0); // 未入帳
			} else {
				ac.setEntAc(1); // 已入帳
			}
			// 主管放行訂正不更新訂正別(於經辦定正時更新)
			if (!titaVo.isActfgSuprele()) {
				if (titaVo.getEntDyI() == titaVo.getOrgEntdyI()) {
					ac.setTitaHCode("2"); // 訂正別 2:被訂正
				} else {
					ac.setTitaHCode("4"); // 訂正別 4:被沖正
				}
			}
			this.info("procAcHCode2 ac=" + ac.toString());
			// 產生新分錄，入總帳記號3.沖正
			acSeq++;
			acDetail = new AcDetail();
			moveAcDetail(ac, acDetail);
			acDetail.setAcSeq(acSeq);
			if (ac.getDbCr().equals("D")) {
				acDetail.setDbCr("C");
			} else {
				acDetail.setDbCr("D");
			}
			acList2.add(acDetail);
		}
		// AcHCode=2,3=>以沖正分錄入帳(正常分錄在此更新)
		if (AcHCode == 2 || AcHCode == 3) {
			try {
				acDetailService.updateAll(acList0, titaVo); // update AcDetail
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "procAcHCode2 update " + e.getErrorMsg());
			}
		}

		/* 產生會計分錄 */
		this.txBuffer.setAcDetailList(acList2);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);
		this.setTxBuffer(acDetailCom.getTxBuffer());
	}

	private void moveAcDetail(AcDetail ac, AcDetail acDetail) {
		if (titaVo.getEntDyI() == titaVo.getOrgEntdyI()) {
			acDetail.setTitaHCode("1"); // 訂正別 1:訂正
		} else {
			acDetail.setTitaHCode("3"); // 訂正別 3:沖正
		}
		acDetail.setCustNo(ac.getCustNo());
		acDetail.setFacmNo(ac.getFacmNo());
		acDetail.setBormNo(ac.getBormNo());
		acDetail.setTxAmt(ac.getTxAmt());
		acDetail.setAcctCode(ac.getAcctCode());
		acDetail.setReceivableFlag(ac.getReceivableFlag());
		acDetail.setRvNo(ac.getRvNo());
		acDetail.setSlipNote(ac.getSlipNote());
		acDetail.setJsonFields(ac.getJsonFields());
		acDetail.setSumNo(ac.getSumNo());
		acDetail.setAcBookCode(ac.getAcBookCode());
		acDetail.setAcSubBookCode(ac.getAcSubBookCode());
		this.info("moveAcDetail=" + acDetail.toString());
	}

	/* 處理訂正(反向沖正傳票) */
	private void procAcHCode90(List<AcDetail> acList0) throws LogicException {
		this.info("procAcHCode90 acListsize=" + acList0.size());
//	    .依原分錄產生借貸相反之新分錄(，
		int slipBatNo = acList0.get(0).getSlipBatNo();
		AcDetail acDetail = new AcDetail();
		List<AcDetail> acList2 = new ArrayList<AcDetail>();
		for (int i = acList0.size() - 1; i >= 0; i--) {
			AcDetail ac = acList0.get(i);
			ac.setTitaHCode("4"); // 訂正別 4:被沖正
			acSeq++;
			acDetail = new AcDetail();
			moveAcDetail(ac, acDetail);
			acDetail.setAcDate(ac.getAcDate());
			acDetail.setSlipBatNo(slipBatNo);
			acDetail.setAcSeq(acSeq);
			if (ac.getDbCr().equals("D")) {
				acDetail.setDbCr("C");
			} else {
				acDetail.setDbCr("D");
			}
			acList2.add(acDetail);
		}
		// 更新原分錄
		try {
			acDetailService.updateAll(acList0, titaVo); // update AcDetail
		} catch (DBException e) {
			throw new LogicException(titaVo, "E6003", "procAcHCode2 update " + e.getErrorMsg());
		}
		this.txBuffer.setAcDetailList(acList2);

		/* 產生會計分錄 */
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);
		this.setTxBuffer(acDetailCom.getTxBuffer());
	}

}
