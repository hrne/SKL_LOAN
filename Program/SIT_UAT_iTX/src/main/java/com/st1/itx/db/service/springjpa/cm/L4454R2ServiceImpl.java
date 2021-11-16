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

@Service("L4454R2ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454R2ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	private int entdy = 0;

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("銀扣失敗五萬元以上報表 findAll...");

		int entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;

		this.info("entryDate= " + entryDate);

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
		sql += "   , cc.\"CityItem\"                           AS F9                         ";
		sql += "   , ce.\"Fullname\"                           AS F10                        ";
		sql += "   from \"BankDeductDtl\" bd                                                    ";
		sql += "   left join \"LoanBorMain\" lbm   on lbm.\"CustNo\"    = bd.\"CustNo\"     ";
		sql += "                                and lbm.\"FacmNo\"    = bd.\"FacmNo\"       ";
		sql += "                                and lbm.\"BormNo\"    = bd.\"BormNo\"       ";
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
		sql += "           Distinct \"CustUKey\"                                             ";
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
		sql += "   where bd.\"EntryDate\" = :entryDate                                       ";
		sql += "     and bd.\"RepayType\" = 1                                                ";
	    sql += "     and bd.\"MediaCode\" = 'Y'                                              ";
		sql += "     and NVL(bd.\"ReturnCode\",'  ') not in ('  ','00')                      ";
		sql += "     and bd.\"RepayAmt\" >= 50000                                            ";
		sql += "   group by CASE WHEN                                                        ";
		sql += "                 lbm.\"PrevPayIntDate\" < 19110000                           ";
		sql += "            THEN lbm.\"PrevPayIntDate\"                                      ";
		sql += "            ELSE lbm.\"PrevPayIntDate\" - 19110000                           ";
		sql += "            END,                                                             ";
		sql += "            CASE WHEN fm.\"FirstDrawdownDate\" < 19110000                    ";
		sql += "            THEN fm.\"FirstDrawdownDate\"                                    ";
		sql += "            ELSE fm.\"FirstDrawdownDate\" - 19110000                         ";
		sql += "            END,                                                             ";
		sql += "            cc.\"CityItem\", ";
		sql += "            ce.\"Fullname\"  ";
		sql += "   order by \"F0\",\"F1\", \"F2\"                                           ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entryDate", entryDate);
		return this.convertToMap(query);
	}
}