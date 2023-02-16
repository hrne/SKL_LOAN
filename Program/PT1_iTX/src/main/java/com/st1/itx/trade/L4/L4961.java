package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4961ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4961")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4961 extends TradeBuffer {
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	DateUtil dDateUtil;
	

	/* DB服務注入 */
	@Autowired
	public L4961ServiceImpl l4961ServiceImpl;
	@Autowired
	L4961ReportC l4961ReportC;
	@Autowired
	L4961ReportA l4961ReportA;
	@Autowired
	L4961ReportB l4961ReportB;
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4961 ");
		this.totaVo.init(titaVo);

		BigDecimal totalCnt = BigDecimal.ZERO;
		BigDecimal totalAmt = BigDecimal.ZERO;
		int iSearchOption = parse.stringToInteger(titaVo.getParam("SearchOption"));
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		String parentTranCode = titaVo.getTxcd();

		
//		[查詢方式"],1:火險到期年月;2:未銷全部
//		[火險到期年月],(1:火險到期年月)
//		[繳款方式],01:匯款轉帳;02:銀行扣款;03:員工扣薪;04:支票兌現;99:全部
//		[查詢選項],0:正常未繳;1:正常已繳;2:借支;7:續保;8:自保;9:全部  (1:火險到期年月)
//		查詢續保檔(InsuReNew)
		if (iSearchOption == 0) {
			// 0:正常未繳產出火險保費明細表
			l4961ReportC.setParentTranCode(parentTranCode);
			l4961ReportC.exec(titaVo);
			l4961ReportC.close();
		} else if (iSearchOption == 2) {
			// 2:火險保費借支未銷明細表
			l4961ReportA.setParentTranCode(parentTranCode);
			l4961ReportA.exec(titaVo);
			l4961ReportA.close();
		} else if (iSearchOption == 3) {
			// 3:催收款項-火險費明細表
			l4961ReportB.setParentTranCode(parentTranCode);
			l4961ReportB.exec(titaVo);
			l4961ReportB.close();
		} else {
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			try {
				// *** 折返控制相關 ***
				resultList = l4961ServiceImpl.findAll(this.index, this.limit, titaVo);
			} catch (Exception e) {
				this.error("l4961ServiceImpl findByCondition " + e.getMessage());
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() > 0) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

				for (Map<String, String> result : resultList) {

					OccursList occursList = new OccursList();

					int insuMonth = parse.stringToInteger(result.get("F0"));
					int acDate = parse.stringToInteger(result.get("F9"));

					if (insuMonth > 191100) {
						insuMonth = insuMonth - 191100;
					}
					if (acDate > 19110000) {
						acDate = acDate - 19110000;
					}

					occursList.putParam("OOInsuYearMonth", insuMonth);
					occursList.putParam("OOPrevInsuNo", result.get("F1"));
					occursList.putParam("OONowInsuNo", result.get("F2"));
					occursList.putParam("OOCustNo", result.get("F3"));
					occursList.putParam("OOFacmNo", result.get("F4"));
					occursList.putParam("OOCustNm", result.get("F5"));
					occursList.putParam("OOTotalInsuAmt", result.get("F6"));
					occursList.putParam("OORepayCode", result.get("F7"));
					occursList.putParam("OOStatusCode", result.get("F8"));
					occursList.putParam("OOAcDate", acDate);

					totalCnt = totalCnt.add(new BigDecimal(1));
					totalAmt = totalAmt.add(parse.stringToBigDecimal(result.get("F6")));
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				}

				if (totalCnt == BigDecimal.ZERO) {
					throw new LogicException(titaVo, "E0001", "查無資料");
				}

				if (this.index == 0) {
					this.totaVo.putParam("OtotalCnt", totalCnt);
					this.totaVo.putParam("OtotalAmt", totalAmt);
					titaVo.putParam("OtotalCnt", totalCnt);
					titaVo.putParam("OtotalAmt", totalAmt);
				} else {
					this.totaVo.putParam("OtotalCnt",
							totalCnt.add(parse.stringToBigDecimal(titaVo.getParam("OtotalCnt"))));
					this.totaVo.putParam("OtotalAmt",
							totalAmt.add(parse.stringToBigDecimal(titaVo.getParam("OtotalAmt"))));
					titaVo.putParam("OtotalCnt", totalCnt.add(parse.stringToBigDecimal(titaVo.getParam("OtotalCnt"))));
					titaVo.putParam("OtotalAmt", totalAmt.add(parse.stringToBigDecimal(titaVo.getParam("OtotalAmt"))));
				}

				if (resultList.size() == this.limit && hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}
			} else {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		}
		this.addList(this.totaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(),dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4961", "L4961-報表已完成", titaVo);
		
		return this.sendList();
		

	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l4961ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}
}