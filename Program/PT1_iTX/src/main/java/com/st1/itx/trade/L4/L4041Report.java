package com.st1.itx.trade.L4;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L4041Report extends MakeReport {

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	CdCodeService cdCodeService;

	@Autowired
	CdEmpService cdEmpService;

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	int propDate = 0;

//	自訂表頭
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(58);
	}

	public void printHeaderL() {
		this.print(-3, 5, "程式ID：" + this.getParentTranCode());
		this.print(-3, 50, "新光人壽保險股份有限公司", "C");
		this.print(-4, 5, "報  表：L4041");
		this.print(-4, 44, "授權資料明細表");
		this.print(-3, 80, "報表等級：機密");
		String bcDate = dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6, 8)
				+ "/" + dDateUtil.getNowStringBc().substring(2, 4);
		this.print(-4, 80, "日　　期：" + bcDate);
		this.print(-5, 80, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-6, 80, "頁　　數：　	　" + this.getNowPage());
	}

	public void exec(List<Map<String, String>> ListResult, TitaVo titaVo) throws LogicException {

		this.info("L4041Report Start, size=" + ListResult.size());

		if (ListResult != null && ListResult.size() > 0) {
			makeReport(ListResult, titaVo);
		}

	}

	public void makeReport(List<Map<String, String>> ListResult, TitaVo titaVo) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4041", "授權資料明細表", "", "A4", "P");

		this.print(-9, 3, "戶號     扣款人ID 　   郵局存款別　　扣款帳號　　　　　授權方式　　授權類別　 建檔人員　　建檔日期");
		this.print(-10, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		for (Map<String, String> result : ListResult) {
			propDate = parse.stringToInteger(result.get("F16"));

			if (!"Y".equals(result.get("F13")) && propDate > 0) {

				// 戶號
				print(1, 3, padStart(result.get("F2"), 7, "0"));

				// 扣款人ID
				print(0, 12, result.get("F7"), "L");

				// 郵局存款別
				print(0, 29, result.get("F3"));

				// 扣款帳號
				print(0, 38, result.get("F4"));

				// 授權方式
				print(0, 56, "紙本");

				// 授權類別
				String iAuthCode = result.get("F5");
				Slice<CdCode> tCdCode = cdCodeService.defCodeEq("AuthCode", iAuthCode, 0, Integer.MAX_VALUE, titaVo);
				if (tCdCode != null) {
					iAuthCode = tCdCode.getContent().get(0).getItem();
				} else {
					iAuthCode = "";
				}
				print(0, 67, iAuthCode);

				// 建檔人員
				String iEmp = result.get("F25");
				CdEmp tCdEmp = cdEmpService.findById(iEmp, titaVo);
				if (tCdEmp != null) {
					iEmp = tCdEmp.getFullname();
				} else {
					iEmp = "";
				}
				print(0, 76, iEmp);

				// 建檔日期
				String iAuthCreatDate = result.get("F0");
				this.info("iAuthCreatDate=" + iAuthCreatDate);
				iAuthCreatDate = showDate(iAuthCreatDate, 1);
				print(0, 87, iAuthCreatDate);

				// 檢查列數
				checkRow();
			}

		}

		this.print(-64, 50, "===== 報　表　結　束 =====", "C");

	}

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

	/**
	 * 檢查列數
	 * 
	 */
	private void checkRow() {
		if (this.NowRow >= 60) {

			newPage();
			this.print(-9, 3, "戶號 扣款人ID 　郵局存款別　　扣款帳號　　　　　授權方式　　　　授權類別　建檔人員　　　建檔日期");
			this.print(-10, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		}

	}

	private String showDate(String date, int iType) {
		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
			return " ";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
		if (rocdatex.length() == 7) {
			return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
		} else {
			return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);

		}

	}

}
