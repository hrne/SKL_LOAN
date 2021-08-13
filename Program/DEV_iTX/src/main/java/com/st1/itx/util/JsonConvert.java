package com.st1.itx.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.format.HostFormatter;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.log.SysLogger;

@Component("jsonConvert")
@Scope("prototype")
public class JsonConvert extends SysLogger {

	@Autowired
	private HostFormatter hostFormatter;

	public String orgTitaString;

	public TitaVo orgTitaObj;

	private boolean status = true;

	public TitaVo serializationToObject(String msgJson) throws JsonParseException, JsonMappingException, IOException {
		this.info("serializationToObject...");
		this.orgTitaString = msgJson;

		TitaVo msgMap = new ObjectMapper().readValue(msgJson, TitaVo.class);
		this.orgTitaObj = (TitaVo) msgMap.clone();

		return (TitaVo) msgMap.clone();
	}

	public String getJsonString(TitaVo titaVo) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(titaVo);
	}

	public String deserializationJsonString(TitaVo titaVo, ArrayList<TotaVo> totaVo) throws LogicException, IOException {
		this.info("deserializationJsonString...");
		String tota = "";
		// try {
		/* 排序 isWarn擺在最後 */
		Collections.sort(totaVo, new Comparator<TotaVo>() {
			@Override
			public int compare(TotaVo o1, TotaVo o2) {
				int a = o1.getTxrsut().equals("W") ? 2 : 1;
				int b = o2.getTxrsut().equals("W") ? 2 : 1;

				if (a >= b)
					return 1;
				else
					return -1;
			}
		});

		for (TotaVo t : totaVo) {
			if (totaVo.size() == 1 && (t.getMsgId().equals("LC100") || t.getMsgId().equals("LC013"))) {
				tota = t.getJsonString();
				tota = FormatUtil.pad9(Integer.toString(tota.getBytes("UTF-8").length + 5), 5) + tota;

				return tota;
			}
			if (!t.isError() && !t.isWarn()) {
				if (t.getEcTitaVo() != null) {
					TitaVo titaVoEC = t.getEcTitaVo();
					hostFormatter.init(titaVoEC.getTxcd() + ".tim");
					titaVoEC.putParam("ISEC", "");
					String totat = hostFormatter.format(true, titaVoEC);

					String msglen = StringCut.countLen(totat);
					this.info("tota len : " + msglen);
					tota += FormatUtil.pad9(msglen, 5) + totat.substring(5);
				} else {
					hostFormatter.init(t.getMsgId() + ".tom");
					String totat = hostFormatter.format(true, t);
					if (t.getOccursList().size() > 0) {
						hostFormatter.init(t.getMsgId() + "_OC.tom");
						for (LinkedHashMap<String, String> o : t.getOccursList()) {
							String s = hostFormatter.format(false, o);
							totat += s;
						}
					}
					String msglen = StringCut.countLen(totat);
					this.info("tota len : " + msglen);

					if (Integer.parseInt(msglen) > 62000)
						throw new LogicException(titaVo, "EC000", "下送電文超過最大長度62000..");

					tota += FormatUtil.pad9(msglen, 5) + totat.substring(5);

					if (t.isReentry()) {
//					hostFormatter.init(t.getMsgId() + ".tim");
//					String tita = "00000" + hostFormatter.format(true, titaVo).substring(180);
						String tita = tota.substring(0, 71) + this.getJsonString(titaVo);
						msglen = StringCut.countLen(tita);
						tota += FormatUtil.pad9(msglen, 5) + tita.substring(5);
					}
				}
			} else {
				if (t.isWarn())
					hostFormatter.init("WARN.tom");
				else
					hostFormatter.init("ERROR.tom");

				String totat = hostFormatter.format(true, t);
				String msglen = totat.getBytes("UTF-8").length + "";
				tota += FormatUtil.pad9(msglen, 5) + totat.substring(5);
			}
		}
		this.setStatus(true);
//		} catch (VoException e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.warn(errors.toString());
//
//			hostFormatter.init("ERROR.tom");
//			String totat = hostFormatter.format(true, new LogicException("CE000", errors.toString()).getErrorMsg(titaVo));
//			String msglen = totat.getBytes("BIG5").length + "";
//			tota += FormatUtil.pad9(msglen, 5) + totat.substring(5);
//			this.setStatus(false);
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.warn(errors.toString());
//
//			hostFormatter.init("ERROR.tom");
//			String totat = hostFormatter.format(true, new LogicException("CE000", errors.toString().substring(0, 200)).getErrorMsg(titaVo));
//			String msglen = totat.getBytes("BIG5").length + "";
//			tota += FormatUtil.pad9(msglen, 5) + totat.substring(5);
//			this.setStatus(false);
//		}

		tota = FormatUtil.pad9(Integer.toString(tota.getBytes("UTF-8").length + 5), 5) + tota;

//		try {
//			tota = new ObjectMapper().writeValueAsString(totaVo);
//			tota = FormatUtil.pad9(Integer.toString(tota.getBytes("UTF-8").length + 5), 5) + tota;
//		} catch (JsonProcessingException | UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return tota;
	}

//	private boolean isutf8_3(char ch) {
//		String s = Character.toString(ch);
//		try {
//			if (s.getBytes("UTF-8").length >= 3)
//				return true;
//		} catch (UnsupportedEncodingException e) {
//			return true;
//		}
//	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
