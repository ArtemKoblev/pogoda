import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Exception;
public class Parser {
    private static Document getPage() throws Exception {
        String url = "http://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url),3000);
        return page;
    }
    private static Pattern patern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static String getDateFromString(String stringDate)throws Exception{
        Matcher matcher = patern.matcher(stringDate);
        if (matcher.find()){
            return matcher.group();
        }
       throw new Exception("Cant extract date from string");
    }
    private static int printPartValues(Elements values, int index){
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isUtro = valueLn.text().contains("Утро");
            boolean isDen = valueLn.text().contains("День");
            boolean isVeher = valueLn.text().contains("Вечер");
             boolean isNoh = valueLn.text().contains("Ночь");


            if (isUtro) {
                iterationCount = 3;
            } else if ( isDen) {
                iterationCount = 2;
            } else if ( isVeher) {
                iterationCount = 1;
            } else if ( isNoh) {
                iterationCount = 0;
            }


        }
            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "   ");
                }
                System.out.println();
            }


        return iterationCount;
    }
    public static void main (String[]args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;

        for (Element name: names){
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + " Явление    Температура    Давл    Влажность   Ветер");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }



    }
}
