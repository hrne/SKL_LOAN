package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.domain.CustRmkId;
import com.st1.itx.db.service.CustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R30")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R30 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustRmkService sCustRmkService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R30 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 備忘錄序號
		int iRmkNo = parse.stringToInteger(titaVo.getParam("RimRmkNo"));

		// new table
		CustRmk tCustRmk = new CustRmk();
		// new PK
		CustRmkId CustRmkId = new CustRmkId();
		// 塞值到TablePK
		CustRmkId.setCustNo(iCustNo);
		CustRmkId.setRmkNo(iRmkNo);
		// FunCd 1新增
		if (iFunCd == 1) {

			// 測試該戶號是否存在顧客管控警訊檔
			tCustRmk = sCustRmkService.maxRmkNoFirst(iCustNo, titaVo);
			// 不存在備忘錄序號為1
			if (tCustRmk == null) {
				tCustRmk = new CustRmk();
			}
			this.info("RmkNo :" + tCustRmk.getRmkNo());
			this.totaVo.putParam("L2r30RmkNo", tCustRmk.getRmkNo() + 1);
			this.totaVo.putParam("L2r30RmkCode", "");
			this.totaVo.putParam("L2r30RmkDesc", "");
			// FunCd 2修改.4刪除.5查詢
		} else {
			tCustRmk = sCustRmkService.findById(CustRmkId, titaVo);
			// 該戶號 備忘錄序號查不到資料 拋錯
			if (tCustRmk == null) {
				throw new LogicException(titaVo, "E0001", "  該戶號" + iCustNo + "備忘錄序號" + iRmkNo + "不存在顧客管控警訊檔。"); //查詢資料不存在
			}

			this.totaVo.putParam("L2r30RmkNo", tCustRmk.getRmkNo());
			this.totaVo.putParam("L2r30RmkCode", tCustRmk.getRmkCode());
			this.totaVo.putParam("L2r30RmkDesc", tCustRmk.getRmkDesc());

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}