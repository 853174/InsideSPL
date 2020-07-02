package com.onekin.insideSpl.utils;

public class Pair<A, B> {

	private A fst;
	private B snd;

	public Pair(A fst, B snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public A getFst() {
		return fst;
	}

	public void setFst(A fst) {
		this.fst = fst;
	}

	public B getSnd() {
		return snd;
	}

	public void setSnd(B snd) {
		this.snd = snd;
	}

}
