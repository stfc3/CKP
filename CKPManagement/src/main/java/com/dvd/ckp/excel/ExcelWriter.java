/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author viettx
 */
public class ExcelWriter<T> {

	private static final Logger LOGGER = Logger.getLogger(ExcelWriter.class);

	public void write(List<T> listData, String filePath, String fileOutPut) throws Exception {
		Workbook w = null;
		XLSTransformer transformer;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			File fileIn = new File(filePath);
			// if (!fileIn.exists()) {
			// fileIn.createNewFile();
			// }
			inputStream = new FileInputStream(fileIn);

			File fileOut = new File(fileOutPut);
			if (!fileOut.exists()) {
				fileOut.createNewFile();
			}
			outputStream = new FileOutputStream(fileOut);

			Map<String, Object> beans = new HashMap();
			beans.put("data", listData);
			transformer = new XLSTransformer();
			w = transformer.transformXLS(inputStream, beans);
			w.write(outputStream);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {

			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				inputStream.close();
			}
		}

	}

}
