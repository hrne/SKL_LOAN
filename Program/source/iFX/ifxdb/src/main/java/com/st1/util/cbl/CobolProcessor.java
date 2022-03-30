package com.st1.util.cbl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class CobolProcessor {

	@SuppressWarnings("rawtypes")
	public static String generate(Object cobol) throws Exception {
		Class<?> type = cobol.getClass();
		// System.out.println(type.getCanonicalName());

		Map map = BeanUtils.describe(cobol);
		// System.out.println(map.toString());
		// Dumper.dump(type.getName(), cobol);
		List<FieldType> list = new ArrayList<FieldType>();
		processList(list, "", type);
		//
		// Dumper.dump("FF", list);

		FmtTmp fmt = new FmtTmp();

		for (FieldType f : list) {
			// System.out.println("=====>" + f.getTagName());
			// sb.append(f.put(PropertyUtils.getProperty(cobol,
			// f.getTagName()).toString()));
			String value;
			try {
				value = PropertyUtils.getProperty(cobol, f.getTagName()).toString();

			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.out.println("warnning!! tag:" + f.getTagName() + " is null, let it be empty");
				value = "";
			}
			// System.out.println(f.getTagName() + "=>" + value);

			switch (f.getType()) {
			case FieldType.N:
				if (f.getFractionLen() == 0) {
					fmt.put9(value, f.getLen());
				} else {
					fmt.putMoney(value, f.getLen(), f.getFractionLen());
				}
				break;
			case FieldType.D:
				Object obj = PropertyUtils.getProperty(cobol, f.getTagName());

				fmt.putDate(obj, f.getLen());
				break;
			case FieldType.T:
				Object obj2 = PropertyUtils.getProperty(cobol, f.getTagName());
				fmt.putTime(obj2, f.getLen());
				break;
			case FieldType.X:
			default:
				fmt.put(value, f.getLen());
				break;

			}
		}
		// System.out.println(fmt.getBuilder().toString());
		return fmt.getBuilder().toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int parse(String tita, Object t) throws Exception {
		List<FieldType> list = new ArrayList<FieldType>();
		processList(list, "", t.getClass());
		Map map = new HashMap();
		FmtTmp fmt = new FmtTmp();
		fmt.setTextBuf(new TextBuf(tita));

		for (FieldType f : list) {
			String tag = f.getTagName();
			// System.out.println(f.getTagName());
			// String value = "";
			Object value;
			switch (f.getType()) {
			case FieldType.N:
				if (f.getFractionLen() > 0) {
					value = fmt.pickMoney(f.getLen(), f.getFractionLen()).toPlainString();
				} else {
					// value = fmt.pick9AsString(f.getLen());
					value = fmt.pick(f.getLen());
				}
				break;
			case FieldType.D:
				value = fmt.pickDate(f.getLen());
				break;
			case FieldType.T:
				value = fmt.pickTime(f.getLen());
				break;
			case FieldType.X:
			default:
				value = fmt.pick(f.getLen());
			}
			// System.out.println(tag + "=[" + value + "]" + "," + f.getLen());
			map.put(tag, value);
		}
		BeanUtils.populate(t, map);
		Dumper.dump("t2", t);

		return fmt.getCurrentOffset();
	}

	private static void processList(List<FieldType> list, String prefix, Class<?> clazz) throws Exception {

		if (clazz.isAnnotationPresent(FieldList.class)) {
			String[] vv = clazz.getAnnotation(FieldList.class).value();
			for (int i = 0; i < vv.length; i++)
				System.out.println(vv[i]);
			for (String v : vv) {
				Field f = clazz.getDeclaredField(v);
				if (f.isAnnotationPresent(Cobol.class)) {

					String ano = f.getAnnotation(Cobol.class).value();
					System.out.println(ano);
					String[] ss = ano.split(",");

					if (ano.trim().length() == 0) // component, type
						processList(list, f.getName(), f.getType());
					else if (ano.startsWith("O")) { // array, occurs
						for (int i = 0; i < Integer.parseInt(ss[1]); i++) {
							String tag = getName(prefix, v);
							processList(list, tag + "[" + i + "]", f.getType().getComponentType());
							// processList(list, f.getName() + "[" + i + "]",
							// f.getType().getComponentType());

						}
					} else {
						String tag = getName(prefix, v);
						// System.out.println(tag + "=" + ano);
						list.add(FieldType.build(tag, ss[0], ss.length > 1 ? ss[1] : "1"));
					}

				}
			}
		}
	}

	private static String getName(String prefix, String name) {
		if (prefix == null || prefix.trim().length() == 0)
			return name;
		else
			return prefix + "." + name;
	}
}
