package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
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
		String iTitaKinBr = "";
		String iTitaTlrNo = "";
		String iTitaTxtNo = "";
		if (titaVo.getParam("TxtNo").length() == 18) {
			iTitaKinBr = titaVo.getParam("TxtNo").substring(0, 4);
			iTitaTlrNo = titaVo.getParam("TxtNo").substring(4, 10);
			iTitaTxtNo = titaVo.getParam("TxtNo").substring(10, 18);
		}
		this.info("iTitaKinBr = " + iTitaKinBr);
		this.info("iTitaTlrNo = " + iTitaTlrNo);
		this.info("iTitaTxtNo = " + iTitaTxtNo);
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
		sql += "   WITH LASTTX AS ( 														";
		sql += "   SELECT																	";
		sql += "      \"CustNo\"				AS	\"CustNo\"								";
		sql += "     ,\"AcDate\"	            AS	\"AcDate\"								";
		sql += "     ,\"TitaKinBr\"	        AS	\"TitaKinBr\"								";
		sql += "     ,\"TitaTlrNo\"	        AS	\"TitaTlrNo\"								";
		sql += "     ,\"TitaTxtNo\"		    AS	\"TitaTxtNo\"								";
		sql += "  FROM																		";
		sql += "   (SELECT																	";
		sql += "      \"CustNo\"		    AS	\"CustNo\"					    			";
		sql += "     ,\"AcDate\"	        AS	\"AcDate\"						    		";
		sql += "     ,\"TitaKinBr\"	        AS	\"TitaKinBr\"								";
		sql += "     ,\"TitaTlrNo\"	        AS	\"TitaTlrNo\"								";
		sql += "     ,\"TitaTxtNo\"		    AS	\"TitaTxtNo\"								";
		sql += " 	 ,ROW_NUMBER() OVER (Partition By \"CustNo\"             				";
		sql += "       ,CASE WHEN \"AcctCode\" IN ('T11','T12','T13','T21','T22','T23') THEN 1  "; // 債協科目分開控管
		sql += "             WHEN NVL(JSON_VALUE(\"OtherFields\", '$.ReconCode'),' ')  IN ('A6') THEN 1 "; // 債協入帳分開控管
		sql += "             ELSE 0 END";
		sql += " 	   ORDER BY \"AcDate\" Desc 											";
		sql += "               ,\"TitaCalDy\" Desc	                    					";
		sql += "               ,\"TitaCalTm\"	Desc                    					";
		sql += "               ,\"TitaTxtNo\" Desc             	    						";
		sql += "               ,\"AcSeq\"     Desc )  AS \"RowNumber\"  					";
		sql += "    FROM																	";
		sql += "      \"LoanBorTx\"															";
		sql += "    WHERE \"CustNo\" = :CustNo                               				";
		sql += "      AND \"TitaTxCd\" IN ('L3200','L3210','L3220','L3230','L3410','L3420','L3410','L3440','L3711','L3712', 'L618B','L618C')							";
		sql += "      AND \"TitaHCode\" = '0'											 	";
		sql += "  ) 																		";
		sql += "  	WHERE \"RowNumber\"	= 1													";
		sql += "  	)																	 	";
		sql += "  SELECT																	";
		sql += "   ln3.*          															";
		sql += "  ,cdr.\"Item\"					   AS \"RepayCodeX\"						";
		sql += "  ,CASE WHEN ln3.\"TxDescCode\" = 'Fee' AND ln3.\"TitaTxCd\" = 'L3210' THEN '暫收銷'  ||  cdf.\"Item\" 	 ";
		sql += "        WHEN ln3.\"TxDescCode\" = 'Fee' AND ln3.\"TitaTxCd\" = 'L3230' THEN '暫收退'  ||  cdf.\"Item\" 	 ";
		sql += "        WHEN ln3.\"TxDescCode\" = 'Fee' THEN cdf.\"Item\"                                            ";
		sql += "        ELSE NVL(cdt.\"Item\",ln3.\"Desc\")  	    	                                             ";
		sql += "        END                     AS \"TxDescCodeX\"	                                                 ";
		sql += "  ,ln2.\"TotTxAmt\"												  			";
		sql += "  ,NVL(JSON_VALUE(ln3.\"OtherFields\", '$.ReconCode'), rm.\"ReconCode\")  AS  \"ReconCode\" ";
		sql += "  ,CASE WHEN NVL(la.\"AcDate\", 0) > 0 THEN 'Y' ELSE ' ' END	    AS  \"HCodeFlag\" ";
		sql += "  FROM																		";
		sql += "  ( SELECT																	";
		sql += "      ln.\"CustNo\"				AS	\"CustNo\"								";
		sql += "     ,ln.\"AcDate\"	            AS	\"AcDate\"								";
		sql += "     ,ln.\"TitaKinBr\"	        AS	\"TitaKinBr\"							";
		sql += "     ,ln.\"TitaTlrNo\"	        AS	\"TitaTlrNo\"							";
		sql += "     ,ln.\"TitaTxtNo\"		    AS	\"TitaTxtNo\"							";
		sql += "     ,MIN(ln.\"TitaCalDy\")     AS 	\"TitaCalDy\"							";
		sql += "     ,MIN(ln.\"TitaCalTm\")	  	AS	\"TitaCalTm\"               			";
		sql += "     ,SUM(ln.\"TxAmt\")         AS  \"TotTxAmt\"			     			";
		sql += "    FROM																	";
		sql += "      \"LoanBorTx\"	ln														";
		sql += "    WHERE ln.\"CustNo\" = :CustNo                               			";
		if (iAcDate == 0) {
			sql += "      AND ln.\"EntryDate\" BETWEEN :EntryDateS AND :DateEnd 			";
		} else {
			sql += "      AND ln.\"AcDate\" BETWEEN :AcDateS AND :DateEnd 					";
		}
		sql += "      AND ln.\"Displayflag\" IN ('Y','I','A','F')							";
		if (iTitaHCode == 0) {
			sql += "      AND ln.\"TitaHCode\" = '0'										";
		}
		sql += "      GROUP BY	 ln.\"CustNo\" , ln.\"AcDate\" , ln.\"TitaKinBr\" ,ln.\"TitaTlrNo\" ,ln.\"TitaTxtNo\" 	";
		sql += "  ) ln2										";
		sql += " left join \"LoanBorTx\" ln3  											     ";
		sql += "   ON  ln2.\"CustNo\" = ln3.\"CustNo\"   									 ";
		sql += "  AND  ln2.\"AcDate\" = ln3.\"AcDate\"   									 ";
		sql += "  AND  ln2.\"TitaKinBr\" = ln3.\"TitaKinBr\"   								 ";
		sql += "  AND  ln2.\"TitaTlrNo\" = ln3.\"TitaTlrNo\"   								 ";
		sql += "  AND  ln2.\"TitaTxtNo\" = ln3.\"TitaTxtNo\"   								 ";
		sql += " left join \"BankRmtf\" rm  											     ";
		sql += "   ON  rm.\"CustNo\" = ln3.\"CustNo\"   									 ";
		sql += "  AND  rm.\"AcDate\" = ln3.\"AcDate\"   									 ";
		sql += "  AND  rm.\"TitaTlrNo\" = ln3.\"TitaTlrNo\"   								 ";
		sql += "  AND  rm.\"TitaTxtNo\" = ln3.\"TitaTxtNo\"   								 ";
		sql += "  AND  rm.\"TitaTxtNo\" = ln3.\"TitaTxtNo\"   								 ";
		sql += "  AND  ln3.\"RepayCode\" =  1         										 ";
		sql += "  AND  NVL(JSON_VALUE(ln3.\"OtherFields\", '$.ReconCode'), ' ') = ' '        ";
		sql += " left  join \"CdCode\" cdr  													 ";
		sql += "   on  cdr.\"DefCode\" = 'BatchRepayCode'   									     ";
		sql += "  AND  cdr.\"Code\"    = ln3.\"RepayCode\" 									 ";
		sql += " left  join \"CdCode\" cdt													 ";
		sql += "   on  cdt.\"DefCode\" = 'TxDescCode'   									     ";
		sql += "  AND  cdt.\"Code\"    = ln3.\"TxDescCode\" 									 ";
		sql += " left  join \"CdCode\" cdf  													 ";
		sql += "   on  ln3.\"TxDescCode\" = 'Fee'   									     ";
		sql += "  AND  cdf.\"DefCode\" = 'AcctCode'   									     ";
		sql += "  AND  cdf.\"Code\"    = ln3.\"AcctCode\" 									 ";
		sql += " LEFT JOIN LASTTX la 			 											 ";
		sql += "   on ln3.\"CustNo\" = la.\"CustNo\"										 ";
		sql += "  and ln3.\"AcDate\" = la.\"AcDate\"										 ";
		sql += "  and ln3.\"TitaKinBr\"	 = la.\"TitaKinBr\"									 ";
		sql += "  and ln3.\"TitaTlrNo\"	 = la.\"TitaTlrNo\"									 ";
		sql += "  and ln3.\"TitaTxtNo\"	 = la.\"TitaTxtNo\"									 ";

		sql += " WHERE 											 							 ";
		if (iTitaTxtNo.isEmpty()) {
			sql += " ln3.\"FacmNo\" >= :FacmNoS											 	 ";
			sql += "  AND  ln3.\"FacmNo\" <= :FacmNoE										 ";
			sql += "  AND  ln3.\"BormNo\" >= :BormNoS										 ";
			sql += "  AND  ln3.\"BormNo\" <= :BormNoE										 ";
		} else {
			sql += " ln3.\"TitaKinBr\" = :titaKinBr											 ";
			sql += "  AND  ln3.\"TitaTlrNo\" = :titaTlrNo									 ";
			sql += "  AND  ln3.\"TitaTxtNo\" = :titaTxtNo									 ";
		}
		if (iTitaHCode == 0) {
			sql += "      AND ln3.\"TitaHCode\" = '0'										 ";
		}
		sql += " ORDER BY ln2.\"AcDate\" ASC ";
		sql += "         ,ln2.\"TitaCalDy\" ASC	                    						 ";
		sql += "         ,ln2.\"TitaCalTm\"	ASC                    							 ";
		sql += "         ,ln3.\"TitaTxtNo\" ASC             	    						 ";
		sql += "         ,ln3.\"AcSeq\"     ASC                     						 ";
		sql += "         ,ln3.\"CreateDate\" ASC                    						 ";
		sql += "         ,ln3.\"Displayflag\" ASC                  							 ";
		sql += " " + sqlRow;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("CustNo", iCustNo);
		if (iTitaTxtNo.isEmpty()) {
			query.setParameter("FacmNoS", wkFacmNoStart);
			query.setParameter("FacmNoE", wkFacmNoEnd);
			query.setParameter("BormNoS", wkBormNoStart);
			query.setParameter("BormNoE", wkBormNoEnd);
		} else {
			query.setParameter("titaKinBr", iTitaKinBr);
			query.setParameter("titaTlrNo", iTitaTlrNo);
			query.setParameter("titaTxtNo", iTitaTxtNo);
		}
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
	
	public List<Map<String, String>> custNoLastLoanBorTx(int iCustNo, TitaVo titaVo) throws Exception {
		String sql = "";
		sql += "  SELECT																	";
		sql += "  tx.*                                    						    	   	";
		sql += "  FROM																		";
		sql += "   (SELECT																	";
		sql += "     \"LoanBorTx\".* 					    			                    ";
		sql += " 	 ,ROW_NUMBER() OVER (Partition By \"CustNo\"             				";
		sql += "       ,CASE WHEN \"AcctCode\" IN ('T11','T12','T13','T21','T22','T23') THEN 1  "; // 債協科目分開控管
		sql += "             WHEN NVL(JSON_VALUE(\"OtherFields\", '$.ReconCode'),' ')  IN ('A6') THEN 1 "; // 債協入帳分開控管
		sql += "             ELSE 0 END";
		sql += " 	   ORDER BY \"AcDate\" Desc 											";
		sql += "               ,\"TitaCalDy\" Desc	                    					";
		sql += "               ,\"TitaCalTm\"	Desc                    					";
		sql += "               ,\"TitaTxtNo\" Desc             	    						";
		sql += "               ,\"AcSeq\"     Desc )  AS \"RowNumber\"  					";
		sql += "    FROM																	";
		sql += "      \"LoanBorTx\"															";
		sql += "    WHERE \"CustNo\" = :CustNo                               				";
		sql += "      AND \"TitaTxCd\" IN ('L3200','L3210','L3220','L3230','L3410','L3420','L3410','L3440','L3711','L3712', 'L618B','L618C')							";
		sql += "      AND \"TitaHCode\" = '0'											 	";
		sql += "  ) tx																		";
		sql += "  WHERE \"RowNumber\"	= 1													";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		this.info("sql=" + sql);
		query.setParameter("CustNo", iCustNo);
		query.setFirstResult(0);
		query.setMaxResults(this.limit);
		List<Object> result = query.getResultList();
		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

}