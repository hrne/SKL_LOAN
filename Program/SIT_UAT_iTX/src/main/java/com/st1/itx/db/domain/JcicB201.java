package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * JcicB201 聯徵授信餘額月報資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB201`")
public class JcicB201 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5996102797085071911L;

@EmbeddedId
  private JcicB201Id jcicB201Id;

  // 資料年月
  @Column(name = "`DataYM`", insertable = false, updatable = false)
  private int dataYM = 0;

  // 總行代號
  /* Key,金融機構總機構之代號，三位數字 */
  @Column(name = "`BankItem`", length = 3, insertable = false, updatable = false)
  private String bankItem;

  // 分行代號
  /* Key,金融機構分支機構之代號，四位數字 */
  @Column(name = "`BranchItem`", length = 4, insertable = false, updatable = false)
  private String branchItem;

  // 交易代碼
  /* A 新增 C 異動 D刪除 ;每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：1. 新增資料時填A;2. 修改非key值欄位值時填C; 3. 刪除原報送資料時填D;4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料 */
  @Column(name = "`TranCode`", length = 1, insertable = false, updatable = false)
  private String tranCode;

  // 帳號屬性註記
  /* A 本月新增帳號(非上月轉換而來)C 本月轉換帳號(如填報C，請務必填報帳號轉換檔)X 舊有帳號 */
  @Column(name = "`SubTranCode`", length = 1, insertable = false, updatable = false)
  private String subTranCode;

  // 本筆撥款帳號
  /* Key,左靠，填報本筆授信撥款帳號或交易序號，該帳號在同一金融機構內需為唯一(不可重複)，且一號到底(每月同筆撥款帳號均為相同號碼);若帳號無法定義至每筆撥款，如循環信用之存融、現金卡放款、透支戶及理財型房貸…等，則可定義為對應該筆撥款額度項下之帳號(可為契約或批覆書號碼)前揭循環信用業務若有另產生放款帳號，則以該放款帳號填報，若無產生放款帳號，則請填報原存款帳號。如為票券公司可以 成交單號 報送本欄位。若有部分轉催、部分轉呆或金融機構合併或轉籍之狀況，其帳號均不可重複，若其帳號無法一號到底需轉換帳號時，請於第3.2欄填報C(本月轉換帳號)，並務必另填報帳號轉換檔。 */
  @Column(name = "`AcctNo`", length = 50, insertable = false, updatable = false)
  private String acctNo;

  // 本筆撥款帳號序號
  /* 1=關係人5位以內2=關係人5位以上記在第2筆 */
  @Column(name = "`SeqNo`", insertable = false, updatable = false)
  private int seqNo = 0;

  // 金額合計
  /* 右靠左補0，本欄為第24欄訂約金額(台幣)、第25欄訂約金額(外幣)、第29欄授信未逾期餘額(台幣)、第30欄授信未逾期餘額(外幣)、第31欄逾期未還餘額(台幣)、第32欄逾期未還金額(外幣)等六項金額欄位之合計，主要共金額欄位數值之校對。 */
  @Column(name = "`TotalAmt`")
  private BigDecimal totalAmt = new BigDecimal("0");

  // 授信戶IDN/BAN
  /* 左靠，法人請填八碼營利事業統一編號，自然人及非法人組織授信按請填自然人身份證統一編號或統一證號 */
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 上欄IDN或BAN錯誤註記
  /* 第6欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報 */
  @Column(name = "`CustIdErr`", length = 1)
  private String custIdErr;

  // 負責人IDN/負責之事業體BAN
  /* A 企業授信戶負責人IDNB 個人授信戶負責之事業體BANC 助學貸款戶學校代號D 留學生就學貸款之留學生IDN左靠，請填a 企業負責人之身分證統一編號b 個人授信戶負責之事業體(僅限非法人組織)營利事業統一編號c 如授信戶為高級中等以上學校就學貸款戶，本欄請填該筆貸款所屬之教育部編列六碼學校代號d 如授信戶為留學生就學貸款，本欄位請填該筆貸款所屬留學生IDN，若授信戶為留學生本人，本欄與第6欄為相同之IDN */
  @Column(name = "`SuvId`", length = 10)
  private String suvId;

  // 上欄IDN或BAN錯誤註記
  /* 第8欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報 */
  @Column(name = "`SuvIdErr`", length = 1)
  private String suvIdErr;

  // 外僑兼具中華民國國籍IDN
  /* 授信戶或授信戶負責人外僑兼具中華民國國籍，並以外橋身份編號填報於第6欄或第8欄者，本欄填其十位文數字國民身份證統一編號;如無前述狀況請填空白 */
  @Column(name = "`OverseasId`", length = 10)
  private String overseasId;

  // 授信戶行業別
  /* 授信對象所屬行業，請參考附件二授信戶行業別代號，若授信互為現金卡放款戶，本欄需填報該戶之職業身份代號(請參考附件二之一) */
  @Column(name = "`IndustryCode`", length = 6)
  private String industryCode;

  // 空白
  /* 備用 */
  @Column(name = "`Filler12`", length = 3)
  private String filler12;

  // 科目別
  /* 請參考附件三授信科目代號表 */
  @Column(name = "`AcctCode`", length = 1)
  private String acctCode;

  // 科目別註記
  /* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表 */
  @Column(name = "`SubAcctCode`", length = 1)
  private String subAcctCode;

  // 轉催收款(或呆帳)前原科目別
  /* 第13欄 科目別填報A:催收款項及B呆帳者，或第41欄 不良債權處理註記 填報代號B 轉銷呆帳及C轉催收者，本欄位為必要填報項目，請填報轉催收款(或呆帳)前原核准貸放授信科目(若為轉呆帳，本欄位請勿填報催收科目A)，且每月需持續報送該轉催(呆)前授信科目，左靠右空白。 */
  @Column(name = "`OrigAcctCode`", length = 1)
  private String origAcctCode;

  // 個人消費性貸款註記
  /* 個人貸款屬消費性貸款者請填Y，屬於非消費性貸款(如非法人組織貸款)請填N，非屬個人貸款填X */
  @Column(name = "`ConsumeFg`", length = 1)
  private String consumeFg;

  // 融資分類
  /* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款 */
  @Column(name = "`FinCode`", length = 1)
  private String finCode;

  // 政府專業補助貸款分類
  /* 填報該筆授信屬何種政府專案補助性質之貸款：專案補助貸款大項分類為01 振興傳統產業專案貸款 02 輔導中小企業升級或協助紮根貸款 03 振興景氣分案專案貸款 04 購置自動化機器 05 行政院開發基金或中小企業發展基金 06 微型企業創業貸款 07 九二一大地震金融因應措施 08 SARS 金融因應措施 09 青年優惠房屋貸款 10 優惠購屋專案貸款 11 輔助人民自購住宅貸款(首購或非首購) 12 國民住宅貸款 13 公教人員購置住宅 14 勞工貸款(含自購住宅及房屋修繕) 15 國軍官兵貸款 17 原住民貸款 18 學生助學貸款 19 獎勵數位內容產業及文化創意產業優惠貸款 20 留學生就學貸款 21 青年創業貸款(非屬農業部分 ) 22 天然災害房屋整修及重建貸款(非屬九二一地震受災戶部分) 23 參與度是更新計畫貸款  31 農機貸款 32 輔導農糧業經營貸款 33 輔導漁業經營貸款 34提升處勤產業經營貸款 35 農民經營改善貸款 36 農業科技園區進駐業者貸款 37 山坡地保育利用貸款 38 農業綜合貸款 39 改善財物貸款 40 農會事業發展貸款 41 農業產銷班貸款 50 民營事業污染防治設備低利貸款 51 行政院醫療發展基金構建醫療機構(設備)貸款 ，52 經濟部中小企業發展基金支援辦理四項專案貸款 99 其他政府專案補助貸款，如非屬專案補助貸款之授信填XX。 */
  @Column(name = "`ProjCode`", length = 2)
  private String projCode;

  // 不計入授信項目
  /* 不計入授信項目代號請參照附件四 */
  @Column(name = "`NonCreditCode`", length = 1)
  private String nonCreditCode;

  // 用途別
  /* 用途別請參照附件五 (1:購置不動產 2:購置動產 3:企業投資 4:週轉金) */
  @Column(name = "`UsageCode`", length = 1)
  private String usageCode;

  // 本筆撥款利率
  /* 本項資料本中心不予揭露，僅將作為本中心協助主管機關及會員金融機構風險因子計算之基礎及方法驗證之用。單位：百分比，填報本筆撥款帳號報送當月底之適用利率，如屬專案補助貸款則填報其實際計息利率;若保證業務已發生墊款，請填報保證墊款利率;八位文數字，以xx.xxxxx表示，填至小數點以下五位，例如3.456％請填03.45600 */
  @Column(name = "`ApproveRate`")
  private BigDecimal approveRate = new BigDecimal("0");

  // 本筆撥款開始年月
  /* 以'YYYMM'(民國年)表示，填報對應本筆撥款之起始年月，例如撥歌開始於92年1月，請填'09201'；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）或屬有額度未動用授信，則填報其合約起始年月 */
  @Column(name = "`DrawdownDate`")
  private int drawdownDate = 0;

  // 本筆撥款約定清償年月
  /* 以'YYYMM'(民國年)表示，填報對應本筆撥款之約定清償年月，例如撥歌之釣定清債年月為112年1月，請填'11201'；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）或屬有額度未動用授信，則填報其合約截止年月；如為中長期分期償還放款，本欄請填報約定之最後清償年月；如無約定清償年月或到期年月，本欄請填'99999' */
  @Column(name = "`MaturityDate`")
  private int maturityDate = 0;

  // 授信餘額幣別
  /* 三個英文字．填報該幣別國際通用英文字母代號，請參照附件六"國際通用幣別代號表" */
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 訂約金額(台幣)
  /* 右靠左補0，填報本筆撥款帳號所屬訂約金額(現金卡放款請填報契約額度)，單位新台幣千元，本欄位與第25欄請擇一填報；有追索權應收帳款請填報融資額度；若有共用額度狀況，請於各該筆撥款之本欄位皆填0，並將該共用額度額度金額填報於授信額度資料檔之「本階訂約金額」欄，本欄位如無資料請填'0' */
  @Column(name = "`DrawdownAmt`")
  private BigDecimal drawdownAmt = new BigDecimal("0");

  // 訂約金額(外幣)
  /* 右靠左補0，填報本筆撥款帳號所屬訂約金額(現金卡放款請填報契約額度)，單位新台幣千元，本欄位與第25欄請擇一填報；有追索權應收帳款請填報融資額度；若有共用額度狀況，請於各該筆撥款之本欄位皆填0，並將該共用額度額度金額填報於授信額度資料檔之「本階訂約金額」欄，本欄位如無資料請填'0' */
  @Column(name = "`DrawdownAmtFx`")
  private BigDecimal drawdownAmtFx = new BigDecimal("0");

  // 循環信用註記
  /* 'Y':是，'N':否，填報該筆授信是否屬循環性質 */
  @Column(name = "`RecycleCode`", length = 1)
  private String recycleCode;

  // 額度可否撤銷
  /* Y：可撤銷，N:不可撤銷；可撤銷之定義，依據96年金管會銀行局發佈之【銀行自有資本與風險性資產之計算方法說明及表格】第7頁所示，如該筆授信符合信用轉換係數為0者屬之，意即：(1)銀行無需事先通知即得隨時無條件取消之承諾【註：若銀行對客戶之零售債權承諾，符合消費者保護法及相關法令規定，且銀行得隨時無條件取消該承諾者，得被認為係無條件可取消之承諾。】(2)當借款人信用貶落時，銀行有權自動取消之承諾否則該授信可視為不可撤銷 */
  @Column(name = "`IrrevocableFlag`", length = 1)
  private String irrevocableFlag;

  // 上階共用額度控制編碼
  /* 每筆撥款之共用額度控制編碼，若為不同筆撥款共用一授信額度時，皆需於本欄填報多肇撥款相同之上階共用額度控制編碼；如無對應額度檔，則以'9'填滿本欄位 */
  @Column(name = "`FacmNo`", length = 50)
  private String facmNo;

  // 未逾期/乙類逾期/應予觀察授信餘額(台幣)
  /* a.未逾期授信餘額b.乙類逾期放款餘額c.應予觀察授信餘額右靠左補0，單位新台幣千元；a.如該筆為未逾期授信b.如該筆為乙類逾期放款（即第40欄填報代號'1'~'7'者）c.如該筆為應予觀察授信（即第40欄填報代號'A'~'B'者）請將其台幣授信餘額填報於本欄位。有追索權應收帳款請填報融資餘額；凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填'1'，本欄位如無資料請填'0' */
  @Column(name = "`UnDelayBal`")
  private BigDecimal unDelayBal = new BigDecimal("0");

  // 未逾期/乙類逾期/應予觀察授信餘額(外幣)
  /* a.未逾期授信餘額b.乙類逾期放款餘額c.應予觀察授信餘額右靠左補0，單位新台幣千元，填報等值之台幣金額；請將其外幣授信餘額換算為台幣填報於本欄位。有追索權應收帳款請填報融資餘額；凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填'1'，本欄位如無資料請填'0' */
  @Column(name = "`UnDelayBalFx`")
  private BigDecimal unDelayBalFx = new BigDecimal("0");

  // 逾期未還餘額（台幣）
  /* 右靠左補0，單位新台幣千元；如該筆授信屬甲類逾期放款（即第39欄填報代號 "1"~"4"者）、逾期授信（即第39欄填報代號"A"、"B"者）、催收款（授信科目"A"）已列報逾期者或呆帳（授信科目"B"），請將其台幣授信餘額填報於本欄位。凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填‘1’，本欄位如無資料請填‘0’ */
  @Column(name = "`DelayBal`")
  private BigDecimal delayBal = new BigDecimal("0");

  // 逾期未還餘額（外幣）
  /* 右靠左補0，單位新台幣千元，填報等值之台幣金額；如該筆授信屬甲類逾期放款（即第39欄填報代號 "1"~"4"者）、逾期授信（即第39欄填報代號"A"、"B"者）、催收款（授信科目"A"）已列報逾期者或呆帳（授信科目"B"），請將其外幣授信餘額換算為台幣填報於本欄位。凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填‘1’，本欄位如無資料請填‘0’ */
  @Column(name = "`DelayBalFx`")
  private BigDecimal delayBalFx = new BigDecimal("0");

  // 逾期期限
  /* 逾期期限代號請參照附件七，其中代號"9":未逾期或乙類逾期/應予觀察授信，屬於未逾期、乙類逾期或應予觀察授信者，其逾期期限代號請填報'9’如本筆屬有額度未動用授信，本欄位請填報'9’ */
  @Column(name = "`DelayPeriodCode`", length = 1)
  private String delayPeriodCode;

  // 本月還款紀錄
  /* 填報本月底應還款（包括本金、利息或本利攤還）之正常還款或遲延情形，代號1~6單位為月份，請參照附件八還款紀錄代號表如本筆屬有額度未動用授信*本欄位請填報'X' */
  @Column(name = "`RepayCode`", length = 1)
  private String repayCode;

  // 本月（累計）應繳金額
  /* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，填報本月應還款（包括本金、利息 ' 本利攤還及其他應繳費用）之金額，或催收款、呆帳之對外債權金額（呆帳之對外債權若屬有擔保且送法院拍賣者，其金額應為報給法院之債權金額），以當月之最後繳款期限日（非繳款期限日後之最大寬限期）為準。房貸業務請務必填報每月依約應還款金額於本欄位；如為循環型授信，且有最低應繳金額者，請填當月最低應繳金額。若該筆授信於上月或前一期繳款期限日前未繳或未繳足應繳金額，本月本欄則填報累計應繳金額。若於當月有一次以上之應繳金額與繳款期限日（如雙週繳款之攤還型授信），則以當月最後一次繳款期限日為準，填報累計之應繳金額。若金額小於千元部分，請以小數點方式填報，例如2345元請填'0000002.345'；若該筆授信當月無應繳金額，或循環型授信無當月最低應繳金額，本欄請填'0' */
  @Column(name = "`PayAmt`")
  private BigDecimal payAmt = new BigDecimal("0");

  // 本月收回本金
  /* 本項資料本中心不予揭露，右靠左補0，單位:新台幣千元，請填本筆授信債權自撥款後之本月回收本金；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）授信，仍需就每月印用額度後之合計收回本金填報本欄位；若金額小於千元部分，請以小數點方式填報，例如2345元請填'0000002.345'；本月如有退回收回本金‧請於本欄位第一碼（即左靠）加註'-'（負號），例如本月退回12000元請填'-0000000012'；本月如無收回本金.本欄請填'0' */
  @Column(name = "`Principal`")
  private BigDecimal principal = new BigDecimal("0");

  // 本月收取利息
  /* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，請填本筆授信債權之本月收取利息，若金額小於千元部分，請以小數點方式填報，例如2345元請填'000002.345；本月如有退回收取利息，請於本欄位第一碼（即左靠）加註'-'（負號）；本月如無收取利息，本欄請填0 */
  @Column(name = "`Interest`")
  private BigDecimal interest = new BigDecimal("0");

  // 本月收取其他費用
  /* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，請填本筆授信債權之本月收取其他費用；回收其他費用係包括開辦費、手續費、簽證費、承銷費、管理費、承諾費、違約金、法務費用及訴訟費用等所有相關成本費用。若金額小於千元部分，請以小數點方式填報，例如2345元請填 '000002.345；本月如有退回收取其他費用，請於本欄位第一碼（即左靠）加註'-'（負號）；本月如無收取其他費用，本欄請填0 */
  @Column(name = "`Fee`")
  private BigDecimal fee = new BigDecimal("0");

  // 甲類逾期放款分類
  /* 左靠右空白，a 如該筆授信屬甲類逾期放款，請填報其分類代號如下 1 放款本金超過清償期三個月而未獲清償，或雖為屆滿三個月，但已向主、從債務人訴追獲處分擔保品者， 2 放款本金未超過清償期三個月，而利息未按期繳納超過六個月者， 3 分期償還放款未按期攤還超過六個月，或雖未屆滿六個月，但已向主、從債務人訴追獲處分擔保品者， 4 協議分期償還放款符合一定條件而曾經免列報逾期放款案件，於免列報期間再發生未依約清償超過三個月者；同一筆授信之分類，請於本欄位之分類代號與第40欄之分類代號中則一填報；若非屬甲類逾期放款請填 X。 */
  @Column(name = "`FirstDelayCode`", length = 3)
  private String firstDelayCode;

  // 乙類逾期放款分類
  /* a 如該筆授信與乙類逾期放款，請填報其分類代號如下:1 放款本金未超過清償期三個月，惟利息未按期繳納超過清償期三個月，分期償還放款未按期攤還超過三個月至六個月者， 3 協議分期償還放款，協議條件符合規定，且借款戶依協議條件按期履約未滿六個月者， 4 已獲信用保證基金同意理賠款項或有足額存單或存款備償(需辦妥質權設定且徵得發單行拋棄抵銷權同意書)，而約定待其他債務人財產處分後再予沖償者， 5 已確定分配之債權，惟尚未接獲分配款者， 6 債務人兼擔保品提供人死亡，於辦理繼承期間，借期而為清償之放款，其繳息正常且有十足擔保者， 7 其他(如921震災展延本息之放款)；同一筆授信之分類，請於本欄位之分類代號與第39欄之分類代號中則一填報；若非屬類逾期放款請填 X。 */
  @Column(name = "`SecondDelayCode`", length = 1)
  private String secondDelayCode;

  // 不良債權處理註記
  /* 如該筆授信債權於本月處理且結案者，請直接填報第42欄 債權結案註記 ，本欄位無須填報；本欄位僅處理當月報送處理註記代號即可，次月不需重複報送，左靠。P 不良債權主債務人自償 Q 不良債權從債務人代位償還 R 主、從債務人以外之第三人代位償還，G 代物償還(指債權人受領其他給付，以消滅原債權之償還)，E 債務承擔(指原有債務為第三人承擔)，D 處分擔保品 A 承受擔保品，本欄位若有填報代號P、 Q、 R、G、 D及A者，亦請配合於第36、37或38欄填報收回金額；B轉銷呆帳，C 轉催收款，如無前述狀況請填空白，勿填0。 */
  @Column(name = "`BadDebtCode`", length = 3)
  private String BadDebtCode;

  // 債權結束註記
  /* 左靠，3 正常債權全額清償 ；T 債權轉讓， S債權證券化；P 不良債權主債務人自償 ， Q 不良債權從債務人代位償還 R 主、從債務人以外之第三人代位償還，G 代物償還(指債權人受領其他給付，以消滅原債權之償還)，E 債務承擔(指原有債務為第三人承擔)，D 處分擔保品 A 承受擔保品；H 債權和解，N債權拋棄 ，J 合併或業務移轉，X 債權轉讓後原債權機購買回，F 信保基金退理賠，Z 結清銷戶；如無結案狀況請填空白，勿填0。 */
  @Column(name = "`NegStatus`", length = 3)
  private String negStatus;

  // 債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN
  /* a 債權處理後新債權人IDN/BANb 債權轉讓後原債權機購買回之前手債權人IDN/BANc 信保基金退理賠信用保證機構BAN左靠，a.如第42欄填T或S，本欄位請填報債權轉讓資產管理公司、其他機構或第三人，辦理資產證券化受託機構或特殊目的公司之IDN/BANb.如第42欄填X，本欄請填報債權轉讓後原債權機構買回之前手債權人IDN/BANc.如第42欄填F，本欄請填報信保基金退理賠信用保證機構之八碼營利事業統一編號本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`NegCreditor`", length = 10)
  private String negCreditor;

  // 債權處理案號
  /* 左靠，如第42欄填T或S者，本欄統一以YYYMMDDNXYYYMM共14碼表示，其資料欄位定義/結構如下:前7碼YYYMMDD：契約簽訂日期；第8碼N:當日摽售批次序號，1-第一批出售案、2-第二批出售案；第9碼X:債權類別，B:現金卡、C:授信(不含現金卡)；第10~14碼YYYMM債權處理年月(債權交割日)；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`NegNo`", length = 14)
  private String negNo;

  // 債權轉讓年月/債權轉讓後原債權機構買回年月
  /* a.債權轉讓年月b.債權轉讓後原債權機構買回年月左靠，a.如第42欄填T者，本欄請填報金融機構與資產管理公司約定之債權交割年月b.如第42欄填X者，本欄請填報債權轉讓後原債權機構買回年月：以民國年YYYMM表示，本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`NegTransYM`", length = 5)
  private String negTransYM;

  // 空白
  /* 備用 */
  @Column(name = "`Filler443`", length = 6)
  private String filler443;

  // 擔保品組合型態
  /* 0:純信用                    1:單一擔保品(或保證)2:多種擔保品含股票          3:多種擔保品不含股票 */
  @Column(name = "`ClType`", length = 1)
  private String clType;

  // 擔保品(合計)鑑估值
  /* 右靠，單位:新台幣千元。如為多筆授信共用擔保品者，請將擔保品(合計)鑑估值歸入其中一筆授信之本欄位填報，其餘個筆授信此欄位則註記*，表示係與他筆授信共用擔保品 */
  @Column(name = "`ClEvaAmt`")
  private BigDecimal clEvaAmt = new BigDecimal("0");

  // 擔保品類別
  /* 擔保品類別代號表請參照附件九(本修訂版將原擔保品類別:信用保證機構保證細分為5項，05:：中小企業信用保證基金保證，06:農業信用保證基金保證、07:華僑貸款信用保證基金保證，08:國際合作發展基金會信用保證，09:原住民族綜合發展基金信用保證) */
  @Column(name = "`ClTypeCode`", length = 2)
  private String clTypeCode;

  // 國內或國際連貸
  /* A 國內 B 國際，如非屬聯貸案填空白，勿填0。 */
  @Column(name = "`SyndKind`", length = 1)
  private String syndKind;

  // 聯貸合約訂定日期
  /* 以西元年月日YYYYMMDD表示，如非屬聯貸案填空白，勿填0。 */
  @Column(name = "`SyndContractDate`", length = 8)
  private String syndContractDate;

  // 聯貸參貸比例
  /* 右靠左補0，填報聯貸案個別參貸比例，若參貸比列小於1%，請以小數點方式填報，例如0.8%請填00.8；非屬聯貸案填空白，勿填0；如該聯貸合約載明與其他授信戶共用此參貸比例時，僅需於其中一授信戶之本欄位填報參貸比例，其他授信戶之本欄位請填報*。 */
  @Column(name = "`SyndRatio`")
  private BigDecimal syndRatio = new BigDecimal("0");

  // 空白
  /* 本欄位原為 無追索權應收帳款承購業務買方國別，現改為空白 */
  @Column(name = "`Filler51`", length = 2)
  private String filler51;

  // 空白
  /* 本欄位原為 無追索權應收帳款承購業務買方行業別，現改為空白 */
  @Column(name = "`Filler52`", length = 6)
  private String filler52;

  // 代放款註記
  /* 如為代放款(非銀行之自有資金且會計科目屬代放款者)請填Y，否則請填N。 */
  @Column(name = "`PayablesFg`", length = 1)
  private String payablesFg;

  // 債務協商註記
  /* 如本筆授信以參加銀行公會消金債務協商機制，且目前為有效停催案件者請填Y，否則請填N */
  @Column(name = "`NegFg`", length = 1)
  private String negFg;

  // 空白
  /* 備用 */
  @Column(name = "`Filler533`", length = 1)
  private String filler533;

  // 共同債務人或債務關係人身份代號1
  /* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaTypeCode1`", length = 1)
  private String guaTypeCode1;

  // 共同債務人或債務關係人身份統一編號1
  /* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaId1`", length = 10)
  private String guaId1;

  // 上欄IDN或BAN錯誤註記
  /* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報 */
  @Column(name = "`GuaIdErr1`", length = 1)
  private String guaIdErr1;

  // 與主債務人關係1
  /* 請參照附件十二；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaRelCode1`", length = 2)
  private String guaRelCode1;

  // 共同債務人或債務關係人身份代號2
  /* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaTypeCode2`", length = 1)
  private String guaTypeCode2;

  // 共同債務人或債務關係人身份統一編號2
  /* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaId2`", length = 10)
  private String guaId2;

  // 上欄IDN或BAN錯誤註記
  /* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報 */
  @Column(name = "`GuaIdErr2`", length = 1)
  private String guaIdErr2;

  // 與主債務人關係2
  /* 請參照附件十二；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaRelCode2`", length = 2)
  private String guaRelCode2;

  // 共同債務人或債務關係人身份代號3
  /* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaTypeCode3`", length = 1)
  private String guaTypeCode3;

  // 共同債務人或債務關係人身份統一編號3
  /* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaId3`", length = 10)
  private String guaId3;

  // 上欄IDN或BAN錯誤註記
  /* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報 */
  @Column(name = "`GuaIdErr3`", length = 1)
  private String guaIdErr3;

  // 與主債務人關係3
  /* 請參照附件十二；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaRelCode3`", length = 2)
  private String guaRelCode3;

  // 共同債務人或債務關係人身份代號4
  /* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaTypeCode4`", length = 1)
  private String guaTypeCode4;

  // 共同債務人或債務關係人身份統一編號4
  /* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaId4`", length = 10)
  private String guaId4;

  // 上欄IDN或BAN錯誤註記
  /* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報 */
  @Column(name = "`GuaIdErr4`", length = 1)
  private String guaIdErr4;

  // 與主債務人關係4
  /* 請參照附件十二；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaRelCode4`", length = 2)
  private String guaRelCode4;

  // 共同債務人或債務關係人身份代號5
  /* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaTypeCode5`", length = 1)
  private String guaTypeCode5;

  // 共同債務人或債務關係人身份統一編號5
  /* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaId5`", length = 10)
  private String guaId5;

  // 上欄IDN或BAN錯誤註記
  /* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報 */
  @Column(name = "`GuaIdErr5`", length = 1)
  private String guaIdErr5;

  // 與主債務人關係5
  /* 請參照附件十二；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`GuaRelCode5`", length = 2)
  private String guaRelCode5;

  // 空白
  /* 備用 */
  @Column(name = "`Filler741`", length = 10)
  private String filler741;

  // 空白
  /* 備用 */
  @Column(name = "`Filler742`", length = 10)
  private String filler742;

  // 呆帳轉銷年月
  /* 以YYYMM(民國)表示，請填報該筆呆帳之轉銷年月；第13欄科目別填報B：呆帳者，本欄位為必要填報項目(且每月皆需報送)；本欄位如無資料請填空白，勿填0。 */
  @Column(name = "`BadDebtDate`")
  private int badDebtDate = 0;

  // 聯貸主辦(管理)行註記
  /* 第48欄 國內或國外聯貸填報A及B者，本欄為必要填報項目：如為聯貸案單一主辦行請填Y(非單一主辦行請填X)，額度管理行請填L，如為共同主辦行兼額度管理行請填YL，單一主辦案填報Y或L，或共同主辦案本欄位填報YL者，需配合報送 聯貸合約各參貸機構參貸比例資料檔案格式；如為擔保品管理行請填C，填報C時須配合報送 「擔保品關連檔格式」及「擔保品茗係檔格式」；前述代號若有同時兼具之狀況時，請填報所有代號；若非屬前述主辦(管理)行請填X */
  @Column(name = "`SyndCode`", length = 5)
  private String syndCode;

  // 破產宣告日(或法院裁定開始清算日)
  /* 以民國年月日YYYMMDD表示，授信戶經法院依破產法宣告破產者，本欄位請填報破產宣告日；授信戶經法院依「消費者債務清理條例」裁定開始清算程序者，本欄位請填報開始清算日(且每月皆需報送)；如無資料請填空白，勿填0。 */
  @Column(name = "`BankruptDate`")
  private int bankruptDate = 0;

  // 建築貸款註記
  /* 若本筆貸款屬報送「央行消費與建築貸款統計表」中之建築貸款者請填'Y'（建築貸秋包括對建築業貸款、對其他企業建築貸款及對個人戶建築貸款）；非屬建築貸款請填'N' */
  @Column(name = "`BdLoanFg`", length = 1)
  private String bdLoanFg;

  // 授信餘額列報1（千元）之原始金額（元）
  /* 右靠左補0，單位新臺幣元；上開第29欄、第30欄、第31欄或第32欄之授信餘額列報為1（千元），即小於1,499元者，請將其原始金額以元為單位填報於本欄。本欄位如無資料請填'0' */
  @Column(name = "`SmallAmt`")
  private int smallAmt = 0;

  // 補充揭露案件註記－案件屬性
  /* 請填報該授信案件屬經主管機關函釋之爭議屬性：請填報該授信案件屬經主管機關函釋之爭議屬性：'A':遞延（預付）型消費性貸款；'B':客戶衍生性金融商品交易爭議案件；'C':協議分期償還放款依協議條件正常履約經報送為不良授信案件 */
  @Column(name = "`ExtraAttrCode`", length = 1)
  private String extraAttrCode;

  // 補充揭露案件註記－案件情形
  /* 請填報該授信案性屬經主管機關函釋之爭議案件：'01':學承電腦倒閉，'02':威爾斯美語倒閉；源自衍生性爭議案件者，請填報：'31':協商；'32':評議/調處；'33':仲裁；'34':訴訟；若同一授信有兩種以上情事者，請銀行自行判定以最主要/具強制力者為準。74.8欄如填案件屬性'C'之案件，本欄請填空白。 */
  @Column(name = "`ExtraStatusCode`", length = 2)
  private String extraStatusCode;

  // 空白
  /* 備用 */
  @Column(name = "`Filler74A`", length = 9)
  private String filler74A;

  // 資料所屬年月
  /* 請填報本筆授信資料所屬年月；每月正常報送授信餘額月報資料及發函更正(已上線)授信資料時，本欄位皆需報送資料所屬年月 */
  @Column(name = "`JcicDataYM`")
  private int jcicDataYM = 0;

  // 資料結束註記
  /* 空值，出表時判斷 (Y 該筆授信記錄結束 N 該筆授信記錄尚未結束) */
  @Column(name = "`DataEnd`", length = 1)
  private String dataEnd;

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public JcicB201Id getJcicB201Id() {
    return this.jcicB201Id;
  }

  public void setJcicB201Id(JcicB201Id jcicB201Id) {
    this.jcicB201Id = jcicB201Id;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param dataYM 資料年月
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 總行代號<br>
	* Key,金融機構總機構之代號，三位數字
  *
  * @param bankItem 總行代號
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 分行代號<br>
	* Key,金融機構分支機構之代號，四位數字
  *
  * @param branchItem 分行代號
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
  }

/**
	* 交易代碼<br>
	* A 新增 C 異動 D刪除 ;
每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：
1. 新增資料時填A;
2. 修改非key值欄位值時填C; 
3. 刪除原報送資料時填D;
4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
	* @return String
	*/
  public String getTranCode() {
    return this.tranCode == null ? "" : this.tranCode;
  }

/**
	* 交易代碼<br>
	* A 新增 C 異動 D刪除 ;
每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：
1. 新增資料時填A;
2. 修改非key值欄位值時填C; 
3. 刪除原報送資料時填D;
4. 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
  *
  * @param tranCode 交易代碼
	*/
  public void setTranCode(String tranCode) {
    this.tranCode = tranCode;
  }

/**
	* 帳號屬性註記<br>
	* A 本月新增帳號(非上月轉換而來)
C 本月轉換帳號(如填報C，請務必填報帳號轉換檔)
X 舊有帳號
	* @return String
	*/
  public String getSubTranCode() {
    return this.subTranCode == null ? "" : this.subTranCode;
  }

/**
	* 帳號屬性註記<br>
	* A 本月新增帳號(非上月轉換而來)
C 本月轉換帳號(如填報C，請務必填報帳號轉換檔)
X 舊有帳號
  *
  * @param subTranCode 帳號屬性註記
	*/
  public void setSubTranCode(String subTranCode) {
    this.subTranCode = subTranCode;
  }

/**
	* 本筆撥款帳號<br>
	* Key,左靠，填報本筆授信撥款帳號或交易序號，該帳號在同一金融機構內需為唯一(不可重複)，且一號到底(每月同筆撥款帳號均為相同號碼);若帳號無法定義至每筆撥款，如循環信用之存融、現金卡放款、透支戶及理財型房貸…等，則可定義為對應該筆撥款額度項下之帳號(可為契約或批覆書號碼)前揭循環信用業務若有另產生放款帳號，則以該放款帳號填報，若無產生放款帳號，則請填報原存款帳號。如為票券公司可以 成交單號 報送本欄位。若有部分轉催、部分轉呆或金融機構合併或轉籍之狀況，其帳號均不可重複，若其帳號無法一號到底需轉換帳號時，請於第3.2欄填報C(本月轉換帳號)，並務必另填報帳號轉換檔。
	* @return String
	*/
  public String getAcctNo() {
    return this.acctNo == null ? "" : this.acctNo;
  }

/**
	* 本筆撥款帳號<br>
	* Key,左靠，填報本筆授信撥款帳號或交易序號，該帳號在同一金融機構內需為唯一(不可重複)，且一號到底(每月同筆撥款帳號均為相同號碼);若帳號無法定義至每筆撥款，如循環信用之存融、現金卡放款、透支戶及理財型房貸…等，則可定義為對應該筆撥款額度項下之帳號(可為契約或批覆書號碼)前揭循環信用業務若有另產生放款帳號，則以該放款帳號填報，若無產生放款帳號，則請填報原存款帳號。如為票券公司可以 成交單號 報送本欄位。若有部分轉催、部分轉呆或金融機構合併或轉籍之狀況，其帳號均不可重複，若其帳號無法一號到底需轉換帳號時，請於第3.2欄填報C(本月轉換帳號)，並務必另填報帳號轉換檔。
  *
  * @param acctNo 本筆撥款帳號
	*/
  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

/**
	* 本筆撥款帳號序號<br>
	* 1=關係人5位以內
2=關係人5位以上記在第2筆
	* @return Integer
	*/
  public int getSeqNo() {
    return this.seqNo;
  }

/**
	* 本筆撥款帳號序號<br>
	* 1=關係人5位以內
2=關係人5位以上記在第2筆
  *
  * @param seqNo 本筆撥款帳號序號
	*/
  public void setSeqNo(int seqNo) {
    this.seqNo = seqNo;
  }

/**
	* 金額合計<br>
	* 右靠左補0，本欄為第24欄訂約金額(台幣)、第25欄訂約金額(外幣)、第29欄授信未逾期餘額(台幣)、第30欄授信未逾期餘額(外幣)、第31欄逾期未還餘額(台幣)、第32欄逾期未還金額(外幣)等六項金額欄位之合計，主要共金額欄位數值之校對。
	* @return BigDecimal
	*/
  public BigDecimal getTotalAmt() {
    return this.totalAmt;
  }

/**
	* 金額合計<br>
	* 右靠左補0，本欄為第24欄訂約金額(台幣)、第25欄訂約金額(外幣)、第29欄授信未逾期餘額(台幣)、第30欄授信未逾期餘額(外幣)、第31欄逾期未還餘額(台幣)、第32欄逾期未還金額(外幣)等六項金額欄位之合計，主要共金額欄位數值之校對。
  *
  * @param totalAmt 金額合計
	*/
  public void setTotalAmt(BigDecimal totalAmt) {
    this.totalAmt = totalAmt;
  }

/**
	* 授信戶IDN/BAN<br>
	* 左靠，法人請填八碼營利事業統一編號，自然人及非法人組織授信按請填自然人身份證統一編號或統一證號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 授信戶IDN/BAN<br>
	* 左靠，法人請填八碼營利事業統一編號，自然人及非法人組織授信按請填自然人身份證統一編號或統一證號
  *
  * @param custId 授信戶IDN/BAN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第6欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報
	* @return String
	*/
  public String getCustIdErr() {
    return this.custIdErr == null ? "" : this.custIdErr;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第6欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報
  *
  * @param custIdErr 上欄IDN或BAN錯誤註記
	*/
  public void setCustIdErr(String custIdErr) {
    this.custIdErr = custIdErr;
  }

/**
	* 負責人IDN/負責之事業體BAN<br>
	* A 企業授信戶負責人IDN
B 個人授信戶負責之事業體BAN
C 助學貸款戶學校代號
D 留學生就學貸款之留學生IDN

左靠，請填a 企業負責人之身分證統一編號
b 個人授信戶負責之事業體(僅限非法人組織)營利事業統一編號
c 如授信戶為高級中等以上學校就學貸款戶，本欄請填該筆貸款所屬之教育部編列六碼學校代號
d 如授信戶為留學生就學貸款，本欄位請填該筆貸款所屬留學生IDN，若授信戶為留學生本人，本欄與第6欄為相同之IDN
	* @return String
	*/
  public String getSuvId() {
    return this.suvId == null ? "" : this.suvId;
  }

/**
	* 負責人IDN/負責之事業體BAN<br>
	* A 企業授信戶負責人IDN
B 個人授信戶負責之事業體BAN
C 助學貸款戶學校代號
D 留學生就學貸款之留學生IDN

左靠，請填a 企業負責人之身分證統一編號
b 個人授信戶負責之事業體(僅限非法人組織)營利事業統一編號
c 如授信戶為高級中等以上學校就學貸款戶，本欄請填該筆貸款所屬之教育部編列六碼學校代號
d 如授信戶為留學生就學貸款，本欄位請填該筆貸款所屬留學生IDN，若授信戶為留學生本人，本欄與第6欄為相同之IDN
  *
  * @param suvId 負責人IDN/負責之事業體BAN
	*/
  public void setSuvId(String suvId) {
    this.suvId = suvId;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第8欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報
	* @return String
	*/
  public String getSuvIdErr() {
    return this.suvIdErr == null ? "" : this.suvIdErr;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第8欄授信戶IDN/BAN經邏輯檢查無誤者，本欄位空白，否則以 * 填報
  *
  * @param suvIdErr 上欄IDN或BAN錯誤註記
	*/
  public void setSuvIdErr(String suvIdErr) {
    this.suvIdErr = suvIdErr;
  }

/**
	* 外僑兼具中華民國國籍IDN<br>
	* 授信戶或授信戶負責人外僑兼具中華民國國籍，並以外橋身份編號填報於第6欄或第8欄者，本欄填其十位文數字國民身份證統一編號;如無前述狀況請填空白
	* @return String
	*/
  public String getOverseasId() {
    return this.overseasId == null ? "" : this.overseasId;
  }

/**
	* 外僑兼具中華民國國籍IDN<br>
	* 授信戶或授信戶負責人外僑兼具中華民國國籍，並以外橋身份編號填報於第6欄或第8欄者，本欄填其十位文數字國民身份證統一編號;如無前述狀況請填空白
  *
  * @param overseasId 外僑兼具中華民國國籍IDN
	*/
  public void setOverseasId(String overseasId) {
    this.overseasId = overseasId;
  }

/**
	* 授信戶行業別<br>
	* 授信對象所屬行業，請參考附件二授信戶行業別代號，若授信互為現金卡放款戶，本欄需填報該戶之職業身份代號(請參考附件二之一)
	* @return String
	*/
  public String getIndustryCode() {
    return this.industryCode == null ? "" : this.industryCode;
  }

/**
	* 授信戶行業別<br>
	* 授信對象所屬行業，請參考附件二授信戶行業別代號，若授信互為現金卡放款戶，本欄需填報該戶之職業身份代號(請參考附件二之一)
  *
  * @param industryCode 授信戶行業別
	*/
  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller12() {
    return this.filler12 == null ? "" : this.filler12;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler12 空白
	*/
  public void setFiller12(String filler12) {
    this.filler12 = filler12;
  }

/**
	* 科目別<br>
	* 請參考附件三授信科目代號表
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 科目別<br>
	* 請參考附件三授信科目代號表
  *
  * @param acctCode 科目別
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 科目別註記<br>
	* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表
	* @return String
	*/
  public String getSubAcctCode() {
    return this.subAcctCode == null ? "" : this.subAcctCode;
  }

/**
	* 科目別註記<br>
	* 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表
  *
  * @param subAcctCode 科目別註記
	*/
  public void setSubAcctCode(String subAcctCode) {
    this.subAcctCode = subAcctCode;
  }

/**
	* 轉催收款(或呆帳)前原科目別<br>
	* 第13欄 科目別填報A:催收款項及B呆帳者，或第41欄 不良債權處理註記 填報代號B 轉銷呆帳及C轉催收者，本欄位為必要填報項目，請填報轉催收款(或呆帳)前原核准貸放授信科目(若為轉呆帳，本欄位請勿填報催收科目A)，且每月需持續報送該轉催(呆)前授信科目，左靠右空白。
	* @return String
	*/
  public String getOrigAcctCode() {
    return this.origAcctCode == null ? "" : this.origAcctCode;
  }

/**
	* 轉催收款(或呆帳)前原科目別<br>
	* 第13欄 科目別填報A:催收款項及B呆帳者，或第41欄 不良債權處理註記 填報代號B 轉銷呆帳及C轉催收者，本欄位為必要填報項目，請填報轉催收款(或呆帳)前原核准貸放授信科目(若為轉呆帳，本欄位請勿填報催收科目A)，且每月需持續報送該轉催(呆)前授信科目，左靠右空白。
  *
  * @param origAcctCode 轉催收款(或呆帳)前原科目別
	*/
  public void setOrigAcctCode(String origAcctCode) {
    this.origAcctCode = origAcctCode;
  }

/**
	* 個人消費性貸款註記<br>
	* 個人貸款屬消費性貸款者請填Y，屬於非消費性貸款(如非法人組織貸款)請填N，非屬個人貸款填X
	* @return String
	*/
  public String getConsumeFg() {
    return this.consumeFg == null ? "" : this.consumeFg;
  }

/**
	* 個人消費性貸款註記<br>
	* 個人貸款屬消費性貸款者請填Y，屬於非消費性貸款(如非法人組織貸款)請填N，非屬個人貸款填X
  *
  * @param consumeFg 個人消費性貸款註記
	*/
  public void setConsumeFg(String consumeFg) {
    this.consumeFg = consumeFg;
  }

/**
	* 融資分類<br>
	* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款
個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 融資分類<br>
	* 法人特殊融資分為五類 A 專案融資 B 商品融資 C標的融資 D 收益型商用不動產融資 E 高風險商用不動產融資 K 非屬前述特殊融資之其他一般法人金融貸款
個人(含非法人組織)金融貸款分類為L 購買住宅貸款(非自用) M 購買住宅貸款(自用) N 一般房屋抵押貸款(房屋修繕貸款) O 由信用卡衍生之小額信用貸款 R 由現金卡衍生之小額信用貸款 S 創業貸款 T 所營事業營運周轉金貸款 U 建築用融資貸款 V 代墊投標保證金貸款 W 農業用途貸款 X 參與都市更新更新計畫貸款 Y 企業員工認購股票(或可轉換公司債)貸款 ,Z 其他個人金融貸款 ,1 個人投資理財貸款
  *
  * @param finCode 融資分類
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }

/**
	* 政府專業補助貸款分類<br>
	* 填報該筆授信屬何種政府專案補助性質之貸款：專案補助貸款大項分類為01 振興傳統產業專案貸款 02 輔導中小企業升級或協助紮根貸款 03 振興景氣分案專案貸款 04 購置自動化機器 05 行政院開發基金或中小企業發展基金 06 微型企業創業貸款 07 九二一大地震金融因應措施 08 SARS 金融因應措施 09 青年優惠房屋貸款 10 優惠購屋專案貸款 11 輔助人民自購住宅貸款(首購或非首購) 12 國民住宅貸款 13 公教人員購置住宅 14 勞工貸款(含自購住宅及房屋修繕) 15 國軍官兵貸款 17 原住民貸款 18 學生助學貸款 19 獎勵數位內容產業及文化創意產業優惠貸款 20 留學生就學貸款 21 青年創業貸款(非屬農業部分 ) 22 天然災害房屋整修及重建貸款(非屬九二一地震受災戶部分) 23 參與度是更新計畫貸款  31 農機貸款 32 輔導農糧業經營貸款 33 輔導漁業經營貸款 34提升處勤產業經營貸款 35 農民經營改善貸款 36 農業科技園區進駐業者貸款 37 山坡地保育利用貸款 38 農業綜合貸款 39 改善財物貸款 40 農會事業發展貸款 41 農業產銷班貸款 50 民營事業污染防治設備低利貸款 51 行政院醫療發展基金構建醫療機構(設備)貸款 ，52 經濟部中小企業發展基金支援辦理四項專案貸款 99 其他政府專案補助貸款，如非屬專案補助貸款之授信填XX。
	* @return String
	*/
  public String getProjCode() {
    return this.projCode == null ? "" : this.projCode;
  }

/**
	* 政府專業補助貸款分類<br>
	* 填報該筆授信屬何種政府專案補助性質之貸款：專案補助貸款大項分類為01 振興傳統產業專案貸款 02 輔導中小企業升級或協助紮根貸款 03 振興景氣分案專案貸款 04 購置自動化機器 05 行政院開發基金或中小企業發展基金 06 微型企業創業貸款 07 九二一大地震金融因應措施 08 SARS 金融因應措施 09 青年優惠房屋貸款 10 優惠購屋專案貸款 11 輔助人民自購住宅貸款(首購或非首購) 12 國民住宅貸款 13 公教人員購置住宅 14 勞工貸款(含自購住宅及房屋修繕) 15 國軍官兵貸款 17 原住民貸款 18 學生助學貸款 19 獎勵數位內容產業及文化創意產業優惠貸款 20 留學生就學貸款 21 青年創業貸款(非屬農業部分 ) 22 天然災害房屋整修及重建貸款(非屬九二一地震受災戶部分) 23 參與度是更新計畫貸款  31 農機貸款 32 輔導農糧業經營貸款 33 輔導漁業經營貸款 34提升處勤產業經營貸款 35 農民經營改善貸款 36 農業科技園區進駐業者貸款 37 山坡地保育利用貸款 38 農業綜合貸款 39 改善財物貸款 40 農會事業發展貸款 41 農業產銷班貸款 50 民營事業污染防治設備低利貸款 51 行政院醫療發展基金構建醫療機構(設備)貸款 ，52 經濟部中小企業發展基金支援辦理四項專案貸款 99 其他政府專案補助貸款，如非屬專案補助貸款之授信填XX。
  *
  * @param projCode 政府專業補助貸款分類
	*/
  public void setProjCode(String projCode) {
    this.projCode = projCode;
  }

/**
	* 不計入授信項目<br>
	* 不計入授信項目代號請參照附件四
	* @return String
	*/
  public String getNonCreditCode() {
    return this.nonCreditCode == null ? "" : this.nonCreditCode;
  }

/**
	* 不計入授信項目<br>
	* 不計入授信項目代號請參照附件四
  *
  * @param nonCreditCode 不計入授信項目
	*/
  public void setNonCreditCode(String nonCreditCode) {
    this.nonCreditCode = nonCreditCode;
  }

/**
	* 用途別<br>
	* 用途別請參照附件五 (1:購置不動產 2:購置動產 3:企業投資 4:週轉金)
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 用途別<br>
	* 用途別請參照附件五 (1:購置不動產 2:購置動產 3:企業投資 4:週轉金)
  *
  * @param usageCode 用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 本筆撥款利率<br>
	* 本項資料本中心不予揭露，僅將作為本中心協助主管機關及會員金融機構風險因子計算之基礎及方法驗證之用。
單位：百分比，填報本筆撥款帳號報送當月底之適用利率，如屬專案補助貸款則填報其實際計息利率;若保證業務已發生墊款，請填報保證墊款利率;八位文數字，以xx.xxxxx表示，填至小數點以下五位，例如3.456％請填03.45600
	* @return BigDecimal
	*/
  public BigDecimal getApproveRate() {
    return this.approveRate;
  }

/**
	* 本筆撥款利率<br>
	* 本項資料本中心不予揭露，僅將作為本中心協助主管機關及會員金融機構風險因子計算之基礎及方法驗證之用。
單位：百分比，填報本筆撥款帳號報送當月底之適用利率，如屬專案補助貸款則填報其實際計息利率;若保證業務已發生墊款，請填報保證墊款利率;八位文數字，以xx.xxxxx表示，填至小數點以下五位，例如3.456％請填03.45600
  *
  * @param approveRate 本筆撥款利率
	*/
  public void setApproveRate(BigDecimal approveRate) {
    this.approveRate = approveRate;
  }

/**
	* 本筆撥款開始年月<br>
	* 以'YYYMM'(民國年)表示，填報對應本筆撥款之起始年月，例如撥歌開始於92年1月，請填'09201'；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）或屬有額度未動用授信，則填報其合約起始年月
	* @return Integer
	*/
  public int getDrawdownDate() {
    return this.drawdownDate;
  }

/**
	* 本筆撥款開始年月<br>
	* 以'YYYMM'(民國年)表示，填報對應本筆撥款之起始年月，例如撥歌開始於92年1月，請填'09201'；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）或屬有額度未動用授信，則填報其合約起始年月
  *
  * @param drawdownDate 本筆撥款開始年月
	*/
  public void setDrawdownDate(int drawdownDate) {
    this.drawdownDate = drawdownDate;
  }

/**
	* 本筆撥款約定清償年月<br>
	* 以'YYYMM'(民國年)表示，填報對應本筆撥款之約定清償年月，例如撥歌之釣定清債年月為112年1月，請填'11201'；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）或屬有額度未動用授信，則填報其合約截止年月；如為中長期分期償還放款，本欄請填報約定之最後清償年月；如無約定清償年月或到期年月，本欄請填'99999'
	* @return Integer
	*/
  public int getMaturityDate() {
    return this.maturityDate;
  }

/**
	* 本筆撥款約定清償年月<br>
	* 以'YYYMM'(民國年)表示，填報對應本筆撥款之約定清償年月，例如撥歌之釣定清債年月為112年1月，請填'11201'；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）或屬有額度未動用授信，則填報其合約截止年月；如為中長期分期償還放款，本欄請填報約定之最後清償年月；如無約定清償年月或到期年月，本欄請填'99999'
  *
  * @param maturityDate 本筆撥款約定清償年月
	*/
  public void setMaturityDate(int maturityDate) {
    this.maturityDate = maturityDate;
  }

/**
	* 授信餘額幣別<br>
	* 三個英文字．填報該幣別國際通用英文字母代號，請參照附件六"國際通用幣別代號表"
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 授信餘額幣別<br>
	* 三個英文字．填報該幣別國際通用英文字母代號，請參照附件六"國際通用幣別代號表"
  *
  * @param currencyCode 授信餘額幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 訂約金額(台幣)<br>
	* 右靠左補0，填報本筆撥款帳號所屬訂約金額(現金卡放款請填報契約額度)，單位新台幣千元，本欄位與第25欄請擇一填報；有追索權應收帳款請填報融資額度；若有共用額度狀況，請於各該筆撥款之本欄位皆填0，並將該共用額度額度金額填報於授信額度資料檔之「本階訂約金額」欄，本欄位如無資料請填'0'
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmt() {
    return this.drawdownAmt;
  }

/**
	* 訂約金額(台幣)<br>
	* 右靠左補0，填報本筆撥款帳號所屬訂約金額(現金卡放款請填報契約額度)，單位新台幣千元，本欄位與第25欄請擇一填報；有追索權應收帳款請填報融資額度；若有共用額度狀況，請於各該筆撥款之本欄位皆填0，並將該共用額度額度金額填報於授信額度資料檔之「本階訂約金額」欄，本欄位如無資料請填'0'
  *
  * @param drawdownAmt 訂約金額(台幣)
	*/
  public void setDrawdownAmt(BigDecimal drawdownAmt) {
    this.drawdownAmt = drawdownAmt;
  }

/**
	* 訂約金額(外幣)<br>
	* 右靠左補0，填報本筆撥款帳號所屬訂約金額(現金卡放款請填報契約額度)，單位新台幣千元，本欄位與第25欄請擇一填報；有追索權應收帳款請填報融資額度；若有共用額度狀況，請於各該筆撥款之本欄位皆填0，並將該共用額度額度金額填報於授信額度資料檔之「本階訂約金額」欄，本欄位如無資料請填'0'
	* @return BigDecimal
	*/
  public BigDecimal getDrawdownAmtFx() {
    return this.drawdownAmtFx;
  }

/**
	* 訂約金額(外幣)<br>
	* 右靠左補0，填報本筆撥款帳號所屬訂約金額(現金卡放款請填報契約額度)，單位新台幣千元，本欄位與第25欄請擇一填報；有追索權應收帳款請填報融資額度；若有共用額度狀況，請於各該筆撥款之本欄位皆填0，並將該共用額度額度金額填報於授信額度資料檔之「本階訂約金額」欄，本欄位如無資料請填'0'
  *
  * @param drawdownAmtFx 訂約金額(外幣)
	*/
  public void setDrawdownAmtFx(BigDecimal drawdownAmtFx) {
    this.drawdownAmtFx = drawdownAmtFx;
  }

/**
	* 循環信用註記<br>
	* 'Y':是，'N':否，填報該筆授信是否屬循環性質
	* @return String
	*/
  public String getRecycleCode() {
    return this.recycleCode == null ? "" : this.recycleCode;
  }

/**
	* 循環信用註記<br>
	* 'Y':是，'N':否，填報該筆授信是否屬循環性質
  *
  * @param recycleCode 循環信用註記
	*/
  public void setRecycleCode(String recycleCode) {
    this.recycleCode = recycleCode;
  }

/**
	* 額度可否撤銷<br>
	* Y：可撤銷，N:不可撤銷；
可撤銷之定義，依據96年金管會銀行局發佈之【銀行自有資本與風險性資產之計算方法說明及表格】第7頁所示，如該筆授信符合信用轉換係數為0者屬之，意即：
(1)銀行無需事先通知即得隨時無條件取消之承諾【註：若銀行對客戶之零售債權承諾，符合消費者保護法及相關
法令規定，且銀行得隨時無條件取消該承諾者，得被認為係無條件可取消之承諾。】
(2)當借款人信用貶落時，銀行有權自動取消之承諾
否則該授信可視為不可撤銷
	* @return String
	*/
  public String getIrrevocableFlag() {
    return this.irrevocableFlag == null ? "" : this.irrevocableFlag;
  }

/**
	* 額度可否撤銷<br>
	* Y：可撤銷，N:不可撤銷；
可撤銷之定義，依據96年金管會銀行局發佈之【銀行自有資本與風險性資產之計算方法說明及表格】第7頁所示，如該筆授信符合信用轉換係數為0者屬之，意即：
(1)銀行無需事先通知即得隨時無條件取消之承諾【註：若銀行對客戶之零售債權承諾，符合消費者保護法及相關
法令規定，且銀行得隨時無條件取消該承諾者，得被認為係無條件可取消之承諾。】
(2)當借款人信用貶落時，銀行有權自動取消之承諾
否則該授信可視為不可撤銷
  *
  * @param irrevocableFlag 額度可否撤銷
	*/
  public void setIrrevocableFlag(String irrevocableFlag) {
    this.irrevocableFlag = irrevocableFlag;
  }

/**
	* 上階共用額度控制編碼<br>
	* 每筆撥款之共用額度控制編碼，若為不同筆撥款共用一授信額度時，皆需於本欄填報多肇撥款相同之上階共用額度控制編碼；如無對應額度檔，則以'9'填滿本欄位
	* @return String
	*/
  public String getFacmNo() {
    return this.facmNo == null ? "" : this.facmNo;
  }

/**
	* 上階共用額度控制編碼<br>
	* 每筆撥款之共用額度控制編碼，若為不同筆撥款共用一授信額度時，皆需於本欄填報多肇撥款相同之上階共用額度控制編碼；如無對應額度檔，則以'9'填滿本欄位
  *
  * @param facmNo 上階共用額度控制編碼
	*/
  public void setFacmNo(String facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 未逾期/乙類逾期/應予觀察授信餘額(台幣)<br>
	* a.未逾期授信餘額
b.乙類逾期放款餘額
c.應予觀察授信餘額
右靠左補0，單位新台幣千元；
a.如該筆為未逾期授信
b.如該筆為乙類逾期放款（即第40欄填報代號'1'~'7'者）
c.如該筆為應予觀察授信（即第40欄填報代號'A'~'B'者）
請將其台幣授信餘額填報於本欄位。有追索權應收帳款請填報融資餘額；凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填'1'，本欄位如無資料請填'0'
	* @return BigDecimal
	*/
  public BigDecimal getUnDelayBal() {
    return this.unDelayBal;
  }

/**
	* 未逾期/乙類逾期/應予觀察授信餘額(台幣)<br>
	* a.未逾期授信餘額
b.乙類逾期放款餘額
c.應予觀察授信餘額
右靠左補0，單位新台幣千元；
a.如該筆為未逾期授信
b.如該筆為乙類逾期放款（即第40欄填報代號'1'~'7'者）
c.如該筆為應予觀察授信（即第40欄填報代號'A'~'B'者）
請將其台幣授信餘額填報於本欄位。有追索權應收帳款請填報融資餘額；凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填'1'，本欄位如無資料請填'0'
  *
  * @param unDelayBal 未逾期/乙類逾期/應予觀察授信餘額(台幣)
	*/
  public void setUnDelayBal(BigDecimal unDelayBal) {
    this.unDelayBal = unDelayBal;
  }

/**
	* 未逾期/乙類逾期/應予觀察授信餘額(外幣)<br>
	* a.未逾期授信餘額
b.乙類逾期放款餘額
c.應予觀察授信餘額
右靠左補0，單位新台幣千元，填報等值之台幣金額；
請將其外幣授信餘額換算為台幣填報於本欄位。有追索權應收帳款請填報融資餘額；凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填'1'，本欄位如無資料請填'0'
	* @return BigDecimal
	*/
  public BigDecimal getUnDelayBalFx() {
    return this.unDelayBalFx;
  }

/**
	* 未逾期/乙類逾期/應予觀察授信餘額(外幣)<br>
	* a.未逾期授信餘額
b.乙類逾期放款餘額
c.應予觀察授信餘額
右靠左補0，單位新台幣千元，填報等值之台幣金額；
請將其外幣授信餘額換算為台幣填報於本欄位。有追索權應收帳款請填報融資餘額；凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填'1'，本欄位如無資料請填'0'
  *
  * @param unDelayBalFx 未逾期/乙類逾期/應予觀察授信餘額(外幣)
	*/
  public void setUnDelayBalFx(BigDecimal unDelayBalFx) {
    this.unDelayBalFx = unDelayBalFx;
  }

/**
	* 逾期未還餘額（台幣）<br>
	* 右靠左補0，單位新台幣千元；如該筆授信屬甲類逾期放款（即第39欄填報代號 "1"~"4"者）、逾期授信（即第39欄填報代號"A"、"B"者）、催收款（授信科目"A"）已列報逾期者或呆帳（授信科目"B"），請將其台幣授信餘額填報於本欄位。凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填‘1’，本欄位如無資料請填‘0’
	* @return BigDecimal
	*/
  public BigDecimal getDelayBal() {
    return this.delayBal;
  }

/**
	* 逾期未還餘額（台幣）<br>
	* 右靠左補0，單位新台幣千元；如該筆授信屬甲類逾期放款（即第39欄填報代號 "1"~"4"者）、逾期授信（即第39欄填報代號"A"、"B"者）、催收款（授信科目"A"）已列報逾期者或呆帳（授信科目"B"），請將其台幣授信餘額填報於本欄位。凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填‘1’，本欄位如無資料請填‘0’
  *
  * @param delayBal 逾期未還餘額（台幣）
	*/
  public void setDelayBal(BigDecimal delayBal) {
    this.delayBal = delayBal;
  }

/**
	* 逾期未還餘額（外幣）<br>
	* 右靠左補0，單位新台幣千元，填報等值之台幣金額；如該筆授信屬甲類逾期放款（即第39欄填報代號 "1"~"4"者）、逾期授信（即第39欄填報代號"A"、"B"者）、催收款（授信科目"A"）已列報逾期者或呆帳（授信科目"B"），請將其外幣授信餘額換算為台幣填報於本欄位。凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填‘1’，本欄位如無資料請填‘0’
	* @return BigDecimal
	*/
  public BigDecimal getDelayBalFx() {
    return this.delayBalFx;
  }

/**
	* 逾期未還餘額（外幣）<br>
	* 右靠左補0，單位新台幣千元，填報等值之台幣金額；如該筆授信屬甲類逾期放款（即第39欄填報代號 "1"~"4"者）、逾期授信（即第39欄填報代號"A"、"B"者）、催收款（授信科目"A"）已列報逾期者或呆帳（授信科目"B"），請將其外幣授信餘額換算為台幣填報於本欄位。凡本欄位金額請四捨五入後填報，不足千元者以一千元計，請填‘1’，本欄位如無資料請填‘0’
  *
  * @param delayBalFx 逾期未還餘額（外幣）
	*/
  public void setDelayBalFx(BigDecimal delayBalFx) {
    this.delayBalFx = delayBalFx;
  }

/**
	* 逾期期限<br>
	* 逾期期限代號請參照附件七，其中代號"9":未逾期或乙類逾期/應予觀察授信，屬於未逾期、乙類逾期或應予觀察授信者，其逾期期限代號請填報'9’
如本筆屬有額度未動用授信，本欄位請填報'9’
	* @return String
	*/
  public String getDelayPeriodCode() {
    return this.delayPeriodCode == null ? "" : this.delayPeriodCode;
  }

/**
	* 逾期期限<br>
	* 逾期期限代號請參照附件七，其中代號"9":未逾期或乙類逾期/應予觀察授信，屬於未逾期、乙類逾期或應予觀察授信者，其逾期期限代號請填報'9’
如本筆屬有額度未動用授信，本欄位請填報'9’
  *
  * @param delayPeriodCode 逾期期限
	*/
  public void setDelayPeriodCode(String delayPeriodCode) {
    this.delayPeriodCode = delayPeriodCode;
  }

/**
	* 本月還款紀錄<br>
	* 填報本月底應還款（包括本金、利息或本利攤還）之正常還款或遲延情形，代號1~6單位為月份，請參照附件八還款紀錄代號表
如本筆屬有額度未動用授信*本欄位請填報'X'
	* @return String
	*/
  public String getRepayCode() {
    return this.repayCode == null ? "" : this.repayCode;
  }

/**
	* 本月還款紀錄<br>
	* 填報本月底應還款（包括本金、利息或本利攤還）之正常還款或遲延情形，代號1~6單位為月份，請參照附件八還款紀錄代號表
如本筆屬有額度未動用授信*本欄位請填報'X'
  *
  * @param repayCode 本月還款紀錄
	*/
  public void setRepayCode(String repayCode) {
    this.repayCode = repayCode;
  }

/**
	* 本月（累計）應繳金額<br>
	* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，填報本月應還款（包括本金、利息 ' 本利攤還及其他應繳費用）之金額，或催收款、呆帳之對外債權金額（呆帳之對外債權若屬有擔保且送法院拍賣者，其金額應為報給法院之債權金額），以當月之最後繳款期限日（非繳款期限日後之最大寬限期）為準。房貸業務請務必填報每月依約應還款金額於本欄位；如為循環型授信，且有最低應繳金額者，請填當月最低應繳金額。若該筆授信於上月或前一期繳款期限日前未繳或未繳足應繳金額，本月本欄則填報累計應繳金額。若於當月有一次以上之應繳金額與繳款期限日（如雙週繳款之攤還型授信），則以當月最後一次繳款期限日為準，填報累計之應繳金額。若金額小於千元部分，請以小數點方式填報，例如2345元請填'0000002.345'；若該筆授信當月無應繳金額，或循環型授信無當月最低應繳金額，本欄請填'0'
	* @return BigDecimal
	*/
  public BigDecimal getPayAmt() {
    return this.payAmt;
  }

/**
	* 本月（累計）應繳金額<br>
	* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，填報本月應還款（包括本金、利息 ' 本利攤還及其他應繳費用）之金額，或催收款、呆帳之對外債權金額（呆帳之對外債權若屬有擔保且送法院拍賣者，其金額應為報給法院之債權金額），以當月之最後繳款期限日（非繳款期限日後之最大寬限期）為準。房貸業務請務必填報每月依約應還款金額於本欄位；如為循環型授信，且有最低應繳金額者，請填當月最低應繳金額。若該筆授信於上月或前一期繳款期限日前未繳或未繳足應繳金額，本月本欄則填報累計應繳金額。若於當月有一次以上之應繳金額與繳款期限日（如雙週繳款之攤還型授信），則以當月最後一次繳款期限日為準，填報累計之應繳金額。若金額小於千元部分，請以小數點方式填報，例如2345元請填'0000002.345'；若該筆授信當月無應繳金額，或循環型授信無當月最低應繳金額，本欄請填'0'
  *
  * @param payAmt 本月（累計）應繳金額
	*/
  public void setPayAmt(BigDecimal payAmt) {
    this.payAmt = payAmt;
  }

/**
	* 本月收回本金<br>
	* 本項資料本中心不予揭露，右靠左補0，單位:新台幣千元，請填本筆授信債權自撥款後之本月回收本金；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）授信，仍需就每月印用額度後之合計收回本金填報本欄位；若金額小於千元部分，請以小數點方式填報，例如2345元請填'0000002.345'；本月如有退回收回本金‧請於本欄位第一碼（即左靠）加註'-'（負號），例如本月退回
12000元請填'-0000000012'；本月如無收回本金.本欄請填'0'
	* @return BigDecimal
	*/
  public BigDecimal getPrincipal() {
    return this.principal;
  }

/**
	* 本月收回本金<br>
	* 本項資料本中心不予揭露，右靠左補0，單位:新台幣千元，請填本筆授信債權自撥款後之本月回收本金；若為循環信用（例如存融、現金卡放款、透支戶及理財型房貸等）授信，仍需就每月印用額度後之合計收回本金填報本欄位；若金額小於千元部分，請以小數點方式填報，例如2345元請填'0000002.345'；本月如有退回收回本金‧請於本欄位第一碼（即左靠）加註'-'（負號），例如本月退回
12000元請填'-0000000012'；本月如無收回本金.本欄請填'0'
  *
  * @param principal 本月收回本金
	*/
  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

/**
	* 本月收取利息<br>
	* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，請填本筆授信債權之本月收取利息，若金額小於千元部分，請以小數點方式填報，例如2345元請填'000002.345；本月如有退回收取利息，請於本欄位第一碼（即左靠）加註'-'（負號）；本月如無收取利息，本欄請填0
	* @return BigDecimal
	*/
  public BigDecimal getInterest() {
    return this.interest;
  }

/**
	* 本月收取利息<br>
	* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，請填本筆授信債權之本月收取利息，若金額小於千元部分，請以小數點方式填報，例如2345元請填'000002.345；本月如有退回收取利息，請於本欄位第一碼（即左靠）加註'-'（負號）；本月如無收取利息，本欄請填0
  *
  * @param interest 本月收取利息
	*/
  public void setInterest(BigDecimal interest) {
    this.interest = interest;
  }

/**
	* 本月收取其他費用<br>
	* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，請填本筆授信債權之本月收取其他費用；回收其他費用係包括開辦費、手續費、簽證費、承銷費、管理費、承諾費、違約金、法務費用及訴訟費用等所有相關成本費用。若金額小於千元部分，請以小數點方式填報，例如2345元請填 '000002.345；本月如有退回收取其他費用，請於本欄位第一碼（即左靠）加註'-'（負號）；本月如無收取其他費用，本欄請填0
	* @return BigDecimal
	*/
  public BigDecimal getFee() {
    return this.fee;
  }

/**
	* 本月收取其他費用<br>
	* 本項資料本中心不予揭露，右靠左補0，單位：新台幣千元，請填本筆授信債權之本月收取其他費用；回收其他費用係包括開辦費、手續費、簽證費、承銷費、管理費、承諾費、違約金、法務費用及訴訟費用等所有相關成本費用。若金額小於千元部分，請以小數點方式填報，例如2345元請填 '000002.345；本月如有退回收取其他費用，請於本欄位第一碼（即左靠）加註'-'（負號）；本月如無收取其他費用，本欄請填0
  *
  * @param fee 本月收取其他費用
	*/
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

/**
	* 甲類逾期放款分類<br>
	* 左靠右空白，
a 如該筆授信屬甲類逾期放款，請填報其分類代號如下 1 放款本金超過清償期三個月而未獲清償，或雖為屆滿三個月，但已向主、從債務人訴追獲處分擔保品者， 2 放款本金未超過清償期三個月，而利息未按期繳納超過六個月者， 3 分期償還放款未按期攤還超過六個月，或雖未屆滿六個月，但已向主、從債務人訴追獲處分擔保品者， 4 協議分期償還放款符合一定條件而曾經免列報逾期放款案件，於免列報期間再發生未依約清償超過三個月者；同一筆授信之分類，請於本欄位之分類代號與第40欄之分類代號中則一填報；若非屬甲類逾期放款請填 X。
	* @return String
	*/
  public String getFirstDelayCode() {
    return this.firstDelayCode == null ? "" : this.firstDelayCode;
  }

/**
	* 甲類逾期放款分類<br>
	* 左靠右空白，
a 如該筆授信屬甲類逾期放款，請填報其分類代號如下 1 放款本金超過清償期三個月而未獲清償，或雖為屆滿三個月，但已向主、從債務人訴追獲處分擔保品者， 2 放款本金未超過清償期三個月，而利息未按期繳納超過六個月者， 3 分期償還放款未按期攤還超過六個月，或雖未屆滿六個月，但已向主、從債務人訴追獲處分擔保品者， 4 協議分期償還放款符合一定條件而曾經免列報逾期放款案件，於免列報期間再發生未依約清償超過三個月者；同一筆授信之分類，請於本欄位之分類代號與第40欄之分類代號中則一填報；若非屬甲類逾期放款請填 X。
  *
  * @param firstDelayCode 甲類逾期放款分類
	*/
  public void setFirstDelayCode(String firstDelayCode) {
    this.firstDelayCode = firstDelayCode;
  }

/**
	* 乙類逾期放款分類<br>
	* a 如該筆授信與乙類逾期放款，請填報其分類代號如下:1 放款本金未超過清償期三個月，惟利息未按期繳納超過清償期三個月，分期償還放款未按期攤還超過三個月至六個月者， 3 協議分期償還放款，協議條件符合規定，且借款戶依協議條件按期履約未滿六個月者， 4 已獲信用保證基金同意理賠款項或有足額存單或存款備償(需辦妥質權設定且徵得發單行拋棄抵銷權同意書)，而約定待其他債務人財產處分後再予沖償者， 5 已確定分配之債權，惟尚未接獲分配款者， 6 債務人兼擔保品提供人死亡，於辦理繼承期間，借期而為清償之放款，其繳息正常且有十足擔保者， 7 其他(如921震災展延本息之放款)；同一筆授信之分類，請於本欄位之分類代號與第39欄之分類代號中則一填報；若非屬類逾期放款請填 X。
	* @return String
	*/
  public String getSecondDelayCode() {
    return this.secondDelayCode == null ? "" : this.secondDelayCode;
  }

/**
	* 乙類逾期放款分類<br>
	* a 如該筆授信與乙類逾期放款，請填報其分類代號如下:1 放款本金未超過清償期三個月，惟利息未按期繳納超過清償期三個月，分期償還放款未按期攤還超過三個月至六個月者， 3 協議分期償還放款，協議條件符合規定，且借款戶依協議條件按期履約未滿六個月者， 4 已獲信用保證基金同意理賠款項或有足額存單或存款備償(需辦妥質權設定且徵得發單行拋棄抵銷權同意書)，而約定待其他債務人財產處分後再予沖償者， 5 已確定分配之債權，惟尚未接獲分配款者， 6 債務人兼擔保品提供人死亡，於辦理繼承期間，借期而為清償之放款，其繳息正常且有十足擔保者， 7 其他(如921震災展延本息之放款)；同一筆授信之分類，請於本欄位之分類代號與第39欄之分類代號中則一填報；若非屬類逾期放款請填 X。
  *
  * @param secondDelayCode 乙類逾期放款分類
	*/
  public void setSecondDelayCode(String secondDelayCode) {
    this.secondDelayCode = secondDelayCode;
  }

/**
	* 不良債權處理註記<br>
	* 如該筆授信債權於本月處理且結案者，請直接填報第42欄 債權結案註記 ，本欄位無須填報；本欄位僅處理當月報送處理註記代號即可，次月不需重複報送，左靠。P 不良債權主債務人自償 Q 不良債權從債務人代位償還 R 主、從債務人以外之第三人代位償還，G 代物償還(指債權人受領其他給付，以消滅原債權之償還)，E 債務承擔(指原有債務為第三人承擔)，D 處分擔保品 A 承受擔保品，本欄位若有填報代號P、 Q、 R、G、 D及A者，亦請配合於第36、37或38欄填報收回金額；B轉銷呆帳，C 轉催收款，如無前述狀況請填空白，勿填0。
	* @return String
	*/
  public String getBadDebtCode() {
    return this.BadDebtCode == null ? "" : this.BadDebtCode;
  }

/**
	* 不良債權處理註記<br>
	* 如該筆授信債權於本月處理且結案者，請直接填報第42欄 債權結案註記 ，本欄位無須填報；本欄位僅處理當月報送處理註記代號即可，次月不需重複報送，左靠。P 不良債權主債務人自償 Q 不良債權從債務人代位償還 R 主、從債務人以外之第三人代位償還，G 代物償還(指債權人受領其他給付，以消滅原債權之償還)，E 債務承擔(指原有債務為第三人承擔)，D 處分擔保品 A 承受擔保品，本欄位若有填報代號P、 Q、 R、G、 D及A者，亦請配合於第36、37或38欄填報收回金額；B轉銷呆帳，C 轉催收款，如無前述狀況請填空白，勿填0。
  *
  * @param BadDebtCode 不良債權處理註記
	*/
  public void setBadDebtCode(String BadDebtCode) {
    this.BadDebtCode = BadDebtCode;
  }

/**
	* 債權結束註記<br>
	* 左靠，3 正常債權全額清償 ；T 債權轉讓， S債權證券化；P 不良債權主債務人自償 ， Q 不良債權從債務人代位償還 R 主、從債務人以外之第三人代位償還，G 代物償還(指債權人受領其他給付，以消滅原債權之償還)，E 債務承擔(指原有債務為第三人承擔)，D 處分擔保品 A 承受擔保品；H 債權和解，N債權拋棄 ，J 合併或業務移轉，X 債權轉讓後原債權機購買回，F 信保基金退理賠，Z 結清銷戶；如無結案狀況請填空白，勿填0。
	* @return String
	*/
  public String getNegStatus() {
    return this.negStatus == null ? "" : this.negStatus;
  }

/**
	* 債權結束註記<br>
	* 左靠，3 正常債權全額清償 ；T 債權轉讓， S債權證券化；P 不良債權主債務人自償 ， Q 不良債權從債務人代位償還 R 主、從債務人以外之第三人代位償還，G 代物償還(指債權人受領其他給付，以消滅原債權之償還)，E 債務承擔(指原有債務為第三人承擔)，D 處分擔保品 A 承受擔保品；H 債權和解，N債權拋棄 ，J 合併或業務移轉，X 債權轉讓後原債權機購買回，F 信保基金退理賠，Z 結清銷戶；如無結案狀況請填空白，勿填0。
  *
  * @param negStatus 債權結束註記
	*/
  public void setNegStatus(String negStatus) {
    this.negStatus = negStatus;
  }

/**
	* 債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN<br>
	* a 債權處理後新債權人IDN/BAN
b 債權轉讓後原債權機購買回之前手債權人IDN/BAN
c 信保基金退理賠信用保證機構BAN
左靠，a.如第42欄填T或S，本欄位請填報債權轉讓資產管理公司、其他機構或第三人，辦理資產證券化受託機構或特殊目的公司之IDN/BAN
b.如第42欄填X，本欄請填報債權轉讓後原債權機構買回之前手債權人IDN/BAN
c.如第42欄填F，本欄請填報信保基金退理賠信用保證機構之八碼營利事業統一編號
本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getNegCreditor() {
    return this.negCreditor == null ? "" : this.negCreditor;
  }

/**
	* 債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN<br>
	* a 債權處理後新債權人IDN/BAN
b 債權轉讓後原債權機購買回之前手債權人IDN/BAN
c 信保基金退理賠信用保證機構BAN
左靠，a.如第42欄填T或S，本欄位請填報債權轉讓資產管理公司、其他機構或第三人，辦理資產證券化受託機構或特殊目的公司之IDN/BAN
b.如第42欄填X，本欄請填報債權轉讓後原債權機構買回之前手債權人IDN/BAN
c.如第42欄填F，本欄請填報信保基金退理賠信用保證機構之八碼營利事業統一編號
本欄位如無資料請填空白，勿填0。
  *
  * @param negCreditor 債權處理後新債權人ID/債權轉讓後前手債權人ID/信保基金退理賠信用保證機構BAN
	*/
  public void setNegCreditor(String negCreditor) {
    this.negCreditor = negCreditor;
  }

/**
	* 債權處理案號<br>
	* 左靠，如第42欄填T或S者，本欄統一以YYYMMDDNXYYYMM共14碼表示，其資料欄位定義/結構如下:前7碼YYYMMDD：契約簽訂日期；第8碼N:當日摽售批次序號，1-第一批出售案、2-第二批出售案；第9碼X:債權類別，B:現金卡、C:授信(不含現金卡)；第10~14碼YYYMM債權處理年月(債權交割日)；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getNegNo() {
    return this.negNo == null ? "" : this.negNo;
  }

/**
	* 債權處理案號<br>
	* 左靠，如第42欄填T或S者，本欄統一以YYYMMDDNXYYYMM共14碼表示，其資料欄位定義/結構如下:前7碼YYYMMDD：契約簽訂日期；第8碼N:當日摽售批次序號，1-第一批出售案、2-第二批出售案；第9碼X:債權類別，B:現金卡、C:授信(不含現金卡)；第10~14碼YYYMM債權處理年月(債權交割日)；本欄位如無資料請填空白，勿填0。
  *
  * @param negNo 債權處理案號
	*/
  public void setNegNo(String negNo) {
    this.negNo = negNo;
  }

/**
	* 債權轉讓年月/債權轉讓後原債權機構買回年月<br>
	* a.債權轉讓年月
b.債權轉讓後原債權機構買回年月
左靠，a.如第42欄填T者，本欄請填報金融機構與資產管理公司約定之債權交割年月
b.如第42欄填X者，本欄請填報債權轉讓後原債權機構買回年月：以民國年YYYMM表示，本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getNegTransYM() {
    return this.negTransYM == null ? "" : this.negTransYM;
  }

/**
	* 債權轉讓年月/債權轉讓後原債權機構買回年月<br>
	* a.債權轉讓年月
b.債權轉讓後原債權機構買回年月
左靠，a.如第42欄填T者，本欄請填報金融機構與資產管理公司約定之債權交割年月
b.如第42欄填X者，本欄請填報債權轉讓後原債權機構買回年月：以民國年YYYMM表示，本欄位如無資料請填空白，勿填0。
  *
  * @param negTransYM 債權轉讓年月/債權轉讓後原債權機構買回年月
	*/
  public void setNegTransYM(String negTransYM) {
    this.negTransYM = negTransYM;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller443() {
    return this.filler443 == null ? "" : this.filler443;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler443 空白
	*/
  public void setFiller443(String filler443) {
    this.filler443 = filler443;
  }

/**
	* 擔保品組合型態<br>
	* 0:純信用                    1:單一擔保品(或保證)
2:多種擔保品含股票          3:多種擔保品不含股票
	* @return String
	*/
  public String getClType() {
    return this.clType == null ? "" : this.clType;
  }

/**
	* 擔保品組合型態<br>
	* 0:純信用                    1:單一擔保品(或保證)
2:多種擔保品含股票          3:多種擔保品不含股票
  *
  * @param clType 擔保品組合型態
	*/
  public void setClType(String clType) {
    this.clType = clType;
  }

/**
	* 擔保品(合計)鑑估值<br>
	* 右靠，單位:新台幣千元。如為多筆授信共用擔保品者，請將擔保品(合計)鑑估值歸入其中一筆授信之本欄位填報，其餘個筆授信此欄位則註記*，表示係與他筆授信共用擔保品
	* @return BigDecimal
	*/
  public BigDecimal getClEvaAmt() {
    return this.clEvaAmt;
  }

/**
	* 擔保品(合計)鑑估值<br>
	* 右靠，單位:新台幣千元。如為多筆授信共用擔保品者，請將擔保品(合計)鑑估值歸入其中一筆授信之本欄位填報，其餘個筆授信此欄位則註記*，表示係與他筆授信共用擔保品
  *
  * @param clEvaAmt 擔保品(合計)鑑估值
	*/
  public void setClEvaAmt(BigDecimal clEvaAmt) {
    this.clEvaAmt = clEvaAmt;
  }

/**
	* 擔保品類別<br>
	* 擔保品類別代號表請參照附件九(本修訂版將原擔保品類別:信用保證機構保證細分為5項，05:：中小企業信用保證基金保證，06:農業信用保證基金保證、07:華僑貸款信用保證基金保證，08:國際合作發展基金會信用保證，09:原住民族綜合發展基金信用保證)
	* @return String
	*/
  public String getClTypeCode() {
    return this.clTypeCode == null ? "" : this.clTypeCode;
  }

/**
	* 擔保品類別<br>
	* 擔保品類別代號表請參照附件九(本修訂版將原擔保品類別:信用保證機構保證細分為5項，05:：中小企業信用保證基金保證，06:農業信用保證基金保證、07:華僑貸款信用保證基金保證，08:國際合作發展基金會信用保證，09:原住民族綜合發展基金信用保證)
  *
  * @param clTypeCode 擔保品類別
	*/
  public void setClTypeCode(String clTypeCode) {
    this.clTypeCode = clTypeCode;
  }

/**
	* 國內或國際連貸<br>
	* A 國內 B 國際，如非屬聯貸案填空白，勿填0。
	* @return String
	*/
  public String getSyndKind() {
    return this.syndKind == null ? "" : this.syndKind;
  }

/**
	* 國內或國際連貸<br>
	* A 國內 B 國際，如非屬聯貸案填空白，勿填0。
  *
  * @param syndKind 國內或國際連貸
	*/
  public void setSyndKind(String syndKind) {
    this.syndKind = syndKind;
  }

/**
	* 聯貸合約訂定日期<br>
	* 以西元年月日YYYYMMDD表示，如非屬聯貸案填空白，勿填0。
	* @return String
	*/
  public String getSyndContractDate() {
    return this.syndContractDate == null ? "" : this.syndContractDate;
  }

/**
	* 聯貸合約訂定日期<br>
	* 以西元年月日YYYYMMDD表示，如非屬聯貸案填空白，勿填0。
  *
  * @param syndContractDate 聯貸合約訂定日期
	*/
  public void setSyndContractDate(String syndContractDate) {
    this.syndContractDate = syndContractDate;
  }

/**
	* 聯貸參貸比例<br>
	* 右靠左補0，填報聯貸案個別參貸比例，若參貸比列小於1%，請以小數點方式填報，例如0.8%請填00.8；非屬聯貸案填空白，勿填0；如該聯貸合約載明與其他授信戶共用此參貸比例時，僅需於其中一授信戶之本欄位填報參貸比例，其他授信戶之本欄位請填報*。
	* @return BigDecimal
	*/
  public BigDecimal getSyndRatio() {
    return this.syndRatio;
  }

/**
	* 聯貸參貸比例<br>
	* 右靠左補0，填報聯貸案個別參貸比例，若參貸比列小於1%，請以小數點方式填報，例如0.8%請填00.8；非屬聯貸案填空白，勿填0；如該聯貸合約載明與其他授信戶共用此參貸比例時，僅需於其中一授信戶之本欄位填報參貸比例，其他授信戶之本欄位請填報*。
  *
  * @param syndRatio 聯貸參貸比例
	*/
  public void setSyndRatio(BigDecimal syndRatio) {
    this.syndRatio = syndRatio;
  }

/**
	* 空白<br>
	* 本欄位原為 無追索權應收帳款承購業務買方國別，現改為空白
	* @return String
	*/
  public String getFiller51() {
    return this.filler51 == null ? "" : this.filler51;
  }

/**
	* 空白<br>
	* 本欄位原為 無追索權應收帳款承購業務買方國別，現改為空白
  *
  * @param filler51 空白
	*/
  public void setFiller51(String filler51) {
    this.filler51 = filler51;
  }

/**
	* 空白<br>
	* 本欄位原為 無追索權應收帳款承購業務買方行業別，現改為空白
	* @return String
	*/
  public String getFiller52() {
    return this.filler52 == null ? "" : this.filler52;
  }

/**
	* 空白<br>
	* 本欄位原為 無追索權應收帳款承購業務買方行業別，現改為空白
  *
  * @param filler52 空白
	*/
  public void setFiller52(String filler52) {
    this.filler52 = filler52;
  }

/**
	* 代放款註記<br>
	* 如為代放款(非銀行之自有資金且會計科目屬代放款者)請填Y，否則請填N。
	* @return String
	*/
  public String getPayablesFg() {
    return this.payablesFg == null ? "" : this.payablesFg;
  }

/**
	* 代放款註記<br>
	* 如為代放款(非銀行之自有資金且會計科目屬代放款者)請填Y，否則請填N。
  *
  * @param payablesFg 代放款註記
	*/
  public void setPayablesFg(String payablesFg) {
    this.payablesFg = payablesFg;
  }

/**
	* 債務協商註記<br>
	* 如本筆授信以參加銀行公會消金債務協商機制，且目前為有效停催案件者請填Y，否則請填N
	* @return String
	*/
  public String getNegFg() {
    return this.negFg == null ? "" : this.negFg;
  }

/**
	* 債務協商註記<br>
	* 如本筆授信以參加銀行公會消金債務協商機制，且目前為有效停催案件者請填Y，否則請填N
  *
  * @param negFg 債務協商註記
	*/
  public void setNegFg(String negFg) {
    this.negFg = negFg;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller533() {
    return this.filler533 == null ? "" : this.filler533;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler533 空白
	*/
  public void setFiller533(String filler533) {
    this.filler533 = filler533;
  }

/**
	* 共同債務人或債務關係人身份代號1<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaTypeCode1() {
    return this.guaTypeCode1 == null ? "" : this.guaTypeCode1;
  }

/**
	* 共同債務人或債務關係人身份代號1<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
  *
  * @param guaTypeCode1 共同債務人或債務關係人身份代號1
	*/
  public void setGuaTypeCode1(String guaTypeCode1) {
    this.guaTypeCode1 = guaTypeCode1;
  }

/**
	* 共同債務人或債務關係人身份統一編號1<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaId1() {
    return this.guaId1 == null ? "" : this.guaId1;
  }

/**
	* 共同債務人或債務關係人身份統一編號1<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
  *
  * @param guaId1 共同債務人或債務關係人身份統一編號1
	*/
  public void setGuaId1(String guaId1) {
    this.guaId1 = guaId1;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
	* @return String
	*/
  public String getGuaIdErr1() {
    return this.guaIdErr1 == null ? "" : this.guaIdErr1;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
  *
  * @param guaIdErr1 上欄IDN或BAN錯誤註記
	*/
  public void setGuaIdErr1(String guaIdErr1) {
    this.guaIdErr1 = guaIdErr1;
  }

/**
	* 與主債務人關係1<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaRelCode1() {
    return this.guaRelCode1 == null ? "" : this.guaRelCode1;
  }

/**
	* 與主債務人關係1<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
  *
  * @param guaRelCode1 與主債務人關係1
	*/
  public void setGuaRelCode1(String guaRelCode1) {
    this.guaRelCode1 = guaRelCode1;
  }

/**
	* 共同債務人或債務關係人身份代號2<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaTypeCode2() {
    return this.guaTypeCode2 == null ? "" : this.guaTypeCode2;
  }

/**
	* 共同債務人或債務關係人身份代號2<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
  *
  * @param guaTypeCode2 共同債務人或債務關係人身份代號2
	*/
  public void setGuaTypeCode2(String guaTypeCode2) {
    this.guaTypeCode2 = guaTypeCode2;
  }

/**
	* 共同債務人或債務關係人身份統一編號2<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaId2() {
    return this.guaId2 == null ? "" : this.guaId2;
  }

/**
	* 共同債務人或債務關係人身份統一編號2<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
  *
  * @param guaId2 共同債務人或債務關係人身份統一編號2
	*/
  public void setGuaId2(String guaId2) {
    this.guaId2 = guaId2;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
	* @return String
	*/
  public String getGuaIdErr2() {
    return this.guaIdErr2 == null ? "" : this.guaIdErr2;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
  *
  * @param guaIdErr2 上欄IDN或BAN錯誤註記
	*/
  public void setGuaIdErr2(String guaIdErr2) {
    this.guaIdErr2 = guaIdErr2;
  }

/**
	* 與主債務人關係2<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaRelCode2() {
    return this.guaRelCode2 == null ? "" : this.guaRelCode2;
  }

/**
	* 與主債務人關係2<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
  *
  * @param guaRelCode2 與主債務人關係2
	*/
  public void setGuaRelCode2(String guaRelCode2) {
    this.guaRelCode2 = guaRelCode2;
  }

/**
	* 共同債務人或債務關係人身份代號3<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaTypeCode3() {
    return this.guaTypeCode3 == null ? "" : this.guaTypeCode3;
  }

/**
	* 共同債務人或債務關係人身份代號3<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
  *
  * @param guaTypeCode3 共同債務人或債務關係人身份代號3
	*/
  public void setGuaTypeCode3(String guaTypeCode3) {
    this.guaTypeCode3 = guaTypeCode3;
  }

/**
	* 共同債務人或債務關係人身份統一編號3<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaId3() {
    return this.guaId3 == null ? "" : this.guaId3;
  }

/**
	* 共同債務人或債務關係人身份統一編號3<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
  *
  * @param guaId3 共同債務人或債務關係人身份統一編號3
	*/
  public void setGuaId3(String guaId3) {
    this.guaId3 = guaId3;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
	* @return String
	*/
  public String getGuaIdErr3() {
    return this.guaIdErr3 == null ? "" : this.guaIdErr3;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
  *
  * @param guaIdErr3 上欄IDN或BAN錯誤註記
	*/
  public void setGuaIdErr3(String guaIdErr3) {
    this.guaIdErr3 = guaIdErr3;
  }

/**
	* 與主債務人關係3<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaRelCode3() {
    return this.guaRelCode3 == null ? "" : this.guaRelCode3;
  }

/**
	* 與主債務人關係3<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
  *
  * @param guaRelCode3 與主債務人關係3
	*/
  public void setGuaRelCode3(String guaRelCode3) {
    this.guaRelCode3 = guaRelCode3;
  }

/**
	* 共同債務人或債務關係人身份代號4<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaTypeCode4() {
    return this.guaTypeCode4 == null ? "" : this.guaTypeCode4;
  }

/**
	* 共同債務人或債務關係人身份代號4<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
  *
  * @param guaTypeCode4 共同債務人或債務關係人身份代號4
	*/
  public void setGuaTypeCode4(String guaTypeCode4) {
    this.guaTypeCode4 = guaTypeCode4;
  }

/**
	* 共同債務人或債務關係人身份統一編號4<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaId4() {
    return this.guaId4 == null ? "" : this.guaId4;
  }

/**
	* 共同債務人或債務關係人身份統一編號4<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
  *
  * @param guaId4 共同債務人或債務關係人身份統一編號4
	*/
  public void setGuaId4(String guaId4) {
    this.guaId4 = guaId4;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
	* @return String
	*/
  public String getGuaIdErr4() {
    return this.guaIdErr4 == null ? "" : this.guaIdErr4;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
  *
  * @param guaIdErr4 上欄IDN或BAN錯誤註記
	*/
  public void setGuaIdErr4(String guaIdErr4) {
    this.guaIdErr4 = guaIdErr4;
  }

/**
	* 與主債務人關係4<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaRelCode4() {
    return this.guaRelCode4 == null ? "" : this.guaRelCode4;
  }

/**
	* 與主債務人關係4<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
  *
  * @param guaRelCode4 與主債務人關係4
	*/
  public void setGuaRelCode4(String guaRelCode4) {
    this.guaRelCode4 = guaRelCode4;
  }

/**
	* 共同債務人或債務關係人身份代號5<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaTypeCode5() {
    return this.guaTypeCode5 == null ? "" : this.guaTypeCode5;
  }

/**
	* 共同債務人或債務關係人身份代號5<br>
	* 共同債務人或債務關係人身份代號請參照附件十一；本欄位如無資料請填空白，勿填0。
  *
  * @param guaTypeCode5 共同債務人或債務關係人身份代號5
	*/
  public void setGuaTypeCode5(String guaTypeCode5) {
    this.guaTypeCode5 = guaTypeCode5;
  }

/**
	* 共同債務人或債務關係人身份統一編號5<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaId5() {
    return this.guaId5 == null ? "" : this.guaId5;
  }

/**
	* 共同債務人或債務關係人身份統一編號5<br>
	* 左靠，營利事業或身份證統一編號；本欄位如無資料請填空白，勿填0。
  *
  * @param guaId5 共同債務人或債務關係人身份統一編號5
	*/
  public void setGuaId5(String guaId5) {
    this.guaId5 = guaId5;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
	* @return String
	*/
  public String getGuaIdErr5() {
    return this.guaIdErr5 == null ? "" : this.guaIdErr5;
  }

/**
	* 上欄IDN或BAN錯誤註記<br>
	* 第55欄IDN/BAN經過邏輯檢查無誤者，本欄位空白，否則以*填報
  *
  * @param guaIdErr5 上欄IDN或BAN錯誤註記
	*/
  public void setGuaIdErr5(String guaIdErr5) {
    this.guaIdErr5 = guaIdErr5;
  }

/**
	* 與主債務人關係5<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
	* @return String
	*/
  public String getGuaRelCode5() {
    return this.guaRelCode5 == null ? "" : this.guaRelCode5;
  }

/**
	* 與主債務人關係5<br>
	* 請參照附件十二；本欄位如無資料請填空白，勿填0。
  *
  * @param guaRelCode5 與主債務人關係5
	*/
  public void setGuaRelCode5(String guaRelCode5) {
    this.guaRelCode5 = guaRelCode5;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller741() {
    return this.filler741 == null ? "" : this.filler741;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler741 空白
	*/
  public void setFiller741(String filler741) {
    this.filler741 = filler741;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller742() {
    return this.filler742 == null ? "" : this.filler742;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler742 空白
	*/
  public void setFiller742(String filler742) {
    this.filler742 = filler742;
  }

/**
	* 呆帳轉銷年月<br>
	* 以YYYMM(民國)表示，請填報該筆呆帳之轉銷年月；第13欄科目別填報B：呆帳者，本欄位為必要填報項目(且每月皆需報送)；本欄位如無資料請填空白，勿填0。
	* @return Integer
	*/
  public int getBadDebtDate() {
    return this.badDebtDate;
  }

/**
	* 呆帳轉銷年月<br>
	* 以YYYMM(民國)表示，請填報該筆呆帳之轉銷年月；第13欄科目別填報B：呆帳者，本欄位為必要填報項目(且每月皆需報送)；本欄位如無資料請填空白，勿填0。
  *
  * @param badDebtDate 呆帳轉銷年月
	*/
  public void setBadDebtDate(int badDebtDate) {
    this.badDebtDate = badDebtDate;
  }

/**
	* 聯貸主辦(管理)行註記<br>
	* 第48欄 國內或國外聯貸填報A及B者，本欄為必要填報項目：如為聯貸案單一主辦行請填Y(非單一主辦行請填X)，額度管理行請填L，如為共同主辦行兼額度管理行請填YL，單一主辦案填報Y或L，或共同主辦案本欄位填報YL者，需配合報送 聯貸合約各參貸機構參貸比例資料檔案格式；如為擔保品管理行請填C，填報C時須配合報送 「擔保品關連檔格式」及「擔保品茗係檔格式」；前述代號若有同時兼具之狀況時，請填報所有代號；若非屬前述主辦(管理)行請填X
	* @return String
	*/
  public String getSyndCode() {
    return this.syndCode == null ? "" : this.syndCode;
  }

/**
	* 聯貸主辦(管理)行註記<br>
	* 第48欄 國內或國外聯貸填報A及B者，本欄為必要填報項目：如為聯貸案單一主辦行請填Y(非單一主辦行請填X)，額度管理行請填L，如為共同主辦行兼額度管理行請填YL，單一主辦案填報Y或L，或共同主辦案本欄位填報YL者，需配合報送 聯貸合約各參貸機構參貸比例資料檔案格式；如為擔保品管理行請填C，填報C時須配合報送 「擔保品關連檔格式」及「擔保品茗係檔格式」；前述代號若有同時兼具之狀況時，請填報所有代號；若非屬前述主辦(管理)行請填X
  *
  * @param syndCode 聯貸主辦(管理)行註記
	*/
  public void setSyndCode(String syndCode) {
    this.syndCode = syndCode;
  }

/**
	* 破產宣告日(或法院裁定開始清算日)<br>
	* 以民國年月日YYYMMDD表示，授信戶經法院依破產法宣告破產者，本欄位請填報破產宣告日；授信戶經法院依「消費者債務清理條例」裁定開始清算程序者，本欄位請填報開始清算日(且每月皆需報送)；如無資料請填空白，勿填0。
	* @return Integer
	*/
  public int getBankruptDate() {
    return this.bankruptDate;
  }

/**
	* 破產宣告日(或法院裁定開始清算日)<br>
	* 以民國年月日YYYMMDD表示，授信戶經法院依破產法宣告破產者，本欄位請填報破產宣告日；授信戶經法院依「消費者債務清理條例」裁定開始清算程序者，本欄位請填報開始清算日(且每月皆需報送)；如無資料請填空白，勿填0。
  *
  * @param bankruptDate 破產宣告日(或法院裁定開始清算日)
	*/
  public void setBankruptDate(int bankruptDate) {
    this.bankruptDate = bankruptDate;
  }

/**
	* 建築貸款註記<br>
	* 若本筆貸款屬報送「央行消費與建築貸款統計表」中之建築貸款者請填'Y'（建築貸秋包括對建築業貸款、對其他企業建築貸款及對個人戶建築貸款）；非屬建築貸款請填'N'
	* @return String
	*/
  public String getBdLoanFg() {
    return this.bdLoanFg == null ? "" : this.bdLoanFg;
  }

/**
	* 建築貸款註記<br>
	* 若本筆貸款屬報送「央行消費與建築貸款統計表」中之建築貸款者請填'Y'（建築貸秋包括對建築業貸款、對其他企業建築貸款及對個人戶建築貸款）；非屬建築貸款請填'N'
  *
  * @param bdLoanFg 建築貸款註記
	*/
  public void setBdLoanFg(String bdLoanFg) {
    this.bdLoanFg = bdLoanFg;
  }

/**
	* 授信餘額列報1（千元）之原始金額（元）<br>
	* 右靠左補0，單位新臺幣元；
上開第29欄、第30欄、第31欄或第32欄之授信餘額列報為1（千元），即小於1,499元者，請將其原始金額以元為單位填報於本欄。本欄位如無資料請填'0'
	* @return Integer
	*/
  public int getSmallAmt() {
    return this.smallAmt;
  }

/**
	* 授信餘額列報1（千元）之原始金額（元）<br>
	* 右靠左補0，單位新臺幣元；
上開第29欄、第30欄、第31欄或第32欄之授信餘額列報為1（千元），即小於1,499元者，請將其原始金額以元為單位填報於本欄。本欄位如無資料請填'0'
  *
  * @param smallAmt 授信餘額列報1（千元）之原始金額（元）
	*/
  public void setSmallAmt(int smallAmt) {
    this.smallAmt = smallAmt;
  }

/**
	* 補充揭露案件註記－案件屬性<br>
	* 請填報該授信案件屬經主管機關函釋之爭議屬性：請填報該授信案件屬經主管機關函釋之爭議屬性：'A':遞延（預付）型消費性貸款；'B':客戶衍生性金融商品交易爭議案件；'C':協議分期償還放款依協議條件正常履約經報送為不良授信案件
	* @return String
	*/
  public String getExtraAttrCode() {
    return this.extraAttrCode == null ? "" : this.extraAttrCode;
  }

/**
	* 補充揭露案件註記－案件屬性<br>
	* 請填報該授信案件屬經主管機關函釋之爭議屬性：請填報該授信案件屬經主管機關函釋之爭議屬性：'A':遞延（預付）型消費性貸款；'B':客戶衍生性金融商品交易爭議案件；'C':協議分期償還放款依協議條件正常履約經報送為不良授信案件
  *
  * @param extraAttrCode 補充揭露案件註記－案件屬性
	*/
  public void setExtraAttrCode(String extraAttrCode) {
    this.extraAttrCode = extraAttrCode;
  }

/**
	* 補充揭露案件註記－案件情形<br>
	* 請填報該授信案性屬經主管機關函釋之爭議案件：'01':學承電腦倒閉，'02':威爾斯美語倒閉；
源自衍生性爭議案件者，請填報：'31':協商；'32':評議/調處；'33':仲裁；'34':訴訟；若同一授信有兩種以上情事者，請銀行自行判定以最主要/具強制力者為準。
74.8欄如填案件屬性'C'之案件，本欄請填空白。
	* @return String
	*/
  public String getExtraStatusCode() {
    return this.extraStatusCode == null ? "" : this.extraStatusCode;
  }

/**
	* 補充揭露案件註記－案件情形<br>
	* 請填報該授信案性屬經主管機關函釋之爭議案件：'01':學承電腦倒閉，'02':威爾斯美語倒閉；
源自衍生性爭議案件者，請填報：'31':協商；'32':評議/調處；'33':仲裁；'34':訴訟；若同一授信有兩種以上情事者，請銀行自行判定以最主要/具強制力者為準。
74.8欄如填案件屬性'C'之案件，本欄請填空白。
  *
  * @param extraStatusCode 補充揭露案件註記－案件情形
	*/
  public void setExtraStatusCode(String extraStatusCode) {
    this.extraStatusCode = extraStatusCode;
  }

/**
	* 空白<br>
	* 備用
	* @return String
	*/
  public String getFiller74A() {
    return this.filler74A == null ? "" : this.filler74A;
  }

/**
	* 空白<br>
	* 備用
  *
  * @param filler74A 空白
	*/
  public void setFiller74A(String filler74A) {
    this.filler74A = filler74A;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月；每月正常報送授信餘額月報資料及發函更正(已上線)授信資料時，本欄位皆需報送資料所屬年月
	* @return Integer
	*/
  public int getJcicDataYM() {
    return this.jcicDataYM;
  }

/**
	* 資料所屬年月<br>
	* 請填報本筆授信資料所屬年月；每月正常報送授信餘額月報資料及發函更正(已上線)授信資料時，本欄位皆需報送資料所屬年月
  *
  * @param jcicDataYM 資料所屬年月
	*/
  public void setJcicDataYM(int jcicDataYM) {
    this.jcicDataYM = jcicDataYM;
  }

/**
	* 資料結束註記<br>
	* 空值，出表時判斷 (Y 該筆授信記錄結束 N 該筆授信記錄尚未結束)
	* @return String
	*/
  public String getDataEnd() {
    return this.dataEnd == null ? "" : this.dataEnd;
  }

/**
	* 資料結束註記<br>
	* 空值，出表時判斷 (Y 該筆授信記錄結束 N 該筆授信記錄尚未結束)
  *
  * @param dataEnd 資料結束註記
	*/
  public void setDataEnd(String dataEnd) {
    this.dataEnd = dataEnd;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "JcicB201 [jcicB201Id=" + jcicB201Id
           + ", totalAmt=" + totalAmt + ", custId=" + custId + ", custIdErr=" + custIdErr + ", suvId=" + suvId + ", suvIdErr=" + suvIdErr
           + ", overseasId=" + overseasId + ", industryCode=" + industryCode + ", filler12=" + filler12 + ", acctCode=" + acctCode + ", subAcctCode=" + subAcctCode + ", origAcctCode=" + origAcctCode
           + ", consumeFg=" + consumeFg + ", finCode=" + finCode + ", projCode=" + projCode + ", nonCreditCode=" + nonCreditCode + ", usageCode=" + usageCode + ", approveRate=" + approveRate
           + ", drawdownDate=" + drawdownDate + ", maturityDate=" + maturityDate + ", currencyCode=" + currencyCode + ", drawdownAmt=" + drawdownAmt + ", drawdownAmtFx=" + drawdownAmtFx + ", recycleCode=" + recycleCode
           + ", irrevocableFlag=" + irrevocableFlag + ", facmNo=" + facmNo + ", unDelayBal=" + unDelayBal + ", unDelayBalFx=" + unDelayBalFx + ", delayBal=" + delayBal + ", delayBalFx=" + delayBalFx
           + ", delayPeriodCode=" + delayPeriodCode + ", repayCode=" + repayCode + ", payAmt=" + payAmt + ", principal=" + principal + ", interest=" + interest + ", fee=" + fee
           + ", firstDelayCode=" + firstDelayCode + ", secondDelayCode=" + secondDelayCode + ", BadDebtCode=" + BadDebtCode + ", negStatus=" + negStatus + ", negCreditor=" + negCreditor + ", negNo=" + negNo
           + ", negTransYM=" + negTransYM + ", filler443=" + filler443 + ", clType=" + clType + ", clEvaAmt=" + clEvaAmt + ", clTypeCode=" + clTypeCode + ", syndKind=" + syndKind
           + ", syndContractDate=" + syndContractDate + ", syndRatio=" + syndRatio + ", filler51=" + filler51 + ", filler52=" + filler52 + ", payablesFg=" + payablesFg + ", negFg=" + negFg
           + ", filler533=" + filler533 + ", guaTypeCode1=" + guaTypeCode1 + ", guaId1=" + guaId1 + ", guaIdErr1=" + guaIdErr1 + ", guaRelCode1=" + guaRelCode1 + ", guaTypeCode2=" + guaTypeCode2
           + ", guaId2=" + guaId2 + ", guaIdErr2=" + guaIdErr2 + ", guaRelCode2=" + guaRelCode2 + ", guaTypeCode3=" + guaTypeCode3 + ", guaId3=" + guaId3 + ", guaIdErr3=" + guaIdErr3
           + ", guaRelCode3=" + guaRelCode3 + ", guaTypeCode4=" + guaTypeCode4 + ", guaId4=" + guaId4 + ", guaIdErr4=" + guaIdErr4 + ", guaRelCode4=" + guaRelCode4 + ", guaTypeCode5=" + guaTypeCode5
           + ", guaId5=" + guaId5 + ", guaIdErr5=" + guaIdErr5 + ", guaRelCode5=" + guaRelCode5 + ", filler741=" + filler741 + ", filler742=" + filler742 + ", badDebtDate=" + badDebtDate
           + ", syndCode=" + syndCode + ", bankruptDate=" + bankruptDate + ", bdLoanFg=" + bdLoanFg + ", smallAmt=" + smallAmt + ", extraAttrCode=" + extraAttrCode + ", extraStatusCode=" + extraStatusCode
           + ", filler74A=" + filler74A + ", jcicDataYM=" + jcicDataYM + ", dataEnd=" + dataEnd + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
