package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxInquiry;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxInquiryService;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.service.springjpa.cm.L6046ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L6064ServiceImpl;

@Service("L6046")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L6046 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	TxInquiryService sTxInquiryService;
	@Autowired
	public TxTranCodeService txTranCodeService;
	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	CustMainService sCustMainService;

	@Autowired
	public L6046ServiceImpl l6046ServiceImpls;

	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6045 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 64 * 200 = 12,800

		List<Map<String, String>> L6046DateList = null;
		try {
			L6046DateList = l6046ServiceImpls.findAll(titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		if (L6046DateList != null) {
			for (Map<String, String> t : L6046DateList) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo",t.get("CustNo"));
				occursList.putParam("OOCustId",t.get("CustId"));
				occursList.putParam("OOXXCustId",t.get("XXCustId"));
				occursList.putParam("OOCustName",t.get("CustName"));
				occursList.putParam("OOCustName1",t.get("CustName1"));
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}