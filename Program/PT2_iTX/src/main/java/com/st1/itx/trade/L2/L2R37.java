package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R37")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R37 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R37 ");
		this.totaVo.init(titaVo);

		// tita參數
		// 功能
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 契變日期
		int iContractChgDate = parse.stringToInteger(titaVo.getParam("RimContractChgDate")) + 19110000;
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		parse.stringToInteger(titaVo.getParam("RimContractChgNo"));

		String iRvNo = iContractChgDate + titaVo.getParam("RimContractChgNo");

		this.info("iRvNo = " + iRvNo);

		AcReceivable tAcReceivable = acReceivableService.findById(new AcReceivableId("F29", iCustNo, iFacmNo, iRvNo), titaVo);

		if (tAcReceivable == null) {
			switch (iFunCd) {

			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "貸後契變手續費檔");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "貸後契變手續費檔");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "貸後契變手續費檔");

			case 7:
				// 若為列印，但資料不存在，拋錯
				throw new LogicException("E0001", "貸後契變手續費檔");

			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "貸後契變手續費檔");

			}
		}
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(tAcReceivable.getJsonFields());

		int CreateDate = parse.stringToInteger((String.valueOf(
				tAcReceivable.getCreateDate().toString().substring(0, 4) + tAcReceivable.getCreateDate().toString().substring(5, 7) + tAcReceivable.getCreateDate().toString().substring(8, 10))))
				- 19110000;

		String CreateTime = tAcReceivable.getCreateDate().toString().substring(11, 19);

		this.totaVo.putParam("L2r37ContractChgCode", tTempVo.get("ContractChgCode")); // 契變項目代碼
		this.totaVo.putParam("L2r37CurrencyCode", tAcReceivable.getCurrencyCode()); // 幣別
		this.totaVo.putParam("L2r37FeeAmt", tAcReceivable.getRvAmt()); // 契變手續費

		if (tAcReceivable.getClsFlag() == 1) {
			this.totaVo.putParam("L2r37AcDate", tAcReceivable.getLastAcDate()); // 會計日期
			this.totaVo.putParam("L2r37TitaTxtNo", tAcReceivable.getTitaTxtNo()); // 交易序號
		} else {
			this.totaVo.putParam("L2r37AcDate", 0); // 會計日期
			this.totaVo.putParam("L2r37TitaTxtNo", 0); // 交易序號
		}
		this.totaVo.putParam("L2r37TLR_NO", tAcReceivable.getTitaTlrNo()); // 經辦編號
		this.totaVo.putParam("L2r37MODIFY_DT", CreateDate); // 作業日期
		this.totaVo.putParam("L2r37WKTIME", CreateTime); // 作業時間
		this.addList(this.totaVo);
		return this.sendList();
	}
}