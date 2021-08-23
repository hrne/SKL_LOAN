package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2222")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2222 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2222.class);

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;
	
	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2222 ");
		this.totaVo.init(titaVo);
		
//		String iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		String creditSysNo = titaVo.getParam("CreditSysNo").toString().trim();
		int iCreditSysNo = 0;
		if (!"".equals(creditSysNo)) {
			iCreditSysNo = parse.stringToInteger(creditSysNo);
		}
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		String relUKey = titaVo.getParam("RelUKey");
		String relCode = titaVo.getParam("RelCode");
		
		
		ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
		clOwnerRelationId.setCreditSysNo(iCreditSysNo);
		clOwnerRelationId.setCustNo(iCustNo);
		clOwnerRelationId.setOwnerCustUKey(relUKey);
		
		ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId, titaVo);

		if (clOwnerRelation == null) {
			clOwnerRelation = new ClOwnerRelation();
			clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
			clOwnerRelation.setOwnerRelCode(relCode);
			try {
				sClOwnerRelationService.insert(clOwnerRelation, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
			}
		} else {
			clOwnerRelation.setOwnerRelCode(relCode);
			try {
				sClOwnerRelationService.update(clOwnerRelation, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
			}
		}		
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}