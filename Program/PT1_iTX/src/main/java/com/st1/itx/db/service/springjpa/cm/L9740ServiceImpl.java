package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;
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

@Service
@Repository
public class L9740ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * Query(新撥款之戶號)
	 * 
	 * @param titaVo
	 * @param sDate 撥款起日(西元)
	 * @param eDate 撥款迄日(西元) 
	 * @param acctCode 科目代號
	 * @param renewFlag 是否借新還舊
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findPage1(TitaVo titaVo, int sDate,int eDate,String acctCode ,String renewFlag) throws Exception {
		this.info("l9740.findPage1 ");
		

		String sql = " ";
		sql += "	SELECT M.\"CustNo\"";
		sql += "		  ,M.\"FacmNo\"";
//		sql += "		  ,M.\"BormNo\"";
		sql += "		  ,M.\"DrawdownDate\" - 19110000 AS \"DrawdownDate\"";
		sql += "		  ,M.\"DrawdownAmt\"";
		sql += "		  ,M.\"StoreRate\"";
		sql += "	FROM \"LoanBorMain\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"DrawdownDate\" BETWEEN :startDate AND :endDate";
		//RenewFlag 0:正常 1:展期撥款 2:借新還舊撥款
		if("Y".equals(renewFlag)) {
			sql += "	  AND M.\"RenewFlag\" <> 1 ";		
		}else {
			sql += "	  AND M.\"RenewFlag\" = 1 ";			
		}
		sql += "	  AND F.\"AcctCode\" = :acctCode ";
		sql += "	ORDER BY M.\"CustNo\" ASC";
		sql += "			,M.\"FacmNo\" ASC";
//		sql += "			,M.\"BormNo\" ASC";

		
		this.info("sDate =" + sDate);
		this.info("eDate =" + eDate);
		this.info("acctCode =" + acctCode);
		this.info("renewFlag =" + renewFlag);

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", sDate);
		query.setParameter("endDate", eDate);
		query.setParameter("acctCode", acctCode);
		return this.convertToMap(query);
	}

	/**
	 * Query(續期放款利率 最低 最高)
	 * 
	 * @param titaVo
	 * @param date 撥款日(西元)
	 * @param acctCode 科目代號
	 * @param statusCode 戶況代號 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findPage2(TitaVo titaVo, int date,String acctCode ,String statusCode ) throws Exception {
		this.info("l9740.findPage2 ");

				
		String sql = " ";
		sql += "	SELECT MIN(M.\"StoreRate\") AS \"minRate\"";
		sql += "		  ,MAX(M.\"StoreRate\") AS \"maxRate\"";
		sql += "	FROM \"LoanBorMain\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"DrawdownDate\" <= :endDate";
		sql += "	  AND F.\"AcctCode\" = :acctCode ";

		//status 0:正常戶 2:催收戶
		if("0".equals(statusCode)) {
			sql += "	  AND M.\"Status\" IN (0)";
		}else if("1".equals(statusCode)) {
			sql += "	  AND M.\"Status\" IN (2)";
		}else {
			sql += "	  AND M.\"Status\" IN (0,2)";
		}
		this.info("date =" + date);
		this.info("acctCode =" + acctCode);
		this.info("status =" + statusCode);
		
		this.info("sql2=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("endDate", date);
		query.setParameter("acctCode", acctCode);
		return this.convertToMap(query);
	}

	/**
	 * Query(x利率超過 X%之借戶)
	 * 
	 * @param titaVo
	 * @param date 撥款日(西元)
	 * @param acctCode 科目代號
	 * @param statusCode 戶況代號 
	 * @param rate 利率
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findPage3(TitaVo titaVo, int date,String acctCode ,String statusCode, BigDecimal rate) throws Exception {
		this.info("l9740.findPage3 ");

		String sql = " ";
		sql += "	SELECT M.\"CustNo\"";
		sql += "		  ,M.\"FacmNo\"";
//		sql += "		  ,MIN(M.\"BormNo\") AS \"BormNo\"";
		sql += "		  ,MIN(M.\"DrawdownDate\" - 19110000) AS \"DrawdownDate\"";
		sql += "		  ,SUM(M.\"DrawdownAmt\") AS \"DrawdownAmt\"";
		sql += "		  ,MIN(L.\"FitRate\") AS \"StoreRate\"";
		sql += "		  ,MIN(M.\"PrevPayIntDate\" - 19110000) AS \"PrevPayIntDate\"";
		sql += "	FROM \"LoanBorMain\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";

		sql += "	LEFT JOIN ( ";
		sql += "		SELECT R1.\"CustNo\"";
		sql += "			  ,R1.\"FacmNo\"";
		sql += "			  ,R1.\"BormNo\"";
		sql += "			  ,R2.\"FitRate\"";
		sql += "		FROM (";
		sql += "			SELECT \"CustNo\"";
		sql += "				  ,\"FacmNo\"";
		sql += "				  ,\"BormNo\"";
		sql += "				  ,MAX(\"EffectDate\") AS \"EffectDate\"";
		sql += "			FROM \"LoanRateChange\" ";
		sql += "			WHERE \"EffectDate\" <= :endDate";
		sql += "			GROUP BY \"CustNo\"";
		sql += "					,\"FacmNo\"";
		sql += "					,\"BormNo\"";
		sql += "		) R1 ";
		sql += "		LEFT JOIN \"LoanRateChange\" R2 ON R2.\"CustNo\" = R1.\"CustNo\"";
		sql += "	  								   AND R2.\"FacmNo\" = R1.\"FacmNo\"";
		sql += "	  								   AND R2.\"BormNo\" = R1.\"BormNo\"";
		sql += "	  								   AND R2.\"EffectDate\" = R1.\"EffectDate\"";		
		sql += "	) L  ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "	    AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	    AND L.\"BormNo\" = M.\"BormNo\"";		

		sql += "	WHERE M.\"DrawdownDate\" <= :endDate";
		sql += "	  AND L.\"FitRate\" > :rate ";
		sql += "	  AND F.\"AcctCode\" = :acctCode ";

		//status 0:正常戶 2:催收戶
		if("0".equals(statusCode)) {
			sql += "	  AND M.\"Status\" IN (0)";
		}else if("1".equals(statusCode)) {
			sql += "	  AND M.\"Status\" IN (2)";
		}else {
			sql += "	  AND M.\"Status\" IN (0,2)";
		}
		sql += "	GROUP BY M.\"CustNo\"";
		sql += "			,M.\"FacmNo\"";
		sql += "	ORDER BY \"CustNo\" ASC";
		sql += "			,\"FacmNo\" ASC";
//		sql += "			,\"BormNo\" ASC";

		this.info("date =" + date);
		this.info("acctCode =" + acctCode);
		this.info("status =" + statusCode);
		
		this.info("sql3=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("endDate", date);
		query.setParameter("rate", rate);
		query.setParameter("acctCode", acctCode);
		return this.convertToMap(query);
	}

}