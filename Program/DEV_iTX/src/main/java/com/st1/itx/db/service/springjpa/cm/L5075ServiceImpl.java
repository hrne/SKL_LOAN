package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("l5075ServiceImpl")
@Repository
public class L5075ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	public NegMainService sNegMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}
	// *** 折返控制相關 ***
		private int index;

		// *** 折返控制相關 ***
		private int limit;
		
		
	@SuppressWarnings("unchecked")
	public List<NegMain> findData(TitaVo titaVo,int index, int limit) throws LogicException {
		this.info("Run l5075ServiceImpl.findData ");
		String WorkSubject = titaVo.getParam("WorkSubject").trim(); // 作業項目 1:滯繳(時間到未繳);2:應繳(通通抓出來);3即將到期(本金餘額<=三期期款)
		String NextPayDate = titaVo.getParam("NextPayDate").trim(); // 1:滯繳- 逾期基準日;2:應繳-下次應繳日
		String AcDate = titaVo.getParam("AcDate").trim(); // 3:還款結束日 >= 會計日
		String CustId = titaVo.getParam("CustId").trim(); // 員工編號
		String sql = UseSql(titaVo);
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		this.info("L597AServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
		this.info("sql=" + sql);
		// int AcDate=titaVo.getOrgEntdyI();

		this.info("L5075ServiceImpl NextPayDate= " + NextPayDate + "");
		if (NextPayDate != null && NextPayDate.length() != 0) {
			if (NextPayDate.length() == 7) {
				NextPayDate = String.valueOf((Integer.parseInt(NextPayDate) + 19110000));
			}
		}

		this.info("L5075ServiceImpl AcDate= " + AcDate + "");
		if (AcDate != null && AcDate.length() != 0) {
			if (AcDate.length() == 7) {
				AcDate = String.valueOf((Integer.parseInt(AcDate) + 19110000));
			}
		}

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql, NegMain.class);
	
		
		switch (WorkSubject) {
		case "1":
			// 1:滯繳(時間到未繳)
			// NextPayDate 欄位名稱為逾期基準日
			query.setParameter("NextPayDateFrom", "0");
			query.setParameter("NextPayDateTo", NextPayDate);
			break;
		case "2":
			// 2:應繳(通通抓出來)
			// NextPayDate 欄位名稱為下次應繳日
			query.setParameter("NextPayDateFrom", NextPayDate);
			query.setParameter("NextPayDateTo", NextPayDate);
			break;
		case "3":
			// 3:即將到期(本金餘額<=三期期款)
			// 還款結束日 >= 會計日
			query.setParameter("AcDate", AcDate);
			break;

		}

		if (CustId != null && CustId.length() != 0) {
			query.setParameter("CustId", CustId);
		}
		
		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		 query.setFirstResult(this.index*this.limit);
//		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
				
		return query.getResultList();
	}

	public String UseSql(TitaVo titaVo) throws LogicException {
		String IsMainFin = titaVo.getParam("IsMainFin").trim(); // 是否為最大債權 1:Y;2:N
		String WorkSubject = titaVo.getParam("WorkSubject").trim(); // 作業項目 1:滯繳(時間到未繳);2:應繳(通通抓出來);3即將到期(本金餘額<=三期期款)
		// String NextPayDate=titaVo.getParam("NextPayDate").trim(); //1:滯繳-
		// 逾期基準日;2:應繳-下次應繳日
		String CustId = titaVo.getParam("CustId").trim(); // 員工編號

		String sql = "";
		sql += "SELECT "
				// +
				// "\"CustNo\",\"CaseSeq\",\"CaseKindCode\",\"Status\",\"CustLoanKind\",\"DeferYMStart\",\"DeferYMEnd\",\"ApplDate\",\"DueAmt\",\"TotalPeriod\",\"IntRate\",\"FirstDueDate\",\"LastDueDate\",\"IsMainFin\",\"TotalContrAmt\",\"MainFinCode\",\"PrincipalBal\",\"AccuTempAmt\",\"AccuOverAmt\",\"AccuDueAmt\",\"AccuSklShareAmt\",\"RepaidPeriod\",\"TwoStepCode\",\"ChgCondDate\",\"NextPayDate\",\"PayIntDate\",\"RepayPrincipal\",\"RepayInterest\",\"StatusDate\",\"ThisAcDate\",\"ThisTitaTlrNo\",\"ThisTitaTxtNo\",\"LastAcDate\",\"LastTitaTlrNo\",\"LastTitaTxtNo\",\"CreateDate\",\"CreateEmpNo\",\"LastUpdate\",\"LastUpdateEmpNo\""
				+ "* " + "FROM \"NegMain\" M WHERE 1=1 ";
		sql += "AND \"Status\"='0' ";// 債權戶況 0:正常,1:已變更,2:毀諾,3:結案,4:未生效

		if (IsMainFin != null && IsMainFin.length() != 0) {
			if (("1").equals(IsMainFin)) {
				sql += "AND M.\"IsMainFin\"='Y' ";
			} else {
				sql += "AND M.\"IsMainFin\"='N' ";
			}
		}

		if (CustId != null && CustId.length() != 0) {
			sql += "AND M.\"CustNo\"= (SELECT \"CustNo\" FROM \"CustMain\" WHERE \"CustId\"= :CustId)";
		}
		if (("1").equals(WorkSubject) || ("2").equals(WorkSubject)) {
			// 1:滯繳(時間到未繳)
			// NextPayDate 欄位名稱為逾期基準日

			// 2:應繳(通通抓出來)
			// NextPayDate 欄位名稱為下次應繳日
			sql += "AND M.\"NextPayDate\" BETWEEN :NextPayDateFrom AND :NextPayDateTo ";
		} else if (("3").equals(WorkSubject)) {
			// 3:即將到期(本金餘額<=三期期款)
			// 還款結束日 >= 會計日
			sql += "AND M.\"PrincipalBal\"<=3*M.\"DueAmt\" ";
			sql += "AND M.\"LastDueDate\">= :AcDate ";
		} else {
			// E0010 功能選擇錯誤
			throw new LogicException("E0010", "無效的[作業項目]");
		}
 
		sql += "Order By \"CustNo\" Asc , \"CaseSeq\" Desc";
		return sql;
	}
}