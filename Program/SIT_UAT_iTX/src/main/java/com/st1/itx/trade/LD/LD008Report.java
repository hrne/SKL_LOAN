package com.st1.itx.trade.LD;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.db.service.springjpa.cm.LD008ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LD008Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LD008Report.class);
	
	@Autowired
	LD008ServiceImpl lD008ServiceImpl;

	@Autowired
	InnFundAplService innFundAplService;

	String exportDate;

	// 報表帳務日
	int rptDate;

	// 報表種類
	int rptType;

	// 報表種類中文
	String rptTypeItem;

	// 本帳務日可用資金
	BigDecimal availableFunds;
	BigDecimal fundsLimit;
	BigDecimal fundsLimitAmt;
	String fundsMonth;
	String fundsDay;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, 84, "新光人壽保險股份有限公司", "C");
		this.print(-1, 146, "機密等級：" + this.getRptSecurity());
		this.print(-2, 1, "報　表：" + this.getRptCode());
		this.print(-2, 84, this.getRptItem() + "--" + rptTypeItem, "C");
		this.print(-2, 146, "日　　期：" + exportDate);
		this.print(-3, 146, "時　　間：" + showTime(this.getNowTime()));
		this.print(-4, 146, "頁　　次：" + this.getNowPage());
		this.print(-5, 84, showRocDate(rptDate, 0), "C");
		this.print(-5, 146, "單位：元");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);
	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		this.titaVo = titaVo;
		
		Boolean findRelatedOnly = titaVo.getParam("inputShowType").equals("1") ? true : false;

		// 設定製表日期
		setExportDate(dDateUtil.getNowStringBc());

		// 取得帳務日
		rptDate = titaVo.getEntDyI();

		// 取得最新可用資金資料
		getFundData();

		// 設定每頁報表種類
		List<Map<String, String>> listSubBookCodes = null;
		try {
			listSubBookCodes = lD008ServiceImpl.findAll_SubBookCodes(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lD008ServiceImpl.findAll error = " + e.getMessage());
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD008", (findRelatedOnly ? "關係人" : "") + "放款餘額總表", "機密", "A4", "L");

		List<Map<String, String>> listLD008 = null;
		List<Map<String, String>> listLD008r = null;

		// 是否為第一頁
		boolean isFirstPage = true;

		for (Map<String, String> SubBookCodesVo : listSubBookCodes) {

			String AcSubBookCode = SubBookCodesVo.get("F0");
			
			rptTypeItem = SubBookCodesVo.get("F1");

			if (!isFirstPage) {
				// 非第一頁時先換頁
				this.newPage();
			} else {
				isFirstPage = false;
			}

			try {
				if (findRelatedOnly)
				{
				listLD008r = lD008ServiceImpl.findAll_related(rptType, AcSubBookCode, titaVo);
				}
				else
				{
				listLD008 = lD008ServiceImpl.findAll(rptType, AcSubBookCode, titaVo);
				}
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("lD008ServiceImpl.findAll error = " + e.getMessage());
			}
			
			if (findRelatedOnly)
			{
			exportPdf_Related(listLD008r);
			}
			else
			{
			exportPdf(listLD008);
			}
		}

		long sno = this.close();

		this.toPdf(sno);
		return true;

	}

	BigDecimal relatedTotal; // 關係人金額
	BigDecimal subTotalCountA; // A 戶數
	BigDecimal subTotalA; // A 金額
	BigDecimal subTotalCountB; // B 戶數
	BigDecimal subTotalB; // B 金額
	BigDecimal subTotalCountC; // C 戶數
	BigDecimal subTotalC; // C 金額
	BigDecimal subTotalCountABCD; // ABCD 戶數
	BigDecimal subTotalABCD; // ABCD 金額
	BigDecimal totalCount; // ABCDEF 戶數
	BigDecimal total; // ABCDEF 金額

	private void exportPdf(List<Map<String, String>> listLD008) {

		// 測試使用-印表前先把list資料列印到LOG
		this.info("listLD008 detailSeq , counts , loanBalSum");
		for (Map<String, String> tLD008 : listLD008) {
			String detailSeq = tLD008.get("F0");
			String counts = tLD008.get("F1");
			String loanBalSum = tLD008.get("F2");
			this.info("listLD008 " + detailSeq + " , " + counts + " , " + loanBalSum);
		}
		this.info("-----------------------------------------");

		ArrayList<Map<String, String>> cloneListLD008 = new ArrayList<>(listLD008);

		// 計算小計及合計金額
		computeTotal(cloneListLD008);

		/**
		 *
		 * ------------------------------------------------------------------------------------------------------------1
		 * ------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7
		 * ---------12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(1, 1, "┌──────────────────────────────┬────────────┬──────────────────────┬──────┬───────┐");
		print(1, 1, "│　　　　　　　科　　　　　　 　　　　　 　　　目　　　　　　│　　　戶　　　　數　　　│　　　　　金　　　　　　　　　　額　　　　　│　各項比率　│　占資金比率　│");
		print(1, 1, "├─────────────────┬────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　關　　係　　人　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(0).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(0).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(0).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(0).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　短　期　擔　保　　　　　｜　　公　　司　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(1).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(1).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(1).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(1).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　放　款　　　　　　　｜　　個　　人　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(2).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(2).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(2).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(2).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　　小　　　計　（Ａ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountA, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalA, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalA), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalA), 2), "R"); // 占資金比率
		print(1, 1, "├─────────────────┼────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　關　　係　　人　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(3).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(3).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(3).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(3).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　中　期　擔　保　　　　　｜　　公　　司　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(4).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(4).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(4).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(4).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　放　款　　　　　　　｜　　個　　人　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(5).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(5).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(5).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(5).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　　小　　　計　（Ｂ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountB, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalB, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalB), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalB), 2), "R"); // 占資金比率
		print(1, 1, "├─────────────────┼────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　關　　係　　人　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(6).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(6).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(6).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(6).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　長　期　擔　保　　　　　｜　　公　　司　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(7).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(7).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(7).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(7).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　放　款　　　　　　　｜　　個　　人　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(8).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(8).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(8).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(8).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　　小　　　計　（Ｃ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountC, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalC, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalC), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalC), 2), "R"); // 占資金比率
		print(1, 1, "├─────────────────┴────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　催　　　　收　　　　款　　　　項　　　　　　（Ｄ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(9).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(9).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(9).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(9).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "├──────────────────────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　放　款　合　計（未含三十年房貸）　　　　（Ａ＋Ｂ＋Ｃ＋Ｄ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountABCD, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalABCD, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalABCD), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalABCD), 2), "R"); // 占資金比率
		print(1, 1, "├──────────────────────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　三　十　年　房　屋　貸　款　　　　　　　　　　　　　（Ｅ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(10).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(10).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(10).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(10).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "├──────────────────────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　３０年房屋貸款催收款項　　　　　　　　　　　　　　　（Ｆ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008.get(11).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008.get(11).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008.get(11).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008.get(11).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "├──────────────────────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　放　款　合　計（含三十年房貸）　（Ａ＋Ｂ＋Ｃ＋Ｄ＋Ｅ＋Ｆ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(totalCount, 0), "R"); // 戶數
		print(0, 130, formatAmt(total, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(total), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(total), 2), "R"); // 占資金比率
		print(1, 1, "└──────────────────────────────┴────────────┴──────────────────────┴──────┴───────┘");
		/**
		 *
		 * ------------------------------------------------------------------------------------------------------------1
		 * ------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7
		 * ---------12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(1, 1, "註：1.關係人金額　　　　　　　　　　　　　元占放款比率之　　　　　　　％");
		print(0, 42, formatAmt(relatedTotal, 0), "R"); // 關係人金額
		print(0, 68, formatAmt(percentOfTotal(relatedTotal), 2), "R"); // 關係人金額占放款比率
		print(1, 1, "　　2.公司到　　　月　　　日資金為　　　　　　　　　　　　　元，上限　　　　　　　％　為　　　　　　　　　　　　　元");
		print(0, 17, fundsMonth, "R"); // 月
		print(0, 26, fundsDay, "R"); // 日
		print(0, 60, formatAmt(availableFunds, 0), "R"); // 資金金額
		print(0, 80, formatAmt(fundsLimit, 2), "R");
		print(0, 114, formatAmt(fundsLimitAmt, 0), "R"); // 上限金額

	}
	
	private void exportPdf_Related(List<Map<String, String>> listLD008r) {

		// 測試使用-印表前先把list資料列印到LOG
		this.info("listLD008r detailSeq , counts , loanBalSum");
		for (Map<String, String> tLD008 : listLD008r) {
			String detailSeq = tLD008.get("F0");
			String counts = tLD008.get("F1");
			String loanBalSum = tLD008.get("F2");
			this.info("listLD008r " + detailSeq + " , " + counts + " , " + loanBalSum);
		}
		this.info("-----------------------------------------");

		ArrayList<Map<String, String>> cloneListLD008r = new ArrayList<>(listLD008r);

		// 計算小計及合計金額
		computeTotal(cloneListLD008r);

		/**
		 *
		 * ------------------------------------------------------------------------------------------------------------1
		 * ------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7
		 * ---------12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(1, 1, "┌──────────────────────────────┬────────────┬──────────────────────┬──────┬───────┐");
		print(1, 1, "│　　　　　　　科　　　　　　 　　　　　 　　　目　　　　　　│　　　戶　　　　數　　　│　　　　　金　　　　　　　　　　額　　　　　│　各項比率　│　占資金比率　│");
		print(1, 1, "├─────────────────┬────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　短　期　擔　保　　　　　｜　　公　　司　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008r.get(1).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008r.get(1).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008r.get(1).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008r.get(1).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　放　款　　　　　　　｜　　個　　人　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008r.get(2).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008r.get(2).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008r.get(2).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008r.get(2).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　　小　　　計　（Ａ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountA, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalA, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalA), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalA), 2), "R"); // 占資金比率
		print(1, 1, "├─────────────────┼────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　中　期　擔　保　　　　　｜　　公　　司　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008r.get(4).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008r.get(4).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008r.get(4).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008r.get(4).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　放　款　　　　　　　｜　　個　　人　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008r.get(5).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008r.get(5).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008r.get(5).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008r.get(5).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　　小　　　計　（Ｂ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountB, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalB, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalB), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalB), 2), "R"); // 占資金比率
		print(1, 1, "├─────────────────┼────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　長　期　擔　保　　　　　｜　　公　　司　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008r.get(7).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008r.get(7).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008r.get(7).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008r.get(7).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　放　款　　　　　　　｜　　個　　人　　戶　　　│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(listLD008r.get(8).get("F1"), 0), "R"); // 戶數
		print(0, 130, formatAmt(listLD008r.get(8).get("F2"), 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(listLD008r.get(8).get("F2")), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(listLD008r.get(8).get("F2")), 2), "R"); // 占資金比率
		print(1, 1, "│　　　　　　　　　　　　　　　　　├────────────┼────────────┼──────────────────────┼──────┼───────┤");
		print(1, 1, "│　　　　　　　　　　　　　　　　　｜　　　小　　　計　（Ｃ）│　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　│");
		print(0, 86, formatAmt(subTotalCountC, 0), "R"); // 戶數
		print(0, 130, formatAmt(subTotalC, 0), "R"); // 金額
		print(0, 147, formatAmt(percentOfTotal(subTotalC), 2), "R"); // 各項比率
		print(0, 163, formatAmt(percentOfFunds(subTotalC), 2), "R"); // 占資金比率
		print(1, 1, "└──────────────────────────────┴────────────┴──────────────────────┴──────┴───────┘");
		/**
		 *
		 * ------------------------------------------------------------------------------------------------------------1
		 * ------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7
		 * ---------12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */

	}

	private BigDecimal percentOfTotal(String input) {

		BigDecimal amt = new BigDecimal(input);

		return percentOfTotal(amt);
	}

	private BigDecimal percentOfTotal(BigDecimal amt) {

		BigDecimal hundred = new BigDecimal("100");

		return computeDivide(amt, total, 4).multiply(hundred);
	}

	private BigDecimal percentOfFunds(String input) {

		BigDecimal amt = new BigDecimal(input);

		return percentOfFunds(amt);
	}

	private BigDecimal percentOfFunds(BigDecimal amt) {

		BigDecimal hundred = new BigDecimal("100");

		return computeDivide(amt, availableFunds, 4).multiply(hundred);
	}

	private void computeTotal(List<Map<String, String>> listLD008) {

		// 歸零
		relatedTotal = BigDecimal.ZERO;
		subTotalCountA = BigDecimal.ZERO;
		subTotalA = BigDecimal.ZERO;
		subTotalCountB = BigDecimal.ZERO;
		subTotalB = BigDecimal.ZERO;
		subTotalCountC = BigDecimal.ZERO;
		subTotalC = BigDecimal.ZERO;
		subTotalCountABCD = BigDecimal.ZERO;
		subTotalABCD = BigDecimal.ZERO;
		totalCount = BigDecimal.ZERO;
		total = BigDecimal.ZERO;

		// 計算
		for (Map<String, String> tLD008 : listLD008) {
			int detailSeq = Integer.parseInt(tLD008.get("F0"));
			BigDecimal counts = new BigDecimal(tLD008.get("F1"));
			BigDecimal amt = new BigDecimal(tLD008.get("F2"));

			switch (detailSeq) {
			case 1:
				relatedTotal = relatedTotal.add(amt);
			case 2:
			case 3:
				subTotalCountA = subTotalCountA.add(counts);
				subTotalA = subTotalA.add(amt);
				subTotalCountABCD = subTotalCountABCD.add(counts);
				subTotalABCD = subTotalABCD.add(amt);
				break;
			case 4:
				relatedTotal = relatedTotal.add(amt);
			case 5:
			case 6:
				subTotalCountB = subTotalCountB.add(counts);
				subTotalB = subTotalB.add(amt);
				subTotalCountABCD = subTotalCountABCD.add(counts);
				subTotalABCD = subTotalABCD.add(amt);
				break;
			case 7:
				relatedTotal = relatedTotal.add(amt);
			case 8:
			case 9:
				subTotalCountC = subTotalCountC.add(counts);
				subTotalC = subTotalC.add(amt);
				subTotalCountABCD = subTotalCountABCD.add(counts);
				subTotalABCD = subTotalABCD.add(amt);
				break;
			case 10:
				subTotalCountABCD = subTotalCountABCD.add(counts);
				subTotalABCD = subTotalABCD.add(amt);
				break;
			default:
				break;
			}

			// 合計(ABCDEF)
			totalCount = totalCount.add(counts);
			total = total.add(amt);
		}

	}

	private void setExportDate(String nowStringBc) {
		exportDate = nowStringBc.substring(4, 6) + "/" + nowStringBc.substring(6, 8) + "/"
				+ nowStringBc.substring(2, 4);
	}

	private void getFundData() {

		InnFundApl tInnFundApl = innFundAplService.acDateFirst(BigDecimal.ZERO, titaVo);

		if (tInnFundApl == null) {
			availableFunds = BigDecimal.ZERO;
			fundsLimit = BigDecimal.ZERO;
			fundsLimitAmt = BigDecimal.ZERO;
			fundsMonth = "";
			fundsDay = "";
		} else {
			availableFunds = tInnFundApl.getResrvStndrd();
			fundsLimit = tInnFundApl.getPosbleBorPsn();
			fundsLimitAmt = tInnFundApl.getPosbleBorAmt();
			String acDate = String.valueOf(tInnFundApl.getAcDate());
			fundsMonth = acDate.substring(4, 6);
			fundsDay = acDate.substring(6, 8);
		}

	}
}
