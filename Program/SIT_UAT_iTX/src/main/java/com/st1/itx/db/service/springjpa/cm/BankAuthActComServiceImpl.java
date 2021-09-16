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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
public class BankAuthActComServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	DateUtil dateUtil;

	String sql = "";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> doQuery(int custNo, int facmNo, int adjDate, TitaVo titaVo) throws Exception {

		sql = " ";
		sql += " SELECT T.\"EntryDate\""; // F0:入帳日

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAcctSeq(int custNo, String depCode, TitaVo titaVo) throws Exception {

		sql = "";

		sql += " select                                ";
		sql += "             F0                        ";
		sql += " ,MAX(F1) as F1                        ";
		sql += " from (                                ";
		sql += " select                                ";
		sql += "    F0                                 ";
		sql += "   ,case when NVL(\"Seq\",'') = '' then ' ' else to_char(to_number(NVL(trim(\"Seq\"),'0')) + 1) end  as F1 ";
		sql += "   from (                              ";
		sql += "   select                              ";
		sql += "     \"CustNo\"           as F0        ";
		sql += "   , \"RepayAcct\"        as F1        ";
//		火險期款授權算一筆
		sql += "   , MAX(case when \"AuthCode\" != '00' then '00' else '00' end) as F2 ";
		sql += "   , MAX(\"RepayAcctSeq\")                                       as \"Seq\" ";
		sql += "   from \"PostAuthLog\"                ";
		sql += "  where \"CustNo\" = " + custNo;
		sql += "    and \"PostDepCode\" = '" + depCode + "'";
		sql += "   group by \"CustNo\",\"RepayAcct\"   ";
		sql += "   order by \"CustNo\",\"RepayAcct\"   ";
		sql += "   )                                   ";
		sql += "   )                                   ";
		sql += "   group by F0                         ";
		sql += "   order by F0                         ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}