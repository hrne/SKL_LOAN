package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.service.TxAttachmentService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.springjpa.cm.L5903ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * ApplDateFrom=9,7<br>
 * ApplDateTo=9,7<br>
 * UsageCode=9,2<br>
 * ApplCode=9,2<br>
 * END=X,1<br>
 */

@Service("L5903")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5903 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5903ServiceImpl l5903ServiceImpl;

	@Autowired
	TxAttachmentService sTxAttachmentService;

	@Autowired
	public TxTellerService txTellerService;
	@Autowired
	private MakeExcel makeExcel;
	@Autowired
	private LoanCom loanCom;
	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5903 ");
		this.totaVo.init(titaVo);

		String Searc = titaVo.getParam("Searc");

		if (Searc.equals("2")) {

//			 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
			this.index = titaVo.getReturnIndex();
//			設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
			this.limit = 100;

			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				// *** 折返控制相關 ***
				resultList = l5903ServiceImpl.findAll(this.index, this.limit, titaVo);
			} catch (Exception e) {
				this.error("l5903ServiceImpl findAll " + e.getMessage());
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() > 0) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (resultList.size() == this.limit && hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}

				for (Map<String, String> result : resultList) {

					int applDate = parse.stringToInteger(result.get("F6"));
					int returnDate = parse.stringToInteger(result.get("F7"));

					if (applDate > 19110000) {
						applDate = applDate - 19110000;
					}

					if (returnDate > 19110000) {
						returnDate = returnDate - 19110000;
					}

					OccursList occursList = new OccursList();
					occursList.putParam("OOCustNo", result.get("F0"));
					occursList.putParam("OOFacmNo", result.get("F1"));
					occursList.putParam("OOApplSeq", result.get("F2"));
					occursList.putParam("OOCustName", result.get("F3"));
					occursList.putParam("OOKeeperEmpName", result.get("F4"));
					occursList.putParam("OOKeeperEmpNo", result.get("F13"));
					occursList.putParam("OOApplEmpName", result.get("F5"));
					occursList.putParam("OOApplEmpNo", result.get("F14"));
					occursList.putParam("OOApplDate", applDate);
					occursList.putParam("OOReturnDate", returnDate);
					occursList.putParam("OOReturnEmpName", result.get("F8"));
					occursList.putParam("OOReturnEmpNo", result.get("F15"));
					occursList.putParam("OOUsageCode", result.get("F9"));
					occursList.putParam("OOCopyCode", result.get("F10"));
					occursList.putParam("OORemark", result.get("F11"));
					occursList.putParam("OOApplObj", result.get("F12"));
					occursList.putParam("OOModifyFg", result.get("ModifyFg"));
					occursList.putParam("OODeleteFg", result.get("DeleteFg"));
					occursList.putParam("OOEntDy", result.get("TitaEntDy"));
					occursList.putParam("OOTxNo", "0000" + result.get("TitaTlrNo") + result.get("TitaTxtNo"));
					occursList.putParam("OOReturnFg", result.get("ReturnFg"));
					occursList.putParam("OOTitaActFg", result.get("TitaActFg"));
					occursList.putParam("OOFacmNoMemo", result.get("F17"));
					occursList.putParam("OOEnable", result.get("Enable"));
					occursList.putParam("OOKeeperEnable", result.get("KeeperEnable"));
					occursList.putParam("OOActFgX", result.get("ActFgX"));

					// 判斷是否應顯示【附件查詢】按鈕

					Slice<TxAttachment> slTxAttachment = sTxAttachmentService.findByTran("L5103",
							FormatUtil.pad9(result.get("F0"), 7) + "-" + FormatUtil.pad9(result.get("F1"), 3) + "-"
									+ FormatUtil.pad9(result.get("F2"), 3),
							0, 1, titaVo);
					List<TxAttachment> lTxAttachment = slTxAttachment == null ? null : slTxAttachment.getContent();
					occursList.putParam("OOHasAttachment",
							lTxAttachment != null && !lTxAttachment.isEmpty() ? "Y" : "N");

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				}

			} else {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		} else {

			getExcel(titaVo);

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l5903ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}

	// 產生excel
	private void getExcel(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> resultListAll = new ArrayList<Map<String, String>>();
		try {
			// *** 折返控制相關 ***
			resultListAll = l5903ServiceImpl.findAll(0, Integer.MAX_VALUE, titaVo);
		} catch (Exception e) {
			this.error("l5903ServiceImpl findAll " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
				.setRptCode("L5903").setRptItem("檔案借閱明細資料查詢").build();
		// open
		makeExcel.open(titaVo, reportVo, "L5903檔案借閱明細資料查詢");
		// 設定excel欄位寬度
		makeExcel.setWidth(1, 10);// 戶號
		makeExcel.setWidth(2, 10);// 額度
		makeExcel.setWidth(3, 20);// 同押品額度
		makeExcel.setWidth(4, 10);// 申請序號
		makeExcel.setWidth(5, 50);// 戶名
		makeExcel.setWidth(6, 12);// 登放狀態
		makeExcel.setWidth(7, 24);// 管理人
		makeExcel.setWidth(8, 24);// 借閱人
		makeExcel.setWidth(9, 12);// 借閱日期
		makeExcel.setWidth(10, 12);// 歸還日期
		makeExcel.setWidth(11, 24);// 歸還人
		makeExcel.setWidth(12, 15);// 正本/影本
		makeExcel.setWidth(13, 15);// 用途
		makeExcel.setWidth(14, 50);// 備註
		makeExcel.setValue(1, 1, "戶號");
		makeExcel.setValue(1, 2, "額度");
		makeExcel.setValue(1, 3, "同押品額度");
		makeExcel.setValue(1, 4, "申請序號");
		makeExcel.setValue(1, 5, "戶名");
		makeExcel.setValue(1, 6, "登放狀態");
		makeExcel.setValue(1, 7, "管理人");
		makeExcel.setValue(1, 8, "借閱人");
		makeExcel.setValue(1, 9, "借閱日期");
		makeExcel.setValue(1, 10, "歸還日期");
		makeExcel.setValue(1, 11, "歸還人");
		makeExcel.setValue(1, 12, "正本/影本");
		makeExcel.setValue(1, 13, "用途");
		makeExcel.setValue(1, 14, "備註");

		if (resultListAll != null && resultListAll.size() > 0) {
			int R = 1;

			for (Map<String, String> result : resultListAll) {
				R++;
				String custNo = String.format("%07d", parse.stringToInteger(result.get("F0")));
				String facmNo = String.format("%03d", parse.stringToInteger(result.get("F1")));
				String facmNoMemo = result.get("F17");
				String applSeq = result.get("F2");
				String custName = result.get("F3");
				String actFgX = result.get("ActFgX");
				String keeperEmpName = result.get("F4");
				String keeperEmpNo = result.get("F13");
				String applEmpName = result.get("F5");
				String applEmpNo = result.get("F14");
				String applDateX = result.get("F6");
				String returnDateX = result.get("F7");
				String returnEmpName = result.get("F8");
				String returnEmpNo = result.get("F15");
				String usageCode = result.get("F9");
				String usageCodeX = loanCom.getCdCodeX("UsageCodeX", usageCode, titaVo);
				String copyCode = result.get("F10");
				String copyCodeX = loanCom.getCdCodeX("CopyCode", copyCode, titaVo);
				String remark = result.get("F11");

				makeExcel.setValue(R, 1, custNo);// 戶號
				makeExcel.setValue(R, 2, facmNo);// 額度
				makeExcel.setValue(R, 3, facmNoMemo);// 同押品額度
				makeExcel.setValue(R, 4, applSeq);// 申請序號
				makeExcel.setValue(R, 5, custName);// 戶名
				makeExcel.setValue(R, 6, actFgX);// 登放狀態
				makeExcel.setValue(R, 7, keeperEmpNo + "-" + keeperEmpName);// 管理人
				makeExcel.setValue(R, 8, applEmpNo + "-" + applEmpName);// 借閱人
				makeExcel.setValue(R, 9, applDateX);// 借閱日期
				makeExcel.setValue(R, 10, returnDateX);// 歸還日期
				makeExcel.setValue(R, 11, returnEmpNo + "-" + returnEmpName);// 歸還人
				makeExcel.setValue(R, 12, copyCode + "-" + copyCodeX);// 正本/影本
				makeExcel.setValue(R, 13, usageCode + "-" + usageCodeX);// 用途
				makeExcel.setValue(R, 14, remark);// 備註
			}
		}
		makeExcel.toExcel(makeExcel.close());

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", "", "L5903已完成", titaVo);

	}

}