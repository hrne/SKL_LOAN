package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L618D")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L618D extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L618D.class);

	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcDetailCom acDetailCom;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L618D ");
		this.totaVo.init(titaVo);
		txToDoCom.setTxBuffer(this.getTxBuffer());

		// 提存項目
		String iItemCode = titaVo.getParam("TxItemCode");
		// 會計日期
		int iAcdate = parse.stringToInteger(titaVo.getParam("TxAcDate"));
		int iSlipBatNo =   parse.stringToInteger(titaVo.getParam("TxSlipBatNo"));
		List<AcDetail> acDetailList = new ArrayList<AcDetail>();
		AcDetail acDetail = new AcDetail();
		TempVo tTempVo = new TempVo();

//		應處理清單
		if (titaVo.isHcodeNormal()) {
			Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange(iItemCode, 0, 0, this.index,
					Integer.MAX_VALUE, titaVo);
			List<TxToDoDetail> lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
			if (lTxToDoDetail != null) {
				for (TxToDoDetail t : lTxToDoDetail) {
					tTempVo = tTempVo.getVo(t.getProcessNote());
					// 相同會計日
					if (tTempVo.getParam("AcDate").equals(titaVo.getParam("TxAcDate"))) {
						// update應處理清單
						txToDoCom.updDetailStatus(2, t.getTxToDoDetailId(), titaVo);
						/* 借 */
						for (int i = 1; i <= 5; i++) {
							if (tTempVo.get("DbAcctCode" + i) != null) {
								this.info("DbAcctCode = " + tTempVo.get("DbAcctCode" + i));
								acDetail = new AcDetail();
								acDetail.setAcDate(iAcdate); // 會計日期
								acDetail.setSlipBatNo(parse.stringToInteger(tTempVo.getParam("SlipBatNo"))); // 傳票批號
								acDetail.setDbCr("D"); // 借貸別
								acDetail.setAcctCode(tTempVo.get("DbAcctCode" + i)); // 業務科目
								acDetail.setTxAmt(parse.stringToBigDecimal(tTempVo.getParam("DbTxAmt" + i))); // 記帳金額
								acDetail.setRvNo(tTempVo.getParam("DbRvNo" + i)); // 銷帳編號
								acDetail.setSlipNote(tTempVo.getParam("SlipNote")); // 傳票摘要
								acDetail.setAcSubBookCode(tTempVo.get("AcSubBookCode")); // 區隔帳冊
								acDetailList.add(acDetail);
							}
						}

						/* 貸 */
						for (int i = 1; i <= 5; i++) {
							if (tTempVo.get("CrAcctCode" + i) != null) {
								this.info("CrAcctCode = " + tTempVo.get("CrAcctCode" + i));
								acDetail = new AcDetail();
								acDetail.setAcDate(iAcdate); // 會計日期
								acDetail.setSlipBatNo(parse.stringToInteger(tTempVo.getParam("SlipBatNo"))); // 傳票批號
								acDetail.setDbCr("C"); // 借貸別
								acDetail.setAcctCode(tTempVo.get("CrAcctCode" + i)); // 業務科目
								acDetail.setTxAmt(parse.stringToBigDecimal(tTempVo.getParam("CrTxAmt" + i))); // 記帳金額
								acDetail.setRvNo(tTempVo.getParam("CrRvNo" + i)); // 銷帳編號
								acDetail.setSlipNote(tTempVo.getParam("SlipNote")); // 傳票摘要
								acDetail.setAcSubBookCode(tTempVo.get("AcSubBookCode")); // 區隔帳冊
								acDetailList.add(acDetail);
							}
						}
					}
				}
			}
		}

//		帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			this.txBuffer.addAllAcDetailList(acDetailList);
			/* 產生會計分錄 */
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}

// BroadCast L6102
		if (titaVo.isHcodeNormal()) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6102",
					titaVo.getParam("TxAcDate") + titaVo.getParam("TxSlipBatNo"), "請執行L6102-核心傳票相關單獨作業，傳票批號: " + iSlipBatNo, titaVo);
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}