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
import com.st1.itx.util.parse.Parse;

@Service("L5903ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5903ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	String sql = "";

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

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iApplDateFrom = parse.stringToInteger(titaVo.getParam("ApplDateFrom")) + 19110000;
		int iApplDateTo = parse.stringToInteger(titaVo.getParam("ApplDateTo")) + 19110000;
		if (iApplDateTo == 19110000) {//無輸入起訖日時
			iApplDateTo = 99991231;
		}
		
		String iUsageCode = titaVo.getParam("UsageCode");
		String iApplCode = titaVo.getParam("ApplCode");

//		T(3,01:未還;02:已還;09:全部)

		this.info("L5903.findAll iCustNo=" + iCustNo);
		this.info("L5903.findAll iApplDateFrom=" + iApplDateFrom);
		this.info("L5903.findAll iApplDateTo=" + iApplDateTo);
		this.info("L5903.findAll iUsageCode=" + iUsageCode);
		this.info("L5903.findAll iApplCode=" + iApplCode);

		sql = " select  i.\"CustNo\"                                      ";
		sql += "        ,i.\"FacmNo\"                                     ";
		sql += "        ,i.\"ApplSeq\"                                    ";
		sql += "        ,c.\"CustName\"                                   ";
		sql += "        ,NVL(e1.\"Fullname\",i.\"KeeperEmpNo\")    AS F4  ";
		sql += "        ,NVL(e2.\"Fullname\",i.\"ApplEmpName\")    AS F5  ";
		sql += "        ,i.\"ApplDate\"                                   ";
		sql += "        ,i.\"ReturnDate\"                                 ";
		sql += "        ,NVL(e3.\"Fullname\",i.\"ReturnEmpNo\")    AS F8  ";
		sql += "        ,i.\"UsageCode\"                                  ";
		sql += "        ,i.\"CopyCode\"                                   ";
		sql += "        ,i.\"Remark\"                                     ";
		sql += "        ,i.\"ApplObj\"                                    ";
		sql += "        ,i.\"KeeperEmpNo\"                                ";
		sql += "        ,i.\"ApplEmpNo\"                                ";
		sql += "        ,i.\"ReturnEmpNo\"                                ";
		sql += "        ,i.\"TitaActFg\"                                ";
		sql += "        ,i.\"FacmNoMemo\"                                ";
		sql += " from \"InnDocRecord\" i                                  ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"  ";
		sql += " left join \"CdEmp\" e1 on e1.\"EmployeeNo\" = i.\"KeeperEmpNo\"  ";
		sql += " left join \"CdEmp\" e2 on e2.\"EmployeeNo\" = i.\"ApplEmpNo\"  ";
		sql += " left join \"CdEmp\" e3 on e3.\"EmployeeNo\" = i.\"ReturnEmpNo\"  ";

		if ("01".equals(iApplCode)) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"CustNo\"                                          ";
			sql += "     ,\"FacmNo\"                                          ";
			sql += "     ,\"ApplDate\"                                        ";
			sql += "     ,\"ApplSeq\"                                         ";
			sql += "     from \"InnDocRecord\"                                ";
			sql += "     where \"ApplCode\" = 2                               ";
			sql += " ) i2  on i2.\"CustNo\"  = i.\"CustNo\"                   ";
			sql += "      and i2.\"FacmNo\"  = i.\"FacmNo\"                   ";
			sql += "      and i2.\"ApplSeq\" = i.\"ApplSeq\"                  ";
		}

		sql += " where i.\"ApplDate\" >= " + iApplDateFrom;
		sql += "   and i.\"ApplDate\" <= " + iApplDateTo;

		if (iCustNo != 0) {
			sql += "   and i.\"CustNo\" = " + iCustNo;
		}

		if (!"00".equals(iUsageCode)) {
			sql += "   and i.\"UsageCode\" = " + iUsageCode;
		}

		if (!"09".equals(iApplCode)) {
			sql += "   and i.\"ApplCode\" = " + iApplCode;
			if ("01".equals(iApplCode)) {
				sql += " and NVL(i2.\"CustNo\",0) = 0                             ";
			}
		}

		sql += "order by i.\"CustNo\",i.\"FacmNo\" ,i.\"ApplSeq\"";
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

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}

}