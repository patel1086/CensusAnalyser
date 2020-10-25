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
	public int loadIndiaCensusData(String csvFilePath) throws CSVBuilderException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			censusCSVList = CSVBuilderFactory.createCSVBuilder().getCSVFList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
		} catch (IllegalStateException e) {
			throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
		} catch (IOException | RuntimeException e) {
			throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.CENSUS_FILE_PROBLEM);
		}
	}

	public int loadIndiaStateData(String csvFilePath) throws CSVBuilderException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			Iterator<IndiaCensusCSV> censusCSVIterator1 = CSVBuilderFactory.createCSVBuilder()
					.getCSVFileIterator(reader, IndiaCensusCSV.class);
			return this.getCount(censusCSVIterator1);
		} catch (IllegalStateException e) {
			throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
		} catch (IOException | RuntimeException e) {
			throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.STATE_CODE_FILE_PROBLEM);
		}
	}

	public <E> Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CSVBuilderException {
		try {
			CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
			csvToBeanBuilder.withType(csvClass);
			csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
			CsvToBean<E> csvToBean = csvToBeanBuilder.build();
			Iterator<E> censusCSVIterator = csvToBean.iterator();
			return censusCSVIterator;

		} catch (IllegalStateException e) {
			throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
		} catch (RuntimeException e) {
			throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.STATE_CODE_FILE_PROBLEM);

		}

	}

	public int loadIndiaOrStateCodeCensusDataUsingCommonsCSV(String csvFilePath) throws CSVBuilderException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			Iterator<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().
										  withIgnoreHeaderCase().
										  withTrim().
										  parse(reader).
										  iterator();
			return this.getCount(records);
		} catch (IOException | RuntimeException e) {
			throw new CSVBuilderException(e.getMessage(),
					CSVBuilderException.ExceptionType.CENSUS_FILE_PROBLEM);
		}

	}

	public <E> int getCount(Iterator<E> csvIterator) {
		Iterable<E> csvIterable = () -> csvIterator;
		return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
	}
	
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
}
