package ind.xyz.mywebsite.util.md;


import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Heading;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @author k
 * @date 2021/10/17 14:07
 * @email 3511896104@qq.com
 */
public class Md2HtmlUtil {

    /**
     * markdown格式转换成HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Markdown转换成HTML
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
//        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        List<Extension> tableExtension = Collections.singletonList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
            if(node instanceof Heading){
                attributes.put("class","fw-bolder f-header");

            }
            if(node instanceof Paragraph){
                attributes.put("class","lh-base fs-5");
                attributes.put("style","color:#666666;");
            }
        }
    }


//    public static void main(String[] args) {
//        String table = "| hello | hi   | k   |\n" +
//                "| ----- | ---- | ----- |\n" +
//                "| 阿尔及发  | 士大夫  | aa    |\n" +
//                "| 阿什顿发  | 阿斯蒂芬 | 阿斯顿马丁 |\n" +
//                "\n";
//        String temp = "[imCoding k](https://blog.csdn.net/jahdgja?spm=1000.2115.3001.5343)";
//        System.out.println(markdownToHtmlExtensions(table));
//    }

}