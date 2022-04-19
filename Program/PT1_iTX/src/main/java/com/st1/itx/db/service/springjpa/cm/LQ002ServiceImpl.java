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
/* 逾期放款明細 */
public class LQ002ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo, int yy, int mm) throws Exception {
		this.info("lQ002.findAll ");
//		this.info("impl002." + Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
//		1090420+19110000=20200420
		String iENTDY = String.valueOf((Integer.valueOf(titaVo.get("ENTDY")) + 19110000));
		this.info("" + iENTDY);
		int sMM = 0;
		int eMM = 0;

		if (mm <= 3) {
			sMM = yy + 1911 + 1;
			eMM = yy * 100 + 3;
		} else if (mm <= 6) {
			sMM = yy * 100 + 4;
			eMM = yy * 100 + 6;
		} else if (mm <= 9) {
			sMM = yy * 100 + 7;
			eMM = yy * 100 + 9;
		} else if (mm <= 12) {
			sMM = yy * 100 + 10;
			eMM = yy * 100 + 12;
		}
		this.info("sMM" + sMM + "~" + "eMM" + eMM);
//		2021/04/19 Ted 	
		String sql = "SELECT CM.\"ClCode1\" AS F0";
		sql += "			,CM.\"ClCode2\" AS F1";
		sql += "			,CM.\"ClNo\" AS F2";
		sql += "			,CC.\"CityCode\" AS F3";
		sql += "			,FM.\"LineAmt\" AS F4";
		sql += "			,MLB.\"CustNo\" AS F5";
		sql += "			,MLB.\"FacmNo\" AS F6";
		sql += "			,FM.\"FirstDrawdownDate\" AS F7";
		sql += "			,CI.\"EvaNetWorth\" AS F8";
		sql += "			,DECODE(CI.\"EvaNetWorth\",0,0,ROUND(FM.\"LineAmt\"/CI.\"EvaNetWorth\",8)) AS F9";
		sql += "	  FROM \"MonthlyLoanBal\" MLB";
		sql += "	  LEFT JOIN \"ClFac\" CF ON CF.\"FacmNo\"=MLB.\"FacmNo\"";
		sql += "	  						AND CF.\"CustNo\"=MLB.\"CustNo\"";
		sql += "							AND CF.\"MainFlag\"='Y'";
		sql += "	  LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\"=CF.\"ClCode1\"";
		sql += "							 AND CM.\"ClCode2\"=CF.\"ClCode2\"";
		sql += "							 AND CM.\"ClNo\"=CF.\"ClNo\"";
		sql += "	  LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\"=MLB.\"CustNo\"";
		sql += "							  AND FM.\"FacmNo\"=MLB.\"FacmNo\"";
		sql += "	  LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\"=CF.\"ClCode1\"";
		sql += "							AND CI.\"ClCode2\"=CF.\"ClCode2\"";
		sql += "							AND CI.\"ClNo\"=CF.\"ClNo\"";
		sql += "	  LEFT JOIN \"CdCity\" CC ON CC.\"CityCode\" = CM.\"CityCode\"";
		sql += "	  WHERE MLB.\"FacAcctCode\" IN('310','320','330','340')";
		sql += "	    AND CM.\"ClCode1\"='1'";
		sql += "		AND CM.\"ClCode2\"='1'";
		sql += "		AND MLB.\"EntCode\" = 0";
		sql += "		AND CI.\"EvaNetWorth\" > 0";
		sql += "	    AND TRUNC(FM.\"FirstDrawdownDate\" / 100) >= :sMM ";
		sql += "	    AND TRUNC(FM.\"FirstDrawdownDate\" / 100) <= :eMM ";
		sql += "	  GROUP BY CM.\"ClCode1\"";
		sql += "			  ,CM.\"ClCode2\"";
		sql += "			  ,CM.\"ClNo\"";
		sql += "			  ,CC.\"CityCode\"";
		sql += "			  ,FM.\"LineAmt\"";
		sql += "			  ,MLB.\"CustNo\"";
		sql += "			  ,MLB.\"FacmNo\"";
		sql += "			  ,FM.\"FirstDrawdownDate\"";
		sql += "			  ,CI.\"EvaNetWorth\"";
		sql += "	  ORDER BY CC.\"CityCode\"";
		sql += "	          ,MLB.\"CustNo\"";
		sql += "	          ,MLB.\"FacmNo\"";
		this.info("sql=" + sql);

		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("sMM", sMM + 191100);
		query.setParameter("eMM", eMM + 191100);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findTotal(TitaVo titaVo, int yy, int mm) throws Exception {
		this.info("lQ002.findTotal ");

		String iENTDY = String.valueOf((Integer.valueOf(titaVo.get("ENTDY")) + 19110000));
		this.info("" + iENTDY);

		int sMM = 0;
		int eMM = 0;

		if (mm <= 3) {
			sMM = yy * 100 + 1;
			eMM = yy * 100 + 3;
		} else if (mm <= 6) {
			sMM = yy * 100 + 4;
			eMM = yy * 100 + 6;
		} else if (mm <= 9) {
			sMM = yy * 100 + 7;
			eMM = yy * 100 + 9;
		} else if (mm <= 12) {
			sMM = yy * 100 + 10;
			eMM = yy * 100 + 12;
		}
		this.info("sMM" + sMM + "~" + "eMM" + eMM);
//		2021/04/19 Ted 	
		String sql = " ";
		sql += "	SELECT CC.\"CityItem\"";
		sql += "		  ,RES.\"F3\" AS　\"CityCode\"";
		sql += "		  ,ROUND(SUM(RES.\"F9\") / COUNT (RES.\"F9\"),8) AS　\"LoanRatio\"";
		sql += "	   FROM(SELECT CM.\"ClCode1\" AS F0";
		sql += "	  			  ,CM.\"ClCode2\" AS F1";
		sql += "				  ,CM.\"ClNo\" AS F2";
		sql += "				  ,CC.\"CityCode\" AS F3";
		sql += "				  ,FM.\"LineAmt\" AS F4";
		sql += "				  ,MLB.\"CustNo\" AS F5";
		sql += "				  ,MLB.\"FacmNo\" AS F6";
		sql += "				  ,FM.\"FirstDrawdownDate\" AS F7";
		sql += "				  ,CI.\"EvaNetWorth\" AS F8";
		sql += "				  ,DECODE(CI.\"EvaNetWorth\",0,0,ROUND(FM.\"LineAmt\"/CI.\"EvaNetWorth\",8)) AS F9";
		sql += "	  	   FROM \"MonthlyLoanBal\" MLB";
		sql += "	  	   LEFT JOIN \"ClFac\" CF ON CF.\"FacmNo\"=MLB.\"FacmNo\"";
		sql += "	  							 AND CF.\"CustNo\"=MLB.\"CustNo\"";
		sql += "								 AND CF.\"MainFlag\"='Y'";
		sql += "	  	   LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\"=CF.\"ClCode1\"";
		sql += "							 	 AND CM.\"ClCode2\"=CF.\"ClCode2\"";
		sql += "							 	 AND CM.\"ClNo\"=CF.\"ClNo\"";
		sql += "	  	   LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\"=MLB.\"CustNo\"";
		sql += "							  	AND FM.\"FacmNo\"=MLB.\"FacmNo\"";
		sql += "	  	   LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\"=CF.\"ClCode1\"";
		sql += "								 AND CI.\"ClCode2\"=CF.\"ClCode2\"";
		sql += "								 AND CI.\"ClNo\"=CF.\"ClNo\"";
		sql += "	  	   LEFT JOIN \"CdCity\" CC ON CC.\"CityCode\" = CM.\"CityCode\"";
		sql += "	 	   WHERE MLB.\"FacAcctCode\" IN('310','320','330','340')";
		sql += "	    	 AND CM.\"ClCode1\"='1'";
		sql += "			 AND CM.\"ClCode2\"='1'";
		sql += "			 AND CI.\"EvaNetWorth\" > 0";
		sql += "			 AND MLB.\"EntCode\" = 0";
		sql += "	    	 AND TRUNC(FM.\"FirstDrawdownDate\" / 100) >= :sMM ";
		sql += "	    	 AND TRUNC(FM.\"FirstDrawdownDate\" / 100) <= :eMM ";
		sql += "	  	   GROUP BY CM.\"ClCode1\"";
		sql += "			  	   ,CM.\"ClCode2\"";
		sql += "			  	   ,CM.\"ClNo\"";
		sql += "			  	   ,CC.\"CityCode\"";
		sql += "			  	   ,FM.\"LineAmt\"";
		sql += "			  	   ,MLB.\"CustNo\"";
		sql += "			  	   ,MLB.\"FacmNo\"";
		sql += "			  	   ,FM.\"FirstDrawdownDate\"";
		sql += "			 	   ,CI.\"EvaNetWorth\" ) RES";
		sql += "	  LEFT JOIN \"CdCity\" CC ON RES.\"F3\"=CC.\"CityCode\" ";
		sql += "      GROUP BY CC.\"CityItem\"";
		sql += "              ,RES.\"F3\"";
		sql += "      ORDER BY RES.\"F3\" ASC";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("sMM", sMM + 191100);
		query.setParameter("eMM", eMM + 191100);
		return this.convertToMap(query.getResultList());
	}

}
