package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryParas;
import com.st1.itx.db.service.MlaundryParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 BusinessType=X,2 Factor1TotLimit=m,14,S Factor2Count=N,4,S
 * Factor2AmtStart=m,14,S Factor2AmtEnd=m,14,S Factor3TotLimit=m,14,S END=X,1
 */

@Service("L8201")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8201 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8201.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryParasService sMlaundryParasService;
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
		this.info("active L8201 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iBusinessType = titaVo.getParam("BusinessType");
		this.info("L8201 iBusinessType : " + iBusinessType);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L8201"); // 功能選擇錯誤
		}

		// 更新疑似洗錢樣態條件設定檔
		MlaundryParas tMlaundryParas = new MlaundryParas();
		switch (iFuncCode) {
		case 1: // 新增
			moveMlaundryParas(tMlaundryParas, iFuncCode, titaVo);
			try {
				sMlaundryParasService.insert(tMlaundryParas);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iBusinessType); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;
		case 2: // 修改
			tMlaundryParas = sMlaundryParasService.holdById(iBusinessType);
			if (tMlaundryParas == null) {
				throw new LogicException(titaVo, "E0003", iBusinessType); // 修改資料不存在
			}
			//刷主管卡後始可刪除
			// 交易需主管核可
			if (!titaVo.getHsupCode().equals("1")) {
				//titaVo.getSupCode();
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
				MlaundryParas tMlaundryParas2 = (MlaundryParas) dataLog.clone(tMlaundryParas); ////
				try {
					moveMlaundryParas(tMlaundryParas, iFuncCode, titaVo);
					tMlaundryParas = sMlaundryParasService.update2(tMlaundryParas);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tMlaundryParas2, tMlaundryParas); ////
				dataLog.exec(); ////
				//放行 1:登 2:放
//				int ActFg=titaVo.getActFgI();
//				if(ActFg==2){
//			}
			
			break;
		case 4: // 刪除
			tMlaundryParas = sMlaundryParasService.holdById(iBusinessType);
			if (tMlaundryParas != null) {
				try {
					sMlaundryParasService.delete(tMlaundryParas);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iBusinessType); // 刪除資料不存在
			}
			break;
		case 5: // inq
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveMlaundryParas(MlaundryParas mMlaundryParas, int mFuncCode, TitaVo titaVo) throws LogicException {

		mMlaundryParas.setFactor1TotLimit(this.parse.stringToBigDecimal(titaVo.getParam("Factor1TotLimit")));
		mMlaundryParas.setFactor2Count(this.parse.stringToInteger(titaVo.getParam("Factor2Count")));
		mMlaundryParas.setFactor2AmtStart(this.parse.stringToBigDecimal(titaVo.getParam("Factor2AmtStart")));
		mMlaundryParas.setFactor2AmtEnd(this.parse.stringToBigDecimal(titaVo.getParam("Factor2AmtEnd")));
		mMlaundryParas.setFactor3TotLimit(this.parse.stringToBigDecimal(titaVo.getParam("Factor3TotLimit")));
		mMlaundryParas.setFactorDays(this.parse.stringToInteger(titaVo.getParam("FactorDays")));
		if (mFuncCode != 2) {
			mMlaundryParas.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mMlaundryParas.setCreateEmpNo(titaVo.getTlrNo());
		}
		mMlaundryParas.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mMlaundryParas.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
