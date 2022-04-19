package com.st1.itx.util.common.data;

import java.util.Objects;

import javax.persistence.Entity;

@Entity
public class ExcelFontStyleVo implements Cloneable {

	@Override
	public ExcelFontStyleVo clone() throws CloneNotSupportedException {
		return (ExcelFontStyleVo) super.clone();
	}

	// 文字大小
	private short size;

	// 文字顏色
	private String color;

	// 文字字型
	private short font;

	// 文字粗體
	private boolean bold;

	// 文字斜體
	private boolean italic;

	// 文字格式
	private String format;

	// 文字對齊方式
	private String align;

	// 文字底線
	private boolean underline;

	// 表格框 粗細
	private short borderAll;

	// 表格背景色
	private String bgColor;

	// 多行
	private boolean wrapText;

	public ExcelFontStyleVo() {
		this.setAlign("");
		this.setBgColor("");
		this.setBold(false);
		this.setBorderAll((short) 0);
		this.setColor("");
		this.setFont((short) 0);
		this.setFormat("");
		this.setItalic(false);
		this.setSize((short) 0);
		this.setUnderline(false);
		this.setWrapText(false);
	}

	public ExcelFontStyleVo init() {

		ExcelFontStyleVo excelFontStyleVo = new ExcelFontStyleVo();

		this.setAlign("");
		this.setBgColor("");
		this.setBold(false);
		this.setBorderAll((short) 0);
		this.setColor("");
		this.setFont((short) 0);
		this.setFormat("");
		this.setItalic(false);
		this.setSize((short) 0);
		this.setUnderline(false);
		this.setWrapText(false);

		return excelFontStyleVo;
	}

	/**
	 * @return the size 文字大小
	 */
	public short getSize() {
		return size;
	}

	/**
	 * @return the color 文字顏色
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return the font 文字字體
	 */
	public short getFont() {
		return font;
	}

	/**
	 * @return the bold 文字粗體
	 */
	public boolean isBold() {
		return bold;
	}

	/**
	 * @return the italic 文字斜體
	 */
	public boolean isItalic() {
		return italic;
	}

	/**
	 * @return the format 文字格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @return the align 文字對齊方式
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @return the underline 文字底線
	 */
	public boolean isUnderline() {
		return underline;
	}

	/**
	 * @return the borderAll 邊框粗細
	 */
	public short getBorderAll() {
		return borderAll;
	}

	/**
	 * @return the bgColor 背景顏色
	 */
	public String getBgColor() {
		return bgColor;
	}

	/**
	 * @param size the size to set 文字大小
	 */
	public void setSize(short size) {
		this.size = size;
	}

	/**
	 * @param color the color to set 文字顏色
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * 字型<br>
	 * 1=標楷體<br>
	 * 2=微軟正黑體<br>
	 * 3=Times New Roman<br>
	 * 4=Arial<br>
	 * 5=新細明體
	 * 
	 * @param font the font to set 文字字體
	 */
	public void setFont(short font) {
		this.font = font;
	}

	/**
	 * @param bold the bold to set 文字粗體
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	/**
	 * @param italic the italic to set 文字斜體
	 */
	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	/**
	 * @param format the format to set 文字格式
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param align the align to set 文字對齊方式
	 */
	public void setAlign(String align) {
		this.align = align;
	}

	/**
	 * @param underline the underline to set 文字底線
	 */
	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	/**
	 * @param borderAll the borderAll to set 邊框粗細
	 */
	public void setBorderAll(short borderAll) {
		this.borderAll = borderAll;
	}

	/**
	 * @param bgColor the bgColor to set 背景顏色
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @return the wrapText
	 */
	public boolean isWrapText() {
		return wrapText;
	}

	/**
	 * @param wrapText the wrapText to set
	 */
	public void setWrapText(boolean wrapText) {
		this.wrapText = wrapText;
	}

	@Override
	public int hashCode() {
		return Objects.hash(align, bgColor, bold, borderAll, color, font, format, italic, size, underline, wrapText);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExcelFontStyleVo other = (ExcelFontStyleVo) obj;
		return Objects.equals(align, other.align) && Objects.equals(bgColor, other.bgColor) && bold == other.bold && borderAll == other.borderAll && Objects.equals(color, other.color)
				&& font == other.font && Objects.equals(format, other.format) && italic == other.italic && size == other.size && underline == other.underline && wrapText == other.wrapText;
	}

	@Override
	public String toString() {
		return "ExcelFontStyleVo [size=" + size + ", color=" + color + ", font=" + font + ", bold=" + bold + ", italic=" + italic + ", format=" + format + ", align=" + align + ", underline="
				+ underline + ", borderAll=" + borderAll + ", bgColor=" + bgColor + ", wrapText=" + wrapText + "]";
	}

}
