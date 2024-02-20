package cn.sibetech.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liwj
 * @date 2022/1/25 17:24
 */
@ApiModel(value = "分页")
public class Pager<T> implements Serializable {
    private static final long serialVersionUID = 8848523495013555357L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    @ApiModelProperty("每页显示记录数")
    private int pageSize;
    @ApiModelProperty("当前页")
    private int page = 1;
    @ApiModelProperty("总页数")
    private int total;
    @ApiModelProperty("总记录数")
    private long records;
    @ApiModelProperty("数据")
    private List<T> rows = new ArrayList<>();
    public Pager() {
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pager(int page, int pageSize) {
        this.pageSize = pageSize;
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    public int getTotal() {
        if (this.total < 0) {
            this.total = (int) Math.ceil((double) this.records / (double) this.pageSize);
        }
        return total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
        this.records = records > 0 ? records : 0;
        this.total = (int) Math.ceil((double) records / (double) this.pageSize);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
