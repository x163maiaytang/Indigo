package com.fire.excel.txt;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.CellType;
import jxl.format.CellFormat;

public class TxtCell implements Cell{

	private String data;
	public TxtCell(String cellData) {
		this.data = cellData;
	}

	@Override
	public CellFeatures getCellFeatures() {
		return null;
	}

	@Override
	public CellFormat getCellFormat() {
		return null;
	}

	@Override
	public int getColumn() {
		return 0;
	}

	@Override
	public String getContents() {
		return data.trim();
	}

	@Override
	public int getRow() {
		return 0;
	}

	@Override
	public CellType getType() {
		return null;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

}
