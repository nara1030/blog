package myblog.blog.member.application.port.incomming.response.userinfo;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;


public class NaverUserInfo implements Oauth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverUserInfo(OAuth2User oAuth2User) {
        this.attributes = oAuth2User.getAttribute("response");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return ProviderType.NAVER.getValue();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getUserName() {
        return (String) attributes.get("name") + "#"+ ((String) attributes.get("id")).substring(0,5);
    }

    @Override
    public String getPicture() {
        return (String) attributes.get("profile_image");
    }
}
