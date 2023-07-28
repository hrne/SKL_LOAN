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
public class L9721ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L9721ServiceImpl findAll ");

		int inputEndDate = parse.stringToInteger(titaVo.getParam("InputEndDate"));

		this.info("L9721ServiceImpl inputEndDate = " + inputEndDate);
//		1.資料止日為該工作月的止日
//		2.撥款日期>=資料止日
//		3.戶況為0正常或已結案但結案日>資料止日
//		4.商品參數主檔員工貸款記號為Y。

		String sql = "SELECT m.\"CustNo\" AS \"戶號\"";
		sql += "            ,m.\"FacmNo\" AS \"額度\"";
		sql += "            ,m.\"BormNo\" AS \"撥款\"";
		sql += "            ,m.\"ProdNo\" AS \"利率代碼\"";
		sql += "            ,m.\"FitRate\" AS \"利率\"";
		sql += "            ,m.\"EffectDate\" AS \"利率生效日\"";
		sql += "            ,m.\"UsageCode\" AS \"用途別\"";
		sql += "            ,cdc.\"CityItem\" AS \"縣市\"";
		sql += "            ,cda.\"AreaItem\" AS \"鄉鎮市區\"";
		sql += "            ,cl.\"BdLocation\" AS \"地址\"";
		sql += "            ,m.\"DrawdownDate\" AS \"撥款日\"";
		sql += "            ,m.\"MaturityDate\" AS \"到期日\"";
		sql += "            ,m.\"DrawdownAmt\" AS \"撥款金額\"";
		sql += "            ,m.\"LoanBal\" AS \"放款餘額\"";
		sql += "            ,m.\"PrevPayIntDate\" AS \"繳息迄日\"";
		sql += "            ,m.\"PieceCode\" AS \"計件代碼\"";
		sql += "            ,c.\"ClNo\" AS \"擔保品號碼\"";
		sql += "            ,c.\"ClCode1\" AS \"擔保品代號1\"";
		sql += "            ,c.\"ClCode2\" AS \"擔保品代號2\"";
		sql += "      FROM ( SELECT b.\"CustNo\" AS \"CustNo\"";
		sql += "                   ,b.\"FacmNo\" AS \"FacmNo\"";
		sql += "                   ,b.\"BormNo\" AS \"BormNo\"";
		sql += "                   ,fac.\"ProdNo\" AS \"ProdNo\"";
		sql += "                   ,b.\"UsageCode\" AS \"UsageCode\"";
		sql += "                   ,r.\"FitRate\" AS \"FitRate\"";
		sql += "                   ,r.\"EffectDate\" AS \"EffectDate\"";
		sql += "                   ,b.\"DrawdownDate\" AS \"DrawdownDate\"";
		sql += "                   ,b.\"MaturityDate\" AS \"MaturityDate\"";
		sql += "                   ,b.\"DrawdownAmt\" AS \"DrawdownAmt\"";
		sql += "                   ,b.\"LoanBal\" AS \"LoanBal\"";
		sql += "                   ,b.\"PrevPayIntDate\" AS \"PrevPayIntDate\"";
		sql += "                   ,b.\"PieceCode\" AS \"PieceCode\"";
		sql += "                   ,ROW_NUMBER() OVER(PARTITION BY b.\"CustNo\"";
		sql += "                                                  ,b.\"FacmNo\"";
		sql += "                                                  ,b.\"BormNo\"";
		sql += "                                      ORDER BY r.\"EffectDate\" DESC";
		sql += "                    ) AS row_number";
		sql += "             FROM \"LoanBorMain\" b";
		sql += "             LEFT JOIN \"FacMain\" fac ON fac.\"CustNo\" = b.\"CustNo\"";
		sql += "                                      AND fac.\"FacmNo\" = b.\"FacmNo\"";
		sql += "             LEFT JOIN \"FacProd\" p ON p.\"ProdNo\" = fac.\"ProdNo\"";
		sql += "             LEFT JOIN \"LoanRateChange\" r ON r.\"CustNo\" = b.\"CustNo\"";
		sql += "                                           AND r.\"FacmNo\" = b.\"FacmNo\"";
		sql += "                                           AND r.\"BormNo\" = b.\"BormNo\"";
		sql += "             WHERE b.\"DrawdownDate\" <= :inputEndDate";
	    sql += "               AND (b.\"Status\" = 0 OR (b.\"Status\" > 0 AND b.\"AcDate\" > :inputEndDate )) ";
		sql += "               AND p.\"EmpFlag\" = 'Y' ) m";
		sql += "      LEFT JOIN \"ClFac\" c ON c.\"CustNo\" = m.\"CustNo\"";
		sql += "                           AND c.\"FacmNo\" = m.\"FacmNo\"";
		sql += "      LEFT JOIN \"ClBuilding\"  cl ON cl.\"ClCode1\" = c.\"ClCode1\"";
		sql += "                                  AND cl.\"ClCode2\" = c.\"ClCode2\"";
		sql += "                                  AND cl.\"ClNo\" = c.\"ClNo\"";
		sql += "      LEFT JOIN \"CdCity\" cdc ON cdc.\"CityCode\" = cl.\"CityCode\"";
		sql += "      LEFT JOIN \"CdArea\" cda ON cda.\"CityCode\" = cl.\"CityCode\"";
		sql += "                              AND cda.\"AreaCode\" = cl.\"AreaCode\"";
		sql += "      WHERE m.row_number = 1";
		sql += "      ORDER BY  m.\"CustNo\", m.\"FacmNo\", m.\"BormNo\"";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		
		query.setParameter("inputEndDate", inputEndDate);

		return this.convertToMap(query);
	}

}