#pragma once

class Tests
{
	private:
		// tests for the Dog class
		static void testDog();

		// tests for the DynamicVector class
		static void testDynamicVector();

		// tests for the Repository class
		static void testRepository();

		// tests for the Service class
		static void testService();

	public:
		// run all the available tests
		static void testAll();
};