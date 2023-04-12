package com.rundreams.graphicvalidatecode.code;

import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.FilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * rundreams.blog.csdn.net
 *
 * @Time 2023/4/12
 * @Author Zhang Zihao
 * @description:
 */

@Slf4j
@Controller
@RequestMapping("/code")
public class CodeController {

    private static ConfigurableCaptchaService cs = CaptchaFactory.getInstance();

    private static List<FilterFactory> factories;

    static {
        factories = new ArrayList<FilterFactory>();
        factories.add(new CurvesRippleFilterFactory(cs.getColorFactory()));
        factories.add(new MarbleRippleFilterFactory());
        factories.add(new DoubleRippleFilterFactory());
        factories.add(new WobbleRippleFilterFactory());
        factories.add(new DiffuseRippleFilterFactory());
    }

    @GetMapping("/getCode")
    public void getImage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            cs.setFilterFactory(factories.get(new Random().nextInt(5)));
            setResponseHeaders(response);

            String token = EncoderHelper.getChallangeAndWriteImage(cs, "png",
                    response.getOutputStream());
            session.setAttribute("TEST_YZM", token);
            log.info("当前的SessionID = " + session.getId() + "，  验证码 = " + token);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping(value = "/validate")
    public String validate(HttpServletRequest request, HttpServletResponse response, String code) {

        log.info("用户输入的验证码为code={}", code);
        String yzm = (String) request.getSession().getAttribute("TEST_YZM");
        String prin = "";
        if (!yzm.equals(code)) {
            prin = "##########################################验证码输入错误############################################";
        } else {
            prin = "******************************************验证码输入成功********************************************";
        }
        return prin;
    }

    private void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }

}
