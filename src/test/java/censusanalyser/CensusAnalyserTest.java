package censusanalyser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.gson.Gson;

public class CensusAnalyserTest {
	private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_TYPE_PATH = "./src/test/resources/IndiaStateCensusData.txt";
    private static final String WRONG_FILE_PATH = "./src/test/resources/IndianCensus.csv";
    private static final String WRONG_FILE_HEADER_PATH = "./src/test/resources/IndianNewCensus.csv";
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_STATE_CSV_FILE_PATH = "./src/main/resources/IndiaStateCode.csv";
    private static final String WRONG_STATE_CSV_FILE_TYPE = "./src/test/resources/IndiaStateCode.txt";
    private static final String WRONG_STATE_CSV_FILE_DELIMITER = "./src/test/resources/IndianStateCodes.csv";
    private static final String WRONG_STATE_CSV_FILE_HEADER="./src/test/resources/NewIndianStateCodes.csv" ;
   
    @Test
    public void givenIndianCensus_CSVFileReturns_CorrectRecords() throws CensusAnalyserException, CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }
    
    @Test
    public void givenIndiaCensusData_WithWrongType_ShouldThrowException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_TYPE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenIndiaCensusData_WithWrongFilePath_ShouldThrowException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenIndiaCensusData_WithWrongHeader_ShouldThrowException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_FILE_HEADER_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenStateCodeCSVFile_ReturnsCorrectRecords() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaStateData(INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(37,numOfRecords);
        } catch (CensusAnalyserException e) {
        	Assert.assertEquals(CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenWrongStateCodeCSVFilePath_ShouldThrowException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaStateData(WRONG_STATE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenWrongStateCodeCSVFileType_ShouldThrowException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaStateData(WRONG_STATE_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenWrongStateCodeCSVFile_WithWrongDelimiter_ShouldThrowsException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaStateData(WRONG_STATE_CSV_FILE_DELIMITER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenStateCodeCSVFile_WithWrongHeader_ShouldThrowsException() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaStateData(WRONG_STATE_CSV_FILE_HEADER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenIndiaCensusFile_ForCommonsCSV_ShouldReturnsCorrectNoOfEntries() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaOrStateCodeCensusDataUsingCommonsCSV(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void givenStateCodeFile_ForCommonsCSV_ShouldReturnsCorrectNoOfEntries() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaOrStateCodeCensusDataUsingCommonsCSV(INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(37, numOfRecords);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void givenStateCodeFile_WithWrongFileType_UsingCommonsCSV_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaOrStateCodeCensusDataUsingCommonsCSV(WRONG_STATE_CSV_FILE_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void givenStateCodeFile_WithWrongHeader_UsingCommonsCSV_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaOrStateCodeCensusDataUsingCommonsCSV(WRONG_STATE_CSV_FILE_HEADER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
    
    @Test
    public void giveIndianCensusData_ShouldSortOnState_ThenReturnSortedResult() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV = new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", indiaCensusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    
    @Test
    public void giveIndianCensusData_ShouldSortOnState_ThendReturnSortedResult2() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV = new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("West Bengal", indiaCensusCSV[indiaCensusCSV.length-1].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    
    @Test
    public void giveIndianStateData_WhenSortOnStateCode_ShouldReturnSortedResult() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaStateData(INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getStateCodeWiseSortedCensusData();
            IndiaStateCodeCSV[] indiaStateCSV = new Gson().fromJson(sortCensusData, IndiaStateCodeCSV[].class);
           Assert.assertEquals("AD", indiaStateCSV[0].stateCode);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    @Test
    public void giveIndianStateData_WhenSortOnStateCode_ShouldReturnSortedResult2() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaStateData(INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getStateCodeWiseSortedCensusData();
            IndiaStateCodeCSV[] indiaStateCSV = new Gson().fromJson(sortCensusData, IndiaStateCodeCSV[].class);
            Assert.assertEquals("WB", indiaStateCSV[indiaStateCSV.length-1].stateCode);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    
    @Test
    public void giveIndianStateData_WhenSortOnPopulation_ShouldReturnSortedResult() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getPopulationWiseSortedCensusData();
           IndiaCensusCSV[] indiaCensusCSV= new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
           Assert.assertEquals(199812341, indiaCensusCSV[0].population);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }

    @Test
    public void giveIndianStateData_WhenSortOnPopulation_ShouldReturnSortedResult1() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getPopulationWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV= new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(607688, indiaCensusCSV[indiaCensusCSV.length-1].population);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    
    @Test
    public void giveIndianStateData_WhenSortOnPopulationDensity_ShouldReturnSortedResult() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getPopulationDensityWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV = new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(1102, indiaCensusCSV[0].densityPerSqKm);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    
    @Test
    public void giveIndianStateData_WhenSortOnPopulationDensity_ShouldReturnSortedResult1() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getPopulationDensityWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV = new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(50, indiaCensusCSV[indiaCensusCSV.length-1].densityPerSqKm);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    
    @Test
    public void giveIndianStateData_WhenSortOnArea_ShouldReturnSortedResult2() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getAreaWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV = new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Rajasthan", indiaCensusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
    @Test
    public void giveIndianStateData_WhenSortOnArea_ShouldReturnSortedResult() throws CSVBuilderException {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortCensusData = censusAnalyser.getAreaWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV = new Gson().fromJson(sortCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Goa", indiaCensusCSV[indiaCensusCSV.length-1].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();

        }
    }
}
