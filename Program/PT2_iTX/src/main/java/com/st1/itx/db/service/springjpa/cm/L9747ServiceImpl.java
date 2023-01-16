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
public class L9747ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	 * @param dataDate 資料日期
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int dataDate) throws Exception {
		this.info("L9747.findAll ");
		dataDate = dataDate + 19110000;
		this.info("dataDate =" + dataDate);
		
		
		String sql = "";
		sql += " SELECT CT.\"CityItem\" ";
		sql += "       ,v.\"CityCode\" ";
		sql += "       ,v.\"CustNo\" ";
		sql += "       ,v.\"FacmNo\" ";
		sql += "       ,v.\"Status\" ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\" ";
		sql += "       ,A.\"RvBal\" ";
		sql += " FROM ( SELECT CL.\"CityCode\" AS \"CityCode\"";
		sql += "              ,MAX(L.\"Status\") \"Status\" ";
		sql += "              ,D.\"CustNo\" AS \"CustNo\"";
		sql += "              ,MIN(D.\"FacmNo\") \"FacmNo\" ";
		sql += "        FROM \"DailyLoanBal\" D";
		sql += " 		LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = D.\"CustNo\" ";
		sql += " 								   AND L.\"FacmNo\" = D.\"FacmNo\" ";
		sql += " 								   AND L.\"BormNo\" = D.\"BormNo\" ";
		sql += " 		LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = L.\"CustNo\" ";
		sql += " 							  AND CF.\"FacmNo\" = L.\"FacmNo\" ";
		sql += " 							  AND CF.\"MainFlag\" = 'Y' ";
		sql += " 		LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += " 							   AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += " 							   AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += "        WHERE D.\"DataDate\" = :dataday ";
		sql += "          AND D.\"LatestFlag\" = 1 ";
		sql += "          AND L.\"Status\" IN (2,6) ";
		sql += "        GROUP BY CL.\"CityCode\" ";
		sql += "                ,D.\"CustNo\" ) v ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(\"RvBal\") \"RvBal\" ";
		sql += "             FROM \"AcReceivable\" ";
		sql += "             WHERE \"AcctCode\" = 'TAV' ";
		sql += "             GROUP BY \"CustNo\" ) A ON A.\"CustNo\" = v.\"CustNo\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = v.\"CustNo\" ";
		sql += " LEFT JOIN \"CdCity\" CT  ON CT.\"CityCode\" = v.\"CityCode\" ";
		sql += " WHERE NVL(A.\"RvBal\", 0) > 0 ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("dataday",dataDate);
		return this.convertToMap(query);
	}

}