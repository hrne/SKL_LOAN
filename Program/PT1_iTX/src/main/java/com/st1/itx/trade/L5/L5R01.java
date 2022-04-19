package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.NegFinShare;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegFinShareService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

@Service("L5R01")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R01 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public NegFinShareService sNegFinShareService;

	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public NegCom sNegCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// 查詢 L5701專用
		this.info("active L5R01 ");
		this.totaVo.init(titaVo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		String iFunctionCode = titaVo.getParam("FunctionCode").trim();// 交易代碼
		// 01 新增
		// 02 維護
		// 03 毀諾
		// 04 刪除
		// 05 查詢

		String CustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String CustNo = titaVo.getParam("RimCustNo").trim();// 戶號
		String CaseSeq = titaVo.getParam("RimCaseSeq").trim();// 案件序號

		int iCustNo = 0;
		if (CustNo != null && CustNo.length() != 0) {
			iCustNo = parse.stringToInteger(CustNo);
		}

		int iCaseSeq = 0;
		if (CaseSeq != null && CaseSeq.length() != 0) {
			iCaseSeq = parse.stringToInteger(CaseSeq);
		}
		if (("02").equals(iFunctionCode)) {
			// 只有CustId進來
			if (CustId != null && CustId.length() != 0) {
				if (iCustNo == 0 && iCaseSeq == 0) {
					// E0001 查詢資料不存在
					// throw new LogicException(titaVo, "E0001", "查無資料");
					CustMain CustMainVO = sCustMainService.custIdFirst(CustId);
					if (CustMainVO != null) {
						iCustNo = CustMainVO.getCustNo();
						NegMain NegMainVO = sNegMainService.custNoFirst(iCustNo);
						if (NegMainVO != null) {
							iCaseSeq = NegMainVO.getCaseSeq();
						}
					}

				}
			} else {
				// E0001 查詢資料不存在
				throw new LogicException(titaVo, "E0001", "[身份證字號]未填寫");
			}

		}

		// 查詢MAIN
		NegMainId NegMainIdVO = new NegMainId();
		NegMainIdVO.setCustNo(iCustNo);
		NegMainIdVO.setCaseSeq(iCaseSeq);

		NegMain NegMainVO = new NegMain();
		NegMainVO = sNegMainService.findById(NegMainIdVO);
		if (NegMainVO != null) {
			totaVo.putParam("L5r01CustId", CustId);//
			totaVo.putParam("L5r01CustNo", NegMainVO.getCustNo());//
			totaVo.putParam("L5r01CaseSeq", NegMainVO.getCaseSeq());//
			totaVo.putParam("L5r01CaseKindCode", NegMainVO.getCaseKindCode());//
			totaVo.putParam("L5r01CustLoanKind", NegMainVO.getCustLoanKind());//
			totaVo.putParam("L5r01Status", NegMainVO.getStatus());//
			if (NegMainVO.getDeferYMStart() == 0) {
				totaVo.putParam("L5r01DeferYMStart", NegMainVO.getDeferYMStart());//
			} else {
				totaVo.putParam("L5r01DeferYMStart", NegMainVO.getDeferYMStart() - 191100);//
			}

			if (NegMainVO.getDeferYMStart() == 0) {
				totaVo.putParam("L5r01DeferYMEnd", NegMainVO.getDeferYMEnd());//
			} else {
				totaVo.putParam("L5r01DeferYMEnd", NegMainVO.getDeferYMEnd() - 191100);//
			}

			totaVo.putParam("L5r01ApplDate", NegMainVO.getApplDate());//
			totaVo.putParam("L5r01DueAmt", NegMainVO.getDueAmt());//
			totaVo.putParam("L5r01TotalPeriod", NegMainVO.getTotalPeriod());//
			totaVo.putParam("L5r01IntRate", NegMainVO.getIntRate());//
			totaVo.putParam("L5r01FirstDueDate", NegMainVO.getFirstDueDate());//
			totaVo.putParam("L5r01LastDueDate", NegMainVO.getLastDueDate());//
			totaVo.putParam("L5r01IsMainFin", NegMainVO.getIsMainFin());//
			totaVo.putParam("L5r01TotalContrAmt", NegMainVO.getTotalContrAmt());//
			totaVo.putParam("L5r01MainFinCode", NegMainVO.getMainFinCode());//
			totaVo.putParam("L5r01PrincipalBal", NegMainVO.getPrincipalBal());//
			totaVo.putParam("L5r01TwoStepCode", NegMainVO.getTwoStepCode());//
			totaVo.putParam("L5r01PayerCustNo", NegMainVO.getPayerCustNo());//
			totaVo.putParam("L5r01ChgCondDate", NegMainVO.getChgCondDate());//
			totaVo.putParam("L5r01CourtCode", NegMainVO.getCourtCode());//
		} else {
			// E0001 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		// 丟TRANS檔案
		Slice<NegFinShare> slNegFinShare = sNegFinShareService.findFinCodeAll(iCustNo, iCaseSeq, this.index, this.limit);
		List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();
		int lNegFinShareS = 0;
		if (lNegFinShare != null && lNegFinShare.size() != 0) {
			lNegFinShareS = lNegFinShare.size();
			this.info("L5R01 lNegFinShareS=" + lNegFinShareS);
			for (int i = 0; i < lNegFinShareS; i++) {
				NegFinShare NegFinShareVO = lNegFinShare.get(i);
				int Row = i + 1;
				totaVo.putParam("L5r01NegFinShareFinCode" + Row + "", NegFinShareVO.getFinCode());// 債權機構
				// totaVo.putParam("L5r01NegFinShareFinName"+Row+"",sNegCom.FindCdBank(NegFinShareVO.getFinCode(),titaVo)[0]);//債權機構名稱
				totaVo.putParam("L5r01NegFinShareFinName" + Row + "", sNegCom.FindNegFinAcc(NegFinShareVO.getFinCode(), titaVo)[0]);// 債權機構名稱
				totaVo.putParam("L5r01NegFinShareContractAmt" + Row + "", NegFinShareVO.getContractAmt());// 簽約金額
				totaVo.putParam("L5r01NegFinShareAmtRatio" + Row + "", NegFinShareVO.getAmtRatio());// 債權比例%
				totaVo.putParam("L5r01NegFinShareDueAmt" + Row + "", NegFinShareVO.getDueAmt());// 期款
				totaVo.putParam("L5r01NegFinShareCancelDate" + Row + "", NegFinShareVO.getCancelDate());// 註銷日期
				totaVo.putParam("L5r01NegFinShareCancelAmt" + Row + "", NegFinShareVO.getCancelAmt());// 註銷本金
			}
		}

		for (int i = lNegFinShareS + 1; i <= 30; i++) {
			totaVo.putParam("L5r01NegFinShareFinCode" + i + "", "");// 債權機構
			totaVo.putParam("L5r01NegFinShareFinName" + i + "", "");// 債權機構名稱
			totaVo.putParam("L5r01NegFinShareContractAmt" + i + "", "");// 簽約金額
			totaVo.putParam("L5r01NegFinShareAmtRatio" + i + "", "");// 債權比例%
			totaVo.putParam("L5r01NegFinShareDueAmt" + i + "", "");// 期款
			totaVo.putParam("L5r01NegFinShareCancelDate" + i + "", "");// 註銷日期
			totaVo.putParam("L5r01NegFinShareCancelAmt" + i + "", "");// 註銷本金
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}