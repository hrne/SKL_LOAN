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

@Service("bS600ServiceImpl")
@Repository
public class BS600ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String conditionSql;

	private Query query;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> execSql(TitaVo titaVo) throws Exception {
		// 依戶號加總，按帳冊別、分配順序、戶號排序
		this.info("bS600ServiceImpl.find");

		String sql = "";
		sql += "SELECT S1.\"CustNo\" ";
		sql += "      ,S1.\"AcBookCode\" ";
		sql += "      ,S1.\"AcSubBookCode\" ";
		sql += "      ,SUM(S1.\"RvBal\") AS \"Bal\" ";
		sql += "      ,NVL(S2.\"AssignSeq\",0) AS \"AssignSeq\" ";
		sql += "FROM \"AcReceivable\" S1 ";
		sql += "LEFT JOIN \"CdAcBook\" S2 ON S2.\"AcBookCode\" = S1.\"AcBookCode\" ";
		sql += "                         AND S2.\"AcSubBookCode\" = S1.\"AcSubBookCode\" ";
		sql += "WHERE S1.\"AcctFlag\" = 1 ";
		sql += "GROUP BY S1.\"CustNo\",S1.\"AcBookCode\",S1.\"AcSubBookCode\",NVL(S2.\"AssignSeq\",0) ";
		sql += "ORDER BY \"AcBookCode\",\"AssignSeq\",\"CustNo\" ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(0);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(Integer.MAX_VALUE);

		@SuppressWarnings("unchecked")
		List<Object> result = query.getResultList();

		return this.convertToMap(result);
	}

}