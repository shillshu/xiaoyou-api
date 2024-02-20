package cn.sibetech.core.util.word;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordRenderPolicy {

    public enum RenderEnum{
        TABLE, HTML, PARAGRAPH
    }

    private String key;

    private RenderEnum renderEnum;

    public WordRenderPolicy(String key, RenderEnum renderEnum) {
        this.key = key;
        this.renderEnum = renderEnum;
    }
}
