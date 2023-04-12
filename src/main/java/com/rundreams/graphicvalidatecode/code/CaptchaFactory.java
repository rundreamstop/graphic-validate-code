package com.rundreams.graphicvalidatecode.code;

import com.github.bingoohuang.patchca.color.RandomColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.word.RandomWordFactory;

/**
 * rundreams.blog.csdn.net
 *
 * @Time 2023/4/12
 * @Author Zhang Zihao
 * @description:
 */
public class CaptchaFactory {

    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

    static {
        cs.setColorFactory(new RandomColorFactory());
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("1234567890");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }

    public static ConfigurableCaptchaService getInstance() {
        return cs;
    }
}
