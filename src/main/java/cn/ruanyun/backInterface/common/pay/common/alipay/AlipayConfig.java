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
	public static final String APP_ID = "2021001156627358";

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static final String MERCHANT_PRIVATE_KEY= "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCKFzftjMy0X1wr0IlgNtDgRpYUpw79qdAUcYtlCJ6Qrwy0I8CLre8LL6QOOEZIQ6TxFqxnH1oYoiAKCsXiZ6aVKCcdTL684thusOROd1DXvfilGTPInAfbNI7BDsjsT0IdnqPFhj77aB6xIF++FOHi/2KYMGLHWi/+ILmilX0Vmyu6AkJw9PuCGn00UM95d0tU0tVHv6PSH/UKedHOmbzgjgx4IrdlNro/8odg3ns2/IqKsmD0nu7Wf6PvOXO6KkKKBA7dX8Ja3X+2fzPqrGusKXVbbVcSPPb07FjjpAKfWsgVVUPmvHrp2DshO3JnpIyKJb2HIfs46fLy9Vqrj4k5AgMBAAECggEAUM9lm05aPPPRLJLM0dFCSv0DtvJMg4Vsj4oi2O1CXckS1hhWBWe0DAfFKc0k1AVB8RQjr/VY9DFsLnQ/eU7O3VqyCSNwO3lEzwIVmRWZ6abyaytkcnFc55rKL5BHZIK9XjgH/v+gu98iluUrIkF2LzDkscsfAyp0C3N7LvnsSUe41OfanA4HowBw6COigpJ+Tr7Ir2dY8MT+DjZvnjJyWLbcPgUX/EObMlbG+McZMa/bip0eSiQrTVaBw2e1V5PS6prH6Osp3ImOKvDK6vjkbXAOQf6iGLtNL2LVrjfKo/zfB+HdsKGe605CNnh9f70aF521uMxM181uPnCMTpVlAQKBgQDHAi2qPGNGZNmZrlVoymC5olEvw3n23ioruMl6VqRkzxu3k8GbqJMfkDhzYQ8et20DpVGC2Z6vlgg8+4eWKW9njVUsrc3/IR5whau3Rw/U0lxRc4zUvGlxHRqTzlKzDklHNrWu9AdGej9mpcIJkAJrERJwj5/qNayY1vpLP87iJQKBgQCxov/1anIR6q7d6ZiljDznjm1pZnmGWAuwMx0vfpDyv0F9qD3uVnXzZc7XjadMUBS5qbnhJ3rslBQw2Epsrq34J2sny3okJhKBb9hcTpLEhN2CaQTmkkriy24ywpB6NUbiIgIfBiZqf1DBp2eMI2QDpOsOBKGI6/7VYl69Z3gchQKBgDcNoThg8E2/BHuBQNt2cP+pqa/+8bSbjSr/Oa2AXoYMzWwhkgPPxLOMoDE6/oXO1HeXuk7qiP2mEa1dIAsod0s8S/3KLxyPOu6B7BkNrZGAiE14G9OWM8048etFvpcOMrNirSwzEfgrRXLOFDm0/xZDaMh+0wxlwNOfVSqQAlmJAoGAKJ3Vmr7ZEEGx9Cm6bnfvYR7O45+7v70YBxTY4og1Clwy1FGIv5tjYuDvd5f7HdbdtkoZp2LIl5mQOE7sTYdvXeNde/7pkaRm7NOvDAQnSOn8YBo1NadTmiHbs52LPXY7G++cvo1AcW9k/rKRWZiqAK9P06e5a75SSWYxtWsYKqUCgYAiEsuXX6D3Kj8czadxIytYwG01P/Iz10wlmXmDQK1m6VhGWABF9Dw/bJuzKVjEo7JHb7tQLLXKvkQMDLKO8SGYDh3LNDHoNoaBtrlkQQI5ByQg53j6sUca+vlYDONJUga2ZCjwRliYG8g17tCzjuQwKyH9XfqCSoSltHdcAjT2jg==";

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	public static final String ALIPAY_PUBLIC_KEY= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvB08MFXzM8pn1xmetQaTpm673ZDa1Q/C7X9pJUikMxjXX0yXFe6E3cC82WxaKq7IYCtHhC8RrM2hkbC0MszR+69hgYoUi1+FbJ5LZupeQLra1OlG8+gFoX4y6z2Ix+Kolvl5loxwnGHiZiduopwVh9CNiqAnTA+pKEv1dkrIrE6K/a/w9Jfsqj72OoQnwG6euKy+Ab9d7mL6/Cnw/+Ue2oMLhAv1Uqls/38KZPeV8kLk0UXenzp6tqombMVixl3T69oCnuO2Squmcc4pOVshG/3sw3NPWlP4/HNHwFpI+iIXbmfZDhQiU0Rqyc4WZorpIx4D97ysQOLWylJSljsoxQIDAQAB";

	// 阿里支付应用公钥
	public static final String ALIPAY_APPLICATION_OF_PUBLIC_KEY= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAihc37YzMtF9cK9CJYDbQ4EaWFKcO/anQFHGLZQiekK8MtCPAi63vCy+kDjhGSEOk8RasZx9aGKIgCgrF4memlSgnHUy+vOLYbrDkTndQ1734pRkzyJwH2zSOwQ7I7E9CHZ6jxYY++2gesSBfvhTh4v9imDBix1ov/iC5opV9FZsrugJCcPT7ghp9NFDPeXdLVNLVR7+j0h/1CnnRzpm84I4MeCK3ZTa6P/KHYN57NvyKirJg9J7u1n+j7zlzuipCigQO3V/CWt1/tn8z6qxrrCl1W21XEjz29OxY46QCn1rIFVVD5rx66dg7ITtyZ6SMiiW9hyH7OOny8vVaq4+JOQIDAQAB";

	// 服务器  异步通知
//	public static final String NOTIFY_URL= "http://localhost:8888/xboot/alipay/alipayNotifyNotice.action";
	public static final String NOTIFY_URL= "http://121.196.28.116:8083/ruanyun/order/aliPayNotify";
//	public static final String NOTIFY_URL= "http://vpd8ra.natappfree.cc/ruanyun/order/aliPayNotify";

	// 页面跳转  同步通知
	public static final String RETURN_URL= "http://localhost:8888/xboot/alipay/alipayReturnNotice.action";

	// 签名方式
	public static final String SIGN_TYPE= "RSA2";

	//格式
	public static final String FORMAT= "json";

	// 字符编码格式
	public static final String CHARSET= "utf-8";

	//	支付宝网关  注意：沙箱测试环境，正式环境为：https://openapi.alipay.com/gateway.do  测试沙箱:https://openapi.alipaydev.com/gateway.do
	public static final String GATEWAY_URL= "https://openapi.alipay.com/gateway.do";

	public static final String BODY="梵莎夫订单支付";

	public static final String SUBJECT="梵莎夫订单";


}
