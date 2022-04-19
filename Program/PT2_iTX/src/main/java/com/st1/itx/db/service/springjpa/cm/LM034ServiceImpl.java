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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
/* 逾期放款明細 */
public class LM034ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private DateUtil dDateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

//		int entdy = titaVo.getEntDyI() + 19110000;

//		dDateUtil.setDate_1(entdy);
//
//		dDateUtil.setYears(-3);
//
//		// 三年前
//		String threeYearsAgo = String.valueOf(dDateUtil.getCalenderDay());
//
//		String thisYearMonth = String.valueOf(entdy / 100);
		String endDate = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000));
		String startDate = String.valueOf(Integer.parseInt(endDate) / 100) + "01";
		this.info("lM034.findAll ");
		this.info("startDate = " + startDate + ", endDate = " + endDate);

		String sql = "SELECT A.\"ApplDate\"";
		sql += "            ,A.\"ApproveDate\"";
		sql += "            ,F.\"CustNo\"";
		sql += "            ,F.\"FacmNo\"";
		sql += "            ,F.\"LineAmt\"";
		sql += "            ,F.\"UtilAmt\"";
		sql += "            ,F.\"UtilBal\"";
		sql += "            ,M.\"YearMonth\"";
		sql += "            ,M.\"OvduDays\"";
		sql += "            ,M.\"PrevIntDate\"";
		sql += "            ,M.\"NextIntDate\"";
		sql += "            ,CASE WHEN M.\"YearMonth\" IS NULL THEN 0";
		sql += "             ELSE M.\"PrinBalance\" END \"UnpaidAmt\"";
		sql += "            ,F.\"DepartmentCode\"";
		sql += "            ,F.\"PieceCode\"";
		sql += "      FROM \"FacCaseAppl\" A";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"ApplNo\" = A.\"ApplNo\"";
		sql += "      LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                   AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "                                   AND M.\"OvduDays\" > 0";
		sql += "      WHERE A.\"ApproveDate\" BETWEEN :StartDate AND :EndDate";
		sql += "      ORDER BY NVL(M.\"YearMonth\", 0), F.\"FacmNo\"";
//		String sql = "SELECT A.\"ApplDate\"               F0";
//		sql += "            ,A.\"ApproveDate\"            F1";
//		sql += "            ,I.\"CustNo\"                 F2";
//		sql += "            ,I.\"FacmNo\"                 F3";
//		sql += "            ,F.\"LineAmt\"                F4";
//		sql += "            ,F.\"UtilAmt\"                F5";
//		sql += "            ,F.\"UtilBal\"                F6";
//		sql += "            ,TRUNC(I.\"EntryDate\" / 100) F7";
//		sql += "            ,0                            F8";
//		sql += "            ,I.\"DueDate\"                F9";
//		sql += "            ,M.\"NextIntDate\"            F10";
//		sql += "            ,I.\"UnpaidAmt\"              F11";
//		sql += "            ,M.\"EntCode\"                F12";
//		sql += "            ,F.\"PieceCode\"              F13";
//		sql += "      FROM (SELECT \"CustNo\"";
//		sql += "                  ,\"FacmNo\"";
//		sql += "                  ,\"EntryDate\"";
//		sql += "                  ,\"DueDate\"";
//		sql += "                  ,\"UnpaidAmt\"";
//		sql += "                  ,\"SEQ\"";
//		sql += "            FROM (SELECT \"CustNo\"";
//		sql += "                        ,\"FacmNo\"";
//		sql += "                        ,\"EntryDate\"";
//		sql += "                        ,\"DueDate\"";
//		sql += "                        ,\"UnpaidAmt\"";
//		sql += "                        ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\"";
//		sql += "                                                        ,\"FacmNo\"";
//		sql += "                                            ORDER BY \"EntryDate\") AS SEQ";
//		sql += "                  FROM (SELECT T.\"CustNo\"";
//		sql += "                              ,T.\"FacmNo\"";
//		sql += "                              ,DECODE(T.\"EntryDate\" ,0 ,T.\"AcDate\" ,T.\"EntryDate\") \"EntryDate\"";
//		sql += "                              ,T.\"DueDate\"";
//		sql += "                              ,T.\"Principal\" + T.\"Interest\" \"UnpaidAmt\"";
//		sql += "                         FROM \"LoanBorTx\" T";
//		sql += "                         WHERE T.\"AcDate\" >= :threeYearsAgo";
//		sql += "                           AND T.\"Principal\" + T.\"Interest\" > 0";
//		sql += "                           AND T.\"DueDate\" > 0";
//		sql += "                           AND TO_DATE(TO_CHAR(DECODE(T.\"EntryDate\",0 ,T.\"AcDate\",T.\"EntryDate\")),'YYYYMMDD')";
//		sql += "                              - TO_DATE(TO_CHAR(T.\"DueDate\"),'YYYYMMDD') >= 90";
//		sql += "                         UNION ALL";
//		sql += "                         SELECT M.\"CustNo\"";
//		sql += "                               ,M.\"FacmNo\"";
//		sql += "                               ,0 \"EntryDate\"";
//		sql += "                               ,M.\"NextIntDate\" \"DueDate\"";
//		sql += "                               ,M.\"UnpaidPrincipal\" + M.\"UnpaidInterest\" \"UnpaidAmt\"";
//		sql += "                         FROM \"MonthlyFacBal\" M";
//		sql += "                         WHERE M.\"YearMonth\" = :thisYearMonth";
//		sql += "                           AND M.\"OvduDays\" >= 90";
//		sql += "                       ) I ";
//		sql += "                 ) I ";
//		sql += "            WHERE I.\"SEQ\" = 1";
//		sql += "           ) I";
//		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = I.\"CustNo\"";
//		sql += "                             AND F.\"FacmNo\" = I.\"FacmNo\"";
//		sql += "      LEFT JOIN \"FacCaseAppl\" A ON A.\"ApplNo\" = F.\"ApplNo\"";
//		sql += "      LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :thisYearMonth";
//		sql += "                                   AND M.\"CustNo\"    = I.\"CustNo\"";
//		sql += "                                   AND M.\"FacmNo\"    = I.\"FacmNo\"";
//		sql += "      WHERE F.\"PieceCode\" NOT IN ('3', '5', '7', 'C', 'E')  ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("StartDate", startDate);
		query.setParameter("EndDate", endDate);
		return this.convertToMap(query.getResultList());
	}

}