package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.parse.Parse;

@Service("L650B")
@Scope("prototype")
/**
 * 排除部門別-
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L650B extends TradeBuffer {

	/* 轉型共用工具 */

	@Autowired
	public CdPfParmsService iCdPfParmsService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L650B ");
		this.totaVo.init(titaVo);
		// 先刪除
		Slice<CdPfParms> dCdPfParms = null;
		if (!titaVo.getHsupCode().equals("1")) {
			iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
		}
		dCdPfParms = iCdPfParmsService.findConditionCode1Eq("2", 0, Integer.MAX_VALUE, titaVo);
		if (dCdPfParms != null) {
			for (CdPfParms xCdPfParms : dCdPfParms) {
				try {
					iCdPfParmsService.delete(xCdPfParms, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}

		CdPfParms iCdPfParams = new CdPfParms();
		CdPfParmsId iCdPfParamsId = new CdPfParmsId();
		
		String[] types = new String[] {"業績全部", "換算業績、業務報酬", "換算業績、業務報酬", "加碼獎勵津貼", "協辦獎金"};
		
		for (int t = 0; t < types.length; t++)
		{
			char code = (char)((int)'A' + t); // A B C D E
			this.info("code: " + code);
			
			for (int i = 1; i <= 30; i++)
			{
				String deptCode = titaVo.getParam("DeptCode" + code + i);
				
				if (deptCode == null || deptCode.trim().isEmpty())
				{
					break;
				}
				
				int workMonthStart = parse.stringToInteger(titaVo.getParam("WorkMonthS" + code + i));
				int workMonthEnd = parse.stringToInteger(titaVo.getParam("WorkMonthE" + code + i));
				
				workMonthStart += workMonthStart > 0 ? 191100 : 0;
				workMonthEnd += workMonthEnd > 0 ? 191100 : 0;
				
				this.info("deptCode: " + deptCode + " workMonthStart: " + workMonthStart + " workMonthEnd: " + workMonthEnd);
				
				iCdPfParamsId.setConditionCode1("2");
				iCdPfParamsId.setConditionCode2(parse.IntegerToString(t+1, 1));
				iCdPfParamsId.setCondition(deptCode);
				iCdPfParams.setCdPfParmsId(iCdPfParamsId);
				iCdPfParams.setWorkMonthStart(workMonthStart);
				iCdPfParams.setWorkMonthEnd(workMonthEnd);
				
				try {
					iCdPfParmsService.insert(iCdPfParams, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "排除商品別-" + types[t]);
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}