package com.games.mmo.remoting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.backend.vo.ForbidVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.ForbidPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.RoleService;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.remoting.BasePushTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.StringUtil;
@Controller
public class ForbidRemoting extends BaseRemoting{
	@Autowired
	private RoleService roleService;
	@Autowired
	private CheckService checkService;
	
	public List<ForbidVo> list(){
		return ForbidVo.createFromPos();
	}

	
	public String addEdit(Integer id,String name,Long startTime,Long endTime,String note){
    	if(id==0){
    		RolePo rolePo = roleService.findRoleByName(name);
    		if(rolePo!=null){
        		ForbidPo forbidPo = new ForbidPo();
        		forbidPo.setRoleId(rolePo.getId());
        		forbidPo.setRoleName(rolePo.getName());
        		forbidPo.setStartTime(startTime);
        		forbidPo.setEndTime(endTime);
        		forbidPo.setNote(note);
        		BaseDAO.instance().insert(forbidPo);
        		if(System.currentTimeMillis()>forbidPo.getStartTime() && System.currentTimeMillis()<=forbidPo.getEndTime()){
              		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
               		rolePo.sendUpdateShowMsg("你已经被禁掉了");
    				BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
    				basePushTemplate.singleSession("PushRemoting.sendIAmKicked", rolePo.fetchSession(), new Object[]{1}, null,true);
    				roleService.logoff(rolePo.fetchSession(), 0);
        		}        		
    		}
    	}
    	else{
    		ForbidPo forbidPo = ForbidPo.findEntity(id);
			if(forbidPo.getId()==id.intValue()){
				forbidPo.setEndTime(endTime);
				forbidPo.setStartTime(startTime);
				forbidPo.setNote(note);
				RolePo rolePo = RolePo.findEntity(forbidPo.getRoleId());
        		BaseDAO.instance().syncToDB(forbidPo);
        		if(System.currentTimeMillis()>forbidPo.getStartTime() && System.currentTimeMillis()<=forbidPo.getEndTime()){
              		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
               		rolePo.sendUpdateShowMsg("你已经被禁掉了");
    				BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
    				basePushTemplate.singleSession("PushRemoting.sendIAmKicked", rolePo.fetchSession(), new Object[]{1}, null,true);
    				roleService.logoff(rolePo.fetchSession(), 0);
        		}        		
			}
			
    	}

    	GlobalCache.syncForbids();
		return "";
	}
    
	public String addEditWithType(Integer id, Integer type, String name, Long startTime,Long endTime, String note){
		ForbidPo forbidPo = null;
		int newId = id;
		if(id==0){
			if(type==1){//按角色名封人
				RolePo rolePo = roleService.findRoleByName(name);
	    		if(rolePo!=null){
	        		forbidPo = new ForbidPo();
	        		forbidPo.setType(type);
	        		forbidPo.setRoleId(rolePo.getId());
	        		forbidPo.setRoleName(rolePo.getName());
	        		forbidPo.setStartTime(startTime);
	        		forbidPo.setEndTime(endTime);
	        		forbidPo.setNote(note);
	        		newId = BaseDAO.instance().insert(forbidPo).getId();
	        		
	        		if(System.currentTimeMillis()>forbidPo.getStartTime() && System.currentTimeMillis()<=forbidPo.getEndTime()){
	              		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
	               		rolePo.sendUpdateShowMsg("你已经被禁掉了");
	    				BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
	    				basePushTemplate.singleSession("PushRemoting.sendIAmKicked", rolePo.fetchSession(), new Object[]{1}, null,true);
	    				roleService.logoff(rolePo.fetchSession(), 0);
	        		}   
	    		}
			}else{
				forbidPo = new ForbidPo();
				forbidPo.setType(type);
        		forbidPo.setRoleName(name);
        		forbidPo.setStartTime(startTime);
        		forbidPo.setEndTime(endTime);
        		forbidPo.setNote(note);
        		BaseDAO.instance().insert(forbidPo);
        		
        		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
        		List<RolePo> rolePos = null;
        		if(type==2){//按IP封人
        			rolePos = roleService.findRoleByIp(name);
        		}else if(type==3){//按user_iuid封人
        			rolePos = roleService.findRoleByUserIuid(name);
        		}else{//按设备ID封人
        			rolePos = roleService.findRoleByDeviceId(name);
        		}
        		for(RolePo rolePo :rolePos){
        			if(rolePo.fetchRoleOnlineStatus()){
        				rolePo.sendUpdateShowMsg("你已经被禁掉了");
        				BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
        				basePushTemplate.singleSession("PushRemoting.sendIAmKicked", rolePo.fetchSession(), new Object[]{1}, null,true);
        				roleService.logoff(rolePo.fetchSession(), 0);
        			}
        		}
			}
    	}
    	else{
    		forbidPo = ForbidPo.findEntity(id);
    		forbidPo.setType(type);
    		forbidPo.setRoleName(name);
			forbidPo.setEndTime(endTime);
			forbidPo.setStartTime(startTime);
			forbidPo.setNote(note);
    		BaseDAO.instance().syncToDB(forbidPo);
			if(forbidPo != null){
        		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
        		if(type==1){
        			RolePo rolePo = RolePo.findEntity(forbidPo.getRoleId());
        			forbidPo.setRoleId(rolePo.getId());
        			if(System.currentTimeMillis()>forbidPo.getStartTime() && System.currentTimeMillis()<=forbidPo.getEndTime()){
                   		rolePo.sendUpdateShowMsg("你已经被禁掉了");
        				BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
        				basePushTemplate.singleSession("PushRemoting.sendIAmKicked", rolePo.fetchSession(), new Object[]{1}, null,true);
        				roleService.logoff(rolePo.fetchSession(), 0);
            		}
        		}else{
        			List<RolePo> rolePos = null;
        			if(type==2){//按IP封人
            			rolePos = roleService.findRoleByIp(name);
            		}else if(type==3){//按user_iuid封人
            			rolePos = roleService.findRoleByUserIuid(name);
            		}else{//按设备ID封人
            			rolePos = roleService.findRoleByDeviceId(name);
            		}
            		for(RolePo rolePo :rolePos){
            			if(rolePo.fetchRoleOnlineStatus()){
            				rolePo.sendUpdateShowMsg("你已经被禁掉了");
            				BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
            				basePushTemplate.singleSession("PushRemoting.sendIAmKicked", rolePo.fetchSession(), new Object[]{1}, null,true);
            				roleService.logoff(rolePo.fetchSession(), 0);
            			}
            		}
        		}
			}
    	}

    	GlobalCache.syncForbids();
		return newId + StringUtil.EMPTY;
	}
	
	public ForbidVo show(Integer id){
		checkService.checkExisForbidPo(id);
		return ForbidVo.fromPo(ForbidPo.findEntity(id));
	}
    
	
	public String delete(Integer id) throws Exception{
		checkService.checkExisForbidPo(id);
		BaseDAO.instance().remove(ForbidPo.findEntity(id));
    	GlobalCache.syncForbids();
		return "";
	}   
}
