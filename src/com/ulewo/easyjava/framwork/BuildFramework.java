/**
 * Project Name:CreateProject
 * File Name:CreateBean.java
 * Package Name:com.createproject
 * Date:2016年4月11日上午11:26:08
 * Copyright (c) 2016, stnts.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.framwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.ulewo.easyjava.bean.DataTableInfo;
import com.ulewo.easyjava.utils.Constants;

/**
 * ClassName:CreateBean <br/>
 * Date:     2016年4月11日 上午11:26:08 <br/>
 * @author   luohl
 * Copyright (c) 2016, stnts.com All Rights Reserved. 
 */
public class BuildFramework {

	static String classzName = new Object() {
		public String getClassName() {
			String clazzName = this.getClass().getName();
			return clazzName.substring(0, clazzName.lastIndexOf('$'));
		}
	}.getClassName();

	public static void initFramwork(DataTableInfo tableInfo) {
		//Enums
		List<String> headInfoList = new ArrayList<String>();
		headInfoList.add("package " + Constants.PACKAGE_ENUMS);
		buildEnums(tableInfo, headInfoList, "PageSize", Constants.PATH_ENUMS);

		//VO
		headInfoList.clear();
		headInfoList.add("package " + Constants.PACKAGE_VO);
		headInfoList.add("import java.util.ArrayList");
		headInfoList.add("import java.util.List");
		buildEnums(tableInfo, headInfoList, "PaginationResult", Constants.PATH_VO);

		headInfoList.clear();
		headInfoList.add("package " + Constants.PACKAGE_VO);
		headInfoList.add("import " + Constants.PACKAGE_ENUMS + ".PageSize");
		buildEnums(tableInfo, headInfoList, "SimplePage", Constants.PATH_VO);

		//Mapper
		headInfoList.clear();
		headInfoList.add("package " + Constants.PACKAGE_MAPPER);
		headInfoList.add("import java.util.List");
		buildEnums(tableInfo, headInfoList, "BaseMapper", Constants.PATH_MAPPER);

		//Param
		headInfoList.clear();
		headInfoList.add("package " + Constants.PACKAGE_PARAM);
		headInfoList.add("import " + Constants.PACKAGE_VO + ".SimplePage");
		buildEnums(tableInfo, headInfoList, "BaseParam", Constants.PATH_PARAM);

	}

	private static void buildEnums(DataTableInfo tableInfo, List<String> headInfoList, String fileName,
			String outPutPath) {
		File folder = new File(outPutPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(outPutPath, fileName + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		InputStream in = null;
		InputStreamReader inr = null;
		BufferedReader bf = null;
		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			String framworkPath = classzName.substring(0, classzName.lastIndexOf("."));
			in = new FileInputStream(new File("src/" + framworkPath.replace(".", "/") + "/" + fileName + ".txt"));
			inr = new InputStreamReader(in, "utf-8");
			bf = new BufferedReader(inr);
			for (String str : headInfoList) {
				bw.write(str + ";");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			String lineInfo = null;
			while ((lineInfo = bf.readLine()) != null) {
				bw.write(lineInfo);
				bw.newLine();
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inr != null) {
				try {
					inr.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (null != bf) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

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
