package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R13")
@Scope("prototype")
/**
 * L2R13 尋找協辦人員檔資料
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R13 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R13.class);

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	public PfCoOfficerService sPfCoOfficerService;
	@Autowired
	Parse parse;

	public PfCoOfficer tPfCoOfficer;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R13 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iEmployeeNo = titaVo.getParam("RimEmployeeNo").trim();
		int iFunCode = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		tPfCoOfficer = new PfCoOfficer();


		// 查詢員工檔
		CdEmp tCdEmp = sCdEmpService.findById(iEmployeeNo, titaVo);
		
		//查詢 刪除 列印時 純顯示
		if (iFunCode == 4 || iFunCode == 5|| iFunCode == 7) {

			if (tCdEmp == null) {
				this.totaVo.putParam("OFullName", "");
				this.totaVo.putParam("OCenterCode2", "");
				this.totaVo.putParam("OCenterCode2Name", "");
			} else {

				this.totaVo.putParam("OFullName", tCdEmp.getFullname());
				this.totaVo.putParam("OCenterCode2", tCdEmp.getCenterCode2());
				this.totaVo.putParam("OCenterCode2Name", tCdEmp.getCenterCode2Name());

			}

		} else {
//			新增修改複製時 檢查是否存在員工檔並檢查是否為協辦人員
			if (tCdEmp == null) {
				throw new LogicException(titaVo, "E0001", "員工資料檔  員工編號=" + iEmployeeNo); // 查無資料
			} else {
//				檢查是否為協辦人員
				tPfCoOfficer = sPfCoOfficerService.EffectiveDateFirst(tCdEmp.getEmployeeNo(), 0, Integer.MAX_VALUE,
						titaVo);
				if (tPfCoOfficer == null) {
					throw new LogicException(titaVo, "E0001", "協辦人員檔  員工編號=" + tCdEmp.getEmployeeNo()); // 查無資料
				}

				this.totaVo.putParam("OFullName", tCdEmp.getFullname());
				this.totaVo.putParam("OCenterCode2", tCdEmp.getCenterCode2());
				this.totaVo.putParam("OCenterCode2Name", tCdEmp.getCenterCode2Name());

			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}