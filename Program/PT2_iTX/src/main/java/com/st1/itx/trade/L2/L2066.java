package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.springjpa.cm.L4943ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2066")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2066 extends TradeBuffer {

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
		this.info("active L2066 ");
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

		// new ArrayList
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

		Slice<AcReceivable> slAcReceivable = acReceivableService.useL2062Eq("F10", iCustNo, iFacmNo,
				iFacmNo == 0 ? 999 : iFacmNo, 0, 1, this.index, this.limit, titaVo);
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcReceivable != null && slAcReceivable.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		// 查無資料
		if (lAcReceivable == null) {
			throw new LogicException(titaVo, "E2003", "此戶號額度無帳管費資料"); // 查無資料
		}

		this.totaVo.putParam("OCustNo", iCustNo);
		this.totaVo.putParam("OFacmNo", iFacmNo);
		for (AcReceivable tmpAcReceivable : lAcReceivable) {
			// new occurs
			OccursList occursList = new OccursList();

			occursList.putParam("OOCustNo", tmpAcReceivable.getCustNo());
			occursList.putParam("OOFacmNo", tmpAcReceivable.getFacmNo());
			occursList.putParam("OORvNo", tmpAcReceivable.getRvNo());
			occursList.putParam("OOOpenAcDate", tmpAcReceivable.getOpenAcDate());
			occursList.putParam("OOCurrencyCode", tmpAcReceivable.getCurrencyCode());
			occursList.putParam("OOAcctFee", tmpAcReceivable.getRvAmt());
			occursList.putParam("OOSlipNote", tmpAcReceivable.getSlipNote());
			occursList.putParam("OOCls", tmpAcReceivable.getClsFlag());
			occursList.putParam("OOClsX", tmpAcReceivable.getClsFlag() == 1 ? "已銷帳" : "未銷帳");

			this.info("occursList L2066" + occursList);
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}