package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxAmlLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.parse.Parse;

// 功能說明 :  
//  
//   一、 撥款匯款 (匯款戶名)
//      A.hcodeSendOut 撥款匯款 Call by  Call by ApControl 經辦提交  (L3100 撥款  2.L3110 預約撥款)
//        1.經辦登錄、修正，核對正確後以快捷鍵  [經辦提交]送AML檢核，寫入AML檢查紀錄檔
//        2.檢核或確認通過，主管才能放行或駁回
//        3.確認未過，需訂正
//        4.檢核或確認通過但主管駁回，可修正後，重跑流程
//      B.remitOut 撥款匯款 (暫收款退還 ) Call by L3220 暫收款退還 
//        1.經辦交易送AML檢核
//        2.檢核或確認通過，交易成功
// 
//   二、 銀扣授權 (扣款帳號戶名)
//      A.achAuth ACH銀扣授權  Call by  call by L440A(L4040)產生ACH授權提出資料
//	      1.篩選提出資料時，送AML檢核，寫入AML檢查紀錄檔
//        2.檢核或確認通過，篩選成功
//        3.確認未過，篩選失敗 
//      B.postAuth 郵局銀扣授權  Call by  call by L441A(L4041)產生 郵局授權提出資料
//        1.篩選提出資料時，送AML檢核，寫入AML檢查紀錄檔
//        2.檢核或確認通過，篩選成功
//        3.確認未過，篩選失敗 
//
//   三、 產出銀扣檔 (扣款帳號戶名) 
//      bankDeduct Call by L4450-產出銀行扣帳檔、L4451-銀行扣帳檔維護、L4452-產出媒體檔         
//        1.產出銀扣檔時，送AML檢核，寫入AML檢查紀錄檔
//        2.檢核或確認通過，製成媒體檔
//        3.檢核或確認未過，不會製成媒體檔，出錯誤清單
//        4.媒體可重製
//        
//   四、 整批入帳(1.匯款轉帳：借款人、交易人，2.支票兌現：借款人、發票人)
//      A.bankRmtf Call by L4200-入帳檔上傳作業 匯款轉帳
//        1.整批上傳時，送AML檢核，寫入AML檢查紀錄檔
//      B.batxCheque Call by L4200-入帳檔上傳作業 支票兌現
//        1.整批上傳時，送AML檢核，寫入AML檢查紀錄檔
//      C.batxCheck Call by TxBatch.Com (整批入帳公用程式） 
//        1.整批上傳時為需審查/確認名單，L420A整批檢核(L4002整批入帳作業)時，再送AML檢核
//        2.檢核AmlRsp1(借款人）或 AmlRsp2 (交易人/發票人)，依檢核狀態處理
//           0.非可疑名單/已完成名單確認   ==> 交易正常
//           1.需審查/確認 ==> E0022 該筆資料需進行AML審查/確認
//           2.為凍結名單/未確定名單 ==>  L420C轉暫收(L4002整批入帳作業)：暫收原因(10.AML凍結／未確定)     
//
//    五、AML需審查/確認，交易失敗寫入AML紀錄檔
//      nameCheckInsert call by ApControl 判斷交易有寫入AML紀錄檔時(txBuffer內AML檢查紀錄檔筆數>0)時
//        1.交易失敗，DB Rollback，寫入的AML紀錄檔也會被 Rollback
//        2.錯誤訊息為E0022-該筆資料需進行AML審查/確認時，仍需寫入AML檢查紀錄檔，以便經辦登入AML系統，接續作業
//
// 處理說明 : 
//   1. AML姓名檢核係透過 Call CheckAmlCom 進行檢核，並寫入AML檢查紀錄檔
//   2. 進行AML姓名檢核前，會先找AML紀錄檔是否已存在
//     A.不存在，則進行 AML姓名檢核
//     B.存在 且檢核狀態 = 1.需審查/確認，則會進行 AML姓名再檢核
//   3. 交易檢核時依檢核狀態回應訊息
//     0.非可疑名單/已完成名單確認  ==> 交易通過
//     1.需審查/確認 ==> E0022 該筆資料需進行AML審查/確認  
//     2.為凍結名單/未確定名單 ==>  ==> E0023 AML凍結名單/未確定名單  
//   4. 整批入帳時，會將檢核狀態寫入整批入帳檔(檢核未通過時整批入帳查詢時會顯示)，進行後續處理
//   5. AML檢查紀錄檔之 email address: 執行該交易的經辦 email(CdEmp 員工資料檔)

/**
 * AML交易檢核<BR>
 * 1.hcodeSendOut 撥款匯款 Call by Call by ApControl 經辦提交<BR>
 * 2.remitOut 暫收款退還 Call by Call by L3220 暫收款退還<BR>
 * 3.achAuth ACH 銀扣授權 ACH Call by call by L440A<BR>
 * 4.postAuth ACH 銀扣授權 ACH Call by call by L441A<BR>
 * 5.bankDeduct 產出銀扣檔 Call by L4450-產出銀行扣帳檔、L4451-銀行扣帳檔維護、L4452-產出媒體檔<BR>
 * 6.bankRmtf 匯款轉帳 整批上傳 Call by L4200-入帳檔上傳作業<BR>
 * 7.batxCheque 支票兌現 整批上傳 Call by L4200-入帳檔上傳作業<BR>
 * 8.batxCheck 整批入帳 Call by TxBatch.Com<BR>
 * 9.nameCheckInsert AML需審查/確認，交易失敗寫入AML紀錄檔 Call By ApControl
 * 
 * @author st1
 *
 */
@Component("txAmlCom")
@Scope("prototype")

public class TxAmlCom extends TradeBuffer {

	@Autowired
	Parse parse;

	@Autowired
	public CheckAml checkAml;

	@Autowired
	public TxAmlLogService txAmlLogService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public BankRmtfService bankRmtfService;

	private CustMain tCustMain = new CustMain();
	private int custNo = 0;

	/*---------- run ----------*/
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	/**
	 * 經辦提交 Call By ApControl 1.L3100 撥款 2.L3110 預約撥款
	 * 
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void hcodeSendOut(TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom.hcodeSendOut .....");
		if (titaVo.isHcodeSendOut()) {
			CheckAmlVo checkAmlVo = new CheckAmlVo();
			// 1.L3100 撥款 2.L3110 預約撥款
			if ("L3100".equals(titaVo.getTxcd())) {
				// AML@交易序號：前兩碼03+會計日期+交易序號
				checkAmlVo.setTransactionId("03" + "-" + (titaVo.getEntDyI() + 19110000) + "-" + titaVo.getOrgTxSeq());
				remitRp(checkAmlVo, titaVo);
			} else {
			}
		}
	}

	/**
	 * AML需審查/確認，交易失敗寫入AML紀錄檔 Call By ApControl
	 * 
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void nameCheckInsert(TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom.nameCheckInsert .....");
		// 交易失敗寫入AML紀錄檔, E0022 該筆資料需進行AML審查/確認
		if (this.txBuffer.getTxCom().getTxRsut() == 1 && "E0022".equals(this.txBuffer.getTxCom().getMsgId())) {
			List<TxAmlLog> lTxAmlLog = new ArrayList<TxAmlLog>();
			lTxAmlLog = this.txBuffer.getAmlList();
			if (lTxAmlLog != null && lTxAmlLog.size() > 0) {
				try {
					txAmlLogService.insertAll(lTxAmlLog, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E6003", "TxAmlCom nameCheckInsert " + e.getErrorMsg());
				}
			}
		}
	}

	/**
	 * 撥款匯款 Call By L3220 暫收款退還
	 * 
	 * @param borxNo 交易內容檔序號
	 * @param titaVo TitaVo
	 * @throws LogicException Exception
	 */
	/**
	 * 
	 * @param borxNo
	 * @param titaVo
	 * @throws LogicException
	 */
	public void remitOut(int borxNo, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom.remit .....");
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號：前兩碼03+會計日期+MRKey(戶號+額度)+交易內容檔序號
		checkAmlVo.setTransactionId("03" + "-" + (titaVo.getEntDyI() + 19110000) + "-" + titaVo.getMrKey().trim() + "-"
				+ parse.IntegerToString(borxNo, 4));
		remitRp(checkAmlVo, titaVo);
	}

	/*---------- 撥款 ----------*/
	private CheckAmlVo remitRp(CheckAmlVo checkAmlVo, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom.remitRp .....");

// 輸入欄位：titaVo 交易電文
//   RpCustName    匯款戶名
//                   1.借款人    ===> 自動帶入tim，畫面隱藏
//                   2.銀扣戶名  ===> 自動帶入tim，畫面隱藏  
//                   3.其他戶名  ===> 輸入以下欄位      
//   RemitIdKind   身份別(1)                1:自然人  2:法人    必輸
//   RemitId       身份證/居留證號碼(10)    可為空白
//   RemitGender   性別(1)                   法人填空白， 自然人必輸 1:男 2:女 
// 	 RemitBirthday 出生日期(7)            法人填空白， 自然人可不輸入                                      
//
// 設定欄位：
//   AML@交易序號：前兩碼03+會計日期+交易序號
//   保單角色： 4.借款人/7.轉帳委託人(BY 匯款戶名)	
//
// 回應訊息(檢核狀態)：
//   0.非可疑名單/已完成名單確認 ==> OK
//	 1. 需審查/確認  ==> E022
//   2.為凍結名單/未確定名單 ==> E023

		for (int i = 1; i <= 5; i++) {
			/* SecNo : 01:撥款匯款 */
			/* 撥款方式為 1 ~ 89 */
			if ("01".equals(titaVo.getSecNo()) && titaVo.get("RpCode" + i) != null) {
				if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) > 0
						&& parse.stringToInteger(titaVo.getParam("RpCode" + i)) < 90) {
					// 保單角色： 4.借款人/7.轉帳委託人(BY 匯款戶名)
					custNo = parse.stringToInteger(titaVo.getMrKey().substring(0, 7));
					tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
					if (titaVo.getParam("RpCustName" + i).equals(tCustMain.getCustName())) {
						checkAmlVo.setRoleId("4");
						custMainMove(tCustMain, checkAmlVo, titaVo);
					} else {
						checkAmlVo.setRoleId("7");
						checkAmlVo.setName(titaVo.getParam("RpCustName" + i)); // 姓名：戶名
						checkAmlVo.setIdentityCd(titaVo.getParam("RemitIdKind")); // 身份別 1:自然人 2:法人
						checkAmlVo.setCustKey(titaVo.getParam("RemitId")); // 身份證ID/居留證號碼
						checkAmlVo.setSex(titaVo.getParam("RemitGender")); // 性別
						if (parse.stringToInteger(titaVo.getParam("RemitBirthday")) == 0)
							checkAmlVo.setBirthEstDt(""); // 出生日期
						else
							checkAmlVo.setBirthEstDt("0" + titaVo.getParam("RemitBirthday")); // 出生日期
					}
					checkAmlVo.setRefNo(parse.IntegerToString(custNo, 7)); // 相關編號
					checkAmlVo.setAcctNo(titaVo.getMrKey()); // 放款案號：戶號-額度-撥款
					checkAmlVo.setCaseNo(titaVo.getBacthNo()); // 案號：整批批號(ex.LNnnnn)
					// AML檢核
					checkAmlVo = amlLogCheck(checkAmlVo, titaVo);
					// AML檢核回應訊息
					amlCheckRsp(checkAmlVo, titaVo);
				}
			}
		}
		return checkAmlVo;
	}

	/**
	 * ACH銀扣授權
	 * 
	 * @param tAchAuthLog ACH授權記錄檔
	 * @param titaVo      TitaVo
	 * @return CheckAmlVo
	 * @throws LogicException ...
	 */
	public CheckAmlVo authAch(AchAuthLog tAchAuthLog, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom achAuth .....");
// 輸入欄位：tAchAuthLog ACH授權記錄檔
//   RelAcctName     第三人帳戶戶名
///  RelationId      第三人身分證字號
//   RelAcctGender   M/F/space => 自然人 2:法人 性別 : M/F;若是法人填空格(space)
// 	 RelAcctBirthday 第三人出生日期                                          
//
// 設定欄位：
//   AML@交易序號 ：03+戶號(7)+扣款銀行(3)+扣款帳號(14)+交易日期(8)； 銀扣授權，提出後取消，隔日再提出，需再重新檢核
//   保單角色： 4.借款人/7.轉帳委託人(BY 與借款人關係)	
//
// 回應訊息(檢核狀態)：
//   0.非可疑名單/已完成名單確認 ==> OK
//	 1. 需審查/確認  ==> E022
//   2.為凍結名單/未確定名單 ==> E022

		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號 ：03+戶號(7)+扣款銀行(3)+扣款帳號(14)
		checkAmlVo.setTransactionId("03-" + parse.IntegerToString(tAchAuthLog.getCustNo(), 7) + "-"
				+ tAchAuthLog.getRepayBank() + "-" + tAchAuthLog.getRepayAcct() + "-" + titaVo.getCalDy());
		checkAmlVo.setRefNo(parse.IntegerToString(tAchAuthLog.getCustNo(), 7)); // 相關編號
		checkAmlVo.setAcctNo(tAchAuthLog.getCustNo() + "-" + tAchAuthLog.getFacmNo()); // 放款案號：戶號-額度
		checkAmlVo.setCaseNo("AUTH"); // 案號：AUTH
		// 保單角色： 4.借款人/7.轉帳委託人
		if ("00".equals(tAchAuthLog.getRelationCode())) {
			checkAmlVo.setRoleId("4");
			custNo = tAchAuthLog.getCustNo();
			tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
			custMainMove(tCustMain, checkAmlVo, titaVo);
		} else {
			checkAmlVo.setRoleId("7");
			checkAmlVo.setName(tAchAuthLog.getRelAcctName()); // 姓名：戶名
			checkAmlVo.setCustKey(tAchAuthLog.getRelationId());// 身份證ID/居留證號碼
			// 身份別 1:自然人 2:法人 性別 : M/F;若是法人填空格(space)
			if ("M".equals(tAchAuthLog.getRelAcctGender())) {
				checkAmlVo.setIdentityCd("1");
				checkAmlVo.setSex("M");
			} else if ("F".equals(tAchAuthLog.getRelAcctGender())) {
				checkAmlVo.setIdentityCd("1");
				checkAmlVo.setSex("F");
			} else {
				checkAmlVo.setIdentityCd("2");
				checkAmlVo.setSex("");
			}
			// 個人出生日
			if (tAchAuthLog.getRelAcctBirthday() > 0)
				checkAmlVo.setBirthEstDt("0" + parse.IntegerToString(tAchAuthLog.getRelAcctBirthday(), 7));
			else
				checkAmlVo.setBirthEstDt("");
		}
		// AML檢核
		checkAmlVo = amlLogCheck(checkAmlVo, titaVo);
		// AML檢核回應訊息
		amlCheckRsp(checkAmlVo, titaVo);

		return checkAmlVo;

	}

	/**
	 * 郵局銀扣授權
	 * 
	 * @param tPostAuthLog PostAuthLog
	 * @param titaVo       TitaVo
	 * @return CheckAmlVo
	 * @throws LogicException LogicException
	 */
	public CheckAmlVo authPost(PostAuthLog tPostAuthLog, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom authPost .....");
// 輸入欄位：PostAuthLog 郵局授權記錄檔
//   RelAcctName     第三人帳戶戶名
///  RelationId      第三人身分證字號
//   RelAcctGender   M/F/space => 自然人 2:法人 性別 : M/F;若是法人填空格(space)
//	 RelAcctBirthday 第三人出生日期                                          
//
// 設定欄位：
//   AML@交易序號： 03+戶號(7)+郵局帳戶別(1)+扣款帳號(14)+交易日期(8)； 銀扣授權，提出後取消，隔日再提出，需再重新檢核
//   保單角色： 4.借款人/7.轉帳委託人(BY 與借款人關係)	
//
// 回應訊息(檢核狀態)：
//   0.非可疑名單/已完成名單確認 ==> OK
//	 1. 需審查/確認  ==> E022
//   2.為凍結名單/未確定名單 ==> E022
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號： 03+戶號(7)+郵局帳戶別(1)+扣款帳號(14)
		checkAmlVo.setTransactionId("03-" + parse.IntegerToString(tPostAuthLog.getCustNo(), 7) + "-"
				+ tPostAuthLog.getPostDepCode() + "-" + tPostAuthLog.getRepayAcct() + "-" + titaVo.getCalDy());
		checkAmlVo.setUnit("10HC00"); // 查詢單位：10HC00
		checkAmlVo.setAcceptanceUnit(""); // 代辦單位：space
		checkAmlVo.setRefNo(parse.IntegerToString(tPostAuthLog.getCustNo(), 7)); // 相關編號
		checkAmlVo.setAcctNo(tPostAuthLog.getCustNo() + "-" + tPostAuthLog.getFacmNo()); // 放款案號：戶號-額度
		checkAmlVo.setCaseNo("AUTH"); // 案號：AUTH
		// 保單角色： 4.借款人/7.轉帳委託人
		if ("00".equals(tPostAuthLog.getRelationCode())) {
			checkAmlVo.setRoleId("4");
			custNo = tPostAuthLog.getCustNo();
			tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
			custMainMove(tCustMain, checkAmlVo, titaVo);
		} else {
			checkAmlVo.setRoleId("7");
			checkAmlVo.setName(tPostAuthLog.getRelAcctName()); // 姓名：戶名
			checkAmlVo.setCustKey(tPostAuthLog.getRelationId());// 身份證ID/居留證號碼
			// 性別 : M/F;若是法人填空格(space)
			if ("M".equals(tPostAuthLog.getRelAcctGender())) {
				checkAmlVo.setIdentityCd("1");
				checkAmlVo.setSex("M");
			} else if ("F".equals(tPostAuthLog.getRelAcctGender())) {
				checkAmlVo.setIdentityCd("1");
				checkAmlVo.setSex("F");
			} else {
				checkAmlVo.setIdentityCd("2");
				checkAmlVo.setSex("");
			}
			// 個人出生日
			if (tPostAuthLog.getRelAcctBirthday() > 0)
				checkAmlVo.setBirthEstDt("0" + parse.IntegerToString(tPostAuthLog.getRelAcctBirthday(), 7));
			else
				checkAmlVo.setBirthEstDt("");
		}
		// AML檢核
		checkAmlVo = amlLogCheck(checkAmlVo, titaVo);
		// AML檢核回應訊息
		amlCheckRsp(checkAmlVo, titaVo);

		return checkAmlVo;
	}

	/**
	 * 銀行扣款
	 * 
	 * @param tBankDeductDtl 銀行扣款檔
	 * @param titaVo         TitaVo
	 * @return CheckAmlVo
	 * @throws LogicException LogicException
	 */
	public CheckAmlVo deduct(BankDeductDtl tBankDeductDtl, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom deduct .....");
// 輸入欄位：PostAuthLog 銀行扣款檔
//   RelAcctName     第三人帳戶戶名
//   RelCustId       第三人身分證字號
//   RelAcctGender   M/F/space => 自然人 2:法人 性別 : M/F;若是法人填空格(space)
//	 RelAcctBirthday 第三人出生日期                                          
//
// 設定欄位：
//   AML@交易序號： 03+戶號(7)+郵局帳戶別(1)+扣款帳號(14)
//   保單角色： 4.借款人/7.轉帳委託人(BY 與借款人關係)	
//
// 回應訊息：整批作業自行檢核AML回應訊息
//   ConfirmStatus	檢核狀態
//		0.非可疑名單/已完成名單確認
//		1.需審查/確認
//		2.為凍結名單/未確定名單
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號： 03+戶號(7)+扣款銀行(3)+郵局帳戶別(1)+扣款帳號(14)
		checkAmlVo.setTransactionId("03" + tBankDeductDtl.getCustNo() + tBankDeductDtl.getRepayBank()
				+ tBankDeductDtl.getPostCode() + tBankDeductDtl.getRepayAcctNo());
		checkAmlVo.setRefNo(parse.IntegerToString(tBankDeductDtl.getCustNo(), 7)); // 相關編號
		checkAmlVo.setAcctNo(tBankDeductDtl.getCustNo() + "-" + tBankDeductDtl.getFacmNo()); // 放款案號：戶號-額度
		checkAmlVo.setCaseNo("DEDUCT"); // 案號：DEDUCT
		// 保單角色： 4.借款人/7.轉帳委託人
		if ("00".equals(tBankDeductDtl.getRelationCode()) || tBankDeductDtl.getRelationCode().isEmpty()) {
			checkAmlVo.setRoleId("4");
			custNo = tBankDeductDtl.getCustNo();
			tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
			custMainMove(tCustMain, checkAmlVo, titaVo);
		} else {
			checkAmlVo.setRoleId("7");
			checkAmlVo.setName(tBankDeductDtl.getRelCustName()); // 姓名：戶名
			checkAmlVo.setCustKey(tBankDeductDtl.getRelCustId());// 身份證ID/居留證號碼
			// 身份別 1:自然人 2:法人 性別 : M/F;若是法人填空格(space)
			if ("1".equals(tBankDeductDtl.getRelAcctGender())) {
				checkAmlVo.setIdentityCd("1");
				checkAmlVo.setSex("M");
			} else if ("2".equals(tBankDeductDtl.getRelAcctGender())) {
				checkAmlVo.setIdentityCd("1");
				checkAmlVo.setSex("F");
			} else {
				checkAmlVo.setIdentityCd("2");
				checkAmlVo.setSex("");
			}
			// 個人出生日
			if (tBankDeductDtl.getRelAcctBirthday() > 0)
				checkAmlVo.setBirthEstDt("0" + parse.IntegerToString(tBankDeductDtl.getRelAcctBirthday(), 7));
			else
				checkAmlVo.setBirthEstDt("");
			// 身份證ID/居留證號碼
			if (tBankDeductDtl.getRelCustId() != null)
				checkAmlVo.setCustKey(tBankDeductDtl.getRelCustId());
			else
				checkAmlVo.setCustKey("");
		}
		// AML檢核
		checkAmlVo = amlLogCheck(checkAmlVo, titaVo);

		// 整批作業自行檢核AML回應訊息

		return checkAmlVo;
	}

	/*----------  ----------*/
	private void custMainMove(CustMain tCustMain, CheckAmlVo checkAmlVo, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom custMain .....");
		if (tCustMain == null)
			tCustMain = new CustMain();

		// 姓名：戶名
		if (tCustMain.getCustName() != null)
			checkAmlVo.setName(tCustMain.getCustName());
		checkAmlVo.setEnglishName(""); // 英文姓名
		// 身份證ID/居留證號碼
		if (tCustMain.getCustId() != null)
			checkAmlVo.setCustKey(tCustMain.getCustId());
		else
			checkAmlVo.setCustKey("");
		// 身份別 1:自然人 2:法人
		// EntCode 企金別 0:個金 1:企金 2:企金自然人""
		if (tCustMain.getEntCode() != null && "1".equals(tCustMain.getEntCode()))
			checkAmlVo.setIdentityCd("2");
		else
			checkAmlVo.setIdentityCd("1");
		// 國籍/註冊地(登記地)國籍
		if (tCustMain.getNationalityCode() != null)
			checkAmlVo.setNationalCd(tCustMain.getNationalityCode());
		else
			checkAmlVo.setNationalCd("");
		checkAmlVo.setBirthNationCd(""); // 營業地國籍/居住地國籍
		// 性別 : M/F;若是法人填空格(space)
		// Sex 性別 1:男性 2:女性
		if (tCustMain.getSex() == null)
			checkAmlVo.setSex("");
		else if ("1".equals(tCustMain.getSex()))
			checkAmlVo.setSex("M");
		else if ("2".equals(tCustMain.getSex()))
			checkAmlVo.setSex("F");
		else
			checkAmlVo.setSex("");
		// 個人出生日
		if (tCustMain.getBirthday() > 0 && !"1".equals(tCustMain.getEntCode()))
			checkAmlVo.setBirthEstDt("0" + parse.IntegerToString(tCustMain.getBirthday(), 7));
		else
			checkAmlVo.setBirthEstDt("");

	}

	/**
	 * 整批檢核
	 * 
	 * @param tTempVo     jsonformat處理說明
	 * @param tBatxDetail 整批入帳明細檔
	 * @param titaVo      TitaVo
	 * @return tTempVo
	 * @throws LogicException LogicException
	 */
	public TempVo batxCheck(TempVo tTempVo, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom batxCheck .....");
// 輸入欄位：BatxDetail 整批入帳明細檔
//
// 設定欄位：
//   AML@交易序號
//      匯款轉帳： 03+批號(6)+明細序號(6)+檢查類別(1)-1.借款人 
//                                                   -2.交易人
//      支票兌現： 03+批號(6)+戶號(7)+支票帳號(9)+支票號碼(7))+檢查類別(1)-1.借款人
//                                                   -2.發票人
//   交易人/發票人
//      保單角色
//        交易人
//	   	    0001 現金存入   -->10.現金繳款人
//		    0002 現金存沖  -->10.現金繳款人
//		    0087 ＡＴ存入  -->10.現金繳款人
//		    0071 匯入匯款   -->11.匯款人
//		    else         -->7.轉帳委託人
//	           發票人
//		    7.轉帳委託人
//      身份別
//          預設值：  1:自然人
//      性別
//          預設值： 空白		
//
// 回應訊息：整批作業自行檢核AML回應訊息
//    AmlRsp1 1.借款人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
//    AmlRsp2 2.交易人、發票人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單

		// AML@交易序號
		String transactionId = null;
		// 匯款轉帳： 03+批號(6)+明細序號(6)+檢查類別(1)-1.借款人 2.交易人
		// 支票兌現： 03+批號(6)+戶號(7)+支票帳號(9)+支票號碼(7))+檢查類別(1)-1.借款人 2.發票人

		if (tBatxDetail.getRepayCode() == 1) {
			transactionId = "03-" + tBatxDetail.getBatchNo() + "-"
					+ parse.IntegerToString(tBatxDetail.getDetailSeq(), 6);
		} else if (tBatxDetail.getRepayCode() == 4) {
			transactionId = "03-" + tBatxDetail.getBatchNo() + "-" + parse.IntegerToString(tBatxDetail.getCustNo(), 7)
					+ "-" + tBatxDetail.getRvNo().substring(0, 9) + "-" + tBatxDetail.getRvNo().substring(10, 17);
		}

		// 1.借款人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		if (tTempVo.get("AmlRsp1") != null && transactionId != null) {
			CheckAmlVo checkAmlVo = new CheckAmlVo();
			checkAmlVo.setTransactionId(transactionId + "-1");
			// AML檢核
			checkAmlVo = amlLogCheck(checkAmlVo, titaVo);
			tTempVo.put("AmlRsp1", checkAmlVo.getConfirmStatus());
		}

		// 2.交易人、發票人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		if (tTempVo.get("AmlRsp2") != null && transactionId != null) {
			if (transactionId != null) {
				CheckAmlVo checkAmlVo = new CheckAmlVo();
				checkAmlVo.setTransactionId(transactionId + "-2");
				// AML檢核
				checkAmlVo = amlLogCheck(checkAmlVo, titaVo);
				tTempVo.put("AmlRsp2", checkAmlVo.getConfirmStatus()); // 檢核狀態
			}
		}
		// 更新匯款轉帳檔 AML回應碼
		if (tBatxDetail.getRepayCode() == 1) {
			updateBankRmtf(tBatxDetail.getAcDate(), tBatxDetail.getBatchNo(), tBatxDetail.getDetailSeq(), tTempVo,
					titaVo);
		}

		return tTempVo;
	}

	/**
	 * 匯款轉帳
	 * 
	 * @param tTempVo   jsonformat處理說明
	 * @param tBankRmtf 匯款轉帳檔
	 * @param titaVo    TitaVo
	 * @return TempVo
	 * @throws LogicException ...
	 */
	public TempVo bankRmtf(TempVo tTempVo, BankRmtf tBankRmtf, TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom bankRmtf .....");
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號 03+[批號(6)+明細序號(6)]+檢查類別(1)-1.借款人 2.交易人
		String transactionId = tBankRmtf.getBatchNo() + "-" + parse.IntegerToString(tBankRmtf.getDetailSeq(), 6);
		// 1.借款人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		String custName = "";
		if (tBankRmtf.getCustNo() > 0) {
			tCustMain = custMainService.custNoFirst(tBankRmtf.getCustNo(), tBankRmtf.getCustNo(), titaVo);
			if (tCustMain != null) {
				checkAmlVo = customerCheck(tBankRmtf.getCustNo(), transactionId, tBankRmtf.getBatchNo(), tCustMain,
						titaVo);
				tTempVo.put("AmlRsp1", checkAmlVo.getConfirmStatus());
				custName = tCustMain.getCustName();
			}
		}
		// 2.交易人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		String traderName = "";
		String str = tBankRmtf.getTraderInfo();
		// 交易人資料有值、非數字(非借款人時)
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				if (!parse.isNumeric(str.substring(i, i + 1)))
					traderName = traderName + str.substring(i, i + 1);
			}
			traderName = traderName.trim();
		}
		this.info("TxAmlCom bankRmtf .....");
		if (traderName != null & traderName.length() > 0 && !traderName.equals(custName)) {
			checkAmlVo = traderCheck(tBankRmtf, transactionId, traderName, titaVo);
			tTempVo.put("AmlRsp2", checkAmlVo.getConfirmStatus());
		}
		// 更新匯款轉帳檔 AML回應碼
		updateBankRmtf(tBankRmtf.getAcDate(), tBankRmtf.getBatchNo(), tBankRmtf.getDetailSeq(), tTempVo, titaVo);

		return tTempVo;
	}

	/**
	 * 支票兌現
	 * 
	 * @param tTempVo     jsonformat處理說明
	 * @param tLoanCheque 支票檔
	 * @param batchNo     整批批號
	 * @param titaVo      TitaVo
	 * @return TempVo
	 * @throws LogicException LogicException
	 */
	public TempVo batxCheque(TempVo tTempVo, LoanCheque tLoanCheque, String batchNo, TitaVo titaVo)
			throws LogicException {
		this.info("TxAmlCom batxCheque .....");
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號 03+[批號(6)+戶號(7)+支票帳號(9)+支票號碼(7)]+檢查類別(1)-1.借款人 2.交易人/發票人
		String transactionId = batchNo + "-" + parse.IntegerToString(tLoanCheque.getCustNo(), 7) + "-"
				+ parse.IntegerToString(tLoanCheque.getChequeAcct(), 9) + "-"
				+ parse.IntegerToString(tLoanCheque.getChequeNo(), 7);
		// 1.借款人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		String custName = "";
		tCustMain = custMainService.custNoFirst(tLoanCheque.getCustNo(), tLoanCheque.getCustNo(), titaVo);
		if (tCustMain != null) {
			checkAmlVo = customerCheck(tLoanCheque.getCustNo(), transactionId, batchNo, tCustMain, titaVo);
			tTempVo.put("AmlRsp1", checkAmlVo.getConfirmStatus());
			custName = tCustMain.getCustName();
		}
		// 2.發票人, AML回應碼 : 0.非可疑名單/已完成名單確認 1.需審查/確認 2.為凍結名單/未確定名單
		if (tLoanCheque.getChequeName() != null & !tLoanCheque.getChequeName().equals(custName)) {
			checkAmlVo = ChequerCheck(tLoanCheque, transactionId, batchNo, titaVo);
			tTempVo.put("AmlRsp2", checkAmlVo.getConfirmStatus());
		}
		return tTempVo;
	}

	/*---------- 1.借款人 ----------*/
	private CheckAmlVo customerCheck(int custNo, String transactionId, String batchNo, CustMain tCustMain,
			TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom bankRmtf .....");
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號 03+transactionI+檢查類別(1)-1.借款人 2.交易人
		checkAmlVo.setTransactionId("03-" + transactionId + "-1");
		checkAmlVo.setRoleId("4"); // 保單角色： 4.借款人
		checkAmlVo.setRefNo(parse.IntegerToString(custNo, 7)); // 相關編號
		checkAmlVo.setAcctNo(parse.IntegerToString(custNo, 7)); // 放款案號：戶號
		checkAmlVo.setCaseNo(batchNo); // 案號：整批批號 'BATXnn'
		custMainMove(tCustMain, checkAmlVo, titaVo);
		// AML檢核
		checkAmlVo = amlLogCheck(checkAmlVo, titaVo);

		return checkAmlVo;
	}

	/*---------- 2.發票人 ----------*/
	private CheckAmlVo ChequerCheck(LoanCheque tLoanCheque, String transactionId, String batchNo, TitaVo titaVo)
			throws LogicException {
		this.info("TxAmlCom bankRmtf .....");
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號 03+transactionId+檢查類別(1)-1.借款人 2.交易人/發票人
		checkAmlVo.setTransactionId("03-" + transactionId + "-2");
		checkAmlVo.setRoleId("7"); // 保單角色 7.轉帳委託人
		checkAmlVo.setRefNo(parse.IntegerToString(tLoanCheque.getCustNo(), 7)); // 相關編號
		checkAmlVo.setAcctNo(parse.IntegerToString(tLoanCheque.getCustNo(), 7)); // 放款案號：戶號
		checkAmlVo.setCaseNo(batchNo); // 案號：整批批號 'BATXnn'
		checkAmlVo.setName(tLoanCheque.getChequeName()); // 姓名
		// default 身份別 1:自然人 2:法人 性別 : 空白
		checkAmlVo.setIdentityCd("1");
		checkAmlVo.setSex(" ");
		// AML檢核
		checkAmlVo = amlLogCheck(checkAmlVo, titaVo);

		return checkAmlVo;

	}

	/*---------- 2.交易人 ----------*/
	private CheckAmlVo traderCheck(BankRmtf tBankRmtf, String transactionId, String traderName, TitaVo titaVo)
			throws LogicException {
		this.info("TxAmlCom bankRmtf .....");
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		// AML@交易序號 03+批號(6)+明細序號(6)+檢查類別(1)-1.借款人 2.交易人/發票人
		checkAmlVo.setTransactionId("03-" + transactionId + "-2");
		// 保單角色
//		0001 現金存入   -->10.現金繳款人
//		0002 現金存沖  -->10.現金繳款人
//		0087 ＡＴ存入  -->10.現金繳款人
//		0071 匯入匯款   -->11.匯款人
//		else         -->7.轉帳委託人
		if ("0001".equals(tBankRmtf.getDscptCode()) || "0002".equals(tBankRmtf.getDscptCode())
				|| "0087".equals(tBankRmtf.getDscptCode()))
			checkAmlVo.setRoleId("10");
		else if ("0087".equals(tBankRmtf.getDscptCode()))
			checkAmlVo.setRoleId("11");
		else
			checkAmlVo.setRoleId("7");
		checkAmlVo.setRefNo(parse.IntegerToString(tBankRmtf.getCustNo(), 7)); // 相關編號
		checkAmlVo.setAcctNo(parse.IntegerToString(tBankRmtf.getCustNo(), 7)); // 放款案號：戶號
		checkAmlVo.setCaseNo(tBankRmtf.getBatchNo()); // 案號：整批批號 'BATXnn'
		checkAmlVo.setName(traderName); // 姓名
		// default 身份別 1:自然人 2:法人 性別 : 空白
		checkAmlVo.setIdentityCd("1");
		checkAmlVo.setSex(" ");
		// AML檢核
		checkAmlVo = amlLogCheck(checkAmlVo, titaVo);

		return checkAmlVo;

	}

	/* 檢查AML紀錄檔 */
	private CheckAmlVo amlLogCheck(CheckAmlVo checkAmlVo, TitaVo titaVo) throws LogicException {

		this.info("TxAmlLog amlLogCheck, Entdy=" + titaVo.getEntDyI() + ", TransactionId="
				+ checkAmlVo.getTransactionId());
		TxAmlLog tTxAmlLog = txAmlLogService.findByTransactionIdFirst(titaVo.getEntDyI() + 19110000,
				checkAmlVo.getTransactionId(), titaVo);
		String orgName = "";
		if (tTxAmlLog != null) {
			Document doc = checkAml.convertStringToXml(tTxAmlLog.getMsgRg());
			orgName = checkAml.getXmlValue(doc, "Name");
		}

		// AML紀錄檔存在，檢核狀態 = 1.需審查/確認 => AML姓名再檢核
		if (tTxAmlLog != null) {
			if ("1".equals(tTxAmlLog.getConfirmStatus()) || !orgName.equals(checkAmlVo.getName())) {
				checkAmlVo.setLogNo(tTxAmlLog.getLogNo()); // LogNo
				checkAmlVo.setEntdy(titaVo.getEntDyI()); // 帳務日
				checkAmlVo.setBrNo(titaVo.getKinbr()); // 單位
				checkAmlVo.setUnit("10HC00"); // 查詢單位：10HC00
				checkAmlVo.setAcceptanceUnit(""); // 代辦單位：space
				checkAmlVo.setNotifyEmail(empEmail(titaVo)); // EMAIL
				checkAmlVo.setQueryId(titaVo.getTlrNo()); // 查詢者ID
				checkAml.setTxBuffer(this.getTxBuffer());
				if ("".equals(checkAmlVo.getName()))
					checkAmlVo = checkAml.refreshStatus(tTxAmlLog.getLogNo(), titaVo); // 更新指定序號檢核狀態
				else
					checkAmlVo = checkAml.reCheckName(tTxAmlLog.getLogNo(), checkAmlVo, titaVo); // AML姓名再檢核
			} else {
				// 回應紀錄檔狀態
				checkAmlVo.setLogNo(tTxAmlLog.getLogNo());
				checkAmlVo.setStatus(tTxAmlLog.getStatus());
				checkAmlVo.setStatusCode(tTxAmlLog.getStatusCode());
				checkAmlVo.setStatusDesc(tTxAmlLog.getStatusDesc());
				checkAmlVo.setIsSimilar(tTxAmlLog.getIsSimilar());
				checkAmlVo.setIsSan(tTxAmlLog.getIsSan());
				checkAmlVo.setIsBanNation(tTxAmlLog.getIsBanNation());
				checkAmlVo.setConfirmStatus(tTxAmlLog.getConfirmStatus());
			}
		}
		// AML紀錄檔不存在， AML姓名檢核
		else {
			checkAmlVo.setEntdy(titaVo.getEntDyI()); // 帳務日
			checkAmlVo.setBrNo(titaVo.getKinbr()); // 單位
			checkAmlVo.setUnit("10HC00"); // 查詢單位：10HC00
			checkAmlVo.setAcceptanceUnit(""); // 代辦單位：space
			checkAmlVo.setNotifyEmail(empEmail(titaVo)); // EMAIL
			checkAmlVo.setQueryId(titaVo.getTlrNo()); // 查詢者ID
			// AML姓名檢核
			checkAml.setTxBuffer(this.getTxBuffer());
			checkAmlVo = checkAml.checkName(checkAmlVo, titaVo);
		}

		return checkAmlVo;
	}

	// 更新匯款轉帳檔 AML回應碼
	private void updateBankRmtf(int acDate, String batchNo, int DetailSeq, TempVo tempVo, TitaVo titaVo)
			throws LogicException {
		String amlRsp = "0";
		if ("1".equals(tempVo.get("AmlRsp1")) || "1".equals(tempVo.get("AmlRsp2")) || "2".equals(tempVo.get("AmlRsp1"))
				|| "2".equals(tempVo.get("AmlRsp2"))) {
			if ("2".equals(tempVo.get("AmlRsp1")) || "2".equals(tempVo.get("AmlRsp2"))) {
				amlRsp = "2";
			} else if ("1".equals(tempVo.get("AmlRsp1")) || "1".equals(tempVo.get("AmlRsp2"))) {
				amlRsp = "1";
			}
		}
		BankRmtf tBankRmtf = bankRmtfService.holdById(new BankRmtfId(acDate + 19110000, batchNo, DetailSeq), titaVo);
		if (tBankRmtf != null && !tBankRmtf.getAmlRsp().equals(amlRsp)) {
			tBankRmtf.setAmlRsp(amlRsp);
			try {
				bankRmtfService.update(tBankRmtf);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "update BankRmtf " + e.getErrorMsg());
			}
		}
	}

	/* AML檢核回應訊息 */
	private void amlCheckRsp(CheckAmlVo checkAmlVo, TitaVo titaVo) throws LogicException {
		// 訂正交易，不回應錯誤訊息
		if (!titaVo.isHcodeErase() && !"0".equals(checkAmlVo.getConfirmStatus())) {
			if ("1".equals(checkAmlVo.getConfirmStatus()))
				throw new LogicException(titaVo, "E0022", checkAmlVo.getName() + ", " + checkAmlVo.getStatusDesc()); // 該筆資料需進行AML審查/確認
			else
				throw new LogicException(titaVo, "E0023", checkAmlVo.getName() + ", " + checkAmlVo.getStatusDesc()); // AML凍結名單/未確定名單
		}
	}

	/*---------- 經辦 email ----------*/
	private String empEmail(TitaVo titaVo) throws LogicException {
		this.info("TxAmlCom gettingNotifyEmail.....");
		String empEmail = "";
		CdEmp tCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);
		if (tCdEmp != null && tCdEmp.getEmail() != null) {
			empEmail = tCdEmp.getEmail();
		}

		return empEmail;

	}
}