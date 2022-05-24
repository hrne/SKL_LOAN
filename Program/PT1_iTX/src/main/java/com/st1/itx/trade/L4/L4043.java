package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L4043ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * SearchFlag=9,1<br>
 * DateFrom=9,7<br>
 * DateTo=9,7<br>
 * CustNo=9,7<br>
 * RepayAcct=9,14<br>
 * END=X,1<br>
 */

@Service("L4043")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4043 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L4043ServiceImpl l4043ServiceImpl;
	
	@Autowired 
	public CdEmpService sCdEmpService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4043 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l4043ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (resultList != null && resultList.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				int authCreateDate = parse.stringToInteger(result.get("F11"));
				int propDate = parse.stringToInteger(result.get("F12")); // 提出
				int retrDate = parse.stringToInteger(result.get("F13")); // 提回
				int stampFinishDate = parse.stringToInteger(result.get("F19"));
				int stampCancelDate = parse.stringToInteger(result.get("F29"));
				int deleteDate = parse.stringToInteger(result.get("F20"));
				String wkCreateFlag = result.get("F10");
				String wkPostMediaCode = result.get("F16"); // 媒體檔
				String createempno = result.get("F23");
				String lastupdateempno = result.get("F25");
				int creatdate = Integer.parseInt(result.get("F24"));
				int lastupdate = Integer.parseInt(result.get("F26"));
				if (wkPostMediaCode == null) {
					wkPostMediaCode = "";
				}
				String wkRetFlag = ""; // 是否顯示修改按鈕

				if (authCreateDate > 19110000) {
					authCreateDate = authCreateDate - 19110000;
				}
				if (propDate > 19110000) {
					propDate = propDate - 19110000;
				}
				if (retrDate > 19110000) {
					retrDate = retrDate - 19110000;
				}
				if (stampFinishDate > 19110000) {
					stampFinishDate = stampFinishDate - 19110000;
				}
				if (stampCancelDate > 19110000) {
					stampCancelDate = stampCancelDate - 19110000;
				}

				if (deleteDate > 19110000) {
					deleteDate = deleteDate - 19110000;
				}
				
				if (creatdate > 19110000) {
					creatdate = creatdate - 19110000;
				}
				if (lastupdate > 19110000) {
					lastupdate = lastupdate - 19110000;
				}
				if (deleteDate > 0) {
//					1申請 2終止 9暫停
					if ("00".equals(result.get("F15"))) {
						wkCreateFlag = "9";
					} else {
						wkCreateFlag = "2";
					}
				}
//				申請代號為申請			
				if ("1".equals(wkCreateFlag)) {
					// 授權成功時可修改
					if ("00".equals(result.get("F15").trim())) {
						wkRetFlag = "Y";
					}
					// 未授權未產生媒體檔可修改
					if ("".equals(wkPostMediaCode)) {
						wkRetFlag = "Y";
					}

				}
				// 暫停授權可修改
				if (deleteDate > 0) {
					wkRetFlag = "Y";
				}

				occursList.putParam("OOCustNo", result.get("F0"));
				occursList.putParam("OOFacmNo", result.get("F1"));
				occursList.putParam("OOAuthType", result.get("F2"));
//				occursList.putParam("OORepayBank", result.get("F3"));
				occursList.putParam("OOPostDepCode", result.get("F4"));
				occursList.putParam("OORepayAcct", result.get("F5"));
				occursList.putParam("OOStatus", result.get("F6"));
				occursList.putParam("OOLimitAmt", result.get("F7"));
				occursList.putParam("OOAcctSeq", result.get("F8"));
				occursList.putParam("OOCustId", result.get("F9"));
				occursList.putParam("OOCreateFlag", wkCreateFlag);
				occursList.putParam("OOAuthCreateDate", authCreateDate);
				occursList.putParam("OOPropDate", propDate);
				occursList.putParam("OORetrDate", retrDate);
				occursList.putParam("OOStampCode", result.get("F14"));
				occursList.putParam("OOAuthErrorCode", result.get("F15"));
				occursList.putParam("OOPostMediaCode", result.get("F16"));
				occursList.putParam("OOAmlRsp", result.get("F17"));
				occursList.putParam("OORepayAcctLog", result.get("F18"));
				occursList.putParam("OOStampFinishDate", stampFinishDate);
				occursList.putParam("OODeleteDate", deleteDate);
//				原為歷程於L4042僅最新一筆需有按鈕控制用，現無作用
				occursList.putParam("OOButtenFlagA", result.get("F21"));
				occursList.putParam("OORetFlag", wkRetFlag);
				occursList.putParam("OOTitaTxCd", result.get("F22"));
				// 判斷是否有歷程
				occursList.putParam("OOHistory", result.get("F28"));
				
				createempno = findCdEmp(createempno,titaVo);
				lastupdateempno = findCdEmp(lastupdateempno,titaVo);
				occursList.putParam("OOCreateEmpNo", createempno);
				occursList.putParam("OOCreareDate", creatdate);
				occursList.putParam("OOLastUpdateEmpNo", lastupdateempno);
				occursList.putParam("OOLastUpdate", lastupdate);
				occursList.putParam("OOStampCancelDate", stampCancelDate);
				
				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l4043ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}
	
	private String findCdEmp(String iEmpno, TitaVo titaVo) throws LogicException{
		String fullname="";
		
		CdEmp tCdEmp = sCdEmpService.findById(iEmpno, titaVo);
		
		if(tCdEmp!=null) {
			fullname = tCdEmp.getFullname();
		}
		
		return fullname;
	}
}