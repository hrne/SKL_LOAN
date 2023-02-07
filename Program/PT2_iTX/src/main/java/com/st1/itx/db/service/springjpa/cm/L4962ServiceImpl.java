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

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

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
		sql += " ,coll.\"Status\"                                        AS F11  ";
		sql += " ,NVL(rn.\"InsuYearMonth\",0)                            AS F12  ";
		sql += " from(                                                           ";
		sql += "   select                                                        ";
		sql += "    \"CustNo\"                                                   ";
		sql += "   ,\"FacmNo\"                                                   ";
		sql += "   ,\"Status\"                                                   ";
		sql += "     from \"CollList\"                                           ";
		sql += "    where \"Status\" in ('0','2','4','7')                    ";
		sql += " ) coll                                                          ";
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

		if (iDrawdownDateFrom > 0) {//撥款區間
			sql += "   and NVL(LB.\"DrawdownDate\",0) >= :drawdownDateFrom ";
			sql += "   and NVL(LB.\"DrawdownDate\",0) <= :drawdownDateTo ";
			sql += " ORDER BY coll.\"CustNo\" ASC , coll.\"FacmNo\" ASC , LB.\"DrawdownDate\" ASC ";  
		}

		if (iInsuEndMonthFrom2 > 0) {//火險年月
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
		
		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}