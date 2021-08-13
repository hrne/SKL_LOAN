package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.NegQueryCust;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.NegQueryCustService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;

/**
 * Tita<br>
*/

@Service("L5705")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5705 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5705.class);
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public NegQueryCustService sNegQueryCustService;
	@Autowired
	public MakeFile makeFile;

	String OccName[]= {"OOCustId"};
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5705 ");
		this.totaVo.init(titaVo);
		this.info("L5705 titaVo=["+titaVo+"]");
		String UseFunc = titaVo.getParam("UseFunc").trim();//使用功能 0:選擇身份證 1:製檔
		String CustId = titaVo.getParam("CustId").trim();//身份證字號
		String RcDateFrom = titaVo.getParam("RcDateFrom").trim();//協商申請日(起)
		String RcDateTo = titaVo.getParam("RcDateTo").trim();//協商申請日(訖)
		
		
		int iRcDateFrom=0;
		int iRcDateTo=0;
		
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;
		this.limit=Integer.MAX_VALUE;
		this.index=0;
		
		if(RcDateFrom!=null && RcDateFrom.length()!=0 && Integer.parseInt(RcDateFrom)!=0) {
			if(RcDateFrom.length()==7 || RcDateFrom.length()==6) {
				iRcDateFrom=Integer.parseInt(RcDateFrom)+19110000;
			}
		}
		if(RcDateTo!=null && RcDateTo.length()!=0 && Integer.parseInt(RcDateTo)!=0) {
			if(RcDateTo.length()==7 || RcDateTo.length()==6) {
				iRcDateTo=Integer.parseInt(RcDateTo)+19110000;
			}
		}
		
		long TxtSnoF=0L;
		if(("0").equals(UseFunc)) {
			//選擇身份證
			InsertCust(CustId,iRcDateFrom,iRcDateTo,this.index,this.limit,titaVo);
		}else {
			//製檔
			String strToday=titaVo.getCalDy();
			int Today=0;
			if(String.valueOf(Integer.parseInt(strToday)).length()!=8) {
				Today=Integer.parseInt(strToday)+19110000;
			}
			TxtSnoF=makeTxt(String.valueOf(Today),titaVo);
		}
		
		this.info("L5705 TxtSnoF="+TxtSnoF);
		
		
		totaVo.put("TxtSnoF", "" + TxtSnoF);
		this.addList(this.totaVo);
		
		return this.sendList();
	}
	public int maxSeqForHadNegQueryCust(String DcToday,TitaVo titaVo) throws LogicException {
		int seq=0;
		Slice<NegQueryCust> slHadNegQueryCust=sNegQueryCustService.FirstAcDate(Integer.parseInt(DcToday), "Y", 0, Integer.MAX_VALUE, titaVo);
		List<NegQueryCust> lHadNegQueryCust = slHadNegQueryCust == null ? null : slHadNegQueryCust.getContent();
		if(lHadNegQueryCust!=null && lHadNegQueryCust.size()!=0) {
			int TempSeq=0;
			for(NegQueryCust NegQueryCustVo:lHadNegQueryCust) {
				if(TempSeq<NegQueryCustVo.getSeqNo()) {
					TempSeq=NegQueryCustVo.getSeqNo();
				}
			}
			seq=TempSeq;
		}
		return seq;
	}
	public long makeTxt(String DcToday,TitaVo titaVo) throws LogicException {
		if(DcToday!=null && DcToday.length()!=0) {
			
		}else {
			this.info("L5707 Error makeTxt 日期格式不正確Today=["+DcToday+"]");
			//E5009	資料檢核錯誤
			throw new LogicException(titaVo, "E5009","日期格式不正確");
		}

		int ThisSeq=0;//批號
		List<String> lCustId = new ArrayList<String>();
		
		Slice<NegQueryCust> slNegQueryCust=sNegQueryCustService.FirstAcDate(Integer.parseInt(DcToday), "N", 0, Integer.MAX_VALUE, titaVo);
		List<NegQueryCust> lNegQueryCust = slNegQueryCust == null ? null : slNegQueryCust.getContent();
		List<NegQueryCust> lUpdNegQueryCust =new ArrayList<NegQueryCust>();
		

		if(lNegQueryCust!=null && lNegQueryCust.size()!=0) {
			int MaxSeq=0;
			//找到最大的未製檔批號
			for(NegQueryCust NegQueryCustVo:lNegQueryCust) {
				if(MaxSeq<NegQueryCustVo.getSeqNo()) {
					MaxSeq=NegQueryCustVo.getSeqNo();
				}
			}
			ThisSeq=MaxSeq;
			
			//最大的已製檔批號
			int ThisHadSeq=maxSeqForHadNegQueryCust(DcToday,titaVo);
			if(ThisSeq<=ThisHadSeq) {
				ThisSeq=ThisHadSeq+1;
			}
			
			for(NegQueryCust NegQueryCustVo:lNegQueryCust) {
				lCustId.add(NegQueryCustVo.getCustId());
				NegQueryCustVo.setFileYN("Y");
				NegQueryCustVo.setSeqNo(ThisSeq);
				lUpdNegQueryCust.add(NegQueryCustVo);
			}
			
			try {
				sNegQueryCustService.updateAll(lUpdNegQueryCust, titaVo);//資料異動
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "");
			}
		}else {
			// E2003 查無資料
			this.info("L5705 Error 製檔 NegQueryCust 查無資料");
			throw new LogicException(titaVo, "E2003", "債協客戶請求資料");
		}
		long sno=0L;
		String yyyy=DcToday.substring(0,4);
		String yyy=String.valueOf(Integer.parseInt(yyyy)-1911);
		String mm=DcToday.substring(4,6);
		String dd=DcToday.substring(6,8);
		String RocToday=yyy+mm+dd;
		String fileitem = "債權比例分攤(產出)";
		
		String strThisSeq=String.valueOf(ThisSeq);
		for(int i=strThisSeq.length();i<2;i++) {
			strThisSeq="0"+strThisSeq;
		}
//		String filename = "458"+mm+dd+String.valueOf(ThisSeq)+".sta";   輸出時批號固定為1
		String filename = "458"+mm+dd+"1"+".sta";
		//458+會計日(月份)+會計日(日期)+批號.sta
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), fileitem, filename, 2);
		//JCIC-INQ-BARE-V01-4580001(24)+民國年月日(7)+批號(2)
//		String Head="JCIC-INQ-BARE-V01-4580001"+RocToday+strThisSeq;   輸出時批號固定為1
		String Head="JCIC-INQ-BARE-V01-4580001"+RocToday+"01";
		makeFile.put(Head);
		for (String CustId : lCustId) {
			//FB1(3)+身份證字號(10)+空白(20)+Z98(3)
			String Detail="FB1"+CustId+"                    Z98";
			makeFile.put(Detail);
		}
		String hhmm=titaVo.getCalTm().substring(0,4);
		//TRLR0000002(10)+民國年月日(7)+hhmm(4)
		String End="TRLR0000002"+RocToday+hhmm;
		makeFile.put(End);
		sno = makeFile.close();
		//makeFile.toFile(sno);//及時輸出的才要加這行
		return sno;
	}
	public void InsertCust(String CustId,int AcDateFrom,int AcDateTo,int index,int limit,TitaVo titaVo) throws LogicException {
		boolean bRcDate=(AcDateFrom!=0 && AcDateTo!=0);
		Slice<JcicZ048> slJcicZ048 = null;
		if(CustId!=null && CustId.length()!=0) {
			if(bRcDate) {
				slJcicZ048 = sJcicZ048Service.CustIdRcBetween(CustId, AcDateFrom, AcDateTo, index,limit , titaVo);
			}else {
				slJcicZ048 = sJcicZ048Service.CustIdEq(CustId, index,limit, titaVo);
			}
		}else {
			if(bRcDate) {
				slJcicZ048 = sJcicZ048Service.RcDateBetween(AcDateFrom, AcDateTo, index,limit, titaVo);
			}else {
				//E5009	資料檢核錯誤
				throw new LogicException(titaVo, "E5009","身份證字號與協商申請日至少填寫其中一項");
			}
		}
		List<JcicZ048> lJcicZ048 = slJcicZ048 == null ? null : slJcicZ048.getContent();
		if(lJcicZ048!=null && lJcicZ048.size()!=0) {
			List<String> DistinctCustId=new ArrayList<String>();
			for(JcicZ048 JcicZ048Vo:lJcicZ048) {
				if(!DistinctCustId.contains(JcicZ048Vo.getCustId())) {
					DistinctCustId.add(JcicZ048Vo.getCustId());
				}
			}
			if(DistinctCustId!=null && DistinctCustId.size()!=0) {
				for(String ThisCustId:DistinctCustId) {
					OccursList occursList = new OccursList();
					occursList.putParam(OccName[0], ThisCustId);// 身分證字號
					this.totaVo.addOccursList(occursList);
				}
			}
		}else {
			//E2003 查無資料
			throw new LogicException(titaVo, "E2003","債務人基本資料");
		}
	}
}