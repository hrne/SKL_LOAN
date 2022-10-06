package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L5905ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Autowired
	Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;


	public List<Map<String, String>> queryresult(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L5905ServiceImpl.queryresult");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		// 取得輸入資料
		int iInqFg = Integer.parseInt(titaVo.getParam("InqFg"));//查詢條件:1.依年月份, 2.依年份區間, 3.依追蹤年月, 4.未完成追蹤
		int iConditionCode = Integer.parseInt(titaVo.getParam("ConditionCode"));//條件代碼
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));
		this.info("L5905 iInqFg=" + iInqFg + ", iConditionCode=" + iConditionCode + ", iCustNo=" + iCustNo);

		int iYearMonth = Integer.parseInt(titaVo.getParam("YearMonth"));//資料年月
		int iFYearMonth = 0;
		if (iYearMonth > 0) {
			iFYearMonth = iYearMonth + 191100;
		}
		int iYearMonthS = Integer.parseInt(titaVo.getParam("YearMonthS"));//資料年份起
		int iFYearMonthS = 0;
		int iYearMonthE = Integer.parseInt(titaVo.getParam("YearMonthE"));//資料年份迄
		int iFYearMonthE = 0;
		if (iYearMonthS > 0) {
			iFYearMonthS = iYearMonthS + 191100;
			iFYearMonthE = iYearMonthE + 191100;
		}
		int iTraceYearMonthS = Integer.parseInt(titaVo.getParam("TraceYearMonthS"));//追蹤年月起
		int iFTraceYearMonthS = 0;
		int iTraceYearMonthE = Integer.parseInt(titaVo.getParam("TraceYearMonthE"));//追蹤年月迄
		int iFTraceYearMonthE = 0;
		if (iTraceYearMonthS > 0) {
			iFTraceYearMonthS = iTraceYearMonthS + 191100;
			iFTraceYearMonthE = iTraceYearMonthE + 191100;
		}
		String iReChkMonth = titaVo.getParam("ReChkMonth");//覆審月份
		if (iInqFg == 4) {//未完成:追蹤年月未到期,尚須追蹤
			iFTraceYearMonthS = parse.stringToInteger(titaVo.getCalDy().substring(0,5))+191100;
			iFTraceYearMonthE = 999999;
		}
		this.info("L5905 iFYearMonth : " + iFYearMonth + "-" + iFYearMonthS + "-" + iFYearMonthE);
		this.info("L5905 iFTraceYearMonthS = " + iFTraceYearMonthS + "-" + iFTraceYearMonthE );

		String sql = "";
		sql += " SELECT                               			 	   					\n";
		sql += "  I.\"YearMonth\"		            	AS \"YearMonth\"				\n"; // 資料年月
		sql += " ,I.\"CustNo\"		            		AS \"CustNo\"				 	\n"; // 戶號
		sql += " ,I.\"FacmNo\" 							AS \"FacmNo\"					\n"; // 額度號碼
		sql += " ,C.\"CustName\" 						AS \"CustName\"					\n"; // 戶名
		sql += " ,I.\"ReChkYearMonth\" 					AS \"ReChkYearMonth\"			\n"; // 覆審年月
		sql += " ,I.\"ConditionCode\" 					AS \"ConditionCode\"			\n"; // 條件代碼
		sql += " ,I.\"ReCheckCode\" 		        	AS \"ReCheckCode\"				\n"; // 覆審記號
		sql += " ,I.\"FollowMark\" 						AS \"FollowMark\"				\n"; // 追蹤記號
		sql += " ,I.\"DrawdownDate\" 					AS \"DrawdownDate\"				\n"; // 撥款日期
		sql += " ,I.\"LoanBal\" 						AS \"LoanBal\"  				\n"; // 貸放餘額
		sql += " ,I.\"Evaluation\" 			    		AS \"Evaluation\"  		  		\n"; // 評等
		sql += " ,I.\"CustTypeItem\" 					AS \"CustTypeItem\"				\n"; // 客戶別
		sql += " ,I.\"UsageItem\" 						AS \"UsageItem\"				\n"; // 用途別
		sql += " ,I.\"CityItem\" 						AS \"CityItem\"					\n"; // 地區別
		sql += " ,I.\"ReChkUnit\" 						AS \"ReChkUnit\"				\n"; // 應覆審單位
		sql += " ,I.\"Remark\" 							AS \"Remark\"					\n"; // 備註
		sql += " ,I.\"TraceMonth\" 						AS \"TraceMonth\"				\n"; // 追蹤年月
		sql += " ,I.\"SpecifyFg\" 						AS \"SpecifyFg\"				\n"; // 指定複審記號

		sql += " FROM \"InnReCheck\" I													\n";
		sql += " LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = I.\"CustNo\"				\n";

		sql += " WHERE  1=1     \n";

		if (iCustNo > 0) {
			sql += " AND  I.\"CustNo\" =  :iCustNo     \n";
		}

		if (iInqFg==1) {
			sql += " AND  I.\"YearMonth\" = :iFYearMonth       \n";
			sql += " AND  I.\"ConditionCode\" = :iConditionCode       \n";
		}
		if (iInqFg == 2) {
			sql += " AND ( I.\"YearMonth\" >= :iFYearMonthS    AND   \n";
			sql += "       I.\"YearMonth\" <=  :iFYearMonthE    )   \n";
			sql += " AND  SUBSTR(SUBSTR('000000' || NVL(I.\"ReChkYearMonth\",0) , -6),5,2)  = :iReChkMonth    \n";
		}
		if (iInqFg == 3 ) {
			sql += " AND ( I.\"TraceMonth\" >= :iFTraceYearMonthS    AND   \n";
			sql += "       I.\"TraceMonth\" <= :iFTraceYearMonthE    )   \n";
		}
		if (iInqFg == 4) {
			sql += " AND ( I.\"TraceMonth\" >= :iFTraceYearMonthS     AND   \n";
			sql += "       I.\"TraceMonth\" <= :iFTraceYearMonthE    )   \n";
			sql += " AND  I.\"FollowMark\" = '2'        \n";
		}
		
		sql += "ORDER BY  I.\"YearMonth\" ,I.\"ConditionCode\" ,I.\"CustNo\" , I.\"FacmNo\" ";


		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (iCustNo > 0) {
			query.setParameter("iCustNo", iCustNo);
		}
		if (iInqFg==1) {
			query.setParameter("iFYearMonth", iFYearMonth);
			query.setParameter("iConditionCode", iConditionCode);
		}
		if (iInqFg == 2) {
			query.setParameter("iFYearMonthS", iFYearMonthS);
			query.setParameter("iFYearMonthE", iFYearMonthE);
			query.setParameter("iReChkMonth", iReChkMonth);
		}
		if (iInqFg == 3 ) {
			query.setParameter("iFTraceYearMonthS", iFTraceYearMonthS);
			query.setParameter("iFTraceYearMonthE", iFTraceYearMonthE);
		}
		if (iInqFg == 4 ) {
			query.setParameter("iFTraceYearMonthS", iFTraceYearMonthS);
			query.setParameter("iFTraceYearMonthE", iFTraceYearMonthE);
		}

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);
		
		return this.convertToMap(query);

	}

	public int getSize() {
		return cnt;
	}

}