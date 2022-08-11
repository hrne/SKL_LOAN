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
public class LM037ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo,int yearMonth) throws Exception {

		int entdy = yearMonth;

		this.info("lM037.findAll ");
		String sql = "SELECT DECODE(M.\"EntCode\", '1', '1', '0')          F1";
		sql += "			,NVL (M.\"CityCode\", '0') F2";
		sql += "			,SUM(M.\"LoanBalance\") F3";
		sql += "	  FROM \"MonthlyLoanBal\" M";
		sql += "	  WHERE M.\"YearMonth\" = :entdy";
		sql += "		AND DECODE(M.\"DepartmentCode\", '1', '1', '0') = 0";
		sql += "		AND M.\"AcctCode\" = '990'";
		sql += "		AND M.\"LoanBalance\" > 0";
		sql += " 	  GROUP BY DECODE(M.\"EntCode\", '1', '1', '0')";
		sql += "			  ,M.\"CityCode\"";
		sql += "	  ORDER BY F1";
		sql += "			  ,M.\"CityCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("entdy", entdy);
		
		return this.convertToMap(query);
	}

}