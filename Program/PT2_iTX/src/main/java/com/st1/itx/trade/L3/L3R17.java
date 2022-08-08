package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.domain.LoanFacTmpId;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3R17")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L3R17 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanFacTmpService sLoanFacTmpService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R17 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改  3查詢 4刪除 
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 額度編號
		int iFacmNo	= parse.stringToInteger(titaVo.getParam("RimFacmNo"));

		// new table
		LoanFacTmp tLoanFacTmp = new LoanFacTmp();
		// new PK
		LoanFacTmpId LoanFacTmpId = new LoanFacTmpId();
		// 塞值到TablePK
		LoanFacTmpId.setCustNo(iCustNo);
		LoanFacTmpId.setFacmNo(iFacmNo);
		// FunCd 1新增
		tLoanFacTmp = sLoanFacTmpService.findById(LoanFacTmpId, titaVo);
		this.info("iFunCd    = " + iFunCd );
		if (iFunCd == 1) {
			
			if(tLoanFacTmp != null) {
				throw new LogicException(titaVo, "E0001", " 該額度存在暫收款指定額度設定查詢檔。"); 
			}
			this.info("回傳空的處理說明");
			this.totaVo.putParam("L3r17RmkDescribe", "");
			
		}else{

			this.info("iFunCdelse   = " + iFunCd);
			// 該戶號查不到資料 拋錯
			if (tLoanFacTmp == null) {
				throw new LogicException(titaVo, "E0001", "  該戶號" + iCustNo +"不存在暫收款指定額度設定查詢。"); //查詢資料不存在
			}
			this.totaVo.putParam("L3r17RmkDescribe", tLoanFacTmp.getDescribe());

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}