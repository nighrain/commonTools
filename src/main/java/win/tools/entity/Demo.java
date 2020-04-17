package win.tools.entity;

import javax.persistence.Table;
import java.util.Date;

/**
 *    
 * Title         [title]
 * Author:       [..]
 * CreateDate:   [2019-06-21--09:26]  @_@ ~~
 * Version:      [v1.0]
 * Description:  [..]
 * <p></p>
 *  
 */

@Table(name = "CUSTOMER_LEVEL_POLICY")
public class Demo extends DemoPar {

    private static final long serialVersionUID = 2441139569173626962L;
    private DemoPar policyNo;					//所属政策编码
    private String superPartnerNo;				//所属超级机构编号
    private Date effectTime;					//生效时间
    private Date expireTime;					//失效时间
    private YesNoa status;						//状态

    private String name;						//等级名称
    private String description;					//等级描述
    private Integer effectDays;					//有效天数

    private Double mposRate;					//刷卡费率
    private Double mposCouponRate;				//优惠券刷卡费率
    private Double qrpayRate;					//扫码支付费率
    private Double quickpayRate;				//快捷支付费率
    private Double quickPassRate;           	//闪付费率
    private Double d0settleFee;					//D0结算手续费（+1、+2）

    private Integer expandCount;				//升级条件推广激活商户数量


}
