package com.games.mmo.thread;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.ChannelVo;
import com.games.mmo.vo.RetrieveSystemVo;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * 每5分钟写入日志
 * @author Administrator
 *
 */
public class Every5minutesLogWrite  extends Thread {
	@Override
	public void run() {
		setName("Every5minutesLogWrite");
		while(true){
			try {
				if(RoleTemplate.roleIdIuidMapping!= null){
					 ConcurrentHashMap<String, ChannelVo> map = new ConcurrentHashMap<String, ChannelVo>();
					for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
						RolePo rolePo = RolePo.findEntity(roleId);
						if(rolePo == null || StringUtil.isEmpty(rolePo.getChannelKey())){
							continue;
						}
						if(map.containsKey(rolePo.getChannelKey())){
							ChannelVo channelVo = map.get(rolePo.getChannelKey());
							channelVo.channelValue++;
						}else{
							ChannelVo channelVo = new ChannelVo(rolePo.getChannelKey(), 1);
							map.put(rolePo.getChannelKey(), channelVo);
						}
					}
					
					for(String str : map.keySet()){
						ChannelVo channelVo = map.get(str);
						LogUtil.writeLog(null, 309, channelVo.channelValue, 0, 0,GlobalCache.fetchLanguageMap("key2498"),channelVo.channelKey);
					}					
				}
			} catch (Exception e1) {
				ExceptionUtil.processException(e1);
			}
			try {
				Thread.sleep(900000);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
		}
	}
	

}
