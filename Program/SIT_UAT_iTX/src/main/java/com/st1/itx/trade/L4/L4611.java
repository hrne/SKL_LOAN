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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.SendRsp;
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

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public SendRsp sendRsp;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public FacMainService facMainService;

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
	private int noticeYearMonth = 0; // 已執行通知作業年月

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

		BigDecimal TotalPrem = new BigDecimal("0");

		// 已執行通知作業年月
		InsuRenew t1InsuRenew = insuRenewService.findNotiTempFgFirst("Y", titaVo);
		if (t1InsuRenew != null) {
			noticeYearMonth = t1InsuRenew.getInsuYearMonth();
		}
		if ("".equals(endoInsuNo)) {
			endoInsuNo = " ";
		}
		this.info("noticeYearMonth=" + noticeYearMonth);
		// 額度
		FacMain tFacMain = facMainService.findById(new FacMainId(custNo, facmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0015", "額度檔不存在"); // 檢查錯誤
		}

		InsuRenewId tInsuRenewId = new InsuRenewId();
		InsuRenew tInsuRenew = new InsuRenew();
		tInsuRenewId.setClCode1(clCode1);
		tInsuRenewId.setClCode2(clCode2);
		tInsuRenewId.setClNo(clNo);
		tInsuRenewId.setPrevInsuNo(prevInsuNo);
		tInsuRenewId.setEndoInsuNo(endoInsuNo);
		this.info("tInsuRenewId = " + tInsuRenewId);
		tInsuRenew = insuRenewService.holdById(tInsuRenewId, titaVo);
		if ("1".equals(iFunctionCode)) {
			if (tInsuRenew != null) {
				throw new LogicException(titaVo, "E0002", "InsuRenew"); // 新增資料已存在
			}
		} else {
			if (tInsuRenew == null) {
				throw new LogicException(titaVo, "E0006", "InsuRenew"); // 鎖定資料時，發生錯誤
			}
		}
		InsuRenew oldInsuRenew = tInsuRenew;

		switch (iFunctionCode) {
		case "1": // 新增
			tInsuRenew = new InsuRenew();
			tInsuRenew.setInsuRenewId(tInsuRenewId);
			tInsuRenew.setClCode1(clCode1);
			tInsuRenew.setClCode2(clCode2);
			tInsuRenew.setClNo(clNo);
			tInsuRenew.setPrevInsuNo(prevInsuNo);
			tInsuRenew.setEndoInsuNo(endoInsuNo);
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

			tInsuRenew.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
			tInsuRenew.setRemark(titaVo.getParam("Remark").trim());

			tInsuRenew.setAcDate(0);
			tInsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
			tInsuRenew.setTitaTxtNo("" + this.getTxBuffer().getTxCom().getRelTno());
			if (tInsuRenew.getRenewCode() == 2) {
				if (insuYearMonth <= noticeYearMonth) {
					tInsuRenew.setNotiTempFg("N"); // N:未入(通知作業後新增)
				} else {
					tInsuRenew.setNotiTempFg(""); // null:待通知
				}
			}
			tInsuRenew.setStatusCode(0);
			tInsuRenew.setOvduDate(0);
			tInsuRenew.setOvduNo(BigDecimal.ZERO);
			totPrem = parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem"))
					.add(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
			tInsuRenew.setTotInsuPrem(totPrem);
			tInsuRenew.setRepayCode(tFacMain.getRepayCode());
			try {
				insuRenewService.insert(tInsuRenew, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L4611 InsuRenew insert " + e.getErrorMsg());
			}

			resetAcReceivable(0, tInsuRenew, titaVo); // 0-起帳
			break;

		case "2": // 修改

			TotalPrem = oldInsuRenew.getFireInsuPrem().add(oldInsuRenew.getEthqInsuPrem());
			totPrem = parse.stringToBigDecimal(titaVo.getParam("TotalPrem"));
			// 保險狀態不同、 總保費不同
			if (oldInsuRenew.getRenewCode() != parse.stringToInteger(titaVo.getParam("RenewCode"))
					|| TotalPrem.compareTo(totPrem) != 0) {
				if (tInsuRenew.getAcDate() > 0) {
					throw new LogicException(titaVo, "E0015", "此筆已入帳，不可修改"); // 檢查錯誤
				}
				if (tInsuRenew.getStatusCode() == 1) {
					throw new LogicException(titaVo, "E0015", "此筆已轉借支，不可修改"); // 檢查錯誤
				}
				if (tInsuRenew.getStatusCode() == 2) {
					throw new LogicException(titaVo, "E0015", "此筆已轉催收，不可修改"); // 檢查錯誤
				}
			}

			resetAcReceivable(2, tInsuRenew, titaVo); // 2-起帳刪除

			custNo = parse.stringToInteger(titaVo.getParam("NewCustNo"));
			facmNo = parse.stringToInteger(titaVo.getParam("NewFacmNo"));

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
			tInsuRenew.setNotiTempFg(titaVo.getParam("NotiTempFg"));
			tInsuRenew.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
			tInsuRenew.setStatusCode(parse.stringToInteger(titaVo.getParam("StatusCode")));
			tInsuRenew.setInsuCompany(titaVo.getParam("InsuCompany"));
			tInsuRenew.setInsuTypeCode(titaVo.getParam("InsuTypeCode"));
			tInsuRenew.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
			tInsuRenew.setRemark(titaVo.getParam("Remark").trim());
			totPrem = parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem"))
					.add(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
			tInsuRenew.setTotInsuPrem(totPrem);
			if (tInsuRenew.getRenewCode() == 2) {
				if (insuYearMonth <= noticeYearMonth) {
					if ("Y".equals(oldInsuRenew.getNotiTempFg())) {
						tInsuRenew.setNotiTempFg("Y"); // 已通知
					} else {
						tInsuRenew.setNotiTempFg("N"); // N:未入(通知作業後修改)
					}
				} else {
					tInsuRenew.setNotiTempFg(""); // null:待通知
				}
			}

			try {
				insuRenewService.update(tInsuRenew, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L4611 InsuRenew update " + e.getErrorMsg());
			}

			resetAcReceivable(0, tInsuRenew, titaVo); // 0-起帳刪除
			break;

		case "4": // 刪除
			if (tInsuRenew.getAcDate() > 0) {
				throw new LogicException(titaVo, "E0015", "此筆已入帳，不可刪除"); // 檢查錯誤
			}
			if (tInsuRenew.getStatusCode() == 1) {
				throw new LogicException(titaVo, "E0015", "此筆已轉借支，不可刪除"); // 檢查錯誤
			}
			if (tInsuRenew.getStatusCode() == 2) {
				throw new LogicException(titaVo, "E0015", "此筆已轉催收，不可刪除"); // 檢查錯誤
			}

			// 刪除須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			try {
				insuRenewService.delete(tInsuRenew, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			resetAcReceivable(2, tInsuRenew, titaVo); // 2-起帳刪除
			break;

		case "6": // 自保
			if (tInsuRenew.getRenewCode() == 2) {
				if (tInsuRenew.getAcDate() > 0) {
					throw new LogicException(titaVo, "E0015", "此筆已入帳，不可自保"); // 檢查錯誤
				}
				if (tInsuRenew.getStatusCode() == 1) {
					throw new LogicException(titaVo, "E0015", "此筆已轉借支，不可自保"); // 檢查錯誤
				}
				if (tInsuRenew.getStatusCode() == 2) {
					throw new LogicException(titaVo, "E0015", "此筆已轉催收，不可自保"); // 檢查錯誤
				}
			}
			resetAcReceivable(2, tInsuRenew, titaVo); // 2-起帳刪除
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
			tInsuRenew.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
			tInsuRenew.setRemark(titaVo.getParam("Remark").trim());
			totPrem = parse.stringToBigDecimal(titaVo.getParam("NewFireInsuPrem"))
					.add(parse.stringToBigDecimal(titaVo.getParam("NewEthqInsuPrem")));
			tInsuRenew.setTotInsuPrem(totPrem);
			try {
				insuRenewService.update(tInsuRenew, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void resetAcReceivable(int flag, InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("resetAcReceivable.." + flag + ", " + noticeYearMonth + ", " + tInsuRenew.toString());
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
		if (noticeYearMonth == 0) {
			return;
		}

		if (tInsuRenew.getRenewCode() != 2) {
			this.info("skip AcReceivable RenewCode" + tInsuRenew.getRenewCode());
			return;
		}

		if (tInsuRenew.getStatusCode() > 0) {
			this.info("skip AcReceivable StatusCode= " + tInsuRenew.getStatusCode());
			return;
		}
		// 尚未通知
		if ("".equals(tInsuRenew.getNotiTempFg())) {
			this.info("skip AcReceivable NotiTempFg=" + tInsuRenew.getNotiTempFg());
			return;
		}

		AcReceivable acReceivable = new AcReceivable();
		acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
		acReceivable.setAcctCode("TMI"); // 業務科目
		acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
		acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
		acReceivable.setFacmNo(tInsuRenew.getFacmNo());
		acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
		acReceivable.setOpenAcDate(tInsuRenew.getInsuYearMonth() * 100 + 01);
		acReceivableList.add(acReceivable);
		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(flag, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
	}

	private String getOrigInsuNo(TitaVo titaVo) throws LogicException {
		String result = "";
		InsuOrignal tInsuOrignal = insuOrignalService
				.findById(new InsuOrignalId(clCode1, clCode2, clNo, prevInsuNo, " "), titaVo);
		if (tInsuOrignal == null) {
			InsuRenew t2InsuRenew = insuRenewService.findL4600AFirst(clCode1, clCode2, clNo, prevInsuNo, titaVo);
			if (t2InsuRenew == null) {
				throw new LogicException(titaVo, "E0015", "無原保單資料，不可修改"); // 檢查錯誤
			}
			result = t2InsuRenew.getOrigInsuNo();
		} else {
			result = tInsuOrignal.getOrigInsuNo();
		}

		return result;
	}
}