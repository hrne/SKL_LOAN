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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("L2918ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2918ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;
	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		// work area
		int wkFacmNoStart = 0;
		int wkFacmNoEnd = 999;
		int wkClCode1S = 0;
		int wkClCode1E = 9;
		int wkClCode2S = 0;
		int wkClCode2E = 99;
		int wkClNoS = 0;
		int wkClNoE = 9999999;
		int wkDateEnd = 99991231;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iClCode1 > 0) {
			wkClCode1S = iClCode1;
			wkClCode1E = iClCode1;
		}
		if (iClCode2 > 0) {
			wkClCode2S = iClCode2;
			wkClCode2E = iClCode2;
		}
		if (iClNo > 0) {
			wkClNoS = iClNo;
			wkClNoE = iClNo;
		}

		String sql = "SELECT";
		sql += " clor.\"ClCode1\"   AS \"ClCode1\", ";
		sql += " clor.\"ClCode2\"   AS \"ClCode2\", ";
		sql += " clor.\"ClNo\"      AS \"ClNo\", ";
		sql += " clor.\"Seq\"       AS \"Seq\", ";
		sql += " MIN(clor.\"RecYear\") AS \"RecYear\", ";
		sql += " MIN(clor.\"RecNumber\") AS \"RecNumber\", ";
		sql += " MIN(clor.\"RightsNote\") AS \"RightsNote\", ";
		sql += " MIN(clor.\"SecuredTotal\") AS \"SecuredTotal\", ";
		sql += " nvl(MIN(clor.\"OtherCity\"), MIN(ccode2.\"Item\") ) AS \"CityItem\", ";
		sql += " nvl(MIN(clor.\"OtherLandAdm\"), MIN(cl.\"LandOfficeItem\")) AS \"LandAdm\", ";
		sql += " nvl(MIN(clor.\"OtherRecWord\"), MIN(clo.\"RecWordItem\")) AS \"RecWordItem\" ";
//		sql += " MIN(clor.\"CustNo\") AS \"CustNo\" ";
		sql += " FROM ";
		sql += " ( ";
		sql += " SELECT ";
		sql += " * ";
		sql += " FROM ";
		sql += " \"ClOtherRights\" ";
		sql += " WHERE ";
		sql += "\"ClCode1\" >= :clCode1S ";
		sql += "AND \"ClCode1\" <= :clCode1E ";
		sql += "AND \"ClCode2\" >= :clCode2S ";
		sql += "AND \"ClCode2\" <= :clCode2E ";
		sql += "AND \"ClNo\" >= :clNoS ";
		sql += "AND \"ClNo\" <= :clNoE ";
		sql += ") clor ";
		sql += "LEFT JOIN \"ClFac\"    cf ON clor.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                         AND clor.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                         AND clor.\"ClNo\" = cf.\"ClNo\" ";
		sql += " LEFT JOIN \"CdLand\" cl ON cl.\"CityCode\" = clor.\"City\" ";
		sql += "                             AND cl.\"LandOfficeCode\" = clor.\"LandAdm\" ";
		sql += " LEFT JOIN \"CdLandOffice\" clo ON clo.\"LandOfficeCode\" = clor.\"LandAdm\" ";
		sql += "                                      AND clo.\"RecWord\" = clor.\"RecWord\" ";
		sql += "                                     AND clo.\"CityCode\" = clor.\"City\" ";

		sql += " LEFT JOIN \"CdCode\" ccode2 ON ccode2.\"Code\" = clor.\"City\" ";
		sql += "                         AND ccode2.\"DefCode\" = 'ClOtherRightsCityCd' ";
		sql += " LEFT JOIN \"ClOtherRightsFac\" crf ON crf.\"ClCode1\" = clor.\"ClCode1\" ";//20230327增加條件
		sql += "                                   AND crf.\"ClCode2\" = clor.\"ClCode2\" ";
		sql += "                                   AND crf.\"ClNo\"    = clor.\"ClNo\" ";
		sql += "                                   AND crf.\"Seq\"     = clor.\"Seq\" ";
		
		if (iCustNo > 0) {
			sql += " WHERE ";
			sql += " cf.\"CustNo\" = :custNo ";
			sql += " AND cf.\"FacmNo\" >= :facmNoS ";
			sql += " AND cf.\"FacmNo\" <= :facmNoE ";
			sql += " AND crf.\"CustNo\" = :custNo ";
			sql += " AND crf.\"FacmNo\" >= :facmNoS ";
			sql += " AND crf.\"FacmNo\" <= :facmNoE ";
		}

		sql += " GROUP BY ";
		sql += " clor.\"ClCode1\", ";
		sql += " clor.\"ClCode2\", ";
		sql += " clor.\"ClNo\", ";
		sql += " clor.\"Seq\" ";
		sql += " ORDER BY ";
		sql += " clor.\"ClCode1\" ASC, ";
		sql += " clor.\"ClCode2\" ASC, ";
		sql += " clor.\"ClNo\" ASC, ";
		sql += " clor.\"Seq\" ASC ";
		sql += " " + sqlRow;

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (iCustNo > 0) {
			query.setParameter("custNo", iCustNo);
			query.setParameter("facmNoS", wkFacmNoStart);
			query.setParameter("facmNoE", wkFacmNoEnd);
		}
		query.setParameter("clCode1S", wkClCode1S);
		query.setParameter("clCode1E", wkClCode1E);
		query.setParameter("clCode2S", wkClCode2S);
		query.setParameter("clCode2E", wkClCode2E);
		query.setParameter("clNoS", wkClNoS);
		query.setParameter("clNoE", wkClNoE);
		this.info("iCustNo = " + iCustNo);
		this.info("wkFacmNoStart = " + wkFacmNoStart);
		this.info("wkFacmNoEnd = " + wkFacmNoEnd);
		this.info("wkClCode1S = " + wkClCode1S);
		this.info("wkClCode1E = " + wkClCode1E);
		this.info("wkClCode2S = " + wkClCode2S);
		this.info("wkClCode2E = " + wkClCode2E);
		this.info("wkClNoS = " + wkClNoS);
		this.info("wkClNoE = " + wkClNoE);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		this.info("index = " + index);
		this.info("limit = " + limit);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(this.index * this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		List<Object> result = query.getResultList();

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}

}