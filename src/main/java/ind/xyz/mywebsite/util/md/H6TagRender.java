//package ind.xyz.mywebsite.util.md;
//
//import com.vladsch.flexmark.ast.HtmlBlock;
//import com.vladsch.flexmark.html.HtmlWriter;
//import com.vladsch.flexmark.html.renderer.AttributablePart;
//import com.vladsch.flexmark.html.renderer.NodeRenderer;
//import com.vladsch.flexmark.html.renderer.NodeRendererContext;
//import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
//import com.vladsch.flexmark.html.renderer.RenderingContext;
//import com.vladsch.flexmark.html.renderer.TagRenderer;
//import com.vladsch.flexmark.util.ast.Node;
//import com.vladsch.flexmark.util.data.DataHolder;
//import com.vladsch.flexmark.util.html.Attribute;
//import com.vladsch.flexmark.util.html.Attributes;
//import com.vladsch.flexmark.util.html.NodeRendererContext;
//import com.vladsch.flexmark.util.html.RenderPurpose;
//import com.vladsch.flexmark.util.options.DataHolder;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class H6TagRender  implements NodeRenderer {
//
//    public CustomTagRenderer(DataHolder options) {
//    }
//
//    @Override
//    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
//        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
//        set.add(new NodeRenderingHandler<>(HtmlBlock.class, new NodeRenderer() {
//            @Override
//            public void render(Node node, NodeRendererContext context, HtmlWriter html) {
//                HtmlBlock htmlBlock = (HtmlBlock) node;
//                Attributes attributes = new Attributes();
//                attributes.addValue("class", "custom-class");
//                context.tag("div", attributes);
//                context.delegateRender();
//                html.tag("/div");
//            }
//        }));
//        return set;
//    }
//}
