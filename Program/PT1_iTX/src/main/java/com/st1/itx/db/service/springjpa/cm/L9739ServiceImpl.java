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
	 * 執行報表輸出(放款餘額明細表)
	 * 
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findStandard(TitaVo titaVo, String prodCode) throws Exception {
		this.info("l9739.findAll ");

		String sql = " ";
		sql += "	SELECT P.\"ProdNo\"";
		sql += "		  ,P.\"ProdName\"";
		sql += "		  ,R.\"EffectDate\"";
		sql += "		  ,R.\"FitRate\"";
		sql += "	FROM \"FacProd\" P";
		sql += "	LEFT JOIN (";
		sql += "		SELECT DISTINCT \"ProdNo\"";
		sql += "			  ,\"FitRate\"";
		sql += "			  ,\"EffectDate\"";
		sql += "		FROM \"LoanRateChange\"";
		sql += "		WHERE REGEXP_LIKE(\"ProdNo\",'I[A-I]')";
		sql += "		  AND \"EffectDate\" = (";
		sql += "			SELECT MAX(\"EffectDate\") AS \"EffectDate\"";
		sql += "			FROM \"LoanRateChange\"";
		sql += "			WHERE REGEXP_LIKE(\"ProdNo\",'I[A-I]')";
		sql += "		)";
		sql += "	) R ON R.\"ProdNo\" = P.\"ProdNo\"";
		if ("0".equals(prodCode)) {
			sql += "	WHERE REGEXP_LIKE(P.\"ProdNo\",'I[A-I]')";
		} else {
			sql += "	WHERE REGEXP_LIKE(P.\"ProdNo\",'" + prodCode + "')";
		}
		sql += "	ORDER BY P.\"ProdNo\" ASC";

		this.info("sql1=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出(放款餘額明細表)
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("l9739.findAll ");

		String sql = " ";
		sql += "	SELECT L.\"CustNo\"";
		sql += "		  ,L.\"FacmNo\"";
		sql += "		  ,L.\"BormNo\"";
		sql += " 		  ,NVL(ML.\"StoreRate\",0.00) AS \"StoreRate\"";
		sql += " 		  ,NVL(LC.\"EffectDate\",0) AS \"EffectDate\"";
		sql += "		  ,F.\"ProdNo\"";
		sql += "	FROM \"LoanBorMain\" L";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "	LEFT JOIN \"MonthlyLoanBal\" ML ON ML.\"CustNo\" = L.\"CustNo\"";
		sql += "								   AND ML.\"FacmNo\" = L.\"FacmNo\"";
		sql += "								   AND ML.\"BormNo\" = L.\"BormNo\"";
		sql += "								   AND ML.\"YearMonth\" = :yearmonth ";
		// --確認最新訂定生效日
		sql += "	LEFT JOIN \"FacProd\" P ON P.\"ProdNo\" = F.\"ProdNo\"";
		// --確認戶號撥款最新商品生效日
		sql += "	LEFT JOIN (";
		sql += "		SELECT \"CustNo\"";
		sql += "			  ,\"FacmNo\"";
		sql += "			  ,\"BormNo\"";
		sql += "			  ,MAX(\"EffectDate\") AS \"EffectDate\"";
		sql += "		FROM \"LoanRateChange\"";
		sql += "		WHERE REGEXP_LIKE(\"ProdNo\",'I[A-I]')";
		sql += "		GROUP BY \"CustNo\"";
		sql += "				,\"FacmNo\"";
		sql += "				,\"BormNo\"";
		sql += "	)LC ON LC.\"CustNo\" = L.\"CustNo\"";
		sql += "	   AND LC.\"FacmNo\" = L.\"FacmNo\"";
		sql += "	   AND LC.\"BormNo\" = L.\"BormNo\"";
		sql += "	LEFT JOIN \"LoanRateChange\" LRC ON LRC.\"CustNo\" = LC.\"CustNo\"";
		sql += "								    AND LRC.\"FacmNo\" = LC.\"FacmNo\"";
		sql += "								    AND LRC.\"BormNo\" = LC.\"BormNo\"";
		sql += "								    AND LRC.\"EffectDate\" = LC.\"EffectDate\"";
		sql += "	WHERE L.\"Status\" = 0";
		sql += "	  AND REGEXP_LIKE(P.\"ProdNo\",'I[A-I]')";
		sql += "	  AND P.\"StartDate\" = LC.\"EffectDate\"";
		sql += "	  AND LRC.\"FitRate\" <> ML.\"StoreRate\"";
		sql += "	ORDER BY L.\"CustNo\" ASC";
		sql += "			,L.\"FacmNo\" ASC";
		sql += "			,L.\"BormNo\" ASC";

		this.info("sql2=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearmonth", yearMonth);
		return this.convertToMap(query);
	}
}