package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("http://scpfoundation.net/scp-009").get();
            Element e = doc.getElementById("page-content");

            String title = doc.getElementById("page-title").text().split(" - ")[1];
            Element img = e.getElementsByClass("rimg").get(0).children().get(0);

            String number = null;
            String type = null;
            Integer conditionIndex = null;
            Integer descIndex = null;

            Elements paragraphs = e.getElementsByTag("p");
            for (Element el : paragraphs) {
                if (el.getElementsByTag("strong").size() > 0) {
                    if (el.getElementsByTag("strong").get(0).text().equals("Объект №:")) {
                        el.getElementsByTag("strong").get(0).remove();
                        number = el.text();
                    }
                    else if (el.getElementsByTag("strong").get(0).text().equals("Класс объекта:")) {
                        el.getElementsByTag("strong").get(0).remove();
                        type = el.text();
                    }
                    else if (el.getElementsByTag("strong").get(0).text().equals("Особые условия содержания:")) {
                        el.getElementsByTag("strong").get(0).remove();
                        conditionIndex = e.children().indexOf(el);
                    }
                    else if (el.getElementsByTag("strong").get(0).text().equals("Описание:")) {
                        el.getElementsByTag("strong").get(0).remove();
                        descIndex = e.children().indexOf(el);
                    }
                }
            }

            System.out.println("Заголовок: " + title);
            System.out.println("Объект №: " + number);
            System.out.println("Класс объекта: " + type);
            System.out.println("Картинка: " + img.attr("src"));

            Elements condition = new Elements();
            Elements desc = new Elements();

            for (int i = 0; i < e.children().size(); i++) {
                if (i < conditionIndex) continue;
                if (i == descIndex) break;

                condition.add(e.children().get(i));
            }

            for (int i = 0; i < e.children().size() - 1; i++) {
                if (i < descIndex) continue;
                if (e.children().get(i).tagName() == "hr") break;

                desc.add(e.children().get(i));
            }

            System.out.println("Особые условия содержания: " + condition);
            System.out.println("Описание: " + desc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
