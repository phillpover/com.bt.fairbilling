package com.bt.fairbilling.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;

public class ArgumentsUtilTest {
	
	private String arg1 = "ARG1";
	private String arg2 = "ARG2";
	private String arg3 = "ARG3";
	
	@Before
	public void setUp() {
		initMocks(this);
	}
	
	@Test
	public void getArguments_WhenArgsIsEmpty_ThenReturnsEmptyArrayList() {
		
		List<String> result = ArgumentsUtil.getArguments(new String[0]);
		
		assertThat(result, IsEmptyCollection.empty());
	}
	
	@Test
	public void getArguments_WhenArgsHasOneValue_ThenReturnsArrayListWithArgAsValue() {
		String[] args = {arg1};
		
		List<String> result = ArgumentsUtil.getArguments(args);
		
		assertThat(result, contains(arg1));
		assertThat(result.size(), equalTo(1));
	}
	
	@Test
	public void getArguments_WhenArgsHasMultipleValues_ThenReturnsArrayListWithArgsAsValues() {
		String[] args = {arg1, arg2, arg3};
		
		List<String> result = ArgumentsUtil.getArguments(args);
		
		assertThat(result, contains(arg1, arg2, arg3));
		assertThat(result.size(), equalTo(3));
	}
}
