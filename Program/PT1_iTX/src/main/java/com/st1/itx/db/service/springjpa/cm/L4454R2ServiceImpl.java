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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("L4454R2ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454R2ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("銀扣失敗五萬元以上報表 findAll...");

		int entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;

		this.info("entryDate= " + entryDate);

		String sql = " ";
		sql += " WITH \"mainData\" AS (";
		sql += "	SELECT \"RepayAcctNo\"";
		sql += "		  ,\"CustNo\"";
		sql += "		  ,MIN(\"FacmNo\") AS \"FacmNo\"";
		sql += "		  ,SUM(\"RepayAmt\") AS \"RepayAmt\"";
		sql += "		  ,\"RelCustName\"";
		sql += "	FROM \"BankDeductDtl\"";
		sql += "	WHERE \"EntryDate\" = :entryDate                                       ";
		sql += "      AND \"RepayType\" = 1                                                ";
		sql += "      AND \"MediaCode\" = 'Y'                                              ";
		sql += "     AND NVL(\"ReturnCode\",'  ') not in ('  ','00')                      ";
		sql += "	GROUP BY \"RepayAcctNo\"";
		sql += "			,\"CustNo\"";
		sql += "			,\"RelCustName\"";
		sql += " ), \"tmpFirstDrawdownDate\" AS (";
		sql += "	SELECT L.\"CustNo\"";
//		sql += "		  ,MIN(FM.\"FirstDrawdownDate\") AS \"FirstDrawdownDate\"";
		sql += "		  ,MIN(L.\"DrawdownDate\") AS \"FirstDrawdownDate\"";
		sql += "	FROM \"LoanBorMain\" L";
		sql += "    LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = L.\"CustNo\"";
		sql += "                            AND FM.\"FacmNo\"= L.\"FacmNo\"";
		sql += "	WHERE L.\"Status\" = 0";
		sql += "	GROUP BY L.\"CustNo\"";
		sql += " ), \"tmpPrevPayIntDate\" AS (";
		sql += "	SELECT L.\"CustNo\"";
		sql += "		  ,MIN(L.\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
		sql += "	FROM \"LoanBorMain\" L";
		sql += "	WHERE L.\"Status\" = 0";
		sql += "	GROUP BY L.\"CustNo\"";
		sql += " )	";
		sql += "	SELECT M.\"RepayAcctNo\"";
		sql += "		  ,LPAD(M.\"CustNo\",7,0) AS \"CustNo\"";
		sql += "		  ,CM.\"CustName\"";
		sql += "		  ,M.\"RepayAmt\"";
		sql += "   		  ,NVL(\"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1),\"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1)) AS \"PhoneNo\"";
		sql += "   		  ,NVL(M.\"RelCustName\", CM.\"CustName\")  AS \"RelCustName\"";         
		sql += "		  ,PP.\"PrevPayIntDate\" - 19110000 \"PrevPayIntDate\"";
		sql += "		  ,FD.\"FirstDrawdownDate\" - 19110000 AS \"FirstDrawdownDate\"";
		sql += "		  ,CC.\"CityItem\"";
		sql += "		  ,CC.\"CityCode\"";
		sql += "		  ,CE.\"Fullname\"";
		sql += "	FROM \"mainData\" M ";
		sql += "    LEFT JOIN \"tmpFirstDrawdownDate\" FD ON FD.\"CustNo\" = M.\"CustNo\" ";
		sql += "    LEFT JOIN \"tmpPrevPayIntDate\" PP ON PP.\"CustNo\" = M.\"CustNo\" ";
		sql += "    LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND FM.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "    LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                          AND CF.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += "                          AND CF.\"MainFlag\" = 'Y' ";
		sql += "    LEFT JOIN \"ClBuilding\" CB  ON CB.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                                AND CB.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                                AND CB.\"ClNo\" = CF.\"ClNo\" ";
		sql += "    LEFT JOIN \"CdCity\" CC ON CC.\"CityCode\" = CB.\"CityCode\" ";
		sql += "    LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\"";
		sql += "    LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = FM.\"FireOfficer\" ";
		sql += "	WHERE M.\"RepayAmt\" > 50000 ";
		sql += "	ORDER BY CC.\"CityCode\" ASC";
		sql += "		  	,M.\"RepayAcctNo\" ASC";
		sql += "			,M.\"CustNo\" ASC";
	

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entryDate", entryDate);
		return this.convertToMap(query);
	}
}