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
public class LM002ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> doQuery(TitaVo titaVo) throws Exception {
		this.info("lM002.doQuery");

		// 資料前兩年一月起至當月
		String startYM = (parse.stringToInteger(titaVo.get("ENTDY").substring(0, 4)) + 1909) + "01";
		String endYM = parse.IntegerToString(parse.stringToInteger(titaVo.get("ENTDY").substring(0, 6)) + 191100, 1);

		String sql = "";
		// 為了排除掉不是出表範圍的資料，實際Query包進subquery，以便篩選掉DataType=0者
		sql += " SELECT \"Year\" ";
		sql += "       ,\"DataType\" ";
		sql += "       ,\"Month\" ";
		sql += "       ,SUM(\"LoanBalance\") \"LoanSum\" ";

		sql += " FROM ( SELECT TRUNC(MLB.\"YearMonth\" / 100) AS \"Year\" ";
		sql += "              ,CASE WHEN MLB.\"AcctCode\" != '990' ";
		sql += "                    THEN CASE WHEN NVL(FP.\"GovOfferFlag\", 'N') = 'Y' ";
		sql += "                              THEN 2 "; // 政府優惠: 非990; 政府優惠記號為Y
		sql += "                              WHEN MLB.\"AcctCode\" = '340' ";
		sql += "                              THEN 3 "; // 首購: 非990; 交易代號為340
		sql += "                              WHEN MLB.\"ProdNo\" IN ('81','82','83') ";
		sql += "                              THEN 1 "; // 921: 非990; 商品代號為81, 82, 83
		sql += "                         ELSE 0 END "; // 非資料範圍者標記為0，在外層去除掉
		sql += "                    WHEN NVL(FP.\"GovOfferFlag\", 'N') = 'Y' ";
		sql += "                      OR MLB.\"FacAcctCode\" = '340' ";
		sql += "                    THEN 4 "; // 催收款項: 是990; GovOfferFlag為Y 或 原會計科目為340者
		sql += "               ELSE 0 END AS \"DataType\" "; // 非資料範圍者標記為0，在外層去除掉
		// 非催收三種的子條件做成巢狀稍微難讀，但會使速度較快

		sql += "              ,MOD(MLB.\"YearMonth\", 100) AS \"Month\" ";
		sql += "              ,MLB.\"LoanBalance\" AS \"LoanBalance\" ";
		sql += "        FROM \"MonthlyLoanBal\" MLB ";
		sql += "        LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = MLB.\"ProdNo\" ";
		sql += "        WHERE MLB.\"YearMonth\" BETWEEN :startYM AND :endYM ";
		sql += "          AND MLB.\"LoanBalance\" > 0 ";
		sql += "          AND NVL(FP.\"ProdNo\", 'XXX') != 'XXX' ";
		sql += " ) ";
		sql += " WHERE \"DataType\" != 0 ";
		sql += " GROUP BY \"Year\" "; // 為了DRY，把GROUP BY拉出來外層做，否則DataType條件會需要寫兩次
		sql += "         ,\"DataType\" "; // 對於效能並無顯著影響
		sql += "         ,\"Month\" ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startYM", startYM);
		query.setParameter("endYM", endYM);

		return this.convertToMap(query);
	}

}