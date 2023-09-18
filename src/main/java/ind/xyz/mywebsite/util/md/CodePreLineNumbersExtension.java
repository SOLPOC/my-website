package ind.xyz.mywebsite.util.md;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import org.jetbrains.annotations.NotNull;

public class CodePreLineNumbersExtension implements HtmlRenderer.HtmlRendererExtension {
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
