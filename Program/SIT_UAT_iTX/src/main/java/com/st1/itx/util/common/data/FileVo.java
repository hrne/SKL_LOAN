package com.st1.itx.util.common.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.st1.itx.Exception.LogicException;

/**
 * @author ST1-Chih Wei
 *
 */
public abstract class FileVo extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -6230783957408765718L;

	abstract void setValueFromFile(ArrayList<String> lineList) throws LogicException;

	abstract ArrayList<String> toFile();

}
