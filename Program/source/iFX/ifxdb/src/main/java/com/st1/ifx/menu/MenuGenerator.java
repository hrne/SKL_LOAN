package com.st1.ifx.menu;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;

public class MenuGenerator {
	private static final Logger logger = LoggerFactory.getLogger(MenuGenerator.class);

	static Menu convertToMenu(List<TranItem> trans) {
		Menu root = new Menu("root", true);
		HashMap<String, Menu> menuMap = new HashMap<String, Menu>();
		Menu parentMenu = null;
		for (TranItem tranItem : trans) {
			try {
				// System.out.println("convert tranItem " + tranItem.txcd +
				// " is " + tranItem.enabled);

				if (tranItem.type == 0) {// menu
					// 潘 空白導致一直出Exception
					if (tranItem.sbtyp.trim().isEmpty())
						continue;

					String menuName = tranItem.sbtyp.length() == 2 ? tranItem.sbtyp : tranItem.sbtyp.substring(2);
					menuName = menuName + " " + tranItem.txnm;
					Menu m = new Menu(menuName, tranItem.isEnabled());
					menuMap.put(tranItem.sbtyp, m);
					if (tranItem.sbtyp.length() == 2) {
						root.addMenu(m);
					} else {
						parentMenu = menuMap.get(tranItem.sbtyp.substring(0, 2));
						if (parentMenu != null) {
							parentMenu.addMenu(m);
							if (!parentMenu.isEnabled()) {
								m.setEnabled(false);
							}
						} else {
							logger.warn(FilterUtils.escape(tranItem.toString()));
						}

					}
				} else {
					parentMenu = menuMap.get(tranItem.sbtyp.substring(0, 2));
					if (parentMenu != null) {
						parentMenu.addItem(tranItem);
						if (!parentMenu.isEnabled()) {
							tranItem.enabled = false;
						}
					} else {
						logger.warn(tranItem.toString());
					}

				}
			} catch (Exception ex) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString()); // 柯 暫時關閉測試看看是否是 ststemerr.log null的問題
				logger.error("eror tran item {}, {}", tranItem.txcd, tranItem.sbtyp);
			}
		}
		// System.out.println(root.toString());
		return root;
	}

	static Map<String, TranItem> createEnabledTranMap(List<TranItem> trans) {
		Map<String, TranItem> enabledTranMap = new HashMap<String, TranItem>();
		List<String> disabledList = new ArrayList<String>();
		for (TranItem tranItem : trans) {
			if (tranItem.type == 2) {
				if (tranItem.enabled) {
					enabledTranMap.put(tranItem.txcd, tranItem);
				} else {
					disabledList.add(tranItem.txcd);
				}
			}
		}
		logger.debug("Trans:{}", trans.size());
		logger.debug("enabled trans:{}", enabledTranMap.size());
		logger.debug("disabled:{}, {}", disabledList.size(), disabledList);

		return enabledTranMap;
	}

	public static String toHtml(Menu rootmenu) {
		StringBuilder sb = new StringBuilder();
		// generate tab header
		sb.append(generateTabHeader(rootmenu));

		for (int i = 0; i < rootmenu.getSubmenus().size(); i++) {
			sb.append(generateTabPanel(rootmenu.submenus.get(i), i + 1));
		}
		return sb.toString();
	}

	private static String generateTabPanel(Menu menu, int tabIndex) {
		// System.out.println("generate level1 menu :" + menu.getName());
		String atemplate = "<a href=\"#\">%s</a>";
		String disabledTtemplate = "<span class='itemDisabled'>%s</span>";
		StringBuilder sb = new StringBuilder();
		// <div id=tab1>Contents of first tab</div>
		String s = "<div class='mytabclass' id='tab" + tabIndex + "'>";
		sb.append(s);

		sb.append("<span id='menulink'>");
		for (int i = 0; i < menu.submenus.size(); i++) {
			Menu m = menu.submenus.get(i);
			sb.append("\t<a href='#mi_" + tabIndex + "_" + i + "'>" + m.getName() + "</a>");
		}
		sb.append("</span><br/>");

		for (int i = 0; i < menu.submenus.size(); i++) {

			Menu m = menu.submenus.get(i);
			// System.out.println("generate level2 submenu :" + m.getName());
			String k = "#k" + tabIndex + "_" + i;
			// sb.append("\t<span>" + m.getName() + "</span><br/>");
			sb.append("\t<a class='menuanchor'  name='mi_" + tabIndex + "_" + i + "'>" + m.getName() + "</a><br/>");
			// sb.append("<div class='menuitem'>");
			sb.append("<table class='menuTable'>");
			for (int j = 0; j < m.items.size(); j += 2) {
				sb.append("<tr>");
				TranItem item = m.items.get(j);
				String t = item.txcd + " " + item.txnm;
				sb.append("<td>").append(String.format(item.isEnabled() ? atemplate : disabledTtemplate, t))
						.append("</td>");
				if (j + 1 < m.items.size()) {
					item = m.items.get(j + 1);
					t = item.txcd + " " + item.txnm;
					sb.append("<td>").append(String.format(item.isEnabled() ? atemplate : disabledTtemplate, t))
							.append("</td>");
				}
				sb.append("</tr>");
			}

			sb.append("</table>");
			// sb.append("</div><br/>");
			sb.append("<br/>");

		}

		sb.append("</div>");
		return sb.toString();
	}

	private static String generateTabHeader(Menu rootmenu) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		String template = "<li><a href=#tab%d>%s</a></li>";
		for (int i = 0; i < rootmenu.submenus.size(); i++) {
			int k = i + 1;
			String s = String.format(template, k, rootmenu.submenus.get(i).getName());
			sb.append("\t").append(s).append("");
		}
		sb.append("</ul>");

		return sb.toString();
	}

}
