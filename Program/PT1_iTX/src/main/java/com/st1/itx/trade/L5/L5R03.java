package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.Map;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
/*DB服務*/
import com.st1.itx.util.common.NegCom;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegMainService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R03")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R03 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegTransService sNegTransService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public NegCom sNegCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R03 ");
		this.totaVo.init(titaVo);
		// 用於L5702試算用 得到NegTrans 試算Main結果
		String RimAcDate = titaVo.getParam("RimAcDate").trim();// 會計日期NegTrans-Key
		String RimTitaTlrNo = titaVo.getParam("RimTitaTlrNo").trim();// 經辦NegTrans-Key
		String RimTitaTxtNo = titaVo.getParam("RimTitaTxtNo").trim();// 交易序號NegTrans-Key
		String RimTrialFunc = titaVo.getParam("RimTrialFunc").trim();// 測試是否為一開始的,還是後面修改後的調Rim 0:一開始試算 1:異動後 試算 2:UPDATE
		String RimTxKind = titaVo.getParam("RimTxKind").trim();// 交易別

		// 1.得到NegTrans資料取用NegMains資料
		NegTransId NegTransIdVO = new NegTransId();
		int AcDate = parse.stringToInteger(RimAcDate);
		NegTransIdVO.setAcDate(AcDate);
		NegTransIdVO.setTitaTlrNo(RimTitaTlrNo);
		NegTransIdVO.setTitaTxtNo(parse.stringToInteger(RimTitaTxtNo));
		this.info("L5R03 NegTransIdVO AcDate=[" + AcDate + "],TitaTlrNo=[" + RimTitaTlrNo + "],TitaTxtNo=[" + RimTitaTxtNo + "]");

		NegTrans NegTransVO = new NegTrans();
		NegTransVO = sNegTransService.findById(NegTransIdVO);
		if (NegTransVO != null) {
			// 2.得到Main的資料
			// 3.單筆試算

			Map<String, String> Map = sNegCom.trialNegtrans(NegTransVO, RimTrialFunc, RimTxKind, titaVo);
			// 4.丟資料回去

			// 資料
			// RIM對應名稱,Map對應名稱,中文名稱,特殊規則
			String Data[][] = sNegCom.getData();
			if (Data != null && Data.length != 0) {

			} else {
				throw new LogicException(titaVo, "E0001", "未從NegCom得到相關資料");
			}

			for (int i = 0; i < Data.length; i++) {
				String RimName = Data[i][0];// RIM對應名稱
				String MapName = Data[i][1];// Map對應名稱
				String NameCh = Data[i][2];// 中文名稱
				String NameSpRule = Data[i][3];// 特殊規則

				String MapValue = Map.get(MapName);
				String MapValueSet = "";
				if (MapValue != null && MapValue.trim().length() != 0) {
					MapValueSet = MapValue;
					switch (NameSpRule) {
					case "DateRocToAc":
						// 拿到Roc的資料轉成Ac
						int IntMapValueSet = Integer.parseInt(MapValueSet);
						if (IntMapValueSet > 0 && MapValueSet.length() == 7) {
							IntMapValueSet = IntMapValueSet + 19110000;
							MapValueSet = String.valueOf(IntMapValueSet);
						}
						break;
					default:
					}
				} else {
					this.info("L5R03 RimName=[" + RimName + "],MapName=[" + MapName + "],NameCh=[" + NameCh + "] 無資料");
				}

				totaVo.putParam(RimName, MapValueSet);
			}
			/*
			 * totaVo.putParam("L5r03CustId",Map.get("CustId"));
			 * totaVo.putParam("L5r03CaseSeq",Map.get("CaseSeq"));
			 * totaVo.putParam("L5r03CustNo",Map.get("CustNo"));
			 * totaVo.putParam("L5r03CustName",Map.get("CustName"));
			 * totaVo.putParam("L5r03Status",Map.get("Status"));
			 * totaVo.putParam("L5r03CustLoanKind",Map.get("CustLoanKind"));
			 * totaVo.putParam("L5r03ApplDate",Map.get("ApplDate"));
			 * totaVo.putParam("L5r03MainDueAmt",Map.get("MainDueAmt"));
			 * totaVo.putParam("L5r03TotalPeriod",Map.get("TotalPeriod"));
			 * totaVo.putParam("L5r03IntRate",Map.get("IntRate"));
			 * 
			 * totaVo.putParam("L5r03RepaidPeriod",Map.get("RepaidPeriod"));//已繳期數
			 * totaVo.putParam("L5r03NewRepaidPeriod",Map.get("NewRepaidPeriod"));
			 * 
			 * totaVo.putParam("L5r03IsMainFin",Map.get("IsMainFin"));
			 * totaVo.putParam("L5r03MainFinCode",Map.get("MainFinCode"));
			 * 
			 * totaVo.putParam("L5r03OrgPrincipalBal",Map.get("OrgPrincipalBal"));//總本金餘額
			 * totaVo.putParam("L5r03NewPrincipalBal",Map.get("NewPrincipalBal"));
			 * 
			 * totaVo.putParam("L5r03OrgAccuTempAmt",Map.get("OrgAccuTempAmt"));//累暫收金額
			 * totaVo.putParam("L5r03NewAccuTempAmt",Map.get("NewAccuTempAmt"));//
			 * 
			 * totaVo.putParam("L5r03OrgAccuOverAmt",Map.get("OrgAccuOverAmt"));//累溢收金額
			 * totaVo.putParam("L5r03NewAccuOverAmt",Map.get("NewAccuOverAmt"));
			 * 
			 * totaVo.putParam("L5r03OrgAccuDueAmt",Map.get("OrgAccuDueAmt"));//
			 * totaVo.putParam("L5r03NewAccuDueAmt",Map.get("NewAccuDueAmt"));
			 * 
			 * totaVo.putParam("L5r03OrgAccuSklShareAmt",Map.get("OrgAccuSklShareAmt"));//
			 * 新壽攤分
			 * totaVo.putParam("L5r03NewAccuSklShareAmt",Map.get("NewAccuSklShareAmt"));
			 * 
			 * totaVo.putParam("L5r03OrgNextPayDate",Map.get("OrgNextPayDate"));
			 * 
			 * totaVo.putParam("L5r03OrgRepayPrincipal",Map.get("OrgRepayPrincipal"));//還本本金
			 * totaVo.putParam("L5r03NewRepayPrincipal",Map.get("NewRepayPrincipal"));
			 * 
			 * totaVo.putParam("L5r03OrgRepayInterest",Map.get("OrgRepayInterest"));//還本利息
			 * totaVo.putParam("L5r03NewRepayInterest",Map.get("NewRepayInterest"));
			 * 
			 * totaVo.putParam("L5r03TwoStepCode",Map.get("TwoStepCode"));
			 * totaVo.putParam("L5r03ChgCondDate",Map.get("ChgCondDate"));
			 * totaVo.putParam("L5r03PayIntDate",Map.get("PayIntDate"));
			 * totaVo.putParam("L5r03OrgStatusDate",Map.get("OrgStatusDate"));//戶況日期
			 * totaVo.putParam("L5r03TransAcDate",Map.get("TransAcDate"));
			 * totaVo.putParam("L5r03TransTitaTlrNo",Map.get("TransTitaTlrNo"));
			 * totaVo.putParam("L5r03TransTitaTxtNo",Map.get("TransTitaTxtNo"));
			 * totaVo.putParam("L5r03TransCustNo",Map.get("TransCustNo"));
			 * totaVo.putParam("L5r03TransCaseSeq",Map.get("TransCaseSeq"));
			 * totaVo.putParam("L5r03TransEntryDate",Map.get("TransEntryDate"));//入賬日期
			 * 
			 * totaVo.putParam("L5r03TransTxStatus",Map.get("TransTxStatus"));//交易狀態
			 * totaVo.putParam("L5r03NewTransTxStatus",Map.get("NewTransTxStatus"));
			 * 
			 * totaVo.putParam("L5r03TransTxKind",Map.get("TransTxKind"));//交易別
			 * totaVo.putParam("L5r03NewTransTxKind",Map.get("NewTransTxKind"));
			 * 
			 * totaVo.putParam("L5r03TransTxAmt",Map.get("TransTxAmt"));//交易金額
			 * totaVo.putParam("L5r03NewTransTxAmt",Map.get("NewTransTxAmt"));
			 * 
			 * totaVo.putParam("L5r03TransPrincipalBal",Map.get("TransPrincipalBal"));//本金餘額
			 * totaVo.putParam("L5r03NewTransPrincipalBal",Map.get("NewTransPrincipalBal"));
			 * 
			 * totaVo.putParam("L5r03TransReturnAmt",Map.get("TransReturnAmt"));//退還金額
			 * totaVo.putParam("L5r03NewTransReturnAmt",Map.get("NewTransReturnAmt"));
			 * 
			 * totaVo.putParam("L5r03TransSklShareAmt",Map.get("TransSklShareAmt"));//新壽攤分
			 * totaVo.putParam("L5r03NewTransSklShareAmt",Map.get("NewTransSklShareAmt"));
			 * 
			 * totaVo.putParam("L5r03TransApprAmt",Map.get("TransApprAmt"));//撥付金額
			 * totaVo.putParam("L5r03NewTransApprAmt",Map.get("NewTransApprAmt"));
			 * 
			 * totaVo.putParam("L5r03TransExportDate",Map.get("TransExportDate"));
			 * totaVo.putParam("L5r03TransExportAcDate",Map.get("TransExportAcDate"));
			 * 
			 * totaVo.putParam("L5r03TransTempRepayAmt",Map.get("TransTempRepayAmt"));//
			 * 暫收抵繳金額
			 * totaVo.putParam("L5r03NewTransTempRepayAmt",Map.get("NewTransTempRepayAmt"));
			 * 
			 * totaVo.putParam("L5r03TransOverRepayAmt",Map.get("TransOverRepayAmt"));//
			 * 溢收抵繳金額
			 * totaVo.putParam("L5r03NewTransOverRepayAmt",Map.get("NewTransOverRepayAmt"));
			 * 
			 * totaVo.putParam("L5r03TransPrincipalAmt",Map.get("TransPrincipalAmt"));//本金金額
			 * totaVo.putParam("L5r03NewTransPrincipalAmt",Map.get("NewTransPrincipalAmt"));
			 * 
			 * totaVo.putParam("L5r03TransInterestAmt",Map.get("TransInterestAmt"));//利息金額
			 * totaVo.putParam("L5r03NewTransInterestAmt",Map.get("NewTransInterestAmt"));
			 * 
			 * totaVo.putParam("L5r03TransOverAmt",Map.get("TransOverAmt"));//轉入溢收金額
			 * totaVo.putParam("L5r03NewTransOverAmt",Map.get("NewTransOverAmt"));
			 * 
			 * totaVo.putParam("L5r03TransIntStartDate",Map.get("TransIntStartDate"));
			 * totaVo.putParam("L5r03TransIntEndDate",Map.get("TransIntEndDate"));
			 * 
			 * totaVo.putParam("L5r03TransRepayPeriod",Map.get("TransRepayPeriod"));//還款期數
			 * totaVo.putParam("L5r03NewTransRepayPeriod",Map.get("NewTransRepayPeriod"));
			 * 
			 * totaVo.putParam("L5r03TransShouldPayPeriod",Map.get("TransShouldPayPeriod"));
			 * totaVo.putParam("L5r03TransDueAmt",Map.get("TransDueAmt")); int
			 * TransRepayDate=0; String StrTransRepayDate=Map.get("TransRepayDate").trim();
			 * if(StrTransRepayDate!=null && StrTransRepayDate.length()!=0 &&
			 * StrTransRepayDate.length()==8) {
			 * TransRepayDate=Integer.parseInt(StrTransRepayDate)-19110000; }
			 * totaVo.putParam("L5r03TransRepayDate",TransRepayDate);
			 * totaVo.putParam("L5r03TransOrgAccuOverAmt",Map.get("TransOrgAccuOverAmt"));
			 * totaVo.putParam("L5r03TransAccuOverAmt",Map.get("TransAccuOverAmt")); //新資料
			 */
		} else {
			// E0001 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "債務協商交易檔");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}