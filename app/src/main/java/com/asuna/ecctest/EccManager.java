package com.asuna.ecctest;

import android.util.Log;

/**
 * 写好一些对 libecc4a.so 方法的调用,让使用变得更简单 此 libecc4a.so 文件必须用visual studio 2015 编译
 * 不同版本的vs编译器使用的平台工具集不同,所以为了正常调用 libecc4a.so 必须安装visual studio 2015
 *
 * 保存密钥对的类
 * C++中 char为8位, 而java中char是16位 所以只能用byte[] 作为参数调用C++代码中的char[]
 * 
 * @author 结城明日奈
 *
 */
public abstract class EccManager {
	// 必须在每个使用 libecc4a.so 的类中使用下面这行代码初始化,否则可能会出现一些问题
	// libjnidispatch.so 是我在JNA框架中的 JNA\jna-master\dist\android-arm.jar
	// 右键单击此jar包,用好压打卡 你会发现jar包中有 libjnidispatch.so 解压缩这个jar包就能找到 libjnidispatch.so
	// JNA框架中有对应于不同平台的 libjnidispatch.so

	/**
	 * 要想使用 libecc4a.so 必须先加载JNA提供的 libjnidispatch.so
	 */
	static {
		System.loadLibrary("jnidispatch");
	}

	private static Prikey prikey;

	private static Pubkey pubkey;

	public  static int len=-1;

	/**
	 * 当前类的 私钥 对象
	 *
	 * @return
	 */
	public static Prikey getPrikey() {
		return prikey;
	}

	/**
	 * 当前类的公钥对象
	 *
	 * @return
	 */
	public static Pubkey getPubkey() {
		return pubkey;
	}

	/**
	 * 用于从客户端接收的公钥生成 Pubkey 对象
	 *
	 * @param pubkey
	 * @return
	 */
	public static Pubkey getPubkey(String pubkey) {
		Pubkey p = null;
		if (pubkey != null && pubkey.indexOf("|") > 0 && pubkey.indexOf("-") > 0) {
			String[] pubKeys = pubkey.substring(0, pubkey.lastIndexOf("|")).split("-");
			if (pubKeys.length == 6) {
				p = new Pubkey();
				p.setQx(pubKeys[0].getBytes());
				p.setQy(pubKeys[1].getBytes());
				p.setGx(pubKeys[2].getBytes());
				p.setGy(pubKeys[3].getBytes());
				p.setA(pubKeys[4].getBytes());
				p.setP(pubKeys[5].getBytes());
				return p;
			}
		}
		return null;
	}

	/**
	 * 用于从客户端接收的私钥字符串 生成Prikey 对象
	 *
	 * @param prikey
	 * @return
	 */
	public static Prikey getPrikey(String prikey) {
		Prikey p = null;
		if (prikey != null && prikey.indexOf("|") > 0 && prikey.indexOf("-") > 0) {
			String[] prikeys = prikey.substring(0, prikey.lastIndexOf("|")).split("-");
			if (prikeys.length == 3) {
				p = new Prikey();
				p.setK(prikeys[0].getBytes());
				p.setA(prikeys[1].getBytes());
				p.setP(prikeys[2].getBytes());
				return p;
			}
		}
		return null;
	}

	/**
	 * 为当前类 对象生成密钥对,每个当前类对象应用程序运行只能生成一次,多次生成密钥对耗费性能
	 */
	public static void buildKeyPair() {
		if (prikey == null || pubkey == null) {
			byte[] pub = new byte[255];
			byte[] pri = new byte[127];

			Ecc4a.instance.getkeypair(pub, pri);
			prikey = getPrikey(new String(pri));
			pubkey = getPubkey(new String(pub));
		}
	}


	/**
	 * 加密
	 *
	 * @param src 明文字节流,长度必须<=40字节, 不对称加密算法不能用于加密文件,非常耗时和性能 主要用于使用公钥加密网络传输的
	 *            对称加密密钥,发给对方之后,对方使用私钥解密
	 *            src 字节流长度区间 6-20 或者 26-40,否则后面的一部分会丢失数据,
	 *            可能是算法的一个bug,一般把对称密钥设置为 16字节或者32字节就行了
	 *            最好是随机生成16或者32个数字0-9 字母 a-z A-Z 的对称密钥,不要手动输入
	 * @return 密文字节流
	 */
	public static byte[] Enc2(byte[] src, Pubkey pubkey) {
		if (pubkey == null) {
			Log.d("Enc2", "没有公钥对象");
			return null;
		}
		if (src.length > 40) {
			Log.d("Enc2", "明文字节流过长");
			return null; // 不要加密超过40字节的数据,否则会非常卡
		}

		// 这应该是Ecc加密算法的一个小bug	src.length % 20 的值在 1-5 之间 明文字节流后面的部分字符会丢失
		// 一般对称密钥都是随机生成16或者32个字节就行了,这时候没有任何影响
		// 加密大文件也是  只要那个文件的长度(单位字节) % 20 的值在 1-5之间,必定会丢失后面的几个字节
		// 解决方法,我这种只是治标不治本 如下所示: 在后面补上 |-----
		// String s= "1234512345123451234512|-----"
		// 获取实际的值  String string= s.substring(0,s.lastindexof("|"));
		// 这时候变量 string 的值就是 "1234512345123451234512"
		// 最好是用代码随机生成 16或者32个  数字 0-9 字母 a-z A-Z 的对称密钥,而不是手动输入
		if (src.length % 20 > 0 && src.length % 20 < 6) {
			Log.d("Enc2", "字节流长度必须 在 6-20 或者 26-40之间");
			return null;
		}

		// #提示,我当初被这个坑跪了,尼玛Java把String转换为字节流之后,末尾没有结束符 \0
		// #然而在C/C++中却是以 \0 判断字符串长度,C#没有这个坑,直接转换为字节流就行
		byte[] b = new byte[41];
		for (int i = 0; i < src.length; i++) {
			b[i] = src[i]; // 后面都以 \0 填充
		}
		b[40] = 0; // 这里给加上 \0 然后C/C++代码里根据这个 \0 的索引值判断出 字节流长度
		// #

		byte[] enc = new byte[300];
		Ecc4a.instance.enc2(pubkey.getQx(), pubkey.getQy(), pubkey.getGx(), pubkey.getGy(), pubkey.getA(),
				pubkey.getP(), b, enc, enc.length);
		return enc;
	}

	/**
	 * 解密
	 *
	 * @param enc 密文字节流
	 * @return
	 */
	public static byte[] Dec2(byte[] enc, Prikey prikey) {
		if (prikey == null) {
			Log.d("Dec2", "没有私钥对象");
			System.out.println("没有私钥对象");
			return null;
		}
		byte[] dec = new byte[300];
		int a = Ecc4a.instance.dec2(prikey.getK(), prikey.getA(), prikey.getP(), enc, enc.length, dec);
		len=a;
		return dec;
	}

	/**
	 * 同样的代码.这个方法的返回值在不同的处理器架构中是不同的
	 * 希望所有程序员能够切记这一点
	 * @return
     */
	public static int getchar255(){
		return Ecc4a.instance.getchar255();
	}
}
