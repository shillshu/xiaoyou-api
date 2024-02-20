package cn.sibetech.core.util;

public class FlowConstant {
    /**
     * 流程状态：开始
     */
    public static final String PROCESS_STATUS_START = "start";
    /**
     * 流程状态：活动
     */
    public static final String PROCESS_STATUS_ACTIVE = "active";
    /**
     * 流程状态：结束
     */
    public static final String PROCESS_STATUS_END = "end";

    /**
     * 审批环节状态：未提交
     */
    public static final String PROCESS_STEP_CREATE = "0";
    /**
     * 审批环节状态：已提交
     */
    public static final String PROCESS_STEP_APPROVE = "1";
    public static final String PROCESS_STEP_STATUS_PASS = "1";
    public static final String PROCESS_STUDENT = "student";

    public static final String PROCESS_BACK = "back";
    public static final String PROCESS_XY = "xy";
    public static final String PROCESS_XGC = "xgc";

    /**
     * 会签节点
     */
    public static final String PROCESS_MULTI_INSTANCE = "multiInstance";

    /**
     * 发起人
     */
    public static final String START_USER = "startUserId";

    public static final String COMMENT_STATUS = "taskStatus";

    public static final String COMMENT_MESSAGE = "comment";

    /**
     * 加分项申请
     */
    public static final String FLOW_YSSJ_MINGXI = "yssj-mingxi";
}
