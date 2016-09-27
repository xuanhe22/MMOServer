package com.games.mmo.vo;

import java.util.Comparator;

public class DistributeComparator implements Comparator<String> {


    public int compare(String o1, String o2) {  
    	return Integer.valueOf(o1.split("~")[0])-Integer.valueOf(o2.split("~")[0]);
    }     

}
