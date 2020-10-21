package censusanalyser;
import java.io.Reader;
import java.util.Iterator;
public interface InterfaceCSVBuilder {
	<E> Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvClass) throws CensusAnalyserException;
}
