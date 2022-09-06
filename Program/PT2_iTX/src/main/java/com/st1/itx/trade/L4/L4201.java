package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxBatchCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RepayTypeA=9,1<br>
 * VirtualAcctNoA=9,14<br>
 * END=X,1<br>
 */

@Service("L4201")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4201 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public TxBatchCom txBatchCom;

	@Autowired
	public L420ABatch l420ABatch;

	int btnIndex = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4201 ");
		this.totaVo.init(titaVo);
		txBatchCom.setTxBuffer(this.getTxBuffer());

		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String iBatchNo = titaVo.getParam("BatchNo");
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("DetailSeq"));
		int iRepayTypeA = parse.stringToInteger(titaVo.getParam("RepayTypeA"));
		int iCustNoA = parse.stringToInteger(titaVo.getParam("CustNoA"));
		int iFacmNoA = parse.stringToInteger(titaVo.getParam("FacmNoA"));
		String iProcStsCode = titaVo.getParam("ProcStsCode");
		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();

		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(iDetailSeq);

		tBatxDetail = batxDetailService.holdById(tBatxDetailId);
		if (tBatxDetail == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(tBatxDetail.getProcNote());
		int oldPreRepayTerms = 0;
		if (tBatxDetail.getRepayType() == 1) {
			if (tTempVo.get("PreRepayTerms") != null) {
				oldPreRepayTerms = parse.stringToInteger(tTempVo.get("PreRepayTerms"));
				tTempVo.remove("PreRepayTerms");
			}
		}
//		整批變更還款類別
		int newPreRepayTerms = 0;
		if (titaVo.get("selectTotal") != null) {
			btnIndex = parse.stringToInteger(titaVo.getBtnIndex());
			// 期款 0-1, 1-2, 2-3, 3-1(不預收)
			switch (btnIndex) {
			case 0: // 期款
				iRepayTypeA = 1;
				newPreRepayTerms = this.txBuffer.getSystemParas().getPreRepayTermsBatch();
				break;
			case 1: // 部分償還
				iRepayTypeA = 2;
				break;
			case 2: // 結案
				iRepayTypeA = 3;
				break;
			case 3: // 期款(不預收)
				iRepayTypeA = 1;
				newPreRepayTerms = 0;
				break;
			}
		}
		if (tBatxDetail.getRepayType() == iRepayTypeA && tBatxDetail.getCustNo() == iCustNoA && tBatxDetail.getFacmNo() == iFacmNoA && iProcStsCode.equals(tBatxDetail.getProcStsCode())
				&& oldPreRepayTerms == newPreRepayTerms) {
			throw new LogicException(titaVo, "E0012", "修改值與現有資料相同");
		}
		tBatxDetail.setRepayType(iRepayTypeA);
		tBatxDetail.setCustNo(iCustNoA);
		tBatxDetail.setFacmNo(iFacmNoA);
		tBatxDetail.setProcStsCode(iProcStsCode);
		// 預收期數
		if (tBatxDetail.getRepayType() == 1) {
			tTempVo.putParam("PreRepayTerms", newPreRepayTerms);
		} else {
			tTempVo.remove("PreRepayTerms");
		}
		tBatxDetail.setProcNote(tTempVo.getJsonString());
		try {
			batxDetailService.update(tBatxDetail);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "update " + e.getErrorMsg());
		}
		// 戶號整批檢核
		doCheckCustNo(tBatxDetail, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 戶號整批檢核
	private void doCheckCustNo(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		List<String> iProcStsCode = new ArrayList<String>();
		iProcStsCode.add("0");
		iProcStsCode.add("2");
		iProcStsCode.add("3");
		iProcStsCode.add("4");
		Slice<BatxDetail> slBatxDetail = batxDetailService.findL4930CAEq(tBatxDetail.getAcDate() + 19110000, tBatxDetail.getBatchNo(), tBatxDetail.getCustNo(), iProcStsCode, 0, Integer.MAX_VALUE,
				titaVo);
		if (slBatxDetail != null) {
			l420ABatch.setTxBuffer(this.txBuffer);
			l420ABatch.doCheckAll(false, tBatxDetail.getReconCode(), slBatxDetail, titaVo);
		}
	}
}