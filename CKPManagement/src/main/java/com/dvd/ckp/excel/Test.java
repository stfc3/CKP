package com.dvd.ckp.excel;

import java.util.List;

import com.dvd.ckp.excel.domain.Pumps;
import com.dvd.ckp.excel.exception.EmptyCellException;
import com.dvd.ckp.excel.exception.InvalidCellValueException;

public class Test {
	public static void main(String[] arg) throws InvalidCellValueException, EmptyCellException, Exception {
		ExcelReader<Pumps> reader = new ExcelReader<>();
		List<Pumps> listData = reader.read(
				"E:\\source\\CKP\\stfc\\CKP\\CKPManagement\\src\\main\\webapp\\file\\template\\import\\import_pump_data.xlsx",
				Pumps.class);
		for (Pumps pumps : listData) {
			System.out.println("Pump code: " + pumps.getPumpsCode());
		}
	}
}
