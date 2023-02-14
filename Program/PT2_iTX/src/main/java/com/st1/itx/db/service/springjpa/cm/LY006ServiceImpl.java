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

	public List<Map<String, String>> queryDetail(int inputYearMonth,TitaVo titaVo) throws Exception {
		this.info("LY006ServiceImpl queryDetail ");
		String sql = "";
		sql += " select ";
		sql += " \"RelWithCompany\", ";
		sql += " \"HeadId\", ";
		sql += " \"HeadName\", ";
		sql += " \"HeadTitle\", ";
		sql += " \"RelId\", ";
		sql += " \"RelName\", ";
		sql += " \"RelKinShip\", ";
		sql += " \"RelTitle\", ";
		sql += " \"BusId\", ";
		sql += " \"BusName\", ";
		sql += " \"ShareHoldingRatio\", ";
		sql += " \"BusTitle\", ";
		sql += " \"LineAmt\", ";
		sql += " \"LoanBalance\" ";
		sql += " from \"LifeRelHead\" ";
		sql += "order by (case ";
		sql += "when\"RelWithCompany\"='C' then 0\r\n"; 
		sql += "when \"RelWithCompany\"='D' then 1\r\n" ; 
		sql += "when \"RelWithCompany\"='F' then 2\r\n" ;
		sql += "else 6 \r\n" ; 
		sql += "end\r\n" ; 
		sql += "),\"RelWithCompany\" asc";
		
		

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		query.setParameter("inputYearMonth", inputYearMonth);
		return this.convertToMap(query);
	}


}