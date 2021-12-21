package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimEmployeeNo=X,6
 */
@Service("L6R17") // 尋找員工檔資料
@Scope("prototype")
public class L6R17 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R17 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimEmployeeNo = titaVo.getParam("RimEmployeeNo");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R17"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R17"); // 功能選擇錯誤
		}
		// if (iRimEmployeeNo.isEmpty()) {
		// throw new LogicException(titaVo, "E6011", "員工編號"); // 查詢資料不可為空白
		// }

		// 初始值Tota
		moveTotaCdEmp(new CdEmp());

		// 查詢員工資料檔
		CdEmp tCdEmp = cdEmpService.findById(iRimEmployeeNo, titaVo);

		/* 如有找到資料 */
		if (tCdEmp != null) {
//			if (!(tCdEmp.getAgCurInd().equals("Y") || tCdEmp.getAgCurInd().equals("y"))) {
//				throw new LogicException(titaVo, "E6012", "員工編號 = " + iRimEmployeeNo); // 該員工非現職人員
//			} else {
			moveTotaCdEmp(tCdEmp);
//			}
		} else {
			if (iRimFuncCode == 5) {
				this.info("L6R17 iRimFuncCode : " + iRimFuncCode);
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "員工資料檔  員工編號 = " + iRimEmployeeNo); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveTotaCdEmp(CdEmp mCdEmp) throws LogicException {

		this.totaVo.putParam("L6R17EmployeeNo", mCdEmp.getEmployeeNo()); // 員工編號
		this.info("L6R17 moveTotaCdEmp EmployeeNo : " + mCdEmp.getEmployeeNo());
		this.totaVo.putParam("L6R17CommLineCode", mCdEmp.getCommLineCode()); // 業務線代號(35為15日薪)
		this.info("L6R17 moveTotaCdEmp CommLineCode : " + mCdEmp.getCommLineCode());
		this.totaVo.putParam("L6R17CommLineType", mCdEmp.getCommLineType()); // 員工身份別
		this.totaVo.putParam("L6R17RegisterDate", mCdEmp.getRegisterDate()); // 在職/締約日期
		this.totaVo.putParam("L6R17CenterCode", mCdEmp.getCenterCode()); // 單位代號UnitCode
		this.totaVo.putParam("L6R17AgStatusDate", mCdEmp.getAgStatusDate()); // 業務人員任用狀況異動日
		this.totaVo.putParam("L6R17AgentId", mCdEmp.getAgentId()); // 業務人員身份證字號
		this.totaVo.putParam("L6R17AgCurInd", mCdEmp.getAgCurInd()); // 現職指示碼(Y:現職)
		this.totaVo.putParam("L6R17Fullname", mCdEmp.getFullname()); // 姓名
		this.info("L6R17 moveTotaCdEmp Fullname : " + mCdEmp.getFullname());
		this.totaVo.putParam("L6R17QuitDate", mCdEmp.getQuitDate()); // 離職/停約日
		this.totaVo.putParam("L6R17CenterCodeName", mCdEmp.getCenterCodeName()); // 單位名稱UnitItem
		this.totaVo.putParam("L6R17CenterCode2", mCdEmp.getCenterCode2()); // 部室代號DeptCode
		this.totaVo.putParam("L6R17CenterCode2Name", mCdEmp.getCenterCode2Name()); // 部室名稱DeptItem
		this.totaVo.putParam("L6R17AgPost", mCdEmp.getAgPost()); // 職務
		this.totaVo.putParam("L6R17LevelNameChs", mCdEmp.getLevelNameChs()); // 職等中文

	}

}