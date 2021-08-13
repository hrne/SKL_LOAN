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
import com.st1.itx.eum.ContentName;

@Service
@Repository
/* 逾期放款明細 */
public class LQ001ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LQ001ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	// 20201211 ted 增加SQL
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lQ001.findAll ");
		// 20200430
		String lastENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000 - 10000);
		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		// 月
		String iMM = iENTDY.substring(4, 6);
		// 年
		String lastYEAR = lastENTDY.substring(0, 4);
		String nowYEAR = iENTDY.substring(0, 4);

		String lastsd = "";
		String lasted = "";
		String nowsd = "";
		String nowed = "";

		switch (iMM) {
		case "01":
		case "02":
		case "03":
			lastsd = lastYEAR + "1001";
			lasted = lastYEAR + "1231";
			nowsd = nowYEAR + "0101";
			nowed = nowYEAR + "0331";
			break;
		case "04":
		case "05":
		case "06":
			lastsd = nowYEAR + "0101";
			lasted = nowYEAR + "0331";
			nowsd = nowYEAR + "0401";
			nowed = nowYEAR + "0630";
			break;
		case "07":
		case "08":
		case "09":
			lastsd = nowYEAR + "0401";
			lasted = nowYEAR + "0630";
			nowsd = nowYEAR + "0701";
			nowed = nowYEAR + "0930";
			break;
		case "10":
		case "11":
		case "12":
			lastsd = nowYEAR + "0701";
			lasted = nowYEAR + "0930";
			nowsd = nowYEAR + "1001";
			nowed = nowYEAR + "1231";
			break;
		}
		logger.info("SQLlastsd:" + lastsd);
		logger.info("SQLlasted:" + lasted);
		logger.info("SQLnowsd:" + nowsd);
		logger.info("SQLnowed:" + nowed);
//		sql += "			,CC.\"AS400CityCode\" AS F1";
		String sql = "SELECT CC.\"CityItem\" AS F0";
		sql += "			,A1.\"C1\" AS F1";
		sql += "			,A2.\"C2\" AS F2";
		sql += "			,A2.\"C3\" AS F3";
		sql += "			,A3.\"C4\" AS F4";
		sql += "			,A3.\"C5\" AS F5";
		sql += "			,A5.\"C6\" AS F6";
		sql += "			,A4.\"C7\" AS F7";
		sql += "			,A5.\"C8\" AS F8";
		sql += "			,A5.\"C9\" AS F9";
		sql += "			,A4.\"C10\" AS F10";
		sql += "	  FROM \"CdCity\" CC";
		// A1 季末住宅餘額 C1
		sql += "	  LEFT JOIN(SELECT CM.\"CityCode\"";
		sql += "					  ,SUM(DLB.\"LoanBalance\") \"C1\"";
		sql += "				FROM \"DailyLoanBal\" DLB";
		sql += "	  LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = DLB.\"CustNo\"";
		sql += "							AND CF.\"FacmNo\" = DLB.\"FacmNo\"";
		sql += "							AND CF.\"MainFlag\" = 'Y'";
		sql += "	  LEFT JOIN \"ClMain\" CM ON CF.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "							 AND CF.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "							 AND CF.\"ClNo\" = CM.\"ClNo\"";
		sql += "	  WHERE DLB.\"AcctCode\" IN('310', '320', '330', '340')";
		sql += "		AND CF.\"ClCode1\" = '1'";
		sql += "		AND CF.\"ClCode2\" = '1'";
		sql += "		AND DLB.\"DataDate\" = :nowed ";// 本季底
		sql += "	  GROUP BY CM.\"CityCode\") A1";
		sql += "	  ON A1.\"CityCode\" = CC.\"CityCode\"";
		// A2 本季核准金額 C2  筆數 C3
		sql += "	  LEFT JOIN(SELECT CM.\"CityCode\"";
		sql += "					  ,SUM(FM.\"LineAmt\") \"C2\"";
		sql += "					  ,COUNT(*) \"C3\"";
		sql += "				FROM \"FacMain\" FM";
		sql += "				LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = FM.\"ApplNo\"";
		sql += "				LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = FM.\"CustNo\"";
		sql += "									  AND CF.\"FacmNo\" = FM.\"FacmNo\"";
		sql += "									  AND CF.\"MainFlag\" = 'Y'";
		sql += "				LEFT JOIN \"ClMain\" CM ON CF.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "									   AND CF.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "									   AND CF.\"ClNo\" = CM.\"ClNo\"";
		sql += "				WHERE FM.\"AcctCode\" IN('310', '320', '330', '340')";
		sql += "				  AND CF.\"ClCode1\" = '1'";
		sql += "				  AND CF.\"ClCode2\" = '1'";
		sql += "				  AND FCA.\"ApproveDate\" >= :nowsd "; // 本季初
		sql += "				  AND FCA.\"ApproveDate\" <= :nowed "; // 本季底
		sql += "				  GROUP BY CM.\"CityCode\") A2";
		sql += "	  ON A2.\"CityCode\" = CC.\"CityCode\"";
		// A3 季末平均利率 C4  季末總利息 C5
		sql += "	  LEFT JOIN(SELECT CM.\"CityCode\"";
		sql += "					  ,ROUND(SUM(DLB.\"StoreRate\") / COUNT(*), 4) \"C4\"";
		sql += "					  ,SUM(DLB.\"IntAmtRcv\") \"C5\"";
		sql += "				FROM \"DailyLoanBal\" DLB";
		sql += "	  			LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = DLB.\"CustNo\"";
		sql += "									  AND CF.\"FacmNo\" = DLB.\"FacmNo\"";
		sql += "									  AND CF.\"MainFlag\" = 'Y'";
		sql += "	  			LEFT JOIN \"ClMain\" CM ON CF.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "							 		   AND CF.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "							 		   AND CF.\"ClNo\" = CM.\"ClNo\"";
		sql += "	  			WHERE DLB.\"AcctCode\" IN('310', '320', '330', '340')";
		sql += "				  AND DLB.\"DataDate\" = :nowed ";// 本季底
		sql += "	  			GROUP BY CM.\"CityCode\") A3";
		sql += "	  ON A3.\"CityCode\" = CC.\"CityCode\"";
		// A4 本季總利息C7 本季比數C10
		sql += "	  LEFT JOIN(SELECT CM.\"CityCode\"";
		sql += "					  ,SUM(DLB.\"IntAmtRcv\") \"C7\"";
		sql += "					  ,COUNT(*) \"C10\"";
		sql += "				FROM \"DailyLoanBal\" DLB";
		sql += "				LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = DLB.\"CustNo\"";
		sql += "									  AND CF.\"FacmNo\" = DLB.\"FacmNo\"";
		sql += "									  AND CF.\"MainFlag\" = 'Y'";
		sql += "				LEFT JOIN \"ClMain\" CM ON CF.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "									   AND CF.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "									   AND CF.\"ClNo\" = CM.\"ClNo\"";
		sql += "				WHERE DLB.\"AcctCode\" IN('310', '320', '330', '340')";
		sql += "				  AND DLB.\"DataDate\" >= :nowsd "; // 本季初
		sql += "				  AND DLB.\"DataDate\" <= :nowed "; // 本季底
		sql += "				GROUP BY CM.\"CityCode\") A4";
		sql += "	  ON A4.\"CityCode\" = CC.\"CityCode\"";
		// --季增平均利率C6 季增餘額C8 季增平均期數C9
		sql += "	  LEFT JOIN(SELECT CM.\"CityCode\"";
		sql += "					  ,ROUND((";
		sql += "					       SUM(CASE";
		sql += "						 	     WHEN DLB.\"DataDate\" >= :nowsd AND DLB.\"DataDate\" <= :nowed THEN DLB.\"StoreRate\"";// 本季初/底
		sql += "					   		   ELSE 0 END) - ";
		sql += "					       SUM(CASE";
		sql += "								 WHEN DLB.\"DataDate\" >= :lastsd AND DLB.\"DataDate\" <= :lasted THEN DLB.\"StoreRate\"";// 上季初/底
		sql += "							   ELSE 0 END))/ COUNT(*),4 ) AS \"C6\"";
		sql += "					  ,ROUND((";
		sql += "						   SUM(CASE";
		sql += "								 WHEN DLB.\"DataDate\" >= :nowsd AND DLB.\"DataDate\" <= :nowed THEN DLB.\"LoanBalance\""; // 本季初/底
		sql += "							   ELSE 0 END) - ";
		sql += "						   SUM(CASE";
		sql += "								 WHEN DLB.\"DataDate\" >= :lastsd AND DLB.\"DataDate\" <= :lasted THEN DLB.\"LoanBalance\"";// 上季初/底
		sql += "							   ELSE 0 END)) / COUNT(*),0) AS \"C8\"";
		sql += "					  ,ROUND((";
		sql += "						   SUM(CASE";
		sql += "								 WHEN DLB.\"DataDate\" >= :nowsd AND DLB.\"DataDate\" <= :nowed THEN LBM.\"TotalPeriod\"";// 本季初/底
		sql += "							   ELSE 0 END) - ";
		sql += "						   SUM(CASE";
		sql += "								 WHEN DLB.\"DataDate\" >= :lastsd AND DLB.\"DataDate\" <= :lasted THEN LBM.\"TotalPeriod\"";// 上季初/底
		sql += "						   	   ELSE 0 END)) / COUNT(*),0) AS \"C9\"";
		sql += "					  ,COUNT(*) AS \"C10\"";
		sql += "				FROM \"DailyLoanBal\" DLB";
		sql += "				LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = DLB.\"CustNo\"";
		sql += "									  AND CF.\"FacmNo\" = DLB.\"FacmNo\"";
		sql += "									  AND CF.\"MainFlag\" = 'Y'";
		sql += "			    LEFT JOIN \"ClMain\" CM ON CF.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "									   AND CF.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "									   AND CF.\"ClNo\" = CM.\"ClNo\"";
		sql += "				LEFT JOIN (SELECT \"CustNo\"";
		sql += "								 ,\"FacmNo\"";
		sql += "								 ,ROUND(SUM(\"TotalPeriod\") / COUNT(*), 0) AS \"TotalPeriod\"";
		sql += "						   FROM \"LoanBorMain\"";
		sql += "					       GROUP BY \"CustNo\",\"FacmNo\") LBM";
		sql += "	  			ON LBM.\"CustNo\" = DLB.\"CustNo\" AND LBM.\"FacmNo\" = DLB.\"FacmNo\"";
		sql += "	  			WHERE DLB.\"AcctCode\" IN('310', '320', '330', '340')";
		sql += "				GROUP BY CM.\"CityCode\") A5";
		sql += "	  ON A5.\"CityCode\" = CC.\"CityCode\"";
		sql += "	  ORDER BY CC.\"CityCode\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("lastsd", lastsd);
		query.setParameter("lasted", lasted);
		query.setParameter("nowsd", nowsd);
		query.setParameter("nowed", nowed);

		return this.convertToMap(query.getResultList());
	}

}

//String sql = "SELECT mcc.\"CityItem\" AS F0";
//sql += "			,pone.\"TLB\" AS F1";
//sql += "			,pone.\"LineAmt\" AS F2";
//sql += "			,pone.\"CCC\" AS F3";
//sql += "			,ptwo.\"AvgRate\" AS F4";
//sql += "			,ptwo.\"IntAmtRcv\" AS F5";
//sql += "			,ptwo.\"seasonInRATE\" AS F6";
//sql += "			,ptwo.\"LoanBalance\" AS F7";
//sql += "			,ptwo.\"seasonInLoanBalance\" AS F8";
//sql += "			,ptwo.\"seasonInTotalPeriod\" AS F9";
//sql += "			,ptwo.\"TotalPeriod\" AS F10";
//sql += "	  FROM \"CdCity\" mcc";
//sql += "	  LEFT JOIN(SELECT cc.\"CityCode\"";
//sql += "					  ,SUM(dlb.\"LoanBalance\") AS tlb";
//sql += "					  ,fm.\"LineAmt\"";
//sql += "				      ,COUNT(cm.\"CityCode\") AS ccc";
//sql += "					  ,fca.\"ApproveDate\"";
//sql += "				FROM \"DailyLoanBal\" dlb";
//sql += "				LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = dlb.\"CustNo\" AND fm.\"FacmNo\" = dlb.\"FacmNo\"";
//sql += "				LEFT JOIN \"FacCaseAppl\" fca ON fca.\"ApplNo\" = fm.\"ApplNo\"";
//sql += "				LEFT JOIN \"ClFac\" cf ON cf.\"CustNo\" = dlb.\"CustNo\"";
//sql += "									  AND cf.\"FacmNo\" = dlb.\"FacmNo\"";
//sql += "									  AND cf.\"MainFlag\" = 'Y'";
//sql += "				LEFT JOIN \"ClMain\" cm ON cm.\"ClCode1\" = cf.\"ClCode1\"";
//sql += "									   AND cm.\"ClCode2\" = cf.\"ClCode2\"";
//sql += "									   AND cm.\"ClNo\" = cf.\"ClNo\"";
//sql += "				LEFT JOIN \"CdCity\" cc ON cc.\"CityCode\" = cm.\"CityCode\"";
//sql += "				WHERE fm.\"AcctCode\" IN ('310','320','330','340')";
//sql += "				  AND cf.\"ClCode1\" = '1'";
//sql += "				  AND cf.\"ClCode2\" = '1'";
//sql += "				  AND \"ApproveDate\" >= :nowsd"; // 當季日期(起)
//sql += "				  AND \"ApproveDate\" <= :nowed";
//sql += "				GROUP BY cc.\"CityCode\"";
//sql += "						,fm.\"LineAmt\"";
//sql += "						,fca.\"ApproveDate\") pone";
//sql += "	   ON pone.\"CityCode\" = mcc.\"CityCode\"";
//sql += "	   LEFT JOIN(SELECT a1.\"AVGRATE\" AS \"AvgRate\"";
//sql += "					   ,a1.\"IntAmtRcv\" AS \"IntAmtRcv\"";
//sql += "					   ,(CASE";
//sql += "						   WHEN a1.\"ApproveDate\" >= :nowsd AND a1.\"ApproveDate\" <= :nowed THEN a1.\"AVGRATE\"";
//sql += "						 ELSE 0 END)-";
//sql += "						(CASE";
//sql += "						   WHEN a1.\"ApproveDate\" >= :lastsd AND a1.\"ApproveDate\" <= :lasted THEN a1.\"AVGRATE\"";
//sql += "						 ELSE 0 END) AS \"seasonInRATE\"";
//sql += "					   ,a1.\"LoanBalance\"";
//sql += "					   ,(CASE";
//sql += "						   WHEN a1.\"ApproveDate\" >= :nowsd AND a1.\"ApproveDate\" <= :nowed THEN a1.\"LoanBalance\"";
//sql += "						 ELSE 0 END)-";
//sql += "						(CASE";
//sql += "						   WHEN a1.\"ApproveDate\" >= :lastsd AND a1.\"ApproveDate\" <= :lasted THEN a1.\"LoanBalance\"";
//sql += "						 ELSE 0 END) AS \"seasonInLoanBalance\"";
//sql += "					   ,(CASE";
//sql += "						   WHEN a1.\"ApproveDate\" >= :nowsd AND a1.\"ApproveDate\" <= :nowed THEN a2.\"TotalPeriod\"";
//sql += "						 ELSE 0 END)-";
//sql += "						(CASE";
//sql += "						   WHEN a1.\"ApproveDate\" >= :lastsd AND a1.\"ApproveDate\" <= :lasted THEN a2.\"TotalPeriod\"";
//sql += "						 ELSE 0 END) AS \"seasonInTotalPeriod\"";
//sql += "					   ,a2.\"TotalPeriod\"";
//sql += "					   ,a1.\"ApproveDate\"";
//sql += "					   ,a1.\"CityCode\"";
//sql += "				FROM(SELECT dlb.\"CustNo\"";
//sql += "						   ,dlb.\"FacmNo\"";
//sql += "						   ,fca.\"ApproveDate\"";
//sql += "						   ,round(SUM(dlb.\"StoreRate\") / DECODE(COUNT(cm.\"CityCode\"), 0, 1, COUNT(cm.\"CityCode\"), COUNT(cm.\"CityCode\")), 4) AS avgrate";
//sql += "						   ,SUM(dlb.\"IntAmtRcv\") AS \"IntAmtRcv\"";
//sql += "						   ,SUM(dlb.\"LoanBalance\") AS \"LoanBalance\"";
//sql += "						   ,cc.\"CityCode\"";
//sql += "					 FROM \"DailyLoanBal\" dlb";
//sql += "					 LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = dlb.\"CustNo\" AND fm.\"FacmNo\" = dlb.\"FacmNo\"";
//sql += "					 LEFT JOIN \"FacCaseAppl\" fca ON fca.\"ApplNo\" = fm.\"ApplNo\"";
//sql += "					 LEFT JOIN \"ClFac\" cf ON cf.\"CustNo\" = dlb.\"CustNo\"";
//sql += "										   AND cf.\"FacmNo\" = dlb.\"FacmNo\"";
//sql += "										   AND cf.\"MainFlag\" = 'Y'";
//sql += "					 LEFT JOIN \"ClMain\" cm ON cm.\"ClCode1\" = cf.\"ClCode1\"";
//sql += "											AND cm.\"ClCode2\" = cf.\"ClCode2\"";
//sql += "											AND cm.\"ClNo\" = cf.\"ClNo\"";
//sql += "					 LEFT JOIN \"CdCity\" cc ON cc.\"CityCode\" = cm.\"CityCode\"";
//sql += "					 WHERE fm.\"AcctCode\" IN('310','320','330','340')";
//sql += "					 GROUP BY dlb.\"CustNo\"";
//sql += "						     ,dlb.\"FacmNo\"";
//sql += "						     ,fm.\"AcctCode\"";
//sql += "							 ,fca.\"ApproveDate\"";
//sql += "							 ,cc.\"CityCode\") a1";
//sql += "				     LEFT JOIN(SELECT bb.\"CustNo\"";
//sql += "								   	 ,bb.\"FacmNo\"";
//sql += "								   	 ,round(SUM(bb.\"TotalPeriod\") / COUNT(bb.\"TotalPeriod\"), 0) AS \"TotalPeriod\"";
//sql += "							   FROM \"LoanBorMain\" bb";
//sql += "							   GROUP BY bb.\"CustNo\"";
//sql += "						   	  	       ,bb.\"FacmNo\") a2";
//sql += "				ON a1.\"CustNo\" = a2.\"CustNo\" AND a1.\"FacmNo\" = a2.\"FacmNo\"";
//sql += "			    WHERE \"ApproveDate\" >= :nowsd";
//sql += "		 	      AND \"ApproveDate\" <= :nowed) ptwo";
//sql += "	   ON ptwo.\"CityCode\" = mcc.\"CityCode\"";