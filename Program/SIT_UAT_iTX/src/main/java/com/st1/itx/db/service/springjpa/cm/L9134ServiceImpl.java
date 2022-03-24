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

@Service
@Repository
public class L9134ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * L9134 暫收款傳票金額表(累計)
	 * 
	 * @param startDate 查詢區間起日
	 * @param endDate   查詢區間迄日
	 * @param titaVo    TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doQuery(int startDate, int endDate, TitaVo titaVo) {

		String sql = " ";
		sql += " WITH rawData AS ( ";
		sql += "     SELECT \"AcDate\" ";
		sql += "          , \"AcNoCode\" ";
		sql += "          , SUM( ";
		sql += "              CASE ";
		sql += "                WHEN \"DbCr\" = 'D' ";
		sql += "                THEN \"TxAmt\" ";
		sql += "              ELSE 0 END ";
		sql += "            ) AS \"DrAmt\" ";
		sql += "          , SUM( ";
		sql += "              CASE ";
		sql += "                WHEN \"DbCr\" = 'C' ";
		sql += "                THEN \"TxAmt\" ";
		sql += "              ELSE 0 END ";
		sql += "            ) AS \"CrAmt\" ";
		sql += "     FROM \"AcDetail\" ";
		sql += "     WHERE \"AcDate\" >= :startDate ";
		sql += "       AND \"AcDate\" <= :endDate ";
		sql += "       AND \"AcNoCode\" IN ('20222020000','20222180000') ";
		sql += "     GROUP BY \"AcDate\" ";
		sql += "            , \"AcNoCode\" ";
		sql += " ) ";
		sql += " SELECT \"AcDate\" ";
		sql += "      , \"AcNoCode\" ";
		sql += "      , \"DrAmt\" ";
		sql += "      , \"CrAmt\" ";
		sql += "      , \"DrAmt\" - \"CrAmt\" AS \"DiffAmt\" ";
		sql += " FROM rawData ";
		sql += " ORDER BY \"AcDate\" ";
		sql += "        , \"AcNoCode\" ";

		this.info("doQueryL9134A sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}

	/**
	 * L9134 暫收款傳票金額表(明細)
	 * 
	 * @param startDate 查詢區間起日
	 * @param endDate   查詢區間迄日
	 * @param titaVo  TitaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> doDetailQuery(int startDate, int endDate, TitaVo titaVo) {

		String sql = " ";
		sql += "     SELECT AD.\"AcDate\" ";
		sql += "          , AD.\"AcNoCode\" ";
		sql += "          , CASE ";
		sql += "              WHEN AD.\"DbCr\" = 'D' ";
		sql += "              THEN AD.\"TxAmt\" ";
		sql += "            ELSE 0 END AS \"DrAmt\" ";
		sql += "          , CASE ";
		sql += "              WHEN AD.\"DbCr\" = 'C' ";
		sql += "              THEN AD.\"TxAmt\" ";
		sql += "            ELSE 0 END AS \"CrAmt\" ";
		sql += "          , AD.\"SlipNo\" ";
		sql += "          , AD.\"TitaTxtNo\" ";
		sql += "          , LPAD(AD.\"CustNo\",7,'0') AS \"CustNo\" ";
		sql += "          , CM.\"CustName\" ";
		sql += "     FROM \"AcDetail\" AD ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = AD.\"CustNo\" ";
		sql += "                              AND AD.\"CustNo\" != 0 ";
		sql += "     WHERE \"AcDate\" >= :startDate ";
		sql += "       AND \"AcDate\" <= :endDate ";
		sql += "       AND \"AcNoCode\" IN ('20222020000','20222180000') ";
		sql += "     ORDER BY \"AcDate\" ";
		sql += "            , \"AcNoCode\" ";
		sql += "            , \"SlipNo\" ";
		sql += "            , \"TitaTxtNo\" ";
		
		this.info("doDetailQuery sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}
}