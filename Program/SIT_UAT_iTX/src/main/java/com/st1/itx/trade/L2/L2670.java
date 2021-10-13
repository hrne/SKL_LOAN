package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2670")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2670 extends TradeBuffer {

	@Autowired
	public CdEmpService sCdEmpService;
	
	// 銷帳處理
	@Autowired
	public AcReceivableCom acReceivableCom;

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	/* 報表服務注入 */
	@Autowired
	public L2670Report L2670Rpt;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;
	public long doRpt;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2670 ");
		this.totaVo.init(titaVo);
		acReceivableCom.setTxBuffer(this.getTxBuffer());

		// tita
		// 功能1.新增 2.修改 4.刪除 5.查詢 7.列印
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 契變日期
		int iContractChgDate = parse.stringToInteger(titaVo.getParam("ContractChgDate")) + 19110000;
		// 序號
		int iContractChgNo = parse.stringToInteger(titaVo.getParam("ContractChgNo"));
		// 契變手續費
		BigDecimal feeAmt = parse.stringToBigDecimal(titaVo.getParam("TimFeeAmt"));
		String iRvNo = parse.IntegerToString(iContractChgDate, 8) + titaVo.getParam("ContractChgNo");
		String rvNo = "0000000000";
		this.info("iRvNo = " + iRvNo);
		// new table
		AcReceivable tAcReceivable = new AcReceivable();
		TempVo tTempVo = new TempVo();
		// new ArrayList
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
		AcReceivable tmpAcReceivable = new AcReceivable();

		// 新增
		if (iFunCd == 1) {

			// 取該契變日期戶號額度最大序號 +1為當前序號
			tAcReceivable = acReceivableService.useL2670First("F29", iCustNo, iFacmNo, iContractChgDate);
			this.info("L2670 tAcReceivable =" + tAcReceivable);
			if (tAcReceivable != null) {
				iRvNo = parse.IntegerToString(parse.stringToInteger(tAcReceivable.getRvNo()) + 1, 9);
			} else {
				iRvNo = parse.IntegerToString(iContractChgDate * 100 + 1, 9);
			}
			tAcReceivable = new AcReceivable();

			this.info("rvNo = " + rvNo);

			tAcReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			tAcReceivable.setAcctCode("F29"); // 業務科目

			tAcReceivable.setRvAmt(feeAmt); // 契變手續費
			tAcReceivable.setCustNo(iCustNo); // 戶號
			tAcReceivable.setFacmNo(iFacmNo); // 額度
			tAcReceivable.setRvNo(iRvNo); // 契變日期,契變序號
			tAcReceivable.setOpenAcDate(iContractChgDate); // 契變日期
			tAcReceivable.setSlipNote(titaVo.getParam("ContractChgCodeX")); // 契變項目中文名稱
			tTempVo.putParam("ContractChgCode", titaVo.getParam("ContractChgCode"));
			tAcReceivable.setJsonFields(tTempVo.getJsonString());
			this.info("jsonfield = " + tAcReceivable.getJsonFields());
			acReceivableList.add(tAcReceivable);

			acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳
			this.info("tAcReceivable = " + tAcReceivable);
			// 修改
		} else if (iFunCd == 2) {

			// 鎖定這筆
			tAcReceivable = acReceivableService.holdById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo));
			if (tAcReceivable.getClsFlag() == 1)
				throw new LogicException(titaVo, "E0010", "該筆已銷帳"); // E0010功能選擇錯誤

//			變更理由由acReceivableCom寫入

			acReceivableList = new ArrayList<AcReceivable>();
			tAcReceivable = new AcReceivable();

			tAcReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			tAcReceivable.setAcctCode("F29"); // 業務科目

			tAcReceivable.setRvAmt(feeAmt); // 契變手續費
			tAcReceivable.setCustNo(iCustNo); // 戶號
			tAcReceivable.setFacmNo(iFacmNo); // 額度
			tAcReceivable.setRvNo(iRvNo); // 契變日期,契變序號
			tAcReceivable.setOpenAcDate(iContractChgDate); // 契變日期
			tAcReceivable.setSlipNote(titaVo.getParam("ContractChgCodeX")); // 契變項目中文名稱
			tTempVo.putParam("ContractChgCode", titaVo.getParam("ContractChgCode"));
			tAcReceivable.setJsonFields(tTempVo.getJsonString());

			acReceivableList.add(tAcReceivable);

			acReceivableCom.mnt(3, acReceivableList, titaVo); // 0-起帳 1-銷帳 2起帳刪除 3變更

			// 刪除
		} else if (iFunCd == 4) {
			tmpAcReceivable = acReceivableService.findById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo));
			// 鎖定這筆
			tAcReceivable = acReceivableService.holdById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo));
			if (tAcReceivable.getClsFlag() == 1)
				throw new LogicException(titaVo, "E0010", "該筆已銷帳"); // E0010功能選擇錯誤

			// 變更前
			AcReceivable beforeAcReceivable = (AcReceivable) dataLog.clone(tAcReceivable);

			acReceivableList.add(tAcReceivable);

			acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除

			// 列印
		} else if (iFunCd == 7 || iFunCd == 8) {

			// 鎖定這筆
			tAcReceivable = acReceivableService.holdById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo));

			tTempVo = tTempVo.getVo(tAcReceivable.getJsonFields());
			int PrintCode = 0; 
			if (!"1".equals(tTempVo.getParam("PrintCode"))) {

				tTempVo = tTempVo.getVo(tAcReceivable.getJsonFields());
				tTempVo.putParam("PrintCode", "1"); // 列印
				tAcReceivable.setJsonFields(tTempVo.getJsonString());
				try {
					acReceivableService.update(tAcReceivable, titaVo); // update
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E6003", "AcReceivable update " + tAcReceivable + e.getErrorMsg());
				}

				try {
					acReceivableService.update2(tAcReceivable, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E6003", "AcReceivable.mnt delete " + tAcReceivable + e.getErrorMsg());
				}
			} else {
				PrintCode = 1;
			}
			doRpt = doRpt(titaVo, PrintCode);
		}
		if (iFunCd != 4) {
			tmpAcReceivable = acReceivableService.findById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo));
			this.info("tmpAcReceivable =" + tmpAcReceivable);
		}
		// 宣告
		Timestamp ts = tmpAcReceivable.getCreateDate();
		this.info("ts = " + ts);
		String createDate = "";
		String createTime = "";
		DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
		DateFormat sdftime = new SimpleDateFormat("HHmmss");

		createDate = sdfdate.format(ts);
		createTime = sdftime.format(ts);
		this.info("createDate = " + createDate);
		this.info("createTime = " + createTime);

		this.totaVo.putParam("OContractChgNo", rvNo.substring(8));
		if (tmpAcReceivable.getClsFlag() == 1) {
			this.totaVo.putParam("OAcDate", tmpAcReceivable.getLastAcDate());
			this.totaVo.putParam("OTitaTxtNo", tmpAcReceivable.getTitaTxtNo());
		} else {
			this.totaVo.putParam("OAcDate", 0);
			this.totaVo.putParam("OTitaTxtNo", 0);
		}
		
		
		this.totaVo.putParam("OTlrNo", tmpAcReceivable.getTitaTlrNo());
		this.totaVo.putParam("OEmpName", "");
		
		CdEmp tCdEmp = new CdEmp();	
		tCdEmp = sCdEmpService.findById(tmpAcReceivable.getTitaTlrNo(), titaVo);	
		if( tCdEmp != null) {
			this.totaVo.putParam("OEmpName", tCdEmp.getFullname()); // 建檔人員姓名
		}
		
		this.totaVo.putParam("OModifyDate", parse.stringToInteger(createDate) - 19110000);
		this.totaVo.putParam("OModifyTime", createTime);
		this.totaVo.putParam("PdfSno", doRpt);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public long doRpt(TitaVo titaVo, int PrintCode) throws LogicException {
		this.info("L2670 doRpt started.");

		// 撈資料組報表
		L2670Rpt.exec(titaVo, PrintCode);

		// 寫產檔記錄到TxReport
		long rptNo = L2670Rpt.close();

		// 產生PDF檔案
		L2670Rpt.toPdf(rptNo);

		this.info("L2670 doRpt finished.");
		return rptNo;
	}
}