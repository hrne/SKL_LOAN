package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.service.springjpa.cm.L4943ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2061")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2061 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	L4943ServiceImpl l4943ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2061 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 37 * 500 = 18500

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 已入帳註記 0:未入帳,1:已入帳
		int ClsFlag = 0;

		// new ArrayList
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

		Slice<AcReceivable> slAcReceivable = acReceivableService.useL2062Eq("F29", iCustNo, iFacmNo, iFacmNo, ClsFlag, ClsFlag, this.index, this.limit, titaVo);
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcReceivable != null && slAcReceivable.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		// 查無資料
		if (lAcReceivable == null) {
			throw new LogicException(titaVo, "E2028", "L2061(貸後契變手續費檔)"); // 查無資料
		}

		for (AcReceivable tmpAcReceivable : lAcReceivable) {
			// new occurs
			OccursList occursList = new OccursList();
			TempVo tTempVo = new TempVo();

			tTempVo = tTempVo.getVo(tmpAcReceivable.getJsonFields());

			this.info("tTempVo = " + tTempVo);

			int contractChgCode = parse.stringToInteger(tTempVo.getParam("ContractChgCode"));
			occursList.putParam("OOAcDate", tmpAcReceivable.getLastAcDate());
			occursList.putParam("OOContractChgDate", tmpAcReceivable.getOpenAcDate());
			occursList.putParam("OOCustNo", tmpAcReceivable.getCustNo());
			occursList.putParam("OOFacmNo", tmpAcReceivable.getFacmNo());
			occursList.putParam("OOContractChgNo", tmpAcReceivable.getRvNo().substring(8));
			occursList.putParam("OOContractChgCode", contractChgCode);
			occursList.putParam("OOCurrencyCode", tmpAcReceivable.getCurrencyCode());
			occursList.putParam("OOFeeAmt", tmpAcReceivable.getRvAmt());
			
			// L4943 需要的欄位
			titaVo.putParam("CustNo", iCustNo);
			titaVo.putParam("EntryDateFm", tmpAcReceivable.getLastAcDate());
			titaVo.putParam("EntryDateTo", tmpAcReceivable.getLastAcDate());
			titaVo.putParam("FunctionCode", 1);
			
			titaVo.putParam("BankCode", "");
			titaVo.putParam("OpItem", 0);
			titaVo.putParam("RepayType", 0);
			titaVo.putParam("PostLimitAmt", 0);
			titaVo.putParam("SingleLimit", 0);
			titaVo.putParam("LowLimit", 0);
			
			List<Map<String, String>> l4943Vo = null;
			
			try {
				l4943Vo = l4943ServiceImpl.findAll(0, titaVo);
			} catch (Exception e) {
				this.error("L2061 Exception when L4943ServiceImpl: " + e.getMessage());
				throw new LogicException("E0013", "L4943ServiceImpl");
			}
			
			occursList.putParam("OOHasL4943", l4943Vo != null && !l4943Vo.isEmpty() ? "Y" : "N");

			this.info("occursList L2061" + occursList);
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}