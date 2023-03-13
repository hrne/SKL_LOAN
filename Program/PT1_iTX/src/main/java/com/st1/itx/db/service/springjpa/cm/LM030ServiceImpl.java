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
/* 逾期放款明細 */
public class LM030ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM030.findAll ");

		this.info("YearMonth = " + titaVo.getParam("YearMonth"));

		String sql = " ";
		sql += "	SELECT CD.\"CityItem\"";
		sql += "          ,\"Fn_GetEmpName\"(MF.\"AccCollPsn\", 1) \"AccCollPsn\" ";
		sql += "       	  ,MF.\"CustNo\" ";
		sql += "          ,MF.\"FacmNo\" ";
		sql += "          ,\"Fn_ParseEOL\"(CM.\"CustName\",0) AS \"CustName\" ";
		sql += "       	  ,F.\"FirstDrawdownDate\" - 19110000 AS \"DrawdownDate\" ";
		sql += " 		  ,MF.\"PrinBalance\" AS \"LoanBal\"";
		sql += " 		  ,0 AS \"Interest\"";
		sql += "       	  ,ML.\"StoreRate\" AS \"StoreRate\" ";
		sql += "       	  ,MF.\"PrevIntDate\" - 19110000 AS \"PrevPayIntDate\" ";
		sql += "          ,TO_NUMBER(TO_CHAR(";
		sql += " 					ADD_MONTHS(TO_DATE(TO_CHAR(MF.\"DueDate\"),'YYYYMMDD'),1)";
		sql += "					,'YYYYMMDD')) AS  \"DueDate\" ";
		sql += "	FROM \"MonthlyFacBal\" MF";
		sql += "    LEFT JOIN \"CdCity\" CD ON CD.\"CityCode\" = MF.\"CityCode\" ";
		sql += "    LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MF.\"CustNo\" ";
		sql += "    LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = MF.\"CustNo\" ";
		sql += "                           AND F.\"FacmNo\" = MF.\"FacmNo\" ";
		sql += "    LEFT JOIN (SELECT \"CustNo\"";
		sql += "					  ,\"FacmNo\"";
		sql += " 					 ,MAX(\"StoreRate\") AS \"StoreRate\"";
		sql += "			   FROM \"MonthlyLoanBal\"";
		sql += "               WHERE \"YearMonth\" = :yymm ";
		sql += "                 AND \"LoanBalance\" > 0 ";
		sql += "               GROUP BY \"CustNo\"";
		sql += "               		   ,\"FacmNo\"";
		sql += "              ) ML ON ML.\"CustNo\" = MF.\"CustNo\" AND ML.\"FacmNo\" = MF.\"FacmNo\"";
		sql += "    WHERE MF.\"YearMonth\" = :yymm ";
		// --找餘5期 跟 餘4期是1號
		sql += "      AND (MF.\"OvduTerm\" = 5 ";
		sql += "      	OR (MF.\"OvduTerm\" = 4 AND SUBSTR(MF.\"PrevIntDate\",7,2) = '01' ))";
		sql += "    ORDER BY MF.\"PrevIntDate\" ASC ";
		sql += "    	    ,F.\"FirstDrawdownDate\" ASC ";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("yymm", parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100);

		return this.convertToMap(query);
	}

}