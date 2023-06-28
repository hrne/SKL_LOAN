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

@Service("L4962ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4962ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4962.findAll");
		int iDrawdownDateFrom = Integer.parseInt(titaVo.getParam("DrawdownDateFrom"));
		int iDrawdownDateTo = Integer.parseInt(titaVo.getParam("DrawdownDateTo"));
		int iInsuEndMonthFrom2 = Integer.parseInt(titaVo.getParam("InsuEndMonthFrom2"));
		int iInsuEndMonthTo2 = Integer.parseInt(titaVo.getParam("InsuEndMonthTo2"));

		String sql = " select                                                    ";
		sql += "  coll.\"CustNo\"                                        AS F0   ";
		sql += " ,coll.\"FacmNo\"                                        AS F1   ";
		sql += " ,c.\"CustName\"                                         AS F2   ";
		sql += " ,NVL(LB.\"DrawdownDate\",0)                             AS F3   ";
//		sql += " ,NVL(f.\"FirstDrawdownDate\",0)                         AS F3   ";
		sql += " ,cf.\"ClCode1\"                                         AS F4   ";
		sql += " ,cf.\"ClCode2\"                                         AS F5   ";
		sql += " ,cf.\"ClNo\"                                            AS F6   ";
		sql += " ,NVL(rn.\"NowInsuNo\",NVL(og.\"OrigInsuNo\",''))        AS F7   ";
		sql += " ,NVL(rn.\"InsuStartDate\",NVL(og.\"InsuStartDate\",0))  AS F8   ";
		sql += " ,NVL(rn.\"InsuEndDate\",NVL(og.\"InsuEndDate\",0))      AS F9   ";
		sql += " ,NVL(rn.\"PrevInsuNo\",NVL(og.\"OrigInsuNo\",''))       AS F10  ";
		sql += " ,cd.\"Item\"                                            AS F11  ";
		sql += " ,NVL(rn.\"InsuYearMonth\",0)                            AS F12  ";
		sql += " from(                                                           ";
		sql += "   select                                                        ";
		sql += "    \"CustNo\"                                                   ";
		sql += "   ,\"FacmNo\"                                                   ";
		sql += "   ,lpad(decode(\"Status\",'4','0',\"Status\"), 2, '0') as \"Status\" ";
		sql += "     from \"CollList\"                                           ";
		sql += "    where \"Status\" in ('0','2','4','7')                    ";
		sql += " ) coll                                                          ";
		sql += " left join \"CdCode\" cd on cd.\"DefCode\" = 'ColStatus'         ";
		sql += "                        and cd.\"Code\" = coll.\"Status\"        ";
		sql += " left join \"ClFac\" cf   on cf.\"CustNo\" = coll.\"CustNo\"     ";
		sql += "                       and cf.\"FacmNo\" = coll.\"FacmNo\"       ";
		sql += " left join \"CustMain\" c on  c.\"CustNo\" = coll.\"CustNo\"     ";
		sql += " left join \"FacMain\"  f on  f.\"CustNo\" = coll.\"CustNo\"     ";
		sql += "                       and  f.\"FacmNo\" = coll.\"FacmNo\"       ";
		sql += " left join \"LoanBorMain\"  LB on  LB.\"CustNo\" = coll.\"CustNo\" ";
		sql += "                              and  LB.\"FacmNo\" = coll.\"FacmNo\" ";
		sql += " left join (                                                     ";
		sql += "     select                                                      ";
		sql += "     \"ClCode1\"                                                 ";
		sql += "    ,\"ClCode2\"                                                 ";
		sql += "    ,\"ClNo\"                                                    ";
		sql += "    ,\"InsuYearMonth\"                                           ";
		sql += "    ,\"InsuStartDate\"                                           ";
		sql += "    ,\"InsuEndDate\"                                             ";
		sql += "    ,\"PrevInsuNo\"                                              ";
		sql += "    ,\"NowInsuNo\"                                               ";
		sql += "    ,row_number() over (partition by \"ClCode1\", \"ClCode2\", \"ClNo\"  ";
		sql += "                order by \"InsuEndDate\" Desc, \"InsuStartDate\") as seq ";
		sql += "       from \"InsuRenew\"                                        ";
		sql += " ) rn on rn.\"ClCode1\" = cf.\"ClCode1\"                         ";
		sql += "     and rn.\"ClCode2\" = cf.\"ClCode2\"                         ";
		sql += "     and rn.\"ClNo\"    = cf.\"ClNo\"                            ";
		sql += "     and rn.seq = 1                                              ";
		sql += " left join (                                                     ";
		sql += "     select                                                      ";
		sql += "     \"ClCode1\"                                                 ";
		sql += "    ,\"ClCode2\"                                                 ";
		sql += "    ,\"ClNo\"                                                    ";
		sql += "    ,\"InsuStartDate\"                                           ";
		sql += "    ,\"InsuEndDate\"                                             ";
		sql += "    ,\"OrigInsuNo\"                                              ";
		sql += "    ,row_number() over (partition by \"ClCode1\", \"ClCode2\", \"ClNo\"  ";
		sql += "                order by \"InsuEndDate\" Desc, \"InsuStartDate\") as seq ";
		sql += "       from \"InsuOrignal\"                                      ";
		sql += " ) og on og.\"ClCode1\" = cf.\"ClCode1\"                         ";
		sql += "     and og.\"ClCode2\" = cf.\"ClCode2\"                         ";
		sql += "     and og.\"ClNo\" = cf.\"ClNo\"                               ";
		sql += "     and NVL(rn.\"InsuEndDate\",0) = 0                           ";
		sql += "     and og.seq = 1                                              ";
		sql += "  where NVL(f.\"FirstDrawdownDate\",0) != 0                      ";
		sql += "    and cf.\"ClCode1\" = 1                                       ";

		if (iDrawdownDateFrom > 0) {// 撥款區間
			sql += "   and NVL(LB.\"DrawdownDate\",0) >= :drawdownDateFrom ";
			sql += "   and NVL(LB.\"DrawdownDate\",0) <= :drawdownDateTo ";
			sql += " ORDER BY coll.\"CustNo\" ASC , coll.\"FacmNo\" ASC , LB.\"DrawdownDate\" ASC ";
		}

		if (iInsuEndMonthFrom2 > 0) {// 火險年月
			sql += "   and NVL(rn.\"InsuYearMonth\",0)  >= :insuEndMonthFrom2 ";
			sql += "   and NVL(rn.\"InsuYearMonth\",0)  <= :insuEndMonthTo2 ";
			sql += " ORDER BY coll.\"CustNo\" ASC , coll.\"FacmNo\" ASC , rn.\"InsuYearMonth\" ASC ";
		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		if (iDrawdownDateFrom > 0) {
			query.setParameter("drawdownDateFrom", iDrawdownDateFrom + 19110000);
			query.setParameter("drawdownDateTo", iDrawdownDateTo + 19110000);
		}
		if (iInsuEndMonthFrom2 > 0) {
			query.setParameter("insuEndMonthFrom2", iInsuEndMonthFrom2 + 191100);
			query.setParameter("insuEndMonthTo2", iInsuEndMonthTo2 + 191100);
		}

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findNoInsuCL(TitaVo titaVo) throws Exception {
        // 擔保品無保險單
		this.info("L4962.findClMain");
		String sql = " select                                                    ";
		sql += "  coll.\"CustNo\"                                        AS \"CustNo\"   ";
		sql += " ,coll.\"FacmNo\"                                        AS \"FacmNo\"   ";
		sql += " ,c.\"CustName\"                                         AS \"CustName\" ";
		sql += " ,cd.\"Item\"                                            AS \"Status\"   ";
		sql += " ,cf.\"ClCode1\"                                         AS \"ClCode1\"  ";
		sql += " ,cf.\"ClCode2\"                                         AS \"ClCode2\"  ";
		sql += " ,cf.\"ClNo\"                                            AS \"ClNo\"     ";
		sql += " from(                                                           ";
		sql += "   select                                                        ";
		sql += "    \"CustNo\"                                                   ";
		sql += "   ,\"FacmNo\"                                                   ";
		sql += "   ,lpad(decode(\"Status\",'4','0',\"Status\"), 2, '0') as \"Status\" ";
		sql += "    from \"CollList\"                                            ";
		sql += "    where \"Status\" in ('0','2','4','7')                        "; // 撥款的正常戶、逾期戶、催收戶、催呆戶(排除結案戶、呆帳戶、未撥款戶)
		sql += " ) coll                                                          ";
		sql += " left join \"CdCode\" cd on cd.\"DefCode\" = 'ColStatus'         ";
		sql += "                        and cd.\"Code\" = coll.\"Status\"        ";
		sql += " left join \"CustMain\" c on  c.\"CustNo\" = coll.\"CustNo\"     ";
		sql += " left join \"ClFac\" cf   on cf.\"CustNo\" = coll.\"CustNo\"     ";
		sql += "                       and cf.\"FacmNo\" = coll.\"FacmNo\"       ";
		sql += "                       and cf.\"ClCode1\" =  1                   ";
		sql += " left join \"ClMain\" cm on cm.\"ClCode1\" = cf.\"ClCode1\"      ";
		sql += "                        and cm.\"ClCode2\" = cf.\"ClCode2\"      "; 
		sql += "                        and cm.\"ClNo\" = cf.\"ClNo\"            ";
		sql += "                        and cm.\"ClStatus\" = '1'             "; // 已設定抵押的擔保品需有保險單， 擔保品狀況碼 :0:未抵押 1:已抵押
		sql += " left join \"InsuOrignal\" io on io.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                             and io.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                             and io.\"ClNo\" = cf.\"ClNo\"       ";
		sql += " where NVL(cm.\"ClStatus\",'0') = '1'                            ";
		sql += "  and NVL(io.\"ClNo\",0) = 0                                     ";
		sql += " group by coll.\"CustNo\"                                        ";
		sql += "         ,coll.\"FacmNo\"                                        ";
		sql += "         ,c.\"CustName\"                                         ";
		sql += "         ,cd.\"Item\"                                            ";
		sql += "         ,cf.\"ClCode1\"                                         ";
		sql += "         ,cf.\"ClCode2\"                                         ";
		sql += "         ,cf.\"ClNo\"                                            ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}
}