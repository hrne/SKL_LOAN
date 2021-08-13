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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ReceiveDate=9,7<br>
 */

@Service("L2603")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2603 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2603.class);

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService sForeclosureFeeService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2603 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		// tita
		// 收件日
		int iReceiveDateS = parse.stringToInteger(titaVo.getParam("ReceiveDateSt"));
		int iReceiveDateE = parse.stringToInteger(titaVo.getParam("ReceiveDateEd"));

		// new ArrayList
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();
		// new table
		ForeclosureFee tForeclosureFee = new ForeclosureFee();
		// 測試該收件日是否存在法拍費用檔
		Slice<ForeclosureFee> slForeclosureFee = sForeclosureFeeService.receiveDateBetween(iReceiveDateS + 19110000,
				iReceiveDateE + 19110000, this.index, this.limit);
		lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
		// 不存在拋錯
		if (lForeclosureFee == null) {
			throw new LogicException("E0001", "該收件日在法拍費用檔無資料");
		}
		// 法務費金額
		BigDecimal feeamt = BigDecimal.ZERO;
		// 鑑定費金額
		BigDecimal amt = BigDecimal.ZERO;
		// 筆數
		int cnt = lForeclosureFee.size();

		for (ForeclosureFee tmpForeclosureFee : lForeclosureFee) {
			// new occurs
			OccursList occurslist = new OccursList();
			// new table
			CustMain tCustMain = new CustMain();
			int custno = tmpForeclosureFee.getCustNo();
			// 戶號進客戶主檔取姓名
			tCustMain = sCustMainService.custNoFirst(custno, custno);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}
			// 把每筆資料的科目為07之法拍費加起來
			if (tmpForeclosureFee.getFeeCode().equals("07") || tmpForeclosureFee.getFeeCode().equals("7")) {
				amt = amt.add(tmpForeclosureFee.getFee());
				this.info("鑑定費科目 = " + tmpForeclosureFee.getFeeCode());
			} else {
				// 把每筆資料的科目除去04,05的法拍費加起來
				feeamt = feeamt.add(tmpForeclosureFee.getFee());
				this.info("法務費科目 = " + tmpForeclosureFee.getFeeCode());
			}

			// 取筆數如法拍費為0則不記筆數
			if (tmpForeclosureFee.getFee().compareTo(BigDecimal.ZERO) == 0) {
				cnt = cnt- 1;
			}

			// 筆數
			this.totaVo.putParam("OCnt", cnt);
			// 暫付及待結轉款項-法務費金額
			this.totaVo.putParam("OFeeAmt", feeamt);
			// 暫付及待結轉款項-鑑定費金額
			this.totaVo.putParam("OAmt", amt);

			occurslist.putParam("OOCustNo", tmpForeclosureFee.getCustNo());
			occurslist.putParam("OOCustName", tCustMain.getCustName());
			occurslist.putParam("OOReceiveDate", tmpForeclosureFee.getReceiveDate());
			occurslist.putParam("OOFee", tmpForeclosureFee.getFee());
			occurslist.putParam("OOFeeCode", tmpForeclosureFee.getFeeCode());
			occurslist.putParam("OOCloseNo", tmpForeclosureFee.getCloseNo());
			occurslist.putParam("OOLegalStaff", tmpForeclosureFee.getLegalStaff());
			occurslist.putParam("OORmk", tmpForeclosureFee.getRmk());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}