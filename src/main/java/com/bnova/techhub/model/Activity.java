package com.bnova.techhub.model;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Activity
{
	private String activity;
	private String type;
	private int participants;
	private double price;
	private String link;
	private String key;
	private double accessibility;
}
