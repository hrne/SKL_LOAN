package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * InsuYearMonth=9,5<br>
 * ClNo=9,12<br>
 * NowInsuNo=X,16<br>
 * NewFireInsuCovrg=9,14.2<br>
 * NewFireInsuPrem=9,14.2<br>
 * NewEthqInsuCovrg=9,14.2<br>
 * NewEthqInsuPrem=9,14.2<br>
 * NewInsuStartDate=9,7<br>
 * NewInsuEndDate=9,7<br>
 * TotalPrem=9,14.2<br>
 * AcDate=9,7<br>
 * NotiTempFg=X,1<br>
 * TitaTxtNo=9,7<br>
 * StatusCode=9,1<br>
 * OvduDate=9,7<br>
 * OvduNo=9,10<br>
 */

@Service("L4611")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4611 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4611.class);

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public AcReceivableCom acReceivableCom;

	private String iFunctionCode = "";
	private int clCode1 = 1;
	private int clCode2 = 0;
	private int clNo = 0;
	private int custNo = 0;
	private int facmNo = 0;
	private int insuYearMonth = 0;
	private String prevInsuNo = "";
	private String endoInsuNo = "";
	private BigDecimal totPrem = BigDecimal.ZERO;
	private int renewCode = 0;
	private int nullCode = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4611 ");
		this.totaVo.init(titaVo);

		iFunctionCode = titaVo.getParam("FunctionCode").trim();
		clCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		clCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		clNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		insuYearMonth = parse.stringToInteger(titaVo.getParam("InsuYearMonth")) + 191100;
		prevInsuNo = titaVo.getParam("PrevInsuNo");
		endoInsuNo = titaVo.getParam("EndoInsuNo");
		renewCode = parse.stringToInteger(titaVo.getParam("RenewCode"));

		if ("".equals(endoInsuNo)) {
			endoInsuNo = " ";
		}

		InsuRenew tInsuRenew = new InsuRenew();
//		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
		InsuRenewId tInsuRenewId = new InsuRenewId();

		tInsuRenewId.setClCode1(clCode1);
		tInsuRenewId.setClCode2(clCode2);
		tInsuRenewId.setClNo(clNo);
		tInsuRenewId.setPrevInsuNo(prevInsuNo);
		tInsuRenewId.setEndoInsuNo(endoInsuNo);

		if ("1".equals(iFunctionCode)) {

			tInsuRenew.setInsuRenewId(tInsuRenewId);
			tInsuRenew.setCustNo(custNo);
			tInsuRenew.setFacmNo(facmNo);
			tInsuRenew.setInsuYearMonth(insuYearMonth);
			tInsuRenew.setNowInsuNo(titaVo.getParam("NowInsuNo"));
			tInsuRenew.setOrigInsuNo(getOrigInsuNo(titaVo));
			tInsuRenew.setRenewCode(renewCode);
			tInsuRenew.setInsuCompany(titaVo.getParam("InsuCompany"));
			tInsuRenew.setInsuTypeCode(titaVo.getParam("InsuTypeCode"));
			tInsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuCovrg")));
			tInsuRenew.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem")));
			tInsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuCovrg")));
			tInsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
			tInsuRenew.setInsuStartDate(parse.stringToInteger(titaVo.getParam("NewInsuStartDate")));
			tInsuRenew.setInsuEndDate(parse.stringToInteger(titaVo.getParam("NewInsuEndDate")));
			tInsuRenew.setAcDate(parse.stringToInteger(titaVo.getParam("AcDate")));

			tInsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
			tInsuRenew.setTitaTxtNo("" + this.getTxBuffer().getTxCom().getRelTno());
			tInsuRenew.setNotiTempFg("N");
			tInsuRenew.setStatusCode(0);
			tInsuRenew.setOvduDate(0);
			tInsuRenew.setOvduNo(BigDecimal.ZERO);

			totPrem = parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem"))
					.add(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));

			tInsuRenew.setTotInsuPrem(totPrem);

			try {
				// 送出到DB
				insuRenewService.insert(tInsuRenew, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L4611 InsuRenew insert " + e.getErrorMsg());
			}

//			0-起帳 1-銷帳 2-起帳刪除
			resetAcReceivable(0, tInsuRenew, titaVo);

		} else if ("2".equals(iFunctionCode)) {
			tInsuRenew = insuRenewService.holdById(tInsuRenewId);
			if (tInsuRenew != null) {
				
				if (tInsuRenew.getAcDate() > 0) {
					throw new LogicException(titaVo, "E0007", "此筆已入帳，不可修改");
				} else if (tInsuRenew.getRenewCode() == 3) {
					throw new LogicException(titaVo, "E0007", "此筆已轉借支，不可修改");
				}

				tInsuRenew.setClCode1(clCode1);
				tInsuRenew.setClCode2(clCode2);
				tInsuRenew.setClNo(clNo);
				tInsuRenew.setCustNo(custNo);
				tInsuRenew.setFacmNo(facmNo);
				tInsuRenew.setInsuYearMonth(insuYearMonth);
				tInsuRenew.setNowInsuNo(titaVo.getParam("NowInsuNo"));
				tInsuRenew.setOrigInsuNo(getOrigInsuNo(titaVo));
				tInsuRenew.setRenewCode(renewCode);
				tInsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuCovrg")));
				tInsuRenew.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem")));
				tInsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuCovrg")));
				tInsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
				tInsuRenew.setInsuStartDate(parse.stringToInteger(titaVo.getParam("NewInsuStartDate")));
				tInsuRenew.setInsuEndDate(parse.stringToInteger(titaVo.getParam("NewInsuEndDate")));
				tInsuRenew.setAcDate(parse.stringToInteger(titaVo.getParam("AcDate")));
				tInsuRenew.setNotiTempFg(titaVo.getParam("NotiTempFg"));
				tInsuRenew.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
//				已入帳(AcDate>0)、轉借支不可變更狀態
				tInsuRenew.setStatusCode(parse.stringToInteger(titaVo.getParam("StatusCode")));

				tInsuRenew.setOvduDate(parse.stringToInteger(titaVo.getParam("OvduDate")));
				tInsuRenew.setOvduNo(parse.stringToBigDecimal(titaVo.getParam("OvduNo")));
				tInsuRenew.setInsuCompany(titaVo.getParam("InsuCompany"));
				tInsuRenew.setInsuTypeCode(titaVo.getParam("InsuTypeCode"));

				totPrem = parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem"))
						.add(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));

				tInsuRenew.setTotInsuPrem(totPrem);

				try {
					// 送出到DB
					insuRenewService.update(tInsuRenew);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L4611 InsuRenew update " + e.getErrorMsg());
				}

//				0-起帳 1-銷帳 2-起帳刪除
				resetAcReceivable(2, tInsuRenew, titaVo);

//				若結案則不必再起帳
				if (parse.stringToInteger(titaVo.getParam("StatusCode")) != 4) {
					resetAcReceivable(0, tInsuRenew, titaVo);
				}
			}
		} else if ("4".equals(iFunctionCode)) {
			// 抓出要刪除的資料
			tInsuRenew = insuRenewService.holdById(tInsuRenewId);
			if (tInsuRenew != null) {
				if (tInsuRenew.getAcDate() > 0) {
					throw new LogicException(titaVo, "E0015", "該筆已入帳");
				}

				try {
					insuRenewService.delete(tInsuRenew);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}

//				0-起帳 1-銷帳 2-起帳刪除
				resetAcReceivable(2, tInsuRenew, titaVo);

			}
		}
//		L4964連動L4611自保
//	  	a.該筆狀態改為自保
//		b.改AcReceivable檔該記號銷帳(若該筆未完成下期續保)
		else if ("6".equals(iFunctionCode)) {
//			1.新保待續保或已完成續保待產生下次續保 ... 新增一筆狀態為1.自保
//			2.若該筆尚未續保 ... 變更狀態1.自保

			tInsuRenew = insuRenewService.holdById(tInsuRenewId);

//			新保單無續保 或 已續保過，未完成下期續保
			if (tInsuRenew == null || !"".equals(tInsuRenew.getNowInsuNo())) {
				InsuRenew t2InsuRenew = new InsuRenew();

				t2InsuRenew.setInsuRenewId(tInsuRenewId);
				t2InsuRenew.setCustNo(custNo);
				t2InsuRenew.setFacmNo(facmNo);
				t2InsuRenew.setInsuYearMonth(insuYearMonth);
				t2InsuRenew.setNowInsuNo(titaVo.getParam("NowInsuNo"));
				t2InsuRenew.setOrigInsuNo(getOrigInsuNo(titaVo));
				t2InsuRenew.setRenewCode(1);
				t2InsuRenew.setInsuCompany(titaVo.getParam("InsuCompany"));
				t2InsuRenew.setInsuTypeCode(titaVo.getParam("InsuTypeCode"));
				t2InsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuCovrg")));
				t2InsuRenew.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem")));
				t2InsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuCovrg")));
				t2InsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
				t2InsuRenew.setInsuStartDate(parse.stringToInteger(titaVo.getParam("NewInsuStartDate")));
				t2InsuRenew.setInsuEndDate(parse.stringToInteger(titaVo.getParam("NewInsuEndDate")));
				t2InsuRenew.setAcDate(parse.stringToInteger(titaVo.getParam("AcDate")));

				t2InsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
				t2InsuRenew.setTitaTxtNo("" + this.getTxBuffer().getTxCom().getRelTno());
				t2InsuRenew.setNotiTempFg("N");
				t2InsuRenew.setStatusCode(0);
				t2InsuRenew.setOvduDate(0);
				t2InsuRenew.setOvduNo(BigDecimal.ZERO);

				totPrem = parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem"))
						.add(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));

				t2InsuRenew.setTotInsuPrem(totPrem);

				try {
					// 送出到DB
					insuRenewService.insert(t2InsuRenew, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L4611 InsuRenew insert " + e.getErrorMsg());
				}
			} else {
				tInsuRenew.setRenewCode(1);
				tInsuRenew.setNowInsuNo(titaVo.getParam("NowInsuNo"));
				tInsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuCovrg")));
				tInsuRenew.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem")));
				tInsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuCovrg")));
				tInsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
				tInsuRenew.setInsuStartDate(parse.stringToInteger(titaVo.getParam("NewInsuStartDate")));
				tInsuRenew.setInsuEndDate(parse.stringToInteger(titaVo.getParam("NewInsuEndDate")));
				tInsuRenew.setInsuCompany(titaVo.getParam("InsuCompany"));
				tInsuRenew.setInsuTypeCode(titaVo.getParam("InsuTypeCode"));

				try {
					insuRenewService.update(tInsuRenew, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}

//					0-起帳 1-銷帳 2-起帳刪除
				resetAcReceivable(2, tInsuRenew, titaVo);
			}
		} else {
			throw new LogicException(titaVo, "E0010", "FunctionCode 錯誤!!");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void resetAcReceivable(int flag, InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();

		if (!"Y".equals(tInsuRenew.getNotiTempFg())) {
			this.info("該筆未入通知，NotiTempFg : " + tInsuRenew.getNotiTempFg());
			return;
		}

		if (tInsuRenew.getStatusCode() == 0) {
			AcReceivable acReceivable = new AcReceivable();

			acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			acReceivable.setAcctCode("TMI"); // 業務科目
			acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
			acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
			acReceivable.setFacmNo(tInsuRenew.getFacmNo());
			acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
			acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
			acReceivableList.add(acReceivable);

			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(flag, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
		} else {
			throw new LogicException(titaVo, "E0015", "該筆狀態不為正常，Status : " + tInsuRenew.getStatusCode());
		}
	}

	private String getOrigInsuNo(TitaVo titaVo) {
		String result = "";

		InsuOrignal tInsuOrignal = new InsuOrignal();
		InsuOrignalId tInsuOrignalId = new InsuOrignalId();

		tInsuOrignalId.setClCode1(clCode1);
		tInsuOrignalId.setClCode2(clCode2);
		tInsuOrignalId.setClNo(clNo);
		tInsuOrignalId.setOrigInsuNo(prevInsuNo);
		tInsuOrignalId.setEndoInsuNo(" ");

		tInsuOrignal = insuOrignalService.findById(tInsuOrignalId, titaVo);

		if (tInsuOrignal != null) {
			result = tInsuOrignal.getOrigInsuNo();
		}

		return result;
	}
}