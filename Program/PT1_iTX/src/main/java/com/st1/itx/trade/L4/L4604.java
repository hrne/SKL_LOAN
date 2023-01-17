package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * InsuEndMonth=9,5<br>
 */

@Service("L4604")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4604 extends TradeBuffer {

	@Autowired
	public InsuRenewService insuRenewService;
	@Autowired
	public SystemParasService systemParasService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	AcDetailCom acDetailCom;

	private int iInsuEndMonth = 0;
	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4604 ");
		this.totaVo.init(titaVo);
		acReceivableCom.setTxBuffer(this.getTxBuffer());

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

		String slipNote = titaVo.getParam("InsuEndMonth").substring(0, 3) + "年"
				+ titaVo.getParam("InsuEndMonth").substring(3, 5) + "月已繳火險保費轉應付費用";
// 與新光產險對帳後，列應付費用
// 一、正常繳款(放款系統出帳)
//  　　借:20232010 暫收及待結轉帳項-火險保費
//　　 貸:20210391 應付費用-待匯
// 二、墊付火險費(核心系統出帳)
// 　 　借:10522106 暫付及待結轉帳項-火險保費
//  　　貸:20210391 應付費用-待匯

		// 已繳
		Slice<InsuRenew> sInsuRenew = insuRenewService.findL4604A(iInsuEndMonth, 2, 1, 99999999,  0, Integer.MAX_VALUE,titaVo);
		if (sInsuRenew != null) {
			BigDecimal txAmt = BigDecimal.ZERO;
			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {
				if (tInsuRenew.getStatusCode() == 0) {
					txAmt = txAmt.add(tInsuRenew.getTotInsuPrem());
				}
			}
			// 帳務處理
			if (this.txBuffer.getTxCom().isBookAcYes()) {
				List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("TMI"); // 暫收及待結轉帳項-火險保費
				acDetail.setTxAmt(txAmt);
				acDetail.setSlipNote(slipNote);
				lAcDetail.add(acDetail);
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode("ERT"); // 應付費用-待匯
				acDetail.setTxAmt(txAmt);
				acDetail.setSlipNote(slipNote);
				lAcDetail.add(acDetail);
				// 產生會計分錄
				this.txBuffer.addAllAcDetailList(lAcDetail);
				acDetailCom.setTxBuffer(this.txBuffer);
				acDetailCom.run(titaVo);
			}
		}

		// 未繳
		sInsuRenew = insuRenewService.findL4604A(iInsuEndMonth, 2, 0, 0, 0, Integer.MAX_VALUE,titaVo);
		int status = 0;
		if (titaVo.isHcodeNormal()) {
			status = 0;
		} else {
			status = 1;
		}
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		if (sInsuRenew != null) {
			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {
				if (tInsuRenew.getStatusCode() == status) {
					lInsuRenew.add(tInsuRenew);
				}
			}
		}

		if (lInsuRenew.size() > 0) {
//			一般
			if (titaVo.isHcodeNormal()) {
				updateInsuRenew(lInsuRenew, 1); // StatusCode = 1.借支
			}
//			訂正
			if (titaVo.isHcodeErase()) {
				updateInsuRenew(lInsuRenew, 0); // StatusCode = 0.正常
			}
//			1.銷帳 -> 未收
			closeAcReceivable(lInsuRenew, titaVo);
//			2.起帳 -> 暫付
			openAcReceivable(lInsuRenew, titaVo);
//			產出交易後報表
			prodOutPut(lInsuRenew, titaVo);
		}
		totaVo.putParam("OCnt", cnt);

		SystemParas tSystemParas = systemParasService.holdById("LN", titaVo);
		if (tSystemParas != null) {
			if (titaVo.isHcodeNormal()) {
				titaVo.putParam("InsuEndMonthOld", tSystemParas.getInsuSettleDate());
				tSystemParas.setInsuSettleDate(titaVo.getEntDyI());
			} else {
				tSystemParas.setInsuSettleDate(parse.stringToInteger(titaVo.getParam("InsuEndMonthOld")));
			}
			try {
				systemParasService.update(tSystemParas);
			} catch (DBException e) {
				throw new LogicException("E0007", "tSystemParas update error");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void closeAcReceivable(List<InsuRenew> lInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("CloseAcReceivable Start...");
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
		for (InsuRenew tInsuRenew : lInsuRenew) {
			// 正常、未收
			AcReceivable acReceivable = new AcReceivable();
			acReceivable.setReceivableFlag(3); // 火險保費 -> 3-未收費用
			acReceivable.setAcctCode("TMI"); // 火險保費
			acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
			acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
			acReceivable.setFacmNo(tInsuRenew.getFacmNo());
			acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
			acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
			acReceivableList.add(acReceivable);
		}
		acReceivableCom.mnt(1, acReceivableList, titaVo); // 0-起帳 1-銷帳2-刪除
	}

	private void openAcReceivable(List<InsuRenew> lInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("openAcReceivable Start...");
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
		for (InsuRenew tInsuRenew : lInsuRenew) {
			// 正常、未收
			AcReceivable acReceivable = new AcReceivable();
			acReceivable.setReceivableFlag(2); // 2-核心出帳
			acReceivable.setAcctCode("F09"); // 業務科目
			acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
			acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
			acReceivable.setFacmNo(tInsuRenew.getFacmNo());
			acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
			acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
			TempVo tTempVo = new TempVo();
			tTempVo.clear();
			tTempVo.putParam("InsuYearMonth", tInsuRenew.getInsuYearMonth());
			acReceivable.setJsonFields(tTempVo.getJsonString());
			acReceivableList.add(acReceivable);
		}
		acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳
	}

	private void updateInsuRenew(List<InsuRenew> lInsuRenew, int flag) throws LogicException {
		this.info("openAcReceivable Start Flag = " + flag);
		for (InsuRenew tInsuRenew : lInsuRenew) {
			if (tInsuRenew.getStatusCode() == flag) {
				if (flag == 0) {
					throw new LogicException("E0005", "該筆狀態為正常");
				} else {
					throw new LogicException("E0005", "該筆狀態不為正常");
				}
			}
			// 正常、未收
			tInsuRenew = insuRenewService.holdById(tInsuRenew);
			tInsuRenew.setStatusCode(flag);
			try {
				insuRenewService.update(tInsuRenew);
			} catch (DBException e) {
				throw new LogicException("E0007", "InsuRenew update error");
			}
		}
	}

	private void prodOutPut(List<InsuRenew> lInsuRenew, TitaVo titaVo) {
		for (InsuRenew tInsuRenew : lInsuRenew) {
			cnt = cnt + 1;

			int month = tInsuRenew.getInsuYearMonth();

			if (month > 191100) {
				month = month - 191100;
			}
			OccursList occursList = new OccursList();

			occursList.putParam("OOCustNo", tInsuRenew.getCustNo());
			occursList.putParam("OOFacmNo", tInsuRenew.getFacmNo());
			occursList.putParam("OOInsuYearMonth", month);
			occursList.putParam("OOTotInsuPrem", tInsuRenew.getTotInsuPrem());
			occursList.putParam("OOFireInsuCovrg", tInsuRenew.getFireInsuCovrg());
			occursList.putParam("OOFireInsuPrem", tInsuRenew.getFireInsuPrem());
			occursList.putParam("OOEthqInsuCovrg", tInsuRenew.getEthqInsuCovrg());
			occursList.putParam("OOEthqInsuPrem", tInsuRenew.getEthqInsuPrem());
			String statusCodeX = "";
//			0:正常 1:借支 2:催收 3:呆帳
			switch (tInsuRenew.getStatusCode()) {
			case 1:
				statusCodeX = "借支";
				break;
			case 2:
				statusCodeX = "催收";
				break;
			case 3:
				statusCodeX = "呆帳";
				break;
			case 0:
				statusCodeX = "正常";
				break;
			default:
				break;
			}
			occursList.putParam("OOStatusCodeX", statusCodeX);

			this.totaVo.addOccursList(occursList);

		}
	}
}