package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.domain.JcicZ046;
/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ046Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
* FUNCD=X,1<br>
* DEBTOR_IDNO=X,10<br>
* DEPARTMIND=X,10<br>
* NEGO_APPLDATE=9,7<br>
* INTED_DATE=9,7<br>
* ACCEPTIND=9,1<br>
* RE_FINANCIAL_IND=X,3<br>
* UNDIS_CLAIMSORG_IND1=X,3<br>
* UNDIS_CLAIMSORG_IND2=X,3<br>
* UNDIS_CLAIMSORG_IND3=X,3<br>
* OUT_JcicTXT_DATE=9,7<br>
* END=X,1<br>
*/

@Service("L8301")
@Scope("prototype")
public class L8301 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8301.class);
	
	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	
	
	@Autowired
	public JcicCom jcicCom;
	@Autowired
	SendRsp sendRsp;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public DataLog dataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8301 ");
		
		this.totaVo.init(titaVo);//前端資料
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String RbDate=titaVo.getParam("RbDate").trim();//止息基準日
		String ApplyType=titaVo.getParam("ApplyType").trim();//受理方式
		String RefBankId=titaVo.getParam("RefBankId").trim();//轉介金融機構代號
		String NotBankId1=titaVo.getParam("NotBankId1").trim();//未揭露債權機構代號1
		String NotBankId2=titaVo.getParam("NotBankId2").trim();//未揭露債權機構代號2
		String NotBankId3=titaVo.getParam("NotBankId3").trim();//未揭露債權機構代號3
		String NotBankId4=titaVo.getParam("NotBankId4").trim();//未揭露債權機構代號4
		String NotBankId5=titaVo.getParam("NotBankId5").trim();//未揭露債權機構代號5
		String NotBankId6=titaVo.getParam("NotBankId6").trim();//未揭露債權機構代號6
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//String CustName=titaVo.getParam("CustName").trim();//協商債務人姓名
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ040 tJcicZ040 = new JcicZ040();
		JcicZ040Id tJcicZ040Id = new JcicZ040Id();

		tJcicZ040Id.setCustId(CustId);//債務人IDN
		tJcicZ040Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ040Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日

		tJcicZ040.setJcicZ040Id(tJcicZ040Id);

		tJcicZ040.setCustId(tJcicZ040Id.getCustId());//債務人IDN
		tJcicZ040.setSubmitKey(tJcicZ040Id.getSubmitKey());//報送單位代號
		tJcicZ040.setRcDate(tJcicZ040Id.getRcDate());//協商申請日
		
		tJcicZ040.setTranKey(TranKey);//交易代碼
		tJcicZ040.setApplyType(ApplyType);//受理方式
		tJcicZ040.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ040.setRbDate(Integer.parseInt(jcicCom.RocTurnDc(RbDate,0)));//止息基準日
		tJcicZ040.setRefBankId(RefBankId);//轉介金融機構代號
		tJcicZ040.setNotBankId1(NotBankId1);//未揭露債權機構代號1
		tJcicZ040.setNotBankId2(NotBankId2);//未揭露債權機構代號2
		tJcicZ040.setNotBankId3(NotBankId3);//未揭露債權機構代號3
		tJcicZ040.setNotBankId4(NotBankId4);//未揭露債權機構代號4
		tJcicZ040.setNotBankId5(NotBankId5);//未揭露債權機構代號5
		tJcicZ040.setNotBankId6(NotBankId6);//未揭露債權機構代號6

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ040.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ040.setOutJcicTxtDate(0);
		}
		
//		jcicCom.checkJcicBankCode(SubmitKey,1, titaVo);
//		jcicCom.checkJcicBankCode(RefBankId,1, titaVo);
//		jcicCom.checkJcicBankCode(NotBankId1,1, titaVo);
//		jcicCom.checkJcicBankCode(NotBankId2,1, titaVo);
//		jcicCom.checkJcicBankCode(NotBankId3,1, titaVo);
//		jcicCom.checkJcicBankCode(NotBankId4,1, titaVo);
//		jcicCom.checkJcicBankCode(NotBankId5,1, titaVo);
//		jcicCom.checkJcicBankCode(NotBankId6,1, titaVo);
		JcicZ040 tJcicZ040VO=sJcicZ040Service.holdById(tJcicZ040Id, titaVo);
		JcicZ040 OrgJcicZ040 = null;
		if(tJcicZ040VO!=null) {
			OrgJcicZ040 = (JcicZ040) dataLog.clone(tJcicZ040VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ040VO,tJcicZ040VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ040.getTranKey())) {
					
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
					sJcicZ040Service.delete(tJcicZ040VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			checkJcicZ046(tJcicZ040, titaVo);//資料檢核
			if(tJcicZ040VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ040.setCreateDate(tJcicZ040VO.getCreateDate());
				tJcicZ040.setCreateEmpNo(tJcicZ040VO.getCreateEmpNo());

				tJcicZ040.setCreateDate(OrgJcicZ040.getCreateDate());
				tJcicZ040.setCreateEmpNo(OrgJcicZ040.getCreateEmpNo());
				try {
					tJcicZ040 = sJcicZ040Service.update2(tJcicZ040, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ040, tJcicZ040);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ040.setTranKey(TranKey);
				try {
					sJcicZ040Service.insert(tJcicZ040, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	public void checkJcicZ046(JcicZ040 tJcicZ040,TitaVo titaVo)throws LogicException {
		Slice<JcicZ046> slJcicZ046=sJcicZ046Service.CustIdEq(tJcicZ040.getJcicZ040Id().getCustId(), 0, Integer.MAX_VALUE, titaVo);
		//Slice<JcicZ046> slJcicZ046=sJcicZ046Service.CustRcEq(tJcicZ040.getJcicZ040Id().getCustId(),tJcicZ040.getJcicZ040Id().getRcDate(), 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ046> lJcicZ046 = slJcicZ046 == null ? null : slJcicZ046.getContent();
		if (lJcicZ046 != null && lJcicZ046.size() != 0) {
			//有報送過JCICZ046
			List<String> CloseCodeNotApply = Arrays.asList(new String[]{"00","01","11","12","13","14","15","17","18","19","21","49"});//這些不得申請
			List<String>  CloseCodeCloseDateOver180 = Arrays.asList(new String[]{"53","55","56","89"}); //結案日超過180天才可申請
			List<String>  CloseCodeCanApplyp = Arrays.asList(new String[]{"90","96","95","97","98","99"}); //可直接申請
			
			for (JcicZ046 tJcicZ046 : lJcicZ046) {
				String CloseCode=tJcicZ046.getCloseCode();
				//String BreakCode=tJcicZ046.getBreakCode();
				int CloseDate=tJcicZ046.getCloseDate();
				if(CloseCodeNotApply.contains(CloseCode)) {
					//E5009	資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "存在JcicZ046資料不可報送.(身分證字號:"+tJcicZ046.getCustId()+",協商申請日:"+tJcicZ046.getRcDate()+" 結案原因代號:"+CloseCode+") ");
				}else {
					if(CloseCodeCanApplyp.contains(CloseCode)){
						//可直接申請
					}else if(CloseCodeCloseDateOver180.contains(CloseCode)) {
						if(CloseDate!=0) {
							//ClosedDate 與本次協商超過180天才可申請
							String ClosedDateOver180=jcicCom.DateAdjust(String.valueOf(CloseDate),0,0,180);
							if(ClosedDateOver180!=null && ClosedDateOver180.length()!=0) {
								this.info("L8301 checkJcicZ046 CloseDate=["+CloseDate+"],ClosedDateOver180=["+ClosedDateOver180+"]");
								if(Integer.parseInt(ClosedDateOver180)<tJcicZ046.getRcDate()) {
									//結案日超過180天
									
								}else {
									//E5009	資料檢核錯誤
									throw new LogicException(titaVo, "E5009", "存在JcicZ046資料不可報送.結案日未超過180天.(身分證字號:"+tJcicZ046.getCustId()+",協商申請日:"+tJcicZ046.getRcDate()+" 結案原因代號:"+CloseCode+") ");
								}
							}else {
								//替退
								//E5009	資料檢核錯誤
								throw new LogicException(titaVo, "E5009","[結案日期]+180天發生錯誤");
							}
						}else {
							//E5009	資料檢核錯誤
							throw new LogicException(titaVo, "E5009","[結案日期]不存在");
						}
						
					}else {
						//替退
						//E5009	資料檢核錯誤
						throw new LogicException(titaVo, "E5009", "存在JcicZ046資料不可報送.結案原因代號不存在請查驗.(身分證字號:"+tJcicZ046.getCustId()+",協商申請日:"+tJcicZ046.getRcDate()+" 結案原因代號:"+CloseCode+") ");
					}
				}
				
			}
		}else {
			//未報送過JCICZ046
		}
	}
}