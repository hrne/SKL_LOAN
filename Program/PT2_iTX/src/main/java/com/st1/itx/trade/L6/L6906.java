package com.st1.itx.trade.L6;

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
import com.st1.itx.db.service.springjpa.cm.L6906ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6906") // 會計分錄查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6906 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	L6906ServiceImpl l6906ServiceImpl;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6906 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 461 * 100 = 46,100

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			resultList = l6906ServiceImpl.findAll(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		List<LinkedHashMap<String, String>> chkOccursList = null;
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", result.get("CustNo"));
				occursList.putParam("OOFacmNo", result.get("FacmNo"));
				occursList.putParam("OOBormNo", result.get("BormNo"));
				occursList.putParam("OOAcNoCode", result.get("AcNoCode"));
				occursList.putParam("OOAcSubCode", result.get("AcSubCode"));
				occursList.putParam("OOAcDtlCode", result.get("AcDtlCode"));
				occursList.putParam("OOTitaBatchNo", result.get("TitaBatchNo"));
				occursList.putParam("OOSlipNote", result.get("SlipNote"));
				occursList.putParam("OOTitaTxCd", result.get("TitaTxCd"));
				occursList.putParam("OOTranItem", result.get("TranItem"));

				occursList.putParam("OOTitaTlrNo", result.get("TitaTlrNo"));
				occursList.putParam("OOTlrItem", result.get("TlrName"));
				occursList.putParam("OOTitaTxtNo", result.get("TitaTxtNo"));

				occursList.putParam("OOTitaSupNo", result.get("TitaSupNo"));
				occursList.putParam("OOSupItem", result.get("SupName"));

				occursList.putParam("OODbAmt", result.get("DbAmt"));
				occursList.putParam("OOCrAmt", result.get("CrAmt"));
				occursList.putParam("OOAcNoItem", result.get("AcNoItem"));
				String DateTime = parse.stringToStringDateTime(result.get("LastUpdate"));
				occursList.putParam("OOLastUpdate", DateTime);

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			chkOccursList = this.totaVo.getOccursList();

			if (l6906ServiceImpl.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}

		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException("E0001", "會計帳務明細檔");
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

}