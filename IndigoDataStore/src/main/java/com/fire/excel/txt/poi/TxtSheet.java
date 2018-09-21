package com.fire.excel.txt.poi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.ss.usermodel.AutoFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;


public class TxtSheet implements Sheet{

	
	private List<TxtRow> datas;
	
	
	private List<Byte> columnType = new ArrayList<Byte>();


	private String className;

	public TxtSheet(String content, String className) {
		this.className = className;
		this.datas = new ArrayList<TxtRow>();
		
		int index = 0;
		TxtRow row;
		
		String[] split = content.split("\n");
		for(String rowData : split){
			
			datas.add(row = new TxtRow(rowData, this, index, index + 1 == split.length));
			
			if(index == 1){
				parseType(row);
			}
			index ++;
		}
	}
	
	private void parseType(TxtRow row){
		for(int index = 0; index < row.size(); index ++){
			TxtCell txtCell = row.get(index);
			
			columnType.add(getCellType(txtCell.toString()));
		}
	}
	
	private byte getCellType(String data) {
		if (data.equalsIgnoreCase("int") || data.equalsIgnoreCase("short")
				|| data.equalsIgnoreCase("float")
				|| data.equalsIgnoreCase("double")
				|| data.equalsIgnoreCase("byte")
				|| data.equalsIgnoreCase("long")) {
			return 0;
		} else if (data.equalsIgnoreCase("string")) {
			return 1;
		} else if(data.equalsIgnoreCase("boolean") || data.equalsIgnoreCase("bool")){
			return 4;
		}
		else
		{
			System.out.println("===================================錯誤的類型"
					+ data
					+ "====================================================" + className);
			System.exit(-1);
			return -1;
		}
	}

	@Override
	public Iterator<Row> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Row createRow(int rownum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRow(Row row) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Row getRow(int rownum) {
		
		if(rownum >= datas.size()){
			return null;
		}
		return datas.get(rownum);
	}

	@Override
	public int getPhysicalNumberOfRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFirstRowNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLastRowNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setColumnHidden(int columnIndex, boolean hidden) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isColumnHidden(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRightToLeft(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRightToLeft() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnWidth(int columnIndex, int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getColumnWidth(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultColumnWidth(int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDefaultColumnWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getDefaultRowHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDefaultRowHeightInPoints() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultRowHeight(short height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultRowHeightInPoints(float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CellStyle getColumnStyle(int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addMergedRegion(CellRangeAddress region) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVerticallyCenter(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHorizontallyCenter(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getHorizontallyCenter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getVerticallyCenter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeMergedRegion(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumMergedRegions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CellRangeAddress getMergedRegion(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Row> rowIterator() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void setAutobreaks(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisplayGuts(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisplayZeros(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisplayZeros() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFitToPage(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRowSumsBelow(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRowSumsRight(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getAutobreaks() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getDisplayGuts() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getFitToPage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getRowSumsBelow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getRowSumsRight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrintGridlines() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPrintGridlines(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PrintSetup getPrintSetup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Header getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Footer getFooter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelected(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getMargin(short margin) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMargin(short margin, double size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getProtect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void protectSheet(String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getScenarioProtect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setZoom(int numerator, int denominator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getTopRow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getLeftCol() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showInPane(short toprow, short leftcol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shiftRows(int startRow, int endRow, int n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shiftRows(int startRow, int endRow, int n,
			boolean copyRowHeight, boolean resetOriginalRowHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createFreezePane(int colSplit, int rowSplit,
			int leftmostColumn, int topRow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createFreezePane(int colSplit, int rowSplit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSplitPane(int xSplitPos, int ySplitPos,
			int leftmostColumn, int topRow, int activePane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PaneInformation getPaneInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayGridlines(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisplayGridlines() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDisplayFormulas(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisplayFormulas() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDisplayRowColHeadings(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisplayRowColHeadings() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRowBreak(int row) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRowBroken(int row) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeRowBreak(int row) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getRowBreaks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getColumnBreaks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setColumnBreak(int column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isColumnBroken(int column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeColumnBreak(int column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupColumn(int fromColumn, int toColumn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ungroupColumn(int fromColumn, int toColumn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupRow(int fromRow, int toRow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ungroupRow(int fromRow, int toRow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRowGroupCollapsed(int row, boolean collapse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultColumnStyle(int column, CellStyle style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void autoSizeColumn(int column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void autoSizeColumn(int column, boolean useMergedCells) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Comment getCellComment(int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drawing createDrawingPatriarch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Workbook getWorkbook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSheetName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CellRange<? extends Cell> setArrayFormula(String formula,
			CellRangeAddress range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellRange<? extends Cell> removeArrayFormula(Cell cell) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataValidationHelper getDataValidationHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addValidationData(DataValidation dataValidation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AutoFilter setAutoFilter(CellRangeAddress range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SheetConditionalFormatting getSheetConditionalFormatting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellRangeAddress getRepeatingRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellRangeAddress getRepeatingColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRepeatingRows(CellRangeAddress rowRangeRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRepeatingColumns(CellRangeAddress columnRangeRef) {
		// TODO Auto-generated method stub
		
	}
	 
	public byte getType(int columnIndex){
		return columnType.get(columnIndex);
	}

}
