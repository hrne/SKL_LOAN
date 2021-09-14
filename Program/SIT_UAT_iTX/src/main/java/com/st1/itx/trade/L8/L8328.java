package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.UUID;
import java.math.BigDecimal;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ446;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Id;
import com.st1.itx.db.domain.JcicZ448Log;
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ448LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.service.JcicZ454Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;



@Service("L8328")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8328 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ442Service sJcicZ442Service;
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ448Service sJcicZ448Service;
	@Autowired
	public JcicZ448LogService sJcicZ448LogService;
	@Autowired
	public JcicZ454Service sJcicZ454Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8328 ");
		this.totaVo.init(titaVo);
			
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		int iSignPrin = Integer.valueOf(titaVo.getParam("SignPrin"));
		int iSignOther = Integer.valueOf(titaVo.getParam("SignOther"));
		BigDecimal iOwnPercentage = new BigDecimal(titaVo.getParam("OwnPercentage"));
		int iAcQuitAmt = Integer.valueOf(titaVo.getParam("AcQuitAmt"));
		int ixApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"))+19110000;
		String iKey = "";
		//JcicZ448
		JcicZ448 iJcicZ448 = new JcicZ448();
		JcicZ448Id iJcicZ448Id = new JcicZ448Id();
		iJcicZ448Id.setApplyDate(iApplyDate);
		iJcicZ448Id.setCustId(iCustId);
		iJcicZ448Id.setSubmitKey(iSubmitKey);
		iJcicZ448Id.setCourtCode(iCourtCode);
		iJcicZ448Id.setMaxMainCode(iMaxMainCode);
		JcicZ448 chJcicZ448 = new JcicZ448();
		//檢核項目(D-56)
		//同一key值之「應回報債權金融機構」皆已回報「'442':回報無擔保債權金額資料」、「'443':回報有擔保債權金額資料」檔案資料(交易代碼為X不需檢核)
		//二start
		if (iTranKey.equals("X")) {
		}else {
			Slice<JcicZ442> xJcicZ442 = sJcicZ442Service.custIdEq(iCustId, this.index, this.limit, titaVo);
			if (xJcicZ442 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for(JcicZ442 xoJcicZ442:xJcicZ442) {
				String iSubmitKey442 = xoJcicZ442.getSubmitKey();
				String iCustId442 = xoJcicZ442.getCustId();
				if(iCustId == iCustId442) {
					if(iSubmitKey != iSubmitKey442) {
						throw new LogicException(titaVo, "E0005", "「應回報債權金融機構」未回報「'442':回報無擔保債權金額資料」"); 
					}
				}
			}
			Slice<JcicZ443> xJcicZ443 = sJcicZ443Service.custIdEq(iCustId, this.index, this.limit, titaVo);
			if (xJcicZ443 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for(JcicZ443 xoJcicZ443:xJcicZ443) {
				String iSubmitKey443 = xoJcicZ443.getSubmitKey();
				String iCustId443 = xoJcicZ443.getCustId();
				if(iCustId == iCustId443) {
					if(iSubmitKey != iSubmitKey443) {
						throw new LogicException(titaVo, "E0005", "「應回報債權金融機構」未回報「'443':回報有擔保債權金額資料」"); 
					}
				}
			}
		}
		//二end
		//「簽約金額-本金」需等於該債權金融機構報送之「'442'回報無擔保債權金額資料」之17(信用放款本金)+21(現金卡本金)+25(信用卡本金)+29(保證債權本金)欄位加總(交易代碼為X不需檢核)「」
		//三start
		if (iTranKey.equals("X")) {
		}else {
			Slice<JcicZ442> xJcicZ442 = sJcicZ442Service.custIdEq(iCustId, this.index, this.limit, titaVo);
			if (xJcicZ442 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for(JcicZ442 xoJcicZ442:xJcicZ442) {
				int iReceExpPrin442 = xoJcicZ442.getReceExpPrin();
				int iCashCardPrin442 = xoJcicZ442.getCashCardPrin();
				int iCreditCardPrin442 = xoJcicZ442.getCreditCardPrin();
				int iGuarObliPrin442 = xoJcicZ442.getGuarObliPrin();
				String iCustId442 = xoJcicZ442.getCustId();
				String iSubmitKey442 = xoJcicZ442.getSubmitKey();
				if(iCustId == iCustId442 && iSubmitKey442 == iSubmitKey) {
					if((iReceExpPrin442+iCashCardPrin442+iCreditCardPrin442+iGuarObliPrin442) != iSignPrin) {
						throw new LogicException(titaVo, "E0005","「簽約金額-本金」需等於該債權金融機構報送之「'442'回報無擔保債權金額資料」之17(信用放款本金)+21(現金卡本金)+25(信用卡本金)+29(保證債權本金)欄位加總");
					}
				}
			}
		}
		//三end
		//各債權金融機構「債權比例」加總需為100.00%
		//四start OwnPercentage 需提問嘉榮
		
		//四end
		//任一債權金融機構報送同一key值'454'單獨受償檔案資料後，本檔案資料不得異動、刪除或補件
		//五start
		if(iTranKey.equals("A")) {
		}else {
		Slice<JcicZ454> xJcicZ454 = sJcicZ454Service.custIdEq(iCustId, this.index, this.limit, titaVo);
		if (xJcicZ454 != null) {
			throw new LogicException(titaVo, "E0005","任一債權金融機構報送同一key值'454'單獨受償檔案資料後，本檔案資料不得異動、刪除或補件");
			}
		}
		//五end
		//同一key值報送'446'檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料
		//六start 
		Slice<JcicZ446> xJcicZ446 = sJcicZ446Service.otherEq(iSubmitKey,iCustId,ixApplyDate,iCourtCode, this.index, this.limit, titaVo);
		if (xJcicZ446 != null) {
			throw new LogicException(titaVo, "E0005","同一key值報送'446'檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料");
		}
		//六end
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ448
			chJcicZ448 = sJcicZ448Service.findById(iJcicZ448Id, titaVo);
			if (chJcicZ448 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ448.setJcicZ448Id(iJcicZ448Id);
			iJcicZ448.setTranKey(iTranKey);
			iJcicZ448.setSignPrin(iSignPrin);
			iJcicZ448.setSignOther(iSignOther);
			iJcicZ448.setOwnPercentage(iOwnPercentage);
			iJcicZ448.setAcQuitAmt(iAcQuitAmt);
			iJcicZ448.setUkey(iKey);
			try {
				sJcicZ448Service.insert(iJcicZ448, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ448 = sJcicZ448Service.ukeyFirst(iKey, titaVo);
			JcicZ448 uJcicZ448 = new JcicZ448();
			uJcicZ448 = sJcicZ448Service.holdById(iJcicZ448.getJcicZ448Id(), titaVo);
			if (uJcicZ448 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ448.setSignPrin(iSignPrin);
			uJcicZ448.setSignOther(iSignOther);
			uJcicZ448.setOwnPercentage(iOwnPercentage);
			uJcicZ448.setAcQuitAmt(iAcQuitAmt);
			uJcicZ448.setTranKey(iTranKey);
			uJcicZ448.setOutJcicTxtDate(0);
			JcicZ448 oldJcicZ448 = (JcicZ448) iDataLog.clone(uJcicZ448);
			try {
				sJcicZ448Service.update(uJcicZ448, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ448, uJcicZ448);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ448 = sJcicZ448Service.findById(iJcicZ448Id);
			if (iJcicZ448 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ448Log> dJcicLogZ448 = null;
			dJcicLogZ448 = sJcicZ448LogService.ukeyEq(iJcicZ448.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ448 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ448Service.delete(iJcicZ448, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ448Log iJcicZ448Log = dJcicLogZ448.getContent().get(0);			
				iJcicZ448.setSignPrin(iJcicZ448Log.getSignPrin());
				iJcicZ448.setSignOther(iJcicZ448Log.getSignOther());
				iJcicZ448.setOwnPercentage(iJcicZ448Log.getOwnPercentage());
				iJcicZ448.setAcQuitAmt(iJcicZ448Log.getAcQuitAmt());
				iJcicZ448.setTranKey(iJcicZ448Log.getTranKey());
				iJcicZ448.setOutJcicTxtDate(iJcicZ448Log.getOutJcicTxtDate());
				try {
					sJcicZ448Service.update(iJcicZ448, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}