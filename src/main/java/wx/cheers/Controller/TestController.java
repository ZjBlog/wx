package wx.cheers.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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



    @ResponseBody
    @RequestMapping("/test2")
    public String test2(HttpServletRequest request) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set("zj".getBytes(),"张123".getBytes());
                return null;
            }
        });
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/test3")
    public String test3(HttpServletRequest request) {
        redisTemplate.opsForValue().set("zj1","张123");
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/test4")
    public String test4(HttpServletRequest request) {

        return (String)redisTemplate.opsForValue().get("zj1");
    }

    @ResponseBody
    @RequestMapping("/test5")
    public String test5() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set("z".getBytes(),"zj123".getBytes());
                return null;
            }
        });
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/test6")
    public String test6() {

        return (String)redisTemplate.opsForValue().get("z");
    }

    @ResponseBody
    @RequestMapping("/test7")
    public String test7() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set("z".getBytes(),"zj123张".getBytes());
                return null;
            }
        });
        return "ok";
    }
    @ResponseBody
    @RequestMapping("/test8")
    public String test8() {
       return (String)redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] bytes = redisConnection.get("z".getBytes());
                return new String(bytes);
            }
        });
    }
}
