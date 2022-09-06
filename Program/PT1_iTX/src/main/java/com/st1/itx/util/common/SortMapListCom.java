package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;

/**
 * Sort Map List<br>
 * <br>
 * 將如 query 傳回的 Maps List 進行排序用的工具。<br>
 * 先用 beginSort 取得物件，再一個一個欄位流水線式地增加排序，<br>
 * 最後 getList 取回已經排序好的 List。<br>
 * 實際進行排序的 List 是一份新拷貝的 List。<br>
 * 範例可參考 L4211Report
 * 
 * @author xiangwei
 * @version 1.0.0
 *
 */
@Component("SortMapListCom")
@Scope("prototype")
public class SortMapListCom {

	@Autowired
	MakeReport makeReport;

	/**
	 * Object to indicate a column to compare
	 * 
	 * @author xiangwei
	 *
	 */
	private static class Comparer {

		private String key;
		private ComparisonStrategy strategy;
		private Boolean byAsc = false;

		public Comparer(String _key, ComparisonStrategy _strategy, boolean _byAsc) {
			key = _key;
			strategy = _strategy;
			byAsc = _byAsc;
		}

		public int doCompare(String value1, String value2) {
			int result = this.strategy.compare(value1, value2);
			return result * (this.byAsc ? 1 : -1);
		}
	}

	/**
	 * Strategies for comparing
	 * 
	 * @author xiangwei
	 *
	 */
	private interface ComparisonStrategy {
		int compare(String value1, String value2);
	}

	private static class SimpleStringStrategy implements ComparisonStrategy {
		public int compare(String value1, String value2) {
			return value1.compareTo(value2);
		}
	}

	private static class SimpleNumberStrategy implements ComparisonStrategy {
		private BigDecimal getBigDecimal(String inputString) {
			BigDecimal result = BigDecimal.ZERO;

			if (inputString != null && !inputString.isEmpty()) {
				try {
					result = new BigDecimal(inputString);
				} catch (NumberFormatException e) {
					result = BigDecimal.ZERO;
				}
			}
			return result;
		}

		public int compare(String value1, String value2) {
			BigDecimal bd1 = getBigDecimal(value1);
			BigDecimal bd2 = getBigDecimal(value2);
			return bd1.compareTo(bd2);
		}
	}

	private static final ComparisonStrategy simpleString = new SimpleStringStrategy();
	private static final ComparisonStrategy simpleNumber = new SimpleNumberStrategy();

	/**
	 * 用於排序的物件，排序完後透過 getList 取得 List。<br>
	 * 雖然 getList 之後仍可以再進行排序、再進行 get，<br>
	 * 但用的都會是同一個 List 物件，所以這種處理時需要注意物件的狀態與處理順序。
	 * 
	 * @author xwh
	 */
	public static class MapListToSort {
		private List<Map<String, String>> wrappedList;
		private List<Comparer> comparers = new ArrayList<Comparer>();

		private MapListToSort(List<Map<String, String>> l) {
			wrappedList = new ArrayList<Map<String, String>>(l);
		}

		/**
		 * 新增以 String 方式進行漸增排序的欄位。<br>
		 * 底部用的是 String 的 compareTo。
		 * 
		 * @param key 欄位名稱
		 * @return 排序用的物件
		 */
		public MapListToSort ascString(String key) {
			comparers.add(new Comparer(key, simpleString, true));
			return this;
		}

		/**
		 * 新增以 String 方式進行漸減排序的欄位。<br>
		 * 底部用的是 String 的 compareTo。
		 * 
		 * @param key 欄位名稱
		 * @return 排序用的物件
		 */
		public MapListToSort descString(String key) {
			comparers.add(new Comparer(key, simpleString, false));
			return this;
		}

		/**
		 * 新增以數字方式進行漸增排序的欄位。<br>
		 * 底部用的是 BigDecimal 的 compareTo。
		 * 
		 * @param key 欄位名稱
		 * @return 排序用的物件
		 */
		public MapListToSort ascNumber(String key) {
			comparers.add(new Comparer(key, simpleNumber, true));
			return this;
		}

		/**
		 * 新增以數字方式進行漸減排序的欄位。<br>
		 * 底部用的是 BigDecimal 的 compareTo。
		 * 
		 * @param key 欄位名稱
		 * @return 排序用的物件
		 */
		public MapListToSort descNumber(String key) {
			comparers.add(new Comparer(key, simpleNumber, false));
			return this;
		}

		/**
		 * @throws LogicException 取得排序好的 List。
		 * @return 排序好的 List
		 */
		public List<Map<String, String>> getList() throws LogicException {
			try {
				sortList(wrappedList, comparers);
				return wrappedList;
			} catch (Exception e) {
				final Logger logger = LoggerFactory.getLogger(SortMapListCom.class);
				logger.error("Sort failed. Orders: " + comparers.toString());
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				throw new LogicException("E0013", "SortMapListCom");
			}
		}

		private void sortList(List<Map<String, String>> list, List<Comparer> comparers) {
			list.sort((c1, c2) -> {
				int comparison = 0;
				for (Comparer comparer : comparers) {
					comparison = comparer.doCompare(c1.get(comparer.key), c2.get(comparer.key));

					if (comparison != 0)
						return comparison;
				}
				return 0;
			});
		}
	}

	/**
	 * 將 Maps List 轉換成排序用的物件。<br>
	 * 排序物件用的是一份另外拷貝的 List，所以輸入的 List 不會被更動。
	 * 
	 * @param list
	 * @return 用於排序的物件
	 */
	public MapListToSort beginSort(List<Map<String, String>> list) {
		return new MapListToSort(list);
	}
}
