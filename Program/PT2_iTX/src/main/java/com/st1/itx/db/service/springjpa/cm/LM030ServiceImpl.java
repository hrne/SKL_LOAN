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
		
		// --找餘5期 跟 餘4期是1號
		sql += "    WITH \"tmpMain\" AS (";
		sql += "        SELECT *";
		sql += "        FROM \"MonthlyFacBal\"";
		sql += "        WHERE \"YearMonth\" = :Yymm";
		sql += "            AND (";
		sql += "                \"OvduTerm\" = 5";
		sql += "                OR (";
		sql += "                    \"OvduTerm\" = 4";
		sql += "                    AND Substr(";
		sql += "                        \"PrevIntDate\",";
		sql += "                        7,";
		sql += "                        2";
		sql += "                    ) = '01'";
		sql += "                )";
		sql += "            )";
		sql += "    ),";
		// --同擔保品額度需列出
		sql += "    \"Main\" AS (";
		sql += "        SELECT M.*";
		sql += "        FROM \"MonthlyFacBal\" M";
		sql += "            LEFT JOIN \"tmpMain\" Mm ON Mm.\"ClCode1\" = M.\"ClCode1\"";
		sql += "            AND Mm.\"ClCode2\" = M.\"ClCode2\"";
		sql += "            AND Mm.\"ClNo\" = M.\"ClNo\"";
		sql += "        WHERE M.\"YearMonth\" = :Yymm";
		sql += "            AND M.\"PrinBalance\" > 0";
		sql += "            AND Mm.\"CustNo\" IS NOT NULL";
		sql += "    )";
		sql += "    SELECT Cd.\"CityItem\",";
		sql += "        \"Fn_GetEmpName\"(M.\"AccCollPsn\", 1) \"AccCollPsn\",";
		sql += "        M.\"CustNo\",";
		sql += "        M.\"FacmNo\",";
		sql += "        \"Fn_ParseEOL\"(Cm.\"CustName\", 0) AS \"CustName\",";
		sql += "        F.\"FirstDrawdownDate\" - 19110000 AS \"DrawdownDate\",";
		sql += "        M.\"PrinBalance\" AS \"LoanBal\",";
		sql += "        0 AS \"Interest\",";
		sql += "        Ml.\"StoreRate\" AS \"StoreRate\",";
		sql += "        M.\"PrevIntDate\" - 19110000 AS \"PrevPayIntDate\",";
		sql += "        To_Number(";
		sql += "            To_Char(";
		sql += "                Add_Months(";
		sql += "                    To_Date(";
		sql += "                        To_Char(";
		sql += "                            Decode(";
		sql += "                                M.\"DueDate\",";
		sql += "                                0,";
		sql += "                                19110101,";
		sql += "                                M.\"DueDate\"";
		sql += "                            )";
		sql += "                        ),";
		sql += "                        'YYYYMMDD'";
		sql += "                    ),";
		sql += "                    1";
		sql += "                ),";
		sql += "                'YYYYMMDD'";
		sql += "            )";
		sql += "        ) AS \"DueDate\",";
		sql += "        M.\"UnpaidPrincipal\",";
		sql += "        M.\"UnpaidInterest\",";
		sql += "        M.\"UnpaidBreachAmt\",";
		sql += "        M.\"UnpaidDelayInt\"";
		sql += "    FROM \"Main\" M";
		sql += "        LEFT JOIN \"CdCity\" Cd ON Cd.\"CityCode\" = M.\"CityCode\"";
		sql += "        LEFT JOIN \"CustMain\" Cm ON Cm.\"CustNo\" = M.\"CustNo\"";
		sql += "        LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "        AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        LEFT JOIN (";
		sql += "            SELECT \"CustNo\",";
		sql += "                \"FacmNo\",";
		sql += "                MAX(\"StoreRate\") AS \"StoreRate\"";
		sql += "            FROM \"MonthlyLoanBal\"";
		sql += "            WHERE \"YearMonth\" = :Yymm";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "            GROUP BY \"CustNo\",";
		sql += "                \"FacmNo\"";
		sql += "        ) Ml ON Ml.\"CustNo\" = M.\"CustNo\"";
		sql += "        AND Ml.\"FacmNo\" = M.\"FacmNo\"";
		sql += "    ORDER BY M.\"CustNo\" ASC,";
		sql += "        M.\"PrevIntDate\" ASC,";
		sql += "        F.\"FirstDrawdownDate\" ASC";




		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("Yymm", parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100);

		return this.convertToMap(query);
	}

}