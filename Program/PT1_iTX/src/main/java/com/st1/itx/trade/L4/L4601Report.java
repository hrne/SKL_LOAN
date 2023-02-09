package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClNoMap;
import com.st1.itx.db.domain.ClNoMapId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClNoMapService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
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
	public ClNoMapService sClNoMapService;
	@Autowired
	Parse parse;

	@Autowired
	SortMapListCom sortMapListCom;
	
	@Autowired
	public InsuRenewService insuRenewService;
	
	@Autowired
	public CdCodeService cdCodeService;


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
	String reportName = "";
	private int iInsuEndMonth;
	private String sInsuEndMonth = "";

	// 每頁筆數
	private int pageIndex = 38;
	private int reporttype = 0;

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
				"  戶號       額度    借款人      擔保品號碼　　　   新保險起日      新保險迄日    原有保單號碼             保險起日      保險迄日  險種註記");
		this.print(-8, 0,
				"------------------------------------------------------------------------------------------------------------------------------------------------");

	}

	public void exec(TitaVo titaVo) throws LogicException {
		sInsuEndMonth = titaVo.getParam("InsuEndMonth");
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg(iInsuEndMonth + "",
				iInsuEndMonth + "", 0, Integer.MAX_VALUE, titaVo);
		// 同戶號額度才show
		reporttype = 1;
		reportName = "火險詢價重複投保報表";
		report(titaVo, slInsuRenewMediaTemp);
		//
		reporttype = 2;
		reportName = "火險重複投保－戶號建物";
		report(titaVo, slInsuRenewMediaTemp);

	}

	private void report(TitaVo titaVo, Slice<InsuRenewMediaTemp> sL4601List) throws LogicException {

		// 戶號額度
		HashMap<tmpCF, Integer> infor = new HashMap<>();

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno())
				.setRptCode(txCode).setRptItem(reportName).setRptSize("A4").setPageOrientation("L").build();

		this.open(titaVo, reportVo);

		String tempCustNo = "";
		String tempSeq = "";
		int rowCount = 0;
		if (sL4601List != null) {
			this.info("List is true");
			// 比較戶號額度 + 分隔線
			int count = 0;
			List<InsuRenewMediaTemp> lTemp = new ArrayList<InsuRenewMediaTemp>();

			// 只抓有B有重複的
			for (InsuRenewMediaTemp l : sL4601List.getContent()) {
				if (!"".equals(l.getCheckResultB())) {

					tmpCF tmp = new tmpCF(parse.stringToInteger(l.getCustNo()), parse.stringToInteger(l.getFacmNo()));
					if (!infor.containsKey(tmp)) {
						infor.put(tmp, 1);
					} else {
						infor.put(tmp, Integer.sum(infor.get(tmp), 1)); // 累積次數為了第一張表 需戶號額度都相同重複
					}
				}
			}

			this.info("infor ... " + infor);

			for (InsuRenewMediaTemp l : sL4601List.getContent()) {
				if (!"".equals(l.getCheckResultB())) {

					if (reporttype == 1) {
						tmpCF tmp = new tmpCF(parse.stringToInteger(l.getCustNo()),
								parse.stringToInteger(l.getFacmNo()));

						this.info("tmp ... " + tmp);

						if (infor.get(tmp) != null) {

							this.info("infor : " + infor.get(tmp));

							if (infor.get(tmp) >= 2) {
								lTemp.add(l);
							}
						}
					} else {
						lTemp.add(l);
					}
				}
			}

			int listsize = lTemp.size();

			if (lTemp.size() != 0) {

				tempCustNo = lTemp.get(0).getCustNo();
				tempSeq = lTemp.get(0).getSeq();
				for (InsuRenewMediaTemp t : lTemp) {

					if (rowCount != 0) { // 換頁第一筆不判斷

						if (!tempCustNo.equals(t.getCustNo()) || !tempSeq.equals(t.getSeq())) {
							this.print(1, 0,
									"------------------------------------------------------------------------------------------------------------------------------------------------");
							rowCount = rowCount + 1;
						}

					}
					this.print(1, 2, t.getCustNo());
					this.print(0, 14, t.getFacmNo());
					this.print(0, 21, t.getLoanCustName());

					ClNoMap tClNoMap = new ClNoMap();

					ClNoMapId clNoMapId = new ClNoMapId();
					clNoMapId.setGdrId1(parse.stringToInteger(t.getClCode1()));
					clNoMapId.setGdrId2(parse.stringToInteger(t.getClCode2()));
					clNoMapId.setGdrNum(parse.stringToInteger(t.getClNo()));
					clNoMapId.setLgtSeq(parse.stringToInteger(t.getSeq()));

					tClNoMap = sClNoMapService.findById(clNoMapId, titaVo);

					int ClCode1 = 0, ClCode2 = 0, ClNo = 0;
					// ClNoMap 與 InsuRenewMediaTemp 擔保品編號2 差補0
					if (tClNoMap != null) {
						this.print(0, 32, tClNoMap.getClCode1() + "-" + FormatUtil.pad9("" + tClNoMap.getClCode2(), 2)
								+ "-" + tClNoMap.getClNo());

						ClCode1 = tClNoMap.getClCode1();
						ClCode2 = tClNoMap.getClCode2();
						ClNo = tClNoMap.getClNo();
					} else {
						this.print(0, 32, t.getClCode1() + "-" + t.getClCode2() + "-" + t.getClNo());

						ClCode1 = parse.stringToInteger(t.getClCode1());
						ClCode2 = parse.stringToInteger(t.getClCode2());
						ClNo = parse.stringToInteger(t.getClNo());
					}

					this.print(0, 50, showBcDate(t.getNewInsuStartDate(), 0));
					this.print(0, 65, showBcDate(t.getNewInsuEndDate(), 0));
					this.print(0, 80, t.getInsuNo());
					this.print(0, 100, showBcDate(t.getInsuStartDate(), 0));
					this.print(0, 113, showBcDate(t.getInsuEndDate(), 0));
					
					InsuRenew tInsuRenew = insuRenewService.prevInsuNoFirst(
							parse.stringToInteger(t.getCustNo()),
							parse.stringToInteger(t.getFacmNo()), t.getInsuNo(), titaVo);
					
					CdCode tCdCode = cdCodeService.findById(new CdCodeId("CommericalFlag", tInsuRenew.getCommericalFlag()),
							titaVo);
					String CommericalFlagX = "";
					if (tCdCode != null) {
						CommericalFlagX = tCdCode.getItem();
					}
					
					this.print(0, 125, CommericalFlagX);

				
					this.print(1, 0, "");

					ClBuildingId clBuildingId = new ClBuildingId();
					clBuildingId.setClCode1(ClCode1);
					clBuildingId.setClCode2(ClCode2);
					clBuildingId.setClNo(ClNo);

					ClBuilding tClBuilding = clBuildingService.findById(clBuildingId, titaVo);

					if (reporttype == 1) {
						if (tClBuilding != null) {
							this.print(0, 21, tClBuilding.getBdLocation());
						} else {
							this.print(0, 21, t.getAddress());
						}
					} else {
						if (tClBuilding != null) {
							this.print(0, 21, "建號： " + tClBuilding.getBdNo1());
							this.print(0, 48, tClBuilding.getBdLocation());
						} else {
							this.print(0, 21, "建號： " + t.getPostalCode());
							this.print(0, 48, t.getAddress());
						}

					}
					rowCount = rowCount + 2;
					tempCustNo = t.getCustNo();
					tempSeq = t.getSeq();
					count++;

					if (listsize == count) { // 最後一筆
						this.print(1, 0,
								"------------------------------------------------------------------------------------------------------------------------------------------------");
						rowCount++;
						this.print(pageIndex - rowCount - 2, this.getMidXAxis(), "=====報表結束=====", "C");
					}

					if (rowCount >= 30) { // 超過30筆自動換頁 並印出當前的代碼

						this.print(pageIndex - rowCount - 2, this.getMidXAxis(), "=====續下頁=====", "C");
						rowCount = 0;
						newPage();
					}

				} // for
			} else {
				this.setRptItem(reportName + "(無符合資料)");			
				
				this.print(1, 1, "*******    查無資料   ******");
				
				
			}

		} else {
			this.setRptItem(reportName + "(無符合資料)");
			this.print(1, 1, "*******    查無資料   ******");
		}

		long sno = this.close();
		this.toPdf(sno);
	}

//	暫時紀錄戶號額度
	/**
	 * 
	 * @author custNo <br>
	 *         facmNo <br>
	 *
	 */
	private class tmpCF {

		public tmpCF(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);

		}

		private int custNo = 0;
		private int facmNo = 0;

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
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
			tmpCF other = (tmpCF) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			return true;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private L4601Report getEnclosingInstance() {
			return L4601Report.this;
		}

	}

}
