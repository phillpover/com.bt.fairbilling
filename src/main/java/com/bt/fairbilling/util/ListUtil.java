package com.bt.fairbilling.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility Class that takes an ArrayList<T> and returns the same
 * List with the items in reverse order, to keep code clean and readable.
 * 
 * @author Phill Pover
 * @since 05/02/2019
 * 
 */

public final class ListUtil {

	public static<T> List<T> reverseList(List<T> list)
	{
		return list.stream()
			.collect(Collectors.collectingAndThen(
				Collectors.toCollection(ArrayList::new), newList -> {
					Collections.reverse(newList);
					return newList.stream();
				}
			)).collect(Collectors.toCollection(ArrayList::new));
	}

}
