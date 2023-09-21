package ind.xyz.mywebsite.util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class HtmlUtil {
    public static String wrapWithArticle(String html) {
        Document doc = Jsoup.parse(html);
        Elements blockquotes = doc.select("blockquote");
        Elements pres = doc.select("pre");
        Elements tables = doc.select("table");
        Elements lis = doc.select("li");

        // 将 <blockquote>, <pre>, <table> 节点移动到 <article> 标签下
        for (Element blockquote : blockquotes) {
            Element article = doc.createElement("article");
            article.addClass("markdown-body");
            blockquote.wrap(article.toString());
        }
        for (Element pre : pres) {
            Element article = doc.createElement("article");
            article.addClass("markdown-body");
            pre.wrap(article.toString());
        }
        for (Element table : tables) {
            Element article = doc.createElement("article");
            article.addClass("markdown-body");
            table.wrap(article.toString());
        }
        for (Element li : lis) {
            li.addClass("list-group-item fs-5");
        }

        return doc.html();
    }
}
