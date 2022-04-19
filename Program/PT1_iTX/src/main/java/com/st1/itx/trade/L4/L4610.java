package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * PrevInsuNo=X,16<br>
 * NowInsuNo=X,16<br>
 * InsuCompany=9,2<br>
 * InsuTypeCode=X,2<br>
 * FireInsuCovrgI=9,14.2<br>
 * EthqInsuCovrgI=9,14.2<br>
 * FireInsuPrem=9,14.2<br>
 * EthqInsuPrem=9,14.2<br>
 * InsuStartDateI=9,7<br>
 * InsuEndDateI=9,7<br>
 */

@Service("L4610")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4610 extends TradeBuffer {

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

	private Boolean typeInFlag = true;
	private BigDecimal totPrem = BigDecimal.ZERO;

	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4610 ");
		this.totaVo.init(titaVo);

		this.info("L4610 - A Test");

		String iFunctionCode = titaVo.getParam("FunctionCode").trim();
		int clCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int clCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		String origInsuNo = titaVo.getParam("PrevInsuNo");
		String endoInsuNo = titaVo.getParam("EndoInsuNo");
		// 新保:1 、 自保:2 、 維護:3

		if ("".equals(endoInsuNo)) {
			endoInsuNo = " ";
			typeInFlag = false;
		}

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		InsuOrignal tInsuOrignal = new InsuOrignal();

		InsuOrignalId tInsuOrignalId = new InsuOrignalId();
		tInsuOrignalId.setClCode1(clCode1);
		tInsuOrignalId.setClCode2(clCode2);
		tInsuOrignalId.setClNo(clNo);
		tInsuOrignalId.setOrigInsuNo(origInsuNo);
		tInsuOrignalId.setEndoInsuNo(endoInsuNo);

		if (isEloan) {
			InsuOrignal insuOrignal = insuOrignalService.findById(tInsuOrignalId, titaVo);
			if (insuOrignal != null) {
				iFunctionCode = "2";
			}
		}

		if (iFunctionCode.equals("1")) {

//			QC360.若需新增一筆批單號碼，於此交易新增
//			1.檢核批單號碼是否存在，不存在提錯誤
//			2.若存在若在renew則建在renew，orig則建在orig

//			已輸入批單號碼
			if (typeInFlag) {
				InsuRenew tOldInsuRenew = insuRenewService.findL4600AFirst(clCode1, clCode2, clNo, origInsuNo, titaVo);
				InsuRenew tNewInsuRenew = new InsuRenew();
				InsuRenewId tInsuRenewId = new InsuRenewId();
				if (tOldInsuRenew != null && !isEloan) {
					tInsuRenewId.setClCode1(clCode1);
					tInsuRenewId.setClCode2(clCode2);
					tInsuRenewId.setClNo(clNo);
					tInsuRenewId.setPrevInsuNo(tOldInsuRenew.getPrevInsuNo());
					tInsuRenewId.setEndoInsuNo(endoInsuNo);
					tNewInsuRenew.setInsuRenewId(tInsuRenewId);
					tNewInsuRenew.setCustNo(tOldInsuRenew.getCustNo());
					tNewInsuRenew.setFacmNo(tOldInsuRenew.getFacmNo());
					tNewInsuRenew.setInsuYearMonth(tOldInsuRenew.getInsuYearMonth());
					tNewInsuRenew.setNowInsuNo(tOldInsuRenew.getNowInsuNo());
					tNewInsuRenew.setOrigInsuNo(tOldInsuRenew.getOrigInsuNo());
					tNewInsuRenew.setRenewCode(1); // 1.自保
					tNewInsuRenew.setInsuCompany(tOldInsuRenew.getInsuCompany());
					tNewInsuRenew.setInsuTypeCode(tOldInsuRenew.getInsuTypeCode());
					tNewInsuRenew.setRepayCode(tOldInsuRenew.getRepayCode());
					tNewInsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("FireInsuCovrg")));
					tNewInsuRenew.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("FireInsuPrem")));
					tNewInsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("EthqInsuCovrg")));
					tNewInsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("EthqInsuPrem")));
					tNewInsuRenew.setInsuStartDate(parse.stringToInteger(titaVo.getParam("InsuStartDate")));
					tNewInsuRenew.setInsuEndDate(parse.stringToInteger(titaVo.getParam("InsuEndDate")));
					tNewInsuRenew.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
					tNewInsuRenew.setRemark(titaVo.getParam("Remark").trim());

					if (parse.stringToInteger(titaVo.getParam("InsuEndDate")) != tOldInsuRenew.getInsuEndDate()) {
						throw new LogicException(titaVo, "E0007", "L4610 登打批單號碼時，保險迄日需相同");
					}

					tNewInsuRenew.setAcDate(tOldInsuRenew.getAcDate());
					tNewInsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
					tNewInsuRenew.setTitaTxtNo("" + this.getTxBuffer().getTxCom().getRelTno());
					tNewInsuRenew.setNotiTempFg("");
					tNewInsuRenew.setStatusCode(0);
					tNewInsuRenew.setOvduDate(0);
					tNewInsuRenew.setOvduNo(BigDecimal.ZERO);

					totPrem = parse.stringToBigDecimal(titaVo.getParam("FireInsuPrem")).add(parse.stringToBigDecimal(titaVo.getParam("EthqInsuPrem")));

					tNewInsuRenew.setTotInsuPrem(totPrem);

					try {
						// 送出到DB
						insuRenewService.insert(tNewInsuRenew, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "L4611 InsuRenew insert " + e.getErrorMsg());
					}
				} else {
					InsuOrignalId tOldInsuOrignalId = new InsuOrignalId();
					InsuOrignal tOldInsuOrignal = new InsuOrignal();
					InsuOrignal tNewInsuOrignal = new InsuOrignal();

					tOldInsuOrignalId.setClCode1(clCode1);
					tOldInsuOrignalId.setClCode2(clCode2);
					tOldInsuOrignalId.setClNo(clNo);
					tOldInsuOrignalId.setOrigInsuNo(origInsuNo);
					tOldInsuOrignalId.setEndoInsuNo(" ");

					tOldInsuOrignal = insuOrignalService.findById(tOldInsuOrignalId, titaVo);
					if (tOldInsuOrignal == null) {
						throw new LogicException(titaVo, "E0005", "L4610 登打批單號碼時，需先有此保單號碼");
					}

					tNewInsuOrignal.setInsuOrignalId(tInsuOrignalId);

					tNewInsuOrignal.setInsuCompany(titaVo.getParam("InsuCompany").trim());
					tNewInsuOrignal.setInsuTypeCode(titaVo.getParam("InsuTypeCode").trim());
					tNewInsuOrignal.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("FireInsuCovrg").trim()));
					tNewInsuOrignal.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("EthqInsuCovrg").trim()));
					tNewInsuOrignal.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("FireInsuPrem").trim()));
					tNewInsuOrignal.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("EthqInsuPrem").trim()));
					tNewInsuOrignal.setInsuStartDate(parse.stringToInteger(titaVo.getParam("InsuStartDate").trim()));
					tNewInsuOrignal.setInsuEndDate(parse.stringToInteger(titaVo.getParam("InsuEndDate").trim()));
					tNewInsuOrignal.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
					tNewInsuOrignal.setRemark(titaVo.getParam("Remark").trim());
					if (parse.stringToInteger(titaVo.getParam("InsuEndDate")) != tOldInsuOrignal.getInsuEndDate()) {
						throw new LogicException(titaVo, "E0007", "L4610 登打批單號碼時，保險迄日需相同");
					}

					try {
						insuOrignalService.insert(tNewInsuOrignal, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "L4610 InsuOrignal insert " + e.getErrorMsg());
					}
				}
//			未輸入批單號碼
			} else {
				tInsuOrignal.setInsuOrignalId(tInsuOrignalId);

				tInsuOrignal.setInsuCompany(titaVo.getParam("InsuCompany").trim());
				tInsuOrignal.setInsuTypeCode(titaVo.getParam("InsuTypeCode").trim());
				tInsuOrignal.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("FireInsuCovrg").trim()));
				tInsuOrignal.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("EthqInsuCovrg").trim()));
				tInsuOrignal.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("FireInsuPrem").trim()));
				tInsuOrignal.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("EthqInsuPrem").trim()));
				tInsuOrignal.setInsuStartDate(parse.stringToInteger(titaVo.getParam("InsuStartDate").trim()));
				tInsuOrignal.setInsuEndDate(parse.stringToInteger(titaVo.getParam("InsuEndDate").trim()));
				tInsuOrignal.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
				tInsuOrignal.setRemark(titaVo.getParam("Remark").trim());
				try {
					insuOrignalService.insert(tInsuOrignal, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L4610 InsuOrignal insert " + e.getErrorMsg());
				}
			}
		} else if (iFunctionCode.equals("2")) {
			InsuOrignal editInsuOrignal = insuOrignalService.holdById(tInsuOrignalId, titaVo);

			if (editInsuOrignal == null) {
				throw new LogicException(titaVo, "E0003", "火險初保檔 ");
			}
			editInsuOrignal.setInsuCompany(titaVo.getParam("InsuCompany").trim());
			editInsuOrignal.setInsuTypeCode(titaVo.getParam("InsuTypeCode").trim());
			editInsuOrignal.setFireInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("FireInsuCovrg").trim()));
			editInsuOrignal.setEthqInsuCovrg(parse.stringToBigDecimal(titaVo.getParam("EthqInsuCovrg").trim()));
			editInsuOrignal.setFireInsuPrem(parse.stringToBigDecimal(titaVo.getParam("FireInsuPrem").trim()));
			editInsuOrignal.setEthqInsuPrem(parse.stringToBigDecimal(titaVo.getParam("EthqInsuPrem").trim()));
			editInsuOrignal.setInsuStartDate(parse.stringToInteger(titaVo.getParam("InsuStartDate").trim()));
			editInsuOrignal.setInsuEndDate(parse.stringToInteger(titaVo.getParam("InsuEndDate").trim()));
			editInsuOrignal.setCommericalFlag(titaVo.getParam("CommericalFlag").trim());
			editInsuOrignal.setRemark(titaVo.getParam("Remark").trim());
			try {
				// 送出到DB
				insuOrignalService.update(editInsuOrignal, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2)
					throw new LogicException(titaVo, "E0007", "L4610 InsuOrignal update " + e.getErrorMsg());
			}

		} else if (iFunctionCode.equals("4")) {
			// 抓出要刪除的資料

			// 刪除須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			InsuOrignal deleInsuOrignal = insuOrignalService.holdById(tInsuOrignalId, titaVo);
			if (deleInsuOrignal == null) {
				throw new LogicException(titaVo, "E0004", "火險初保檔 ");
			}
			try {
				insuOrignalService.delete(deleInsuOrignal, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		} else {
			throw new LogicException(titaVo, "E0010", "FunctionCode 錯誤!!");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}