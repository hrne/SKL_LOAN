package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9132ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")
public class L9132ReportD extends MakeReport {

	@Autowired
	L9132ServiceImpl sL9132ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9132D";
	private String reportItem = "放款部日計表(總帳)";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	private String batchNo;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// 自訂表頭
	@Override
	public void printHeader() {
		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 1, "報　表：" + this.reportCode);
		this.print(-2, this.getMidXAxis(), this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 1, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, this.getMidXAxis(), showRocDate(this.reportDate), "C");

		// 印明細表頭
		this.printDetailHeader();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(34);
	}

	@Override
	public void printContinueNext() {
		this.print(1, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

	private void printDetailHeader() {
		print(1, 1, "傳票批號：　" + this.batchNo);
		print(1, 1, "");
		print(1, 1, "┌—————————————┬—————————————————————————————————————————┬————————┬—————————————┐");
		print(1, 1, "｜　　　　　支出（借）　　　｜　　　　　　　　　　　　　　　　　科　　　　　目　　　　　　　　　　　　　　　　　｜　　區隔帳冊　　｜　　　　　收入（貸）　　　｜");
		print(1, 1, "├—————————————┼—————————————————————————————————————————┼————————┼—————————————┤");
		// -------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		// ----------12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9132ReportA exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		setCharSpaces(0);

		// 傳票批號篩選
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		this.batchNo = FormatUtil.pad9(titaVo.getParam("BatchNo"), 2);

		List<Map<String, String>> resultList = null;

		// 查資料庫
		resultList = sL9132ServiceImpl.doQueryL9132D(this.reportDate, iBatchNo, titaVo);

		BigDecimal dbAmtTotal = BigDecimal.ZERO;
		BigDecimal crAmtTotal = BigDecimal.ZERO;

		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, String> result : resultList) {
				print(1, 1, "｜　　　　　　　　　　　　　｜　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　｜　　　　　　　　｜　　　　　　　　　　　　　｜");
				String acNo = result.get("AcNo") == null ? "" : result.get("AcNo");
				String acSubBookItem = result.get("AcSubBookItem") == null ? "" : result.get("AcSubBookItem");
				BigDecimal dbAmt = getBigDecimal(result.get("DbAmt"));
				BigDecimal crAmt = getBigDecimal(result.get("CrAmt"));

				print(0, 31, acNo);
				print(0, 119, acSubBookItem);
				print(0, 23, formatAmt(dbAmt, 0), "R");
				print(0, 153, formatAmt(crAmt, 0), "R");

				// 加總
				dbAmtTotal = dbAmtTotal.add(dbAmt);
				crAmtTotal = crAmtTotal.add(crAmt);
			}
		} else {
			// 本日無資料
			print(1, 1, "｜　　　　　　　　　　　　　｜　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　｜　　　　　　　　｜　　　　　　　　　　　　　｜");
			print(1, 3, "本日無資料");
		}
		// 印總計
		print(1, 1, "├—————————————┼—————————————————————————————————————————┼————————┼—————————————┤");
		print(1, 1, "｜　　　　　　　　　　　　　｜　　　　　　　　　　　　　　　　　合　　　　　計　　　　　　　　　　　　　　　　　｜　　　　　　　　｜　　　　　　　　　　　　　｜");
		print(0, 23, formatAmt(dbAmtTotal, 0), "R");
		print(0, 153, formatAmt(crAmtTotal, 0), "R");
		print(1, 1, "└—————————————┴—————————————————————————————————————————┴————————┴—————————————┘");

	}
}
