package com.games.mmo.event.listener;

import java.util.Date;

import org.springframework.stereotype.Controller;

import com.games.mmo.po.RolePo;
import com.games.mmo.util.TimeUtil;
import com.storm.lib.event.EventArg;
import com.storm.lib.event.IEventListener;

@Controller
public class LoginListener implements IEventListener {

	@Override
	public void onEvent(EventArg arg) {
		RolePo rolePo = (RolePo)arg.getSource();
		Date loginTime = (Date)arg.getData();

		//登陆时更新称号是否到期
		rolePo.updateTitle(false);
	}

}
