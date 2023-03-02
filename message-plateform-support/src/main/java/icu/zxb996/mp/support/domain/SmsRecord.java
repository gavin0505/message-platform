package icu.zxb996.mp.support.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 短信（回执和发送记录）
 *
 * @author Gavin Zhang
 * @date 2023/3/2 19:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sms_record")
/**
 * 短信记录（回执和发送记录）
 * @author 3y
 */
public class SmsRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 渠道商Id
     */
    private Integer supplierId;

    /**
     * 渠道商名字
     */
    private String supplierName;

    /**
     * 短信发送的内容
     */
    private String msgContent;

    /**
     * 批次号Id
     */
    private String seriesId;

    /**
     * 计费条数
     */
    private Integer chargingNum;

    /**
     * 回执信息
     */
    private String reportContent;

    /**
     * 短信状态
     */
    private Integer status;

    /**
     * 发送日期
     */
    private Date sendDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer ifDeleted;
}