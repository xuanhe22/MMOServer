package com.games.mmo.thread;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.UserPo;
import com.games.mmo.type.RoleType;
import com.games.mmo.vo.LoginQueueVo;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ExceptionUtil;

public class LoginQueueThread  extends Thread  {
	@Override
	public void run() {
		setName("LoginQueueThread");
		while(true){
			try {	
				if(GlobalCache.fetchLoginQueueList().size() > 0){
					GlobalCache.sortServerQueueNum();
					//排队人数
					int queueNum = GlobalCache.fetchLoginQueueList().size();
//					System.out.println("queueNum="+queueNum);
					// 在线人数
					int onlineNum = RoleTemplate.roleIdIuidMapping.size();
//					System.out.println("onlineNum="+onlineNum);
					// 
					GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum);
					// 最大在线人数
					int onlineMaxNum = Integer.valueOf(globalPo.valueObj.toString());
//					System.out.println("onlineMaxNum="+onlineMaxNum);
					int num = onlineMaxNum - onlineNum;
					int size = Math.min(num, queueNum);
//					System.out.println("size="+size);
					for(int i=0; i<GlobalCache.fetchLoginQueueList().size(); i++){
						LoginQueueVo loginQueueVo = GlobalCache.fetchLoginQueueList().get(i);
						UserPo userPo = UserPo.findEntity(loginQueueVo.userId);
						if(userPo!= null){
							if(i < size){
								userPo.sendUpdateLoginQueueInfo(1, queueNum, loginQueueVo.rankNum);
							}else{
								userPo.sendUpdateLoginQueueInfo(0, queueNum, loginQueueVo.rankNum);
							}
						}
					}
					GlobalCache.checkRemoveQueueUser(2);
				}
			}catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
		}
	}
}
