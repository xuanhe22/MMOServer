package com.games.mmo.remoting;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.backend.vo.BlockVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.BlockPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.RoleService;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.util.StringUtil;
@Controller
public class BlockRemoting extends BaseRemoting{
	@Autowired
	private RoleService roleService;
	
	public List<BlockVo> list(){
		return BlockVo.createFromPos();
	}

	
	public String addEdit(Integer id,String name,Long startTime,Long endTime,String note){
		int newId = id;
		RolePo rolePo = roleService.findRoleByName(name);
    	if(id==0){
    		if(rolePo!=null){
        		BlockPo blockPo = new BlockPo();
        		blockPo.setRoleId(rolePo.getId());
        		blockPo.setRoleName(rolePo.getName());
        		blockPo.setStartTime(startTime);
        		blockPo.setEndTime(endTime);
        		blockPo.setNote(note);
        		newId = BaseDAO.instance().insert(blockPo).getId();
    		}
    	}
    	else{
    		BlockPo blockPo = BlockPo.findEntity(id);
    		if(blockPo != null){
				blockPo.setEndTime(endTime);
				blockPo.setStartTime(startTime);
        		blockPo.setNote(note);
        		BaseDAO.instance().syncToDB(blockPo);
    		}
    	}
    	GlobalCache.syncBlockChat();
		return newId + StringUtil.EMPTY;
	}
    
	
	public BlockVo show(Integer id){
		return BlockVo.fromPo(BlockPo.findEntity(id));
	}
    
	
	public String delete(Integer id) throws Exception{
		BaseDAO.instance().remove(BlockPo.findEntity(id));
    	GlobalCache.syncBlockChat();

		return "";
	}    
}
