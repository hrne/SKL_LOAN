package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;

/* Tita
 * RimEmployeeNo=X,6
 */
/**
 * L2R08 尋找員工檔資料
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R08")
@Scope("prototype")
public class L2R08 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public TxTellerService txTellerService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R08 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iEmployeeNo = titaVo.getParam("RimEmployeeNo").trim();
		String iErrorSkip = titaVo.get("RimErrorSkip");

		if (iErrorSkip == null)
			iErrorSkip = "N";
		// 檢查輸入資料
		if (iEmployeeNo.isEmpty()) {
			throw new LogicException(titaVo, "E2012", "員工編號"); // 查詢資料不可為空白
		}

		// 查詢員工資料檔
		CdEmp tCdEmp = cdEmpService.findById(iEmployeeNo, titaVo);
		if (tCdEmp == null) {
			// X add by eric for L5503
			if (iErrorSkip.equals("Y") || iErrorSkip.equals("Z")) {
				this.totaVo.putParam("OFullName", "");
				this.totaVo.putParam("OCenterCode1", "");
				this.totaVo.putParam("OCenterCode1Name", "");
				this.totaVo.putParam("OCenterCode2", "");
				this.totaVo.putParam("OCenterCode2Name", "");
			} else {
				throw new LogicException(titaVo, "E0001", " 員工資料檔  員工編號=" + iEmployeeNo); // 查無資料
			}
		} else {
			if (!tCdEmp.getAgCurInd().equals("Y")) {
				if (!iErrorSkip.equals("Y")) {
					throw new LogicException(titaVo, "E2081", " 員工編號=" + iEmployeeNo); // 該員工非現職人員
				}
			}
			this.totaVo.putParam("OFullName", tCdEmp.getFullname());
			this.totaVo.putParam("OCenterCode1", tCdEmp.getCenterCode1());
			this.totaVo.putParam("OCenterCode1Name", tCdEmp.getCenterCode1Name());
			this.totaVo.putParam("OCenterCode2", tCdEmp.getCenterCode2());
			this.totaVo.putParam("OCenterCode2Name", tCdEmp.getCenterCode2Name());
		}

		TxTeller tTxTeller = txTellerService.findById(iEmployeeNo, titaVo);
		this.totaVo.putParam("OGroupNo", "");
		this.totaVo.putParam("OBrno", "");
		if(tTxTeller!=null) {
			this.totaVo.putParam("OGroupNo", tTxTeller.getGroupNo());
			this.totaVo.putParam("OBrno", tTxTeller.getBrNo());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}