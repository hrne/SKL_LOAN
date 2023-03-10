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

@Service
@Repository
public class L9739ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param reportDate 帳務日
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findStandard(TitaVo titaVo, int reportDate) throws Exception {
		this.info("l9739.findAll ");

		this.info("getEntDyI =" + reportDate);

		String sql = " ";
		sql += "	SELECT P.\"ProdNo\"";
		sql += "		  ,P.\"ProdName\"";
		sql += "		  ,P.\"ProdIncr\"";
		sql += "		  ,NVL(S.\"EffectDate\",0) AS \"EffectDate\"";
		sql += "		  ,NVL(S.\"JsonFields\",'0') AS \"JsonFields\"";
		sql += "		  ,NVL(S.\"lJsonFields\",'0') AS \"lJsonFields\"";
		sql += "		  ,ROW_NUMBER()OVER(ORDER BY P.\"ProdNo\" ASC) as \"Seq\"";
		sql += "		  ,NVL(B.\"EffectDate\",0) AS \"BaseEffectDate\"";
		sql += "		  ,NVL(B.\"BaseRate\",0) AS \"BaseRate\"";
		sql += "	FROM \"FacProd\" P";
		sql += "	LEFT JOIN (";
		sql += "		SELECT MAX(DECODE(R.\"Seq\",1,R.\"EffectDate\",0)) AS \"EffectDate\"";
		sql += "			  ,MAX(DECODE(R.\"Seq\",1,R.\"JsonFields\",'')) AS \"JsonFields\"";
		sql += "			  ,MAX(DECODE(R.\"Seq\",1,0,R.\"EffectDate\")) AS \"lEffectDate\"";
		sql += "			  ,MAX(DECODE(R.\"Seq\",1,'',R.\"JsonFields\")) AS \"lJsonFields\"";
		sql += "		FROM (";
		sql += "			SELECT \"EffectDate\"";
		sql += "				  ,\"JsonFields\"";
		sql += "				  ,ROW_NUMBER()OVER(ORDER BY \"EffectDate\" DESC) as \"Seq\"";
		sql += "			FROM \"CdComm\"";
		sql += "			WHERE \"CdType\" = '01'";
		sql += "			  AND \"CdItem\" = '01'";
		sql += "			  AND \"EffectDate\" <= :entdy ";
		sql += "		) R ";
		sql += "		WHERE R.\"Seq\" <= 2";
		sql += "	) S ON S.\"EffectDate\" > 0";
		sql += "	LEFT JOIN (";
		sql += "		SELECT \"EffectDate\"";
		sql += "			  ,\"BaseRate\"";
		sql += "		FROM \"CdBaseRate\"";
		sql += "		WHERE \"EffectDate\" = (";
		sql += "					SELECT MAX(\"EffectDate\") ";
		sql += "					FROM \"CdBaseRate\"";
		sql += "					WHERE \"EffectDate\" <= :entdy ";
		sql += "					  AND \"BaseRateCode\" = '02' )";
		sql += "		  AND \"BaseRateCode\" = '02' "; // --郵局儲蓄利率
		sql += "	) B ON B.\"EffectDate\" > 0";
		sql += "	WHERE P.\"GovOfferFlag\" = 'Y' ";
		sql += "	ORDER BY P.\"ProdNo\" ASC";

		this.info("sql1=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", reportDate);
		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出(放款餘額明細表)
	 * 
	 * @param titaVo
	 * @param tmpEffectDate 西元年月日
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int tmpEffectDate) throws Exception {
		this.info("l9739.findAll ");

		this.info("tmpEffectDate = " + tmpEffectDate);

		String sql = " ";
		sql += "	SELECT L.\"CustNo\"";
		sql += "		  ,L.\"FacmNo\"";
		sql += "		  ,L.\"BormNo\"";
		sql += "		  ,L.\"EffectDate\"";
		sql += " 	      ,TO_CHAR(NVL(L.\"FitRate\",0),'0.000') AS \"StoreRate\"";
		sql += "		  ,L.\"ProdNo\"";
		sql += "	FROM \"LoanRateChange\" L";
		sql += "	LEFT JOIN (";
		sql += "		SELECT \"CustNo\"";
		sql += "			  ,\"FacmNo\"";
		sql += "			  ,\"BormNo\"";
		sql += "			  ,MAX(\"EffectDate\") AS \"EffectDate\"";
		sql += "		FROM \"LoanRateChange\"";
		sql += "		WHERE REGEXP_LIKE(\"ProdNo\",'I[A-I]')";
		sql += "		  AND \"EffectDate\" <= :entdy ";
		sql += "		GROUP BY \"CustNo\"";
		sql += "				,\"FacmNo\"";
		sql += "				,\"BormNo\"";
		sql += "	)LC ON LC.\"CustNo\" = L.\"CustNo\"";
		sql += "	   AND LC.\"FacmNo\" = L.\"FacmNo\"";
		sql += "	   AND LC.\"BormNo\" = L.\"BormNo\"";
		sql += "	   AND LC.\"EffectDate\" = L.\"EffectDate\"";
		sql += "	LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = L.\"CustNo\"";
		sql += "							   AND M.\"FacmNo\" = L.\"FacmNo\"";
		sql += "							   AND M.\"BormNo\" = L.\"BormNo\"";
		sql += "	WHERE REGEXP_LIKE(L.\"ProdNo\",'I[A-I]')";
		sql += "	  AND L.\"EffectDate\" <= :entdy ";
		sql += "	  AND M.\"Status\" = 0 ";
		sql += "	  AND LC.\"CustNo\" IS NOT NULL ";
		sql += "	ORDER BY L.\"ProdNo\" ASC";
		sql += "		    ,L.\"CustNo\" ASC";
		sql += "			,L.\"FacmNo\" ASC";
		sql += "			,L.\"BormNo\" ASC";

		// LoanBorMain Status= 0 join LoanRateChange MAX(生效日期)<=今天(系統日)
		this.info("sql2=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", tmpEffectDate);
		return this.convertToMap(query);
	}
}