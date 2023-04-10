package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ConstructionCompany;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ConstructionCompanyService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;


/**
 * L5983 建商名單維護
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L5983")
@Scope("prototype")
public class L5983 extends TradeBuffer {

	@Autowired
	ConstructionCompanyService constructionCompanyService;

	@Autowired
	CustMainService custMainService;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5983 ");
		this.totaVo.init(titaVo);

		String funCd = titaVo.getParam("FunCd");

		switch (funCd) {
		case "1": // 新增
			insertConstructionCompany(titaVo);
			break;
		case "2": // 修改
			modifyConstructionCompany(titaVo);
			break;
		case "4": // 刪除
			deleteConstructionCompany(titaVo);
			break;
		default:
			// TODO: 回傳FunCd錯誤
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 新增資料
	 * 
	 * @param titaVo titaVo @throws LogicException @throws
	 */
	private void insertConstructionCompany(TitaVo titaVo) throws LogicException {
		// 檢查戶號是否存在於客戶主檔
		int custNo = Integer.parseInt(titaVo.getParam("CustNo"));

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);

		if (custMain == null) {
			throw new LogicException("E0005", "欲新增至建商名單檔的戶號(" + custNo + ")不存在於客戶主檔");
		}

		ConstructionCompany constructionCompany = new ConstructionCompany();

		constructionCompany.setCustNo(custNo);

		try {
			constructionCompanyService.insert(constructionCompany, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "建商名單檔");
		}
	}

	/**
	 * 修改資料
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException
	 */
	private void modifyConstructionCompany(TitaVo titaVo) throws LogicException {

		int custNo = Integer.parseInt(titaVo.getParam("CustNo"));

		ConstructionCompany constructionCompany;

		// hold
		try {
			constructionCompany = constructionCompanyService.holdById(custNo, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0006", "建商名單檔");
		}
		if (constructionCompany == null) {
			throw new LogicException("E0003", "建商名單檔");
		}

		ConstructionCompany beforeConstructionCompany = (ConstructionCompany)dataLog.clone(constructionCompany);
		
		// modify
		constructionCompany.setDeleteFlag(titaVo.getParam("DeleteFlag"));

		// TODO: 歷程紀錄

		// update
		try {
			constructionCompanyService.update(constructionCompany, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "建商名單檔");
		}
		dataLog.setEnv(titaVo, beforeConstructionCompany,constructionCompany);
		dataLog.exec("修改建商名單檔");

	}

	/**
	 * 刪除資料
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException
	 */
	private void deleteConstructionCompany(TitaVo titaVo) throws LogicException {

		int custNo = Integer.parseInt(titaVo.getParam("CustNo"));

		ConstructionCompany constructionCompany;

		// hold
		try {
			constructionCompany = constructionCompanyService.holdById(custNo, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0006", "建商名單檔");
		}

		if (constructionCompany == null) {
			throw new LogicException("E0004", "建商名單檔");
		}
		ConstructionCompany beforeConstructionCompany = (ConstructionCompany)dataLog.clone(constructionCompany);

		// delete
		try {
			constructionCompanyService.delete(constructionCompany, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0008", "建商名單檔");
		}
		dataLog.setEnv(titaVo, beforeConstructionCompany,constructionCompany);
		dataLog.exec("刪除建商名單檔");

	}
}