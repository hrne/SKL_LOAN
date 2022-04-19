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

@Service
@Repository
public class L9715ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {
		// 產表條件：月底產表，往前回推18個月，初貸日包含在內的案件
		// ex:202108月底產表，初貸日的範圍為20200301~20210831

		int approDate = 0;
		int ovduTermStart = 0;
		int ovduTermEnd = 999;
		int ovduDaysStart = 0;
		int ovduDaysEnd = 999999;

		this.info("L9715ServiceImpl APPRO_DAY = " + titaVo.getParam("APPRO_DAY"));
		this.info("L9715ServiceImpl UNPAY_TERM_ST = " + titaVo.getParam("UNPAY_TERM_ST"));
		this.info("L9715ServiceImpl UNPAY_TERM_ED = " + titaVo.getParam("UNPAY_TERM_ED"));
		this.info("L9715ServiceImpl UNPAY_DAY_ST = " + titaVo.getParam("UNPAY_DAY_ST"));
		this.info("L9715ServiceImpl UNPAY_DAY_ED = " + titaVo.getParam("UNPAY_DAY_ED"));

		approDate = Integer.parseInt(titaVo.getParam("APPRO_DAY")) + 19110000;
		int unpayTermSt = Integer.parseInt(titaVo.getParam("UNPAY_TERM_ST"));
		int unpayTermEd = Integer.parseInt(titaVo.getParam("UNPAY_TERM_ED"));
		int unpayDaySt = Integer.parseInt(titaVo.getParam("UNPAY_DAY_ST"));
		int unpayDayEd = Integer.parseInt(titaVo.getParam("UNPAY_DAY_ED"));

		if (unpayTermSt > 0) {
			ovduTermStart = unpayTermSt;
			ovduTermEnd = unpayTermEd;
		} else if (unpayDaySt > 0) {
			ovduDaysStart = unpayDaySt;
			ovduDaysEnd = unpayDayEd;
		}

		this.info("L9715ServiceImpl queryOvdu");
		this.info("L9715ServiceImpl approDate = " + approDate);
		this.info("L9715ServiceImpl ovduTermStart = " + ovduTermStart);
		this.info("L9715ServiceImpl ovduTermEnd = " + ovduTermEnd);
		this.info("L9715ServiceImpl ovduDaysStart = " + ovduDaysStart);
		this.info("L9715ServiceImpl ovduDaysEnd = " + ovduDaysEnd);

		String sql = " ";
		sql += " SELECT \"Fn_GetEmpName\"(FAC.\"BusinessOfficer\",1) ";
		sql += "                                AS F0 "; // -- 經辦
		sql += "      , CITY.\"CityItem\"         AS F1 "; // -- 擔保品地區別
		sql += "      , \"Fn_GetCdCode\"('RepayCode', LPAD(FAC.\"RepayCode\",2,'0')) ";
		sql += "                                AS F2 "; // -- 繳款方式
		sql += "      , COLL.\"CustNo\"           AS F3 "; // -- 戶號
		sql += "      , COLL.\"FacmNo\"           AS F4 "; // -- 額度
		sql += "      , SUBSTR(CM.\"CustName\",0,10) ";
		sql += "                                  AS F5 "; // -- 戶名
		sql += "      , FAC.\"FirstDrawdownDate\" AS F6 "; // -- 初貸日
		sql += "      , COLL.\"PrinBalance\"      AS F7 "; // -- 本金餘額
		sql += "      , NVL(LBM.\"StoreRate\",0)  AS F8 "; // -- 利率
		sql += "      , COLL.\"PrevIntDate\"      AS F9 "; // -- 繳息迄日
		sql += "      , COLL.\"NextIntDate\"      AS F10 "; // -- 最近應繳日
		sql += "      , COLL.\"OvduDays\"         AS F11 "; // -- 逾期日數
		sql += "      , NVL(CTN.\"LiaisonName\",N' ') ";
		sql += "                                AS F12 "; // -- 聯絡人
		sql += "      , NVL(CTN.\"TelNoFirst\",' ') ";
		sql += "                                AS F13 "; // -- 電話
		sql += "      , NVL(CTN.\"TelNos\",' ')   AS F14 "; // -- 其他電話
		sql += " FROM \"CollList\" COLL ";
		sql += " LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = COLL.\"CustNo\" ";
		sql += "                        AND FAC.\"FacmNo\" = COLL.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = COLL.\"CustNo\" ";
		sql += " LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                     AND CF.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += "                     AND CF.\"MainFlag\" = 'Y' ";
		sql += " LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                      AND CL.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                      AND CL.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = CL.\"CityCode\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                  , \"FacmNo\" ";
		sql += "                  , \"StoreRate\" ";
		sql += "                  , ROW_NUMBER() OVER (PARTITION BY \"CustNo\",\"FacmNo\" ORDER BY \"BormNo\") AS SEQ ";
		sql += "             FROM \"LoanBorMain\" ";
		sql += "             WHERE \"LoanBal\" > 0 ";
		sql += "           ) LBM ON LBM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                AND LBM.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += "                AND LBM.SEQ = 1 ";
		sql += " LEFT JOIN ( SELECT CM.\"CustNo\" ";
		sql += "                  , CTN.\"LiaisonName\" ";
		sql += "                  , CTN.\"TelNoFirst\" ";
		sql += "                  , CTN.\"TelNos\" ";
		sql += "             FROM \"CustMain\" CM ";
		sql += "             LEFT JOIN ( SELECT TMP.\"CustUKey\" ";
		sql += "                              , MAX(CASE ";
		sql += "                                      WHEN TMP.\"Seq\" = 1 ";
		sql += "                                      THEN TMP.\"LiaisonName\" ";
		sql += "                                    ELSE N' ' END ";
		sql += "                                   ) AS \"LiaisonName\" ";
		sql += "                              , MAX(CASE ";
		sql += "                                      WHEN TMP.\"Seq\" = 1 ";
		sql += "                                      THEN TMP.\"TelNo\" ";
		sql += "                                    ELSE ' ' END ";
		sql += "                                   ) AS \"TelNoFirst\" ";
		sql += "                              , LISTAGG(TMP.\"TelNo\",' ') WITHIN GROUP (ORDER BY TMP.\"Seq\") AS \"TelNos\" ";
		sql += "                         FROM ( SELECT \"CustUKey\" ";
		sql += "                                     , \"LiaisonName\" ";
		sql += "                                     , CASE ";
		sql += "                                         WHEN \"TelArea\" IS NOT NULL THEN \"TelArea\" || '-' "; // --
																													// 電話區碼
		sql += "                                       ELSE '' END ";
		sql += "                                       || CASE ";
		sql += "                                            WHEN \"TelNo\" IS NOT NULL THEN \"TelNo\""; // -- 電話號碼
		sql += "                                          ELSE '' END ";
		sql += "                                       || CASE ";
		sql += "                                            WHEN \"TelExt\" IS NOT NULL THEN '-' || \"TelExt\"  "; // --
																													// 分機號碼
		sql += "                                          ELSE '' END AS \"TelNo\" ";
		sql += "                                     , ROW_NUMBER() OVER (PARTITION BY \"CustUKey\" ";
		sql += "                                                          ORDER BY \"RelationCode\" ";
		sql += "                                                                 , \"TelTypeCode\") AS \"Seq\" ";
		sql += "                                FROM \"CustTelNo\" ";
		sql += "                                WHERE \"Enable\" = 'Y' ";
		sql += "                              ) TMP ";
		sql += "                         WHERE TMP.\"Seq\" <= 4 ";
		sql += "                         GROUP BY TMP.\"CustUKey\" ";
		sql += "                       ) CTN ON CTN.\"CustUKey\" = CM.\"CustUKey\" ";
		sql += "             WHERE CM.\"CustNo\" > 0 ";
		sql += "           ) CTN ON CTN.\"CustNo\" = COLL.\"CustNo\" ";
		sql += " WHERE COLL.\"CaseCode\" = 1 ";
		sql += "   AND COLL.\"Status\" IN (0,2,4,6) ";
		sql += "   AND FAC.\"FirstDrawdownDate\" >= :approDate ";
		sql += "   AND COLL.\"OvduTerm\" >= :ovduTermStart ";
		sql += "   AND COLL.\"OvduTerm\" <= :ovduTermEnd ";
		sql += "   AND COLL.\"OvduDays\" >= :ovduDaysStart ";
		sql += "   AND COLL.\"OvduDays\" <= :ovduDaysEnd ";
		sql += " ORDER BY F0 ";
		sql += "        , F3 ";
		sql += "        , F4 ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		query.setParameter("approDate", approDate);
		query.setParameter("ovduTermStart", ovduTermStart);
		query.setParameter("ovduTermEnd", ovduTermEnd);
		query.setParameter("ovduDaysStart", ovduDaysStart);
		query.setParameter("ovduDaysEnd", ovduDaysEnd);

		return this.convertToMap(query);
	}

}