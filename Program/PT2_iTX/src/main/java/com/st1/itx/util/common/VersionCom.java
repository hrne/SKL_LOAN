package com.st1.itx.util.common;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Component
@Scope("prototype")
public class VersionCom extends TradeBuffer {

	@Value("${appVersion}")
	private String appVersion;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("VersionCom run ... ");
		return null;
	}

	public String getAppVersion() {
		return appVersion;
	}
}
