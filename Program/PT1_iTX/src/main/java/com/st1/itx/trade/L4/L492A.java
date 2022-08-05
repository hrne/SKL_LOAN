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
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L492A")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L492A extends TradeBuffer {

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

	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L492A ");
		this.totaVo.init(titaVo);

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo").trim());
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo").trim());
		int repayCode = parse.stringToInteger(titaVo.getParam("RepayCode").trim());
		int repayType = parse.stringToInteger(titaVo.getParam("RepayType").trim());

		List<BatxDetail> lBatxDetail = new ArrayList<BatxDetail>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		Slice<BatxDetail> sBatxDetail = null;

		List<String> procStsCode = new ArrayList<String>();
		procStsCode.add("2");
		procStsCode.add("3");
		procStsCode.add("4");
		sBatxDetail = batxDetailService.findL492AEq(custNo, titaVo.getEntDyI() + 19110000, procStsCode, this.index,
				this.limit);

		lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

		if (lBatxDetail != null && lBatxDetail.size() != 0) {
			for (BatxDetail tBatxDetail : lBatxDetail) {
				if (repayCode > 0 && tBatxDetail.getRepayCode() != repayCode) {
					continue;
				}
				if (repayType > 0 && tBatxDetail.getRepayType() != repayType) {
					continue;
				}
				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(tBatxDetail.getProcNote());

//				aml為1.需審查/確認  or 2.為凍結名單/未確定名單者     跳過
				if ("1".equals(tempVo.get("AmlRsp1")) || "1".equals(tempVo.get("AmlRsp2"))
						|| "2".equals(tempVo.get("AmlRsp1")) || "2".equals(tempVo.get("AmlRsp2"))) {
					continue;
				}

				String procNote = "";
				String dscpt = "";
				String note = "";

				cnt = cnt + 1;

				if (tempVo.get("Note") != null) {
					note = tempVo.get("Note");
				}

				if (tempVo.get("DscptCode") != null) {
					dscpt = tempVo.getParam("DscptCode");
				}

				this.info("procNote : " + procNote);

				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", tBatxDetail.getCustNo());
				occursList.putParam("OOFacmNo", tBatxDetail.getFacmNo());
				occursList.putParam("OOEntryDate", tBatxDetail.getEntryDate());
				occursList.putParam("OORepayCode", tBatxDetail.getRepayCode());
				occursList.putParam("OORepayType", tBatxDetail.getRepayType());
				occursList.putParam("OODetailSeq", tBatxDetail.getBatxDetailId().getDetailSeq());
				occursList.putParam("OOBatchNo", tBatxDetail.getBatxDetailId().getBatchNo());
				occursList.putParam("OOAcDate", tBatxDetail.getBatxDetailId().getAcDate());
				occursList.putParam("OOAcctCode", tBatxDetail.getReconCode());
				occursList.putParam("OORepayAmt", tBatxDetail.getRepayAmt());
				occursList.putParam("OOAcNo", tBatxDetail.getRepayAcCode());
				occursList.putParam("OORvNo", tBatxDetail.getRvNo());
				occursList.putParam("OODscpt", dscpt);
				occursList.putParam("OONote", note);

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);

			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		if (cnt == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
//			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}