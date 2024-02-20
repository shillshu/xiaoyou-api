package cn.sibetech.core.util;

import cn.sibetech.core.domain.Role;

public class RoleUtil {
    /**
     * 系统管理员
     */
    public static final String SYS_ADMIN = "SYS_ADMIN";
    /**
     * 学校管理员
     */

    public static final String SYS_SCHOOL = "SYS_SCHOOL";
    /**
     * 院系管理员
     */
    public static final String SYS_DEPT = "SYS_DEPT";
    /**
     * 院系管理员
     */
    public static final String SYS_FDY = "SYS_FDY";

    public static final String SYS_NJ = "SYS_NJ";

    public static final String SYS_CLASS = "SYS_CLASS";

    /**
     * 是否是学工处管理员
     * @param role
     * @return
     */
    public static boolean isSysAdmin(Role role) {
        return role != null && (SYS_ADMIN.equals(role.getScope()) || SYS_SCHOOL.equals(role.getScope()));
    }

    /**
     * 是否是学院管理员
     * @param role
     * @return
     */
    public static boolean isDeptAdmin(Role role) {
        return role != null && SYS_DEPT.equals(role.getScope());
    }
    /**
     * 学院秘书
     * @param role
     * @return
     */
    public static boolean isFdy(Role role) {
        return role != null &&  ( SYS_FDY.equals(role.getScope())  || SYS_NJ.equals(role.getScope()) || SYS_CLASS.equals(role.getScope()));
    }
}
