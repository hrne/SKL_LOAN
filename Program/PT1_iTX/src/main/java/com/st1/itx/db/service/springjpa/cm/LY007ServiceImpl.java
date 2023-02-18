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

@Service("LY007ServiceImpl")
@Repository
public class LY007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth, TitaVo titaVo) throws Exception {
		this.info("LY007ServiceImpl queryDetail ");
		
		this.info("inputYearMonth=" + inputYearMonth);
		
		String sql = "";
		sql += "	SELECT s1.\"Rel\" AS \"Rel\"";
		sql += "	       ,s0.\"CustNo\" AS \"CustNo\"";
		sql += "	       ,cm.\"CustId\" AS \"CustId\"";
		sql += "	       ,cm.\"CustName\" AS \"CustName\"";
		sql += "	,CASE WHEN s4.\"LandLocation\" IS NOT NULL";
		sql += "	      AND s3.\"BdLocation\" IS NOT NULL";
		sql += "	     THEN s3.\"BdLocation\" ";
		sql += "	     ELSE substr(s4.\"LandLocation\",1,length(s4.\"LandLocation\")-12)";
		sql += "    END AS \"BdLocation\"     ";
		sql += "	       ,l.\"DrawdownDate\" AS \"DrawdownDate\"";
		sql += "	       ,m2.\"LoanBal\" AS \"LoanBal\"";
		sql += "	,CASE WHEN fm.\"Supervisor\" = '999999' OR fm.\"Supervisor\" IS NULL THEN 'B'";			
		sql += "	     ELSE to_char(ce.\"Fullname\")  ";
		sql += "    END AS \"Supervisor\"     ";		
		sql += "	FROM \"MonthlyLoanBal\"  s0";
		sql += "          LEFT JOIN (";
		sql += "              SELECT \"CustNo\"";
		sql += "                    ,MIN(\"FacmNo\") AS \"FacmNo\" ";
		sql += "                    ,MIN(\"BormNo\") AS \"BormNo\" ";
		sql += "                    ,SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += "                    ,MIN(\"DrawdownDate\") AS \"DrawdownDate\" ";
		sql += "              FROM \"LoanBorMain\" ";
		sql += "              WHERE \"LoanBal\" > 0 ";
		sql += "              GROUP BY \"CustNo\") l ON l.\"CustNo\" = s0.\"CustNo\" ";
		sql += "                                    AND l.\"FacmNo\" = s0.\"FacmNo\" ";
		sql += "                                    AND l.\"BormNo\" = s0.\"BormNo\" ";
		sql += "          LEFT JOIN (";
		sql += "              SELECT \"CustNo\"";			
		sql += "                    ,SUM(\"LoanBalance\") AS \"LoanBal\" ";		
		sql += "              FROM \"MonthlyLoanBal\" ";
		sql += "              WHERE \"LoanBalance\" > 0 ";
		sql += "                AND \"YearMonth\" = :inputYearMonth ";
		sql += "              GROUP BY \"CustNo\") m2 ON m2.\"CustNo\" = l.\"CustNo\" ";
		sql += "          LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = s0.\"CustNo\" ";
		sql += "          LEFT JOIN \"FacMain\"  fm ON fm.\"CustNo\" = s0.\"CustNo\" ";
		sql += "                                    AND fm.\"FacmNo\" = s0.\"FacmNo\" ";
		sql += "          LEFT JOIN (";
		sql += "              SELECT decode(\"BusId\",'-',decode(\"RelId\",'-',\"HeadName\",\"RelName\"),\"BusName\") AS \"CustName\" ";
		sql += "                    ,to_char(decode(\"BusId\",'-',decode(\"RelId\",'-',\"HeadId\",\"RelId\"),\"BusId\")) AS \"RptId\" ";
		sql += "                    ,decode(\"BusId\",'-',decode(\"RelId\",'-',decode(\"RelWithCompany\",'A','A;G'),\"RelWithCompany\"),'Z','Z') AS \"Rel\" ";
		sql += "                    ,\"HeadName\" ";
		sql += "                    ,\"HeadTitle\" ";
		sql += "                    ,\"RelName\" ";
		sql += "                    ,\"RelTitle\" ";
		sql += "                    ,\"BusTitle\" ";
		sql += "              FROM \"LifeRelHead\" ";
		sql += "              WHERE \"LoanBalance\" > 0 ";
		sql += "              UNION ";
		sql += "              SELECT \"EmpName\" AS \"CustName\" ";
		sql += "                    ,to_char(\"EmpId\") AS \"RptId\" ";
		sql += "                    ,'N' AS \"Rel\" ";
		sql += "                    ,NULL AS \"HeadName\" ";
		sql += "                    ,NULL AS \"HeadTitle\" ";
		sql += "                    ,NULL AS \"RelName\" ";
		sql += "                    ,NULL AS \"RelTitle\" ";
		sql += "                    ,NULL AS \"BusTitle\" ";
		sql += "              FROM \"LifeRelEmp\" ";
		sql += "              ) s1 ON s1.\"RptId\" = cm.\"CustId\" ";
		sql += "          LEFT JOIN \"ClBuilding\" s3 ON s3.\"ClCode1\" = s0.\"ClCode1\" ";
		sql += "                                     AND s3.\"ClCode2\" = s0.\"ClCode2\" ";
		sql += "                                     AND s3.\"ClNo\" = s0.\"ClNo\" ";
		sql += "          LEFT JOIN \"ClLand\" s4 ON s4.\"ClCode1\" = s0.\"ClCode1\" ";
		sql += "                                     AND s4.\"ClCode2\" = s0.\"ClCode2\" ";
		sql += "                                     AND s4.\"ClNo\" = s0.\"ClNo\" ";
		sql += "          LEFT JOIN \"CdEmp\" ce ON ce.\"EmployeeNo\" = fm.\"Supervisor\" ";
		sql += "    WHERE s0.\"LoanBalance\" > 0 ";
		sql += "      AND s0.\"YearMonth\" = :inputYearMonth ";
		sql += "      AND s1.\"Rel\" IS NOT NULL ";
		sql += "      AND  l.\"CustNo\" IS NOT NULL ";
		sql += "	GROUP BY s1.\"Rel\" ";
		sql += "	       ,s0.\"CustNo\" ";
		sql += "	       ,cm.\"CustId\" ";
		sql += "	       ,cm.\"CustName\" ";
		sql += "	,CASE WHEN s4.\"LandLocation\" IS NOT NULL";
		sql += "	      AND s3.\"BdLocation\" IS NOT NULL";
		sql += "	     THEN s3.\"BdLocation\" ";
		sql += "	     ELSE substr(s4.\"LandLocation\",1,length(s4.\"LandLocation\")-12)";
		sql += "    END ";
		sql += "	       ,l.\"DrawdownDate\" ";
		sql += "	       ,m2.\"LoanBal\" ";
		sql += "	,CASE WHEN fm.\"Supervisor\" = '999999'OR fm.\"Supervisor\" IS NULL THEN 'B'";			
		sql += "	     ELSE to_char(ce.\"Fullname\")  ";
		sql += "    END ";
		sql += "    ORDER BY cm.\"CustId\" ASC";
		
		

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}