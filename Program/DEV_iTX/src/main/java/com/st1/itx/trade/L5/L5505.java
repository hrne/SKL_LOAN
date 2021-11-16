package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.data.PfDetailVo;

import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.PfItDetailService;

@Service("L5505")
@Scope("prototype")
/**
 * 業績案件計件代碼維護
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5505 extends TradeBuffer {
	
	@Autowired
	PfDetailCom pfDetailCom;
	
	@Autowired
	public PfItDetailService pfItDetailService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5505 ");
		this.totaVo.init(titaVo);

		Long iLogNo = Long.valueOf(titaVo.getParam("LogNo").trim());
//		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim());
//		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo").trim());
//		int iBormNo = Integer.valueOf(titaVo.getParam("BormNo").trim());
		String PieceCodeAft = titaVo.getParam("PieceCodeAft").trim();
		
		PfItDetail pfItDetail = pfItDetailService.findById(iLogNo, titaVo);
		
		if (pfItDetail == null) {
			throw new LogicException("E0001", "業績資料=" + iLogNo);
		}
		
		pfDetailCom.setTxBuffer(this.getTxBuffer());

		PfDetailVo pf = new PfDetailVo();
		
		pf.setCustNo(pfItDetail.getCustNo()); // 借款人戶號
		pf.setFacmNo(pfItDetail.getFacmNo()); // 額度編號
		pf.setBormNo(pfItDetail.getBormNo()); // 撥款序號
		pf.setPieceCode(PieceCodeAft); // 計件代碼
		pf.setPieceCodeSecond(""); // 計件代碼2
		pf.setRepayType(1); // 還款類別 1.計件代碼變更
		pf.setEmpResetFg("N"); // 是否更新介紹人所屬單位資料欄Y/N
		pf.setDrawdownDate(pfItDetail.getDrawdownDate());// 撥款日期
		pf.setDrawdownAmt(pfItDetail.getDrawdownAmt());// 撥款金額
		pf.setPieceCodeSecondAmt(new BigDecimal("0"));// 計件代碼2金額
		pf.setRepaidPeriod(0); // 已攤還期數

		pfDetailCom.addDetail(pf, titaVo); // 產生業績明細

		this.addList(this.totaVo);
		return this.sendList();
	}
}