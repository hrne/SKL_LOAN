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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4454BServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454BServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4454B.findAll");

		int acDay1 = parse.stringToInteger(titaVo.get("AcDay1")) + 19110000;
		int entryDay1 = parse.stringToInteger(titaVo.get("EntryDay1")) + 19110000;
		int acDate = titaVo.getEntDyI() + 19110000;
		int custNo = 0;
		int facmNo = 0;
		this.info("today = " + titaVo.getEntDyI());

//		單筆輸入
		if (titaVo.get("AcDate") != null && !"0000000".equals(titaVo.get("AcDate"))) {
			this.info("AcDate = " + titaVo.get("AcDate"));
			acDate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		}
		if (titaVo.get("CustNo") != null) {
			custNo = parse.stringToInteger(titaVo.get("CustNo"));
		}
		if (titaVo.get("FacmNo") != null) {
			facmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		}

		this.info("acDay1 = " + acDay1);
		this.info("entryDay1 = " + entryDay1);
		this.info("acDate = " + acDate);
		this.info("custNo = " + custNo);
		this.info("facmNo = " + facmNo);

		String sql = " select                                   ";
		sql += "   d.\"CustNo\"                               ";
		sql += "  ,d.\"FacmNo\"                               ";
		sql += "  ,c.\"CustName\"                             ";
		sql += "  ,d.\"RepayCode\"                            ";
		sql += "  from \"BatxDetail\" d                       ";
		sql += " left join \"CustMain\" c on c.\"CustNo\"  =  d.\"CustNo\"     ";
		sql += " left join \"BatxHead\" H on H.\"AcDate\"  =  d.\"AcDate\"     ";
		sql += "                         and H.\"BatchNo\" =  d.\"BatchNo\"	   ";
		sql += "  left join (                                 ";
		sql += "      select distinct                         ";
		sql += "      dd.\"CustNo\"                           ";
		sql += "     ,dd.\"FacmNo\"                           ";
		sql += "  from \"BatxDetail\" dd                      ";
		sql += "  left join \"BankDeductDtl\" b on b.\"MediaDate\" = dd.\"MediaDate\"  ";
		sql += "                               and b.\"MediaKind\" = dd.\"MediaKind\"  ";
		sql += "                               and b.\"MediaSeq\"  = dd.\"MediaSeq\"   ";
		sql += "  LEFT JOIN \"BatxHead\" H      on H.\"AcDate\"    = dd.\"AcDate\"     ";
		sql += "                               and H.\"BatchNo\"   = dd.\"BatchNo\"	   ";
		sql += "  where dd.\"RepayType\" = 1                  ";
		sql += "    and dd.\"RepayCode\" = 2                  ";
		sql += "    and  H.\"BatxExeCode\" <> 8               ";
		sql += "    and dd.\"ProcCode\" != '00000'            ";
		sql += "    and dd.\"EntryDate\" = " + entryDay1;
		sql += "    and b.\"PayIntDate\" >= " + acDay1;
		sql += "    and b.\"PayIntDate\" <= " + entryDay1;
		sql += "  ) da on da.\"CustNo\" = d.\"CustNo\"        ";
		sql += "      and da.\"FacmNo\" = d.\"FacmNo\"        ";
		sql += "  where d.\"RepayType\" = 5                   ";
		sql += "    and d.\"RepayCode\" = 2                   ";
		sql += "    and H.\"BatxExeCode\" <> 8               ";
		sql += "    and d.\"ProcCode\" = '00000'              ";
		sql += "    and d.\"EntryDate\" =  " + entryDay1;
		sql += "    and d.\"AcDate\" =  " + acDate;
		sql += "    and da.\"CustNo\" is not null             ";
		if (custNo > 0 && facmNo > 0) {
			sql += " and d.\"CustNo\" = " + custNo;
			sql += " and d.\"FacmNo\" = " + facmNo;
		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}