package com.bt.fairbilling.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class ListUtilTest {
	private String value1 = "VALUE1";
	private String value2 = "VALUE2";
	private String value3 = "VALUE3";
	
	@Test
	public void reverseList_WhenEmptyList_ThenReturnsEmptyList() {
		
		List<String> result = ListUtil.reverseList(new ArrayList<String>());
		
		assertThat(result, IsEmptyCollection.empty());
	}
	
	@Test
	public void reverseList_WhenListHasOneValue_ThenReturnsList() {
		List<String> input = new ArrayList<>();
		input.add(value1);
		
		List<String> result = ListUtil.reverseList(input);

		assertThat(result.size(), equalTo(1));
		assertThat(result, contains(value1));
	}
	
	@Test
	public void reverseList_WhenListHasMultipleValues_ThenReturnsListInReverseOrder() {
		List<String> input = new ArrayList<>();
		input.add(value1);
		input.add(value2);
		input.add(value3);
		
		List<String> result = ListUtil.reverseList(input);

		assertThat(result.size(), equalTo(3));
		assertThat(result, contains(value3, value2, value1));
	}
}
