package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.ConstructionCompany;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.ConstructionCompanyService;
import com.st1.itx.db.service.CustMainService;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R50")
@Scope("prototype")
/**
 * L5R50 建商名單檔
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
public class L5R50 extends TradeBuffer {

	@Autowired
	ConstructionCompanyService constructionCompanyService;

	@Autowired
	CustMainService custMainService;

	@Autowired
	CdEmpService cdEmpService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R50 ");
		this.totaVo.init(titaVo);

		int rimFunCd = Integer.parseInt(titaVo.getParam("RimFunCd"));
		int rimCustNo = Integer.parseInt(titaVo.getParam("RimCustNo"));

		ConstructionCompany constructionCompany;

		constructionCompany = constructionCompanyService.findById(rimCustNo, titaVo);

		if (rimFunCd == 1) {
			if (constructionCompany == null) {
				CustMain custMain = custMainService.custNoFirst(rimCustNo, rimCustNo, titaVo);
				String custName = custMain == null ? "" : custMain.getCustName();
				this.totaVo.putParam("L5r50CustNo", rimCustNo);
				this.totaVo.putParam("L5r50CustName", custName);
				this.totaVo.putParam("L5r50DeleteFlag", "");
				this.totaVo.putParam("L5r50CreateDate", "");
				this.totaVo.putParam("L5r50CreateEmpNo", "");
				this.totaVo.putParam("L5r50CreateEmpName", "");
				this.totaVo.putParam("L5r50LastUpdate", "");
				this.totaVo.putParam("L5r50LastUpdateEmpNo", "");
				this.totaVo.putParam("L5r50LastUpdateEmpName", "");
			} else {
				// 回傳"已存在資料"的錯誤
				throw new LogicException("E0002", "建商名單檔已存在此戶號(" + rimCustNo + ")");
			}
		} else {
			if (constructionCompany == null) {
				// 回傳"查無資料"的錯誤
				throw new LogicException("E0001", "建商名單檔不存在此戶號(" + rimCustNo + ")");
			} else {
				int custNo = constructionCompany.getCustNo();

				CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
				String custName = custMain == null ? "" : custMain.getCustName();

				this.totaVo.putParam("L5r50CustNo", constructionCompany.getCustNo());
				this.totaVo.putParam("L5r50CustName", custName);
				this.totaVo.putParam("L5r50DeleteFlag", constructionCompany.getDeleteFlag());
				this.totaVo.putParam("L5r50CreateDate",
						parse.timeStampToStringDate(constructionCompany.getCreateDate()));
				this.totaVo.putParam("L5r50CreateEmpNo", constructionCompany.getCreateEmpNo());
				this.totaVo.putParam("L5r50CreateEmpName", getEmpName(titaVo, constructionCompany.getCreateEmpNo()));
				this.totaVo.putParam("L5r50LastUpdate",
						parse.timeStampToStringDate(constructionCompany.getLastUpdate()));
				this.totaVo.putParam("L5r50LastUpdateEmpNo", constructionCompany.getLastUpdateEmpNo());
				this.totaVo.putParam("L5r50LastUpdateEmpName",
						getEmpName(titaVo, constructionCompany.getLastUpdateEmpNo()));
			}
		}
		this.addList(this.totaVo);
		return this.sendList();

	}

	private String getEmpName(TitaVo titaVo, String empNo) {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}