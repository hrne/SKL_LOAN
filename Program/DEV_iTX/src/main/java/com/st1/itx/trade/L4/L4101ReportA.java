package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L4101ReportA extends MakeReport {

	/* DB服務注入 */
	@Autowired
	SlipMediaService slipMediaService;
	@Autowired
	AcDetailService acDetailService;
	@Autowired
	AcCloseService acCloseService;
	@Autowired
	CdAcCodeService cdAcCodeService;

	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L4101";
	private String reportItem = "撥款傳票總表";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int acDate = 0;
	String batchNo = "";
	List<String> tempList = null;
	HashMap<String, BigDecimal> dbAmt = new HashMap<>();
	HashMap<String, BigDecimal> crAmt = new HashMap<>();
	HashMap<String, String> relTxSeq = new HashMap<>();
	HashMap<String, Integer> cntR = new HashMap<>();
	HashMap<String, String> slipNo = new HashMap<>();
	int cnt = 0; // 總筆數
	BigDecimal sumDbAmt = BigDecimal.ZERO;// 借方總金額
	BigDecimal sumCrAmt = BigDecimal.ZERO;// 貸方總金額

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L4101ReportA.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 2, "批號：" + titaVo.getBacthNo());
		this.print(-4, 145, "頁　　次：" + this.getNowPage());

		// 頁首帳冊別判斷
//		print(-4, 10, this.nowAcBookCode);
//		print(-4, 14, this.nowAcBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "　日期　　科子細目名稱　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　借方金額　　　　　　貸方金額　");
		print(1, 1, "－－－－　－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－　－－－－－－－－－　－－－－－－－－－　");

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4101ReportA exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = titaVo.getBacthNo();
		reportCode = titaVo.getTxcd();
		reportCode = reportCode + "-" + batchNo;
		// 分錄
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
		this.info("L4101ReportA BatchNo = " + batchNo);
		Slice<AcDetail> slAcDetail = acDetailService.acdtlTitaBatchNo(titaVo.getAcbrNo(), titaVo.getCurName(), acDate,
				batchNo, 0, Integer.MAX_VALUE, titaVo);
		lAcDetail = slAcDetail == null ? null : new ArrayList<AcDetail>(slAcDetail.getContent());

		if (lAcDetail == null || lAcDetail.isEmpty()) {
			// 出空表
			this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);
			this.setCharSpaces(0);
			print(1, 1, "本日無資料");
			return;
		}

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);
		tempList = procReportA(lAcDetail, titaVo);
		for (String tempL4101Vo : tempList) {
			print(1, 1, "　　　　　　           ");

			String acNoCode = tempL4101Vo.substring(0, 11);
			String acSubCode = tempL4101Vo.substring(11, 16);
			String acDtlCode = "  ";
			CdAcCode tCdAcCode = cdAcCodeService.findById(new CdAcCodeId(acNoCode, acSubCode, acDtlCode), titaVo);
			// 明細資料第一行
//			print(1, 1, "　　");
			print(0, 1, this.showRocDate(acDate, 1)); // 日期
			print(0, 11, tempL4101Vo + " " + tCdAcCode.getAcNoItem()); // 科子細目+科子細目名稱

			print(0, 105, formatAmt(dbAmt.get(tempL4101Vo), 0), "R"); // 借方金額
			print(0, 125, formatAmt(crAmt.get(tempL4101Vo), 0), "R");// 貸方金額

			if (dbAmt.get(tempL4101Vo) != null) {
				sumDbAmt = sumDbAmt.add(dbAmt.get(tempL4101Vo));
			}
			if (crAmt.get(tempL4101Vo) != null) {
				sumCrAmt = sumCrAmt.add(crAmt.get(tempL4101Vo));
			}

		}

		print(1, 1, "－－－－　－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－　－－－－－－－－－　－－－－－－－－－　");
		print(1, 1, "　　　　　　           ");

//		print(0, 61, currencyCode);
		print(0, 105, formatAmt(sumDbAmt, 0), "R");// 借方金額加總
		print(0, 125, formatAmt(sumCrAmt, 0), "R");// 貸方金額加總

	}

	private List<String> procReportA(List<AcDetail> lAcDetail, TitaVo titaVo) {
		dbAmt = new HashMap<>();
		crAmt = new HashMap<>();
		relTxSeq = new HashMap<>();
		cntR = new HashMap<>();
		slipNo = new HashMap<>();
		cnt = 0;
		if (lAcDetail.size() > 0) {
			for (AcDetail tAcDetail : lAcDetail) {
				String acNo = FormatUtil.padX(tAcDetail.getAcNoCode(), 11)
						+ FormatUtil.padX(tAcDetail.getAcSubCode(), 5);
				String slip = parse.IntegerToString(tAcDetail.getSlipBatNo(), 2)
						+ parse.IntegerToString(tAcDetail.getSlipNo(), 6);

				if (dbAmt.containsKey(acNo) || crAmt.containsKey(acNo)) {
					if ("D".equals(tAcDetail.getDbCr())) {
						dbAmt.put(acNo, dbAmt.get(acNo).add(tAcDetail.getTxAmt()));
					}
					if ("C".equals(tAcDetail.getDbCr())) {
						crAmt.put(acNo, crAmt.get(acNo).add(tAcDetail.getTxAmt()));
					}
				} else {
					if ("D".equals(tAcDetail.getDbCr())) {
						dbAmt.put(acNo, tAcDetail.getTxAmt());
					}
					if ("C".equals(tAcDetail.getDbCr())) {
						crAmt.put(acNo, tAcDetail.getTxAmt());
					}
				}
				relTxSeq.put(acNo, tAcDetail.getRelTxseq());
				slipNo.put(acNo, slip);

				if (!cntR.containsKey(tAcDetail.getRelTxseq())) {
					cntR.put(tAcDetail.getRelTxseq(), 1);
					cnt = cnt + 1;
				}

			}

			Set<String> tempSet = slipNo.keySet();

			tempList = new ArrayList<>();

			for (Iterator<String> it = tempSet.iterator(); it.hasNext();) {
				String tmpBatxVo = it.next();
				tempList.add(tmpBatxVo);
			}

			if (tempList != null && tempList.size() != 0) {
//				sort by acNoCode, acSubCode, acDtlCode
				tempList.sort((c1, c2) -> {
					int result = 0;
					if (c1.substring(0, 16).compareTo(c2.substring(0, 16)) != 0) {
						result = c1.substring(0, 16).compareTo(c2.substring(0, 16));
					}
					return result;
				});

			}
		} else {
			this.info("reportA no data !!");
		}
		return tempList;
	}

}
