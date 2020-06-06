package cn.ruanyun.backInterface.common.pay.common.alipay;

import org.springframework.stereotype.Component;

/**
 * @ClassTitle: AlipayConfig
 * @ProjectName fresh_project
 * @Description: 阿里支付参数
 * @Author Sangsang
 * @Date 2020/2/5
 * @Time 20:08
 */
@Component
public class AlipayConfig {
	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static final String APP_ID = "2021001161677428";

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static final String MERCHANT_PRIVATE_KEY= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdAu+9hA9uje3+ZXKZk+KPPRhuROEGSyYqkDD+XXp+7EbTONTuGV38rQ9HOMF1vZKbkRlT3kNJeDZEAhUoNrC/Zxyv8jG6CrZ2dkZJUk7RYlXwN7v1zLoYDFBLmA5KGeZBuQmfFPcVOgma38JEdtjp17pCSDuUzOq64qDznq5g6Tw5mk8iRgLtXLMNQVoskBEPyXecj2XfBVJFjdEphphWmj8QqvoDXQys/gZG6Fvl0OTgXx6pOkH54InQvsypz/rqcRhUAFm3wJKdcdtRtxcusQYYIBPNqgITo+2xEBqVYrF5LGDfnrBWuMg0WOwoHm128RJ61+75ByWyPG23uM0rAgMBAAECggEAJuP4T//q9p5PKekk5qd7wbEHYSDMgAuTK43FpOuwkMmjjWQRUdyUrCaNlz2WpbxMGR5qoJx4HDWbc2L0rJ5HT1GSCH68FHHc2iDXrgemIDT0Rmj3tJ1+JsC+DsteFrqtju/vUykVPnu+r07nRqANgHZ/2zUbFPNg3OOPrGhMlxIpHFcApqptm19gz0Wmbasz0RauPPNd/HTiykx6vOSsB0QNyNzbjsCiSGLfsyw/MMsYF3OGsyNxulvOakSnCXa+5YEN24/ioA9PL8J60CaIotRpN7rQN5M4kd4XHiKMMTrJzPpyRGwqT497d7uR1lNsMFOYM+7V4I/1jnlcM8dgkQKBgQD0v8v4FlXZMeK/iCOinwHO8+4HAtSbbAF/xcBLMwXGKOW/46ARBKfJwdDoIrA1fiCyyOZEQW/VIPESwm78wFr+ylt6JBrbP8TDLcC+m043nTYO9gImrIBWxQ7Xii6brE6ggYqOyKnFTNQYx4+h/3TXy5ETQ7R5oRmXhVEcyCgByQKBgQCkOqTdOdgoADAgnOgCCACiBPt643petYU+3sB3Abmol9aTYQsHmHdEgtpahoA/Da6zBXvxFqZoZz0TEhyBpFK95ECmRH8gMcZsffgh/FR0Ps+674Zv7DL0oz4xMciOw18uETk2S+gd5v8t0iRGjJA/1d0cSZ7pYBl9W4GQuYLxUwKBgQDWLLjLIpCBfjwUq4ezmT214pQbIoj2cS9mpqA9riqRW9yhbQn3moFR9v9fBGv6g6MNkOygMjsw4KDVb7w8BgAbEu22Fr5Buq3utq0Cn1l1JWfW88SyHyY3yD3StCNRKxmrL3EWvdaCx1abUJcKhkezR3pux0K5ZRbGF24Bp84KsQKBgDgm9QfPJ2FF7y63d3x6T/0VxwaVkNmhJxYAA76tEFXc1Lk/jQ3jy6PZOd+J7X+hpgH/9gjQvcUkx3Ul1ClfjgrvaEtxUCYp3rZpu9Wi+R1l/JDmJQlPsXyD9FfNtc1ab8jIis9N1Oeke8um9lBI72lkLl3lrjgNa6m5B+v8AhGRAoGAcsi2eIRirZT5jgkQi2id0pcyjOUlxgxDLSX1AmBwki0VaJE3oMKCqnkA5EJEocYSkkbwcggH2DPiVgMM3dn+/Bs2XWwjVpeCVYjc/m5laoLMdFOoGwwtr+6WmrssmYSY0401ybng4xXIfSfeALXVYvS0GwqeWoNfFoEncxxhXdE=";

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	public static final String ALIPAY_PUBLIC_KEY= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkvGBNrRA7ABOSfFkcBnpP+2XF2AcJ/ZPKhn6GU7Kt+rfn55wh86mBBZp/fzZNx1NsjXVrrInYFtCSr816tSrd94qTes/74eDH+rak5rRZD8ST0vZdpjpLsASPydvFTMrDAsQru9Jgpn7MNzqquJ6rAxHS7YE5XqQyO3PYIi3QOaLwcq0bM83Sb3KgpX9Z7EIGDC2MKz2r5pySRBwooCNIwCKhixDBLGBeYYXzpWoiZT+hKvdgu6E4HYEaDC8BtYiWhF8ZocPcr3zKumFZGSH/vBcxLbGbsIfyjxr1o1HxinmscLT5Ao4SLE5NMlWpwxct/kveyLQyNO/q/FtGmVr7wIDAQAB";

	// 阿里支付应用公钥
	public static final String ALIPAY_APPLICATION_OF_PUBLIC_KEY= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnQLvvYQPbo3t/mVymZPijz0YbkThBksmKpAw/l16fuxG0zjU7hld/K0PRzjBdb2Sm5EZU95DSXg2RAIVKDawv2ccr/Ixugq2dnZGSVJO0WJV8De79cy6GAxQS5gOShnmQbkJnxT3FToJmt/CRHbY6de6Qkg7lMzquuKg856uYOk8OZpPIkYC7VyzDUFaLJARD8l3nI9l3wVSRY3RKYaYVpo/EKr6A10MrP4GRuhb5dDk4F8eqTpB+eCJ0L7Mqc/66nEYVABZt8CSnXHbUbcXLrEGGCATzaoCE6PtsRAalWKxeSxg356wVrjINFjsKB5tdvESetfu+Qclsjxtt7jNKwIDAQAB";

	// 服务器  异步通知
	public static final String NOTIFY_URL= "http://47.99.50.236:8085/ruanyun/order/aliPayNotify";

	// 签名方式
	public static final String SIGN_TYPE= "RSA2";

	//格式
	public static final String FORMAT= "json";

	// 字符编码格式
	public static final String CHARSET= "utf-8";


	public static final String GATEWAY_URL= "https://openapi.alipay.com/gateway.do";

	public static final String BODY="梵莎夫订单支付";

	public static final String SUBJECT="梵莎夫订单";


}
