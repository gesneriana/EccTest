package com.asuna.ecctest;

public class Pubkey {
	/**
	 * 公钥字节数组
	 */
	private byte[] qx, qy, gx, gy, a, p;

	public void setQx(byte[] qx) {
		byte[] temp=new byte[qx.length+1];
		for (int i=0;i<qx.length;i++){
			temp[i]=qx[i];
		}
		temp[qx.length]=0;
		this.qx = temp;
	}

	public void setQy(byte[] qy) {
		byte[] temp=new byte[qy.length+1];
		for (int i=0;i<qy.length;i++){
			temp[i]=qy[i];
		}
		temp[qy.length]=0;
		this.qy = temp;
	}

	public void setGx(byte[] gx) {
		byte[] temp=new byte[gx.length+1];
		for (int i=0;i<gx.length;i++){
			temp[i]=gx[i];
		}
		temp[gx.length]=0;
		this.gx = temp;
	}

	public void setGy(byte[] gy) {
		byte[] temp=new byte[gy.length+1];
		for (int i=0;i<gy.length;i++){
			temp[i]=gy[i];
		}
		temp[gy.length]=0;
		this.gy = temp;
	}

	public void setA(byte[] a) {
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
	 * 公钥x坐标
	 */
	public byte[] getQx() {
		return qx;
	}

	/**
	 * 公钥y坐标
	 */
	public byte[] getQy() {
		return qy;
	}

	/**
	 * 曲线G点x坐标
	 */
	public byte[] getGx() {
		return gx;
	}

	/**
	 * 曲线G点y坐标
	 */
	public byte[] getGy() {
		return gy;
	}

	/**
	 * 曲线参数a
	 */
	public byte[] getA() {
		return a;
	}

	/**
	 * 有限域Fp
	 */
	public byte[] getP() {
		return p;
	}

	/**
	 * 将当前的公钥对象转换为字符串的形式
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(new String(qx) + "-");
		sb.append(new String(qy) + "-");
		sb.append(new String(gx) + "-");
		sb.append(new String(gy) + "-");
		sb.append(new String(a) + "-");
		sb.append(new String(p) + "|");

		return sb.toString();
	}

}
