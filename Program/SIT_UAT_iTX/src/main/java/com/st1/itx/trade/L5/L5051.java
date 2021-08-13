package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* FunctionCd=9,1<br>
* PerfDateFm=9,7<br>
* PerfDateTo=9,7<br>
* CustNo=9,7<br>
* FacmNo=9,3<br>
* END=X,1<br>
*/

@Service("L5051")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5051 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5051.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	private L5051ServiceImpl l5051ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5051 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //查詢方式 1:業績日期;2:戶號
		String PerfDateFm=titaVo.getParam("PerfDateFm").trim(); //業績日期From
		String PerfDateTo=titaVo.getParam("PerfDateTo").trim(); //業績日期To
		String CustNo=titaVo.getParam("CustNo").trim(); //戶號
		String FacmNo=titaVo.getParam("FacmNo").trim(); //額度編號
		
		if(("1").equals(FunctionCd)) {
			if(PerfDateFm!=null && PerfDateTo!=null) {
				if(Integer.parseInt(PerfDateFm)>Integer.parseInt(PerfDateTo)) {
					//E5009	資料檢核錯誤
					throw new LogicException(titaVo, "E5009","業績日期起訖有誤");
				}
			}
		}
		
		String L5051Sql="";
		List<Object> L5051VoList = null;
//		try {
//			L5051Sql=l5051ServiceImpl.FindL5051(FunctionCd,PerfDateFm,PerfDateTo,CustNo,FacmNo);
//		} catch (Exception e) {
//			//E5003 組建SQL語法發生問題
//			this.info("L5051 ErrorForSql="+e);
//			throw new LogicException(titaVo, "E5003","");
//		}
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		//this.limit=Integer.MAX_VALUE;//查全部
		this.limit=40;//查全部
		
		try {
			L5051VoList=l5051ServiceImpl.L5051FindData(this.index,this.limit,titaVo,PerfDateFm,PerfDateTo,CustNo,FacmNo);
		} catch (Exception e) {
			//E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		this.info("L5051 L5051VoList IS NULL=["+(L5051VoList==null)+"],L5051VoListSize=["+L5051VoList.size()+"]");
		
		if(L5051VoList!=null && L5051VoList.size()>=this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			//this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		
		if(L5051VoList!=null && L5051VoList.size()!=0) {
			for(Object ThisObject:L5051VoList) {
				@SuppressWarnings("unchecked")
				Map<String,String> MapL5051=(Map<String, String>) ThisObject;
//				L5051Vo L5051VO=(L5051Vo) l5051ServiceImpl.MapToVO(MapL5051,"L5051",titaVo);
				OccursList occursList = new OccursList();
//				occursList.putParam("OOLastUpdateEmpNo",L5051VO.getLastUpdateEmpNo());//經辦
//				occursList.putParam("OOBsOfficer",L5051VO.getBsOfficer());//房貸專員
//				occursList.putParam("OOBsOfficerName",L5051VO.getBsOfficerName());//房貸專員
//				occursList.putParam("OOCustNm",L5051VO.getCustNm());//戶名
//				occursList.putParam("OOCustNo",L5051VO.getCustNo());//戶號
//				occursList.putParam("OOFacmNo",L5051VO.getFacmNo());//額度編號
//				occursList.putParam("OOBormNo",L5051VO.getBormNo());//撥款序號
//				occursList.putParam("OODrawdownDate",L5051VO.getDrawdownDate());//撥款日
//				occursList.putParam("OOProdCode",L5051VO.getProdCode());//商品代碼
//				occursList.putParam("OOPieceCode",L5051VO.getPieceCode());//計件代碼
//				occursList.putParam("OOCntingCode",L5051VO.getCntingCode());//是否計件
//				occursList.putParam("OODrawdownAmt",L5051VO.getDrawdownAmt());//撥款金額
//				occursList.putParam("OODeptCode",L5051VO.getDeptCode());//單位代號
//				occursList.putParam("OODistCode",L5051VO.getDistCode());//區部代號
//				occursList.putParam("OOUnitCode",L5051VO.getUnitCode());//部室代號
//				occursList.putParam("OODeptCodeX",L5051VO.getDeptCodeX());//單位
//				occursList.putParam("OODistCodeX",L5051VO.getDistCodeX());//區部
//				occursList.putParam("OOUnitCodeX",L5051VO.getUnitCodeX());//部室
//				occursList.putParam("OOEmpNo",L5051VO.getEmpNo());//員工代號
//				occursList.putParam("OOIntroducer",L5051VO.getIntroducer());//介紹人
//				occursList.putParam("OOIntroducerName",L5051VO.getIntroducerName());//介紹人
//				occursList.putParam("OOUnitManager",L5051VO.getUnitManager());//處經理
//				occursList.putParam("OODistManager",L5051VO.getDistManager());//區經理
//				occursList.putParam("OODeptManager",L5051VO.getDeptManager());//部經理
//				occursList.putParam("OOPerfCnt",L5051VO.getPerfCnt());//件數
//				occursList.putParam("OOPerfEqAmt",L5051VO.getPerfEqAmt());//換算業績
//				occursList.putParam("OOPerfReward",L5051VO.getPerfReward());//業務報酬
//				occursList.putParam("OOPerfAmt",L5051VO.getPerfAmt());//業績金額
//				occursList.putParam("OOIntroducerBonus",L5051VO.getIntroducerBonus());//介紹獎金
//				occursList.putParam("OOPerfDate",L5051VO.getPerfDate());//業績日期
				occursList.putParam("OOLastUpdateEmpNo",MapL5051.get("F0"));//經辦
				occursList.putParam("OOBsOfficer",MapL5051.get("F28"));//房貸專員
				occursList.putParam("OOBsOfficerName",MapL5051.get("F1"));//房貸專員
				occursList.putParam("OOCustNm",MapL5051.get("F2"));//戶名
				occursList.putParam("OOCustNo",MapL5051.get("F3"));//戶號
				occursList.putParam("OOFacmNo",MapL5051.get("F4"));//額度編號
				occursList.putParam("OOBormNo",MapL5051.get("F5"));//撥款序號
				occursList.putParam("OODrawdownDate",toRoc(MapL5051.get("F6")));//撥款日
				occursList.putParam("OOProdCode",MapL5051.get("F7"));//商品代碼
				occursList.putParam("OOPieceCode",MapL5051.get("F8"));//計件代碼
				occursList.putParam("OOCntingCode",MapL5051.get("F9"));//是否計件
				occursList.putParam("OODrawdownAmt",MapL5051.get("F10"));//撥款金額
				occursList.putParam("OODeptCode",MapL5051.get("F11"));//單位代號
				occursList.putParam("OODistCode",MapL5051.get("F12"));//區部代號
				occursList.putParam("OOUnitCode",MapL5051.get("F13"));//部室代號
				occursList.putParam("OODeptCodeX",MapL5051.get("F14"));//單位
				occursList.putParam("OODistCodeX",MapL5051.get("F15"));//區部
				occursList.putParam("OOUnitCodeX",MapL5051.get("F16"));//部室
				occursList.putParam("OOEmpNo",MapL5051.get("F17"));//員工代號
				occursList.putParam("OOIntroducer",MapL5051.get("F29"));//介紹人
				occursList.putParam("OOIntroducerName",MapL5051.get("F18"));//介紹人
				occursList.putParam("OOUnitManager",MapL5051.get("F19"));//處經理
				occursList.putParam("OODistManager",MapL5051.get("F20"));//區經理
				occursList.putParam("OODeptManager",MapL5051.get("F21"));//部經理
				occursList.putParam("OOPerfCnt",MapL5051.get("F22"));//件數
				occursList.putParam("OOPerfEqAmt",MapL5051.get("F23"));//換算業績
				occursList.putParam("OOPerfReward",MapL5051.get("F24"));//業務報酬
				occursList.putParam("OOPerfAmt",MapL5051.get("F25"));//業績金額
				occursList.putParam("OOIntroducerBonus",MapL5051.get("F26"));//介紹獎金
				occursList.putParam("OOPerfDate",toRoc(MapL5051.get("F27")));//業績日期
				
				int canModify = 0;
				int RewardDate = Integer.valueOf(MapL5051.get("F30"));
				int MediaDate = Integer.valueOf(MapL5051.get("F31"));
				if (RewardDate > 0 && MediaDate == 0) {
					canModify = 1;
				}
				occursList.putParam("OOCanModify",canModify);//可修改記號
				
				this.totaVo.addOccursList(occursList);
			}
			
		}else {
			if(this.index!=0) {
				//代表有多筆查詢,然後筆數剛好可以被整除
				
			}else {
				// E2003 查無資料
				throw new LogicException(titaVo, "E2003", "");
			}
		}

		/*
		#OOUnitItem
		#OOBsOfficer
		#OOCustNm
		#OOCustNo
		#OOFacmNo
		#OOBormNo
		#OODrawdownDate
		#OOProdCode
		#OOPieceCode
		#OOCntingCode
		#OODrawdownAmt
		#OODeptCode
		#OODistCode
		#OOUnitCode
		#OODeptCodeX
		#OODistCodeX
		#OOUnitCodeX
		#OOEmpNo
		#OOIntroducer
		#OOUnitManager
		#OODistManager
		#OODeptManager
		#OOPerfCnt
		#OOPerfEqAmt
		#OOPerfReward
		#OOPerfAmt
		#OOIntroducerBonus
		#OOPerfDate
		*/
		this.addList(this.totaVo);
		return this.sendList();
	}
	public Integer toRoc(String iDate) {
		int date = 0;
		if ("".equals(iDate) || iDate == null) {
			date = 0;
			
		} else {
		    date = Integer.valueOf(iDate);
			if (date > 19110000) {
				date -= 19110000;
			}
		}
		return date;
	}

}