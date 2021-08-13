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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("lM051ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LM051ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo, int i) throws Exception {
		String yymm = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000);

		this.info("lM051.findAll ");
		this.info("yymm:" + yymm);
		
		// 0
		String groupSelect1 = "	WHERE M.\"ProdNo\" NOT IN ('60','61','62') AND M.\"Status\" = 0 ";
		// 協
		String groupSelect2 = "	WHERE M.\"ProdNo\" IN ('60','61','62') AND M.\"Status\" = 0 ";
		// 催
		String groupSelect3 = "	WHERE M.\"ProdNo\" NOT IN ('60','61','62') AND M.\"Status\" IN (2,6,7) ";
		// 催 + 協
		String groupSelect4 = "	WHERE M.\"ProdNo\" IN ('60','61','62') AND M.\"Status\" IN (2,6,7) ";

		String sql = "SELECT M.\"CustNo\""; // F0
		sql += "			,M.\"FacmNo\""; // F1
		sql += "			,DECODE(M.\"AcSubBookCode\",' ',' ','00A','A') AS \"AcSubBookCode\""; // F2
		sql += "			,C.\"CustName\""; // F3
		sql += "			,M.\"PrinBalance\""; // F4
		sql += "			,M.\"FacAcctCode\""; // F5
		sql += "			,M.\"OvduTerm\""; // F6
		sql += "			,M.\"CityCode\""; // F7
		sql += "			,M.\"PrevIntDate\""; // F8
		sql += "			,SUBSTR(M.\"AssetClass\",0,1) AS \"Class\""; // F9
		sql += "			,CD.\"Item\""; // F10
		sql += "			,NVL(M.\"Amount\",0) AS \"Amount\""; // F11
		sql += "			,' ' AS \"Memo\""; // F12
		sql += "			,M.\"ProdNo\""; // F13
		sql += "			,M.\"RenewCode\""; // F14
		sql += "			,M.\"LawAmount\""; // F15
		sql += "			,M.\"AssetClass\""; // F16
		sql += "			,M.\"Status\""; // F17
		sql += "			,M.\"OvduText\""; // F18
		sql += "	  FROM(SELECT M.\"CustNo\"";
		sql += "				 ,M.\"FacmNo\"";
		sql += "				 ,NVL(M.\"AcSubBookCode\",' ') AS \"AcSubBookCode\"";
		sql += "				 ,M.\"PrinBalance\"";
		sql += "				 ,M.\"FacAcctCode\"";
		sql += "				 ,M.\"OvduTerm\"";
		sql += "				 ,M.\"CityCode\"";
		sql += "				 ,M.\"PrevIntDate\"";
		sql += "				 ,NVL(L.\"LegalProg\",'000') AS \"LegalProg\"";
		sql += "				 ,L.\"Amount\"";
		sql += "				 ,L.\"Memo\"";
		sql += "				 ,M.\"RenewCode\"";
		sql += "				 ,M.\"LawAmount\"";
		sql += "				 ,M.\"AssetClass\"";
		sql += "				 ,M.\"Status\"";
		sql += "				 ,M.\"OvduText\"";
		sql += "				 ,M.\"ProdNo\"";
		sql += "		   FROM(SELECT M.\"CustNo\"";
		sql += "					  ,M.\"FacmNo\"";
		sql += "					  ,DECODE(M.\"AcSubBookCode\",'201','00A',' ') AS \"AcSubBookCode\"";
		sql += "					  ,M.\"PrinBalance\"";
		sql += "					  ,M.\"FacAcctCode\"";

		sql += "					  ,(CASE";
		sql += "						 WHEN M.\"OvduTerm\" > 5 OR M.\"OvduTerm\"= 0 THEN 99";
		sql += "						 ELSE M.\"OvduTerm\"";
		sql += "					   END) AS \"OvduTerm\"";

		sql += "					  ,(CASE";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" <= 5 AND M.\"OvduDays\" > 30 AND M.\"Status\" = 0 THEN '*協-' ";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 AND M.\"OvduDays\" =0 AND M.\"Status\" = 0 THEN '協' ";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduDays\" > 0 AND M.\"OvduDays\" < 30 AND M.\"Status\" = 0 THEN '協*' ";
		sql += "						 WHEN M.\"ProdNo\" NOT IN ('60','61','62') AND M.\"Status\" IN (2,6,7) THEN '催' ";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"Status\" IN (2,6,7)  THEN '催協' ";
		sql += "						 ELSE ' '";
		sql += "					   END) AS \"OvduText\"";

		sql += "					  ,M.\"CityCode\"";
		sql += "					  ,M.\"PrevIntDate\"";
		sql += "					  ,M.\"RenewCode\"";
		sql += "					  ,M.\"LawAmount\"";
		sql += "					  ,M.\"AssetClass\"";
		sql += "				 	  ,M.\"Status\"";
		sql += "				 	  ,M.\"ProdNo\"";
		sql += "				FROM \"MonthlyFacBal\" M";
		sql += "				WHERE M.\"YearMonth\" =  :yymm ";
		sql += "				  AND M.\"Status\" IN (0, 2, 6, 7)";
		sql += "				  AND M.\"AssetClass\" IN ('21', '22', '23', '3', '4', '5')";
		sql += "				  AND M.\"OvduTerm\" >= 0 ";
		sql += "				  AND M.\"PrinBalance\" > 0 ) M";
		sql += "			 	LEFT JOIN(SELECT * ";
		sql += "						  FROM(SELECT L.\"CustNo\"";
		sql += "									 ,L.\"FacmNo\"";
		sql += "									 ,L.\"LegalProg\"";
		sql += "									 ,L.\"Amount\"";
		sql += "									 ,L.\"Memo\"";
		sql += "									 ,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\", L.\"FacmNo\" ORDER BY L.\"TitaTxtNo\" DESC) AS SEQ";
		sql += "							   FROM \"CollLaw\" L";
		sql += "						  	   WHERE TRUNC(L.\"AcDate\" / 100) <= :yymm ) L";
		sql += "						  WHERE L.SEQ = 1) L ";
		sql += "				ON L.\"CustNo\" = M.\"CustNo\" AND L.\"FacmNo\" = M.\"FacmNo\" ) M";
		sql += "			LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "			LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" = M.\"AcSubBookCode\"";
		sql += "			LEFT JOIN \"CdCode\" CD ON CD.\"Code\" = M.\"LegalProg\"";
		sql += "			AND CD.\"DefCode\" = 'LegalProg' ";

		// 有四種不同條件分四次
		switch (i) {
		case 1:
			sql += groupSelect1;
			break;
		case 2:
			sql += groupSelect2;
			break;
		case 3:
			sql += groupSelect3;
			break;
		case 4:
			sql += groupSelect4;
			break;
		default:
			break;
		}

		sql += "			ORDER BY M.\"OvduTerm\",M.\"CustNo\",M.\"FacmNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yymm.substring(0, 6));
		return this.convertToMap(query.getResultList());
	}

}