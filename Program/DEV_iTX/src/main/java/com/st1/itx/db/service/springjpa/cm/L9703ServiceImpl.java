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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9703ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> queryForDetail(TitaVo titaVo) throws LogicException {

		String icustno = titaVo.getParam("CustNo");
		String ifacmno = titaVo.getParam("FacmNo");
		String unpay = titaVo.getParam("UnpaidCond");
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
		int prinBalance = parse.stringToInteger(titaVo.getParam("PrinBalance"));
		
		this.info("L9703 queryForDetail");
		this.info("L9703 icustno    = " + icustno);
		this.info("L9703 ifacmno    = " + ifacmno);
		this.info("L9703 UnpaidCond = " + unpay);
		this.info("L9703 st         = " + st);
		this.info("L9703 ed         = " + ed);
		this.info("L9703 repay      = " + repay);
		this.info("L9703 custType   = " + custType);
		this.info("L9703 prinBalance   = " + prinBalance);
		
		String sql = "SELECT CC.\"CityItem\" AS \"CityItem\"";
		sql += "            ,\"Fn_GetEmpName\"(CC.\"AccCollPsn\",1) AS F1";
		sql += "            ,D.\"CustNo\" AS \"CustNo\"";
		sql += "            ,D.\"FacmNo\" AS \"FacmNo\"";
		sql += "            ,D.\"CustName\" AS \"CustName\"";
		sql += "            ,D.\"FirstDrawdownDate\" AS \"FirstDrawdownDate\"";
		sql += "            ,D.\"PrinBalance\" AS \"PrinBalance\"";
		sql += "            ,D.\"PrevIntDate\" AS \"PrevIntDate\"";
		sql += "            ,D.\"NextIntDate\" AS \"NextIntDate\"";
		sql += "            ,D.\"OvduDays\" AS \"OvduDays\"";
		sql += "            ,NVL(T1.\"LiaisonName\",' ') AS \"LiaisonName\"";
		sql += "            ,\"Fn_GetTelNo\"(D.\"CustUKey\",'02',1)  AS F11";
		sql += "            ,\"Fn_GetTelNo\"(D.\"CustUKey\",'03',1)  AS F12";
		sql += "            ,\"Fn_GetCdCode\"('RepayCode',LPAD(D.\"RepayCode\",2,'0')) AS F13";
		sql += "      FROM (SELECT L.\"CustNo\"";
		sql += "                  ,L.\"FacmNo\"";
		sql += "                  ,C.\"CustName\"";
		sql += "                  ,F.\"FirstDrawdownDate\"";
		sql += "                  ,L.\"PrinBalance\"";
		sql += "                  ,L.\"PrevIntDate\"";
		sql += "                  ,L.\"NextIntDate\"";
		sql += "                  ,L.\"OvduDays\"";
		sql += "                  ,F.\"RepayCode\"";
		sql += "                  ,C.\"CustUKey\"";
		sql += "            FROM \"CollList\" L";
		sql += "            LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "            LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "                                   AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "            WHERE L.\"CaseCode\" = 1";
		sql += "              AND L.\"Status\" IN (0, 4)";
		sql += queryCondition(icustno, ifacmno, unpay, repay, custType, prinBalance);// 在子查詢的where篩選
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

		int custno = Integer.parseInt(icustno);
		int facmno = Integer.parseInt(ifacmno);

//		if (!"0000000".equals(icustno)) {
		if (custno > 0) {
			query.setParameter("icustno", icustno);
//			if (!"000".equals(ifacmno)) {
			if (facmno > 0) {
				query.setParameter("ifacmno", ifacmno);
			}
		} else {
			query.setParameter("st", st);
			query.setParameter("ed", ed);
			if ("1".equals(repay) || "2".equals(repay) || "3".equals(repay) || "4".equals(repay)) {
				query.setParameter("repay", repay);
			}
		}
		
		query.setParameter("prinBalance", prinBalance); 
		
		return this.convertToMap(query);
	}

	private String queryCondition(String icustno, String ifacmno, String unpay, String repay, String custType, int prinBalance) {

		String condition = " ";

		int custno = Integer.parseInt(icustno);
		int facmno = Integer.parseInt(ifacmno);
		// 沒輸入戶號
		// if ("0000000".equals(icustno)) {
		if (custno == 0) {
			if ("1".equals(unpay)) {
				condition += "  AND  L.\"OvduTerm\" >= :st";
				condition += "  AND  L.\"OvduTerm\" <= :ed";
			} else {
				condition += "  AND  L.\"OvduDays\" >= :st";
				condition += "  AND  L.\"OvduDays\" <= :ed";
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

		} else { // 有輸入戶號
			condition += "  AND  F.\"CustNo\" = :icustno";
//			if (!"000".equals(ifacmno)) {
			if (facmno > 0) {
				condition += "  AND  F.\"FacmNo\" = :ifacmno";
			}
		}

		condition += "  AND  L.\"PrinBalance\" >= :prinBalance";
		
		return condition;
	}

	public List<Map<String, String>> queryForNotice(TitaVo titaVo) throws LogicException {

		this.info("L9703 queryForPdf");

		String iCUSTNO = titaVo.get("CustNo");
		String iFACMNO = titaVo.get("FacmNo");
		String tlrno = titaVo.getParam("TLRNO");

		String unpay = titaVo.getParam("UnpaidCond");
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
		int prinBalance = parse.stringToInteger(titaVo.getParam("PrinBalance"));
		
		this.info("L9703 queryForNotice");
		this.info("L9703 iCUSTNO    = " + iCUSTNO);
		this.info("L9703 iFACMNO    = " + iFACMNO);
		this.info("L9703 UnpaidCond = " + unpay);
		this.info("L9703 st         = " + st);
		this.info("L9703 ed         = " + ed);
		this.info("L9703 repay      = " + repay);
		this.info("L9703 custType   = " + custType);
		this.info("L9703 prinBalance   = " + prinBalance);
		
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
		sql += "                   ,M.\"BormNo\"              AS \"BormNo\"";
		sql += "                   ,ROW_NUMBER() OVER (PARTITION BY M.\"CustNo\",M.\"FacmNo\" ";
		sql += "                                       ORDER BY T.\"SEQ\") AS SEQ";
		sql += "                   ,C.\"EntCode\"             AS \"EntCode\"";
		sql += "             FROM (SELECT M.\"CustNo\"";
		sql += "                         ,M.\"FacmNo\"";
		sql += "                         ,M.\"BormNo\"";
		sql += "                         ,M.\"MaturityDate\"";
		sql += "                         ,M.\"LoanBal\"";
		sql += "                         ,M.\"PrevPayIntDate\"";
		sql += "                         ,M.\"StoreRate\"";
		sql += "                         ,M.\"NextRepayDate\"";
		sql += "                         ,M.\"AmortizedCode\"";
		sql += "                   FROM \"LoanBorMain\" M";
		sql += "                   LEFT JOIN \"CollList\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "                                           AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                   LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "                   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                          AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                   WHERE M.\"Status\" = 0";
		sql += "                     AND NVL(L.\"OvduDays\",0) >= 1";
		sql += queryCondition(iCUSTNO, iFACMNO, unpay, repay, custType, prinBalance);

		int custno = parse.stringToInteger(iCUSTNO);
		int facmno = parse.stringToInteger(iFACMNO);

		this.info("L9703 custno/facmno    = " + custno + "-" + facmno);
		this.info("L9703 tlrno    = " + tlrno);

		if (custno > 0) {
			sql += "          AND  M.\"CustNo\"     =  :icustno";
			if (facmno > 0) {
				sql += "          AND  M.\"FacmNo\"     =  :ifacmno";
			}
		}
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

		if (custno > 0) {
			query.setParameter("icustno", iCUSTNO);
			if (facmno > 0) {
				query.setParameter("ifacmno", iFACMNO);
			}
		} else {
			query.setParameter("st", st);
			query.setParameter("ed", ed);
			if ("1".equals(repay) || "2".equals(repay) || "3".equals(repay) || "4".equals(repay)) {
				query.setParameter("repay", repay);
			}
		}
		query.setParameter("tlrno", tlrno);
		query.setParameter("prinBalance", prinBalance); 
		
		return this.convertToMap(query);
	}

}