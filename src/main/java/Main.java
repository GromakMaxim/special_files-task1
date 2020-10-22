import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        // >_< продолжаю практиковать лямбды
        CSVReadable csvReader = (tableHeader, csvTitle) -> {
            try (CSVReader csvReader1 = new CSVReader(new FileReader(csvTitle))) {
                ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();//определяем "стратегию"
                strategy.setType(Employee.class);//данные из csv привяязаны к классу employee
                strategy.setColumnMapping(tableHeader);//обозначаем "шапку"
                CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader1)
                        .withMappingStrategy(strategy)//взаимодействие документа и выбранной стратегии
                        .build();
                List<Employee> staff = csv.parse();//парсим csv в список объектов типа employee
                return staff;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };


        JSONWritable jsonWriter = (resultPath, employee) -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(resultPath), employee);//записываем результат в файл
        };

        List<Employee> employeeArr = csvReader.csvRead(new String[]{"id", "firstName", "lastName", "country", "age"}, "example.csv");
        employeeArr.forEach(System.out::println);//выводим в консольку содержимое csv

        jsonWriter.createJSON("newJSONfile.json", employeeArr);
    }
}



