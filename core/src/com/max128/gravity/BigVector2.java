package com.max128.gravity;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigVector2 {
	BigDecimal x;
	BigDecimal y;
	private MathContext mc;
	
	public BigVector2(float x, float y, MathContext mc) {
		this.mc = mc;
		this.x = new BigDecimal(x);
		this.y = new BigDecimal(y);
	}
	
	public BigVector2(String x, String y, MathContext mc) {
		this.mc = mc;
		this.x = new BigDecimal(x);
		this.y = new BigDecimal(y);
	}
	
	public BigVector2(BigDecimal x, BigDecimal y, MathContext mc) {
		this.mc = mc;
		this.x = x;
		this.y = y;
	}
	
	public BigVector2 cpy() {
		return new BigVector2(x,y,mc);
	}
	
	public BigVector2 scl(BigDecimal s) {
		x.multiply(s);
		y.multiply(s);
		return this;
	}
	
	public BigVector2 scl(float s) {
		BigDecimal d = new BigDecimal(s);
		x.multiply(d);
		y.multiply(d);
		return this;
	}
	
	public BigVector2 add(BigVector2 other) {
		x = x.add(other.x);
		y = y.add(other.y);
		return this;
	}
	
	public BigVector2 sub(BigVector2 other) {
		x = x.subtract(other.x);
		y = y.subtract(other.y);
		return this;
	}
	
	public BigDecimal len2() {
		return x.pow(2).add(y.pow(2));
	}
	
	public BigDecimal len() {
		return len2().sqrt(mc);
	}
	
	public BigVector2 nor() {
		BigDecimal l = len();
		x = x.divide(l,mc);
		y = y.divide(l,mc);
		return this;
	}
	
	public float dst(float x, float y) {
		BigVector2 other = new BigVector2(x,y,mc);
		return cpy().sub(other).len().floatValue();
	}
}
