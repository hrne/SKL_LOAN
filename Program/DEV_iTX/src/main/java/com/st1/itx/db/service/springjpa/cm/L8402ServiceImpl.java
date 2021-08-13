package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("l8402ServiceImpl")
@Repository
/*
 * L8402: 產生JCIC月報媒體檔
 * 
 * @param fg fg=1: B201 聯徵授信餘額月報檔；<br> fg=2: B207 授信戶基本資料檔；<br> fg=3: B080
 * 授信額度資料檔；<br> fg=4: B085 帳號轉換資料檔；<br> fg=5: B087
 * 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔；<br> fg=6: B090 擔保品關聯檔資料檔；<br> fg=7: B092
 * 不動產擔保品明細檔；<br> fg=8: B093 動產及貴重物品擔保品明細檔；<br> fg=9: B094 股票擔保品明細檔；<br> fg=10:
 * B095 不動產擔保品明細-建號附加檔；<br> fg=11: B096 不動產擔保品明細-地號附加檔；<br> fg=12: B680
 * 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔；<br> // 以下新系統［需求規格書］未列入 fg=13:
 * B086 聯貸合約各參貸機構參貸比例資料檔；<br> fg=14: B091 有價證券(股票除外)擔保品明細檔；<br>
 */
public class L8402ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L8402ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(TitaVo titaVo, int fg) throws Exception {
		logger.info("L8402.findAll ");

		int dataYM = Integer.parseInt(titaVo.get("BodyFld1")) + 191100; // 西元年月
		logger.info("dataYM= " + dataYM);

		String sql = "";

		if (fg == 1) {
			sql = "SELECT M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"TranCode\"" + "     , M.\"SubTranCode\"" + "     , M.\"AcctNo\"" + "     , M.\"TotalAmt\"" + "     , M.\"CustId\""
					+ "     , M.\"CustIdErr\"" + "     , M.\"SuvId\"" + "     , M.\"SuvIdErr\"" + "     , M.\"OverseasId\"" + "     , M.\"IndustryCode\"" + "     , M.\"Filler12\""
					+ "     , M.\"AcctCode\"" + "     , M.\"SubAcctCode\"" + "     , M.\"OrigAcctCode\"" + "     , M.\"ConsumeFg\"" + "     , M.\"FinCode\"" + "     , M.\"ProjCode\""
					+ "     , M.\"NonCreditCode\"" + "     , M.\"UsageCode\"" + "     , M.\"ApproveRate\"" + "     , M.\"DrawdownDate\"" + "     , M.\"MaturityDate\"" + "     , M.\"CurrencyCode\""
					+ "     , M.\"DrawdownAmt\"" + "     , M.\"DrawdownAmtFx\"" + "     , M.\"RecycleCode\"" + "     , M.\"IrrevocableFlag\"" + "     , M.\"FacmNo\"" + "     , M.\"UnDelayBal\""
					+ "     , M.\"UnDelayBalFx\"" + "     , M.\"DelayBal\"" + "     , M.\"DelayBalFx\"" + "     , M.\"DelayPeriodCode\"" + "     , M.\"RepayCode\"" + "     , M.\"PayAmt\""
					+ "     , M.\"Principal\"" + "     , M.\"Interest\"" + "     , M.\"Fee\"" + "     , M.\"FirstDelayCode\"" + "     , M.\"SecondDelayCode\"" + "     , M.\"BadDebtCode\""
					+ "     , M.\"NegStatus\"" + "     , M.\"NegCreditor\"" + "     , M.\"NegNo\"" + "     , M.\"NegTransYM\"" + "     , M.\"Filler443\"" + "     , M.\"ClType\""
					+ "     , M.\"ClEvaAmt\"" + "     , M.\"ClTypeCode\"" + "     , M.\"SyndKind\"" + "     , M.\"SyndContractDate\"" + "     , M.\"SyndRatio\"" + "     , M.\"Filler51\""
					+ "     , M.\"Filler52\"" + "     , M.\"PayablesFg\"" + "     , M.\"NegFg\"" + "     , M.\"Filler533\"" + "     , M.\"GuaTypeCode1\"" + "     , M.\"GuaId1\""
					+ "     , M.\"GuaIdErr1\"" + "     , M.\"GuaRelCode1\"" + "     , M.\"GuaTypeCode2\"" + "     , M.\"GuaId2\"" + "     , M.\"GuaIdErr2\"" + "     , M.\"GuaRelCode2\""
					+ "     , M.\"GuaTypeCode3\"" + "     , M.\"GuaId3\"" + "     , M.\"GuaIdErr3\"" + "     , M.\"GuaRelCode3\"" + "     , M.\"GuaTypeCode4\"" + "     , M.\"GuaId4\""
					+ "     , M.\"GuaIdErr4\"" + "     , M.\"GuaRelCode4\"" + "     , M.\"GuaTypeCode5\"" + "     , M.\"GuaId5\"" + "     , M.\"GuaIdErr5\"" + "     , M.\"GuaRelCode5\""
					+ "     , M.\"Filler741\"" + "     , M.\"Filler742\"" + "     , M.\"BadDebtDate\"" + "     , M.\"SyndCode\"" + "     , M.\"BankruptDate\"" + "     , M.\"BdLoanFg\""
					+ "     , M.\"SmallAmt\"" + "     , M.\"ExtraAttrCode\"" + "     , M.\"ExtraStatusCode\"" + "     , M.\"Filler74A\"" + "     , M.\"JcicDataYM\"" + "     , M.\"DataEnd\""
					+ " FROM  \"JcicB201\" M" + " WHERE M.\"DataYM\" = " + dataYM + "   AND M.\"TranCode\" = 'A'" + " ORDER BY M.\"BankItem\", M.\"BranchItem\", M.\"TranCode\", M.\"AcctNo\" ";
		}

		if (fg == 2) {
			sql = "SELECT M.\"TranCode\"" + "     , M.\"BankItem\"" + "     , M.\"Filler3\"" + "     , M.\"DataDate\"" + "     , M.\"CustId\"" + "     , M.\"CustName\"" + "     , M.\"EName\""
					+ "     , M.\"Birthday\"" + "     , M.\"RegAddr\"" + "     , M.\"CurrZip\"" + "     , M.\"CurrAddr\"" + "     , M.\"Tel\"" + "     , M.\"Mobile\"" + "     , M.\"Filler14\""
					+ "     , M.\"EduCode\"" + "     , M.\"OwnedHome\"" + "     , M.\"CurrCompName\"" + "     , M.\"CurrCompId\"" + "     , M.\"JobCode\"" + "     , M.\"CurrCompTel\""
					+ "     , M.\"JobTitle\"" + "     , M.\"JobTenure\"" + "     , M.\"IncomeOfYearly\"" + "     , M.\"IncomeDataDate\"" + "     , M.\"Sex\"" + "     , M.\"NationalityCode\""
					+ "     , M.\"PassportNo\"" + "     , M.\"PreTaxNo\"" + "     , M.\"FullCustName\"" + "     , M.\"Filler30\"" + " FROM  \"JcicB207\" M" + " WHERE M.\"DataYM\" = " + dataYM
					+ " ORDER BY M.\"BankItem\", M.\"CustId\" ";
		}

		if (fg == 3) {
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"TranCode\"" + "     , M.\"Filler4\"" + "     , M.\"CustId\"" + "     , M.\"FacmNo\""
					+ "     , M.\"CurrencyCode\"" + "     , M.\"DrawdownAmt\"" + "     , M.\"DrawdownAmtFx\"" + "     , M.\"DrawdownDate\"" + "     , M.\"MaturityDate\"" + "     , M.\"RecycleCode\""
					+ "     , M.\"IrrevocableFlag\"" + "     , M.\"UpFacmNo\"" + "     , M.\"AcctCode\"" + "     , M.\"SubAcctCode\"" + "     , M.\"ClTypeCode\"" + "     , M.\"Filler18\""
					+ "     , M.\"JcicDataYM\"" + " FROM  \"JcicB080\" M" + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"FacmNo\" ";
		}

		if (fg == 4) { // B085 帳號轉換資料檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"RenewYM\"" + "     , M.\"CustId\"" + "     , M.\"BefBankItem\"" + "     , M.\"BefBranchItem\"" + "     , M.\"Filler6\""
					+ "     , M.\"BefAcctNo\"" + "     , M.\"AftBankItem\"" + "     , M.\"AftBranchItem\"" + "     , M.\"Filler10\"" + "     , M.\"AftAcctNo\"" + "     , M.\"Filler12\""
					+ " FROM  \"JcicB085\" M" + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"RenewYM\", M.\"CustId\", M.\"BefAcctNo\", M.\"AftAcctNo\" ";
		}

		if (fg == 5) { // B087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔 (none)
			sql = "";
		}

		if (fg == 6) { // B090 擔保品關聯檔資料檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"CustId\"" + "     , M.\"ClActNo\"" + "     , M.\"FacmNo\""
					+ "     , M.\"GlOverseas\"" + "     , M.\"JcicDataYM\"" + " FROM  \"JcicB090\" M" + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"CustId\", M.\"ClActNo\", M.\"FacmNo\" ";
		}

		if (fg == 7) { // B092 不動產擔保品明細檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\""
					+ "     , M.\"EvaAmt\"" + "     , M.\"EvaDate\"" + "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\"" + "     , M.\"MonthSettingAmt\"" + "     , M.\"SettingSeq\""
					+ "     , M.\"SettingAmt\"" + "     , M.\"PreSettingAmt\"" + "     , M.\"DispPrice\"" + "     , M.\"IssueEndDate\"" + "     , M.\"CityJCICCode\"" + "     , M.\"AreaJCICCode\""
					+ "     , M.\"IrCode\"" + "     , M.\"LandNo1\"" + "     , M.\"LandNo2\"" + "     , M.\"BdNo1\"" + "     , M.\"BdNo2\"" + "     , M.\"Zip\"" + "     , M.\"InsuFg\""
					+ "     , M.\"LVITax\"" + "     , M.\"LVITaxYearMonth\"" + "     , M.\"ContractPrice\"" + "     , M.\"ContractDate\"" + "     , M.\"ParkingTypeCode\"" + "     , M.\"Area\""
					+ "     , M.\"LandOwnedArea\"" + "     , M.\"BdTypeCode\"" + "     , M.\"Filler33\"" + "     , M.\"JcicDataYM\"" + " FROM  \"JcicB092\" M" + " WHERE M.\"DataYM\" = " + dataYM
					+ " ORDER BY M.\"ClActNo\",  M.\"OwnerId\", \"CityJCICCode\", \"AreaJCICCode\", \"IrCode\", \"LandNo1\", \"LandNo2\", \"BdNo1\", \"BdNo2\"";
		}

		if (fg == 8) { // B093 動產及貴重物品擔保品明細檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\""
					+ "     , M.\"EvaAmt\"" + "     , M.\"EvaDate\"" + "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\"" + "     , M.\"MonthSettingAmt\"" + "     , M.\"SettingSeq\""
					+ "     , M.\"SettingAmt\"" + "     , M.\"PreSettingAmt\"" + "     , M.\"DispPrice\"" + "     , M.\"IssueEndDate\"" + "     , M.\"InsuFg\"" + "     , M.\"Filler19\""
					+ "     , M.\"JcicDataYM\"" + " FROM  \"JcicB093\" M" + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"ClActNo\" ";
		}

		if (fg == 9) { // B094 股票擔保品明細檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\""
					+ "     , M.\"EvaAmt\"" + "     , M.\"EvaDate\"" + "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\"" + "     , M.\"CompanyId\"" + "     , M.\"CompanyCountry\""
					+ "     , M.\"StockCode\"" + "     , M.\"StockType\"" + "     , M.\"Currency\"" + "     , M.\"SettingBalance\"" + "     , M.\"LoanBal\"" + "     , M.\"InsiderJobTitle\""
					+ "     , M.\"InsiderPosition\"" + "     , M.\"LegalPersonId\"" + "     , M.\"DispPrice\"" + "     , M.\"Filler19\"" + "     , M.\"JcicDataYM\"" + " FROM  \"JcicB094\" M"
					+ " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"ClActNo\" ";
		}

		if (fg == 10) { // B095 不動產擔保品明細-建號附加檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"OwnerId\"" + "     , M.\"CityJCICCode\""
					+ "     , M.\"AreaJCICCode\"" + "     , M.\"IrCode\"" + "     , M.\"BdNo1\"" + "     , M.\"BdNo2\"" + "     , M.\"CityName\"" + "     , M.\"AreaName\"" + "     , M.\"Addr\""
					+ "     , M.\"BdMainUseCode\"" + "     , M.\"BdMtrlCode\"" + "     , M.\"BdSubUsageCode\"" + "     , M.\"TotalFloor\"" + "     , M.\"FloorNo\"" + "     , M.\"BdDate\""
					+ "     , M.\"TotalArea\"" + "     , M.\"FloorArea\"" + "     , M.\"BdSubArea\"" + "     , M.\"PublicArea\"" + "     , M.\"Filler33\"" + "     , M.\"JcicDataYM\""
					+ " FROM  \"JcicB095\" M" + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"ClActNo\", \"CityJCICCode\", \"AreaJCICCode\", \"IrCode\", \"BdNo1\", \"BdNo2\"";
		}

		if (fg == 11) { // B096 不動產擔保品明細-地號附加檔
			sql = "SELECT M.\"DataType\"" + "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\"" + "     , M.\"ClActNo\"" + "     , M.\"OwnerId\"" + "     , M.\"CityJCICCode\""
					+ "     , M.\"AreaJCICCode\"" + "     , M.\"IrCode\"" + "     , M.\"LandNo1\"" + "     , M.\"LandNo2\"" + "     , M.\"LandCode\"" + "     , M.\"Area\""
					+ "     , M.\"LandZoningCode\"" + "     , M.\"LandUsageType\"" + "     , M.\"PostedLandValue\"" + "     , M.\"PostedLandValueYearMonth\"" + "     , M.\"Filler18\""
					+ "     , M.\"JcicDataYM\"" + " FROM  \"JcicB096\" M" + " WHERE M.\"DataYM\" = " + dataYM
					+ " ORDER BY M.\"ClActNo\", \"CityJCICCode\", \"AreaJCICCode\", \"IrCode\", \"LandNo1\", \"LandNo2\"";
		}

		if (fg == 12) { // B680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
			sql = "SELECT M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"TranCode\"" + "     , M.\"CustId\"" + "     , M.\"CustIdErr\"" + "     , M.\"Filler6\"" + "     , M.\"Amt\""
					+ "     , M.\"JcicDataYM\"" + "     , M.\"Filler9\"" + " FROM  \"JcicB680\" M" + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"CustId\" ";
		}

		// 以下新系統［需求規格書］未列入
		/*
		 * if (fg == 13) { // B086 聯貸合約各參貸機構參貸比例資料 (none) sql = ""; }
		 * 
		 * if (fg == 14) { // B091 有價證券(股票除外)擔保品明細檔 sql = "SELECT M.\"DataType\"" +
		 * "     , M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"Filler4\""
		 * + "     , M.\"ClActNo\"" + "     , M.\"ClTypeJCIC\"" + "     , M.\"OwnerId\""
		 * + "     , M.\"EvaAmt\"" + "     , M.\"EvaDate\"" +
		 * "     , M.\"LoanLimitAmt\"" + "     , M.\"SettingDate\"" +
		 * "     , M.\"CompanyId\"" + "     , M.\"CompanyCountry\"" +
		 * "     , M.\"StockCode\"" + "     , M.\"Currency\"" +
		 * "     , M.\"PledgeEndYM\"" + "     , M.\"DispPrice\"" +
		 * "     , M.\"Filler19\"" + "     , M.\"JcicDataYM\"" + " FROM  \"JcicB091\" M"
		 * + " WHERE M.\"DataYM\" = " + dataYM + " ORDER BY M.\"ClActNo\" "; }
		 */

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}