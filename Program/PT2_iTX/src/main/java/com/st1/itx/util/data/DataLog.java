package com.st1.itx.util.data;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;

import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.TxDataLogId;
import com.st1.itx.db.service.TxDataLogService;

import com.st1.itx.db.service.springjpa.cm.TableColumnServiceImpl;

import com.st1.itx.util.parse.Parse;

@Component("dataLog")
@Scope("prototype")
public class DataLog extends CommBuffer {
	/* DB服務注入 */
	@Autowired
	public TxDataLogService txDataLogService;

	@Autowired
	public TableColumnServiceImpl tableColumnServiceImpl;

	@Autowired
	Parse parse;

	private Object bef;

	private Object aft;

	private String objectName = "";

	private int txsno = 0;

	private List<Map<String, Object>> otherList = new ArrayList<Map<String, Object>>();

	public static AtomicInteger atomNext = new AtomicInteger(0);

	private Map<String, Object> oldnewMap = new LinkedHashMap<String, Object>();

	/**
	 * set parameters
	 * 
	 * @param titaVo TitaVo
	 * @param bef    Before data
	 * @param aft    After data
	 */
	public void setEnv(TitaVo titaVo, Object bef, Object aft) {
//		this.setTitaVo(titaVo);
//		this.bef = bef;
//		this.aft = aft;
//		objectName = bef.getClass().getSimpleName();
		setEnv(titaVo, bef, aft, null);
	}

	/**
	 * set parameters
	 * 
	 * @param titaVo  TitaVo
	 * @param bef     Before data
	 * @param aft     After data
	 * @param otrList 其他變更項目一次放入
	 */
	public void setEnv(TitaVo titaVo, Object bef, Object aft, List<Map<String, Object>> otrList) {
		this.setTitaVo(titaVo);
		this.bef = bef;
		this.aft = aft;
		objectName = bef.getClass().getSimpleName();
		if (otrList != null) {
			otherList.addAll(otrList);
		}
	}

	/**
	 * 逐一放入其他變更項目
	 * 
	 * @param key key
	 * @param bef before value
	 * @param aft after value
	 */
	public void setLog(String key, Object bef, Object aft) {
//		this.info("setLog = " + key + "/" + bef + "/" + aft);
//		if (otherList == null) {
//			otherList = new ArrayList<HashMap<String, Object>>();
//		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("f", key);
		map.put("o", bef);
		map.put("n", aft);
		otherList.add(map);
	}

	/**
	 * 針對多筆式明細檔比對用,放入原值
	 * 
	 * @param key key
	 * @param val value
	 */
	public void putOld(String key, Object val) {
		oldnewMap.put(key + "_O", val);
	}

	/**
	 * 針對多筆式明細檔比對用,放入新值
	 * 
	 * @param key key
	 * @param val value
	 */
	public void putNew(String key, Object val) {
		oldnewMap.put(key + "_N", val);
	}

	/**
	 * 記錄oldnewMap新舊有差異者
	 */

	/**
	 * 記錄oldnewMap新舊有差異者
	 * 
	 * @param dVal 預設值
	 */
	public void compareOldNew(Object dVal) {
		String oKey = "";
		Object oVal = dVal;
		Object nVal = dVal;
		boolean newfg = false;

		Set set = oldnewMap.keySet();
		Object[] arr = set.toArray();
		Arrays.sort(arr);

		for (Object key : arr) {
//		for (Map.Entry<String, Object> entry : oldnewMap.entrySet()) {
//			this.info("key:" + entry.getKey() + ",value:" + entry.getValue());
//			String mapKey = entry.getKey().trim();
			String orgKey = key.toString();

			String mapKey = orgKey.substring(0, orgKey.length() - 2);

			if (!oKey.isEmpty() && !oKey.equals(mapKey)) {
				// add difference
				if (oVal != nVal) {
					this.setLog(oKey, oVal, nVal);
				}
				oVal = dVal;
				nVal = dVal;
			}

			oKey = mapKey;

//			if ("_O".equals(entry.getKey().substring(entry.getKey().length() - 2))) {
			if ("_O".equals(orgKey.substring(orgKey.length() - 2))) {
//				oVal = entry.getValue();
				oVal = oldnewMap.get(orgKey).toString();
			} else {
//				nVal = entry.getValue();
				nVal = oldnewMap.get(orgKey).toString();
			}
		}
	}

	@Override
	public void exec() throws LogicException {
		toExec("", "");
	}

	public void exec(String reason) throws LogicException {
		toExec(reason, "");
	}

	public void exec(String reason, String MrKey) throws LogicException {
		this.info("DataLog exec 3 = " + reason + "/" + MrKey);
		toExec(reason, MrKey);
	}

	private void toExec(String reason, String MrKey) throws LogicException {
		Map<String, Map<String, Object>> resultMap = this.compareFields(this.bef, this.aft);
		resultMap.remove("lastUpdate");
		resultMap.remove("lastUpdateEmpNo");
		int size = resultMap.size();

		Map<String, String> columnMap = new LinkedHashMap<String, String>();

		try {
			List<Map<String, String>> lTableColumnVo = tableColumnServiceImpl.findAll(objectName);
			for (Map<String, String> tVo : lTableColumnVo) {
//				this.info("Vo = " + tVo.get("F0") + "/" + tVo.get("F1") + "/" + tVo.get("F2"));
				columnMap.put(tVo.get("F1").toLowerCase(), tVo.get("F2"));
			}
		} catch (Throwable e) {
			throw new LogicException(titaVo, "EC004", e.getMessage());
		}

		TxDataLog txDataLog = new TxDataLog();

		TxDataLogId txDataLogId = new TxDataLogId();

//		txDataLogId.setTxDate(Integer.parseInt(this.titaVo.getCalDy()));
		txDataLogId.setTxDate(this.titaVo.getEntDyI());
		txDataLogId.setTxSeq(this.titaVo.getTxSeq());
		txDataLogId.setTxSno(txsno);
		this.txsno++;

		txDataLog.setTxDataLogId(txDataLogId);

		txDataLog.setTlrNo(this.titaVo.getTlrNo());
		txDataLog.setTranNo(this.titaVo.getTxCode());

		int CustNo = 0;
		int FacmNo = 0;
		int BormNo = 0;
		try {
			String CustNoX = this.titaVo.get("CustNo").toString();
			if (CustNoX != null && !"".equals(CustNoX))
				CustNo = Integer.parseInt(CustNoX);
		} catch (Throwable e) {

		}
		txDataLog.setCustNo(CustNo);
		try {
			String FacmNoX = this.titaVo.get("FacmNo").toString();
			if (FacmNoX != null && !"".equals(FacmNoX))
				FacmNo = Integer.parseInt(FacmNoX);
		} catch (Throwable e) {

		}
		txDataLog.setFacmNo(FacmNo);
		try {
			String BormNoX = this.titaVo.get("BormNo").toString();
			if (BormNoX != null && !"".equals(BormNoX))
				BormNo = Integer.parseInt(BormNoX);
		} catch (Throwable e) {

		}
		txDataLog.setBormNo(BormNo);

		if (MrKey.isEmpty()) {
			txDataLog.setMrKey(this.titaVo.get("MRKEY").trim());
		} else {
			txDataLog.setMrKey(MrKey);
		}

		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		if (size > 0) {
			this.info("物件1與物件2的屬性值有差異,差異結果如下：");

			Iterator<String> it = resultMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
//				this.info("  " + key + "(oldValue:" + resultMap.get(key).get("oldValue") + ",newValue:" + resultMap.get(key).get("newValue") + ")");
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("f", columnMap.get(key.toLowerCase()));
				map.put("o", resultMap.get(key).get("oldValue"));
				map.put("n", resultMap.get(key).get("newValue"));
				listMap.add(map);
			}
		} else {
			this.info("物件1與物件2的屬性值無差異！");
		}

		if (otherList != null) 
			listMap.addAll(otherList);

		txDataLog.setReason(reason);

		try {
			ObjectMapper mapper = new ObjectMapper();
			txDataLog.setContent(mapper.writeValueAsString(listMap));
		} catch (IOException e) {
			throw new LogicException("EC009", "資料格式");
		}

		try {
			txDataLogService.insert(txDataLog, titaVo);
		} catch (DBException e) {
			if (e.getErrorId() == 2)
				while (true) {
					if (this.txsno > 30)
						break;
					txDataLogId = txDataLog.getTxDataLogId();
					this.txsno++;
					txDataLogId.setTxSno(this.txsno);
					txDataLog.setTxDataLogId(txDataLogId);
					try {
						txDataLogService.insert(txDataLog, titaVo);
						break;
					} catch (DBException e1) {
						if (e1.getErrorId() == 2)
							this.warn("TxDataLog Key Duplicate : " + txDataLogId);
						else
							throw new LogicException(titaVo, "EC002", "資料變更紀錄(DataLog):" + e1.getErrorMsg());
					}
				}
			else
				throw new LogicException(titaVo, "EC002", "資料變更紀錄(DataLog):" + e.getErrorMsg());
		}
	}

	/**
	 * 比較兩個實體屬性值，返回一個map以有差異的屬性名為key，value為一個Map分別存oldObject,newObject此屬性名的值
	 * 
	 * @param oldObject 進行屬性比較的物件1
	 * @param newObject 進行屬性比較的物件2
	 * @return 屬性差異比較結果map
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Map<String, Object>> compareFields(Object oldObject, Object newObject) {
		Map<String, Map<String, Object>> map = null;

		try {
			/**
			 * 只有兩個物件都是同一型別的才有可比性
			 */
			if (oldObject.getClass() == newObject.getClass()) {
				map = new LinkedHashMap<String, Map<String, Object>>();

				Class clazz = oldObject.getClass();
				// 獲取object的所有屬性
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();

				for (PropertyDescriptor pd : pds) {
					// 遍歷獲取屬性名
					String name = pd.getName();

					// 獲取屬性的get方法
					Method readMethod = pd.getReadMethod();

					// 在oldObject上呼叫get方法等同於獲得oldObject的屬性值
					Object oldValue = readMethod.invoke(oldObject);
					// 在newObject上呼叫get方法等同於獲得newObject的屬性值
					Object newValue = readMethod.invoke(newObject);

					if (oldValue instanceof List) {
						continue;
					}

					if (newValue instanceof List) {
						continue;
					}

					// eric 2020.06.26
					if (oldValue == null) {
						oldValue = "";
					}
					if (newValue == null) {
						newValue = "";
					}

					if (oldValue instanceof Timestamp) {
//						oldValue = new Date(((Timestamp) oldValue).getTime());
						oldValue = parse.timeStampToString((Timestamp) oldValue);
					}

					if (newValue instanceof Timestamp) {
//						newValue = new Date(((Timestamp) newValue).getTime());
						newValue = parse.timeStampToString((Timestamp) newValue);
					}

					this.info("DataLog compareFields : " + name + "=" + oldValue + "/" + newValue);

					if (oldValue == null && newValue == null) {
						continue;
					} else if (oldValue == null && newValue != null) {
						Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
						valueMap.put("oldValue", oldValue);
						valueMap.put("newValue", newValue);

						map.put(name, valueMap);

						continue;
					}

					if (!oldValue.equals(newValue)) {// 比較這兩個值是否相等,不等就可以放入map了
						Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
						if (oldValue instanceof BigDecimal && (((BigDecimal) oldValue).compareTo((BigDecimal) newValue) == 0))
							continue;

						valueMap.put("oldValue", oldValue.toString());
						valueMap.put("newValue", newValue.toString());

						map.put(name, valueMap);

					}
				}
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		return map;
	}

	public Object clone(Object enity) {
		Class<?> c;
		Constructor<?> cons;
		Object object = null;
		try {
			c = Class.forName(enity.getClass().getName());
			cons = c.getConstructor();
			object = cons.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		new ModelMapper().map(enity, object);
		return object;
	}

	public Object dup(Object enity) {
		return enity;
	}

	protected int getNextHostTranC() {
		atomNext.compareAndSet(9999, 0); // 如果到底了 就歸零
		return atomNext.incrementAndGet();
	}

	/**
	 * 訂正刪除原紀錄
	 * 
	 * @param titaVo titaVo
	 * @throws LogicException ..
	 */

	public void delete(TitaVo titaVo) throws LogicException {
		TxDataLogId txDataLogId = new TxDataLogId();
		txDataLogId.setTxDate(titaVo.getOrgEntdyI() + 19110000);
		txDataLogId.setTxSeq(titaVo.getOrgTxSeq());

		int sno = 0;
		while (true) {
			txDataLogId.setTxSno(sno);
			TxDataLog txDataLog = txDataLogService.findById(txDataLogId, titaVo);
			if (txDataLog != null) {
				try {
					txDataLogService.delete(txDataLog, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0004", "資料變更紀錄檔" + e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				break;
			}

			sno++;
		}
	}
}
