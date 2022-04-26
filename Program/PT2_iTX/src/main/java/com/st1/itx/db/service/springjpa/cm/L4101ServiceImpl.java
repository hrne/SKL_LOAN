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

@Service("L4101ServiceImpl")
@Repository
public class L4101ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		int drawdowndate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		this.info("L4101 sql start");
		String sql = "  SELECT "; 
		sql +=  "    ln.\"CustNo\","; 
		sql +=  "    cm.\"CustName\","; 
		sql +=  "    lb.\"FacmNo\",";
		sql +=  "    lb.\"BormNo\","; 
		sql +=  "    ln.\"NotYetCode\","; 
		sql +=  "    cl.\"NotYetItem\""; 
		sql +=  "    FROM  "; 
		sql +=  "    \"LoanNotYet\"    ln"; 
		sql +=  "    LEFT JOIN \"LoanBorMain\"   lb ON lb.\"CustNo\" = ln.\"CustNo\""; 
		sql +=  "                                  AND lb.\"FacmNo\" = ln.\"FacmNo\""; 
		sql +=  "    LEFT JOIN \"CustMain\"      cm ON cm.\"CustNo\" = ln.\"CustNo\""; 
		sql +=  "    LEFT JOIN \"CdLoanNotYet\" cl on cl.\"NotYetCode\" = ln.\"NotYetCode\""; 
		sql +=  "    WHERE";
		sql +=  "    lb.\"DrawdownDate\" = :drawdowndate";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("drawdowndate", drawdowndate);
		return this.convertToMap(query);
	}
}
