package com.st1.ifx.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.st1.ifx.domain.Sbctl;
import com.st1.ifx.domain.Txcd;
import com.st1.ifx.domain.TxcdGrbrfg;
import com.st1.ifx.etc.Pair;
import com.st1.ifx.etc.SomeHelper;
import com.st1.ifx.etc.Tuples;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.SbctlService;
import com.st1.ifx.service.TxcdService;
// 可能可以把SbctlService相關的移除,因已沒有再使用

@Component
@Scope("prototype")
public class TranListBuilder {
	private static final Logger logger = LoggerFactory.getLogger(TranListBuilder.class);

	private List<TranItem> trans = new ArrayList<TranItem>();

	String wk_osbrno;

	int wk_tdrelcd;
	String wk_tlrno;
	int wk_torelcd;
	int wk_type;

	int wk_drelcd;
	String wk_dabrno;
	String wk_drbrno;
	int wk_orelcd;
	String wk_orbrno;

	private TxcdService txcdService;

	@Autowired
	public void setTxcdService(TxcdService txcdService) {
		logger.info("setTxcdService");
		this.txcdService = txcdService;
	}

	private SbctlService sbctlService;

	@Autowired
	public void setSbctlService(SbctlService sbctlService) {
		logger.info("setSbctlService");
		this.sbctlService = sbctlService;
	}

	private Attach attachNoUsed;
	private C1200 c12NoUsed;
	private SignonTota signonTota;
	TxcdGrbrfg txcdGr;
	String brno;
	String tlrno;

	public void preload() {
		txcdService.findNotTypeOf(1);
	}

	private void parseLoginText(String attachText, String c12Text) {
		attachNoUsed = SimpleMapper.parse(attachText, Attach.class);
		c12NoUsed = SimpleMapper.parse(c12Text, C1200.class);
		signonTota = new SignonTota();
		signonTota.attach = attachNoUsed;
		signonTota.c12 = c12NoUsed;
	}

	@SuppressWarnings("rawtypes")
	private void parseLoginMap(HashMap map) {
		signonTota = new SignonTota();
		C1200 c12 = new C1200();
		c12.txgrp = (String) map.get("TXGRP");
		c12.level = Integer.parseInt((String) map.get("LEVEL"));
		c12.fxlvl = Integer.parseInt((String) map.get("FXLVL"));
		c12.dapknd = (String) map.get("DAPKND");
		c12.oapknd = (String) map.get("OAPKND");
		Attach attach = new Attach();
		attach.fxlvl = c12.fxlvl;
		signonTota.attach = attach;
		signonTota.c12 = c12;
	}

	// 建立全行共用選單 先不管權限
	// @Cacheable("txcdone")
	private Tuples.Tuple3<String, String, Map<String, TranItem>> buildOnce() {
		logger.info("build menu for av8d");
		// parseLoginText(attachText, c12Text);

		// find txcd where type <> 1
		List<Txcd> txcdList = txcdService.findNotTypeOf(1);
		logger.info("total matched txcd records:" + txcdList.size());
		for (Txcd txcd : txcdList) { // 每一個txcd
			trans.add(makeTranItem(txcd, false)); // 產生交易定義, 添加入List
		}
		Map<String, TranItem> enabledTranMap = new HashMap<String, TranItem>();
		// 讓選單上每個交易都可執行
		for (TranItem tranItem : trans) {
			tranItem.enabled = true;
			if (tranItem.type == 2) {
				if (tranItem.enabled) {
					enabledTranMap.put(tranItem.txcd, tranItem);
				}
			}
		}

		String html = MenuGenerator.toHtml(MenuGenerator.convertToMenu(trans));
		String json = new Gson().toJson(enabledTranMap);

		return new Tuples.Tuple3<String, String, Map<String, TranItem>>(html, json, enabledTranMap);

	}

	public String getMenuHtml() {
		return this.buildOnce().getT1();

	}

	public String getMenuJson() {
		return this.buildOnce().getT2();
	}

	public Map<String, TranItem> getTranMap() {
		return this.buildOnce().getT3();
	}

	// @Cacheable("menutxcd") //潘 20180410
	public List<String> buildMenu2(String menuName, String brno, String tlrno, HashMap c12Map, String attachText, String c12Text) {
		this.brno = brno;
		this.tlrno = tlrno;// .substring(1);
		logger.info("buildMenu2 c12Text:" + c12Text);

		logger.info("build menu for {},{}", brno, tlrno);
		logger.info("shorter tlrno to:{}", this.tlrno);

		// parseLoginText(attachText, c12Text);
		parseLoginMap(c12Map);
		// reset local variables
		reset_wk();
		// find txcd where type <> 1
		List<Txcd> txcdList = txcdService.findNotTypeOf(1, menuName);

		logger.info("total matched txcd records:" + txcdList.size());
		List<String> result = new ArrayList<String>();
		try {
			for (Txcd txcd : txcdList) { // 每一個txcd
				if (txcd.getTxdfg() == 1)
					trans.add(makeTranItem(txcd, true)); // 產生交易定義, 添加入List
			}
			logger.info("after trans.add.");
			Map<String, TranItem> tranMap = MenuGenerator.createEnabledTranMap(trans);
			logger.info("tranMap.");
			result.add(itemKeys);
			for (TranItem ti : trans) {
				if (!tranMap.containsKey(ti.txcd)) {
					ti.enabled = false;
				}
				result.add(shortenTranItem(ti));
			}
			logger.info("result....");
		} catch (Exception e) {
			logger.error("ERROR:" + e.getMessage());
		}
		return result;

	}

	@CacheEvict(value = { "menutxcd", "txcdone" }, allEntries = true)
	public void evict() {
		logger.info("evict code menutxcd");

	}

	String itemKeys = "type,sbtype,enabled,txcd,txnm,dbucd,hodecd,chopcd,obucd,passcd,secno,tlrfg,drelcd,orelcd,txdfg";

	private String shortenTranItem(TranItem item) {
		String fmd = "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s";
		return String.format(fmd, item.type, item.sbtyp, item.isEnabled() ? "1" : "0", item.txcd, item.txnm, item.dbucd, item.hodecd, item.chopcd, item.obucd, item.passcd, item.secno, item.tlrfg,
				item.drelcd, item.orelcd, item.txdfg);
	}

	// 舊版 設定個人化選單
	// @Cacheable("txcd")
	@SuppressWarnings("rawtypes")
	public Pair<String, Map<String, TranItem>> build(String brno, String tlrno, HashMap c12Map, String attachText, String c12Text) {
		this.brno = brno;
		this.tlrno = tlrno;// .substring(1);

		logger.info("build menu for {},{}", brno, tlrno);
		logger.info("shorter tlrno to:{}", this.tlrno);

		// parseLoginText(attachText, c12Text);
		parseLoginMap(c12Map);
		// reset local variables
		reset_wk();
		// find txcd where type <> 1
		List<Txcd> txcdList = txcdService.findNotTypeOf(1);
		logger.info("total matched txcd records:" + txcdList.size());
		for (Txcd txcd : txcdList) { // 每一個txcd
			trans.add(makeTranItem(txcd, false)); // 產生交易定義, 添加入List
		}
		return Pair.of(MenuGenerator.toHtml(MenuGenerator.convertToMenu(trans)), MenuGenerator.createEnabledTranMap(trans));

	}

	private TranItem makeTranItem(Txcd txcd, boolean root) { // 柯 新增第二個參數 只有ROOT MENU時要做
		TranItem tranItem = new TranItem(txcd);

		// 柯 新增 start 只有ROOT MENU時要做
		if (root) {
//			logger.info("makeTranItem root!");
//			logger.info("txcd.getTxcd():" + txcd.getTxcd());
			char word = txcd.getTxcd().charAt(1); // 取得輸txcd的第二碼
//			logger.info("one word:" + word);
			// logger.info("word:" +word);
			int number = word; // 將取得的字元轉換成Unicode碼
			int newnumber = 0;
			newnumber = (number - 65); // 不+ 1 從0開始
			// logger.info("number:" +number);
			// logger.info("newnumber:" +newnumber);
			// logger.info("oapknd num:" + oapknd.charAt(newnumber));
			// logger.info("dapknd num:" + dapknd.charAt(newnumber));
//			logger.info("newnumber:" + newnumber);

			// 暫時點掉
			// if (oapknd.charAt(newnumber) == dapknd.charAt(newnumber) &&
			// dapknd.charAt(newnumber) == '0') {
			// tranItem.enabled = false;
			// logger.info("tranItem.enabled false:" + word);
			// }

//			logger.info("after newnumber");
		}

		return tranItem; // 暫時都不處理
	}

	private void reset_wk() {
		wk_drelcd = wk_orelcd = 0;
		wk_tdrelcd = wk_torelcd = 0;
		wk_dabrno = wk_orbrno = null;
	}

	private void dump_wk() {
		logger.info("wk_drelcd:" + wk_drelcd);
		logger.info("wk_orelcd:" + wk_orelcd);
		logger.info("wk_tdrelcd:" + wk_tdrelcd);
		logger.info("wk_torelcd:" + wk_torelcd);
	}

	private void mergeSbctl(TranItem tranItem, int brset, String sbtyp) {
		logger.info("{} brset: {}", tranItem.txcd, brset);
		// **交易業務,
		switch (brset) {
		case 0: // **按分行類別設定
			// IF DB_TXCD_BRSET = 0
			// MOVE 0 TO WK-DABRNO,WK-DRBRNO,WK-ORBRNO
			// MOVE DB-TXCD-BRFG (TOM-ATTACH-FXLVL) TO WK-DRELCD
			// MOVE DB-TXCD-OCHOP(TOM-ATTACH-FXLVL) TO WK-ORELCD
			// **各分行個別設定
			// ELSE IF DB_TXCD-BRSET = 1
			wk_drelcd = txcdGr.getBrfg();
			wk_orelcd = txcdGr.getOchop();
			logger.info("wk_drelcd change to:" + wk_drelcd);
			logger.info("wk_orelcd change to:" + wk_orelcd);
			break;
		case 1: // **各分行個別設定
			// **副程式 讀取分行業務權限
			// PERFORM FNSBCTL-RTN
			fetchSbctlByBranch(sbtyp);
			break;
		case 2:// **各子業務自訂
				// MOVE 0 TO WK-DRELCD,WK-DABRNO,WK-DRBRNO,WK-ORELCD,WK-ORBRNO
			reset_wk();
			break;
		default:
			break;
		}

		// **副程式 讀取櫃員業務權限
		// FNSBCTL2-RTN.
		fetchSbctlByTlr(sbtyp);

		// MOVE2-RTM.
		// MOVE WK-DRELCD TO $DRELCD.
		// MOVE WK-DABRNO TO $DABRNO.
		// MOVE WK-DRBRNO TO $DRBRNO.
		// MOVE WK-ORELCD TO $ORELCD.
		// MOVE WK-ORBRNO TO $ORBRNO.
		tranItem.drelcd = wk_drelcd;
		tranItem.dabrno = wk_dabrno;
		tranItem.drbrno = wk_drbrno;
		tranItem.orelcd = wk_orelcd;
		tranItem.orbrno = wk_orbrno;
		// **省空間下列不需要
		// MOVE 0 TO $DBUCD.
		// MOVE 0 TO $HODECD.
		// MOVE 0 TO $CHOPCD.
		// MOVE 0 TO $OBUCD
		tranItem.dbucd = tranItem.hodecd = tranItem.chopcd = tranItem.obucd = 0;
	}

	// 副程式 讀取分行業務權限
	// FNSBCTL-RTN.
	private void fetchSbctlByBranch(String sbtyp /* from txcd */) {
		// SLEECT * FROM SBCTL
		// WHERE DB_SBCTL_TYPE = 0
		// AND DB_SBCTL_BRNO = 分行代碼
		// AND DB_SBCTL_TLRNO = 0000
		// AND DB_SBCTL_SBTYP = DB_TXCD_SBTYP.
		//
		// IF 讀不到
		// MOVE 0 TO WK-DRELCD
		// WK-DABRNO
		// WK-DRBRNO
		// WK-ORELCD
		// WK-ORBRNO
		// ELSE
		// MOVE DB-SBCTL-DRELCD TO WK-DRELCD
		// MOVE DB-SBCTL-DABRNO TO WK-DABRNO
		// MOVE DB-SBCTL-DRBRNO TO WK-DRBRNO
		// MOVE DB-SBCTL-ORELCD TO WK-ORELCD
		// MOVE DB-SBCTL-ORBRNO TO WK-ORBRNO.

		// already reset_wk() in caller
		Sbctl sbctl = sbctlService.findById(0, brno, "  ", sbtyp);
		if (sbctl != null) {
			wk_drelcd = sbctl.getDrelcd();
			wk_dabrno = sbctl.getDabrno();
			wk_drbrno = sbctl.getDrbrno();
			wk_orelcd = sbctl.getOrelcd();
			wk_orbrno = sbctl.getOrbrno();
		} else {
			logger.info("sbctl not founD");
		}
	}

	// 副程式 讀取櫃員業務權限
	// FNSBCTL2-RTN.
	private void fetchSbctlByTlr(String sbtyp /* from txcd */) {
		// **櫃員無櫃員群組
		// IF TOM-C1200-TXGRP = 0
		// MOVE 櫃員代碼 TO WK-TLRNO
		// MOVE 1 TO WK-TYPE
		// ELSE
		// **櫃員有櫃員群組
		// MOVE TOM-C1200-TXGRP TO WK-TLRNO
		// MOVE 2 TO WK-TYPE.

		if ("00".equals(signonTota.c12.txgrp)) {
			wk_tlrno = tlrno;
			wk_type = 1;
		} else {
			wk_tlrno = signonTota.c12.txgrp;
			wk_type = 2;
		}
		logger.info("finding sbctl, type:" + wk_type + ", brno:" + brno + ",tlrno:" + wk_tlrno + ",sbtyp:" + sbtyp);
		Sbctl sbctl = sbctlService.findById(wk_type, brno, wk_tlrno, sbtyp);

		// wk_tdrelcd = wk_torelcd = 0; //應該在上一層就已重設
		if (sbctl != null) {
			logger.info(FilterUtils.escape("sbctl found:" + sbctl.toString()));

			wk_tdrelcd = sbctl.getDrelcd();
			wk_torelcd = sbctl.getOrelcd();
			logger.info("wk_tdrelcd change to {}", wk_tdrelcd);
			logger.info("wk_torelcd change to {}", wk_torelcd);
		} else {
			logger.info("sbctl not found ");
		}
	}

	// MOVE-RTN, MOVE2-RTN
	private void mergeC12(Txcd txcd, TranItem tranItem) {

		// MOVE DB-TXCD-BRFG(TOM-ATTACH-FXLVL) TO $DBUCD.
		// MOVE DB-TXCD-SECNO TO $SECNO.
		// MOVE DB-TXCD-TLRFG(TOM-C1200-LEVEL) TO $TLRFG.
		// IF IF DB-TXCD-TLRFG(TOM-C1200-LEVEL) < $DBUCD
		// MOVE DB-TXCD-TLRFG(TOM-C1200-LEVEL) TO $DBUCD.
		int c12fg = txcd.getTlrfgAtLevel(signonTota.c12.level);

		tranItem.secno = txcd.getSecno();
		tranItem.tlrfg = c12fg;
		tranItem.dbucd = Math.min(txcdGr.getBrfg(), c12fg);
		logger.info("Brfg: {} tlrfg: {} ==> dbucd: {}", txcdGr.getBrfg(), tranItem.tlrfg, tranItem.dbucd);
		// MOVE DB-TXCD-HCODE TO $HODECD.
		tranItem.hodecd = txcd.getHcode();

		// MOVE DB-TXCD-CHOP TO $CHOPCD.
		tranItem.chopcd = txcdGr.getChop();

		// MOVE DB-TXCD-PASS TO $PASSCD.
		tranItem.passcd = txcd.getPass();

		// MOVE DB-TXCD-OCHOP TO $OBUCD .
		// IF IF DB-TXCD-TLRFG(TOM-C1200-LEVEL) < $OBUCD
		// MOVE DB-TXCD-TLRFG(TOM-C1200-LEVEL) TO $OBUCD.
		tranItem.obucd = Math.min(txcdGr.getOchop(), c12fg);
		logger.info("Ochop: {} tlrfg: {} ==> obucd: {}", txcdGr.getOchop(), tranItem.tlrfg, tranItem.obucd);

		// **櫃員業務權限
		// IF WK-TDRELCD < $DBUCD
		// MOVE WK-TRELCD TO $DBUCD.
		tranItem.dbucd = Math.min(wk_tdrelcd, tranItem.dbucd);
		logger.info("wk_tdrelcd: {} tranItem.dbucd: {} ==> dbucd: {}", wk_tdrelcd, tranItem.dbucd, tranItem.dbucd);

		// IF WK-TORELCD < $OBUCD
		// MOVE WK-TORELCD TO $OBUCD.
		tranItem.obucd = Math.min(wk_torelcd, tranItem.obucd);
		logger.info("wk_torelcd: {} tranItem.obucd: {} ==> obucd: {}", wk_torelcd, tranItem.obucd, tranItem.obucd);
		// **分行業務權限
		// IF WK-DRELCD = 0
		// MOVE 0 TO $DBUCD
		// ELSE IF WK-DRELCD < $DBUCD
		// AND WK-DABRNO = 0
		// MOVE WK-DRELCD TO $DBUCD.
		logger.info("now dbucd: {}, obucd: {}", tranItem.dbucd, tranItem.obucd);
		if (0 == wk_drelcd) {
			tranItem.dbucd = 0;
			logger.info("dbucd change to 0, because wk_drelcd=0");
		} else if (wk_drelcd < tranItem.dbucd && SomeHelper.isNullOrEmpty(wk_dabrno)) {
			tranItem.dbucd = wk_drelcd;
			logger.info("dbucd change to 0, because wk_dabrno=null");
		}

		// IF WK-ORELCD = 0
		// MOVE 0 TO $OBUCD
		// ELSE IF WK-DRELCD < $DBUCD
		// AND WK-DABRNO = 0
		// MOVE WK-DRELCD TO $DBUCD.
		if (0 == wk_orelcd) {
			tranItem.obucd = 0;
			logger.info("obucd change to 0, because wk_orelcd=0");
		} else if (wk_drelcd < tranItem.obucd && SomeHelper.isNullOrEmpty(wk_dabrno)) {
			tranItem.dbucd = wk_drelcd;
			logger.info("obucd change to {}, because wk_dabrno=0 and wk_drelcd({}) is less than obucd", tranItem.obucd, wk_drelcd);
		}

		// **若要省空間增加
		// MOVE DB-TXCD-PASS TO $PASSCD.
		tranItem.passcd = txcd.getPass();

		// **下列可不執行,執行交易時,再以子業務之參數再代入執行下列程式
		// MOVE WK-DRELCD TO $DRELCD.
		tranItem.orelcd = wk_drelcd;
		// MOVE WK-DABRNO TO $DABRNO.
		tranItem.dabrno = wk_dabrno;
		// MOVE WK-DRBRNO TO $DRBRNO.
		tranItem.drbrno = wk_drbrno;
		// MOVE WK-ORELCD TO $ORELCD.
		tranItem.orelcd = wk_orelcd;
		// MOVE WK-ORBRNO TO $ORBRNO.
		tranItem.orbrno = wk_orbrno;

		// IF $PASSCD = 0
		// AND WK-DRELCD > 1
		// MOVE 1 TO $DRELCD
		// MOVE WK-DABRNO TO $DRBRNO.
		// IF $PASSCD = 0
		// AND WK-ORELCD > 1
		// MOVE 1 TO $DRELCD.
		if (0 == tranItem.passcd) {
			if (wk_drelcd > 1) {
				tranItem.drelcd = 1;
				tranItem.drbrno = wk_dabrno;
			}
			if (wk_orelcd > 1) {
				tranItem.drelcd = 1;
			}
		}

		logger.info("finally dbucd: {}, obucd: {}", tranItem.dbucd, tranItem.obucd);
	}

}
