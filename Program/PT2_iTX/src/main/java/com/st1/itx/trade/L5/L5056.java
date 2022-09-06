package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.springjpa.cm.L5056ServiceImpl;

@Service("L5056")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5056 extends TradeBuffer {

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	private L5056ServiceImpl l5056ServiceImpl;

	@Autowired
	InsuRenewService sInsuRenewService;

	@Autowired
	InsuOrignalService sInsuOrignalService;

	@Autowired
	TxDataLogService sTxDataLogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5056 ");
		this.totaVo.init(titaVo);

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// this.limit=Integer.MAX_VALUE;//查全部
		this.limit = 50;// 查全部

		List<Map<String, String>> L5056List = null;

		try {
			L5056List = l5056ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5056 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		if (L5056List == null || L5056List.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");
		}

		for (Map<String, String> d : L5056List) {
			OccursList occursList = new OccursList();

			occursList.putParam("OOLogNo", d.get("LogNo"));
			occursList.putParam("OOCustNo", d.get("CustNo"));
			occursList.putParam("OOFacmNo", d.get("FacmNo"));
			occursList.putParam("OOBormNo", d.get("BormNo"));
			occursList.putParam("OOCustName", d.get("CustName"));

			occursList.putParam("OOWorkMonth", d.get("YM"));

			BigDecimal amt = new BigDecimal(d.get("SumAmt"));
			BigDecimal cnt = new BigDecimal(d.get("SumCnt"));
			if (amt.compareTo(BigDecimal.ZERO) == 0 && cnt.compareTo(BigDecimal.ZERO) == 0) {
				occursList.putParam("OOCanDelete", 0);
			} else {
				occursList.putParam("OOCanDelete", 1);
			}

			occursList.putParam("OOCreateEmp", d.get("CreateEmpNo") + " " + d.get("CreateEmpName"));
			occursList.putParam("OOCreateDate", parse.stringToStringDateTime(d.get("CreateDate")));
			occursList.putParam("OOLastUpdateEmp", d.get("LastUpdateEmpNo") + " " + d.get("LastUpdateEmpName"));
			occursList.putParam("OOLastUpdate", parse.stringToStringDateTime(d.get("LastUpdate")));

			// 若有歷程就顯示，無則不顯示
			Slice<TxDataLog> slTxDataLog = sTxDataLogService.findByTranNo("L5506",
					FormatUtil.pad9(d.get("CustNo"), 7) + "-" + FormatUtil.pad9(d.get("FacmNo"), 3) + "-" + FormatUtil.pad9(d.get("BormNo"), 3), 0, 1, titaVo);
			List<TxDataLog> lTxDataLog = slTxDataLog != null ? slTxDataLog.getContent() : null;
			occursList.putParam("OOHasHistory", lTxDataLog != null && !lTxDataLog.isEmpty() ? "Y" : "N");
			this.totaVo.addOccursList(occursList);
		}

		if (L5056List != null && L5056List.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}