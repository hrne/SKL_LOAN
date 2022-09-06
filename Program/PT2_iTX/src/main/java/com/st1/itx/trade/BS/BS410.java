package com.st1.itx.trade.BS;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.UpdateBankRemitFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("BS410")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class BS410 extends TradeBuffer {
	@Autowired
	public FileCom fileCom;

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public UpdateBankRemitFileVo updateBankRemitFileVo;

	@Autowired
	public BankRemitService bankRemitService;

	@Autowired
	public WebClient webClient;

	private int iAcDate = 0;
	private String iTlrNo = "";

	private int tableSize = 0;
	private BigDecimal bigDe100 = new BigDecimal("100");

	private List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
	private Slice<BankRemit> slBankRemit = null;

//	寄送筆數
	private int commitCnt = 500;

	private String sendMsg = "";
	private Boolean checkFlag = true;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS410 ");
		this.totaVo.init(titaVo);

		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;

		iTlrNo = titaVo.getTlrNo();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

//		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", titaVo.getTlrNo(),
//				"L4200" + " - " + iBatchNo + " 整批處理中，請稍候", titaVo);

//		暫定路徑 待討論過後決定抓取路徑方法

		String filena = titaVo.getParam("FILENA").trim();

		if (filena.indexOf(";") < 0) {
			filena = filena + ";";
		}

//		檢核檔名、檔案格式
		checkFile(filena, titaVo);

		String[] filelist = filena.split(";");
		for (String filename : filelist) {
			this.info("fileName : " + filename);
//				重製VO的OccursList
			initialFileVoOccursList();
//TODO: 回銷檔名稱待訂
			if (filename.indexOf("LNM24r") >= 0) {
				String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + filename;
				this.info("回銷檔 Start With -> " + filePath1);

				ArrayList<String> dataLineList1 = new ArrayList<>();
				try {
					dataLineList1 = fileCom.intputTxt(filePath1, "big5");
				} catch (IOException e) {
					this.info("BS410(" + filePath1 + ") : " + e.getMessage());
				}
				try {
					procBatxRmtf(filePath1, dataLineList1, titaVo);
				} catch (LogicException e) {
					sendMsg = e.getMessage();
					checkFlag = false;
				}
			}
		}

//		執行無誤者連結查詢清單
		if (checkFlag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4001", titaVo.getTlrNo(), "上傳完成", titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4104", titaVo.getTlrNo(), sendMsg, titaVo);
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

	private void procBatxRmtf(String filePath1, ArrayList<String> dataLineList1, TitaVo titaVo) throws LogicException {

		BankRemit tBankRemit = new BankRemit();
		BankRmtfId tBankRmtfId = new BankRmtfId();

		if (dataLineList1 != null && dataLineList1.size() != 0) {
			// 使用資料容器內定義的方法切資料
			updateBankRemitFileVo.setValueFromFile(dataLineList1);

			ArrayList<OccursList> uploadFile = updateBankRemitFileVo.getOccursList();
			HashMap<String, BigDecimal> negaDraw = new HashMap<>();
			HashMap<String, BigDecimal> negaDepo = new HashMap<>();
			HashMap<String, BigDecimal> posiDepo = new HashMap<>();
			for (OccursList t : uploadFile) {
				this.info("t = " + t);
				String OccSeq = t.get("OccSeq");
				String OccNum = t.get("OccNum");
				String OccPayStatus = t.get("OccPayStatus");
				String OccPayModifyDate = t.get("OccPayModifyDate");
				String OccRelNum = t.get("OccRelNum");
				String OccPayName = t.get("OccPayName");
				String OccChequeNum = t.get("OccChequeNum");
				String OccReceiveDate = t.get("OccReceiveDate");
				String OccPaymentDate = t.get("OccPaymentDate");
				// 退款
				if ("-".equals(OccRelNum.substring(6, 7))) {
					String wkTlrNo = OccRelNum.substring(0, 6);
					String wkTxtNo = OccRelNum.substring(7, 15);
					this.info(" RelNum wkTlrNoTxtNo =" + wkTlrNo + "-" + wkTxtNo);
					tBankRemit = bankRemitService.findL4104AFirst(wkTlrNo, wkTxtNo, 5, titaVo);
				}
				// 撥款
				else {
					int wkCustNo = parse.stringToInteger(OccRelNum.substring(0, 7));
					int wkFacmNo = parse.stringToInteger(OccRelNum.substring(8, 11));
					int wkBormNo = parse.stringToInteger(OccRelNum.substring(12, 15));
					this.info(" RelNum wkCustNo =" + wkCustNo + "-" + wkFacmNo + "-" + wkBormNo);
					tBankRemit = bankRemitService.findL4104BFirst(wkCustNo, wkFacmNo, wkBormNo, 1, titaVo);
				}
				if (tBankRemit != null) {

					this.info("tBankRemit =" + tBankRemit);
					this.info("OccPayStatus =" + OccPayStatus);

				} else {

					throw new LogicException("E2003", "相關號碼 = " + OccRelNum);// 查無資料
				}

				tBankRemit.setPayCode(OccPayStatus);
			}

			try {
				bankRemitService.update(tBankRemit, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException("E0005", "BankRemit update Fail");
			}

//			}
//
//		} else {
		}

	}

	private void initialFileVoOccursList() {
		ArrayList<OccursList> occursList = new ArrayList<OccursList>();
		updateBankRemitFileVo.setOccursList(occursList);
	}

	private void checkFile(String fileName, TitaVo titaVo) throws LogicException {
		this.info("checkFile Start...");
//		1.檢核檔名，不為下列區間者為False
//		2.檢核檔案內容，若格式不合，此階段先提出錯誤

		this.info("fileName ..." + fileName);

		String[] filelist = fileName.split(";");

		if (filelist == null || filelist.length == 0) {
			sendMsg = sendMsg + "請輸入檔案。";
			checkFlag = false;
		}

		for (String filename : filelist) {
//			每次執行setValue後，需初始化occursList
			initialFileVoOccursList();

			this.info("filename ..." + filename);

			String filePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + filename;

			ArrayList<String> dataLineList1 = new ArrayList<>();
			try {
				dataLineList1 = fileCom.intputTxt(filePath, "big5");
			} catch (IOException e) {
				this.info("BS410(" + filePath + ") : " + e.getMessage());
			}

			if (filename.indexOf("LNM24r") >= 0) {
				try {
					updateBankRemitFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else {
				sendMsg = sendMsg + removeDot(filename) + "，檔名不符本交易處理範圍。";
				checkFlag = false;
				break;
			}

		}
	}

	private String removeDot(String input) {

		if (input.indexOf(".") >= 0) {
			input = input.substring(0, input.indexOf("."));
		}

		return input;
	}
}