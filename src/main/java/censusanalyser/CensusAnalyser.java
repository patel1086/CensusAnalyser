package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CensusAnalyser {
	public int loadIndiaCensusData(String csvFilePath) throws CSVBuilderException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
			Iterator<IndiaCensusCSV> censusCSVIterator = CSVBuilderFactory.createCSVBuilder().getCSVFileIterator(reader,IndiaCensusCSV.class);
			return this.getCount(censusCSVIterator);
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
}
