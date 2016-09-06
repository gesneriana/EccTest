package com.asuna.ecctest;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 调用 libecc4a.so 加密算法的接口
 * Created by 结城明日奈 on 2016-08-04.
 */
 public interface Ecc4a extends Library {

    Ecc4a instance = (Ecc4a) Native.loadLibrary("ecc4a", Ecc4a.class);

    /**
     * 重设密钥,仅用于Windows平台
     */
    // void rebuildkey();

    /**
     * 重新生成密钥对
     *
     * @param pubkey 大于255字节的公钥字符串
     * @param prikey 大于127字节的私钥字符串
     */
    void getkeypair(byte[] pubkey, byte[] prikey);

    /**
     * 根据公钥的6个参数 加密 src 明文字节流 enc 是加密后的密文字节流 返回值
     *
     * @param qx      公钥x
     * @param qy      公钥y
     * @param gx      曲线G点x坐标
     * @param gy      曲线G点y坐标
     * @param a       曲线参数a
     * @param p       有限域Fp
     * @param src     明文字节流
     * @param enc     密文字节流-返回值
     * @param encSize 密文字节流分配的内存空间大小 建议默认值 256字节
     */
    void enc2(byte[] qx, byte[] qy, byte[] gx, byte[] gy, byte[] a, byte[] p, byte[] src, byte[] enc, int encSize);


    /**
     * 根据私钥的3个参数 解密 enc密文字节流 dec是解密后的明文字节流 返回值
     *
     * @param k       私钥k
     * @param a       曲线参数a
     * @param p       有限域Fp
     * @param enc     密文字节流
     * @param encSize 密文字节流的长度 单位字节
     * @param dec     解密后的字节流-返回值
     */
    int dec2(byte[] k, byte[] a, byte[] p, byte[] enc, int encSize, byte[] dec);

   /**
    * 不同的处理器结构,可能返回值不同
    * 在x86处理器中  int a= (char) 255;   a的值实际为-1;
    * 在arm处理器中  int a= (char) 255;   a的值实际为255;
    * 我被这个区别给坑惨了,之前在x86的模拟器中调试运行成功,
    * 结构在 arm的真机中运行不成功,经过我不断调试才知道
    * @return
     */
   int getchar255();
}
