package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.service.springjpa.cm.LM060ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM060Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM060Report.class);

	@Autowired
	LM060ServiceImpl lm060ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

//	自訂表頭
	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");
		// 設定字體大小
		this.setFontSize(16);
 
		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(2);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public void exec(TitaVo titaVo, TxCom txcom) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM060Report exec txcom " + txcom);
//		this.info("LM060Report exec titaVo" + titaVo);
		String iENTDY = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6) + "."
				+ titaVo.get("ENTDY").substring(6, 8);

		String iLMONDY = String.valueOf(txcom.getLmndy());

		iLMONDY = iLMONDY.substring(0, 3) + "." + iLMONDY.substring(3, 5) + "." + iLMONDY.substring(5, 7);

		this.info("LM060Report exec IENTDY=" + iENTDY + ",ILMONDY=" + iLMONDY);

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM060", "轉銷呆帳備忘錄", "密", "A4", "P");

		try {
			fnAllList = lm060ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM060ServiceImpl.findAll error = " + errors.toString());
		}

		Map<String, String> tLVo = fnAllList.get(0);

		if (fnAllList.size() > 0) {
			this.print(1, 6, iENTDY);

			this.print(1, 6, "");
			this.print(1, 6, "暫付款--法務課暨催收款項-法務費用調節總表：");
			this.print(3, 6, "");
			if(tLVo.get("F8")=="0") {
				this.print(0, 10, "本日無資料");
				
			}
			this.print(1, 28, "前期餘額：" + iLMONDY + "  ＝");
			this.print(0, 67, showAmt(tLVo.get("F0")), "R");
			this.print(2, 6, "");
			this.print(1, 6, "一般電腦帳 【借方】：    暫付款項--法務費用");
			this.print(0, 67, showAmt(tLVo.get("F1")), "R");
			this.print(1, 31, "催收款項--法務費用");
			this.print(0, 67, showAmt(tLVo.get("F2")), "R");
			this.print(2, 6, "");
			this.print(1, 17, "【貸方】：    暫付款項--法務費用");
			this.print(0, 67, showAmt(tLVo.get("F3")), "R");
			this.print(1, 31, "催收款項--法務費用");
			this.print(0, 67, showAmt(tLVo.get("F4")), "R");
			this.print(2, 6, "");
			this.print(1, 28, iENTDY + "  餘額合計  ＝");
			this.print(0, 67, showAmt(tLVo.get("F5")), "R");
			this.print(1, 6, "");
			this.print(1, 6, "────────────────────────────────");
			this.print(1, 6, "");
			this.print(1, 6, "1、暫付款及待結轉帳__放款法務費用");
			this.print(0, 67, showAmt(tLVo.get("F6")), "R");
			this.print(1, 6, "2、催收款項__法務費用 ");
			this.print(0, 67, showAmt(tLVo.get("F7")), "R");
			this.print(1, 6, "");
			this.print(1, 6, "────────────────────────────────");
			this.print(1, 6, "");
			this.print(1, 28, iENTDY + "  餘額合計  ＝");
			this.print(0, 67, showAmt(tLVo.get("F8")), "R");
			this.print(3, 6, "");
			if (tLVo.get("F8").equals(tLVo.get("F5"))) {
				this.print(1, 6, "至" + iENTDY + ".止放款法務費用總額為 $" + showAmt(tLVo.get("F8")) + "會計科子目數字相符。");
			} else {
				this.print(1, 6, "至" + iENTDY + ".止放款法務費用總額為 $" + showAmt(tLVo.get("F8")) + "會計科子目數字不相符。");
			}

			this.print(1, 6, "陳  核");
//			this.print(1, 3, "");
//			this.print(1, 3, "");
//			this.print(1, 3, "");
//			this.print(1, 3, "經辦人：                        經理：");
//			for(int i=0;i<tLVo.size();i++) {
//				this.info("F" +i+":"+ tLVo.get(("F"+i).toString()));
//			}
		}
		long sno = this.close();
		this.toPdf(sno);
	}

	private String showAmt(String xamt) {
//		this.info("LM060Report toPdf showRocDate1 = " + xamt);
		if (xamt == null || xamt.equals("") || xamt.equals("0")) {
			return "0";
		}
		BigDecimal amt = new BigDecimal(xamt);

		DecimalFormat df = new DecimalFormat("#,##0");
		return df.format(amt);
	}
}
