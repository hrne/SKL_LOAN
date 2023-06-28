package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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
		String taipeiTimestamp = "";
		try {
			SimpleDateFormat utcFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			Date utcDate = utcFormat.parse(appVersion);

			SimpleDateFormat taipeiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			taipeiFormat.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

			taipeiTimestamp = taipeiFormat.format(utcDate);
			this.info("Taipei Timestamp: " + taipeiTimestamp);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("VersionCom getAppVersion Exception = " + e.getMessage());
			return "";
		}
		return taipeiTimestamp;
	}
}
