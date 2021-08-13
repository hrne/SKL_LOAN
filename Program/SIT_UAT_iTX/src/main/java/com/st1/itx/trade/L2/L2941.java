package com.st1.itx.trade.L2;

import java.math.BigDecimal;
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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUSTNO=9,7<br>
 * END=X,1<br>
 */

@Service("L2941")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */

public class L2941 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2941.class);

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2941 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 50 * 500 = 25000

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		String overdueCode = "";
		// new ArrayList
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();
		// new table
		ForeclosureFee tForeclosureFee = new ForeclosureFee();
		// 測試該戶號是否存在法拍費用檔
		Slice<ForeclosureFee> slForeclosureFee = sForeclosureFeeService.custNoEq(iCustNo, this.index, this.limit,
				titaVo);
		lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slForeclosureFee != null && slForeclosureFee.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		if (lForeclosureFee == null) {
			throw new LogicException("E0001", "L2941該戶號在法拍費用檔無資料");
		}
		int filecnt = lForeclosureFee.size();
		int custNo = 0;
		BigDecimal fee = BigDecimal.ZERO;
		BigDecimal fee1 = BigDecimal.ZERO;
		String crdr = "";
		for (ForeclosureFee tmpForeclosureFee : lForeclosureFee) {
			// new occurs
			OccursList occurslist = new OccursList();
			// 取法拍費總計 同一戶號則累計
			if (tmpForeclosureFee.getOverdueDate() != 0) {
				fee1 = fee1.add(tmpForeclosureFee.getFee());
			} else {
				fee = fee.add(tmpForeclosureFee.getFee());
			}
			// 催收註記
			if (tmpForeclosureFee.getOverdueDate() != 0) {
				overdueCode = "Y";
			} else {
				overdueCode = "";
			}
			// 取筆數如法拍費為0則不記筆數
			if (tmpForeclosureFee.getFee().compareTo(BigDecimal.ZERO) == 0) {
				filecnt = filecnt - 1;
			}
			// 借貸 銷號有值為貸,無值為借
			if (tmpForeclosureFee.getCloseDate() != 0) {

				crdr = "貸";
			} else {
				crdr = "借";
			}

			this.totaVo.putParam("OFeeAmt", fee);
			this.totaVo.putParam("OOverdueLawFee", fee1);
			this.totaVo.putParam("OFileCnt", filecnt);

			occurslist.putParam("OOReceiveDate", tmpForeclosureFee.getReceiveDate());
			occurslist.putParam("OOFee", tmpForeclosureFee.getFee());
			occurslist.putParam("OOFeeCode", tmpForeclosureFee.getFeeCode());
			// 借貸
			occurslist.putParam("OODrCr", crdr);
			occurslist.putParam("OOCaseCode", tmpForeclosureFee.getCaseCode());
			// 入帳日期 =會計日期
			occurslist.putParam("OOEntryDate", tmpForeclosureFee.getDocDate());
			occurslist.putParam("OOCloseDate", tmpForeclosureFee.getCloseDate());
			// 催收註記
			occurslist.putParam("OOOverdue", overdueCode);
			occurslist.putParam("OORecordNo", tmpForeclosureFee.getRecordNo());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}