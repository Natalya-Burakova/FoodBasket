package controller;

import analysis.json.Json;
import analysis.json.Parser;
import analysis.json.Validator;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import product.Product;
import product.category.*;

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

    //открываем json file с продуктами
    public void clickOnOpen(ActionEvent actionEvent) {
        Window window = NewWindowFactory.makeWindow(WINDOW_WIDTH, WINDOW_HEIGHT);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");//Расширение
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

            Json json = new Json(txtFieldPath.getCharacters().toString());
            json.getXsd(json.toString(), "productsJsonToXml.xml", "productsXmlToXsd.xsd");

            Parser parser = new Parser(json.toString());//парсим файл с продуктами
            parser.parce();

            if (Validator.validate(json.toString()))
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

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (newTable.getSelectionModel().getSelectedItem() != null) {
                    newEnterProduct((Product) newTable.getSelectionModel().getSelectedItem(), labelPrice, basket, newTableBasket);
                }
            }
        });

        btnRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (newTableBasket.getSelectionModel().getSelectedItem()!=null) {
                    Product pr = (Product) newTableBasket.getSelectionModel().getSelectedItem();
                    //удаляеям выбранный продукт
                    basket.getListBasket().remove(pr);
                    newTableBasket.setItems(basket.getListBasket());
                    //вычитываем из общей цены стоимость удаленного продукта
                    generalPrice = generalPrice - Double.parseDouble(pr.getPrice().getValue());
                    labelPrice.setText("Price: " + generalPrice + " rub");
                }
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //если лист не пустой, сохраняем в файл данные, иначе - сообщение с предупреждением
                if (basket.getListBasket().size() != 0)
                    saveBasket(window, basket);
                else
                    viewWarning("It is impossible to save the file." + "\nSince the list of products is empty.");
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
        String category = product.getCategoryProduct().getName();
        if (category.equals(BakeryProducts.getInstance().getName())
                || category.equals(ConfectioneryProducts.getInstance().getName())
                || category.equals(GritsProducts.getInstance().getName())
                || category.equals(MilkProducts.getInstance().getName())
                || category.equals(SpiceProducts.getInstance().getName()));
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

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
            }
        });
        window.getStage().show();
    }



    public double getPrice(double price, int count, double weight) {
        return count*(price*weight);
    }

    public void saveBasket(Window window, Basket basket){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File fileUser = fileChooser.showSaveDialog(window.getStage());
        if (fileUser != null) {
            try (BufferedWriter writer = new BufferedWriter( new FileWriter(fileUser));)
            {
                ObservableList products = basket.getListBasket();
                writer.write("{ \"products\": {\n \"product\": [\n");
                for (int i = 0; i<products.size(); i++) {
                    Product prod = (Product) products.get(i);
                    writer.write("{ "+"\"name\": "  + "\""+prod.getName().getValue()+"\""+",\n");
                    writer.write("\"count\": " + "\"" + prod.getCount().getValue()+"\""+",\n");
                    writer.write("\"weight\": " + "\"" + prod.getWeight().getValue()+"\""+",\n");
                    writer.write("\"price\": " + "\"" + prod.getPrice().getValue()+"\""+"\n");
                    writer.write("}");
                    if (i==products.size()-1)
                        writer.write("\n");
                    else
                        writer.write(",\n");
                }
                writer.write("],\n");
                writer.write("\"generalPrice\": " + "\"" + generalPrice + "\"");
                writer.write("}}");

            } catch (IOException e) {}
            //делаем xml и xsd
            Json newJsonProduct = new Json(fileUser.getAbsolutePath());
            newJsonProduct.getXsd(newJsonProduct.toString(), "basketJsonToXml.xml", "basketXmlToXsd.xsd");
        }
    }

    public void viewWarning (String warning) {
        Window wind = NewWindowFactory.makeWindow(WINDOW_WIDTH/2/2, WINDOW_HEIGHT/2/2);
        wind.getStage().setTitle("Warning!");
        Label lab = NewLabelFactory.makeLabel(warning, 15, 10, 10).getLable();
        wind.getAnchorPane().getChildren().addAll(lab);
        wind.getStage().show();
    }


}
