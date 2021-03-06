package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD003ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LD003Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LD003Report.class);

	@Autowired
	// 在Spring管理的Bean，能夠找到，可允許找不到時設定為null，也可以指定required=fails
	LD003ServiceImpl lD003ServiceImpl;
 
	@Autowired
	DateUtil dateUtil;
 
	// 自訂明細標題
	@Override
	public void printHeader() {
		logger.info("printTitle nowRow = " + this.NowRow);

		this.setCharSpaces(0);

		this.print(-2, 5, "程式ID：" + this.getParentTranCode());
		this.print(-2, 60, "新光人壽保險股份有限公司", "C");
		this.print(-1, 100, "機密等級：密");
		this.print(-3, 5, "報　表：" + this.getRptCode());
		this.print(-3, 60, "放款明細餘額總表（日）", "C");
		this.print(-2, 100, "日　期：" + this.showBcDate(dateUtil.getNowStringBc(), 1));
		this.print(-3, 100, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 100, "頁　數：" + this.getNowPage());
		this.print(-5, 60, getshowRocDate(this.getReportDate()), "C");
		this.print(-6, 100, "單位：元");

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(83);
	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LD003List = null;
		try {
			LD003List = lD003ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			logger.info("lD003ServiceImpl.findAll error = " + e.toString());
		}
		exportResult(titaVo, LD003List);
	}

	public void exportResult(TitaVo titaVo, List<Map<String, String>> LD003List) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD003", "放款明細餘額總表(日)", "機密", "A4", "P");

		this.print(1, 1, "");


		int i = 0;

		this.print(-8, 1, "┌──────────────────────────┬─────────────┬───────────────┐");
		this.print(-9, 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
		this.print(-9, 70, "件　　　數", "C");
		this.print(-9, 100, "貸　款　金　額", "C");
		this.print(-10, 1, "├──────────────────────────┼─────────────┼───────────────┤");

		for (i = 0; i < 4; i++) {

			this.print(-11 - (i * 10), 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
			this.print(-12 - (i * 10), 1, "├──────────────────────────┼─────────────┼───────────────┤");
			this.print(-13 - (i * 10), 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
			this.print(-14 - (i * 10), 1, "├──────────────────────────┼─────────────┼───────────────┤");
			this.print(-15 - (i * 10), 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
			this.print(-16 - (i * 10), 1, "├──────────────────────────┼─────────────┼───────────────┤");
			this.print(-17 - (i * 10), 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
			this.print(-18 - (i * 10), 1, "├──────────────────────────┼─────────────┼───────────────┤");
			this.print(-19 - (i * 10), 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
			this.print(-19 - (i * 10), 30, "小　　計", "L");
			this.print(-20 - (i * 10), 1, "├──────────────────────────┼─────────────┼───────────────┤");
			if (i == 3) {
				this.print(-21 - (i * 10), 1, "│　　　　　　　　　　　　　  　　　　　　　　　　　　│　　　　　　　　　　　　　│　　　　　　　　　　　　　　　│");
				this.print(-21 - (i * 10), 20, "合　　　　　計", "L");
				this.print(-22 - (i * 10), 1, "└──────────────────────────┴─────────────┴───────────────┘");
			}

			switch (i) {
			case 0:
				this.print(-11 - (i * 10), 10, "短期擔保放款", "L");
				break;
			case 1:
				this.print(-11 - (i * 10), 10, "中期擔保放款", "L");
				break;
			case 2:
				this.print(-11 - (i * 10), 10, "長期擔保放款", "L");
				break;
			case 3:
				this.print(-11 - (i * 10), 10, "三十年房貸", "L");
				break;
			default:
				break;

			}
			this.print(-11 - (i * 10), 30, "傳統帳冊", "L");
			this.print(-13 - (i * 10), 30, "利變年金帳冊A", "L");
			this.print(-15 - (i * 10), 30, "利變年金帳冊B", "L");
			this.print(-17 - (i * 10), 30, "傳統帳冊A", "L");
		}

		int groupA = 0;
		int groupB = 0;
		int groupC = 0;
		int groupD = 0;

		BigDecimal totalSC = BigDecimal.ZERO;
		BigDecimal totalSA = BigDecimal.ZERO;

		BigDecimal totalMC = BigDecimal.ZERO;
		BigDecimal totalMA = BigDecimal.ZERO;

		BigDecimal totalLC = BigDecimal.ZERO;
		BigDecimal totalLA = BigDecimal.ZERO;

		BigDecimal totalTC = BigDecimal.ZERO;
		BigDecimal totalTA = BigDecimal.ZERO;

		if (LD003List != null && LD003List.size() > 0) {

			DecimalFormat df1 = new DecimalFormat("#,##0");

			for (int j = 0; j < LD003List.size(); j++) {

				int colCount = Integer.parseInt(LD003List.get(j).get("F0"));
				BigDecimal totalCount = new BigDecimal(LD003List.get(j).get("F1"));
				BigDecimal totalAmt = new BigDecimal(LD003List.get(j).get("F2"));

				if (colCount < 5 || colCount == 310) {

					this.print(-11 - groupA, 83, df1.format(totalCount), "R");
					this.print(-11 - groupA, 115, df1.format(totalAmt), "R");
					groupA += 2;
					totalSC = totalSC.add(totalCount);
					totalSA = totalSA.add(totalAmt);

				} else if (colCount < 9 || colCount == 320) {
					this.print(-21 - groupB, 83, df1.format(totalCount), "R");
					this.print(-21 - groupB, 115, df1.format(totalAmt), "R");
					groupB += 2;
					totalMC = totalMC.add(totalCount);
					totalMA = totalMA.add(totalAmt);

				} else if (colCount < 13 || colCount == 330) {
					this.print(-31 - groupC, 83, df1.format(totalCount), "R");
					this.print(-31 - groupC, 115, df1.format(totalAmt), "R");
					groupC += 2;
					totalLC = totalLC.add(totalCount);
					totalLA = totalLA.add(totalAmt);

				} else {
					this.print(-41 - groupD, 83, df1.format(totalCount), "R");
					this.print(-41 - groupD, 115, df1.format(totalAmt), "R");
					groupD += 2;
					totalTC = totalTC.add(totalCount);
					totalTA = totalTA.add(totalAmt);

				}
			}

			this.print(-19, 83, df1.format(totalSC), "R");
			this.print(-19, 115, df1.format(totalSA), "R");

			this.print(-29, 83, df1.format(totalMC), "R");
			this.print(-29, 115, df1.format(totalMA), "R");

			this.print(-39, 83, df1.format(totalLC), "R");
			this.print(-39, 115, df1.format(totalLA), "R");

			this.print(-49, 83, df1.format(totalTC), "R");
			this.print(-49, 115, df1.format(totalTA), "R");

			this.print(-51, 83, df1.format(totalSC.add(totalMC).add(totalLC).add(totalTC)), "R");
			this.print(-51, 115, df1.format(totalSA.add(totalMA).add(totalLA).add(totalTA)), "R");
			
			this.print(-53, 0, ""); // 把 cursor 移動到最後一行以後，避免簽核歪掉

		} else {
			this.print(-11, 115, "本日無資料", "R");
		}

		this.close();

		//this.toPdf(sno);
		
	}
}
