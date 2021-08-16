package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.springjpa.cm.L5R15ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R15")
@Scope("prototype")
/**
 * 催收主檔上方共用區內容使用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R15 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CollListService sCollListService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public L5R15ServiceImpl iL5R15ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R15 ");
		this.info("L5R15 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("RimFacmNo"));
		String iL5R15Sql = "";
		List<Map<String, String>> i5R15SqlReturn = new ArrayList<Map<String, String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			iL5R15Sql = iL5R15ServiceImpl.FindL5R15(iCustNo, iFacmNo);
		} catch (Exception e) {
			// E5003 組建SQL語法發生問題
			this.info("L5R15 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5003", "");
		}
		try {
			i5R15SqlReturn = iL5R15ServiceImpl.FindData(iL5R15Sql, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5R15 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (i5R15SqlReturn.isEmpty()) {
			totaVo.putParam("L5R15CustName", "");
			totaVo.putParam("L5R15AccCollPsn", "");
			totaVo.putParam("L5R15LegalPsn", "");
			totaVo.putParam("L5R15AccCollPsnX", "");
			totaVo.putParam("L5R15LegalPsnX", "");
			totaVo.putParam("L5R15DrawDownDate", 0);
			totaVo.putParam("L5R15LoanAmt", "");
			totaVo.putParam("L5R15PrevIntDate", 0);
			totaVo.putParam("L5R15LoanBal", "");
			totaVo.putParam("L5R15NextIntDate", 0);
			totaVo.putParam("L5R15CustUKey", "");
			totaVo.putParam("L5R15ClNo", "");
			totaVo.putParam("L5R15ClCode1", "");
			totaVo.putParam("L5R15ClCode2", "");
		} else {
			for (Map<String, String> r5015SqlReturn : i5R15SqlReturn) {
				// 戶號名稱
				if (r5015SqlReturn.get("F8").equals("")) {
					totaVo.putParam("L5R15CustName", "");
				} else {
					totaVo.putParam("L5R15CustName", r5015SqlReturn.get("F8"));
				}
				// 催收人員
				totaVo.putParam("L5R15AccCollPsn", r5015SqlReturn.get("F0"));
				// 法務人員
				totaVo.putParam("L5R15LegalPsn", r5015SqlReturn.get("F1"));
				// //催收人員姓名
				totaVo.putParam("L5R15AccCollPsnX", r5015SqlReturn.get("F12"));
				// //法務人員姓名
				totaVo.putParam("L5R15LegalPsnX", r5015SqlReturn.get("F13"));
				// //撥款日期
				if (r5015SqlReturn.get("F10").equals("0") || r5015SqlReturn.get("F10").equals("")) {
					totaVo.putParam("L5R15DrawDownDate", 0);
				} else {
					totaVo.putParam("L5R15DrawDownDate", Integer.valueOf(r5015SqlReturn.get("F10")) - 19110000);
				}
				// //撥款金額
				totaVo.putParam("L5R15LoanAmt", r5015SqlReturn.get("F11"));
				// 繳息迄日 (沒繳息迄日的放撥款日期 )
				if (r5015SqlReturn.get("F2").equals("0") || r5015SqlReturn.get("F2").equals("")) {
					if (r5015SqlReturn.get("F10").equals("0") || r5015SqlReturn.get("F10").equals("")) {
						totaVo.putParam("L5R15PrevIntDate", 0);
					} else {
						totaVo.putParam("L5R15PrevIntDate", Integer.valueOf(r5015SqlReturn.get("F10")) - 19110000);
					}
				} else {
					totaVo.putParam("L5R15PrevIntDate", Integer.valueOf(r5015SqlReturn.get("F2")) - 19110000);
				}
				// //放款餘額
				totaVo.putParam("L5R15LoanBal", r5015SqlReturn.get("F3"));
				// //下次應繳日
				if (r5015SqlReturn.get("F4").equals("0") || r5015SqlReturn.get("F4").equals("")) {
					totaVo.putParam("L5R15NextIntDate", 0);
				} else {
					totaVo.putParam("L5R15NextIntDate", Integer.valueOf(r5015SqlReturn.get("F4")) - 19110000);
				}
				// //客戶識別碼
				totaVo.putParam("L5R15CustUKey", r5015SqlReturn.get("F9"));
				// 擔保品編號
				totaVo.putParam("L5R15ClNo", r5015SqlReturn.get("F5"));
				// 擔保品代號1
				totaVo.putParam("L5R15ClCode1", r5015SqlReturn.get("F6"));
				// 擔保品代號2
				totaVo.putParam("L5R15ClCode2", r5015SqlReturn.get("F7"));
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}