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

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> querySummary(TitaVo titaVo) throws Exception {

		this.info("L4943ServiceImpl.querySummary");

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
		sql += " select                                                   ";
		sql += "  SUM(BDD.\"UnpaidAmt\")   AS \"UnpaidAmt\"               ";
		sql += " ,SUM(BDD.\"TempAmt\")     AS \"TempAmt\"                 ";
		sql += " ,SUM(BDD.\"RepayAmt\")    AS \"RepayAmt\"                ";
		sql += " from \"BankDeductDtl\" BDD                               ";
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
		if (functionCode == 8) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"CustNo\"                                          ";
			sql += "     from \"FacClose\"                               ";
			sql += "     where \"CloseDate\" = 0                           ";
			sql += "       and \"EntryDate\" >= :entDy";
			sql += "     group by \"CustNo\"                                  ";
			sql += "     ) facclose on facclose.\"CustNo\" = BDD.\"CustNo\" ";
		}
		if (functionCode == 10) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"AcDate\"                                          ";
			sql += "     ,\"TitaTlrNo\"                                       ";
			sql += "     ,\"TitaTxtNo\"                                       ";
			sql += "     ,SUM(Case when \"AcSeq\" = 1 then \"TempAmt\" else 0 end) as  \"TempAmt\"  ";
			sql += "     ,SUM(Case when \"AcSeq\" = 1 then \"Overflow\" else \"Overflow\"-\"TempAmt\" end) as \"OverAmt\" ";
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

		if (functionCode == 11) {
			sql += " left join  \"BatxDetail\"  BATX on BATX.\"AcDate\" = BDD.\"AcDate\"              ";
			sql += "          and BATX.\"TitaTlrNo\" = BDD.\"TitaTlrNo\"        ";
			sql += "          and BATX.\"TitaTxtNo\" = BDD.\"TitaTxtNo\"        ";
		}
		if (functionCode == 12) {
			sql += " left join  ";
			sql += "   (  SELECT             ";
			sql += "       \"CustNo\"        ";
			sql += "      FROM               ";
			sql += "      (                  ";
			sql += "       SELECT             ";
			sql += "        TMP.\"CustNo\" ";
			sql += "       ,TMP.\"RepayBank\" ";
			sql += "       ,TMP.\"TempAmt\" ";
			sql += "       ,TAV.\"RvBal\" ";
			sql += "       FROM ";
			sql += "       ( ";
			sql += "        select  ";
			sql += "         b.\"CustNo\" ";
			sql += "        ,b.\"RepayBank\" ";
			sql += "        ,SUM(b.\"TempAmt\") AS \"TempAmt\" ";
			sql += "        from \"BankDeductDtl\" b ";
			sql += "        where b.\"TempAmt\" > 0 ";
			sql += "        group by b.\"CustNo\" ";
			sql += "                ,b.\"RepayBank\" ";
			sql += "        order by b.\"CustNo\" ";
			sql += "       ) TMP ";
			sql += "       left join ";
			sql += "        (select  ";
			sql += "          b.\"CustNo\" ";
			sql += "         ,a.\"RepayBank\" ";
			sql += "         ,SUM(b.\"RvBal\") AS \"RvBal\" ";
			sql += "         from \"AcReceivable\" b ";
			sql += "         left join \"FacMain\" f on  f.\"CustNo\" = b.\"CustNo\" ";
			sql += "                              and  f.\"FacmNo\" = b.\"FacmNo\" ";
			sql += "                              and  f.\"RepayCode\" = 2 ";
			sql += "        left join \"BankAuthAct\" a on  a.\"CustNo\" = f.\"CustNo\" ";
			sql += "                                  and  a.\"FacmNo\" = f.\"FacmNo\" ";
			sql += "                                  and  a.\"AuthType\" in ('00','01') ";
			sql += "         where b.\"AcctCode\" = 'TAV'    ";
			sql += "          and	b.\"RvBal\" > 0	                ";
			sql += "          and nvl(a.\"RepayBank\",' ') <> ' ' ";
			sql += "        group by b.\"CustNo\" ";
			sql += "                 ,a.\"RepayBank\" ";
			sql += "       ) TAV on TAV.\"CustNo\" = TMP.\"CustNo\" ";
			sql += "            and TAV.\"RepayBank\" = TMP.\"RepayBank\" ";
			sql += "       where  TMP.\"TempAmt\" > nvl(TAV.\"RvBal\",0) ";
			sql += "      ) group by \"CustNo\" ";
			sql += "   )  T on T.\"CustNo\" = BDD.\"CustNo\"     ";
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
		case 3: // 下限金額-短繳金額
			sql += "   and BDD.\"RepayType\" = 1                  ";
			sql += "   and BDD.\"IntStartDate\" = 0               "; // 短繳
			sql += "   and BDD.\"RepayAmt\" between 1 and :lowLimitAmt";
			break;
		case 4: // 檢核不正常
			sql += "   and case when BDD.\"AmlRsp\" in ('1','2') then 1 ";
			sql += "            when NVL(JSON_VALUE(BDD.\"JsonFields\", '$.Auth'),'A') <> 'A' then 1 ";
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
		case 8: // 戶號已設定清償
			sql += "   and NVL(facclose.\"CustNo\",0) > 0 ";
			break;
		case 9: // 整批
			break;
		case 10: // 溢短收
			sql += "   and (   NVL(TX.\"OverAmt\",0)  > 0 ";
			sql += "        or NVL(TX.\"ShortAmt\",0) > 0 )";
			break;
		case 11: // 入帳後有費用未收(銀扣期款不收費用)
			sql += "   and BDD.\"RepayType\" = 1";
			sql += "   and NVL(JSON_VALUE(BATX.\"ProcNote\", '$.UnPayFeeX'),' ') <> ' ' ";
			break;
		case 12: // 暫收抵用額度檢核
			sql += "   and NVL(T.\"CustNo\",0) > 0 ";
			break;
		}

		if (functionCode == 1) {
			sql += "   GROUP BY BDD.\"CustNo\" ";
		} else {
			sql += "   GROUP BY BDD.\"EntryDate\" ";
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
		} else if (functionCode == 8) {
			query.setParameter("entDy", titaVo.getEntDyI() + 19110000);
		}

		return this.convertToMap(query); // 此段為彙總,不需要做折返
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		this.info("L4943.findAll");

		this.index = index;
		this.limit = limit;

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
		sql += " ,BDD.\"TitaTlrNo\"   AS \"TitaTlrNo\"                    ";
		sql += " ,BDD.\"TitaTxtNo\"   AS \"TitaTxtNo\"                    ";
		if (functionCode == 10 || functionCode == 11) {
			sql += " ,TX.\"TempAmt\"  AS \"TxTempAmt\"                    ";
			sql += " ,TX.\"OverAmt\"  - \"ShortAmt\"   AS \"OverShort\"   ";
		}
		if (functionCode == 11) {
			sql += " ,JSON_VALUE(BATX.\"ProcNote\", '$.UnPayFeeX') AS \"UnPayFeeX\"    ";
		}
		sql += " from \"BankDeductDtl\" BDD                               ";

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
		if (functionCode == 8) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"CustNo\"                                          ";
			sql += "     from \"FacClose\"                               ";
			sql += "     where \"CloseDate\" = 0                           ";
			sql += "       and \"EntryDate\" >= :entDy";
			sql += "     group by \"CustNo\"                                  ";
			sql += "     ) facclose on facclose.\"CustNo\" = BDD.\"CustNo\" ";
		}
		if (functionCode == 10 || functionCode == 11) {
			sql += " left join (                                              ";
			sql += "     select                                               ";
			sql += "      \"AcDate\"                                          ";
			sql += "     ,\"TitaTlrNo\"                                       ";
			sql += "     ,\"TitaTxtNo\"                                       ";
			sql += "     ,SUM(Case when \"TitaTxCd\" = 'L3210' and \"AcSeq\" = 1 then \"TempAmt\" ";
			sql += "               when \"TitaTxCd\" = 'L3210' and \"AcSeq\" > 1 then 0           ";
			sql += "               else \"TempAmt\" end)   as  \"TempAmt\"  ";
			sql += "     ,SUM(Case when \"TitaTxCd\" = 'L3210' and \"AcSeq\" = 1 then \"Overflow\" ";
			sql += "               when \"TitaTxCd\" = 'L3210' and \"AcSeq\" > 1 then \"Overflow\"-\"TempAmt\" ";
			sql += "               else \"Overflow\" end)  as \"OverAmt\" ";
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

		if (functionCode == 11) {
			sql += " left join  \"BatxDetail\"  BATX on BATX.\"AcDate\" = BDD.\"AcDate\"              ";
			sql += "          and BATX.\"TitaTlrNo\" = BDD.\"TitaTlrNo\"        ";
			sql += "          and BATX.\"TitaTxtNo\" = BDD.\"TitaTxtNo\"        ";
		}
		if (functionCode == 12) {
			sql += " left join  ";
			sql += "   (  SELECT             ";
			sql += "       \"CustNo\"        ";
			sql += "      FROM               ";
			sql += "      (                  ";
			sql += "       SELECT             ";
			sql += "        TMP.\"CustNo\" ";
			sql += "       ,TMP.\"RepayBank\" ";
			sql += "       ,TMP.\"TempAmt\" ";
			sql += "       ,TAV.\"RvBal\" ";
			sql += "       FROM ";
			sql += "       ( ";
			sql += "        select  ";
			sql += "         b.\"CustNo\" ";
			sql += "        ,b.\"RepayBank\" ";
			sql += "        ,SUM(b.\"TempAmt\") AS \"TempAmt\" ";
			sql += "        from \"BankDeductDtl\" b ";
			sql += "        where b.\"TempAmt\" > 0 ";
			sql += "        group by b.\"CustNo\" ";
			sql += "                ,b.\"RepayBank\" ";
			sql += "        order by b.\"CustNo\" ";
			sql += "       ) TMP ";
			sql += "       left join ";
			sql += "        (select  ";
			sql += "          b.\"CustNo\" ";
			sql += "         ,a.\"RepayBank\" ";
			sql += "         ,SUM(b.\"RvBal\") AS \"RvBal\" ";
			sql += "         from \"AcReceivable\" b ";
			sql += "         left join \"FacMain\" f on  f.\"CustNo\" = b.\"CustNo\" ";
			sql += "                              and  f.\"FacmNo\" = b.\"FacmNo\" ";
			sql += "                              and  f.\"RepayCode\" = 2 ";
			sql += "        left join \"BankAuthAct\" a on  a.\"CustNo\" = f.\"CustNo\" ";
			sql += "                                  and  a.\"FacmNo\" = f.\"FacmNo\" ";
			sql += "                                  and  a.\"AuthType\" in ('00','01') ";
			sql += "         where b.\"AcctCode\" = 'TAV'    ";
			sql += "          and	b.\"RvBal\" > 0	                ";
			sql += "          and nvl(a.\"RepayBank\",' ') <> ' ' ";
			sql += "        group by b.\"CustNo\" ";
			sql += "                 ,a.\"RepayBank\" ";
			sql += "       ) TAV on TAV.\"CustNo\" = TMP.\"CustNo\" ";
			sql += "            and TAV.\"RepayBank\" = TMP.\"RepayBank\" ";
			sql += "       where  TMP.\"TempAmt\" > nvl(TAV.\"RvBal\",0) ";
			sql += "      ) group by \"CustNo\" ";
			sql += "   )  T on T.\"CustNo\" = BDD.\"CustNo\"     ";
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
		case 3: // 下限金額-短繳金額
			sql += "   and BDD.\"RepayType\" = 1                  ";
			sql += "   and BDD.\"IntStartDate\" = 0               "; // 短繳
			sql += "   and BDD.\"RepayAmt\" between 1 and :lowLimitAmt";
			break;
		case 4: // 檢核不正常
			sql += "   and case when BDD.\"AmlRsp\" in ('1','2') then 1 ";
			sql += "            when NVL(JSON_VALUE(BDD.\"JsonFields\", '$.Auth'),'A') <> 'A' then 1 ";
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
		case 8: // 戶號已設定清償
			sql += "   and NVL(facclose.\"CustNo\",0) > 0 ";
			break;
		case 9: // 整批
			break;
		case 10: // 溢短收
			sql += "   and (   NVL(TX.\"OverAmt\",0)  > 0 ";
			sql += "        or NVL(TX.\"ShortAmt\",0) > 0 )";
			break;
		case 11: // 入帳後有費用未收(銀扣期款不收費用)
			sql += "   and BDD.\"RepayType\" = 1";
			sql += "   and NVL(JSON_VALUE(BATX.\"ProcNote\", '$.UnPayFeeX'),' ') <> ' ' ";
			break;
		case 12: // 暫收抵用額度檢核
			sql += "   and NVL(T.\"CustNo\",0) > 0 ";
			break;
		}
		sql += " order by BDD.\"EntryDate\" , BDD.\"CustNo\" ,BDD.\"FacmNo\" ,BDD.\"PrevIntDate\" ";

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
		} else if (functionCode == 8) {
			query.setParameter("entDy", titaVo.getEntDyI() + 19110000);
		}

		return switchback(query);
	}

	// 7:已到期未至應繳日
	public List<Map<String, String>> doQuery7(int inputIndex, int inputLimit, TitaVo titaVo) throws Exception {

		int entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;
		int bankCode = Integer.parseInt(titaVo.getParam("BankCode"));

		this.index = inputIndex;
		this.limit = inputLimit;

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

		return switchback(query);
	}
}