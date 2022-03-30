package com.st1.def.menu;

public class MenuBuilder {

	// public static Menu fromXml(String filePath) throws Exception {
	// PoorManXml poorMan = new PoorManXml();
	// Document doc = poorMan.loadXml(filePath);
	// return parseDoc(doc);
	// }

	// private static Menu parseDoc(Document doc) throws Exception {
	// NodeList aplist = XPathAPI.selectNodeList(doc, "//menudef/menu");
	// Menu rootmenu = new Menu("root");
	// for (int i = 0; i < aplist.getLength(); i++) {
	//
	// Node apnode = aplist.item(i);
	// Menu apmenu = new Menu(apnode.getAttributes().getNamedItem("name")
	// .getNodeValue());
	// rootmenu.addMenu(apmenu);
	// //
	// System.out.println(apnode.getAttributes().getNamedItem("name").getNodeValue());
	// NodeList sublist = XPathAPI.selectNodeList(apnode, "menu");
	// for (int j = 0; j < sublist.getLength(); j++) {
	// Node subnode = sublist.item(j);
	// Menu submenu = new Menu(subnode.getAttributes()
	// .getNamedItem("name").getNodeValue());
	// apmenu.addMenu(submenu);
	// // System.out.println("\t" +
	// // subnode.getAttributes().getNamedItem("name").getNodeValue());
	// NodeList itemlist = XPathAPI.selectNodeList(subnode, "item");
	// for (int k = 0; k < itemlist.getLength(); k++) {
	// submenu.addItem(parseMenuItem(rootmenu, itemlist.item(k)));
	// }
	// }
	//
	// }
	//
	// return rootmenu;
	// }

	// private static MenuItem parseMenuItem(Menu root, Node item) {
	// String code = attrib(item, "code");
	// String name = attrib(item, "name");
	// String type = attrib(item, "type");
	// String auth = attrib(item, "auth");
	// String pass = attrib(item, "pass");
	// String cor = attrib(item, "cor");
	//
	// MenuItem menuItem = new MenuItem(code, name, type, auth, pass, cor);
	// root.getCodeMap().put(code, menuItem);
	// return menuItem;
	// }
	//
	// private static String attrib(Node item, String name) {
	// Node n = item.getAttributes().getNamedItem(name);
	// if (n == null)
	// return null;
	// else
	// return n.getNodeValue();
	//
	// }
	//
	// public static String toHtml(Menu rootmenu) {
	// StringBuilder sb = new StringBuilder();
	// // generate tab header
	// sb.append(generateTabHeader(rootmenu));
	//
	// for (int i = 0; i < rootmenu.getSubmenus().size(); i++) {
	// sb.append(generateTabPanel(rootmenu.submenus.get(i), i + 1));
	// }
	// return sb.toString();
	// }
	//
	// private static String generateTabPanel(Menu menu, int tabIndex) {
	// String atemplate = "<a href=\"#\">%s</a>";
	// StringBuilder sb = new StringBuilder();
	// // <div id=tab1>Contents of first tab</div>
	// String s = "<div class='mytabclass' id='tab" + tabIndex + "'>";
	// sb.append(s);
	//
	// sb.append("<span id='menulink'>");
	// for (int i = 0; i < menu.submenus.size(); i++) {
	// Menu m = menu.submenus.get(i);
	// sb.append("\t<a href='#mi_" + tabIndex + "_" + i + "'>"
	// + m.getName() + "</a>");
	// }
	// sb.append("</span><br/>");
	//
	// for (int i = 0; i < menu.submenus.size(); i++) {
	// Menu m = menu.submenus.get(i);
	// String k = "#k" + tabIndex + "_" + i;
	// // sb.append("\t<span>" + m.getName() + "</span><br/>");
	// sb.append("\t<a class='menuanchor' name='mi_" + tabIndex + "_" + i
	// + "'>" + m.getName() + "</a><br/>");
	// //sb.append("<div class='menuitem'>");
	// sb.append("<table class='menuTable'>");
	// for (int j = 0; j < m.items.size(); j += 2) {
	// sb.append("<tr>");
	// MenuItem item = m.items.get(j);
	// String t = item.code + " " + item.name;
	// sb.append("<td>").append(String.format(atemplate, t))
	// .append("</td>");
	// if (j + 1 < m.items.size()) {
	// item = m.items.get(j + 1);
	// t = item.code + " " + item.name;
	// sb.append("<td>").append(String.format(atemplate, t))
	// .append("</td>");
	// }
	// sb.append("</tr>");
	// }
	//
	// sb.append("</table>");
	// //sb.append("</div><br/>");
	// sb.append("<br/>");
	//
	// }
	//
	// sb.append("</div>");
	// return sb.toString();
	// }
	//
	// private static String generateTabHeader(Menu rootmenu) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("<ul>");
	// String template = "<li><a href=#tab%d>%s</a></li>";
	// for (int i = 0; i < rootmenu.submenus.size(); i++) {
	// int k = i + 1;
	// String s = String.format(template, k, rootmenu.submenus.get(i)
	// .getName());
	// sb.append("\t").append(s).append("");
	// }
	// sb.append("</ul>");
	//
	// return sb.toString();
	// }
	//
	// public static String build() throws Exception {
	// String filePath = GlobalValues.getMenuPath();
	// String s = toHtml(fromXml(filePath));
	// // System.out.println(s);
	// return s;
	// }
	//
	// /**
	// * @param args
	// */
	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// String filePath = "D:/myProject2/var4/java/ifxfolder/var/menu/menu2.xml";
	// try {
	// Menu rootmenu = fromXml(filePath);
	// // System.out.println(rootmenu.getSubmenus().size());
	// // System.out.println(rootmenu.toString());
	// System.out.println(toHtml(rootmenu));
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// }
	// }

}
