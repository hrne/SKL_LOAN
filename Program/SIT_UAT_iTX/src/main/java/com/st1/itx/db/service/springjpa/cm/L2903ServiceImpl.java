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

public class L2903ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int CustNo, int index, int limit, TitaVo titaVo) throws Exception {
		this.info("L2903ServiceImpl.findAll ");
		String sql = "";
		sql += "     SELECT Min(\"CaseNo\") AS F1";
		sql += "	           ,\"CustNo\"       ";
		sql += "               ,\"ReltUKey\"     ";
		sql += "               ,\"FinalFg\"      ";
		sql += "     FROM \"ReltMain\"           ";
		sql += "     WHERE \"CustNo\" = :custno  ";
		sql += "       AND \"FinalFg\" = 'Y'     "; 
		sql += "     GROUP BY \"CustNo\", \"ReltUKey\", \"FinalFg\"";

		

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custno", CustNo);
		
		return this.convertToMap(query);
	}
	
}