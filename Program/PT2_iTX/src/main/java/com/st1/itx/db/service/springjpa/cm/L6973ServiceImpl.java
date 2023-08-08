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

@Service("L6973ServiceImpl")
@Repository
public class L6973ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> doQuery(int index, int limit, TitaVo titaVo) throws Exception {
		this.info("L6973ServiceImpl.doQuery index:" + index + ",limit:" + limit);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		String sql = "";
		sql += " SELECT \"LogUkey\" ";
		sql += "      , \"LogDate\" ";
		sql += "      , LPAD(\"LogTime\",6,'0') AS \"LogTime\" ";
		sql += "      , \"UspName\" ";
		sql += "      , \"ErrorMessage\" ";
		sql += "      , \"ErrorBackTrace\" ";
		sql += "      , \"ExecEmpNo\" ";
		sql += " FROM \"UspErrorLog\" ";
		sql += " ORDER BY \"LogDate\" DESC ";
		sql += "        , LPAD(\"LogTime\",6,'0') DESC ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);

		return switchback(query);
	}

	public List<Map<String, String>> doQueryByJobTxSeq(String jobTxSeq, int index, int limit, TitaVo titaVo)
			throws Exception {
		this.info("L6973ServiceImpl.doQueryByJobTxSeq jobTxSeq:" + jobTxSeq + ",index:" + index + ",limit:" + limit);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		String sql = "";
		sql += " WITH u AS ( ";
		sql += " SELECT \"LogDate\" ";
		sql += "      , LPAD(\"LogTime\",6,'0') AS \"LogTime\" ";
		sql += "      , \"UspName\" ";
		sql += "      , \"ErrorMessage\" ";
		sql += "      , \"ErrorBackTrace\" ";
		sql += "      , \"ExecEmpNo\" ";
		sql += " FROM \"UspErrorLog\" ";
		sql += " WHERE \"JobTxSeq\" = :jobTxSeq ";
		sql += " )";
		sql += " , j AS ( ";
		sql += " SELECT \"ExecDate\"        AS \"LogDate\" ";
		sql += "      , CASE ";
		sql += "          WHEN \"LastUpdate\" IS NOT NULL ";
		sql += "          THEN TO_CHAR(\"LastUpdate\", 'HH24MISS') ";
		sql += "        ELSE ' ' ";
		sql += "        END                 AS \"LogTime\" ";
		sql += "      , \"StepId\"          AS \"UspName\" ";
		sql += "      , \"ErrContent\"      AS \"ErrorMessage\" ";
		sql += "      , \"ErrContent\"      AS \"ErrorBackTrace\" ";
		sql += "      , \"CreateEmpNo\"     AS \"ExecEmpNo\" ";
		sql += " FROM \"JobDetail\" ";
		sql += " WHERE \"TxSeq\" = :jobTxSeq ";
		sql += "   AND \"ErrContent\" IS NOT NULL ";
		sql += " )";
		sql += " SELECT TO_CHAR(\"LogDate\") AS \"LogDate\" ";
		sql += "      , TO_CHAR(\"LogTime\") AS \"LogTime\" ";
		sql += "      , TO_CHAR(\"UspName\") AS \"UspName\" ";
		sql += "      , TO_CHAR(\"ErrorMessage\") AS \"ErrorMessage\" ";
		sql += "      , TO_CHAR(\"ErrorBackTrace\") AS \"ErrorBackTrace\" ";
		sql += "      , TO_CHAR(\"ExecEmpNo\") AS \"ExecEmpNo\" ";
		sql += " FROM u ";
		sql += " UNION ALL ";
		sql += " SELECT TO_CHAR(\"LogDate\") AS \"LogDate\" ";
		sql += "      , TO_CHAR(\"LogTime\") AS \"LogTime\" ";
		sql += "      , TO_CHAR(\"UspName\") AS \"UspName\" ";
		sql += "      , TO_CHAR(\"ErrorMessage\") AS \"ErrorMessage\" ";
		sql += "      , TO_CHAR(\"ErrorBackTrace\") AS \"ErrorBackTrace\" ";
		sql += "      , TO_CHAR(\"ExecEmpNo\") AS \"ExecEmpNo\" ";
		sql += " FROM j ";
		sql += " ORDER BY \"LogDate\" DESC ";
		sql += "        , \"LogTime\" DESC ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);
		query.setParameter("jobTxSeq", jobTxSeq);

		return switchback(query);
	}

}