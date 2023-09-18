package ind.xyz.mywebsite.util.md;


import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.MutableAttributes;
import org.jetbrains.annotations.NotNull;

public class CodePreLineNumbersAttributeProvider implements AttributeProvider {
    @Override
    public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
        // 定位到<pre>标签元素进行修改
        if (node instanceof FencedCodeBlock && part == AttributablePart.NODE) {
            attributes.addValue("class", "line-numbers");
        }
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
