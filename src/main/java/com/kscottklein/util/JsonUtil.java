package com.kscottklein.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	private static final Gson GSON = new Gson();
	private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

	public static String toPrettyString(Object o) {
		return PRETTY_GSON.toJson(o);
	}

	public static String toString(Object o) {
		return GSON.toJson(o);
	}
}
