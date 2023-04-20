package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
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

		int iAcDateS = parse.stringToInteger(titaVo.getParam("AcDateS")) + 19110000;
		int iAcDateE = parse.stringToInteger(titaVo.getParam("AcDateE")) + 19110000;
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		String strBatchNo = titaVo.getParam("BatchNo").trim();
		int intRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode").trim());
		String strCreateEmpNo = titaVo.getParam("CreateEmpNo").trim();
		if (iCustNo == 0) {
			if ("".equals(strCreateEmpNo) && "99".equals(titaVo.getParam("RepayCode")) && "".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchRuleE(iAcDateS, iAcDateE, this.index, this.limit, titaVo);
				this.info("flag = E");
			} else if ("".equals(strCreateEmpNo) && "99".equals(titaVo.getParam("RepayCode"))) {
				sBatxOthers = batxOthersService.searchRuleA(iAcDateS, iAcDateE, strBatchNo, this.index, this.limit,
						titaVo);
				this.info("flag = A");
			} else if ("99".equals(titaVo.getParam("RepayCode")) && "".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchRuleG(iAcDateS, iAcDateE, strCreateEmpNo, this.index, this.limit,
						titaVo);
				this.info("flag = G");
			} else if ("".equals(strCreateEmpNo) && "".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchRuleF(iAcDateS, iAcDateE, intRepayCode, this.index, this.limit,
						titaVo);
				this.info("flag = F");
			} else if ("".equals(strCreateEmpNo)) {
				sBatxOthers = batxOthersService.searchRuleB(iAcDateS, iAcDateE, strBatchNo, intRepayCode, this.index,
						this.limit, titaVo);
				this.info("flag = B");
			} else if ("99".equals(titaVo.getParam("RepayCode"))) {
				sBatxOthers = batxOthersService.searchRuleC(iAcDateS, iAcDateE, strBatchNo, strCreateEmpNo, this.index,
						this.limit);
				this.info("flag = C");
			} else if ("".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchRuleH(iAcDateS, iAcDateE, intRepayCode, strCreateEmpNo,
						this.index, this.limit, titaVo);
				this.info("flag = H");
			} else {
				sBatxOthers = batxOthersService.searchRuleD(iAcDateS, iAcDateE, strBatchNo, intRepayCode,
						strCreateEmpNo, this.index, this.limit, titaVo);
				this.info("flag = D");
			}
		} else {
			if ("".equals(strCreateEmpNo) && "99".equals(titaVo.getParam("RepayCode")) && "".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchCustNoE(iAcDateS, iAcDateE, iCustNo, this.index, this.limit,
						titaVo);
				this.info("CustNoflag = E");
			} else if ("".equals(strCreateEmpNo) && "99".equals(titaVo.getParam("RepayCode"))) {
				sBatxOthers = batxOthersService.searchCustNoA(iAcDateS, iAcDateE, iCustNo, strBatchNo, this.index,
						this.limit, titaVo);
				this.info("CustNoflag = A");
			} else if ("99".equals(titaVo.getParam("RepayCode")) && "".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchCustNoG(iAcDateS, iAcDateE, iCustNo, strCreateEmpNo, this.index,
						this.limit, titaVo);
				this.info("CustNoflag = G");
			} else if ("".equals(strCreateEmpNo) && "".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchCustNoF(iAcDateS, iAcDateE, iCustNo, intRepayCode, this.index,
						this.limit, titaVo);
				this.info("CustNoflag = F");
			} else if ("".equals(strCreateEmpNo)) {
				sBatxOthers = batxOthersService.searchCustNoB(iAcDateS, iAcDateE, iCustNo, strBatchNo, intRepayCode,
						this.index, this.limit, titaVo);
				this.info("CustNoflag = B");
			} else if ("99".equals(titaVo.getParam("RepayCode"))) {
				sBatxOthers = batxOthersService.searchCustNoC(iAcDateS, iAcDateE, iCustNo, strBatchNo, strCreateEmpNo,
						this.index, this.limit);
				this.info("CustNoflag = C");
			} else if ("".equals(strBatchNo)) {
				sBatxOthers = batxOthersService.searchCustNoH(iAcDateS, iAcDateE, iCustNo, intRepayCode, strCreateEmpNo,
						this.index, this.limit, titaVo);
				this.info("CustNoflag = H");
			} else {
				sBatxOthers = batxOthersService.searchCustNoD(iAcDateS, iAcDateE, iCustNo, strBatchNo, intRepayCode,
						strCreateEmpNo, this.index, this.limit, titaVo);
				this.info("CustNoflag = D");
			}

		}
		lBatxOthers = sBatxOthers == null ? null : sBatxOthers.getContent();
		int i = 0;
		if (lBatxOthers != null && lBatxOthers.size() != 0) {
			this.info("L4921-A lBatxOthers.size()= " + lBatxOthers.size());

			for (BatxOthers tBatxOthersVO : lBatxOthers) {
				OccursList occursList = new OccursList();

				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				tBatxDetailId.setAcDate(tBatxOthersVO.getAcDate());
				tBatxDetailId.setBatchNo(tBatxOthersVO.getBatchNo());
				tBatxDetailId.setDetailSeq(tBatxOthersVO.getDetailSeq());

				tBatxDetail = batxDetailService.findById(tBatxDetailId, titaVo);
				if (tBatxDetail == null) {
					continue;
				}

				// 大額增入帳:若整批入帳檔的大額手工增入帳與匯款轉帳連結序號有值則顯示<匯款>按鈕，連結L4201查詢
				// RepayCode11Chain
				TempVo tTempVo = new TempVo();
				int chainAcDate = 0;
				String chainBatchNo = "";
				int chainDetailSeq = 0;
				String chainFlag = "N";
				if (tBatxOthersVO.getRepayCode() == 11) {
					tTempVo = tTempVo.getVo(tBatxDetail.getProcNote());
					if ((!"".equals(tTempVo.getParam("RepayCode11Chain")))) {
						String[] thisColumn = tTempVo.getParam("RepayCode11Chain").split("-");
						chainAcDate = parse.stringToInteger(thisColumn[0]) - 19110000;
						chainBatchNo = thisColumn[1];
						chainDetailSeq = parse.stringToInteger(thisColumn[2]);
						this.info("RepayCode11Chain = " + tTempVo.getParam("RepayCode11Chain"));
						chainFlag = "Y";
					}
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
				occursList.putParam("OOTitaEntdy", tBatxOthersVO.getTitaEntdy());
				occursList.putParam("OOTitaTlrNo", tBatxOthersVO.getTitaTlrNo());
				occursList.putParam("OOTitaTxtNo", tBatxOthersVO.getTitaTxtNo());
				occursList.putParam("OOChainAcDate", chainAcDate);
				occursList.putParam("OOChainBatchNo", chainBatchNo);
				occursList.putParam("OOChainDetailSeq", chainDetailSeq);
				occursList.putParam("OOChainFlag", chainFlag);
				String modifyFg = "N";
				String deleteFg = "N";

				if (tBatxOthersVO.getTitaEntdy() == 0) {
					if (tBatxOthersVO.getAcDate() == titaVo.getEntDyI()) {
						// 可修改刪除
						modifyFg = "Y";
						deleteFg = "Y";
					} else {
						modifyFg = "N";
						deleteFg = "Y";
						// 可刪除
					}
				}
				occursList.putParam("OOModifyFlag", modifyFg);
				occursList.putParam("OODeleteFlag", deleteFg);
				i++;
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			if (i == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}

		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}