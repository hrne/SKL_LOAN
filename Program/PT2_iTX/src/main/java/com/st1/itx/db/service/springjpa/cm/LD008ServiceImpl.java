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
public class LD008ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int rptType, String acSubBookCode, TitaVo titaVo) throws Exception {
		this.info("LD008 findAll rptType = " + rptType + " , acSubBookCode = " + acSubBookCode);

		int iENTDAY = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;

		String sql = " ";
		sql += " WITH loanData AS ( ";
		sql += "   SELECT B.\"CustNo\" ";
		sql += "        , B.\"FacmNo\" ";
		sql += "        , B.\"BormNo\" ";
		sql += "        , CASE ";
		sql += "            WHEN B.\"Status\" IN (2,5,6,7,8,9) ";
		sql += "            THEN NVL(O.\"AcctCode\",'   ') ";
		sql += "          ELSE F.\"AcctCode\" END         AS \"AcctCode\" ";
		sql += "        , F.\"AcctCode\"                  AS \"FacAcctCode\" ";
		sql += "        , CASE  ";
		sql += "            WHEN B.\"Status\" IN (0,4)  ";
		sql += "            THEN B.\"LoanBal\" ";
		sql += "            WHEN B.\"Status\" IN (2,7)   ";
		sql += "            THEN NVL(O.\"OvduBal\",0) ";
		sql += "          ELSE 0 END                      AS \"LoanBalance\"  ";
		sql += "   FROM \"LoanBorMain\" B ";
		sql += "   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = B.\"CustNo\" ";
		sql += "                          AND F.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "   LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = B.\"CustNo\" ";
		sql += "                              AND O.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "                              AND O.\"BormNo\" = B.\"BormNo\" ";
		sql += "                              AND O.\"OvduNo\" = B.\"LastOvduNo\" ";
		sql += "                              AND B.\"Status\" IN (2,5,6,7,8,9) ";
		sql += "   WHERE B.\"Status\" in (0,1,2,3,4,5,6,7,8,9) ";
		sql += "     AND B.\"DrawdownDate\" <= :TBSDYF ";
		sql += " ) ";
		sql += " SELECT S.\"DetailSeq\"";
		sql += "       ,SUM(S.\"Counts\")      AS \"Counts\"";
		sql += "       ,SUM(S.\"LoanBalance\") AS \"Amt\"";
		sql += " FROM (  SELECT CASE";
		sql += "                  WHEN D.\"AcctCode\" = '310' AND C.\"IsRelated\" = 'Y'          THEN 1";
		sql += "                  WHEN D.\"AcctCode\" = '310' AND C.\"EntCode\" IN ('1')         THEN 2"; // EntCode = 2
																											// 為企金自然人,算在個人戶
		sql += "                  WHEN D.\"AcctCode\" = '310'                                    THEN 3";
		sql += "                  WHEN D.\"AcctCode\" = '320' AND C.\"IsRelated\" = 'Y'          THEN 4";
		sql += "                  WHEN D.\"AcctCode\" = '320' AND C.\"EntCode\" IN ('1')         THEN 5"; // EntCode = 2
																											// 為企金自然人,算在個人戶
		sql += "                  WHEN D.\"AcctCode\" = '320'                                    THEN 6";
		sql += "                  WHEN D.\"AcctCode\" = '330' AND C.\"IsRelated\" = 'Y'          THEN 7";
		sql += "                  WHEN D.\"AcctCode\" = '330' AND C.\"EntCode\" IN ('1')         THEN 8"; // EntCode = 2
																											// 為企金自然人,算在個人戶
		sql += "                  WHEN D.\"AcctCode\" = '330'                                    THEN 9";
		sql += "                  WHEN D.\"AcctCode\" = '990' AND D.\"FacAcctCode\" <> '340'     THEN 10";
		sql += "                  WHEN D.\"AcctCode\" = '340'                                    THEN 11";
		sql += "                  WHEN D.\"AcctCode\" = '990' AND D.\"FacAcctCode\" = '340'      THEN 12";
		sql += "                ELSE 99 END AS \"DetailSeq\"";
		sql += "               ,1 AS \"Counts\"";
		sql += "               ,D.\"LoanBalance\"";
		sql += "         FROM ( SELECT D.\"AcctCode\"";
		sql += "                      ,D.\"FacAcctCode\"";
		sql += "                      ,D.\"CustNo\"";
		sql += "                      ,D.\"FacmNo\"";
		sql += "                      ,SUM(D.\"LoanBalance\") AS \"LoanBalance\"";
		sql += "                FROM loanData D ";
		sql += "                WHERE D.\"LoanBalance\" > 0";
		sql += "                GROUP BY D.\"AcctCode\"";
		sql += "                        ,D.\"FacAcctCode\"";
		sql += "                        ,D.\"CustNo\"";
		sql += "                        ,D.\"FacmNo\"";
		sql += "              ) D";
		sql += "         LEFT JOIN ( SELECT S0.\"CustNo\"";
		sql += "                           ,S0.\"EntCode\"";
		sql += "                           ,CASE";
		sql += "                              WHEN S0.\"IsRelated\" > 0 THEN 'Y'";
		sql += "                            ELSE 'N' END AS \"IsRelated\"";
		sql += "                     FROM ( SELECT CM.\"CustNo\"";
		sql += "                                  ,NVL(CM.\"EntCode\",'0') AS \"EntCode\"";
//		sql += "                                  ,SUM(TO_NUMBER(NVL(BRS.\"LAW001\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRS.\"LAW005\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRC.\"LAW001\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRC.\"LAW005\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRF.\"LAW001\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRF.\"LAW005\",'0'))";
//		sql += "                                      ) AS \"IsRelated\"";
		sql += "                                  ,COUNT(S.\"StaffId\") AS \"IsRelated\"";
		sql += "                            FROM \"CustMain\" CM";
//		sql += "                            LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
//		sql += "                            LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
//		sql += "                            LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                            LEFT JOIN \"StakeholdersStaff\" S ON S.\"StaffId\" = CM.\"CustId\"";
		sql += "                            WHERE CM.\"CustNo\" > 0";
		sql += "                            GROUP BY CM.\"CustNo\",NVL(CM.\"EntCode\",'0')";
		sql += "                          ) S0";
		sql += "                   ) C ON C.\"CustNo\" = D.\"CustNo\"";
		sql += "         LEFT JOIN ( SELECT A0.\"AcctCode\"";
		sql += "                           ,A0.\"CustNo\"";
		sql += "                           ,A0.\"FacmNo\"";
		sql += "                           ,A0.\"AcSubBookCode\"";
		sql += "                           ,ROW_NUMBER() OVER (PARTITION BY A0.\"CustNo\"";
		sql += "                                                           ,A0.\"FacmNo\"";
		sql += "                                               ORDER BY A0.\"AcctCode\") AS \"Seq\"";
		sql += "                     FROM loanData D0";
		sql += "                     LEFT JOIN \"AcReceivable\" A0 ON A0.\"AcctCode\" = D0.\"AcctCode\"";
		sql += "                                                  AND A0.\"CustNo\"   = D0.\"CustNo\"";
		sql += "                                                  AND A0.\"FacmNo\"   = D0.\"FacmNo\"";
		sql += "                                                  AND SUBSTR(A0.\"RvNo\",0,3) = LPAD(D0.\"BormNo\",3,'0')";
		sql += "                     WHERE D0.\"LoanBalance\" > 0";
		sql += "                       AND A0.\"AcctCode\" IN ('310','320','330','340','990')";
		sql += "                   ) A ON A.\"AcctCode\" = D.\"AcctCode\"";
		sql += "                      AND A.\"CustNo\"   = D.\"CustNo\"";
		sql += "                      AND A.\"FacmNo\"   = D.\"FacmNo\"";
		sql += "                      AND A.\"Seq\"      = 1";
		sql += "         WHERE D.\"LoanBalance\" > 0";
		sql += condition(rptType);
		sql += "         UNION ALL SELECT 1,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 2,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 3,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 4,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 5,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 6,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 7,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 8,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 9,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 10,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 11,0,0 FROM DUAL";
		sql += "         UNION ALL SELECT 12,0,0 FROM DUAL";
		sql += "       ) S";
		sql += " WHERE S.\"DetailSeq\" <> 99";
		sql += " GROUP BY S.\"DetailSeq\"";
		sql += " ORDER BY S.\"DetailSeq\"";
		;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (rptType < 9) {
			query.setParameter("acSubBookCode", acSubBookCode);
		}
		query.setParameter("TBSDYF", iENTDAY);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll_SubBookCodes(TitaVo titaVo) throws Exception {
		this.info("LD008 findAll_SubBookCodes initiated");

		String sql = "";
		sql += " SELECT \"Code\" F0 ";
		sql += "       ,\"Item\" F1 ";
		sql += " FROM \"CdCode\" ";
		sql += " WHERE \"DefCode\" = 'AcSubBookCode' ";
		sql += " UNION ALL ";
		sql += " SELECT '000' F0 ";
		sql += "       ,n'傳統帳冊A' F1 ";
		sql += " FROM DUAL ";
		sql += " UNION ALL ";
		sql += " SELECT '%' F0 ";
		sql += "       ,n'全部' F1 ";
		sql += " FROM DUAL ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll_related(int rptType, String acSubBookCode, TitaVo titaVo)
			throws Exception {
		this.info("LD008 findAll rptType = " + rptType + " , acSubBookCode = " + acSubBookCode);

		int iENTDAY = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;

		String sql = " ";
		sql += " WITH loanData AS ( ";
		sql += "   SELECT B.\"CustNo\" ";
		sql += "        , B.\"FacmNo\" ";
		sql += "        , B.\"BormNo\" ";
		sql += "        , CASE ";
		sql += "            WHEN B.\"Status\" IN (2,5,6,7,8,9) ";
		sql += "            THEN NVL(O.\"AcctCode\",'   ') ";
		sql += "          ELSE F.\"AcctCode\" END         AS \"AcctCode\" ";
		sql += "        , F.\"AcctCode\"                  AS \"FacAcctCode\" ";
		sql += "        , CASE  ";
		sql += "            WHEN B.\"Status\" IN (0,4)  ";
		sql += "            THEN B.\"LoanBal\" ";
		sql += "            WHEN B.\"Status\" IN (2,7)   ";
		sql += "            THEN NVL(O.\"OvduBal\",0) ";
		sql += "          ELSE 0 END                      AS \"LoanBalance\"  ";
		sql += "   FROM \"LoanBorMain\" B ";
		sql += "   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = B.\"CustNo\" ";
		sql += "                          AND F.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "   LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = B.\"CustNo\" ";
		sql += "                              AND O.\"FacmNo\" = B.\"FacmNo\" ";
		sql += "                              AND O.\"BormNo\" = B.\"BormNo\" ";
		sql += "                              AND O.\"OvduNo\" = B.\"LastOvduNo\" ";
		sql += "                              AND B.\"Status\" IN (2,5,6,7,8,9) ";
		sql += "   WHERE B.\"Status\" in (0,1,2,3,4,5,6,7,8,9) ";
		sql += "     AND B.\"DrawdownDate\" <= :TBSDYF ";
		sql += " ) ";
		sql += " SELECT S.\"DetailSeq\" ";
		sql += "       ,SUM(S.\"Counts\")      AS \"Counts\" ";
		sql += "       ,SUM(S.\"LoanBalance\") AS \"Amt\" ";
		sql += " FROM (  SELECT CASE WHEN C.\"IsRelated\" = 'Y' ";
		sql += "                     THEN CASE WHEN D.\"AcctCode\" = '310' AND C.\"EntCode\" IN ('1')         THEN 1 ";
		sql += "                               WHEN D.\"AcctCode\" = '310'                                    THEN 2 ";
		sql += " 						       WHEN D.\"AcctCode\" = '320' AND C.\"EntCode\" IN ('1')         THEN 3 ";
		sql += "                               WHEN D.\"AcctCode\" = '320'                                    THEN 4 ";
		sql += " 						       WHEN D.\"AcctCode\" = '330' AND C.\"EntCode\" IN ('1')         THEN 5 ";
		sql += "                               WHEN D.\"AcctCode\" = '330'                                    THEN 6 ";
		sql += "                               WHEN D.\"AcctCode\" = '990' AND D.\"FacAcctCode\" <> '340'     THEN 7 ";
		sql += "                               WHEN D.\"AcctCode\" = '340'                                    THEN 8 ";
		sql += "                               WHEN D.\"AcctCode\" = '990' AND D.\"FacAcctCode\" = '340'      THEN 9 ";
		sql += "                          ELSE 990 END ";
		sql += " 			   	     ELSE 99 END AS \"DetailSeq\" ";
		sql += "               ,1 AS \"Counts\" ";
		sql += "               ,D.\"LoanBalance\" ";
		sql += "         FROM ( SELECT D.\"AcctCode\" ";
		sql += "                      ,D.\"FacAcctCode\" ";
		sql += "                      ,D.\"CustNo\" ";
		sql += "                      ,D.\"FacmNo\" ";
		sql += "                      ,SUM(D.\"LoanBalance\") AS \"LoanBalance\" ";
		sql += "                FROM loanData D  ";
		sql += "                WHERE D.\"LoanBalance\" > 0 ";
		sql += "                GROUP BY D.\"AcctCode\" ";
		sql += "                        ,D.\"FacAcctCode\" ";
		sql += "                        ,D.\"CustNo\" ";
		sql += "                        ,D.\"FacmNo\" ";
		sql += "              ) D ";
		sql += "         LEFT JOIN ( SELECT S0.\"CustNo\" ";
		sql += "                           ,S0.\"EntCode\" ";
		sql += "                           ,CASE WHEN S0.\"IsRelated\" > 0 ";
		sql += "                                 THEN 'Y' ";
		sql += "                            ELSE 'N' END AS \"IsRelated\" ";
		sql += "                     FROM ( SELECT CM.\"CustNo\" ";
		sql += "                                  ,NVL(CM.\"EntCode\",'0') AS \"EntCode\" ";
//		sql += "                                  ,SUM(TO_NUMBER(NVL(BRS.\"LAW001\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRS.\"LAW005\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRC.\"LAW001\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRC.\"LAW005\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRF.\"LAW001\",'0'))";
//		sql += "                                       + TO_NUMBER(NVL(BRF.\"LAW005\",'0'))";
//		sql += "                                      ) AS \"IsRelated\"";
		sql += "                                  ,COUNT(S.\"StaffId\") AS \"IsRelated\"";
		sql += "                            FROM \"CustMain\" CM";
//		sql += "                            LEFT JOIN \"BankRelationSelf\" BRS ON BRS.\"CustId\" = CM.\"CustId\"";
//		sql += "                            LEFT JOIN \"BankRelationCompany\" BRC ON BRC.\"CompanyId\" = CM.\"CustId\"";
//		sql += "                            LEFT JOIN \"BankRelationFamily\" BRF ON BRF.\"RelationId\" = CM.\"CustId\"";
		sql += "                            LEFT JOIN \"StakeholdersStaff\" S ON S.\"StaffId\" = CM.\"CustId\"";
		sql += "                            WHERE CM.\"CustNo\" > 0 ";
		sql += "                            GROUP BY CM.\"CustNo\",NVL(CM.\"EntCode\",'0')  ";
		sql += "                          ) S0 ";
		sql += "                   ) C ON C.\"CustNo\" = D.\"CustNo\" ";
		sql += "         LEFT JOIN ( SELECT A0.\"AcctCode\" ";
		sql += "                           ,A0.\"CustNo\" ";
		sql += "                           ,A0.\"FacmNo\" ";
		sql += "                           ,A0.\"AcSubBookCode\" ";
		sql += "                           ,ROW_NUMBER() OVER (PARTITION BY A0.\"CustNo\" ";
		sql += "                                                           ,A0.\"FacmNo\" ";
		sql += "                                               ORDER BY A0.\"AcctCode\") AS \"Seq\" ";
		sql += "                     FROM loanData D0 ";
		sql += "                     LEFT JOIN \"AcReceivable\" A0 ON A0.\"AcctCode\" = D0.\"AcctCode\" ";
		sql += "                                                  AND A0.\"CustNo\"   = D0.\"CustNo\" ";
		sql += "                                                  AND A0.\"FacmNo\"   = D0.\"FacmNo\" ";
		sql += "                                                  AND SUBSTR(A0.\"RvNo\",0,3) = LPAD(D0.\"BormNo\",3,'0') ";
		sql += "                     WHERE D0.\"LoanBalance\" > 0 ";
		sql += "                       AND A0.\"AcctCode\" IN ('310','320','330','340','990') ";
		sql += "                   ) A ON A.\"AcctCode\" = D.\"AcctCode\" ";
		sql += "                      AND A.\"CustNo\"   = D.\"CustNo\" ";
		sql += "                      AND A.\"FacmNo\"   = D.\"FacmNo\" ";
		sql += "                      AND A.\"Seq\"      = 1 ";
		sql += "         WHERE D.\"LoanBalance\" > 0 ";
		sql += "           AND NVL(A.\"AcctCode\",'   ') IN ('310','320','330','340','990') ";
		sql += condition(rptType);
		sql += "         UNION ALL SELECT 1,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 2,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 3,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 4,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 5,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 6,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 7,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 8,0,0 FROM DUAL ";
		sql += "         UNION ALL SELECT 9,0,0 FROM DUAL ";
		sql += "       ) S ";
		sql += " WHERE S.\"DetailSeq\" <> 99 ";
		sql += " GROUP BY S.\"DetailSeq\" ";
		sql += " ORDER BY S.\"DetailSeq\" ";
		sql += "  ";
		;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (rptType < 9) {
			query.setParameter("acSubBookCode", acSubBookCode);
		}
		query.setParameter("TBSDYF", iENTDAY);

		return this.convertToMap(query);
	}

	private String condition(int rptType) {
		String result = "";

		if (rptType < 9) {
			result += " AND NVL(A.\"AcSubBookCode\",'   ') LIKE :acSubBookCode";
		}

		return result;
	}

}