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

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2606")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2606 extends TradeBuffer {

	@Autowired
	public CdEmpService sCdEmpService;

	// 銷帳處理
	@Autowired
	public AcReceivableCom acReceivableCom;

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2606 ");
		this.totaVo.init(titaVo);
		acReceivableCom.setTxBuffer(this.getTxBuffer());

		// tita
		// 功能1.新增 2.修改 4.刪除 5.查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 起帳日期
		int iOpenAcDate = parse.stringToInteger(titaVo.getParam("OpenAcDate"));
		// 帳管費
		BigDecimal iAcctFee = parse.stringToBigDecimal(titaVo.getParam("TimAcctFee"));
		String iRvNo = titaVo.getParam("RvNo");
		// new ArrayList
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
		// new table
		AcReceivable tAcReceivable = new AcReceivable();
		AcReceivable cAcReceivable = new AcReceivable();
		// 新增
		if (iFunCd == 1) {

			// 取該契變日期戶號額度最大序號 +1為當前序號
			tAcReceivable = acReceivableService.useL2670First("F10", iCustNo, iFacmNo, iOpenAcDate + 19110000);
			this.info("L2606 tAcReceivable =" + tAcReceivable);
			if (tAcReceivable != null) {
				iRvNo = parse.IntegerToString(parse.stringToInteger(tAcReceivable.getRvNo()) + 1, 9);
			} else {
				iRvNo = parse.IntegerToString(iOpenAcDate * 100 + 1, 9);
			}
			tAcReceivable = new AcReceivable();

			tAcReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			tAcReceivable.setAcctCode("F10"); // 業務科目

			tAcReceivable.setCustNo(iCustNo); // 戶號
			tAcReceivable.setFacmNo(iFacmNo); // 額度
			tAcReceivable.setRvNo(iRvNo); // 會計日期+序號
			tAcReceivable.setOpenAcDate(iOpenAcDate); // 起帳日期必須輸入 預設會計日
			tAcReceivable.setRvAmt(iAcctFee); // 帳管費
			tAcReceivable.setSlipNote(titaVo.getParam("SlipNote")); // //備註
			acReceivableList.add(tAcReceivable);

			acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳
			// 修改
		} else if (iFunCd == 2) {
			// 檢查是否已銷帳
			cAcReceivable = acReceivableService.findById(new AcReceivableId("F10", iCustNo, iFacmNo, iRvNo));
			if (cAcReceivable.getClsFlag() == 1) {
				throw new LogicException(titaVo, "E0010", "該筆已銷帳"); // E0010功能選擇錯誤
			}

			// 鎖定這筆
			tAcReceivable = acReceivableService.holdById(new AcReceivableId("F10", iCustNo, iFacmNo, iRvNo));
			// 變更理由由acReceivableCom寫入

			acReceivableList = new ArrayList<AcReceivable>();
			tAcReceivable = new AcReceivable();

			tAcReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			tAcReceivable.setAcctCode("F10"); // 業務科目

			tAcReceivable.setCustNo(iCustNo); // 戶號
			tAcReceivable.setFacmNo(iFacmNo); // 額度
			tAcReceivable.setRvNo(iRvNo); // 起帳日期,契變序號
			tAcReceivable.setRvAmt(iAcctFee); // 帳管費
			tAcReceivable.setOpenAcDate(iOpenAcDate); // 起帳日期
			tAcReceivable.setSlipNote(titaVo.getParam("SlipNote")); // 備註

			acReceivableList.add(tAcReceivable);
			acReceivableCom.mnt(3, acReceivableList, titaVo); // 0-起帳 1-銷帳 2起帳刪除 3變更
			// 刪除
		} else if (iFunCd == 4) {

			// 檢查是否已銷帳
			cAcReceivable = acReceivableService.findById(new AcReceivableId("F10", iCustNo, iFacmNo, iRvNo));
			if (cAcReceivable.getClsFlag() == 1) {
				throw new LogicException(titaVo, "E0010", "該筆已銷帳"); // E0010功能選擇錯誤
			}
			// 鎖定這筆
			tAcReceivable = acReceivableService.holdById(new AcReceivableId("F10", iCustNo, iFacmNo, iRvNo));

			AcReceivable beforeAcReceivable = (AcReceivable) dataLog.clone(tAcReceivable);

			acReceivableList.add(tAcReceivable);

			acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除

			dataLog.setEnv(titaVo, tAcReceivable, tAcReceivable);
			dataLog.exec("刪除會計銷帳檔-帳管費");
			// 列印
		} else {
		}

		if (iFunCd != 4) {
			cAcReceivable = acReceivableService.findById(new AcReceivableId("F10", iCustNo, iFacmNo, iRvNo));
			this.info("tmpAcReceivable =" + cAcReceivable);
		}
		// 宣告
		Timestamp ts = cAcReceivable.getCreateDate();
		this.info("ts = " + ts);
		String createDate = "";
		String createTime = "";
		DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
		DateFormat sdftime = new SimpleDateFormat("HHmmss");

		createDate = sdfdate.format(ts);
		createTime = sdftime.format(ts);
		this.info("createDate = " + createDate);
		this.info("createTime = " + createTime);

		this.totaVo.putParam("ORvNo", cAcReceivable.getRvNo());
		if (cAcReceivable.getClsFlag() == 1) {
			this.totaVo.putParam("OAcDate", cAcReceivable.getLastAcDate());
			this.totaVo.putParam("OTitaTxtNo", cAcReceivable.getTitaTxtNo());
			this.totaVo.putParam("OTlrNo", cAcReceivable.getTitaTlrNo());
			CdEmp tCdEmp = new CdEmp();
			tCdEmp = sCdEmpService.findById(cAcReceivable.getTitaTlrNo(), titaVo);
			if (tCdEmp != null) {
				this.totaVo.putParam("OEmpName", tCdEmp.getFullname()); // 建檔人員姓名
			}
		} else {
			this.totaVo.putParam("OAcDate", 0);
			this.totaVo.putParam("OTitaTxtNo", 0);
			this.totaVo.putParam("OTlrNo", "");
			this.totaVo.putParam("OEmpName", "");
		}

		this.totaVo.putParam("OCreateDate", parse.stringToInteger(createDate) - 19110000);
		this.totaVo.putParam("OCreateTime", createTime);

		this.addList(this.totaVo);
		return this.sendList();
	}

}