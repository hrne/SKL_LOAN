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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * EntryDateFrom=9,7<br>
 * EntryDateTo=9,7<br>
 * RepayCode=9,2<br>
 * CustNo=9,7<br>
 * END=X,1<br>
 */

@Service("L4925")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4925 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4925.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4925 ");
		this.totaVo.init(titaVo);

		int iEntryDateFrom = parse.stringToInteger(titaVo.getParam("EntryDateFrom")) + 19110000;
		int iEntryDateTo = parse.stringToInteger(titaVo.getParam("EntryDateTo")) + 19110000;
		int iRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		List<String> iProcStsCode = new ArrayList<String>();

		List<BatxDetail> lBatxDetail = new ArrayList<BatxDetail>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

//		全部
		if ("A".equals(titaVo.getParam("ProcStsCode"))) {
			iProcStsCode.add("0");
			iProcStsCode.add("1");
			iProcStsCode.add("2");
			iProcStsCode.add("3");
			iProcStsCode.add("4");
			iProcStsCode.add("5");
			iProcStsCode.add("6");
			iProcStsCode.add("7");
		}
//		待處理
		else if ("R".equals(titaVo.getParam("ProcStsCode"))) {
			iProcStsCode.add("0");
			iProcStsCode.add("2");
			iProcStsCode.add("3");
			iProcStsCode.add("4");
		}
//		已處理
		else if ("S".equals(titaVo.getParam("ProcStsCode"))) {
			iProcStsCode.add("5");
			iProcStsCode.add("6");
			iProcStsCode.add("7");
		} else {
			iProcStsCode.add(titaVo.getParam("ProcStsCode"));
		}

		Slice<BatxDetail> sBatxDetail = null;

		if (iRepayCode == 0 && iCustNo == 0) {
			sBatxDetail = batxDetailService.findL4925AEq(iEntryDateFrom, iEntryDateTo, 0, 9999999, 0, 99, iProcStsCode,
					this.index, this.limit, titaVo);
		} else if (iRepayCode == 0) {
			sBatxDetail = batxDetailService.findL4925AEq(iEntryDateFrom, iEntryDateTo, iCustNo, iCustNo, 0, 99,
					iProcStsCode, this.index, this.limit, titaVo);
		} else if (iCustNo == 0) {
			sBatxDetail = batxDetailService.findL4925AEq(iEntryDateFrom, iEntryDateTo, 0, 9999999, iRepayCode,
					iRepayCode, iProcStsCode, this.index, this.limit, titaVo);
		} else {
			sBatxDetail = batxDetailService.findL4925AEq(iEntryDateFrom, iEntryDateTo, iCustNo, iCustNo, iRepayCode,
					iRepayCode, iProcStsCode, this.index, this.limit, titaVo);
		}

		lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

		if (lBatxDetail != null && lBatxDetail.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (sBatxDetail != null && sBatxDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (BatxDetail tBatxDetail : lBatxDetail) {

				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(tBatxDetail.getProcNote());

				this.info("tBatxDetail.getProcNote() : " + tBatxDetail.getProcNote());

				OccursList occursList = new OccursList();
				occursList.putParam("OOAcDate", tBatxDetail.getAcDate());
				occursList.putParam("OOBatchNo", tBatxDetail.getBatchNo());
				occursList.putParam("OODetailSeq", tBatxDetail.getDetailSeq());
				occursList.putParam("OORepayCode", tBatxDetail.getRepayCode());
				occursList.putParam("OOEntryDate", tBatxDetail.getEntryDate());
				occursList.putParam("OOCustNo", tBatxDetail.getCustNo());
				occursList.putParam("OOFacmNo", tBatxDetail.getFacmNo());
				occursList.putParam("OORepayType", tBatxDetail.getRepayType());
				occursList.putParam("OOReconCode", tBatxDetail.getReconCode());
				occursList.putParam("OORepayAmt", tBatxDetail.getRepayAmt());
				occursList.putParam("OOAcctAmt", tBatxDetail.getAcctAmt());
				occursList.putParam("OODisacctAmt", tBatxDetail.getDisacctAmt());
				occursList.putParam("OOProcStsCode", tBatxDetail.getProcStsCode());
				occursList.putParam("OOProcCode", tBatxDetail.getProcCode());

				String procNote = "";
				if (tBatxDetail.getProcNote() != null) {
					tempVo = tempVo.getVo(tBatxDetail.getProcNote());

					if (tempVo.get("CheckMsg") != null && tempVo.get("CheckMsg").length() > 0) {
						procNote = " 檢核訊息:" + tempVo.get("CheckMsg") + " ";
					}
					if (tempVo.get("ErrorMsg") != null && tempVo.get("ErrorMsg").length() > 0) {
						procNote = procNote + " 錯誤訊息:" + tempVo.get("ErrorMsg") + " ";
					}
					if (tempVo.get("Note") != null && tempVo.get("Note").length() > 0) {
						procNote = procNote + " 摘要:" + tempVo.get("Note");
					}
//					當吃檔進去時不會寫入還款類別，檢核後才會寫入。
//					若該筆無還款類別且為數字型態，顯示虛擬帳號
					if (tempVo.get("VirtualAcctNo") != null && tBatxDetail.getRepayType() == 0
							&& isNumeric(tempVo.get("VirtualAcctNo"))) {
						procNote = procNote + " 虛擬帳號:" + tempVo.get("VirtualAcctNo");
					}
				}

				occursList.putParam("OOProcNote", procNote);

				occursList.putParam("OOTitaTlrNo", tBatxDetail.getTitaTlrNo());
				occursList.putParam("OOTitaTxtNo", tBatxDetail.getTitaTxtNo());
				occursList.putParam("OOTxSn",
						titaVo.getKinbr() + tBatxDetail.getTitaTlrNo() + tBatxDetail.getTitaTxtNo());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
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