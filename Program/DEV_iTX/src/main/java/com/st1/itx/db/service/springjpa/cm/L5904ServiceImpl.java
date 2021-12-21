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

@Service("L5904ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5904ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	private String sql = "";

	private int applStartDate = 0;
	private int applEndDate = 0;
	private String usageCode = "";
	private int type = 0;
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

//		T(3,01:未還;02:已還;09:全部)

//		L5104 預設出報表皆為未歸還

		this.info("L5904.findAll applStartDate=" + applStartDate);
		this.info("L5904.findAll applEndDate=" + applEndDate);

		sql = " select   i.\"CustNo\"                                     "; // F0
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
		sql += "     ,\"ApplSeq\"                                        ";
		sql += "     from \"InnDocRecord\"                                ";
		sql += "     where \"ApplCode\" = 2                               ";
		sql += " ) i2  on i2.\"CustNo\"  = i.\"CustNo\"                   ";
		sql += "      and i2.\"FacmNo\"  = i.\"FacmNo\"                   ";
		sql += "      and i2.\"ApplSeq\" = i.\"ApplSeq\"                ";

		sql += " where i.\"ApplDate\" >= " + applStartDate;
		sql += "   and i.\"ApplDate\" <= " + applEndDate;

		if (!"00".equals(usageCode)) {
			sql += "   and \"UsageCode\" = " + usageCode;
		}
		if (type == 1 || type == 2) {
			sql += "   and i.\"ApplCode\" = 01  ";
			sql += "   and NVL(i2.\"CustNo\",0) = 0                             ";
		}
		sql += " order by  i.\"UsageCode\",i.\"ApplDate\",i.\"CustNo\"                          ";

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

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int applStartDate, int applEndDate, String usageCode, int type, int index, int limit, TitaVo titaVo) throws Exception {
		this.applStartDate = applStartDate;
		this.applEndDate = applEndDate;
		this.usageCode = usageCode;
		this.type = type;
		this.index = index;
		this.limit = limit;
		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}

}