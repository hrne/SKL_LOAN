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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L8205ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Autowired
	Parse parse;

	public List<Map<String, String>> L8205Rpt1(TitaVo titaVo) throws Exception {

		this.info("L8205Rpt1");
		String iEntryDateStart = titaVo.getParam("DateStart");
		String iEntryDateEnd = titaVo.getParam("DateEnd");
			
		int afEntryDateStart = Integer.parseInt(iEntryDateStart) + 19110000;
		int afEntryDateEnd = Integer.parseInt(iEntryDateEnd) + 19110000;
		

		String sql = "";
		sql += " SELECT                               			 	   			\n";
		sql += "M.\"Factor\" 				    as F0							\n"; // 樣態
		sql += ",M.\"EntryDate\" 				as F1							\n"; // 入帳日
		sql += ",M.\"CustNo\"		            as F2				   			\n"; // 戶號
		sql += ",C.\"CustName\" 				as F3							\n"; // 戶名
		sql += ",M.\"TotalAmt\" 				as F4							\n"; // 累積金額
		sql += ",M.\"TotalCnt\" 				as F5							\n"; // 次數
		sql += ",CD.\"GroupItem\" 				as F6							\n"; // 課組別
		sql += ",M.\"Rational\" 				as F7							\n"; // 合理性
		sql += ",to_char(M.\"LastUpdate\", 'yyyymmdd') as F8					\n"; // 異動時間
		sql += ",M.\"EmpNoDesc\" 				as F9							\n"; // 經辦說明
		sql += ",M.\"ManagerCheck\" 			as F10  						\n"; // 主管覆核
		sql += "from \"MlaundryDetail\" M										\n";
		sql += "left join \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"		\n";
		sql += "left join \"TxTeller\" T ON T.\"TlrNo\" = M.\"LastUpdateEmpNo\"	\n";
		sql += "left join \"CdBranchGroup\" CD ON CD.\"BranchNo\" = T.\"BrNo\"	\n";
		sql += "and CD.\"GroupNo\" = T.\"GroupNo\"								\n";
		sql += "where M.\"EntryDate\" >= :entryStart and M.\"EntryDate\" <= :entrydEnd  and M.\"Factor\"='3'   \n";
		sql += "order by M.\"EntryDate\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entryStart", afEntryDateStart);
		query.setParameter("entrydEnd", afEntryDateEnd);


		return this.convertToMap(query);
	}

	public List<Map<String, String>> L8205Rpt2(TitaVo titaVo) throws Exception {

		this.info("L8205Rpt2");
		String iEntryDateStart = titaVo.getParam("DateStart");
		String iEntryDateEnd = titaVo.getParam("DateEnd");

		int afEntryDateStart = Integer.parseInt(iEntryDateStart) + 19110000;
		int afEntryDateEnd = Integer.parseInt(iEntryDateEnd) + 19110000;
		this.info("afEntryDateStart=" + afEntryDateStart + ",afEntryDateEnd=" + afEntryDateEnd);

		String sql = "";
		sql += " SELECT                               			 	   			\n";
		sql += "M.\"Factor\" 				    as F0							\n"; // 樣態
		sql += ",M.\"EntryDate\" 				as F1							\n"; // 入帳日
		sql += ",M.\"CustNo\"		            as F2				   			\n"; // 戶號
		sql += ",C.\"CustName\" 				as F3							\n"; // 戶名
		sql += ",M.\"TotalAmt\" 				as F4							\n"; // 累積金額
		sql += ",E.\"Fullname\" 		        as F5							\n"; // 經辦
		sql += ",M.\"Rational\" 				as F6							\n"; // 合理性
		sql += ",to_char(M.\"LastUpdate\", 'yyyymmdd') as F7					\n"; // 異動時間
		sql += ",M.\"EmpNoDesc\" 				as F8							\n"; // 經辦說明
		sql += ",M.\"ManagerCheck\" 			as F9  						\n"; // 主管覆核
		sql += "from \"MlaundryDetail\" M										\n";
		sql += "left join \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"		\n";
		sql += "left join \"CdEmp\" E ON M.\"CreateEmpNo\" = E.\"EmployeeNo\"	\n";
		sql += "where M.\"EntryDate\" >= :entryStart and M.\"EntryDate\" <= :entrydEnd   and M.\"Factor\" <> '3'\n";
		sql += "order by M.\"EntryDate\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entryStart", afEntryDateStart);
		query.setParameter("entrydEnd", afEntryDateEnd);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> L8205Rpt3(TitaVo titaVo) throws Exception {

		this.info("L8205Rpt1");
		String iEntryDateStart = titaVo.getParam("DateStart");
		String iEntryDateEnd = titaVo.getParam("DateEnd");

		int afEntryDateStart = Integer.parseInt(iEntryDateStart) + 19110000;
		int afEntryDateEnd = Integer.parseInt(iEntryDateEnd) + 19110000;
		this.info("afEntryDateStart=" + afEntryDateStart + ",afEntryDateEnd=" + afEntryDateEnd);

		String sql = "";
		sql += " SELECT                               			 	   			\n";
		sql += "M.\"Factor\" 				    as F0							\n"; // 樣態
		sql += ",M.\"EntryDate\" 				as F1							\n"; // 入帳日
		sql += ",M.\"CustNo\"		            as F2				   			\n"; // 戶號
		sql += ",C.\"CustName\" 				as F3							\n"; // 戶名
		sql += ",M.\"TotalAmt\" 				as F4							\n"; // 累積金額
		sql += ",M.\"TotalCnt\" 				as F5							\n"; // 次數
		sql += ",CD.\"GroupItem\" 				as F6							\n"; // 課組別
		sql += ",M.\"Rational\" 				as F7							\n"; // 合理性
		sql += ",to_char(M.\"LastUpdate\", 'yyyymmdd') as F8					\n"; // 異動時間
		sql += ",M.\"EmpNoDesc\" 				as F9							\n"; // 經辦說明
		sql += ",M.\"ManagerCheck\" 			as F10  						\n"; // 主管覆核
		sql += ",M.\"ManagerDate\" 			    as F11  					    \n"; // 主管覆核日期
		sql += "from \"MlaundryDetail\" M										\n";
		sql += "left join \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"		\n";
		sql += "left join \"TxTeller\" T ON T.\"TlrNo\" = M.\"LastUpdateEmpNo\"	\n";
		sql += "left join \"CdBranchGroup\" CD ON CD.\"BranchNo\" = T.\"BrNo\"	\n";
		sql += "and CD.\"GroupNo\" = T.\"GroupNo\"								\n";
		sql += "where M.\"EntryDate\" >= :entryStart and M.\"EntryDate\" <= :entrydEnd  and M.\"Factor\"='3'   \n";
		sql += "and (M.\"ManagerDate\" = 0 or M.\"ManagerDate\">= M.\"EntryDate\"+4)      \n     ";
		sql += "order by M.\"EntryDate\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entryStart", afEntryDateStart);
		query.setParameter("entrydEnd", afEntryDateEnd);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> L8205Rpt4(TitaVo titaVo) throws Exception {

		this.info("L8205Rpt4");
		String iEntryDateStart = titaVo.getParam("DateStart");
		String iEntryDateEnd = titaVo.getParam("DateEnd");

		int afEntryDateStart = Integer.parseInt(iEntryDateStart) + 19110000;
		int afEntryDateEnd = Integer.parseInt(iEntryDateEnd) + 19110000;
		this.info("afEntryDateStart=" + afEntryDateStart + ",afEntryDateEnd=" + afEntryDateEnd);

		String sql = "";
		sql += " SELECT                               			 	   			\n";
		sql += "M.\"Factor\" 				    as F0							\n"; // 樣態
		sql += ",M.\"EntryDate\" 				as F1							\n"; // 入帳日
		sql += ",M.\"CustNo\"		            as F2				   			\n"; // 戶號
		sql += ",C.\"CustName\" 				as F3							\n"; // 戶名
		sql += ",M.\"TotalAmt\" 				as F4							\n"; // 累積金額
		sql += ",E.\"Fullname\" 		        as F5							\n"; // 經辦
		sql += ",M.\"Rational\" 				as F6							\n"; // 合理性
		sql += ",to_char(M.\"LastUpdate\", 'yyyymmdd') as F7					\n"; // 異動時間
		sql += ",M.\"EmpNoDesc\" 				as F8							\n"; // 經辦說明
		sql += ",M.\"ManagerCheck\" 			as F9  							\n"; // 主管覆核
		sql += ",M.\"ManagerDate\" 			    as F10  					    \n"; // 主管覆核日期
		sql += "from \"MlaundryDetail\" M										\n";
		sql += "left join \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"		\n";
		sql += "left join \"CdEmp\" E ON M.\"CreateEmpNo\" = E.\"EmployeeNo\"	\n";
		sql += "where M.\"EntryDate\" >= :entryStart and M.\"EntryDate\" <= :entrydEnd and M.\"Factor\" <> '3'  \n";
		sql += "and ( M.\"ManagerDate\" = 0 or M.\"ManagerDate\">= M.\"EntryDate\"+4)      \n     ";
		sql += "order by M.\"EntryDate\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entryStart", afEntryDateStart);
		query.setParameter("entrydEnd", afEntryDateEnd);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> L8205Rpt5(TitaVo titaVo) throws Exception {

		this.info("L8205Rpt5");
		int iRepayDate = Integer.parseInt(titaVo.getParam("RepayDate"));
		int iActualRepayDate = Integer.parseInt(titaVo.getParam("ActualRepayDate"));
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));
				
		if(iRepayDate>0) {
			iRepayDate = iRepayDate+19110000; 
		}
		
		if(iActualRepayDate>0) {
			iActualRepayDate = iActualRepayDate+19110000; 
		}
		
		boolean useCustNo = iCustNo > 0;

		String sql = "";
		sql += " SELECT                               			    \n";
		sql += "M.\"RecordDate\" 				as F0					\n"; // 訪談日
		sql += ",M.\"RepayDate\" 				as F1					\n"; // 預定還款日期
		sql += ",M.\"ActualRepayDate\"		as F2				    \n"; // 實際還款日
		sql += ",M.\"CustNo\" 				as F3					\n"; // 戶號
		sql += ",C.\"CustName\" 				as F4					\n"; // 戶號
		sql += ",M.\"RepayAmt\" 				as F5					\n"; // 還款金額
		sql += ",M.\"Career\" 				as F6					\n"; // 職業
		sql += ",M.\"Income\" 				as F7					\n"; // 年收入
		sql += ",M.\"RepaySource\" 			as F8					\n"; // 還款來源
		sql += ",M.\"RepayBank\" 				as F9					\n"; // 代償銀行
		sql += ",M.\"Description\" 			as F10				\n"; // 其他說明
		sql += ",M.\"CreateEmpNo\" 			as F11 				\n"; // 建檔人員
		sql += ",CD.\"Item\"                   as F12            \n"; // 還款來源中文
		sql += ",E.\"Fullname\"                   as F13            \n";
		sql += "from \"MlaundryRecord\" M							\n";
		sql += "left join \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"			\n";
		sql += "left join \"CdCode\" CD on CD.\"DefCode\" = 'RepaySource'			\n";
		sql += "and CD.\"Code\" = M.\"RepaySource\"			\n";
		sql += "left join \"CdEmp\" E on E.\"EmployeeNo\" = M.\"CreateEmpNo\"			\n";
		// 條件：資料有實際償還日期時比實際償還日期
		//                       否則比預定償還日期
		sql += "WHERE CASE WHEN NVL(M.\"ActualRepayDate\", 0) > 0 ";
		sql += "           THEN M.\"ActualRepayDate\" ";
		sql += "           ELSE M.\"RepayDate\" END ";
		sql += "      = ";
		sql += "      CASE WHEN NVL(M.\"ActualRepayDate\", 0) > 0 ";
		sql += "           THEN :actualRepayDate ";
		sql += "           ELSE :repayDate END ";
		if(useCustNo) {
			sql += " and M.\"CustNo\" = :custNo";
		}
		sql += " order by M.\"RecordDate\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("actualRepayDate", iActualRepayDate);
		query.setParameter("repayDate", iRepayDate);
		
		if (useCustNo)
			query.setParameter("custNo", iCustNo);

		return this.convertToMap(query);
	}

}