package com.games.mmo.event.listener;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.AwardRetrieveVo;
import com.games.mmo.vo.xml.ConstantFile.Trade.Cart;
import com.storm.lib.event.EventArg;
import com.storm.lib.event.IEventListener;

@Controller
public class TaskSuccessListener implements IEventListener {

	@Override
	public void onEvent(EventArg arg) {
		RolePo rolePo = (RolePo)arg.getSource();
		TaskPo taskPo = (TaskPo)arg.getData();

		
		// 运镖任务
		if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730){
			if(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality!=-1){
				List<Cart> cartList = XmlCache.xmlFiles.constantFile.trade.cart;
				Cart cart = cartList.get(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality);
				int param = rolePo.getLv() -15;
				if(param < 0){
					param =1;
				}
				int baseExp = 12000*param;
				int basePrestige = 2000;
				int totalExp = baseExp*cart.expPar/100;
				int totalPrestige = basePrestige*cart.prestigePar/100;
				rolePo.adjustExp(totalExp);
				LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_EXP, 0, totalExp, GlobalCache.fetchLanguageMap("key2339"), "");
				rolePo.adjustPrestige(totalPrestige);
			}
			rolePo.initYunDartInfo(taskPo.getId());
			rolePo.sendUpdateYunDartTaskInfo();
		}
		
	}

}
