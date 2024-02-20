package cn.sibetech.core.controller;

import cn.sibetech.core.domain.PermModel;
import cn.sibetech.core.domain.Role;
import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.IdDto;
import cn.sibetech.core.domain.dto.IdsDto;
import cn.sibetech.core.domain.dto.RolePermDto;
import cn.sibetech.core.domain.dto.RoleQueryDto;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.service.RoleService;
import cn.sibetech.core.util.PermUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static cn.sibetech.core.util.SecurityConstants.*;

@Api(tags = "角色管理（管理员）")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;
    @ApiOperation(value = "角色管理")
    @PostMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true),
    })
    public ResponseBean<IPage<Role>> page(@RequestBody @Valid RoleQueryDto dto) {
        try {
            return ResponseBean.ok(roleService.queryPage(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "角色查询")
    @GetMapping("/select_role")
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    })
    public ResponseBean<List<SelectModel>> selectRole(){
        return ResponseBean.ok(roleService.selectRoleList());
    }


    @ApiOperation(value = "新增角色")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/create")
    public ResponseBean<String> saveRole(@RequestBody @Valid Role dto) {
        try {
            roleService.add(dto);
            return ResponseBean.okMsg("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改角色")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/update")
    public ResponseBean<String> updateRole(@RequestBody @Valid Role dto) {
        try {
            roleService.edit(dto);
            return ResponseBean.okMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/delete")
    public ResponseBean<String> removeRole(@RequestBody @Valid IdsDto dto) {
        try {
            if (null == dto || null == dto.getIds() || dto.getIds().size() == 0) {
                return ResponseBean.error("参数错误,id不能为空");
            }
            roleService.remove(dto.getIds());
            return ResponseBean.okMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取角色基本信息")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/detail")
    public ResponseBean<Role> dataInfo(@RequestBody @Valid IdDto dto) {
        try {
            Role vo = roleService.getById(dto.getId());
            return ResponseBean.ok(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

   /* @ApiOperation(value = "获取权限")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/role_perms")
    public ResponseBean<RolePermDto> getRolePerms(@RequestBody @Valid IdDto dto) {
        try {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseBean.error("参数错误,id不能为空");
            }
            RolePermDto data = roleService.selectPermModelByRoleId(dto.getId());
            return ResponseBean.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }*/

    @ApiOperation(value = "获取权限")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @GetMapping("/role_perms/{roleId}")
    public ResponseBean<RolePermDto> queryRolePerms(@PathVariable String  roleId) {
        try {
            RolePermDto dto = new RolePermDto();
            dto.setRoleId(roleId);
            List<PermModel> perms = PermUtil.getAllPermsForRole();
            dto.setPerms(perms);
            List<String> rperms = roleService.selectPerms(roleId);
            dto.setPermString(rperms);
            return ResponseBean.ok(dto);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }

    @ApiOperation(value = "分配资源-保存角色资源授权")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/role_perms_update")
    public ResponseBean<String> grantRolePerm(@RequestBody @Valid RolePermDto dto) {
        if (null == dto || StringUtils.isEmpty(dto.getRoleId())) {
            return ResponseBean.error("参数错误！");
        }
        try {
            roleService.grantPerms(dto.getRoleId(), dto.getPermString());
            return ResponseBean.okMsg("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error(e.getMessage());
        }
    }
}
