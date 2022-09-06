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

/**
 * L9738 評估淨值明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 *
 */
@Service("L9738ServiceImpl")
@Repository
public class L9738ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getEvaAmtData(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getEvaAmtData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

		String sql = "  ";

		sql += "WITH rawData AS ( ";
		sql += "  SELECT DISTINCT ";
		sql += "         MLB.\"CustNo\" ";
		sql += "       , MLB.\"FacmNo\" ";
		sql += "  FROM \"MonthlyLoanBal\" MLB ";
		sql += "  LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                             AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                             AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += "  WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "    AND MLB.\"LoanBalance\" > 0 ";
		sql += "    AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += ") ";
		sql += "SELECT RD.\"CustNo\" ";
		sql += "     , RD.\"FacmNo\" ";
		sql += "     , FM.\"LineAmt\" ";
		sql += "     , FM.\"UsageCode\" ";
		sql += "     , CF.\"ClCode1\" ";
		sql += "     , CF.\"ClCode2\" ";
		sql += "     , CF.\"ClNo\" ";
		sql += "     , CF.\"MainFlag\" ";
		sql += "     , CI.\"EvaNetWorth\" ";
		sql += "     , CLM.\"EvaDate\" ";
		sql += "     , CASE ";
		sql += "         WHEN CF.\"ClCode1\" = 1 ";
		sql += "         THEN CB.\"BdLocation\" ";
		sql += "       ELSE CL.\"LandLocation\" END AS \"Location\" ";
		sql += "     , CASE ";
		sql += "         WHEN CF.\"ClCode1\" = 1 ";
		sql += "         THEN CB.\"BdNo1\" || '-' || CB.\"BdNo2\" ";
		sql += "       ELSE CL.\"LandNo1\" || '-' || CL.\"LandNo2\" ";
		sql += "       END                   AS \"BdNoLdNo\" ";
		sql += "FROM rawData RD ";
		sql += "LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = RD.\"CustNo\" ";
		sql += "                      AND FM.\"FacmNo\" = RD.\"FacmNo\" ";
		sql += "LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = RD.\"CustNo\" ";
		sql += "                    AND CF.\"FacmNo\" = RD.\"FacmNo\" ";
		sql += "LEFT JOIN \"ClMain\" CLM ON CLM.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                      AND CLM.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                      AND CLM.\"ClNo\" = CF.\"ClNo\" ";
		sql += "LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                    AND CI.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                    AND CI.\"ClNo\" = CF.\"ClNo\" ";
		sql += "LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                         AND CB.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                         AND CB.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                         AND CF.\"ClCode1\" = 1 ";
		sql += "LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                     AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                     AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                     AND CF.\"ClCode1\" = 2 ";
		sql += "WHERE CF.\"ClCode1\" IN (1,2) ";
		sql += "ORDER BY RD.\"CustNo\" ";
		sql += "       , RD.\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

}