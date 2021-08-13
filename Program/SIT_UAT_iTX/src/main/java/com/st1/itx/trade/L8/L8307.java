package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;


/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;
import com.st1.itx.db.service.JcicZ040Service;
/*DB服務*/
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ051Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
* TranKey=X,3<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* CloseCode=X,2<br>
* BreakCode=X,2<br>
* CloseDate=9,7<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8307")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8307 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8307.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicCom jcicCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;
			
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L8307");
		this.info("active L8307 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		String CloseDate=titaVo.getParam("CloseDate").trim();//結案日期
		
		String CloseCode=titaVo.getParam("CloseCode").trim();//結案原因代號
		String BreakCode=titaVo.getParam("BreakCode").trim();//毀諾原因代號
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ046 tJcicZ046 = new JcicZ046();
		JcicZ046Id tJcicZ046Id = new JcicZ046Id();
		tJcicZ046Id.setCustId(CustId);//債務人IDN
		tJcicZ046Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ046Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ046Id.setCloseDate(Integer.parseInt(jcicCom.RocTurnDc(CloseDate,0)));//結案日期
		tJcicZ046.setJcicZ046Id(tJcicZ046Id);
		
		tJcicZ046.setTranKey(TranKey);
		tJcicZ046.setBreakCode(BreakCode);//毀諾原因代號
		tJcicZ046.setCloseCode(CloseCode);//結案原因代號
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ046.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ046.setOutJcicTxtDate(0);
		}
		JcicZ046 tJcicZ046VO=sJcicZ046Service.holdById(tJcicZ046Id, titaVo);
		JcicZ046 OrgJcicZ046 = null;
		if(tJcicZ046VO!=null) {
			OrgJcicZ046 = (JcicZ046) dataLog.clone(tJcicZ046VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ046VO,tJcicZ046VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ046.getTranKey())) {
					
				}else {
					//刷主管卡後始可刪除
					// 交易需主管核可
					if (!titaVo.getHsupCode().equals("1")) {
						//titaVo.getSupCode();
						sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
					}
				}
				//刪除
				try {
					sJcicZ046Service.delete(tJcicZ046VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			//jcicCom.checkJcicZ040(tJcicZ046.getJcicZ046Id().getCustId(),tJcicZ046.getJcicZ046Id().getRcDate(),tJcicZ046.getJcicZ046Id().getSubmitKey(), titaVo);//資料檢核
			
			CheckCloseCode(tJcicZ046,titaVo);
			Map<String,String> mCloseCode=new HashMap<String,String>();
			mCloseCode.put("11", "未能接受足以負擔之還款方案");
			mCloseCode.put("12", "要求折讓本金未為金融機構所接受");
			mCloseCode.put("13", "要求撤銷原已協商通過之還款方案並要求更優惠還款方案");
			mCloseCode.put("14", "無法負擔任何還款條件");
			mCloseCode.put("15", "本行/本公司未能於文件齊全後30日內開始協商");
			mCloseCode.put("17", "協商意願低落");
			mCloseCode.put("18", "債務人於協商前大量借款或密集消費");
			mCloseCode.put("19", "債務人於最大債權金融機構通知簽署協議書10日曆天內未完成簽約手續");
			mCloseCode.put("21", "資產大於負債");
			mCloseCode.put("49", "其他(協商不成立)");
			
			if(mCloseCode.keySet().contains(CloseCode)) {
				jcicCom.checkJcicZ044(tJcicZ046.getJcicZ046Id().getCustId(),tJcicZ046.getJcicZ046Id().getRcDate(),tJcicZ046.getJcicZ046Id().getSubmitKey(), titaVo);//資料檢核
				jcicCom.checkJcicZ048(tJcicZ046.getJcicZ046Id().getCustId(),tJcicZ046.getJcicZ046Id().getRcDate(),tJcicZ046.getJcicZ046Id().getSubmitKey(), titaVo);//資料檢核
			}
			if(tJcicZ046VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ046.setCreateDate(tJcicZ046VO.getCreateDate());
				tJcicZ046.setCreateEmpNo(tJcicZ046VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ046.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ046.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ046.setCreateDate(OrgJcicZ046.getCreateDate());
				tJcicZ046.setCreateEmpNo(OrgJcicZ046.getCreateEmpNo());
				try {
					tJcicZ046 = sJcicZ046Service.update2(tJcicZ046, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ046, tJcicZ046);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ046.setTranKey(TranKey);
				try {
					sJcicZ046Service.insert(tJcicZ046, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	
	public void CheckCloseCode(JcicZ046 tJcicZ046,TitaVo titaVo) throws LogicException {
		String CloseCode=tJcicZ046.getCloseCode();
		jcicCom.l8307checkJcicZ047(tJcicZ046.getJcicZ046Id().getCustId(),tJcicZ046.getJcicZ046Id().getRcDate(),tJcicZ046.getJcicZ046Id().getSubmitKey(),CloseCode, titaVo);//資料檢核
		//90:毀諾後清償
		if(("90").equals(CloseCode)) {
			if(!tJcicZ046.getTranKey().equals("C")) {
				//E5009	資料檢核錯誤
				throw new LogicException(titaVo, "E5009","結案原因代號:『90:毀諾後清償』僅得以異動方式進行報送");
			}
		}
	}
	public void CheckJcicZ051(JcicZ046 tJcicZ046,TitaVo titaVo) throws LogicException {
		String EntDy=titaVo.getEntDy();
		this.info("L8307 會計日 EntDy='"+EntDy+"'");
		if(EntDy!=null && EntDy.length()!=0) {
			int EntDyL=EntDy.length();
			
			int intEntDy=0;
			if(EntDyL==7) {
				//Roc
				intEntDy=Integer.parseInt(EntDy.substring(0,5))+191100;
			}else if(EntDyL==8) {
				intEntDy=Integer.parseInt(EntDy.substring(0,6));
			}
			Slice<JcicZ051> slJcicZ051=sJcicZ051Service.InJcicZ051(tJcicZ046.getJcicZ046Id().getSubmitKey(), tJcicZ046.getJcicZ046Id().getCustId(), tJcicZ046.getJcicZ046Id().getRcDate(), intEntDy, 0, Integer.MAX_VALUE, titaVo);
			List<JcicZ051> lJcicZ051 = slJcicZ051 == null ? null : slJcicZ051.getContent();
			if(lJcicZ051!=null && lJcicZ051.size()!=0) {
				//在喘息期範圍
				if(("00").equals(tJcicZ046.getCloseCode())) {
					//00:毀諾
					//E5009	資料檢核錯誤
					throw new LogicException(titaVo, "E5009","該客戶尚在喘息期間,不可報送毀諾.");
				}else {
					
				}
			}else {
				//不在喘息其範圍
				
			}
			
		}else {
			
		}
		
		
		JcicZ051Id jcicZ051Id=new JcicZ051Id();
		sJcicZ051Service.findById(jcicZ051Id, titaVo);
	}
}