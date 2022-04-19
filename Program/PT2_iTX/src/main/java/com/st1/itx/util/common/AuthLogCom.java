package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 查詢帳號明細內容 <BR>
 * 1.依據戶號額度查詢帳號檔，再根據帳號查詢Log檔，取得明細 <BR>
 * 2. call by LXXXX<BR>
 * 
 * @author st1
 *
 */
@Component("authLogCom")
@Scope("prototype")
public class AuthLogCom extends TradeBuffer {

	private TitaVo titaVo;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BankAuthActService bankAuthActService;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public PostAuthLogService postAuthLogService;

	private int iCustNo = 0;
	private int iFacmNo = 0;
	private TempVo tempVo = new TempVo();
	private String mainStatus = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.titaVo = titaVo;
		this.totaVo.init(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 
	 * @param custNo 戶號數字
	 * @param facmNo 額度數字
	 * @param titaVo titaVo
	 * @return RepayBank 扣款銀行 <br>
	 *         RepayAcctNo 扣款帳號 <br>
	 *         PostCode 郵局存款別 <br>
	 *         RelationCode 與借款人關係 <br>
	 *         RelationName 第三人帳戶戶名 <br>
	 *         RelationId 第三人身份證字號 <br>
	 *         RelationBirthday 第三人出生日期 <br>
	 *         RelationGender 第三人性別 <br>
	 *         AchAuthCode ACH-授權方式 <br>
	 *         AuthStatus LOG-授權狀態 <br>
	 *         MainStatus MAIN-授權方式 <br>
	 * @throws LogicException LogicException
	 */
	public TempVo exec(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		this.info("AuthLogCom...");
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;

//		1.tita CustNo, FacmNo
//		2.BankAuthAct RepayBank (if AuthType !=00 , PostAuthLog , AchAuthLog)
//		3.getValue

		iCustNo = custNo;
		iFacmNo = facmNo;

		tempVo = new TempVo();

		Slice<BankAuthAct> sBankAuthAct = null;
		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		sBankAuthAct = bankAuthActService.facmNoEq(iCustNo, iFacmNo, this.index, this.limit, titaVo);
		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct tBankAuthAct : lBankAuthAct) {
				this.info("ALL RepayBank : " + tBankAuthAct.getRepayBank());

				if (tBankAuthAct.getStatus() != null && "".equals(tBankAuthAct.getStatus().trim())) {
					mainStatus = "N";
				} else {
					mainStatus = tBankAuthAct.getStatus();
				}
				this.info("mainStatus : " + mainStatus);

				if ("700".equals(tBankAuthAct.getRepayBank())) {
					this.info("PostTempVo...");
					setPostTempVo(tBankAuthAct, titaVo);
				} else if (!"".equals(tBankAuthAct.getRepayBank())) {
					this.info("AchTempVo...");
					setAchTempVo(tBankAuthAct, titaVo);
					break;
				} else {
//					throw new LogicException(titaVo, "E0001", "查無資料");
				}
			}
		} else {
//			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.info("ALL tempVo : " + tempVo.toString());
		return tempVo;
	}

	private void setPostTempVo(BankAuthAct tBankAuthAct, TitaVo titaVo) throws LogicException {

		PostAuthLog tPostAuthLog = new PostAuthLog();
		tPostAuthLog = postAuthLogService.repayAcctFirst(tBankAuthAct.getCustNo(), tBankAuthAct.getPostDepCode(), tBankAuthAct.getRepayAcct(), "1", titaVo);

		if (tPostAuthLog != null) {
			this.info("Post tPostAuthLog : " + tPostAuthLog.toString());

			int cnt = 0;
			this.info("Post AuthErrorCode() : " + tPostAuthLog.getAuthErrorCode());

//			if (!"00".equals(tPostAuthLog.getAuthErrorCode())) {
//				return;
//			}
			cnt++;

//					RepayBank        扣款銀行
//					RepayAcctNo      扣款帳號
//					PostCode         郵局存款別
//					RelationCode     與借款人關係
//					RelationName     第三人帳戶戶名
//					RelationId       第三人身份證字號
//					RelationBirthday 第三人出生日期
//					RelationGender   第三人性別
//					AchAuthCode      ACH-授權方式
//					AuthStatus       授權狀態 1申請2終止

			this.info("AuthLogCom.RepayAcct : " + tPostAuthLog.getRepayAcct());

			tempVo.put("RepayBank", "700");
			tempVo.put("RepayAcctNo", tPostAuthLog.getRepayAcct());
			tempVo.put("PostCode", tPostAuthLog.getPostDepCode());
			tempVo.put("RelationCode", tPostAuthLog.getRelationCode());
			tempVo.put("RelationName", tPostAuthLog.getRelAcctName());
			tempVo.put("RelationId", tPostAuthLog.getRelationId());
			tempVo.put("RelationBirthday", "" + tPostAuthLog.getRelAcctBirthday());
			tempVo.put("RelationGender", tPostAuthLog.getRelAcctGender());
			tempVo.put("AchAuthCode", "");
			tempVo.put("AuthStatus", tPostAuthLog.getAuthApplCode());
			tempVo.put("MainStatus", mainStatus);

			if (cnt == 0) {
//				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		} else {
			this.info("tPostAuthLog null... ");
		}
		this.info("Post tempVo : " + tempVo.toString());
	}

	private void setAchTempVo(BankAuthAct tBankAuthAct, TitaVo titaVo) throws LogicException {
		AchAuthLog tAchAuthLog = new AchAuthLog();

		tAchAuthLog = achAuthLogService.repayAcctFirst(tBankAuthAct.getCustNo(), tBankAuthAct.getRepayBank(), tBankAuthAct.getRepayAcct(), titaVo);

		if (tAchAuthLog != null) {
			this.info("ACH tAchAuthLog : " + tAchAuthLog.toString());

			int cnt = 0;
			this.info("ACH AuthStatus() : " + tAchAuthLog.getAuthStatus());

//			if (!"0".equals(tAchAuthLog.getAuthStatus())) {
//				return;
//			}
			cnt++;

//					RepayBank        扣款銀行
//					RepayAcctNo      扣款帳號
//					PostCode         郵局存款別
//					RelationCode     與借款人關係
//					RelationName     第三人帳戶戶名
//					RelationId       第三人身份證字號
//					RelationBirthday 第三人出生日期
//					RelationGender   第三人性別
//					AchAuthCode      ACH-授權方式
//					AuthStatus       授權狀態 A:新增D:取消

			this.info("AuthLogCom.RepayBank : " + tAchAuthLog.getRepayBank());

			tempVo.put("RepayBank", tAchAuthLog.getRepayBank());
			tempVo.put("RepayAcctNo", tAchAuthLog.getRepayAcct());
			tempVo.put("PostCode", "");
			tempVo.put("RelationCode", tAchAuthLog.getRelationCode());
			tempVo.put("RelationName", tAchAuthLog.getRelAcctName());
			tempVo.put("RelationId", tAchAuthLog.getRelationId());
			tempVo.put("RelationBirthday", "" + tAchAuthLog.getRelAcctBirthday());
			tempVo.put("RelationGender", tAchAuthLog.getRelAcctGender());
			tempVo.put("AchAuthCode", tAchAuthLog.getAuthMeth());
			tempVo.put("AuthStatus", tAchAuthLog.getCreateFlag());
			tempVo.put("MainStatus", mainStatus);
			if (cnt == 0) {
//				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		} else {
			this.info("tAchAuthLog null... ");
		}
		this.info("ACH tempVo : " + tempVo.toString());
	}
}
