package com.onekin.insideSpl.utils;

public class Triplet<A,B,C> {

	private A fst;
	private B snd;
	private C trd;
	
	public Triplet(A fst, B snd, C trd) {
		this.fst = fst;
		this.snd = snd;
		this.trd = trd;
	}

	public A getFst() {
		return fst;
	}

	public B getSnd() {
		return snd;
	}

	public C getTrd() {
		return trd;
	}
	
	
	
}
