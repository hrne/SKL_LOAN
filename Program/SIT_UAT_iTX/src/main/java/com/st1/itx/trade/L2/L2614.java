package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.springjpa.cm.L2614ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2614")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2614 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService foreclosureFeeService;
	/* DB服務注入 */
	@Autowired
	public AcDetailService acDetailService;
	/* DB服務注入 */
	@Autowired
	public CdAcCodeService cdAcCodeService;
	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	/* DB服務注入 */
	@Autowired
	public L2614ServiceImpl sL2614ServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2614 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		try {
			// *** 折返控制相關 ***
			resultList = sL2614ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2022ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException(titaVo, "E0001", "法拍費用檔"); // 查無資料

		}
		
		
		List<LinkedHashMap<String, String>> chkOccursList = null;
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				
				occursList.putParam("OOTxtNo", result.get("TitaTxtNo")); // 交易序號
				occursList.putParam("OOSlipNo", result.get("SlipNo")); // 傳票號碼
				occursList.putParam("OOacNoCode", result.get("AcNoCode")); // 科目代號
				occursList.putParam("OOacSubCode", result.get("AcSubCode")); // 子目代號
				occursList.putParam("OOacDtlCode", result.get("AcDtlCode")); // 細目代號
				occursList.putParam("OOAcNoItem", result.get("AcNoItem")); // 科子目名稱
				occursList.putParam("OODTxAmt", result.get("DTxAmt")); // 借方金額
				occursList.putParam("OOCTxAmt", result.get("CTxAmt")); // 貸方金額
				occursList.putParam("OOCustNo", result.get("CustNo")); // 戶號
				occursList.putParam("OOCustName", result.get("CustName")); // 戶名

				this.info("occursList L2614" + occursList);
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
				
			} // for
			
			chkOccursList = this.totaVo.getOccursList();

			if (resultList.size() >= this.limit ) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}
		
		
		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException(titaVo, "E0001", "法拍費用檔"); // 查無資料
		}
		
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}