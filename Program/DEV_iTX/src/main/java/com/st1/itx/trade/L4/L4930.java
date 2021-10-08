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

@Service("L4930")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4930 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4930 ");
		this.totaVo.init(titaVo);

//		0.將1.溢繳2.不足利息3.積欠期款 由2.人工處理改為 7.虛擬轉暫收
//		1.查詢BatxDetail所有未入帳金額>0者(勾選畫面)
		int iFunctionCode = 0;
		int iCustNo = 0;
		int iAcDate = 0;
		String iBatchNo = "";

		iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		iBatchNo = titaVo.getParam("BatchNo");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

		Slice<BatxDetail> sBatxDetail = null;

		List<BatxDetail> lBatxDetail = new ArrayList<BatxDetail>();

		List<String> func2 = new ArrayList<String>();
		// 整批轉暫收
		func2.add("2");
		func2.add("3");
		func2.add("4");
		func2.add("7");
		// 整批訂正
		List<String> func1 = new ArrayList<String>();
		func1.add("5");
		func1.add("6");
		func1.add("7");

		if (iFunctionCode == 2) {
			if (iCustNo == 0) {
				sBatxDetail = batxDetailService.findL4930BAEq(iAcDate, iBatchNo, func2, this.index, this.limit, titaVo);
			} else {
				sBatxDetail = batxDetailService.findL4930CAEq(iAcDate, iBatchNo, iCustNo, func2, this.index, this.limit,
						titaVo);
			}
		} else if (iFunctionCode == 1) {
			if (iCustNo == 0) {
				sBatxDetail = batxDetailService.findL4930BHEq(iAcDate, iBatchNo, func1, this.index, this.limit, titaVo);
			} else {
				sBatxDetail = batxDetailService.findL4930CHEq(iAcDate, iBatchNo, iCustNo, func1, this.index, this.limit,
						titaVo);
			}
		}

		lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

		if (lBatxDetail != null && lBatxDetail.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (sBatxDetail != null && sBatxDetail.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			int cnt = 0;
			for (BatxDetail tBatxDetail : lBatxDetail) {
// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.虛擬轉暫收
				cnt = cnt + 1;

//					傳回AcDate BatchNo Seq 勾選傳值
				OccursList occursList = new OccursList();
				occursList.putParam("OOAcDate", tBatxDetail.getAcDate());
				occursList.putParam("OOBatchNo", tBatxDetail.getBatchNo());
				occursList.putParam("OODetailSeq", tBatxDetail.getDetailSeq());
				occursList.putParam("OOCustNo", tBatxDetail.getCustNo());
				occursList.putParam("OOFacmNo", tBatxDetail.getFacmNo());
				occursList.putParam("OORepayType", tBatxDetail.getRepayType());
				occursList.putParam("OORepayAmt", tBatxDetail.getRepayAmt());
				occursList.putParam("OOAcctAmt", tBatxDetail.getAcctAmt());
				occursList.putParam("OODisAcctAmt", tBatxDetail.getRepayAmt().subtract(tBatxDetail.getAcctAmt()));
				occursList.putParam("OOProcCode", tBatxDetail.getProcCode());
				occursList.putParam("OOProcStsCode", tBatxDetail.getProcStsCode());
				occursList.putParam("OOEntryDate", tBatxDetail.getEntryDate());

				String procNote = "";
				if (tBatxDetail.getProcNote() != null) {

					TempVo tempVo = new TempVo();
					tempVo = tempVo.getVo(tBatxDetail.getProcNote());

					if (tempVo.get("CheckMsg") != null && tempVo.get("CheckMsg").length() > 0) {
						procNote = "檢核訊息:" + tempVo.get("CheckMsg") + " ";
					}
					if (tempVo.get("ErrorMsg") != null && tempVo.get("ErrorMsg").length() > 0) {
						procNote = procNote + "錯誤訊息:" + tempVo.get("ErrorMsg") + " ";
					}
					if (tempVo.get("Note") != null && tempVo.get("Note").length() > 0) {
						procNote = procNote + "摘要:" + tempVo.get("Note");
					}
//					當吃檔進去時不會寫入還款類別，檢核後才會寫入。
//					若該筆無還款類別且為數字型態，顯示虛擬帳號
					if (tempVo.get("VirtualAcctNo") != null && tBatxDetail.getRepayType() == 0
							&& isNumeric(tempVo.get("VirtualAcctNo"))) {
						procNote = procNote + "虛擬帳號:" + tempVo.get("VirtualAcctNo");
					}
				}

				occursList.putParam("OOProcNote", procNote);

				if ("".equals(tBatxDetail.getTitaTxtNo())) {
					occursList.putParam("OOTxSn", "");
				} else {
					occursList.putParam("OOTxSn",
							titaVo.getKinbr() + tBatxDetail.getTitaTlrNo() + tBatxDetail.getTitaTxtNo());
				}

				this.totaVo.addOccursList(occursList);
			}

			if (iFunctionCode == 2 && lBatxDetail.size() != 0 && cnt == 0) {
				throw new LogicException("E0001", "資料狀態皆不為 : 2.人工處理 3.檢核錯誤 4.檢核正常 7.轉暫收");
			}
			if (iFunctionCode == 1 && lBatxDetail.size() != 0 && cnt == 0) {
				throw new LogicException("E0001", "資料狀態皆不為 :5.人工入帳 6.批次入帳 7.轉暫收");
			}
		} else {
			throw new LogicException("E0001", "查無資料");
		}
//		2.勾選送出 L420C
//			a.若已入帳=0，執行L3210
//			b.若已入帳>0，將狀態由7.虛擬轉暫收改為6.批次入帳

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