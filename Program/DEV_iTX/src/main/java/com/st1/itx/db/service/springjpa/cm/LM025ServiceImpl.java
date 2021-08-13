package com.st1.itx.db.service.springjpa.cm;

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

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM025ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM025ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, int caseType) throws Exception {

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		String rateCode="";
		if(caseType==1){
			//1跟4 為浮動利率
			 rateCode = "1,4";
		}else if(caseType==0){
			//2跟3 為固定利率
			 rateCode = "2,3";
		}

		logger.info("lM025.findAll ");
		String sql = "";
		sql += "	SELECT S.\"MonthEndYm\" AS \"YearMonth\"";
		sql += "	      ,LPAD(TO_CHAR(S.\"CustNo\"),7,'0') || LPAD(TO_CHAR(S.\"FacmNo\"),3,'0') || LPAD(TO_CHAR(S.\"BormNo\"),3,'0') AS \"Account\"";
		sql += "          ,S.\"ReNewRate\"";
		sql += "          ,S.\"StoreRate\" / 100 AS \"StoreRate\"";
		sql += "          ,S.\"LoanBalance\"";
		sql += "    FROM(SELECT D.\"MonthEndYm\"";
		sql += "               ,D.\"CustNo\"";
		sql += "               ,D.\"FacmNo\"";
		sql += "               ,D.\"BormNo\"";
		sql += "               ,D.\"StoreRate\"";
		sql += "               ,D.\"LoanBalance\"";
		sql += "               ,F.\"ProdNo\"";
		sql += "               ,L.\"RateCode\"";
		sql += "               ,CASE";
		sql += " 				  WHEN F.\"IfrsStepProdCode\" = 'A' THEN 3";
		sql += " 				  WHEN F.\"IfrsStepProdCode\" = 'B' THEN 4";
		sql += " 				  WHEN F.\"IfrsStepProdCode\" = ' ' THEN DECODE(L.\"RateCode\",2,2,1)";
		sql += " 				ELSE 0 END AS \"ReNewRate\"";
		sql += " 		 FROM \"DailyLoanBal\" D";
		sql += "    	 LEFT JOIN \"FacProd\" F ON F.\"ProdNo\" = D.\"ProdNo\"";
		sql += "    	 LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = D.\"CustNo\"";
		sql += "    	  						    AND L.\"FacmNo\" = D.\"FacmNo\"";
		sql += "    	  						    AND L.\"BormNo\" = D.\"BormNo\"";
		sql += "      WHERE D.\"MonthEndYm\" = :entdy";
		sql += "        AND D.\"LoanBalance\" > 0 ) S ";
		sql += "      WHERE S.\"ReNewRate\" IN ("+ rateCode +")";
		sql += "      ORDER BY \"CustNo\",\"FacmNo\",\"BormNo\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}
	
//	public List<Map<String, String>> findData(TitaVo titaVo) throws Exception {
//	public List<Map<String, String>> findData(TitaVo titaVo, String yyyymm, int caseType) throws Exception {
//		// Wei建議1:caseType變數名稱宣告不符專案規定,必須改為caseType
//		// caseType 1:固定 2:機動
//		logger.info("lM025.findAll ");
//		String sql = "";
//
//		sql += " SELECT \"F1\" \"利率\"";
//		sql += "       ,SUM(F2) \"筆數\"";
//		sql += "       ,SUM(F3) \"金額合計\"";
//		sql += " FROM( ";
//
//		// Wei建議2:根據caseType變動的數字是0.1和0.25,可改為變數傳入
//		switch (caseType) {
//		case 1:
//			// 固定
//			sql += "SELECT TRUNC(M.\"StoreRate\" / 0.1 ) * 0.1  F1,1 F2, M.\"LoanBalance\" F3 ";
//			break;
//		case 2:
//			// 機動
//			sql += "SELECT TRUNC(M.\"StoreRate\" / 0.25) * 0.25 F1,1 F2, M.\"LoanBalance\" F3 ";
//			break;
//		default:
//			break;
//
//		}
//
//		sql += "FROM \"MonthlyLoanBal\" M ";
//		sql += "LEFT JOIN \"LoanBorMain\" L ";
//		sql += "ON  L.\"CustNo\"=M.\"CustNo\"  ";
//		sql += "AND L.\"FacmNo\"=M.\"FacmNo\" ";
//		sql += "AND L.\"BormNo\"=M.\"BormNo\" ";
//		sql += "WHERE M.\"YearMonth\" = :yyyymm ";
//
//		switch (caseType) {
//		case 1:
//			// 固定
//			sql += "AND  L.\"RateCode\" =  '2') ";
//			break;
//		case 2:
//			// 機動
//			sql += "AND  L.\"RateCode\" !=  '2') ";
//			break;
//		default:
//			break;
//
//		}
//
//		sql += "GROUP BY  \"F1\" ";
//		sql += "ORDER BY  \"F1\" ";
//
//		logger.info("sql=" + sql);
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
//		query = em.createNativeQuery(sql);
//		query.setParameter("yyyymm", yyyymm);
//		return this.convertToMap(query.getResultList());
//		return null;
//	}

}