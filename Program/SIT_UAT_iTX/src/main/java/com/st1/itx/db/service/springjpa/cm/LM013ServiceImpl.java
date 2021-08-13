package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LM013ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM013ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lM013.findAll ");

		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);
		logger.info("entdy = " + entdy);
		String sql = "SELECT * ";
		sql += "        FROM ( SELECT DECODE(C.\"EntCode\",'1','1','0') AS F0";
		sql += "                    , DECODE(R.\"RelsCode\", NULL, 0, 1) AS F1";
		sql += "                    , D.\"CustNo\" AS F2";
		sql += "                    , LPAD(D.\"FacmNo\", 3, '0') AS F3";
		sql += "                    , C.\"CustId\" AS F4";
		sql += "                    , C.\"CustName\" AS F5";
		sql += "                    , CF.\"ClCode1\" AS F6";
		sql += "                    , F.\"LineAmt\" AS F7";
		sql += "                    , SUM(D.\"LoanBalance\") AS F8";
		sql += "               FROM \"DailyLoanBal\" D";
		sql += "               LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
		sql += "               LEFT JOIN \"RelsMain\" R ON R.\"RelsId\" = C.\"CustId\"";
		sql += "                                       AND R.\"RelsCode\" <> '99'";
		sql += "               LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\"";
		sql += "                                      AND F.\"FacmNo\" = D.\"FacmNo\" ";
		sql += "               LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = D.\"CustNo\"";
		sql += "                                     AND CF.\"FacmNo\" = D.\"FacmNo\"";
		sql += "                                     AND CF.\"MainFlag\" = 'Y'";
		sql += "               WHERE D.\"MonthEndYm\" = :entdy";
		sql += "                 AND D.\"ProdNo\" NOT LIKE '8%'";
		sql += "                 AND D.\"ProdNo\" NOT LIKE 'I%'";
		sql += "                 AND D.\"LoanBalance\" > 0";
		sql += "               GROUP BY DECODE(C.\"EntCode\",'1','1','0')";
		sql += "                      , DECODE(R.\"RelsCode\", NULL, 0, 1)";
		sql += "                      , D.\"CustNo\"";
		sql += "                      , LPAD(D.\"FacmNo\", 3, '0')";
		sql += "                      , C.\"CustId\"";
		sql += "                      , C.\"CustName\"";
		sql += "                      , CF.\"ClCode1\"";
		sql += "                      , F.\"LineAmt\"";
		sql += "               UNION ALL";
		sql += "               SELECT DECODE(C.\"EntCode\",'1','1','0') AS F0";
		sql += "                    , DECODE(R.\"RelsCode\", NULL, 0, 1) AS F1";
		sql += "                    , D.\"CustNo\" AS F2";
		sql += "                    , LPAD(D.\"FacmNo\", 3, '0') AS F3";
		sql += "                    , C.\"CustId\" AS F4";
		sql += "                    , C.\"CustName\" AS F5";
		sql += "                    , 0 AS F6";
		sql += "                    , F.\"LineAmt\" AS F7";
		sql += "                    , SUM(D.\"LoanBalance\") AS F8";
		sql += "               FROM \"DailyLoanBal\" D";
		sql += "               LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\" ";
		sql += "               LEFT JOIN \"RelsMain\" R ON R.\"RelsId\" = C.\"CustId\"";
		sql += "                                       AND R.\"RelsCode\" <> '99'";
		sql += "               LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\" ";
		sql += "                                      AND F.\"FacmNo\" = D.\"FacmNo\" ";
		sql += "               WHERE  D.\"MonthEndYm\" = :entdy";
		sql += "                 AND (D.\"ProdNo\" LIKE '8%' OR D.\"ProdNo\" LIKE 'I%')";
		sql += "                 AND D.\"LoanBalance\" > 0";
		sql += "               GROUP BY DECODE(C.\"EntCode\",'1','1','0')";
		sql += "                      , DECODE(R.\"RelsCode\", NULL, 0, 1)";
		sql += "                      , D.\"CustNo\"";
		sql += "                      , LPAD(D.\"FacmNo\", 3, '0')";
		sql += "                      , C.\"CustId\"";
		sql += "                      , C.\"CustName\"";
		sql += "                      , F.\"LineAmt\")";
		sql += "        ORDER BY F0, F1, F2, F3";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", Integer.toString(Integer.parseInt(titaVo.getParam("inputDate"))+19110000).substring(0,6));
		return this.convertToMap(query.getResultList());
	}

}