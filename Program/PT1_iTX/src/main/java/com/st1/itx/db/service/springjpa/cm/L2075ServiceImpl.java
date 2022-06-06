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

@Service("L2075ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2075ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L2075.findAll");

		// tita
		int iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate")) + 19110000;
		int iType = parse.stringToInteger(titaVo.getParam("Type"));
		String sql = "";
		sql = " SELECT														";
		sql += "r2.\"CustNo\",												";
		sql += "r2.\"FacmNo\",												";
		sql += "r2.maxno,													";
		sql += "cf.\"ClCode1\",												";
		sql += "cf.\"ClCode2\",												";
		sql += "cf.\"ClNo\"													";
		sql += "FROM														";
		sql += "(SELECT														";
		sql += "r.\"CustNo\",												";
		sql += "r.\"FacmNo\",												";
		sql += "r.maxno														";
		sql += "FROM														";
		sql += "(															";
		sql += "SELECT														";
		sql += "\"CustNo\",													";
		sql += "\"FacmNo\",													";
		sql += " MAX(\"CloseNo\") AS maxno									";
		sql += "FROM														";
		sql += "\"FacClose\"												";
		sql += "GROUP BY													";
		sql += "\"CustNo\",													";
		sql += "\"FacmNo\"													";
		sql += ") r  					inner join \"FacClose\"          fc	";
		sql += "ON fc.\"CustNo\" = r.\"CustNo\"								";
		sql += "AND fc.\"CloseNo\" = r.maxno								";
		if (iEntryDate > 19110000) {
			sql += "AND  fc.\"EntryDate\" = 								" + iEntryDate;
			sql += " AND \"FunCode\" in ( '1','0')							";
		} else {
			sql += "AND fc.\"ApplDate\"= 									" + iApplDate;
			sql += " AND \"FunCode\" in ( '2','3')							";
		}
		sql += " ) r2														";
		sql += "left join \"ClFac\"   cf ON r2.\"CustNo\" = cf.\"CustNo\"	";
		sql += " AND r2.\"FacmNo\" = cf.\"FacmNo\"							";
		sql += "order by r2.\"CustNo\" ASC,r2.\"FacmNo\"					";

		this.info("sql=" + sql);
		Query query;

		this.info("iEntryDate=" + iEntryDate);
		this.info("iApplDate=" + iApplDate);
		this.info("iType=" + iType);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
//		query.setParameter("entryDate", iEntryDate);
//		query.setParameter("applDate", iApplDate);
//		query.setParameter("type", iType);
		this.info("L2075Service FindData=" + query);

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