package cn.ruanyun.backInterface.modules.base.utils;



import cn.hutool.core.bean.BeanUtil;
import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.vo.MenuVo;

/**
 * @author fei
 */
public class VoUtil {

    public static MenuVo permissionToMenuVo(Permission p){

        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}
