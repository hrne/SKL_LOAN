package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.springjpa.cm.L5975ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FinCode=9,3<br>
 */

@Service("L5977")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5977 extends TradeBuffer {
	/* DB服務注入 */

	@Autowired
	L5975ServiceImpl l5975ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public CustMainService sCustMainService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public NegCom sNegCom;

	@Autowired
	public NegAppr01Service sNegAppr01Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5977");
		this.info("active L5977 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("CustId");
		int iCaseSeq = parse.stringToInteger(titaVo.getParam("CaseSeq"));
		int iCustNo = 0;
		String iCustName="";
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;// 查全部

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		if(tCustMain!=null) {
			iCustNo = tCustMain.getCustNo();
			iCustName = tCustMain.getCustName();
		} else {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔");
		}
		
		List<Map<String, String>> listL5977 = null;
		
			try {
				if(iCaseSeq==0) {
					listL5977 = l5975ServiceImpl.findbyCustNo(iCustNo, titaVo, this.index, this.limit);
				} else {
					listL5977 = l5975ServiceImpl.findbyCustNoCaseSeq(iCustNo, iCaseSeq, titaVo, this.index, this.limit);
				}
				

			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L5975ServiceImpl.findAll error = " + errors.toString());
			}
	

		if (listL5977 != null && listL5977.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		if (listL5977 == null || listL5977.size() == 0) {
			throw new LogicException(titaVo, "E0001", "最大債權撥付統計查詢");

		} else {
			
			for (Map<String, String> t5977 : listL5977) {

				OccursList occursList = new OccursList();
				occursList.putParam("OOFinCode", t5977.get("F0"));
				String MainFinCodeName = sNegCom.FindNegFinAcc(t5977.get("F0"), titaVo)[0];
				occursList.putParam("OOFinCodeName", MainFinCodeName);
				occursList.putParam("OOAmt", t5977.get("F1"));
				occursList.putParam("OOCnt", t5977.get("F2"));
				occursList.putParam("OOCustName", iCustName);
				this.totaVo.addOccursList(occursList);

			} // for

		} // else

		this.addList(this.totaVo);
		return this.sendList();
	}

}