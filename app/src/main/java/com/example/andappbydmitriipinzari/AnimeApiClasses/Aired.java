package com.example.andappbydmitriipinzari.AnimeApiClasses;

public class Aired {


    Prop prop;

    public class Prop {
        From from;
        To to;

        public class From {
            String day;
            String month;
            String year;
        }

        public class To {
            String day;
            String month;
            String year;
        }
    }
}
