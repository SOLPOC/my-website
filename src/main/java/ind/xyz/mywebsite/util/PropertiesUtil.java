package ind.xyz.mywebsite.util;

import ind.xyz.mywebsite.domain.Blog;

import java.net.URISyntaxException;
import java.util.Properties;
import java.io.StringReader;
import java.io.IOException;
public class PropertiesUtil {
    public static Blog parseQueryString(String url) {
            String title = getQueryParam(url, "title");
            String tag = getQueryParam(url, "tag");
            String content = getQueryParam(url,"content");
            String category = getQueryParam( url,"category");
            String language = getQueryParam( url,"language");
            String type = getQueryParam( url,"type");

            Blog blog = new Blog();
            if (title != null) {
                blog.setTitle(title);
            }
            if (tag != null) {
                blog.setTag(tag);
            }
            if (content != null) {
                blog.setContent(content);
            }
            if (category != null) {
                blog.setCategory(category);
            }
            if (language != null) {
                blog.setLanguage(language);
            }
            if (type != null) {
                blog.setType(type);
            }
            return blog;

    }

    private static String getQueryParam(String  url,String paramName) {
        String[] queryPairs = url.split("&");
        for (String pair : queryPairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 &&keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
