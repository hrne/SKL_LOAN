package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4940")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4940 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BankAuthActService bankAuthActService;
	@Autowired
	public PostAuthLogService postAuthLogService;
	@Autowired
	public AchAuthLogService achAuthLogService;
	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4940 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
//		String repayBank = titaVo.getParam("RepayBank");
		String repayAcct = titaVo.getParam("RepayAcct");

		// wk
		String wkCustName = "";
		String wkRelAcctName = "";

		Slice<BankAuthAct> sBankAuthAct = null;
		CustMain tCustMain = new CustMain();
		AchAuthLog tAchAuthLog = new AchAuthLog();
		PostAuthLog tPostAuthLog = new PostAuthLog();

		sBankAuthAct = bankAuthActService.authCheck(custNo, repayAcct, 0, 999, this.index, this.limit, titaVo);
		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
		if (tCustMain != null) {
			wkCustName = tCustMain.getCustName();
		}
		this.totaVo.putParam("OCustName", wkCustName);

		String wkCustNoSeq = "";
		String wksubBankAuth = "";
		String wkCustId = "";
		String wkPostDepCode = "";
		String wkCustNo = "";
		String wkAcctSeq = "";
		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct tBankAuthAct : lBankAuthAct) {
				tCustMain = new CustMain();
				tPostAuthLog = new PostAuthLog();
				tAchAuthLog = new AchAuthLog();

				OccursList occursList = new OccursList();

				occursList.putParam("OOCustNo", tBankAuthAct.getCustNo());
				occursList.putParam("OOFacmNo", tBankAuthAct.getFacmNo());
				occursList.putParam("OOAuthType", tBankAuthAct.getAuthType());
				occursList.putParam("OORepayAcct", tBankAuthAct.getRepayAcct());
				occursList.putParam("OORepayBank", tBankAuthAct.getRepayBank());
				occursList.putParam("OOStatus", tBankAuthAct.getStatus());
				occursList.putParam("OODepCode", tBankAuthAct.getPostDepCode());

				tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);

				if ("700".equals(tBankAuthAct.getRepayBank())) {
					wksubBankAuth = tBankAuthAct.getAuthType();
					if (wksubBankAuth.length() > 1) {
						wksubBankAuth = tBankAuthAct.getAuthType().substring(1, 2);
					}
					tPostAuthLog = postAuthLogService.repayAcctFirst(tBankAuthAct.getCustNo(),
							tBankAuthAct.getPostDepCode(), tBankAuthAct.getRepayAcct(), wksubBankAuth, titaVo);
					if (tPostAuthLog != null) {
						wkCustId = tPostAuthLog.getCustId();
//							本人時用戶號查客戶主檔取戶名
						if ("00".equals(tPostAuthLog.getRelationCode())) {
//								有資料時塞入 否則放空白
							if (tCustMain != null) {
								wkRelAcctName = tCustMain.getCustName();
							} else {
								wkRelAcctName = "";
							}
//								非本人時放入扣款人姓名
						} else {
							wkRelAcctName = tPostAuthLog.getRelAcctName();
						}
					}

					wkPostDepCode = tBankAuthAct.getPostDepCode();
					wkCustNo = FormatUtil.pad9("" + tBankAuthAct.getCustNo(), 7);
					wkAcctSeq = tBankAuthAct.getAcctSeq();
					wkCustNoSeq = wkCustId + wkPostDepCode + wkCustNo + wkAcctSeq;
				} else {
					tAchAuthLog = achAuthLogService.facmNoRepayAcctFirst(tBankAuthAct.getCustNo(),
							tBankAuthAct.getFacmNo(), tBankAuthAct.getRepayBank(), tBankAuthAct.getRepayAcct(), titaVo);
					if (tAchAuthLog != null) {
						if ("00".equals(tAchAuthLog.getRelationCode())) {
//								有資料時塞入 否則放空白
							if (tCustMain != null) {
								wkRelAcctName = tCustMain.getCustName();
							} else {
								wkRelAcctName = "";
							}
//								非本人時放入扣款人姓名
						} else {
							wkRelAcctName = tAchAuthLog.getRelAcctName();
						}
					}
				}
				occursList.putParam("OOAcctNoSeq", wkCustNoSeq);
				occursList.putParam("OORepayAcctName", wkRelAcctName);

				totaVo.addOccursList(occursList);

			}

		} else {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}