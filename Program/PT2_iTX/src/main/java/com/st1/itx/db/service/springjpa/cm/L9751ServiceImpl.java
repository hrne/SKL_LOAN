package com.st1.itx.db.service.springjpa.cm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9751ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {
		this.info("L9751ServiceImpl.findAll");

		// 因報表需計算需有前一日資料，故挑整取資料日期區間
		int inputStartDate = parse.stringToInteger(titaVo.get("DateInputStr")) + 19110000;
		this.info("inputStartDate:"+ inputStartDate);
		LocalDate validDatePivot = LocalDate.of(inputStartDate / 10000,
				(inputStartDate / 100) % 100, inputStartDate % 100);
		
		String startDate = validDatePivot.minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE).toString();
		String endDate = Integer.toString(parse.stringToInteger(titaVo.get("DateInputEnd")) + 19110000);

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += "	SELECT \"AcAcctCheck\".\"AcDate\", \"AcctMasterBal\", \"TdBal\", \"DbAmt\", \"CrAmt\", \"MasterClsAmt\", \"TxAmt\"";
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
		sql += "	    WHERE (\"AcDate\" BETWEEN :inputStartDate AND :inputEndDate) AND \"AcctCode\" IN ('TMI', 'F09', 'F25')";
		sql += "	    GROUP BY \"AcDate\"";
		sql += "	    ORDER BY \"AcDate\"";
		sql += "	) \"AcAcctCheck\"";
		sql += "	JOIN";
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
		sql += "	    WHERE (\"AcDate\" BETWEEN :inputStartDate AND :inputEndDate) AND \"AcctCode\" IN ('TMI', 'F09', 'F25') AND \"EntAc\" = 1";
		sql += "	    GROUP BY \"AcDate\"";
		sql += "	    ORDER BY \"AcDate\"";
		sql += "	) \"AcDetail\"";
		sql += "	ON \"AcAcctCheck\".\"AcDate\" = \"AcDetail\".\"AcDate\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", startDate);
		query.setParameter("inputEndDate", endDate);

		return this.convertToMap(query);
	}

}