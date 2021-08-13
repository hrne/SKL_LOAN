package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R14")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R14 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5R14.class);

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public PfBsOfficerService iPfBsOffcierService;

	@Autowired
	public CdBcmService iCdBcmService;
	@Autowired
	public CdEmpService iCdEmpService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// L5401交易內調RIM用,資料來源為CdBcm表
		this.info("active L5R14 ");
		this.info("L5R14 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iChoose = titaVo.getParam("RimChoose").trim();// 輸入位置
		CdBcm iCdBcm = new CdBcm();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		// 分別撈資料
		CdEmp iCdEmp = new CdEmp();
		switch (iChoose) {
		case "1":
			String iAreaCode = titaVo.getParam("RimAreaCode").trim();// 區域中心
			iCdBcm = iCdBcmService.findById(iAreaCode, titaVo);
			if (iCdBcm != null) {
				totaVo.putParam("L5R14UnitManager", iCdBcm.getUnitManager());
				totaVo.putParam("L5R14DeptManager", "");
				totaVo.putParam("L5R14DistManager", "");
				if (iCdBcm.getUnitManager().equals("")) {
					totaVo.putParam("L5R14EmpName", "");
				} else {
					iCdEmp = iCdEmpService.findById(iCdBcm.getUnitManager(), titaVo);
					if (iCdEmp == null) {
						totaVo.putParam("L5R14EmpName", "");
					}else{
						totaVo.putParam("L5R14EmpName", iCdEmp.getFullname());
					}
				}
				totaVo.putParam("L5R14DeptCode", iCdBcm.getDeptCode());
				totaVo.putParam("L5R14DistCode", iCdBcm.getDistCode());
				totaVo.putParam("L5R14AreaItem", iCdBcm.getUnitItem());
				totaVo.putParam("L5R14DeptItem", iCdBcm.getDeptItem());
				totaVo.putParam("L5R14DistItem", iCdBcm.getDistItem());
				totaVo.putParam("L5R14DirectorCode", "M");
			} else {
				throw new LogicException(titaVo, "E0001", "查無此單位代號"); // 無此代號錯誤
			}
			break;
		case "2":
			this.info("查詢部室代號");
			String iDistCode = titaVo.getParam("RimDistCode").trim();
			iCdBcm = iCdBcmService.distCodeFirst(iDistCode, titaVo);
			if (iCdBcm != null) {
				totaVo.putParam("L5R14UnitManager", "");
				totaVo.putParam("L5R14DeptManager", "");
				totaVo.putParam("L5R14DistManager", iCdBcm.getDistManager());
				if (iCdBcm.getDistManager().equals("")) {
					totaVo.putParam("L5R14EmpName", "");
				} else {
					iCdEmp = iCdEmpService.findById(iCdBcm.getDistManager(), titaVo);
					if (iCdEmp == null) {
						totaVo.putParam("L5R14EmpName", "");
					}else{
						totaVo.putParam("L5R14EmpName", iCdEmp.getFullname());
					}
				}
				totaVo.putParam("L5R14DeptCode", iCdBcm.getDeptCode());
				totaVo.putParam("L5R14DistCode", iCdBcm.getDistCode());
				totaVo.putParam("L5R14AreaItem", "");
				totaVo.putParam("L5R14DeptItem", iCdBcm.getDeptItem());
				totaVo.putParam("L5R14DistItem", iCdBcm.getDistItem());
				totaVo.putParam("L5R14DirectorCode", "B");
			} else {
				throw new LogicException(titaVo, "E0001", "查無此區部代號"); // 無此代號錯誤
			}
			break;
		case "3":
			this.info("查詢區部代號");
			String iDeptCode = titaVo.getParam("RimDeptCode").trim();
			iCdBcm = iCdBcmService.deptCodeFirst(iDeptCode, titaVo);
			if (iCdBcm != null) {
				totaVo.putParam("L5R14UnitManager", "");
				totaVo.putParam("L5R14DeptManager", iCdBcm.getDeptManager());
				totaVo.putParam("L5R14DistManager", "");
				if (iCdBcm.getDeptManager().equals("")) {
					totaVo.putParam("L5R14EmpName", "");
				} else {
					iCdEmp = iCdEmpService.findById(iCdBcm.getDeptManager(), titaVo);
					if (iCdEmp == null) {
						totaVo.putParam("L5R14EmpName", "");
					}else{
						totaVo.putParam("L5R14EmpName", iCdEmp.getFullname());
					}
				}
				totaVo.putParam("L5R14DeptCode", iCdBcm.getDeptCode());
				totaVo.putParam("L5R14DistCode", "");
				totaVo.putParam("L5R14AreaItem", "");
				totaVo.putParam("L5R14DeptItem", iCdBcm.getDeptItem());
				totaVo.putParam("L5R14DistItem", "");
				totaVo.putParam("L5R14DirectorCode", "D");
			} else {
				throw new LogicException(titaVo, "E0001", "查無此部室代號"); // 無此代號錯誤
			}
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}