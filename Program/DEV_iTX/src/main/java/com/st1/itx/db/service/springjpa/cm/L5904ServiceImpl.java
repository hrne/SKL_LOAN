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
import com.st1.itx.util.parse.Parse;

@Service("L5904ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5904ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5904ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	private String sql = "";

	private int applStartDate = 0;
	private int applEndDate = 0;
	private String usageCode = "";

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

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

//		T(3,01:未還;02:已還;09:全部)

//		L5104 預設出報表皆為未歸還

		logger.info("L5904.findAll applStartDate=" + applStartDate);
		logger.info("L5904.findAll applEndDate=" + applEndDate);

		sql = " select  i.\"CustNo\"                                      "; // F0
		sql += "        ,i.\"FacmNo\"                                     "; // F1
		sql += "        ,i.\"ApplSeq\"                                    "; // F2
		sql += "        ,c.\"CustName\"                                   "; // F3
		sql += "        ,i.\"KeeperEmpNo\"                                "; // F4
		sql += "        ,i.\"ApplEmpNo\"                                  "; // F5
		sql += "        ,i.\"ApplDate\"                                   "; // F6
		sql += "        ,i.\"ReturnDate\"                                 "; // F7
		sql += "        ,i.\"ReturnEmpNo\"                                "; // F8
		sql += "        ,i.\"UsageCode\"                                  "; // F9
		sql += "        ,i.\"CopyCode\"                                   "; // F10
		sql += "        ,i.\"Remark\"                                     "; // F11
		sql += "        ,i.\"ApplObj\"                                    "; // F12
		sql += " from \"InnDocRecord\" i                                  ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"  ";

		sql += " left join (                                              ";
		sql += "     select                                               ";
		sql += "      \"CustNo\"                                          ";
		sql += "     ,\"FacmNo\"                                          ";
		sql += "     ,\"ApplDate\"                                        ";
		sql += "     from \"InnDocRecord\"                                ";
		sql += "     where \"ApplCode\" = 2                               ";
		sql += " ) i2  on i2.\"CustNo\"  = i.\"CustNo\"                   ";
		sql += "      and i2.\"FacmNo\"  = i.\"FacmNo\"                   ";
		sql += "      and i2.\"ApplDate\" = i.\"ApplDate\"                ";

		sql += " where i.\"ApplDate\" >= " + applStartDate;
		sql += "   and i.\"ApplDate\" <= " + applEndDate;

		if (!"00".equals(usageCode)) {
			sql += "   and \"UsageCode\" = " + usageCode;
		}

		sql += " and NVL(i2.\"CustNo\",0) = 0                             ";

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

		return this.convertToMap(query.getResultList());
	}

	public List<Map<String, String>> findAll(int applStartDate, int applEndDate, String usageCode, int index, int limit,
			TitaVo titaVo) throws Exception {
		this.applStartDate = applStartDate;
		this.applEndDate = applEndDate;
		this.usageCode = usageCode;
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}

}