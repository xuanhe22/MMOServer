package com.games.mmo.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.po.RolePo;
import com.games.mmo.service.PetService;
import com.storm.lib.event.EventArg;
import com.storm.lib.event.IEventListener;

@Controller
public class PetConstellStarListener implements IEventListener{

	@Autowired
	PetService petService;
	
	@Override
	public void onEvent(EventArg arg) {
		petService.usePetTalent((RolePo)arg.getSource(), Integer.parseInt(arg.getData().toString()));
	}

}
