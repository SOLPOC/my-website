package ind.xyz.mywebsite.util.md;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TableCell;
import com.vladsch.flexmark.ext.tables.TableRow;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.html.MutableAttributes;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 将入参Markdown文档转换为html文档
 */
public class MdUtil {
    public static String MdToHtmlForApiDoc(String md) {
        // 按需添加扩展
        MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                        // 自定义扩展，为<pre>标签添加line-numbers的class，用于prism库代码左侧行号展示
                        CodePreLineNumbersExtension.create(),
                        AutolinkExtension.create(),
                        EmojiExtension.create(),
                        StrikethroughExtension.create(),
                        TaskListExtension.create(),
                        TablesExtension.create(),
                        TocExtension.create()
                ))
                // set GitHub table parsing options
                .set(TablesExtension.WITH_CAPTION, false)
                .set(TablesExtension.COLUMN_SPANS, false)
                .set(TablesExtension.MIN_HEADER_ROWS, 1)
                .set(TablesExtension.MAX_HEADER_ROWS, 1)
                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
                // setup emoji shortcut options
                // uncomment and change to your image directory for emoji images if you have it setup
//				.set(EmojiExtension.ROOT_IMAGE_PATH, emojiInstallDirectory())
                .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
                .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY);
        return mdToHtml(md, options);
    }

    private static String mdToHtml(String md, MutableDataSet options) {
        // uncomment to convert soft-breaks to hard breaks
//		options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Document document = parser.parse(md);
        return renderer.render(document);
    }
    static class CodePreLineNumbersAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
            // 定位到<pre>标签元素进行修改
            if (node instanceof FencedCodeBlock && part == AttributablePart.NODE) {
                attributes.addValue("class", "line-numbers markdown-body");
            }
            if (node instanceof Heading && part == AttributablePart.NODE) {
                attributes.addValue("class", "fw-bolder f-header mt-3");
                attributes.addValue("style",".f-header {\n" +
                        "  font-family: 'Arial', '黑体', 'Yu Gothic', sans-serif;\n" +
                        "}");
            }
            if (node instanceof Paragraph && part == AttributablePart.NODE) {
                attributes.addValue("class", "lh-base mt-3 fs-5");
                attributes.addValue("style","*p_replaced* color:#333333;");
//                attributes.addValue("style","color:#333333;");
            }
//            if (node instanceof Image && part == AttributablePart.NODE) {
//                attributes.addValue("class", "mt-3 w-75");
//               attributes.addValue("src","data:image/png;base64, *image_replaced*");
//            }
//            if (node instanceof ListItem && part == AttributablePart.NODE) {
//                attributes.addValue("class", "list-group-item fs-5");
//                attributes.addValue("style","color:#666666;");
//            }
            if (node instanceof ListBlock && part == AttributablePart.NODE) {
                attributes.addValue("class", "list-group rounded-0");
                attributes.addValue("style","color:#333333; *li_replaced*");
            }
//            if (node instanceof BlockQuote && part == AttributablePart.NODE) {
//                attributes.addValue("class", "markdown-body");
//            }
//            if (node instanceof CodeBlock && part == AttributablePart.NODE) {
//                attributes.addValue("class", "markdown-body");
//            }
//            if (node instanceof TableCell && part == AttributablePart.NODE) {
//                attributes.addValue("class", "markdown-body");
//            }
//            if (node instanceof TableRow && part == AttributablePart.NODE) {
//                attributes.addValue("class", "markdown-body");
//            }
//            if (node instanceof ListItem && part == AttributablePart.NODE) {
//                attributes.addValue("class", "markdown-body");
//                attributes.addValue("style","color:#666666;");
//            }
        }
        static AttributeProviderFactory Factory() {
            return new IndependentAttributeProviderFactory() {
                @NotNull
                @Override
                public AttributeProvider apply(@NotNull LinkResolverContext context) {
                    return new CodePreLineNumbersAttributeProvider();
                }
            };
        }
    }

    static class CodePreLineNumbersExtension implements HtmlRenderer.HtmlRendererExtension {
        @Override
        public void rendererOptions(@NotNull MutableDataHolder options) {
            // add any configuration settings to options you want to apply to everything, here
        }
        @Override
        public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
            htmlRendererBuilder.attributeProviderFactory(CodePreLineNumbersAttributeProvider.Factory());
        }
        static CodePreLineNumbersExtension create() {
            return new CodePreLineNumbersExtension();
        }
    }



}

