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

		String sql = "	";
		sql += "    SELECT Ct.\"CityItem\", ";
		sql += "        Cl.\"CityCode\", ";
		sql += "        V.\"CustNo\", ";
		sql += "        V.\"minFacmNo\", ";
		sql += "        V.\"Status\", ";
		sql += "        \"Fn_ParseEOL\"(C.\"CustName\", 0) AS \"CustName\", ";
		sql += "        NVL( A.\"RvBal\",0) AS \"RvBal\" ";
		sql += "    FROM ( ";
		sql += "            select MAX(L.\"Status\") \"Status\", ";
		sql += "                L.\"CustNo\" \"CustNo\", ";
		sql += "                MIN(L.\"FacmNo\") AS \"minFacmNo\" ";
		sql += "            from \"LoanBorMain\" L ";
		sql += "            WHERE L.\"Status\" IN ( ";
		sql += "                    2, ";
		sql += "                    6, ";
		sql += "                    7 ";
		sql += "                ) ";
		sql += "            GROUP BY L.\"CustNo\" ";
		sql += "        ) V ";
		sql += "        LEFT JOIN ( ";
		sql += "            SELECT \"CustNo\", ";
		sql += "                sum(\"RvBal\") as \"RvBal\" ";
		sql += "            FROM \"AcReceivable\" ";
		sql += "            WHERE \"AcctCode\" = 'TAV' ";
		sql += "            GROUP BY \"CustNo\" ";
		sql += "        ) A on A.\"CustNo\" = V.\"CustNo\" ";
		sql += "        LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = V.\"CustNo\" ";
		sql += "        LEFT JOIN \"ClFac\" Cf ON Cf.\"CustNo\" = V.\"CustNo\" ";
		sql += "        					  AND Cf.\"FacmNo\" = V.\"minFacmNo\" ";
		sql += "        					  AND Cf.\"MainFlag\" = 'Y' ";
		sql += "        LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = Cf.\"ClCode1\" ";
		sql += "        					   AND Cl.\"ClCode2\" = Cf.\"ClCode2\" ";
		sql += "       						   AND Cl.\"ClNo\" = Cf.\"ClNo\" ";
		sql += "        LEFT JOIN \"CdCity\" Ct ON Ct.\"CityCode\" = Cl.\"CityCode\" ";
		sql += "    WHERE Nvl(A.\"RvBal\", 0) > 0 ";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("dataday", dataDate);
		return this.convertToMap(query);
	}

}