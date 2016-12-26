/**
 * Project Name:CreateProject
 * File Name:CreateBean.java
 * Package Name:com.createproject
 * Date:2016年4月11日上午11:26:08
 * Copyright (c) 2016, stnts.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.ulewo.easyjava.bean.DataTableInfo;
import com.ulewo.easyjava.utils.Constants;

/**
 * ClassName:CreateBean <br/>
 * Date:     2016年4月11日 上午11:26:08 <br/>
 * @author   luohl
 * Copyright (c) 2016, stnts.com All Rights Reserved. 
 */
public class BuildController {

	public static void buildController(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_CONTROLLER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_CONTROLLER,
				tableInfo.getBeanName() + Constants.SUFFIX_CONTROLLER + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
			bw.newLine();
			bw.newLine();
			bw.write("import javax.annotation.Resource;");
			bw.newLine();
			bw.newLine();
			bw.write("import org.springframework.stereotype.Controller;");
			bw.newLine();
			bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
			bw.newLine();
			bw.write("import org.springframework.web.servlet.ModelAndView;");
			bw.newLine();
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_PARAM + "." + tableInfo.getBeanParamName() + ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_BEAN + "." + tableInfo.getBeanName() + ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_VO + ".PaginationResult;");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE
					+ ";");
			bw.newLine();
			bw.newLine();
			bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " 控制层");
			bw.newLine();
			bw.write("@Controller");
			bw.newLine();
			bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_CONTROLLER + "{");
			bw.newLine();
			bw.newLine();
			bw.write("\t@Resource");
			bw.newLine();

			String beanName = tableInfo.getBeanName();
			String paramService = beanName + Constants.SUFFIX_SERVICE;
			paramService = paramService.substring(0, 1).toLowerCase() + paramService.substring(1);
			bw.write("\tprivate " + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + " " + paramService + ";");
			bw.newLine();
			//分页查询的方法
			bw = BuildComment.buildMethodComment(bw, "分页查询方法");
			bw.newLine();
			String methodName = "find" + tableInfo.getBeanName() + "ListByPage";
			bw.write("\t@RequestMapping(\"/" + methodName + ".do\")");
			bw.newLine();
			bw.write("\tpublic ModelAndView  " + methodName + "(" + tableInfo.getBeanParamName() + " param) {");
			bw.newLine();
			String pageBeanName = tableInfo.getBeanName();
			pageBeanName = pageBeanName.substring(0, 1).toLowerCase() + pageBeanName.substring(1);
			bw.write("\t\tModelAndView view = new ModelAndView(\"/page/" + pageBeanName + "List\");");
			bw.newLine();
			bw.write("\t\tPaginationResult<" + tableInfo.getBeanName() + "> result = this." + paramService
					+ ".findListByPage(param);");
			bw.newLine();
			bw.write("\t\tview.addObject(\"result\", result);");
			bw.newLine();

			bw.write("\t\treturn view;");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
			bw.write("}");
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outw != null) {
				try {
					outw.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
