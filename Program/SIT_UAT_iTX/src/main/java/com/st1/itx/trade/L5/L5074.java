package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

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

/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegAppr;
import com.st1.itx.db.domain.NegAppr01;
//import com.st1.itx.db.domain.NegApprId;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.service.NegAppr01Service;
/*DB服務*/
import com.st1.itx.db.service.NegApprService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.util.common.NegCom;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* AcDate=9,7<br>
*/

/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Service("L5074")
@Scope("prototype")
public class L5074 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5074.class);
	/* DB服務注入 */
	@Autowired
	public NegApprService sNegApprService;
	
	@Autowired
	public NegAppr01Service sNegAppr01Service;
	
	@Autowired
	public NegTransService sNegTransService;
	
	@Autowired
	public NegCom NegCom;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	private String UseAcDate="";
	
	String ColA[]= {"","最大債權","一般債權"};
	String ColB[]= {"","前日匯入","本日匯入","撥入筆數","檢核成功","檢核失敗"};
	String ColC[]= {"","未入帳","待處理","本日入帳","放款暫收"};
	String ColD[]= {"","撥付金額","放款攤分","保單攤分","結清退還款"};
	String ColE[]= {"","本月入帳","本月放款","本月保單","累計未退還"};
	String ColDetail[]= {"債協","調解","更生","清算"};
	String Issue[]= {"","入帳還款","撥付製檔","撥付出帳","撥付提兌"};//製檔日,傳票日,提兌日
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5074 ");
		this.totaVo.init(titaVo);
		String AcDate=titaVo.getParam("AcDate").trim(); //會計日期
		UseAcDate=AcDate;
		if(AcDate!=null && AcDate.length()!=0) {
			
		}else {
			throw new LogicException(titaVo, "E1002","[會計日期]為空值");
		}
		int IntAcDate=parse.stringToInteger(AcDate);
		if(IntAcDate!=0 && String.valueOf(IntAcDate).length()==7) {
			IntAcDate=IntAcDate+19110000;
		}
		this.info("L5074 IntAcDate=["+IntAcDate+"]");
//		int IntAcDateStart=IntAcDate;
//		int ThisMothStart=Integer.parseInt(String.valueOf(IntAcDate).substring(0,6)+"01");
//		int ThisMothEnd=Integer.parseInt(String.valueOf(IntAcDate).substring(0,6)+"31");
		//先查最大的範圍
//		int ThisYyyyMmAcDate=Integer.parseInt(String.valueOf(IntAcDate).substring(0,6));
//		int YyyyMmStart=AdjustYyyyMm(ThisYyyyMmAcDate,+1);
//		int YyyyMmEnd=AdjustYyyyMm(ThisYyyyMmAcDate,-1);
		
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit=Integer.MAX_VALUE;//查全部
		
		Slice<NegAppr> slNegAppr=sNegApprService.AcDateEq(IntAcDate, IntAcDate, IntAcDate, this.index,this.limit, titaVo); 
		List<NegAppr> lNegAppr = slNegAppr == null ? null : slNegAppr.getContent();

		int TotalLength=23;
		for(int i=1;i<=TotalLength;i++) {
			
			String LabelA="";
			String LabelB="";
			String LabelC="";
			String LabelD="";
			String TodayWorkCnt="0";
			String TodayWorkAmt="0";
			String LabelE="";
			String SummaryWorkCnt="0";
			String SummaryWorkAmt="0";
			String ThisIssue="";
			String ThisTodayIssue="";
			String BufToday="";
			String BufSummary="";
			String BufIssue="";
			String BtnCode="0";//處理事項是否顯示 0不顯示 1:導入L597A 2:撥付產檔日(L5707) 3:撥付傳票日(L5708) 4:撥付提兌日(L5709)
			
			int IsMainFin=0;//最大債權 0否 1是
			int State=0;//01:前日匯入,02:未入帳,03:待處理,04:已入帳,05:本月入帳,06:放款攤分,07:保單攤分,08:結清退還,09:本月放款,10:本月保單,11:累計未退還,12:本日匯入,13:撥入筆數,14:檢核成功,15:檢核失敗,16:放款暫收
			int Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
			int ExportDateYN=0;//0:無(左側),1:已製檔,2:未製檔
			int IsBtn=0;
			String Data[]=new String [4];//筆數,總金額,使用資料庫,Key值
			
			if(i>=1 && i<=18) {
				//最大債權
				IsMainFin=1;
				if(i==1) {
					LabelA=ColA[1];
					LabelB=ColB[1];//本日處理
					State=1;//前日匯入
					Detail=0;//無
					ExportDateYN=0;//無
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					//前日匯入(總筆數)-(未入帳,待處理)
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,IsBtn);
				}else if(i==2) {
					//未入帳
					LabelC=ColC[1];
					
					
					State=2;//未入帳
					Detail=0;//無
					ExportDateYN=0;//無
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					
					ThisIssue=Issue[1];//處理事項
					if(TodayWorkCnt!=null && Integer.parseInt(TodayWorkCnt)>0) {
						BtnCode="1";
					}else {
						BtnCode="0";
					}
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					BufIssue=BufValue(IsMainFin,State,Detail,ExportDateYN,1);
				}else if(i==3) {
					//待處理
					LabelC=ColC[2];
					
					State=3;//待處理
					Detail=0;//無
					ExportDateYN=0;//無
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額

					ThisIssue=Issue[1];//處理事項
					if(TodayWorkCnt!=null && Integer.parseInt(TodayWorkCnt)>0) {
						BtnCode="1";
					}else {
						BtnCode="0";
					}
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					BufIssue=BufValue(IsMainFin,State,Detail,ExportDateYN,1);
				}else if(i==4) {
					//已入帳
					LabelC=ColC[3];
					LabelE=ColE[1]+"－已製檔";

					State=4;//已入帳
					Detail=0;//無
					ExportDateYN=0;//0:無(左側),1:已製檔,2:未製檔
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					
					State=5;//本月入帳
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=1;//0:無(左側),1:已製檔,2:未製檔
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					SummaryWorkCnt=Data[0];//筆數
					SummaryWorkAmt=Data[1];//金額
					BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==5) {	
					//已入帳
					LabelC=ColC[0];
					LabelE=ColE[1]+"－未製檔";
					
					State=5;//本月入帳
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=2;//0:無(左側),1:已製檔,2:未製檔
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					SummaryWorkCnt=Data[0];//筆數
					SummaryWorkAmt=Data[1];//金額
					BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i>=6 && i<=14) {
					//債協(左側)
					//入帳還款日期 RepayDate等於 Tita會計日期
					
					//撥付金額(右側):
					//	已製檔-(製檔日期=Tita日期屬於設定檔的[製檔日期~提兌日])
					//		提兌日Btn:連線到L5709
					//		撥付出帳Btn
					
					//	未製檔(製檔日期=空)
					//		撥付製檔Btn
					
					//債協,調解,更生,清算
					if(i==6) {
						//撥付金額
						LabelD=ColD[1];
					}else {
						int ThisRow=i-7;
						this.info("L5074 i=["+i+"],ThisRow=["+ThisRow+"],ThisRow%2=["+ThisRow%2+"]");
						//債協-已製檔
						
						int TestColDetail=ThisRow/2;
						String UseColDetail=ColDetail[TestColDetail];
						if(ThisRow%2==0) {
							ExportDateYN=1;//已製檔
							
							LabelD=UseColDetail;
							LabelE=UseColDetail+"－已製檔";
							ThisIssue=IssueValue(IntAcDate,UseColDetail,lNegAppr,1); 
						}else {
							ExportDateYN=2;//未製檔
							LabelE=UseColDetail+"－未製檔";
							ThisIssue=IssueValue(IntAcDate,UseColDetail,lNegAppr,0); 
						}
						State=4;//已入帳
						Detail=UseDetail(i,ThisRow);//00:無,01:債協,02:調解,03:更生,04:清算
						//修改:因左側不顯示故不需做下面這段,點掉讀資料
						//try {
						//	Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
						//} catch (LogicException e) {
						//	//E5004 讀取DB時發生問題
						//	this.info("L5051 ErrorForDB="+e);
						//	throw new LogicException(titaVo, "E5004","");
						//}
						//左側


						TodayWorkCnt="0";//筆數   
						TodayWorkAmt="0";//金額  
						
//						TodayWorkCnt=Data[0];//筆數
//						TodayWorkAmt=Data[1];//金額
//						BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
//						BufIssue=BufValue(IsMainFin,State,Detail,ExportDateYN,1);
						
						
						State=4;//已入帳     //修改: 5;//本月入帳
						Detail=UseDetail(i,ThisRow);//00:無,01:債協,02:調解,03:更生,04:清算
						this.info("L5074 i=["+i+"] Detail=["+Detail+"]");
						try {
							Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
						} catch (LogicException e) {
							//E5004 讀取DB時發生問題
							this.info("L5051 ErrorForDB="+e);
							throw new LogicException(titaVo, "E5004","");
						}
						//右側
						SummaryWorkCnt=Data[0];//筆數
						SummaryWorkAmt=Data[1];//金額
						BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
						this.info("\r\nLabelD=["+LabelD+"],LabelE=["+LabelE+"],\r\nTodayWorkCnt=["+TodayWorkCnt+"],TodayWorkAmt=["+TodayWorkAmt+"],\r\nSummaryWorkCnt=["+SummaryWorkCnt+"],SummaryWorkAmt=["+SummaryWorkAmt+"]");
						//ThisIssue=Issue[1];//處理事項
						if(SummaryWorkAmt!=null && Integer.parseInt(SummaryWorkAmt)>0) {
							if(ThisIssue!=null && ThisIssue.trim().length()!=0) {
								//處理事項是否顯示 0不顯示 1:導入L597A 2:撥付產檔日(L5707) 3:撥付傳票日(L5708) 4:撥付提兌日(L5709)
								//String Issue[]= {"","入帳還款","撥付製檔","撥付出帳","撥付提兌"};//製檔日,傳票日,提兌日
								if(ThisIssue.equals(Issue[2])) {
									//撥付製檔
									BtnCode="2";
								}else if(ThisIssue.equals(Issue[3])) {
									//撥付傳票
									BtnCode="3";
								}else if(ThisIssue.equals(Issue[4])) {
									//撥付提兌
									BtnCode="4";
								}
								
								
							}else {
								BtnCode="0";
							}
						}else {
							BtnCode="0";
						}
						BtnCode=HadDo(IntAcDate,BtnCode,titaVo);
						if(ThisIssue!=null && ThisIssue.trim().length()!=0) {
							ThisTodayIssue=ThisIssue;//本日應處理
						}
						
					}
				}else if(i==15) {
					LabelD=ColD[2];
					LabelE=ColE[2];
					
					//放款攤分(左)
					State=6;//放款攤分
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					//本月放款(右)
					State=9;//本月放款
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					SummaryWorkCnt=Data[0];//筆數
					SummaryWorkAmt=Data[1];//金額
					BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==16) {
					LabelD=ColD[3];
					LabelE=ColE[3];
					//保單攤分(左)
					State=7;//保單攤分
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					//本月保單(右)
					State=10;//本月保單
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					SummaryWorkCnt=Data[0];//筆數
					SummaryWorkAmt=Data[1];//金額
					BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==17) {
					LabelD=ColD[4];
					LabelE=ColE[4];
					//結清退還(左)
					State=8;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					//累計未退還(右)
					State=11;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					SummaryWorkCnt=Data[0];//筆數
					SummaryWorkAmt=Data[1];//金額
					BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==18) {
					//本日匯入(左)
					LabelB=ColB[2];
					LabelC=ColC[1];
					State=12;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					ThisIssue=Issue[1];//處理事項
					//if(TodayWorkCnt!=null && Integer.parseInt(TodayWorkCnt)>0) {  
					//	BtnCode="1";
					//}else {
					//	BtnCode="0";
					//}

				}
			}else {
				//一般債權
				IsMainFin=0;
				if(i==19) {
					//撥入筆數(左)
					LabelA=ColA[2];
					LabelB=ColB[3];
					
					State=13;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==20) {
					//檢核成功(左)
					LabelB=ColB[4];
					State=14;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==21) {
					//檢核失敗(左)
					LabelB=ColB[5];
					State=15;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}else if(i==22) {
					//未入帳(左)
					LabelC=ColC[1];
					
					State=2;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
					BufIssue=BufValue(IsMainFin,State,Detail,ExportDateYN,1);
					
					ThisIssue=Issue[1];//處理事項
					if(TodayWorkCnt!=null && Integer.parseInt(TodayWorkCnt)>0) {
						BtnCode="1";
					}else {
						BtnCode="0";
					}
				}else if(i==23) {
					LabelC=ColC[4];//放款暫收
					LabelE=ColE[2];//本月放款
					//已入帳(左)
					State=16;//放款暫收
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					TodayWorkCnt=Data[0];//筆數
					TodayWorkAmt=Data[1];//金額
					BufToday=BufValue(IsMainFin,State,Detail,ExportDateYN,0);

					//本月放款(右)
					State=17;
					Detail=0;//00:無,01:債協,02:調解,03:更生,04:清算
					ExportDateYN=0;
					try {
						Data=NegCom.NegServiceList1(IntAcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn,titaVo);
					} catch (LogicException e) {
						//E5004 讀取DB時發生問題
						this.info("L5051 ErrorForDB="+e);
						throw new LogicException(titaVo, "E5004","");
					}
					SummaryWorkCnt=Data[0];//筆數
					SummaryWorkAmt=Data[1];//金額
					BufSummary=BufValue(IsMainFin,State,Detail,ExportDateYN,0);
				}
			}

			OccursList occursList1 = new OccursList();
			occursList1.putParam("OORank",i);
			occursList1.putParam("OOLabelA",LabelA);
			occursList1.putParam("OOLabelB",LabelB);//本日處理
			occursList1.putParam("OOLabelC",LabelC);
			occursList1.putParam("OOLabelD",LabelD);
			occursList1.putParam("OOTodayWorkCnt",TodayWorkCnt);//筆數
			occursList1.putParam("OOTodayWorkAmt",TodayWorkAmt);//金額
			occursList1.putParam("OOLabelE",LabelE);//累計處理
			occursList1.putParam("OOSummaryWorkCnt",SummaryWorkCnt);//筆數
			occursList1.putParam("OOSummaryWorkAmt",SummaryWorkAmt);//金額
			occursList1.putParam("OOIssue",ThisIssue);//處理事項
			occursList1.putParam("OOTodayIssue",ThisTodayIssue);//本日應處理-提示文字
			occursList1.putParam("OOBufToday",BufToday);//本日_數字按鈕-傳送的資料
			occursList1.putParam("OOBufSummary",BufSummary);//累計_數字按鈕-傳送的資料
			occursList1.putParam("OOBufIssue",BufIssue);//處理事項按鈕-傳送的資料
			occursList1.putParam("OOBtnCode",BtnCode);//處理事項是否顯示 0不顯示 1:導入L597A 2:撥付產檔日(L5707) 3:撥付傳票日(L5708) 4:撥付提兌日(L5709)
			this.totaVo.addOccursList(occursList1);
		}
		
		
//		OccursList occursList = new OccursList();
//		this.totaVo.addOccursList(occursList);
		/*
		OORank=9,2
		OOLabelA=X,8
		OOLabelB=X,8
		OOLabelC=X,6
		OOLabelD=X,10
		OOTodayWorkCnt=9,3
		OOTodayWorkAmt=9,14.2
		OOLabelE=X,10
		OOSummaryWorkCnt=9,3
		OOSummaryWorkAmt=9,14.2
		OOIssue=X,8
		OOTodayIssue=X,6
		OOBufToday=X,5
		OOBufSummary=X,48
		OOBufIssue=X,5
		OOBtnCode=9,1
		*/
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public String HadDo(int IntAcDate,String BtnCode,TitaVo titaVo)throws LogicException {
		//處理事項是否顯示 0不顯示 1:導入L597A 2:撥付產檔日(L5707) 3:撥付傳票日(L5708) 4:撥付提兌日(L5709)
		Boolean TF=false;
		Slice<NegTrans> slNegTrans = null;
		this.info("HadDo BtnCode=["+BtnCode+"]" );
		switch(BtnCode) {
			case "1":
				//導入L597A
				//入賬還款
//				slNegTrans = sNegTransService.RepayDateEq(IntAcDate, 0, 1,titaVo);
//				if(slNegTrans!=null && slNegTrans.getSize()!=0) {
//					TF=true;
//				}
				break;
			case "2":
				//撥付產檔日(L5707)
				slNegTrans = sNegTransService.ExportDateEq(IntAcDate, 0, 1,titaVo);
				if(slNegTrans!=null && slNegTrans.getSize()!=0) {
					TF=true;
				}
				break;
			case "3":
				//撥付傳票日(L5708)
				slNegTrans = sNegTransService.ExportAcDateEq(IntAcDate, 0, 1,titaVo);
				if(slNegTrans!=null && slNegTrans.getSize()!=0) {
					TF=true;
				}
				break;
			case "4":
				//撥付提兌日(L5709)
				Slice<NegAppr01> sNegAppr01=sNegAppr01Service.BringUpDateEq(IntAcDate, 0, 1, titaVo);
				if(sNegAppr01!=null && sNegAppr01.getSize()!=0) {
					TF=true;
				}
				break;
		}
		if(TF) {
			BtnCode="0";
		}
		this.info("HadDo BtnCode Aft=["+BtnCode+"]" );
		return BtnCode;
	}
	public String IssueValue(int IntAcDate,String ColDetailValue,List<NegAppr> lNegAppr,int FileTest) throws LogicException {
		this.info("L5074 IssueValue IntAcDate=["+IntAcDate+"],ColDetailValue=["+ColDetailValue+"],FileTest=["+FileTest+"]");
		//IntAcDate 會計日期
		//ColDetailValue 債協 調解 更生 清算
		//FileTest 0 未製檔 1 已經製檔
		
		//String ColDetail[]= {"債協","調解","更生","清算"};
		String ThisIssue="";
		int ThisKindCode=-1;
		for(int i=0;i<ColDetail.length;i++) {
			if(ColDetailValue.equals(ColDetail[i])) {
				ThisKindCode=i+1;//與NegAppr 的 KindCode配對
				break;
			}
		}
		this.info("L5074 IssueValue ThisKindCode=["+ThisKindCode+"]");
		if(lNegAppr!=null && lNegAppr.size()!=0) {
			this.info("L5074 IssueValue lNegAppr IS NOT NULL");
			for(NegAppr NegApprVO:lNegAppr) {
				int KindCode=NegApprVO.getKindCode();//1:債協 2:調解 二分 3:更生 4:清算
				int ExportDate=RocToDc(NegApprVO.getExportDate());//製檔日
				int ApprAcDate=RocToDc(NegApprVO.getApprAcDate());//傳票日-撥付出帳
				int BringUpDate=RocToDc(NegApprVO.getBringUpDate());//提兌日
				this.info("L5074 IssueValue NegApprVO=["+NegApprVO.toString()+"]");
				if(ThisKindCode==KindCode || (ThisKindCode==1 && (KindCode==1 || KindCode==2))) {
					//String Issue[]= {"","入帳還款","撥付製檔","撥付出帳","撥付提兌"};//製檔日,傳票日,提兌日
					if(FileTest==0) {
						//未製檔
						this.info("L5074 IssueValue ExportDate=["+ExportDate+"]");
						if(ExportDate==IntAcDate) {
							//製檔日
							ThisIssue=Issue[2];
						}
					}else if(FileTest==1) {
						//已製檔
						this.info("L5074 IssueValue ApprAcDate=["+ApprAcDate+"]");
						this.info("L5074 IssueValue BringUpDate=["+BringUpDate+"]");
						if(ApprAcDate==IntAcDate) {
							//傳票日-撥付出帳
							ThisIssue=Issue[3];
							//檢查是否已做好撥付出帳
							
						}else if(BringUpDate==IntAcDate) {
							//提兌日
							ThisIssue=Issue[4];
						}
					}
					
					
				}else {
					continue;
				}
				
			}
		}else {
			this.info("L5074 IssueValue NegAppr not found!");
			//throw new LogicException("E001", "NegAppr");
		}
		this.info("L5074 IssueValue ThisIssue=["+ThisIssue+"]");
		return ThisIssue;
	}
	public int RocToDc(Integer Roc) {
		int Dc=0;
		if(Roc!=null && Roc>0) {
			if(String.valueOf(Roc).length()==7) {
				Dc=Roc+19110000;
			}
		}
		return Dc;
	}
	public int UseDetail(int i,int Row) {
		int ReturnRow=Row/2+1;
		this.info("L5074 UseDetail i="+i+",Row="+Row+",ReturnRow="+ReturnRow+"");
		return ReturnRow;
	}
	public String BufValue(int IsMainFin,int State,int Detail,int ExportDateYN,int IsBtn) {
		//左側 筆數 按鈕 OOBufToday
		//右側 筆數 按鈕 OOBufSummary
		//右側 按鈕 按鈕 OOBufIssue
		String Value=UseAcDate+String.valueOf(IsMainFin)+String.format("%02d", State)+String.valueOf(Detail)+String.valueOf(ExportDateYN)+String.valueOf(IsBtn);
		return Value;
	}
	public int AdjustYyyyMm(int YyyyMm,int AdjustMonth) {
		String StrYyyyyMm=String.valueOf(YyyyMm);
		if(StrYyyyyMm!=null && StrYyyyyMm.length()==6) {
			int Yyyy=Integer.parseInt(StrYyyyyMm.substring(0,4));
			int Mm=Integer.parseInt(StrYyyyyMm.substring(4,6));
			int testAddOrMinus=0;
			if(AdjustMonth>0) {
				testAddOrMinus=1;
			}else if(AdjustMonth<0) {
				testAddOrMinus=-1;
			}
			
			int AdjustYyyy=Math.abs(AdjustMonth)/12;
			AdjustMonth=Math.abs(AdjustMonth)%12;
			
			Yyyy=Yyyy+testAddOrMinus*AdjustYyyy;
			
			Mm=Mm+testAddOrMinus*AdjustMonth;
			
			if(Mm>0) {
				if(Mm/12>=1) {
					if(Mm%12>0) {
						Yyyy=Yyyy+testAddOrMinus*Mm/12;
						Mm=Mm%12;
					}
				}
			}else if(Mm<0) {
				Yyyy=Yyyy-1;
				Mm=Mm+12;
			}else {
				Mm=12;
			}
			YyyyMm=Yyyy*100+Mm;
		}
		return YyyyMm;
	}
}