package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5951ServiceImpl")
@Repository
public class L5951ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5951ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

//	//計件代碼於L5951查詢條件須排除
//	String strPieceCodeNotIn ="'G','G1','G2','GA','GB','GC','GD','GF','GG','GH','GJ','GA', '1B','1C','1D'"; 
//	//排除[團體意外險部]
//	String strDeptCodeNotIn="'109000', 'A0Y000'";
	public String getSqlL5951(String SumByFacm,String PieceCodeNotIn[],String DeptCodeNotIn[]) {
//		if(PieceCodeNotIn!=null && PieceCodeNotIn.trim().length()!=0) {
//			strPieceCodeNotIn=PieceCodeNotIn;
//		}
//		if(DeptCodeNotIn!=null && DeptCodeNotIn.trim().length()!=0) {
//			strDeptCodeNotIn=DeptCodeNotIn;
//		}
		int PieceCodeNotInL=0;
		if(PieceCodeNotIn!=null) {
			PieceCodeNotInL=PieceCodeNotIn.length;
		}
		int DeptCodeNotInL=0;
		if(DeptCodeNotIn!=null) {
			DeptCodeNotInL=DeptCodeNotIn.length;
		}
		
		String sqlL5951="";
		sqlL5951+="SELECT BSOF.\"DepItem\" AS \"部室別\" ";
		sqlL5951+=",EBS.\"Fullname\" AS \"房貸專員姓名\" ";
		sqlL5951+=",B.\"BsOfficer\" AS \"房貸專員員編\" ";
		sqlL5951+=",C.\"CustName\" AS \"戶名\" ";
		sqlL5951+=",I.\"CustNo\" AS \"戶號\" ";
		sqlL5951+=",I.\"FacmNo\" AS \"額度\" ";
		sqlL5951+=",I.\"BormNo\" AS \"撥款序號\" ";
		sqlL5951+=",I.\"DrawdownDate\" AS \"撥款日\" ";
		sqlL5951+=",I.\"ProdCode\" AS \"利率代碼\" ";
		sqlL5951+=",I.\"PieceCode\" AS \"計件代碼\" ";
		sqlL5951+=",I.\"CntingCode\" AS \"是否計件\" ";
		sqlL5951+=",I.\"DrawdownAmt\" AS \"撥款金額\" ";
		sqlL5951+=",I.\"DeptCode\" AS \"介紹人部市代號\" ";
		sqlL5951+=",I.\"DistCode\" AS \"介紹人區部代號\" ";
		sqlL5951+=",I.\"UnitCode\" AS \"介紹人區域代號\" ";
		sqlL5951+=",DeptCB.\"UnitItem\" AS \"介紹人部市名稱\" ";
		sqlL5951+=",DistCB.\"UnitItem\" AS \"介紹人區部名稱\" ";
		sqlL5951+=",UnitCB.\"UnitItem\" AS \"介紹人區域名稱\" ";
		sqlL5951+=",EI.\"Fullname\" AS \"介紹人姓名\" ";
		sqlL5951+=",I.\"Introducer\" AS \"介紹人員編\" ";
		sqlL5951+=",I.\"DeptManager\" AS \"部經理代號\" ";
		sqlL5951+=",DeptE.\"Fullname\" AS \"部經理名稱\" ";
		sqlL5951+=",I.\"DistManager\" AS \"區經理\" ";
		sqlL5951+=",DistE.\"Fullname\" AS \"區經理名稱\" ";
		sqlL5951+=",I.\"UnitManager\" AS \"處經理\" ";
		sqlL5951+=",UnitE.\"Fullname\" AS \"處經理名稱\" ";
//		if(("Y").equals(SumByFacm)) {
//			sqlL5951+=",S.\"SumPerfEqAmt\" AS \"三階換算業績\" ";
//			sqlL5951+=",S.\"SumPerfReward\" AS \"三階業務報酬\" ";
//			sqlL5951+=",S.\"SumPerfAmt\" AS \"已用額度\" ";
//		}else {
			sqlL5951+=",I.\"PerfEqAmt\" AS \"三階換算業績\" ";
			sqlL5951+=",I.\"PerfReward\" AS \"三階業務報酬\" ";
			sqlL5951+=",I.\"PerfAmt\" AS \"已用額度\" ";
//		}
		
		sqlL5951+=",I.\"WorkMonth\" AS \"工作月\" ";
		sqlL5951+=",I.\"PerfDate\" AS \"業績日期\" ";
		sqlL5951+="FROM \"PfItDetail\" I ";
		sqlL5951+="LEFT JOIN \"PfBsDetail\" B ";
		sqlL5951+="ON I.\"PerfDate\" = B.\"PerfDate\" ";
		sqlL5951+="and I.\"CustNo\" = B.\"CustNo\" ";
		sqlL5951+="and I.\"FacmNo\" = B.\"FacmNo\" ";
		sqlL5951+="and I.\"BormNo\" = B.\"BormNo\" ";
		sqlL5951+="LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\" ";
		sqlL5951+="LEFT JOIN \"CdEmp\" EBS ON EBS.\"EmployeeNo\" = B.\"BsOfficer\" ";
		
		sqlL5951+="LEFT JOIN \"PfBsOfficer\" BSOF ";
		sqlL5951+="ON BSOF.\"WorkMonth\"=B.\"WorkMonth\" AND BSOF.\"EmpNo\"=B.\"BsOfficer\" ";
		sqlL5951+="LEFT JOIN \"CdEmp\" EI ON EI.\"EmployeeNo\" = I.\"Introducer\" ";
		sqlL5951+="LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = B.\"DeptCode\" ";
		sqlL5951+="LEFT JOIN \"CdBcm\" DeptCB ON DeptCB.\"UnitCode\" = I.\"DeptCode\" ";
		sqlL5951+="LEFT JOIN \"CdBcm\" DistCB ON DistCB.\"UnitCode\" = I.\"DistCode\" ";
		sqlL5951+="LEFT JOIN \"CdBcm\" UnitCB ON UnitCB.\"UnitCode\" = I.\"UnitCode\" ";
		sqlL5951+="LEFT JOIN \"CdEmp\" DeptE ON DeptE.\"EmployeeNo\" = I.\"DeptManager\" ";
		sqlL5951+="LEFT JOIN \"CdEmp\" DistE ON DistE.\"EmployeeNo\" = I.\"DistManager\" ";
		sqlL5951+="LEFT JOIN \"CdEmp\" UnitE ON UnitE.\"EmployeeNo\" = I.\"UnitManager\" ";
		
//		if(("Y").equals(SumByFacm)) {
//			String sqlSumL5951="";
//			sqlSumL5951+="SELECT \"Introducer\",\"CustNo\",\"FacmNo\",SUM(NVL(\"PerfEqAmt\",0)) AS \"SumPerfEqAmt\",SUM(NVL(\"PerfReward\",0)) AS \"SumPerfReward\",SUM(NVL(\"PerfAmt\",0)) AS \"SumPerfAmt\" ";
//			sqlSumL5951+="FROM \"PfItDetail\" ";
//			sqlSumL5951+="WHERE \"Introducer\" IS NOT NULL ";
//			sqlSumL5951+="AND \"CntingCode\" IN ('Y','N')";
//			
//			sqlSumL5951+="AND \"WorkMonth\" BETWEEN :WorkMonthFm AND :WorkMonthTo ";
//			if(PieceCodeNotInL!=0) {
//				sqlSumL5951+="AND \"PieceCode\" NOT IN ( ";
//				for(int i=0;i<PieceCodeNotInL;i++) {
//					sqlSumL5951+=":PieceCodeNotIn"+i+" ";
//					if(i!=PieceCodeNotInL-1) {
//						sqlSumL5951+=",";
//					}
//				}
//				sqlSumL5951+=") ";
//			}
//
//			if(DeptCodeNotInL!=0) {
//				sqlSumL5951+="AND \"DeptCode\" NOT IN ( ";
//				for(int i=0;i<DeptCodeNotInL;i++) {
//					sqlSumL5951+=":DeptCodeNotIn"+i+" ";
//					if(i!=DeptCodeNotInL-1) {
//						sqlSumL5951+=",";
//					}
//				}
//				sqlSumL5951+=") ";
//			}
//			sqlSumL5951+="AND \"BormNo\">0 ";
//			sqlSumL5951+="GROUP BY \"Introducer\",\"CustNo\",\"FacmNo\" ORDER BY \"Introducer\",\"CustNo\",\"FacmNo\" ";
//			
//			logger.info("L5959.sqlSumL5951=" + sqlSumL5951);
//			
//			sqlL5951+="LEFT JOIN ("+sqlSumL5951+") S ON S.\"Introducer\"=I.\"Introducer\" AND S.\"CustNo\"=I.\"CustNo\" AND S.\"FacmNo\"=I.\"FacmNo\" ";
//		}

//		sqlL5951+="WHERE 1=1 ";
		sqlL5951+="WHERE I.\"CntingCode\" IN ('Y','N') ";
		sqlL5951+="AND I.\"Introducer\" IS NOT NULL ";
		
		if(PieceCodeNotInL!=0) {
			sqlL5951+="AND I.\"PieceCode\" NOT IN ( ";
			for(int i=0;i<PieceCodeNotInL;i++) {
				sqlL5951+=":PieceCodeNotIn"+i+" ";
				if(i!=PieceCodeNotInL-1) {
					sqlL5951+=",";
				}
			}
			sqlL5951+=") ";
		}
		
		if(DeptCodeNotInL!=0) {
			sqlL5951+="AND I.\"DeptCode\" NOT IN ( ";
			for(int i=0;i<DeptCodeNotInL;i++) {
				sqlL5951+=":DeptCodeNotIn"+i+" ";
				if(i!=DeptCodeNotInL-1) {
					sqlL5951+=",";
				}
			}
			sqlL5951+=") ";
		}
		
		sqlL5951+="AND I.\"BormNo\">0 ";
		sqlL5951+="AND I.\"WorkMonth\" BETWEEN :WorkMonthFm AND :WorkMonthTo ";
		
//		sqlL5951+="ORDER BY I.\"DeptCode\",I.\"DistCode\",I.\"UnitCode\",I.\"Introducer\",I.\"CustNo\",I.\"FacmNo\",I.\"BormNo\" ";
		sqlL5951+="ORDER BY I.\"Introducer\",I.\"CustNo\",I.\"FacmNo\",I.\"BormNo\" ";
		
		sqlL5951+=sqlRow;
		
		
		logger.info("L5959.sqlL5951=" + sqlL5951);
		
		return sqlL5951;
	}
	
	public List<String[]> FindData(int index, int limit, String sql,Map<String,String> queryKey, TitaVo titaVo) throws LogicException {
		logger.info("FindData");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		logger.info("JcicServiceImpl sql=[" + sql + "]");
		

		Query query;
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		logger.info("JcicServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		if(queryKey!=null && queryKey.size()!=0) {
			for(String key:queryKey.keySet()) {
				logger.info("JcicService FindJcic Key=["+key+"],keyValue=["+queryKey.get(key)+"]");
				query.setParameter(key, queryKey.get(key));
			}
		}
		
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		
		@SuppressWarnings("unchecked")
		List<Object> lObject=this.convertToMap(query.getResultList());
		
		return FindData(lObject);
	}
	@SuppressWarnings("unchecked")
	public List<String[]> FindData(List<Object> lObject) throws LogicException{
		List<String[]> data=new ArrayList<String[]>();
		if(lObject!=null && lObject.size()!=0) {
			int col=((Map<String, String>)lObject.get(0)).keySet().size();
			for(Object obj :lObject) {
				Map<String,String> MapObj=(Map<String, String>) obj;
				String row[]=new String[col];
				for(int i=0;i<col;i++) {
					row[i]=MapObj.get("F"+String.valueOf(i));
					if(row[i]!=null && row[i].length()!=0) {
						
					}else {
						row[i]="";
					}
				}
				data.add(row);
			}
		}
		return data;
	}
}