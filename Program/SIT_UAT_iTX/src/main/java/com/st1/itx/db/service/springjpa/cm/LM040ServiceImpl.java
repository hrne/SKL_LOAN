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
/* 逾期放款明細 */
public class LM040ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("LM040ServiceImpl findAll ");
		int entryMonth = (titaVo.getEntDyI() + 19110000) / 100;
		int inputDate = parse.stringToInteger(titaVo.getParam("InputDate")) + 19110000;
		
		this.info("LM040ServiceImpl entryMonth = " + entryMonth);
		this.info("LM040ServiceImpl inputDate =  " + inputDate);

		String sql = " ";
		sql += " SELECT DECODE(M.\"EntCode\", 1, 1, 0) AS F0";
		sql += "       ,NVL(M.\"CityCode\", '0') AS F1";
		sql += "       ,SUM(M.\"PrinBalance\")  AS F2";
		sql += " FROM \"MonthlyFacBal\" M";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += " WHERE M.\"YearMonth\" = :entryMonth";
		sql += "   AND M.\"Status\" = 0";
		sql += "   AND M.\"PrinBalance\" > 0";
		sql += "   AND F.\"FirstDrawdownDate\" <= :inputDate";
		sql += " GROUP BY DECODE(M.\"EntCode\", 1, 1, 0)";
		sql += "         ,NVL(M.\"CityCode\", '0') ";
		sql += " ORDER BY F0";
		sql += "         ,F1";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("entryMonth", entryMonth);
		query.setParameter("inputDate", inputDate);

		return this.convertToMap(query);
	}

}