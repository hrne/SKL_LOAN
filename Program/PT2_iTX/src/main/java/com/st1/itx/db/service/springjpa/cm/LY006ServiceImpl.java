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

@Service("LY006ServiceImpl")
@Repository
public class LY006ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth, TitaVo titaVo) throws Exception {
		this.info("LY006ServiceImpl queryDetail ");
		String sql = "";
		sql += " select ";
		sql += " \"RelWithCompany\", ";
		sql += " \"HeadId\", ";
		sql += " \"HeadName\", ";
		sql += " CASE ";
		sql += "   WHEN \"HeadTitle\"='董事長' THEN 'A' ";
		sql += "   WHEN \"HeadTitle\"='副董事長' THEN 'B' ";
		sql += "   WHEN \"HeadTitle\" LIKE'%董事' THEN 'C' ";
		sql += "   WHEN \"HeadTitle\"='監事' THEN 'D' ";
		sql += "   WHEN \"HeadTitle\"='總經理' THEN 'E' ";
		sql += "   WHEN \"HeadTitle\"='副總經理' THEN 'F' ";
		sql += "   WHEN \"HeadTitle\" LIKE '%協理' THEN 'G' ";
		sql += "   WHEN \"HeadTitle\"='經理' THEN 'H' ";
		sql += "   WHEN \"HeadTitle\"='副理' THEN 'I' ";
		sql += "   WHEN \"HeadTitle\" is NULL THEN ' ' ";
		sql += "   ELSE 'J' ";
		sql += " END AS\"HeadTitle\", ";
		sql += " \"RelId\", ";
		sql += " \"RelName\", ";
		sql += " \"RelKinShip\", ";
		sql += " \"RelTitle\", ";
		sql += " \"BusId\", ";
		sql += " \"BusName\", ";
		sql += " \"ShareHoldingRatio\", ";
		sql += " CASE ";
		sql += " WHEN \"BusTitle\"='副董事長' THEN 'B' ";
		sql += " WHEN \"BusTitle\" LIKE'%董事' THEN 'C' ";
		sql += " WHEN \"BusTitle\"='監事' OR \"BusTitle\"='監察人' OR \"BusTitle\" LIKE '(團體)%' THEN 'D' ";
		sql += " WHEN \"BusTitle\"='董事長' OR \"BusTitle\" LIKE '%人'THEN 'A' ";
		sql += " WHEN \"BusTitle\"='總經理' THEN 'E' ";
		sql += " WHEN \"BusTitle\"='副總經理' THEN 'F' ";
		sql += " WHEN \"BusTitle\" LIKE '%協理' THEN 'G' ";
		sql += " WHEN \"BusTitle\"='經理' THEN 'H' ";
		sql += " WHEN \"BusTitle\"='副理' THEN 'I' ";
		sql += " WHEN \"BusTitle\" is NULL THEN ' ' ";
		sql += " ELSE 'J' ";
		sql += " END AS\"BusTitle\", ";
		sql += " \"LineAmt\", ";
		sql += " \"LoanBalance\" ";
		sql += " from \"LifeRelHead\" ";
	  sql += " WHERE \"AcDate\" = (SELECT MAX(\"AcDate\") FROM \"LifeRelHead\" WHERE TRUNC(\"AcDate\" / 100 ) = :inputYearMonth )";
		sql += "   AND \"RelWithCompany\" <> 'E' ";
		sql += " order by (case ";
		sql += "			when\"RelWithCompany\"='C' then 0 ";
		sql += "			when \"RelWithCompany\"='D' then 1 ";
		sql += "			when \"RelWithCompany\"='F' then 2 ";
		sql += "			else 6 ";
		sql += "			end )";
		sql += " 		,\"RelWithCompany\" asc";
		sql += "		,\"HeadId\" asc";
		sql += "		,\"BusId\" asc";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);
		return this.convertToMap(query);
	}

}