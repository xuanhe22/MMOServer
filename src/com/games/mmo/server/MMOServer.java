package com.games.mmo.server;

import java.io.IOException;

import com.games.mmo.po.AbroadPo;
import com.games.mmo.po.AuctionItemPo;
import com.games.mmo.po.BlockPo;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.ForbidPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.po.MailPo;
import com.games.mmo.po.RankArenaPo;
import com.games.mmo.po.RoleInforPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.BattleRangePo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.CastPosePo;
import com.games.mmo.po.game.ConsumPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.DicClientPo;
import com.games.mmo.po.game.DicServerPo;
import com.games.mmo.po.game.DropPo;
import com.games.mmo.po.game.FashionPo;
import com.games.mmo.po.game.GatewayPo;
import com.games.mmo.po.game.GrowthPo;
import com.games.mmo.po.game.InvitationPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.LinesPo;
import com.games.mmo.po.game.LvConfigPo;
import com.games.mmo.po.game.MergePo;
import com.games.mmo.po.game.MilitaryRankPo;
import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.NewbiePo;
import com.games.mmo.po.game.NoticePo;
import com.games.mmo.po.game.OpenfunctionPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.PetRollPo;
import com.games.mmo.po.game.PetSkillPo;
import com.games.mmo.po.game.PetTalentPo;
import com.games.mmo.po.game.PetUpgradePo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.RedremindPo;
import com.games.mmo.po.game.RobotPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.SkillDescriptionPo;
import com.games.mmo.po.game.SkillPo;
import com.games.mmo.po.game.SoulElementPo;
import com.games.mmo.po.game.SoulStepPo;
import com.games.mmo.po.game.StaticLiveActivityPo;
import com.games.mmo.po.game.StaticProductPo;
import com.games.mmo.po.game.StoryActionPo;
import com.games.mmo.po.game.StoryTriggerPo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.po.game.TitlePo;
import com.games.mmo.po.game.UpgradeSkillPo;
import com.games.mmo.po.game.VipPo;
import com.games.mmo.po.game.VisualPo;
import com.games.mmo.type.SystemType;
import com.storm.lib.component.socket.netty.BaseNettySocketServer;
import com.storm.lib.init.BaseInitProcessor;
import com.storm.lib.runner.CloseServerThread;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;

public class MMOServer extends BaseNettySocketServer{
	public static void main(String[] args) {
		try {
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			System.setProperty("org.terracotta.quartz.skipUpdateCheck","true");
			Runtime.getRuntime().addShutdownHook(CloseServerThread.closeServerThread);
			PrintUtil.print("MMO初始化");
			int[] timerTypes=new int[]{};
			Class[] timerClazzs = new Class[]{}; 
			SystemType.initStormLib(args, "com"+BaseStormSystemType.fs+"games"+BaseStormSystemType.fs+"mmo"+BaseStormSystemType.fs, true, false, null, true, SystemType.class);
			BaseInitProcessor initProcessor = (BaseInitProcessor) BeanUtil.getBean("initProcessor");
			initProcessor.init(
					timerTypes,
					timerClazzs,
					new Class[]{RolePo.class,EqpPo.class,MailPo.class,AuctionItemPo.class,RankArenaPo.class,RoleInforPo.class,GuildPo.class,RpetPo.class,UserPo.class,LiveActivityPo.class,CopySceneActivityPo.class,FlagPo.class,GlobalPo.class,com.games.mmo.po.ProductPo.class,BlockPo.class,ForbidPo.class,AbroadPo.class},
					new Class[]{},
					new Class[]{DicClientPo.class,DicServerPo.class,StaticLiveActivityPo.class,GrowthPo.class,ItemPo.class,MonsterPo.class,MonsterFreshPo.class,ScenePo.class,BattleRangePo.class,SkillPo.class,BuffPo.class,VisualPo.class,LvConfigPo.class,DropPo.class,CopyScenePo.class,CopySceneConfPo.class,CastPosePo.class,UpgradeSkillPo.class,GatewayPo.class,TaskPo.class,SkillDescriptionPo.class,LinesPo.class,PetPo.class,PetUpgradePo.class,PetRollPo.class,StaticProductPo.class,ConsumPo.class,MergePo.class,StoryActionPo.class,StoryTriggerPo.class,PetTalentPo.class,PetSkillPo.class,NewbiePo.class,TitlePo.class,FashionPo.class,RechargePo.class,VipPo.class,OpenfunctionPo.class,RedremindPo.class,RobotPo.class,NoticePo.class,MilitaryRankPo.class,InvitationPo.class,SoulElementPo.class,SoulStepPo.class});
		} catch (Exception e1) {
			ExceptionUtil.processException(e1);
		}
		if (Boolean.parseBoolean(System.getProperty("RUNNING_IN_ECLIPSE")) == true) {
	        try {
	                System.in.read();
	        } catch (IOException e) {
				ExceptionUtil.processException(e);
	        }
	        System.exit(0);
		}
	}
}
