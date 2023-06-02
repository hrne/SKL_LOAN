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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L4928ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Autowired
	Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

//	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	public List<Map<String, String>> queryresult(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L4928ServiceImpl.queryresult");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;


		String sql = "";
		sql += " select ";
		sql += "  l.\"CustNo\"			AS \"CustNo\" ";
		sql += " ,l.\"FacmNo\"			AS \"FacmNo\" ";
		sql += " ,l.\"BormNo\"			AS \"BormNo\" ";
		sql += " ,l.\"PrevPayIntDate\"	AS \"PrevPayIntDate\" ";
		sql += " from  ";
		sql += "  (select  ";
		sql += "    \"CustNo\"  ";
		sql += "   ,\"FacmNo\"  ";
		sql += "   ,max(\"PrevPayIntDate\") as MAXDATE  ";
		sql += "   ,min(\"PrevPayIntDate\") as MINDATE  ";
		sql += "   from \"LoanBorMain\"  ";
		sql += "   where \"Status\" = 0  ";
		sql += "   and \"PrevPayIntDate\" > \"DrawdownDate\"  ";
		sql += "   group by \"CustNo\"  ";
		sql += "           ,\"FacmNo\"  ";
		sql += " ) a  ";
		sql += " left join \"LoanBorMain\" l on l.\"CustNo\" = a.\"CustNo\"  ";
		sql += "                          and l.\"FacmNo\" = a.\"FacmNo\"  ";
		sql += "                          and l.\"Status\" = 0  ";
		sql += "                          and l.\"PrevPayIntDate\" > l.\"DrawdownDate\"  ";
		sql += " where a.MAXDATE <> a.MINDATE  ";
		sql += " order by  l.\"CustNo\"  ";
		sql += "          ,l.\"FacmNo\"  ";
		sql += "          ,l.\"BormNo\"  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);


		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(0);
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);

	}

	public int getSize() {
		return cnt;
	}

}