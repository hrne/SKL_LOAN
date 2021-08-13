package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxOthers;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxOthersService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * AcDate=9,7<br>
 * BatchNo=X,6<br>
 * RepayCode=9,2<br>
 * CreateEmpNo=X,6<br>
 * END=X,1<br>
 */

@Service("L4921")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4921 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4921.class);

	/* DB服務注入 */
	@Autowired
	public BatxOthersService batxOthersService;

	@Autowired
	public BatxDetailService batxDetailService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4921 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<BatxOthers> sBatxOthers = null;

		List<BatxOthers> lBatxOthers = new ArrayList<BatxOthers>();

		int adAcDate1 = 0;
		int adAcDate2 = 0;

		if (parse.stringToInteger(titaVo.getParam("AcDate").trim()) == 0) {
			adAcDate1 = 0;
			adAcDate2 = 99991231;
		} else {
			adAcDate1 = parse.stringToInteger(titaVo.getParam("AcDate").trim()) + 19110000;
			adAcDate2 = parse.stringToInteger(titaVo.getParam("AcDate").trim()) + 19110000;
		}

		String strBatchNo = titaVo.getParam("BatchNo").trim();
		int intRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode").trim());
		String strCreateEmpNo = titaVo.getParam("CreateEmpNo").trim();

		if ("".equals(strCreateEmpNo) && "99".equals(titaVo.getParam("RepayCode")) && "".equals(strBatchNo)) {
			sBatxOthers = batxOthersService.searchRuleE(adAcDate1, adAcDate2, this.index, this.limit, titaVo);
			this.info("flag = E");
		} else if ("".equals(strCreateEmpNo) && "99".equals(titaVo.getParam("RepayCode"))) {
			sBatxOthers = batxOthersService.searchRuleA(adAcDate1, adAcDate2, strBatchNo, this.index, this.limit,
					titaVo);
			this.info("flag = A");
		} else if ("99".equals(titaVo.getParam("RepayCode")) && "".equals(strBatchNo)) {
			sBatxOthers = batxOthersService.searchRuleG(adAcDate1, adAcDate2, strCreateEmpNo, this.index, this.limit,
					titaVo);
			this.info("flag = G");
		} else if ("".equals(strCreateEmpNo) && "".equals(strBatchNo)) {
			sBatxOthers = batxOthersService.searchRuleF(adAcDate1, adAcDate2, intRepayCode, this.index, this.limit,
					titaVo);
			this.info("flag = F");
		} else if ("".equals(strCreateEmpNo)) {
			sBatxOthers = batxOthersService.searchRuleB(adAcDate1, adAcDate2, strBatchNo, intRepayCode, this.index,
					this.limit, titaVo);
			this.info("flag = B");
		} else if ("99".equals(titaVo.getParam("RepayCode"))) {
			sBatxOthers = batxOthersService.searchRuleC(adAcDate1, adAcDate2, strBatchNo, strCreateEmpNo, this.index,
					this.limit);
			this.info("flag = C");
		} else if ("".equals(strBatchNo)) {
			sBatxOthers = batxOthersService.searchRuleH(adAcDate1, adAcDate2, intRepayCode, strCreateEmpNo, this.index,
					this.limit, titaVo);
			this.info("flag = H");
		} else {
			sBatxOthers = batxOthersService.searchRuleD(adAcDate1, adAcDate2, strBatchNo, intRepayCode, strCreateEmpNo,
					this.index, this.limit, titaVo);
			this.info("flag = D");
		}

		lBatxOthers = sBatxOthers == null ? null : sBatxOthers.getContent();

		if (lBatxOthers != null && lBatxOthers.size() != 0) {
			this.info("L4921-A lBatxOthers.size()= " + lBatxOthers.size());

			for (BatxOthers tBatxOthersVO : lBatxOthers) {
				OccursList occursList = new OccursList();

				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();
				int flag = 1;

				tBatxDetailId.setAcDate(tBatxOthersVO.getAcDate());
				tBatxDetailId.setBatchNo(tBatxOthersVO.getBatchNo());
				tBatxDetailId.setDetailSeq(tBatxOthersVO.getDetailSeq());

				tBatxDetail = batxDetailService.findById(tBatxDetailId, titaVo);

				if (tBatxDetail.getProcStsCode().compareTo("5") == 1) {
					flag = 0;
				}

				occursList.putParam("OOAcDate", tBatxOthersVO.getAcDate());
				occursList.putParam("OOBatchNo", tBatxOthersVO.getBatchNo());
				occursList.putParam("OODetailSeq", tBatxOthersVO.getDetailSeq());
				occursList.putParam("OORepayCode", tBatxOthersVO.getRepayCode());
				occursList.putParam("OOAcNo", tBatxOthersVO.getRepayAcCode());
				occursList.putParam("OORepayId", tBatxOthersVO.getRepayId());
				occursList.putParam("OORepayName", tBatxOthersVO.getRepayName());
				occursList.putParam("OORvNo", tBatxOthersVO.getRvNo());
				occursList.putParam("OORepayType", tBatxOthersVO.getRepayType());
				occursList.putParam("OORepayAmt", tBatxOthersVO.getRepayAmt());
				occursList.putParam("OOCustNo", tBatxOthersVO.getCustNo());
				occursList.putParam("OOCustNm", tBatxOthersVO.getCustNm());
				occursList.putParam("OORemark", tBatxOthersVO.getNote());
				occursList.putParam("OOBtnFlag", flag);

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}