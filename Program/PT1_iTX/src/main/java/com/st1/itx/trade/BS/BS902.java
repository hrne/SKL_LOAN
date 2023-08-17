package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.http.WebClient;

@Component("BS902")
@Scope("prototype")

/**
 * 新增應處理明細－企金費用攤提（月初日） <br>
 * 執行時機：日始作業，應處理清單維護(BS001)自動執行 <br>
 * 
 * @author Lai
 * @version 1.0.0
 */

public class BS902 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS902 ......");

		txToDoCom.setTxBuffer(this.txBuffer);


		// 取本年月份
		int entryDateMm = this.txBuffer.getMgBizDate().getTbsDy() / 100;

		// find data
		// SL 聯貸費用
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		Slice<AcReceivable> slAcReceivable = acReceivableService.useBs902Eq(0, 9999999, 0, 5, "SL" + "%", 0,
				Integer.MAX_VALUE, titaVo);
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();

		// data size > 0 -> 新增應處理明細
		TxToDoDetail tTxToDoDetail;
		if (lAcReceivable != null) {
			this.info("取年本月份 = " + entryDateMm);
			for (AcReceivable rv : lAcReceivable) {
				// 不為另收費用或已銷帳者不寫入
				this.info("rv =" + rv);
				this.info("rv.getReceivableFlag() =" + rv.getReceivableFlag());
				this.info("rv.getClsFlag() =" + rv.getClsFlag());
				if (rv.getReceivableFlag() != 5 || rv.getClsFlag() == 1) {
					continue;
				}
//				相同月份與小於本月份資料寫入應處理清單
				this.info("rv.getRvNo().length()" + rv.getRvNo().length());
				this.info("rv.getRvNo().substring(10, 15)" + rv.getRvNo().substring(10, 15));
				if (rv.getRvNo().length() > 10
						&& parse.stringToInteger(rv.getRvNo().substring(10, 15)) <= entryDateMm) { // SL-XX-000-YYYMM
					tTxToDoDetail = new TxToDoDetail();
					TempVo tTempVo = new TempVo();
					tTempVo.clear();
					tTempVo.putParam("AcctCode", rv.getAcctCode());
					tTxToDoDetail.setItemCode("SLCL00"); // 聯貸費用攤提
					tTxToDoDetail.setCustNo(rv.getCustNo());
					tTxToDoDetail.setFacmNo(rv.getFacmNo());
					tTxToDoDetail.setDtlValue(rv.getRvNo());
					tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
					txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				}
			}
		}
		if ("LC899".equals(titaVo.getTxcd())) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", "BS902已完成", titaVo);
		}

		return null;
	}

}