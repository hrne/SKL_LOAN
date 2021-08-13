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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.domain.Ias39Loss;
import com.st1.itx.db.domain.Ias39LossId;
import com.st1.itx.db.service.Ias39LossService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

@Service("L7204")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L7204 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7204.class);

	/* DB服務注入 */
	@Autowired
	public Ias39LossService sIas39LossService;
	@Autowired
	public FacMainService FacMainService;

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
		this.info("active L7204 ");
		this.totaVo.init(titaVo);

		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String funcd = titaVo.getParam("FuncCode").trim();
		int iCustNo;
		int iFacmNo;
		int iMarkDate;

		// 檢核 [額度主檔(FacMain)]
		for (int i = 1; i <= 10; i++) {			
			// 若該筆無資料就離開迴圈
			if (titaVo.getParam("CustNo" + i).equals("0000000") || titaVo.getParam("CustNo" + i) == null || titaVo.getParam("CustNo" + i).trim().isEmpty()) {
				break;
			}
			FacMain tFacMain = new FacMain();
			FacMainId tFacMainId = new FacMainId();
			tFacMainId.setCustNo(this.parse.stringToInteger(titaVo.getParam("CustNo" + i)));
			tFacMainId.setFacmNo(this.parse.stringToInteger(titaVo.getParam("FacmNo" + i)));
			tFacMain = FacMainService.findById(tFacMainId, titaVo);
			if (tFacMain == null) {
				switch (funcd) {
				case "1":					// 若為新增，但額度資料不存在，拋錯
					throw new LogicException("E0001", "額度主檔(" + titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + ")" );
				case "2":					// 若為修改，但額度資料不存在，拋錯
					throw new LogicException("E0001", "額度主檔(" + titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + ")" );
				default:
					break;
				}
			}
		}
		
		
		// 新增
		if (funcd.equals("1")) {

			for (int i = 1; i <= 10; i++) {

				this.info("L7204 funcd 1 : " + titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + "-" + titaVo.getParam("MarkDate" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("CustNo" + i).equals("0000000") || titaVo.getParam("CustNo" + i) == null || titaVo.getParam("CustNo" + i).trim().isEmpty()) {
					break;
				}

				Ias39Loss tIas39Loss = new Ias39Loss();
				Ias39LossId tIas39LossId = new Ias39LossId();

				tIas39LossId.setCustNo(this.parse.stringToInteger(titaVo.getParam("CustNo" + i)));
				tIas39LossId.setFacmNo(this.parse.stringToInteger(titaVo.getParam("FacmNo" + i)));
				tIas39LossId.setMarkDate(this.parse.stringToInteger(titaVo.getParam("MarkDate" + i)));
				tIas39Loss.setIas39LossId(tIas39LossId);

				tIas39Loss.setCustNo(this.parse.stringToInteger(titaVo.getParam("CustNo" + i)));
				tIas39Loss.setFacmNo(this.parse.stringToInteger(titaVo.getParam("FacmNo" + i)));
				tIas39Loss.setMarkDate(this.parse.stringToInteger(titaVo.getParam("MarkDate" + i)));
				tIas39Loss.setMarkCode(this.parse.stringToInteger(titaVo.getParam("MarkCode" + i)));
				tIas39Loss.setMarkCodeDesc(titaVo.getParam("MarkCodeDesc" + i));
				tIas39Loss.setStartDate(this.parse.stringToInteger(titaVo.getParam("StartDate" + i)));
				tIas39Loss.setEndDate(this.parse.stringToInteger(titaVo.getParam("EndDate" + i)));

				tIas39Loss.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tIas39Loss.setCreateEmpNo(titaVo.getTlrNo());
				tIas39Loss.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tIas39Loss.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					sIas39LossService.insert(tIas39Loss);
				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + "-" +
													titaVo.getParam("MarkDate" + i)); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		} else if (funcd.equals("2")) {

			for (int i = 1; i <= 10; i++) {

				iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo" + i));
				iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo" + i));
				iMarkDate = this.parse.stringToInteger(titaVo.getParam("MarkDate" + i));
				iMarkDate = iMarkDate + 19110000;

				this.info("L7204 funcd 2 : " + titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + "-" + titaVo.getParam("MarkDate" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("CustNo" + i).equals("0000000") || titaVo.getParam("CustNo" + i) == null || titaVo.getParam("CustNo" + i).trim().isEmpty()) {
					break;
				}

				Ias39Loss tIas39Loss = new Ias39Loss();
				tIas39Loss = sIas39LossService.holdById(new Ias39LossId(iCustNo, iFacmNo, iMarkDate));

				if (tIas39Loss == null) {
					throw new LogicException(titaVo, "E0003", titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + "-" +
                            					titaVo.getParam("MarkDate" + i)); // 修改資料不存在
				}

				Ias39Loss tIas39Loss2 = (Ias39Loss) dataLog.clone(tIas39Loss); ////

				tIas39Loss.setCustNo(this.parse.stringToInteger(titaVo.getParam("CustNo" + i)));
				tIas39Loss.setFacmNo(this.parse.stringToInteger(titaVo.getParam("FacmNo" + i)));
				tIas39Loss.setMarkDate(this.parse.stringToInteger(titaVo.getParam("MarkDate" + i)));
				tIas39Loss.setMarkCode(this.parse.stringToInteger(titaVo.getParam("MarkCode" + i)));
				tIas39Loss.setMarkCodeDesc(titaVo.getParam("MarkCodeDesc" + i));
				tIas39Loss.setStartDate(this.parse.stringToInteger(titaVo.getParam("StartDate" + i)));
				tIas39Loss.setEndDate(this.parse.stringToInteger(titaVo.getParam("EndDate" + i)));

				tIas39Loss.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tIas39Loss.setLastUpdateEmpNo(titaVo.getTlrNo());
				try {
					tIas39Loss = sIas39LossService.update2(tIas39Loss); ////
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tIas39Loss2, tIas39Loss); ////
				dataLog.exec(); ////
			}

		} else if (funcd.equals("4")) {

			for (int i = 1; i <= 10; i++) {

				iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo" + i));
				iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo" + i));
				iMarkDate = this.parse.stringToInteger(titaVo.getParam("MarkDate" + i));
				iMarkDate = iMarkDate + 19110000;

				this.info("L7204 funcd 4 : " + titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + "-" + titaVo.getParam("MarkDate" + i));

				// 若該筆無資料就離開迴圈
				if (titaVo.getParam("CustNo" + i).equals("0000000") || titaVo.getParam("CustNo" + i) == null || titaVo.getParam("CustNo" + i).trim().isEmpty()) {
					break;
				}

				Ias39Loss tIas39Loss = new Ias39Loss();
				tIas39Loss = sIas39LossService.holdById(new Ias39LossId(iCustNo, iFacmNo, iMarkDate));

				if (tIas39Loss != null) {
					try {
						sIas39LossService.delete(tIas39Loss);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", titaVo.getParam("CustNo" + i) + "-" + titaVo.getParam("FacmNo" + i) + "-" +
        										titaVo.getParam("MarkDate" + i)); // 刪除資料不存在
				}

			}

		} else if (!(funcd.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L7204"); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}