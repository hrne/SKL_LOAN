package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

@Component("L5406")
@Scope("prototype")

/**
 * 晤談人員資料維護
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5406 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public PfRewardService iPfRewardService;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iFunctionCd = Integer.valueOf(titaVo.getParam("FunctionCd"));
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iInterviewerA = titaVo.getParam("InterviewerA");
		String iInterviewerB = titaVo.getParam("InterviewerB");
		String iChgRsn = titaVo.getParam("ChgRsn");

		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<PfReward> cPfReward = null;
		cPfReward = iPfRewardService.findByCustNoAndFacmNo(iCustNo, iFacmNo, this.index, this.limit, titaVo);
		if (cPfReward == null) {
			throw new LogicException(titaVo, "E0001", "此戶號、額度查無資料"); // 查無資料時
		}

		for (PfReward xPfReward : cPfReward) {
			PfReward hPfReward = new PfReward();
			hPfReward = iPfRewardService.holdById(xPfReward, titaVo);

			switch (iFunctionCd) {
//			case 1: 
//				break;
			case 2:
				PfReward iPfReward = new PfReward();
				iPfReward = (PfReward) dataLog.clone(hPfReward);
				hPfReward.setInterviewerA(iInterviewerA);
				hPfReward.setInterviewerB(iInterviewerB);
				try {
					hPfReward = iPfRewardService.update2(hPfReward, titaVo);
				} catch (DBException e) {
					// TODO Auto-generated catch block
					throw new LogicException("E0005", "更新時發生錯誤");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, iPfReward, hPfReward);
				dataLog.exec(iChgRsn);
				break;
//			case 3:
//				break;
			case 4:
				iPfReward = new PfReward();
				iPfReward = (PfReward) dataLog.clone(hPfReward);
				hPfReward.setInterviewerA("");
				hPfReward.setInterviewerB("");
				try {
					hPfReward = iPfRewardService.update2(hPfReward, titaVo);
				} catch (DBException e) {
					// TODO Auto-generated catch block
					throw new LogicException("E0005", "更新時發生錯誤");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, iPfReward, hPfReward);
				dataLog.exec(iChgRsn);
				break;
//			case 5:
//				break;
			default:
				break;
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
