package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.domain.ReltMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R15")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R15 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ReltMainService sReltMainService;
	@Autowired
	public CustMainService sCustMainService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R15 ");
		this.totaVo.init(titaVo);

		// tita取值
		// 統編
		int iCaseNo = parse.stringToInteger(titaVo.getParam("RimCaseNo"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		String iReltId = titaVo.getParam("RimReltId");
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFuncCode"));

		String Ukey = "";
		CustMain lCustMain = new CustMain();

		lCustMain = sCustMainService.custIdFirst(iReltId, titaVo);

		if (lCustMain == null) {
			throw new LogicException("E0001", "客戶資料主檔");
		}

		Ukey = lCustMain.getCustUKey();
		ReltMainId iReltMainId = new ReltMainId();
		iReltMainId.setCaseNo(iCaseNo);
		iReltMainId.setCustNo(iCustNo);
		iReltMainId.setReltUKey(Ukey);
		ReltMain tReltMain = sReltMainService.findById(iReltMainId, titaVo);

		this.totaVo.putParam("L2r15ReltName", "");
		this.totaVo.putParam("L2r15PosInd", "");
		this.totaVo.putParam("L2r15RemarkType", "");
		this.totaVo.putParam("L2r15Remark", "");

		if (iFunCd == 1) {
			if (tReltMain != null) {
				throw new LogicException(titaVo, "E0002", ""); // 新增資料已存在
			}
		} else {
			if (tReltMain != null) {
				/* 存入Tota */
				this.totaVo.putParam("L2r15ReltName", lCustMain.getCustName());
				this.totaVo.putParam("L2r15PosInd", tReltMain.getReltCode());
				this.totaVo.putParam("L2r15RemarkType", tReltMain.getRemarkType());
				this.totaVo.putParam("L2r15Remark", tReltMain.getReltmark());
			} else {
				if (iFunCd == 2) {
					throw new LogicException(titaVo, "E0003", ""); // 修改資料不存在
				}
				if (iFunCd == 4) {
					throw new LogicException(titaVo, "E0004", ""); // 刪除資料不存在
				}
				if (iFunCd == 5) {
					throw new LogicException(titaVo, "E0001", ""); // 查詢資料不存在
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();

	}
}