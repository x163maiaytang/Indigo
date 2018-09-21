package com.fire.excel.txt;

import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellView;
import jxl.Hyperlink;
import jxl.Image;
import jxl.LabelCell;
import jxl.Range;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.format.CellFormat;

public class TxtSheet implements Sheet{

	
	private List<List<TxtCell>> datas;

	public TxtSheet(String content) {
		
		this.datas = new ArrayList<List<TxtCell>>();
		
		List<TxtCell> txtCelles;
		TxtCell cell;
		for(String rowData : content.split("\n")){
			txtCelles = new ArrayList<TxtCell>();
			datas.add(txtCelles);
			for(String cellData : rowData.split("\t")){
				cell = new TxtCell(cellData);
				txtCelles.add(cell);
			}
		}
	}

	@Override
	public Cell findCell(String arg0) {
		return null;
	}

	@Override
	public LabelCell findLabelCell(String arg0) {
		return null;
	}

	@Override
	public Cell getCell(int arg0, int arg1) {
		return datas.get(arg1).get(arg0);
	}

	@Override
	public Cell[] getColumn(int arg0) {
		return null;
	}

	@Override
	public CellFormat getColumnFormat(int arg0) {
		return null;
	}

	@Override
	public CellView getColumnView(int arg0) {
		return null;
	}

	@Override
	public int getColumnWidth(int arg0) {
		return 0;
	}

	@Override
	public int getColumns() {
		return datas.get(0).size();
	}

	@Override
	public Image getDrawing(int arg0) {
		return null;
	}

	@Override
	public Hyperlink[] getHyperlinks() {
		return null;
	}

	@Override
	public Range[] getMergedCells() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getNumberOfImages() {
		return 0;
	}

	@Override
	public Cell[] getRow(int arg0) {
		return null;
	}

	@Override
	public int getRowHeight(int arg0) {
		return 0;
	}

	@Override
	public CellView getRowView(int arg0) {
		return null;
	}

	@Override
	public int getRows() {
		return 0;
	}

	@Override
	public SheetSettings getSettings() {
		return null;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public boolean isProtected() {
		return false;
	}

	public void release() {
		if(datas != null){
			
			datas.clear();
			datas = null;
		}
	}

}
