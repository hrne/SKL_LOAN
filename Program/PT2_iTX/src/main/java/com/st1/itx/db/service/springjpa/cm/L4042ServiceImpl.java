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
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4042ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4042ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

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

		this.info("L4920.findAll");

		int iSearchFlag = parse.stringToInteger(titaVo.get("SearchFlag"));
		int iDateFrom = parse.stringToInteger(titaVo.get("DateFrom")) + 19110000;
		int iDateTo = parse.stringToInteger(titaVo.get("DateTo")) + 19110000;
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iRepayBank = parse.stringToInteger(titaVo.get("RepayBank"));
		
		String iRepayAcct = FormatUtil.pad9(titaVo.get("RepayAcct").trim(), 14);

		String sql = "";
		sql += " select * from (                                                             ";
		sql += " select                                                                      ";
		sql += "     act.\"CustNo\"            as F0                                         ";
		sql += "    ,act.\"FacmNo\"            as F1                                         ";
		sql += "    ,'0'                       as F2                                         ";
		sql += "    ,act.\"RepayBank\"         as F3                                         ";
		sql += "    ,act.\"RepayAcct\"         as F4                                         ";
		sql += "    ,'0'                       as F5                                         ";
		sql += "    ,act.\"LimitAmt\"          as F6                                         ";
		sql += "    ,act.\"CreateFlag\"        as F7                                         ";
		sql += "    ,act.\"AuthCreateDate\"    as F8                                         ";
		sql += "    ,act.\"PropDate\"          as F9                                         ";
		sql += "    ,act.\"RetrDate\"          as F10                                        ";
		sql += "    ,act.\"AuthStatus\"        as F11                                        ";
		sql += "    ,act.\"MediaCode\"         as F12                                        ";
		sql += "    ,act.\"AmlRsp\"            as F13                                        ";
		sql += "    ,act.\"RepayBank\"         as F14                                        ";
		sql += "    ,act.\"RepayAcct\"         as F15                                        ";
		sql += "    ,act.\"StampFinishDate\"   as F16                                        ";
		sql += "    ,act.\"DeleteDate\"        as F17                                        ";
		sql += "    ,case when row_number() over (partition by act.\"CustNo\",act.\"RepayBank\",act.\"RepayAcct\" order by act.\"CreateDate\" Desc) = 1 then 1 else 0 end as F18 ";
		sql += "    ,act.\"TitaTxCd\"          as F19                                        ";
		sql += "    ,act.\"CreateEmpNo\"          as F20                                        ";
		sql += "    ,act.\"ProcessDate\"          as F21                                        ";
		sql += "    ,act.\"ProcessTime\"          as F22                                        ";
		sql += "    ,act.\"AuthMeth\"          as F23                                        ";
		sql += "    ,case when act.\"LogNo\" is null then 'N' else 'Y' end as F24            ";
		sql += "   from \"AchAuthLog\" act                                            ";
		sql += "   left join \"AchAuthLogHistory\" ah on ah.\"CustNo\" = act.\"CustNo\"  ";
		sql += "                                    and ah.\"FacmNo\" = act.\"FacmNo\"  ";
		sql += "  where                                                                    ";
		if (iSearchFlag == 1) {
			sql += "            act.\"AuthCreateDate\" >= " + iDateFrom;
			sql += "        and act.\"AuthCreateDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 2) {
			sql += "            act.\"PropDate\" >= " + iDateFrom;
			sql += "        and act.\"PropDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 3) {
			sql += "            act.\"RetrDate\" >= " + iDateFrom;
			sql += "        and act.\"RetrDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 4) {
			sql += "            act.\"CustNo\" = " + iCustNo;
		}
		if (iSearchFlag == 5) {
			sql += "            act.\"RepayAcct\" = " + iRepayAcct;
		}
		if(iRepayBank ==998) {
			sql += "          and  act.\"RepayBank\" <> " + "103";
		}
		if(iRepayBank ==103) {
			sql += "          and  act.\"RepayBank\" = " + "103";
		}
		sql += "  order by act.\"CustNo\",act.\"FacmNo\",act.\"CreateDate\" Desc  ";
		sql += " ) a where a.\"F18\" = 1                                          ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

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