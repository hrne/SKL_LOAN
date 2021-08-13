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
public class L9724ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, int aging) throws Exception {
		this.info("l9724.findAll ");

		String sql = "SELECT ali.\"CustNo\" AS F0 ";
		sql += "            ,MAX(ali.\"Aging\") AS F1 ";
		sql += "            ,MIN(ali.\"IntStartDate\") AS F2 ";
		sql += "            ,SUM(ali.\"Interest\") AS F3 ";
		sql += "            ,MAX(ali.\"PayIntDate\") AS F4 ";
		sql += "      FROM \"AcLoanInt\" ali ";
		sql += "      WHERE ali.\"YearMonth\" = :inputYearMonth ";
		if (aging != 9)
		{
			sql += "        AND ali.\"Aging\" = :aging ";
		}
		sql += "      GROUP BY ali.\"YearMonth\", ali.\"CustNo\" ";
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		String inputYearMonth = Integer.toString(Integer.parseInt(titaVo.getParam("inputYear")) + 1911) + titaVo.getParam("inputMonth");
		this.info("l9724 input peko");
		this.info("inputYearMonth: " + inputYearMonth);
		this.info("aging: " + Integer.toString(aging));
		query.setParameter("inputYearMonth", inputYearMonth);

		// 9 means All
		// sorry for magic number
		if (aging != 9)
		{
			query.setParameter("aging", Integer.toString(aging));
		}

		return this.convertToMap(query.getResultList());
	}

}