package xmlread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlToJob {

	public String readxml(String filename) throws DocumentException {

		StringBuffer sql = new StringBuffer();

		// String filename =
		// "C:/Users/my_perfume/Desktop/download/mkt_lbs_city_visitors.xml";
		File f = new File(filename);

		// 读取文件
		SAXReader saxReader = new SAXReader();
		Document document;

		// 获取对象
		document = saxReader.read(f);
		Element root = document.getRootElement();// 获取根节点

		// 寻找根节点的所有子节点
		List<Element> listElement = root.elements();
		String jobname = "";
		for (Element e : listElement) {

			if (e.getName().equals("job")) { // 找到job
				List<Element> jobs = e.elements();

				
				String run_num = "";
				String interval = "";
				String run_flag = "";
				String ctrl_db = "";
				String ctrl_file = "";

				for (Element s : jobs) {

					if (s.getName().equals("name"))
						jobname = s.getTextTrim();
					else if (s.getName().equals("run_num"))
						run_num = s.getTextTrim();
					else if (s.getName().equals("interval"))
						interval = s.getTextTrim();
					else if (s.getName().equals("run_flag"))
						run_flag = s.getTextTrim();
					else if (s.getName().equals("ctrl_db"))
						ctrl_db = s.getTextTrim();
					else if (s.getName().equals("ctrl_file"))
						ctrl_file = s.getTextTrim();
				}

				// 插入 mgr_job表sql
				sql.append("insert into mgr_job_x (job_id , job_name ,run_num ,interval ,run_flag ,ctrl_db ,ctrl_file ,ctrl_file_path, class_id, ctrl_id)  values (" );
				sql.append("'"+ jobname + "' ,");
				sql.append("'"+ jobname + "' ,");
				sql.append("'"+ run_num + "' ,");
				sql.append("'"+ interval + "' ,");
				sql.append("'"+ run_flag + "' ,");
				sql.append("'none' ,");
				sql.append("'none' ,");
				sql.append("'none' ,");
				sql.append("'1' ,");
				sql.append("'"+ jobname + "'");
				sql.append(" ) ;");
				sql.append("\n");
				sql.append("commit;");
				sql.append("\n");
			}
			

			if (e.getName().equals("steps")) { // 找到steps

				List<Element> step = e.elements();
				for (Element s : step) {

					if (s.getName().equals("step")) {
						String no = "";
						String fun_code = "";
						String is_iterate = "";
						String iterate_var = "";
						String is_run = "";
						String rel_step = "";
						String desc = "";
						List<Element> stepElement = s.elements();
						for (Element stepnode : stepElement) { // 获得所有step

							if (stepnode.getName().equals("no"))
								no = stepnode.getTextTrim();
							else if (stepnode.getName().equals("type"))
								fun_code = stepnode.getTextTrim();
							else if (stepnode.getName().equals("is_iterate"))
								is_iterate = stepnode.getTextTrim();
							else if (stepnode.getName().equals("iterate_var"))
								iterate_var = stepnode.getTextTrim();
							else if (stepnode.getName().equals("is_run"))
								is_run = stepnode.getTextTrim();
							else if (stepnode.getName().equals("rel_step"))
								rel_step = stepnode.getTextTrim();
							else if (stepnode.getName().equals("desc"))
								desc = stepnode.getTextTrim();
							else if (stepnode.getName().equals("params")) {

								List<Element> param = stepnode.elements();

								String param_code;
								String param_value;

								for (Element p : param) {
									if (p.getName().equals("param")) { // 获得param
																		// 内容

										Attribute p_name = p.attribute("name");

										param_value = p.getTextTrim().replace("'", "''");
										param_code = p_name.getValue();

										sql.append("insert into mgr_job_step_params_x ("
												+ "job_id, no, param_code, param_value, param_type ) values (");
										sql.append("'" + jobname + "' ,");
										sql.append("'" + no + "' ,");
										sql.append("'" + param_code + "' ,");
										sql.append("'" + param_value + "' ,");
										sql.append("1 ) ;");
										sql.append("\n");
										sql.append("commit;");
										sql.append("\n");

									}

								}

							}
						}

						sql.append("insert into mgr_job_step_x (job_id, no, fun_code, is_iterate, iterate_var"
								+ ", rel_step, is_run, step_desc) values ( ");

						sql.append("'" + jobname + "' ,");
						sql.append("'" + no + "' ,");
						sql.append("'" + fun_code + "' ,");
						sql.append("'" + is_iterate + "' ,");
						sql.append("'" + iterate_var + "' ,");
						sql.append("'" + rel_step + "' ,");
						sql.append("'" + is_run + "' ,");
						sql.append("'" + desc + "'");
						sql.append(") ;");
						sql.append("\n");
						sql.append("commit;");
						sql.append("\n");
					}
				}
			}
			if (e.getName().equals("vars")) {

				List<Element> var = e.elements();

				for (Element v : var) {
					if (v.getName().equals("var")) {

						List<Element> varElement = v.elements();

						String name = "";
						String content = "";
						String type = "";
						String exec_class = "";

						for (Element varnode : varElement) {

							if (varnode.getName().equals("name"))
								name = varnode.getTextTrim();
							if (varnode.getName().equals("type"))
								type = varnode.getTextTrim();
							if (varnode.getName().equals("content"))
								content = varnode.getTextTrim();
							if (varnode.getName().equals("exec_class"))
								exec_class = varnode.getTextTrim().replace("com.nl.analyse.var", "com.nl.task.var");

						}
						sql.append("insert into mgr_job_var (job_id, name, type, content, exec_class) values ( ");
						sql.append("'" + jobname + "' ,");
						sql.append("'" + name + "' ,");
						sql.append("'" + type + "' ,");
						sql.append("'" + content + "' ,");
						sql.append("'" + exec_class + "'");
						sql.append(") ;");
						sql.append("\n");
						sql.append("commit;");
						sql.append("\n");

					}
				}
			}
		}

		return sql.toString();

	}

	public static void main(String[] args) throws DocumentException, IOException {

		// 需要转换的文件列表 ：
		// xxx.xml
		// ssss.xml
		String xmlsfiles = "D:/aa.txt";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(xmlsfiles)));
		StringBuffer sqls =new StringBuffer();
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			String filename = line.replace(".xml", "");
			String infile = "C:/Users/my_perfume/Desktop/download/move/aa/" + filename + ".xml";

			// 转换为 insert sql
			String sql = new XmlToJob().readxml(infile);
			// System.out.println(sql);
			// 生成 sql文件
			sqls.append(sql);
			//File outfile = new File("d:/new_job/" + filename + ".sql");
			//PrintStream ps = new PrintStream(new FileOutputStream(outfile));
			//ps.println(sql);// 往文件里写入字符串
		}
		File outfile = new File("d:/newjob.sql");
		PrintStream ps = new PrintStream(new FileOutputStream(outfile));
		ps.println(sqls.toString());// 往文件里写入字符串
	}

}