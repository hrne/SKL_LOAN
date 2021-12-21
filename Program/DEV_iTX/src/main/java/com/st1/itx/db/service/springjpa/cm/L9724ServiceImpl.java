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
public class L9724ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int aging) throws Exception {
		this.info("l9724.findAll ");

		String sql = "SELECT ali.\"CustNo\" AS F0 ";
		sql += "            ,MAX(ali.\"Aging\") AS F1 ";
		sql += "            ,MIN(ali.\"IntStartDate\") AS F2 ";
		sql += "            ,SUM(ali.\"Interest\") AS F3 ";
		sql += "            ,MAX(lbm.\"NextPayIntDate\") AS F4 ";
		sql += "      FROM \"AcLoanInt\" ali ";
		sql += "      LEFT JOIN \"LoanBorMain\" lbm ON lbm.\"CustNo\" = ali.\"CustNo\" ";
		sql += "                                   AND lbm.\"FacmNo\" = ali.\"FacmNo\" ";
		sql += "                                   AND lbm.\"BormNo\" = ali.\"BormNo\" ";
		sql += "      WHERE ali.\"YearMonth\" = :inputYearMonth ";
		sql += "        AND ali.\"IntStartDate\" > 0 ";
		sql += "        AND ( ali.\"Aging\" = :aging OR :aging = 9 ) ";
		sql += "      GROUP BY ali.\"YearMonth\" ";
		sql += "              ,ali.\"CustNo\" ";
		sql += "              ,ali.\"IntStartDate\" ";
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		int inputYearMonth = parse.stringToInteger(titaVo.getParam("inputYear") + titaVo.getParam("inputMonth")) + 191100;

		this.info("l9724 input");
		this.info("inputYearMonth: " + inputYearMonth);
		this.info("aging: " + aging);

		query.setParameter("inputYearMonth", inputYearMonth);
		query.setParameter("aging", parse.IntegerToString(aging, 1)); // 9 代表出全部資料

		return this.convertToMap(query);
	}

}