package com.st1.itx.trade.L9;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcAcctCheckDetail;
import com.st1.itx.db.service.AcAcctCheckDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component("L9133Report2")
@Scope("prototype")

public class L9133Report2 extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9133Report2.class);

	/* DB服務注入 */
	@Autowired
	AcAcctCheckDetailService sAcAcctCheckDetailService;

	/* DB服務注入 */
	@Autowired
	CdEmpService sCdEmpService;

	/* DB服務注入 */
	@Autowired
	CdAcCodeService sCdAcCodeService;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9133";
	private String reportItem = "放款會計與主檔餘額檢核明細表";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;
	
	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9133Report2.printHeader");

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 1, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, 85, showRocDate(this.reportDate), "C");
		// 明細表頭
		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		this.print(-5, 1, "科目　　　　　　　　　　　　　　　　戶號　　　　　　　　　　　　　　　　會計帳餘額　　　　　　　　　　　　　　主檔餘額　　　　　　　　　　　　　　　　差額");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

	}
	
	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9133Report2 exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		// 查會計業務檢核檔
		Slice<AcAcctCheckDetail> slAcAcctCheckDetail = sAcAcctCheckDetailService.findAcDate(this.reportDate, 0,
				Integer.MAX_VALUE, titaVo);
		List<AcAcctCheckDetail> lAcAcctCheckDetail = slAcAcctCheckDetail == null ? null
				: slAcAcctCheckDetail.getContent();

		if (lAcAcctCheckDetail == null || lAcAcctCheckDetail.size() == 0) {
			throw new LogicException("E0001", "會計業務檢核明細檔");
		}

		for (AcAcctCheckDetail tAcAcctCheckDetail : lAcAcctCheckDetail) {

			// 明細資料新的一行
			print(1, 1, "　　");

			// 科目
			String acctItem = tAcAcctCheckDetail.getAcctItem();
			print(0, 1, acctItem);

			// 戶號
			String custNo = FormatUtil.pad9(String.valueOf(tAcAcctCheckDetail.getCustNo()), 7);
			String facmNo = FormatUtil.pad9(String.valueOf(tAcAcctCheckDetail.getFacmNo()), 3);
			String bormNo = FormatUtil.pad9(String.valueOf(tAcAcctCheckDetail.getBormNo()), 3);
			print(0, 38, custNo + "-" + facmNo + "-" + bormNo, "C");

			// 會計帳餘額
			String acMainBal = formatAmt(tAcAcctCheckDetail.getAcBal(), 0);
			print(0, 83, acMainBal, "R");

			// 業務帳餘額
			String masterBal = formatAmt(tAcAcctCheckDetail.getAcctMasterBal(), 0);
			print(0, 119, masterBal, "R");

			// 差額 (會計帳餘額-業務帳餘額)
			String diffAmt = formatAmt(tAcAcctCheckDetail.getAcBal().subtract(tAcAcctCheckDetail.getAcctMasterBal()),
					0);
			print(0, 155, diffAmt, "R");

		}

	}

}
