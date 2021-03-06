package com.st1.itx.util.report;

import java.util.HashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.util.common.MakeReport;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;

@Component("warningLetterForm")
@Scope("prototype")

public class WarningLetterForm extends MakeReport {
	// 自訂表頭
	@Override
	public void printHeader() {

	}

	// 自訂明細標題
	@Override
	public void printTitle() {

	}

	public void run(TitaVo titaVo, HashMap<String, Object> map) throws LogicException {

		this.info("WarningLetter beginning");
		String iCustNo = titaVo.getParam("OOCustNo");
		String iFacmNo = titaVo.getParam("OOFacmNo");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5060", "催收存證信函" + iCustNo + "-" + iFacmNo, "", "郵局存證信函2021格式.pdf");

		newP(titaVo, map);

		int cnt = 0;
		int row = 0;
		int col = 0;

		for (int p = 1; p < 10; p++) {
			if (map.get("p" + p) == null) {
				break;
			}
			String text = map.get("p" + p).toString();

			this.info("WarningLetter p" + p + "=" + text);

			row++;
			col = 0;

			for (int i = 0; i < text.length(); i++) {
				String s = text.substring(i, i + 1);
				if (col >= 20 || row > 10) {
					if (row >= 10) {
						newP(titaVo, map);
						cnt = 0;
						row = 1;
						col = 1;
					} else {
						row++;
						col = 1;
					}
				} else {
					col++;
				}
				this.setField("W" + row + "-" + col, s);
				cnt++;
			}
		}

		long sno = this.close();

		toPdf(sno);
	}

	private void newP(TitaVo titaVo, HashMap<String, Object> map) {
		this.newPage();

		// 寄件人
		this.setField("SenderName", map.get("SenderName").toString());
		// 寄件人
		this.setField("AgentName", map.get("AgentName").toString());
		// 寄件人地址
		this.setField("SenderAddress", map.get("SenderAddress").toString());

		// 收件人1
		if (map.get("RcvName1") != null) {
			this.setField("RcvName1", map.get("RcvName1").toString());
		}
		// 收件人地1
		if (map.get("RcvAddress1") != null) {
			this.setField("RcvAddress1", map.get("RcvAddress1").toString());
		}

		// 收件人2
		if (map.get("RcvName2") != null) {
			this.setField("RcvName2", map.get("RcvName2").toString());
		}
		// 收件人地2
		if (map.get("RcvAddress2") != null) {
			this.setField("RcvAddress2", map.get("RcvAddress2").toString());
		}

		// 收件人3
		if (map.get("RcvName3") != null) {
			this.setField("RcvName3", map.get("RcvName3").toString());
		}
		// 收件人地3
		if (map.get("RcvAddress3") != null) {
			this.setField("RcvAddress3", map.get("RcvAddress3").toString());
		}

	}

}
