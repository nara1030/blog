package myblog.blog.article.dto;

import lombok.Getter;
import lombok.Setter;
import myblog.blog.article.domain.Article;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Setter
@Getter
public class ArticleForm {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String toc;

    private String thumbnailUrl;
    @NotBlank(message = "카테고리를 입력해주세요")
    private String category;
    @NotBlank(message = "태그를 하나이상 입력해주세요")
    private String tags;

}
