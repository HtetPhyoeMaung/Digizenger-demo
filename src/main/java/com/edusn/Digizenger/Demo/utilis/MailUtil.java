package com.edusn.Digizenger.Demo.utilis;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;

    public void sendOtpEmail(String fullName,String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        LocalDate today = LocalDate.now();




        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<div")
                .append(" style=\"")
                .append("max-width: 680px;")
                .append("margin: 0 auto;")
                .append("padding: 45px 30px 60px;")
                .append("background: #f4f7ff;")
                .append("background-image: url(https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661497957196_595865/email-template-background-banner);")
                .append("background-repeat: no-repeat;")
                .append("background-size: 800px 452px;")
                .append("background-position: top center;")
                .append("font-size: 14px;")
                .append("color: #434343;")
                .append("\"")
                .append(">")
                .append("<header>")
                .append("<table style=\"width: 100%;\">")
                .append("<tbody>")
                .append("<tr style=\"height: 0;\">")
                .append("<td>")
                .append("<img")
                .append(" style=\"max-width: 20%; height: auto;\"") // Make the image responsive
                .append(" alt=\"\"")
                .append(" src=\"cid:logoImage\"") // Use cid to reference the embedded image
                .append(" />")
                .append("</td>")
                .append("<td style=\"text-align: right;\">")
                .append("<span")
                .append(" style=\"font-size: 16px; line-height: 30px; color: #ffffff;\"")
                .append(">")
                .append(today)
                .append("</span>")
                .append("</td>")
                .append("</tr>")
                .append("</tbody>")
                .append("</table>")
                .append("</header>")
                .append("<main>")
                .append("<div")
                .append(" style=\"")
                .append("margin: 0;")
                .append("margin-top: 70px;")
                .append("padding: 92px 30px 115px;")
                .append("background: #ffffff;")
                .append("border-radius: 30px;")
                .append("text-align: center;")
                .append("\"")
                .append(">")
                .append("<div style=\"width: 100%; max-width: 489px; margin: 0 auto;\">")
                .append("<h1")
                .append(" style=\"")
                .append("margin: 0;")
                .append("font-size: 24px;")
                .append("font-weight: 500;")
                .append("color: #1f1f1f;")
                .append("\"")
                .append(">")
                .append("Your OTP")
                .append("</h1>")
                .append("<p")
                .append(" style=\"")
                .append("margin: 0;")
                .append("margin-top: 17px;")
                .append("font-size: 16px;")
                .append("font-weight: 500;")
                .append("\"")
                .append(">")
                .append("Hey ")
                .append(fullName)
                .append("</p>")
                .append("<p")
                .append(" style=\"")
                .append("margin: 0;")
                .append("margin-top: 17px;")
                .append("font-weight: 500;")
                .append("letter-spacing: 0.56px;")
                .append("\"")
                .append(">")
                .append("Thank you for choosing Digizenger Company. Use the following OTP")
                .append(" to complete the procedure to change your email address. OTP is")
                .append(" valid for")
                .append(" <span style=\"font-weight: 600; color: #1f1f1f;\">1 minute</span>.")
                .append(" Do not share this code with others, including Digizenger")
                .append(" employees.")
                .append("</p>")
                .append("<p")
                .append(" style=\"")
                .append("margin: 0;")
                .append("margin-top: 60px;")
                .append("font-size: 40px;")
                .append("font-weight: 600;")
                .append("letter-spacing: 25px;")
                .append("color: #ba3d4f;")
                .append("\"")
                .append(">")
                .append(otp)
                .append("</p>")
                .append("</div>")
                .append("</div>")
                .append("<p")
                .append(" style=\"")
                .append("max-width: 400px;")
                .append("margin: 0 auto;")
                .append("margin-top: 90px;")
                .append("text-align: center;")
                .append("font-weight: 500;")
                .append("color: #8c8c8c;")
                .append("\"")
                .append(">")
                .append("Need help? Ask at")
                .append(" <a")
                .append(" href=\"mailto:digizenger@gmail.com\"")
                .append(" style=\"color: #499fb6; text-decoration: none;\"")
                .append(">digizenger@gmail.com</a>")
                .append(" or visit our")
                .append(" <a")
                .append(" href=\"\"")
                .append(" target=\"_blank\"")
                .append(" style=\"color: #499fb6; text-decoration: none;\"")
                .append(">Help Center</a>")
                .append("</p>")
                .append("</main>")
                .append("<footer")
                .append(" style=\"")
                .append("width: 100%;")
                .append("max-width: 490px;")
                .append("margin: 20px auto 0;")
                .append("text-align: center;")
                .append("border-top: 1px solid #e6ebf1;")
                .append("\"")
                .append(">")
                .append("<p")
                .append(" style=\"")
                .append("margin: 0;")
                .append("margin-top: 40px;")
                .append("font-size: 16px;")
                .append("font-weight: 600;")
                .append("color: #434343;")
                .append("\"")
                .append(">")
                .append("Digizenger Company")
                .append("</p>")
                .append("<p style=\"margin: 0; margin-top: 8px; color: #434343;\">")
                .append("Address 540, City, State.")
                .append("</p>")
                .append("<div style=\"margin: 0; margin-top: 16px;\">")
                .append("<a href=\"\" target=\"_blank\" style=\"display: inline-block;\">")
                .append("<img")
                .append(" width=\"36px\"")
                .append(" alt=\"Facebook\"")
                .append(" src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661502815169_682499/email-template-icon-facebook\"")
                .append(" />")
                .append("</a>")
                .append("<a")
                .append(" href=\"\"")
                .append(" target=\"_blank\"")
                .append(" style=\"display: inline-block; margin-left: 8px;\"")
                .append(">")
                .append("<img")
                .append(" width=\"36px\"")
                .append(" alt=\"Instagram\"")
                .append(" src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661504218208_684135/email-template-icon-instagram\"")
                .append(" /></a>")
                .append("<a")
                .append(" href=\"\"")
                .append(" target=\"_blank\"")
                .append(" style=\"display: inline-block; margin-left: 8px;\"")
                .append(">")
                .append("<img")
                .append(" width=\"36px\"")
                .append(" alt=\"Twitter\"")
                .append(" src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503043040_372004/email-template-icon-twitter\"")
                .append(" /></a>")
                .append("<a")
                .append(" href=\"\"")
                .append(" target=\"_blank\"")
                .append(" style=\"display: inline-block; margin-left: 8px;\"")
                .append(">")
                .append("<img")
                .append(" width=\"36px\"")
                .append(" alt=\"Youtube\"")
                .append(" src=\"https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661503195931_210869/email-template-icon-youtube\"")
                .append(" /></a>")
                .append("</div>")
                .append("<p style=\"margin: 0; margin-top: 16px; color: #434343;\">")
                .append("Copyright © 2022 Company. All rights reserved.")
                .append("</p>")
                .append("</footer>")
                .append("</div>");

        mimeMessageHelper.setText(emailContent.toString(), true);

        // Add the image as an inline resource
        ClassPathResource logoImage = new ClassPathResource("logo.png");
        mimeMessageHelper.addInline("logoImage", logoImage);

        javaMailSender.send(mimeMessage);
    }

}