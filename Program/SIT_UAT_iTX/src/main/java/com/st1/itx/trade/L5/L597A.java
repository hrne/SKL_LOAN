package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.springjpa.cm.L597AServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* AcDate=9,7<br>
* IsMainFin=9,1<br>
* SearchOption=X,2<br>
* SearchDetail=X,1<br>
* Export=X,1<br>
* IsBtn=X,1<br>
*/

@Service("L597A")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L597A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L597A.class);
	/* DB服務注入 */
	@Autowired
	public NegCom NegCom;
	
	@Autowired
	public NegTransService sNegTransService;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public L597AServiceImpl l597AServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L597A ");
		this.totaVo.init(titaVo);
		int AcDate=Integer.parseInt(titaVo.getParam("AcDate").trim()); //
		int IsMainFin=Integer.parseInt(titaVo.getParam("IsMainFin").trim()); //
		int SearchOption=Integer.parseInt(titaVo.getParam("SearchOption").trim()); //
		int SearchDetail=Integer.parseInt(titaVo.getParam("SearchDetail").trim()); //
		int Export=Integer.parseInt(titaVo.getParam("Export").trim()); //
		int IsBtn=Integer.parseInt(titaVo.getParam("IsBtn").trim()); //
		String TransTxKind = titaVo.getParam("TransTxKind").trim(); //
		Boolean selectfg = false;
		Boolean timesfg = false;
		if(!"".equals(TransTxKind)) {
			selectfg = true;
		}
		
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		//this.limit=Integer.MAX_VALUE;//查全部
		this.limit=120;//查全部
		
//		String OccursName[]= {"OOSeq","OOCustId","OOCustNo","OOCaseSeq","OOCustName","OOTxSts","OORemark","OOAcctDate","OOEntryDate","OORepayDate","OOTmpAmt","OOOverPayAmt","OOPayPeriod","OOPayAmt","OORevivPeriod","OORevivAmt","OOAcumTmpAmt","OOSklShareAmt","OOApprAmt","OOReturnAmt","OOTitaTlrNo","OOTitaTxtNo"};
//		int OccursNameL=OccursName.length;
		String sql="";
		List<String[]> Data=null;
		try {
			sql = l597AServiceImpl.FindL597A(titaVo, AcDate, IsMainFin, SearchOption, SearchDetail, Export, IsBtn,"");
		} catch (Exception e) {
			//E5003 組建SQL語法發生問題
			logger.info("L5051 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5003","");
		}
		try {
			Data=l597AServiceImpl.FindL597A(l597AServiceImpl.FindData(this.index, this.limit, sql, titaVo, AcDate, IsMainFin, SearchOption, SearchDetail, Export, IsBtn),"L597A");
		} catch (Exception e) {
			//E5004 讀取DB時發生問題
			logger.info("L5051 ErrorForDB="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		
		if(Data!=null && Data.size()!=0) {
			int Seq=0;
			for(String [] lData:Data) {
				for(int i=0;i<lData.length;i++) {
					String strValue=lData[i];
					if(strValue!=null) {
						
					}else {
						strValue="";
					}
					//occursList1.putParam(OccursName[i],strValue);
				}
				
				String TxKind = lData[6];
				String NewtransTxKind = "";
				
				if(SearchOption == 2 || SearchOption == 3 || SearchOption == 12) {
				  NegTransId NegTransIdVO = new NegTransId();
				  NegTransIdVO.setAcDate(parse.stringToInteger(DcToRoc(lData[8])));
				  NegTransIdVO.setTitaTlrNo(lData[21]);
				  NegTransIdVO.setTitaTxtNo(parse.stringToInteger(lData[22]));
				
				  NegTrans NegTransVO = new NegTrans();
				  NegTransVO = sNegTransService.findById(NegTransIdVO);
				  if (NegTransVO != null) {
				    Map<String, String> Map = NegCom.trialNegtrans(NegTransVO, "0", TxKind, titaVo);
				    NewtransTxKind = Map.get("NewtransTxKind");//試算交易別   
				  }
				} // if
				
				OccursList occursList1 = new OccursList();
				if(selectfg) { // 試算交易別有值
					
					if(NewtransTxKind.equals(TransTxKind)) {
						Seq++;
						occursList1.putParam("OOSeq",Seq);//
						occursList1.putParam("OOCustId",lData[2]);//身分證號
						occursList1.putParam("OOCaseSeq",lData[3]);//案件序號
						occursList1.putParam("OOCustNo",lData[4]);//戶號
						occursList1.putParam("OOCustName",lData[5]);//戶名
						occursList1.putParam("OOTxSts",TxKind);//交易別
						occursList1.putParam("OORemark",lData[7]);//備註
						occursList1.putParam("OOAcctDate",DcToRoc(lData[8]));//會計日
						occursList1.putParam("OOEntryDate",DcToRoc(lData[9]));//入帳日
						occursList1.putParam("OORepayDate",DcToRoc(lData[10]));//入帳還款日
						occursList1.putParam("OOTmpAmt",lData[11]);//暫收金額
						occursList1.putParam("OOOverPayAmt",lData[12]);//溢繳款
						occursList1.putParam("OOPayPeriod",lData[13]);//繳期數
						occursList1.putParam("OOPayAmt",lData[14]);//還款金額
						occursList1.putParam("OORevivPeriod",lData[15]);//應還期數
						occursList1.putParam("OORevivAmt",lData[16]);//應還金額
						occursList1.putParam("OOAcumTmpAmt",lData[17]);//累溢短收
						occursList1.putParam("OOSklShareAmt",lData[18]);//新壽攤分
						occursList1.putParam("OOApprAmt",lData[19]);//撥付金額
						occursList1.putParam("OOReturnAmt",lData[20]);//退還金額
						occursList1.putParam("OOTitaTlrNo",lData[21]);//經辦
						occursList1.putParam("OOTitaTxtNo",lData[22]);//交易序號
						occursList1.putParam("OOTxSts1", NewtransTxKind );//試算交易別
						timesfg = true;
						this.totaVo.addOccursList(occursList1);
					} else {
						continue;
					}
					
				} else {
					
					Seq++;
					occursList1.putParam("OOSeq",Seq);//
					occursList1.putParam("OOCustId",lData[2]);//身分證號
					occursList1.putParam("OOCaseSeq",lData[3]);//案件序號
					occursList1.putParam("OOCustNo",lData[4]);//戶號
					occursList1.putParam("OOCustName",lData[5]);//戶名
					occursList1.putParam("OOTxSts",TxKind);//交易別
					occursList1.putParam("OORemark",lData[7]);//備註
					occursList1.putParam("OOAcctDate",DcToRoc(lData[8]));//會計日
					occursList1.putParam("OOEntryDate",DcToRoc(lData[9]));//入帳日
					occursList1.putParam("OORepayDate",DcToRoc(lData[10]));//入帳還款日
					occursList1.putParam("OOTmpAmt",lData[11]);//暫收金額
					occursList1.putParam("OOOverPayAmt",lData[12]);//溢繳款
					occursList1.putParam("OOPayPeriod",lData[13]);//繳期數
					occursList1.putParam("OOPayAmt",lData[14]);//還款金額
					occursList1.putParam("OORevivPeriod",lData[15]);//應還期數
					occursList1.putParam("OORevivAmt",lData[16]);//應還金額
					occursList1.putParam("OOAcumTmpAmt",lData[17]);//累溢短收
					occursList1.putParam("OOSklShareAmt",lData[18]);//新壽攤分
					occursList1.putParam("OOApprAmt",lData[19]);//撥付金額
					occursList1.putParam("OOReturnAmt",lData[20]);//退還金額
					occursList1.putParam("OOTitaTlrNo",lData[21]);//經辦
					occursList1.putParam("OOTitaTxtNo",lData[22]);//交易序號
					occursList1.putParam("OOTxSts1",NewtransTxKind);//試算交易別
					this.totaVo.addOccursList(occursList1);
				}
				
				
				
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if(Data !=null && Data.size() >=this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				//this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			
			if(selectfg && !timesfg) {
				throw new LogicException(titaVo, "E2003","");
			}
		}else {
			//E2003 查無資料
			throw new LogicException(titaVo, "E2003","");
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public String DcToRoc(String Dc) {
		String Roc="0";
		if(Dc!=null && Dc.length()!=0) {
			int DcL=Dc.length();
			if(DcL==8) {
				Roc=String.valueOf(Integer.parseInt(Dc)-19110000);
			}
		}
		return Roc;
	}	
}