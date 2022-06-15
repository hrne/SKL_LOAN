package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.service.springjpa.cm.L6908ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6908") // 銷帳歷史明細查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6908 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public TxTranCodeService sTxTranCodeService;
	@Autowired
	public AcReceivableService sAcReceivableService;
	@Autowired
	public LoanBorTxService sLoanBorTxService;
	@Autowired
	Parse parse;

	@Autowired
	public L6908ServiceImpl l6908ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6908 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 316 * 100 = 31,600

		List<Map<String, String>> L6908List = null;

		try {
			L6908List = l6908ServiceImpl.FindAll(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		if (L6908List == null || L6908List.size() == 0) {
			// 查無資料 拋錯
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔");
		}

		if (L6908List != null) {

			for (Map<String, String> t : L6908List) {
				OccursList occursList = new OccursList();
				occursList.putParam("OORvNo", t.get("RvNo"));
				occursList.putParam("OORvAmt", t.get("RvAmt"));
				occursList.putParam("OOTitaTlrNo", t.get("TitaTlrNo"));
				occursList.putParam("OOTitaTxtNo", t.get("TitaTxtNo"));
				occursList.putParam("OOTranItem", t.get("TranItem"));
				occursList.putParam("OOTitaTxCd", t.get("TitaTxCd"));
				occursList.putParam("OOSlipNote", t.get("SlipNote"));
				int acdate = parse.stringToInteger(t.get("AcDate"));
				int entrydate = parse.stringToInteger(t.get("EntryDate"));
				int txtNo = parse.stringToInteger(t.get("TitaTxtNo"));
				LoanBorTx tLoanBorTx = sLoanBorTxService.borxTxtNoFirst(acdate, t.get("TitaTlrNo"),
						parse.IntegerToString(txtNo, 8), titaVo);
				if (tLoanBorTx != null) {
					entrydate = tLoanBorTx.getEntryDate();
				}
				if (acdate >= 19110000) {
					acdate = acdate - 19110000;
				}

				if (entrydate >= 19110000) {
					entrydate = entrydate - 19110000;
				}

				occursList.putParam("OOAcDate", acdate);
				occursList.putParam("OOClsFlag", t.get("ClsFlag"));
				occursList.putParam("OOEntryDate", entrydate);

				occursList.putParam("OOCreateDate", parse.stringToStringDate(t.get("CreateDate")).replace("/", ""));
				occursList.putParam("OOCreateTime", parse.stringToStringTime(t.get("CreateDate")));

				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}