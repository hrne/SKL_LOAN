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
		sql += "     MIN(bd.\"RepayAcctNo\")                        AS F0                         ";
		sql += "   , MIN(LPAD(bd.\"CustNo\", 7 , '0'))              AS F1                         ";
		sql += "   , MIN(LPAD(bd.\"FacmNo\", 3 , '0'))              AS F2                         ";
		sql += "   , MIN(cm.\"CustName\")                           AS F3                         ";
		sql += "   , MIN(bd.\"RepayAmt\")                           AS F4                         ";
		sql += "   , MIN(NVL(ctl.\"PhoneNo\", ''))                  AS F5                         ";
		sql += "   , MIN(NVL(bd.\"RelCustName\", cm.\"CustName\"))  AS F6                         ";
		sql += "   , case when lbm.\"PrevPayIntDate\" < 19110000 then lbm.\"PrevPayIntDate\" ";
		sql += "          else lbm.\"PrevPayIntDate\" - 19110000 end     AS F7               ";
		sql += "   , case when fm.\"FirstDrawdownDate\" < 19110000 then fm.\"FirstDrawdownDate\" ";
		sql += "          else fm.\"FirstDrawdownDate\" - 19110000 end  AS F8               ";
		sql += "   , ce.\"Fullname\"                           AS F9                         ";
		sql += "   from \"BankDeductDtl\" bd                                                    ";
		sql += "   left join \"LoanBorMain\" lbm   on lbm.\"CustNo\"    = bd.\"CustNo\"     ";
		sql += "                                and lbm.\"FacmNo\"    = bd.\"FacmNo\"       ";
		sql += "   left join \"FacMain\" fm        on fm.\"CustNo\"     = bd.\"CustNo\"      ";
		sql += "                                and fm.\"FacmNo\"     = bd.\"FacmNo\"        ";
		sql += "   left join \"CustMain\" cm       on cm.\"CustNo\"     = bd.\"CustNo\"      ";
		sql += "   left join (                                                               ";
		sql += "          select                                                             ";
		sql += "           \"CustUKey\"                                                      ";
		sql += "          ,\"LiaisonName\"                                                   ";
		sql += "          ,\"Enable\"                                                        ";
		sql += "          ,NVL(\"TelArea\" || '-', '') || NVL(\"TelNo\", '') || NVL('-' || \"TelExt\", '') as \"PhoneNo\" ";
		sql += "          ,0 as \"Mobile\"                                                   ";
		sql += "          ,ROW_NUMBER() OVER (PARTITION BY \"CustUKey\", \"TelTypeCode\" ORDER BY \"LastUpdate\" Desc) AS SEQ ";
		sql += "          from \"CustTelNo\"                                                 ";
		sql += "          where \"TelTypeCode\" in ('01','02','03','05')                     ";
		sql += "            and \"Enable\" = 'Y'                                             ";
		sql += "      )    ctl ON ctl.\"CustUKey\" = cm.\"CustUKey\"                         ";
		sql += "              AND ctl.SEQ = 1                                                ";
		sql += "   left join \"CdEmp\" ce          on ce.\"EmployeeNo\" = fm.\"FireOfficer\" ";
		sql += "   where bd.\"EntryDate\" = :entryDate                                       ";
		sql += "     and bd.\"MediaCode\" = 'Y'                                              ";
		sql += "     and bd.\"RepayType\" = 1                                                ";
		sql += "     and NVL(bd.\"ReturnCode\",'  ') not in ('  ','00')                      ";
		sql += "     and (fm.\"FirstDrawdownDate\" >= :lastYearEntryDate";
		sql += "           and  fm.\"FirstDrawdownDate\" <= :entryDate";
		sql += "           )                                                                 ";
		sql += "   group by CASE WHEN                                                        ";
		sql += "                 lbm.\"PrevPayIntDate\" < 19110000 ";
		sql += "                 THEN lbm.\"PrevPayIntDate\" ";
		sql += "                 ELSE lbm.\"PrevPayIntDate\" - 19110000 ";
		sql += "            END, ";
		sql += "            CASE WHEN fm.\"FirstDrawdownDate\" < 19110000 ";
		sql += "                 THEN fm.\"FirstDrawdownDate\" ";
		sql += "                 ELSE fm.\"FirstDrawdownDate\" - 19110000 ";
		sql += "            END, ";
		sql += "ce.\"Fullname\"";
		sql += "   order by \"F0\",\"F1\", \"F2\"                                           ";
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