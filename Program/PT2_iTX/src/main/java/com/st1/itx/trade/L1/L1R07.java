package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R07")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R07 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R07 ");
		this.totaVo.init(titaVo);

		// 給L2902,L2919使用查是否為借戶
		// 參數使用RimCustId

		// tita
		// 統編
		String iCustId = titaVo.getParam("RimCustId");

		// 借戶否
		String Borrower = "";

		// new table
		CustMain tCustMain = new CustMain();

		// 統編查客戶主檔
		tCustMain = iCustMainService.custIdFirst(iCustId, titaVo);

		// 如果大於0 則為借戶給Y 否則給N
		if (tCustMain == null) {
			Borrower = "N";
			this.info("null查無資料給N");

		} else {
			int lastFacmNo = tCustMain.getLastFacmNo();

			if (lastFacmNo > 0) {
				Borrower = "Y";
				this.info("有資料lastfacmno大於0給Y");
			} else {
				Borrower = "N";
				this.info("有資料lastfacmno等於0給N");
			}
		}

		this.totaVo.putParam("L1r07Borrower", Borrower);

		this.addList(this.totaVo);
		return this.sendList();
	}
}