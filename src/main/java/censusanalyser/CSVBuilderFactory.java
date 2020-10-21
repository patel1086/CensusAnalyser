package censusanalyser;

public class CSVBuilderFactory {
	public static InterfaceCSVBuilder createCSVBuilder(){
        return (InterfaceCSVBuilder) new OpenCSVBuilder();
}
}
