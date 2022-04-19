package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9715ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9715Report extends MakeReport {

	@Autowired
	L9715ServiceImpl l9715ServiceImpl;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	Parse parse;

	private String name = "";

	private String fundDay = "";
	private String approDay = "";

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL1();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	public void printHeaderL1() {

		this.setFontSize(8);
		this.setCharSpaces(0);
		this.print(-1, 184, "機密等級：機密");
		this.print(-2, 184, "基　礎：" + showBcDate(fundDay, 0));
		this.print(-3, 8, "程式ID：" + this.getParentTranCode());
		this.print(-3, 100, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dDateUtil.getNowStringBc().substring(4, 6)));
		this.print(-3, 184, "日　期：" + tim + "/" + dDateUtil.getNowStringBc().substring(6) + "/" + dDateUtil.getNowStringBc().substring(2, 4));
		this.print(-4, 8, "報　表：" + this.getRptCode());
		this.print(-4, 100, "業務專辦照顧十八個月明細表", "C");
		this.print(-4, 184, "時　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-5, 184, "頁　數：　" + this.getNowPage());
		this.print(-6, 6, "經辦：");
		this.print(-6, 100, "撥款日期　" + showBcDate(approDay, 0) + "　起", "C");
//		this.print(-6, 100, "撥款日期　" + dDateUtil.getNowStringBc().substring(0, 4) + "/" + dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6) + "　起", "C");
//		this.print(-6, 100, "撥款日期　" + dDateUtil.getNowStringBc().substring(0, 4) + "/" + dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6) + "　起", "C");
		this.print(-6, 184, "單　位：元");
//		this.print(-8, 6, " 押品　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　最近　　逾期");
		this.print(-8, 6, "擔保品");
		this.print(-9, 6, "地區別");
		this.print(-9, 14, "繳款方式");
		this.print(-9, 24, "戶號");
		this.print(-9, 38, "戶名");
		this.print(-9, 55, "初貸日");
		this.print(-9, 65, "本金餘額");
		this.print(-9, 75, "利率");
		this.print(-9, 85, "繳息迄日");
		this.print(-8, 95, "最近");
		this.print(-9, 95, "應繳日");
		this.print(-8, 105, "逾期");
		this.print(-9, 105, "日數");
		this.print(-9, 115, "未收本息");
		this.print(-9, 125, "違約金");
		this.print(-9, 135, "溢短繳");
		this.print(-9, 150, "合計");
		this.print(-9, 160, "連絡人");
		this.print(-9, 170, "電話");
		this.print(-9, 192, "追蹤情形");
//		this.print(-9, 6,
//				"地區別　繳款方式　　戶號　　　　戶名　　　　初貸日　　　本金餘額　 利率　 繳息迄日　   應繳日　  日數　　　  未收本息　　違約金　　溢短款　　　　合計　連絡人　　電話　　　　　　追蹤情形");
		this.print(-10, 5,
				"------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9715", "業務專辦照顧十八個月明細表", "密", "A4", "L");

		// 基礎日期
		fundDay = titaVo.getParam("FUND_DAY");

		// 撥款日期
		approDay = titaVo.getParam("APPRO_DAY");

		this.setFontSize(8);

		List<Map<String, String>> l9715List = null;

		try {

			l9715List = l9715ServiceImpl.findAll(titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9715ServiceImpl.findall error = " + errors.toString());

		}

		if (l9715List.size() != 0) {

			String nameNo = "";
			boolean openClose = true;

			for (Map<String, String> tL9715 : l9715List) {
				// input:
				// entryDate 入帳日期
//					CustNo	       戶號
//					FacmNo	       額度
//					BormNo	       撥款
//					RepayType	還款類別
//						01-期款        
//						02-部分償還    
//						03-結案
//						04-帳管費
//						05-火險費
//						06-契變手續費
//						07-法務費
//						09-其他
//						11-債協匯入款
//					TxAmt 	回收金額

				List<BaTxVo> listBaTxVo = new ArrayList<>();

				dBaTxCom.setTxBuffer(txbuffer);

				int custNo = parse.stringToInteger(tL9715.get("F3"));
				int facmNo = parse.stringToInteger(tL9715.get("F4"));

				int tbsdy = parse.stringToInteger(fundDay);

				this.info("L9715Report1 tL9715.custNo = " + custNo);
				this.info("L9715Report1 tL9715.facmNo = " + facmNo);
				this.info("L9715Report1 fundDay = " + tbsdy);

				try {
					listBaTxVo = dBaTxCom.settingUnPaid(tbsdy, custNo, facmNo, 0, 1, BigDecimal.ZERO, titaVo);

				} catch (LogicException e) {
					this.info("baTxCom.setTxBuffer ErrorMsg :" + e.getMessage());
				}

				this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
				this.info("listBaTxVo-------->" + listBaTxVo.toString());

				BigDecimal unpaidPriInt = BigDecimal.ZERO; // 未收本息
				BigDecimal breachAmtAndDelayInt = BigDecimal.ZERO; // 違約金(報表最終產出的違約金包含違約金及延遲息)
				BigDecimal overflow = BigDecimal.ZERO; // 溢短繳
				BigDecimal total; // 合計

				// 未收本息 = 本金+利息 baTxVo.Principal + baTxVo.Interest
				// 違約金 有 但要扣成 0 baTxVo.BreachAmt
				// 溢短繳 baTxVo.UnPaidAmt
				for (BaTxVo baTxVo : listBaTxVo) {
					int dataKind = baTxVo.getDataKind();
					int repayType = baTxVo.getRepayType();

					if (dataKind == 2 && repayType == 1) {
						unpaidPriInt = unpaidPriInt.add(baTxVo.getPrincipal()); // 未收本
						unpaidPriInt = unpaidPriInt.add(baTxVo.getInterest()); // 未收息
						breachAmtAndDelayInt = breachAmtAndDelayInt.add(baTxVo.getBreachAmt()); // 違約金
						breachAmtAndDelayInt = breachAmtAndDelayInt.add(baTxVo.getDelayInt()); // 違約金

					}

					// 溢短繳 = 溢繳 - 短繳

					// 溢繳 dataKind = 3.暫收抵繳
					if (dataKind == 3) {
						overflow = overflow.add(baTxVo.getUnPaidAmt());
					}

					// 短繳
					// dataKind = 1.應收費用+未收費用+短繳期金
					// repayType = 01 期款
					if (dataKind == 1 && repayType == 1) {
						overflow = overflow.subtract(baTxVo.getUnPaidAmt());
					}

				}

				total = unpaidPriInt.add(breachAmtAndDelayInt).add(overflow); // 合計

				name = tL9715.get("F0");

				// 經辦 F0
				if (!nameNo.equals(tL9715.get("F0"))) {
					nameNo = tL9715.get("F0");
					if (openClose) {
						this.print(-6, 11, name);
						this.print(-10, 10, "");
						openClose = false;
					} else {
						this.newPage();
						this.print(-6, 11, name);
						this.print(-10, 10, "");
					}
				}

				// 押品地區別 F1
				this.print(1, 6, tL9715.get("F1"));
				// 繳款方式 F2
				this.print(0, 14, tL9715.get("F2"));
				// 戶號 F3 + F4
				this.print(0, 24, String.format("%07d", Integer.parseInt(tL9715.get("F3"))) + "-" + String.format("%03d", Integer.parseInt(tL9715.get("F4"))));
				// 戶名 F5
				this.print(0, 37, tL9715.get("F5"));

				// 初貸日 F6
				this.print(0, 62, showRocDate(tL9715.get("F6"), 1), "R");

				// 本金餘額 F7
				this.print(0, 74, formatAmt(tL9715.get("F7"), 0), "R");

				// 利率 F8
				this.print(0, 82, formatAmt(tL9715.get("F8"), 4), "R");

				// 繳息迄日 F9
				this.print(0, 93, showRocDate(tL9715.get("F9"), 1), "R");

				// 最近應繳日 F10
				this.print(0, 104, showRocDate(tL9715.get("F10"), 1), "R");

				// 逾期日數 F11
				this.print(0, 109, tL9715.get("F11"), "R");

				// 未收本息
				this.print(0, 123, formatAmt(unpaidPriInt, 0), "R");

				// 違約金
				this.print(0, 131, formatAmt(breachAmtAndDelayInt, 0), "R");

				// 溢短繳
				this.print(0, 141, formatAmt(overflow, 0), "R");

				// 合計
				this.print(0, 154, formatAmt(total, 0), "R");

				// 聯絡人 F12
				this.print(0, 160, tL9715.get("F12"));
				// 電話 F13
				this.print(0, 170, tL9715.get("F13"));

				// 其他電話 F14
				this.print(1, 38, "其他電話：");
				this.print(0, 49, tL9715.get("F14"));

				this.print(1, 5,
						"------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			} // for
		} else {

			this.print(-6, 6, "經辦：" + name);
			this.print(1, 8, "本日無資料");

		}

		long sno = this.close();

		this.toPdf(sno);

	}

}
