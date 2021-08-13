package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.util.parse.Parse;

@Service("L4454R2ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454R2ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4454R2ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	private int entdy = 0;

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		logger.info("銀扣失敗五萬元以上報表 findAll...");

		entdy = titaVo.getEntDyI() + 19110000;

		logger.info("entdy = " + entdy);
		
		String sql = " select                                                                ";
		sql += "     bdd.\"RepayAcctNo\"                       AS F0                         ";
		sql += "   , LPAD(bd.\"CustNo\", 7 , '0')              AS F1                         ";
		sql += "   , LPAD(bd.\"FacmNo\", 3 , '0')              AS F2                         ";
		sql += "   , cm.\"CustName\"                           AS F3                         ";
		sql += "   , bd.\"RepayAmt\"                           AS F4                         ";
		sql += "   , NVL(ctl.\"PhoneNo\", '')                  AS F5                         ";
		sql += "   , NVL(bdd.\"RelCustName\", cm.\"CustName\") AS F6                         ";
		sql += "   , case when lbm.\"PrevPayIntDate\" < 19110000 then lbm.\"PrevPayIntDate\" ";
		sql += "          else lbm.\"PrevPayIntDate\" - 19110000 end     AS F7               ";
		sql += "   , case when fm.\"FirstDrawdownDate\" < 19110000 then fm.\"FirstDrawdownDate\" ";
		sql += "          else fm.\"FirstDrawdownDate\" - 19110000 end  AS F8               ";
		sql += "   , cc.\"CityItem\"                           AS F9                         ";
		sql += "   , ce.\"Fullname\"                           AS F10                        ";
		sql += "   from \"BatxDetail\" bd                                                    ";
		sql += "   left join \"BankDeductDtl\" bdd on bdd.\"MediaDate\" = bd.\"MediaDate\"   ";
		sql += "                                and bdd.\"MediaKind\" = bd.\"MediaKind\"     ";
		sql += "                                and bdd.\"MediaSeq\"  = bd.\"MediaSeq\"      ";
		sql += "   left join \"LoanBorMain\" lbm   on lbm.\"CustNo\"    = bdd.\"CustNo\"     ";
		sql += "                                and lbm.\"FacmNo\"    = bdd.\"FacmNo\"       ";
		sql += "                                and lbm.\"BormNo\"    = bdd.\"BormNo\"       ";
		sql += "   left join \"FacMain\" fm        on fm.\"CustNo\"     = bd.\"CustNo\"      ";
		sql += "                                and fm.\"FacmNo\"     = bd.\"FacmNo\"        ";
		sql += "   left join \"ClFac\" cf          on cf.\"CustNo\"     = fm.\"CustNo\"      ";
		sql += "                                and cf.\"FacmNo\"     = fm.\"FacmNo\"        ";
		sql += "                                and cf.\"MainFlag\"   = 'Y'                  ";
		sql += "   left join \"ClBuilding\" cb     on cb.\"ClCode1\"    = cf.\"ClCode1\"     ";
		sql += "                                and cb.\"ClCode2\"    = cf.\"ClCode2\"       ";
		sql += "                                and cb.\"ClNo\"       = cf.\"ClNo\"          ";
		sql += "   left join \"CdCity\" cc         on cc.\"CityCode\"   = cb.\"CityCode\"    ";
		sql += "   left join \"CustMain\" cm       on cm.\"CustNo\"     = bd.\"CustNo\"      ";
		sql += "   left join (                                                               ";
		sql += "          select                                                             ";
		sql += "           \"CustUKey\"                                                      ";
		sql += "          ,\"LiaisonName\"                                                   ";
		sql += "          ,\"Enable\"                                                        ";
		sql += "          ,NVL(\"TelArea\" || '-', '') || NVL(\"TelNo\", '') || NVL('-' || \"TelExt\", '') as \"PhoneNo\" ";
		sql += "          ,0 as\"Mobile\"                                                    ";
		sql += "          ,ROW_NUMBER() OVER (PARTITION BY \"CustUKey\", \"TelTypeCode\" ORDER BY \"LastUpdate\" Desc) AS SEQ ";
		sql += "          from \"CustTelNo\"                                                 ";
		sql += "          where \"TelTypeCode\" in ('01','02','03','05')                     ";
		sql += "            and \"Enable\" = 'Y'                                             ";
		sql += "      )    ctl ON ctl.\"CustUKey\" = cm.\"CustUKey\"                         ";
		sql += "              AND ctl.SEQ = 1                                                ";
		sql += "   left join \"CdEmp\" ce          on ce.\"EmployeeNo\" = fm.\"FireOfficer\" ";
		sql += "   left join \"BatxHead\" bh       on bh.\"AcDate\"     = bd.\"AcDate\"      ";
		sql += "                                and bh.\"BatchNo\"    = bd.\"BatchNo\"       ";
		sql += "   where bd.\"AcDate\" = " + entdy;
		sql += "     and bd.\"RepayCode\"    = 2                                             ";
		sql += "     and bh.\"BatxExeCode\" <> 8                                             ";
		sql += "     and (bd.\"ProcCode\" <> '00000' and substr(bd.\"ProcCode\",1,1) <> 'E') ";
		sql += "     and bd.\"RepayAmt\" >= 50000                                            ";

		logger.info("sql=" + sql);
		Query query;
//
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
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