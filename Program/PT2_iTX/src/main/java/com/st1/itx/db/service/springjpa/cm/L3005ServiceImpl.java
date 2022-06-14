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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L3005ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L3005ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

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

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iTitaHCode = this.parse.stringToInteger(titaVo.getParam("TitaHCode"));

		// work area
		int wkFacmNoStart = 0;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 0;
		int wkBormNoEnd = 900;
		int wkAcDateStart = iAcDate;
		int wkEntryDateStart = iEntryDate;
		int wkDateEnd = 99991231;
		if (iFacmNo != 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo != 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		String sql = " SELECT																	";

		sql += "      ln3.*																	,	";
		sql += "      cd.\"Item\"																";
		sql += "      ln2.\"SumTxAmt\"																";

		sql += "  FROM							(												";
		sql += " SELECT																	";
		sql += "      ln.\"CustNo\"																	,	";
		sql += "      ln.\"AcDate\"																	,	";
		sql += "      ln.\"TitaKinBr\"																	,	";
		sql += "      ln.\"TitaTlrNo\"																	,	";
		sql += "      ln.\"TitaTxtNo\"																	,	";
		sql += "      SUM.\"TxAmt\"							as \"SumTxAmt\"						";
		sql += "  FROM																			";
		sql += "      \"LoanBorTx\"	ln															";

		if (iAcDate == 0) {
			sql += "      WHERE ln.\"EntryDate\" BETWEEN :EntryDateS AND :DateEnd 				";
		} else {
			sql += "      WHERE ln.\"AcDate\" BETWEEN :AcDateS AND :DateEnd 					";
		}

		sql += "      AND ln.\"CustNo\" = :CustNo												";
		sql += "      AND ln.\"FacmNo\" >= :FacmNoS												";
		sql += "      AND ln.\"FacmNo\" <= :FacmNoE												";
		sql += "      AND ln.\"BormNo\" >= :BormNoS												";
		sql += "      AND ln.\"BormNo\" <= :BormNoE												";
		sql += "      AND ln.\"Displayflag\" IN ('Y','I','A','F')								";
		if (iTitaHCode == 0) {
			sql += "      AND ln.\"TitaHCode\" = '0'											";
		}
		sql += "      GROUP BY	 ln.\"CustNo\" , ln\"AcDate\" , ln\"TitaKinBr\" ,ln\"TitaTlrNo\" ,ln\"TitaTxtNo\" ,		";
		sql += "     ) 			ln2										";
		sql += "     left join \"LoanBorTx\" ln3  													";
		sql += "     on ln2.\"CustNo\" = ln3.\"CustNo\"   										";
		sql += "     on ln2.\"AcDate\" = ln3.\"AcDate\"   										";
		sql += "     on ln2.\"TitaKinBr\" = ln3.\"TitaKinBr\"   										";
		sql += "     on ln2.\"TitaTlrNo\" = ln3.\"TitaTlrNo\"   										";
		sql += "     on ln2.\"TitaTxtNo\" = ln3.\"TitaTxtNo\"   										";

		sql += "     left join \"CdCode\" cd  													";
		sql += "     on ln3.\"RepayCode\" = cd.\"Code\"   										";
		sql += "     AND cd.\"DefCode\" = 'RepayCode'   										";

		sql += "      ORDER BY ln3.\"AcDate\" asc, ";
		sql += " NVL(JSON_VALUE(ln3.\"OtherFields\",'$.CreateDate'),ln3.\"CreateDate\")  asc, ln3.\"Displayflag\" asc ,";
		sql += "CASE WHEN ln2.\"SumTxAmt\" > 0  THEN 1 ELSE 0 END DESC ";
		sql += " " + sqlRow;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("CustNo", iCustNo);
		query.setParameter("FacmNoS", wkFacmNoStart);
		query.setParameter("FacmNoE", wkFacmNoEnd);
		query.setParameter("BormNoS", wkBormNoStart);
		query.setParameter("BormNoE", wkBormNoEnd);
		if (iAcDate == 0) {
			query.setParameter("EntryDateS", wkEntryDateStart + 19110000);
			query.setParameter("DateEnd", wkDateEnd);
		} else {
			query.setParameter("AcDateS", wkAcDateStart + 19110000);
			query.setParameter("DateEnd", wkDateEnd);
		}
		this.info("iCustNo = " + iCustNo);
		this.info("wkFacmNoStart = " + wkFacmNoStart);
		this.info("wkFacmNoEnd = " + wkFacmNoEnd);
		this.info("wkBormNoStart = " + wkBormNoStart);
		this.info("wkBormNoEnd = " + wkBormNoEnd);
		this.info("wkAcDateStart = " + wkAcDateStart);
		this.info("wkEntryDateStart = " + wkEntryDateStart);
		this.info("wkDateEnd = " + wkDateEnd);

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

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}

}