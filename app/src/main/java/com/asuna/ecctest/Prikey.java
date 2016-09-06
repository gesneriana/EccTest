package com.asuna.ecctest;

public class Prikey {

	private byte[] k, a, p;

	public void setK(byte[] k) {
		byte[] temp=new byte[k.length+1];
		for (int i=0;i<k.length;i++){
			temp[i]=k[i];
		}
		temp[k.length]=0;
		this.k = temp;
	}

	public void setA(byte[] a)
	{
		byte[] temp=new byte[a.length+1];
		for (int i=0;i<a.length;i++){
			temp[i]=a[i];
		}
		temp[a.length]=0;
		this.a = temp;
	}

	public void setP(byte[] p) {
		byte[] temp=new byte[p.length+1];
		for (int i=0;i<p.length;i++){
			temp[i]=p[i];
		}
		temp[p.length]=0;
		this.p = temp;
	}

	/**
	 * 私钥K
	 * 
	 * @return
	 */
	public byte[] getK() {
		return k;
	}

	/**
	 * 私钥参数A,实际上和公钥参数A是相等的
	 * 
	 * @return
	 */
	public byte[] getA() {
		return a;
	}

	/**
	 * 有限域Fp
	 * 
	 * @return
	 */
	public byte[] getP() {
		return p;
	}

	/**
	 * 将当前的公钥对象转换为字符串的形式
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(new String(k) + "-");
		sb.append(new String(a) + "-");
		sb.append(new String(p) + "|");

		return sb.toString();
	}
}
