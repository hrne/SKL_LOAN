package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAoDept;
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 #loop{times:20,i:1} EmployeeNo=X,6 DeptCode=X,6 #end
 * END=X,1
 */

@Service("L6753")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6753 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdAoDeptService sCdAoDeptService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6753 ");
		this.totaVo.init(titaVo);

		// 功能 1:新增 2:修改 4:刪除
		String funcd = titaVo.getParam("FuncCode").trim();
		String EmployeeNo;

		// 新增
		if (funcd.equals("1")) {

			for (int i = 1; i <= 20; i++) {

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("EmployeeNo" + i) == null || titaVo.getParam("EmployeeNo" + i).trim().isEmpty()) {
					break;
				}

				CdAoDept tCdAoDept = new CdAoDept();
				tCdAoDept.setEmployeeNo(titaVo.getParam("EmployeeNo" + i));
				tCdAoDept.setDeptCode(titaVo.getParam("DeptCode" + i));
				tCdAoDept.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdAoDept.setCreateEmpNo(titaVo.getTlrNo());
				tCdAoDept.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdAoDept.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					sCdAoDeptService.insert(tCdAoDept, titaVo);
				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		} else if (funcd.equals("2")) {

			for (int i = 1; i <= 20; i++) {

				EmployeeNo = titaVo.getParam("EmployeeNo" + i);

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("EmployeeNo" + i) == null || titaVo.getParam("EmployeeNo" + i).trim().isEmpty()) {
					break;
				}

				CdAoDept tCdAoDept = new CdAoDept();
				tCdAoDept = sCdAoDeptService.holdById(EmployeeNo);
				if (tCdAoDept == null) {
					throw new LogicException(titaVo, "E0003", EmployeeNo); // 修改資料不存在
				}

				CdAoDept tCdAoDept2 = (CdAoDept) dataLog.clone(tCdAoDept); ////
				try {
					tCdAoDept.setEmployeeNo(titaVo.getParam("EmployeeNo" + i));
					tCdAoDept.setDeptCode(titaVo.getParam("DeptCode" + i));
					tCdAoDept.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
					tCdAoDept.setLastUpdateEmpNo(titaVo.getTlrNo());

					tCdAoDept = sCdAoDeptService.update2(tCdAoDept, titaVo); ////
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tCdAoDept2, tCdAoDept); ////
				dataLog.exec("修改房貸專員所屬業務部室"); ////
			}

		} else if (funcd.equals("4")) {

			for (int i = 1; i <= 20; i++) {

				EmployeeNo = titaVo.getParam("EmployeeNo" + i);

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("EmployeeNo" + i) == null || titaVo.getParam("EmployeeNo" + i).trim().isEmpty()) {
					break;
				}

				CdAoDept tCdAoDept = new CdAoDept();
				tCdAoDept = sCdAoDeptService.holdById(EmployeeNo);
				if (tCdAoDept != null) {
					try {
						sCdAoDeptService.delete(tCdAoDept);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", EmployeeNo); // 刪除資料不存在
				}

			}

		} else if (!(funcd.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L6753"); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}