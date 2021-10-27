package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM009ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM009Report extends MakeReport {

	@Autowired
	LM009ServiceImpl lM009ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void printHeader() {

		printHeaderP();
		this.setBeginRow(7);

		this.setMaxRows(50);
	}

	private void printHeaderP() {
		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, 50, "新光人壽保險股份有限公司", "C");
		this.print(-1, 80, "機密等級：機密");
		this.print(-2, 1, "報  表：" + this.getRptCode());
		this.print(-2, 50, "應 收 利 息 總 表", "C");
		this.print(-2, 80, "日　　期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		this.print(-3, 80, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 80, "頁　　次：" + this.getNowPage());
		this.print(-6, 50, getshowRocDate(this.getReportDate()), "C");
	}

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM009", "應收利息總表", "機密", "A4", "P");

		List<Map<String, String>> LM009List = null;
		try {
			LM009List = lM009ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lM009ServiceImpl.findAll error = " + e.toString());
		}

		if (LM009List != null && LM009List.size() != 0) {
			int i = 0;
			BigDecimal cnt = BigDecimal.ZERO;
			BigDecimal amt = BigDecimal.ZERO;
			String lastAcctItem = "";
			BigDecimal totcnt = BigDecimal.ZERO;
			BigDecimal totamt = BigDecimal.ZERO;
			this.print(1, 80, "單位：元");
			this.print(1, 10, "┌────────────────────┬───────┬──────────┐");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　　│　　金　　　　額　　│");
			for (Map<String, String> tLM009Vo : LM009List) {
				if (!lastAcctItem.equals(tLM009Vo.get("F0"))) {
					if (i != 0) {
						this.print(1, 10, "├────────────────────┼───────┼──────────┤");
						this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
						this.print(0, 33, "小　　計");

						this.print(0, 61, formatAmt(cnt, 0), "R");
						this.print(0, 80, formatAmt(amt, 0), "R");

						totcnt = totcnt.add(cnt);
						totamt = totamt.add(amt);

						cnt = BigDecimal.ZERO;
						amt = BigDecimal.ZERO;
					}
					this.print(1, 10, "├────────────────────┼───────┼──────────┤");
					this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
					
					lastAcctItem = tLM009Vo.get("F0");
					this.print(0, 15, tLM009Vo.get("F0"));
					this.print(0, 33, tLM009Vo.get("F1"));

					BigDecimal f2 = getBigDecimal(tLM009Vo.get("F2"));
					BigDecimal f3 = getBigDecimal(tLM009Vo.get("F3"));

					this.print(0, 61, formatAmt(f2, 0), "R");
					this.print(0, 80, formatAmt(f3, 0), "R");
					cnt = f2;
					amt = f3;
				} else {
					this.print(1, 10, "├────────────────────┼───────┼──────────┤");
					this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
					this.print(0, 33, tLM009Vo.get("F1"));

					BigDecimal f2 = getBigDecimal(tLM009Vo.get("F2"));
					BigDecimal f3 = getBigDecimal(tLM009Vo.get("F3"));

					this.print(0, 61, formatAmt(f2, 0), "R");
					this.print(0, 80, formatAmt(f3, 0), "R");

					cnt = cnt.add(f2);
					amt = amt.add(f3);

				}
				if (i == LM009List.size() - 1) {
					this.print(1, 10, "├────────────────────┼───────┼──────────┤");
					this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
					this.print(0, 33, "小　　計");

					this.print(0, 61, formatAmt(cnt, 0), "R");
					this.print(0, 80, formatAmt(amt, 0), "R");

					totcnt = totcnt.add(cnt);
					totamt = totamt.add(amt);

					cnt = BigDecimal.ZERO;
					amt = BigDecimal.ZERO;
				}
				i++;
			}
			this.print(1, 10, "├────────────────────┼───────┼──────────┤");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
			this.print(0, 15, "　　　　合　　　　　　計");
			this.print(0, 61, formatAmt(totcnt, 0), "R");
			this.print(0, 80, formatAmt(totamt, 0), "R");
			this.print(1, 10, "└────────────────────┴───────┴──────────┘");
//			this.print(2, 10, "協　　　　　　　經　　　　　　　副　　　　　　　襄　　　　　　　覆　　　　　　　製");
//			this.print(2, 10, "理　　　　　　　理　　　　　　　理　　　　　　　理　　　　　　　核　　　　　　　製");
//			this.print(2, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　人");
		} else {
			this.print(1, 80, "單位：元");
			this.print(0, 15, "本日無資料");
			this.print(1, 10, "┌────────────────────┬───────┬──────────┐");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　　│　　金　　　　額　　│");
			this.print(1, 10, "├────────────────────┼───────┼──────────┤");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
			this.print(1, 10, "├────────────────────┼───────┼──────────┤");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
			this.print(1, 10, "├────────────────────┼───────┼──────────┤");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
			this.print(0, 33, "小　　計");
			this.print(1, 10, "├────────────────────┼───────┼──────────┤");
			this.print(1, 10, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　　　　　│");
			this.print(0, 15, "　　　　合　　　　　　計");
			this.print(1, 10, "└────────────────────┴───────┴──────────┘");
			this.print(1, 50, "===== 報 表 結 束 =====", "C");

		}

		long sno = this.close();
		this.toPdf(sno);
	}

}
