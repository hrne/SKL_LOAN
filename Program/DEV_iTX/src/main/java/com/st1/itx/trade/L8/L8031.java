package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Log;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Log;
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Log;
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Log;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8031")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8031 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8031.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ570Service iJcicZ570Service;
	@Autowired
	public JcicZ570LogService iJcicZ570LogService;
	@Autowired
	public JcicZ571Service iJcicZ571Service;
	@Autowired
	public JcicZ571LogService iJcicZ571LogService;
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	@Autowired
	public JcicZ572LogService iJcicZ572LogService;
	@Autowired
	public JcicZ573Service iJcicZ573Service;
	@Autowired
	public JcicZ573LogService iJcicZ573LogService;
	@Autowired
	public JcicZ574Service iJcicZ574Service;
	@Autowired
	public JcicZ574LogService iJcicZ574LogService;
	@Autowired
	public JcicZ575Service iJcicZ575Service;
	@Autowired
	public JcicZ575LogService iJcicZ575LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8031 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");
		String iChainCd = titaVo.getParam("ChainCd");
		
		this.index = titaVo.getReturnIndex();
		this.limit = 40;

		switch(iChainCd) {
		case"L8332":
			Slice<JcicZ570Log> rJcicZ570Log = null;
			rJcicZ570Log = iJcicZ570LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ570 rJcicZ570 = new JcicZ570();
			rJcicZ570 = iJcicZ570Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ570 == null) {
			    throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ570.getTranKey().equals("A")&&rJcicZ570.getOutJcicTxtDate()==0) {
			    OccursList occursListA = new OccursList();
			    //專用
			    occursListA.putParam("OOTranKey", rJcicZ570.getTranKey());
			    occursListA.putParam("OOAdjudicateDate", rJcicZ570.getAdjudicateDate());
			    occursListA.putParam("OOBankCount", rJcicZ570.getBankCount());
			    occursListA.putParam("OOBank1", rJcicZ570.getBank1());
			    occursListA.putParam("OOBank2", rJcicZ570.getBank2());
			    occursListA.putParam("OOBank3", rJcicZ570.getBank3());
			    occursListA.putParam("OOBank4", rJcicZ570.getBank4());
			    occursListA.putParam("OOBank5", rJcicZ570.getBank5());
			    occursListA.putParam("OOBank6", rJcicZ570.getBank6());
			    occursListA.putParam("OOBank7", rJcicZ570.getBank7());
			    occursListA.putParam("OOBank8", rJcicZ570.getBank8());
			    occursListA.putParam("OOBank9", rJcicZ570.getBank9());
			    occursListA.putParam("OOBank10", rJcicZ570.getBank10());
			    occursListA.putParam("OOBank11", rJcicZ570.getBank11());
			    occursListA.putParam("OOBank12", rJcicZ570.getBank12());
			    occursListA.putParam("OOBank13", rJcicZ570.getBank13());
			    occursListA.putParam("OOBank14", rJcicZ570.getBank14());
			    occursListA.putParam("OOBank15", rJcicZ570.getBank15());
			    occursListA.putParam("OOBank16", rJcicZ570.getBank16());
			    occursListA.putParam("OOBank17", rJcicZ570.getBank17());
			    occursListA.putParam("OOBank18", rJcicZ570.getBank18());
			    occursListA.putParam("OOBank19", rJcicZ570.getBank19());
			    occursListA.putParam("OOBank20", rJcicZ570.getBank20());
			    occursListA.putParam("OOBank21", rJcicZ570.getBank21());
			    occursListA.putParam("OOBank22", rJcicZ570.getBank22());
			    occursListA.putParam("OOBank23", rJcicZ570.getBank23());
			    occursListA.putParam("OOBank24", rJcicZ570.getBank24());
			    occursListA.putParam("OOBank25", rJcicZ570.getBank25());
			    occursListA.putParam("OOBank26", rJcicZ570.getBank26());
			    occursListA.putParam("OOBank27", rJcicZ570.getBank27());
			    occursListA.putParam("OOBank28", rJcicZ570.getBank28());
			    occursListA.putParam("OOBank29", rJcicZ570.getBank29());
			    occursListA.putParam("OOBank30", rJcicZ570.getBank30());

			    //補齊     
			    occursListA.putParam("OOUnallotAmt", "");
			    occursListA.putParam("OOAllotAmt", "");
			    occursListA.putParam("OOOwnerAmt", "");
			    occursListA.putParam("OOOwnerYn", "");
			    occursListA.putParam("OOPayYn", "");
			    occursListA.putParam("OOModifyType", "");
			    occursListA.putParam("OOStartDate", "");
			    occursListA.putParam("OOOwnPercentage", "");
			    occursListA.putParam("OOCloseDate", "");
			    occursListA.putParam("OOCloseMark", "");
			    occursListA.putParam("OOPhoneNo", "");
			    occursListA.putParam("OOPayAmt", "0");
			    occursListA.putParam("OOTotalPayAmt", "0");
			    //固定
			    String taU = rJcicZ570.getLastUpdate().toString();
			    String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
			    uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
			    String uTime = taU.substring(11,19);
			    occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
			    occursListA.putParam("OOLastUpdateEmpNo", rJcicZ570.getLastUpdateEmpNo());
			    occursListA.putParam("OOOutJcicTxtDate", rJcicZ570.getOutJcicTxtDate());
			    this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ570Log == null) {
			    throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ570Log rrJcicZ570Log:rJcicZ570Log) {
			    OccursList occursList = new OccursList();
			    //專用
			    occursList.putParam("OOTranKey", rrJcicZ570Log.getTranKey());
			    occursList.putParam("OOAdjudicateDate", rJcicZ570.getAdjudicateDate());
			    occursList.putParam("OOBankCount", rJcicZ570.getBankCount());
			    occursList.putParam("OOBank1", rJcicZ570.getBank1());
			    occursList.putParam("OOBank2", rJcicZ570.getBank2());
			    occursList.putParam("OOBank3", rJcicZ570.getBank3());
			    occursList.putParam("OOBank4", rJcicZ570.getBank4());
			    occursList.putParam("OOBank5", rJcicZ570.getBank5());
			    occursList.putParam("OOBank6", rJcicZ570.getBank6());
			    occursList.putParam("OOBank7", rJcicZ570.getBank7());
			    occursList.putParam("OOBank8", rJcicZ570.getBank8());
			    occursList.putParam("OOBank9", rJcicZ570.getBank9());
			    occursList.putParam("OOBank10", rJcicZ570.getBank10());
			    occursList.putParam("OOBank11", rJcicZ570.getBank11());
			    occursList.putParam("OOBank12", rJcicZ570.getBank12());
			    occursList.putParam("OOBank13", rJcicZ570.getBank13());
			    occursList.putParam("OOBank14", rJcicZ570.getBank14());
			    occursList.putParam("OOBank15", rJcicZ570.getBank15());
			    occursList.putParam("OOBank16", rJcicZ570.getBank16());
			    occursList.putParam("OOBank17", rJcicZ570.getBank17());
			    occursList.putParam("OOBank18", rJcicZ570.getBank18());
			    occursList.putParam("OOBank19", rJcicZ570.getBank19());
			    occursList.putParam("OOBank20", rJcicZ570.getBank20());
			    occursList.putParam("OOBank21", rJcicZ570.getBank21());
			    occursList.putParam("OOBank22", rJcicZ570.getBank22());
			    occursList.putParam("OOBank23", rJcicZ570.getBank23());
			    occursList.putParam("OOBank24", rJcicZ570.getBank24());
			    occursList.putParam("OOBank25", rJcicZ570.getBank25());
			    occursList.putParam("OOBank26", rJcicZ570.getBank26());
			    occursList.putParam("OOBank27", rJcicZ570.getBank27());
			    occursList.putParam("OOBank28", rJcicZ570.getBank28());
			    occursList.putParam("OOBank29", rJcicZ570.getBank29());
			    occursList.putParam("OOBank30", rJcicZ570.getBank30());
			    //補齊
			    occursList.putParam("OOUnallotAmt", "");
			    occursList.putParam("OOAllotAmt", "");
			    occursList.putParam("OOOwnerAmt", "");
			    occursList.putParam("OOOwnerYn", "");
			    occursList.putParam("OOPayYn", "");
			    occursList.putParam("OOModifyType", "");
			    occursList.putParam("OOStartDate", "");
			    occursList.putParam("OOOwnPercentage", "");
			    occursList.putParam("OOCloseDate", "");
			    occursList.putParam("OOCloseMark", "");
			    occursList.putParam("OOPhoneNo", "");
			    occursList.putParam("OOPayAmt", "0");
			    occursList.putParam("OOTotalPayAmt", "0");
			    //固定
			    String taU = rrJcicZ570Log.getLastUpdate().toString();
			    String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
			    uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
			    String uTime = taU.substring(11,19);
			    occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
			    occursList.putParam("OOLastUpdateEmpNo", rrJcicZ570Log.getLastUpdateEmpNo());
			    occursList.putParam("OOOutJcicTxtDate", rrJcicZ570Log.getOutJcicTxtDate());
			    this.totaVo.addOccursList(occursList);
			}
			break;
		case"L8333":
			Slice<JcicZ571Log> rJcicZ571Log = null;
			rJcicZ571Log = iJcicZ571LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ571 rJcicZ571 = new JcicZ571();
			rJcicZ571 = iJcicZ571Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ571 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ571.getTranKey().equals("A")&&rJcicZ571.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				//專用
				occursListA.putParam("OOTranKey", rJcicZ571.getTranKey());
				occursListA.putParam("OOOwnerYn", rJcicZ571.getOwnerYn());
                occursListA.putParam("OOPayYn", rJcicZ571.getPayYn());
                occursListA.putParam("OOOwnerAmt", rJcicZ571.getOwnerAmt());
                occursListA.putParam("OOAllotAmt", rJcicZ571.getAllotAmt());
                occursListA.putParam("OOUnallotAmt", rJcicZ571.getUnallotAmt());
                //補齊     
                occursListA.putParam("OOAdjudicateDate", "");
                occursListA.putParam("OOBankCount", "");
                occursListA.putParam("OOBank1", "");
                occursListA.putParam("OOBank2", "");
                occursListA.putParam("OOBank3", "");
                occursListA.putParam("OOBank4", "");
                occursListA.putParam("OOBank5", "");
                occursListA.putParam("OOBank6", "");
                occursListA.putParam("OOBank7", "");
                occursListA.putParam("OOBank8", "");
                occursListA.putParam("OOBank9", "");
                occursListA.putParam("OOBank10", "");
                occursListA.putParam("OOBank11", "");
                occursListA.putParam("OOBank12", "");
                occursListA.putParam("OOBank13", "");
                occursListA.putParam("OOBank14", "");
                occursListA.putParam("OOBank15", "");
                occursListA.putParam("OOBank16", "");
                occursListA.putParam("OOBank17", "");
                occursListA.putParam("OOBank18", "");
                occursListA.putParam("OOBank19", "");
                occursListA.putParam("OOBank20", "");
                occursListA.putParam("OOBank21", "");
                occursListA.putParam("OOBank22", "");
                occursListA.putParam("OOBank23", "");
                occursListA.putParam("OOBank24", "");
                occursListA.putParam("OOBank25", "");
                occursListA.putParam("OOBank26", "");
                occursListA.putParam("OOBank27", "");
                occursListA.putParam("OOBank28", "");
                occursListA.putParam("OOBank29", "");
                occursListA.putParam("OOBank30", "");
                occursListA.putParam("OOModifyType", "");
                occursListA.putParam("OOStartDate", "");
				occursListA.putParam("OOOwnPercentage", "");
				occursListA.putParam("OOCloseDate", "");
				occursListA.putParam("OOCloseMark", "");
				occursListA.putParam("OOPhoneNo", "");
				occursListA.putParam("OOPayAmt", "0");
				occursListA.putParam("OOTotalPayAmt", "0");
				//固定
				String taU = rJcicZ571.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ571.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ571.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ571Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ571Log rrJcicZ571Log:rJcicZ571Log) {
				OccursList occursList = new OccursList();
				//專用
				occursList.putParam("OOTranKey", rrJcicZ571Log.getTranKey());
                occursList.putParam("OOOwnerYn", rrJcicZ571Log.getOwnerYn());
                occursList.putParam("OOPayYn", rrJcicZ571Log.getPayYn());
                occursList.putParam("OOOwnerAmt", rrJcicZ571Log.getOwnerAmt());
                occursList.putParam("OOAllotAmt", rrJcicZ571Log.getAllotAmt());
                occursList.putParam("OOUnallotAmt", rrJcicZ571Log.getUnallotAmt());			
				//補齊
                occursList.putParam("OOAdjudicateDate", "");
                occursList.putParam("OOBankCount", "");
                occursList.putParam("OOBank1", "");
                occursList.putParam("OOBank2", "");
                occursList.putParam("OOBank3", "");
                occursList.putParam("OOBank4", "");
                occursList.putParam("OOBank5", "");
                occursList.putParam("OOBank6", "");
                occursList.putParam("OOBank7", "");
                occursList.putParam("OOBank8", "");
                occursList.putParam("OOBank9", "");
                occursList.putParam("OOBank10", "");
                occursList.putParam("OOBank11", "");
                occursList.putParam("OOBank12", "");
                occursList.putParam("OOBank13", "");
                occursList.putParam("OOBank14", "");
                occursList.putParam("OOBank15", "");
                occursList.putParam("OOBank16", "");
                occursList.putParam("OOBank17", "");
                occursList.putParam("OOBank18", "");
                occursList.putParam("OOBank19", "");
                occursList.putParam("OOBank20", "");
                occursList.putParam("OOBank21", "");
                occursList.putParam("OOBank22", "");
                occursList.putParam("OOBank23", "");
                occursList.putParam("OOBank24", "");
                occursList.putParam("OOBank25", "");
                occursList.putParam("OOBank26", "");
                occursList.putParam("OOBank27", "");
                occursList.putParam("OOBank28", "");
                occursList.putParam("OOBank29", "");
                occursList.putParam("OOBank30", "");
                occursList.putParam("OOModifyType", "");
                occursList.putParam("OOStartDate", "");
				occursList.putParam("OOOwnPercentage", "");
				occursList.putParam("OOCloseDate", "");
				occursList.putParam("OOCloseMark", "");
				occursList.putParam("OOPhoneNo", "");
				occursList.putParam("OOPayAmt", "0");
				occursList.putParam("OOTotalPayAmt", "0");
				//固定
				String taU = rrJcicZ571Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", rrJcicZ571Log.getLastUpdateEmpNo());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ571Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}
			break;
		case"L8334":
			Slice<JcicZ572Log> rJcicZ572Log = null;
			rJcicZ572Log = iJcicZ572LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ572 rJcicZ572 = new JcicZ572();
			rJcicZ572 = iJcicZ572Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ572 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ572.getTranKey().equals("A")&&rJcicZ572.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				//專用
				occursListA.putParam("OOTranKey", rJcicZ572.getTranKey());
				occursListA.putParam("OOStartDate", rJcicZ572.getStartDate());
				occursListA.putParam("OOAllotAmt", rJcicZ572.getAllotAmt());
				occursListA.putParam("OOOwnPercentage", rJcicZ572.getOwnPercentage());
				//補齊
			    occursListA.putParam("OOAdjudicateDate", "");
			    occursListA.putParam("OOBankCount", "");
			    occursListA.putParam("OOBank1", "");
			    occursListA.putParam("OOBank2", "");
			    occursListA.putParam("OOBank3", "");
			    occursListA.putParam("OOBank4", "");
			    occursListA.putParam("OOBank5", "");
			    occursListA.putParam("OOBank6", "");
			    occursListA.putParam("OOBank7", "");
			    occursListA.putParam("OOBank8", "");
			    occursListA.putParam("OOBank9", "");
			    occursListA.putParam("OOBank10", "");
			    occursListA.putParam("OOBank11", "");
			    occursListA.putParam("OOBank12", "");
			    occursListA.putParam("OOBank13", "");
			    occursListA.putParam("OOBank14", "");
			    occursListA.putParam("OOBank15", "");
			    occursListA.putParam("OOBank16", "");
			    occursListA.putParam("OOBank17", "");
			    occursListA.putParam("OOBank18", "");
			    occursListA.putParam("OOBank19", "");
			    occursListA.putParam("OOBank20", "");
			    occursListA.putParam("OOBank21", "");
			    occursListA.putParam("OOBank22", "");
			    occursListA.putParam("OOBank23", "");
			    occursListA.putParam("OOBank24", "");
			    occursListA.putParam("OOBank25", "");
			    occursListA.putParam("OOBank26", "");
			    occursListA.putParam("OOBank27", "");
			    occursListA.putParam("OOBank28", "");
			    occursListA.putParam("OOBank29", "");
			    occursListA.putParam("OOBank30", "");
				occursListA.putParam("OOPayAmt", "");
				occursListA.putParam("OOTotalPayAmt", "");
				occursListA.putParam("OOCloseDate", "");
				occursListA.putParam("OOCloseMark", "");
				occursListA.putParam("OOPhoneNo", "");
				occursListA.putParam("OOModifyType", "");
				occursListA.putParam("OOOwnerYn", "");
                occursListA.putParam("OOPayYn", "");
                occursListA.putParam("OOOwnerAmt", "");
                occursListA.putParam("OOUnallotAmt", "");
				//固定
				String taU = rJcicZ572.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ572.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ572.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ572Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ572Log rrJcicZ572Log:rJcicZ572Log) {
				OccursList occursList = new OccursList();
				//專用
				occursList.putParam("OOTranKey", rrJcicZ572Log.getTranKey());			
				occursList.putParam("OOStartDate", rrJcicZ572Log.getStartDate());
				occursList.putParam("OOAllotAmt", rrJcicZ572Log.getAllotAmt());
				occursList.putParam("OOOwnPercentage", rrJcicZ572Log.getOwnPercentage());
				//補齊
			    occursList.putParam("OOAdjudicateDate", "");
			    occursList.putParam("OOBankCount", "");
			    occursList.putParam("OOBank1", "");
			    occursList.putParam("OOBank2", "");
			    occursList.putParam("OOBank3", "");
			    occursList.putParam("OOBank4", "");
			    occursList.putParam("OOBank5", "");
			    occursList.putParam("OOBank6", "");
			    occursList.putParam("OOBank7", "");
			    occursList.putParam("OOBank8", "");
			    occursList.putParam("OOBank9", "");
			    occursList.putParam("OOBank10", "");
			    occursList.putParam("OOBank11", "");
			    occursList.putParam("OOBank12", "");
			    occursList.putParam("OOBank13", "");
			    occursList.putParam("OOBank14", "");
			    occursList.putParam("OOBank15", "");
			    occursList.putParam("OOBank16", "");
			    occursList.putParam("OOBank17", "");
			    occursList.putParam("OOBank18", "");
			    occursList.putParam("OOBank19", "");
			    occursList.putParam("OOBank20", "");
			    occursList.putParam("OOBank21", "");
			    occursList.putParam("OOBank22", "");
			    occursList.putParam("OOBank23", "");
			    occursList.putParam("OOBank24", "");
			    occursList.putParam("OOBank25", "");
			    occursList.putParam("OOBank26", "");
			    occursList.putParam("OOBank27", "");
			    occursList.putParam("OOBank28", "");
			    occursList.putParam("OOBank29", "");
			    occursList.putParam("OOBank30", "");
				occursList.putParam("OOPayAmt", "" );
				occursList.putParam("OOTotalPayAmt", "");
				occursList.putParam("OOModifyType", "");
				occursList.putParam("OOCloseDate", "");
				occursList.putParam("OOCloseMark", "");
				occursList.putParam("OOPhoneNo", "");
				occursList.putParam("OOOwnerYn", "");
                occursList.putParam("OOPayYn", "");
                occursList.putParam("OOOwnerAmt", "");
                occursList.putParam("OOUnallotAmt", "");
				//固定
				String taU = rrJcicZ572Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", rrJcicZ572Log.getLastUpdateEmpNo());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ572Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}
			break;
		case"L8335":
			Slice<JcicZ573Log> rJcicZ573Log = null;
			rJcicZ573Log = iJcicZ573LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ573 rJcicZ573 = new JcicZ573();
			rJcicZ573 = iJcicZ573Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ573 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ573.getTranKey().equals("A")&&rJcicZ573.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				//專用
				occursListA.putParam("OOTranKey", rJcicZ573.getTranKey());
				occursListA.putParam("OOPayAmt", rJcicZ573.getPayAmt());
				occursListA.putParam("OOTotalPayAmt", rJcicZ573.getTotalPayAmt());
				//補齊
			    occursListA.putParam("OOAdjudicateDate", "");
			    occursListA.putParam("OOBankCount", "");
			    occursListA.putParam("OOBank1", "");
			    occursListA.putParam("OOBank2", "");
			    occursListA.putParam("OOBank3", "");
			    occursListA.putParam("OOBank4", "");
			    occursListA.putParam("OOBank5", "");
			    occursListA.putParam("OOBank6", "");
			    occursListA.putParam("OOBank7", "");
			    occursListA.putParam("OOBank8", "");
			    occursListA.putParam("OOBank9", "");
			    occursListA.putParam("OOBank10", "");
			    occursListA.putParam("OOBank11", "");
			    occursListA.putParam("OOBank12", "");
			    occursListA.putParam("OOBank13", "");
			    occursListA.putParam("OOBank14", "");
			    occursListA.putParam("OOBank15", "");
			    occursListA.putParam("OOBank16", "");
			    occursListA.putParam("OOBank17", "");
			    occursListA.putParam("OOBank18", "");
			    occursListA.putParam("OOBank19", "");
			    occursListA.putParam("OOBank20", "");
			    occursListA.putParam("OOBank21", "");
			    occursListA.putParam("OOBank22", "");
			    occursListA.putParam("OOBank23", "");
			    occursListA.putParam("OOBank24", "");
			    occursListA.putParam("OOBank25", "");
			    occursListA.putParam("OOBank26", "");
			    occursListA.putParam("OOBank27", "");
			    occursListA.putParam("OOBank28", "");
			    occursListA.putParam("OOBank29", "");
			    occursListA.putParam("OOBank30", "");
				occursListA.putParam("OOStartDate", "");
				occursListA.putParam("OOAllotAmt", "");
				occursListA.putParam("OOOwnPercentage", "");
				occursListA.putParam("OOCloseDate", "");
				occursListA.putParam("OOCloseMark", "");
				occursListA.putParam("OOPhoneNo", "");
				occursListA.putParam("OOModifyType", "");
				occursListA.putParam("OOOwnerYn", "");
                occursListA.putParam("OOPayYn", "");
                occursListA.putParam("OOOwnerAmt", "");
                occursListA.putParam("OOUnallotAmt", "");
				//固定
				String taU = rJcicZ573.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ573.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ573.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ573Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ573Log rrJcicZ573Log:rJcicZ573Log) {
				OccursList occursList = new OccursList();
				//專用
				occursList.putParam("OOTranKey", rrJcicZ573Log.getTranKey());			
				occursList.putParam("OOPayAmt", rrJcicZ573Log.getPayAmt());
				occursList.putParam("OOTotalPayAmt", rrJcicZ573Log.getTotalPayAmt());
				//補齊
			    occursList.putParam("OOAdjudicateDate", "");
			    occursList.putParam("OOBankCount", "");
			    occursList.putParam("OOBank1", "");
			    occursList.putParam("OOBank2", "");
			    occursList.putParam("OOBank3", "");
			    occursList.putParam("OOBank4", "");
			    occursList.putParam("OOBank5", "");
			    occursList.putParam("OOBank6", "");
			    occursList.putParam("OOBank7", "");
			    occursList.putParam("OOBank8", "");
			    occursList.putParam("OOBank9", "");
			    occursList.putParam("OOBank10", "");
			    occursList.putParam("OOBank11", "");
			    occursList.putParam("OOBank12", "");
			    occursList.putParam("OOBank13", "");
			    occursList.putParam("OOBank14", "");
			    occursList.putParam("OOBank15", "");
			    occursList.putParam("OOBank16", "");
			    occursList.putParam("OOBank17", "");
			    occursList.putParam("OOBank18", "");
			    occursList.putParam("OOBank19", "");
			    occursList.putParam("OOBank20", "");
			    occursList.putParam("OOBank21", "");
			    occursList.putParam("OOBank22", "");
			    occursList.putParam("OOBank23", "");
			    occursList.putParam("OOBank24", "");
			    occursList.putParam("OOBank25", "");
			    occursList.putParam("OOBank26", "");
			    occursList.putParam("OOBank27", "");
			    occursList.putParam("OOBank28", "");
			    occursList.putParam("OOBank29", "");
			    occursList.putParam("OOBank30", "");
				occursList.putParam("OOStartDate", "");
				occursList.putParam("OOAllotAmt", "");
				occursList.putParam("OOOwnPercentage", "");
				occursList.putParam("OOModifyType", "");
				occursList.putParam("OOCloseDate", "");
				occursList.putParam("OOCloseMark", "");
				occursList.putParam("OOPhoneNo", "");
				occursList.putParam("OOOwnerYn", "");
                occursList.putParam("OOPayYn", "");
                occursList.putParam("OOOwnerAmt", "");
                occursList.putParam("OOUnallotAmt", "");
				//固定
				String taU = rrJcicZ573Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", rrJcicZ573Log.getLastUpdateEmpNo());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ573Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}
			break;
		case"L8336":
			Slice<JcicZ574Log> rJcicZ574Log = null;
			rJcicZ574Log = iJcicZ574LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ574 rJcicZ574 = new JcicZ574();
			rJcicZ574 = iJcicZ574Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ574 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ574.getTranKey().equals("A")&&rJcicZ574.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				//專用
				occursListA.putParam("OOTranKey", rJcicZ574.getTranKey());
				occursListA.putParam("OOCloseDate", rJcicZ574.getCloseDate());
				occursListA.putParam("OOCloseMark", rJcicZ574.getCloseMark());
				occursListA.putParam("OOPhoneNo", rJcicZ574.getPhoneNo());
				//補齊
			    occursListA.putParam("OOAdjudicateDate", "");
			    occursListA.putParam("OOBankCount", "");
			    occursListA.putParam("OOBank1", "");
			    occursListA.putParam("OOBank2", "");
			    occursListA.putParam("OOBank3", "");
			    occursListA.putParam("OOBank4", "");
			    occursListA.putParam("OOBank5", "");
			    occursListA.putParam("OOBank6", "");
			    occursListA.putParam("OOBank7", "");
			    occursListA.putParam("OOBank8", "");
			    occursListA.putParam("OOBank9", "");
			    occursListA.putParam("OOBank10", "");
			    occursListA.putParam("OOBank11", "");
			    occursListA.putParam("OOBank12", "");
			    occursListA.putParam("OOBank13", "");
			    occursListA.putParam("OOBank14", "");
			    occursListA.putParam("OOBank15", "");
			    occursListA.putParam("OOBank16", "");
			    occursListA.putParam("OOBank17", "");
			    occursListA.putParam("OOBank18", "");
			    occursListA.putParam("OOBank19", "");
			    occursListA.putParam("OOBank20", "");
			    occursListA.putParam("OOBank21", "");
			    occursListA.putParam("OOBank22", "");
			    occursListA.putParam("OOBank23", "");
			    occursListA.putParam("OOBank24", "");
			    occursListA.putParam("OOBank25", "");
			    occursListA.putParam("OOBank26", "");
			    occursListA.putParam("OOBank27", "");
			    occursListA.putParam("OOBank28", "");
			    occursListA.putParam("OOBank29", "");
			    occursListA.putParam("OOBank30", "");
				occursListA.putParam("OOStartDate", "");
				occursListA.putParam("OOAllotAmt", "");
				occursListA.putParam("OOOwnPercentage", "");
				occursListA.putParam("OOModifyType", "");
				occursListA.putParam("OOPayAmt", "0");
				occursListA.putParam("OOTotalPayAmt", "0");
				occursListA.putParam("OOOwnerYn", "");
                occursListA.putParam("OOPayYn", "");
                occursListA.putParam("OOOwnerAmt", "");
                occursListA.putParam("OOUnallotAmt", "");
				//固定
				String taU = rJcicZ574.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ574.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ574.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ574Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ574Log rrJcicZ574Log:rJcicZ574Log) {
				OccursList occursList = new OccursList();
				//專用
				occursList.putParam("OOTranKey", rrJcicZ574Log.getTranKey());
				occursList.putParam("OOCloseDate", rrJcicZ574Log.getCloseDate());
				occursList.putParam("OOCloseMark", rrJcicZ574Log.getCloseMark());
				occursList.putParam("OOPhoneNo", rrJcicZ574Log.getPhoneNo());
				//補齊
				occursList.putParam("OOAdjudicateDate", "");
				occursList.putParam("OOBankCount", "");
				occursList.putParam("OOBank1", "");
				occursList.putParam("OOBank2", "");
				occursList.putParam("OOBank3", "");
				occursList.putParam("OOBank4", "");
				occursList.putParam("OOBank5", "");
				occursList.putParam("OOBank6", "");
				occursList.putParam("OOBank7", "");
				occursList.putParam("OOBank8", "");
				occursList.putParam("OOBank9", "");
				occursList.putParam("OOBank10", "");
				occursList.putParam("OOBank11", "");
				occursList.putParam("OOBank12", "");
				occursList.putParam("OOBank13", "");
				occursList.putParam("OOBank14", "");
				occursList.putParam("OOBank15", "");
				occursList.putParam("OOBank16", "");
				occursList.putParam("OOBank17", "");
				occursList.putParam("OOBank18", "");
				occursList.putParam("OOBank19", "");
				occursList.putParam("OOBank20", "");
				occursList.putParam("OOBank21", "");
				occursList.putParam("OOBank22", "");
				occursList.putParam("OOBank23", "");
			    occursList.putParam("OOBank24", "");
				occursList.putParam("OOBank25", "");
				occursList.putParam("OOBank26", "");
				occursList.putParam("OOBank27", "");
				occursList.putParam("OOBank28", "");
				occursList.putParam("OOBank29", "");
				occursList.putParam("OOBank30", "");
				occursList.putParam("OOStartDate", "");
				occursList.putParam("OOOwnPercentage", "");
				occursList.putParam("OOModifyType", "");
				occursList.putParam("OOPayAmt", "0");
				occursList.putParam("OOTotalPayAmt", "0");
				occursList.putParam("OOOwnerYn", "");
                occursList.putParam("OOPayYn", "");
                occursList.putParam("OOAllotAmt", "");
                occursList.putParam("OOOwnerAmt", "");
                occursList.putParam("OOUnallotAmt", "");
				//固定
				String taU = rrJcicZ574Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", rrJcicZ574Log.getLastUpdateEmpNo());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ574Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}
			break;
		case"L8337":
			Slice<JcicZ575Log> rJcicZ575Log = null;
			rJcicZ575Log = iJcicZ575LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ575 rJcicZ575 = new JcicZ575();
			rJcicZ575 = iJcicZ575Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ575 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ575.getTranKey().equals("A")&&rJcicZ575.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				//專用
				occursListA.putParam("OOTranKey", rJcicZ575.getTranKey());
				occursListA.putParam("OOModifyType", rJcicZ575.getModifyType());
				//補齊
			    occursListA.putParam("OOAdjudicateDate", "");
			    occursListA.putParam("OOBankCount", "");
			    occursListA.putParam("OOBank1", "");
			    occursListA.putParam("OOBank2", "");
			    occursListA.putParam("OOBank3", "");
			    occursListA.putParam("OOBank4", "");
			    occursListA.putParam("OOBank5", "");
			    occursListA.putParam("OOBank6", "");
			    occursListA.putParam("OOBank7", "");
			    occursListA.putParam("OOBank8", "");
			    occursListA.putParam("OOBank9", "");
			    occursListA.putParam("OOBank10", "");
			    occursListA.putParam("OOBank11", "");
			    occursListA.putParam("OOBank12", "");
			    occursListA.putParam("OOBank13", "");
			    occursListA.putParam("OOBank14", "");
			    occursListA.putParam("OOBank15", "");
			    occursListA.putParam("OOBank16", "");
			    occursListA.putParam("OOBank17", "");
			    occursListA.putParam("OOBank18", "");
			    occursListA.putParam("OOBank19", "");
			    occursListA.putParam("OOBank20", "");
			    occursListA.putParam("OOBank21", "");
			    occursListA.putParam("OOBank22", "");
			    occursListA.putParam("OOBank23", "");
			    occursListA.putParam("OOBank24", "");
			    occursListA.putParam("OOBank25", "");
			    occursListA.putParam("OOBank26", "");
			    occursListA.putParam("OOBank27", "");
			    occursListA.putParam("OOBank28", "");
			    occursListA.putParam("OOBank29", "");
			    occursListA.putParam("OOBank30", "");
				occursListA.putParam("OOStartDate", "");
				occursListA.putParam("OOAllotAmt", "");
				occursListA.putParam("OOOwnPercentage", "");
				occursListA.putParam("OOCloseDate", "");
				occursListA.putParam("OOCloseMark", "");
				occursListA.putParam("OOPhoneNo", "");
				occursListA.putParam("OOPayAmt", "0");
				occursListA.putParam("OOTotalPayAmt", "0");
				occursListA.putParam("OOOwnerYn", "");
                occursListA.putParam("OOPayYn", "");
                occursListA.putParam("OOOwnerAmt", "");
                occursListA.putParam("OOUnallotAmt", "");
				//固定
				String taU = rJcicZ575.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ575.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ575.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ575Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ575Log rrJcicZ575Log:rJcicZ575Log) {
				OccursList occursList = new OccursList();
				//專用
				occursList.putParam("OOTranKey", rrJcicZ575Log.getTranKey());
				occursList.putParam("OOModifyType", rrJcicZ575Log.getModifyType());
				//補齊
				occursList.putParam("OOAdjudicateDate", "");
				occursList.putParam("OOBankCount", "");
				occursList.putParam("OOBank1", "");
				occursList.putParam("OOBank2", "");
				occursList.putParam("OOBank3", "");
				occursList.putParam("OOBank4", "");
				occursList.putParam("OOBank5", "");
				occursList.putParam("OOBank6", "");
				occursList.putParam("OOBank7", "");
				occursList.putParam("OOBank8", "");
				occursList.putParam("OOBank9", "");
				occursList.putParam("OOBank10", "");
				occursList.putParam("OOBank11", "");
				occursList.putParam("OOBank12", "");
				occursList.putParam("OOBank13", "");
				occursList.putParam("OOBank14", "");
				occursList.putParam("OOBank15", "");
				occursList.putParam("OOBank16", "");
				occursList.putParam("OOBank17", "");
				occursList.putParam("OOBank18", "");
				occursList.putParam("OOBank19", "");
				occursList.putParam("OOBank20", "");
				occursList.putParam("OOBank21", "");
				occursList.putParam("OOBank22", "");
				occursList.putParam("OOBank23", "");
			    occursList.putParam("OOBank24", "");
				occursList.putParam("OOBank25", "");
				occursList.putParam("OOBank26", "");
				occursList.putParam("OOBank27", "");
				occursList.putParam("OOBank28", "");
				occursList.putParam("OOBank29", "");
				occursList.putParam("OOBank30", "");
				occursList.putParam("OOStartDate", "");
				occursList.putParam("OOAllotAmt", "");
				occursList.putParam("OOOwnPercentage", "");
				occursList.putParam("OOCloseDate", "");
				occursList.putParam("OOCloseMark", "");
				occursList.putParam("OOPhoneNo", "");
				occursList.putParam("OOPayAmt", "0");
				occursList.putParam("OOTotalPayAmt", "0");
				occursList.putParam("OOOwnerYn", "");
                occursList.putParam("OOPayYn", "");
                occursList.putParam("OOOwnerAmt", "");
                occursList.putParam("OOUnallotAmt", "");
				//固定
				String taU = rrJcicZ575Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", rrJcicZ575Log.getLastUpdateEmpNo());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ575Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}
			break;
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}