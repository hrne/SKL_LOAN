package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;

@Service("L4454R3ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454R3ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	private int entryDate = 0;
	private int lastYearEntryDate = 0;

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

//		4-8-62_一年內新貸件扣款失敗表
		this.info("一年內新貸件扣款失敗表 findAll...");

		entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;

		this.info("entryDate= " + entryDate);

		this.info("entryDate= " + entryDate);
		lastYearEntryDate = calDate(entryDate, -1, 0, 0);

		this.info("lastYearEntryDate = " + lastYearEntryDate);

		String sql = " select                                                                ";
		sql += "     MIN(bd.\"RepayAcctNo\")                        AS \"RepayAcctNo\"                         ";
		sql += "   , MIN(LPAD(bd.\"CustNo\", 7 , '0'))              AS \"CustNo\"                         ";
		sql += "   , MIN(LPAD(bd.\"FacmNo\", 3 , '0'))              AS \"FacmNo\"                         ";
		sql += "   , MIN(LPAD(lbm.\"BormNo\",3,'0'))	            AS \"BormNo\"                         ";
		sql += "   , MIN(cm.\"CustName\")                           AS \"CustName\"                         ";
		sql += "   , MIN(bd.\"RepayAmt\")                           AS \"RepayAmt\"                         ";
		sql += "   , MIN(NVL(\"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1),NVL(\"Fn_GetTelNo\"(CM.\"CustUKey\",'02',1),\"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1))))     AS \"PhoneNo\"                         ";
		sql += "   , MIN(NVL(bd.\"RelCustName\", cm.\"CustName\"))  AS \"RelCustName\"                         ";
		sql += "   , case when lbm.\"PrevPayIntDate\" < 19110000 then lbm.\"PrevPayIntDate\" ";
		sql += "          else lbm.\"PrevPayIntDate\" - 19110000 end     AS \"PrevPayIntDate\"               ";
		sql += "   , case when lbm.\"DrawdownDate\" < 19110000 then lbm.\"DrawdownDate\" ";
		sql += "          else lbm.\"DrawdownDate\" - 19110000 end  AS \"DrawdownDate\"               ";
		sql += "   , ce.\"Fullname\"                           AS \"Fullname\"                         ";
		sql += "   from \"BankDeductDtl\" bd                                                    ";
		sql += "   left join \"LoanBorMain\" lbm   on lbm.\"CustNo\"    = bd.\"CustNo\"     ";
		sql += "                                and lbm.\"FacmNo\"    = bd.\"FacmNo\"       ";
		sql += "   left join \"FacMain\" fm        on fm.\"CustNo\"     = bd.\"CustNo\"      ";
		sql += "                                and fm.\"FacmNo\"     = bd.\"FacmNo\"        ";
		sql += "   left join \"CustMain\" cm       on cm.\"CustNo\"     = bd.\"CustNo\"      ";
		sql += "   left join \"CdEmp\" ce          on ce.\"EmployeeNo\" = fm.\"FireOfficer\" ";
		sql += "   where bd.\"EntryDate\" = :entryDate                                       ";
		sql += "     and bd.\"MediaCode\" = 'Y'                                              ";
		sql += "     and bd.\"RepayType\" = 1                                                ";
		sql += "     and NVL(bd.\"ReturnCode\",'  ') not in ('  ','00')                      ";
		sql += "     and (lbm.\"DrawdownDate\" >= :lastYearEntryDate";
		sql += "           and  lbm.\"DrawdownDate\" <= :entryDate";
		sql += "           )                                                                 ";
		sql += "   group by CASE WHEN                                                        ";
		sql += "                 lbm.\"PrevPayIntDate\" < 19110000 ";
		sql += "                 THEN lbm.\"PrevPayIntDate\" ";
		sql += "                 ELSE lbm.\"PrevPayIntDate\" - 19110000 ";
		sql += "            END, ";
		sql += "            CASE WHEN lbm.\"DrawdownDate\" < 19110000 ";
		sql += "                 THEN lbm.\"DrawdownDate\" ";
		sql += "                 ELSE lbm.\"DrawdownDate\" - 19110000 ";
		sql += "            END ";
		sql += "		  ,ce.\"Fullname\"";
		sql += "   order by \"RepayAcctNo\",\"CustNo\", \"FacmNo\"                                           ";
		this.info("sql=" + sql);
		Query query;
//
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("entryDate", entryDate);
		query.setParameter("lastYearEntryDate", lastYearEntryDate);
		return this.convertToMap(query);
	}

	private int calDate(int today, int year, int month, int day) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setYears(year);
		dateUtil.setMons(month);
		dateUtil.setDays(day);
		today = dateUtil.getCalenderDay();
		return today;
	}
}