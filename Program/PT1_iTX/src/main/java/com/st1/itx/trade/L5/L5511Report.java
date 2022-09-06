package com.st1.itx.trade.L5;

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
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.db.service.springjpa.cm.L5511ServiceImpl;

@Component("l5511Report")
@Scope("prototype")

public class L5511Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L5511Report.class);

	@Autowired
	public L5511ServiceImpl l5511ServiceImpl;

	String subRptItem = "";

	String dashLine = "-----------------------------------------------------------------------------------------------------------------";
	String dashLine2 = "=================================================================================================================";

	@Override
	public void printHeader() {
		this.print(-1, 3, "程式ID：" + this.getRptCode());
		this.print(-1, 50, "新光人壽保險股份有限公司", "C");
		this.print(-1, 90, "機密等級：" + this.getRptSecurity());
		this.print(-2, 3, "報　表：" + this.getRptCode());
		this.print(-2, 50, this.getRptItem(), "C");

		this.print(-2, 90, "日　　期：" + showDate(this.getNowDate()));
		this.print(-3, 90, "時　　間：" + showTime(this.getNowTime()));
		this.print(-4, 50, this.subRptItem, "C");
		this.print(-4, 90, "頁　　次：" + this.getNowPage());
//			this.print(-6, 50, showRocDate(this.date), "C");

	}

	@Override
	public void printTitle() {

		this.print(-6, 3, "計件");
		this.print(-6, 8, "商品");
		this.print(-6, 14, " 介紹人");
		this.print(-6, 45, "獎金");

		this.print(-7, 3, "代碼");
		this.print(-7, 8, "代碼");
		this.print(-7, 14, "員工代號");
		this.print(-7, 23, "介紹人");
		this.print(-7, 30, "借戶戶號");
		this.print(-7, 45, "類別");
		this.print(-7, 51, "戶名");
		this.print(-7, 69, "撥款日期");
		this.print(-7, 95, "撥款金額", "R");
		this.print(-7, 105, "  車馬費", "R");
		this.print(-7, 115, "    小計", "R");
		this.print(-8, 3, dashLine2);
	}

	public long exec(TitaVo titaVo, int workYM, int bonusDate) throws LogicException {

		// 設定字體1:標楷體 字體大小36
		this.setFont(1, 36);
		int reportDate = Integer.valueOf(titaVo.getEntDy());
		String brno = titaVo.getBrno();
		String owDate = dDateUtil.getNowStringRoc();
//				this.pageSize = "A5";
		String NowTime = dDateUtil.getNowStringTime();
		String reportItem = "車馬費發放明細表";
		subRptItem = showRocDate(bonusDate, 0);

		this.open(titaVo, reportDate, brno, "L5511.1", reportItem, "機密", "A4", "P");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 10);
		this.setCharSpaces(0);

		this.setLineSpaces(2);

		this.setBeginRow(12);
		this.setMaxRows(50);

		List<Map<String, String>> L5511List = null;

		try {
			L5511List = l5511ServiceImpl.FindData(titaVo, workYM);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E5004", "");
		}

		if (L5511List == null || L5511List.size() == 0) {
			throw new LogicException("E0001", "");
		} else {
			int rCnt = 0;

			double normalCnt = 0; // 一般帳戶件數
			double normalAmt = 0; // 一般帳戶合計
			double unnormalCnt1 = 0; // 利變A帳戶件數
			double unnormalAmt1 = 0; // 利變A帳戶合計
			double unnormalCnt2 = 0; // 其他帳戶件數
			double unnormalAmt2 = 0; // 其他帳戶合計

			double cnt1a = 0; // 梆約件數(看計件代碼12345)
			double amt1a = 0;// 梆約件數(看計件代碼12345)
			double cnt1b = 0; // 非梆約件數(看計件代碼12345)
			double amt1b = 0;// 非梆約件數(看計件代碼非12345)
			int cnt2 = 0; // 非1類件數
			double amt2 = 0; // 非1類合計
			int cnt6 = 0; // 專案獎勵金件數
			double amt6 = 0; // 專案獎勵金合計
			String employeeNo = "";
			double bonusTotal = 0;
			DecimalFormat df = new DecimalFormat("###,###,###");

			for (Map<String, String> mapL5511 : L5511List) {
				logger.info("L5511Reort L5511List count =" + ++rCnt);
				logger.info(rCnt + "=" + mapL5511.get("F0") + "/" + mapL5511.get("F1") + "/" + mapL5511.get("F2") + "/" + mapL5511.get("F3"));

				if (!"".equals(employeeNo) && !employeeNo.equals(mapL5511.get("F2"))) {

					this.print(0, 115, df.format(bonusTotal), "R");// "小計"
					bonusTotal = 0;
					this.print(1, 3, dashLine);
				}
				String pieceCode = mapL5511.get("F0");
				String bonusType = mapL5511.get("F7");
				String AcSubBookCode = mapL5511.get("F12").trim(); // 區隔帳冊(00A:傳統帳冊;201:利變年金帳冊)

				this.print(1, 14, mapL5511.get("F2"));// 介紹人員工代號
				this.print(0, 23, mapL5511.get("F3"));// 介紹人
				this.print(0, 47, bonusType);// "類別"

				// 非專業獎勵金
				if (!"6".equals(bonusType)) {
					this.print(0, 3, pieceCode); // 計件代碼
					this.print(0, 8, mapL5511.get("F1"));
					this.print(0, 30, String.format("%07d-%03d-%03d", Integer.valueOf(mapL5511.get("F4")), Integer.valueOf(mapL5511.get("F5")), Integer.valueOf(mapL5511.get("F6"))));

					int len = Integer.min(mapL5511.get("F8").length(), 5);
					this.print(0, 51, mapL5511.get("F8").substring(0, len));// "戶名"

					this.print(0, 69, this.showRocDate(Integer.valueOf(mapL5511.get("F9")), 1));// "撥款日期"

					double loanAmt = Double.valueOf(mapL5511.get("F10"));
					this.print(0, 95, df.format(loanAmt), "R");// "撥款金額"
				}

				double bonusAmt = Double.valueOf(mapL5511.get("F11"));
				this.print(0, 105, df.format(bonusAmt), "R");// "車馬費

				employeeNo = mapL5511.get("F2");
				bonusTotal += bonusAmt;

				if (!"6".equals(bonusType)) {
					if ("00A".equals(AcSubBookCode)) {
						normalCnt++;
						normalAmt += bonusAmt;
					} else if ("201".equals(AcSubBookCode)) {
						unnormalCnt1++;
						unnormalAmt1 += bonusAmt;
					} else {
						unnormalCnt2++;
						unnormalAmt2 += bonusAmt;
					}

				}

				if ("1".equals(bonusType)) {
					if ("1".equals(pieceCode) || "2".equals(pieceCode) || "3".equals(pieceCode) || "4".equals(pieceCode) || "5".equals(pieceCode)) {
						cnt1a++;
						amt1a += bonusAmt;
					} else {
						cnt1b++;
						amt1b += bonusAmt;
					}
				} else if ("6".equals(bonusType)) {
					cnt6++;
					amt6 += bonusAmt;
				} else {
					cnt2++;
					amt2 += bonusAmt;
				}
				// this.print(0, 89, df.format(bonusAmt));//"小計"
			}
			if (!"".equals(employeeNo)) {
				this.print(0, 115, df.format(bonusTotal), "R");// "小計"
				this.print(1, 3, dashLine);
			}

			if (normalCnt != 0) {
				this.print(1, 30, "一般帳戶");
				this.print(0, 51, "小　計：");
				this.print(0, 73, df.format(normalCnt) + "筆", "R");
				this.print(0, 115, df.format(normalAmt), "R");
//				this.print(1, 3, dashLine);
			}

			if (unnormalCnt1 != 0) {
				this.print(1, 30, "利變Ａ");
				this.print(0, 51, "小　計：");
				this.print(0, 73, df.format(unnormalCnt1) + "筆", "R");
				this.print(0, 115, df.format(unnormalAmt1), "R");
//				this.print(1, 3, dashLine);
			}

			if (unnormalCnt2 != 0) {
				this.print(1, 30, "其他");
				this.print(0, 51, "小　計：");
				this.print(0, 73, df.format(unnormalCnt2) + "筆", "R");
				this.print(0, 115, df.format(unnormalAmt2), "R");
//				this.print(1, 3, dashLine);
			}

			if (cnt6 != 0) {
				this.print(1, 30, "專業獎勵金");
				this.print(0, 51, "小　計：");
				this.print(0, 73, df.format(cnt6) + "筆", "R");
				this.print(0, 115, df.format(amt6), "R");
//				this.print(1, 3, dashLine);
			}

			this.print(1, 3, dashLine);
			this.print(1, 51, "總　計：");
			this.print(0, 73, df.format(normalCnt + unnormalCnt1 + unnormalCnt2 + cnt6) + "筆", "R");
			this.print(0, 115, df.format(normalAmt + unnormalAmt1 + unnormalAmt2 + amt6), "R");

			this.print(1, 3, dashLine);
			this.print(2, 3, dashLine2);
			this.print(1, 3, "獎金類別");
			this.print(0, 15, "類別顯示");
			this.print(0, 30, "計件代碼");
			this.print(0, 60, "件數", "R");
			this.print(0, 80, "發放金額", "R");
			this.print(1, 3, dashLine);
			this.print(1, 3, "１");
			this.print(0, 15, "綁約");
			this.print(0, 30, "１２３４５");
			this.print(0, 60, df.format(cnt1a), "R");
			this.print(0, 80, df.format(amt1a), "R");
			// this.print(1, 3, "１");
			this.print(1, 15, "不綁約");
			this.print(0, 30, "非１２３４５");
			this.print(0, 60, df.format(cnt1b), "R");
			this.print(0, 80, df.format(amt1b), "R");
			this.print(1, 3, dashLine);
			this.print(1, 3, "非１");
			this.print(0, 15, "協辦人員");
			this.print(0, 30, "全部");
			this.print(0, 60, df.format(cnt2), "R");
			this.print(0, 80, df.format(amt2), "R");
			if (cnt6 > 0) {
				this.print(1, 15, "專案獎勵金");
				this.print(0, 30, "全部");
				this.print(0, 60, df.format(cnt6), "R");
				this.print(0, 80, df.format(amt6), "R");
			}
			this.print(1, 3, dashLine2);
		}

		this.print(3, 13, "協理：　　　　　　經理：　　　　　　覆核：　　　　　　製表人：");

		long pdfSno = this.close();

		this.toPdf(pdfSno, "L5511");

		return pdfSno;

	}
}
