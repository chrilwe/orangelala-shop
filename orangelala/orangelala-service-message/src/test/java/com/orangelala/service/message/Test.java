package com.orangelala.service.message;

public class Test {
	public static void main(String[] args) {
		Test test = new Test();
		int index = 0;
		String word = "hello";
		test.doIt(index, word);
	}
	
	public void doIt(int index,String word) {
		int length = word.length();
		if(index == length - 1) {
			System.out.println(word.substring(length - 1));
			return;
		}
		index++;
		doIt(index,word);
		String words = word.substring(index - 1, index);
		System.out.println(words);
	}
}
