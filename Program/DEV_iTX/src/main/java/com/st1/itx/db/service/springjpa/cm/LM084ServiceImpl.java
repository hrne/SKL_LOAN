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
public class LM084ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("LM084ServiceImpl findAll ");

		/*
		 * AcLoanInt.Aging 0.一個月以下 1.一～三個月 2.三～六個月 3.六個月以上
		 */

		String sql = "";
		sql += " SELECT ALI.\"Aging\"        "; // 帳齡
		sql += "       ,ALI.\"CustNoShow\"   "; // 戶名（顯示）
		sql += "       ,ALI.\"CustName\"     "; // 姓名（顯示）
		sql += "       ,ALI.\"IntStartDate\" "; // 繳息迄日
		sql += "       ,ALI.\"PayIntDate\"   "; // 應繳息日
		sql += "       ,SUM(ALI.\"Interest\" ";
		sql += "            + NVL(ALIDelayed.\"Interest\", 0)) ";
		sql += "        AS \"Interest\"      "; // 應收利息
		sql += " FROM ( SELECT ALI.\"YearMonth\" ";
		sql += "              ,ALI.\"Aging\" ";
		sql += "              ,ALI.\"CustNo\" ";
		sql += "              ,LPAD(ALI.\"CustNo\", 7, 0) \"CustNoShow\" ";
		sql += "              ,SUBSTR(CM.\"CustName\", 0, 20) \"CustName\" ";
		sql += "              ,ALI.\"IntStartDate\" ";
		sql += "              ,ALI.\"PayIntDate\" ";
		sql += "              ,ALI.\"Interest\" ";
		sql += "              ,ROW_NUMBER() OVER (PARTITION BY ALI.\"CustNo\" ";
		sql += "                                  ORDER BY ALI.\"TermNo\" DESC) \"Seq\" ";
		sql += "        FROM \"AcLoanInt\" ALI ";
		sql += "        LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = ALI.\"CustNo\" ";
		sql += "        WHERE ALI.\"IntStartDate\" > 0 "; // 非滯繳息
		sql += "          AND ALI.\"Interest\"     > 0 "; // 只顯示利息大於0者
		sql += "          AND ALI.\"YearMonth\"    = :inputYearMonth ) ALI ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(\"Interest\") \"Interest\" ";
		sql += "             FROM \"AcLoanInt\" ";
		sql += "             WHERE \"IntStartDate\" = 0 "; // 滯繳息
		sql += "               AND \"YearMonth\"    = :inputYearMonth ";
		sql += "             GROUP BY \"CustNo\" ) ALIDelayed ON ALIDelayed.\"CustNo\" = ALI.\"CustNo\" ";
		sql += "                                             AND ALI.\"Seq\"           = 1 ";
		sql += " GROUP BY ALI.\"Aging\" ";
		sql += "         ,ALI.\"CustNoShow\" ";
		sql += "         ,ALI.\"CustName\" ";
		sql += "         ,ALI.\"IntStartDate\" ";
		sql += "         ,ALI.\"PayIntDate\" ";
		sql += " ORDER BY \"Aging\" ";
		sql += "         ,\"CustNoShow\" ";
		sql += "         ,\"CustName\" ";
		sql += "         ,\"IntStartDate\" ";
		sql += "         ,\"PayIntDate\" ";
		

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		String inputYearMonth = Integer.toString(Integer.parseInt(titaVo.getParam("inputYear")) + 1911) + titaVo.getParam("inputMonth");
		this.info("LM084ServiceImpl input");
		this.info("inputYearMonth: " + inputYearMonth);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}