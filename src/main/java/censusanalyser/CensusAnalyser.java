package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
	public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
		try 
			(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));)
			{
			Iterator<IndiaCensusCSV> censusCSVIterator = this.getCSVFileIterator(reader, IndiaCensusCSV.class);
			Iterator<IndiaCensusCSV> censusCSVIterator1 = new OpenCSVBuilder().getCSVFileIterator(reader, IndiaCensusCSV.class);
			Iterator<IndiaCensusCSV> censusCSVIterator2 = CSVBuilderFactory.createCSVBuilder().getCSVFileIterator(reader, IndiaCensusCSV.class);
			return this.getCount(censusCSVIterator2);
		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		}catch (IOException | RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		}
	}
	
	public int loadIndiaStateData(String csvFilePath) throws CensusAnalyserException {
		try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) 
			{
			Iterator<IndiaStateCodeCSV> censusCSVIterator = this.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
			Iterator<IndiaCensusCSV> censusCSVIterator1 = new OpenCSVBuilder().getCSVFileIterator(reader, IndiaCensusCSV.class);
			Iterator<IndiaCensusCSV> censusCSVIterator2 = CSVBuilderFactory.createCSVBuilder().getCSVFileIterator(reader, IndiaCensusCSV.class);
			return this.getCount(censusCSVIterator2);
		} catch (IllegalStateException e) {
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
		} catch (IOException | RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.STATE_CODE_FILE_PROBLEM);
		}

	}
	
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
	public <E> int getCount(Iterator<E> csvIterator) {
        Iterable<E> csvIterable = () -> csvIterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }
}
