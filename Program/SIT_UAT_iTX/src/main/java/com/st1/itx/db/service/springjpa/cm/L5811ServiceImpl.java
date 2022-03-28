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

	public List<Map<String, String>> checkAll(String iYear, TitaVo titaVo) throws Exception {

		String sql = "　";
		int iYYYYMM = Integer.parseInt(iYear + 12) + 191100;
		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo")); //L5810連動
		String iAcctCode = titaVo.getParam("Acct");//L5810連動
		int iStartMonth = Integer.parseInt(titaVo.get("StartMonth"));
		if(iStartMonth>0) {
			iStartMonth = iStartMonth+191100;
		}
		int iEndMonth = Integer.parseInt(titaVo.get("EndMonth"));
		if(iEndMonth>0) {
			iEndMonth = iEndMonth+191100;
		}
		if(iStartMonth>0 && iEndMonth >0) {
			iYYYYMM = iEndMonth;
		}
		
		this.info("iYYYYMM=="+iYYYYMM);
		this.info("iStartMonth=="+iStartMonth);
		this.info("iEndMonth=="+iEndMonth);
		this.info("iCustNo=="+iCustNo);
		this.info("iAcctCode=="+iAcctCode);
		
		
		sql += "SELECT CASE WHEN NVL(C.\"CustName\",'') = '' THEN 'V'                       \r\n ";
		sql += "        ELSE ' '                    									    \r\n ";
		sql += "END                                            AS F0  						 \r\n"; // r1 借戶姓名空白
		sql += ",CASE WHEN NVL(C.\"CustId\",'') = '' THEN 'V'   							 \r\n";
		sql += "  ELSE ' '             														 \r\n";
		sql += "END                                            AS F1						 \r\n"; // r2 統一編號空白
		sql += ",CASE WHEN Y.\"CustNo\" = 0 OR Y.\"FacmNo\" = 0 THEN 'V'     			     \r\n";
		sql += "  ELSE ' '                    											    \r\n ";
		sql += "END                                            AS F2                        \r\n "; // r3 貸款帳號空白
		sql += ",CASE WHEN Y.\"LoanAmt\" = 0 THEN 'V'            							\r\n ";
		sql += "  ELSE ' '                   												\r\n ";
		sql += "END                                            AS F3                        \r\n "; // r4 最初貸款金額為０
		sql += ",CASE WHEN Y.\"LoanAmt\" > F.\"LineAmt\" THEN 'V'             			    \r\n ";
		sql += "  ELSE ' '                     												\r\n ";
		sql += "END                                            AS F4       				    \r\n "; // r5 最初貸款金額＞核准額度
		sql += ",CASE WHEN Y.\"LoanAmt\" < Y.\"LoanBal\" THEN 'V'             				\r\n ";
		sql += "  ELSE ' '            													    \r\n  ";
		sql += " END                                           AS F5                		\r\n "; // r6 最初貸款金額＜放款餘額
		sql += ",CASE WHEN Y.\"FirstDrawdownDate\" = 0 THEN 'V'                 			\r\n ";
		sql += "  ELSE ' '                            										\r\n ";
		sql += "END                                            AS F6             		    \r\n  "; // r7 貸款起日空白
		sql += ",CASE WHEN Y.\"MaturityDate\" = 0 THEN 'V'                    				\r\n ";
		sql += "  ELSE ' '                            										\r\n ";
		sql += "END                                            AS F7        		        \r\n   "; // r8 貸款訖日空白
		sql += ",CASE WHEN Y.\"YearMonth\" = 0 THEN 'V'                        				\r\n ";
		sql += "  ELSE ' '                     												 \r\n";
		sql += "END                                            AS F8            		     \r\n"; // r10 繳息所屬年月空白
		sql += ",CASE WHEN Y.\"YearlyInt\" = 0 THEN 'V'                      				 \r\n";
		sql += "  ELSE ' '                          									     \r\n";
		sql += "END                                            AS F9                		 \r\n"; // r11 繳息金額為 0
		sql += ",CASE WHEN Y.\"AcctCode\" = ' ' THEN 'V'                   					\r\n ";
		sql += "  ELSE ' '                           										\r\n ";
		sql += "END                                            AS F10            		    \r\n "; // r12 科子細目代號暨說明空白
		sql += ",CASE WHEN C.\"LastFacmNo\" = 1 AND F.\"LastBormNo\" = 1 THEN 'V'           \r\n ";
		sql += "  ELSE ' '                           										\r\n ";
		sql += "END                                            AS F11               	    \r\n "; // c1 一額度一撥款
		sql += ",CASE WHEN C.\"LastFacmNo\" = 1 AND F.\"LastBormNo\" > 1 THEN 'V'           \r\n ";
		sql += "  ELSE ' '                     												\r\n ";
		sql += "END                                            AS F12              		    \r\n "; // c2 一額度多撥款
		sql += ",CASE WHEN C.\"LastFacmNo\" > 1 THEN 'V'                        		    \r\n ";
		sql += "  ELSE ' '                   											    \r\n  ";
		sql += "END                                            AS F13           		    \r\n "; // c3 多額度多撥款
		sql += ",CASE WHEN NVL(R.\"CustNo\",0) = 0 THEN 'V'                  		        \r\n ";
		sql += "  ELSE ' '                           								        \r\n  ";
		sql += "END                                            AS F14            		    \r\n  "; // c4 借新還舊件
		sql += ",CASE WHEN Y.\"LoanBal\" = 0 THEN 'V'                 					    \r\n  ";
		sql += "  ELSE ' '                            							   	 	 	\r\n ";
		sql += "END                                            AS F15               	    \r\n  "; // c5 清償件
		sql += ",NVL(CA.\"Zip3\",' ')                          AS F16            		    \r\n  "; // CK01 郵遞區號
		sql += ",Y.\"CustNo\"                                  AS F17  					    \r\n  "; // CK02 戶號
		sql += ",Y.\"FacmNo\"                                  AS F18  						\r\n "; // CK04 額度
		sql += ",NULL                                          AS F19 						\r\n "; // CK05 聯絡人姓名 ???
		sql += ",NULL                                          AS F20 						\r\n "; // CK06 戶名
		sql += ",NVL(JSON_VALUE(Y.\"JsonFields\",  '$.BdLoacation'),'')    AS F21 			\r\n "; // 地址
		sql += ",NVL(C.\"CustName\",'')                        AS F22  						\r\n "; // 借戶姓名
		sql += ",NVL(C.\"CustId\",'')                          AS F23 						\r\n "; // 統一編號
		sql += ",Y.\"LoanAmt\"                                 AS F24 						\r\n "; // 初貸金額
		sql += ",Y.\"FirstDrawdownDate\" - 19110000            AS F25   					\r\n "; // 貸款起日
		sql += ",Y.\"MaturityDate\" - 19110000                 AS F26  						\r\n "; // 貸款迄日
		sql += ",Y.\"LoanBal\"                                 AS F27  						\r\n "; // 本期未償還本金額
		sql += ", CASE ";
		sql += "   WHEN TRUNC(Y.\"FirstDrawdownDate\" / 100 ) < :iYYYYMM 					\r\n"; // 2022-03-18 智偉修改
		sql += "   THEN (TRUNC( :iYYYYMM / 100) - 1911) * 100 + 01 							\r\n"; // 首撥日小於查詢區間起月時 放查詢區間起月
		sql += "   ELSE TRUNC(Y.\"FirstDrawdownDate\" / 100 ) - 191100					    \r\n"; // 否則放首撥日月份
		sql += " END                                           AS F28  						\r\n "; // 繳息所屬年月(起)
		sql += ",Y.\"YearMonth\" - 191100                      AS F29 						\r\n "; // 繳息所屬年月(止)
		sql += ",Y.\"YearlyInt\"                               AS F30  						\r\n "; // 繳息金額
		sql += ",Y.\"AcctCode\"                                AS F31  						\r\n "; // 科子細目代號暨說明 ???
		sql += ",LPAD(Y.\"UsageCode\",2,'0')                   AS F32  						\r\n "; // 科子細目代號暨說明 ???
		sql += "FROM \"YearlyHouseLoanInt\" Y  												\r\n ";
		sql += "LEFT JOIN \"CustMain\" C   						 							\r\n ";
		sql += "    ON C.\"CustNo\" =  Y.\"CustNo\"  										\r\n ";
		sql += "LEFT JOIN \"FacMain\" F    													\r\n ";
		sql += "    ON F.\"CustNo\" = Y.\"CustNo\"  						 				\r\n ";
		sql += "   AND F.\"FacmNo\" = Y.\"FacmNo\"   										\r\n ";
		sql += "LEFT JOIN (SELECT   														\r\n ";
		sql += "         \"CustNo\",   														\r\n ";
		sql += "         \"NewFacmNo\" AS \"FacmNo\"  										\r\n ";
		sql += "        FROM \"AcLoanRenew\"  												\r\n ";
		sql += "        GROUP BY \"CustNo\", \"NewFacmNo\"  						 		\r\n ";
		sql += "        ) R  																\r\n ";
		sql += "   ON Y.\"CustNo\" = R.\"CustNo\"											\r\n ";
		sql += "  AND Y.\"FacmNo\" = R.\"FacmNo\"											\r\n ";
		sql += "  LEFT JOIN \"ClFac\" CL                         							\r\n "; // 擔保品與額度關聯檔
		sql += "    ON CL.\"CustNo\"      = Y.\"CustNo\"									\r\n ";
		sql += "   AND CL.\"FacmNo\"      = Y.\"FacmNo\"									\r\n ";
		sql += "    AND CL.\"MainFlag\"    = 'Y'                						    \r\n "; // 主要擔保品
		sql += "LEFT JOIN \"ClBuilding\" CB                      							\r\n "; // 擔保品不動產建物檔
		sql += "    ON CB.\"ClCode1\" = CL.\"ClCode1\"										\r\n ";
		sql += "   AND CB.\"ClCode2\" = CL.\"ClCode2\"										\r\n ";
		sql += "   AND CB.\"ClNo\"    = CL.\"ClNo\"											\r\n ";
		sql += " LEFT JOIN \"CdArea\" CA                          							\r\n "; // 縣市與鄉鎮區對照檔
		sql += "    ON CA.\"CityCode\" = CB.\"CityCode\"									\r\n ";
		sql += "   AND CA.\"AreaCode\" = CB.\"AreaCode\"								    \r\n	 ";
		sql += " WHERE Y.\"YearMonth\" = :iYYYYMM 										    \r\n";
		sql += " AND ( C.\"CuscCd\" = "+1+ "or CL.\"ClCode1\"=" + 1+") 						\r\n";
		sql +=" AND CASE												\r\n "; // L5810連動有值時，依值篩選
		sql +="    WHEN :CustNo != 0									\r\n";
		sql +="	         AND Y.\"CustNo\" = :CustNo						\r\n ";
		sql +="    THEN 1												\r\n ";
		sql +="    WHEN :CustNo != 0									\r\n";
		sql +="    THEN 0												\r\n ";
		sql +="    ELSE 1 END = 1										\r\n ";
		sql +=" AND CASE												\r\n "; // L5810連動有值時，依值篩選
		sql +="    WHEN NVL(:AcctCode,' ') != ' '						\r\n";
		sql +="	        AND Y.\"AcctCode\" = :AcctCode					\r\n ";
		sql +="    THEN 1												\r\n ";
		sql +="    WHEN NVL(:AcctCode,' ') = ' '						\r\n";
		sql +="    THEN 1												\r\n ";
		sql +="    ELSE 0 END = 1										\r\n ";	
		if(iStartMonth>0 && iEndMonth >0) {		// L5810連動有值時，依值篩選				
		sql +=" and NVL(JSON_VALUE(Y.\"JsonFields\",  '$.StartMonth'),0) = :StartMonth		\r\n ";	
		sql +=" and NVL(JSON_VALUE(Y.\"JsonFields\",  '$.EndMonth'),0) = :EndMonth		\r\n ";	
		}
		sql += " ORDER BY Y.\"CustNo\" 								\r\n";
		sql += "        , Y.\"FacmNo\"								\r\n";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iYYYYMM", iYYYYMM);
		query.setParameter("CustNo", iCustNo);
		query.setParameter("AcctCode", iAcctCode);
		if(iStartMonth>0 && iEndMonth >0) {
		query.setParameter("StartMonth", iStartMonth);
		query.setParameter("EndMonth", iEndMonth);
		}
		return this.convertToMap(query);
	}

//	public List<Map<String, String>> doJsonField(String iYear, TitaVo titaVo) throws Exception {
//		this.info("doJsonField");
//		String sql = "　";
//		int iYYYYMM = Integer.parseInt(iYear + 12) + 191100;
//
//		sql += "SELECT C.\"CustName\" 							AS F0	\n"; // 借戶姓名
//		sql += ",C.\"CustId\"                                   AS F1	\n"; // 統一編號
//		sql += ",Y.\"CustNo\"  			   						AS F2 	\n"; // 戶號key
//		sql += ",Y.\"FacmNo\"               					AS F3 	\n"; // 額度key
//		sql += ",Y.\"LoanAmt\"              					AS F4   \n"; // 最初貸款金額
//		sql += ",F.\"LineAmt\"            						AS F5	\n"; // 核准額度
//		sql += ",Y.\"LoanBal\"              					AS F6	\n"; // 放款餘額
//		sql += ",Y.\"FirstDrawdownDate\"           				AS F7   \n"; // 貸款起日
//		sql += ",Y.\"MaturityDate\"                             AS F8   \n"; // 貸款訖日
//		sql += ",Y.\"YearMonth\"                                AS F9   \n"; // 繳息所屬年月key
//		sql += ",Y.\"YearlyInt\"                                AS F10  \n"; // 繳息金額
//		sql += ",Y.\"AcctCode\"  								AS F11  \n"; // 科子細目代號暨說明
//		sql += ",Y.\"UsageCode\"  								AS F12  \n"; // 資金用途別key
//		sql += ",NVL(CB.\"BdLocation\",'')                      AS F21 	\n"; // 地址
//		sql += "FROM \"YearlyHouseLoanInt\" Y  												 ";
//		sql += "LEFT JOIN \"CustMain\" C   						 							 ";
//		sql += "    ON C.\"CustNo\" =  Y.\"CustNo\"  										 ";
//		sql += "LEFT JOIN \"FacMain\" F    													 ";
//		sql += "    ON F.\"CustNo\" = Y.\"CustNo\"  						 				 ";
//		sql += "   AND F.\"FacmNo\" = Y.\"FacmNo\"   										 ";
//		sql += "LEFT JOIN (SELECT   														 ";
//		sql += "         \"CustNo\",   														 ";
//		sql += "         \"NewFacmNo\" AS \"FacmNo\"  										 ";
//		sql += "        FROM \"AcLoanRenew\"  												 ";
//		sql += "        GROUP BY \"CustNo\", \"NewFacmNo\"  						 		 ";
//		sql += "        ) R  																 ";
//		sql += "   ON Y.\"CustNo\" = R.\"CustNo\"											 ";
//		sql += "  AND Y.\"FacmNo\" = R.\"FacmNo\"											 ";
//		sql += "  LEFT JOIN \"ClFac\" CL                         							 "; // 擔保品與額度關聯檔
//		sql += "    ON CL.\"CustNo\"      = Y.\"CustNo\"									 ";
//		sql += "   AND CL.\"FacmNo\"      = Y.\"FacmNo\"									 ";
//		sql += "    AND CL.\"MainFlag\"    = 'Y'                						     "; // 主要擔保品
//		sql += "LEFT JOIN \"ClBuilding\" CB                      							 "; // 擔保品不動產建物檔
//		sql += "    ON CB.\"ClCode1\" = CL.\"ClCode1\"										 ";
//		sql += "   AND CB.\"ClCode2\" = CL.\"ClCode2\"										 ";
//		sql += "   AND CB.\"ClNo\"    = CL.\"ClNo\"											 ";
//		sql += "WHERE Y.\"YearMonth\" = :iYYYYMM ";
//
//		this.info("sql=" + sql);
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		query = em.createNativeQuery(sql);
//		query.setParameter("iYYYYMM", iYYYYMM);
//		return this.convertToMap(query);
//	}

}