package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonus;
import com.st1.itx.db.domain.CdBonusId;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita FuncCode=9,1 Year=9,3 Month=9,2 #loop{times:20,i:0} PieceCode{i}=X,1
 * ProdNo{i}=X,5 AmtStartRange{i}=m,14 AmtEndRange{i}=m,14 Bonus{i}=m,14 #end
 * END=X,1
 */

@Service("L6751")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6751 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBonusService sCdBonusService;
	
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;
	
	@Autowired
	public CdPfParmsService iCdPfParmsService;
	
	@Autowired
	SendRsp sendRsp;
	

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private boolean duringWorkMonth = false;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6751 ");
		this.totaVo.init(titaVo);

		String iFuncCode = titaVo.getParam("FuncCode");
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		
		int AcDate = titaVo.getEntDyI()+19110000;
		int iWoarkMonthAcDAte = 0;
		
		CdWorkMonth tWorkMonth = iCdWorkMonthService.findDateFirst(AcDate, AcDate, titaVo);
		this.info("tWorkMonth=="+tWorkMonth);
		if(tWorkMonth!=null) {
			iWoarkMonthAcDAte = Integer.parseInt(String.valueOf(tWorkMonth.getYear())+parse.IntegerToString(tWorkMonth.getMonth(), 2));
			this.info("iWoarkMonthAcDAte=="+iWoarkMonthAcDAte);
			if(iWorkMonth<=iWoarkMonthAcDAte) {
				duringWorkMonth = true;
			}
		}
		
		
		
		

		// 新增 - 每個條件都建一筆
		if (iFuncCode.equals("1") || iFuncCode.equals("3")) {

			// 每個條件都建一筆
			moveCdBonus(iFuncCode, iWorkMonth, titaVo);

		} else if (iFuncCode.equals("2")) { // 修改 - 因為修改前後筆數不固定 , 所以先全部刪除再建立

			// 先全部刪除
			Slice<CdBonus> slCdBonus;
			slCdBonus = sCdBonusService.findYearMonth(iWorkMonth, iWorkMonth, this.index, Integer.MAX_VALUE);
			List<CdBonus> lCdBonus = slCdBonus == null ? null : slCdBonus.getContent();

			if (lCdBonus == null || lCdBonus.size() == 0) {
				throw new LogicException(titaVo, "E0001", "介紹人加碼獎勵津貼標準設定檔"); // 查無資料
			}

			// 如有找到資料
			for (CdBonus tCdBonus : lCdBonus) {

				CdBonus dCdBonus = new CdBonus();
				dCdBonus = sCdBonusService.holdById(new CdBonusId(iWorkMonth, tCdBonus.getConditionCode(), tCdBonus.getCondition()));

				if (dCdBonus != null) {
					try {
						sCdBonusService.delete(dCdBonus);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", tCdBonus.getCondition()); // 刪除資料不存在
				}

			}

			// 每個條件都建一筆
			moveCdBonus(iFuncCode, iWorkMonth, titaVo);

		} else if (iFuncCode.equals("4")) { // 刪除 - 每個條件一起刪除

			Slice<CdBonus> slCdBonus;
			slCdBonus = sCdBonusService.findYearMonth(iWorkMonth, iWorkMonth, this.index, Integer.MAX_VALUE);
			List<CdBonus> lCdBonus = slCdBonus == null ? null : slCdBonus.getContent();

			if (lCdBonus == null || lCdBonus.size() == 0) {
				throw new LogicException(titaVo, "E0001", "介紹人加碼獎勵津貼標準設定檔"); // 查無資料
			}

			// 如有找到資料
			for (CdBonus tCdBonus : lCdBonus) {

				CdBonus dCdBonus = new CdBonus();
				dCdBonus = sCdBonusService.holdById(new CdBonusId(iWorkMonth, tCdBonus.getConditionCode(), tCdBonus.getCondition()));

				if (dCdBonus != null) {
					try {
						sCdBonusService.delete(dCdBonus);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", tCdBonus.getCondition()); // 刪除資料不存在
				}

			}

		} else if (!(iFuncCode.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L6751"); // 功能選擇錯誤
		}
		
		if(("1").equals(iFuncCode) || ("2").equals(iFuncCode) || ("4").equals(iFuncCode)) {
			
			if(duringWorkMonth==true) {
				this.info("duringWorkMonth True");
				
				CdPfParms tCdPfParm = new CdPfParms();
				CdPfParmsId tCdPfParmId = new CdPfParmsId();
				
				
				CdPfParms sCdPfParm = iCdPfParmsService.findById(new CdPfParmsId("R"," "," "),titaVo);
				this.info("sCdPfParm=="+sCdPfParm);
				if(sCdPfParm == null) {
					//業績重算 設條件記號1=R 有效工作月起 其餘為空白 OR 0
					tCdPfParmId.setConditionCode1("R");
					tCdPfParmId.setConditionCode2(" ");
					tCdPfParmId.setCondition(" ");
					tCdPfParm.setWorkMonthStart(iWorkMonth);
					tCdPfParm.setWorkMonthEnd(0);
					tCdPfParm.setCdPfParmsId(tCdPfParmId);
					
					try {
						iCdPfParmsService.insert(tCdPfParm, titaVo);
					} catch(DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
					}
					
				} else {
					tCdPfParm = iCdPfParmsService.holdById(new CdPfParmsId("R"," "," "),titaVo);
					tCdPfParm.setWorkMonthStart(iWorkMonth);
					tCdPfParm.setWorkMonthEnd(0);
					try {
						iCdPfParmsService.update(tCdPfParm, titaVo);
					} catch(DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
					}
					
				}
				//主管授權
				if (!titaVo.getHsupCode().equals("1")) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
				}
				
			}
			
			
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdBonus(String mFuncCode, int mWorkMonth, TitaVo titaVo) throws LogicException {

		String seq;
		String pieceCode;
		BigDecimal amtStartRange;
		BigDecimal amtEndRange;
		BigDecimal bonus;
		int i = 0;

		// 條件記號=1 (篩選條件-計件代碼)
		for (int j = 0; j <= 19; j++) {

			pieceCode = titaVo.getParam("PieceCode" + j);
			this.info("L6751 pieceCode : " + j + "-" + mWorkMonth + "-" + pieceCode);

			if (pieceCode.isEmpty()) {
				break;
			}
			movepieceCode(mWorkMonth, pieceCode, titaVo);

		} // end j

		// 條件記號=3 (金額級距)
		for (int l = 0; l <= 19; l++) {

			i++;
			seq = i + "";
			amtStartRange = this.parse.stringToBigDecimal(titaVo.getParam("AmtStartRange" + l));
			amtEndRange = this.parse.stringToBigDecimal(titaVo.getParam("AmtEndRange" + l));
			bonus = this.parse.stringToBigDecimal(titaVo.getParam("Bonus" + l));

			this.info("L6751 amtStartRange : " + l + "-" + mWorkMonth + "-" + amtStartRange);

			if (amtStartRange.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}

			CdBonus tCdBonus = new CdBonus();
			CdBonusId tCdBonusId = new CdBonusId();
			tCdBonusId.setWorkMonth(mWorkMonth);
			tCdBonusId.setConditionCode(3);
			tCdBonusId.setCondition(seq); // 流水序號處理唯一
			tCdBonus.setCdBonusId(tCdBonusId);

			tCdBonus.setAmtStartRange(amtStartRange);
			tCdBonus.setAmtEndRange(amtEndRange);
			tCdBonus.setBonus(bonus);

			tCdBonus.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
			tCdBonus.setCreateEmpNo(titaVo.getTlrNo());
			tCdBonus.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
			tCdBonus.setLastUpdateEmpNo(titaVo.getTlrNo());

			try {
				sCdBonusService.insert(tCdBonus);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}

		} // end l

	}

	// 條件記號=1 (篩選條件-計件代碼)
	private void movepieceCode(int aWorkMonth, String apieceCode, TitaVo titaVo) throws LogicException {

		CdBonus tCdBonus = new CdBonus();
		CdBonusId tCdBonusId = new CdBonusId();
		tCdBonusId.setWorkMonth(aWorkMonth);
		tCdBonusId.setConditionCode(1);
		tCdBonusId.setCondition(apieceCode);
		tCdBonus.setCdBonusId(tCdBonusId);

		tCdBonus.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
		tCdBonus.setCreateEmpNo(titaVo.getTlrNo());
		tCdBonus.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
		tCdBonus.setLastUpdateEmpNo(titaVo.getTlrNo());

		try {
			sCdBonusService.insert(tCdBonus);
		} catch (DBException e) {
			if (e.getErrorId() == 2) {
				throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
			} else {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

}