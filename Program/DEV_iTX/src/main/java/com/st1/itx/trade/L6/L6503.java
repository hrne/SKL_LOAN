package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.tradeService.TradeBuffer;



@Service("L6503")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L6503 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6503.class);

	/* 轉型共用工具 */
	
	@Autowired
	public CdPfParmsService iCdPfParmsService;
	
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		
		this.info("active L6503 ");
		this.totaVo.init(titaVo);
		//先刪除		
		Slice<CdPfParms> dCdPfParms = null;
		dCdPfParms = iCdPfParmsService.findAll(0, Integer.MAX_VALUE, titaVo);	
		if (dCdPfParms !=null) {
			for (CdPfParms xCdPfParms:dCdPfParms) {
				try {
					iCdPfParmsService.delete(xCdPfParms, titaVo);
				}catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}
		
		//新增
		CdPfParms iCdPfParms = new CdPfParms();
		CdPfParmsId iCdPfParmsId = new CdPfParmsId();
		//排除商品別-全部業績 -conditioncode1 = 1;conditioncode2 = input;condition= input
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("1");
		iCdPfParmsId.setConditionCode2("1");
		ArrayList<String> iProdNoListA = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iProdNoA = titaVo.getParam("ProdNoA"+c);
			if (iProdNoA.equals("")) {
				continue;
			}
			if (iProdNoListA.contains(iProdNoA)) {
				continue;
			}else {
				iProdNoListA.add(iProdNoA);
			}
			iCdPfParmsId.setCondition(iProdNoA);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS1")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE1")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-全部業績");
			}
		}
		//排除商品別-換算業績
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("1");
		iCdPfParmsId.setConditionCode2("2");
		ArrayList<String> iProdNoListB = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iProdNoB = titaVo.getParam("ProdNoB"+c);
			if (iProdNoB.equals("")) {
				continue;
			}
			if (iProdNoListB.contains(iProdNoB)) {
				continue;
			}else {
				iProdNoListB.add(iProdNoB);
			}
			iCdPfParmsId.setCondition(iProdNoB);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS2")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE2")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-換算業績");
			}
		}
		//排除商品別-介紹獎金
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("1");
		iCdPfParmsId.setConditionCode2("3");
		ArrayList<String> iProdNoListC = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iProdNoC = titaVo.getParam("ProdNoC"+c);
			if (iProdNoC.equals("")) {
				continue;
			}
			if (iProdNoListC.contains(iProdNoC)) {
				continue;
			}else {
				iProdNoListC.add(iProdNoC);
			}
			iCdPfParmsId.setCondition(iProdNoC);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS3")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE3")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-介紹獎金");
			}
		}
		//排除商品別-加碼獎勵津貼
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("1");
		iCdPfParmsId.setConditionCode2("4");
		ArrayList<String> iProdNoListD = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iProdNoD = titaVo.getParam("ProdNoD"+c);
			if (iProdNoD.equals("")) {
				continue;
			}
			if (iProdNoListD.contains(iProdNoD)) {
				continue;
			}else {
				iProdNoListD.add(iProdNoD);
			}
			iCdPfParmsId.setCondition(iProdNoD);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS4")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE4")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-加碼獎勵津貼");
			}
		}
		//排除商品別-協辦獎金
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("1");
		iCdPfParmsId.setConditionCode2("5");
		ArrayList<String> iProdNoListE = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iProdNoE = titaVo.getParam("ProdNoE"+c);
			if (iProdNoE.equals("")) {
				continue;
			}
			if (iProdNoListE.contains(iProdNoE)) {
				continue;
			}else {
				iProdNoListE.add(iProdNoE);
			}
			iCdPfParmsId.setCondition(iProdNoE);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS5")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE5")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-協辦獎金");
			}
		}
		
		//排除部門別-全部業績 -conditioncode1 = 2;conditioncode2 = input;condition= input
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("2");
		iCdPfParmsId.setConditionCode2("1");
		ArrayList<String> iDeptCodeListF = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iDeptCodeF = titaVo.getParam("DeptCodeA"+c);
			if (iDeptCodeF.equals("")) {
				continue;
			}
			if (iDeptCodeListF.contains(iDeptCodeF)) {
				continue;
			}else {
				iDeptCodeListF.add(iDeptCodeF);
			}
			iCdPfParmsId.setCondition(iDeptCodeF);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS6")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE6")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-全部業績");
			}
		}
		//排除部門別-換算業績
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("2");
		iCdPfParmsId.setConditionCode2("2");
		ArrayList<String> iDeptCodeListG = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iDeptCodeG = titaVo.getParam("DeptCodeB"+c);
			if (iDeptCodeG.equals("")) {
				continue;
			}
			if (iDeptCodeListG.contains(iDeptCodeG)) {
				continue;
			}else {
				iDeptCodeListG.add(iDeptCodeG);
			}
			iCdPfParmsId.setCondition(iDeptCodeG);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS7")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE7")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-換算業績");
			}
		}
		//排除部門別-介紹獎金
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("2");
		iCdPfParmsId.setConditionCode2("3");
		ArrayList<String> iDeptCodeListH = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iDeptCodeH = titaVo.getParam("DeptCodeC"+c);
			if (iDeptCodeH.equals("")) {
				continue;
			}
			if (iDeptCodeListH.contains(iDeptCodeH)) {
				continue;
			}else {
				iDeptCodeListH.add(iDeptCodeH);
			}
			iCdPfParmsId.setCondition(iDeptCodeH);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS8")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE8")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-介紹獎金");
			}
		}
		//排除部門別-加碼獎勵津貼
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("2");
		iCdPfParmsId.setConditionCode2("4");
		ArrayList<String> iDeptCodeListI = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iDeptCodeI = titaVo.getParam("DeptCodeC"+c);
			if (iDeptCodeI.equals("")) {
				continue;
			}
			if (iDeptCodeListI.contains(iDeptCodeI)) {
				continue;
			}else {
				iDeptCodeListI.add(iDeptCodeI);
			}
			iCdPfParmsId.setCondition(iDeptCodeI);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS9")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE9")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-加碼獎勵津貼");
			}
		}
		//排除部門別-協辦獎金
		iCdPfParms = new CdPfParms();
		iCdPfParmsId = new CdPfParmsId();
		iCdPfParmsId.setConditionCode1("2");
		iCdPfParmsId.setConditionCode2("5");
		ArrayList<String> iDeptCodeListJ = new ArrayList<>();
		for (int c = 1 ; c<=30 ;c++) {
			String iDeptCodeJ = titaVo.getParam("DeptCodeC"+c);
			if (iDeptCodeJ.equals("")) {
				continue;
			}
			if (iDeptCodeListJ.contains(iDeptCodeJ)) {
				continue;
			}else {
				iDeptCodeListJ.add(iDeptCodeJ);
			}
			iCdPfParmsId.setCondition(iDeptCodeJ);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS10")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE10")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "排除商品別-協辦獎金");
			}
		}
		
		//是否排除 -conditioncode1 = 3;conditioncode2 = input;condition= input
		int iMonth = 11;
		for (int b=1 ;b<=5;b++) {
			iCdPfParms = new CdPfParms();
			iCdPfParmsId = new CdPfParmsId();
			iCdPfParmsId.setConditionCode1("3");
			iCdPfParmsId.setConditionCode2(String.valueOf(b));
			String iYesNo = titaVo.getParam("YesNo"+b);
			if (iYesNo.equals("")) {
				continue;
			}
			iCdPfParmsId.setCondition(iYesNo);
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS"+iMonth)));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE"+iMonth)));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "15日薪非業績人員");
			}
			iMonth++;
		}


		//員工代號 -conditioncode1 = 4;conditioncode2 = 0;condition= 員工編號
		ArrayList<String> iEmpNoList = new ArrayList<>();
		for (int a = 1;a<=30;a++) {
			if (iEmpNoList.contains(titaVo.getParam("EmpNo"+a))) {
				continue;
			}else {
				iEmpNoList.add(titaVo.getParam("EmpNo"+a));
			}
			if (titaVo.getParam("EmpNo"+a).equals("")) {
				continue;
			}
			iCdPfParms = new CdPfParms();
			iCdPfParmsId = new CdPfParmsId();
			iCdPfParmsId.setConditionCode1("4");
			iCdPfParmsId.setConditionCode2("0");	
			iCdPfParmsId.setCondition(titaVo.getParam("EmpNo"+a));
			iCdPfParms.setCdPfParmsId(iCdPfParmsId);
			iCdPfParms.setWorkMonthStart(Integer.valueOf(titaVo.getParam("WorkMonthS16")));
			iCdPfParms.setWorkMonthEnd(Integer.valueOf(titaVo.getParam("WorkMonthE16")));
			try {
				iCdPfParmsService.insert(iCdPfParms,titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工代號");
			}
		}


		this.addList(this.totaVo);
		return this.sendList();
	}
}