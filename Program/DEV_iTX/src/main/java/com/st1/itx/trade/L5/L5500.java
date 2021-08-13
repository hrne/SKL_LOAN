package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.HlAreaData;
import com.st1.itx.db.domain.HlAreaLnYg6Pt;
import com.st1.itx.db.domain.HlAreaLnYg6PtId;
import com.st1.itx.db.domain.HlCusData;
import com.st1.itx.db.domain.HlEmpLnYg5Pt;
import com.st1.itx.db.domain.HlEmpLnYg5PtId;
import com.st1.itx.db.domain.HlThreeDetail;
import com.st1.itx.db.domain.HlThreeDetailId;
import com.st1.itx.db.domain.HlThreeLaqhcp;
import com.st1.itx.db.domain.HlThreeLaqhcpId;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.HlAreaDataService;
import com.st1.itx.db.service.HlAreaLnYg6PtService;
import com.st1.itx.db.service.HlCusDataService;
import com.st1.itx.db.service.HlEmpLnYg5PtService;
import com.st1.itx.db.service.HlThreeDetailService;
import com.st1.itx.db.service.HlThreeLaqhcpService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.db.service.PfDeparmentService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.springjpa.cm.L5500ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5500")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5500 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5500.class);
	/* DB服務注入 */
	@Autowired
	public SystemParasService systemParasService;
	
	@Autowired
	public HlAreaDataService sHlAreaDataService;
	@Autowired
	public HlAreaLnYg6PtService sHlAreaLnYg6PtService;
	@Autowired
	public HlEmpLnYg5PtService sHlEmpLnYg5PtService;
	@Autowired
	public HlThreeDetailService sHlThreeDetailService;
	@Autowired
	public HlThreeLaqhcpService sHlThreeLaqhcpService;
	@Autowired
	public HlCusDataService sHlCusDataService;
	
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public PfRewardService sPfRewardService;//介紹、協辦獎金發放檔
	@Autowired
	public PfItDetailService sPfItDetailService;//介紹人業績明細檔
	@Autowired
	public PfDeparmentService sPfDeparmentService;//單位、區部、部室業績目標檔
	@Autowired
	public PfCoOfficerService sPfCoOfficerService;//協辦人員等級檔
	@Autowired
	public PfBsOfficerService sPfBsOfficerService;//房貸專員業績目標檔
	@Autowired
	public PfBsDetailService sPfBsDetailService;//房貸專員業績明細檔

	@Autowired
	public CdAoDeptService sCdAoDeptService;//
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	public CdBcmService sCdBcmService;
	
	@Autowired
	public L5500ServiceImpl l5500ServiceImpl;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	String Connect=",";
	
	Integer UpNo=0;
	String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
	//{"資料庫TB縮寫","DB Column","DB Column Name","DB Column Type AND Length"}
	private String sqlColumn[][]={
		{"PID","PerfDate","業績日期","DecimalD(8)"},
		{"PID","CustNo","戶號","DECIMAL(7)"},
		{"PID","FacmNo","額度編號","DECIMAL(3)"},
		{"PID","BormNo","撥款序號","DECIMAL(3)"},
		{"PID","RepayType","還款類別","DECIMAL(1)"},
		{"PID","DrawdownDate","撥款日/還款日","DecimalD(8)"},
		{"PID","ProdCode","商品代碼","VARCHAR2(5)"},
		{"PID","PieceCode","計件代碼","VARCHAR2(1)"},
		{"PID","CntingCode","是否計件","VARCHAR2(1)"},
		{"PID","DrawdownAmt","撥款金額/追回金額","DECIMAL(16.2)"},
		{"PID","UnitCode","單位代號","VARCHAR2(6)"},
		{"PID","DistCode","區部代號","VARCHAR2(6)"},
		{"PID","DeptCode","部室代號","VARCHAR2(6)"},
		{"PID","Introducer","介紹人","NVARCHAR2(8)"},
		{"PID","UnitManager","處經理代號","NVARCHAR2(8)"},
		{"PID","DistManager","區經理代號","NVARCHAR2(8)"},
		{"PID","DeptManager","部經理代號","NVARCHAR2(8)"},
		{"PID","PerfCnt","件數","DECIMAL(5.1)"},
		{"PID","PerfEqAmt","換算業績","DECIMAL(16.2)"},
		{"PID","PerfReward","業務報酬","DECIMAL(16.2)"},
		{"PID","PerfAmt","業績金額","DECIMAL(16.2)"},
		{"PID","WorkMonth","工作月","DECIMAL(6)"},
		{"PID","WorkSeason","工作季","DECIMAL(5)"},
		{"PID","CreateDate","建檔日期時間","DATE"},
		{"PID","CreateEmpNo","建檔人員","VARCHAR2(6)"},
		{"PID","LastUpdate","最後更新日期時間","DATE"},
		{"PID","LastUpdateEmpNo","最後更新人員","VARCHAR2(6)"},
		{"PR","Introducer","介紹人員編","VARCHAR2(6)"},
		{"PR","Coorgnizer","協辦人員編","VARCHAR2(6)"},
		{"PR","InterviewerA","晤談一員編","VARCHAR2(6)"},
		{"PR","InterviewerB","晤談二員編","VARCHAR2(6)"},
		{"PR","IntroducerBonus","介紹人介紹獎金","DECIMAL(16.2)"},
		{"PR","IntroducerBonusDate","介紹獎金發放日","DecimalD(8)"},
		{"PR","IntroducerAddBonus","介紹人加碼獎勵津貼","DECIMAL(16.2)"},
		{"PR","IntroducerAddBonusDate","獎勵津貼發放日","DecimalD(8)"},
		{"PR","CoorgnizerBonus","協辦人員協辦獎金","DECIMAL(16.2)"},
		{"PR","CoorgnizerBonusDate","協辦獎金發放日","DecimalD(8)"},
		{"PR","WorkMonth","工作月","DECIMAL(6)"},
		{"PR","WorkSeason","工作季","DECIMAL(5)"},
		{"PR","CreateDate","建檔日期時間","DATE"},
		{"PR","CreateEmpNo","建檔人員","VARCHAR2(6)"},
		{"PR","LastUpdate","最後更新日期時間","DATE"},
		{"PR","LastUpdateEmpNo","最後更新人員","VARCHAR2(6)"},
		{"PBD","BsOfficer","房貸專員","VARCHAR2(6)"},
		{"PBD","DeptCode","部室代號","VARCHAR2(6)"},
		{"PBD","DrawdownDate","撥款日","DecimalD(8)"},
		{"PBD","ProdCode","商品代碼","VARCHAR2(5)"},
		{"PBD","PieceCode","計件代碼","VARCHAR2(1)"},
		{"PBD","DrawdownAmt","撥款金額",""},
		{"PBD","PerfCnt","件數","DECIMAL(5.1)"},
		{"PBD","PerfAmt","業績金額","DECIMAL(16.2)"},
		{"PBD","WorkMonth","工作月","DECIMAL(6)"},
		{"PBD","WorkSeason","工作季","DECIMAL(5)"},
		{"PBD","CreateDate","建檔日期時間","DATE()"},
		{"PBD","CreateEmpNo","建檔人員","VARCHAR2(6)"},
		{"PBD","LastUpdate","最後更新日期時間","DATE()"},
		{"PBD","LastUpdateEmpNo","最後更新人員","VARCHAR2(6)"}
	};
			
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5500 ");
		this.totaVo.init(titaVo);
		UpNo=Integer.parseInt(titaVo.getEmpNot());
		
		/*
		String WorkDate=titaVo.getParam("WorkDate").trim();//工作日
		int PerfDate=0;
		if(WorkDate!=null && WorkDate.length()!=0) {
			PerfDate=parse.stringToInteger(WorkDate);
			if(PerfDate>0 && String.valueOf(PerfDate).length()==7) {
				PerfDate=PerfDate+19110000;
			}
		}else {
			//工作日未填寫
			throw new LogicException(titaVo, "XXXX", "[工作日]未填寫");
		}
		*/
		// 下一業績日期
		int nextPerfDate = this.txBuffer.getMgBizDate().getNbsDy();
		

		// 1.業績日期 SystemParas.PerfDate
		SystemParas tSystemParas = systemParasService.holdById("LN", titaVo);
		if (tSystemParas.getPerfDate() != nextPerfDate) {
			tSystemParas.setPerfDate(nextPerfDate);
			try {
				systemParasService.update(tSystemParas, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "BS050 update SystemParas " + tSystemParas + e.getErrorMsg());
			}
		}
		

		int PerfDate=nextPerfDate;
		
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit=Integer.MAX_VALUE;//查全部

		String sqlL5500=sqlL5500();
		this.info("L5500 sqlL5500 sql=["+sqlL5500+"]");
		Map<String,String> queryKey=new HashMap<String,String>();
		int PerfDateL=String.valueOf(PerfDate).length();
		if(PerfDateL==6 || PerfDateL==7) {
			PerfDate=PerfDate+19110000;
		}
		queryKey.put("PerfDate", String.valueOf(PerfDate));
		this.info("L5500 QueryPerfDate=["+PerfDate+"]");
		
		DataInsertUpdateAreaData(this.index,this.limit,queryKey,titaVo);
		DataInsertUpdateHlThreeLaqhcp(this.index,this.limit,queryKey,titaVo);
		DataInsertUpdateHlThreeDetail(this.index,this.limit,queryKey,titaVo);
		DataInsertUpdateHlEmpLnYg5Pt(this.index,this.limit,queryKey,titaVo);
		DataInsertUpdateHlCusData(this.index,this.limit,queryKey,titaVo);
		DataInsertUpdateHlAreaLnYg6Pt(this.index,this.limit,queryKey,titaVo);
		
//		List<String[]> dataL5500=l5500ServiceImpl.FindData(this.index, this.limit, sqlL5500, queryKey, titaVo);
//		if(dataL5500!=null && dataL5500.size()!=0) {
//			for(String [] lData:dataL5500) {
//				
//			}
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public void DataInsertUpdateHlAreaLnYg6Pt(int index,int limit,Map<String,String> queryKey,TitaVo titaVo) throws LogicException {
		String sqlHlAreaLnYg6Pt="";
		if(sqlHlAreaLnYg6Pt!=null && sqlHlAreaLnYg6Pt.length()!=0) {
			List<String[]> dataHlAreaLnYg6Pt=l5500ServiceImpl.FindData(index,limit, sqlHlAreaLnYg6Pt, null, titaVo);
			List<HlAreaLnYg6Pt> lHlAreaLnYg6Pt=new ArrayList<HlAreaLnYg6Pt>();
			//List<String> key=new ArrayList<String>();
			if(dataHlAreaLnYg6Pt!=null && dataHlAreaLnYg6Pt.size()!=0) {
				for(String [] lDataHlAreaLnYg6Pt:dataHlAreaLnYg6Pt) {
					HlAreaLnYg6Pt tHlAreaLnYg6Pt=new HlAreaLnYg6Pt();
					HlAreaLnYg6PtId tHlAreaLnYg6PtId=new HlAreaLnYg6PtId();
//					tHlAreaLnYg6PtId.setAreaUnitNo(areaUnitNo);
//					tHlAreaLnYg6PtId.setWorkYM(workYM);
					
//					tHlAreaLnYg6PtId.setAreaUnitNo(areaUnitNo);
//					tHlAreaLnYg6PtId.setWorkYM(workYM);
					tHlAreaLnYg6Pt.setHlAreaLnYg6PtId(tHlAreaLnYg6PtId);
					tHlAreaLnYg6Pt.setAreaUnitNo(tHlAreaLnYg6PtId.getAreaUnitNo());
					tHlAreaLnYg6Pt.setWorkYM(tHlAreaLnYg6PtId.getWorkYM());
					if(tHlAreaLnYg6Pt!=null && !lHlAreaLnYg6Pt.contains(tHlAreaLnYg6Pt)) {
						lHlAreaLnYg6Pt.add(tHlAreaLnYg6Pt);
					}
				}
				
			}
			try {
				sHlAreaLnYg6PtService.insertAll(lHlAreaLnYg6Pt, titaVo);
			} catch (DBException e) {
				this.info("L5500 HlAreaLnYg6Pt["+e.getErrorMsg()+"]");
				throw new LogicException(titaVo, "E0005", "HlAreaLnYg6Pt");
			}
		}
	}


	public void DataInsertUpdateHlEmpLnYg5Pt(int index,int limit,Map<String,String> queryKey,TitaVo titaVo) throws LogicException {
		String sqlHlEmpLnYg5Pt="SELECT \"WorkMonth\",\"EmpNo\",\"Fullname\",\"AreaCode\",\"AreaItem\",\"DeptCode\",\"DepItem\",\"DistCode\",\"DistItem\",\"GoalAmt\" FROM \"PfBsOfficer\" WHERE (\"WorkMonth\"!=0 AND \"EmpNo\" IS NOT NULL AND \"AreaCode\" IS NOT NULL) AND \"LastUpdate\">=to_date(:PerfDate,'YYYYMMDD') ";
		sqlHlEmpLnYg5Pt=sqlHlEmpLnYg5Pt+sqlRow;
		if(sqlHlEmpLnYg5Pt!=null && sqlHlEmpLnYg5Pt.length()!=0) {
			List<String[]> dataHlEmpLnYg5Pt=l5500ServiceImpl.FindData(index,limit, sqlHlEmpLnYg5Pt, queryKey, titaVo);
			List<HlEmpLnYg5Pt> lHlEmpLnYg5Pt=new ArrayList<HlEmpLnYg5Pt>();
			List<HlEmpLnYg5PtId> lHlEmpLnYg5PtId=new ArrayList<HlEmpLnYg5PtId>();
			if(dataHlEmpLnYg5Pt!=null && dataHlEmpLnYg5Pt.size()!=0) {
				for(String [] lDataHlEmpLnYg5Pt:dataHlEmpLnYg5Pt) {
					HlEmpLnYg5Pt tHlEmpLnYg5Pt=new HlEmpLnYg5Pt();
					HlEmpLnYg5PtId tHlEmpLnYg5PtId=new HlEmpLnYg5PtId();
					tHlEmpLnYg5PtId.setWorkYM(lDataHlEmpLnYg5Pt[0]);
					tHlEmpLnYg5PtId.setAreaUnitNo(lDataHlEmpLnYg5Pt[3]);
					tHlEmpLnYg5PtId.setHlEmpNo(lDataHlEmpLnYg5Pt[1]);
					
					tHlEmpLnYg5Pt.setHlEmpLnYg5PtId(tHlEmpLnYg5PtId);
//					tHlEmpLnYg5Pt.setWorkYM(tHlEmpLnYg5PtId.getWorkYM());
//					tHlEmpLnYg5Pt.setAreaUnitNo(tHlEmpLnYg5PtId.getAreaUnitNo());
//					tHlEmpLnYg5Pt.setHlEmpNo(tHlEmpLnYg5PtId.getHlEmpNo());
					
					tHlEmpLnYg5Pt.setHlEmpName(avoidTolong(lDataHlEmpLnYg5Pt[2],15));
					tHlEmpLnYg5Pt.setDeptNo(avoidTolong(lDataHlEmpLnYg5Pt[3],6));
					tHlEmpLnYg5Pt.setDeptName(avoidTolong(lDataHlEmpLnYg5Pt[4],20));
					tHlEmpLnYg5Pt.setArea(avoidTolong(lDataHlEmpLnYg5Pt[6],20));
					tHlEmpLnYg5Pt.setBranchName(avoidTolong(lDataHlEmpLnYg5Pt[8],20));
					tHlEmpLnYg5Pt.setGoalAmt(parse.stringToBigDecimal(lDataHlEmpLnYg5Pt[9]));

					if(!lHlEmpLnYg5PtId.contains(tHlEmpLnYg5PtId)) {
						lHlEmpLnYg5PtId.add(tHlEmpLnYg5PtId);
						if(tHlEmpLnYg5Pt!=null && !lHlEmpLnYg5Pt.contains(tHlEmpLnYg5Pt)) {
							lHlEmpLnYg5Pt.add(tHlEmpLnYg5Pt);
						}
					}
					
				}
				
			}
			try {
				sHlEmpLnYg5PtService.insertAll(lHlEmpLnYg5Pt, titaVo);
			} catch (DBException e) {
				this.info("L5500 HlEmpLnYg5Pt["+e.getErrorMsg()+"]");
				throw new LogicException(titaVo, "E0005", "HlEmpLnYg5Pt");
			}
		}
	}
	
	public void DataInsertUpdateHlCusData(int index,int limit,Map<String,String> queryKey,TitaVo titaVo) throws LogicException {
		String sqlHlCusData="SELECT \"CustNo\",\"CustName\",to_char(\"LastUpdate\",'YYYYMMDD') AS \"ProcessDate\" FROM \"CustMain\" WHERE (\"CustNo\"!=0 AND \"CustName\" IS NOT NULL) AND \"LastUpdate\">=to_date(:PerfDate,'YYYYMMDD') ";
		sqlHlCusData=sqlHlCusData+sqlRow;
		if(sqlHlCusData!=null && sqlHlCusData.length()!=0) {
			List<String[]> dataHlCusData=l5500ServiceImpl.FindData(index,limit, sqlHlCusData, queryKey, titaVo);
			List<HlCusData> lHlCusData=new ArrayList<HlCusData>();
			//List<String> key=new ArrayList<String>();
			if(dataHlCusData!=null && dataHlCusData.size()!=0) {
				for(String [] lDataHlCusData:dataHlCusData) {
					HlCusData tHlCusData=new HlCusData();
					tHlCusData.setHlCusNo(Long.parseLong(lDataHlCusData[0], 10));
					tHlCusData.setHlCusName(avoidTolong(lDataHlCusData[1],50));//nvarchar2(50)
					tHlCusData.setProcessDate(parse.stringToInteger(lDataHlCusData[2]));
					if(tHlCusData!=null && !lHlCusData.contains(tHlCusData)) {
						lHlCusData.add(tHlCusData);
					}
				}
				
			}
			try {
				sHlCusDataService.insertAll(lHlCusData, titaVo);
			} catch (DBException e) {
				this.info("L5500 HlCusData["+e.getErrorMsg()+"]");
				throw new LogicException(titaVo, "E0005", "HlCusData");
			}
		}
	}
	
	public void DataInsertUpdateHlThreeDetail(int index,int limit,Map<String,String> queryKey,TitaVo titaVo) throws LogicException {
		String sqlHlThreeDetail="";
		if(sqlHlThreeDetail!=null && sqlHlThreeDetail.length()!=0) {
			List<String[]> dataHlThreeDetail=l5500ServiceImpl.FindData(index,limit, sqlHlThreeDetail, null, titaVo);
			List<HlThreeDetail> lHlThreeDetail=new ArrayList<HlThreeDetail>();
			//List<String> key=new ArrayList<String>();
			if(dataHlThreeDetail!=null && dataHlThreeDetail.size()!=0) {
				for(String [] lDataHlThreeDetail:dataHlThreeDetail) {
					HlThreeDetail tHlThreeDetail=new HlThreeDetail();
					HlThreeDetailId tHlThreeDetailId=new HlThreeDetailId();
					
					tHlThreeDetail.setHlThreeDetailId(tHlThreeDetailId);

					if(tHlThreeDetail!=null && !lHlThreeDetail.contains(tHlThreeDetail)) {
						lHlThreeDetail.add(tHlThreeDetail);
					}
				}
				
			}
			try {
				sHlThreeDetailService.insertAll(lHlThreeDetail, titaVo);
			} catch (DBException e) {
				this.info("L5500 HlThreeDetail["+e.getErrorMsg()+"]");
				throw new LogicException(titaVo, "E0005", "HlThreeDetail");
			}
		}
	}
	public void DataInsertUpdateHlThreeLaqhcp(int index,int limit,Map<String,String> queryKey,TitaVo titaVo) throws LogicException {
		String sqlHlThreeLaqhcp="";
		if(sqlHlThreeLaqhcp!=null && sqlHlThreeLaqhcp.length()!=0) {
			List<String[]> dataHlThreeLaqhcp=l5500ServiceImpl.FindData(index,limit, sqlHlThreeLaqhcp, null, titaVo);
			List<HlThreeLaqhcp> lHlThreeLaqhcp=new ArrayList<HlThreeLaqhcp>();
			//List<String> key=new ArrayList<String>();
			if(dataHlThreeLaqhcp!=null && dataHlThreeLaqhcp.size()!=0) {
				for(String [] lDataHlThreeLaqhcp:dataHlThreeLaqhcp) {
					HlThreeLaqhcp tHlThreeLaqhcp=new HlThreeLaqhcp();
					HlThreeLaqhcpId tHlThreeLaqhcpId=new HlThreeLaqhcpId();
					
//					tHlThreeLaqhcpId.setBranchNo(HTLBranchNo);
//					tHlThreeLaqhcpId.setCalDate(HTLCalDate);
//					tHlThreeLaqhcpId.setDeptNo(HTLDeptNo);
//					tHlThreeLaqhcpId.setEmpNo(HTLEmpNo);
//					tHlThreeLaqhcpId.setUnitNo(HTLUnitNo);
					tHlThreeLaqhcp.setHlThreeLaqhcpId(tHlThreeLaqhcpId);

					if(tHlThreeLaqhcp!=null && !lHlThreeLaqhcp.contains(tHlThreeLaqhcp)) {
						lHlThreeLaqhcp.add(tHlThreeLaqhcp);
					}
				}
				
			}
			try {
				sHlThreeLaqhcpService.insertAll(lHlThreeLaqhcp, titaVo);
			} catch (DBException e) {
				this.info("L5500 HlThreeLaqhcp["+e.getErrorMsg()+"]");
				throw new LogicException(titaVo, "E0005", "HlThreeLaqhcp");
			}
		}
	}
	public void DataInsertUpdateAreaData(int index,int limit,Map<String,String> queryKey,TitaVo titaVo) throws LogicException {
		String sqlAreaData="SELECT NVL(\"UnitCode\",' ') AS \"區域代碼\",NVL(\"UnitItem\",' ') AS \"區域名稱\",NVL(\"EmpNo\",' ') AS \"員工代號\",NVL(\"EmpName\",' ') AS \"員工姓名\" FROM \"PfDeparment\" ";
		sqlAreaData=sqlAreaData+sqlRow;
		if(sqlAreaData!=null && sqlAreaData.length()!=0) {
			List<String[]> dataAreaData=l5500ServiceImpl.FindData(index,limit, sqlAreaData, null, titaVo);
			List<HlAreaData> lHlAreaData=new ArrayList<HlAreaData>();
			List<String> key=new ArrayList<String>();
			if(dataAreaData!=null && dataAreaData.size()!=0) {
				for(String [] lDdataAreaData:dataAreaData) {
					HlAreaData tHlAreaData=new HlAreaData();
					String AreaUnitNo=lDdataAreaData[0];
					String AreaChiefName=lDdataAreaData[3];
					tHlAreaData.setAreaUnitNo(AreaUnitNo);//區域代碼--VARCHAR2(6)
					tHlAreaData.setAreaName("");//區域名稱--VARCHAR2(20)
					tHlAreaData.setAreaChiefEmpNo(lDdataAreaData[2]);//區域主管員編--VARCHAR2(6)
					tHlAreaData.setAreaChiefName(avoidTolong(AreaChiefName,15));//區域主管名稱--NVARCHAR2(15)
					if(!key.contains(AreaUnitNo)) {
						key.add(AreaUnitNo);
						if(tHlAreaData!=null && !lHlAreaData.contains(tHlAreaData)) {
							lHlAreaData.add(tHlAreaData);
						}
					}
				}
			}
			if(lHlAreaData!=null && lHlAreaData.size()!=0) {
				try {
					sHlAreaDataService.insertAll(lHlAreaData, titaVo);
				} catch (DBException e) {
					this.info("L5500 HlAreaData["+e.getErrorMsg()+"]");
					throw new LogicException(titaVo, "E0005", "HlAreaData");
				}
			}
		}
	}
	public String avoidTolong(String str,int length) {
		if(str!=null) {
			int strL=str.length();
			if(strL<=length) {
				return str;
			}else {
				return str.substring(0,length);
			}
		}else {
			return " ";
		}
	}
	public String sqlL5500() {
		StringBuffer sbL5500=new StringBuffer();
		sbL5500.append("SELECT ");
		
		String thisSqlColumn[][]=sqlColumn;
		int thisSqlColumnL=thisSqlColumn.length;
		int thisSqlColumnL1=thisSqlColumnL-1;
		for(int i=0;i<thisSqlColumnL;i++) {
			String TBCol[]=thisSqlColumn[i];
			sbL5500.append(TBCol[0]+".\""+TBCol[1]+"\" AS \""+TBCol[0]+TBCol[1]+"\" ");
			if(i!=thisSqlColumnL1) {
				sbL5500.append(",");
			}
		}
		sbL5500.append("FROM \"PfItDetail\" PID "); //--介紹人業績明細檔
		sbL5500.append("LEFT JOIN \"PfReward\" PR "); //--介紹、協辦獎金發放檔
		sbL5500.append("ON PID.\"PerfDate\"=PR.\"PerfDate\" ");
		sbL5500.append("AND PID.\"CustNo\"=PR.\"CustNo\" ");
		sbL5500.append("AND PID.\"FacmNo\"=PR.\"FacmNo\" ");
		sbL5500.append("AND PID.\"BormNo\"=PR.\"BormNo\" ");
		sbL5500.append("LEFT JOIN \"PfBsDetail\" PBD "); //--房貸專員業績明細檔
		sbL5500.append("ON PID.\"PerfDate\"=PBD.\"PerfDate\" "); 
		sbL5500.append("AND PID.\"CustNo\"=PBD.\"CustNo\" ");
		sbL5500.append("AND PID.\"FacmNo\"=PBD.\"FacmNo\" ");
		sbL5500.append("AND PID.\"BormNo\"=PBD.\"BormNo\" ");
		sbL5500.append("WHERE 1=1 ");
		sbL5500.append("AND PID.\"PerfDate\"=:PerfDate ");
		
		sbL5500.append(sqlRow);
		return sbL5500.toString();
	}
}