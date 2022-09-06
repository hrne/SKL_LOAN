package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.common.SendRsp;

/**
 * Tita FuncCode=9,1 BusinessType=X,2 JcicEmpName=x,8 JcicEmpTel=x,16 END=X,1
 */

@Service("L8501")
@Scope("prototype")
/**
 *
 *
 * @author St1
 * @version 1.0.0
 */
public class L8501 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public SystemParasService sSystemParasService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8501 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iBusinessType = titaVo.getParam("BusinessType");
		this.info("L8501 iBusinessType : " + iBusinessType);

		// 檢查輸入資料
		if (iFuncCode == 2) {
		} else {
			throw new LogicException(titaVo, "E0010", "L8501"); // 功能選擇錯誤-只可修改
		}

		// 更新系統參數設定檔
		SystemParas tSystemParas = new SystemParas();
		switch (iFuncCode) {
		case 2: // 修改
			tSystemParas = sSystemParasService.holdById(iBusinessType);
			if (tSystemParas == null) {
				throw new LogicException(titaVo, "E0003", iBusinessType); // 修改資料不存在
			}
			SystemParas tSystemParas2 = (SystemParas) dataLog.clone(tSystemParas); ////
			try {
				moveSystemParas(tSystemParas, iFuncCode, titaVo);
				tSystemParas = sSystemParasService.update2(tSystemParas, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tSystemParas2, tSystemParas); ////
			dataLog.exec("修改系統變數級參數設定值-JCIC放款報送人員維護"); ////
			break;

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveSystemParas(SystemParas mSystemParas, int mFuncCode, TitaVo titaVo) throws LogicException {

		mSystemParas.setJcicEmpName(titaVo.getParam("JcicEmpName"));
		mSystemParas.setJcicEmpTel(titaVo.getParam("JcicEmpTel"));

		mSystemParas.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mSystemParas.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
