package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.springjpa.cm.L2077ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2077")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2077 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2077.class);

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public L2077ServiceImpl l2077ServiceImpl;

	@Autowired
	public LoanCloseBreachCom loanCloseBreachCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	BaTxCom baTxCom;
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	private List<ClFac> lClFac = new ArrayList<ClFac>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2077 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 293 * 100 = 29300

		// tita
		// 申請日期
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));
		// 入帳日期
		int iTranDate = parse.stringToInteger(titaVo.getParam("TranDate"));
		// 戶號查詢區間
		int iCustNoS = parse.stringToInteger(titaVo.getParam("CustNoS"));
		int iCustNoE = parse.stringToInteger(titaVo.getParam("CustNoE"));

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l2077ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}
		this.info("resultList = " + resultList);
		this.info("resultList.size() = " + resultList.size());
		if (resultList != null && resultList.size() != 0) {

			this.info("Size =" + resultList.size());
			int i = 1;
			for (Map<String, String> result : resultList) {

				int wkCloseDate = 0;
//				只找同戶號額度最後一筆序號
				if (i < resultList.size() && result.get("CustNo") == resultList.get(i).get("CustNo")
						&& result.get("FacmNo") == resultList.get(i).get("FacmNo")) {
					i++;
					continue;
				}
				i++;
				// new occurs
				OccursList occursList = new OccursList();
				// 宣告
				Timestamp ts = parse.StringToSqlDateO(result.get("CreateDate"), result.get("CreateDate"));
				String createDate = "";
				String wkRepayFg = "";
				DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				createDate = sdf.format(ts);
				this.info("createDate = " + createDate);
				// new table
				CustMain tCustMain = new CustMain();
				int custno = parse.stringToInteger(result.get("CustNo"));
				tCustMain = sCustMainService.custNoFirst(custno, custno);
				if (tCustMain == null) {
					tCustMain = new CustMain();
				}

				wkCloseDate = parse.stringToInteger(result.get("CloseDate"));
				if (wkCloseDate > 0) {
					wkCloseDate = wkCloseDate - 19110000;
				}
				// 查詢各項費用
				baTxCom.setTxBuffer(txBuffer);
				baTxCom.settingUnPaid(parse.stringToInteger(result.get("EntryDate")),
						parse.stringToInteger(result.get("CustNo")), parse.stringToInteger(result.get("FacmNo")), 000,
						0, BigDecimal.ZERO, titaVo);
				this.info("CloseBreach = " + baTxCom.getShortCloseBreach());
				this.info("FunCode = " + result.get("FunCode"));
				if (("2".equals(result.get("FunCode")) || "3".equals(result.get("FunCode")))
						&& baTxCom.getShortCloseBreach().compareTo(BigDecimal.ZERO) == 0) {
					this.info("wkRepayFg = \"Y\"");
					wkRepayFg = "Y";
				}

				// 全部結案

				// 擔保品與額度關聯檔
				Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(custno,
						parse.stringToInteger(result.get("FacmNo")), parse.stringToInteger(result.get("FacmNo")), 0,
						Integer.MAX_VALUE, titaVo);
				lClFac = slClFac == null ? null : slClFac.getContent();
				boolean isAllClose = true;
				if (lClFac != null) {

					for (ClFac t : lClFac) {

						// 全部結案
						List<ClFac> l2ClFac = new ArrayList<ClFac>(); // 擔保品與額度關聯檔
						Slice<ClFac> slClFac2 = clFacService.clNoEq(t.getClCode1(), t.getClCode2(), t.getClNo(), 0,
								Integer.MAX_VALUE, titaVo);
						l2ClFac = slClFac2 == null ? null : slClFac2.getContent();
						for (ClFac c : l2ClFac) {

							// 撥款主檔
							Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(),
									c.getFacmNo(), c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
							if (slLoanBorMain != null) {
								for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
									// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
									if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2
											|| tLoanBorMain.getStatus() == 4 || tLoanBorMain.getStatus() == 6
											|| tLoanBorMain.getStatus() == 8) {
										isAllClose = false;
										break;
									}
								}
							}
						}
						// 全部結案

					}

				}
				int entryDate = parse.stringToInteger(result.get("EntryDate"));
				if (entryDate > 0) {
					entryDate = entryDate - 19110000;
				}
				int receiveDate = parse.stringToInteger(result.get("ReceiveDate"));
				if (receiveDate > 0) {
					receiveDate = receiveDate - 19110000;
				}
				int applDate = parse.stringToInteger(result.get("ApplDate"));
				if (applDate > 0) {
					applDate = applDate - 19110000;
				}


				occursList.putParam("OOEntryDate", entryDate);
				occursList.putParam("OOApplDate", applDate);
				occursList.putParam("OOFunCode", result.get("FunCode"));
				occursList.putParam("OOCustNo", result.get("CustNo"));
				occursList.putParam("OOCustName", tCustMain.getCustName());
				occursList.putParam("OOFacmNo", result.get("FacmNo"));
				occursList.putParam("OOCloseNo", result.get("CloseNo"));
				occursList.putParam("OOCloseReasonCode", result.get("CloseReasonCode"));
				occursList.putParam("OOCloseAmt", result.get("CloseAmt"));
				occursList.putParam("OOCollectWayCode", result.get("CollectWayCode"));
				occursList.putParam("OOReceiveDate", receiveDate);
				if (parse.stringToInteger(createDate) > 19110000) {
					occursList.putParam("OOCreateDate", parse.stringToInteger(createDate) - 19110000);
				} else {
					occursList.putParam("OOCreateDate", 0);
				}
				occursList.putParam("OORmk", result.get("Rmk"));
				occursList.putParam("OOTelNo1", result.get("TelNo1"));
				occursList.putParam("OODocNo", result.get("DocNo"));
				occursList.putParam("OOAgreeNo", result.get("AgreeNo"));
				occursList.putParam("OOClsNo", result.get("ClsNo"));
				occursList.putParam("OOCloseDate", wkCloseDate);
				if (isAllClose) {
					occursList.putParam("OOAllCloseFg", "Y");
				} else {
					occursList.putParam("OOAllCloseFg", "N");
				}
				occursList.putParam("OORepayFg", wkRepayFg);
				occursList.putParam("OOReceiveFg", result.get("ReceiveFg"));

				this.info("occursList L2077" + occursList);
				this.totaVo.addOccursList(occursList);
			}

			this.addList(this.totaVo);
			return this.sendList();

		} else {

			throw new LogicException(titaVo, "E0001", "查無資料");
		}
	}
}