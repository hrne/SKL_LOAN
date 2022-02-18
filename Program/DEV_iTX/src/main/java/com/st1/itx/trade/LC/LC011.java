package com.st1.itx.trade.LC;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LC011ServiceImpl;

@Service("LC011")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC011 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public TxTranCodeService sTxTranCodeService;

	@Autowired
	CdBranchService cdBranchService;

	@Autowired
	private LC011ServiceImpl lC011ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	private HashMap<String, String> tlrItems = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC011 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 20;

		List<Map<String, String>> dList = null;

		try {
			dList = lC011ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E0014", "");
		}

		if (dList == null || dList.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易明細資料");
		}

		String iBrNo = titaVo.get("iBrNo").trim();

		CdBranch cdBranch = cdBranchService.findById(iBrNo);

		for (Map<String, String> d : dList) {
			OccursList occursList = new OccursList();
			occursList.putParam("CalDate", Integer.valueOf(d.get("CalDate").toString().trim()) - 19110000);
			occursList.putParam("CalTime", d.get("CalTime"));
			occursList.putParam("Entdy", Integer.valueOf(d.get("Entdy").toString().trim()) - 19110000);
			occursList.putParam("TxNo", d.get("TxNo"));
			occursList.putParam("TranNo", d.get("TranNo"));
			occursList.putParam("MrKey", d.get("MrKey"));
			occursList.putParam("CurName", d.get("CurName"));
			occursList.putParam("TxAmt", new BigDecimal(d.get("TxAmt").toString().trim()));
			occursList.putParam("BrNo", iBrNo + " " + cdBranch.getBranchShort());
			occursList.putParam("FlowMode", d.get("FlowMode"));

			String iTlrItem = "";
			// 放行、審核放行，抓經辦代碼
			int flowStep = Integer.valueOf(d.get("FlowStep"));
			if (flowStep == 2 || flowStep == 4) {
				occursList.putParam("TlrNo", d.get("OrgTxNo").toString().substring(4, 10));
				occursList.putParam("SupNo", d.get("TlrNo"));
				iTlrItem = "";
				iTlrItem = inqCdEmp(d.get("OrgTxNo").toString().substring(4, 10), titaVo);
				occursList.putParam("TlrItem", iTlrItem);
				iTlrItem = "";
				iTlrItem = inqCdEmp(d.get("TlrNo"), titaVo);
				occursList.putParam("SupItem", iTlrItem);
			} else {
				occursList.putParam("TlrNo", d.get("TlrNo"));
				occursList.putParam("SupNo", d.get("SupNo"));
				iTlrItem = "";
				iTlrItem = inqCdEmp(d.get("TlrNo"), titaVo);
				occursList.putParam("TlrItem", iTlrItem);
				iTlrItem = "";
				iTlrItem = inqCdEmp(d.get("SupNo"), titaVo);
				occursList.putParam("SupItem", iTlrItem);
			}
			occursList.putParam("FlowType", d.get("FlowType"));
			occursList.putParam("FlowStep", d.get("FlowStep"));
			
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(d.get("TranData"));
			occursList.putParam("Funcind", tTempVo.get("FUNCIND"));

			if ("1".equals(d.get("Hcode").toString()) && Integer.valueOf(d.get("OrgEntdy").toString()) > 0
					&& d.get("OrgEntdy").toString().equals(d.get("Entdy").toString())) {
				occursList.putParam("Hcode", 3);
			} else {
				occursList.putParam("Hcode", d.get("Hcode"));
			}

			occursList.putParam("Status", d.get("ActionFg"));
			occursList.putParam("FlowNo", d.get("FlowNo"));
			if (Integer.valueOf(d.get("OrgEntdy").toString()) > 0
					&& d.get("OrgEntdy").toString().equals(d.get("Entdy").toString())) {
				occursList.putParam("OOOrgEntdy", Integer.valueOf(d.get("OrgEntdy").toString().trim()) - 19110000);
			} else {
				occursList.putParam("OOOrgEntdy", "");
			}

			occursList.putParam("TranItem", d.get("TranItem"));

			if (Integer.valueOf(d.get("AcCnt")) > 0) {
				// 當天訂正及被訂正交易 無分錄
				if (("1".equals(d.get("Hcode").toString())
						&& d.get("OrgEntdy").toString().equals(d.get("Entdy").toString()))
						|| "1".equals(d.get("ActionFg").toString())) {
					occursList.putParam("AcCnt", 0);
				} else {
					occursList.putParam("AcCnt", 1);
				}

			} else {
				occursList.putParam("AcCnt", 0);
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		if (dList != null && dList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢使用者

	private String inqCdEmp(String tlrNo, TitaVo titaVo) {
		String tlrItem = "";

		if ("".equals(tlrNo)) {
			return tlrItem;
		}

		if (tlrItems.size() > 0) {
			if (tlrItems.get(tlrNo) != null) {
				tlrItem = tlrItems.get(tlrNo).toString();
			}
		}

		if ("".equals(tlrItem)) {
			CdEmp cdEmp = cdEmpService.findById(tlrNo, titaVo);
			if (cdEmp == null) {
				tlrItem = tlrNo;
			} else {
				tlrItem = cdEmp.getFullname();
				tlrItems.put(tlrNo, tlrItem);
			}
		}
		return tlrNo + " " + tlrItem;
	}
}