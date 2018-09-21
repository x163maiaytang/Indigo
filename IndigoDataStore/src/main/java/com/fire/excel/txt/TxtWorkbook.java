package com.fire.excel.txt;

import java.io.UnsupportedEncodingException;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.PasswordException;
 

public class TxtWorkbook extends Workbook{
	
	private TxtSheet sheet;
	

	public TxtWorkbook(byte[] bytes){
		try {
			String content = new String(bytes, "UTF-8");
			
			sheet = new TxtSheet(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		if(sheet != null){
			sheet.release();			
			sheet = null;
		}
	}

	@Override
	public Range[] findByName(String arg0) {
		return null;
	}

	@Override
	public Cell findCellByName(String arg0) {
		return null;
	}

	@Override
	public int getNumberOfSheets() {
		return 0;
	}

	@Override
	public String[] getRangeNames() {
		return null;
	}

	@Override
	public Sheet getSheet(int arg0) throws IndexOutOfBoundsException {
		return sheet;
	}

	@Override
	public Sheet getSheet(String arg0) {
		return null;
	}

	@Override
	public String[] getSheetNames() {
		return null;
	}

	@Override
	public Sheet[] getSheets() {
		return null;
	}

	@Override
	public boolean isProtected() {
		return false;
	}

	@Override
	protected void parse() throws BiffException, PasswordException {
		
	}

}
