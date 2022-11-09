package com.st1.itx.trade.L4;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuComm;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuCommService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4966")
@Scope("prototype")

public class L4966 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public InsuCommService insuCommService;

	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4966 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 217 * 200 = 43400

		Slice<InsuComm> slInsuComm = null;
		List<InsuComm> lInsuComm = new ArrayList<InsuComm>();
		String iCustid = titaVo.getParam("CustId");
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		String iEmpId = titaVo.getParam("EmpId");
		String iFireOfficer = titaVo.getParam("FireOfficer");

		if (!"".equals(iCustid)) {
			CustMain tCustMain = custMainService.custIdFirst(iCustid, titaVo);
			int tCustNo = 0;
			if (tCustMain != null) {
				tCustNo = tCustMain.getCustNo();
			} else {
				throw new LogicException(titaVo, "E0001", "客戶主檔");
			}
			slInsuComm = insuCommService.findCustNo(tCustNo, this.index, this.limit, titaVo);
		} else if (iCustNo != 0) {
			slInsuComm = insuCommService.findCustNo(iCustNo, this.index, this.limit, titaVo);
		} else if (!"".equals(iEmpId)) {
			slInsuComm = insuCommService.findEmpId(iEmpId, this.index, this.limit, titaVo);
		} else if (!"".equals(iFireOfficer)) {
			slInsuComm = insuCommService.findFireOfficer(iFireOfficer, this.index, this.limit, titaVo);
		}

		lInsuComm = slInsuComm == null ? null : slInsuComm.getContent();

		if (lInsuComm == null || lInsuComm.size() == 0) {
			throw new LogicException(titaVo, "E0001", "火險佣金檔");
		}

		for (InsuComm tnsuComm : lInsuComm) {
			if (tnsuComm.getInsuYearMonth() < 299912) {
				OccursList occursList = new OccursList();
				int custno = tnsuComm.getCustNo();
				occursList.putParam("OOInsuYearMonth", tnsuComm.getInsuYearMonth());
				occursList.putParam("OONowInsuNo", tnsuComm.getNowInsuNo());
				occursList.putParam("OOInsuCate", tnsuComm.getInsuCate());
				occursList.putParam("OOInsuPrem", tnsuComm.getInsuPrem());
				occursList.putParam("OOInsuStartDate", tnsuComm.getInsuStartDate());
				occursList.putParam("OOInsuEndDate", tnsuComm.getInsuEndDate());
				occursList.putParam("OOInsuredAddr", tnsuComm.getInsuredAddr());
				occursList.putParam("OOCustNo", custno);
				occursList.putParam("OOFacmNo", tnsuComm.getFacmNo());

				CustMain tCustMain = custMainService.custNoFirst(custno, custno, titaVo);
				occursList.putParam("OOCustName", "");
				if (tCustMain != null) {
					occursList.putParam("OOCustName", tCustMain.getCustName());
				}
				occursList.putParam("OOFireOfficer", tnsuComm.getFireOfficer() + " " + tnsuComm.getEmpName());
				occursList.putParam("OOEmpId", tnsuComm.getEmpId());
				occursList.putParam("OODueAmt", tnsuComm.getDueAmt());
				BigDecimal tDueAmt = tnsuComm.getDueAmt();
				BigDecimal ZERO = BigDecimal.ZERO;
				this.info("tDueAmt   =" + tDueAmt);

				if ("Y".equals(tnsuComm.getMediaCode())) {
					this.info("走到已發放");
					occursList.putParam("OOIssue", "O");
				} else if (tDueAmt.compareTo(ZERO) == 0) {
					this.info("走到0");
					occursList.putParam("OOIssue", " ");
				} else if (tDueAmt.compareTo(ZERO) > -1) {
					this.info("走到未發放");
					occursList.putParam("OOIssue", "X");
				}

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		if (slInsuComm != null && slInsuComm.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
