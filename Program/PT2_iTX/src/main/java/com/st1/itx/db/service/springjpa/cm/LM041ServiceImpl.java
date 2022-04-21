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
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM041.findAll ");

//		int yearMonth = parse.stringToInteger(titaVo.get("ENTDY")) / 100 + 191100;

		String sql = "";
		sql += " SELECT CT.\"CityItem\" ";
		sql += "       ,ViableCusts.\"CityCode\" ";
		sql += "       ,ViableCusts.\"CustNo\" ";
		sql += "       ,ViableCusts.\"FacmNo\" ";
		sql += "       ,ViableCusts.\"Status\" ";
		sql += "       ,\"Fn_ParseEOL\"(CM.\"CustName\",0) ";
		sql += "       ,A.\"RvBal\" ";
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
		sql += "                   ,SUM(\"RvBal\") \"RvBal\" ";
		sql += "             FROM \"AcReceivable\" ";
		sql += "             WHERE \"AcctCode\" = 'TAV' ";
		sql += "             GROUP BY \"CustNo\" ) A ON A.\"CustNo\" = ViableCusts.\"CustNo\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ViableCusts.\"CustNo\" ";
		sql += " LEFT JOIN \"CdCity\" CT  ON CT.\"CityCode\" = ViableCusts.\"CityCode\" ";
		sql += " WHERE NVL(A.\"RvBal\", 0) > 0 ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);
		return this.convertToMap(query);
	}

}