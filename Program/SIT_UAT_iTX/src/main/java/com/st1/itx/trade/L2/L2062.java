package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUSTNO=9,7<br>
 * CUSTNO1=9,3<br>
 * END=X,1<br>
 */

@Service("L2062")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2062 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2062.class);

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2062 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 26 * 500 = 13000

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 銷帳註記
		int ClsFlagSt = 0;
		int ClsFlagEd = 1;

		// new ArrayList
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

		Slice<AcReceivable> slAcReceivable = acReceivableService.useL2062Eq("F29", iCustNo, iFacmNo, iFacmNo, ClsFlagSt,
				ClsFlagEd, this.index, this.limit,titaVo);

		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcReceivable != null && slAcReceivable.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		// 查無資料拋錯
		if (lAcReceivable == null) {
			throw new LogicException(titaVo, "E2028", "L2062(貸後契變手續費檔)"); // 查無資料
		}

		for (AcReceivable tmpAcReceivable : lAcReceivable) {
			// new occurs
			OccursList occursList = new OccursList();
			TempVo tTempVo = new TempVo();

			tTempVo = tTempVo.getVo(tmpAcReceivable.getJsonFields());

			this.info("tTempVo = " + tTempVo);

			int contractChgCode = parse.stringToInteger(tTempVo.getParam("ContractChgCode"));

			String entryfg = "";
			if (tmpAcReceivable.getClsFlag() == 1) {
				entryfg = "A";
			}

			occursList.putParam("OOCustNo", tmpAcReceivable.getCustNo());
			occursList.putParam("OOFacmNo", tmpAcReceivable.getFacmNo());
			occursList.putParam("OOContractChgDate", tmpAcReceivable.getOpenAcDate());
			occursList.putParam("OOContractChgNo", tmpAcReceivable.getRvNo().substring(8));
			occursList.putParam("OOContractChgCode", contractChgCode);
			occursList.putParam("OOCurrencyCode", tmpAcReceivable.getCurrencyCode());
			occursList.putParam("OOFeeAmt", tmpAcReceivable.getRvAmt());
			occursList.putParam("OOEntryFG", entryfg);
			occursList.putParam("OOPrintCode", tTempVo.getParam("PrintCode"));
			this.info("occursList L2062" + occursList);
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}