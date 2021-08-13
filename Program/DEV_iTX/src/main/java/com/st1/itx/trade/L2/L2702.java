package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.domain.CustRmkId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustRmkService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2702")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2702 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2702.class);

	/* DB服務注入 */
	@Autowired
	public CustRmkService sCustRmkService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

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
		this.info("active L2702 ");
		this.totaVo.init(titaVo);

		// tita
		// 功能 1新增 2修改 4刪除 5查詢
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 備忘錄序號
		int iRmkNo = parse.stringToInteger(titaVo.getParam("RmkNo"));

		// new table
		CustMain tCustMain = new CustMain();
		CustRmk tCustRmk = new CustRmk();
		// new PK
		CustRmkId custRmkId = new CustRmkId();
		// 塞值到TablePK
		custRmkId.setCustNo(iCustNo);
		custRmkId.setRmkNo(iRmkNo);

		// 新增
		if (iFunCd == 1) {

			// 測試該戶號是否存在客戶主檔
			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);
			this.info("tCustMain = " + tCustMain);
			// 該戶號部存在客戶主檔 拋錯
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0005", "L2702 該戶號" + iCustNo + "不存在客戶主檔。");
			}

			tCustRmk = sCustRmkService.findById(custRmkId);
			// 新增時 該戶號,備忘錄序號查有資料 拋錯
			if (tCustRmk != null) {
				throw new LogicException(titaVo, "E0002", "L2702 該戶號,備忘錄序號" + iCustNo + iRmkNo + "已存在於顧客管控警訊檔。");
			}

			tCustRmk = new CustRmk();

			tCustRmk.setCustRmkId(custRmkId);
			tCustRmk.setCustNo(iCustNo);
			tCustRmk.setRmkNo(iRmkNo);
			tCustRmk.setCustUKey(tCustMain.getCustUKey());
			tCustRmk.setRmkCode(titaVo.getParam("RmkCode"));
			tCustRmk.setRmkDesc(titaVo.getParam("RmkDesc"));
                        tCustRmk.setCreateEmpNo(titaVo.getParam("TlrNo"));
			tCustRmk.setLastUpdateEmpNo(titaVo.getParam("TlrNo"));

			/* 存入DB */
			try {
				sCustRmkService.insert(tCustRmk);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
			// FunCd 2修改
		} else if (iFunCd == 2) {

			tCustRmk = new CustRmk();
			// PK找顧客管控警訊檔HOLD資料
			tCustRmk = sCustRmkService.holdById(custRmkId);

                        if (tCustRmk == null) {
				throw new LogicException(titaVo, "E0003", "L2702 該戶號,備忘錄序號" + iCustNo + iRmkNo + "不存在於顧客管控警訊檔。");
			}
			// 變更前
			CustRmk beforeCustRmk = (CustRmk) dataLog.clone(tCustRmk);

			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo);

			tCustRmk.setCustRmkId(custRmkId);
			tCustRmk.setCustNo(iCustNo);
			tCustRmk.setRmkNo(iRmkNo);
			tCustRmk.setCustUKey(tCustMain.getCustUKey());
			tCustRmk.setRmkCode(titaVo.getParam("RmkCode"));
			tCustRmk.setRmkDesc(titaVo.getParam("RmkDesc"));
			tCustRmk.setLastUpdateEmpNo(titaVo.getParam("TlrNo"));


			try {
				// 修改
				tCustRmk = sCustRmkService.update2(tCustRmk);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeCustRmk, tCustRmk);
			dataLog.exec();

			// FunCd 4刪除
		} else if (iFunCd == 4) {

			tCustRmk = new CustRmk();

			// PK找關係人主檔HOLD資料
			tCustRmk = sCustRmkService.holdById(custRmkId);
			if (tCustRmk == null) {
				throw new LogicException(titaVo, "E0004", "L2702 該戶號,備忘錄序號" + iCustNo + iRmkNo + "不存在於顧客管控警訊檔。");
			}

			try {

				this.info(" L2702 deleteCustRmkLog" + tCustRmk);

				if (tCustRmk != null) {
					sCustRmkService.delete(tCustRmk);
				}
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		} else if (iFunCd == 5) {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}