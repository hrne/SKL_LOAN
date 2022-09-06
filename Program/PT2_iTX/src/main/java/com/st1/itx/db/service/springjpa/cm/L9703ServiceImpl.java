package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9703ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryForDetail(TitaVo titaVo) throws LogicException {

		int icustno = parse.stringToInteger(titaVo.getParam("CustNo"));
		int ifacmno = parse.stringToInteger(titaVo.getParam("FacmNo"));
		String unpay = titaVo.getParam("UnpaidCond"); // 1-逾期期數 2-逾期日數
		;
		int st;
		int ed;
		if ("1".equals(unpay)) {
			st = parse.stringToInteger(titaVo.getParam("UnpaidTermSt"));
			ed = parse.stringToInteger(titaVo.getParam("UnpaidTermEd"));
		} else {
			st = parse.stringToInteger(titaVo.getParam("UnpaidDaySt"));
			ed = parse.stringToInteger(titaVo.getParam("UnpaidDayEd"));
		}
		String repay = titaVo.getParam("RepayType");
		String custType = titaVo.getParam("CustType");
		int prinBalance = titaVo.containsKey("PrinBalance") ? parse.stringToInteger(titaVo.getParam("PrinBalance")) : 0;
		int acdate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int payIntDateSt = parse.stringToInteger(titaVo.getParam("PayIntDateSt"));
		if (payIntDateSt > 0) {
			payIntDateSt = payIntDateSt + 19110000;
		}
		int payIntDateEd = parse.stringToInteger(titaVo.getParam("PayIntDateEd"));
		if (payIntDateEd > 0) {
			payIntDateEd = payIntDateEd + 19110000;
		}

		this.info("L9703 queryForDetail");
		this.info("L9703 iCustno    = " + icustno);
		this.info("L9703 iFacmno    = " + ifacmno);
		this.info("L9703 UnpaidCond = " + unpay);
		this.info("L9703 st         = " + st);
		this.info("L9703 ed         = " + ed);
		this.info("L9703 repay      = " + repay);
		this.info("L9703 custType   = " + custType);
		this.info("L9703 prinBalance   = " + prinBalance);
		this.info("L9703 acdate     = " + acdate);
		this.info("L9703 payIntDateSt  = " + payIntDateSt);
		this.info("L9703 payIntDateEd  = " + payIntDateEd);

		String sql = "";
		sql += "SELECT CC.\"CityItem\" AS \"CityItem\"";
		sql += "            ,\"Fn_GetEmpName\"(CC.\"AccCollPsn\",1) AS F1";
		sql += "            ,D.\"CustNo\" AS \"CustNo\"";
		sql += "            ,D.\"FacmNo\" AS \"FacmNo\"";
		sql += "            ,D.\"CustName\" AS \"CustName\"";
		sql += "            ,D.\"FirstDrawdownDate\" AS \"FirstDrawdownDate\"";
		sql += "            ,D.\"PrinBalance\" AS \"PrinBalance\"";
		sql += "            ,D.\"PrevIntDate\" AS \"PrevIntDate\"";
		sql += "            ,D.\"NextIntDate\" AS \"NextIntDate\"";
		sql += "            ,TO_DATE(:acdate,'YYYYMMDD')  - TO_DATE(D.\"NextIntDate\",'YYYYMMDD')  AS \"OvduDays\"";
		sql += "            ,NVL(T1.\"LiaisonName\",' ') AS \"LiaisonName\"";
		sql += "            ,\"Fn_GetTelNo\"(D.\"CustUKey\",'02',1)  AS F11";
		sql += "            ,\"Fn_GetTelNo\"(D.\"CustUKey\",'03',1)  AS F12";
		sql += "            ,\"Fn_GetCdCode\"('RepayCode',LPAD(D.\"RepayCode\",2,'0')) AS F13";
		sql += "      FROM (SELECT L.\"CustNo\"";
		sql += "                  ,L.\"FacmNo\"";
		sql += "                  ,C.\"CustName\"";
		sql += "                  ,F.\"FirstDrawdownDate\"";
		sql += "                  ,F.\"UtilAmt\"             AS \"PrinBalance\"";
		sql += "                  ,MIN(L.\"PrevPayIntDate\") AS \"PrevIntDate\"";
		sql += "                  ,MIN(L.\"NextPayIntDate\") AS \"NextIntDate\"";
		sql += "                  ,F.\"RepayCode\"";
		sql += "                  ,C.\"CustUKey\"";
		sql += "            FROM \"LoanBorMain\" L";
		sql += "            LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "            LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "                                   AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "            WHERE L.\"Status\" = 0";
		sql += "             AND  L.\"SpecificDd\" > 0";
		sql += queryCondition(icustno, ifacmno, unpay, repay, custType, prinBalance, payIntDateSt, payIntDateEd, acdate);
		sql += "            GROUP BY L.\"CustNo\", L.\"FacmNo\", ";
		sql += "                     F.\"FirstDrawdownDate\", F.\"UtilAmt\", F.\"RepayCode\", C.\"CustName\", C.\"CustUKey\" ";
		sql += "           ) D";
		sql += "      LEFT JOIN \"ClFac\" F ON F.\"CustNo\" = D.\"CustNo\"";
		sql += "                           AND F.\"FacmNo\" = D.\"FacmNo\"";
		sql += "                           AND F.\"MainFlag\" = 'Y'";
		sql += "      LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = F.\"ClCode1\"";
		sql += "                             AND Cl.\"ClCode2\" = F.\"ClCode2\"";
		sql += "                             AND Cl.\"ClNo\"    = F.\"ClNo\"";
		sql += "      LEFT JOIN \"CdCity\" CC ON CC.\"CityCode\" = Cl.\"CityCode\"";
		sql += "      LEFT JOIN (                              ";
		sql += "          SELECT CTN.\"CustUKey\" ";
		sql += "               , CASE";
		sql += "                   WHEN CTN.\"RelationCode\" = '00' ";
		sql += "                   THEN CM.\"CustName\" ";
		sql += "                 ELSE CTN.\"LiaisonName\" END AS \"LiaisonName\" ";
		sql += "               , ROW_NUMBER() OVER (PARTITION BY CTN.\"CustUKey\" ORDER BY CTN.\"RelationCode\") AS SEQ ";
		sql += "          FROM \"CustTelNo\" CTN";
		sql += "          LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = CTN.\"CustUKey\" ";
		sql += "          WHERE CTN.\"TelTypeCode\" = '02' ";
		sql += "            AND CTN.\"Enable\" = 'Y' ";
		sql += "      )    T1 ON T1.\"CustUKey\" = D.\"CustUKey\" ";
		sql += "             AND T1.SEQ = 1                    ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (icustno > 0) {
			query.setParameter("icustno", icustno);
			if (ifacmno > 0) {
				query.setParameter("ifacmno", ifacmno);
			}
		}
		query.setParameter("st", st);
		query.setParameter("ed", ed);
		if ("1".equals(repay) || "2".equals(repay) || "3".equals(repay) || "4".equals(repay)) {
			query.setParameter("repay", repay);
		}
		query.setParameter("prinBalance", prinBalance);
		query.setParameter("acdate", acdate);
		query.setParameter("iSpecificDays", getSpecificDays(payIntDateSt, payIntDateEd));

		return this.convertToMap(query);
	}

	private String queryCondition(int icustno, int ifacmno, String unpay, String repay, String custType, int prinBalance, int payIntDateSt, int payIntDateEd, int acdate) throws LogicException {

		String condition = " ";

		// 有輸入戶號
		if (icustno > 0) {
			condition += "  AND  F.\"CustNo\" = :icustno";
			if (ifacmno > 0) {
				condition += "  AND  F.\"FacmNo\" = :ifacmno";
			}
		}
		// 逾期數
		if ("1".equals(unpay)) {
			condition += " AND CASE L.\"SpecificDd\" ";
			condition += "     WHEN  0 ";
			condition += "      THEN TRUNC(MONTHS_BETWEEN(TO_DATE(:acdate,'YYYYMMDD'), TO_DATE(L.\"NextPayIntDate\", 'YYYYMMDD'))) ";
			condition += "     ELSE    ";
			condition += "           TRUNC(MONTHS_BETWEEN(TO_DATE(:acdate,'YYYYMMDD'), TO_DATE(19110100 + L.\"SpecificDd\", 'YYYYMMDD'))) ";
			condition += "         - TRUNC(MONTHS_BETWEEN(TO_DATE(L.\"NextPayIntDate\",'YYYYMMDD'), TO_DATE(19110100 + L.\"SpecificDd\", 'YYYYMMDD'))) ";
			condition += "     END BETWEEN :st AND :ed ";
		} else {
			condition += "  AND (TO_DATE(:acdate,'YYYYMMDD')  - TO_DATE(L.\"NextPayIntDate\",'YYYYMMDD'))  BETWEEN :st AND :ed ";
		}
		// repay == 0 時不篩選,
		// repay == 9 時 篩選 RepayCode = 5,6,7,8
		// else 篩選 RepayCode = repay
		if ("1".equals(repay) || "2".equals(repay) || "3".equals(repay) || "4".equals(repay)) {
			// 繳款方式1-4
			condition += "  AND F.\"RepayCode\" = :repay";
		} else if ("9".equals(repay)) {
			// 繳款方式 9
			condition += "  AND F.\"RepayCode\" IN (5, 6, 7, 8) ";
		} // else

		/*
		 * titaVo.CustType 0:全部 1:自然人 2:法人
		 * 
		 * CustMain.EntCode 企金別 0:個金 1:企金 2:企金自然人
		 * 
		 * 若titaVo.CustType == 0 時,不篩選 若titaVo.CustType == 1 時,篩選CustMain.EntCode == 0 或
		 * 2 若titaVo.CustType == 2 時,篩選CustMain.EntCode == 1
		 */
		if ("1".equals(custType)) { // 戶別 1.自然人
			condition += "  AND  C.\"EntCode\" IN ('0', '2')";
		} else if ("2".equals(custType)) { // 戶別 2.法人
			condition += "  AND  C.\"EntCode\" =  '1'";
		}
		// 餘額
		condition += "  AND  F.\"UtilAmt\" >= :prinBalance";

		// 應繳日
		condition += "               and l.\"SpecificDd\" IN :iSpecificDays ";

		return condition;
	}

	// 扣款應繳日的應繳日(2碼)
//	List<Integer> iSpecificDays = new ArrayList<>();
	private List<Integer> getSpecificDays(int payIntDateSt, int payIntDateEd) throws LogicException {
		List<Integer> iSpecificDays = new ArrayList<>();
		// 最近應繳起止日: 20220228~20220302，iSpecificDays = [28,29,30,31,01,02]
		if (payIntDateSt > 0) {
			int startMonth = payIntDateSt / 100;
			int endMonth = payIntDateEd / 100;

			int startDay = payIntDateSt % 100;
			int endDay = payIntDateEd % 100;

			iSpecificDays.add(startDay);

			// 最近應繳起日 若為當月月底日(例如2/28),iSpecificDays需滾到31日
			if (isEndOfMonth(payIntDateSt)) {
				for (int i = startDay + 1; i <= 31; i++) {
					iSpecificDays.add(i);
				}
			}

			// 跨月情況
			if (startMonth < endMonth) {
				for (int i = startDay + 1; i <= 31; i++) {
					iSpecificDays.add(i);
				}
				for (int i = 1; i <= endDay; i++) {
					iSpecificDays.add(i);
				}
			} else {
				for (int i = startDay + 1; i <= endDay; i++) {
					iSpecificDays.add(i);
				}
			}
		}
		this.info("iSpecificDays=" + iSpecificDays);
		return iSpecificDays;
	}

	// 判斷是否為月底日
	private boolean isEndOfMonth(int date1) throws LogicException {

		int day1 = date1;

		// 若隔天就不同月份,則為月底日
		dateUtil.init();
		dateUtil.setDate_1(day1);
		dateUtil.setDays(1);
		dateUtil.getCalenderDay();

		int day2 = dateUtil.getDate_2Integer();

		int day1Month = day1 / 100;

		int day2Month = day2 / 100;

		// 回傳是否為月底日
		return day1Month != day2Month; // 若月份不同，則為月底日，回傳是
	}

	public List<Map<String, String>> queryForNotice(TitaVo titaVo) throws LogicException {
		int icustno = parse.stringToInteger(titaVo.getParam("CustNo"));
		int ifacmno = parse.stringToInteger(titaVo.getParam("FacmNo"));
		String unpay = titaVo.getParam("UnpaidCond"); // 1-逾期期數 2-逾期日數
		;
		int st;
		int ed;
		if ("1".equals(unpay)) {
			st = parse.stringToInteger(titaVo.getParam("UnpaidTermSt"));
			ed = parse.stringToInteger(titaVo.getParam("UnpaidTermEd"));
		} else {
			st = parse.stringToInteger(titaVo.getParam("UnpaidDaySt"));
			ed = parse.stringToInteger(titaVo.getParam("UnpaidDayEd"));
		}
		String repay = titaVo.getParam("RepayType");
		String custType = titaVo.getParam("CustType");
		int prinBalance = titaVo.containsKey("PrinBalance") ? parse.stringToInteger(titaVo.getParam("PrinBalance")) : 0;
		int acdate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int payIntDateSt = parse.stringToInteger(titaVo.getParam("PayIntDateSt"));
		if (payIntDateSt > 0) {
			payIntDateSt = payIntDateSt + 19110000;
		}
		int payIntDateEd = parse.stringToInteger(titaVo.getParam("PayIntDateEd"));
		if (payIntDateEd > 0) {
			payIntDateEd = payIntDateEd + 19110000;
		}

		this.info("L9703 queryForNotice");
		this.info("L9703 icustno    = " + icustno);
		this.info("L9703 ifacmno    = " + ifacmno);
		this.info("L9703 UnpaidCond = " + unpay);
		this.info("L9703 st         = " + st);
		this.info("L9703 ed         = " + ed);
		this.info("L9703 repay      = " + repay);
		this.info("L9703 custType   = " + custType);
		this.info("L9703 prinBalance   = " + prinBalance);
		this.info("L9703 acdate     = " + acdate);
		this.info("L9703 payIntDateSt  = " + payIntDateSt);
		this.info("L9703 payIntDateEd  = " + payIntDateEd);

		String tlrno = titaVo.getParam("TLRNO");

		String sql = " SELECT *";
		sql += "       FROM (SELECT ' ' F0";
		sql += "                   ,Cl.\"CityCode\"           AS \"CityCode\"";
		sql += "                   ,Ci.\"CityItem\"           AS \"CityItem\"";
		sql += "                   ,E.\"Fullname\"            AS \"Fullname\"";
		sql += "                   ,M.\"CustNo\"              AS \"CustNo\"";
		sql += "                   ,M.\"FacmNo\"              AS \"FacmNo\"";
		sql += "                   ,C.\"CustName\"            AS \"CustName\"";
		sql += "                   ,F.\"ApplNo\"              AS \"ApplNo\"";
		sql += "                   ,M.\"MaturityDate\"        AS \"MaturityDate\"";
		sql += "                   ,F.\"LineAmt\"             AS \"LineAmt\"";
		sql += "                   ,M.\"LoanBal\"             AS \"LoanBal\"";
		sql += "                   ,F.\"FirstDrawdownDate\"   AS \"FirstDrawdownDate\"";
		sql += "                   ,M.\"PrevPayIntDate\"      AS \"PrevPayIntDate\"";
		sql += "                   ,M.\"StoreRate\"           AS \"StoreRate\"";
		sql += "                   ,T.\"PhoneNo\"             AS \"PhoneNo\"";
		sql += "                   ,T.\"LiaisonName\"         AS \"LiaisonName\"";
		sql += "                   ,M.\"NextRepayDate\"       AS \"NextRepayDate\"";
		sql += "                   ,C.\"CurrZip3\"            AS \"CurrZip3\"";
		sql += "                   ,C.\"CurrZip2\"            AS \"CurrZip2\"";
		sql += "                   ,C2.\"CityItem\"           AS \"CityItem2\"";
		sql += "                   ,C3.\"AreaItem\"           AS \"AreaItem\"";
		sql += "                   ,C.\"CurrRoad\"            AS \"CurrRoad\"";
		sql += "                   ,C.\"CurrSection\"         AS \"CurrSection\"";
		sql += "                   ,C.\"CurrAlley\"           AS \"CurrAlley\"";
		sql += "                   ,C.\"CurrLane\"            AS \"CurrLane\"";
		sql += "                   ,C.\"CurrNum\"             AS \"CurrNum\"";
		sql += "                   ,C.\"CurrNumDash\"         AS \"CurrNumDash\"";
		sql += "                   ,C.\"CurrFloor\"           AS \"CurrFloor\"";
		sql += "                   ,C.\"CurrFloorDash\"       AS \"CurrFloorDash\"";
		sql += "                   ,F.\"RepayCode\"           AS \"RepayCode\"";
		sql += "                   ,M.\"AmortizedCode\"       AS \"AmortizedCode\"";
		sql += "                   ,F.\"AcctCode\"            AS \"AcctCode\"";
		sql += "                   ,ROW_NUMBER() OVER (PARTITION BY M.\"CustNo\",M.\"FacmNo\" ";
		sql += "                                       ORDER BY T.\"SEQ\") AS SEQ";
		sql += "                   ,C.\"EntCode\"             AS \"EntCode\"";
		sql += "             FROM (SELECT L.\"CustNo\"              AS \"CustNo\"";
		sql += "                         ,L.\"FacmNo\"              AS \"FacmNo\"";
		sql += "                         ,MAX(L.\"MaturityDate\")   AS　\"MaturityDate\"";
		sql += "                         ,SUM(L.\"LoanBal\")        AS \"LoanBal\"";
		sql += "                         ,MIN(L.\"PrevPayIntDate\") AS \"PrevPayIntDate\"　";
		sql += "                         ,MAX(L.\"StoreRate\")      AS \"StoreRate\" ";
		sql += "                         ,MIN(L.\"NextPayIntDate\") AS \"NextRepayDate\" ";
		sql += "                         ,MAX(L.\"AmortizedCode\" ) AS \"AmortizedCode\" ";
		sql += "                   FROM \"LoanBorMain\" L";
		sql += "                   LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "                   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "                                          AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "                   WHERE L.\"Status\" = 0";
		sql += "                    AND  L.\"SpecificDd\" > 0";
		sql += queryCondition(icustno, ifacmno, unpay, repay, custType, prinBalance, payIntDateSt, payIntDateEd, acdate);
		sql += "                   GROUP BY L.\"CustNo\", L.\"FacmNo\" ";
		sql += "                  ) M";
		sql += "             LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "             LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                    AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "             LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "                                   AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                   AND CF.\"MainFlag\" = 'Y'";
		sql += "             LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                    AND Cl.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                    AND Cl.\"ClNo\"    = CF.\"ClNo\"";
		sql += "             LEFT JOIN \"CdCity\" Ci ON Ci.\"CityCode\" = Cl.\"CityCode\"";
		sql += "             LEFT JOIN \"CdCity\" C2 ON C2.\"CityCode\" = C.\"CurrCityCode\"";
		sql += "             LEFT JOIN \"CdArea\" C3 ON C3.\"CityCode\" = C.\"CurrCityCode\"";
		sql += "                                    AND C3.\"AreaCode\" = C.\"CurrAreaCode\"";
		sql += "             LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" =  :tlrno ";
		sql += "             left join (                                                    ";
		sql += "                    select                                                  ";
		sql += "                     \"CustUKey\"                                           ";
		sql += "                    ,\"LiaisonName\"                                        ";
		sql += "                    ,\"Enable\"                                             ";
		sql += "                    ,NVL(\"TelArea\" || '-', '') || NVL(\"TelNo\", '') || NVL('-' || \"TelExt\", '') as \"PhoneNo\" ";
		sql += "                    ,0 as\"Mobile\"                                         ";
		sql += "                    ,ROW_NUMBER() OVER (PARTITION BY \"CustUKey\", \"TelTypeCode\" ORDER BY \"LastUpdate\" Desc) AS SEQ ";
		sql += "                    from \"CustTelNo\"                                       ";
		sql += "                    where \"TelTypeCode\" in ('01','02','03','05')           ";
		sql += "                      and \"Enable\" = 'Y'                                   ";
		sql += "                )    T ON T.\"CustUKey\" = C.\"CustUKey\"                   ";
		sql += "                      AND T.SEQ = 1                                          ";
		sql += "            ) D";
		sql += "       WHERE D.\"SEQ\" = 1";
		sql += "       ORDER BY D.\"F0\", \"CityCode\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (icustno > 0) {
			query.setParameter("icustno", icustno);
			if (ifacmno > 0) {
				query.setParameter("ifacmno", ifacmno);
			}
		}
		query.setParameter("st", st);
		query.setParameter("ed", ed);
		if ("1".equals(repay) || "2".equals(repay) || "3".equals(repay) || "4".equals(repay)) {
			query.setParameter("repay", repay);
		}
		query.setParameter("prinBalance", prinBalance);
		query.setParameter("acdate", acdate);
		query.setParameter("iSpecificDays", getSpecificDays(payIntDateSt, payIntDateEd));
		query.setParameter("tlrno", tlrno);
		return this.convertToMap(query);
	}

}