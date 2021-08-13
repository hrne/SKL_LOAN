package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ENTRYDATE=9,7<br>
 * END=X,1<br>
 */

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
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		int wkCloseDate = 0;
		String wkRepayFg = "";
		// new ArrayList
		List<FacClose> lFacClose = new ArrayList<FacClose>();
		Slice<FacClose> slFacClose = null;
		// 處理邏輯 三擇一輸入
		if (iCustNo != 0) {
			slFacClose = sFacCloseService.findCustNo(iCustNo, this.index, this.limit, titaVo);
			lFacClose = slFacClose == null ? null : slFacClose.getContent();
		} else if (iTranDate > 0) {
			slFacClose = sFacCloseService.findEntryDate(iTranDate + 19110000, this.index, this.limit, titaVo);
			lFacClose = slFacClose == null ? null : slFacClose.getContent();
		} else if (iApplDate > 0) {
			slFacClose = sFacCloseService.findApplDateEq(iApplDate + 19110000, iApplDate + 19110000, this.index,
					this.limit, titaVo);
			lFacClose = slFacClose == null ? null : slFacClose.getContent();
		}
		// 查無資料拋錯
		if (lFacClose == null) {
			throw new LogicException(titaVo, "E2003", "查無清償作業檔資料"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slFacClose != null && slFacClose.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (FacClose tmpFacClose : lFacClose) {
			// new occurs
			OccursList occursList = new OccursList();
			// 宣告
			Timestamp ts = tmpFacClose.getCreateDate();
			String createDate = "";
			DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			createDate = sdf.format(ts);
			this.info("createDate = " + createDate);
			// new table
			CustMain tCustMain = new CustMain();
			int custno = tmpFacClose.getCustNo();
			tCustMain = sCustMainService.custNoFirst(custno, custno);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}

			wkCloseDate = tmpFacClose.getCloseDate();

			// 查詢各項費用
			baTxCom.setTxBuffer(txBuffer);
			baTxCom.settingUnPaid(tmpFacClose.getEntryDate(), tmpFacClose.getCustNo(), tmpFacClose.getFacmNo(), 000, 0,
					BigDecimal.ZERO, titaVo);
			this.info("CloseBreach = "+ baTxCom.getShortCloseBreach());
			this.info("FunCode = "+ tmpFacClose.getFunCode());
			if ("2".equals(tmpFacClose.getFunCode()) && baTxCom.getShortCloseBreach().compareTo(BigDecimal.ZERO) == 0) {
				this.info("wkRepayFg = \"Y\"");
				wkRepayFg = "Y";
			}

			// 全部結案

			// 擔保品與額度關聯檔
			Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(custno, tmpFacClose.getFacmNo(),
					tmpFacClose.getFacmNo(), 0, Integer.MAX_VALUE, titaVo);
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
						Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(), c.getFacmNo(),
								c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
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
			occursList.putParam("OOTranDate", tmpFacClose.getEntryDate());
			occursList.putParam("OOFunCode", tmpFacClose.getFunCode());
			occursList.putParam("OOCustNo", tmpFacClose.getCustNo());
			occursList.putParam("OOCustName", tCustMain.getCustName());
			occursList.putParam("OOFacmNo", tmpFacClose.getFacmNo());
			occursList.putParam("OOCloseNo", tmpFacClose.getCloseNo());
			occursList.putParam("OOCloseReasonCode", tmpFacClose.getCloseReasonCode());
			occursList.putParam("OOCloseAmt", tmpFacClose.getCloseAmt());
			occursList.putParam("OOCollectWayCode", tmpFacClose.getCollectWayCode());
			occursList.putParam("OOReceiveDate", tmpFacClose.getReceiveDate());
			if (parse.stringToInteger(createDate) > 19110000) {
				occursList.putParam("OOCreateDate", parse.stringToInteger(createDate) - 19110000);
			} else {
				occursList.putParam("OOCreateDate", 0);
			}
			occursList.putParam("OORmk", tmpFacClose.getRmk());
			occursList.putParam("OOTelNo1", tmpFacClose.getTelNo1());
			occursList.putParam("OODocNo", tmpFacClose.getDocNo());
			occursList.putParam("OOAgreeNo", tmpFacClose.getAgreeNo());
			occursList.putParam("OOClsNo", tmpFacClose.getClsNo());
			occursList.putParam("OOCloseDate", wkCloseDate);
			if (isAllClose) {
				occursList.putParam("OOAllCloseFg", "Y");
			} else {
				occursList.putParam("OOAllCloseFg", "N");
			}
			occursList.putParam("OORepayFg", wkRepayFg);

			this.info("occursList L2077" + occursList);
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}