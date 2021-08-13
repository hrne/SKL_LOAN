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

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4043ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4043ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4043ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

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

		logger.info("L4920.findAll");

		int iSearchFlag = parse.stringToInteger(titaVo.get("SearchFlag"));
		int iDateFrom = parse.stringToInteger(titaVo.get("DateFrom")) + 19110000;
		int iDateTo = parse.stringToInteger(titaVo.get("DateTo")) + 19110000;
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		String iRepayAcct = FormatUtil.pad9(titaVo.get("RepayAcct").trim(), 14);

		String sql = " select                                                         ";
		sql += "  p.\"CustNo\"            as F0                                       ";
		sql += " ,p.\"FacmNo\"            as F1                                       ";
		sql += " ,p.\"AuthCode\"          as F2                                       ";
		sql += " ,''      as F3                                       ";
		sql += " ,p.\"PostDepCode\"       as F4                                       ";
		sql += " ,''      as F5                                       ";
		sql += " ,''      as F6                                       ";
		sql += " ,''      as F7                                       ";
		sql += " ,p.\"RepayAcctSeq\"      as F8                                       ";
		sql += " ,p.\"CustId\"            as F9                                       ";
		sql += " ,p.\"AuthApplCode\"      as F10                                      ";
		sql += " ,p.\"AuthCreateDate\"    as F11                                      ";
		sql += " ,p.\"PropDate\"          as F12                                      ";
		sql += " ,p.\"RetrDate\"          as F13                                      ";
		sql += " ,p.\"StampCode\"         as F14                                      ";
		sql += " ,p.\"AuthErrorCode\"     as F15                                      ";
		sql += " ,p.\"PostMediaCode\"     as F16                                      ";
		sql += " ,p.\"AmlRsp\"            as F17                                      ";
		sql += " ,p.\"RepayAcct\"         as F18                                      ";
		sql += "  from (                                                              ";
		sql += "     select                                                           ";
		sql += "       \"CustNo\"                                                     ";
		sql += "      ,\"FacmNo\"                                                     ";
		sql += "      ,\"RepayAcct\"                                                  ";
		sql += "      ,\"CustId\"                                                     ";
		sql += "      ,\"AuthCode\"                                                   ";
		sql += "      ,\"AuthApplCode\"                                               ";
		sql += "      ,\"AuthCreateDate\"                                             ";
		sql += "      ,\"PropDate\"                                                   ";
		sql += "      ,\"RetrDate\"                                                   ";
		sql += "      ,\"StampCode\"                                                  ";
		sql += "      ,\"AuthErrorCode\"                                              ";
		sql += "      ,\"PostMediaCode\"                                              ";
		sql += "      ,\"RepayAcctSeq\"                                               ";
		sql += "      ,\"PostDepCode\"                                                ";
		sql += "      ,\"AmlRsp\"                                                     ";
		sql += "      ,row_number() over (partition by \"CustNo\",\"RepayAcct\",\"AuthCode\",\"PostDepCode\" order by \"CreateDate\" Desc) as seq         ";
		sql += "       from \"PostAuthLog\"                                           ";
		sql += "      where \"DeleteDate\" = 0                                        ";
		if (iSearchFlag == 1) {
			sql += "        and \"AuthCreateDate\" >= " + iDateFrom;
			sql += "        and \"AuthCreateDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 2) {
			sql += "        and \"PropDate\" >= " + iDateFrom;
			sql += "        and \"PropDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 3) {
			sql += "        and \"RetrDate\" >= " + iDateFrom;
			sql += "        and \"RetrDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 4) {
			sql += "        and \"CustNo\" = " + iCustNo;
		}
		if (iSearchFlag == 5) {
			sql += "        and \"RepayAcct\" = " + iRepayAcct;
		}
		sql += " ) p                                                                  ";
		sql += " where p.seq = 1                                                      ";
		sql += " order by p.\"CustNo\", p.\"FacmNo\", p.\"RepayAcct\", p.\"AuthCode\" ";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		logger.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		logger.info("Total size ..." + size);

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