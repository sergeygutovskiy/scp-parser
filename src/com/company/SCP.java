package com.company;

import java.util.ArrayList;

class SCP {
    public String number;
    public String name;
    public String type;
    public String img;
    public Integer rating;

    public ArrayList<String> cond;
    public ArrayList<String> desc;
    public ArrayList<String> tags;

    public SCP(String number,
               String name,
               String type,
               String img,
               Integer rating,
               ArrayList<String> cond,
               ArrayList<String> desc,
               ArrayList<String> tags
    ) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.img = img;
        this.rating = rating;

        this.cond = cond;
        this.desc = desc;
        this.tags = tags;
    }

    @Override
    public String toString() {
        // return number + "," + name + "," + type + "," + img;
        String res = number + "\n" + name + "\n" + type + "\n" + rating + "\n";
        if (cond != null) {
            for (String s : cond) {
                res += (s + "\n");
            }
        }
        res += "\n";
        if (desc != null) {
            for (String s : desc) {
                res += (s + "\n");
            }
        }
        res += "\n";
        if (tags != null) {
            for (String s : tags) {
                res += (s + ", ");
            }
        }
        res += "\n";

        return res;
    }
}