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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2633ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2633ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L2633.findAll");

		// tita
		int iEntryDate = parse.stringToInteger(titaVo.getParam("TranDate")) + 19110000;

		String sql = " SELECT f.* ";
		sql += "FROM (SELECT \"CustNo\",\"FacmNo\",MAX(\"CloseNo\") AS MAXNO FROM \"FacClose\" GROUP BY \"CustNo\" ,\"FacmNo\" ) r  ";
		sql += "inner join \"FacClose\" f ";
		sql += "ON  r.\"CustNo\" = f.\"CustNo\"  ";
		sql += "AND r.\"FacmNo\" = f.\"FacmNo\"  ";
		sql += "AND r.\"MAXNO\" = f.\"CloseNo\"  ";
		sql += "AND f.\"CloseDate\"> 0 ";

		sql += "AND f.\"EntryDate\"= " + iEntryDate;
		sql += " AND f.\"FunCode\" in ( '1','0')							";

		sql += "order by f.\"CustNo\" ASC,f.\"FacmNo\"					";

		this.info("sql=" + sql);
		Query query;

		this.info("iEntryDate=" + iEntryDate);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
//		query.setParameter("entryDate", iEntryDate);
//		query.setParameter("applDate", iApplDate);
//		query.setParameter("type", iType);
		this.info("L2633Service FindData=" + query);

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

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAllB(TitaVo titaVo) throws Exception {

		this.info("L2633.findAll");

		// tita
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate")) + 19110000;

		String sql = " SELECT f.* ";
		sql += "FROM (SELECT \"CustNo\",\"FacmNo\",MAX(\"CloseNo\") AS MAXNO FROM \"FacClose\" GROUP BY \"CustNo\" ,\"FacmNo\" ) r  ";
		sql += "inner join \"FacClose\" f ";
		sql += "ON  r.\"CustNo\" = f.\"CustNo\"  ";
		sql += "AND r.\"FacmNo\" = f.\"FacmNo\"  ";
		sql += "AND r.\"MAXNO\" = f.\"CloseNo\"  ";
		sql += "AND f.\"CloseDate\"> 0 ";

		sql += "AND f.\"ApplDate\"= " + iApplDate;
		sql += " AND f.\"FunCode\" in ( '2','3')							";

		sql += "order by f.\"CustNo\" ASC,f.\"FacmNo\"					";

		this.info("sql=" + sql);
		Query query;

		this.info("iApplDate=" + iApplDate);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
//		query.setParameter("entryDate", iEntryDate);
//		query.setParameter("applDate", iApplDate);
//		query.setParameter("type", iType);
		this.info("L2633Service FindData=" + query);

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

	public List<Map<String, String>> findAllB(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAllB(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}