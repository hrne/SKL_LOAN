package com.st1.itx.trade.L3;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3074")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L3074 extends TradeBuffer {

	@Autowired
	public CdEmpService sCdEmpService;

	/* DB服務注入 */
	@Autowired
	public LoanFacTmpService sLoanFacTmpService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3074 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500; // 29 * 500 = 14500

		// tita
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		Timestamp ts;
		Timestamp uts;
		String createDate = "";
		String updateDate = "";
		// new ArrayList
		List<LoanFacTmp> lLoanFacTmp = new ArrayList<LoanFacTmp>();
		Slice<LoanFacTmp> sLoanFacTmp = null;

		// PK
		// 測試該戶號是否有資料存在暫收款指定額度設定查詢檔
		if (iCustNo > 0) {
			sLoanFacTmp = sLoanFacTmpService.findCustNo(iCustNo, this.index, this.limit, titaVo);
			lLoanFacTmp = sLoanFacTmp == null ? null : sLoanFacTmp.getContent();
		} else {
			sLoanFacTmp = sLoanFacTmpService.findAll(this.index, this.limit, titaVo);
			lLoanFacTmp = sLoanFacTmp == null ? null : sLoanFacTmp.getContent();
		}

		if (lLoanFacTmp == null) {
			throw new LogicException(titaVo, "E2003", "L3072 該戶號" + iCustNo + "不存在暫收款指定額度設定查詢檔。");
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sLoanFacTmp != null && sLoanFacTmp.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		CdEmp tCdEmp = new CdEmp();

		for (LoanFacTmp tLoanFacTmp : lLoanFacTmp) {
			this.info("tLoanFacTmp---->" + tLoanFacTmp);
			// new occurs
			OccursList occurslist = new OccursList();

			occurslist.putParam("OOCustNo", tLoanFacTmp.getCustNo());
			occurslist.putParam("OOFacmNo", tLoanFacTmp.getFacmNo());
			occurslist.putParam("OODescribe", tLoanFacTmp.getDescribe());

			String tempEmpNo = tLoanFacTmp.getCreateEmpNo() == "" ? tLoanFacTmp.getLastUpdateEmpNo()
					: tLoanFacTmp.getCreateEmpNo();
			String updateEmpNo = tLoanFacTmp.getLastUpdateEmpNo() == "" ? tLoanFacTmp.getCreateEmpNo()
					: tLoanFacTmp.getLastUpdateEmpNo();

			occurslist.putParam("OOEmpNo", tempEmpNo);

			occurslist.putParam("OOEmpName", ""); // 建檔人員姓名
			tCdEmp = sCdEmpService.findById(tempEmpNo, titaVo);

			if (tCdEmp != null) {
				occurslist.putParam("OOEmpName", tCdEmp.getFullname()); // 建檔人員姓名
			}

			occurslist.putParam("OOUpdateEmpNo", updateEmpNo);

			occurslist.putParam("OOUpdateEmpName", ""); // 更新人員姓名
			tCdEmp = sCdEmpService.findById(updateEmpNo, titaVo);

			if (tCdEmp != null) {
				occurslist.putParam("OOUpdateEmpName", tCdEmp.getFullname()); // 更新人員姓名
			}

			// 宣告
			ts = tLoanFacTmp.getCreateDate();
			uts = tLoanFacTmp.getLastUpdate();
			this.info("ts = " + ts);
			this.info("uts = " + uts);
			DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
			if (ts != null && uts != null) {
				createDate = sdfdate.format(ts);
				updateDate = sdfdate.format(uts);

				createDate = parse.timeStampToStringDate(tLoanFacTmp.getCreateDate()).replace("/", "");
				updateDate = parse.timeStampToStringDate(tLoanFacTmp.getLastUpdate()).replace("/", "");

				occurslist.putParam("OOCreateDate", createDate);
				occurslist.putParam("OOLastUpdate", updateDate);
			} else {
				occurslist.putParam("OOCreateDate", "");
				occurslist.putParam("OOLastUpdate", "");
			}
			this.totaVo.addOccursList(occurslist);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}