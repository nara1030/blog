package myblog.blog.article.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class NewArticleForm {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String toc;
    @NotBlank
    private Long memberId;

    private String thumbnailUrl;

    private String category;
    private String tags;


}