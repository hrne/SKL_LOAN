package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9751ServiceImpl extends ASpringJpaParm implements InitializingBean {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {
		this.info("L9751ServiceImpl.findAll");

		int endDate = parse.stringToInteger(titaVo.get("EndDate")) + 19110000;

		dateUtil.init();
		dateUtil.setDate_1(endDate);
		int startDate = dateUtil.getLastMonthEndBussinessDate(endDate);
		
		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += "	SELECT ";
		sql += "	    \"AcAcctCheck\".\"AcDate\" AS \"AcDate\",";
		sql += "	    NVL(\"AcctMasterBal\",0) AS \"AcctMasterBal\",";
		sql += "	    NVL(\"TdBal\",0) AS \"TdBal\",";
		sql += "	    NVL(\"DbAmt\",0) AS \"DbAmt\",";
		sql += "	    NVL(\"CrAmt\",0) AS \"CrAmt\",";
		sql += "	    NVL(\"MasterClsAmt\",0) AS \"MasterClsAmt\",";
		sql += "	    NVL(\"TxAmt\",0) AS \"TxAmt\" ";
		sql += "	FROM";
		sql += "	(";
		sql += "	    SELECT";
		sql += "	        \"AcDate\",";
		sql += "	        SUM(CASE WHEN \"AcctCode\" IN ('TMI') THEN \"AcctMasterBal\" ELSE 0 END) AS \"AcctMasterBal\",";
		sql += "	        SUM(CASE WHEN \"AcctCode\" IN ('TMI') THEN \"TdBal\" ELSE 0 END) AS \"TdBal\",";
		sql += "	        SUM(CASE WHEN \"AcctCode\" IN ('TMI') THEN \"DbAmt\" ELSE 0 END) AS \"DbAmt\",";
		sql += "	        SUM(CASE WHEN \"AcctCode\" IN ('TMI') THEN \"CrAmt\" ELSE 0 END) AS \"CrAmt\",";
		sql += "	        SUM(CASE WHEN \"AcctCode\" IN ('TMI', 'F09', 'F25') THEN \"MasterClsAmt\" ELSE 0 END) AS \"MasterClsAmt\"";
		sql += "	    FROM \"AcAcctCheck\"";
		sql += "	    WHERE (\"AcDate\" BETWEEN :startDate AND :endDate) ";
		sql += "	    AND \"AcctCode\" IN ('TMI', 'F09', 'F25')";
		sql += "	    GROUP BY \"AcDate\"";
		sql += "	    ORDER BY \"AcDate\"";
		sql += "	) \"AcAcctCheck\"";
		sql += "	LEFT JOIN";
		sql += "	(";
		sql += "	    SELECT";
		sql += "	        \"AcDate\",";
		sql += "	        SUM(";
		sql += "	            CASE WHEN \"AcctCode\" IN ('TMI') AND \"TitaTxCd\" = 'L4604'";
		sql += "	                THEN (CASE WHEN \"DbCr\" = 'D' THEN \"TxAmt\" ELSE 0 - \"TxAmt\" END)";
		sql += "	            WHEN \"AcctCode\" IN ('F09', 'F25')";
		sql += "	                THEN (CASE WHEN \"DbCr\" = 'C' THEN \"TxAmt\" ELSE 0 - \"TxAmt\" END)";
		sql += "	            ELSE 0 END";
		sql += "	        ) AS \"TxAmt\"";
		sql += "	    FROM \"AcDetail\"";
		sql += "	    WHERE (\"AcDate\" BETWEEN :startDate AND :endDate) ";
		sql += "	    AND \"AcctCode\" IN ('TMI', 'F09', 'F25') AND \"EntAc\" = 1";		
		sql += "	    GROUP BY \"AcDate\"";
		sql += "	    ORDER BY \"AcDate\"";
		sql += "	) \"AcDetail\"";
		sql += "	ON \"AcAcctCheck\".\"AcDate\" = \"AcDetail\".\"AcDate\" ";
		sql += "	ORDER BY \"AcAcctCheck\".\"AcDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		return this.convertToMap(query);
	}

}