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


		String sql = " select * from ( select                                         ";
		sql += "  p.\"CustNo\"            as F0                                       ";
		sql += " ,p.\"FacmNo\"            as F1                                       ";
		sql += " ,p.\"AuthCode\"          as F2                                       ";
		sql += " ,'700'                   as F3                                       ";
		sql += " ,p.\"PostDepCode\"       as F4                                       ";
		sql += " ,p.\"RepayAcct\"         as F5                                       ";
		sql += " ,''                      as F6                                       ";
		sql += " ,''                      as F7                                       ";
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
		sql += " ,p.\"StampFinishDate\"   as F19                                      ";
		sql += " ,p.\"DeleteDate\"        as F20                                      ";
		sql += " ,row_number() over (partition by p.\"CustNo\",p.\"RepayAcct\",p.\"AuthCode\",p.\"PostDepCode\" order by p.\"CreateDate\" Desc) as F21 ";
		sql += " from \"PostAuthLog\" p                                             ";
		sql += " where                                                                ";
		if (iSearchFlag == 1) {
			sql += "            \"AuthCreateDate\" >= " + iDateFrom;
			sql += "        and \"AuthCreateDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 2) {
			sql += "            \"PropDate\" >= " + iDateFrom;
			sql += "        and \"PropDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 3) {
			sql += "            \"RetrDate\" >= " + iDateFrom;
			sql += "        and \"RetrDate\" <= " + iDateTo;
		}
		if (iSearchFlag == 4) {
			sql += "            \"CustNo\" = " + iCustNo;
		}
		if (iSearchFlag == 5) {
			sql += "            \"RepayAcct\" = " + iRepayAcct;
		}
		sql += " order by p.\"CustNo\", p.\"FacmNo\", p.\"CreateDate\" Desc , p.\"AuthCode\" ";
        sql += " ) a where a.\"F21\" = 1 ";

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