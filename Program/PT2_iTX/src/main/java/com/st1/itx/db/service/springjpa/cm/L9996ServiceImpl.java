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
public class L9996ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findData(String itemCode, int custNo, String txcd, TitaVo titaVo)
			throws Exception {
		this.info("L9996 findData");

		String sql = " ";
		sql += "	SELECT \"CustNo\"";
		sql += "	      ,\"FacmNo\"";
		sql += "	      ,\"BormNo\"";
		sql += "	      ,\"ItemCode\"";
		sql += "	      ,\"DtlValue\"";
		sql += "	      ,\"ProcessNote\"";
		sql += "	FROM \"TxToDoDetail\"";
		sql += "	WHERE \"ItemCode\" = :itemCode ";
		sql += "	AND \"CustNo\" =  :custNo ";
//		sql += "	AND \"FacmNo\" = 0 ";
//		sql += "	AND \"BormNo\" = 0 ";
		sql += "	AND \"ExcuteTxcd\" = :txcd ";

		this.info("sql1=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("itemCode", itemCode);
		query.setParameter("custNo", custNo);
		query.setParameter("txcd", txcd);
		return this.convertToMap(query);
	}

}