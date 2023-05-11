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
		
		String sql = "";
		sql += " WITH CF AS ( ";
		sql += "   SELECT CF.\"CustNo\" AS \"CustNo\" ";
		sql += "        , CF.\"FacmNo\" AS \"FacmNo\" ";
		sql += "        , ROW_NUMBER() ";
		sql += "          OVER ( ";
		sql += "            PARTITION BY CF.\"CustNo\" ";
		sql += "                       , CF.\"FacmNo\" ";
		sql += "                       , CF.\"ClCode1\" ";
		sql += "                       , CF.\"ClCode2\" ";
		sql += "                       , CF.\"ClNo\" ";
		sql += "            ORDER BY NVL(CE_early.\"EvaDate\",0) DESC "; // 第1段. 最接近該額度核准日期，且擔保品鑑價日小於等於核准日期的那筆資料
		sql += "                   , NVL(CE_later.\"EvaDate\",0) "; // 第2段. 若第1段抓不到資料，才是改為抓鑑價日期最接近核准日期的那筆評估淨值
		sql += "          )                               AS \"Seq\" ";
		sql += "        , NVL(CE_early.\"EvaNetWorth\",NVL(CE_later.\"EvaNetWorth\",0)) ";
		sql += "                                          AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\" CF ";
		sql += "   LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                            AND FAC.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += "   LEFT JOIN \"FacCaseAppl\" CAS ON CAS.\"ApplNo\" = CF.\"ApproveNo\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_early ON CE_early.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_early.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_early.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_early.\"EvaDate\" <= CAS.\"ApproveDate\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_later ON CE_later.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_later.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_later.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_later.\"EvaDate\" > CAS.\"ApproveDate\" ";
		sql += "                               AND NVL(CE_early.\"EvaDate\",0) = 0 "; // 若第1段串不到,才串第2段
		sql += " ) ";
		sql += " , \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"EvaNetWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM CF ";
		sql += "   WHERE \"Seq\" = 1 "; // 每個擔保品只取一筆
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " )";
//		2021/04/19 Ted 	
		sql += "      SELECT CM.\"ClCode1\" AS F0";
		sql += "			,CM.\"ClCode2\" AS F1";
		sql += "			,CM.\"ClNo\" AS F2";
		sql += "			,CC.\"CityCode\" AS F3";
		sql += "			,FM.\"LineAmt\" AS F4";
		sql += "			,MLB.\"CustNo\" AS F5";
		sql += "			,MLB.\"FacmNo\" AS F6";
		sql += "			,FM.\"FirstDrawdownDate\" AS F7";
		sql += "      	    , NVL(CS.\"EvaNetWorth\", 0) AS F8";
		sql += "      		, CASE ";
		sql += "          		WHEN NVL(CS.\"EvaNetWorth\", 0) = 0";
		sql += "          		THEN 0 ";
		sql += "        		ELSE ROUND(FM.\"LineAmt\" / CS.\"EvaNetWorth\", 8)";
		sql += "        		END                        AS F9 "; // 貸款成數
		sql += "	  FROM \"MonthlyLoanBal\" MLB";
		sql += "	  LEFT JOIN \"ClFac\" CF ON CF.\"FacmNo\"=MLB.\"FacmNo\"";
		sql += "	  						AND CF.\"CustNo\"=MLB.\"CustNo\"";
		sql += "							AND CF.\"MainFlag\"='Y'";
		sql += "	  LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\"=CF.\"ClCode1\"";
		sql += "							 AND CM.\"ClCode2\"=CF.\"ClCode2\"";
		sql += "							 AND CM.\"ClNo\"=CF.\"ClNo\"";
		sql += "	  LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\"=MLB.\"CustNo\"";
		sql += "							  AND FM.\"FacmNo\"=MLB.\"FacmNo\"";
		sql += "      LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                            AND CS.\"FacmNo\" = FM.\"FacmNo\" ";
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
		return this.convertToMap(query);
	}

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
		return this.convertToMap(query);
	}

}
