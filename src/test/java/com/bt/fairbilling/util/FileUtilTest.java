package com.bt.fairbilling.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.bt.fairbilling.constants.MessageKeys;
import com.bt.fairbilling.exception.FairBillingException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class, Paths.class, Collectors.class})
public class FileUtilTest {
	
	private final static String brokenPath = "BROKEN_PATH";
	private final static String path = "/path/to/file";
	
	private final static String logLineOne = "LOG_LINE_ONE";
	private final static String logLineTwo = "LOG_LINE_TWO";
	private final static String logLineThree = "LOG_LINE_THREE";
	
	@Mock
	private Path mockPath;
	
	@Mock
	private Stream<String> mockStream;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() {
		initMocks(this);
		mockStatic(Files.class, Paths.class, Collectors.class);
	}
	
	@Test
	public void getLinesFromFile_WhenExceptionGettingFileFromPath_ThenThrowFairBillingException() throws FairBillingException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_FILE);
	    
	    when(Paths.get(brokenPath)).thenThrow(new InvalidPathException(brokenPath, "Invalid path"));
	    
	    FileUtil.getLinesFromFile(brokenPath);
	}
	
	@Test
	public void getLinesFromFile_WhenExceptionGettingLinesFromFile_ThenThrowFairBillingException() throws FairBillingException, IOException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_FILE);
	    
	    when(Paths.get(path)).thenReturn(mockPath);
	    when(Files.lines(mockPath)).thenThrow(new IOException());
	    
	    FileUtil.getLinesFromFile(path);
	}
	
	@Test
	public void getLinesFromFile_WhenNoLogLines_ThenReturnEmptyArrayList() throws FairBillingException, IOException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_FILE);
	    
	    when(Paths.get(path)).thenReturn(mockPath);
	    when(Files.lines(mockPath)).thenReturn(mockStream);
	    when(mockStream.collect(Collectors.toList())).thenReturn(new ArrayList<String>());
	    
	    List<String> result = FileUtil.getLinesFromFile(path);
	    
	    assertThat(result, IsEmptyCollection.empty());
	}
	
	@Test
	public void getLinesFromFile_WhenOneLogLine_ThenReturnsArrayListWithOneItem() throws FairBillingException, IOException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_FILE);
	    
	    when(Paths.get(path)).thenReturn(mockPath);
	    when(Files.lines(mockPath)).thenReturn(mockStream);
	    
	    List<String> logLines = new ArrayList<>();
	    logLines.add(logLineOne);
	    
	    when(mockStream.collect(Collectors.toList())).thenReturn(logLines);
	    
	    List<String> result = FileUtil.getLinesFromFile(path);
	    
	    assertThat(result.size(), equalTo(1));
		assertThat(result, contains(logLineOne));
	}
	
	@Test
	public void getLinesFromFile_WhenMultipleLogLines_ThenReturnsArrayListWithAllItems() throws FairBillingException, IOException {
		expectedException.expect(FairBillingException.class);
	    expectedException.expectMessage(MessageKeys.ERROR_PROCESSING_LOG_FILE);
	    
	    when(Paths.get(path)).thenReturn(mockPath);
	    when(Files.lines(mockPath)).thenReturn(mockStream);
	    
	    List<String> logLines = new ArrayList<>();
	    logLines.add(logLineOne);
	    logLines.add(logLineTwo);
	    logLines.add(logLineThree);
	    
	    when(mockStream.collect(Collectors.toList())).thenReturn(logLines);
	    
	    List<String> result = FileUtil.getLinesFromFile(path);
	    
	    assertThat(result.size(), equalTo(3));
		assertThat(result, contains(logLineOne, logLineTwo, logLineThree));
	}
}
