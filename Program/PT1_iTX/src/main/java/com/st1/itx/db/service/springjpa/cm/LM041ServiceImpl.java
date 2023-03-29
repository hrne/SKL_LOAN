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
public class LM041ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param entdy 會計日
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth, int entdy) throws Exception {
		this.info("lM041.findAll ");


		String sql = "";
		sql += " SELECT CT.\"CityItem\" ";
		sql += "       ,ViableCusts.\"CityCode\" ";
		sql += "       ,ViableCusts.\"CustNo\" ";
		sql += "       ,ViableCusts.\"FacmNo\" ";
		sql += "       ,ViableCusts.\"Status\" ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "       ,DECODE(ViableCusts.\"Status\",2,A2.\"RvBal\",A.\"RvBal\") AS \"RvBal\"";
		sql += " FROM ( SELECT \"CityCode\" ";
		sql += "              ,MAX(\"Status\") \"Status\" ";
		sql += "              ,\"CustNo\" ";
		sql += "              ,MIN(\"FacmNo\") \"FacmNo\" ";
		sql += "        FROM \"MonthlyFacBal\" ";
		sql += "        WHERE \"YearMonth\" = :yearMonth ";
		sql += "          AND \"Status\" IN (2,6) ";
		sql += "        GROUP BY \"CityCode\" ";
		sql += "                ,\"CustNo\" ) ViableCusts ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,\"TxAmt\" AS  \"RvBal\" ";
		sql += "             FROM \"AcDetail\" ";
		sql += "             WHERE \"AcctCode\" = 'TAV' ";
		sql += "               AND \"DbCr\" = 'D' ";
		sql += "               AND \"AcDate\" = :entdy ";
		sql += "             ) A ON A.\"CustNo\" = ViableCusts.\"CustNo\" ";
		sql += "                AND ViableCusts.\"Status\" = 6 ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,\"AcBal\" AS  \"RvBal\" ";
		sql += "             FROM \"AcReceivable\" ";
		sql += "             WHERE \"AcctCode\" = 'TAV' ";
		sql += "               AND \"OpenAcDate\" = :entdy ";
		sql += "             ) A2 ON A2.\"CustNo\" = ViableCusts.\"CustNo\" ";
		sql += "                 AND ViableCusts.\"Status\" = 2 ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ViableCusts.\"CustNo\" ";
		sql += " LEFT JOIN \"CdCity\" CT  ON CT.\"CityCode\" = ViableCusts.\"CityCode\" ";
		sql += " WHERE NVL(A.\"RvBal\", 0) > 0 ";
		sql += " 	OR NVL(A2.\"RvBal\", 0) > 0 ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query);
	}

}