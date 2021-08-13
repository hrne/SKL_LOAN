package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.L5052Vo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* FunctionCd=9,1<br>
* PerfDateFm=9,7<br>
* PerfDateTo=9,7<br>
* CustNo=9,7<br>
* FacmNo=9,3<br>
*/

@Service("L5052")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5052 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5052.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public L5051ServiceImpl l5051ServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5052 ");
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
		
		String L5052Sql="";
		List<Object> L5052VoList = null;
		try {
			L5052Sql=l5051ServiceImpl.FindL5052(FunctionCd,PerfDateFm,PerfDateTo,CustNo,FacmNo);
		} catch (Exception e) {
			//E5003 組建SQL語法發生問題
			this.info("L5051 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5003","");
		}
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		//this.limit=Integer.MAX_VALUE;//查全部
		this.limit=40;//查全部
		try {
			L5052VoList=l5051ServiceImpl.FindData(this.index,this.limit,L5052Sql,titaVo,FunctionCd,PerfDateFm,PerfDateTo,CustNo,FacmNo);
		} catch (Exception e) {
			//E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		
		if(L5052VoList!=null && L5052VoList.size()>=this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			//this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		
		if(L5052VoList!=null && L5052VoList.size()!=0) {
			for(Object ThisObject:L5052VoList) {
				@SuppressWarnings("unchecked")
				Map<String,String> MapL5052=(Map<String, String>) ThisObject;
				L5052Vo L5052VO=(L5052Vo) l5051ServiceImpl.MapToVO(MapL5052,"L5052",titaVo);
				OccursList occursList = new OccursList();
				occursList.putParam("OOPerfDate",L5052VO.getPerfDate());//業績日期
				occursList.putParam("OOAreaCenter",L5052VO.getAreaCenter());//區域中心
				occursList.putParam("OOAreaCenterX",L5052VO.getAreaCenterX());//區域中心名稱
				occursList.putParam("OODeptCode",L5052VO.getDeptCode());//部室別
				occursList.putParam("OODeptCodeX",L5052VO.getDeptCodeX());//部室別名稱
				occursList.putParam("OOBsOfficer",L5052VO.getBsOfficer());//房貸專員
				occursList.putParam("OOBsOfficerName",L5052VO.getBsOfficerName());//房貸專員名稱
				occursList.putParam("OOCustNm",L5052VO.getCustNm());//戶名
				occursList.putParam("OOCustNo",L5052VO.getCustNo());//戶號
				occursList.putParam("OOFacmNo",L5052VO.getFacmNo());//額度編號
				occursList.putParam("OOBormNo",L5052VO.getBormNo());//撥款編號
				occursList.putParam("OODrawdownDate",L5052VO.getDrawdownDate());//撥款日
				occursList.putParam("OOProdCode",L5052VO.getProdCode());//商品代碼
				occursList.putParam("OOPieceCode",L5052VO.getPieceCode());//計件代碼
				occursList.putParam("OODrawdownAmt",L5052VO.getDrawdownAmt());//撥款金額 
				occursList.putParam("OOWorkMonth",L5052VO.getWorkMonth());//工作月-民國
				occursList.putParam("OOPerfCnt",L5052VO.getPerfCnt());//件數
				occursList.putParam("OOPerfAmt",L5052VO.getPerfAmt());//業績金額
				occursList.putParam("OOIntroduceDeptCode",L5052VO.getIntroduceDeptCode());//介紹人部室代號
				occursList.putParam("OOIntroduceDistCode",L5052VO.getIntroduceDistCode());//介紹人區部代號
				occursList.putParam("OOIntroduceUnitCode",L5052VO.getIntroduceUnitCode());//介紹人單位代號
				occursList.putParam("OOIntroducer",L5052VO.getIntroducer());//介紹人員編
				occursList.putParam("OOIntroducerName",L5052VO.getIntroducerName());//介紹人姓名
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
		this.addList(this.totaVo);
		return this.sendList();
	}
}