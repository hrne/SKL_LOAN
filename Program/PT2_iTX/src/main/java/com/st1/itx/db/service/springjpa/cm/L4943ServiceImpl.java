package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;
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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4943ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4943ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int flag, TitaVo titaVo) throws Exception {

		this.info("L4943.findAll");

//		會計日期    #AcDate
//		整批批號    #BatchNo
//		作業狀態    #StatusCode
//		還款來源    #RepayCode
//		檔名            #FileName
//		處理狀態    #ProcStsCode
//		戶號           #CustNo

		int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo").trim());
		String repayBank = titaVo.getParam("BankCode");
		int opItem = parse.stringToInteger(titaVo.getParam("OpItem"));
		int repayType = parse.stringToInteger(titaVo.getParam("RepayType").trim());
		int entryDateFm = parse.stringToInteger(titaVo.getParam("EntryDateFm").trim()) + 19110000;
		int entryDateTo = parse.stringToInteger(titaVo.getParam("EntryDateTo").trim()) + 19110000;
		BigDecimal postLimitAmt = parse.stringToBigDecimal(titaVo.getParam("PostLimitAmt"));
		BigDecimal singleLimitAmt = parse.stringToBigDecimal(titaVo.getParam("SingleLimit"));
		BigDecimal lowLimitAmt = parse.stringToBigDecimal(titaVo.getParam("LowLimit"));

		this.info("functionCode = " + functionCode);
		this.info("custNo = " + custNo);
		this.info("repayBank = " + repayBank);
		this.info("opItem = " + opItem);
		this.info("repayType = " + repayType);
		this.info("entryDateFm = " + entryDateFm);
		this.info("entryDateTo = " + entryDateTo);
		this.info("postLimitAmt = " + postLimitAmt);
		this.info("singleLimitAmt = " + singleLimitAmt);

		String sql = "";
//		flag 0.Part 1.All
		if (flag == 0) {
			sql += " select                                                   ";
			sql += "  BDD.\"EntryDate\"   AS \"EntryDate\"                    ";
			sql += " ,BDD.\"CustNo\"      AS \"CustNo\"                       ";
			sql += " ,BDD.\"FacmNo\"      AS \"FacmNo\"                       ";
			sql += " ,BDD.\"PrevIntDate\" AS \"PrevIntDate\"                  ";
			sql += " ,BDD.\"PayIntDate\"  AS \"PayIntDate\"                   ";
			sql += " ,BDD.\"RepayType\"   AS \"RepayType\"                    ";
			sql += " ,BDD.\"UnpaidAmt\"   AS \"UnpaidAmt\"                    ";
			sql += " ,BDD.\"TempAmt\"     AS \"TempAmt\"                      ";
			sql += " ,BDD.\"RepayAmt\"    AS \"RepayAmt\"                     ";
			sql += " ,BDD.\"MediaCode\"   AS \"MediaCode\"                    ";
			sql += " ,BDD.\"AcDate\"      AS \"AcDate\"                       ";
			sql += " ,BDD.\"JsonFields\"  AS \"JsonFields\"                   ";
			sql += " ,BDD.\"ReturnCode\"  AS \"ReturnCode\"                   ";
			sql += " ,BDD.\"MediaKind\"   AS \"MediaKind\"                    ";
			sql += " ,BDD.\"AmlRsp\"      AS \"AmlRsp\"                       ";
			if (functionCode == 2) {
				sql += " ,TX.\"TempAmt\"  AS \"TxTempAmt\"                    ";
				sql += " ,case when TX.\"ShortAmt\" > 0 then 0 - \"ShortAmt\" ";
				sql += " ,     else TX.\"OverAmt\"  end AS \"OverShort\"      ";
				sql += " ,     end        AS \"OverShort\"                    ";
			}
			sql += " from \"BankDeductDtl\" BDD                               ";

		} else {
			sql += " select                                                   ";
			sql += "  SUM(BDD.\"UnpaidAmt\")   AS \"UnpaidAmt\"               ";
			sql += " ,SUM(BDD.\"TempAmt\")     AS \"TempAmt\"                 ";
			sql += " ,SUM(BDD.\"RepayAmt\")    AS \"RepayAmt\"                ";
			sql += " from \"BankDeductDtl\" BDD                               ";
		}
		if (functionCode == 2) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"CustNo\"                                          ";
			sql += "     ,SUM(\"RepayAmt\") as \"RepayAmt\"                   ";
			sql += "     from \"BankDeductDtl\"                               ";
			sql += "     where \"RepayBank\" = 700                            ";
			sql += "       and \"EntryDate\" >= :entryDateFm";
			sql += "       and \"EntryDate\" <= :entryDateTo";
			sql += "     group by \"CustNo\"                                  ";
			sql += "     ) postLimit on postLimit.\"CustNo\" = BDD.\"CustNo\" ";

		}
		if (functionCode == 10) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"AcDate\"                                          ";
			sql += "     ,\"TitaTlrNo\"                                       ";
			sql += "     ,\"TitaTxtNo\"                                       ";
			sql += "     ,SUM(Case when JSON_VALUE(\"OtherFields\",'$.AcSeq') = '0002' then \"TempAmt\" else 0 end) as  \"TempAmt\"  ";
			sql += "     ,SUM(\"Overflow\"-\"TempAmt\") as \"OverAmt\"        ";
			sql += "     ,SUM(\"UnpaidInterest\"+\"UnpaidPrincipal\" + \"UnpaidCloseBreach\") as \"ShortAmt\" ";
			sql += "     from \"LoanBorTx\"                                   ";
			sql += "     where \"RepayCode\" = 2                              ";
			sql += "       and \"EntryDate\" >= :entryDateFm";
			sql += "       and \"EntryDate\" <= :entryDateTo";
			sql += "       and \"TitaHCode\" = '0'                            ";
			sql += "     group by \"AcDate\"                                  ";
			sql += "             ,\"TitaTlrNo\"                               ";
			sql += "             ,\"TitaTxtNo\"                               ";
			sql += "     ) TX  on TX.\"AcDate\" = BDD.\"AcDate\"              ";
			sql += "          and TX.\"TitaTlrNo\" = BDD.\"TitaTlrNo\"        ";
			sql += "          and TX.\"TitaTxtNo\" = BDD.\"TitaTxtNo\"        ";
		}

		sql += " where BDD.\"EntryDate\" >= :entryDateFm";
		sql += "   and BDD.\"EntryDate\" <= :entryDateTo";

		switch (repayBank) {
		case "": // none
			break;
		case "999": // all
			break;
		case "998": // ach
			sql += "   and BDD.\"RepayBank\" <> 700 ";
			break;
		default:
			sql += "   and BDD.\"RepayBank\" = :repayBank";
			break;
		}

		switch (opItem) {
		case 1: // ach
			sql += "   and BDD.\"RepayBank\" <> 700 ";
			break;
		case 2: // post
			sql += "   and BDD.\"RepayBank\" = 700 ";
			break;
		default:
			break;
		}
		switch (repayType) {
		case 0:
			break;
		case 99:
			break;
		default:
			sql += "   and BDD.\"RepayType\" = :repayType";
			break;
		}
		switch (functionCode) {
		case 1: // 戶號
			sql += "   and BDD.\"CustNo\" = :custNo";
			break;
		case 2: // 上限金額
			sql += "   and (postLimit.\"RepayAmt\" >= :postLimitAmt";
			sql += "        and BDD.\"RepayAmt\" >= :singleLimitAmt" + " )      ";
			break;
		case 3: // 下限金額-
			sql += "   and BDD.\"RepayAmt\" <= :lowLimitAmt";
			break;
		case 4: // 檢核不正常
			sql += "   and case when BDD.\"AmlRsp\" in ('1','2') then 1 ";
			sql += "            when NVL(JSON_VALUE(BDD.\"JsonFields\", '$.Auth'),' ') <> ' ' then 1 ";
			sql += "            when NVL(JSON_VALUE(BDD.\"JsonFields\", '$.Deduct'),' ') <> ' ' then 1 ";
			sql += "            else 0 end = 1 ";
			sql += "   and BDD.\"AcDate\" = 0                     ";
			break;
		case 5: // 扣款金額為0
			sql += "   and BDD.\"RepayAmt\" = 0                     ";
			break;
		case 6: // 媒體檔總金額
			sql += "   and BDD.\"MediaCode\" = 'Y' ";
			break;
		case 9: // 整批
			break;
		case 10: // 溢短收
			sql += "   and (   NVL(TX.\"OverAmt\",0)  > 0 ";
			sql += "        or NVL(TX.\"ShortAmt\",0) > 0 )";
			break;
		}

//		flag 0.Part 1.All
		if (flag == 1) {
			if (functionCode == 1) {
				sql += "   GROUP BY BDD.\"CustNo\" ";
			} else {
				sql += "   GROUP BY BDD.\"EntryDate\" ";
			}
		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("entryDateFm", entryDateFm);
		query.setParameter("entryDateTo", entryDateTo);
		if (!"".equals(repayBank) && !"999".equals(repayBank) && !"998".equals(repayBank)) {
			query.setParameter("repayBank", repayBank);
		}

		if (repayType != 0 && repayType != 99) {
			query.setParameter("repayType", repayType);
		}

		if (functionCode == 1) {
			query.setParameter("custNo", custNo);
		} else if (functionCode == 2) {
			query.setParameter("postLimitAmt", postLimitAmt);
			query.setParameter("singleLimitAmt", singleLimitAmt);
		} else if (functionCode == 3) {
			query.setParameter("lowLimitAmt", lowLimitAmt);
		}

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		if (flag == 0) {
			// *** 折返控制相關 ***
			// 設定從第幾筆開始抓,需在createNativeQuery後設定
			query.setFirstResult(this.index * this.limit);

			// *** 折返控制相關 ***
			// 設定每次撈幾筆,需在createNativeQuery後設定
			query.setMaxResults(this.limit);
		}

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int flag, int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(flag, titaVo);
	}

	public int getSize() {
		return cnt;
	}

	public List<Map<String, String>> doQuery7(int inputIndex, int inputLimit, TitaVo titaVo) throws LogicException {

		int entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;
		int bankCode = Integer.parseInt(titaVo.getParam("BankCode"));

		String sql = " ";
		sql += " WITH loanData AS ( ";
		sql += "     SELECT L.\"CustNo\" ";
		sql += "          , L.\"FacmNo\" ";
		sql += "          , MAX(L.\"MaturityDate\") AS \"MaturityDate\" ";
		sql += "          , MIN(L.\"NextPayIntDate\") AS \"NextPayIntDate\" ";
		sql += "          , SUM(L.\"LoanBal\") AS \"LoanBal\" ";
		sql += "     FROM \"LoanBorMain\" L ";
		sql += "     LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\" ";
		sql += "                            AND F.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "     WHERE L.\"Status\" = 0 ";
		sql += "       AND F.\"RepayCode\" = 2 ";
		sql += "       AND L.\"MaturityDate\" <= :entryDate ";
		sql += "       AND L.\"NextPayIntDate\" = :entryDate ";
		sql += "       AND L.\"NextPayIntDate\" > L.\"MaturityDate\" ";
		sql += "     GROUP BY L.\"CustNo\" ";
		sql += "            , L.\"FacmNo\" ";
		sql += " ) ";
		sql += " , orderedData AS ( ";
		sql += "     SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"RepayType\" ";
		sql += "          , \"EntryDate\" ";
		sql += "          , \"PayIntDate\" ";
		sql += "          , \"PrevIntDate\" ";
		sql += "          , \"UnpaidAmt\" ";
		sql += "          , \"TempAmt\" ";
		sql += "          , \"RepayAmt\" ";
		sql += "          , \"IntStartDate\" ";
		sql += "          , \"IntEndDate\" ";
		sql += "          , \"MediaCode\" ";
		sql += "          , \"ReturnCode\" ";
		sql += "          , \"MediaKind\" ";
		sql += "          , \"AmlRsp\" ";
		sql += "          , \"JsonFields\" ";
		sql += "     FROM \"BankDeductDtl\" ";
		sql += "     WHERE \"RepayType\" <= 3 ";
		sql += " ) ";
		sql += " SELECT L.\"CustNo\" ";
		sql += "      , L.\"FacmNo\" ";
		sql += "      , L.\"MaturityDate\" ";
		sql += "      , L.\"NextPayIntDate\" ";
		sql += "      , L.\"LoanBal\" ";
		sql += "      , 0  AS \"EntryDate\" ";
		sql += "      , 0  AS \"RepayType\" ";
		sql += "      , 0  AS \"PayIntDate\" ";
		sql += "      , 0  AS \"PrevIntDate\" ";
		sql += "      , 0  AS \"UnpaidAmt\" ";
		sql += "      , 0  AS \"TempAmt\" ";
		sql += "      , 0  AS \"RepayAmt\" ";
		sql += "      , 0  AS \"IntStartDate\" ";
		sql += "      , 0  AS \"IntEndDate\" ";
		sql += "      , '' AS \"MediaCode\" ";
		sql += "      , '' AS \"ReturnCode\" ";
		sql += "      , '' AS \"MediaKind\" ";
		sql += "      , '' AS \"AmlRsp\" ";
		sql += "      , '' AS \"JsonFields\" ";
		sql += " FROM loanData L ";
		sql += " LEFT JOIN orderedData O ON O.\"CustNo\" = L.\"CustNo\" ";
		sql += "                        AND O.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                        AND O.\"IntEndDate\" = L.\"MaturityDate\" ";
		sql += " LEFT JOIN \"BankAuthAct\" BA ON BA.\"CustNo\" = L.\"CustNo\" ";
		sql += "                             AND BA.\"FacmNo\" = L.\"FacmNo\" ";
		sql += "                             AND BA.\"AuthType\" IN ('00','01') ";
		sql += " WHERE NVL(O.\"EntryDate\",0) = 0 "; // 沒串到扣款檔的才進表
		sql += "   AND CASE ";
		sql += "         WHEN :bankCode = '999' "; // 輸入參數選999:全部
		sql += "              AND NVL(BA.\"RepayBank\",' ') != ' ' "; // 授權檔有資料
		sql += "         THEN 1 "; // 1:進表
		sql += "         WHEN :bankCode = '998' "; // 輸入參數選998:ACH扣款
		sql += "              AND NVL(BA.\"RepayBank\",'700') != '700' "; // 授權檔有資料且不為郵局
		sql += "         THEN 1 "; // 1:進表
		sql += "         WHEN NVL(BA.\"RepayBank\",' ') = :bankCode "; // 輸入參數選特定銀行時，授權檔有資料且銀行代碼吻合
		sql += "         THEN 1 "; // 1:進表
		sql += "       ELSE 0 "; // 0:不進表
		sql += "       END = 1 "; // 篩選1:進表
		sql += " ORDER BY L.\"CustNo\" ";
		sql += "        , L.\"FacmNo\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entryDate", entryDate);
		query.setParameter("bankCode", bankCode);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(inputIndex * inputLimit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(inputLimit);

		return this.convertToMap(query);
	}
}