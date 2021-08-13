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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
/* 債權案件明細查詢 */
public class L5975ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5975ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	public Parse parse;
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// *** 折返控制相關 ***
		private int index;

		// *** 折返控制相關 ***
		private int limit;

		private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
		
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findFindCode(TitaVo titaVo, int index, int limit) throws Exception {
		logger.info("L5975ServiceImpl.findAll ");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		
		String sql = " ";
		
		sql += " SELECT \"FinCode\"                                                  "; // -- F0 機構代碼
		sql += "      , SUM(\"ApprAmt\")                  AS \"AMT\"                 "; // -- F1 總金額
		sql += "      , COUNT(\"FinCode\")                AS \"CNT\"                 "; // -- F2 總筆數
		sql += " FROM \"NegAppr01\" ";
		sql += "  WHERE \"BringUpDate\" = :BringUpDate";
		sql += "   GROUP BY  \"FinCode\" ";
		sql += "   ORDER BY  \"FinCode\" ";
		sql += sqlRow;
		
		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("BringUpDate", parse.stringToInteger(titaVo.getParam("BringUpDate").trim()) + 19110000);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		
		return this.convertToMap(query.getResultList());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findDataSendUnit(TitaVo titaVo, int index, int limit) throws Exception {
		logger.info("L5975ServiceImpl.findAll ");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		
		String sql = " ";
		
		sql += " SELECT \"DataSendUnit\"                                                  "; // -- F0 資料傳送單位
		sql += "      , SUM(\"ApprAmt\")                       AS \"AMT\"                 "; // -- F1 總金額
		sql += "      , COUNT(\"DataSendUnit\")                AS \"CNT\"                 "; // -- F2 總筆數
		sql += " FROM \"NegAppr01\" ";
		sql += "  WHERE \"BringUpDate\" = :BringUpDate";
		sql += "   GROUP BY  \"DataSendUnit\" ";
		sql += "   ORDER BY  \"DataSendUnit\" ";
		sql += sqlRow;
		
		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("BringUpDate", parse.stringToInteger(titaVo.getParam("BringUpDate").trim()) + 19110000);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		
		return this.convertToMap(query.getResultList());
	}
}