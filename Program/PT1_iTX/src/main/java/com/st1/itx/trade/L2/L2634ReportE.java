package com.st1.itx.trade.L2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2634ReportE")
@Scope("prototype")
public class L2634ReportE extends MakeReport {

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public FacCloseService facCloseService;
	@Autowired
	public ClFacService clFacService;

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public void printTitle() {
	}

	@Override
	public void printHeader() {
	}

	boolean isLast = false;

	public Long exec(List<ClOtherRights> lClOtherRights, TitaVo titaVo) throws LogicException {

		this.info("L2634ReportE exec");

//		String tran = titaVo.getTxCode().isEmpty() ? "L2631E" : titaVo.getTxCode();

		this.openForm(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2634E", "中華郵政掛號郵件收件回執", "cm,19,9.5", "P");

		this.setFont(1);

		this.setFontSize(12);

		for (ClOtherRights t : lClOtherRights) {
			int custNo = 0;
			int closeNo = 0;
			int selectCnt = 0;
			if (custNo != t.getCustNo() || closeNo != t.getCloseNo()) {
				custNo = t.getCustNo();
				closeNo = t.getCloseNo();
				FacClose tFacClose = facCloseService.findById(new FacCloseId(custNo, closeNo), titaVo);
				if (tFacClose == null) {
					continue;
				}
				if (!(tFacClose.getCollectWayCode().equals("21") || tFacClose.getCollectWayCode().equals("26")
						|| tFacClose.getCollectWayCode().equals("27"))) {
					continue;
				}
				getSelecTotal(custNo, closeNo, lClOtherRights, titaVo);

				Slice<FacMain> slFacMain = facMainService.facmCustNoRange(tFacClose.getCustNo(), tFacClose.getCustNo(),
						0, 999, 0, Integer.MAX_VALUE, titaVo);
				List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();

				String facms = "";
				String x = "";

				for (FacMain facMain : lFacMain) {
					facms += x + facMain.getFacmNo();
					x = ".";
				}

				this.printCm(3, 0.5, "#" + String.format("%07d", tFacClose.getCustNo()) + "--" + facms);

				if (tFacClose.getReceiveDate() > 0) {
					this.printCm(13, 0.5, this.showRocDate(tFacClose.getReceiveDate(), 1));
				}

				this.printCm(5.5, 2.5, tFacClose.getPostAddress());

				CustMain custMain = custMainService.custNoFirst(tFacClose.getCustNo(), tFacClose.getCustNo(), titaVo);
				this.printCm(5.5, 3.5, custMain.getCustName());

				this.printCm(6.9, 4.9, "105");

				this.printCm(8, 6, "台北市松山區南京東路五段125號13樓");
				this.printCm(8, 6.5, "新光人壽保險股份有限公司");
				this.printCm(8, 7, "放款部   放款服務課");

				CdEmp cdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);
				this.printCm(8, 7.5, cdEmp.getFullname());
			}
		}
		return this.close();

	}

	private int getSelecTotal(int custNo, int closeNo, List<ClOtherRights> lClOtherRights, TitaVo titaVo)
			throws LogicException {
		int selecTotal = 0;
		int cnt = 0;
		for (ClOtherRights t : lClOtherRights) {
			cnt++;
			if (custNo == t.getCustNo() && closeNo == t.getCloseNo()) {
				selecTotal++;
				if (cnt == lClOtherRights.size()) {
					isLast = true;
				}
			}
		}
		return selecTotal;
	}

	private boolean isFacmNo(int custNo, ClOtherRights tClOtherRights, TitaVo titaVo)
			throws LogicException {
		boolean isFacmNo = true;
//		clFacService.clNoEq(tClOtherRights.getClCode1(), tClOtherRights.getClCode2(), tClOtherRights.getClNo(), 0, Integer.MAX_VALUE, titaVo);
		return isFacmNo;
	}
}
