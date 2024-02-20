package cn.sibetech.core.util;

import cn.sibetech.core.config.ResourceUtils;
import cn.sibetech.core.domain.PermModel;
import cn.sibetech.core.domain.RolePerm;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PermUtil {

    public static List<PermModel> getPerm() {
        JSONArray jsonArray = JSONArray.parseArray(ResourceUtil.getFile("perm.json"));
        List<PermModel> permModelList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(jsonArray)) {
            for(int i=0; i<jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PermModel permModel = new PermModel();
                permModel.setName(jsonObject.getString("name"));
                permModel.setValue(jsonObject.getString("value"));
                JSONArray subPerms = jsonObject.getJSONArray("subPerms");
                if(subPerms!=null && subPerms.size()>0){
                    permModel.setSubPerms(getSubPerm(subPerms));
                }
                permModelList.add(permModel);
            }
        }
        return permModelList;
    }

    public static List<PermModel> getAllPermsForRole() {
        List<PermModel> list = new ArrayList<PermModel>();
        List<JSONObject> perms = ResourceUtils.getPermJson();
        if(CollectionUtils.isNotEmpty(perms)) {
            for(JSONObject jsonObject: perms) {
                PermModel model = new PermModel();
                model.setName(jsonObject.getString("name"));
                model.setValue(jsonObject.getString("value"));
                JSONArray subPerms = jsonObject.getJSONArray("subPerms");
                if(subPerms!=null && subPerms.size()>0){
                    model.setSubPerms(getSubPerm(subPerms));
                }
                if(model.getSubPerms()!=null&&model.getSubPerms().size()>0){
                    list.add(model);
                }
            }
        }
        return list;
    }

    private static List<PermModel> getSubPerm(JSONArray subPerms){
        List<PermModel> list = new ArrayList<PermModel>();
        for(int i=0; i<subPerms.size(); i++){
            JSONObject jsonObject = subPerms.getJSONObject(i);
            PermModel model = new PermModel();
            model.setName(jsonObject.getString("name"));
            model.setValue(jsonObject.getString("value"));
            list.add(model);
        }
        return list;
    }

    private static void isCheck(PermModel permModel, List<RolePerm> perms) {
        if(CollectionUtils.isNotEmpty(perms)) {
            long count = perms.stream().filter(m->m.getPermString().equals(permModel.getValue())).count();
            permModel.setChecked(count>0);
        }
        else {
            permModel.setChecked(false);
        }
    }
}
