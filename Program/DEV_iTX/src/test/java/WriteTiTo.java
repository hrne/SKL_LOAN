
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public class WriteTiTo {

	public static void main(String[] args) {
		System.out.println(writeTom("L4901"));
	}

	public WriteTiTo() {
	}

	public static String writeTom(String txcode) {
//		File js = new File("/home/weblogic/ifxDoc/ifxfolder/runtime/tran/" + txcode.substring(0, 2) + "/" + txcode + ".js");
		File js = new File("Z:\\ifxfolder\\runtime\\tran\\" + txcode.substring(0, 2) + "\\" + txcode + ".js");
		System.out.println("Z:\\ifxfolder\\runtime\\tran\\" + txcode.substring(0, 2) + "\\" + txcode + ".js");
		if (js.exists())
			writeFile(true, js, txcode);
		else
			return "Not found " + txcode + ".js";

		return null;
	}

	@SuppressWarnings("unchecked")
	private static void writeFile(boolean tiOrTo, File js, String txcode) {
		InputStreamReader read = null;
		BufferedReader br = null;
		ScriptEngineManager manager = null;
		ScriptEngine engine = null;
		Invocable invocable = null;
		try {
			read = new InputStreamReader(new FileInputStream(js), "UTF-8");
			br = new BufferedReader(read);
			String re = "";

			while (br.ready())
				re += br.readLine();

			re += "\nvar loop=false;var form=[];var fl={};function fields(){var b={};getTranDef().rtns.forEach(function(c){if(c.type==\"form\")form=form.concat(c.name);c.fields.forEach(function(a){if(null!=a._[2]){b[a._[0]]=a._[1].toString()+','+a._[2].toString()+(a._[3]==0?'':'.'+a._[3].toString());}if(loop){fl[c.name]=a._[0];loop=false;}if(\"#LOOP\"==a._[0]){loop=true}})});return b}function tim(){var b=fields(),c={};getTranDef().tim.list.forEach(function(a){null!=b[a]&&(c[a]=b[a])});return JSON.stringify(c)};function tom(){var b=fields(),c={},d={};form.forEach(function(a){getTranDef().tom[a].forEach(function(x){if(x==fl[a])c[\"#LOOP\"]=\"\";if(null!=b[x]){c[x]=b[x]}});d[a]=c});return JSON.stringify(d)};";
			manager = new ScriptEngineManager();
			engine = manager.getEngineByName("js");
			engine.eval(re);

			invocable = (Invocable) engine;
			String tim = (String) invocable.invokeFunction("tim");
			String tom = (String) invocable.invokeFunction("tom");

			File timD = new File("Z:\\iTX\\tim\\" + txcode.substring(0, 2) + "\\");
			File tomD = new File("Z:\\iTX\\tom\\" + txcode.substring(0, 2) + "\\");
			if (!timD.exists())
				timD.mkdirs();
			if (!tomD.exists())
				tomD.mkdirs();

			LinkedHashMap<String, String> result_tim = new ObjectMapper().readValue(tim, LinkedHashMap.class);
			File timH = new File("Z:\\iTX\\tim\\" + txcode.substring(0, 2) + "\\" + txcode + ".tim");
			if (timH.exists())
				timH.delete();

			FileOutputStream fos = new FileOutputStream(timH, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);

			for (Entry<String, String> s : result_tim.entrySet()) {
				String name = s.getKey().substring(1).trim();
				String type = s.getValue().split(",")[0].trim();
				String precision = s.getValue().split(",")[1].trim();

				if (type.toLowerCase().equals("x") || type.toLowerCase().equals("c") || type.toLowerCase().equals("u") || type.toLowerCase().equals("p") || type.toLowerCase().equals("j"))
					type = "X";
				else if (type.toLowerCase().startsWith("+"))
					type = "+9";
				else
					type = "9";

				bw.write(name + "=" + type + "," + precision + "\r\n");
			}

			bw.flush();
			osw.flush();
			fos.flush();
			bw.close();
			osw.close();
			fos.close();
			bw = null;
			osw = null;
			fos = null;

			LinkedHashMap<String, HashMap<String, String>> result_tom = new ObjectMapper().readValue(tom, LinkedHashMap.class);
			for (Entry<String, HashMap<String, String>> s : result_tom.entrySet()) {
				File tomH = new File("Z:\\iTX\\tom\\" + s.getKey().substring(0, 2) + "\\" + s.getKey() + ".tom");
				File tomO = new File("Z:\\iTX\\tom\\" + s.getKey().substring(0, 2) + "\\" + s.getKey() + "_OC.tom");

				if (tomH.exists())
					tomH.delete();

				if (tomO.exists())
					tomO.delete();

				FileOutputStream fosH = new FileOutputStream(tomH, true);
				OutputStreamWriter oswH = new OutputStreamWriter(fosH, "UTF-8");
				BufferedWriter bwH = new BufferedWriter(oswH);

				FileOutputStream fosO = new FileOutputStream(tomO, true);
				OutputStreamWriter oswO = new OutputStreamWriter(fosO, "UTF-8");
				BufferedWriter bwO = new BufferedWriter(oswO);

				boolean isloopS = false;
				int len = 0;
				String commentH = "", commentO = "";
				for (Entry<String, String> x : s.getValue().entrySet()) {
					if (x.getKey().substring(1).equals("LOOP")) {
						isloopS = true;
						continue;
					}

					String name = x.getKey().substring(1).trim();
					String type = x.getValue().split(",")[0].trim();
					String precision = x.getValue().split(",")[1].trim();

					if (type.toLowerCase().equals("x") || type.toLowerCase().equals("c") || type.toLowerCase().equals("u") || type.toLowerCase().equals("p") || type.toLowerCase().equals("j"))
						type = "X";
					else if (type.toLowerCase().startsWith("+"))
						type = "+9";
					else
						type = "9";

					if (!isloopS)
						commentH += name + "=" + type + "," + precision + "\r\n";
					else
						commentO += name + "=" + type + "," + precision + "\r\n";

					if (precision.indexOf(".") != -1) {
						len += Integer.parseInt(precision.split("\\.")[0]);
						len += Integer.parseInt(precision.split("\\.")[1]);
					} else
						len += Integer.parseInt(precision);
				}

				bwH.write(commentH);
				bwO.write(commentO);

				/* 回收 */
				bwH.flush();
				oswH.flush();
				fosH.flush();
				bwH.close();
				oswH.close();
				fosH.close();
				bwH = null;
				oswH = null;
				fosH = null;

				bwO.flush();
				oswO.flush();
				fosO.flush();
				bwO.close();
				oswO.close();
				fosO.close();
				bwO = null;
				oswO = null;
				fosO = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
