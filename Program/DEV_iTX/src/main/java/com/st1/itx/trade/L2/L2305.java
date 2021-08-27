package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.domain.ReltMainId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 */

@Service("L2305")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L2305 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ReltMainService iReltMainService;

	@Autowired
	public CustMainService sCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2305 ");
		this.totaVo.init(titaVo);

		int iOriginCaseNo = Integer.valueOf(titaVo.getParam("OriginCaseNo"));
		int iTargetCaseNo = Integer.valueOf(titaVo.getParam("TargetCaseNo"));
		
		int iOriginCustNo = Integer.valueOf(titaVo.getParam("OriginCustNo")); //若戶號可修改時使用
		int iTargetCustNo = Integer.valueOf(titaVo.getParam("TargetCustNo"));
		
		Slice<ReltMain> iReltMain = null;
		
		iReltMain = iReltMainService.caseNoEq(iOriginCaseNo, 0, Integer.MAX_VALUE, titaVo);
		
		if (iReltMain == null) {
			throw new LogicException(titaVo, "E2003", "無關係人檔資料"); // 查無資料
		}
		
		for (ReltMain rReltMain : iReltMain) {
			ReltMain nReltMain = new ReltMain();
			ReltMain tReltMain = new ReltMain();
			ReltMainId nReltMainId = new ReltMainId();
			nReltMainId.setCaseNo(iTargetCaseNo);
			nReltMainId.setCustNo(iTargetCustNo);
			nReltMainId.setReltId(rReltMain.getReltId());
			tReltMain = iReltMainService.findById(nReltMainId, titaVo);
			if (tReltMain !=null) {
				throw new LogicException(titaVo, "E0005", "案件編號"+iTargetCaseNo+"已有相同資料(戶號: "+rReltMain.getCustNo()+"，關係人統編: "+rReltMain.getReltId()+")"); 
			}
			nReltMain.setReltMainId(nReltMainId);
			nReltMain.setApplDate(rReltMain.getApplDate());
			nReltMain.setReltCode(rReltMain.getReltCode());
			nReltMain.setReltmark(rReltMain.getReltmark());
			nReltMain.setReltName(rReltMain.getReltName());
			nReltMain.setRemarkType(rReltMain.getRemarkType());
			try {
				iReltMainService.insert(nReltMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}