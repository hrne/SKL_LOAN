package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 CityCode=X,2 AreaCode=X,2 CityItem=X,20 CityShort=X,12
 * AreaItem=X,24 AreaShort=X,16 CityType=X,2 Zip3=X,3 CityGroup=X,1
 * DepartCode=X,6 END=X,1
 */

@Service("L6705")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6705 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6705.class);

	/* DB服務注入 */
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6705 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iCityCode = titaVo.getParam("CityCode");
		String iAreaCode = titaVo.getParam("AreaCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6705"); // 功能選擇錯誤
		}

		// 更新地區別與鄉鎮區對照檔
		CdArea tCdArea = new CdArea();
		CdAreaId tCdAreaId = new CdAreaId();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdArea(tCdArea, tCdAreaId, iFuncCode, iCityCode, iAreaCode, titaVo);
			try {
				this.info("1");
				sCdAreaService.insert(tCdArea, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iCityCode + "-" + iAreaCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdArea = sCdAreaService.holdById(new CdAreaId(iCityCode, iAreaCode));
			if (tCdArea == null) {
				throw new LogicException(titaVo, "E0003", iCityCode + "-" + iAreaCode); // 修改資料不存在
			}
			CdArea tCdArea2 = (CdArea) dataLog.clone(tCdArea); ////
			try {
				moveCdArea(tCdArea, tCdAreaId, iFuncCode, iCityCode, iAreaCode, titaVo);
				tCdArea = sCdAreaService.update2(tCdArea, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdArea2, tCdArea); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdArea = sCdAreaService.holdById(new CdAreaId(iCityCode, iAreaCode));
			if (tCdArea != null) {
				try {
					sCdAreaService.delete(tCdArea);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iCityCode + "-" + iAreaCode); // 刪除資料不存在
			}
			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdArea(CdArea mCdArea, CdAreaId mCdAreaId, int mFuncCode, String mCityCode, String mAreaCode, TitaVo titaVo) throws LogicException {

		mCdAreaId.setCityCode(mCityCode);
		mCdAreaId.setAreaCode(mAreaCode);
		mCdArea.setCdAreaId(mCdAreaId);

		mCdArea.setCityCode(titaVo.getParam("CityCode"));
		mCdArea.setAreaCode(titaVo.getParam("AreaCode"));
		mCdArea.setCityShort(titaVo.getParam("CityShort"));
		mCdArea.setAreaItem(titaVo.getParam("AreaItem"));
		mCdArea.setAreaShort(titaVo.getParam("AreaShort"));
		mCdArea.setCityType(titaVo.getParam("CityType"));
		mCdArea.setZip3(titaVo.getParam("Zip3"));
		mCdArea.setCityGroup(titaVo.getParam("CityGroup"));
		mCdArea.setDepartCode(titaVo.getParam("DepartCode"));

		if (mFuncCode != 2) {
			mCdArea.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdArea.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdArea.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdArea.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
