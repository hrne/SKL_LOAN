package com.st1.itx.trade.L5;

//import static java.util.Collections.sort;

//import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
//import java.io.FileWriter;
//import java.util.Vector;
import java.util.List;
import java.util.Map;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.service.NegMainService;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicAtomDetail;
import com.st1.itx.db.domain.JcicAtomMain;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegFinShareId;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
/*DB服務*/
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicAtomDetailService;
import com.st1.itx.db.service.JcicAtomMainService;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* FilePath=X,20<br>
*/

@Service("L5706")
@Scope("prototype")
/**
 *
 *
 * @author Jacky
 * @version 1.0.0
 */
public class L5706 extends TradeBuffer {
	private static final Logger logger=LoggerFactory.getLogger(L5706.class);
	@Autowired
	public NegFinShareService sNegFinShareService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicAtomMainService sJcicAtomMainService;
	@Autowired
	public JcicAtomDetailService sJcicAtomDetailService;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public NegCom negCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public FileCom FileCom;
	
	@Autowired
	public DataLog dataLog;
	
	@Value("${iTXInFolder}")
	private String inFolder = "";
	
	int updInsDb=1;//1:異動 0:不異動
	int errorMsg=1;//回報錯誤 1:回報 0:不回報
	Map<String,String> OccursDataValue= new HashMap<String,String>();
	String OccursData[]= {"OORow","OOCode","OODataType","OOMainRemark","OOFiledName","OOFiledType","OODetailRemark","OOValue"};
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5706 ");
		this.totaVo.init(titaVo);
		this.info("L5706 TitaVo=["+titaVo+"]");
		//路徑
		String FilePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();
		
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		this.info("L5706 First this.index=["+this.index+"]");
		
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 1;// Integer.MAX_VALUE查全部
		//目前occurst長度
		//int OccursL=1030;
		
		//測試用路徑
		int TestPath=0;//0:不使用測試路徑 1:使用測試路徑
		if(TestPath==1) {
			String Path="D:\\新光人壽\\開發資料\\L5\\L5706檔案匯入資料格式\\";//St1Share的路徑
			String FineName="";
//			FineName="L5706債權比例分攤資料維護(匯入檔案)(單筆).txt";
//			FineName="L5706債權比例分攤資料維護(匯入檔案)(註銷日期和註銷本金)(單筆).txt";
//			FineName="L5706債權比例分攤資料維護(匯入檔案)(多筆).txt";
			FilePath=Path+FineName;
		}
		
		
		File file=null;//讀取檔案
		String ReadFileName="";
		try {
			ReadFileName=FilePath;//讀取檔案
			file=new File(ReadFileName);
		}catch (Exception e) {
			//E5006 未預期的錯誤
			throw new LogicException(titaVo, "E5006","檔案讀取發生問題");
		}
		
		if(file.exists()) {
			this.info("L5706 File Exists True");
			//byte[] byt;
			
			
			ArrayList<String> lBr = new ArrayList<>();
			// 編碼參數，設定為UTF-8 || big5
			try {
				lBr=FileCom.intputTxt(FilePath, "big5");
			} catch (IOException e) {
				this.info("L5706(" + FilePath + ") : " + e.getMessage());
//						throw new LogicException("E0014", "BS420(" + filePath1 + ")");
				//E5006 未預期的錯誤
				throw new LogicException(titaVo, "E5006","檔案讀取發生問題");
			}

			
			
			//int TestType=0;//0:Head 1:Body 2:End

			try {
				int lBrS=lBr.size();
				this.info("L5706 lBrS=["+lBrS+"]");

//				int TestRow=this.index+this.limit;
//				if(TestRow<=lBrS-this.limit) {
//					Row=TestRow;
//					
//					/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//					//this.info("L5706 setReturnIndex=["+this.setIndexNext()+"],Row=["+Row+"]");
//					
////					this.totaVo.setMsgEndToEnter();// 手動折返
//				}else {
//					Row=lBrS-1;
//				}
				int Row=this.index;
				int Row1=Row+this.limit;
				if(Row1<lBrS) {
					titaVo.setReturnIndex(Row1);//this.setIndexNext()這玩意會自動+1;
					this.totaVo.setMsgEndToAuto();// 自動折返
				}
				int RowEnd=0;
				if(Row==0) {
					RowEnd=Row;
					RowEnd=RowEnd+1;
					titaVo.setReturnIndex(RowEnd);
				} else {
					Row++;
					RowEnd = Row;
				}
				this.info("L5706 First,this.index=["+this.index+"] ,this.limit=["+this.limit+"],Row=["+Row+"]");
				for (int i=Row;i<=RowEnd && Row < lBrS;i++) {
					String ThisLine=lBr.get(i);
					
					this.info("L5706 Infor i=["+(i)+"],Row=["+Row+"],this.index=["+this.index+"] ,this.limit=["+this.limit+"] ");
					this.info("L5706 ThisLine =["+ThisLine+"]");
					if(i==0) {
						//資料檢核
						if(ThisLine.indexOf("JCIC-INQ-BARE-V01-458")!=0) {
							// E5009 資料檢核錯誤
							if(errorMsg==1) {
								throw new LogicException(titaVo, "E5009", "首筆不為JCIC-INQ-BARE-V01-458開頭");
							}
						}
						continue;
					}
					
					
					
					//TR 是結尾的記號
					if(ThisLine!=null && ThisLine.length()!=0 && !("TR").equals(ThisLine.substring(0,2))) {
						OccursDataValue= new HashMap<String,String>();//更新
						
						String Code=ThisLine.substring(33,39);
						OccursDataValue.put(OccursData[0],String.valueOf(i+1));//OORow
						OccursDataValue.put(OccursData[1],Code);//OOCode
						this.info("L5706 Code=["+(Code)+"]");
						String ThisLineUse=ThisLine.substring(39,ThisLine.length());
						Map<String,String[]> mLineCut=cutSkill(titaVo,Code,ThisLineUse);
						
						if(mLineCut!=null) {
							for(String Key:mLineCut.keySet()) {
								String Data[]=mLineCut.get(Key);
								
								this.info("Key=["+Key+"],value=["+Data[0]+"],Remark=["+Data[1]+"]");
							}
						}else {
							continue;
						}
						
						
//						//案件種類為債協的代號
//						String CaseKindCode1Function[]= {"AAS003","ZZS240","ZZS260","ZZM260","ZZS263","ZZM262","ZZM263","ZZM264"};
//						//案件種類為調解的代號
//						String CaseKindCode2Function[]= {"ZDM411","ZDM421"};
//						//案件種類為更生的代號
//						String CaseKindCode3Function[]= {"ZZM280","ZZM281","ZZM282","ZZM284","ZZM300","ZZM305","ZZM306","ZZM308","ZZS300"};
//						//案件種類為清算的代號
//						String CaseKindCode4Function[]= {"ZZM280","ZZM283","ZZM285"};
						
//						ZZM260 各債權金融機構無擔保債權暨還款分配資料 (多筆 Z98)
//						ZZM261 債務人所有延期繳款資料 (多筆 Z98)
//						ZZM262 單獨受償後各機構無擔保債權暨還款分配資料 (多筆 Z98)
//						ZZM263 金融機構無擔保債務變更還款條件協議資料 (Z98 金融機構無擔保債務變更還款條件協議資料)
//						ZZM264 債權金融機構剩餘無擔保債權暨還款分配資料 (Z98 各債權金融機構剩餘無擔保債權暨還款分配資料(變更還款後)) 
//						ZZS240 債務人基本資料 (單筆 Z96 Z98)
//						ZZS260 金融機構無擔保債務協議資料 (單筆 Z98)
//						ZZS261 債務人繳款資料 (單筆 Z98 債務人繳款資料)
//						ZZS262 金融機構無擔保債務協議資料二階段還款方案 (單筆 Z98)
//						ZZS263 債務人繳款資料(9904起) (單筆 Z98 債務人繳款資料)
						
						//第三字元 'S' 為單筆, 'M' 為多筆, 'I' 為背景資訊; * 尚未開放查詢
						switch (Code){
							case "AAS003":
								doAAS003(titaVo,Code,mLineCut);    
								break;
							case "ZZS240": 
								doZZS240(titaVo,Code,mLineCut);
								break;
							case "ZZS260":
								doZZS260(titaVo,Code,mLineCut);
								break;
							case "ZZS261":
								break;
							case "ZZM261":
								break;
							case "ZZS262":
								break;
							case "ZZS263":
								doZZS263(titaVo,Code,mLineCut);
								break;
							case "ZZM262": //單獨受償後各機構無擔保債權暨還款分配資料
								doZZM262(titaVo,Code,mLineCut);
								break;
							case "ZZM263": //變更還款條件 UPDATE TBJCICMAIN
								doZZM263(titaVo,Code,mLineCut);
								break;
							case "ZZM264": //變更還款條件 UPDATE TBJCICSHARE
								doZZM264(titaVo,Code,mLineCut);
								break;
							case "ZZM260": //一般債權機構代號及名稱,債權比例
								doZZM260(titaVo,Code,mLineCut);
								break;
							default:
								break;
						}
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.info("L5706 Error BrReady e=["+e.toString()+"]");
				throw new LogicException(titaVo, "E5006","寫入資料庫發生錯誤");
			}
			
		}else {
			String ErrorMsg="檔案不存在,請查驗路徑.\r\n"+ReadFileName;
			//E5006 未預期的錯誤
			throw new LogicException(titaVo, "E5006",ErrorMsg);
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public String getCutSkill(TitaVo titaVo,String Code,Map<String,String[]> mData,String Key) throws LogicException {
		if(!mData.containsKey(Key)) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "資料格式代碼("+Code+"),無此關鍵字("+Key+")");
		}
		String str=mData.get(Key)[0];
		return str;
	}
	/**
	 * 
	 * @param titaVo
	 * @param Code 功能代碼共6碼
	 * @param ThisLineUse 要被切的資料欄位
	 * @return 功能代碼的欄位名稱,StringArray 0:切割後的資料 1:欄位說明
	 * @throws LogicException 錯誤訊息
	 * @throws UnsupportedEncodingException
	 */
	public Map<String,String[]> cutSkill(TitaVo titaVo,String Code,String ThisLineUse) throws LogicException, UnsupportedEncodingException {
		Map<String,String[]> mData=new HashMap<>();
		
		JcicAtomMain tJcicAtomMain=sJcicAtomMainService.findById(Code, titaVo);
		if(tJcicAtomMain!=null) {
			OccursDataValue.put(OccursData[2],tJcicAtomMain.getDataType());//OODataType
			OccursDataValue.put(OccursData[3],tJcicAtomMain.getRemark());//OOMainRemark
			
			Slice<JcicAtomDetail> sJcicAtomDetail=sJcicAtomDetailService.findByFunctionCode(Code, 0, Integer.MAX_VALUE, titaVo);
			List<JcicAtomDetail> lJcicAtomDetail = sJcicAtomDetail == null ? null : sJcicAtomDetail.getContent();
			if(lJcicAtomDetail!=null && lJcicAtomDetail.size()>0) {
				
			}else {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "JcicAtomDetail無此資料格式("+Code+")");
			}
			//ThisLineUse="";
			
			int StartLength=0;
			byte[] byt=ThisLineUse.getBytes("big5");
			int bytL=byt.length;
			for(JcicAtomDetail JcicAtomDetailVo:lJcicAtomDetail) {
				String FiledName=JcicAtomDetailVo.getFiledName();
				//int DataOrder=JcicAtomDetailVo.getDataOrder();
				String FiledType=JcicAtomDetailVo.getFiledType();
				//String FunctionCode=JcicAtomDetailVo.getFunctionCode();
				String Remark=JcicAtomDetailVo.getRemark();
				
				int Length=0;
				if(FiledType!=null) {
					int FiledTypeL=FiledType.length();
					
					int FiledTypeL1=FiledTypeL-1;
					
					if(FiledType.indexOf("Char")!=-1) {
						Length=Integer.parseInt(FiledType.substring(5,FiledTypeL1));
					}else if(FiledType.indexOf("Num")!=-1) {
						Length=Integer.parseInt(FiledType.substring(4,FiledTypeL1));
					}else {
						// E5009 資料檢核錯誤 
						throw new LogicException(titaVo, "E5009", "出現未定義的資料型態["+FiledType+"]");
					}
					//int OrgLenth=Length;
					//int OrgStartLength=StartLength;
					
					if(StartLength>bytL) {
						this.info("L5706 Error ThisLineUse=["+ThisLineUse+"]");
						//E5009 資料檢核錯誤 
						throw new LogicException(titaVo, "E5009", "資料長度有問題,請查驗.");
					}
					
					String Value=ByteToString(titaVo,byt, StartLength, Length);
					StartLength=StartLength+Length;
					
					String data[]= {Value,Remark};
					mData.put(FiledName,data);
					
					//"OOFiledName","OOFiledType","OODetailRemark","OOValue"
					OccursDataValue.put(OccursData[4],FiledName);//OOFiledName
					OccursDataValue.put(OccursData[5],FiledType);//OOFiledType
					
					OccursDataValue.put(OccursData[6],Remark);//OODetailRemark
					OccursDataValue.put(OccursData[7],Value);//OOValue
					//this.info("Key=["+FiledName+"],value=["+Value+"],Remark=["+Remark+"],StartLength=["+OrgStartLength+"],Length=["+OrgLenth+"]");
					
					OccursList occursList = new OccursList();
					for(String Key:OccursDataValue.keySet()) {
						occursList.putParam(Key,OccursDataValue.get(Key));
					}
					this.totaVo.addOccursList(occursList);
				}else {
					// E5009 資料檢核錯誤 
					throw new LogicException(titaVo, "E5009", "FiledType欄位不可為空值");
				}
				
			}
		}else {
//			if(errorMsg==1) {
//				// E5009 資料檢核錯誤 
//				throw new LogicException(titaVo, "E5009", "JcicAtomMain無此資料格式("+Code+")");
//			}else {
//				OccursDataValue.put(OccursData[2],"未知記號");//OODataType
//				OccursDataValue.put(OccursData[3],"無法判別的記號");//OOMainRemark
//				OccursDataValue.put(OccursData[4],"UNKNOW");//OOFiledName
//				OccursDataValue.put(OccursData[5],"CHAR(*)");//OOFiledType
//				
//				OccursDataValue.put(OccursData[6],"未知");//OODetailRemark
//				OccursDataValue.put(OccursData[7],"");//OOValue
//				
//				OccursList occursList = new OccursList();
//				for(String Key:OccursDataValue.keySet()) {
//					occursList.putParam(Key,OccursDataValue.get(Key));
//				}
//				this.totaVo.addOccursList(occursList);
//			}
			OccursDataValue.put(OccursData[2],"未知記號");//OODataType
			OccursDataValue.put(OccursData[3],"無法判別的記號");//OOMainRemark
			OccursDataValue.put(OccursData[4],"UNKNOW");//OOFiledName
			OccursDataValue.put(OccursData[5],"CHAR(*)");//OOFiledType
			
			OccursDataValue.put(OccursData[6],"未知");//OODetailRemark
			OccursDataValue.put(OccursData[7],"");//OOValue
			
			OccursList occursList = new OccursList();
			for(String Key:OccursDataValue.keySet()) {
				occursList.putParam(Key,OccursDataValue.get(Key));
			}
			this.totaVo.addOccursList(occursList);
		}
		return mData;
	}
	public BigDecimal sumAmt(String Amt1,String Amt2,String Amt3) {
		BigDecimal sum=BigDecimal.ZERO;
		String Data[]= {Amt1,Amt2,Amt3};
		for(int i=0;i<Data.length;i++) {
			String thisData=Data[i].replace(" ","");
			if(thisData!=null && thisData.trim().length()!=0) {
				sum=sum.add(new BigDecimal(thisData));
			}
		}
		return sum;
	}
	public String ByteToString (TitaVo titaVo,byte[] byt,int Start ,int len) throws LogicException {
		//ByteToString
		
		byte[] NewByt=new byte[len];
		for(int i=0;i<len;i++) {
			NewByt[i]=byt[(Start+i)];
		}
		String str=new String(NewByt);
		try {
			//str=new String(str.getBytes("utf-8"));
			str=new String(str.getBytes("big5"));
		} catch (UnsupportedEncodingException e) {
			this.info("L5706 ByteToString="+e.toString() );
			//E5010 資料格式轉換有誤
			throw new LogicException(titaVo, "E5010","");
		}
		return str;
	}
	public String TrimStart (String str,char c) {
		String NewStr=str;
		if(str!=null) {
			int strL=str.length();
			for(int i=0;i<strL;i++) {
				if(str.charAt(i)==c) {
					continue;
				}else {
					NewStr=str.substring(i,strL);
					break;
				}
			}
		}
		return NewStr;
	}
	public boolean IsNullOrEmpty(String str) {
		if(str!=null && str.trim().length()!=0) {
			return false;
		}else {
			return true;
		}
	}
	public int CheckCustId(TitaVo titaVo,String id) throws LogicException {
		this.info("L5706 CheckCustId Run");
		int CustNo=0;
		//查驗此客戶編號存在不存在,回傳CustNo
		if(id!=null && id.trim().length()!=0) {
			CustMain CustMainVO=sCustMainService.custIdFirst(id, titaVo);
			if(CustMainVO!=null) {
				CustNo=CustMainVO.getCustNo();
				
				if(CustNo==0) {
					//取號
					CustNo=negCom.getNewCustNo(id,titaVo);
					//E5008 戶號為0
					//throw new LogicException(titaVo, "E5008","");
				}
			}else {
				if(errorMsg==1) {
					//E0001 查詢資料不存在
					throw new LogicException(titaVo, "E0001","查無客戶主檔資料["+id+"]");
				}
				
			}
		}else {
			if(errorMsg==1) {
				//E5007 身分證字號為空值
				throw new LogicException(titaVo, "E5007","");
			}
		}
		
		
		return CustNo;
	}
	public NegMain CheckNegMain(TitaVo titaVo,int CustNo) throws LogicException {
		this.info("L5706 CheckNegMain Run");
		NegMain NegMainVO=new NegMain();
		if(CustNo!=0) {
			NegMainVO=sNegMainService.CustNoFirst(CustNo, titaVo);
			if(NegMainVO!=null) {
				return NegMainVO;
			}else {
				if(errorMsg==1) {
					//E0001 查詢資料不存在
					//throw new LogicException(titaVo, "E0001","NegMain");
				}
			}
		}
		return NegMainVO;
	}
	
	public int StrToInt(TitaVo titaVo,String value,String DateName) throws LogicException {
		int IntValue=0;
		value=TrimStart(value,'0');
		if(IsNullOrEmpty(value)){
			IntValue=Integer.parseInt(value);
		}
		return IntValue;
	}
	
	public BigDecimal StringToBigDecimal(TitaVo titaVo,String strBd) throws LogicException {
		BigDecimal BdV=BigDecimal.ZERO;
		strBd=TrimStart(strBd,'0');
		if(IsNullOrEmpty(strBd)){
			strBd="0";
		}
		BdV=parse.stringToBigDecimal(strBd);
		return BdV;
	}
	public int DateRocToDC(TitaVo titaVo,String Date,String DateName) throws LogicException {
		Date=Date.trim();
		int Dc=0;
		if(Date!=null && Date.length()!=0) {
			int DateL=Date.length();
			if(DateL<=7) {
				Dc=(Integer.parseInt(Date)+19110000);
			}
		}else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", DateName+":不可為空值");
		}
		
		return Dc;
	}
	
	public void doZZS240(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//ZZS240	債務協商	債務人基本資料 (單筆 Z96 Z98)
		
		//IDN_BAN	Char(10)	身分證號
		//MAIN_CODE	Char(3)	最大債權金融機構
		//MAIN_CODE_NAME	Char(24)	最大債權金融機構名稱
		//RECEIVE_DATE	Char(7)	協商申請日期
		//REG_ADDR	Char(76)	戶籍地址
		//COM_ADDR	Char(76)	通訊地址
		//REG_TELNO	Char(16)	戶籍電話
		//COM_TELNO	Char(16)	通訊電話
		//MOBIL_NO	Char(16)	行動電話
		//FILLER	Char(30)	保留欄位
		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
		//String MAIN_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE_NAME");//最大債權金融機構名稱
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		String REG_ADDR=getCutSkill(titaVo,Code,mLineCut,"REG_ADDR");//戶籍地址
		String COM_ADDR=getCutSkill(titaVo,Code,mLineCut,"COM_ADDR");//通訊地址
		String REG_TELNO=getCutSkill(titaVo,Code,mLineCut,"REG_TELNO");//戶籍電話
		String COM_TELNO=getCutSkill(titaVo,Code,mLineCut,"COM_TELNO");//通訊電話
		String MOBIL_NO=getCutSkill(titaVo,Code,mLineCut,"MOBIL_NO");//行動電話
		
		JcicZ048Id tJcicZ048Id=new JcicZ048Id();
		tJcicZ048Id.setCustId(IDN_BAN);
		tJcicZ048Id.setRcDate(DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"));
		tJcicZ048Id.setSubmitKey(MAIN_CODE);
		JcicZ048 tJcicZ048Vo=sJcicZ048Service.findById(tJcicZ048Id, titaVo);
		if(tJcicZ048Vo!=null) {
			//已有資料
		}else {
			//未有資料
			tJcicZ048Vo=new JcicZ048();
			tJcicZ048Vo.setJcicZ048Id(tJcicZ048Id);
			tJcicZ048Vo.setTranKey("A");
			tJcicZ048Vo.setCustRegAddr(REG_ADDR.trim());//債務人戶籍之郵遞區號及地址
			tJcicZ048Vo.setCustComAddr(COM_ADDR.trim());//債務人通訊地之郵遞區號及地址
			tJcicZ048Vo.setCustRegTelNo(REG_TELNO.trim());//債務人戶籍電話
			tJcicZ048Vo.setCustComTelNo(COM_TELNO.trim());//債務人通訊電話
			tJcicZ048Vo.setCustMobilNo(MOBIL_NO.trim());//債務人行動電話
			try {
				sJcicZ048Service.insert(tJcicZ048Vo, titaVo);
			} catch (DBException e) {
				//E0005	新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", "債務人基本資料");
			}
		}
		
	}
	
	public void doAAS003(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//AAS003 自然人姓名,身分證補發,通報,補充註記 (單筆, 取代AAS001)
		//IDN	Char(10)	身分證號
		//PNAME	Char(40)	中文姓名
		//IS_LOST	Char(1)	身分證註銷或一年內掛失紀錄 YN  自 95.07.15 改為空白
		//IS_WANTED	Char(1)	通報案件紀錄 YN
		//IS_NOTE	Char(1)	是否有補充註記1,2,A,N
		//NOTELIST	Char(9)	補充註記  自 97.04.11 改為空白
		//WANTEDLIST	Char(9)	通報案件紀錄  (對照表)
		//FOREIGNER_MARK	Char(1)	在台無戶籍人士身分證號對照索引註記  'Y','N'
		
//		String IDN=getCutSkill(titaVo,Code,mLineCut,"IDN");//身分證號
//		String PNAME=getCutSkill(titaVo,Code,mLineCut,"PNAME");//中文姓名
//		String IS_LOST=getCutSkill(titaVo,Code,mLineCut,"IS_LOST");//身分證註銷或一年內掛失紀錄 YN  自 95.07.15 改為空白
//		String IS_WANTED=getCutSkill(titaVo,Code,mLineCut,"IS_WANTED");//通報案件紀錄 YN
//		String IS_NOTE=getCutSkill(titaVo,Code,mLineCut,"IS_NOTE");//是否有補充註記1,2,A,N
//		String NOTELIST=getCutSkill(titaVo,Code,mLineCut,"NOTELIST");//補充註記  自 97.04.11 改為空白
//		String WANTEDLIST=getCutSkill(titaVo,Code,mLineCut,"WANTEDLIST");//通報案件紀錄  (對照表)
//		String FOREIGNER_MARK=getCutSkill(titaVo,Code,mLineCut,"FOREIGNER_MARK");//在台無戶籍人士身分證號對照索引註記  'Y','N'
		
		//檢查是否有客戶資料
		//不用處理
		
		//新壽原程式碼用來找CustNo戶號用
	}
	
	
	public void doZZS260(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//ZZS260	債務協商	金融機構無擔保債務協議資料 (單筆 Z98)
		
		//IDN_BAN	Char(10)	身分證號
		//MAIN_CODE	Char(3)	最大債權金融機構
		//MAIN_CODE_NAME	Char(24)	最大債權金融機構名稱
		//RECEIVE_DATE	Char(7)	協商申請日期
		//PASS_DATE	Char(7)	協議完成日期
		//INTERVIEW_DATE	Char(7)	面談日期
		//SIGN_DATE	Char(7)	簽約完成日期
		//PERIOD	Num(3)	期數
		//RATE	Num(5)	利率
		//FIRST_PAY_DATE	Char(7)	首期應繳款日期
		//PAY_AMOUNT	Num(9)	月付金
		//PAY_ACCOUNT	Char(20)	繳款帳號
		//EXP_LOAN_AMT	Num(9)	信用貸款債務總簽約金額
		//CASH_CARD_AMT	Num(9)	現金卡債務總簽約金額
		//CREDIT_CARD_AMT	Num(9)	信用卡債務簽約總金額
		//TOTAL_AMT	Num(9)	總簽約金額合計
		//FILLER	Char(30)	保留欄位
		
		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
		//String MAIN_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE_NAME");//最大債權金融機構名稱
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		//String PASS_DATE=getCutSkill(titaVo,Code,mLineCut,"PASS_DATE");//協議完成日期
		//String INTERVIEW_DATE=getCutSkill(titaVo,Code,mLineCut,"INTERVIEW_DATE");//面談日期
		//String SIGN_DATE=getCutSkill(titaVo,Code,mLineCut,"SIGN_DATE");//簽約完成日期
		String PERIOD=getCutSkill(titaVo,Code,mLineCut,"PERIOD");//期數
		String RATE=getCutSkill(titaVo,Code,mLineCut,"RATE");//利率
		String FIRST_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"FIRST_PAY_DATE").trim();//首期應繳款日期
		String PAY_AMOUNT=getCutSkill(titaVo,Code,mLineCut,"PAY_AMOUNT");//月付金
//		String PAY_ACCOUNT=getCutSkill(titaVo,Code,mLineCut,"PAY_ACCOUNT");//繳款帳號
//		String EXP_LOAN_AMT=getCutSkill(titaVo,Code,mLineCut,"EXP_LOAN_AMT");//信用貸款債務總簽約金額
//		String CASH_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CASH_CARD_AMT");//現金卡債務總簽約金額
//		String CREDIT_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CREDIT_CARD_AMT");//信用卡債務簽約總金額
		String TOTAL_AMT=getCutSkill(titaVo,Code,mLineCut,"TOTAL_AMT");//總簽約金額合計
		
		//查驗是否已有客戶主檔
		this.info("L5706 CheckCustId ZZS260");
		int CustNo=CheckCustId(titaVo,IDN_BAN);
		this.info("L5706 CheckCustId ZZS260 End");
//		//查驗是否已有債協主檔,沒有就新增
//		this.info("L5706 CheckNegMain ZZS260");
//		NegMain NegMainVO=CheckNegMain(titaVo,CustNo);
//		this.info("L5706 CheckNegMain ZZS260 End");
//		if(NegMainVO!=null) {
//			
//		}else {
//			this.info("L5706 NegMainInsert ZZS260");
//			//NegMainVO=NegMainInsert(0,titaVo,IDN_BAN,CustNo,pay_status,receive_date,pay_amount,period,rate,first_pay_date,maxmain_code,total_amt,receive_date1);
//			this.info("L5706 NegMainInsert ZZS260 End");
//		}
		
		//檢查是否有NEGMAIN的主檔-無則新增
		NegMain NegMainVO=sNegMainService.CustNoAndApplDateFirst(CustNo, DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"),MAIN_CODE, titaVo);
		if(NegMainVO!=null) {
			//已有資料
			//不做處理
			if(errorMsg==1) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009","身份證字號["+IDN_BAN+"]已存在,債權資料,不可新增.");
			}
			
		}else {
			//未有資料-新增處理
			NegMainId NegMainIdVo=new NegMainId();
			NegMainIdVo.setCaseSeq(1);
			NegMainIdVo.setCustNo(CustNo);
			
			NegMain NegMainVo=new NegMain();
			
			NegMainVo.setNegMainId(NegMainIdVo);
			NegMainVo.setCaseSeq(NegMainIdVo.getCaseSeq());
			NegMainVo.setCustNo(NegMainIdVo.getCustNo());
			NegMainVo.setMainFinCode(MAIN_CODE);
			
			NegMainVo.setApplDate(DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"));
			NegMainVo.setTotalPeriod(StrToInt(titaVo,PERIOD,"期數"));
			NegMainVo.setIntRate(StringToBigDecimal(titaVo,RATE));
			NegMainVo.setFirstDueDate(DateRocToDC(titaVo,FIRST_PAY_DATE,"首次應繳日"));
			NegMainVo.setDueAmt(StringToBigDecimal(titaVo,PAY_AMOUNT));
			NegMainVo.setTotalContrAmt(StringToBigDecimal(titaVo,TOTAL_AMT));//簽約總金額
			
			
			CustMain CustMainVO=sCustMainService.custIdFirst(IDN_BAN, titaVo);
			String CustLoanKind="";
			if(CustMainVO!=null) {
				List<String> CustTypeCode= new ArrayList<String>();
				CustTypeCode.add("05");
				//戶別
				if(CustTypeCode.contains(CustMainVO.getCustTypeCode())) {
					//是保代戶
					CustLoanKind="2";
				}else {
					CustLoanKind="1";
				}
			}else {
				if(errorMsg==1) {
					//E0001 查詢資料不存在
					throw new LogicException(titaVo, "E0001","查無客戶主檔資料["+IDN_BAN+"]");
				}
				
			}
			
			NegMainVo.setCaseKindCode("1");//案件種類  1:協商;2:調解;3:更生;4:清算
			NegMainVo.setStatus("0");//債權戶況 0:正常 1:已變更 2:毀諾 3:結案 4:未生效
			NegMainVo.setCustLoanKind(CustLoanKind);//債權戶別"1:放款戶 ;2:保貸戶;客戶主檔:保貸別
			NegMainVo.setTwoStepCode("N");//二階段註記
			
			NegMainVo.setDeferYMStart(0);//延期繳款年月(起)
			NegMainVo.setDeferYMEnd(0);//延期繳款年月(訖)

			NegMainVo.setPrincipalBal(NegMainVo.getPrincipalBal());//總本金餘額
			
			NegMainVo.setNextPayDate(NegMainVo.getFirstDueDate());//下次應繳日
			int LastDueDate=negCom.AdjMonth(NegMainVo.getFirstDueDate(),NegMainVo.getTotalPeriod(),0);
			NegMainVo.setLastDueDate(LastDueDate);//還款結束日
			String IsMainFin="";
			if(("458").equals(MAIN_CODE)) {
				IsMainFin="Y";
			}else {
				IsMainFin="N";
			}
			NegMainVo.setIsMainFin(IsMainFin);//是否最大債權

			NegMainVo.setStatusDate(dateUtil.getNowIntegerForBC());//戶況日期
			
			if(updInsDb==1) {
				try {
					sNegMainService.insert(NegMainVo, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "債務協商案件主檔");
				}
			}
		}
	}
	
	public void doZZM260(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//ZZM260	債務協商	各債權金融機構無擔保債權暨還款分配資料 (多筆 Z98)
		//IDN_BAN	Char(10)	身分證號
		//RECEIVE_DATE	Char(7)	協商申請日期
		//MAIN_CODE	Char(3)	最大債權金融機構
		//BANK_CODE	Char(3)	債權金融機構
		//BANK_CODE_NAME	Char(24)	債權金融機構名稱
		//EXP_LOAN_AMT	Num(9)	信用貸款債權金額
		//CASH_CARD_AMT	Num(9)	現金卡債權金額
		//CREDIT_CARD_AMT	Num(9)	信用卡債權金額
		//DISP_AMT	Num(9)	每期可分配金額
		//LOAN_RATE	Num(6)	佔全部協商無擔保債權比例  小數點兩位, 例如023.45
		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
		String BANK_CODE=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE");//債權金融機構
		//String BANK_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME");//債權金融機構名稱
		String EXP_LOAN_AMT=getCutSkill(titaVo,Code,mLineCut,"EXP_LOAN_AMT");//信用貸款債權金額
		String CASH_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CASH_CARD_AMT");//現金卡債權金額
		String CREDIT_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CREDIT_CARD_AMT");//信用卡債權金額
		String DISP_AMT=getCutSkill(titaVo,Code,mLineCut,"DISP_AMT");//每期可分配金額
		String LOAN_RATE=getCutSkill(titaVo,Code,mLineCut,"LOAN_RATE");//佔全部協商無擔保債權比例  小數點兩位, 例如023.45
		
		int CustNo=CheckCustId(titaVo,IDN_BAN);
//		CustNoAndApplDateFirst
		NegMain NegMainVO=sNegMainService.CustNoAndApplDateFirst(CustNo, DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"),MAIN_CODE, titaVo);
//		sNegMainService.custNO
		if(NegMainVO!=null) {
			//已有資料
			InsertNegFinShare(titaVo,IDN_BAN,CustNo,NegMainVO.getCaseSeq(),BANK_CODE,EXP_LOAN_AMT, CASH_CARD_AMT, CREDIT_CARD_AMT, LOAN_RATE, DISP_AMT);
		}else {
			//無資料
			if(errorMsg==1) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009","身份證字號["+IDN_BAN+"]債權資料不存在.");
			}
			
		}
	}
	public void doZZS263(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//最後一筆
		//新增主檔
		//ZZS263R120032147099040245810911230000110000013860000013860001       11402100000                               
		//ZZS263N2205132441080529458       000000000000000000000000000        1200910                                   
		//ZZS263P2229779521061030458       000000000000000000000000000        1210110                                   
		//ZZS263T2223782391061109458       000000000000000000000000000        1220710                                   
		
//		ZZS263
//		N220513244
//		1080529
//		458
//		       
//		000000000
//		000000000
//		000000000        1200910
//		
//		身分證號:R120032147
//		協商申請日期:0990402
//		最大債權金融機構:458
//		最近一次繳還款日期:1091123
//		最近一次繳款金額:000011000
//		累計實際還款金額:001386000
//		累計應還款金額:001386000
//		債權狀態:1
//		清償結案或毀諾日期:       
//		前置協商註記揭露期限:1140210
//		進入第二階梯還款年月:0000
		
		//ZZS263	債務協商	債務人繳款資料(9904起) (單筆 Z98 債務人繳款資料)
		//IDN_BAN	Char(10)	身分證號
		//RECEIVE_DATE	Char(7)	協商申請日期
		//MAIN_CODE	Char(3)	最大債權金融機構
		//PAY_DATE	Char(7)	最近一次繳還款日期
		//PAY_AMT	Num(9)	最近一次繳款金額
		//PAYAMT_1	Num(9)	累計實際還款金額
		//PAYAMT_2	Num(9)	累計應還款金額
		//PAY_STATUS	Char(1)	債權狀態  1:正常, 2:結案, 3:毀諾, 4:毀諾後清償, 5:協商終止
		//CASE_DATE	Char(7)	清償結案或毀諾日期
		//LIMIT_DATE	Char(7)	前置協商註記揭露期限
		//LAD_PAY_DATE	Char(5)	進入第二階梯還款年月
		//FILLER	Char(30)	保留欄位
		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
		String PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"PAY_DATE");//最近一次繳還款日期
//		String PAY_AMT=getCutSkill(titaVo,Code,mLineCut,"PAY_AMT");//最近一次繳款金額
//		String PAYAMT_1=getCutSkill(titaVo,Code,mLineCut,"PAYAMT_1");//累計實際還款金額
//		String PAYAMT_2=getCutSkill(titaVo,Code,mLineCut,"PAYAMT_2");//累計應還款金額
		String PAY_STATUS=getCutSkill(titaVo,Code,mLineCut,"PAY_STATUS");//債權狀態  1:正常, 2:結案, 3:毀諾, 4:毀諾後清償, 5:協商終止
//		String CASE_DATE=getCutSkill(titaVo,Code,mLineCut,"CASE_DATE");//清償結案或毀諾日期
//		String LIMIT_DATE=getCutSkill(titaVo,Code,mLineCut,"LIMIT_DATE");//前置協商註記揭露期限
//		String LAD_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"LAD_PAY_DATE");//進入第二階梯還款年月

		if((" ").equals(PAY_STATUS)) {
			//不存在PAY_STATUS
			
		}else {
			//只有異動PayStatus其他不動
			if(PAY_DATE!=null && PAY_DATE.length()!=0) {
				//最近一次繳還款日期
				
				int CustNo=CheckCustId(titaVo,IDN_BAN);
				
				NegMain NegMainVO=sNegMainService.CustNoAndApplDateFirst(CustNo, DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"),MAIN_CODE,titaVo);
				if(NegMainVO!=null) {
					//已有資料
					NegMain OrgNegMain=(NegMain) dataLog.clone(NegMainVO);
					NegMainVO=sNegMainService.holdById(NegMainVO.getNegMainId(), titaVo);
					if(NegMainVO!=null) {
						NegMainVO.setPayIntDate(DateRocToDC(titaVo,PAY_DATE,"最近一次繳還款日期"));
						
						String status="";//債權狀態  0:正常,1:已變更,2:毀諾,3:結案,4:未生效,5:調解不成立
						switch(PAY_STATUS) {
							//PAY_STATUS 0:正常 1:已變更 2:毀諾 3:結案 4:未生效 5:調解不成立
							case "0":
								//PAY_STATUS 0:正常
								status="0";
								break;
							case "1":
								//PAY_STATUS 1:已變更
								status="1";
								break;
							case "2":
								//PAY_STATUS 2:毀諾
								status="2";
								break;
							case "3":
								//PAY_STATUS 3:結案
								status="3";
								break;
							case "4":
								//PAY_STATUS 4:未生效
								status="4";
								break;
							case "5":
								//PAY_STATUS 5:調解不成立
								status="5";
								break;
							default:
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009","[債權狀態]不存在.");
						}
						NegMainVO.setStatus(status);
						
						
						if(errorMsg==1) {
							try {
								sNegMainService.update2(NegMainVO, titaVo);//資料異動後-1
								dataLog.setEnv(titaVo, OrgNegMain, NegMainVO);//資料異動後-2
								dataLog.exec();//資料異動後-3
							} catch (DBException e) {
								//E0007 更新資料時，發生錯誤
								throw new LogicException(titaVo, "E0007", "");
							}
						}
					}else {
						//E0006 鎖定資料時，發生錯誤
						throw new LogicException(titaVo, "E0006","");
					}
				}else {
					//無資料
					if(errorMsg==1) {
						// E5009 資料檢核錯誤
						throw new LogicException(titaVo, "E5009","身份證字號["+IDN_BAN+"]債權資料不存在.");
					}
					
				}
			}else {
				
			}
		}
	}
	
	
	public void doZZM262(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//ZZM262	債務協商	單獨受償後各機構無擔保債權暨還款分配資料 (多筆 Z98)
		//IDN_BAN	Char(10)	身分證號
		//RECEIVE_DATE	Char(7)	協商申請日期
		//MAIN_CODE	Char(3)	最大債權金融機構
		//BANK_CODE	Char(3)	債權金融機構
		//BANK_CODE_NAME	Char(24)	債權金融機構名稱
		//EXP_LOAN_AMT	Num(9)	信用貸款債權金額
		//CASH_CARD_AMT	Num(9)	現金卡債權金額
		//CREDIT_CARD_AMT	Num(9)	信用卡債權金額
		//DISP_AMT	Num(9)	每期可分配金額
		//LOAN_RATE	Num(6)	佔全部協商無擔保債權比例  小數點兩位, 例如023.45
		//PAY_BANK	Char(3)	單獨受償金融機構
		//PAY_DATE	Char(7)	單獨受償日期
		//PAY_SEQ	Num(5)	單獨受償次序
		//PAY_REASON	Char(1)	單獨受償原因代碼  (對照表)
		//FILLER	Char(30)	保留欄位
		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
		String BANK_CODE=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE");//債權金融機構
//		String BANK_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME");//債權金融機構名稱
		String EXP_LOAN_AMT=getCutSkill(titaVo,Code,mLineCut,"EXP_LOAN_AMT");//信用貸款債權金額
		String CASH_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CASH_CARD_AMT");//現金卡債權金額
		String CREDIT_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CREDIT_CARD_AMT");//信用卡債權金額
		String DISP_AMT=getCutSkill(titaVo,Code,mLineCut,"DISP_AMT");//每期可分配金額
		String LOAN_RATE=getCutSkill(titaVo,Code,mLineCut,"LOAN_RATE");//佔全部協商無擔保債權比例  小數點兩位, 例如023.45
//		String PAY_BANK=getCutSkill(titaVo,Code,mLineCut,"PAY_BANK");//單獨受償金融機構
//		String PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"PAY_DATE");//單獨受償日期
//		String PAY_SEQ=getCutSkill(titaVo,Code,mLineCut,"PAY_SEQ");//單獨受償次序
//		String PAY_REASON=getCutSkill(titaVo,Code,mLineCut,"PAY_REASON");//單獨受償原因代碼  (對照表)
		
//		id=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
//		receive_date=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
//		maxmain_code=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE").trim();//最大債權金融機構
//		main_code=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE").trim();//債權金融機構
//		main_codename=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME").trim();//債權金融機構名稱
//		amt1=getCutSkill(titaVo,Code,mLineCut,"EXP_LOAN_AMT");//信用貸款債權金額
//		amt2=getCutSkill(titaVo,Code,mLineCut,"CASH_CARD_AMT");//現金卡債權金額
//		amt3=getCutSkill(titaVo,Code,mLineCut,"CREDIT_CARD_AMT");//信用卡債權金額
//		disp_amt=getCutSkill(titaVo,Code,mLineCut,"DISP_AMT");//每期可分配金額
//		loan_rate=getCutSkill(titaVo,Code,mLineCut,"LOAN_RATE");//佔全部協商無擔保債權比例  小數點兩位, 例如023.45
//		String ZZM262payBank=getCutSkill(titaVo,Code,mLineCut,"PAY_BANK");//單獨受償金融機構
//		String ZZM262payDate=getCutSkill(titaVo,Code,mLineCut,"PAY_DATE");//單獨受償日期
//		String ZZM262paySeq=getCutSkill(titaVo,Code,mLineCut,"PAY_SEQ");//單獨受償次序
//		String ZZM262payReason=getCutSkill(titaVo,Code,mLineCut,"PAY_REASON");//單獨受償原因代碼  (對照表)

		//上筆要變已變更
		//其他的照舊
		int CustNo=CheckCustId(titaVo,IDN_BAN);
		NegMain NegMainVO=sNegMainService.CustNoAndApplDateFirst(CustNo, DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"),MAIN_CODE,titaVo);
		if(NegMainVO!=null) {
			//最大的CaseNo
			int CaseSeq=NegMainVO.getCaseSeq();
			//前一筆資料
			NegMainId tNegMainId=new NegMainId();
			tNegMainId.setCaseSeq(CaseSeq-1);
			tNegMainId.setCustNo(CustNo);
			NegMain NegMainVO1=sNegMainService.findById(tNegMainId, titaVo);
			
			String flowType="0";
			if(NegMainVO1!=null) {
				//有上一筆
				String Status=NegMainVO1.getStatus();
				if(("1").equals(Status)) {
					//已變更
					//直接異動NegFinShare
					flowType="2";
				}else {
					//本筆資料改為已變更,新增一筆NegMain
					flowType="1";
				}
			}else {
				//無上一筆資料
				//本筆資料改為已變更,新增一筆NegMain
				flowType="1";
			}
			switch(flowType) {
				case "0":
					break;
				case "1":
					//本筆資料改為已變更,新增一筆NegMain
					CaseSeq=UpdAndInsNegMain(NegMainVO,titaVo);
				case "2":
					//直接新增NegFinShare
					InsertNegFinShare(titaVo,IDN_BAN,CustNo,CaseSeq,BANK_CODE,EXP_LOAN_AMT, CASH_CARD_AMT, CREDIT_CARD_AMT, LOAN_RATE, DISP_AMT);
					break;
				default:
					
				
			}
		}else {
			//無資料
			if(errorMsg==1) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009","身份證字號["+IDN_BAN+"]債權資料不存在.");
			}
		}
	}

	public void InsertNegFinShare(TitaVo titaVo,String CustId,int CustNo, int CaseSeq,String FinCode,String ExpLoanAmt,String CashCardAmt,String CreditCardAmt,String LoanRate,String DispAmt) throws LogicException {
		NegFinShareId NegFinShareIdVO=new NegFinShareId();
		NegFinShareIdVO.setCaseSeq(CaseSeq);
		NegFinShareIdVO.setCustNo(CustNo);
		NegFinShareIdVO.setFinCode(FinCode);
		this.info("L5706 InsertNegFinShare NegFinShareIdVO=["+NegFinShareIdVO+"]");
		NegFinShare NegFinShareVO=sNegFinShareService.findById(NegFinShareIdVO, titaVo);
		if(NegFinShareVO!=null) {
			if(errorMsg==1) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009","身份證字號["+CustId+"] 戶號:["+CustNo+"] 案件序號:["+CaseSeq+"] 債權銀行:["+FinCode+"] 已有債務協商債權分攤檔資料.");
			}
			
		}else {
			this.info("L5706 ZZM260 Word IsNullOrEmpty EXP_LOAN_AMT=["+ExpLoanAmt+"] amt2=["+CashCardAmt+"] amt3=["+CreditCardAmt+"]");
			
			NegFinShareVO=new NegFinShare();
			NegFinShareVO.setNegFinShareId(NegFinShareIdVO);
			BigDecimal amtall=sumAmt(ExpLoanAmt,CashCardAmt,CreditCardAmt);
			NegFinShareVO.setContractAmt(amtall);//簽約金額
			NegFinShareVO.setAmtRatio(StringToBigDecimal(titaVo,LoanRate));//債權比例% 
			NegFinShareVO.setDueAmt(StringToBigDecimal(titaVo,DispAmt));//期款
			NegFinShareVO.setCancelDate(0);//註銷日期
			NegFinShareVO.setCancelAmt(BigDecimal.ZERO);//註銷本金
			try {
				sNegFinShareService.insert(NegFinShareVO, titaVo);
			} catch (DBException e) {
				//E0005 新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔");
			}
		}
	}
	
	public void UpdNegMain(NegMain NegMainVO,TitaVo titaVo) throws LogicException {
		//本筆資料改為已變更
		NegMain OrgNegMain=(NegMain) dataLog.clone(NegMainVO);
		NegMainVO=sNegMainService.holdById(NegMainVO.getNegMainId(), titaVo);
		if(NegMainVO!=null) {
			NegMainVO.setStatus("1");
			try {
				sNegMainService.update2(NegMainVO, titaVo);//資料異動後-1
				dataLog.setEnv(titaVo, OrgNegMain, NegMainVO);//資料異動後-2
				dataLog.exec();//資料異動後-3
			} catch (DBException e) {
				//E0007 更新資料時，發生錯誤
				throw new LogicException(titaVo, "E0007", "");
			}
			
			
		}else {
			//E0006 鎖定資料時，發生錯誤
			throw new LogicException(titaVo, "E0006", "");
		}
	}
	public int UpdAndInsNegMain(NegMain NegMainVO,TitaVo titaVo)  throws LogicException {
		//本筆資料改為已變更,新增一筆NegMain
		UpdNegMain(NegMainVO,titaVo);
		
		int newCaseSeq=NegMainVO.getCaseSeq()+1;
		NegMainId InsertNegMainId=new NegMainId();
		InsertNegMainId.setCaseSeq(newCaseSeq);
		InsertNegMainId.setCustNo(NegMainVO.getCustNo());
		
		NegMain InsertNegMainVO=new NegMain();
		InsertNegMainVO=(NegMain) dataLog.clone(NegMainVO);
		InsertNegMainVO.setStatus("0");
		InsertNegMainVO.setNegMainId(InsertNegMainId);
		InsertNegMainVO.setCustNo(InsertNegMainId.getCustNo());
		InsertNegMainVO.setCaseSeq(InsertNegMainId.getCaseSeq());
		try {
			sNegMainService.insert(InsertNegMainVO, titaVo);
		} catch (DBException e1) {
			//E0005 新增資料時，發生錯誤
			throw new LogicException(titaVo, "E0005", "債務協商案件主檔");
		}
//		NegMain InsertNegMainVO=sNegMainService.findById(InsertNegMainId, titaVo);
//		if(InsertNegMainVO!=null) {
//			
//		}else {
//			InsertNegMainVO = new NegMain();
//			
//		}
//		InsertNegMainVO.setStatus("0");
//		InsertNegMainVO.setCaseSeq(newCaseSeq);
		
		
		return newCaseSeq;
	}
	
	public void doZZM263(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//ZZM263	債務協商	金融機構無擔保債務變更還款條件協議資料 (Z98 金融機構無擔保債務變更還款條件協議資料)
		//IDN_BAN	Char(10)	身分證號
		//RECEIVE_DATE	Char(7)	協商申請日期
		//MAIN_CODE	Char(3)	最大債權金融機構
		//CHANGE_PAY_DATE	Char(7)	申請變更還款條件日
		//COMMIT_PAY_PERIOD	Num(3)	變更還款條件已履約期數
		//CHANGE_PASS_DATE	Char(7)	變更還款條件協議完成日
		//CHANGE_INTERVI_DATE	Char(7)	變更還款條件面談日期
		//CHANGE_SIGN_DATE	Char(7)	變更還款條件簽約日期
		//FIRST_PERIOD	Num(3)	（第一階梯）期數
		//FIRST_RATE	Num(5)	（第一階梯）利率
		//FIRST_PAY_DATE	Char(7)	變更還款條件首期應繳款日期
		//PAY_AMOUNT	Num(9)	月付金
		//PAY_ACCOUNT	Char(20)	繳款帳號
		//CLOSE_DATE	Char(7)	變更還款條件結案日期
		//REM_EXP_AMT	Num(9)	信用貸款協商剩餘債務總金額
		//REM_CASH_AMT	Num(9)	現金卡協商剩餘債務總金額
		//REM_CREDIT_AMT	Num(9)	信用卡協商剩餘債務總金額
		//CHANGE_TOTAL_AMT	Num(10)	變更還款條件簽約總債務金額
		//LAD_PAY_NOTE	Char(1)	屬階梯式還款註記
		//LAD_PERIOD	Num(3)	第二階梯期數
		//LAD_RATE	Num(5)	第二階梯利率
		//LAD_PAY_AMT	Num(9)	第二階梯月付金
		//FILLER	Char(30)	保留欄位

		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE").trim();//最大債權金融機構
		String CHANGE_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"CHANGE_PAY_DATE");//申請變更還款條件日
		String COMMIT_PAY_PERIOD=getCutSkill(titaVo,Code,mLineCut,"COMMIT_PAY_PERIOD");//變更還款條件已履約期數
		String CHANGE_PASS_DATE=getCutSkill(titaVo,Code,mLineCut,"CHANGE_PASS_DATE");//變更還款條件協議完成日
		String CHANGE_INTERVI_DATE=getCutSkill(titaVo,Code,mLineCut,"CHANGE_INTERVI_DATE");//變更還款條件面談日期
		String CHANGE_SIGN_DATE=getCutSkill(titaVo,Code,mLineCut,"CHANGE_SIGN_DATE");//變更還款條件簽約日期
		String FIRST_PERIOD=getCutSkill(titaVo,Code,mLineCut,"FIRST_PERIOD");//（第一階梯）期數
		String FIRST_RATE=getCutSkill(titaVo,Code,mLineCut,"FIRST_RATE");//（第一階梯）利率
		String FIRST_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"FIRST_PAY_DATE");//變更還款條件首期應繳款日期
		String PAY_AMOUNT=getCutSkill(titaVo,Code,mLineCut,"PAY_AMOUNT");//月付金
		String PAY_ACCOUNT=getCutSkill(titaVo,Code,mLineCut,"PAY_ACCOUNT");//繳款帳號
//		String CLOSE_DATE=getCutSkill(titaVo,Code,mLineCut,"CLOSE_DATE");//變更還款條件結案日期
		String REM_EXP_AMT=getCutSkill(titaVo,Code,mLineCut,"REM_EXP_AMT");//信用貸款協商剩餘債務總金額
		String REM_CASH_AMT=getCutSkill(titaVo,Code,mLineCut,"REM_CASH_AMT");//現金卡協商剩餘債務總金額
		String REM_CREDIT_AMT=getCutSkill(titaVo,Code,mLineCut,"REM_CREDIT_AMT");//信用卡協商剩餘債務總金額
		String CHANGE_TOTAL_AMT=getCutSkill(titaVo,Code,mLineCut,"CHANGE_TOTAL_AMT");//變更還款條件簽約總債務金額
		String LAD_PAY_NOTE=getCutSkill(titaVo,Code,mLineCut,"LAD_PAY_NOTE");//屬階梯式還款註記
		String LAD_PERIOD=getCutSkill(titaVo,Code,mLineCut,"LAD_PERIOD");//第二階梯期數
		String LAD_RATE=getCutSkill(titaVo,Code,mLineCut,"LAD_RATE");//第二階梯利率
		String LAD_PAY_AMT=getCutSkill(titaVo,Code,mLineCut,"LAD_PAY_AMT");//第二階梯月付金
		
		int chgCondDate=DateRocToDC(titaVo,CHANGE_PAY_DATE,"申請變更還款條件日");
		int ApplyDate=DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日");
		int FirstDueDate=DateRocToDC(titaVo,FIRST_PAY_DATE,"首次應繳日");
		//本筆資料改為已變更,新增一筆NegMain
		int CustNo=CheckCustId(titaVo,IDN_BAN);
		NegMain NegMainVO=sNegMainService.CustNoAndApplDateFirst(CustNo, ApplyDate,MAIN_CODE,titaVo);
		if(NegMainVO!=null) {
			//本筆資料改為已變更
			UpdNegMain(NegMainVO,titaVo);
			//新增一筆NegMain
			int newCaseSeq=NegMainVO.getCaseSeq()+1;
			NegMain InsertNegMainVO=new NegMain();
			InsertNegMainVO=(NegMain) dataLog.clone(NegMainVO);
			
			
			
			InsertNegMainVO.setStatus("0");
			InsertNegMainVO.setCaseSeq(newCaseSeq);
			InsertNegMainVO.setChgCondDate(chgCondDate);
			InsertNegMainVO.setTotalContrAmt(new BigDecimal(CHANGE_TOTAL_AMT));//簽約總金額
			InsertNegMainVO.setTotalPeriod(parse.stringToInteger(FIRST_PERIOD));//期數
			InsertNegMainVO.setIntRate(parse.stringToBigDecimal(FIRST_RATE)); //計息條件%
			InsertNegMainVO.setDueAmt(parse.stringToBigDecimal(PAY_AMOUNT));//月付金
			InsertNegMainVO.setFirstDueDate(FirstDueDate);//首次應繳日
			int LastDueDate=negCom.AdjMonth(FirstDueDate,InsertNegMainVO.getTotalPeriod(),0);
			InsertNegMainVO.setLastDueDate(LastDueDate);//還款結束日
			
			InsertNegMainVO.setNextPayDate(FirstDueDate);//下次應繳日
			
			InsertNegMainVO.setRepaidPeriod(0);//已繳期數
			InsertNegMainVO.setPayIntDate(0);//繳息迄日
			InsertNegMainVO.setPrincipalBal(new BigDecimal(CHANGE_TOTAL_AMT));//總本金餘額
			
			String TwoStepCode="N";
			if(("Y").equals(LAD_PAY_NOTE) && ("N").equals(LAD_PAY_NOTE) ) {
				TwoStepCode=LAD_PAY_NOTE;
			}else {
				if(LAD_PERIOD!=null && LAD_PERIOD.length()!=0) {
					if(parse.stringToInteger(LAD_PERIOD)>0) {
						TwoStepCode="Y";
					}
				}
			}
			InsertNegMainVO.setTwoStepCode(TwoStepCode);
			
			try {
				sNegMainService.insert(InsertNegMainVO, titaVo);
			} catch (DBException e1) {
				//E0005 新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", "");
			}
			
			//JcicZ062
			JcicZ062Id tJcicZ062Id=new JcicZ062Id();
			tJcicZ062Id.setChangePayDate(chgCondDate);
			tJcicZ062Id.setCustId(IDN_BAN);
			tJcicZ062Id.setRcDate(ApplyDate);
			tJcicZ062Id.setSubmitKey("458");
			
			JcicZ062 tJcicZ062=sJcicZ062Service.findById(tJcicZ062Id, titaVo);
			if(tJcicZ062!=null) {
				
			}else {
				tJcicZ062=new JcicZ062();
				tJcicZ062.setJcicZ062Id(tJcicZ062Id);
				tJcicZ062.setTranKey("A");//交易代號
				tJcicZ062.setCompletePeriod(parse.stringToInteger(COMMIT_PAY_PERIOD));//變更還款條件已履約期數
				tJcicZ062.setPeriod(parse.stringToInteger(FIRST_PERIOD));//(第一階梯)期數
				tJcicZ062.setRate(parse.stringToBigDecimal(FIRST_RATE));//(第一階梯)利率
				tJcicZ062.setExpBalanceAmt(parse.stringToInteger(REM_EXP_AMT));//信用貸款協商剩餘債務簽約餘額
				tJcicZ062.setCashBalanceAmt(parse.stringToInteger(REM_CASH_AMT));//現金卡協商剩餘債務簽約餘額
				tJcicZ062.setCreditBalanceAmt(parse.stringToInteger(REM_CREDIT_AMT));//信用卡協商剩餘債務簽約餘額
				tJcicZ062.setChaRepayAmt(parse.stringToBigDecimal(CHANGE_TOTAL_AMT));//變更還款條件簽約總債務金額
				tJcicZ062.setChaRepayAgreeDate(DateRocToDC(titaVo,CHANGE_PASS_DATE,"變更還款條件協議完成日"));//變更還款條件協議完成日
				tJcicZ062.setChaRepayViewDate(DateRocToDC(titaVo,CHANGE_INTERVI_DATE,"變更還款條件面談日期"));//變更還款條件面談日期
				tJcicZ062.setChaRepayEndDate(DateRocToDC(titaVo,CHANGE_SIGN_DATE,"變更還款條件簽約日期"));//變更還款條件簽約完成日期
				tJcicZ062.setChaRepayFirstDate(FirstDueDate);//變更還款條件首期應繳款日
				tJcicZ062.setPayAccount(PAY_ACCOUNT);//繳款帳號
				tJcicZ062.setPostAddr("台北市中正區忠孝西路一段66號18樓");//最大債權金融機構聲請狀送達地址
				tJcicZ062.setMonthPayAmt(parse.stringToInteger(PAY_AMOUNT));//月付金
				tJcicZ062.setGradeType(TwoStepCode);//屬階梯式還款註記
				tJcicZ062.setPeriod2(parse.stringToInteger(LAD_PERIOD));//第二階梯期數
				tJcicZ062.setRate2(parse.stringToBigDecimal(LAD_RATE));//第二階梯利率
				tJcicZ062.setMonthPayAmt2(parse.stringToInteger(LAD_PAY_AMT));//第二階段月付金
			}
			
		}else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009","身份證字號["+IDN_BAN+"] 戶號:["+CustNo+"] 最大債權銀行:["+MAIN_CODE+"] 未有債權資料");
		}
	}
	
	public void doZZM264(TitaVo titaVo,String Code,Map<String,String[]>mLineCut) throws LogicException {
		//ZZM264	債務協商	債權金融機構剩餘無擔保債權暨還款分配資料 (Z98 各債權金融機構剩餘無擔保債權暨還款分配資料(變更還款後))
		//IDN_BAN	Char(10)	身分證號
		//RECEIVE_DATE	Char(7)	協商申請日期
		//MAIN_CODE	Char(3)	最大債權金融機構
		//CHANGE_PAY_DATE	Char(7)	申請變更還款條件日
		//BANK_CODE	Char(3)	債權金融機構
		//BANK_CODE_NAME	Char(24)	債權金融機構名稱
		//EXP_LOAN_AMT	Num(9)	信用貸款債權金額
		//CASH_CARD_AMT	Num(9)	現金卡債權金額
		//CREDIT_CARD_AMT	Num(9)	信用卡債權金額
		//DISP_AMT1	Num(9)	第一階梯分配金額
		//DISP_AMT2	Num(9)	第二階梯分配金額
		//LOAN_RATE	Num(6)	佔全部協商無擔保債權比例  小數點兩位, 例如023.45
		//MAIN_SEND_NOTE	Char(1)	最大債權金融機構報送註記
		//FILLER	Char(30)	保留欄位
		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
//		String CHANGE_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"CHANGE_PAY_DATE");//申請變更還款條件日
		String BANK_CODE=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE");//債權金融機構
//		String BANK_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME");//債權金融機構名稱
		String EXP_LOAN_AMT=getCutSkill(titaVo,Code,mLineCut,"EXP_LOAN_AMT");//信用貸款債權金額
		String CASH_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CASH_CARD_AMT");//現金卡債權金額
		String CREDIT_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CREDIT_CARD_AMT");//信用卡債權金額
		String DISP_AMT1=getCutSkill(titaVo,Code,mLineCut,"DISP_AMT1");//第一階梯分配金額
//		String DISP_AMT2=getCutSkill(titaVo,Code,mLineCut,"DISP_AMT2");//第二階梯分配金額
		String LOAN_RATE=getCutSkill(titaVo,Code,mLineCut,"LOAN_RATE");//佔全部協商無擔保債權比例  小數點兩位, 例如023.45
//		String MAIN_SEND_NOTE=getCutSkill(titaVo,Code,mLineCut,"MAIN_SEND_NOTE");//最大債權金融機構報送註記
		
		int CustNo=CheckCustId(titaVo,IDN_BAN);
//		CustNoAndApplDateFirst
		NegMain NegMainVO=sNegMainService.CustNoAndApplDateFirst(CustNo, DateRocToDC(titaVo,RECEIVE_DATE,"協商申請日"),MAIN_CODE, titaVo);
//		sNegMainService.custNO
		if(NegMainVO!=null) {
			//已有資料
			InsertNegFinShare(titaVo,IDN_BAN,CustNo,NegMainVO.getCaseSeq(),BANK_CODE,EXP_LOAN_AMT, CASH_CARD_AMT, CREDIT_CARD_AMT, LOAN_RATE, DISP_AMT1);
		}else {
			//無資料
			if(errorMsg==1) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009","身份證字號["+IDN_BAN+"]債權資料不存在.");
			}
			
		}

	}
}