package com.games.mmo;

import javax.script.ScriptEngine;

import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.util.ScriptUtil;

public class TestMulJs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		String exp1="var ma =self.callReadMagicAttack();var mf =parseFloat(target.callReadMagicDefence());var lv =target.callReadLv();var slv =parseInt(self.callSkillLv(130003))-1)/100;var reduce =parseFloat(1-mf/(mf+lv*3+100));target.callHpChange(self,(-1.5-slv)*ma*reduce);target.callAddBuff(self,3);";
		String exp1="var ma =self.callReadMagicAttack();var mf =parseFloat(target.callReadMagicDefence());var lv =target.callReadLv();var slv =parseInt(self.callSkillLv(130003))-1)/100;";
		Fighter f1 = new Fighter();
		Fighter f2 = new Fighter();
		f1.batMagicDefenceMax=1;
		f1.batMagicDefenceMin=1;
		f2.batMagicDefenceMax=1;
		f2.batMagicDefenceMin=1;

		f1.batMagicAttackMin=1;
		f1.batMagicAttackMax=1;
		f2.batMagicAttackMin=1;
		f2.batMagicAttackMax=1;
		
		ScriptUtil.runScript(f1, f2, exp1);
//		System.out.println(a);
	}

}
