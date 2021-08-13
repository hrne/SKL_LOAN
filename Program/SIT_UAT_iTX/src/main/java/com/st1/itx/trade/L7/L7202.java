package com.st1.itx.trade.L7;

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
import com.st1.itx.db.domain.Ias39LGD;
import com.st1.itx.db.domain.Ias39LGDId;
import com.st1.itx.db.service.Ias39LGDService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 #loop{times:100,i:1} Date=9,7 Type=X,2 TypeDesc=X,20
 * LGDPercent=9,2.5 Enable=X,1 #end END=X,1
 */

@Service("L7202")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L7202 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7202.class);

	/* DB服務注入 */
	@Autowired
	public Ias39LGDService sIas39LGDService;

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
		this.info("active L7202 ");
		this.totaVo.init(titaVo);

		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String funcd = titaVo.getParam("FuncCode").trim();
		String iType;
		int iDate;

		// 新增
		if (funcd.equals("1")) {

			for (int i = 1; i <= 100; i++) {

				this.info("L7202 Date 1 : " + titaVo.getParam("Date" + i) + "-" + titaVo.getParam("Type" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("Date" + i).equals("0000000") || titaVo.getParam("Date" + i) == null || titaVo.getParam("Date" + i).trim().isEmpty()) {
					break;
				}

				Ias39LGD tIas39LGD = new Ias39LGD();
				Ias39LGDId tIas39LGDId = new Ias39LGDId();

				tIas39LGDId.setDate(this.parse.stringToInteger(titaVo.getParam("Date" + i)));
				tIas39LGDId.setType(titaVo.getParam("Type" + i));
				tIas39LGD.setIas39LGDId(tIas39LGDId);

				tIas39LGD.setDate(this.parse.stringToInteger(titaVo.getParam("Date" + i)));
				tIas39LGD.setType(titaVo.getParam("Type" + i));
				tIas39LGD.setTypeDesc(titaVo.getParam("TypeDesc" + i));
				tIas39LGD.setLGDPercent(this.parse.stringToBigDecimal(titaVo.getParam("LGDPercent" + i)));
				tIas39LGD.setEnable(titaVo.getParam("Enable" + i));

				tIas39LGD.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tIas39LGD.setCreateEmpNo(titaVo.getTlrNo());
				tIas39LGD.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tIas39LGD.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					sIas39LGDService.insert(tIas39LGD);
				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", titaVo.getParam("Date" + i) + "-" + titaVo.getParam("Type" + i) ); // E0002 新增資料已存在(XXX)
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		} else if (funcd.equals("2")) {

			for (int i = 1; i <= 100; i++) {

				iDate = this.parse.stringToInteger(titaVo.getParam("Date" + i));
				iDate = iDate + 19110000;
				iType = titaVo.getParam("Type" + i);

				this.info("L7202 Date 2 : " + titaVo.getParam("Date" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("Date" + i).equals("0000000") || titaVo.getParam("Date" + i) == null || titaVo.getParam("Date" + i).trim().isEmpty()) {
					break;
				}

				Ias39LGD tIas39LGD = new Ias39LGD();
				tIas39LGD = sIas39LGDService.holdById(new Ias39LGDId(iDate, iType));

				if (tIas39LGD == null) {
					throw new LogicException(titaVo, "E0003", titaVo.getParam("Date" + i) + "-" + titaVo.getParam("Type" + i) ); // E0003 修改資料不存在(XXX)
				}

				Ias39LGD tIas39LGD2 = (Ias39LGD) dataLog.clone(tIas39LGD); ////

				tIas39LGD.setDate(this.parse.stringToInteger(titaVo.getParam("Date" + i)));
				tIas39LGD.setType(titaVo.getParam("Type" + i));
				tIas39LGD.setTypeDesc(titaVo.getParam("TypeDesc" + i));
				tIas39LGD.setLGDPercent(this.parse.stringToBigDecimal(titaVo.getParam("LGDPercent" + i)));
				tIas39LGD.setEnable(titaVo.getParam("Enable" + i));
				tIas39LGD.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tIas39LGD.setLastUpdateEmpNo(titaVo.getTlrNo());
				try {
					tIas39LGD = sIas39LGDService.update2(tIas39LGD); ////
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tIas39LGD2, tIas39LGD); ////
				dataLog.exec(); ////
			}

		} else if (funcd.equals("4")) {

			for (int i = 1; i <= 100; i++) {

				iDate = this.parse.stringToInteger(titaVo.getParam("Date" + i));
				iDate = iDate + 19110000;
				iType = titaVo.getParam("Type" + i);

				this.info("L7202 Date 4 : " + titaVo.getParam("Date" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("Date" + i).equals("0000000") || titaVo.getParam("Date" + i) == null || titaVo.getParam("Date" + i).trim().isEmpty()) {
					break;
				}

				Ias39LGD tIas39LGD = new Ias39LGD();
				tIas39LGD = sIas39LGDService.holdById(new Ias39LGDId(iDate, iType));

				if (tIas39LGD != null) {
					try {
						sIas39LGDService.delete(tIas39LGD);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", titaVo.getParam("Date" + i) + "-" + titaVo.getParam("Type" + i) ); // E0004 刪除資料不存在(XXX)
				}

			}

		} else if (!(funcd.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L7202"); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}