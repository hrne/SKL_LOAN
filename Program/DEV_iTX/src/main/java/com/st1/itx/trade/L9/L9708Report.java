package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9708ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class L9708Report extends MakeReport {

	@Autowired
	public L9708ServiceImpl l9708ServiceImpl;

	@Autowired
	MakeReport makeReport;

	@Autowired
	DateUtil dateUtil;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int cnt = 0;
	int tcnt = 0;
	int endfg = 0;

	@Override
	public void printHeader() {

		// this.setFontSize(13);

		this.print(-2, 27, "新光人壽房屋貸款自動轉帳申請書明細表");
		this.print(0, 72, "PAGE：");
		this.print(0, 80, Integer.toString(this.getNowPage()), "R");
		String tim = String.format("%02d", Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));
		this.print(0, 1, tim + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + dateUtil.getNowStringBc().substring(2, 4));
		this.print(0, 20, dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-5, 1, "扣款銀行   撥款日期     戶  號   額度  首次應繳日     扣款帳號         公 司 名 稱");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	@Override
	public void printFooter() {
		if (endfg == 9) {
			return;
		}
//		this.print(1, 1, "");
//		this.print(1, 60, "筆數：");
//		this.print(0, 75, Integer.toString(cnt), "R");
//		tcnt += cnt;
//		cnt = 0;
//		if (endfg == 1) {
//			this.print(1, 1, "");
//			this.print(1, 60, "總 筆 數：");
//			this.print(0, 75, Integer.toString(tcnt), "R");
//		}
	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L9708Report exec");

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9708", "貸款自動轉帳申請書明細表", "", "A4", "L");

		List<Map<String, String>> l9708List = null;
		try {
			l9708List = l9708ServiceImpl.findAll(titaVo);
			this.info("L9708Report findAll =" + l9708List.toString());
		} catch (Exception e) {
			this.info("L9708ServiceImpl.LoanBorTx error = " + e.toString());
		}

		makeReport(titaVo, l9708List);

		if (l9708List != null && l9708List.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public void makeReport(TitaVo titaVo, List<Map<String, String>> l9708List) throws LogicException {
		if (l9708List != null && l9708List.size() != 0) {
			int bankGroup = 0;
			int countGroup = 0;
			int countAll = 0;
			for (Map<String, String> l9708Vo : l9708List) {

				if (bankGroup != Integer.valueOf(l9708Vo.get("F0"))) {
					bankGroup = Integer.valueOf(l9708Vo.get("F0"));

					if (l9708List.size() > 0 && countAll != 0) {
						this.print(1, 1, "");
						this.print(1, 60, "筆數：");
						this.print(0, 75, Integer.toString(countGroup), "R");
					}

					countGroup = 0;
				}

				countGroup++;

				if (countGroup == 1) {
					this.print(1, 7, bankGroup + "");
				} else {
					this.print(1, 7, " ");
				}

				this.print(0, 11, showBcDate(Integer.valueOf(l9708Vo.get("F1")), 0));
				this.print(0, 23, String.format("%07d", Integer.valueOf(l9708Vo.get("F2"))));
				this.print(0, 33, String.format("%03d", Integer.valueOf(l9708Vo.get("F3"))));
				this.print(0, 38, showBcDate(Integer.valueOf(l9708Vo.get("F4")), 0));
				this.print(0, 50, l9708Vo.get("F5"));
				this.print(0, 72, l9708Vo.get("F6"));

				countAll++;

				if (countAll == l9708List.size()) {
					this.print(1, 1, "");
					this.print(1, 60, "筆數：");
					this.print(0, 75, Integer.toString(countGroup), "R");

					this.print(1, 1, "");
					this.print(1, 60, "總 筆 數：");
					this.print(0, 75, Integer.toString(countAll), "R");
				}
			}

//			if (this.getNowPage() == 0) {
//				endfg = 9;
//			} else {
//				endfg = 1;
//			}
		} else {
			this.print(1, 1, "無資料");
			print(1, 1, "　＊＊＊ＥＮＤＯＦＲＥＰＯＲＴ＊＊＊");
		}
		long sno = this.close();

		// 輸出PDF
		// this.toPdf(sno);
	}

}
