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
//import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l6069ServiceImpl")
@Repository
public class L6069ServiceImpl extends ASpringJpaParm implements InitializingBean {


	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	// *** 折返控制相關 ***
//	private int index;

	// *** 折返控制相關 ***
	private int limit;

	public List<Map<String, String>> FindAll(TitaVo titaVo, int index, int limit) throws Exception {

		this.info("L6069FindData1");
		this.info("iDefType     = " + titaVo.getParam("DefType"));  //09
		this.info("iCode        = " + titaVo.getParam("Code"));    // DateType
		this.info("iItem        = " + titaVo.getParam("Item"));   // 日期類別
		
//		int iDefType = titaVo.getParam("DefType") == ""||titaVo.getParam("DefType") ==  null? 0:Integer.parseInt(titaVo.getParam("DefType").toString()); // 業務類別
		String iDefType = titaVo.getParam("DefType");
		String iDefCode = titaVo.getParam("Code"); // 代碼類別名稱
		String iItem = titaVo.getParam("Item");   // 代碼類別說明
		String inDefCode ="";
		String inItem ="";
		int iniDefType = 0;

		

		this.info("iDefType     = " + iDefType);  //09
		this.info("iCode        = " + iDefCode);    // DateType
		this.info("iItem        = " + iItem);   // 日期類別
		if(titaVo.getParam("DefType").length() ==0) {
			iniDefType = 0;
		}else {
			iniDefType = Integer.parseInt(titaVo.getParam("DefType").toString());
		}
		this.info("iDefType  = " + iDefType);
		
		if(iDefCode.length()==0) {
			this.info("iDefCode沒有值Simpl");
			inDefCode = "0";
		}
		
		if(iItem.length()==0) {
			this.info("iItem沒有值Simpl");
			inItem = "0";
		}
		

		
		this.info("iDefType   = " + iDefType);
		this.info("iDefCode   = " + iDefCode);
		this.info("iItem   = " + iItem);
		
		
		String sql = "select  ";
		sql += " a.\"DefCode\" as \"ADEFCODE\",";     //代碼檔代號
		sql += " a.\"Code\" as \"ACODE\",";     //代號
		sql += " a.\"DefType\" as \"ADEFTYPE\",";     //業務類別
		sql += " a.\"Item\" as \"AITEM\",";     //代碼說明
		sql += " b.\"DefCode\" as \"BDEFCODE\",";     //代碼檔類別
		sql += " b.\"DefType\" as \"BDEFTYPE\",";     //業務類別
		sql += " b.\"Code\" as \"BCODE\",";     //業務類別說明
		sql += " b.\"Item\" as \"BITEM\"  ";   //代碼類別名稱
		sql += " from \"CdCode\" a ";
		sql += " left join (select * from \"CdCode\"  where \"DefCode\" = 'CodeType') b ";
		sql += " on a.\"DefCode\" = b.\"Code\"  ";	
		      if(titaVo.getParam("DefType").length() !=0 && "0".equals(inDefCode) && "0".equals(inItem)) {
		sql += "     where ";
		sql += "     a.\"DefType\" = :iDefType and";   
		sql += "     b.\"DefCode\" = 'CodeType' ";
		      }
		      if(!"0".equals(inDefCode) && titaVo.getParam("DefType").length() ==0  && "0".equals(inItem)) {
	    sql += "     where ";
		sql += "     a.\"DefCode\" = :iDefCode and";	
		sql += "     b.\"DefCode\" = 'CodeType' ";
		      }
		      if(!"0".equals(inItem) && titaVo.getParam("DefType").length() ==0 && "0".equals(inDefCode) ) {
		sql += "     where ";
		sql += "     a.\"Item\" = :iItem and";	   	  
		sql += "     b.\"DefCode\" = 'CodeType' ";
		      }
		      if (titaVo.getParam("DefType").length() !=0 && !"0".equals(inDefCode) && "0".equals(inItem)){
		sql += "     where ";
		sql += "     a.\"DefType\" = :iDefType and  ";
		sql += "     a.\"DefCode\" = :iDefCode and ";
		sql += "     b.\"DefType\" = :iDefType and ";
		sql += "     b.\"DefCode\" = 'CodeType' ";
		        } 
		      if (titaVo.getParam("DefType").length() !=0 && !"0".equals(inItem) && "0".equals(inDefCode)){
		sql += "     where ";
		sql += "     a.\"DefType\" = :iDefType and  ";
		sql += "     a.\"Item\"    = :iItem and ";
		sql += "     b.\"DefType\" = :iDefType and ";
		sql += "     b.\"DefCode\" = 'CodeType' ";
		        }  
		      if (!"0".equals(inDefCode) && !"0".equals(inItem) && titaVo.getParam("DefType").length() ==0 ){
		sql += "     where ";
		sql += "     a.\"DefCode\" = :iDefCode and ";		
		sql += "     a.\"Item\"    = :iItem and ";
		sql += "     b.\"DefCode\" = 'CodeType' ";
		        }   
		      if (titaVo.getParam("DefType").length() !=0 && !"0".equals(inDefCode) && !"0".equals(inItem) ){
		sql += "     where ";
		sql += "     a.\"DefType\" = :iDefType and  ";
		sql += "     a.\"DefCode\" = :iDefCode and ";		
		sql += "     a.\"Item\"    = :iItem and ";
		sql += "     b.\"DefType\" = :iDefType and ";
		sql += "     b.\"DefCode\" = 'CodeType' ";
		        } 		   
   
		sql += " order by a.\"Code\" ";

		
		
		this.info("L6069Service SQL=" + sql);
		
		Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if(titaVo.getParam("DefType").length() !=0) {
		query.setParameter("iDefType",iDefType);
		}
		if(!"".equals(iDefCode)) {
		query.setParameter("iDefCode",iDefCode);
		}
		if(!"".equals(iItem)) {
		query.setParameter("iItem",iItem);
		}
		
		this.info("L6069Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);

	}

}