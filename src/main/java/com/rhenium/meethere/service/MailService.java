package com.rhenium.meethere.service;

/**
 * @author HavenTong
 * @date 2019/12/7 5:27 下午
 */
public interface MailService {
    void sendHtmlMail(String to, String subject, String content);
}
