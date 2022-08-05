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

		String sql = "";
		sql += "  SELECT																	";
		sql += "   ln3.*          ";
		sql += "  ,cd.\"Item\"																";
		sql += "  ,ln2.\"TotTxAmt\"												  ";
		sql += "  FROM													";
		sql += "  ( SELECT																	";
		sql += "      ln.\"CustNo\"				AS	\"CustNo\"										      ";
		sql += "     ,ln.\"AcDate\"	            AS	\"AcDate\"																 	    ";
		sql += "     ,ln.\"TitaKinBr\"	        AS	\"TitaKinBr\"																";
		sql += "     ,ln.\"TitaTlrNo\"	        AS	\"TitaTlrNo\"															  ";
		sql += "     ,ln.\"TitaTxtNo\"		    AS	\"TitaTxtNo\"														";
		sql += "     ,MIN(ln.\"TitaCalDy\")     AS 	\"TitaCalDy\"						";
		sql += "     ,MIN(ln.\"TitaCalTm\")	  	AS	\"TitaCalTm\"               	";
		sql += "     ,MIN(ln.\"CreateDate\")	AS  \"CreateDate\"					";
		sql += "     ,SUM(ln.\"TxAmt\")         AS  \"TotTxAmt\"			     	";
		sql += "    FROM																			";
		sql += "      \"LoanBorTx\"	ln															";
		sql += "    WHERE ln.\"CustNo\" = :CustNo                               				";
		if (iAcDate == 0) {
			sql += "      AND ln.\"EntryDate\" BETWEEN :EntryDateS AND :DateEnd 				";
		} else {
			sql += "      AND ln.\"AcDate\" BETWEEN :AcDateS AND :DateEnd 					";
		}
		sql += "      AND ln.\"Displayflag\" IN ('Y','I','A','F')								";
		if (iTitaHCode == 0) {
			sql += "      AND ln.\"TitaHCode\" = '0'											";
		}
		sql += "      GROUP BY	 ln.\"CustNo\" , ln.\"AcDate\" , ln.\"TitaKinBr\" ,ln.\"TitaTlrNo\" ,ln.\"TitaTxtNo\" 	";
		sql += "  ) ln2										";
		sql += " left join \"LoanBorTx\" ln3  											     ";
		sql += "   ON  ln2.\"CustNo\" = ln3.\"CustNo\"   									 ";
		sql += "  AND  ln2.\"AcDate\" = ln3.\"AcDate\"   									 ";
		sql += "  AND  ln2.\"TitaKinBr\" = ln3.\"TitaKinBr\"   								 ";
		sql += "  AND  ln2.\"TitaTlrNo\" = ln3.\"TitaTlrNo\"   								 ";
		sql += "  AND  ln2.\"TitaTxtNo\" = ln3.\"TitaTxtNo\"   								 ";
		sql += " left  join \"CdCode\" cd  													 ";
		sql += "   on  ln3.\"RepayCode\" = cd.\"Code\"   									 ";
		sql += "  AND cd.\"DefCode\" = 'RepayCode'   									     ";
		sql += " WHERE ln3.\"FacmNo\" >= :FacmNoS											 ";
		sql += "  AND  ln3.\"FacmNo\" <= :FacmNoE											 ";
		sql += "  AND  ln3.\"BormNo\" >= :BormNoS											 ";
		sql += "  AND  ln3.\"BormNo\" <= :BormNoE											 ";
		if (iTitaHCode == 0) {
			sql += "      AND ln3.\"TitaHCode\" = '0'											";
		}
		sql += " ORDER BY ln3.\"AcDate\" ASC ";
		sql += "         ,ln3.\"TitaCalDy\" ASC	                    ";
		sql += "         ,ln3.\"TitaCalTm\"	ASC                    	";
		sql += "         ,ln3.\"CreateDate\" ASC                    ";
		sql += "         ,ln3.\"Displayflag\" ASC                   ";
		sql += "         ,CASE WHEN ln3.\"TxAmt\" > 0  THEN 1 ELSE 0 END DESC ";
		sql += "         ,ln3.\"IntStartDate\" ASC                  ";
		sql += "         ,ln3.\"Rate\" DESC                  ";
		sql += " " + sqlRow;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
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