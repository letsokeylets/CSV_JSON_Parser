package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        //Создание json на основании csv файла
        writeString(json, "data.json");
        //Создание json на основании xml файла
        List<Employee> listXML = parseXML("data.xml");
        String jsonXML = listToJson(listXML);
        writeString(jsonXML, "data2.json");
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            //передаём ноду и пустой список объектов Employee, метод read заполнит список, считав из xml данные
            read(root, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void read(Node node, List<Employee> list) {
        NodeList nodeList = node.getChildNodes();
        //Проверка ноды на employee
        boolean nodeEmploy = "employee".equals(node.getNodeName());
        //Если доходим то тега employee, тогда необходимо создать новый объект
        Employee employ = nodeEmploy ? new Employee() : null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                if (nodeEmploy) {
                    //Установка значения
                    employ.setValue(element.getNodeName(), element.getTextContent());
                }
                read(node_, list);
            }
        }
        //Добавляем объект в список
        if (employ != null) {
            list.add(employ);
        }
    }

}
