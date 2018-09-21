package com.fire.excel.txt.poi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class TxtRow implements Row{

	private List<TxtCell> rowData;
	private TxtSheet txtSheet;
	private int index;
	private boolean end;
	public TxtRow(String rowData, TxtSheet txtSheet, int index, boolean end) {
		this.end = end;
		this.index = index;
		this.txtSheet = txtSheet;
		this.rowData = new ArrayList<TxtCell>();
		
		int columnIndex = 0;
		
		for(String data : rowData.split("\t")){
			this.rowData.add(new TxtCell(data, index < 4 ? 0 : txtSheet.getType(columnIndex)));
			
			columnIndex ++;
		}
	}

	@Override
	public Iterator<Cell> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cell createCell(int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cell createCell(int column, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeCell(Cell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRowNum(int rowNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRowNum() {
		return index;
	}

	@Override
	public Cell getCell(int cellnum) {
		if(cellnum >= rowData.size()){
			return null;
		}
		return rowData.get(cellnum);
	}

	@Override
	public Cell getCell(int cellnum, MissingCellPolicy policy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getFirstCellNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getLastCellNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPhysicalNumberOfCells() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHeight(short height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setZeroHeight(boolean zHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getZeroHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHeightInPoints(float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeightInPoints() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFormatted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CellStyle getRowStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRowStyle(CellStyle style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Cell> cellIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sheet getSheet() {
		return txtSheet;
	}
	
	public int size(){
		return rowData.size();
	}
	
	public TxtCell get(int index){
		if(index >= rowData.size())
			return null;
		return rowData.get(index);
	}

	public boolean isEnd() {
		return end;
	}

}
