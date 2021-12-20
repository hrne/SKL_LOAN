package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;

import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("L5054ServiceImpl")
@Repository
public class L5054ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5054ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private CdBcmService sCdBcmService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "";// "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";


	/**
	 * query data
	 * @param titaVo titaVo
	 * @param index index
	 * @param limit limit
	 * @return return
	 * @throws Exception Exceptions
	 */
	
	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {

		int iWorkYM = Integer.valueOf(titaVo.getParam("WorkYM").trim()) + 191100; // 工作月

		logger.info("L5054Service FindData=" + iWorkYM);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		String sql = "SELECT " + "a.\"BonusDate\", " // 獎金發放日
				+ "a.\"PerfDate\", " // 業績日期
				+ "a.\"CustNo\", " // 戶號
				+ "a.\"FacmNo\", " // 額度
				+ "a.\"BormNo\", " // 撥款
				+ "a.\"EmployeeNo\", " // 獎金發放員工編號
				+ "b.\"Fullname\", " // 獎金發放員工編號姓名
				+ "a.\"Bonus\", " // 介紹獎金
				+ "a.\"AdjustBonus\", " // 調整後介紹獎金
				+ "a.\"AdjustBonusDate\", " // 調整介紹獎金日期
				+ "a.\"WorkMonth\", " // 工作月
				+ "a.\"WorkSeason\", " // 工作季
				+ "a.\"MediaFg\", " // 產出媒體檔記號
				+ "d.\"CreateDate\" as \"MediaDate\", " // 產出媒體檔日期
				+ "a.\"ManualFg\", " // 人工新增記號
				+ "a.\"BonusType\", " // 獎金類別 15
				+ "a.\"BonusNo\", " // 序號 16
				+ "a.\"Remark\", " // 備註
				+ "a.\"CreateDate\", " // 建檔日期時間
				+ "a.\"CreateEmpNo\", " // 建檔人員
				+ "a.\"LastUpdate\", " // 最後更新日期時間
				+ "a.\"LastUpdateEmpNo\", " // 最後更新人員
				+ "c.\"Fullname\" as \"LastUpdateEmpName\" " // 最後更新人員
				+ "FROM \"PfRewardMedia\" a "; 
		sql	+= "LEFT JOIN \"CdEmp\" b ON b.\"EmployeeNo\"=a.\"EmployeeNo\" ";
		sql += "LEFT JOIN \"CdEmp\" c ON c.\"EmployeeNo\"=a.\"LastUpdateEmpNo\" ";
		sql += "LEFT JOIN \"TxControl\" d ON d.\"Code\"=:Code ";
		sql += "WHERE a.\"WorkMonth\"=:WorkYM and a.\"BonusType\" = 7 ";

		sql += "order by a.\"EmployeeNo\",a.\"PieceCode\",a.\"CustNo\",a.\"FacmNo\",a.\"BormNo\" ";

		query = em.createNativeQuery(sql);
//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);
		query.setParameter("WorkYM", iWorkYM);
		query.setParameter("Code", "L5512."+iWorkYM+".2");

		this.info("L5054ServiceImpl sql=[" + sql + "]");
//		logger.info("L5054ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");
//		logger.info("L5054Service FindData=" + query.toString());

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
//		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
//		query.setMaxResults(this.limit);

		// List<L5051Vo> L5051VoList =this.convertToMap(query.getResultList());

//		@SuppressWarnings("unchecked")
		return this.convertToMap(query);
	}


}