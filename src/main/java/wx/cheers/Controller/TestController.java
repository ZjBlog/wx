package wx.cheers.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(HttpServletRequest request) {
        request.getSession().setAttribute("ddd", "dddd");
        return "test";
    }

    @RequestMapping("/test1")
    public String test1(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String aString = (String) session.getAttribute("ddd");
        System.out.println(aString);
        return "test";
    }
}
