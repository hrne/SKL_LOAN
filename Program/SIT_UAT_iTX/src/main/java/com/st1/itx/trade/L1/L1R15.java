package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustRelDetail;
import com.st1.itx.db.domain.CustRelDetailId;
import com.st1.itx.db.domain.CustRelMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustRelDetailService;
import com.st1.itx.db.service.CustRelMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R15")
@Scope("prototype")
/**
 * 客戶關係調Rim
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R15 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1R15.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;
	@Autowired
	public CustRelMainService iCustRelMainService;
	@Autowired
	public CustRelDetailService iCustRelDetailService;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R15 ");
		this.totaVo.init(titaVo);
		String iCustRelMainUKey = titaVo.getParam("CustRelMainUKey");
		String iUKey = titaVo.getParam("Ukey");
		CustRelMain iCustRelMain = new CustRelMain();
		CustRelDetail iCustRelDetail = new CustRelDetail();
		CustRelDetailId iCustRelDetailId = new CustRelDetailId();
		iCustRelMain = iCustRelMainService.findById(iCustRelMainUKey, titaVo);
		iCustRelDetailId.setCustRelMainUKey(iCustRelMainUKey);
		iCustRelDetailId.setUkey(iUKey);
		this.info("明細檔KEY===="+iCustRelDetailId);
		iCustRelDetail = iCustRelDetailService.findById(iCustRelDetailId, titaVo);
		if (iCustRelMain == null) {
			throw new LogicException("E0001", "客戶關係主檔查無資料"); // 查無資料
		}
		if (iCustRelDetail == null) {
			throw new LogicException("E0001", "客戶關係明細檔查無資料"); // 查無資料
		}
		totaVo.putParam("L1R15CustRelId", iCustRelMain.getCustRelId());
		totaVo.putParam("L1R15CustRelName", iCustRelMain.getCustRelName());
		totaVo.putParam("L1R15RelTypeCode", iCustRelDetail.getRelTypeCode());
		totaVo.putParam("L1R15RelId", iCustRelDetail.getRelId());
		totaVo.putParam("L1R15RelName", iCustRelDetail.getRelName());
		totaVo.putParam("L1R15RelationCode", iCustRelDetail.getRelationCode());
		totaVo.putParam("L1R15RemarkTypeCode", iCustRelDetail.getRemarkTypeCode());
		totaVo.putParam("L1R15Remark", iCustRelDetail.getRemark());
		totaVo.putParam("L1R15Note", iCustRelDetail.getNote().replace("$n", "\n"));
		totaVo.putParam("L1R15Status", iCustRelDetail.getStatus());
		this.addList(this.totaVo);
		return this.sendList();
	}
}