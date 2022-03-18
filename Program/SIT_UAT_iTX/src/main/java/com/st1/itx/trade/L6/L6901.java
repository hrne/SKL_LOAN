package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcEnterCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcTxFormCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6901")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6901 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AcPaymentCom acPaymentCom;

	@Autowired
	public AcTxFormCom acTxFormCom;

	@Autowired
	public AcDetailCom acDetailCom;
	@Autowired
	public AcEnterCom acEnterCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6901 ");
		this.totaVo.init(titaVo);

		// tita 登放日期 RelDy
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate"));
		int iRelDy = parse.stringToInteger(titaVo.getParam("RelDy"));
		// tita 登放序號 RelTxseq
		String iRelTxseq = titaVo.getParam("RelTxseq");
		int iSlipNo = parse.stringToInteger(titaVo.getParam("SlipNo"));
		if (iSlipNo == 0 && iRelTxseq.length() < 18) {
			if (iRelDy == 19110000) {

				throw new LogicException(titaVo, "E6105", ""); // 查無資料
			} else {

				throw new LogicException(titaVo, "E6106", ""); // 查無資料
			}
//			this.info("長度不符18碼拋錯待填"); //TODO:拋錯
		}
		this.info("iAcDate		=" + iAcDate);
		this.info("iRelDy			=" + iRelDy);
		this.info("iRelTxseq		=" + iRelTxseq);
		// new ArrayList
		List<AcDetail> lAcDetailList = new ArrayList<AcDetail>();
		Slice<AcDetail> slAcDetailList;
		// new TABLE acDetail
		// AcDetail tacDetail = new AcDetail();
		// 登放日期,登放序號找AcDetail資料
		if (iRelDy == 0) {
			if(iSlipNo>0) {
				slAcDetailList = sAcDetailService.acdtlSlipNo2(iAcDate + 19110000, iSlipNo, this.index,
						Integer.MAX_VALUE, titaVo);
				if(slAcDetailList!=null) {//再依交易序號查詢
					slAcDetailList = sAcDetailService.acdtlRelTxseqEq2(iAcDate + 19110000, slAcDetailList.getContent().get(0).getRelTxseq(), this.index,
							Integer.MAX_VALUE, titaVo);
				}
			} else {
				slAcDetailList = sAcDetailService.acdtlRelTxseqEq2(iAcDate + 19110000, iRelTxseq, this.index,
						Integer.MAX_VALUE, titaVo);
			}
			
			lAcDetailList = slAcDetailList == null ? null : slAcDetailList.getContent();
		} else {
			if(iSlipNo>0) {
				slAcDetailList = sAcDetailService.acdtlSlipNo(iRelDy + 19110000, iSlipNo, this.index,
						Integer.MAX_VALUE, titaVo);
				if(slAcDetailList!=null) {//再依交易序號查詢
					slAcDetailList = sAcDetailService.acdtlRelTxseqEq2(iAcDate + 19110000, slAcDetailList.getContent().get(0).getRelTxseq(), this.index,
							Integer.MAX_VALUE, titaVo);
				}
			} else {
				slAcDetailList = sAcDetailService.acdtlRelTxseqEq(iRelDy + 19110000, iRelTxseq, this.index,
						Integer.MAX_VALUE, titaVo);
			}
			
			if (slAcDetailList != null) {
				for (AcDetail ac : slAcDetailList.getContent()) {				
					if (iAcDate == 0 || ac.getAcDate() == iAcDate) {
						lAcDetailList.add(ac);
					}
				}
			}
		}

		if (lAcDetailList == null || lAcDetailList.size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}

		this.info("lAcDetailList" + lAcDetailList);
		this.txBuffer.addAllAcDetailList(lAcDetailList);

		acTxFormCom.setTxBuffer(this.getTxBuffer());

		this.addAllList(acTxFormCom.run(titaVo));
		return this.sendList();
	}
}