package com.st1.itx.trade.L4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4601Report")
@Scope("prototype")
public class L4601Report extends MakeReport {
//    @Autowired
//    private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	Parse parse;

	@Autowired
	SortMapListCom sortMapListCom;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();
		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	String txCode = "L4601";
	String reportName = "火險詢價重複投保報表";
	private int iInsuEndMonth;
	private String sInsuEndMonth = "";
	// 每頁筆數
	private int pageIndex = 38;

	public void printHeaderP() {
		this.setFont(1, 10);

		this.print(-2, 3, "程式 ID：" + txCode);
		this.print(-2, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
		this.print(-2, 120, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim);
		this.print(-3, 3, "報  表 ：" + txCode);
		this.print(-3, 70, reportName, "C");
		this.print(-3, 120, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 120, "頁    數：" + this.getNowPage());
		this.print(-5, 58, sInsuEndMonth.substring(0, 3) + "/" + sInsuEndMonth.substring(3, 5));
		this.print(-5, 68, "同年月重複投保");
		this.print(-7, 1,
				"  戶號       額度    借款人                     擔保品號碼　　　   新保險起日      新保險迄日    原有保單號碼             保險起日     保險迄日  ");
		this.print(-8, 0,
				"------------------------------------------------------------------------------------------------------------------------------------------------");

	}

	public void exec(TitaVo titaVo) throws LogicException {
		sInsuEndMonth = titaVo.getParam("InsuEndMonth");
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg(iInsuEndMonth + "",
				iInsuEndMonth + "", 0, Integer.MAX_VALUE, titaVo);

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4601", "火險詢價重複投保報表", "", "A4", "L");

		String tempcustno = "";
		String tempfacmno = "";
		int rowcount = 0;
		if (slInsuRenewMediaTemp != null) {
			tempcustno = slInsuRenewMediaTemp.getContent().get(0).getCustNo();
			tempfacmno = slInsuRenewMediaTemp.getContent().get(0).getFacmNo();
			// 比較戶號額度 + 分隔線
			int listsize = slInsuRenewMediaTemp.getContent().size();
			int count = 0;
			for (InsuRenewMediaTemp t : slInsuRenewMediaTemp.getContent()) {

				if (!tempcustno.equals(t.getCustNo()) || !tempfacmno.equals(t.getFacmNo())) {
					this.print(1, 0,
							"------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 0, "");
					rowcount = rowcount + 2;
				}
				this.print(1, 2, t.getCustNo());
				this.print(0, 14, t.getFacmNo());
				this.print(0, 21, t.getLoanCustName());
				this.print(0, 48, t.getClCode1() + "-" + t.getClCode2() + "-" + t.getClNo());
				this.print(0, 64, showBcDate(t.getNewInsuStartDate(), 0));
				this.print(0, 79, showBcDate(t.getNewInsuEndDate(), 0));
				this.print(0, 94, t.getInsuNo());
				this.print(0, 115, showBcDate(t.getInsuStartDate(), 0));
				this.print(0, 127, showBcDate(t.getInsuEndDate(), 0));

				ClBuilding tClBuilding = clBuildingService
						.findById(
								new ClBuildingId(parse.stringToInteger(t.getClCode1()),
										parse.stringToInteger(t.getClCode2()), parse.stringToInteger(t.getClNo())),
								titaVo);

				if (tClBuilding != null) {
					this.print(1, 21, tClBuilding.getBdLocation().trim());
				} else {
					this.print(1, 21, "");
				}
				rowcount = rowcount + 2;
				tempcustno = t.getCustNo();
				tempfacmno = t.getFacmNo();
				count++;

				if (listsize == count) { // 最後一筆
					this.print(1, 0,
							"------------------------------------------------------------------------------------------------------------------------------------------------");
					rowcount++;
					this.print(pageIndex - rowcount - 2, this.getMidXAxis(), "=====報表結束=====", "C");
				}

				if (rowcount >= 30) { // 超過30筆自動換頁 並印出當前的代碼

					this.print(pageIndex - rowcount - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					rowcount = 0;
					newPage();
				}

			}

		} else {
			this.setRptItem("火險詢價重複投保報表(無符合資料)");
			this.print(1, 1, "*******    查無資料   ******");
		}

		long sno = this.close();
		this.toPdf(sno);
	}

}
