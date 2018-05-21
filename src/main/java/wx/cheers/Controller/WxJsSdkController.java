package wx.cheers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/***
 * 
 * 网页授权获取用户基本信息 里配置域名 不加 http:// 如：cheers.tunnel.qydev.com JS接口安全域名配置：
 * cheers.tunnel.qydev.com
 * 
 * @author Json
 * @Date 2018年4月13日
 *
 */
@Controller
public class WxJsSdkController {

    @Autowired
    private WxMpService wxService;

    @GetMapping("/home1")
    public String home(Model model, @RequestParam(value = "s", required = true) String s) {
        WxJsapiSignature signature = null;
        try {
            signature = wxService.createJsapiSignature("http://cheers.tunnel.qydev.com/home1?s=" + s);
        } catch (WxErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("username", "测试" + s);
        model.addAttribute("signature", signature);
        return "home";
    }

    // 第一步 用户同意获取code
    @GetMapping(value = "/home")
    public String home2(Model model) {
        String url = wxService.oauth2buildAuthorizationUrl("http://cheers.tunnel.qydev.com/home",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, "cheers");
        return "redirect:" + url;
    }

    @GetMapping(value = "/home", params = { "code" })
    public String home1(Model model, @RequestParam("code") String code, @RequestParam("state") String state) {

        System.out.println(code);
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            boolean accessToken = wxService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
            if (!accessToken) {
                wxMpOAuth2AccessToken = wxService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
            }
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            // 创建签名的时候，url必须和分享的链接一样
            WxJsapiSignature signature = wxService
                    .createJsapiSignature("http://cheers.tunnel.qydev.com/home?code=" + code + "&state=" + state);
            model.addAttribute("username", wxMpUser.getNickname());
            model.addAttribute("signature", signature);
        } catch (WxErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "home";
    }
}
