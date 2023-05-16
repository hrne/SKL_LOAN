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
		sql += "     SELECT  rm.\"ReltUKey\" 			AS F0 , ";
		sql += " 			 MIN(cm.\"CustNo\") 		AS F1 ";
		sql += "     FROM \"ReltMain\"     rm      ";
		sql += " LEFT JOIN \"CustMain\"   cm ON cm.\"CustUKey\" = rm.\"ReltUKey\" ";
		sql += "     WHERE rm.\"CustNo\" = :custno  ";
		sql += "       AND rm.\"FinalFg\" = 'Y'     ";
		sql += "  GROUP BY rm.\"ReltUKey\" ";
		sql += " UNION (SELECT cm2.\"CustUKey\" 	AS F0 , ";
		sql += " 			   cm2.\"CustNo\" 		AS F1 ";
		sql += " FROM \"ReltMain\" rm ";
		sql += " LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = :custno ";
		sql += " LEFT JOIN \"CustMain\" cm2 ON cm2.\"CustNo\" = rm.\"CustNo\" ";
		sql += " WHERE rm.\"ReltUKey\" = cm.\"CustUKey\"  ";
		sql += "    AND cm2.\"CustUKey\" IS NOT NULL  ";
		sql += " GROUP BY cm2.\"CustUKey\" ,cm2.\"CustNo\" ) ";
	
		sql += " ORDER BY F1 ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custno", CustNo);

		return this.convertToMap(query);
	}

}