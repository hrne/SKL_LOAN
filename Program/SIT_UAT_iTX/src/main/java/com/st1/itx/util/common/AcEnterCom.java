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
//*    1.正常交易   
//*      A.登帳(一段式、二段式登錄)
//*                     ApCtl CALL TxPgm      ---->   ApCtl CALL AcEnter(0.正常) 
//*                     1).產生交易會計分錄           1).執行分錄處理 
//*                     2).TxPgm Call AcPayment
//*                        a.產生收付欄會計分錄
//*                        b.維護撥款匯款檔
//* 
//*      B.主管放行(二段式) 
//*                     ApCtl CALL TxPgm      ---->   ApCtl CALL AcEnter(0.正常) 
//*                     1).X                          1).執行分錄處理(原分錄)  
//* 
//*    2.修正交易，執行兩次  
//*                     ApCtl CALL AcEnter(1.訂正)   ---->    ApCtl CALL TxPgm      ---->   ApCtl CALL AcEnter(0.正常)
//*                     1).執行分錄處理(原分錄訂正)   1).產生交易會計分錄                    1).執行分錄處理(新分錄)   
//*                                                   2).TxPgm Call AcPayment 
//*                                                      a.產生收付欄會計分錄
//*                                                      b.維護撥款匯款檔
//*    3.當日訂正
//*      A.主管放行訂正(二段式) 
//*                     ApCtl CALL TxPgm       ---->  ApCtl CALL AcEnter(1.訂正) 
//*                     1).X                          1).執行分錄處理 
//* 
//*      B.登帳訂正(一段式、二段式登錄)
//*                     ApCtl CALL TxPgm       ---->  ApCtl CALL AcEnter(1.訂正)
//*                     1).TxPgm Call AcPayment
//*                        a.維護撥款匯款檔 
//* 
//*    4.隔日訂正   
//*      A.隔日訂正自動設為一段式，需主管刷卡
//*                     ApCtl CALL TxPgm       ---->  ApCtl CALL AcEnter(2.隔日訂正) 
//*                     1).X                          1).原分錄->入總帳記號:2-被沖正
//*                                                   2).執行新分錄處理->入總帳記號:3-沖正
//*                                                      a.依原分錄產生借貸相反之新分錄
//*                                                      b.借方整批入帳來源科目不回沖，轉至'暫收款－可抵繳'科目(TAV)
//*                                                      
//*    5.訂正不同傳票批號，視同隔日訂正；經辦需續作訂正(不可修正)		
//*      A.主管放行訂正(二段式) ，視同一段式交易，主管放行訂正不處理
//*                     ApCtl CALL TxPgm       ---->  ApCtl CALL AcEnter(1.訂正) 
//*                     1).X                          1).顯示<訂正前批關帳傳票，出沖正帳務；經辦需續作訂正(不可修正)>訊息
//* 
//*      B.登帳訂正(一段式、二段式登錄)，產生借貸相反分錄
//*                     ApCtl CALL TxPgm       ---->  ApCtl CALL AcEnter(1.訂正)
//*                     1).TxPgm Call AcPayment       1).顯示<訂正前批關帳傳票，出沖正帳務>訊息
//*                        a.維護撥款匯款檔                     2).設定 2.隔日訂正
//*                                                   3).原分錄->入總帳記號:2-被沖正
//*                                                   4).依原分錄產生借貸相反之新分錄->入總帳記號:3-沖正
//* 
//*   二、處理時機                         
//*                                            登帳(一段式、二段式登錄)     入帳(一段式、二段式放行)    
//*    1.call AcBookCom.run.更新會計分錄的帳冊別	 V
//*    2.call AcReceivableCom 銷帳科目處理  	     V
//*    3.call AcLoanRenewCom 借新還舊處理    	     V 
//*    4.call AcNegCom 債協暫收款會計帳務明細檔      V
//*    5.call AcMainCom 總帳入帳處理    	                                   V
//*    6.call AcTxFormCom 交易分錄清單  	         V                         V   
//*    7.call AcCollListCom 更新法催紀錄清單檔                                                      V
//*    8.AcDetail(會計帳務明細檔)更新                        V                         V                        
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
//*  1.訂正記號 
//*     AcHCode        DECIMAL(1)        
//*     0.正常
//*     1.當日訂正       
//*     2.隔日訂正        
//* 
//*  2.會計帳務明細 ArrayList
//* 
//* --- Oput ---
//*  FM101:交易分錄清單

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

	private TitaVo titaVo;

	private List<AcDetail> acList = new ArrayList<AcDetail>();

	private AcClose tAcClose = new AcClose();
	private AcCloseId acCloseId = new AcCloseId();

	private int AcHCode = 0; // 帳務訂正記號
	private int RelDy = 0; // 登放日期
	private String RelTxseq; // 登放序號
	private int AcDate = 0; // 會計日期

	private int SlipBatNo = 0; // 傳票批號
	private int SlipNo = 0; // 傳票號碼
	private String SecNo = ""; // 業務類別
	private String actFg; // 交易進行記號

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		int size = 0;
		if (this.txBuffer.getAcDetailList() != null) {
			size = this.txBuffer.getAcDetailList().size();
		}

		this.titaVo = titaVo;
		RelDy = this.txBuffer.getTxCom().getReldy() + 19110000; // 登放日期
		RelTxseq = this.txBuffer.getTxCom().getRelNo(); // 登放序號
		AcHCode = this.txBuffer.getTxCom().getBookAcHcode(); // 帳務訂正記號

		this.info("AcEnterCom.... : AcHCode=" + AcHCode + ",acListsize=" + size);
//		帳務訂正記號  AcHCode   0.正常     1.當日訂正     2.隔日訂正              

		// 交易進行狀態
//		    titaVo.getActFgS()                         
//		    1STEP TX -> 0 , 3STEP TX -> 1,2,3,4 , 2STEP TX -> 5,6
//		    titaVo.isActfgEntry()      登帳               0,3,5
//		    titaVo.isActfgRelease()    入帳               0,4,6
//		    titaVo.isActfgSuprele()    主管放行           4,6  
		//
		// 交易訂正記號
//		    titaVo.getAcHCode()                      
//		    titaVo.isAcHCodeNormal()     正常交易           0
//		    titaVo.isAcHCodeErase()      訂正交易           1
//		    titaVo.isAcHCodeModify()     修改交易           2

		// 1.prepare acList
//	    	1.隔日訂正，create 借貸相反之新分錄 
//          2.訂正或主管放行， Load from AcDetail DB 
//          3.正常交易，from txBuffer

		if (AcHCode > 0 || titaVo.isActfgSuprele()) { // 訂正或主管放行
			Slice<AcDetail> sacList = acDetailService.acdtlRelTxseqEq(RelDy, RelTxseq, this.index, Integer.MAX_VALUE,
					titaVo); // findByTxseq
			acList = sacList == null ? null : sacList.getContent();
			if (acList == null) {
				throw new LogicException(titaVo, "E6003", "訂正或主管放行時AcDetail NotFound " + RelDy + RelTxseq);
			}
		} else {
			acList = this.txBuffer.getAcDetailList();
		}

		// 業務類別
		SecNo = acList.get(0).getTitaSecNo();
		// 會計日期
		AcDate = this.txBuffer.getTxCom().getTbsdy();

		// 傳票批號 >= 90 提存入帳 => 僅更新AcDetail會計帳務明細檔
		if (acList.get(0).getSlipBatNo() >= 90) {
			AcDate = acList.get(0).getAcDate();
			// 業務關帳檢核
			procAccloseCheck();
			// 3.acList檢核
			procAcListCheck();
			// 會計帳務明細
			procAcDetailUpdate();
			return this.sendList();
		}

		// 訂正不同傳票批號，視同隔日訂正(支票繳款批號:固定11)
		if (AcHCode == 1 && !"02".equals(SecNo)) {
			acCloseId.setAcDate(AcDate);
			acCloseId.setBranchNo(titaVo.getAcbrNo());
			acCloseId.setSecNo("09"); // 業務類別: 09-放款
			tAcClose = acCloseService.findById(acCloseId, titaVo);
			if (tAcClose != null && acList.get(0).getSlipBatNo() != tAcClose.getBatNo()) {
				if (titaVo.isHcodeModify()) {
					throw new LogicException(titaVo, "E6003", "訂正前批關帳之帳務，經辦需續作訂正(不可修正)");
				}
				AcHCode = 2;
				this.txBuffer.getTxCom().setBookAcHcode(2);
				if (titaVo.isActfgSuprele()) {
					this.totaVo.setWarnMsg("訂正前批關帳傳票，出沖正帳務；經辦需續作訂正(不可修正)");
				} else {
					this.totaVo.setWarnMsg("訂正前批關帳傳票，出沖正帳務");
				}
				this.addList(this.totaVo);
			}
		}

		// 隔日訂正視同一段式交易，主管放行訂正不處理
		if (AcHCode == 2) {
			if (titaVo.isActfgSuprele()) {
				return this.sendList();
			} else {
				actFg = titaVo.getParam("ACTFG");
				titaVo.putParam("ACTFG", "0");
			}
		}

		// 隔日訂正：放行起分錄，登錄濾掉被沖正分錄
		if (AcHCode == 2) {
			procAcHCode2(acList);
			acList = this.txBuffer.getAcDetailList(); // 沖正分錄
		}

		// // 核准主管: 訂正或主管放行
		if (AcHCode == 1 || titaVo.isActfgSuprele()) {
			for (AcDetail ac : acList) {
				if (titaVo.isActfgSuprele()) {
					ac.setTitaSupNo(titaVo.getTlrNo());
				}
				ac.setTitaSupNo(titaVo.getEmpNos());
			}
			this.txBuffer.addAllAcDetailList(acList);
		}

		if (acList != null && acList.size() > 0) {
			// 2.業務關帳檢核
			procAccloseCheck();

			// 3.acList檢核
			procAcListCheck();

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
				acMainCom.upd(AcHCode, acList, titaVo); // 入帳更新
			}

			// 9.會計帳務明細 登帳,入帳, 主管放行
			procAcDetailUpdate();

			// 10.交易分錄清單 AcTxFormCom 登帳
			// 整批入帳及修改交易之訂正修改前，不印交易分錄清單
			if (!(titaVo.isHcodeModify() && AcHCode == 1) && !titaVo.isTrmtypBatch()) {
				this.txBuffer.setAcDetailList(acList);
				acTxFormCom.setTxBuffer(this.txBuffer);
				this.addAllList(acTxFormCom.run(titaVo));
			}

			// 11.更新法催紀錄清單檔 AcCollListCom 入帳
			if (titaVo.isActfgRelease()) {
				acCollListCom.setTxBuffer(this.txBuffer);
				acCollListCom.run(titaVo); // 入帳更新
			}
		}

		// 修正交易的訂正正需清空，放修改後的分錄
		if (titaVo.isHcodeModify() && AcHCode == 1)
			this.txBuffer.setAcDetailList(new ArrayList<AcDetail>());

		// 隔日訂正視同一段式交易，處理後放回原交易進行狀態
		if (AcHCode == 2) {
			titaVo.putParam("ACTFG", actFg);
		}

		// Process End
		return this.sendList();

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
			SlipNo = 0; // 傳票號碼
			tAcClose.setSlipNo(acList.size());
			try {
				acCloseService.insert(tAcClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "Acclose insert " + e.getErrorMsg());
			}
		} else {
			SlipNo = tAcClose.getSlipNo(); // 傳票號碼
			// 登帳且非當日訂正=>更新傳票號碼
			if (titaVo.isActfgEntry() && AcHCode != 1)
				tAcClose.setSlipNo(SlipNo + acList.size());
			try {
				acCloseService.update(tAcClose, titaVo); // update AcClose
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "Acclose update " + tAcClose + e.getErrorMsg());
			}
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
			// 當日訂正傳票批號需相同
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
				if (tAcClose.getClsFg() == 1)
					throw new LogicException(titaVo, "E6003", "個別業務已關帳  " + SecNo);
				if (titaVo.isActfgEntry() && AcHCode != 1) {// 登帳且非當日訂正=>更新傳票號碼
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
			this.info("AcEnterCom procAcListCheck" + ac.toString());
			if (ac.getDbCr().equals("D"))
				DbAmt = DbAmt.add(ac.getTxAmt());
			else
				CrAmt = CrAmt.add(ac.getTxAmt());
		}
		if (DbAmt.compareTo(CrAmt) != 0) {
			for (AcDetail ac : acList) {
				this.info("unequal !! " + ac.getDbCr() + " " + ac.getAcctCode() + " "
						+ FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " " + ac.getCustNo() + "-" + ac.getFacmNo() + "-"
						+ ac.getBormNo() + " " + ac.getRvNo());
			}
			this.info("借貸不平，借方=" + DbAmt.setScale(0, BigDecimal.ROUND_HALF_UP) + ", 貸方=" + CrAmt + ", 差額="
					+ DbAmt.subtract(CrAmt));
			throw new LogicException(titaVo, "E6003", "借貸不平，借方=" + DbAmt.setScale(0, BigDecimal.ROUND_HALF_UP) + ", 貸方="
					+ CrAmt + ", 差額=" + DbAmt.subtract(CrAmt));
		}
	}

	/* 會計帳務明細檔更新 */
	private void procAcDetailUpdate() throws LogicException {
//      訂正記號                                                                                 入總帳記號     
//	     0.正常             入帳  titaVo.isActfgRelease()            1:已入帳                                                          
//	     1.當日訂正      主管放行  titaVo.isActfgSuprele()        0:未入帳
//	     2.隔日訂正      新分錄                                                             3.沖正
//                                                            
///	                                
//       登帳、正常/隔日訂正   titaVo.isActfgEntry() AND AcHCode IN(0,2) THEN CREATE 
//       登帳、當日訂正            titaVo.isActfgEntry() AND AcHCode = 1     THEN DELETE 
//       主管放行                      titaVo.isActfgSuprele()                 THEN UPDATE

// 傳票批號
//   1.02:支票繳款 => 固定 11
//   2.其他             => 放款傳票批號		
// 傳票號碼             => 放款傳票號碼

// EntAc  0.未放行 1.已入帳 2.被沖正  3.沖正  4-訂正(出分錄清單用，帳務明細刪除)

		for (int i = 0; i < acList.size(); i++) {
			if (AcHCode == 0 && titaVo.isActfgRelease()) {
				acList.get(i).setEntAc(1);
			}
			if (AcHCode == 1 && titaVo.isActfgEntry()) {
				acList.get(i).setEntAc(4);
			}
			if (AcHCode == 1 && titaVo.isActfgSuprele()) {
				acList.get(i).setEntAc(0);
			}
			if (AcHCode == 2) {
				acList.get(i).setEntAc(3);
			}
			if (titaVo.isActfgEntry() && AcHCode != 1) {
				acList.get(i).setSlipBatNo(SlipBatNo);
				acList.get(i).setSlipNo(SlipNo + i + 1);
			}
			if (titaVo.isActfgSuprele()) {
				acList.get(i).setTitaSupNo(titaVo.getTlrNo());
			}
		}
		if (titaVo.isActfgSuprele()) {
			try {
				acDetailService.updateAll(acList, titaVo); // update AcDetail
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "AcDetail update " + e.getErrorMsg());
			}
		} else {
			if (titaVo.isActfgEntry()) {
				if (AcHCode == 1) {
					try {
						acDetailService.deleteAll(acList, titaVo); // delete AcDetail
					} catch (DBException e) {
						throw new LogicException(titaVo, "E6003", "AcDetail delete " + e.getErrorMsg());
					}
				} else {
					try {
						acDetailService.updateAll(acList, titaVo); // insert AcDetail
					} catch (DBException e) {
						throw new LogicException(titaVo, "E6003", "AcDetail insert " + e.getErrorMsg());
					}
				}

			}
		}

	}

	/* 處理隔日訂正 */
	private void procAcHCode2(List<AcDetail> acList0) throws LogicException {
//	     1.自動設定為一段式交易
//	     2.原分錄入總帳記號皆設定為 2:被沖正 
//	     3.依原分錄產生借貸相反之新分錄(，
//	       A.借方銀行存款科目不回沖，轉至'暫收款－可抵繳'科目(TAV)   
		int rpFacmno = 0;
		if (acList0 == null)
			throw new LogicException(titaVo, "E6003", "訂正或主管放行時AcDetail NotFound " + RelDy + RelTxseq);

		// 找最小額度編號
		for (AcDetail ac : acList0) {
			if (rpFacmno == 0 || ac.getFacmNo() < rpFacmno) {
				rpFacmno = ac.getFacmNo();
			}
		}

		AcDetail acDetail = new AcDetail();
		List<AcDetail> acList2 = new ArrayList<AcDetail>();

		this.info("AcEnterCom procAcHCode2 acListsize=" + acList0.size());
		int i = acList0.size();
		for (AcDetail ac : acList0) {
			// 原分錄，入總帳記號 2:被沖正(隔日訂正)
			ac.setEntAc(2);

			// 產生新分錄，入總帳記號3.沖正(隔日訂正)
			i++;
			acDetail = new AcDetail();
			acDetail.setCustNo(ac.getCustNo());
			acDetail.setFacmNo(ac.getFacmNo());
			acDetail.setBormNo(ac.getBormNo());
			acDetail.setTxAmt(ac.getTxAmt());
			acDetail.setAcctCode(ac.getAcctCode());
			acDetail.setReceivableFlag(ac.getReceivableFlag());
			acDetail.setRvNo(ac.getRvNo());
			acDetail.setSlipNote(ac.getSlipNote());
			acDetail.setSumNo(ac.getSumNo());
			acDetail.setAcBookCode(ac.getAcBookCode());
			acDetail.setAcSeq(i);
			if (ac.getDbCr().equals("D")) {
				acDetail.setDbCr("C");
				// 整批入帳的隔日訂正，還款來源(1xx-應收)回沖至暫收可抵繳
				if (acDetail.getSumNo() != null && acDetail.getSumNo().length() == 3
						&& "1".equals(ac.getSumNo().substring(0, 1)) && titaVo.getEntDyI() != titaVo.getOrgEntdyI()) {
					acDetail.setAcctCode("TAV");
					if (acDetail.getFacmNo() == 0) {
						acDetail.setFacmNo(rpFacmno);
					}
				}
			} else {
				acDetail.setDbCr("D");
			}
			acList2.add(acDetail);
		}
		this.txBuffer.addAllAcDetailList(acList2);

		/* 產生會計分錄 */
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

		this.setTxBuffer(acDetailCom.getTxBuffer());

		// 更新原分錄
		try {
			acDetailService.updateAll(acList0, titaVo); // update AcDetail
		} catch (DBException e) {
			throw new LogicException(titaVo, "E6003", "procAcHCode2 update " + e.getErrorMsg());
		}

	}

}
