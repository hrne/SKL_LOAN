package com.st1.itx.trade.L6;

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
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdAoDept;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R18EmployeeNo=X,6
 */
@Service("L6R18") // 尋找放款專員所屬業務部室對照檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R18 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R18.class);

	/* DB服務注入 */
	@Autowired
	public CdAoDeptService cdAoDeptService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R18 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimEmployeeNo = titaVo.getParam("RimL6R18EmployeeNo");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R18"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R18"); // 功能選擇錯誤
		}
		if (iRimEmployeeNo.isEmpty()) {
			throw new LogicException(titaVo, "E6011", "員工編號"); // 查詢資料不可為空白
		}

		// 查詢員工資料檔
		CdEmp tCdEmp = cdEmpService.findById(iRimEmployeeNo, titaVo);
		if (tCdEmp == null) {
			throw new LogicException(titaVo, "E0001", "員工資料檔  員工編號 = " + iRimEmployeeNo); // 查無資料
		}
		if (!(tCdEmp.getAgCurInd().equals("Y") || tCdEmp.getAgCurInd().equals("y"))) {
			throw new LogicException(titaVo, "E6012", "員工編號 = " + iRimEmployeeNo); // 該員工非現職人員
		}

		// 查詢放款專員所屬業務部室對照檔
		CdAoDept tCdAoDept = cdAoDeptService.findById(iRimEmployeeNo, titaVo);

		/* 如有找到資料 */
		if (tCdAoDept != null) {
			if (iRimTxCode.equals("L6753") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", "員工編號 = " + iRimEmployeeNo); // 新增資料已存在
			} else {
				// 查詢分公司資料檔
				CdBcm tCdBcm = sCdBcmService.deptCodeFirst(tCdAoDept.getDeptCode(), titaVo);
				if (tCdBcm == null) {
					this.totaVo.putParam("L6R18DeptItem", "");
				} else {
					this.totaVo.putParam("L6R18DeptItem", tCdBcm.getDeptItem());
				}
				/* 將資料放入Tota */
				this.totaVo.putParam("L6R18EmployeeNo", tCdAoDept.getEmployeeNo());
				this.totaVo.putParam("L6R18Fullname", tCdEmp.getFullname());
				this.totaVo.putParam("L6R18DeptCode", tCdAoDept.getDeptCode());
			}
		}

		/* 找不到資料 */
		if (tCdAoDept == null) {
			if (iRimTxCode.equals("L6753") && iRimFuncCode == 1) {
				this.totaVo.putParam("L6R18EmployeeNo", tCdEmp.getEmployeeNo());
				this.totaVo.putParam("L6R18Fullname", tCdEmp.getFullname());
				this.totaVo.putParam("L6R18DeptCode", tCdEmp.getCenterCode2());
				this.totaVo.putParam("L6R18DeptItem", tCdEmp.getCenterCode2Name());
			} else {
				throw new LogicException(titaVo, "E0001", "放款專員所屬業務部室對照檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
