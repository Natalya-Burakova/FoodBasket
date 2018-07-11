package controller;

import analysis.xml.CheckValid;
import analysis.xml.XmlObject;
import analysis.xml.Parser;
import basket.Basket;
import controller.button.*;
import controller.label.*;
import controller.table.NewTableFactory;
import controller.window.NewWindowFactory;
import controller.window.Window;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import jdk.nashorn.internal.runtime.ECMAException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import product.Product;
import product.category.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 1210;

    private static double generalPrice = 0;

    public Label lblPrint;
    public Button btnOpen;
    public Button btnNext;
    public TextField txtFieldPath;

    //открываем xml file с продуктами
    public void clickOnOpen(ActionEvent actionEvent) {
        Window window = NewWindowFactory.makeWindow(WINDOW_WIDTH, WINDOW_HEIGHT);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Xml files (*.xml)", "*.xml");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(window.getStage());
        if (file != null) {
            lblPrint.setText("");
            txtFieldPath.setText(file.getAbsolutePath());
        }
        else {
            lblPrint.setText("Error! You have not selected a file.");
            txtFieldPath.setText("");
        }
    }

    public void clickOnNext(ActionEvent actionEvent) {
        if (txtFieldPath.getCharacters().length()!= 0) {
            lblPrint.setText("");

            XmlObject xmlObject = new XmlObject(txtFieldPath.getCharacters().toString());
            Parser parser = new Parser(xmlObject.getXml());//парсим файл с продуктами
            parser.parce();

            if (CheckValid.validate(xmlObject.getXml(), new File("src/main/resources/xsd/products.xsd")))
                newGeneralWindowWithTable(parser);
            else
                lblPrint.setText("Error! The file is corrupt.");
        }
        else
            lblPrint.setText("Error! It is not possible to download the file.");
    }


    public void newGeneralWindowWithTable(Parser parser){
        Window window = NewWindowFactory.makeWindow(WINDOW_WIDTH, WINDOW_HEIGHT);

        //таблица продуктов
        TableView newTable = NewTableFactory.makeTable(parser);

        Button btnAdd =  NewButtonFactory.makeButton(">", WINDOW_WIDTH/2-20, WINDOW_HEIGHT/2 - 50, 50 ,50).getButton();
        Button btnRemove =  NewButtonFactory.makeButton("<", WINDOW_WIDTH/2-20, WINDOW_HEIGHT/2 + btnAdd.getMaxHeight() - 50, 50 ,50).getButton();

        //таблица корзины
        TableView newTableBasket = NewTableFactory.makeTable();
        newTableBasket.setLayoutX(btnAdd.getLayoutX() + btnAdd.getMaxWidth());

        // объект корзины
        Basket basket = new Basket();

        Label labelPrice = NewLabelFactory.makeLabel("Price: " + generalPrice +" rub", 25,WINDOW_WIDTH-300, WINDOW_HEIGHT-50).getLable();
        labelPrice.setTextFill(Color.RED);

        Button btnSave = NewButtonFactory.makeButton("Save basket...", WINDOW_WIDTH-550 , WINDOW_HEIGHT-50 ,220, 50).getButton();

        btnAdd.setOnAction((event) ->  {
                if (newTable.getSelectionModel().getSelectedItem() != null) {
                    newEnterProduct((Product) newTable.getSelectionModel().getSelectedItem(), labelPrice, basket, newTableBasket);
                }
        });

        btnRemove.setOnAction( (event) -> {
                if (newTableBasket.getSelectionModel().getSelectedItem()!=null) {
                    Product pr = (Product) newTableBasket.getSelectionModel().getSelectedItem();
                    //удаляеям выбранный продукт
                    basket.getListBasket().remove(pr);
                    newTableBasket.setItems(basket.getListBasket());
                    //вычитываем из общей цены стоимость удаленного продукта
                    generalPrice = generalPrice - Double.parseDouble(pr.getPrice().getValue());
                    labelPrice.setText("Price: " + generalPrice + " rub");
                }
        });

        btnSave.setOnAction((event) -> {
                //если лист не пустой, сохраняем в файл данные, иначе - сообщение с предупреждением
                if (basket.getListBasket().size() != 0)
                    saveBasket(window, basket);
                else {
                    Window wind = NewWindowFactory.makeWindow(WINDOW_WIDTH/2/2, WINDOW_HEIGHT/2/2);
                    wind.getStage().setTitle("Warning!");
                    Label lab = NewLabelFactory.makeLabel("It is impossible to save the file.\nSince the list of products is empty.", 15, 10, 10).getLable();
                    wind.getAnchorPane().getChildren().addAll(lab);
                    wind.getStage().show();
                }
        });

        window.getAnchorPane().getChildren().addAll(newTable, btnAdd, btnRemove, btnSave, newTableBasket, labelPrice);
        window.getStage().show();
    }


    //добавляем новый продукт
    public void newEnterProduct(Product product, Label labelPrice, Basket basket, TableView newTableBasket) {
        double price = Double.parseDouble(product.getPrice().getValue());

        //создаем окно для ввода количества и веса продукта
        Window window = NewWindowFactory.makeWindow(WINDOW_WIDTH/2/2, WINDOW_HEIGHT/2);

        Label labelQuantity = NewLabelFactory.makeLabel("Enter quantity:",  15,10, 0).getLable();
        labelQuantity.setMaxWidth(120);
        labelQuantity.setMaxHeight(100);

        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        cb.setLayoutX(labelQuantity.getLayoutX()+labelQuantity.getMaxWidth());
        cb.setValue("1");

        Label labelWeight = NewLabelFactory.makeLabel("Enter weight (kg):", 15,10, labelQuantity.getMaxHeight()).getLable();

        Button ok =  NewButtonFactory.makeButton("ok", WINDOW_WIDTH/2/2/2-30, WINDOW_HEIGHT/2-50, 60 ,50).getButton();

        TextField txtWeight = new TextField();
        txtWeight.setLayoutX(labelQuantity.getLayoutX() + labelQuantity.getMaxWidth());
        txtWeight.setLayoutY(labelQuantity.getMaxHeight());

        Label lblPrice = NewLabelFactory.makeLabel("", 15,WINDOW_WIDTH/2/2/2,WINDOW_HEIGHT/2 -100).getLable();
        lblPrice.setTextFill(Color.RED);
        lblPrice.setText("Price: " + product.getPrice().getValue() + " rub");

        window.getAnchorPane().getChildren().addAll(labelQuantity, cb, ok, lblPrice);

        //выбираем какие поля для текущего продукта стоит/не стоит отображать
        Category category = product.getCategoryProduct();
        if (category == Category.Bakery
                || category == Category.Confectionery
                || category == Category.Grits
                || category == Category.Milk
                || category == Category.Spice);
        else
            window.getAnchorPane().getChildren().addAll(labelWeight, txtWeight);

        //если выбрали новое количество из choisebox, обновляем цену
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    double wp = 1; //вес по умолчанию
                    if (txtWeight.getCharacters().length()!=0) {
                        try {
                            wp = Double.parseDouble(txtWeight.getCharacters().toString());
                            product.setWeight(wp);//устанавливаем новый вес
                        }
                        catch (Exception e){}
                    }
                    product.setCount(Integer.parseInt(newValue.toString()));//устанавливаем новое количество
                    lblPrice.setText("Price: " + getPrice(price, Integer.parseInt(newValue.toString()), wp) + " rub");//выводим новую цену
                }
            }
        };
        cb.getSelectionModel().selectedItemProperty().addListener(changeListener);

        //если ввели в текстовое поле новое значение, обновляем цену
        txtWeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!= null) {
                int c = 1;
                double w = 1;
                try {
                    w = Double.parseDouble(newValue);
                    product.setWeight(w);//устанавливаем новый вес
                }
                catch (Exception e) {}
                if (cb.getSelectionModel().getSelectedItem() != null) {
                    c = Integer.parseInt(cb.getSelectionModel().getSelectedItem().toString());
                    product.setCount(c);//устанавливаем новое количество
                }
                lblPrice.setText("Price: " + getPrice(price, c, w) + " rub");//выводим новую цену
            }
        });

        ok.setOnAction((event) -> {
                double pr = Double.parseDouble(product.getPrice().getValue());
                if (lblPrice.getText().length()!=0) {
                    pr =  Double.parseDouble(lblPrice.getText().substring(7, lblPrice.getText().length()-4));//новая цена
                    product.setPrice(pr);//устанавливаем новую цену продукту
                    generalPrice = generalPrice + pr; //добавляем к общей цене, цену продукты
                }

                labelPrice.setText("Price: " + generalPrice +" rub"); //выводим общую цену

                basket.addBasket(product); //добавляем в корзину новый продукт с введеными параметрами
                newTableBasket.setItems(basket.getListBasket()); // добавляем в таблицу саму корзину

                window.getStage().close();//закрываем всплывающее окно
        });
        window.getStage().show();
    }



    public double getPrice(double price, int count, double weight) {
        return count*(price*weight);
    }

    public void saveBasket(Window window, Basket basket){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XmlObject files (*.xml)", "*.xml");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File fileUser = fileChooser.showSaveDialog(window.getStage());
        if (fileUser != null) {
            try (BufferedWriter writer = new BufferedWriter( new FileWriter(fileUser));)
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                Document doc = factory.newDocumentBuilder().newDocument();

                Element root = doc.createElement("products");
                doc.appendChild(root);

                ObservableList products = basket.getListBasket();
                for (int i = 0; i<products.size(); i++) {
                    Product p =  (Product) products.get(i);
                    Element item1 = doc.createElement("product");
                    item1.setAttribute("count", p.getCount().getValue());
                    item1.setAttribute("name", p.getName().getValue());
                    item1.setAttribute("price", p.getPrice().getValue());
                    item1.setAttribute("weignt", p.getWeight().getValue());
                    root.appendChild(item1);
                }

                Element item = doc.createElement("generalPrice");
                item.setAttribute("value", String.valueOf(generalPrice));
                root.appendChild(item);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(new DOMSource(doc), new StreamResult(fileUser));

                if (CheckValid.validate(fileUser, new File("src/main/resources/xsd/basket.xsd")));
                else
                    throw new SAXException();

            } catch (Exception e) {
                Window wind = NewWindowFactory.makeWindow(WINDOW_WIDTH/2/2, WINDOW_HEIGHT/2/2);
                wind.getStage().setTitle("Warning!");
                Label lab = NewLabelFactory.makeLabel("The file was not saved.\nTry again?", 15, 10, 10).getLable();
                Button btn = NewButtonFactory.makeButton("ok", 100, 80,70,40).getButton();
                btn.setOnAction((event)-> { wind.getStage().close(); saveBasket(window, basket);});
                wind.getAnchorPane().getChildren().addAll(lab, btn);
                wind.getStage().show();
            }
        }

    }


}
