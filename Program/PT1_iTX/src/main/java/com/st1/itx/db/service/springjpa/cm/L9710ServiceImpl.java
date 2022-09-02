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

@Service("l9710ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9710ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}


	public List<Map<String, String>> findAll(TitaVo titaVo,int iAcDate) throws Exception {

		this.info("L9710.findAll");

		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ST")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ED")) + 19110000);
		String iCUSTNO = titaVo.get("CustNo");
	 
		
		
		
		this.info("iSDAY:" + iSDAY + ",iEDAY:" + iEDAY + ",iCUSTNO:" + iCUSTNO);
		this.info("iAcDate:" + iAcDate );

		String sql = " SELECT * ";
		sql += "       FROM ( SELECT   ' ' F0";
		sql += "                     , Cl.\"CityCode\" AS \"CityCode\"";
		sql += "                     , Ci.\"CityItem\" AS \"CityItem\"";
		sql += "                     , E.\"Fullname\"  AS \"Fullname\"";
		sql += "                     , M.\"CustNo\"    AS \"CustNo\"";
		sql += "                     , M.\"FacmNo\"    AS \"FacmNo\"";
		sql += "                     , \"Fn_ParseEOL\"(C.\"CustName\",0)   AS \"CustName\"";
		sql += "                     , F.\"ApplNo\"    AS \"ApplNo\"";
		sql += "                     , M.\"GraceDate\" AS \"GraceDate\"";
		sql += "                     , F.\"LineAmt\"   AS \"LineAmt\"";
		sql += "                     , M.\"LoanBal\"   AS \"LoanBal\"";
		sql += "                     , F.\"FirstDrawdownDate\"  AS \"FirstDrawdownDate\"";
		sql += "                     , M.\"PrevPayIntDate\"     AS \"PrevPayIntDate\"";
		sql += "                     , M.\"StoreRate\"          AS \"StoreRate\"";
		sql += "					 ,DECODE(T.\"TelArea\",NULL,T.\"TelNo\",T.\"TelArea\" || '-' || T.\"TelNo\") AS \"TelNo\"";
		sql += "                     , T.\"LiaisonName\"   AS \"LiaisonName\"";
		sql += "                     , M.\"NextRepayDate\" AS \"NextRepayDate\"";
		sql += "        			 ,C.\"CurrZip3\"       AS \"CurrZip3\"";
		sql += "        			 ,C.\"CurrZip2\"       AS \"CurrZip2\"";
		sql += "        			 ,CB.\"BdLocation\" AS \"BdLocation\"";
		sql += "        			 ,CC.\"Item\" AS \"Item\"";
		sql += "        			 ,C.\"EntCode\" AS \"EntCode\"";
		sql += "                     , ROW_NUMBER() OVER ( PARTITION BY M.\"CustNo\", M.\"FacmNo\"";
		sql += "                                               ORDER BY T.\"TelTypeCode\") AS SEQ";
		sql += "                     , F.\"RepayCode\"   AS \"RepayCode\"";
		sql += "              FROM ( SELECT  M.\"CustNo\"";
		sql += "                           , M.\"FacmNo\"";
		sql += "                           , M.\"BormNo\"";
		sql += "                           , M.\"GraceDate\"";
		sql += "                           , M.\"LoanBal\"";
		sql += "                           , M.\"PrevPayIntDate\"";
		sql += "                           , M.\"NextRepayDate\"";
		sql += "                           , RES.\"FitRate\" AS \"StoreRate\"";
		sql += "                     FROM \"LoanBorMain\" M ";
		sql += "                     LEFT JOIN (";
		sql += "						  SELECT \"CustNo\"";
		sql += "								,\"FacmNo\"";
		sql += "								,SUM(\"LoanBal\") \"LoanBal\"";
		sql += " 						  FROM \"LoanBorMain\" M";
		sql += "                     	  WHERE \"GraceDate\"  >= :isday";
		sql += "                       		AND  \"GraceDate\" <= :ieday";
		sql += "                       		AND  \"LoanBal\"  > 0";
		if (!iCUSTNO.equals("0000000")) {
			sql += "                       		AND  \"CustNo\"     =  :icustno";
		}
		sql += "             			  GROUP BY \"CustNo\"";
		sql += "             					  ,\"FacmNo\"";
		sql += "                     ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                     	 AND M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	  		  		 LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   		   		  ,\"FacmNo\"";
		sql += "					           		  ,\"BormNo\"";
		sql += "					   		   	  	  ,\"EffectDate\"";
		sql += "					   		   		  ,\"FitRate\"";
		sql += "					   		   		  ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\"";
		sql += "													   	       		  ,\"FacmNo\"";
		sql += "													           		  ,\"BormNo\"";
		sql += "										       				 ORDER BY \"EffectDate\" DESC) AS \"SEQ\"";
		sql += "				 				FROM \"LoanRateChange\"";
		sql += "				 				WHERE \"EffectDate\" <= :iDate ";
		sql += "				 			   )RES ON RES.\"CustNo\" = M.\"CustNo\"";
		sql += "				    			   AND RES.\"FacmNo\" = M.\"FacmNo\"";
		sql += "								   AND RES.\"BormNo\" = M.\"BormNo\"";
		sql += "								   AND RES.\"SEQ\" = 1";
		sql += "                     WHERE M.\"GraceDate\"  >= :isday";
		sql += "                       AND  M.\"GraceDate\"  <= :ieday";
		sql += "                       AND  M.\"LoanBal\"  > 0";
		if (!iCUSTNO.equals("0000000")) {
			sql += "                       AND  M.\"CustNo\"     =  :icustno";
		}
		sql += "                                                           ) M";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "              LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "              LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "                                    AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                    AND CF.\"MainFlag\" = 'Y'";
		sql += "              LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" =  CF.\"ClCode1\"";
		sql += "                                     AND Cl.\"ClCode2\" =  CF.\"ClCode2\"";
		sql += "                                     AND Cl.\"ClNo\"    =  CF.\"ClNo\"";
		sql += "              LEFT JOIN \"CdCity\" Ci ON Ci.\"CityCode\" =  Cl.\"CityCode\"";
		sql += "              LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" =  CF.\"ClCode1\"";
		sql += "                                         AND CB.\"ClCode2\" =  CF.\"ClCode2\"";
		sql += "                                         AND CB.\"ClNo\"    =  CF.\"ClNo\"";
		sql += "              LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" =  F.\"BusinessOfficer\"";
		sql += "			  LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" = 'RepayCode'";
		sql += "						   		     AND CC.\"Code\" = F.\"RepayCode\"";
		sql += "              LEFT JOIN \"CustTelNo\" T ON T.\"CustUKey\" =  C.\"CustUKey\"";
		sql += "                                       AND T.\"Enable\"   = 'Y' ) D";
		sql += "       WHERE D.\"SEQ\" = 1 ";
		sql += " 	   ORDER BY \"CityCode\" ASC";
		sql += "			    ,\"Fullname\" ASC";
		sql += "			    ,\"GraceDate\" ASC";
		sql += "				,\"CustNo\" ASC";
		sql += "				,\"FacmNo\" ASC";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		query.setParameter("iDate", iAcDate);
		
		if (!iCUSTNO.equals("0000000")) {
			query.setParameter("icustno", iCUSTNO);
		}
		return this.convertToMap(query);
	}

}