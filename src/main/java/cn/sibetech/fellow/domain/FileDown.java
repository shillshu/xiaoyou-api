package cn.sibetech.fellow.domain;

import cn.sibetech.core.domain.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * Created by yuanye on 2017/5/2.
 */
@Data
@TableName(value = "p_file_down")
public class FileDown extends BaseModel {
	@TableField
   private String name;
   
   @TableField
   private String cjrId;
   
   @TableField
   private String cjrName;

   @TableField
   private String path ;

	@TableField
	private String lb ;

	@TableField
	private String note ;

	@TableField
	private String status ;

	@TableField
	private String kfzt ;

}
