package edu.cmu.se355.recitation;

public class TestPrintDefs {
	public void main(String[] args) {
		int x = 3;
		int y = x + 4;
		int z = (y - x) * 2;
		int w = 0;

		for (int i = 0; i < 10; i++) {
			w = w + 1;
		}

		if (x > 4) {
			y = 4;
		} else {
			x = 4;
		}

		System.out.println(x + y + z + w);
	}
}
