package censusanalyser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CensusAnalyser {
	List<IndiaCensusCSV> censusCSVList = null;
	List<IndiaStateCodeCSV> stateCSVList = null;

	// Method for load IndiaCensusData
	public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException, CSVBuilderException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			censusCSVList = CSVBuilderFactory.createCSVBuilder().getCSVFileList(reader, IndiaCensusCSV.class);
			return censusCSVList.size();
		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		} catch (IOException | RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		}
	}

	// Method for load India State Census data
	public int loadIndiaStateData(String csvFilePath) throws CensusAnalyserException, CSVBuilderException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			stateCSVList = CSVBuilderFactory.createCSVBuilder().getCSVFileList(reader, IndiaStateCodeCSV.class);
			return stateCSVList.size();
		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		} catch (IOException | RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM);
		}
	}

	// Generics Method for Iterating Csv
	public <E> Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException {
		try {
			CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
			csvToBeanBuilder.withType(csvClass);
			csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
			CsvToBean<E> csvToBean = csvToBeanBuilder.build();
			Iterator<E> censusCSVIterator = csvToBean.iterator();
			return censusCSVIterator;

		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM);

		}

	}

	// Method to load and Parser CSV by CommonCSV
	public int loadIndiaOrStateCodeCensusDataUsingCommonsCSV(String csvFilePath) throws CensusAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			Iterator<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()
					.parse(reader).iterator();
			return this.getCount(records);
		} catch (IOException | RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		}

	}

	// Method to count total rows in CSV
	public <E> int getCount(Iterator<E> csvIterator) {
		Iterable<E> csvIterable = () -> csvIterator;
		return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
	}

	// Method to get sorted data according to state wise alphabetically order
	public String getStateWiseSortedCensusData() throws CensusAnalyserException {
		try (Writer writer = new FileWriter("./src/test/resources/IndiaStateCensusDataJson.json")) {
			if (censusCSVList == null || censusCSVList.size() == 0) {
				throw new CensusAnalyserException("No data", CensusAnalyserException.ExceptionType.NO_DATA);
			}
			Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
			this.sort(censusComparator);
			String json = new Gson().toJson(censusCSVList);
			Gson gson = new GsonBuilder().create();
			gson.toJson(censusCSVList, writer);
			return json;

		} catch (RuntimeException | IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.FILE_OR_HEADER_PROBLEM);
		}
	}

	// Method to sorted data by State code
	public String getStateCodeWiseSortedCensusData() throws CensusAnalyserException {
		try (Writer writer = new FileWriter("./src/test/resources/IndiaStateCodeDataJson.json")) {
			if (stateCSVList == null || stateCSVList.size() == 0) {
				throw new CensusAnalyserException("No data", CensusAnalyserException.ExceptionType.NO_DATA);
			}
			Comparator<IndiaStateCodeCSV> censusComparator = Comparator.comparing(state -> state.stateCode);
			this.sortState(censusComparator);
			String json = new Gson().toJson(stateCSVList);
			Gson gson = new GsonBuilder().create();
			gson.toJson(stateCSVList, writer);
			return json;

		} catch (RuntimeException | IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.FILE_OR_HEADER_PROBLEM);
		}
	}

	// Method to sorted State Data By Population
	public String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
		try (Writer writer = new FileWriter("./src/test/resources/IndiaStatePopulationDataJson.json")) {
			if (censusCSVList == null || censusCSVList.size() == 0) {
				throw new CensusAnalyserException("No data", CensusAnalyserException.ExceptionType.NO_DATA);
			}
			Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.population);
			this.SortInDescendOrder(censusComparator);
			String json = new Gson().toJson(censusCSVList);
			Gson gson = new GsonBuilder().create();
			gson.toJson(censusCSVList, writer);
			return json;

		} catch (RuntimeException | IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.FILE_OR_HEADER_PROBLEM);
		}
	}

	// Method to sorted data based on Population Density (From Max Populatio Density to Min Population Density)
	public String getPopulationDensityWiseSortedCensusData() throws CensusAnalyserException {
		try (Writer writer = new FileWriter("./src/test/resources/IndiaStatePopulationDensityDataJson.json")) {
			if (censusCSVList == null || censusCSVList.size() == 0) {
				throw new CensusAnalyserException("No data", CensusAnalyserException.ExceptionType.NO_DATA);
			}
			Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.densityPerSqKm);
			this.SortInDescendOrder(censusComparator);
			String json = new Gson().toJson(censusCSVList);
			Gson gson = new GsonBuilder().create();
			gson.toJson(censusCSVList, writer);
			return json;

		} catch (RuntimeException | IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.FILE_OR_HEADER_PROBLEM);
		}
	}

	// Method to sorted data based on area of state (From Largest area to smallest area)
	public String getAreaWiseSortedCensusData() throws CensusAnalyserException {
		try (Writer writer = new FileWriter("./src/test/resources/IndiaStateAreaDataJson.json")) {
			if (censusCSVList == null || censusCSVList.size() == 0) {
				throw new CensusAnalyserException("No data", CensusAnalyserException.ExceptionType.NO_DATA);
			}
			Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.areaInSqKm);
			this.SortInDescendOrder(censusComparator);
			String json = new Gson().toJson(censusCSVList);
			Gson gson = new GsonBuilder().create();
			gson.toJson(censusCSVList, writer);
			return json;

		} catch (RuntimeException | IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.FILE_OR_HEADER_PROBLEM);
		}
	}

	// Sorting Method for India Census CSV Data
	private void sort(Comparator<IndiaCensusCSV> censusComparator) {
		for (int i = 0; i < censusCSVList.size() - 1; i++) {
			for (int j = 0; j < censusCSVList.size() - i - 1; j++) {
				IndiaCensusCSV census1 = censusCSVList.get(j);
				IndiaCensusCSV census2 = censusCSVList.get(j + 1);
				if (censusComparator.compare(census1, census2) > 0) {
					censusCSVList.set(j, census2);
					censusCSVList.set(j + 1, census1);
				}
			}
		}
	}

	// Method for sorting data of state csv in Ascending Order
	private void sortState(Comparator<IndiaStateCodeCSV> censusComparator) {
		for (int i = 0; i < stateCSVList.size() - 1; i++) {
			for (int j = 0; j < stateCSVList.size() - i - 1; j++) {
				IndiaStateCodeCSV census1 = stateCSVList.get(j);
				IndiaStateCodeCSV census2 = stateCSVList.get(j + 1);
				if (censusComparator.compare(census1, census2) > 0) {
					stateCSVList.set(j, census2);
					stateCSVList.set(j + 1, census1);
				}
			}
		}
	}

	// Method for sorting data of state csv in Descending Order
	private void SortInDescendOrder(Comparator<IndiaCensusCSV> censusComparator) {
		for (int i = 0; i < censusCSVList.size() - 1; i++) {
			for (int j = 0; j < censusCSVList.size() - i - 1; j++) {
				IndiaCensusCSV census1 = censusCSVList.get(j);
				IndiaCensusCSV census2 = censusCSVList.get(j + 1);
				if (censusComparator.compare(census1, census2) < 0) {
					censusCSVList.set(j, census2);
					censusCSVList.set(j + 1, census1);
				}
			}
		}
	}
}
