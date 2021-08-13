package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;

@Service("L5R12")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R12 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R12.class);
	@Autowired
	public NegCom sNegCom;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		
		this.info("active L5R12 ");
		this.totaVo.init(titaVo);
		String strBankCode = titaVo.getParam("RimBankCode");//銀行代碼
		this.info("Run L5R12 strBankCode=["+strBankCode+"]");
		checkBankCode(strBankCode,titaVo);
		if(strBankCode!=null && strBankCode.length()!=0) {
			String BankCodeItem[]=sNegCom.FindNegFinAcc(strBankCode,titaVo);
			if(BankCodeItem!=null && BankCodeItem.length!=0) {
				this.totaVo.putParam("L5r12BankItem", BankCodeItem[0]);
				this.totaVo.putParam("L5r12BranchItem", BankCodeItem[1]);
			}
			
		}else {
			this.totaVo.putParam("L5r12BankItem", strBankCode);
			this.totaVo.putParam("L5r12BranchItem", strBankCode);
			//throw new LogicException(titaVo, "E0001", "銀行代碼未輸入"); // 查詢資料不存在
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public void checkBankCode(String bankCode,TitaVo titaVo) throws LogicException {
		if(bankCode!=null && bankCode.length()!=0) {
			for(int i=0;i<bankCode.length();i++) {
				char thisChar=bankCode.charAt(i);
				if (thisChar >= 48 && thisChar <= 57 ) {     //輸入字元0~9
					//System.out.println("此字元是數字");
				} else if (thisChar >= 65 && thisChar <= 90) {     //輸入大寫英文字母 
//					System.out.println("此字元是大寫英文字母");
				} else if (thisChar >= 97 && thisChar <= 122) {     //輸入小寫英文字母
//					System.out.println("此字元是小寫英文字母");
				} else {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "債權機構代號["+bankCode+"]應由阿拉伯數字與英文組成");
				}
			}
		}
	}
}