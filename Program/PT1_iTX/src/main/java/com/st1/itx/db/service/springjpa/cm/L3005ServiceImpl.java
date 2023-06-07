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
	private Parse parse;

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
		// 1.有撥款序號 => 撥款序號交易的最後一筆
		// 2.有暫收可抵繳 => 該戶有暫收可抵繳的最後一筆
		// 3.債協交易 => 該戶債協交易的最後一筆
		// 4.期票交易 => 不顯示<訂正>按紐
		// 5.利率變更(有撥款序號) => 不顯示<訂正>按紐
		// 6.轉換的撥款、內容變更(有撥款序號) => 不顯示<訂正>按紐
		// 7.其他 => 不顯示<訂正>按紐
		String sqlCondition = "";
		sqlCondition += "  WHERE ln.\"CustNo\" = :CustNo                ";
		if (iAcDate == 0) {
			sqlCondition += "      AND ln.\"EntryDate\" BETWEEN :EntryDateS AND :DateEnd 			";
		} else {
			sqlCondition += "      AND ln.\"AcDate\" BETWEEN :AcDateS AND :DateEnd 					";
		}
		if (iTitaHCode == 0) {
			sqlCondition += "      AND ln.\"TitaHCode\" = '0'										";
		}
		String sql = "";
		sql += " WITH TX1 AS (                ";
		sql += "  SELECT                 ";
		sql += "    l.\"CustNo\"		                      ";
		sql += "   ,l.\"AcDate\"	                       ";
		sql += "   ,l.\"TitaKinBr\"	       							                ";
		sql += "   ,l.\"TitaTlrNo\"	        					                ";
		sql += "   ,l.\"TitaTxtNo\"                ";
		sql += "   ,MIN(CASE WHEN la.\"BorxNo\" is null THEN 0 ELSE 1 end) AS \"LastFg\"                ";
		sql += "  FROM \"LoanBorTx\" l                ";
		sql += "  LEFT JOIN                ";
		sql += "   (                 ";
		sql += "   SELECT                 ";
		sql += "   \"CustNo\"                ";
		sql += "  ,\"FacmNo\"                ";
		sql += "  ,\"BormNo\"                ";
		sql += "  ,MAX(\"BorxNo\") AS \"BorxNo\"                ";
		sql += "  FROM \"LoanBorTx\"                ";
		sql += "  WHERE \"CustNo\" = :CustNo                 ";
		sql += "   AND  \"FacmNo\" > 0                ";
		sql += "   AND  \"BormNo\" > 0                ";
		sql += "   AND  \"TitaHCode\" = '0'                ";
		sql += "   AND  CASE WHEN \"TitaTxCd\" in ('L3721') THEN 5                 ";
		sql += "             WHEN \"CreateEmpNo\" in ('999999') AND \"TitaTxCd\" in ('L3100','L3701') THEN 6                ";
		sql += "             ELSE 1                ";
		sql += "        END = 1                ";
		sql += "  GROUP BY  \"CustNo\"                ";
		sql += "           ,\"FacmNo\"                ";
		sql += "           ,\"BormNo\"                ";
		sql += "  ) la on  la.\"CustNo\" = l.\"CustNo\"                ";
		sql += "      AND  la.\"FacmNo\" = l.\"FacmNo\"                ";
		sql += "      AND  la.\"BormNo\" = l.\"BormNo\"                ";
		sql += "      AND  la.\"BorxNo\" = l.\"BorxNo\"                ";
		sql += "  WHERE l.\"CustNo\" = :CustNo                ";
		sql += "   AND  l.\"FacmNo\" > 0                ";
		sql += "   AND  l.\"BormNo\" > 0                ";
		sql += "   AND  CASE WHEN \"TitaTxCd\" in ('L3721') THEN 5                 ";
		sql += "             WHEN \"CreateEmpNo\" in ('999999') AND \"TitaTxCd\" in ('L3100','L3701') THEN 6                ";
		sql += "             ELSE 1                ";
		sql += "        END = 1                ";
		sql += "  GROUP BY                 ";
		sql += "    l.\"CustNo\"		                    ";
		sql += "   ,l.\"AcDate\"	                        ";
		sql += "   ,l.\"TitaKinBr\"	      						                ";
		sql += "   ,l.\"TitaTlrNo\"	       			                ";
		sql += "   ,l.\"TitaTxtNo\"	                ";
		sql += ")                ";
		sql += ", LASTTX AS (              ";
		sql += "   SELECT              ";
		sql += "    l.\"CustNo\"              ";
		sql += "   ,l.\"AcDate\"	                      ";
		sql += "   ,l.\"TitaKinBr\"	      						              ";
		sql += "   ,l.\"TitaTlrNo\"	       			              ";
		sql += "   ,l.\"TitaTxtNo\"              ";
		sql += "   ,L.\"TitaTxCd\"              ";
		sql += "   ,CASE WHEN l.\"TxKind\" = 1 AND l.\"TmpAmt\" > 0 THEN 2 ELSE l.\"TxKind\" END AS \"TxKind\"  ";
		sql += "   ,l.\"TxBorm\"              ";
		sql += "   ,l.\"TmpAmt\"              ";
		sql += "   ,ROW_NUMBER() OVER (Partition By l.\"CustNo\"              ";
		sql += "                                   ,CASE WHEN l.\"TxKind\" = 1 AND l.\"TmpAmt\" > 0 THEN 2 ELSE l.\"TxKind\" END ";
		sql += "                                   ,CASE WHEN l.\"TxKind\" = 1 AND l.\"TmpAmt\" = 0 THEN l.\"AcDate\" ELSE 0 END ";
		sql += "                                   ,CASE WHEN l.\"TxKind\" = 1 AND l.\"TmpAmt\" = 0 THEN l.\"TitaTlrNo\" ELSE ' ' END ";
		sql += "                                   ,CASE WHEN l.\"TxKind\" = 1 AND l.\"TmpAmt\" = 0 THEN l.\"TitaTxtNo\" ELSE ' ' END ";
		sql += "    	                   	   ORDER BY l.\"AcDate\" Desc 							               ";
		sql += "		                               ,l.\"TitaCalDy\" Desc	                       ";
		sql += "	                                   ,l.\"TitaCalTm\"	Desc                             ";
		sql += "                                       ,l.\"TitaTxtNo\" Desc             	    						";
		sql += "	                                   ,l.\"AcSeq\"	Asc              ";
		sql += "	                    ) AS \"ROWNUMBER\"                              ";
		sql += "   ,l.\"AcSeq\"               ";
		sql += "  FROM (              ";
		sql += "        SELECT              ";
		sql += "         \"CustNo\"              ";
		sql += "        ,\"AcDate\"	                      ";
		sql += "        ,\"TitaKinBr\"	      						              ";
		sql += "        ,\"TitaTlrNo\"	       			              ";
		sql += "        ,\"TitaTxtNo\"              ";
		sql += "        ,MAX(\"TitaTxCd\") AS \"TitaTxCd\"              ";
		sql += "        ,MAX(\"TitaCalDy\") AS \"TitaCalDy\"              ";
		sql += "        ,MAX(\"TitaCalTm\") AS \"TitaCalTm\"              ";
		sql += "        ,SUM(\"TempAmt\" + \"Overflow\") AS \"TmpAmt\"              ";
		sql += "        ,MIN(CASE WHEN \"CreateEmpNo\" in ('999999') AND \"TitaTxCd\" in ('L3100','L3701') THEN 6               ";
		sql += "                  WHEN \"TitaTxCd\" in ('L3721') THEN 5               ";
		sql += "                  WHEN \"CreateEmpNo\" in ('999999') AND \"TitaTxCd\" in ('L3100','L3701') THEN 6              ";
		sql += "       		        WHEN \"AcctCode\" IN ('TCK','RCK','BCK') THEN 4                ";
		sql += "       		        WHEN NVL(JSON_VALUE(\"OtherFields\", '$.TempReasonCode'),' ')  IN ('3','6','03','06') THEN 4              ";
		sql += "                  WHEN \"AcctCode\" IN ('TAV') THEN 2               ";
		sql += "                  WHEN (\"TempAmt\" + \"Overflow\") <> 0 THEN 2               ";
		sql += "                  WHEN \"AcctCode\" IN ('T11','T12','T13') THEN 3               ";
		sql += "                  WHEN \"BormNo\" > 0 THEN 1                 ";
		sql += "     		          ELSE 7              ";
		sql += "       	  	  END)                        AS \"TxKind\"               ";
		sql += "        ,MAX(CASE WHEN \"BormNo\" > 0 THEN 1              ";
		sql += "             ELSE 0              ";
		sql += "   	    END)                           AS \"TxBorm\"               ";
		sql += "       ,MAX(\"AcSeq\")                   AS \"AcSeq\"               ";
		sql += "       FROM \"LoanBorTx\"              ";
		sql += "       WHERE \"CustNo\" = :CustNo              ";
		sql += "         AND  \"TitaHCode\" = '0'              ";
		sql += "       GROUP BY \"CustNo\"              ";
		sql += "               ,\"AcDate\"	                      ";
		sql += "               ,\"TitaKinBr\"	      						              ";
		sql += "               ,\"TitaTlrNo\"	       			              ";
		sql += "               ,\"TitaTxtNo\"              ";
		sql += "        ) l              ";
		sql += "    LEFT JOIN TX1 ON l.\"TxBorm\" > 0              ";
		sql += "                 AND TX1.\"CustNo\" = l.\"CustNo\"              ";
		sql += "                 AND TX1.\"AcDate\"	= l.\"AcDate\"                      ";
		sql += "                 AND TX1.\"TitaKinBr\" = l.\"TitaKinBr\"	      						              ";
		sql += "                 AND TX1.\"TitaTlrNo\" = l.\"TitaTlrNo\"	       			              ";
		sql += "                 AND TX1.\"TitaTxtNo\" = l.\"TitaTxtNo\"              ";
		sql += "                 AND TX1.\"LastFg\" = 1              ";
		sql += "    WHERE l.\"TxKind\" IN (1,2,3)              ";
		sql += "      AND CASE WHEN l.\"TxBorm\" = 0 THEN 1               ";
		sql += "               WHEN NVL(TX1.\"LastFg\",0) = 1 THEN 1               ";
		sql += "               ELSE 0              ";
		sql += "          END = 1                ";
		sql += ")                ";
		sql += ", TXRATE AS (              ";
		sql += "       select                                       ";
		sql += "        ln.\"CustNo\"                               ";
		sql += "       ,ln.\"FacmNo\"                               ";
		sql += "       ,ln.\"BormNo\"                               ";
		sql += "       ,ln.\"BorxNo\"                               ";
		sql += "       ,rr.\"FitRate\"                              ";
		sql += "       ,row_number() over (partition by rr.\"CustNo\", rr.\"FacmNo\", rr.\"BormNo\" order by rr.\"EffectDate\" Desc) as ROWNUMBER ";
		sql += "       from \"LoanBorTx\" ln                            ";
		sql += "       left join \"LoanRateChange\" rr ON rr.\"CustNo\" = ln.\"CustNo\" ";
		sql += "                                      AND rr.\"FacmNo\" = ln.\"FacmNo\" ";
		sql += "                                      AND rr.\"BormNo\" = ln.\"BormNo\" ";
		sql += "                                      AND rr.\"EffectDate\" <= ln.\"EntryDate\" ";  // 入帳日利率
		sql += sqlCondition;  
		sql += "                       AND  ln.\"BormNo\" > 0                  ";
		sql += ") ";
		sql += "  SELECT																	";
		sql += "   ln3.*          															";
		sql += "  ,cdr.\"Item\"					   AS \"RepayCodeX\"						";
		sql += "  ,CASE WHEN ln3.\"TxDescCode\" = 'Fee' AND ln3.\"TitaTxCd\" = 'L3210' THEN '暫收銷'  ||  cdf.\"Item\" 	 ";
		sql += "        WHEN ln3.\"TxDescCode\" = 'Fee' AND ln3.\"TitaTxCd\" IN ('L3230','L3250') THEN '暫收退'  ||  cdf.\"Item\" 	 ";
		sql += "        WHEN ln3.\"TxDescCode\" = 'Fee' THEN cdf.\"Item\"                                            ";
		sql += "        WHEN ln3.\"AcctCode\" = 'TAV' THEN REPLACE(NVL(cdt.\"Item\",ln3.\"Desc\"),'債協','一般債權') 	 ";
		sql += "        ELSE NVL(cdt.\"Item\",ln3.\"Desc\")  	    	                                             ";
		sql += "        END                     AS \"TxDescCodeX\"	                                                 ";
		sql += "  ,ln2.\"TotTxAmt\"												  			";
		sql += "  ,NVL(JSON_VALUE(ln3.\"OtherFields\", '$.ReconCode'), ' ')         AS  \"ReconCode\" ";
		sql += "  ,CASE WHEN NVL(la.\"AcDate\", 0) > 0 THEN 'Y' ";
		sql += "        ELSE NVL(JSON_VALUE(ln3.\"OtherFields\", '$.HCodeFlag'), ' ')       ";
		sql += "        END                     AS  \"HCodeFlag\" ";
		sql += "  ,NVL(ra.\"FitRate\",0)        AS \"FitRate\"						";
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
		sql += sqlCondition; 
		sql += "      GROUP BY	 ln.\"CustNo\" , ln.\"AcDate\" , ln.\"TitaKinBr\" ,ln.\"TitaTlrNo\" ,ln.\"TitaTxtNo\" 	";
		sql += "  ) ln2										";
		sql += " left join \"LoanBorTx\" ln3  											     ";
		sql += "   ON  ln2.\"CustNo\" = ln3.\"CustNo\"   									 ";
		sql += "  AND  ln2.\"AcDate\" = ln3.\"AcDate\"   									 ";
		sql += "  AND  ln2.\"TitaKinBr\" = ln3.\"TitaKinBr\"   								 ";
		sql += "  AND  ln2.\"TitaTlrNo\" = ln3.\"TitaTlrNo\"   								 ";
		sql += "  AND  ln2.\"TitaTxtNo\" = ln3.\"TitaTxtNo\"   								 ";
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
		sql += "   on la.\"CustNo\" = ln3.\"CustNo\"										 ";
		sql += "  and la.\"AcDate\" = ln3.\"AcDate\"										 ";
		sql += "  and la.\"TitaKinBr\"	 = ln3.\"TitaKinBr\"									 ";
		sql += "  and la.\"TitaTlrNo\"	 = ln3.\"TitaTlrNo\"									 ";
		sql += "  and la.\"TitaTxtNo\"	 = ln3.\"TitaTxtNo\"									 ";
		sql += "  and la.ROWNUMBER = 1								                      	 ";
		sql += " LEFT JOIN TXRATE ra		 									     		 ";
		sql += "   on ra.\"CustNo\" = ln3.\"CustNo\"										 ";
		sql += "  and ra.\"FacmNo\" = ln3.\"FacmNo\"										 ";
		sql += "  and ra.\"BormNo\" = ln3.\"BormNo\"				 					     ";
		sql += "  and ra.\"BorxNo\" = ln3.\"BorxNo\"								     	 ";
		sql += "  and ra.ROWNUMBER = 1								                      	 ";
		sql += "  and ln3.\"BormNo\" > 0            								    	 ";
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

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> checkIsArchive(TitaVo titaVo) throws Exception {

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));

		String sql = "";
		sql += " SELECT TA.\"CustNo\" ";
		sql += " FROM \"TxArchiveTableLog\" TA ";
		sql += " WHERE TA.\"CustNo\" = :inputCustNo ";
		sql += "   AND TA.\"DataFrom\" = 'ONLINE' ";
		sql += "   AND TA.\"Type\" = '5YTX' ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputCustNo", iCustNo);

		return this.convertToMap(query);
	}
}