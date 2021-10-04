package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
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
import com.st1.itx.util.common.data.BS004Vo;

/**
 * 員工身分變動比對明細 1.員工利率產品比對不符清單 2.客戶檔客戶別與員工檔比對不符清單
 * 
 * @author Lai
 * @version 1.0.0
 */
@Service("bS004ServiceImpl")
@Repository
public class BS004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	/* 員工利率產品比對不符清單 */
	@SuppressWarnings("unchecked")
	public List<BS004Vo> compareProdNo(int quitDate, TitaVo titaVo) throws Exception {
// 商品代碼 ProdNo  VARCHAR2(5) 
//  if 員工利率產品第一碼為'E' 
//     if    1.身份變更        任用狀況碼 <> 1,4 
//       ||  2. 退休屆滿5年    任用狀況碼 = 5 &  QUIT_DATE 離職/停約日 <  本日減5年
//                        
// 業務線別 CommLineType	VARCHAR2(1)	 			 
//      0:單位報備
//      1:在職
//      2:離職
//      3:解聘
//      4:留職停薪 
//      5:退休離職
//      9:未報聘/內勤 
//  EO 員工利率-一般客戶 => 不寫入

		String queryttext = "select NEW com.st1.itx.util.common.data.BS004Vo";
		queryttext += "(a.custNo, a.facmNo, a.bormNo, ' ') ";
		queryttext += "from LoanBorMain a ";
		queryttext += "left join FacMain f on f.custNo = a.custNo and f.facmNo = a.facmNo ";
		queryttext += "left join CustMain c on c.custNo = f.custNo ";
		queryttext += "left join CdEmp e on e.employeeNo = c.empNo ";
		queryttext += "left join FacProd p on p.prodNo = f.prodNo ";
		queryttext += "where a.status in (0,4) "; // 戶況 0: 正常戶, 4: 逾期戶
		queryttext += "  and f.prodNo not in ('EO') "; // EO 員工利率-一般客戶
		queryttext += "  and p.empFlag = 'Y' "; // EmpFlag=Y 員工優惠貸款
		queryttext += "  and e.commLineType is not null ";
		queryttext += "  and (   (e.commLineType not in ('1','4')) ";
		queryttext += "       or (e.commLineType ='5' and e.quitDate < " + quitDate + " ))";

		this.info("queryttext=" + queryttext);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createQuery(queryttext, BS004Vo.class);
		return query.getResultList();
	}

	/* 客戶檔客戶別與員工檔比對不符清單 */
	@SuppressWarnings("unchecked")
	public List<BS004Vo> compareCustTyp() throws Exception {
//　if 業務線別 <> 1,4 && 客戶別 = 01,09 ==> 比對不符清單
// 	CommLineType	VARCHAR2(1)	 		
//  業務線別	 
//      0:單位報備
//      1:在職
//      2:離職
//      3:解聘
//      4:留職停薪 
//      5:退休離職
//      9:未報聘/內勤 
// CustTypeCode 	VARCHAR2(2)	
// 客戶別 		
//		00 一般
//		01 員工
//		02 首購
//		03 關企公司
//		04 關企員工
//		05 保戶
//		07 員工二親等
//		09 新二階員工 
		String queryttext = "select NEW com.st1.itx.util.common.data.BS004Vo";
		queryttext += "(c.custNo, 0, 0, c.custId) ";
		queryttext += "from CustMain c ";
		queryttext += "left join FacMain f on f.custNo = c.custNo ";
		queryttext += "left join CdEmp e on e.employeeNo = c.empNo ";
		queryttext += "where c.custTypeCode in ('01','09') ";
		queryttext += "  and c.custNo > 0 ";
		queryttext += "  and f.utilAmt > 0 ";
		queryttext += "  and e.commLineType is not null";
		queryttext += "  and not e.commLineType in ('1','4') ";
		this.info("queryttext=" + queryttext);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createQuery(queryttext, BS004Vo.class);
		return query.getResultList();
	}
}