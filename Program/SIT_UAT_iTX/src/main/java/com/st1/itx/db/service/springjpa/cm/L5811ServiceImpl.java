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

@Service("l5811ServiceImpl")
@Repository
/* AML每日有效客戶名單 */
public class L5811ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, String>> checkAll(String iYear, TitaVo titaVo) throws Exception {

		String sql = "　";
		int iYYYYMM = Integer.parseInt(iYear+12)+191100;
		
		sql += "SELECT CASE WHEN NVL(C.\"CustName\",'') = '' THEN 'V'                        ";
		sql += "        ELSE ' '                    									     ";
		sql += "END                                            AS F0  						 ";  //r1 借戶姓名空白
		sql += ",CASE WHEN NVL(C.\"CustId\",'') = '' THEN 'V'   							 ";
		sql += "  ELSE ' '             														 ";
		sql += "END                                            AS F1						 ";  //r2 統一編號空白        
		sql += ",CASE WHEN Y.\"CustNo\" = 0 OR Y.\"FacmNo\" = 0 THEN 'V'     			     ";                                               
		sql += "  ELSE ' '                    											     ";
		sql += "END                                            AS F2                         ";  //r3 貸款帳號空白 
		sql += ",CASE WHEN Y.\"LoanAmt\" = 0 THEN 'V'            							 ";                                            
		sql += "  ELSE ' '                   												 "; 
		sql += "END                                            AS F3                         ";  //r4 最初貸款金額為０        
		sql += ",CASE WHEN Y.\"LoanAmt\" > F.\"LineAmt\" THEN 'V'             			     ";                                         
		sql += "  ELSE ' '                     												 ";
		sql += "END                                            AS F4       				     ";  //r5 最初貸款金額＞核准額度
		sql += ",CASE WHEN Y.\"LoanAmt\" < Y.\"LoanBal\" THEN 'V'             				 ";                                            
		sql += "  ELSE ' '            													     ";
		sql += " END                                           AS F5                		 ";  //r6 最初貸款金額＜放款餘額
		sql += ",CASE WHEN Y.\"FirstDrawdownDate\" = 0 THEN 'V'                 			 ";                                             
		sql += "  ELSE ' '                            										 ";
		sql += "END                                            AS F6             		     ";  //r7 貸款起日空白
		sql += ",CASE WHEN Y.\"MaturityDate\" = 0 THEN 'V'                    				 ";                                           
		sql += "  ELSE ' '                            										 ";
		sql += "END                                            AS F7        		         ";  //r8 貸款訖日空白
		sql += ",CASE WHEN Y.\"YearMonth\" = 0 THEN 'V'                        				 ";                                      
		sql += "  ELSE ' '                     												 ";      
		sql += "END                                            AS F8            		     ";  //r10 繳息所屬年月空白
		sql += ",CASE WHEN Y.\"YearlyInt\" = 0 THEN 'V'                      				 ";                                        
		sql += "  ELSE ' '                          									     ";
		sql += "END                                            AS F9                		 ";  //r11 繳息金額為 0	
		sql += ",CASE WHEN Y.\"AcctCode\" = ' ' THEN 'V'                   					 ";                                           
		sql += "  ELSE ' '                           										 ";
		sql += "END                                            AS F10            		     ";  //r12 科子細目代號暨說明空白           
		sql += ",CASE WHEN C.\"LastFacmNo\" = 1 AND F.\"LastBormNo\" = 1 THEN 'V'            ";                                                
		sql += "  ELSE ' '                           										 ";    
		sql += "END                                            AS F11               	     ";  //c1 一額度一撥款
		sql += ",CASE WHEN C.\"LastFacmNo\" = 1 AND F.\"LastBormNo\" > 1 THEN 'V'            ";                                        
		sql += "  ELSE ' '                     												 ";         
		sql += "END                                            AS F12              		     ";  //c2 一額度多撥款
		sql += ",CASE WHEN C.\"LastFacmNo\" > 1 THEN 'V'                        		     ";                                        
		sql += "  ELSE ' '                   											     ";           
		sql += "END                                            AS F13           		     ";  //c3 多額度多撥款
		sql += ",CASE WHEN NVL(R.\"CustNo\",0) = 0 THEN 'V'                  		         ";                                    
		sql += "  ELSE ' '                           								         ";  
		sql += "END                                            AS F14            		     ";  //c4 借新還舊件
		sql += ",CASE WHEN Y.\"LoanBal\" = 0 THEN 'V'                 					     ";                                             
		sql += "  ELSE ' '                            							   	 	 	 ";  
		sql += "END                                            AS F15               	     ";  //c5 清償件
		sql += ",NVL(CA.\"Zip3\",' ')                          AS F16            		     ";  //CK01 郵遞區號
		sql += ",Y.\"CustNo\"                                  AS F17  					     ";  //CK02 戶號 
		sql += ",Y.\"FacmNo\"                                  AS F18  						 ";  //CK04 額度           
		sql += ",NULL                                          AS F19 						 ";  //CK05 聯絡人姓名     ???
		sql += ",NULL                                          AS F20 						 ";  //CK06 戶名
		sql += ",NVL(CB.\"BdLocation\",'')                     AS F21 						 ";  //地址
		sql += ",NVL(C.\"CustName\",'')                        AS F22  						 ";  //借戶姓名
		sql += ",NVL(C.\"CustId\",'')                          AS F23 						 ";  //統一編號
		sql += ",Y.\"LoanAmt\"                                 AS F24 						 ";  //初貸金額
		sql += ",Y.\"FirstDrawdownDate\" - 19110000            AS F25   					 ";  //貸款起日
        sql += ",Y.\"MaturityDate\" - 19110000                 AS F26  						 ";  //貸款迄日
       	sql += ",Y.\"LoanBal\"                                 AS F27  						 ";  //本期未償還本金額
    	sql += ",CASE WHEN TRUNC(Y.\"FirstDrawdownDate\")	< TRUNC("+iYYYYMM +"* 100)	 	 ";
    	sql += "  THEN TRUNC("+iYYYYMM+",-2) -191100+01                               		 ";
    	sql += "        ELSE TRUNC(Y.\"FirstDrawdownDate\",-2)/100 - 191100            		 ";
    	sql += " END                                           AS F28  						 ";  //繳息所屬年月(起)
    	sql += ",Y.\"YearMonth\" - 191100                      AS F29 						 ";  //繳息所屬年月(止)
    	sql += ",Y.\"YearlyInt\"                               AS F30  						 ";  //繳息金額 	
    	sql += ",Y.\"AcctCode\"                                AS F31  						 ";  //科子細目代號暨說明 ???
    	sql += "FROM \"YearlyHouseLoanInt\" Y  												 ";
    	sql += "LEFT JOIN \"CustMain\" C   						 							 ";
    	sql += "    ON C.\"CustNo\" =  Y.\"CustNo\"  										 ";
    	sql += "LEFT JOIN \"FacMain\" F    													 ";
    	sql += "    ON F.\"CustNo\" = Y.\"CustNo\"  						 				 ";
    	sql += "   AND F.\"FacmNo\" = Y.\"FacmNo\"   										 ";
    	sql += "LEFT JOIN (SELECT   														 ";
    	sql += "         \"CustNo\",   														 ";
    	sql += "         \"NewFacmNo\" AS \"FacmNo\"  										 ";
    	sql += "        FROM \"AcLoanRenew\"  												 ";
    	sql += "        GROUP BY \"CustNo\", \"NewFacmNo\"  						 		 ";
    	sql += "        ) R  																 ";
    	sql += "   ON Y.\"CustNo\" = R.\"CustNo\"											 ";
    	sql += "  AND Y.\"FacmNo\" = R.\"FacmNo\"											 "; 
    	sql += "  LEFT JOIN \"ClFac\" CL                         							 ";  //擔保品與額度關聯檔
    	sql += "    ON CL.\"CustNo\"      = Y.\"CustNo\"									 "; 
    	sql += "   AND CL.\"FacmNo\"      = Y.\"FacmNo\"									 ";       
    	sql += "    AND CL.\"MainFlag\"    = 'Y'                						     ";  // 主要擔保品
    	sql += "LEFT JOIN \"ClBuilding\" CB                      							 ";  // 擔保品不動產建物檔
    	sql += "    ON CB.\"ClCode1\" = CL.\"ClCode1\"										 ";
    	sql += "   AND CB.\"ClCode2\" = CL.\"ClCode2\"										 ";
    	sql += "   AND CB.\"ClNo\"    = CL.\"ClNo\"											 ";
    	sql += " LEFT JOIN \"CdArea\" CA                          							 ";  // 縣市與鄉鎮區對照檔
    	sql += "    ON CA.\"CityCode\" = CB.\"CityCode\"									 ";
    	sql += "   AND CA.\"AreaCode\" = CB.\"AreaCode\"									 ";        
    	sql += "WHERE Y.\"YearMonth\" = "+iYYYYMM											;
    	sql += "AND Y.\"UsageCode\" =  '2' or Y.\"UsageCode\" =  '02'							";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}
	
	public List<Map<String, String>> doJsonField(String iYear, TitaVo titaVo) throws Exception {
		this.info("doJsonField");
		String sql = "　";
		int iYYYYMM = Integer.parseInt(iYear+12)+191100;
		
		sql += "SELECT C.\"CustName\" 							AS F0	\n";  //借戶姓名
		sql += ",C.\"CustId\"                                   AS F1	\n";  //統一編號        
		sql += ",Y.\"CustNo\"  			   						AS F2 	\n";  //戶號key                                              
		sql += ",Y.\"FacmNo\"               					AS F3 	\n";  //額度key 
		sql += ",Y.\"LoanAmt\"              					AS F4   \n";  //最初貸款金額       
		sql += ",F.\"LineAmt\"            						AS F5	\n";  //核准額度                     
		sql += ",Y.\"LoanBal\"              					AS F6	\n";  //放款餘額                      
		sql += ",Y.\"FirstDrawdownDate\"           				AS F7   \n";  //貸款起日
		sql += ",Y.\"MaturityDate\"                             AS F8   \n";  //貸款訖日
		sql += ",Y.\"YearMonth\"                                AS F9   \n";  //繳息所屬年月key 
		sql += ",Y.\"YearlyInt\"                                AS F10  \n";  // 繳息金額
		sql += ",Y.\"AcctCode\"  								AS F11  \n";  // 科子細目代號暨說明    
		sql += ",Y.\"UsageCode\"  								AS F12  \n";  // 資金用途別key 
		sql += ",NVL(CB.\"BdLocation\",'')                      AS F21 	\n";  //地址
    	sql += "FROM \"YearlyHouseLoanInt\" Y  												 ";
    	sql += "LEFT JOIN \"CustMain\" C   						 							 ";
    	sql += "    ON C.\"CustNo\" =  Y.\"CustNo\"  										 ";
    	sql += "LEFT JOIN \"FacMain\" F    													 ";
    	sql += "    ON F.\"CustNo\" = Y.\"CustNo\"  						 				 ";
    	sql += "   AND F.\"FacmNo\" = Y.\"FacmNo\"   										 ";    
    	sql += "LEFT JOIN (SELECT   														 ";
    	sql += "         \"CustNo\",   														 ";
    	sql += "         \"NewFacmNo\" AS \"FacmNo\"  										 ";
    	sql += "        FROM \"AcLoanRenew\"  												 ";
    	sql += "        GROUP BY \"CustNo\", \"NewFacmNo\"  						 		 ";
    	sql += "        ) R  																 ";
    	sql += "   ON Y.\"CustNo\" = R.\"CustNo\"											 ";
    	sql += "  AND Y.\"FacmNo\" = R.\"FacmNo\"											 "; 
    	sql += "  LEFT JOIN \"ClFac\" CL                         							 ";  //擔保品與額度關聯檔
    	sql += "    ON CL.\"CustNo\"      = Y.\"CustNo\"									 "; 
    	sql += "   AND CL.\"FacmNo\"      = Y.\"FacmNo\"									 ";       
    	sql += "    AND CL.\"MainFlag\"    = 'Y'                						     ";  // 主要擔保品
    	sql += "LEFT JOIN \"ClBuilding\" CB                      							 ";  // 擔保品不動產建物檔
    	sql += "    ON CB.\"ClCode1\" = CL.\"ClCode1\"										 ";
    	sql += "   AND CB.\"ClCode2\" = CL.\"ClCode2\"										 ";
    	sql += "   AND CB.\"ClNo\"    = CL.\"ClNo\"											 ";
    	sql += "WHERE Y.\"YearMonth\" = "+iYYYYMM											;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}
	
}