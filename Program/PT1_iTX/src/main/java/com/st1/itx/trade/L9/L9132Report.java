package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L9132ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")
public class L9132Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	AcDetailService sAcDetailService;

	/* DB服務注入 */
	@Autowired
	CdEmpService sCdEmpService;

	/* DB服務注入 */
	@Autowired
	CdAcCodeService sCdAcCodeService;

	@Autowired
	L9132ServiceImpl l9132ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9132";
	private String reportItem = "傳票媒體明細表(總帳)";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	private String batchNo;

	private String acNoCode;

	private String acNoItem;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// 子目小計
	int tempAcSubCodeCount = 0;
	int tempAcSubCodeDbAmtTotal = 0;
	int tempAcSubCodeCrAmtTotal = 0;
	// 科目小計
	int tempAcNoCodeCount = 0;
	int tempAcNoCodeDbAmtTotal = 0;
	int tempAcNoCodeCrAmtTotal = 0;
	// 總計
	int count = 0;
	int dbAmtTotal = 0;
	int crAmtTotal = 0;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9132Report.printHeader");

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

		// 明細起始列(自訂亦必須)
		this.setBeginRow(5);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(39);

	}

	@Override
	public void printContinueNext() {
		this.print(1, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

	/**
	 * 表頭列印
	 */
	public void batchTitle() {
		print(1, 1, "傳票批號：");
		print(0, 13, batchNo);
		print(1, 1, "會計科目：");
		print(0, 13, acNoCode);
		print(0, 26, acNoItem);
		print(1, 1, "子目　　　　　　　　　　　　　　　　　　　　　傳票號碼　　　　區隔帳冊　　　　　　戶號　　　　　　　　借方金額　　　　　　　　　貸方金額　　　　　　　經辦");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");

	}

	/**
	 * 子目小計列印
	 */
	private void acSubCodeCalculate() {
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
		print(1, 1, "子目小計　　　　　　　　　　筆");
		// 筆數
		print(0, 26, String.valueOf(tempAcSubCodeCount), "R");
		// 借方金額
		print(0, 118, formatAmt(String.valueOf(tempAcSubCodeDbAmtTotal), 0), "R");
		// 貸方金額
		print(0, 144, formatAmt(String.valueOf(tempAcSubCodeCrAmtTotal), 0), "R");

		tempAcSubCodeCount = 0;
		tempAcSubCodeDbAmtTotal = 0;
		tempAcSubCodeCrAmtTotal = 0;
	}

	/**
	 * 科目小計列印
	 */
	private void acNoCodeCalculate() {
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
		print(1, 1, "科目小計　　　　　　　　　　筆");

		// 筆數
		print(0, 26, String.valueOf(tempAcNoCodeCount), "R");
		// 借方金額
		print(0, 118, formatAmt(String.valueOf(tempAcNoCodeDbAmtTotal), 0), "R");
		// 貸方金額
		print(0, 144, formatAmt(String.valueOf(tempAcNoCodeCrAmtTotal), 0), "R");

		tempAcNoCodeCount = 0;
		tempAcNoCodeDbAmtTotal = 0;
		tempAcNoCodeCrAmtTotal = 0;

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9132Report exec ...");
		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		// 傳票批號篩選
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		this.batchNo = FormatUtil.pad9(titaVo.getParam("BatchNo"), 2);

		List<Map<String, String>> resultList = null;

		// 查資料庫
		resultList = l9132ServiceImpl.doQueryL9132(this.reportDate, iBatchNo, titaVo);

		if (resultList == null || resultList.size() == 0) {
			// 本日無資料
			batchTitle();
			print(1, 1, "本日無資料");

		} else {
			int tempPage = 0;

			int i = 0;

//			String tempAcNoCode = "";
			String tempAcSubCode = "";
			for (Map<String, String> r : resultList) {
				// 計算用
				i++;

				
				tempAcSubCode = r.get("AcSubCode");
				
				if (!r.get("AcNoCode").equals(acNoCode)) {
					// 會計科目
					acNoCode = r.get("AcNoCode");
					acNoItem = "10121100000".equals(acNoCode) ? r.get("AcNoItem").substring(0, 12) : r.get("AcNoItem");

					// 科目
					if (i > 1) {
						acSubCodeCalculate();
						acNoCodeCalculate();
						this.newPage();
					}

					batchTitle();

				} else {

					this.info("AcSubCode=" + tempAcSubCode + " === " + r.get("AcSubCode") + " : " + i);

					// 子目
					if (!r.get("AcSubCode").equals(tempAcSubCode) && i > 2) {
						acSubCodeCalculate();
					}


				}

				
				
				// 子目
				String acSubCode = r.get("AcSubCode");
				// 傳票號碼
				String slipNo = "0".equals(r.get("SlipNo")) ? " " : r.get("SlipNo");

				// 資金來源(帳冊別)
//				String acSubBookCode = r.get("AcSubBookCode");
				String acSubBookItem = r.get("AcSubBookItem");

				// 戶號
				String custNo = r.get("CustNo");

				// 借貸方金額
				BigDecimal dbAmt = "0".equals(r.get("DbAmt")) ? BigDecimal.ZERO : new BigDecimal(r.get("DbAmt"));
				BigDecimal crAmt = "0".equals(r.get("CrAmt")) ? BigDecimal.ZERO : new BigDecimal(r.get("CrAmt"));

				// 借貸方金額(計算用)
				int tDbAmt = Integer.valueOf(r.get("DbAmt")) == 0 ? 0 : Integer.valueOf(r.get("DbAmt"));
				int tCrAmt = Integer.valueOf(r.get("CrAmt")) == 0 ? 0 : Integer.valueOf(r.get("CrAmt"));
				
				// 經辦
				String empName = r.get("EmpName");

				if (this.NowRow == 40) {
					this.newPage();
					tempPage = this.NowRow;
					batchTitle();
				}

				print(1, 1, "　　");
				// 子目
				print(0, 1, acSubCode);
				// 傳票號碼
				print(0, 56, slipNo, "R");
				// 區隔帳冊
				print(0, 67, acSubBookItem,"C");
				// 戶號
				print(0, 82, custNo);
				// 貸方金額
				print(0, 118, formatAmt(dbAmt, 0), "R");
				// 貸方金額
				print(0, 144, formatAmt(crAmt, 0), "R");
				// 經辦
				print(0, 150, empName);

				// 子目小計
				tempAcSubCodeCount++;
				tempAcSubCodeDbAmtTotal = tempAcSubCodeDbAmtTotal + tDbAmt;
				tempAcSubCodeCrAmtTotal = tempAcSubCodeCrAmtTotal + tCrAmt;


				// 科目小計
				tempAcNoCodeCount++;
				tempAcNoCodeDbAmtTotal = tempAcNoCodeDbAmtTotal + tDbAmt;
				tempAcNoCodeCrAmtTotal = tempAcNoCodeCrAmtTotal + tCrAmt;

				// 總計
				count++;
				dbAmtTotal = dbAmtTotal + tDbAmt;
				crAmtTotal = crAmtTotal + tCrAmt;

				if (resultList.size() == i) {
					acSubCodeCalculate();
					acNoCodeCalculate();
				}

			}
			print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
			print(1, 1, "總　　計　　　　　　　　　　筆");
			// 筆數
			print(0, 26, String.valueOf(count), "R");
			// 借方金額
			print(0, 118, formatAmt(String.valueOf(dbAmtTotal), 0), "R");
			// 貸方金額
			print(0, 144, formatAmt(String.valueOf(crAmtTotal), 0), "R");

		}

	}

	public void exec2(TitaVo titaVo) throws LogicException {
		this.info("L9132Report exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		// 傳票批號篩選
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		this.batchNo = FormatUtil.pad9(titaVo.getParam("BatchNo"), 2);

		// 查AcDetail
		Slice<AcDetail> slAcDetail = sAcDetailService.findL9RptData(this.reportDate, iBatchNo, 0, Integer.MAX_VALUE,
				titaVo);
		List<AcDetail> lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

		if (lAcDetail == null || lAcDetail.size() == 0) {
			// 本日無資料
			print(1, 1, "傳票批號：");
			print(1, 1, "會計科目：");
			print(1, 1, "子目　　　　　　　　　　　　　　　　　　　　　傳票號碼　　　　區隔帳冊　　　　　　戶號　　　　　　　　借方金額　　　　　　　　　貸方金額　　　　　　　經辦");
			print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
			print(1, 1, "本日無資料");

		} else {

			// 明細資料容器(以子目為Key)
			TreeMap<String, List<AcDetail>> detailDataMap = new TreeMap<>();

			// 子目小計容器(以子目小計)
			TreeMap<AcSubCodeVo, L9132RptDataVo> acSubCodeMap = new TreeMap<>();

			// 科目小計容器(以科目小計)
			TreeMap<String, L9132RptDataVo> acNoCodeMap = new TreeMap<>();

			// 總計容器
			L9132RptDataVo sumDataVo = new L9132RptDataVo();

			for (AcDetail tAcDetail : lAcDetail) {

				String acNoCode = tAcDetail.getAcNoCode();
				String acSubCode = tAcDetail.getAcSubCode();
				String dbCr = tAcDetail.getDbCr().trim();
				BigDecimal txAmt = tAcDetail.getTxAmt();

				AcSubCodeVo tmpAcSubCodeVo = new AcSubCodeVo(acNoCode, acSubCode);

				String stringAcSubCodeVo = tmpAcSubCodeVo.toString();

				List<AcDetail> tmpList = new ArrayList<>();

				// 明細資料
				if (detailDataMap.containsKey(stringAcSubCodeVo)) {
					tmpList = detailDataMap.get(stringAcSubCodeVo);
					this.info("L9132Report contains");
				}
				tmpList.add(tAcDetail);
				detailDataMap.put(stringAcSubCodeVo, tmpList);

				L9132RptDataVo tmpDataVo = new L9132RptDataVo();

				tmpDataVo.setCnt(1);
				if (dbCr.equals("D")) {
					tmpDataVo.setDbAmt(txAmt);
					tmpDataVo.setCrAmt(new BigDecimal(0));
				}
				if (dbCr.equals("C")) {
					tmpDataVo.setDbAmt(new BigDecimal(0));
					tmpDataVo.setCrAmt(txAmt);
				}

				// 子目小計
				L9132RptDataVo acSubCodeDataVo = tmpDataVo.clone();
				if (acSubCodeMap.containsKey(tmpAcSubCodeVo)) {
					L9132RptDataVo oldDataVo = acSubCodeMap.get(tmpAcSubCodeVo);
					acSubCodeDataVo.add(oldDataVo);
				}
				acSubCodeMap.put(tmpAcSubCodeVo, acSubCodeDataVo);

				// 科目小計
				L9132RptDataVo acNoCodeDataVo = tmpDataVo.clone();
				if (acNoCodeMap.containsKey(acNoCode)) {
					L9132RptDataVo oldDataVo = acNoCodeMap.get(acNoCode);
					acNoCodeDataVo.add(oldDataVo);
				}
				acNoCodeMap.put(acNoCode, acNoCodeDataVo);

				// 總計
				sumDataVo.add(tmpDataVo);
			}

			this.info("L9132Report detailDataMap = " + detailDataMap.toString());

			int pgGrp = 1;

			// 以科目為群組換頁
			for (Iterator<Entry<String, L9132RptDataVo>> it = acNoCodeMap.entrySet().iterator(); it.hasNext();) {
				Entry<String, L9132RptDataVo> tmpEntry = it.next();

				String acNoCode = tmpEntry.getKey();
				String acNoItem = "";
				Slice<CdAcCode> slCdAcCode = sCdAcCodeService.findAcCode(acNoCode, acNoCode, "     ", "     ", "  ",
						"  ", 0, Integer.MAX_VALUE, titaVo);
				List<CdAcCode> lCdAcCode = slCdAcCode == null ? null : slCdAcCode.getContent();
				if (lCdAcCode == null || lCdAcCode.size() == 0) {
					acNoItem = "";
				} else {
					CdAcCode tCdAcCode = lCdAcCode.get(0);
					acNoItem = tCdAcCode.getAcNoItem();
				}
				L9132RptDataVo acNoCodeData = tmpEntry.getValue();

				// 群組換頁
				if (pgGrp >= 2) {
					this.newPage();
				}

				/**
				 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
				 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
				 */
				print(1, 1, "傳票批號：");
				print(0, 13, batchNo);
				print(1, 1, "會計科目：");
				print(0, 13, acNoCode);
				print(0, 26, acNoItem);
				print(1, 1, "子目　　　　　　　　　　　　　　　　　　　　　傳票號碼　　　　區隔帳冊　　　　　　戶號　　　　　　　　借方金額　　　　　　　　　貸方金額　　　　　　　經辦");
				print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");

				for (Iterator<Entry<AcSubCodeVo, L9132RptDataVo>> detailIt = acSubCodeMap.entrySet()
						.iterator(); detailIt.hasNext();) {
					Entry<AcSubCodeVo, L9132RptDataVo> tmpDetailEntry = detailIt.next();

					AcSubCodeVo tmpAcSubCodeVo = tmpDetailEntry.getKey();

					String stringAcSubCodeVo = tmpAcSubCodeVo.toString();

					if (!tmpAcSubCodeVo.getAcNoCode().equals(acNoCode)) {
						continue;
					}

					L9132RptDataVo acSubCodeData = tmpDetailEntry.getValue();

					// 明細資料
					List<AcDetail> detailDataList = detailDataMap.get(stringAcSubCodeVo);

					if (detailDataList == null || detailDataList.size() == 0) {
						continue;
					}

					for (AcDetail tmpAcDetail : detailDataList) {

						if (this.NowRow == 40) {
							this.newPage();
							print(1, 1, "傳票批號：");
							print(0, 13, batchNo);
							print(1, 1, "會計科目：");
							print(0, 13, acNoCode);
							print(0, 26, acNoItem);
							print(1, 1,
									"子目　　　　　　　　　　　　　　　　　　　　　傳票號碼　　　　區隔帳冊　　　　　　戶號　　　　　　　　借方金額　　　　　　　　　貸方金額　　　　　　　經辦");
							print(1, 1,
									"－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
						}

						print(1, 1, "　　");
						// 子目
						print(0, 1, tmpAcDetail.getAcSubCode());
						// 傳票號碼
						print(0, 48, FormatUtil.pad9(String.valueOf(tmpAcDetail.getSlipNo()), 6));
						// 區隔帳冊
						print(0, 64, tmpAcDetail.getAcSubBookCode());
						// 戶號
						print(0, 81, FormatUtil.pad9(String.valueOf(tmpAcDetail.getCustNo()), 7));

						// 借方或貸方金額
						String dbCr = tmpAcDetail.getDbCr();
						String outputAmt = formatAmt(tmpAcDetail.getTxAmt(), 0);
						int amtPosition = 0;
						if (dbCr.equals("D")) {
							amtPosition = 118;
						} else if (dbCr.equals("C")) {
							amtPosition = 144;
						}
						print(0, amtPosition, outputAmt, "R");

						// 經辦
						String empNo = tmpAcDetail.getTitaTlrNo();

						// 查經辦姓名
						String empName = "";

						CdEmp tCdEmp = sCdEmpService.findById(empNo, titaVo);

						if (tCdEmp == null) {
							empName = empNo;
						} else {
							empName = tCdEmp.getFullname();
						}

						print(0, 150, empName);

					}

					// 子目小計
					/**
					 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
					 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
					 */
					print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
					print(1, 1, "子目小計　　　　　　　　　　筆");
					// 筆數
					print(0, 26, String.valueOf(acSubCodeData.getCnt()), "R");
					// 借方金額
					print(0, 118, formatAmt(acSubCodeData.getDbAmt(), 0), "R");
					// 貸方金額
					print(0, 144, formatAmt(acSubCodeData.getCrAmt(), 0), "R");
					print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");

					if (this.NowRow == 40) {
						this.newPage();
						print(1, 1, "傳票批號：");
						print(0, 13, batchNo);
						print(1, 1, "會計科目：");
						print(0, 13, acNoCode);
						print(0, 26, acNoItem);
						print(1, 1, "子目　　　　　　　　　　　　　　　　　　　　　傳票號碼　　　　區隔帳冊　　　　　　戶號　　　　　　　　借方金額　　　　　　　　　貸方金額　　　　　　　經辦");
						print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
					}
				}
				// 科目小計
				/**
				 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
				 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
				 */
				print(1, 1, "科目小計　　　　　　　　　　筆");
				// 筆數
				print(0, 26, String.valueOf(acNoCodeData.getCnt()), "R");
				// 借方金額
				print(0, 118, formatAmt(acNoCodeData.getDbAmt(), 0), "R");
				// 貸方金額
				print(0, 144, formatAmt(acNoCodeData.getCrAmt(), 0), "R");
				print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");

				pgGrp++;

			}
			// 總計
			/**
			 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			print(1, 1, "總　　計　　　　　　　　　　筆");
			// 筆數
			print(0, 26, String.valueOf(sumDataVo.getCnt()), "R");
			// 借方金額
			print(0, 118, formatAmt(sumDataVo.getDbAmt(), 0), "R");
			// 貸方金額
			print(0, 144, formatAmt(sumDataVo.getCrAmt(), 0), "R");
			print(1, 1, "－－－－－－－－－－－－－－－－－－－－－　－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－－　－－－－－－－－－－－－　－－－－－－");
		}
	}

	public class AcSubCodeVo implements Comparable<AcSubCodeVo> {

		private String acNoCode;
		private String acSubCode;

		public AcSubCodeVo(String acNoCode, String acSubCode) {
			setAcNoCode(acNoCode);
			setAcSubCode(acSubCode);
		}

		public String getAcNoCode() {
			return acNoCode;
		}

		public void setAcNoCode(String acNoCode) {
			this.acNoCode = acNoCode;
		}

		public void setAcSubCode(String acSubCode) {
			this.acSubCode = acSubCode;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(acNoCode, acSubCode);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AcSubCodeVo other = (AcSubCodeVo) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(acNoCode, other.acNoCode) && Objects.equals(acSubCode, other.acSubCode);
		}

		@Override
		public int compareTo(AcSubCodeVo obj) {
			if (this == obj)
				return 1;
			if (obj == null)
				return 0;
			if (getClass() != obj.getClass())
				return 0;

			int result = 0;

			AcSubCodeVo other = obj;

			if (this.acNoCode.compareTo(other.acNoCode) != 0) {
				result = this.acNoCode.compareTo(other.acNoCode);
			} else if (this.acSubCode.compareTo(other.acSubCode) != 0) {
				result = this.acSubCode.compareTo(other.acSubCode);
			} else {
				result = 0;
			}
			return result;
		}

		@Override
		public String toString() {
			return "AcSubCodeVo [acNoCode=" + acNoCode + ", acSubCode=" + acSubCode + "]";
		}

		private L9132Report getEnclosingInstance() {
			return L9132Report.this;
		}
	}

	private class L9132RptDataVo implements Cloneable {

		private int cnt;
		private BigDecimal dbAmt;
		private BigDecimal crAmt;

		public L9132RptDataVo() {
			setCnt(0);
			setDbAmt(new BigDecimal(0));
			setCrAmt(new BigDecimal(0));
		}

		@Override
		public L9132RptDataVo clone() {
			L9132RptDataVo clone = new L9132RptDataVo();
			try {
				clone = (L9132RptDataVo) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return clone;
		}

		public void add(L9132RptDataVo oldDataVo) {
			this.cnt += oldDataVo.getCnt();
			this.dbAmt = this.dbAmt.add(oldDataVo.getDbAmt());
			this.crAmt = this.crAmt.add(oldDataVo.getCrAmt());

		}

		public int getCnt() {
			return cnt;
		}

		public void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public BigDecimal getDbAmt() {
			return dbAmt;
		}

		public void setDbAmt(BigDecimal dbAmt) {
			this.dbAmt = dbAmt;
		}

		public BigDecimal getCrAmt() {
			return crAmt;
		}

		public void setCrAmt(BigDecimal crAmt) {
			this.crAmt = crAmt;
		}

		@Override
		public String toString() {
			return "L9132RptDataVo [cnt=" + cnt + ", dbAmt=" + dbAmt + ", crAmt=" + crAmt + "]";
		}

	}

}
