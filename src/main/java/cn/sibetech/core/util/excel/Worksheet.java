package cn.sibetech.core.util.excel;

import java.util.List;

public class Worksheet {
    private String sheet;

    private int columnNum;

    private int rowNum;

    private List<String> title;

    private List<Workrow> rows;

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public List<Workrow> getRows() {
        return rows;
    }

    public void setRows(List<Workrow> rows) {
        this.rows = rows;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

}
