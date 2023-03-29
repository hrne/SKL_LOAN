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
/* 逾期放款明細 */
public class LM015ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM015.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);
		this.info("entdy == >" + entdy);
		String sql = "SELECT \"AcctCodeSeq\"";
		sql += "              , \"CityGroup\"";
		sql += "              , ROUND(SUM(\"LoanBal\"),0) AS \"LoanBal\"";
		sql += "        FROM ( SELECT DECODE(M.\"AcctCode\", '990', 1, 0) AS \"AcctCodeSeq\"";
		sql += "                    , NVL(A.\"CityGroup\",'A') AS \"CityGroup\"";
		sql += "                    , ROUND(NVL(MBV.\"BookValue\",M.\"LoanBalance\")) AS \"LoanBal\"";
		sql += "               FROM \"MonthlyLoanBal\" M";
		sql += "               LEFT JOIN(SELECT \"CityGroup\"";
		sql += "               		           ,\"CityCode\"";
		sql += "                         FROM \"CdArea\"";
		sql += "               	   		 GROUP BY \"CityGroup\"";
		sql += "               					 ,\"CityCode\") A";
		sql += "               ON A.\"CityCode\" = M.\"CityCode\"";
		sql += "               LEFT JOIN \"Ias39IntMethod\" MBV ON MBV.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                                    			 AND MBV.\"CustNo\" = M.\"CustNo\"";
		sql += "                                     			 AND MBV.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                     			 AND MBV.\"BormNo\" = M.\"BormNo\"";
		sql += "               WHERE M.\"YearMonth\" = :entdy";
		sql += "               UNION ALL";
		sql += "               SELECT DECODE(AC.\"AcctCode\",'F24',1,'F25',1,0) AS \"AcctCodeSeq\"";
		sql += "                    , NVL(A.\"CityGroup\",'A') AS \"CityGroup\"";
		sql += "                    , SUM(AC.\"AcBal\") AS \"LoanBal\"";
		sql += "               FROM \"AcReceivable\" AC";
		sql += "               INNER JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :entdy ";
		sql += "               								 AND M.\"CustNo\" = AC.\"CustNo\"";
		sql += "               								 AND M.\"FacmNo\" = AC.\"FacmNo\"";
		sql += "               LEFT JOIN(SELECT \"CityGroup\"";
		sql += "               		           ,\"CityCode\"";
		sql += "                         FROM \"CdArea\"";
		sql += "               	   		 GROUP BY \"CityGroup\"";
		sql += "               					 ,\"CityCode\") A";
		sql += "               ON A.\"CityCode\" = M.\"CityCode\"";
		sql += "               WHERE AC.\"AcNoCode\" IN ('10600304000','10601301000','10601302000')";
		sql += "               GROUP BY DECODE(AC.\"AcctCode\",'F24',1,'F25',1,0) ";
		sql += "                      , NVL(A.\"CityGroup\",'A') ";
		sql += "               )";
		sql += "        GROUP BY \"AcctCodeSeq\", \"CityGroup\" ";
		sql += "        ORDER BY \"AcctCodeSeq\", \"CityGroup\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query);
	}

}
