package com.fire.excel.txt.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class TxtWorkbook implements Workbook{
	
	private TxtSheet sheet;
	

	public TxtWorkbook(byte[] bytes, String className){
		try {
			String content = new String(bytes, "UTF-8");
			
			sheet = new TxtSheet(content, className);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	@Override
	public int getActiveSheetIndex() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setActiveSheet(int sheetIndex) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getFirstVisibleTab() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setFirstVisibleTab(int sheetIndex) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setSheetOrder(String sheetname, int pos) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setSelectedTab(int index) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setSheetName(int sheet, String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getSheetName(int sheet) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getSheetIndex(String name) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getSheetIndex(Sheet sheet) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Sheet createSheet() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Sheet createSheet(String sheetname) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Sheet cloneSheet(int sheetNum) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getNumberOfSheets() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Sheet getSheetAt(int index) {
		return sheet;
	}


	@Override
	public Sheet getSheet(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removeSheetAt(int index) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setRepeatingRowsAndColumns(int sheetIndex, int startColumn,
			int endColumn, int startRow, int endRow) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Font createFont() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Font findFont(short boldWeight, short color, short fontHeight,
			String name, boolean italic, boolean strikeout, short typeOffset,
			byte underline) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public short getNumberOfFonts() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Font getFontAt(short idx) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CellStyle createCellStyle() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public short getNumCellStyles() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public CellStyle getCellStyleAt(short idx) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void write(OutputStream stream) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getNumberOfNames() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Name getName(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Name getNameAt(int nameIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Name createName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getNameIndex(String name) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void removeName(int index) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeName(String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setPrintArea(int sheetIndex, String reference) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setPrintArea(int sheetIndex, int startColumn, int endColumn,
			int startRow, int endRow) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getPrintArea(int sheetIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removePrintArea(int sheetIndex) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public MissingCellPolicy getMissingCellPolicy() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setMissingCellPolicy(MissingCellPolicy missingCellPolicy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public DataFormat createDataFormat() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int addPicture(byte[] pictureData, int format) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public List<? extends PictureData> getAllPictures() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CreationHelper getCreationHelper() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isHidden() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setHidden(boolean hiddenFlag) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isSheetHidden(int sheetIx) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isSheetVeryHidden(int sheetIx) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setSheetHidden(int sheetIx, boolean hidden) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setSheetHidden(int sheetIx, int hidden) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addToolPack(UDFFinder toopack) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setForceFormulaRecalculation(boolean value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean getForceFormulaRecalculation() {
		// TODO Auto-generated method stub
		return false;
	}
	
	 
}
