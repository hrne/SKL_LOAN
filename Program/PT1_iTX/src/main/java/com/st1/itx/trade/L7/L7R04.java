package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.domain.Ias39Loss;
import com.st1.itx.db.domain.Ias39LossId;
import com.st1.itx.db.service.Ias39LossService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.parse.Parse;

/*
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimMarkDate=9,8
 * RimFunCd=9,1
 * */

@Service("L7R04")
@Scope("prototype")
public class L7R04 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public Ias39LossService Ias39LossService;
	@Autowired
	public FacMainService FacMainService;

	@Autowired
	public Parse parse;

	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7R04 ");
		this.totaVo.init(titaVo);

		// RimCustNo=9,7
		int iCustNo = parse.stringToInteger(titaVo.get("RimCustNo").trim());

		// RimFacmNo=9,3
		int iFacmNo = parse.stringToInteger(titaVo.get("RimFacmNo").trim());

		// RimMarkDate=9,8
		this.info("*** L7R04 RimMarkDate=" + parse.stringToInteger(titaVo.get("RimMarkDate").trim()));
		int iMarkDate = parse.stringToInteger(titaVo.get("RimMarkDate").trim()) + 19110000;

		// RimFunCd=9,1
		String funcd = titaVo.get("RimFunCd");

		// 檢核 [額度主檔(FacMain)]
		FacMainId iFacMainId = new FacMainId();
		iFacMainId.setCustNo(iCustNo);
		iFacMainId.setFacmNo(iFacmNo);

		FacMain tFacMain = new FacMain();

		/* DB服務 */
		if (iCustNo > 0) {
			tFacMain = FacMainService.findById(iFacMainId, titaVo);
		} else {
			throw new LogicException("E0010", "額度主檔");
		}

		// 邏輯錯誤處理
		if (tFacMain == null) {
			switch (funcd) {
			case "1":
				// 若為新增，但額度資料不存在，拋錯
				throw new LogicException("E0001", "額度主檔");
			case "2":
				// 若為修改，但額度資料不存在，拋錯
				throw new LogicException("E0001", "額度主檔");
			default:
				break;
			}
		}

		// 檢核 [特殊客觀減損狀況檔(Ias39Loss)]
		Ias39LossId iIas39LossId = new Ias39LossId();
		iIas39LossId.setCustNo(iCustNo);
		iIas39LossId.setFacmNo(iFacmNo);
		iIas39LossId.setMarkDate(iMarkDate);

		Ias39Loss tIas39Loss = new Ias39Loss();

		/* DB服務 */
		if (iCustNo > 0) {
			tIas39Loss = Ias39LossService.findById(iIas39LossId, titaVo);
		} else {
			throw new LogicException("E0010", "特殊客觀減損狀況檔");
		}

		// 邏輯錯誤處理
		if (funcd.equals("1") && tIas39Loss != null) {
			// 若為新增，但資料已存在，拋錯
			throw new LogicException("E0002", "特殊客觀減損狀況檔");
		} else if (tIas39Loss == null) {
			switch (funcd) {
			case "1":
				// 若為新增且資料不存在，存空值到totaVo
				tIas39Loss = new Ias39Loss();
				break;
			case "2":
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "特殊客觀減損狀況檔");
//			case "3":
//				// 若為拷貝，但資料不存在，拋錯
//				throw new LogicException("E0002", "特殊客觀減損狀況檔");
			case "4":
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "特殊客觀減損狀況檔");
//			case "5":
//				// 若為查詢，但資料不存在，拋錯
//				throw new LogicException("E0001", "特殊客觀減損狀況檔");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "特殊客觀減損狀況檔");
			}
		}

		/* 存入Tota */
		/* key 名稱需與L7R04.tom相同 */
		this.totaVo.putParam("L7R04CustNo", tIas39Loss.getCustNo());
		this.totaVo.putParam("L7R04FacmNo", tIas39Loss.getFacmNo());
		if (tIas39Loss.getMarkDate() == 0) {
			this.totaVo.putParam("L7R04MarkDate", "");
		} else {
			this.totaVo.putParam("L7R04MarkDate", tIas39Loss.getMarkDate());
		}
		this.totaVo.putParam("L7R04MarkCode", tIas39Loss.getMarkCode());
		this.totaVo.putParam("L7R04MarkRsn", tIas39Loss.getMarkRsn());
		this.totaVo.putParam("L7R04MarkRsnDesc", tIas39Loss.getMarkRsnDesc());
		this.totaVo.putParam("L7R04LosCod", tIas39Loss.getLosCod());
		if (tIas39Loss.getStartDate() == 0) {
			this.totaVo.putParam("L7R04StartDate", "");
		} else {
			this.totaVo.putParam("L7R04StartDate", tIas39Loss.getStartDate());
		}
		if (tIas39Loss.getEndDate() == 0) {
			this.totaVo.putParam("L7R04EndDate", "");
		} else {
			this.totaVo.putParam("L7R04EndDate", tIas39Loss.getEndDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}