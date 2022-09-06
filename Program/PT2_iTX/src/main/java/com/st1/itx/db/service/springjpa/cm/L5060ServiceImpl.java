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

/**
 * 法催紀錄清單
 * 
 * @author Lai
 * @version 1.0.0
 */
@Service("L5060ServiceImpl")
@Repository
public class L5060ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	/* load 法催紀錄清單檔 */
	public List<Map<String, String>> load(int index, int limit, String iCaseCode, int sOvduTerm, int eOvduTerm, String iOvdamtfm, String iOvdamtto, int iStatus, int iBizDateF, int iIdentity,
			int iCustNo, String iCustName, String iCustId, String iAccCollPsn, String iLegalPsn, String iTxCode, String iCityCode, TitaVo titaVo) throws Exception {

		Query query;
		// *** 折返控制相關 ***
		this.limit = limit;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		String iOprionCd = titaVo.getParam("OprionCd");
		String queryttext = "select ";
		queryttext += "s.\"CustNo\""; // 戶號
		queryttext += ",s.\"FacmNo\""; // 額度
		queryttext += ",s.\"TxDate\""; // 作業日期
		queryttext += ",s.\"TxCode\""; // 作業項目
		queryttext += ",NVL(r.\"RemindDate\",0) as \"RemindDate\""; // 提醒日期
		queryttext += ",s.\"PrevIntDate\""; // 繳息迄日
		queryttext += ",s.\"OvduTerm\""; // 逾期期數
		queryttext += ",s.\"OvduDays\""; // 逾期天數
		queryttext += ",s.\"CurrencyCode\""; // 幣別
		queryttext += ",s.\"PrinBalance\""; // 本金餘額
		queryttext += ",s.\"AccCollPsn\""; // 催收員
		queryttext += ",s.\"LegalPsn\""; // 法務人員
		queryttext += ",e.\"Fullname\" as \"AccCollPsnName\""; // 催收人員姓名
		queryttext += ",f.\"Fullname\" as \"LegalPsnName\""; // 法務人員姓名
		queryttext += ",s.\"Status\""; // 戶況
		queryttext += ",s.\"ClCustNo\""; // 同擔保品戶號
		queryttext += ",s.\"ClFacmNo\""; // 同擔保品額度
		queryttext += ",s.\"ClRowNo\""; // 同擔保品序列號
		queryttext += ",s.\"IsSpecify\"";// 是否為指定
		queryttext += ",s.\"CityCode\"";// 擔保品地區別
//		queryttext += ") ";
		queryttext += " from \"CollList\" s "; // 法催紀錄清單檔
//		queryttext += "left join \"CollList\" c on s.\"ClCustNo\" = s.\"ClCustNo\" and s.\"ClFacmNo\" = s.\"ClFacmNo\" "; // 法催紀錄清單檔
//		queryttext += "left join select \"CollRemind\" r on r.\"CaseCode\" = " + iCaseCode + " and r.\"CustNo\" = s.\"CustNo\" and r.\"FacmNo\" = s.\"FacmNo\" ";
//		queryttext += "                       and r.\"RemindDate\" <=  " + iBizDateF + " and r.\"CondCode\" = '1' "; // 法催紀錄提醒事項檔 (暫時不作判斷給user測)
		queryttext += "left join (select \"CaseCode\", \"CustNo\", \"FacmNo\", \"RemindDate\" , \"CondCode\" ,ROW_NUMBER() Over (Partition By \"CaseCode\", \"CustNo\" , \"FacmNo\" ";
		queryttext += "Order By \"RemindDate\" ASC ) as \"Row_Number\" from \"CollRemind\" where \"RemindDate\" >= " + iBizDateF + " and \"CondCode\" = '1' )";
		queryttext += " r on r.\"Row_Number\" = 1 and r.\"CaseCode\" = s.\"CaseCode\" and r.\"CustNo\" = s.\"CustNo\" and r.\"FacmNo\" = s.\"FacmNo\" ";
		queryttext += "left join \"CdEmp\" e on e.\"EmployeeNo\" = s.\"AccCollPsn\" ";
		queryttext += "left join \"CdEmp\" f on f.\"EmployeeNo\" = s.\"LegalPsn\" ";
		queryttext += "left join \"CustMain\" m on m.\"CustNo\" = s.\"CustNo\" ";
		this.info("系統日期=" + iBizDateF);
		queryttext += "where s.\"CaseCode\" =  '" + iCaseCode + "'"; // 案件種類
		switch (iStatus) {
//		case 0:
//			queryttext += " and s.status in (0,1,2,3,4,5,6,7,8) ";
//			break;
		case 98:
			queryttext += " and s.\"Status\" in (0,4) ";
			break;
		case 99:
//			queryttext += " and s.status in (3,8,9) ";
			break;
		default:
			queryttext += " and s.\"Status\" = '" + iStatus + "'";
		}
		if (!iTxCode.equals("0")) {
			queryttext += " and s.\"TxCode\" =  '" + iTxCode + "'"; // 案件種類
		}
		queryttext += " and (s.\"PrinBalance\" >=  " + iOvdamtfm + " and s.\"PrinBalance\" <= " + iOvdamtto + ") "; // 逾期金額
		switch (iOprionCd) {
		case "1":
			queryttext += " and (s.\"OvduTerm\" >= " + sOvduTerm + " and s.\"OvduTerm\" <= " + eOvduTerm + ") "; // 逾期期數
			break;
		case "2":
			queryttext += " and (s.\"OvduDays\" >= " + sOvduTerm + " and s.\"OvduDays\" <= " + eOvduTerm + ") "; // 逾期天數
			break;
		}
		switch (iIdentity) {
		case 0:
			break;
		case 1:
			queryttext += " and s.\"CustNo\" = '" + iCustNo + "' ";
			break;
		case 2:
			queryttext += " and m.\"CustName\" = '" + iCustName + "' ";
			break;
		case 3:
			queryttext += " and m.\"CustId\" = '" + iCustId + "' ";
			break;
		case 4:
			queryttext += " and s.\"AccCollPsn\" = '" + iAccCollPsn + "' ";
			break;
		case 5:
			queryttext += " and s.\"LegalPsn\" = '" + iLegalPsn + "' ";
			break;
		}
		this.info("iCityCode==" + iCityCode);
		if (!iCityCode.equals("00")) {
			queryttext += " and s.\"CityCode\" =  '" + iCityCode + "'"; // 擔保品地區別
		}

		queryttext += "order by s.\"ClCustNo\", s.\"ClFacmNo\", s.\"ClRowNo\", s.\"CustNo\", s.\"FacmNo\" "; // 01: 逾期/催收戶

		queryttext += sqlRow;

		this.info("Sql == " + queryttext);

		query = em.createNativeQuery(queryttext);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		this.info("L5060Service FindData=" + query.toString());
		return this.convertToMap(query);
	}

}