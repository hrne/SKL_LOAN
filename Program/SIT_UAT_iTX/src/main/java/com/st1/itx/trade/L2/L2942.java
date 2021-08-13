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
 * END=X,1<br>
 */

@Service("L2942")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2942 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2942.class);

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
		this.info("active L2942 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 57 * 500 = 28500

		// tita
		// 會計日期
		int iAccDateStart = parse.stringToInteger(titaVo.getParam("AccDateStart")) + 19110000;
		int iAccDateEnd = parse.stringToInteger(titaVo.getParam("AccDateEnd")) + 19110000;

		// new ArrayList
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();
		// new table
		ForeclosureFee tForeclosureFee = new ForeclosureFee();

		// 測試該會計日期是否存在法拍費用檔
		Slice<ForeclosureFee> slForeclosureFee = sForeclosureFeeService.openAcDateBetween(iAccDateStart, iAccDateEnd,
				this.index, this.limit, titaVo);
		lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
		if (lForeclosureFee == null) {
			throw new LogicException("E0001", "L2942該會計日期在法拍費用檔無資料");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slForeclosureFee != null && slForeclosureFee.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		// 法拍費總計
		BigDecimal rvBal = BigDecimal.ZERO;
		// 催收法務費累餘
		BigDecimal ovdulawfeebal = BigDecimal.ZERO;
		// 法務費累餘
		BigDecimal lawfeebal = BigDecimal.ZERO;

		// 法拍件數
		int CNT = lForeclosureFee.size();
		// 催收註記
		String overdueCode = "";

		for (ForeclosureFee tmpForeclosureFee : lForeclosureFee) {
			// new occurs
			OccursList occurslist = new OccursList();
			// 催收註記 如有催收資料給Y,沒有給空白
			if (tmpForeclosureFee.getOverdueDate() != 0) {
				overdueCode = "Y";
			} else {
				overdueCode = "";
			}
			// 催收法務累餘
			if (tmpForeclosureFee.getOverdueDate() != 0) {

				ovdulawfeebal = ovdulawfeebal.add(tmpForeclosureFee.getFee());
			} else {
				// 法拍費用累餘
				lawfeebal = lawfeebal.add(tmpForeclosureFee.getFee());
				;
			}

			// 取筆數如法拍費為0則不記筆數
			if (tmpForeclosureFee.getFee().compareTo(BigDecimal.ZERO) == 0) {
				CNT = CNT - 1;
			}
			// 每筆法拍費用加起來
			rvBal = rvBal.add(tmpForeclosureFee.getFee());

			this.totaVo.putParam("OCNT", CNT);
			this.totaVo.putParam("OFEEAMT", rvBal);
			// 催收法務累餘
			this.totaVo.putParam("OOVDULAWFEECUMBAL", ovdulawfeebal);
			// 法拍費用累餘
			this.totaVo.putParam("OFEEAMTCUMBAL", lawfeebal);

			// 會計日期 起帳日期
			occurslist.putParam("OOACCTDATE", tmpForeclosureFee.getOpenAcDate());
			// 入帳日期
			occurslist.putParam("OOENTRYDATE", tmpForeclosureFee.getDocDate());
			occurslist.putParam("OOCustNo", tmpForeclosureFee.getCustNo());
			occurslist.putParam("OOFacmNo", tmpForeclosureFee.getFacmNo());
			occurslist.putParam("OOFEEAMT", tmpForeclosureFee.getFee());
			occurslist.putParam("OOFeeCode", tmpForeclosureFee.getFeeCode());
			occurslist.putParam("OOReceiveDate", tmpForeclosureFee.getReceiveDate());
			occurslist.putParam("OOCloseDate", tmpForeclosureFee.getCloseDate());
			// 催收
			occurslist.putParam("OOVERDUE", overdueCode);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}