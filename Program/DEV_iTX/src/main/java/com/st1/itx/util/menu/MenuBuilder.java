package com.st1.itx.util.menu;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.LC900ServiceImpl;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.log.SysLogger;

@Component("menuBuilder")
@Scope("prototype")
//@CacheConfig
public class MenuBuilder extends SysLogger {

	@Autowired
	private LC900ServiceImpl sLC900ServiceImpl;

	@Autowired
	private CdCodeService cdCodeService;

	@Transactional(readOnly = true)
	@Cacheable("rootMenu")
	public List<String> buildRootMenu() {
		Slice<CdCode> cdCodeLi = cdCodeService.defCodeLikeAndCodeLike("Menu%", "L%", 0, Integer.MAX_VALUE);
		List<String> res = new ArrayList<String>();
		res.add("type,sbtype,enabled,txcd,txnm,dbucd,hodecd,chopcd,obucd,passcd,secno,tlrfg,drelcd,orelcd,txdfg");
		if (cdCodeLi != null)
			for (CdCode cd : cdCodeLi.getContent())
				res.add("0," + FormatUtil.padX(cd.getCode(), 2) + ",0," + FormatUtil.padX(cd.getCode(), 5) + "," + cd.getItem().trim() + ",0,0,0,0,0,0,0,0,0,1");

		return res;
	}

	@Transactional(readOnly = true)
	@Cacheable("menu")
	public List<String> buildMenu(String iAuthNo) {
		// TXCD 0L1001202112202L11 00 顧客明細資料查詢 1
		List<String> res = new ArrayList<String>();
		res.add("type,sbtype,enabled,txcd,txnm,dbucd,hodecd,chopcd,obucd,passcd,secno,tlrfg,drelcd,orelcd,txdfg");
		try {
			List<Map<String, String>> dList = sLC900ServiceImpl.findAuthNo(iAuthNo);
			if (dList != null)
				for (Map<String, String> dVo : dList) {
					String s = "";
					if (dVo.get("F1").trim().length() == 5)
						s += "2,";
					else
						s += "0,";
					if (dVo.get("F1").trim().length() == 5)
						s += (dVo.get("F0").trim() + ",1,");
					else
						s += (dVo.get("F0").trim() + ",0,");

					s += (dVo.get("F1").trim() + "," + dVo.get("F2").trim() + ",0,0,0,0,0,0,0,0,0,1");
					res.add(s);
				}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return null;
		}
		return res;
	}

	@Transactional(readOnly = true)
	@Cacheable("autoCp")
	public List<String> buildAutoCP(String iAuthNo, String txcd) {
		List<String> res = new ArrayList<String>();
		try {
			List<Map<String, String>> dList = sLC900ServiceImpl.findAuthNo(iAuthNo, txcd);
			if (dList != null)
				for (Map<String, String> dVo : dList) {
					String s = "";
					if (dVo.get("F1").trim().length() == 5) {
						s += dVo.get("F1").trim() + " " + dVo.get("F2").trim();
						res.add(s);
					}
				}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return null;
		}
		return res;
	}
	
	@CacheEvict(value = { "rootMenu", "menu", "autoCp" }, allEntries = true)
	public void evict() {
		this.mustInfo("evict menuBuilder");
	}
}
