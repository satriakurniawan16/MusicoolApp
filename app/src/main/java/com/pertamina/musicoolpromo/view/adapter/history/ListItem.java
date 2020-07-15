package com.pertamina.musicoolpromo.view.adapter.history;

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
	public static final int TYPE_EVENT = 1;
	public static final int TYPE_BOTTOM = 2;

    abstract public int getType();

} 