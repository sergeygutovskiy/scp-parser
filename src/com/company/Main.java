package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<SCP> scps = new ArrayList<>();

        String url = "http://scpfoundation.net/scp-";
        for (int i = 2; i < 30 ; i++) {
            scps.add(parse(url + String.format("%03d", i)));
        }
        for (SCP o : scps) {
            // System.out.format("%-30s%-50s%-40s%n", o.number, o.name, o.type);
            System.out.println(o);
        }
    }

    public static SCP parse(String link) {
        try {
            Document doc = Jsoup.connect(link).get();
            Element content = doc.getElementById("page-content");

            content.getElementsByTag("hr").remove();

            String scp_name = doc.getElementById("page-title").text().split(" - ")[1];
            String scp_number = doc.getElementById("page-title").text().split(" - ")[0];
            // doc.getElementById("page-title").remove();

            String scp_img = null;

            Elements scp_img_els = content.getElementsByClass("scp-image-block");
            if (scp_img_els.size() > 0) {
                if (scp_img_els.get(0).children().get(0).children().size() > 0) {
                    scp_img = scp_img_els.get(0).children().get(0).children().get(0).attr("src");
                } else
                    scp_img = scp_img_els.get(0).children().get(0).attr("src");
            }
            else if (content.getElementsByClass("rimg").size() > 0){
                scp_img_els = content.getElementsByClass("rimg");

                if (scp_img_els.get(0).children().get(0).children().size() > 0) {
                    scp_img = scp_img_els.get(0).children().get(0).children().get(0).attr("src");
                } else
                    scp_img = scp_img_els.get(0).children().get(0).attr("src");
            }

            String scp_type = null;
            Integer scp_cond_index = null;
            Integer scp_desc_index = null;

            for (Element e: content.children()) {
                if (e.getElementsByTag("strong").size() > 0) {
                     if (e.getElementsByTag("strong").get(0).text().equals("Класс объекта:")) {
                        // e.getElementsByTag("strong").get(0).remove();
                        scp_type = e.text();
                        // break;
                    }
                    else if (e.getElementsByTag("strong").get(0).text().equals("Особые условия содержания:")) {
                        scp_cond_index = content.children().indexOf(e);
                    }
                    else if (e.getElementsByTag("strong").get(0).text().equals("Описание:")) {
                        scp_desc_index = content.children().indexOf(e);
                        break;
                    }
                }
            }

            ArrayList<String> scp_desc = null;
            ArrayList<String> scp_cond = null;

            // System.out.println(scp_cond_index + " " + scp_desc_index);

            if (scp_cond_index != null && scp_desc_index != null) {
                scp_cond = new ArrayList<>();
                for (int i = scp_cond_index; i < scp_desc_index; i++) {
                    Element el = content.children().get(i);
                    if (el.tagName() == "p")
                        scp_cond.add(el.text());
                    else
                        System.out.println("[" + scp_name + "] Not <p>: " + el.tagName());
                }
                for (String s : scp_cond) {
                    // System.out.println(s);
                    // System.out.println();
                }

                System.out.println();

                scp_desc = new ArrayList<>();
                for (int i = scp_desc_index; i < content.children().size() - 1; i++) {
                    Element el = content.children().get(i);
                    if (el.tagName() == "p")
                        scp_desc.add(el.text());
                    else
                        System.out.println("[" + scp_name + "] Not <p>: " + el.tagName());
                }
                for (String s : scp_desc) {
                    // System.out.println(s);
                    // System.out.println();
                }
            }

            Elements scp_tags_el = doc.getElementsByClass("page-tags").get(0).children().get(0).children();
            ArrayList<String> scp_tags = new ArrayList<>();
            for (Element e : scp_tags_el) {
                scp_tags.add(e.text());
                // System.out.print(e.text() + " ");
            }
            // System.out.println();

            Element scp_rating_el = content.getElementsByClass("rate-points").get(0).children().get(0);
            Integer scp_rating = 0;
            if (scp_rating_el.text().contains("-"))
                scp_rating = - Integer.parseInt(scp_rating_el.text().substring(1));
            else
                scp_rating = Integer.parseInt(scp_rating_el.text().substring(1));

            SCP o = new SCP(scp_number, scp_name, scp_type, scp_img, scp_rating, scp_cond, scp_desc, scp_tags);
            return o;

        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    public static void main_old(String[] args) {
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
