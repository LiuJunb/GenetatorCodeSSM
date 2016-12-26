/**
 * Project Name:CreateProject
 * File Name:BuildComment.java
 * Package Name:com.createproject
 * Date:2016年4月11日上午11:52:48
 * Copyright (c) 2016, stnts.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.builder;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * ClassName:BuildComment <br/>
 * Date:     2016年4月11日 上午11:52:48 <br/>
 * @author   luohl
 * Copyright (c) 2016, stnts.com All Rights Reserved. 
 */
public class BuildComment {
	/**
	 *  构建类上面的注释
	 *
	 * @param bw
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	public static BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
		bw.newLine();
		bw.newLine();
		bw.write("/**");
		bw.newLine();
		bw.write(" * ");
		bw.newLine();
		bw.write(" * " + text);
		bw.newLine();
		bw.write(" * ");
		bw.newLine();
		bw.write(" */");
		return bw;
	}

	public static BufferedWriter buildPropertyComment(BufferedWriter bw, String text) throws IOException {
		bw.newLine();
		bw.write("\t/**");
		bw.newLine();
		bw.write("\t * " + text);
		bw.newLine();
		bw.write("\t */");
		return bw;
	}

	/**
	 *  构建方法上面的注释
	 *
	 * @param bw
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	public static BufferedWriter buildMethodComment(BufferedWriter bw, String text) throws IOException {
		bw.newLine();
		bw.write("\t/**");
		bw.newLine();
		bw.write("\t * " + text);
		bw.newLine();
		bw.write("\t */");
		return bw;
	}
}
