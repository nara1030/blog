package myblog.blog.article.application;

import lombok.RequiredArgsConstructor;
import myblog.blog.article.application.port.incomming.ArticleUseCase;
import myblog.blog.article.application.port.incomming.RssUseCase;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myblog.blog.article.domain.Article;
import org.jdom2.*;
import org.jdom2.output.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static myblog.blog.shared.utils.MarkdownUtils.*;

/*
        - rss 서비스 로직
          1. jdom2 사용하여 xml파일 작성
          2. 모든 게시물 item 변환후 xml에 삽입
          3. 발행
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RssService implements RssUseCase {

    static final String ITEM_ROOT = "https://www.jiniaslog.co.kr/article/view?articleId=";

    private final ArticleUseCase articleUseCase;

    @Override
    @Cacheable(value = "seoCaching", key = "0")
    public String getRssFeed() {
        List<Article> articles = articleUseCase.getTotalArticle();
        Document doc = makeRssFeedDocumentFrom(articles);
        XMLOutputter xmlOutputter = getXmlOutputter();
        return xmlOutputter.outputString(doc);
    }

    private Document makeRssFeedDocumentFrom(List<Article> articles) {
        Document doc = new Document();
        Element rss = buildRssRoot();
        doc.setRootElement(rss);
        Element channel = buildChannel();
        rss.addContent(channel);
        addArticleItemsToChannel(articles, channel, new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH));
        return doc;
    }

    private Element buildRssRoot() {
        Element rss = new Element("rss");
        rss.setAttribute(new Attribute("version", "2.0"));
        return rss;
    }

    private Element buildChannel() {
        Element channel = new Element("channel");
        channel.addContent(new Element("title").addContent(new CDATA("Jinia's LOG")));
        channel.addContent(new Element("link").setText("https://www.jiniaslog.co.kr"));
        channel.addContent(new Element("description").addContent(new CDATA("비전공 출신, 개발자 지망생의 공부 내용을 기록하는 블로그입니다.")));
        return channel;
    }

    private void addArticleItemsToChannel(List<Article> articles, Element channel, SimpleDateFormat dateFormat) {
        for (Article article : articles) {
            Element item = createItemTo(article, dateFormat);
            channel.addContent(item);
        }
    }

    private Element createItemTo(Article article, SimpleDateFormat dateFormat) {
        Element item = new Element("item");
        item
                .addContent(new Element("title").addContent(new CDATA(article.getTitle())))
                .addContent(new Element("link").setText(ITEM_ROOT + article.getId()))
                .addContent(new Element("description").addContent(new CDATA(getHtmlRenderer().render(getParser().parse(article.getContent())))))
                .addContent(new Element("pubDate").setText(dateFormat.format(Timestamp.valueOf(article.getCreatedDate()))))
                .addContent(new Element("guid").setText(ITEM_ROOT + article.getId()));
        return item;
    }

    private XMLOutputter getXmlOutputter() {
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setLineSeparator("\r\n");
        return new XMLOutputter(format);
    }
}